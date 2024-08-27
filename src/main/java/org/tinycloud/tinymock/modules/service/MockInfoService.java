package org.tinycloud.tinymock.modules.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.tinycloud.tinymock.common.config.ApplicationConfig;
import org.tinycloud.tinymock.common.config.interceptor.TenantHolder;
import org.tinycloud.tinymock.common.constant.GlobalConstant;
import org.tinycloud.tinymock.common.enums.TenantErrorCode;
import org.tinycloud.tinymock.common.exception.TenantException;
import org.tinycloud.tinymock.common.model.PageModel;
import org.tinycloud.tinymock.common.utils.BeanConvertUtils;
import org.tinycloud.tinymock.common.utils.Zip4jUtils;
import org.tinycloud.tinymock.common.utils.cipher.SM4Utils;
import org.tinycloud.tinymock.modules.bean.dto.MockInfoAddDto;
import org.tinycloud.tinymock.modules.bean.dto.MockInfoEditDto;
import org.tinycloud.tinymock.modules.bean.dto.MockInfoQueryDto;
import org.tinycloud.tinymock.modules.bean.entity.TMockInfo;
import org.tinycloud.tinymock.modules.bean.entity.TMockInfoHistory;
import org.tinycloud.tinymock.modules.bean.enums.OperateTypeEnum;
import org.tinycloud.tinymock.modules.bean.vo.MockInfoVo;
import org.tinycloud.tinymock.modules.mapper.MockInfoHistoryMapper;
import org.tinycloud.tinymock.modules.mapper.MockInfoMapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-14 11:26
 */
@Slf4j
@Service
public class MockInfoService {

    public static String tempDirPath = System.getProperty("java.io.tmpdir");

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private MockInfoMapper mockInfoMapper;

    @Autowired
    private MockInfoHistoryMapper mockInfoHistoryMapper;

    public PageModel<MockInfoVo> query(MockInfoQueryDto queryParam) {
        PageModel<MockInfoVo> responsePage = new PageModel<>(queryParam.getPageNo(), queryParam.getPageSize());

        LambdaQueryWrapper<TMockInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TMockInfo::getDelFlag, GlobalConstant.NOT_DELETED);
        queryWrapper.eq(TMockInfo::getProjectId, queryParam.getProjectId());
        queryWrapper.like(StringUtils.hasLength(queryParam.getUrl()), TMockInfo::getUrl, queryParam.getUrl());
        queryWrapper.like(StringUtils.hasLength(queryParam.getMockName()), TMockInfo::getMockName, queryParam.getMockName());

        Page<TMockInfo> logPage = this.mockInfoMapper.selectPage(Page.of(queryParam.getPageNo(), queryParam.getPageSize()), queryWrapper);

        if (logPage != null && !CollectionUtils.isEmpty(logPage.getRecords())) {
            responsePage.setTotalPage(logPage.getPages());
            responsePage.setTotalCount(logPage.getTotal());
            responsePage.setRecords(logPage.getRecords().stream().map(x -> {
                return BeanConvertUtils.convertTo(x, MockInfoVo::new);
            }).collect(Collectors.toList()));
        }

