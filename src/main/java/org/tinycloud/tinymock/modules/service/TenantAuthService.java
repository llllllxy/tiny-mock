package org.tinycloud.tinymock.modules.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.tinycloud.tinymock.common.config.ApplicationConfig;
import org.tinycloud.tinymock.common.config.interceptor.TenantAuthCache;
import org.tinycloud.tinymock.common.config.interceptor.TenantHolder;
import org.tinycloud.tinymock.common.constant.GlobalConstant;
import org.tinycloud.tinymock.common.enums.TenantErrorCode;
import org.tinycloud.tinymock.common.exception.TenantException;
import org.tinycloud.tinymock.common.utils.*;
import org.tinycloud.tinymock.common.utils.captcha.GifCaptcha;
import org.tinycloud.tinymock.common.utils.cipher.SM2Utils;
import org.tinycloud.tinymock.common.utils.cipher.SM3Utils;
import org.tinycloud.tinymock.common.utils.web.IpAddressUtils;
import org.tinycloud.tinymock.common.utils.web.IpGetUtils;
import org.tinycloud.tinymock.common.utils.web.UserAgentUtils;
import org.tinycloud.tinymock.modules.bean.dto.TenantEditDto;
import org.tinycloud.tinymock.modules.bean.dto.TenantEditPasswordDto;
import org.tinycloud.tinymock.modules.bean.dto.TenantLoginDto;
import org.tinycloud.tinymock.modules.bean.dto.TenantRegisterDto;
import org.tinycloud.tinymock.modules.bean.entity.TTenant;
import org.tinycloud.tinymock.modules.bean.vo.MailConfigVo;
import org.tinycloud.tinymock.modules.bean.vo.TenantCaptchaCodeVo;
import org.tinycloud.tinymock.modules.bean.vo.TenantInfoVo;
import org.tinycloud.tinymock.modules.mapper.TenantMapper;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2023-12-05 15:01
 */
@Slf4j
@Service
public class TenantAuthService {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private MailConfigService mailConfigService;

    @Autowired
    private InviteesInfoService inviteesInfoService;

    @Autowired
    private UploadFileService uploadFileService;

    public TenantCaptchaCodeVo getCode() {
        // 保存验证码信息，生成验证key
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");

        String codeRedisKey = GlobalConstant.TENANT_CAPTCHA_CODE_REDIS_KEY + uuid;

        // 生成验证码图片，并返回base64编码
        GifCaptcha gifCaptcha = new GifCaptcha(120, 38, 5);
        String base64 = gifCaptcha.toBase64();
        // 生成4位随机数，作为验证码图片里的数
        String code = gifCaptcha.text().toLowerCase();

        Map<String, String> rsaKey = SM2Utils.genKeyPair();
        String publicKey = rsaKey.get("pubKey");
        String privateKey = rsaKey.get("priKey");

        // 将验证码和私钥，存入redis 60秒
        this.redisUtils.set(codeRedisKey, String.join("&", code, privateKey), 60, TimeUnit.SECONDS);

        // 验证码图片的bae64和rsa公钥返回给前端
        TenantCaptchaCodeVo vo = new TenantCaptchaCodeVo();
        vo.setImg(base64);
        vo.setUuid(uuid);
        vo.setPublicKey(publicKey);
        return vo;
    }


    public String login(TenantLoginDto dto, HttpServletRequest request) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        String captcha = dto.getCaptcha();
        String uuid = dto.getUuid();

        String codeRedisKey = GlobalConstant.TENANT_CAPTCHA_CODE_REDIS_KEY + uuid;
        String redisCache = (String) this.redisUtils.get(codeRedisKey);
        if (StrUtils.isEmpty(redisCache)) {
            throw new TenantException(TenantErrorCode.CAPTCHA_IS_MISMATCH);
        }
        String code = redisCache.split("&")[0];
        String privateKey = redisCache.split("&")[1];
        if (!captcha.equalsIgnoreCase(code)) {
            throw new TenantException(TenantErrorCode.CAPTCHA_IS_MISMATCH);
        }
        TTenant entity = this.tenantMapper.selectOne(
                Wrappers.<TTenant>lambdaQuery().eq(TTenant::getTenantAccount, username)
                        .eq(TTenant::getDelFlag, GlobalConstant.NOT_DELETED));
        if (Objects.isNull(entity)) {
            throw new TenantException(TenantErrorCode.TENANT_USERNAME_OR_PASSWORD_MISMATCH);
        }
        if (GlobalConstant.DISABLED.equals(entity.getStatus())) {
            throw new TenantException(TenantErrorCode.TENANT_IS_DISABLE);
        }
        String passwordDecrypt = SM2Utils.decrypt(privateKey, password);
        if (StrUtils.isEmpty(passwordDecrypt)) {
            throw new TenantException(TenantErrorCode.CAPTCHA_IS_MISMATCH);
        }
        String passwordDecryptHash = SM3Utils.hash(passwordDecrypt);
        if (!entity.getTenantPassword().equals(passwordDecryptHash)) {
            throw new TenantException(TenantErrorCode.TENANT_USERNAME_OR_PASSWORD_MISMATCH);
        }

