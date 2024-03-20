/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : localhost:3306
 Source Schema         : tiny-mock

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 20/03/2024 22:08:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `t_ur_map_surl_unique`(`content`) USING BTREE COMMENT '唯一索引，surl不允许重复'
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
  UNIQUE INDEX `t_ur_map_surl_unique`(`invitation_code`) USING BTREE COMMENT '唯一索引，surl不允许重复'
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
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注描述信息',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL COMMENT '创建人-对应t_user.id',
  `updated_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人-对应t_user.id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统邮箱配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_mail_config
-- ----------------------------

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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_mock_info
-- ----------------------------
INSERT INTO `t_mock_info` VALUES (1, 2212121, 1729067620457848832, '测试接口1', 'GET', '{\n  \"sites\": {\n    \"site\": [\n      {\n        \"number|1-100\": 100,\n        \"name\": \"@name\",\n        \"url\": \"@email\"\n      },\n      {\n        \"id\": \"@integer(10, 30)\",\n        \"name\": \"@name\",\n        \"url\": \"@email\"\n      },\n      {\n        \"id\": \"3\",\n        \"name\": \"@name\",\n        \"url\": \"@now\"\n      }\n    ]\n  }\n}', 0, '/inter1/append', 0, 0, '2024-03-09 19:12:46', '2024-03-15 15:30:53', NULL, 4000);
INSERT INTO `t_mock_info` VALUES (1768538350125629441, 2212121, 1729067620457848832, '登录接口', 'POST', '{\"data\":{\"token\":\"Admin-DwCfwXoeaNoTTx1tJEBbGP7GJbT5uy\"},\"code\":200,\"message\":\"登录成功\"}', 0, '/login', 0, 0, '2024-03-15 15:22:55', '2024-03-15 15:30:21', 'XXXX', 200);
INSERT INTO `t_mock_info` VALUES (1768544377218842626, 2212121, 1729067620457848832, '获取省份', 'POST', '{\"object|2\":{\"310000\":\"上海市\",\"320000\":\"江苏省\",\"330000\":\"浙江省\",\"340000\":\"安徽省\"}}', 0, '/province', 0, 0, '2024-03-15 15:46:52', '2024-03-15 22:26:19', 'YYYY', 333);

-- ----------------------------
-- Table structure for t_mock_info_history
-- ----------------------------
DROP TABLE IF EXISTS `t_mock_info_history`;
CREATE TABLE `t_mock_info_history`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `mock_id` bigint(20) NOT NULL COMMENT '接口ID',
  `version` int(11) NOT NULL COMMENT '历史版本号，从1开始，每次加1',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  `project_id` bigint(20) NOT NULL COMMENT '项目ID',
  `mock_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口名称',
  `method` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'GET,HEAD,POST,PUT, PATCH, DELETE,OPTIONS,TRACE;',
  `json_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口数据',
  `mockjs_flag` tinyint(4) NOT NULL COMMENT '是否开启mockjs模拟随机数据，0开启，1不开启',
  `url` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `delay` bigint(20) NULL DEFAULT NULL COMMENT '返回延时（单位毫秒）',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '历史记录创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '接口历史版本表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_mock_info_history
-- ----------------------------
INSERT INTO `t_mock_info_history` VALUES (1768639055106686978, 1768544377218842626, 1, 2212121, 1729067620457848832, '获取省', 'POST', '{\n  \"object|2\": {\n    \"310000\": \"上海市\",\n    \"320000\": \"江苏省\",\n    \"330000\": \"浙江省\",\n    \"340000\": \"安徽省\"\n  }\n}', 0, '/province', '', 200, '2024-03-15 22:03:05');
INSERT INTO `t_mock_info_history` VALUES (1768644872535121922, 1768544377218842626, 2, 2212121, 1729067620457848832, '获取省', 'POST', '{\"object|2\":{\"310000\":\"上海市\",\"320000\":\"江苏省\",\"330000\":\"浙江省\",\"340000\":\"安徽省\"}}', 0, '/province', 'YYYY', 2000, '2024-03-15 22:26:12');
INSERT INTO `t_mock_info_history` VALUES (1768644902046244865, 1768544377218842626, 3, 2212121, 1729067620457848832, '获取省份', 'POST', '{\"object|2\":{\"310000\":\"上海市\",\"320000\":\"江苏省\",\"330000\":\"浙江省\",\"340000\":\"安徽省\"}}', 0, '/province', 'YYYY', 222, '2024-03-15 22:26:19');

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
  UNIQUE INDEX `t_ur_map_surl_unique`(`project_name`) USING BTREE COMMENT '唯一索引，surl不允许重复'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_project_info
-- ----------------------------
INSERT INTO `t_project_info` VALUES (1729067620457848832, 2212121, '测试项目', '测试项目1', '/test', 0, 0, '2023-11-28 14:10:01', '2024-03-13 12:03:00', NULL);
INSERT INTO `t_project_info` VALUES (1729067620457848833, 2212121, 'vue-admin-plus', 'vue-admin-plus', '/plus', 0, 0, '2024-03-10 18:34:14', '2024-03-13 12:03:02', NULL);
INSERT INTO `t_project_info` VALUES (1767819694047502338, 2212121, '测试项目2', '', '/test2', 0, 1, '2024-03-13 15:47:14', '2024-03-13 16:05:56', '');
INSERT INTO `t_project_info` VALUES (1767820137268035585, 2212121, '测试项目3', NULL, '/test3', 0, 0, '2024-03-13 15:48:59', '2024-03-13 15:48:59', '');
INSERT INTO `t_project_info` VALUES (2024080000000001905, 2024080000000001698, '李四的项目', '', '/lisi', 0, 0, '2024-03-20 22:06:28', '2024-03-20 22:06:28', '');

-- ----------------------------
-- Table structure for t_seq
-- ----------------------------
DROP TABLE IF EXISTS `t_seq`;
CREATE TABLE `t_seq`  (
  `seq_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `seq_value` bigint(20) NOT NULL COMMENT '当前值',
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`seq_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_seq
-- ----------------------------
INSERT INTO `t_seq` VALUES ('2024066_hhhh', 1100, '2024-03-19 13:48:50', '2024-03-19 13:48:50');
INSERT INTO `t_seq` VALUES ('2024078_t_url_map', 1, '2024-03-19 13:48:50', '2024-03-19 13:48:50');
INSERT INTO `t_seq` VALUES ('2024079_t_feedback', 3, '2024-03-19 16:03:51', '2024-03-19 16:03:51');

-- ----------------------------
-- Table structure for t_seq_copy1
-- ----------------------------
DROP TABLE IF EXISTS `t_seq_copy1`;
CREATE TABLE `t_seq_copy1`  (
  `seq_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `seq_value` bigint(20) NOT NULL COMMENT '当前值',
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`seq_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
  `tenant_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户头像',
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_tenant
-- ----------------------------
INSERT INTO `t_tenant` VALUES (2212121, 'zhangsan', 'e960be68749241c3e3562094239e28e2b4482e9d28c65413bbe2f9bd0419cdb8', '张三', '17862718963', '17862718963@email.com', 0, 0, '2023-12-05 16:33:46', 'AaNs8ROo', NULL, '2024-03-20 17:03:02');
INSERT INTO `t_tenant` VALUES (2024080000000001698, 'lisi', 'e960be68749241c3e3562094239e28e2b4482e9d28c65413bbe2f9bd0419cdb8', '李四', NULL, 'leisure01@aliyun.com', 0, 0, '2024-03-20 22:05:28', NULL, NULL, '2024-03-20 22:05:28');

SET FOREIGN_KEY_CHECKS = 1;
