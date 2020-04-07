/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : localhost:3306
 Source Schema         : demo

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 01/12/2019 11:08:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth2_access_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_access_token`;
CREATE TABLE `oauth2_access_token`  (
  `access_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `expire_in` int(8) NULL DEFAULT NULL,
  `refresh_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `refresh_token_create_time` datetime(0) NULL DEFAULT NULL,
  `access_token_create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`access_token`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oauth2_authorization_code
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization_code`;
CREATE TABLE `oauth2_authorization_code`  (
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `redirect_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `response_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oauth2_authorization_code
-- ----------------------------
INSERT INTO `oauth2_authorization_code` VALUES ('8a429544bd7d4aa9bcf1e8fd9244a9b3112142', '3de80c9f4b926a157c67b2c113e7dfaa', 'app_1', 'http://www.baidu.com', 'user_info', '123', 'code', '2019-12-01 11:07:33');

-- ----------------------------
-- Table structure for oauth2_client
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_client`;
CREATE TABLE `oauth2_client`  (
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `client_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `client_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `scope` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `status` bit(1) NULL DEFAULT NULL,
  `redirect_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `grant_types` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `owner_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oauth2_client
-- ----------------------------
INSERT INTO `oauth2_client` VALUES ('app_1', 'abc', '测试', 'user_info', b'1', 'http://www.baidu.com', 'authorization_code', '1');

-- ----------------------------
-- Table structure for oauth2_permission
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_permission`;
CREATE TABLE `oauth2_permission`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oauth2_permission
-- ----------------------------
INSERT INTO `oauth2_permission` VALUES ('user_info', '昵称、性别', 'get', 'http://www.baidu.com/oauth/user_info', 'user_info');

-- ----------------------------
-- Table structure for oauth2_user
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_user`;
CREATE TABLE `oauth2_user`  (
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oauth2_user
-- ----------------------------
INSERT INTO `oauth2_user` VALUES ('3de80c9f4b926a157c67b2c113e7dfaa', 'tohka', '123456');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `text` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '0', '主导航', NULL, NULL, '2019-10-18 23:28:52', NULL);
INSERT INTO `sys_menu` VALUES ('10', '6', '菜单管理', NULL, '/sys/menu', '2019-10-18 23:29:30', NULL);
INSERT INTO `sys_menu` VALUES ('2', '1', '仪表盘', 'anticon-dashboard', NULL, '2019-10-18 23:29:05', NULL);
INSERT INTO `sys_menu` VALUES ('3', '2', '仪表盘V1', NULL, '/dashboard', '2019-10-18 23:29:08', NULL);
INSERT INTO `sys_menu` VALUES ('4', '0', '系统', NULL, NULL, '2019-10-18 23:29:12', NULL);
INSERT INTO `sys_menu` VALUES ('6', '4', '系统管理', 'anticon-setting', NULL, '2019-10-18 23:29:18', NULL);
INSERT INTO `sys_menu` VALUES ('7', '6', '用户管理', NULL, '/sys/user', '2019-10-18 23:29:21', NULL);
INSERT INTO `sys_menu` VALUES ('8', '6', '角色管理', NULL, '/sys/role', '2019-10-18 23:29:24', NULL);
INSERT INTO `sys_menu` VALUES ('9', '6', '权限管理', NULL, '/sys/permission', '2019-10-18 23:29:27', NULL);

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父权限id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限名',
  `level` tinyint(1) NULL DEFAULT NULL COMMENT '权限级别 0,1,2 数值越小级别越高',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'url',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求method',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('1', '0', '权限管理', 1, NULL, NULL, '2019-10-18 23:32:02', NULL);
INSERT INTO `sys_permission` VALUES ('2', '1', '权限添加', 2, '/api/v1/sysPermissions', 'post', '2019-10-18 23:32:02', NULL);
INSERT INTO `sys_permission` VALUES ('3', '1', '权限修改', 2, '/api/v1/sysPermissions', 'put', '2019-10-18 23:32:02', NULL);
INSERT INTO `sys_permission` VALUES ('4', '1', '权限删除', 2, '/api/v1/sysPermissions', 'delete', '2019-10-18 23:32:02', NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('2822c9119f5945a38f206762ec155d31', '学生', '2019-10-18 23:32:11', NULL);
INSERT INTO `sys_role` VALUES ('4233c4a38e0ca917157824fdb070c5e0', '辅导员', '2019-10-18 23:32:11', NULL);
INSERT INTO `sys_role` VALUES ('8bea6899aebd9664e6f5ab359d6153df', '教学管理人员', '2019-10-18 23:32:11', NULL);
INSERT INTO `sys_role` VALUES ('c24551b39eac04939f6228a2ec222583', '系统管理员', '2019-10-18 23:32:11', NULL);
INSERT INTO `sys_role` VALUES ('dace7bce733b804eef5117c69469d3e9', '教务处学籍科', '2019-10-18 23:32:11', NULL);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `sys_role_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sys_menu_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`sys_role_id`, `sys_menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('2822c9119f5945a38f206762ec155d31', '10');
INSERT INTO `sys_role_menu` VALUES ('2822c9119f5945a38f206762ec155d31', '4');
INSERT INTO `sys_role_menu` VALUES ('2822c9119f5945a38f206762ec155d31', '6');
INSERT INTO `sys_role_menu` VALUES ('2822c9119f5945a38f206762ec155d31', '7');
INSERT INTO `sys_role_menu` VALUES ('2822c9119f5945a38f206762ec155d31', '8');
INSERT INTO `sys_role_menu` VALUES ('2822c9119f5945a38f206762ec155d31', '9');
INSERT INTO `sys_role_menu` VALUES ('c24551b39eac04939f6228a2ec222583', '1');
INSERT INTO `sys_role_menu` VALUES ('c24551b39eac04939f6228a2ec222583', '10');
INSERT INTO `sys_role_menu` VALUES ('c24551b39eac04939f6228a2ec222583', '2');
INSERT INTO `sys_role_menu` VALUES ('c24551b39eac04939f6228a2ec222583', '3');
INSERT INTO `sys_role_menu` VALUES ('c24551b39eac04939f6228a2ec222583', '4');
INSERT INTO `sys_role_menu` VALUES ('c24551b39eac04939f6228a2ec222583', '6');
INSERT INTO `sys_role_menu` VALUES ('c24551b39eac04939f6228a2ec222583', '7');
INSERT INTO `sys_role_menu` VALUES ('c24551b39eac04939f6228a2ec222583', '8');
INSERT INTO `sys_role_menu` VALUES ('c24551b39eac04939f6228a2ec222583', '9');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `sys_role_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sys_permission_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`sys_role_id`, `sys_permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('2822c9119f5945a38f206762ec155d31', '2');
INSERT INTO `sys_role_permission` VALUES ('2822c9119f5945a38f206762ec155d31', '3');
INSERT INTO `sys_role_permission` VALUES ('c24551b39eac04939f6228a2ec222583', '1');
INSERT INTO `sys_role_permission` VALUES ('c24551b39eac04939f6228a2ec222583', '2');
INSERT INTO `sys_role_permission` VALUES ('c24551b39eac04939f6228a2ec222583', '3');
INSERT INTO `sys_role_permission` VALUES ('c24551b39eac04939f6228a2ec222583', '4');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `gender` int(2) NULL DEFAULT NULL COMMENT '性别 1-男 2-女 3-未知或保密',
  `password` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `create_at` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `enable` bit(1) NULL DEFAULT NULL COMMENT '是否启用 0-禁用 1-启用',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `phone_index`(`phone`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('3de80c9f4b926a157c67b2c113e7dfaa', 'tohka', 2, '$2a$10$C8QvimBm2nkv4Yr/HlP4eOEJO0RHPXX3JQZZ05OOq285vzw4vVvNm', '2019-03-25 23:04:38', '2019-03-26 00:14:50', '15277605641', '1019288034@qq.com', 'http://localhost:4200/assets/tmp/img/tohka.jpg', NULL, '2019-10-18 23:32:25', NULL);
INSERT INTO `sys_user` VALUES ('5d575f04e0dd8727c969175b35892e60', 'kotori', 2, '$2a$10$Xi6rUJpTTUmD6SbQvQadqOSFaY5n9wUIpjU9XmLI.343T8Vfj2XTS', '2019-03-25 23:04:38', '2019-04-29 21:25:41', '13132662189', '2514542765@qq.com', 'http://localhost:4200/assets/tmp/img/kotori.jpg', NULL, '2019-10-18 23:32:25', NULL);
INSERT INTO `sys_user` VALUES ('d6ece035dd441dce26b9e84b10b8613b', 'kurumi', 2, '$2a$10$C8QvimBm2nkv4Yr/HlP4eOEJO0RHPXX3JQZZ05OOq285vzw4vVvNm', '2019-03-25 23:04:38', '2019-04-29 21:23:55', '17777580731', '1919288034@qq.com', 'http://localhost:4200/assets/tmp/img/kurumi.jpg', NULL, '2019-10-18 23:32:25', NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `sys_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sys_role_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`sys_user_id`, `sys_role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('3de80c9f4b926a157c67b2c113e7dfaa', '2822c9119f5945a38f206762ec155d31');
INSERT INTO `sys_user_role` VALUES ('3de80c9f4b926a157c67b2c113e7dfaa', 'c24551b39eac04939f6228a2ec222583');
INSERT INTO `sys_user_role` VALUES ('5d575f04e0dd8727c969175b35892e60', 'c24551b39eac04939f6228a2ec222583');
INSERT INTO `sys_user_role` VALUES ('d6ece035dd441dce26b9e84b10b8613b', '2822c9119f5945a38f206762ec155d31');

SET FOREIGN_KEY_CHECKS = 1;
