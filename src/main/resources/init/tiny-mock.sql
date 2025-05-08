/*
 Navicat MySQL Data Transfer

 Source Server         : 个人-本机-127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : localhost:3306
 Source Schema         : tiny-mock

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 10/04/2025 15:55:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_dict
-- ----------------------------
DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE `t_dict`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `dict_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典标识编码',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名称',
  `dict_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典码',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典值',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注描述信息',
  `order_num` int(4) NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(4) NOT NULL COMMENT '状态标志（0--启用1--禁用）',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标志（0--未删除1--已删除）',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `created_by` bigint(20) NOT NULL COMMENT '创建人-对应t_user.id',
  `updated_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人-对应t_user.id',
  `background` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '背景色',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统数据字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_dict
-- ----------------------------
INSERT INTO `t_dict` VALUES (1, 'mock_info_mockjs_flag', 'mock_info_mockjs_flag', '0', '是', NULL, 1, 0, 0, '2025-04-01 12:09:05', '2025-04-01 12:09:05', 1, NULL, 'blue');
INSERT INTO `t_dict` VALUES (2, 'mock_info_mockjs_flag', 'mock_info_mockjs_flag', '1', '否', NULL, 2, 0, 0, '2025-04-01 12:09:05', '2025-04-01 12:09:05', 1, NULL, 'orange');
INSERT INTO `t_dict` VALUES (3, 'mock_info_method', 'mock_info_method', 'GET', 'GET', NULL, 1, 0, 0, '2025-04-01 12:09:56', '2025-04-01 12:10:47', 1, NULL, 'orange');
INSERT INTO `t_dict` VALUES (4, 'mock_info_method', 'mock_info_method', 'POST', 'POST', NULL, 2, 0, 0, '2025-04-01 12:09:56', '2025-04-01 12:10:50', 1, NULL, 'cyan');
INSERT INTO `t_dict` VALUES (5, 'mock_info_method', 'mock_info_method', 'PUT', 'PUT', NULL, 3, 0, 0, '2025-04-01 12:09:56', '2025-04-01 12:10:56', 1, NULL, 'blue');
INSERT INTO `t_dict` VALUES (6, 'mock_info_method', 'mock_info_method', 'DELETE', 'DELETE', NULL, 4, 0, 0, '2025-04-01 12:09:56', '2025-04-01 12:10:58', 1, NULL, 'red');
INSERT INTO `t_dict` VALUES (7, 'operate_log_oper_result', 'operate_log_oper_result', '0', '成功', NULL, 1, 0, 0, '2025-04-01 12:09:56', '2025-04-07 16:44:03', 1, NULL, 'blue');
INSERT INTO `t_dict` VALUES (8, 'operate_log_oper_result', 'operate_log_oper_result', '1', '失败', NULL, 2, 0, 0, '2025-04-01 12:09:56', '2025-04-07 16:49:46', 1, NULL, 'red');
INSERT INTO `t_dict` VALUES (9, 'operate_log_business_type', '业务操作类型', '0', '其它', NULL, 0, 0, 0, '2025-04-01 12:09:56', '2025-04-07 16:49:54', 1, NULL, NULL);
INSERT INTO `t_dict` VALUES (10, 'operate_log_business_type', '业务操作类型', '1', '新增', NULL, 1, 0, 0, '2025-04-01 12:09:56', '2025-04-07 16:49:54', 1, NULL, NULL);
INSERT INTO `t_dict` VALUES (11, 'operate_log_business_type', '业务操作类型', '2', '修改', NULL, 2, 0, 0, '2025-04-01 12:09:56', '2025-04-07 16:49:54', 1, NULL, NULL);
INSERT INTO `t_dict` VALUES (12, 'operate_log_business_type', '业务操作类型', '3', '删除', NULL, 3, 0, 0, '2025-04-01 12:09:56', '2025-04-07 16:49:54', 1, NULL, NULL);
INSERT INTO `t_dict` VALUES (13, 'operate_log_business_type', '业务操作类型', '4', '授权', NULL, 4, 0, 0, '2025-04-01 12:09:56', '2025-04-07 16:49:54', 1, NULL, NULL);
INSERT INTO `t_dict` VALUES (14, 'operate_log_business_type', '业务操作类型', '5', '导出', NULL, 5, 0, 0, '2025-04-01 12:09:56', '2025-04-07 16:49:54', 1, NULL, NULL);
INSERT INTO `t_dict` VALUES (15, 'operate_log_business_type', '业务操作类型', '6', '导入', NULL, 6, 0, 0, '2025-04-01 12:09:56', '2025-04-07 16:49:54', 1, NULL, NULL);
INSERT INTO `t_dict` VALUES (16, 'operate_log_business_type', '业务操作类型', '7', '强退', NULL, 7, 0, 0, '2025-04-01 12:09:56', '2025-04-07 16:49:54', 1, NULL, NULL);
INSERT INTO `t_dict` VALUES (17, 'operate_log_business_type', '业务操作类型', '8', '生成代码', NULL, 8, 0, 0, '2025-04-01 12:09:56', '2025-04-07 16:49:54', 1, NULL, NULL);
INSERT INTO `t_dict` VALUES (18, 'operate_log_business_type', '业务操作类型', '9', '清空数据', NULL, 9, 0, 0, '2025-04-01 12:09:56', '2025-04-07 16:49:54', 1, NULL, NULL);
INSERT INTO `t_dict` VALUES (19, 'operate_log_business_type', '业务操作类型', '10', '启用', NULL, 10, 0, 0, '2025-04-01 12:09:56', '2025-04-07 16:49:54', 1, NULL, NULL);
INSERT INTO `t_dict` VALUES (20, 'operate_log_business_type', '业务操作类型', '11', '禁用', NULL, 11, 0, 0, '2025-04-01 12:09:56', '2025-04-07 16:49:54', 1, NULL, NULL);

-- ----------------------------
-- Table structure for t_feedback
-- ----------------------------
DROP TABLE IF EXISTS `t_feedback`;
CREATE TABLE `t_feedback`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `feed_type` tinyint(4) NOT NULL COMMENT '反馈类型，0建议，1问题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标志（0--未删除1--已删除）',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '反馈建议表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_feedback
-- ----------------------------
INSERT INTO `t_feedback` VALUES (2024079000000002192, 2212121, 0, '哈哈哈哈哈哈', '17862718963@email.com', 0, '2024-03-19 15:59:18', '2024-03-19 15:59:18');
INSERT INTO `t_feedback` VALUES (2024079000000003955, 2212121, 1, '哈哈哈哈哈哈11111', '17862718963@email.com', 0, '2024-03-19 15:59:37', '2024-03-19 15:59:37');

-- ----------------------------
-- Table structure for t_invitees_info
-- ----------------------------
DROP TABLE IF EXISTS `t_invitees_info`;
CREATE TABLE `t_invitees_info`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  `invitation_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邀请码',
  `invitees_tenant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '被邀请人',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标志（0--未删除1--已删除）',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `t_invitees_info_invitation_code_unique`(`invitation_code`) USING BTREE COMMENT '唯一索引，surl不允许重复'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_invitees_info
-- ----------------------------
INSERT INTO `t_invitees_info` VALUES (2024080000000001699, 2212121, 'AaNs8ROo', '2024080000000001698', 0, '2024-03-20 22:05:28', '2024-03-20 22:05:28', NULL);

-- ----------------------------
-- Table structure for t_mail_config
-- ----------------------------
DROP TABLE IF EXISTS `t_mail_config`;
CREATE TABLE `t_mail_config`  (
  `id` bigint(20) NOT NULL COMMENT '自增主键',
  `smtp_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'smtp服务器地址',
  `smtp_port` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'smtp端口',
  `email_account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱账号',
  `email_password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱密码',
  `receive_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '默认收件邮箱地址(多个用逗号隔开)',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标志（0--未删除1--已删除）',
  `ssl_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否开启ssl（0--开启--1关闭）',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注描述信息',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL COMMENT '创建人-对应t_user.id',
  `updated_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人-对应t_user.id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统邮箱配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_mock_access_log
-- ----------------------------
DROP TABLE IF EXISTS `t_mock_access_log`;
CREATE TABLE `t_mock_access_log`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户id',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `mock_id` bigint(20) NOT NULL COMMENT '接口id',
  `access_time` datetime(0) NOT NULL COMMENT '访问时间',
  `access_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问者-IP地址',
  `access_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问者-ip物理地址',
  `access_user_agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问者的user_agent',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_mock_access_log
-- ----------------------------
INSERT INTO `t_mock_access_log` VALUES (1766426839531925506, 1, 1729067620457848832, 1, '2024-03-09 19:32:32', '192.168.0.105', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022,\"browserVersion\":null}', '2024-03-09 19:32:31');
INSERT INTO `t_mock_access_log` VALUES (1766426862617374721, 1, 1729067620457848832, 1, '2024-03-09 19:32:37', '192.168.0.105', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022,\"browserVersion\":null}', '2024-03-09 19:32:37');
INSERT INTO `t_mock_access_log` VALUES (1766427705043333121, 1, 1729067620457848832, 1, '2024-03-09 19:35:58', '192.168.0.105', '本地局域网', '{\"operatingSystem\":\"WINDOWS_7\",\"browser\":\"CHROME54\",\"id\":34934588,\"browserVersion\":{\"version\":\"54.0.2816.0\",\"majorVersion\":\"54\",\"minorVersion\":\"0\"}}', '2024-03-09 19:35:58');
INSERT INTO `t_mock_access_log` VALUES (1766427767035146242, 1, 1729067620457848832, 1, '2024-03-09 19:36:13', '192.168.0.105', '本地局域网', '{\"operatingSystem\":\"WINDOWS_7\",\"browser\":\"CHROME54\",\"id\":34934588,\"browserVersion\":{\"version\":\"54.0.2816.0\",\"majorVersion\":\"54\",\"minorVersion\":\"0\"}}', '2024-03-09 19:36:12');
INSERT INTO `t_mock_access_log` VALUES (1766792734884302849, 1, 1729067620457848832, 1, '2024-03-10 19:46:28', '192.168.0.106', '本地局域网', '{\"operatingSystem\":\"WINDOWS_7\",\"browser\":\"CHROME54\",\"id\":34934588,\"browserVersion\":{\"version\":\"54.0.2816.0\",\"majorVersion\":\"54\",\"minorVersion\":\"0\"}}', '2024-03-10 19:46:28');
INSERT INTO `t_mock_access_log` VALUES (1766792760612163586, 1, 1729067620457848832, 1, '2024-03-10 19:46:34', '192.168.0.106', '本地局域网', '{\"operatingSystem\":\"WINDOWS_7\",\"browser\":\"CHROME54\",\"id\":34934588,\"browserVersion\":{\"version\":\"54.0.2816.0\",\"majorVersion\":\"54\",\"minorVersion\":\"0\"}}', '2024-03-10 19:46:34');
INSERT INTO `t_mock_access_log` VALUES (1766792783383040002, 1, 1729067620457848832, 1, '2024-03-10 19:46:40', '192.168.0.106', '本地局域网', '{\"operatingSystem\":\"WINDOWS_7\",\"browser\":\"CHROME54\",\"id\":34934588,\"browserVersion\":{\"version\":\"54.0.2816.0\",\"majorVersion\":\"54\",\"minorVersion\":\"0\"}}', '2024-03-10 19:46:39');
INSERT INTO `t_mock_access_log` VALUES (1768538639025094658, 2212121, 1729067620457848832, 1768538350125629441, '2024-03-15 15:24:04', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:24:04');
INSERT INTO `t_mock_access_log` VALUES (1768538645769535490, 2212121, 1729067620457848832, 1768538350125629441, '2024-03-15 15:24:06', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:24:05');
INSERT INTO `t_mock_access_log` VALUES (1768538681790218241, 2212121, 1729067620457848832, 1768538350125629441, '2024-03-15 15:24:14', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:24:14');
INSERT INTO `t_mock_access_log` VALUES (1768538691227402241, 2212121, 1729067620457848832, 1768538350125629441, '2024-03-15 15:24:17', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:24:16');
INSERT INTO `t_mock_access_log` VALUES (1768538697946677250, 2212121, 1729067620457848832, 1768538350125629441, '2024-03-15 15:24:18', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:24:18');
INSERT INTO `t_mock_access_log` VALUES (1768538727340359682, 2212121, 1729067620457848832, 1768538350125629441, '2024-03-15 15:24:25', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:24:25');
INSERT INTO `t_mock_access_log` VALUES (1768538741332557825, 2212121, 1729067620457848832, 1768538350125629441, '2024-03-15 15:24:28', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:24:28');
INSERT INTO `t_mock_access_log` VALUES (1768538819485024257, 2212121, 1729067620457848832, 1768538350125629441, '2024-03-15 15:24:47', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:24:47');
INSERT INTO `t_mock_access_log` VALUES (1768538825692594178, 2212121, 1729067620457848832, 1768538350125629441, '2024-03-15 15:24:49', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:24:48');
INSERT INTO `t_mock_access_log` VALUES (1768538832688693250, 2212121, 1729067620457848832, 1768538350125629441, '2024-03-15 15:24:50', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:24:50');
INSERT INTO `t_mock_access_log` VALUES (1768538868952645633, 2212121, 1729067620457848832, 1768538350125629441, '2024-03-15 15:24:59', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:24:58');
INSERT INTO `t_mock_access_log` VALUES (1768539786842521602, 2212121, 1729067620457848832, 1768538350125629441, '2024-03-15 15:28:38', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:28:37');
INSERT INTO `t_mock_access_log` VALUES (1768539870011375617, 2212121, 1729067620457848832, 1768538350125629441, '2024-03-15 15:28:58', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:28:57');
INSERT INTO `t_mock_access_log` VALUES (1768539954102976513, 2212121, 1729067620457848832, 1768538350125629441, '2024-03-15 15:29:18', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:29:17');
INSERT INTO `t_mock_access_log` VALUES (1768539957886238721, 2212121, 1729067620457848832, 1768538350125629441, '2024-03-15 15:29:18', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:29:18');
INSERT INTO `t_mock_access_log` VALUES (1768540180050132993, 2212121, 1729067620457848832, 1768538350125629441, '2024-03-15 15:30:11', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:30:11');
INSERT INTO `t_mock_access_log` VALUES (1768540183745314818, 2212121, 1729067620457848832, 1768538350125629441, '2024-03-15 15:30:12', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:30:12');
INSERT INTO `t_mock_access_log` VALUES (1768540188153528321, 2212121, 1729067620457848832, 1768538350125629441, '2024-03-15 15:30:13', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:30:13');
INSERT INTO `t_mock_access_log` VALUES (1768544406616719361, 2212121, 1729067620457848832, 1768544377218842626, '2024-03-15 15:46:59', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:46:59');
INSERT INTO `t_mock_access_log` VALUES (1768544412077703170, 2212121, 1729067620457848832, 1768544377218842626, '2024-03-15 15:47:00', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:47:00');
INSERT INTO `t_mock_access_log` VALUES (1768544416225869825, 2212121, 1729067620457848832, 1768544377218842626, '2024-03-15 15:47:01', '192.168.5.12', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-03-15 15:47:01');
INSERT INTO `t_mock_access_log` VALUES (1828676311978405890, 2212121, 1729067620457848832, 1, '2024-08-28 14:09:43', '192.168.5.69', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-08-28 14:09:42');
INSERT INTO `t_mock_access_log` VALUES (1828676442089910274, 2212121, 1729067620457848832, 1768538350125629441, '2024-08-28 14:10:14', '192.168.5.69', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-08-28 14:10:13');
INSERT INTO `t_mock_access_log` VALUES (1828676453401948162, 2212121, 1729067620457848832, 1768538350125629441, '2024-08-28 14:10:16', '192.168.5.69', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-08-28 14:10:16');
INSERT INTO `t_mock_access_log` VALUES (1828676500554313729, 2212121, 1729067620457848832, 1768538350125629441, '2024-08-28 14:10:28', '192.168.5.69', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-08-28 14:10:27');
INSERT INTO `t_mock_access_log` VALUES (1828676545215262722, 2212121, 1729067620457848832, 1768538350125629441, '2024-08-28 14:10:38', '192.168.5.69', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-08-28 14:10:38');
INSERT INTO `t_mock_access_log` VALUES (1829421011005943810, 2212121, 1729067620457848832, 1, '2024-08-30 15:28:53', '192.168.5.69', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-08-30 15:28:52');
INSERT INTO `t_mock_access_log` VALUES (1829421868938248194, 2212121, 1729067620457848832, 1829421823077728258, '2024-08-30 15:32:17', '192.168.5.69', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-08-30 15:32:17');
INSERT INTO `t_mock_access_log` VALUES (1829421874231459841, 2212121, 1729067620457848832, 1829421823077728258, '2024-08-30 15:32:18', '192.168.5.69', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-08-30 15:32:18');
INSERT INTO `t_mock_access_log` VALUES (1829422168021483522, 2212121, 1729067620457848832, 1829421823077728258, '2024-08-30 15:33:29', '192.168.5.69', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-08-30 15:33:28');
INSERT INTO `t_mock_access_log` VALUES (1872491365674561537, 2212121, 1729067620457848832, 1, '2024-12-27 11:55:05', '192.168.5.159', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-12-27 11:55:05');
INSERT INTO `t_mock_access_log` VALUES (1872492716026548225, 2212121, 1729067620457848832, 1, '2024-12-27 12:00:27', '192.168.5.159', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-12-27 12:00:27');
INSERT INTO `t_mock_access_log` VALUES (1872493076908658690, 2212121, 1729067620457848832, 1, '2024-12-27 12:01:53', '192.168.5.159', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-12-27 12:01:53');
INSERT INTO `t_mock_access_log` VALUES (1872493097410416641, 2212121, 1729067620457848832, 1, '2024-12-27 12:01:58', '192.168.5.159', '本地局域网', '{\"operatingSystem\":\"UNKNOWN\",\"browser\":\"UNKNOWN\",\"id\":16843022}', '2024-12-27 12:01:58');

-- ----------------------------
-- Table structure for t_mock_info
-- ----------------------------
DROP TABLE IF EXISTS `t_mock_info`;
CREATE TABLE `t_mock_info`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  `project_id` bigint(20) NOT NULL COMMENT '项目ID',
  `mock_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口名称',
  `method` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'GET,HEAD,POST,PUT, PATCH, DELETE,OPTIONS,TRACE;',
  `json_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口数据',
  `mockjs_flag` tinyint(4) NOT NULL COMMENT '是否开启mockjs模拟随机数据，0开启，1不开启',
  `url` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL',
  `status` tinyint(4) NOT NULL COMMENT '状态标志（0--启用1--禁用）',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标志（0--未删除1--已删除）',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `delay` bigint(20) NULL DEFAULT NULL COMMENT '返回延时（单位毫秒）',
  `http_code` int(10) NULL DEFAULT NULL COMMENT 'http响应码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_mock_info
-- ----------------------------
INSERT INTO `t_mock_info` VALUES (1, 2212121, 1729067620457848832, '测试接口1', 'GET', '{\n  \"sites\": {\n    \"site\": [\n      {\n        \"number|1-100\": 100,\n        \"name\": \"@name\",\n        \"url\": \"@email\"\n      },\n      {\n        \"id\": \"@integer(10, 30)\",\n        \"name\": \"@name\",\n        \"url\": \"@email\"\n      },\n      {\n        \"id\": \"3\",\n        \"name\": \"@name\",\n        \"url\": \"@now\"\n      }\n    ]\n  }\n}', 0, '/inter1/append', 0, 0, '2024-03-09 19:12:46', '2025-03-28 11:37:46', NULL, 4000, 200);
INSERT INTO `t_mock_info` VALUES (1768538350125629441, 2212121, 1729067620457848832, '登录接口', 'POST', '{\"data\":{\"token\":\"Admin-DwCfwXoeaNoTTx1tJEBbGP7GJbT5uy\"},\"code\":200,\"message\":\"登录成功\"}', 1, '/login', 0, 0, '2024-03-15 15:22:55', '2025-03-28 10:54:11', 'XXXX', 200, NULL);
INSERT INTO `t_mock_info` VALUES (1768544377218842626, 2212121, 1729067620457848832, '获取省份', 'POST', '{\"object|2\":{\"310000\":\"上海市\",\"320000\":\"江苏省\",\"330000\":\"浙江省\",\"340000\":\"安徽省\"}}', 0, '/province', 0, 0, '2024-03-15 15:46:52', '2024-03-15 22:26:19', 'YYYY', 333, NULL);
INSERT INTO `t_mock_info` VALUES (1829421823077728258, 2212121, 1729067620457848832, '测试接口2', 'POST', '{\"code\":\"0000\",\"data\":{\"list|20\":[{\"name\":\"@name\",\"age\":\"@integer(20)\"}],\"url\":\"\'11111\'\"},\"desc\":\"成功\"}', 0, '/test2', 0, 0, '2024-08-30 15:32:06', '2024-08-30 15:33:15', '', 200, 200);
INSERT INTO `t_mock_info` VALUES (1905549326245621762, 2212121, 1729067620457848832, '测试接口5', 'POST', '{}', 1, '/test5', 0, 0, '2025-03-28 17:15:37', '2025-03-28 17:19:25', '', NULL, 200);
INSERT INTO `t_mock_info` VALUES (1905549455363076097, 2212121, 1729067620457848832, '测试接口6', 'GET', '{}', 0, '/test6', 0, 0, '2025-03-28 17:16:08', '2025-03-28 17:19:15', '', NULL, 200);

-- ----------------------------
-- Table structure for t_mock_info_history
-- ----------------------------
DROP TABLE IF EXISTS `t_mock_info_history`;
CREATE TABLE `t_mock_info_history`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `mock_id` bigint(20) NOT NULL COMMENT '接口ID',
  `version` int(11) NOT NULL COMMENT '历史版本号，从1开始，每次加1',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  `operator_id` bigint(20) NOT NULL COMMENT '操作人ID(有可能是协作者操作的)',
  `operate_type` tinyint(4) NOT NULL COMMENT '操作类型(1-新增，2-更新，3-删除)',
  `project_id` bigint(20) NOT NULL COMMENT '项目ID',
  `mock_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口名称',
  `method` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'GET,HEAD,POST,PUT, PATCH, DELETE,OPTIONS,TRACE;',
  `json_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口数据',
  `mockjs_flag` tinyint(4) NOT NULL COMMENT '是否开启mockjs模拟随机数据，0开启，1不开启',
  `url` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `delay` bigint(20) NULL DEFAULT NULL COMMENT '返回延时（单位毫秒）',
  `http_code` int(10) NULL DEFAULT NULL COMMENT 'http响应码',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '历史记录创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '接口历史版本表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_mock_info_history
-- ----------------------------
INSERT INTO `t_mock_info_history` VALUES (1768639055106686978, 1768544377218842626, 1, 2212121, 2212121, 1, 1729067620457848832, '获取省', 'POST', '{\n  \"object|2\": {\n    \"310000\": \"上海市\",\n    \"320000\": \"江苏省\",\n    \"330000\": \"浙江省\",\n    \"340000\": \"安徽省\"\n  }\n}', 0, '/province', '', 200, NULL, '2024-03-15 22:03:05');
INSERT INTO `t_mock_info_history` VALUES (1768644872535121922, 1768544377218842626, 2, 2212121, 2212121, 1, 1729067620457848832, '获取省', 'POST', '{\"object|2\":{\"310000\":\"上海市\",\"320000\":\"江苏省\",\"330000\":\"浙江省\",\"340000\":\"安徽省\"}}', 0, '/province', 'YYYY', 2000, NULL, '2024-03-15 22:26:12');
INSERT INTO `t_mock_info_history` VALUES (1768644902046244865, 1768544377218842626, 3, 2212121, 2212121, 1, 1729067620457848832, '获取省份', 'POST', '{\"object|2\":{\"310000\":\"上海市\",\"320000\":\"江苏省\",\"330000\":\"浙江省\",\"340000\":\"安徽省\"}}', 0, '/province', 'YYYY', 222, NULL, '2024-03-15 22:26:19');
INSERT INTO `t_mock_info_history` VALUES (1829421823136448514, 1829421823077728258, 1, 2212121, 2212121, 1, 1729067620457848832, '测试接口2', 'POST', '{\"code\":\"0000\",\"data\":{\"list|20\":[{\"name\":\"@name\",\"age\":\"@integer(20)\"}],\"url\":\"\'11111\'\"},\"desc\":\"成功\"}', 0, '/test2', '', 200, 200, '2024-08-30 15:32:06');
INSERT INTO `t_mock_info_history` VALUES (1905453334418001922, 1768538350125629441, 1, 2212121, 2212121, 2, 1729067620457848832, '登录接口', 'POST', '{\"data\":{\"token\":\"Admin-DwCfwXoeaNoTTx1tJEBbGP7GJbT5uy\"},\"code\":200,\"message\":\"登录成功\"}', 0, '/login', 'XXXX', 200, NULL, '2025-03-28 10:54:11');
INSERT INTO `t_mock_info_history` VALUES (1905464278054658050, 1, 2, 2212121, 2212121, 2, 1729067620457848832, '测试接口1', 'GET', '{\n  \"sites\": {\n    \"site\": [\n      {\n        \"number|1-100\": 100,\n        \"name\": \"@name\",\n        \"url\": \"@email\"\n      },\n      {\n        \"id\": \"@integer(10, 30)\",\n        \"name\": \"@name\",\n        \"url\": \"@email\"\n      },\n      {\n        \"id\": \"3\",\n        \"name\": \"@name\",\n        \"url\": \"@now\"\n      }\n    ]\n  }\n}', 0, '/inter1/append', NULL, 4000, 200, '2025-03-28 11:37:40');
INSERT INTO `t_mock_info_history` VALUES (1905464303627329538, 1, 3, 2212121, 2212121, 2, 1729067620457848832, '测试接口1', 'POST', '{\n  \"sites\": {\n    \"site\": [\n      {\n        \"number|1-100\": 100,\n        \"name\": \"@name\",\n        \"url\": \"@email\"\n      },\n      {\n        \"id\": \"@integer(10, 30)\",\n        \"name\": \"@name\",\n        \"url\": \"@email\"\n      },\n      {\n        \"id\": \"3\",\n        \"name\": \"@name\",\n        \"url\": \"@now\"\n      }\n    ]\n  }\n}', 0, '/inter1/append', NULL, 4000, 200, '2025-03-28 11:37:46');
INSERT INTO `t_mock_info_history` VALUES (1905549326291759106, 1905549326245621762, 1, 2212121, 2212121, 1, 1729067620457848832, '测试接口5', 'GET', '{}', 0, '/test5', '', NULL, 200, '2025-03-28 17:15:37');
INSERT INTO `t_mock_info_history` VALUES (1905549455430184962, 1905549455363076097, 1, 2212121, 2212121, 1, 1729067620457848832, '测试接口6', 'GET', '{}', 1, '/test6', '', NULL, 200, '2025-03-28 17:16:08');
INSERT INTO `t_mock_info_history` VALUES (1905550241430171649, 1905549455363076097, 2, 2212121, 2212121, 2, 1729067620457848832, '测试接口6', 'GET', '{}', 1, '/test6', '', NULL, 200, '2025-03-28 17:19:15');
INSERT INTO `t_mock_info_history` VALUES (1905550281880039426, 1905549326245621762, 2, 2212121, 2212121, 2, 1729067620457848832, '测试接口5', 'GET', '{}', 0, '/test5', '', NULL, 200, '2025-03-28 17:19:25');
INSERT INTO `t_mock_info_history` VALUES (1909815407493644289, 1, 4, 2212121, 2212121, 2, 1729067620457848832, '测试接口1', 'GET', '{\n  \"sites\": {\n    \"site\": [\n      {\n        \"number|1-100\": 100,\n        \"name\": \"@name\",\n        \"url\": \"@email\"\n      },\n      {\n        \"id\": \"@integer(10, 30)\",\n        \"name\": \"@name\",\n        \"url\": \"@email\"\n      },\n      {\n        \"id\": \"3\",\n        \"name\": \"@name\",\n        \"url\": \"@now\"\n      }\n    ]\n  }\n}', 0, '/inter1/append', NULL, 4000, 200, '2025-04-09 11:47:30');
INSERT INTO `t_mock_info_history` VALUES (2024086000000003918, 1, 1, 2212121, 2212121, 1, 1729067620457848832, '测试接口1', 'GET', '{\n  \"sites\": {\n    \"site\": [\n      {\n        \"number|1-100\": 100,\n        \"name\": \"@name\",\n        \"url\": \"@email\"\n      },\n      {\n        \"id\": \"@integer(10, 30)\",\n        \"name\": \"@name\",\n        \"url\": \"@email\"\n      },\n      {\n        \"id\": \"3\",\n        \"name\": \"@name\",\n        \"url\": \"@now\"\n      }\n    ]\n  }\n}', 0, '/inter1/append', NULL, 4000, NULL, '2024-03-26 13:36:34');

-- ----------------------------
-- Table structure for t_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `t_operate_log`;
CREATE TABLE `t_operate_log`  (
  `id` bigint(20) NOT NULL COMMENT '业务主键',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户账号',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户密码',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方法名称',
  `request_method` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方式',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求url',
  `oper_result` tinyint(4) NOT NULL COMMENT '操作结果（0成功 1失败）',
  `oper_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作地址IP',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作地点',
  `cost_time` bigint(20) NULL DEFAULT NULL COMMENT '花费时间',
  `business_type` tinyint(4) NOT NULL COMMENT '业务类型（0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据）',
  `operator_type` tinyint(4) NOT NULL COMMENT '操作类别（0其它 1管理端用户 2租户端用户）',
  `operate_at` char(23) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作时间',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误消息',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '返回结果',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '操作人员',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标志（0--未删除1--已删除）',
  `audit_hash` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '审计值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_operate_log
-- ----------------------------
INSERT INTO `t_operate_log` VALUES (1866053486591115265, '10000', '明细', 'org.tinycloud.tinymock.modules.web.ProjectInfoController.detail()', 'GET', '/projectinfo/detail', 0, '192.168.5.21', '本地局域网', 153, 0, 2, '2024-12-09 17:33:15', NULL, '{\"_\":\"1733736739047\",\"id\":\"1729067620457848832\"}', '{\"code\":0,\"data\":{\"id\":1729067620457848832,\"tenantId\":2212121,\"projectName\":\"测试项目\",\"introduce\":\"测试项目1\",\"path\":\"/test\",\"status\":0,\"createdAt\":\"2023-11-28 14:10:01\",\"updatedAt\":\"2024-03-13 12:03:00\"},\"msg\":\"获取成功\",\"time\":1733736795309}', 2212121, '2024-12-09 17:33:15', 0, '');
INSERT INTO `t_operate_log` VALUES (1866053761456439297, '10001', '新增项目', 'org.tinycloud.tinymock.modules.web.ProjectInfoController.add()', 'POST', '/projectinfo/add', 1, '192.168.5.21', '本地局域网', 168, 0, 2, '2024-12-09 17:34:21', '项目名称或项目路径已存在，请修改！', '{\"projectName\":\"测试项目\",\"path\":\"/dsadsada\",\"introduce\":\"\",\"remark\":\"\"}', NULL, 2212121, '2024-12-09 17:34:20', 0, '');
INSERT INTO `t_operate_log` VALUES (1866054197508874241, '10001', '新增项目', 'org.tinycloud.tinymock.modules.web.ProjectInfoController.add()', 'POST', '/projectinfo/add', 1, '192.168.5.21', '本地局域网', 250, 0, 2, '2024-12-09 17:36:05', '项目名称或项目路径已存在，请修改！', '{\"projectName\":\"测试项目\",\"path\":\"/dsadsada\",\"introduce\":\"\",\"remark\":\"\"}', NULL, 2212121, '2024-12-09 17:36:04', 0, '');
INSERT INTO `t_operate_log` VALUES (1866054197508874242, '10001', '新增项目', 'org.tinycloud.tinymock.modules.web.ProjectInfoController.add()', 'POST', '/projectinfo/add', 1, '192.168.5.21', '本地局域网', 706, 0, 2, '2024-12-09 17:36:04', '项目名称或项目路径已存在，请修改！', '{\"projectName\":\"测试项目\",\"path\":\"/dsadsada\",\"introduce\":\"\",\"remark\":\"\"}', NULL, 2212121, '2024-12-09 17:36:04', 0, '');
INSERT INTO `t_operate_log` VALUES (1866054424492044289, '10001', '新增项目', 'org.tinycloud.tinymock.modules.web.ProjectInfoController.add()', 'POST', '/projectinfo/add', 1, '192.168.5.21', '本地局域网', 783, 0, 2, '2024-12-09 17:36:58', '项目名称或项目路径已存在，请修改！', '{\"projectName\":\"测试项目\",\"path\":\"/dsadsada\",\"introduce\":\"\",\"remark\":\"\"}', NULL, 2212121, '2024-12-09 17:36:59', 0, '');
INSERT INTO `t_operate_log` VALUES (1869276449801027586, '10013', '禁用接口', 'org.tinycloud.tinymock.modules.web.MockInfoController.disable()', 'GET', '/mockinfo/disable', 0, '192.168.5.21', '本地局域网', 279, 11, 2, '2024-12-18 15:00:09.320', NULL, '{\"_\":\"1734505201952\",\"id\":\"1\"}', '{\"code\":0,\"data\":true,\"msg\":\"禁用成功!\",\"time\":1734505209336}', 2212121, '2024-12-18 15:00:09', 0, '86a53612a4e855986cd409e2e9213f5151fa9a24333ac302ca2459fe46a5e89c');
INSERT INTO `t_operate_log` VALUES (1869276457443049474, '10012', '启用接口', 'org.tinycloud.tinymock.modules.web.MockInfoController.enable()', 'GET', '/mockinfo/enable', 0, '192.168.5.21', '本地局域网', 61, 10, 2, '2024-12-18 15:00:11.528', NULL, '{\"_\":\"1734505201953\",\"id\":\"1\"}', '{\"code\":0,\"data\":true,\"msg\":\"启用成功!\",\"time\":1734505211540}', 2212121, '2024-12-18 15:00:11', 0, '6d07dceec99fe5137c93bb36f1fef8147290df5fc4eda74c264fc4e8dc6300c4');
INSERT INTO `t_operate_log` VALUES (1871737356477063170, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '192.168.5.145', '本地局域网', 1120, 0, 1, '2024-12-25 09:58:54.507', NULL, '{\"username\":\"zhangsan\",\"password\":\"a0bd88f22d1a1c5b56ec7172b96a36d8871761ea6fea0ced1620a82c48fde4b7c27dcaa202212e9dc9eab00684aee80e8f932547d7ad8592d8044b1236e0a98c4d19bf0f3b2bd2da1066d2999d1ec6ba0b4d08fd34abd97c1e4493887ddeb6560ad65916a4c9df0a3e0e05bdfade28797f0b0b9f92d03472a4a99d97433af261531573e51f431fd80c78acae90ae9e1330277c2e2e0323ed2be4909ae2035e0d\",\"captcha\":\"zmj6u\",\"uuid\":\"f0349cef3c09416986a9e83e40dcf1d4\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3MzUxNzgzMzUsImlhdCI6MTczNTA5MTkzNSwidG9rZW4iOiJlMWE0NTk2MjQ3YzE0Y2MxOWI0Nzg4YzFlNTIyZmUyYSJ9.iijRSdqr9C33p--QIivfI9Rb942yx2MgZTuNGYUJPgc\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1735091935558}', 1, '2024-12-25 09:58:55', 0, 'b213654e32488b6d91d22cd8461630209ff52658eedf4b64b30dc10b25c28e64');
INSERT INTO `t_operate_log` VALUES (1871750337843773442, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '192.168.5.145', '本地局域网', 1858, 0, 1, '2024-12-25 10:50:28.758', NULL, '{\"username\":\"zhangsan\",\"password\":\"5bda28dace569b0bf98c808bfe335aa0f481fb8ee8ade9c6dd7369ab5a0a001856027d1be6ecab4388bdc338c54f3fb53c74d61800d7d0867bde680eec3427577312a7ca6d1a1441b3d084750c9f19d87818f8b21b3c1daeaab186bfcc4dc4dbc83efafcfd45d7801c1f099ecd619eb940adea30669f0161e23aefa4b309da785d834c99bedb5237f4a9307af4730e0bfe8521cc6be280e4acc78227d43e05cd\",\"captcha\":\"kz8yk\",\"uuid\":\"de176292dae549c1b95be057390f6451\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3MzUxODE0MzAsImlhdCI6MTczNTA5NTAzMCwidG9rZW4iOiJhOTQyNmUxY2E4Zjg0NjhiYTBmOGNhMmY5ODMyODYxNSJ9.ZxaPvrjo-8W2_1N5uUCdIkQ9PvMfLuaEMSKthKIGiYk\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1735095030519}', 1, '2024-12-25 10:50:30', 0, '786888dfddb73631c0c4c81ac1c2c880102d913f18b916f317cf58cbdffb56e8');
INSERT INTO `t_operate_log` VALUES (1872491244358512642, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '192.168.5.159', '本地局域网', 2283, 0, 1, '2024-12-27 11:54:34.222', NULL, '{\"username\":\"zhangsan\",\"password\":\"20c2fb10891b12cdbe4ecb320c1ac239f9d53f083c569f7ef2ace92d5dc504cd362f95d3905de61bf9b1a9830f79ee22d642bad65a11d0c8a6b092211ece508fffe874ab6580cb55476f6b64eb7857d7a548522eb639e5f85f1fcf417fcb8b960bc1ce0f5c70651fe76de080c7c7897895a5373186678d3e24286f3f3f7db5ff355ff15d13de962c5d7355da5ff229b03a0b45e2b36238a66cc59648a2d0aa09\",\"captcha\":\"fyzyy\",\"uuid\":\"059aec83012d4ae087fac93b1213715d\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3MzUzNTgwNzUsImlhdCI6MTczNTI3MTY3NSwidG9rZW4iOiI4NjcwYTBmNjIzOGU0NGMxOTU1NjE3ZTA0N2RjMGI0MyJ9.JnRnVEb_9v_FVdHS2mxbYkI08pQsenCSq_fh-Gchg5I\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1735271676419}', 1, '2024-12-27 11:54:36', 0, '3054ba2a49a77b29387be9d6418efd932a452226cba03e1c7d34a2d69debe57a');
INSERT INTO `t_operate_log` VALUES (1872496722744180737, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '192.168.5.159', '本地局域网', 1528, 0, 1, '2024-12-27 12:16:21.121', NULL, '{\"username\":\"zhangsan\",\"password\":\"6fe72a852ec44ba031d956c36b0d28c0690a973327c684c7af356f7c3e5eac88d2ce28bad84ef2af9ee8d5f761b1d162ae399ee16ea1066e4daf7bfeb762496b2e21f1d1f5b0e43bbd2ccffdc54f68b004b6bb349be2589989553332812275064b0f20b16a44be38ce145a40c3ca43e01306e221fe0729107a139d56312b9f39d719a7dce4c408c1ced9c27a69205aded6211a76ac07657fc24ea08ad43d7e32\",\"captcha\":\"wfcnw\",\"uuid\":\"ce44954f924c490fbf8b223ac5005526\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3MzUzNTkzODIsImlhdCI6MTczNTI3Mjk4MiwidG9rZW4iOiJkNTc1ZjRmODlmYzc0NmI0OTg4YzM2OTk2YTgxM2RiNyJ9.1hbwOjjuUysqodyZyo80JiIKb5gKMnGMouDWxQA-leE\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1735272982566}', 1, '2024-12-27 12:16:22', 0, '31fc372b836b3ad303af70f315a8dfc3a60707759b34771dcb5f622621de57b4');
INSERT INTO `t_operate_log` VALUES (1872496944098574337, '20002', '租户退出登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.logout()', 'GET', '/auth/logout', 0, '192.168.5.159', '本地局域网', 170, 0, 2, '2024-12-27 12:17:15.274', NULL, '{\"_\":\"1735272984023\"}', '{\"code\":0,\"data\":true,\"msg\":\"退出登录成功！\",\"time\":1735273035279}', 2212121, '2024-12-27 12:17:15', 0, 'ab18d4bd900f0db2320c772f6736b246fcda6904138f7e867c15c05770e41b1a');
INSERT INTO `t_operate_log` VALUES (1872497002936270850, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '192.168.5.159', '本地局域网', 276, 0, 1, '2024-12-27 12:17:29.197', NULL, '{\"username\":\"zhangsan\",\"password\":\"59f2354295498be5472bb5e1fafd58ed991aa8c5749a5481c8d599c02066939f3a97c519cf7792a75b32b87d50f9959118b3faf426214988310c1977a697eb607a2d0f175e85d42c25208a5630b7151293eaf251a473db94c88788f4a96a1d61b64ea29306177ef40a270ab87e6b29b08558d907c9f92254d60e375d4105e58f9a2d5e1efe058c352ae5707bacc78163c03c55616d7c386388ac4a579566f9da\",\"captcha\":\"sjdpg\",\"uuid\":\"8b5a22735354409ca5aaa44ca23dc46b\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3MzUzNTk0NDksImlhdCI6MTczNTI3MzA0OSwidG9rZW4iOiJhZjliZjdjZDVmMTA0MDhlOGZmZTMxNWU1YTVjYjE0ZCJ9.bXSvKQd3mEtGkkzwsfsh044X_vgiQPumCJ238Gc7B90\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1735273049426}', 1, '2024-12-27 12:17:29', 0, '1aabb0715b1567908861ba7b16671069f8c141f4cbfd5aa9ab38dcb4c30c3ee5');
INSERT INTO `t_operate_log` VALUES (1872497127305773058, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '192.168.5.159', '本地局域网', 278, 0, 1, '2024-12-27 12:17:58.842', NULL, '{\"username\":\"zhangsan\",\"password\":\"baa4b6a6cb175bc533a8ed26531fa498af0e655db4a7bad258e9c7de6816d20de368eb9ee7c5f00e9432a17398652e40b7b5b5a531921b7230763cbbbbc5edf082bd2263be6cda1448309f169f981ec76f165216ac54bc0672fbea7997c5a2b191657bde5ff10f67bbe635d1181ea77e5597e6f598e346a4328e71c273a2d87ed2d7cad191ecabccb18e6b95fdeadbb18f1859898d8ed5c5695c78250aaac549\",\"captcha\":\"y79hg\",\"uuid\":\"1a223832b75848d48db9b2d4511bdef8\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3MzUzNTk0NzgsImlhdCI6MTczNTI3MzA3OCwidG9rZW4iOiJlYTBmMDc3MGVhY2M0MjFkOWMyZDhjMjY1Nzc2ZjBkNSJ9.dqt8wf9sz-limXijjyikkXKaFYePIx9oVN_XuA_W_iA\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1735273079077}', 1, '2024-12-27 12:17:59', 0, '5b8057b268a215fce7e437d1ac5869bd50df9c932261c3525ee5c9ef5092c7d8');
INSERT INTO `t_operate_log` VALUES (1872497237808906241, '20002', '租户退出登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.logout()', 'GET', '/auth/logout', 0, '192.168.5.159', '本地局域网', 141, 0, 2, '2024-12-27 12:18:25.323', NULL, '{\"_\":\"1735273080546\"}', '{\"code\":0,\"data\":true,\"msg\":\"退出登录成功！\",\"time\":1735273105327}', 2212121, '2024-12-27 12:18:25', 0, 'a2cf844a840b0a2f636a9a5252f6da3b01cfd1aa8d4f9c5d05bed0cb0f511dcc');
INSERT INTO `t_operate_log` VALUES (1887757307966373889, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '192.168.5.253', '本地局域网', 2010, 0, 1, '2025-02-07 14:56:27.688', NULL, '{\"username\":\"zhangsan\",\"password\":\"6576fbd8cc9f82db04a76a08b2c6fa74278d0ae93d54a60fcfced917dca140245a85698e02f96023c78530685ed415096db393f8036387e77d57feb1c44c1635003148fb20bbe796eaaeee3bc65b800a3e34ec8de8ec9aa61ce9fdbf52d7a6173c3e3cc5868d1198e04fa290f61db0b362a64519ee26069bc5a7f860a913355fec141a96b4f9ef5a1f47c8f2ac2bb3a6691377a004260045f19a7f4dfeba1c9b\",\"captcha\":\"mrpzb\",\"uuid\":\"55be104cc18c42d8a0b5f1b56a6d3d9e\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3Mzg5OTc3ODksImlhdCI6MTczODkxMTM4OSwidG9rZW4iOiIxOTgyOWZjZWJkYTQ0NGViYWFlMmRjYzUwZWZhNWUyZiJ9.tJe4fpX7Aenw6ajD_LyF7SmaW3TEeCwHe8jz_Yt3Qss\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1738911389609}', 1, '2025-02-07 14:56:29', 0, 'eb6f8829b52584f4aa14bfaced5af5257dc9e02a2e04f762e3bad25d04396635');
INSERT INTO `t_operate_log` VALUES (1905147877217259521, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '192.168.5.199', '本地局域网', 1412, 0, 1, '2025-03-27 14:40:23.096', NULL, '{\"username\":\"zhangsan\",\"password\":\"709b347f404b95b0f24790348948d7e0fb37735e9f340bb69ac820c2ebb6208297cfed0cbdba6be70804ff745248c142edce3e17e315a56dd8bbf1cf1b577630c47621bb09a9320ee7d296fda3c69e48e3bd29876cfc4cc7216b540a893564bf47613a775d33227bd949169ac49dcc6fc7cedefc720f0fc3f4d02c43f324a049f6c9e4961f06da547d90bb002ee58e44418a1b05789c7e352c95aa329227d7bc\",\"captcha\":\"cuvxy\",\"uuid\":\"dd301484565d49388a3950bd6d4edcb6\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3NDMxNDQwMjQsImlhdCI6MTc0MzA1NzYyNCwidG9rZW4iOiJjZGFiOGQ0NWJkYmM0MWU5YTFiNTk3ZDkxYTQyNTdhZiJ9.h5iCd-GO4YgsxOBiFp19vJ7jqlSaApOPrM2DtGlNm_g\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1743057624430}', 1, '2025-03-27 14:40:24', 0, 'bc7d9a4ec194c8c2f694c2ad16d5a7258ba88b5e51c39f14235f8cd513a1f4c4');
INSERT INTO `t_operate_log` VALUES (1905167782746836993, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '192.168.5.199', '本地局域网', 1195, 0, 1, '2025-03-27 15:59:29.148', NULL, '{\"username\":\"zhangsan\",\"password\":\"3d68933b55ffcf907b61a93fd01e97cf6248304dd3fb4a38769c71ccf73b191dcb57182f8a3286b8adef20309d84b692c62d764b8e1b00c1f360f7cdd19851590931554205d77bedf17d47aed0c99afa43c4120d15adc38bed704441a05362bc67b2501adc6038d6ebd6e8f530b52f6f33e8ffb109710dd17ee04f0ce995d319b43f2eca8e32ed174034843b6c334968a25683a10c2e7badcc43d5e20ec94871\",\"captcha\":\"4pgut\",\"uuid\":\"b983022c153940d8915ea5e29702cf50\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3NDMxNDg3NjksImlhdCI6MTc0MzA2MjM2OSwidG9rZW4iOiI1ODUxODY5MzY3YzU0OGRkYWEwMzhiMjU2YmQ0YTk1MSJ9.stLhcq27rZAIJLlf7T0ymEwajWhryu74eYznYh1LPVs\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1743062370241}', 1, '2025-03-27 15:59:30', 0, 'e9d96de2721dd600b406ba6e67270760fd4d0f7658fe98e3a9d75334cec85449');
INSERT INTO `t_operate_log` VALUES (1905167837272788993, '20002', '租户退出登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.logout()', 'GET', '/auth/logout', 0, '192.168.5.199', '本地局域网', 166, 0, 2, '2025-03-27 15:59:43.211', NULL, '{\"_\":\"1743062371690\"}', '{\"code\":0,\"data\":true,\"msg\":\"退出登录成功！\",\"time\":1743062383214}', 2212121, '2025-03-27 15:59:43', 0, '6d245e6c8c4883e0d1a648770e66118349e81b90793153cc4801e74ac7404420');
INSERT INTO `t_operate_log` VALUES (1905167876846047233, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '192.168.5.199', '本地局域网', 314, 0, 1, '2025-03-27 15:59:52.497', NULL, '{\"username\":\"lisi\",\"password\":\"8430abe45623c9e67a7e28821e4197953eeaef0e99b43309afb8a62dc9dfabe2cbee1dd76de079379dd943ed001cecac9e70ebb8c8904ffd76d2a9c5867a5a24a5f99a723b69750ec01c4007d9ebcfbbd7a32d97d100b35d7b6ecbb308f9e9313966937e002d1f820ecd205d9631955879e27546651fd457957f0f58701db2a970eff8b643fa634bd23135302197528e937ac86329ef71af496c5f67090a64a3\",\"captcha\":\"hku2v\",\"uuid\":\"945091a4be6a4e2b92213225266035f7\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3NDMxNDg3OTIsImlhdCI6MTc0MzA2MjM5MiwidG9rZW4iOiI5MzBlMmZhYWZlMzM0NWZjOTI5YzgwNjMxMTYxMjllNiJ9.8a1gLCknLHTxfpfveHG58gYEbOfQzGnk41YbhmK3NDc\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1743062392763}', 1, '2025-03-27 15:59:52', 0, '98216d4635157eb480081a6165581557d9de80fedf94f867ed4a1f53ec46ccf2');
INSERT INTO `t_operate_log` VALUES (1905167918755532802, '10003', '删除项目', 'org.tinycloud.tinymock.modules.web.ProjectInfoController.delete()', 'POST', '/projectinfo/delete', 1, '192.168.5.199', '本地局域网', 238, 3, 2, '2025-03-27 16:00:02.568', '只有项目创建者才可以删除！', '{\"id\":1729067620457848832,\"projectName\":\"dsds\"}', NULL, 2024080000000001698, '2025-03-27 16:00:02', 0, '2396852f7549f42115510c15f9cf6a06767873321d9d8226b50bfa4b40b1802b');
INSERT INTO `t_operate_log` VALUES (1905167925856489474, '10003', '删除项目', 'org.tinycloud.tinymock.modules.web.ProjectInfoController.delete()', 'POST', '/projectinfo/delete', 1, '192.168.5.199', '本地局域网', 55, 3, 2, '2025-03-27 16:00:04.433', '只有项目创建者才可以删除！', '{\"id\":1729067620457848832,\"projectName\":\"dsds\"}', NULL, 2024080000000001698, '2025-03-27 16:00:04', 0, '6a1583381a84f50ce955495aca1f62dbbf98b85fedf9f6f15f7e119e95d81e48');
INSERT INTO `t_operate_log` VALUES (1905167930805768194, '10003', '删除项目', 'org.tinycloud.tinymock.modules.web.ProjectInfoController.delete()', 'POST', '/projectinfo/delete', 1, '192.168.5.199', '本地局域网', 55, 3, 2, '2025-03-27 16:00:05.613', '只有项目创建者才可以删除！', '{\"id\":1729067620457848832,\"projectName\":\"dsds\"}', NULL, 2024080000000001698, '2025-03-27 16:00:05', 0, '1332f3e7af7724afbed65954fa5195b6bab36a0b9506350b3a875d2b7f0de0ee');
INSERT INTO `t_operate_log` VALUES (1905167936535187458, '10003', '删除项目', 'org.tinycloud.tinymock.modules.web.ProjectInfoController.delete()', 'POST', '/projectinfo/delete', 1, '192.168.5.199', '本地局域网', 53, 3, 2, '2025-03-27 16:00:06.993', '只有项目创建者才可以删除！', '{\"id\":1729067620457848832,\"projectName\":\"dsds\"}', NULL, 2024080000000001698, '2025-03-27 16:00:07', 0, '71e1b611294797dba0f51ae5139382ac714d06a74147f8673e080c72ee727b56');
INSERT INTO `t_operate_log` VALUES (1905170389736136706, '10013', '禁用接口', 'org.tinycloud.tinymock.modules.web.MockInfoController.disable()', 'GET', '/mockinfo/disable', 0, '192.168.5.199', '本地局域网', 411, 11, 2, '2025-03-27 16:09:51.216', NULL, '{\"_\":\"1743062986699\",\"id\":\"1\"}', '{\"code\":0,\"data\":true,\"msg\":\"禁用成功!\",\"time\":1743062991245}', 2024080000000001698, '2025-03-27 16:09:51', 0, '11fe0bfe683d09b07bb9bbc78c6cd0a821b54c65ca3ca666c400e6e8d5093b62');
INSERT INTO `t_operate_log` VALUES (1905170417166884866, '10012', '启用接口', 'org.tinycloud.tinymock.modules.web.MockInfoController.enable()', 'GET', '/mockinfo/enable', 0, '192.168.5.199', '本地局域网', 183, 10, 2, '2025-03-27 16:09:58.290', NULL, '{\"_\":\"1743062986700\",\"id\":\"1\"}', '{\"code\":0,\"data\":true,\"msg\":\"启用成功!\",\"time\":1743062998307}', 2024080000000001698, '2025-03-27 16:09:58', 0, 'a2a4e43ae99b38753611f150cc025745752be38a0358ceb560d69fdc8883b4df');
INSERT INTO `t_operate_log` VALUES (1905439495735205889, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '192.168.5.199', '本地局域网', 2146, 0, 1, '2025-03-28 09:59:09.629', NULL, '{\"username\":\"zhangsan\",\"password\":\"dc83a3fdc2d6717b5e3f89c2383b0b1e14a6ac73bbe467782d4928b69cf86b1a98903b23ebf77f0f8a67811ebe91c99d194b5c718972a5dce9fa9700142d13bc897f5347abef14231439c522d0ab7759842c77c61bd8e17dc3f1ccc3458f32cb4a90a0f4035f460a1d248fd6b4d78bcf3d4124e6bce9bd8ef702bc1b231633510155f77ab1081e3abe6c9afc807cdb9ebe9b4135954f2ae97567c4137d0655d9\",\"captcha\":\"quu66\",\"uuid\":\"a47089e67a2c43a581edd459714d830f\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3NDMyMTM1NTAsImlhdCI6MTc0MzEyNzE1MCwidG9rZW4iOiI0YzBhYjRmMGE4NTQ0YTZhYTYzNmFjZTU5ZGZiMzQ5MyJ9.uI3IJgk9AtyMb-oAG85QX6cKB7Pr8nlJcXOWV3OAqPE\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1743127151663}', 1, '2025-03-28 09:59:11', 0, '1aef63390ee77444f7e2ab8f731bc28d4632e03cd9fd91d27f344c6a4710d95e');
INSERT INTO `t_operate_log` VALUES (1905453219234025474, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '192.168.5.199', '本地局域网', 2250, 0, 1, '2025-03-28 10:53:41.465', NULL, '{\"username\":\"zhangsan\",\"password\":\"b1b6db37a38af6d222a86d64eeaf9b961260b6129104d5702e48a9cd82b01b9048d83034469a4f539216e89aa714388f1c48798c9b4602918eb8759f3d262838c86e312fc8d13369921eab73d2d10ee362bb6dbda4b32cdc7a4bffadeef27bbb68e6128044825be7eee30e26919c9bbbec276b097ce8611bbce883caa9c51b59fb7b34b938c7cff268490b89674fd4b78a997e163b9e4be56b6a5fd2b4ae441c\",\"captcha\":\"mcwv2\",\"uuid\":\"724cbe3fa6b34277911a115908e783db\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3NDMyMTY4MjIsImlhdCI6MTc0MzEzMDQyMiwidG9rZW4iOiIyN2RjOWRjYmUyNDQ0Nzc1YWQwMzZiOGVjZDZjYjZlYyJ9.mZZqy9bu1DNB_rSC0eC8Iw7nwcbHH6dklQUyut8Ea14\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1743130423623}', 1, '2025-03-28 10:53:43', 0, 'c6440a6c34d409932cd64d5af6e3c397588cf993f887c18d55b929e1373579ee');
INSERT INTO `t_operate_log` VALUES (1905453335219113986, '10015', '修改接口', 'org.tinycloud.tinymock.modules.web.MockInfoController.edit()', 'POST', '/mockinfo/edit', 0, '192.168.5.199', '本地局域网', 256, 2, 2, '2025-03-28 10:54:11.138', NULL, '{\"id\":1768538350125629441,\"mockName\":\"登录接口\",\"url\":\"/login\",\"method\":\"POST\",\"jsonData\":\"{\\\"data\\\":{\\\"token\\\":\\\"Admin-DwCfwXoeaNoTTx1tJEBbGP7GJbT5uy\\\"},\\\"code\\\":200,\\\"message\\\":\\\"登录成功\\\"}\",\"delay\":200,\"mockjsFlag\":1,\"remark\":\"XXXX\"}', '{\"code\":0,\"data\":true,\"msg\":\"修改成功!\",\"time\":1743130451234}', 2212121, '2025-03-28 10:54:11', 0, 'e61e5a9f15e3c59141104df4fd47d39db7f156ae1f0ed754cfd28bd0485b6303');
INSERT INTO `t_operate_log` VALUES (1905464078191878145, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '192.168.5.199', '本地局域网', 2915, 0, 1, '2025-03-28 11:36:49.750', NULL, '{\"username\":\"zhangsan\",\"password\":\"a356c19d31944d5038813c94c897f02331a38c0c8454d4f4f7685bfcfebdea6e4feed06c2b99b5973c97c49a2db8ecc3921a318285f8f43877f3a4b831a727f6713fb8fb05b49110af8f2448acddf11f8c230284360853b75356148c34b476b38cf23f59c168f2a9863be40cbae346cd715fe3c45838c283cb2d3e610592a5d6dabc437d61c383dacc685037da91a2b8a8dd977a1229ee14b69fabdc4b8be513\",\"captcha\":\"47tuu\",\"uuid\":\"bebb87eadf0448ac8c10118262cff205\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3NDMyMTk0MTEsImlhdCI6MTc0MzEzMzAxMSwidG9rZW4iOiIwMzJhOWEyMWNiNDA0OWM1Yjg2ZTJmMmIwY2FjZmU3MSJ9.ZHkrKSGB3oJi_1j70ZDZ1dvMjNjsTBtESGAtbzpzk8s\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1743133012421}', 1, '2025-03-28 11:36:52', 0, '015316d533dfe8099dfce3af4ff4ce4bbb53b372269c797a1e7bde9b62bef083');
INSERT INTO `t_operate_log` VALUES (1905464278927073281, '10015', '修改接口', 'org.tinycloud.tinymock.modules.web.MockInfoController.edit()', 'POST', '/mockinfo/edit', 0, '192.168.5.199', '本地局域网', 259, 2, 2, '2025-03-28 11:37:40.309', NULL, '{\"id\":1,\"mockName\":\"测试接口1\",\"url\":\"/inter1/append\",\"method\":\"POST\",\"jsonData\":\"{\\n  \\\"sites\\\": {\\n    \\\"site\\\": [\\n      {\\n        \\\"number|1-100\\\": 100,\\n        \\\"name\\\": \\\"@name\\\",\\n        \\\"url\\\": \\\"@email\\\"\\n      },\\n      {\\n        \\\"id\\\": \\\"@integer(10, 30)\\\",\\n        \\\"name\\\": \\\"@name\\\",\\n        \\\"url\\\": \\\"@email\\\"\\n      },\\n      {\\n        \\\"id\\\": \\\"3\\\",\\n        \\\"name\\\": \\\"@name\\\",\\n        \\\"url\\\": \\\"@now\\\"\\n      }\\n    ]\\n  }\\n}\",\"delay\":4000,\"mockjsFlag\":0,\"remark\":\"\",\"httpCode\":200}', '{\"code\":0,\"data\":true,\"msg\":\"修改成功!\",\"time\":1743133060395}', 2212121, '2025-03-28 11:37:40', 0, 'b7f5269ed237e605cdb7ccad1383efa3e9457084802ce476fa7ac02cfb4e33fc');
INSERT INTO `t_operate_log` VALUES (1905464304436830209, '10015', '修改接口', 'org.tinycloud.tinymock.modules.web.MockInfoController.edit()', 'POST', '/mockinfo/edit', 0, '192.168.5.199', '本地局域网', 201, 2, 2, '2025-03-28 11:37:46.444', NULL, '{\"id\":1,\"mockName\":\"测试接口1\",\"url\":\"/inter1/append\",\"method\":\"GET\",\"jsonData\":\"{\\n  \\\"sites\\\": {\\n    \\\"site\\\": [\\n      {\\n        \\\"number|1-100\\\": 100,\\n        \\\"name\\\": \\\"@name\\\",\\n        \\\"url\\\": \\\"@email\\\"\\n      },\\n      {\\n        \\\"id\\\": \\\"@integer(10, 30)\\\",\\n        \\\"name\\\": \\\"@name\\\",\\n        \\\"url\\\": \\\"@email\\\"\\n      },\\n      {\\n        \\\"id\\\": \\\"3\\\",\\n        \\\"name\\\": \\\"@name\\\",\\n        \\\"url\\\": \\\"@now\\\"\\n      }\\n    ]\\n  }\\n}\",\"delay\":4000,\"mockjsFlag\":0,\"remark\":\"\",\"httpCode\":200}', '{\"code\":0,\"data\":true,\"msg\":\"修改成功!\",\"time\":1743133066479}', 2212121, '2025-03-28 11:37:46', 0, '25ddc048c72dd4b9d94f3a2365f047e3bfa0c2899a8bd4795ef23dea071d26e9');
INSERT INTO `t_operate_log` VALUES (1905509347700826114, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '192.168.5.199', '本地局域网', 375, 0, 1, '2025-03-28 14:36:45.420', NULL, '{\"username\":\"zhangsan\",\"password\":\"3486701e990a6e52b048b59e58d9802d5837e709a1ff32f2492c254a28ec4a2b9324749b82cb230006b0624ea0c64461992bc0379d2c72687927e391bee323ff522fb7f4ea0e5ab739f030c3ae6a81ffad5a84036fb51ca6a3934a5bb8b2a9c6e246cbeb12acba5d25f428fd3156df74aadd9c8a2def60a1b81295f045d989ffd306b5038ad4e8a0ecb412db4e9975267b345ea26598113c9f14098a410e23b1\",\"captcha\":\"ufbhy\",\"uuid\":\"66e05f61f52a4c7e847ae43b43a5f08b\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3NDMyMzAyMDUsImlhdCI6MTc0MzE0MzgwNSwidG9rZW4iOiJlOTUxZTY4Zjg1Yjk0NzcwOTA0NTUxYWMwOGM2ZTlkMyJ9.7n5unkf9dmSChStSYGwhsYgS-dESnNCHaNMqqKMIQkE\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1743143805725}', 1, '2025-03-28 14:36:45', 0, '55da54741cf494e50a5dcaa3bcaf9b33c6570df71b169d7f9dc665e85ce6fcf7');
INSERT INTO `t_operate_log` VALUES (1905509491729031169, '10014', '新增接口', 'org.tinycloud.tinymock.modules.web.MockInfoController.add()', 'POST', '/mockinfo/add', 1, '192.168.5.199', '本地局域网', 498, 1, 2, '2025-03-28 14:37:19.642', '\r\n### Error updating database.  Cause: java.sql.SQLException: Field \'mockjs_flag\' doesn\'t have a default value\r\n### The error may exist in org/tinycloud/tinymock/modules/mapper/MockInfoMapper.java (best guess)\r\n### The error may involve org.tinycloud.tinymock.modules.mapper.MockInfoMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO t_mock_info  ( id, tenant_id, project_id, mock_name, method, json_data,  url, status, del_flag,   remark,  http_code )  VALUES (  ?, ?, ?, ?, ?, ?,  ?, ?, ?,   ?,  ?  )\r\n### Cause: java.sql.SQLException: Field \'mockjs_flag\' doesn\'t have a default value\n; Field \'mockjs_flag\' doesn\'t have a default value', '{\"projectId\":1729067620457848832,\"mockName\":\"测试接口5\",\"url\":\"/test5\",\"method\":\"GET\",\"jsonData\":\"{}\",\"remark\":\"\",\"httpCode\":200}', NULL, 2212121, '2025-03-28 14:37:20', 0, '09d255b7a233db75b66cbc20cd7d858fd7e7ee4264b0a82b6a193353abfe6b6d');
INSERT INTO `t_operate_log` VALUES (1905548702129745922, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '192.168.5.199', '本地局域网', 1882, 0, 1, '2025-03-28 17:13:06.586', NULL, '{\"username\":\"zhangsan\",\"password\":\"860c71b307ac53fa912c013124b0315758369bc41cfd79c3860cf860306a325f0f38a8052467debdf4a73e07e304b47bcaf228eb318935debd95843954812fcd5dd3a541db25a456cf845844c210d3671c6a6b694f55aa156e4fe50bf7fed6d67b382ac06f9a751cdbecbc938475ad942b4892b42d840c239b2d98b39d93fdea05a69d372d7b06da536e3b16fe96427676017a1cfe8b642f72c2244ac7abd077\",\"captcha\":\"g4cds\",\"uuid\":\"c9a859b432434048b10b6cbc38f9df2c\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3NDMyMzk1ODcsImlhdCI6MTc0MzE1MzE4NywidG9rZW4iOiI0YmM3MmEyNzg2NWE0NWRiOGFlMmEzZTZiYjZmYzZiZCJ9.jvLBYjOMOC9jsWrnoL3JqnHKzcTKMgA3Y6bHK225SXc\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1743153188366}', 1, '2025-03-28 17:13:08', 0, 'be052959657da991f4a9870f683c30687c5b14947651de55bcf3cf5d061f6088');
INSERT INTO `t_operate_log` VALUES (1905549329387155458, '10014', '新增接口', 'org.tinycloud.tinymock.modules.web.MockInfoController.add()', 'POST', '/mockinfo/add', 0, '192.168.5.199', '本地局域网', 452, 1, 2, '2025-03-28 17:15:37.401', NULL, '{\"projectId\":1729067620457848832,\"mockName\":\"测试接口5\",\"url\":\"/test5\",\"method\":\"GET\",\"jsonData\":\"{}\",\"mockjsFlag\":0,\"remark\":\"\",\"httpCode\":200}', '{\"code\":0,\"data\":true,\"msg\":\"新增成功!\",\"time\":1743153337451}', 2212121, '2025-03-28 17:15:38', 0, '4e364c7e6012b10cd0fb11853d53f5a4afc0aafa01f3943c6f9e28aaabae3d65');
INSERT INTO `t_operate_log` VALUES (1905549456034164737, '10014', '新增接口', 'org.tinycloud.tinymock.modules.web.MockInfoController.add()', 'POST', '/mockinfo/add', 0, '192.168.5.199', '本地局域网', 169, 1, 2, '2025-03-28 17:16:08.205', NULL, '{\"projectId\":1729067620457848832,\"mockName\":\"测试接口6\",\"url\":\"/test6\",\"method\":\"GET\",\"jsonData\":\"{}\",\"mockjsFlag\":1,\"remark\":\"\",\"httpCode\":200}', '{\"code\":0,\"data\":true,\"msg\":\"新增成功!\",\"time\":1743153368238}', 2212121, '2025-03-28 17:16:08', 0, '0becf8d23ead5413bf63a3c4c7ff92e06644a09a22a354f80abf862568232a1f');
INSERT INTO `t_operate_log` VALUES (1905550242659102722, '10015', '修改接口', 'org.tinycloud.tinymock.modules.web.MockInfoController.edit()', 'POST', '/mockinfo/edit', 0, '192.168.5.199', '本地局域网', 303, 2, 2, '2025-03-28 17:19:15.601', NULL, '{\"id\":1905549455363076097,\"mockName\":\"测试接口6\",\"url\":\"/test6\",\"method\":\"GET\",\"jsonData\":\"{}\",\"mockjsFlag\":0,\"remark\":\"\",\"httpCode\":200}', '{\"code\":0,\"data\":true,\"msg\":\"修改成功!\",\"time\":1743153555700}', 2212121, '2025-03-28 17:19:15', 0, '6524bc8f4973f821423095f91d7f3510e6af7f31c04f315133a1c3e0f99f63e7');
INSERT INTO `t_operate_log` VALUES (1905550282995724290, '10015', '修改接口', 'org.tinycloud.tinymock.modules.web.MockInfoController.edit()', 'POST', '/mockinfo/edit', 0, '192.168.5.199', '本地局域网', 272, 2, 2, '2025-03-28 17:19:25.242', NULL, '{\"id\":1905549326245621762,\"mockName\":\"测试接口5\",\"url\":\"/test5\",\"method\":\"POST\",\"jsonData\":\"{}\",\"mockjsFlag\":1,\"remark\":\"\",\"httpCode\":200}', '{\"code\":0,\"data\":true,\"msg\":\"修改成功!\",\"time\":1743153565306}', 2212121, '2025-03-28 17:19:25', 0, '9b232369a45ab31a8132f7bdd89305eadab7304032f06d46119ad2dd62ba65da');
INSERT INTO `t_operate_log` VALUES (1906989893757095937, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '100.100.100.111', '保留地址', 3435, 0, 1, '2025-04-01 16:39:52.004', NULL, '{\"username\":\"zhangsan\",\"password\":\"2687ea880c79235d6f1b1103209caab21b0637bc8856835081f0cdb40b476463a1e2b8da6864503c0d3752e2b907b216d22e87da3678e66437ac25629e6c1d7b6267a5c2f9fced3658f5dc2eb3e547f87420895736c48d4af9d5c2a0e8c0e4fadf794797b3e81732d0974e48834ad300fcd1e9070d452c8c37fd90c0783f49cd5da63f2e60553770e0fefed784bfec195c8097dce9783487bef440265db2ce1d\",\"captcha\":\"v2cs2\",\"uuid\":\"99f06e7b6f194c3fa3694a4bbc7c173a\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3NDM1ODMxOTQsImlhdCI6MTc0MzQ5Njc5NCwidG9rZW4iOiIzMGFhZTY3YjY3NDQ0ZmI1YjRlNGFjZmExMDAzNDA0MCJ9.lRIq5ccdVl13RbN1rafjs2nhd-wIo3_ZrezGixWfdFg\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1743496795322}', 1, '2025-04-01 16:39:55', 0, 'f86ea3dc0eb7c10387cb958c94c8623b6dc52ccca234774717f98c8aa4247a4e');
INSERT INTO `t_operate_log` VALUES (1909168376286068738, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '100.100.100.111', '保留地址', 3273, 0, 1, '2025-04-07 16:56:22.892', NULL, '{\"username\":\"zhangsan\",\"password\":\"c6285d8a57c9c942df0f5dc41056c53e994087e398f694e6a0a1cb64d82300a0005ceb020b34539501c1d589d00200181bf57493524597e78f304e1f37602e32e5c76d4d45db98a84370af25718fd94659a67f750bfaa4d80d5cbf17690cfe0f43d6312fef77111ae80a494fc358a5d3fc9cda9fe60e200e691fe830a3b03c1a633f8d9ad8d24f1ea84ddc63dd0ec47740697b87c5da44210c34585fd545870f\",\"captcha\":\"ernqm\",\"uuid\":\"69bb68a5967941ac9a6f6bebb0be1cf0\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3NDQxMDI1ODUsImlhdCI6MTc0NDAxNjE4NSwidG9rZW4iOiI1OTgxMmFkMzlkN2E0YWI3ODZiMWMxYTYxZmIwN2VkZSJ9.6sVlKqQp0NPwBQrAxkfuTblJ3oN7ggn0fzmiQI6WsMc\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1744016186026}', 1, '2025-04-07 16:56:26', 0, '9f38e84cc20c2ef5d7785b5ec3fc2630aad7bbc51413fd452e3807bcfc95cd61');
INSERT INTO `t_operate_log` VALUES (1909510850302033922, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '100.100.100.111', '保留地址', 1831, 0, 1, '2025-04-08 15:37:16.520', NULL, '{\"username\":\"zhangsan\",\"password\":\"4858a0200a8927e8051fc42ca09479830e3b56675354519733e5faf84536751b71aea2c2c4b67746cba6a2cf94b2ca5b27cda96d5af968deb23c0620d4377d0f902bbf32dcdc12b8a5d8d0e4ebdbc4464498efcffb41a314cbd8593268314962bc8c1f91ee4b677a5e851879a341fd302f8d98e3eeac8246b90940ec03e9afc28c07e69284090d073c2cd81efb8e8ff4d29d84de8772b983d34a5c22787996c6\",\"captcha\":\"uvyws\",\"uuid\":\"73ec969c357041b9b65bb8f3d895f6ed\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3NDQxODQyMzcsImlhdCI6MTc0NDA5NzgzNywidG9rZW4iOiJjZWQxMzVmZTEwZTA0ODM4YTQzZDA5M2UzNWFmYzM2OCJ9.yIpzAPDjuVRCidSo0LyZuBUXanbVuFJzlztGjwjMELo\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1744097838262}', 1, '2025-04-08 15:37:18', 0, '89964d43890bb9c1c1d6320b84423924108f6b097e6392ce97bff6fbe8668324');
INSERT INTO `t_operate_log` VALUES (1909546538783326210, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '100.100.100.111', '保留地址', 641, 0, 1, '2025-04-08 17:59:06.494', NULL, '{\"username\":\"zhangsan\",\"password\":\"7a198fe1e3c08934403cc5a0f0d6181df11cce034d27c574e16cba7e0afcfab57639fe340f17293a4e5947ac023007e1d8896864e7475eb015058acd11030b80157b01e799205f536ba300f86da5b7764e22224d287c74dc1effe430a49df1698f9216c79b2eaf74474382a5095447f0e615dd898bdcfa990a77a24cbce5b77dd7eff3fa4c4b97cbf8c04257d651afea5dc39947e8e84567a0771fbcdc1ad2d5\",\"captcha\":\"wzv5e\",\"uuid\":\"770550beb2984f21af92c44e1ea286ad\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3NDQxOTI3NDYsImlhdCI6MTc0NDEwNjM0NiwidG9rZW4iOiJiYWRhMDM1NzYyYzc0YzM0OWFjZTE4ZmFkYzM0Y2YyNCJ9.CEcy_y9EVoKLIcCNMYGHCglWxIkpSdjKm3bCyM3P43w\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1744106347095}', 1, '2025-04-08 17:59:07', 0, '82e84c9049adc9639a5bb7c3ed9f225388371e61a1df5bae22b224d23166bf10');
INSERT INTO `t_operate_log` VALUES (1909806131456622593, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '192.168.5.10', '本地局域网', 1255, 0, 1, '2025-04-09 11:10:37.612', NULL, '{\"username\":\"zhangsan\",\"password\":\"7fec2039a065ec83e9c7678ee54cba938717a39a504cdfc995a47325a31c8dffe3617c079fa9c2fc4a28af375c6596df939adc0959a9a5ef58659ffb714d2506a759d9915a34e2713c12e14b82b891f84a331ac54e624d86c9f5ec7e2cc38148219ec74335b60aaf89009e618f1af21859abc7e5180c890be6d29ad17e5b561fe190984c107bd5f4dd9b3624d66f37539c8c36720d5027fdc485f5c4411adaf7\",\"captcha\":\"6avdc\",\"uuid\":\"e346d77dd6ad41c0b702ed8286628020\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3NDQyNTQ2MzgsImlhdCI6MTc0NDE2ODIzOCwidG9rZW4iOiJmNDMxNTQxY2U3OTI0M2VhOWYyMmI4MDk5ZmJmMGE5MiJ9.eGOb42sXusHFTyYa5QHxnULIt8htEgbYFeJuqfPYdEo\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1744168238802}', 1, '2025-04-09 11:10:38', 0, '2fae5b7b2f517167d363785854e04ff53ee79bb245f7e765357585ce52d94b27');
INSERT INTO `t_operate_log` VALUES (1909806487674667010, '20002', '租户退出登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.logout()', 'GET', '/auth/logout', 0, '192.168.5.10', '本地局域网', 187, 0, 2, '2025-04-09 11:12:03.616', NULL, '{\"_\":\"1744168240240\"}', '{\"code\":0,\"data\":true,\"msg\":\"退出登录成功！\",\"time\":1744168323618}', 2212121, '2025-04-09 11:12:03', 0, '9f675683c0a2a7dfff1fb2787bd1f1574afbbdebf0d3f7bb700c00988d4d25dc');
INSERT INTO `t_operate_log` VALUES (1909806636639567873, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 1, '192.168.5.10', '本地局域网', 169, 0, 1, '2025-04-09 11:12:39.162', '验证码错误或已过期！', '{\"username\":\"zhangsan222222222222222222222222222222222222222222222222222\",\"password\":\"72b6bfe30a461ecdcb2b90b4fa7de0e76a0477bb82b2303ae71a79d8fa25d6c65a98fa1f6a7b1bb6449e8c447fbda0d1c4756c9ed960139bd998329f1cd560199cc94ae87ea971d1035abaac1ebb906265023e5ebd5bd214a66a08b01ee900823c78f7d68d43eb326e5059b29bb244f34a1643d5491625986faa3cc03c47f93c5362e59ddb39ad7af4d1c9d3b6c155f935c00fabd7b42a1c5b341a2e71eb4260\",\"captcha\":\"2222\",\"uuid\":\"30d636dd10bd46acbe85d3dfcae3d160\"}', NULL, 1, '2025-04-09 11:12:39', 0, '3760b0bd780ca9139e0aecef37da18266d6fd1a16fce6a54f961efd905604a5c');
INSERT INTO `t_operate_log` VALUES (1909807032279875585, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 1, '192.168.5.10', '本地局域网', 184, 0, 1, '2025-04-09 11:14:13.471', '验证码错误或已过期！', '{\"username\":\"zhangsan\",\"password\":\"51839eb5c59c1eb3b3bd85e03abfd237e291f2f6c883645cb5bd17e1bf5dce05165b356af1fc29d237acfc9fd5a33a49c3509069d660082611da200d2b9016e6497353748e7e40acb0b175dfccc864904f09966f1106affa7ef1897b52d8f5a489396a0640172a194d4115fd64b681ae0dd208f33abf94e5245d0fbdd3a012f653ed577baf3ec26b111967cb9eb8cc3fbc3bb8f896751d2eef160053e121dea7\",\"captcha\":\"dsdsd\",\"uuid\":\"5707f61d777e49b6ae43b20749d25b8d\"}', NULL, 1, '2025-04-09 11:14:13', 0, 'e9ad096cedeb367956c182e2426452c0b5e9b9eb3883922c2a673aee9483a756');
INSERT INTO `t_operate_log` VALUES (1909812124320694274, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '192.168.5.10', '本地局域网', 2811, 0, 1, '2025-04-09 11:34:24.857', NULL, '{\"username\":\"zhangsan\",\"password\":\"e995e71b87b8a37019682791cebb0487a579abb2cfa48b2b8c79a5ef28b1b85242c05b221b26d28a87301a21dd6879b46b354d559f0452891d3ffd2be6bd3a63cb130c09ed797de4dfcdb487b5802a3ea2fe2d0c7c015db221e052dfb95f57e67822cb5de4bded0dd28d82c52cdd6752f8d6a3ac3cab9aeda0cfc4b1961f9ca880a51bddd92d127c80e2f11a6491bf150f228e1eb30276fefddd307664dac722\",\"captcha\":\"snydw\",\"uuid\":\"42cac0689baa489686c3e318f24ca655\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3NDQyNTYwNjYsImlhdCI6MTc0NDE2OTY2NiwidG9rZW4iOiJhOGU4NTE0MjExNzc0ZjUxYmEwZTY4MjEwMGI4NTFjOSJ9.-L6PH25zO3RmHts7UbuFe8nC0hapb5evox4sIg6Amc8\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1744169667560}', 1, '2025-04-09 11:34:27', 0, '8795a3a226fd4cf903187939dd6b4364f77b59974066ee3f709b7bd44605b22f');
INSERT INTO `t_operate_log` VALUES (1909815410865864706, '10015', '修改接口', 'org.tinycloud.tinymock.modules.web.MockInfoController.edit()', 'POST', '/mockinfo/edit', 0, '192.168.5.10', '本地局域网', 467, 2, 2, '2025-04-09 11:47:30.427', NULL, '{\"id\":1,\"mockName\":\"测试接口1\",\"url\":\"/inter1/append\",\"method\":\"GET\",\"jsonData\":\"{\\n  \\\"sites\\\": {\\n    \\\"site\\\": [\\n      {\\n        \\\"number|1-100\\\": 100,\\n        \\\"name\\\": \\\"@name\\\",\\n        \\\"url\\\": \\\"@email\\\"\\n      },\\n      {\\n        \\\"id\\\": \\\"@integer(10, 30)\\\",\\n        \\\"name\\\": \\\"@name\\\",\\n        \\\"url\\\": \\\"@email\\\"\\n      },\\n      {\\n        \\\"id\\\": \\\"3\\\",\\n        \\\"name\\\": \\\"@name\\\",\\n        \\\"url\\\": \\\"@now\\\"\\n      }\\n    ]\\n  }\\n}\",\"delay\":4000,\"mockjsFlag\":0,\"remark\":\"\",\"httpCode\":200}', '{\"code\":0,\"data\":true,\"msg\":\"修改成功!\",\"time\":1744170450482}', 2212121, '2025-04-09 11:47:31', 0, '4b0c62143d6968afe78a81c7de13f0bebeded6976e08056d0d6e2fc385483c60');
INSERT INTO `t_operate_log` VALUES (1909905589882527745, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '192.168.5.10', '本地局域网', 2665, 0, 1, '2025-04-09 17:45:48.925', NULL, '{\"username\":\"zhangsan\",\"password\":\"d40456d777fd14bb224c97ff87060b217eab2d26c6cdecf2d0545232481d2edcd165db17ad456bd3f02d78fd6c1ff392640fd2a707335d080e4585c7726b569cbfabf42fb614383dbb78df8a9ceba991cbdc1261c5ae241389b7123789e5b9b26106a370763c665b88546714d2c1176981fc96d6d622960a152ea92782f44288c8058f505153303f7c923e8cdd67c81930bac0b085474f73c334263a64a078ac\",\"captcha\":\"4f6wk\",\"uuid\":\"73788c7db3bf47209392aa6248b43371\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3NDQyNzgzNTAsImlhdCI6MTc0NDE5MTk1MCwidG9rZW4iOiJlZjNmNTc4ODU4YTI0MDA1ODhjMmY0NjQyOGMxNDgyYyJ9.YXQLSH3B6R6fOeL-ve2y3UP0hQbLOwwNOCKVvhJzVlY\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1744191951475}', 1, '2025-04-09 17:45:51', 0, '35d49102fbe914e5bcfb4c5aa91d5b3980a8955ec314d61cd6fda0335fc9b29c');
INSERT INTO `t_operate_log` VALUES (1910144819361841154, '20001', '租户登录', 'org.tinycloud.tinymock.modules.web.TenantAuthController.login()', 'POST', '/auth/login', 0, '192.168.5.10', '本地局域网', 1732, 0, 1, '2025-04-10 09:36:26.617', NULL, '{\"username\":\"zhangsan\",\"password\":\"e7c4ce0f9205c69448b73aad1da9836dfd647e8f1cacf37b9c4e2bfee917224362d2762a92a4daaefad068e035355c442ae6f0a8118753261c99a4a6e011c42a6470a2d917a79f5f95580b8f41817f61dc8a0d6a3df90e0133f2de9fbbba806cd87983c9c15c9c1e57e9f0d148d887817af095f32d0ef39674642519ddaaba1d5b4eabce40b8cca756c8cc6a09e87e7a7325bd8c2fa1065568cf3a7a6d016c97\",\"captcha\":\"4yjaq\",\"uuid\":\"5ed7fac870b04683bf374cd228446b17\"}', '{\"code\":0,\"data\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW55LW1vY2siLCJleHAiOjE3NDQzMzUzODcsImlhdCI6MTc0NDI0ODk4NywidG9rZW4iOiI3NmU2MGUyOGMwYzI0MzY0OGQ1MzUwNmEwYWNjNTdlNSJ9.xcdUkjQGku4Vm3BfhHVucNTkry0JW_pqm9JQsMvVZ4Q\",\"msg\":\"登录成功，欢迎回来！\",\"time\":1744248988267}', 1, '2025-04-10 09:36:28', 0, 'da4dd2b85c2ba0688dcbe4bd2646082aea15456724f4e65e73d9d6a4e776a464');

-- ----------------------------
-- Table structure for t_project_info
-- ----------------------------
DROP TABLE IF EXISTS `t_project_info`;
CREATE TABLE `t_project_info`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  `project_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目名称',
  `introduce` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目介绍',
  `path` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目链接',
  `status` tinyint(4) NOT NULL COMMENT '状态标志（0--启用1--禁用）',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标志（0--未删除1--已删除）',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `t_project_info_project_name_unique`(`project_name`) USING BTREE COMMENT '唯一索引，project_name不允许重复'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_project_info
-- ----------------------------
INSERT INTO `t_project_info` VALUES (1729067620457848832, 2212121, '测试项目', '测试项目1', '/test', 0, 0, '2023-11-28 14:10:01', '2024-03-13 12:03:00', NULL);
INSERT INTO `t_project_info` VALUES (1729067620457848833, 2212121, 'vue-admin-plus', 'vue-admin-plus', '/plus', 0, 0, '2024-03-10 18:34:14', '2024-03-13 12:03:02', NULL);
INSERT INTO `t_project_info` VALUES (1767819694047502338, 2212121, '测试项目2', '', '/test2', 0, 1, '2024-03-13 15:47:14', '2024-03-13 16:05:56', '');
INSERT INTO `t_project_info` VALUES (1767820137268035585, 2212121, '测试项目3', NULL, '/test3', 0, 1, '2024-03-13 15:48:59', '2024-11-06 09:40:45', '');
INSERT INTO `t_project_info` VALUES (2024080000000001905, 2024080000000001698, '李四的项目', '', '/lisi', 0, 0, '2024-03-20 22:06:28', '2024-03-20 22:06:28', '');

-- ----------------------------
-- Table structure for t_project_member
-- ----------------------------
DROP TABLE IF EXISTS `t_project_member`;
CREATE TABLE `t_project_member`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `create_tenant_id` bigint(20) NOT NULL COMMENT '拥有者租户ID',
  `member_tenant_id` bigint(20) NOT NULL COMMENT '协作者租户ID',
  `project_id` bigint(20) NOT NULL COMMENT '项目ID',
  `status` tinyint(4) NOT NULL COMMENT '状态标志（0--启用1--禁用）',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标志（0--未删除1--已删除）',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目成员协作表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_project_member
-- ----------------------------
INSERT INTO `t_project_member` VALUES (1828360277811625986, 2212121, 2024080000000001698, 1729067620457848832, 0, 1, '2024-08-27 17:13:54', '2024-08-27 17:17:44', NULL);
INSERT INTO `t_project_member` VALUES (1828362012600897537, 2212121, 2024080000000001698, 1729067620457848832, 0, 0, '2024-08-27 17:20:47', '2024-08-27 17:20:47', NULL);

-- ----------------------------
-- Table structure for t_tenant
-- ----------------------------
DROP TABLE IF EXISTS `t_tenant`;
CREATE TABLE `t_tenant`  (
  `id` bigint(20) NOT NULL COMMENT '业务主键',
  `tenant_account` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户账号',
  `tenant_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户密码',
  `tenant_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户名称',
  `tenant_phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户电话',
  `tenant_email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户邮箱',
  `status` tinyint(4) NOT NULL COMMENT '状态标志（0--启用1--禁用）',
  `del_flag` tinyint(4) NOT NULL COMMENT '删除标志（0--未删除1--已删除）',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `invitation_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '注册邀请码（8位随机字符）',
  `tenant_avatar` bigint(20) NULL DEFAULT NULL COMMENT '租户头像',
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `password_salt` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户密码盐',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_tenant
-- ----------------------------
INSERT INTO `t_tenant`(`id`, `tenant_account`, `tenant_password`, `tenant_name`, `tenant_phone`, `tenant_email`, `status`, `del_flag`, `created_at`, `invitation_code`, `tenant_avatar`, `updated_at`, `password_salt`) VALUES (2212121, 'zhangsan', '02ec674189cf69fbf65cafe95be168a25e3fb9a2324dfc7e99751100a1111663', '张三', '17862718963', '17862718963@email.com', 0, 0, '2023-12-05 16:33:46', 'AaNs8ROo', 1853985145965862913, '2025-05-08 11:29:47', '289a3aecd27d429d93982e5da14feece');
INSERT INTO `t_tenant`(`id`, `tenant_account`, `tenant_password`, `tenant_name`, `tenant_phone`, `tenant_email`, `status`, `del_flag`, `created_at`, `invitation_code`, `tenant_avatar`, `updated_at`, `password_salt`) VALUES (2024080000000001698, 'lisi', '060519b8108a57db0aa4f6e6a3ef9281a0fcd020a29b4e585214fa0a2f8a1682', '李四', NULL, 'leisure01@aliyun.com', 0, 0, '2024-03-20 22:05:28', NULL, NULL, '2025-05-08 11:30:05', '21aac4945e4046bcac602b14cb81b8ec');

-- ----------------------------
-- Table structure for t_upload_file
-- ----------------------------
DROP TABLE IF EXISTS `t_upload_file`;
CREATE TABLE `t_upload_file`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `file_name_old` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件原名称',
  `file_name_new` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件新名称',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件路径',
  `file_size` bigint(20) NULL DEFAULT NULL COMMENT '文件大小（单位B）',
  `file_size_str` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件大小（带单位, 单位KB）',
  `file_suffix` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件后缀',
  `file_md5` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件MD5',
  `file_sha1` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件MD5',
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_upload_file
-- ----------------------------
INSERT INTO `t_upload_file` VALUES (1853976651158052865, '2024年度全国保密教育线上培训证书.png', '7bd93e193b054463a28d9b450e345d7b.png', '/2024/11/06/7bd93e193b054463a28d9b450e345d7b.png', 401709, '392.29KB', 'png', '9bf5e7540f5b94889ade541409b44b5a', '24c860dc488bfbf04c693c0d64e6aa125b125d3f', '2024-11-06 09:44:13');
INSERT INTO `t_upload_file` VALUES (1853979725247356929, '2024年度全国保密教育线上培训证书.png', 'e802ee57dd684be687c45d48fbfa0c54.png', '/2024/11/06/e802ee57dd684be687c45d48fbfa0c54.png', 401709, '392.29KB', 'png', '9bf5e7540f5b94889ade541409b44b5a', '24c860dc488bfbf04c693c0d64e6aa125b125d3f', '2024-11-06 09:56:26');
INSERT INTO `t_upload_file` VALUES (1853980323548016641, '微信图片_20231114205857.png', '4e047ae3f4ad453892a7f1b275ca71f6.png', '/2024/11/06/4e047ae3f4ad453892a7f1b275ca71f6.png', 122907, '120.03KB', 'png', '8d1711964e217fe5eaa459de8fc6a3a6', '18e8a5a10a273d464277c5b349a0c9602be79445', '2024-11-06 09:58:49');
INSERT INTO `t_upload_file` VALUES (1853980568042352641, '火车票1.jpg', '72db1765a8464b2dbaa7938f35f61fa5.jpg', '/2024/11/06/72db1765a8464b2dbaa7938f35f61fa5.jpg', 707918, '691.33KB', 'jpg', '308804c8b3697bb1f6ad4ea0a50170a9', '412fbedbcd5db2f5cb4b353a648f02be6cd8c06c', '2024-11-06 09:59:48');
INSERT INTO `t_upload_file` VALUES (1853980970263580673, '微信图片_20241021143537.jpg', '8e2cd566f4ec467d88716b9ff3de0a48.jpg', '/2024/11/06/8e2cd566f4ec467d88716b9ff3de0a48.jpg', 106319, '103.83KB', 'jpg', 'a853b6473f0a16aa8b6e9ed0119de89c', '2dde72ad7f3e9e4e795445de1a7095f54d88ec74', '2024-11-06 10:01:23');
INSERT INTO `t_upload_file` VALUES (1853985145965862913, 'bpm审批截图.png', 'eb0ab31a6b64489eaccda78ef7e2be99.png', '/2024/11/06/eb0ab31a6b64489eaccda78ef7e2be99.png', 175739, '171.62KB', 'png', '71e4dc5102c97932558e2c3b72c5bdbe', '4c31327f4c4d8ffd059d475b714d03bc8a72e21c', '2024-11-06 10:17:59');
INSERT INTO `t_upload_file` VALUES (2024085000000001680, '仪表盘.png', '001ce6104db44d41950a3168ce286d83.png', '/2024/03/25/001ce6104db44d41950a3168ce286d83.png', 175945, '171.82KB', 'png', '68b2878bb21cbb0059d1305c82051fb3', '68b2878bb21cbb0059d1305c82051fb3', '2024-03-25 17:00:24');
INSERT INTO `t_upload_file` VALUES (2024085000000002303, 'logo.png', '806f75c72bb4431288bad488aaed7bd2.png', '/2024/03/25/806f75c72bb4431288bad488aaed7bd2.png', 782, '782.0B', 'png', '92cb91002497ad53d8d607b6aac11f97', '92cb91002497ad53d8d607b6aac11f97', '2024-03-25 17:29:39');
INSERT INTO `t_upload_file` VALUES (2024085000000003464, 'logo.png', 'be9cf417ba1143bd862e663e7b7b59d0.png', '/2024/03/25/be9cf417ba1143bd862e663e7b7b59d0.png', 782, '782.0B', 'png', '92cb91002497ad53d8d607b6aac11f97', '92cb91002497ad53d8d607b6aac11f97', '2024-03-25 17:34:34');
INSERT INTO `t_upload_file` VALUES (2024085000000004865, 'logo.png', '548c6a2d4c214f13810446ac0b441bf6.png', '/2024/03/25/548c6a2d4c214f13810446ac0b441bf6.png', 782, '782.0B', 'png', '92cb91002497ad53d8d607b6aac11f97', '92cb91002497ad53d8d607b6aac11f97', '2024-03-25 17:37:27');
INSERT INTO `t_upload_file` VALUES (2024085000000005822, 'questionMark.png', 'e2ff0ee676ec4d439abeaa3b999b2c5b.png', '/2024/03/25/e2ff0ee676ec4d439abeaa3b999b2c5b.png', 6533, '6.38KB', 'png', '30dc5f5c5505cd9caf4f4a10b757e80c', '30dc5f5c5505cd9caf4f4a10b757e80c', '2024-03-25 17:37:31');

SET FOREIGN_KEY_CHECKS = 1;
