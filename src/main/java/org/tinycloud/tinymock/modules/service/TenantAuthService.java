package org.tinycloud.tinymock.modules.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.tinycloud.tinymock.common.config.ApplicationConfig;
import org.tinycloud.tinymock.common.config.interceptor.LoginTenant;
import org.tinycloud.tinymock.common.config.interceptor.TenantAuthUtil;
import org.tinycloud.tinymock.common.config.interceptor.TenantHolder;
import org.tinycloud.tinymock.common.constant.GlobalConstant;
import org.tinycloud.tinymock.common.enums.TenantErrorCode;
import org.tinycloud.tinymock.common.exception.TenantException;
import org.tinycloud.tinymock.common.utils.*;
import org.tinycloud.tinymock.common.utils.cipher.SM2Utils;
import org.tinycloud.tinymock.common.utils.cipher.SM3Utils;
import org.tinycloud.tinymock.common.utils.web.UserAgentUtils;
import org.tinycloud.tinymock.modules.bean.dto.TenantEditDto;
import org.tinycloud.tinymock.modules.bean.dto.TenantLoginDto;
import org.tinycloud.tinymock.modules.bean.dto.TenantRegisterDto;
import org.tinycloud.tinymock.modules.bean.entity.TTenant;
import org.tinycloud.tinymock.modules.bean.vo.MailConfigVo;
import org.tinycloud.tinymock.modules.bean.vo.TenantCaptchaCodeVo;
import org.tinycloud.tinymock.modules.bean.vo.TenantInfoVo;
import org.tinycloud.tinymock.modules.mapper.TenantMapper;