        // 到此，密码已校验成功，开始校验最大并发登录数量，此处加分布式锁控制，防止同时登陆同一账号时出现问题，概率很小
        return GetRedissonLock.execute(() -> {
            List<String> tokenList = this.clearNotExistsToken(username);
            if (tokenList.size() >= applicationConfig.getMaximumConcurrentLogins()) {
                throw new TenantException(TenantErrorCode.MAXIMUM_LOGIN_CONCURRENCY_HAS_EXCEEDED_THE_LIMIT);
            }

            // 构建会话token进行返回
            Map<String, String> payload = new HashMap<>();
            String token = UUID.randomUUID().toString().trim().replaceAll("-", "");
            payload.put("token", token);
            String jwtToken = JwtUtils.sign(applicationConfig.getJwtSecret(), applicationConfig.getName(), payload);

            TenantAuthCache tenantAuthCache = BeanConvertUtils.convertTo(entity, TenantAuthCache::new);
            tenantAuthCache.setToken(token);
            UserAgent userAgent = UserAgentUtils.getUserAgent(request);
            tenantAuthCache.setBrowser(userAgent.getBrowser().getName());
            tenantAuthCache.setOs(userAgent.getOperatingSystem().getName());
            String ip = IpGetUtils.getIpAddr(request);
            tenantAuthCache.setIpAddress(ip);
            tenantAuthCache.setIpLocation(IpAddressUtils.getAddressByIP(ip));
            long currentTime = System.currentTimeMillis();
            tenantAuthCache.setLoginTime(currentTime);
            tenantAuthCache.setLoginExpireTime(currentTime + applicationConfig.getTenantAuthTimeout() * 1000);
            this.redisUtils.set(GlobalConstant.TENANT_TOKEN_REDIS_KEY + token, (tenantAuthCache), applicationConfig.getTenantAuthTimeout(), TimeUnit.SECONDS);

            // 记录此账号同时在线记录
            tokenList.add(token);
            this.redisUtils.set(GlobalConstant.TENANT_NAME_REDIS_KEY + username, (tokenList));

            return GlobalConstant.TOKEN_PREFIX + jwtToken;
        }, "LOGIN_LOCK_KEY_" + username, 3, 10);
    }


    /**
     * 登录时检查同时在线记录，剔除没有及时删除的会话信息记录
     *
     * @param tenantAccount 用户名
     */
    private List<String> clearNotExistsToken(String tenantAccount) {
        List<String> tokenList = (List) this.redisUtils.get(GlobalConstant.TENANT_NAME_REDIS_KEY + tenantAccount);
        if (!CollectionUtils.isEmpty(tokenList)) {
            tokenList.removeIf(token -> Boolean.FALSE.equals(this.redisUtils.hasKey(GlobalConstant.TENANT_TOKEN_REDIS_KEY + token)));
        }
        if (CollectionUtils.isEmpty(tokenList)) {
            this.redisUtils.del(GlobalConstant.TENANT_NAME_REDIS_KEY + tenantAccount);
        } else {
            this.redisUtils.set(GlobalConstant.TENANT_NAME_REDIS_KEY + tenantAccount, (tokenList));
        }

        if (CollectionUtils.isEmpty(tokenList)) {
            tokenList = new ArrayList<>();
        }
        return tokenList;
    }

    public Boolean logout(HttpServletRequest request) {
        String token = TenantHolder.getTenant().getToken();
        // 先清除token
        this.redisUtils.del(GlobalConstant.TENANT_TOKEN_REDIS_KEY + token);
        // 再清除登录记录
        List<String> tokenList = (List) this.redisUtils.get(GlobalConstant.TENANT_NAME_REDIS_KEY + TenantHolder.getTenantAccount());
        if (!CollectionUtils.isEmpty(tokenList)) {
            tokenList = tokenList.stream()
                    .filter(s -> !s.equals(token))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(tokenList)) {
                this.redisUtils.del(GlobalConstant.TENANT_NAME_REDIS_KEY + TenantHolder.getTenantAccount());
            } else {
                this.redisUtils.set(GlobalConstant.TENANT_NAME_REDIS_KEY + TenantHolder.getTenantAccount(), (tokenList));
            }
        }
        return true;
    }

    public TenantInfoVo getTenantInfo() {
        Long tenantId = TenantHolder.getTenantId();
        TTenant entity = this.tenantMapper.selectOne(
                Wrappers.<TTenant>lambdaQuery().eq(TTenant::getId, tenantId)
                        .eq(TTenant::getDelFlag, GlobalConstant.NOT_DELETED));
        TenantInfoVo tenantInfoVo = BeanConvertUtils.convertTo(entity, TenantInfoVo::new);
        if (Objects.nonNull(entity.getTenantAvatar())) {
            try {
                tenantInfoVo.setTenantAvatarAddress(uploadFileService.imageToBase64(entity.getTenantAvatar()));
            } catch (Exception e) {
                tenantInfoVo.setTenantAvatarAddress(null);
            }
        }
        return tenantInfoVo;
    }

    public Boolean editTenantInfo(TenantEditDto dto) {
        Long tenantId = TenantHolder.getTenantId();
        LambdaUpdateWrapper<TTenant> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TTenant::getId, tenantId);
        wrapper.set(TTenant::getTenantName, dto.getTenantName());
        wrapper.set(TTenant::getTenantPhone, dto.getTenantPhone());
        wrapper.set(TTenant::getTenantEmail, dto.getTenantEmail());
        wrapper.set(Objects.nonNull(dto.getTenantAvatar()), TTenant::getTenantAvatar, dto.getTenantAvatar());
        int rows = this.tenantMapper.update(wrapper);
        return rows > 0;
    }


    public Map<String, String> sendEmail(String receiveEmail) {
        // 保存验证码信息，生成验证key
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        String codeRedisKey = GlobalConstant.TENANT_EMAIL_CODE_REDIS_KEY + uuid;

        Map<String, String> rsaKey = SM2Utils.genKeyPair();
        String publicKey = rsaKey.get("pubKey");
        String privateKey = rsaKey.get("priKey");
        // 生成6位随机邮箱验证码
        String randomCode = StrUtils.randomStr(6);

        // 发送邮件
        MailConfigVo mailConfig = mailConfigService.detail();
        String emailTitle = "TINY-MOCK租户账户激活邮箱验证";
        String emailMsg = "<h2>您好，感谢您注册TINY-MOCK平台！</h2>"
                + "您的账户激活邮箱验证码为: " + randomCode + "，有效期十分钟"
                + "<br/><br/>"
                + "如果不是本人操作，请忽略。"
                + "<br/><br/>"
                + "TINY-MOCK - 简单好用的在线接口 MOCK 平台";
        EmailUtils.sendMsg(mailConfig.getEmailAccount(), mailConfig.getEmailPassword(), mailConfig.getSmtpAddress(), mailConfig.getSmtpPort(), mailConfig.getSslFlag() == 0,
                new String[]{receiveEmail}, emailTitle, emailMsg);

        // 将验证码和私钥，存入redis 600秒（十分钟）
        this.redisUtils.set(codeRedisKey, String.join("&", randomCode, privateKey), 600, TimeUnit.SECONDS);
        HashMap<String, String> result = new HashMap<>();
        result.put("publicKey", publicKey);
        result.put("uuid", uuid);
        return result;
    }

    @Transactional
    public Boolean register(TenantRegisterDto dto) {
        String invitationCode = dto.getInvitationCode();
        String tenantAccount = dto.getTenantAccount();
        String password = dto.getPassword();
        String tenantName = dto.getTenantName();
        String uuid = dto.getUuid();
        String tenantEmail = dto.getTenantEmail();
        String emailCode = dto.getEmailCode();

        String codeRedisKey = GlobalConstant.TENANT_EMAIL_CODE_REDIS_KEY + uuid;
        String redisCache = (String) this.redisUtils.get(codeRedisKey);
        if (StrUtils.isEmpty(redisCache)) {
            throw new TenantException(TenantErrorCode.EMAILCODE_IS_MISMATCH);
        }
        String code = redisCache.split("&")[0];
        String privateKey = redisCache.split("&")[1];
        if (!emailCode.equalsIgnoreCase(code)) {
            throw new TenantException(TenantErrorCode.EMAILCODE_IS_MISMATCH);
        }

        TTenant tTenant = this.tenantMapper.selectOne(
                Wrappers.<TTenant>lambdaQuery().eq(TTenant::getInvitationCode, invitationCode)
                        .eq(TTenant::getDelFlag, GlobalConstant.NOT_DELETED));
        if (Objects.isNull(tTenant)) {
            throw new TenantException(TenantErrorCode.INVITATIONCODE_IS_NOT_EXIST);
        }

        String passwordDecrypt = SM2Utils.decrypt(privateKey, password);
        if (StrUtils.isEmpty(passwordDecrypt)) {
            throw new TenantException(TenantErrorCode.EMAILCODE_IS_MISMATCH);
        }
        String passwordDecryptHash = SM3Utils.hash(passwordDecrypt);

        // 插入租户
        TTenant entity = new TTenant();
        entity.setDelFlag(GlobalConstant.NOT_DELETED);
        entity.setStatus(GlobalConstant.ENABLED);
        entity.setTenantPassword(passwordDecryptHash);
        entity.setTenantAccount(tenantAccount);
        entity.setTenantEmail(tenantEmail);
        entity.setTenantName(tenantName);
        this.tenantMapper.insert(entity);

        // 记录租户邀请码记录表
        this.inviteesInfoService.add(tTenant.getId(), invitationCode, entity.getId());

        return true;
    }

    public String refreshInvitationCode() {
        String newCode = StrUtils.randomStr(8);
        Long tenantId = TenantHolder.getTenantId();
        LambdaUpdateWrapper<TTenant> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TTenant::getId, tenantId);
        wrapper.set(TTenant::getInvitationCode, newCode);
        int rows = this.tenantMapper.update(wrapper);
        return newCode;
    }

    public Boolean editPassword(TenantEditPasswordDto dto) {
        String oldPassword = dto.getOldPassword();
        String newPassword = dto.getNewPassword();
        String againPassword = dto.getAgainPassword();
        String captcha = dto.getCaptcha();
        String uuid = dto.getUuid();

        String codeRedisKey = GlobalConstant.TENANT_CAPTCHA_CODE_REDIS_KEY + uuid;
        String redisCache = (String) this.redisUtils.get(codeRedisKey);
        if (StrUtils.isEmpty(redisCache)) {
            throw new TenantException(TenantErrorCode.CAPTCHA_IS_MISMATCH);
        }
        String code = redisCache.split("&")[0];
        String privateKey = redisCache.split("&")[1];
        if (!captcha.equalsIgnoreCase(code)) {
            throw new TenantException(TenantErrorCode.CAPTCHA_IS_MISMATCH);
        }
        TTenant entity = this.tenantMapper.selectOne(
                Wrappers.<TTenant>lambdaQuery().eq(TTenant::getId, TenantHolder.getTenantId())
                        .eq(TTenant::getDelFlag, GlobalConstant.NOT_DELETED));
        if (Objects.isNull(entity)) {
            throw new TenantException(TenantErrorCode.TENANT_IS_NOT_EXIST);
        }
        if (GlobalConstant.DISABLED.equals(entity.getStatus())) {
            throw new TenantException(TenantErrorCode.TENANT_IS_DISABLE);
        }

        String oldPasswordDecrypt = SM2Utils.decrypt(privateKey, oldPassword);
        String newPasswordDecrypt = SM2Utils.decrypt(privateKey, newPassword);
        String againPasswordDecrypt = SM2Utils.decrypt(privateKey, againPassword);

        if (StrUtils.isEmpty(oldPasswordDecrypt) || StrUtils.isEmpty(newPasswordDecrypt) || StrUtils.isEmpty(againPasswordDecrypt)) {
            throw new TenantException(TenantErrorCode.CAPTCHA_IS_MISMATCH);
        }
        if (!newPasswordDecrypt.equals(againPasswordDecrypt)) {
            throw new TenantException(TenantErrorCode.TENANT_PASSWORD_IS_ENTERED_INCONSISTENTLY);
        }
        String oldPasswordDecryptHash = SM3Utils.hash(oldPasswordDecrypt);
        String newPasswordDecryptHash = SM3Utils.hash(newPasswordDecrypt);

        if (!oldPasswordDecryptHash.equals(entity.getTenantPassword())) {
            throw new TenantException(TenantErrorCode.TENANT_OLD_PASSWORD_IS_WRONG);
        }
        LambdaUpdateWrapper<TTenant> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TTenant::getId, TenantHolder.getTenantId());
        wrapper.set(TTenant::getTenantPassword, newPasswordDecryptHash);
        int rows = this.tenantMapper.update(wrapper);
        return rows > 0;
    }
}