        return responsePage;
    }

    public MockInfoVo detail(Long id) {
        TMockInfo mockInfo = this.mockInfoMapper.selectOne(
                Wrappers.<TMockInfo>lambdaQuery()
                        .eq(TMockInfo::getId, id)
                        .eq(TMockInfo::getDelFlag, GlobalConstant.NOT_DELETED));
        return BeanConvertUtils.convertTo(mockInfo, MockInfoVo::new);
    }

    @Transactional
    public Boolean delete(Long id) {
        TMockInfo mockInfo = this.mockInfoMapper.selectOne(Wrappers.<TMockInfo>lambdaQuery()
                .eq(TMockInfo::getDelFlag, GlobalConstant.NOT_DELETED)
                .eq(TMockInfo::getId, id));
        if (Objects.isNull(mockInfo)) {
            throw new TenantException(TenantErrorCode.TENANT_MOCKINFO_NOT_EXIST);
        }
        // 逻辑删除
        LambdaUpdateWrapper<TMockInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TMockInfo::getId, id);
        wrapper.set(TMockInfo::getDelFlag, GlobalConstant.DELETED);
        int rows = this.mockInfoMapper.update(null, wrapper);

        // 插入旧的数据到历史版本表中
        this.saveHistory(mockInfo, OperateTypeEnum.DELETE.getCode());

        return rows > 0;
    }


    public Boolean enable(Long id) {
        TMockInfo mockInfo = this.mockInfoMapper.selectOne(Wrappers.<TMockInfo>lambdaQuery()
                .eq(TMockInfo::getDelFlag, GlobalConstant.NOT_DELETED)
                .eq(TMockInfo::getId, id));
        if (Objects.isNull(mockInfo)) {
            throw new TenantException(TenantErrorCode.TENANT_MOCKINFO_NOT_EXIST);
        }
        if (mockInfo.getStatus().equals(GlobalConstant.ENABLED)) {
            throw new TenantException(TenantErrorCode.TENANT_MOCKINFO_IS_ENABLE);
        }
        LambdaUpdateWrapper<TMockInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TMockInfo::getId, id);
        wrapper.set(TMockInfo::getStatus, GlobalConstant.ENABLED);
        int rows = this.mockInfoMapper.update(null, wrapper);
        return rows > 0;
    }

    public Boolean disable(Long id) {
        TMockInfo mockInfo = this.mockInfoMapper.selectOne(Wrappers.<TMockInfo>lambdaQuery()
                .eq(TMockInfo::getDelFlag, GlobalConstant.NOT_DELETED)
                .eq(TMockInfo::getId, id));
        if (Objects.isNull(mockInfo)) {
            throw new TenantException(TenantErrorCode.TENANT_MOCKINFO_NOT_EXIST);
        }
        if (mockInfo.getStatus().equals(GlobalConstant.DISABLED)) {
            throw new TenantException(TenantErrorCode.TENANT_MOCKINFO_IS_DISABLE);
        }
        LambdaUpdateWrapper<TMockInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TMockInfo::getId, id);
        wrapper.set(TMockInfo::getStatus, GlobalConstant.DISABLED);
        int rows = this.mockInfoMapper.update(null, wrapper);
        return rows > 0;
    }

    @Transactional
    public Boolean add(MockInfoAddDto dto) {
        boolean exists = this.mockInfoMapper.exists(Wrappers.<TMockInfo>lambdaQuery()
                .eq(TMockInfo::getDelFlag, GlobalConstant.NOT_DELETED)
                .eq(TMockInfo::getProjectId, dto.getProjectId())
                .and(i -> i.eq(TMockInfo::getMockName, dto.getMockName())
                        .or()
                        .eq(TMockInfo::getUrl, dto.getUrl()))
        );
        if (exists) {
            throw new TenantException(TenantErrorCode.TENANT_MOCKINFO_NAME_OR_URL_ALREADY_EXIST);
        }
        TMockInfo tMockInfo = new TMockInfo();
        tMockInfo.setTenantId(TenantHolder.getTenantId());
        tMockInfo.setDelFlag(GlobalConstant.NOT_DELETED);
        tMockInfo.setStatus(GlobalConstant.ENABLED);
        tMockInfo.setRemark(dto.getRemark());
        tMockInfo.setMockName(dto.getMockName());
        tMockInfo.setMethod(dto.getMethod());
        tMockInfo.setDelay(dto.getDelay());
        tMockInfo.setUrl(dto.getUrl());
        tMockInfo.setJsonData(dto.getJsonData());
        tMockInfo.setProjectId(dto.getProjectId());
        tMockInfo.setMockjsFlag(dto.getMockjsFlag());
        tMockInfo.setHttpCode(dto.getHttpCode());
        int rows = this.mockInfoMapper.insert(tMockInfo);

        // 插入旧的数据到历史版本表中
        this.saveHistory(tMockInfo, OperateTypeEnum.ADD.getCode());
        return rows > 0;
    }

    @Transactional
    public Boolean edit(MockInfoEditDto dto) {
        TMockInfo mockInfo = this.mockInfoMapper.selectOne(Wrappers.<TMockInfo>lambdaQuery()
                .eq(TMockInfo::getDelFlag, GlobalConstant.NOT_DELETED)
                .eq(TMockInfo::getId, dto.getId()));
        if (Objects.isNull(mockInfo)) {
            throw new TenantException(TenantErrorCode.TENANT_MOCKINFO_NOT_EXIST);
        }
        boolean exists = this.mockInfoMapper.exists(Wrappers.<TMockInfo>lambdaQuery()
                .eq(TMockInfo::getDelFlag, GlobalConstant.NOT_DELETED)
                .eq(TMockInfo::getProjectId, mockInfo.getProjectId())
                .ne(TMockInfo::getId, mockInfo.getId())
                .and(i -> i.eq(TMockInfo::getMockName, dto.getMockName())
                        .or()
                        .eq(TMockInfo::getUrl, dto.getUrl()))
        );
        if (exists) {
            throw new TenantException(TenantErrorCode.TENANT_MOCKINFO_NAME_OR_URL_ALREADY_EXIST);
        }

        // 插入旧的数据到历史版本表中
        this.saveHistory(mockInfo, OperateTypeEnum.UPDATE.getCode());

        LambdaUpdateWrapper<TMockInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TMockInfo::getId, dto.getId());
        wrapper.set(StringUtils.hasLength(dto.getRemark()), TMockInfo::getRemark, dto.getRemark());
        wrapper.set(TMockInfo::getMockName, dto.getMockName());
        wrapper.set(TMockInfo::getMethod, dto.getMethod());
        wrapper.set(Objects.nonNull(dto.getDelay()), TMockInfo::getDelay, dto.getDelay());
        wrapper.set(Objects.nonNull(dto.getHttpCode()), TMockInfo::getHttpCode, dto.getHttpCode());
        wrapper.set(TMockInfo::getUrl, dto.getUrl());
        wrapper.set(TMockInfo::getJsonData, dto.getJsonData());
        wrapper.set(TMockInfo::getMockjsFlag, dto.getMockjsFlag());

        int rows = this.mockInfoMapper.update(null, wrapper);
        return rows > 0;
    }

    public void saveHistory(TMockInfo mockInfo, Integer operateType) {
        Integer version = this.mockInfoHistoryMapper.selectVersion(mockInfo.getId(), mockInfo.getProjectId());
        if (Objects.isNull(version)) {
            version = 1;
        } else {
            version = version + 1;
        }
        TMockInfoHistory tMockInfoHistory = new TMockInfoHistory();
        tMockInfoHistory.setMockId(mockInfo.getId());
        tMockInfoHistory.setTenantId(mockInfo.getTenantId());
        tMockInfoHistory.setOperatorId(TenantHolder.getTenantId());
        tMockInfoHistory.setOperateType(operateType);
        tMockInfoHistory.setProjectId(mockInfo.getProjectId());
        tMockInfoHistory.setVersion(version);
        tMockInfoHistory.setMockName(mockInfo.getMockName());
        tMockInfoHistory.setMethod(mockInfo.getMethod());
        tMockInfoHistory.setDelay(mockInfo.getDelay());
        tMockInfoHistory.setMockjsFlag(mockInfo.getMockjsFlag());
        tMockInfoHistory.setJsonData(mockInfo.getJsonData());
        tMockInfoHistory.setUrl(mockInfo.getUrl());
        tMockInfoHistory.setRemark(mockInfo.getRemark());
        tMockInfoHistory.setHttpCode(mockInfo.getHttpCode());
        this.mockInfoHistoryMapper.insert(tMockInfoHistory);
    }


    public void export(Long projectId, HttpServletResponse response) {
        List<TMockInfo> mockInfoList = this.mockInfoMapper.selectList(Wrappers.<TMockInfo>lambdaQuery()
                .eq(TMockInfo::getProjectId, projectId));
        String folderPath = UUID.randomUUID().toString().replace("-", "");
        // 根据folderPath创建文件夹
        Path folder = Paths.get(tempDirPath + folderPath);
        log.info("export folder: {}", folder);
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            Files.createDirectories(folder);
            for (TMockInfo mockInfo : mockInfoList) {
                String jsonData = mockInfo.getJsonData();
                String mockName = mockInfo.getMockName();
                Path filePath = folder.resolve(mockName + ".json");
                Files.write(filePath, jsonData.getBytes(StandardCharsets.UTF_8));
            }
            String sqlString = generateInsertTableSQL(dataSource, "t_mock_info", TenantHolder.getTenantId(), projectId);
            sqlString = SM4Utils.encrypt(applicationConfig.getProjectExportDek(), sqlString);
            Files.write(folder.resolve(projectId + ".back"), sqlString.getBytes(StandardCharsets.UTF_8));

            String zipFilePathString = folder.toString() + File.separator + projectId + ".zip";
            Zip4jUtils.zip(folder.toString(), zipFilePathString, null);
            // 获取压缩文件路径
            Path zipFilePath = Paths.get(zipFilePathString);
            // 设置文件下载方式：附件下载
            response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(projectId + ".zip", "UTF-8"));
            response.setContentLengthLong(Files.size(zipFilePath));
            // 下载文件
            Files.copy(zipFilePath, outputStream);
        } catch (IOException e) {
            log.error("export and zip file error: ", e);
        } finally {
            // 清空这个文件夹
            try {
                Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file); // 删除文件
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir); // 删除文件夹
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                log.error("export walkFileTree error: ", e);
            }
        }
    }

    @Transactional
    public boolean importZip(MultipartFile multipartFile, String fileName, Long projectId) {
        String folderPath = UUID.randomUUID().toString().replace("-", "");
        // 根据folderPath创建文件夹
        Path folder = Paths.get(tempDirPath + folderPath);
        if (!StringUtils.hasText(fileName)) {
            fileName = multipartFile.getOriginalFilename();
        }
        log.info("importZip folder: {}", folder.toString());
        log.info("importZip fileName: {}", fileName);
        try {
            Files.createDirectories(folder);
            Path zipFilePath = folder.resolve(fileName);
            // 文件保存到指定位置
            multipartFile.transferTo(zipFilePath);

            // 解压
            Zip4jUtils.unZip(zipFilePath.toString(), folder.toString(), null);
            // 加载备份文件
            Path projectBackFilePath = folder.resolve(projectId + ".back");
            if (!Files.exists(projectBackFilePath)) {
                throw new TenantException(TenantErrorCode.TENANT_MOCKINFO_BACK_FILE_NOT_BELONG_THIS_PROJECT);
            }
            // 读取文件内容
            String backContent = new String(Files.readAllBytes(projectBackFilePath), StandardCharsets.UTF_8);
            // 解密文件数据
            backContent = SM4Utils.decrypt(applicationConfig.getProjectExportDek(), backContent);

            this.mockInfoMapper.deleteByProjectAndTenant(projectId);
            this.mockInfoMapper.commonInsert(backContent);
        } catch (Exception e) {
            log.error("importZip error: ", e);
            throw new TenantException(TenantErrorCode.TENANT_MOCKINFO_IMPORT_PROJECT_ERROR);
        } finally {
            // 清空这个文件夹
            try {
                Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file); // 删除文件
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir); // 删除文件夹
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                log.error("export walkFileTree error: ", e);
            }
        }
        return true;
    }

    /**
     * 根据表里的数据，生成insert语句
     *
     * @param dataSource 数据源参数
     * @param tableName  表名
     * @return sql-insert语句
     */
    private static String generateInsertTableSQL(DataSource dataSource, String tableName, Long tenantId, Long projectId) {
        StringBuilder sql = new StringBuilder();
        if (tableName == null || tableName.isEmpty()) {
            return sql.toString();
        }
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE tenant_id = " + tenantId + " AND project_id = " + projectId);
            rs.last();
            int rowCount = rs.getRow();
            if (rowCount <= 0) {
                return sql.toString();
            }
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            StringBuilder firstHalfSql = new StringBuilder();
            firstHalfSql.append("INSERT INTO ").append(tableName).append("(");
            // 先构造insert语句的前半段，前半段是通用的
            for (int i = 0; i < columnCount; i++) {
                firstHalfSql.append(metaData.getColumnName(i + 1)).append(",");
            }
            // 删除掉最后一个无用字符
            firstHalfSql.deleteCharAt(firstHalfSql.length() - 1).append(") VALUES ");

            rs.beforeFirst();
            while (rs.next()) {
                // 然后循环拼接上insert语句的前半段
                sql.append(firstHalfSql);
                sql.append("(");
                for (int i = 0; i < columnCount; i++) {
                    int columnType = metaData.getColumnType(i + 1);
                    int columnIndex = i + 1;
                    if (Objects.isNull(rs.getObject(columnIndex))) {
                        sql.append("").append(rs.getObject(columnIndex)).append(",");
                    } else if (columnType == Types.INTEGER || columnType == Types.TINYINT || columnType == Types.BIT) {
                        sql.append(rs.getInt(columnIndex)).append(",");
                    } else if (columnType == Types.BIGINT) {
                        sql.append(rs.getLong(columnIndex)).append(",");
                    } else {
                        String val = rs.getString(columnIndex);
                        val = val.replace("'", "\\'");

                        sql.append("'").append(val).append("',");
                    }
                }
                sql.deleteCharAt(sql.length() - 1);
                sql.append(");\n");
            }
        } catch (Exception e) {
            log.error("generateInsertTableSQL Exception: ", e);
        }
        return sql.toString();
    }
}