import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    private static final String expireTimeString = "2099-12-31 23:59:59";
    private static final String expireTimePattern = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private MailConfigService mailConfigService;

    public TenantCaptchaCodeVo getCode() {
        // 保存验证码信息，生成验证key
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");

        String codeRedisKey = GlobalConstant.TENANT_CAPTCHA_CODE_REDIS_KEY + uuid;

        // 生成验证码图片，并返回base64编码
        CaptchaCodeGen captchaCode = new CaptchaCodeGen(120, 38, 4, 10);
        String base64 = captchaCode.getBase64();
        // 生成4位随机数，作为验证码图片里的数
        String code = captchaCode.getCode();

        Map<String, String> rsaKey = SM2Utils.genKeyPair();
        String publicKey = rsaKey.get("pubKey");
        String privateKey = rsaKey.get("priKey");

        // 将验证码和私钥，存入redis 60秒
        this.stringRedisTemplate.opsForValue().set(codeRedisKey, String.join("&", code, privateKey), 60, TimeUnit.SECONDS);

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
        String redisCache = this.stringRedisTemplate.opsForValue().get(codeRedisKey);
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

        // 构建会话token进行返回
        String token = "tinyurl_" + UUID.randomUUID().toString().trim().replaceAll("-", "");
        LoginTenant loginTenant = BeanConvertUtils.convertTo(entity, LoginTenant::new);
        loginTenant.setToken(token);
        UserAgent userAgent = UserAgentUtils.getUserAgent(request);
        loginTenant.setBrowser(userAgent.getBrowser().getName());
        loginTenant.setOs(userAgent.getOperatingSystem().getName());
        String ip = IpGetUtils.getIpAddr(request);
        loginTenant.setIpAddress(ip);
        loginTenant.setIpLocation(IpAddressUtils.getAddressByIP(ip));
        long currentTime = System.currentTimeMillis();
        loginTenant.setLoginTime(currentTime);
        loginTenant.setLoginExpireTime(currentTime + applicationConfig.getTenantAuthTimeout() * 1000);
        this.stringRedisTemplate.opsForValue().set(GlobalConstant.TENANT_TOKEN_REDIS_KEY + token, JsonUtils.toJsonString(loginTenant), applicationConfig.getTenantAuthTimeout(), TimeUnit.SECONDS);

        return token;
    }

    public Boolean logout(HttpServletRequest request) {
        String token = TenantAuthUtil.getToken(request);
        this.stringRedisTemplate.delete(GlobalConstant.TENANT_TOKEN_REDIS_KEY + token);
        return true;
    }

    public TenantInfoVo getTenantInfo() {
        Long tenantId = TenantHolder.getTenantId();
        TTenant entity = this.tenantMapper.selectOne(
                Wrappers.<TTenant>lambdaQuery().eq(TTenant::getId, tenantId)
                        .eq(TTenant::getDelFlag, GlobalConstant.NOT_DELETED));
        TenantInfoVo tenantInfoVo = BeanConvertUtils.convertTo(entity, TenantInfoVo::new);
        return tenantInfoVo;
    }

    public Boolean editTenantInfo(TenantEditDto dto) {
        Long tenantId = TenantHolder.getTenantId();
        LambdaUpdateWrapper<TTenant> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TTenant::getId, tenantId);
        wrapper.set(TTenant::getTenantName, dto.getTenantName());
        wrapper.set(TTenant::getTenantPhone, dto.getTenantPhone());
        wrapper.set(TTenant::getTenantEmail, dto.getTenantEmail());
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
        String emailTitle = "TINY短链租户账户激活邮箱验证";
        String emailMsg = "<h2>您好，感谢您注册TINY短链平台！</h2>"
                + "您的账户激活邮箱验证码为: " + randomCode + "，有效期十分钟"
                + "<br/><br/>"
                + "如果不是本人操作，请忽略。"
                + "<br/><br/>"
                + "TINY短链 - 专业的短链服务商";
        EmailUtils.sendMsg(mailConfig.getEmailAccount(), mailConfig.getEmailPassword(), mailConfig.getSmtpAddress(), mailConfig.getSmtpPort(),
                new String[]{receiveEmail}, emailTitle, emailMsg);

        // 将验证码和私钥，存入redis 60秒
        this.stringRedisTemplate.opsForValue().set(codeRedisKey, String.join("&", randomCode, privateKey), 60, TimeUnit.SECONDS);

        return new HashMap<String, String>() {{
            put("publicKey", publicKey);
            put("uuid", uuid);
        }};
    }

    public Boolean register(TenantRegisterDto dto) {
        String tenantAccount = dto.getTenantAccount();
        String password = dto.getPassword();
        String tenantName = dto.getTenantName();
        String uuid = dto.getUuid();
        String tenantEmail = dto.getTenantEmail();
        String emailCode = dto.getEmailCode();

        String codeRedisKey = GlobalConstant.TENANT_EMAIL_CODE_REDIS_KEY + uuid;
        String redisCache = this.stringRedisTemplate.opsForValue().get(codeRedisKey);
        if (StrUtils.isEmpty(redisCache)) {
            throw new TenantException(TenantErrorCode.EMAILCODE_IS_MISMATCH);
        }
        String code = redisCache.split("&")[0];
        String privateKey = redisCache.split("&")[1];
        if (!emailCode.equalsIgnoreCase(code)) {
            throw new TenantException(TenantErrorCode.EMAILCODE_IS_MISMATCH);
        }
        String passwordDecrypt = SM2Utils.decrypt(privateKey, password);
        if (StrUtils.isEmpty(passwordDecrypt)) {
            throw new TenantException(TenantErrorCode.EMAILCODE_IS_MISMATCH);
        }
        String passwordDecryptHash = SM3Utils.hash(passwordDecrypt);

        TTenant entity = new TTenant();
        entity.setDelFlag(GlobalConstant.NOT_DELETED);
        entity.setStatus(GlobalConstant.ENABLED);
        entity.setTenantPassword(passwordDecryptHash);
        entity.setTenantAccount(tenantAccount);
        entity.setTenantEmail(tenantEmail);
        entity.setTenantName(tenantName);
        this.tenantMapper.insert(entity);
        return true;
    }
}