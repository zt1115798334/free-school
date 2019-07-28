/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : free_school

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 17/06/2019 18:19:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE `t_comment`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '用户id',
  `topic_id` bigint(20) NOT NULL COMMENT '主体id',
  `topic_type` tinyint(2) UNSIGNED NOT NULL DEFAULT 1 COMMENT '主体类型：1，交易；2，讯息；3，问答；4，时光',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '评论内容',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_comment_reply
-- ----------------------------
DROP TABLE IF EXISTS `t_comment_reply`;
CREATE TABLE `t_comment_reply`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `comment_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '评论id',
  `reply_type` tinyint(2) UNSIGNED NULL DEFAULT 1 COMMENT '1为回复评论，2为回复别人的回复',
  `reply_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '回复目标id，reply_type为1时，是comment_id，reply_type为2时为回复表的id',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '回复内容',
  `to_user_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '回复目标id',
  `from_user_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '回复用户id',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `comment_id`(`comment_id`) USING BTREE,
  INDEX `from_uid`(`from_user_id`) USING BTREE,
  INDEX `to_uid`(`to_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论回复表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_file_info
-- ----------------------------
DROP TABLE IF EXISTS `t_file_info`;
CREATE TABLE `t_file_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_path` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件路径',
  `file_size` double NOT NULL COMMENT '文件大小',
  `file_md5` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件md5',
  `full_file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '存在服务器的名称',
  `original_file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '原名称 带后缀',
  `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '原名称',
  `suffix_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '后缀名',
  `created_datetime` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 191 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_information
-- ----------------------------
DROP TABLE IF EXISTS `t_information`;
CREATE TABLE `t_information`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '商品描述',
  `integral` int(20) NULL DEFAULT NULL COMMENT '积分',
  `state` tinyint(4) NOT NULL COMMENT '交易状态{inRelease：发布中，newRelease：新发布，afterRelease，发布后；solve:已解决,lowerShelf，下架}',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `delete_state` tinyint(4) NOT NULL COMMENT '删除状态：1已删除 0未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资讯表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_knowing
-- ----------------------------
DROP TABLE IF EXISTS `t_knowing`;
CREATE TABLE `t_knowing`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '商品描述',
  `integral` int(20) NULL DEFAULT NULL COMMENT '积分',
  `state` tinyint(4) NOT NULL COMMENT '交易状态{inRelease：发布中，newRelease：新发布，afterRelease，发布后；solve:已解决,lowerShelf，下架}',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `delete_state` tinyint(4) NOT NULL COMMENT '删除状态：1已删除 0未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资讯表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_record
-- ----------------------------
DROP TABLE IF EXISTS `t_record`;
CREATE TABLE `t_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '商品描述',
  `integral` int(20) NULL DEFAULT NULL COMMENT '积分',
  `state` tinyint(4) NOT NULL COMMENT '交易状态{inRelease：发布中，newRelease：新发布，afterRelease，发布后；solve:已解决,lowerShelf，下架}',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `delete_state` tinyint(4) NOT NULL COMMENT '删除状态：1已删除 0未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_topic_img
-- ----------------------------
DROP TABLE IF EXISTS `t_topic_img`;
CREATE TABLE `t_topic_img`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `topic_id` bigint(20) NOT NULL COMMENT '主体id',
  `topic_type` tinyint(2) UNSIGNED NOT NULL DEFAULT 1 COMMENT '主体类型：1，交易；2，讯息；3，问答；4，时光',
  `img_id` bigint(20) NOT NULL COMMENT '图片id',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `delete_state` tinyint(4) NOT NULL COMMENT '删除状态：1已删除 0未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '主体图片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_transaction
-- ----------------------------
DROP TABLE IF EXISTS `t_transaction`;
CREATE TABLE `t_transaction`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `desc` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '商品描述',
  `state` tinyint(4) NOT NULL COMMENT '交易状态{inRelease：发布中，newRelease：新发布，afterRelease，发布后；solve:已解决,lowerShelf，下架}',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `delete_state` tinyint(4) NOT NULL COMMENT '删除状态：1已删除 0未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '交易表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账户',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `salt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '盐',
  `user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `phone` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `school` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学校',
  `school_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学校地址',
  `account_state` tinyint(4) NOT NULL COMMENT '账户状态：{0：冻结，1正常}',
  `account_type` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账户类型：{admin :管理员用户,ordinary:普通用户,}',
  `sex` tinyint(4) NOT NULL COMMENT '性别：{0:女 1: 男  99: 未知}',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `updated_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `delete_state` tinyint(4) NOT NULL COMMENT '删除状态：1已删除 0未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 99 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (99, '11157983341', 'faeb5d65d2ce9a3e17312ca058d342f663a135ea', '9c5ed56f09c91ba2', '测试', '15130097582', 'zhangtong9498@qq.com', '五道口职业技术学院', '五道口', 1, '2', 0, '2019-06-17 18:18:09', NULL, '2019-06-17 18:19:06', 0);

-- ----------------------------
-- Table structure for t_user_log
-- ----------------------------
DROP TABLE IF EXISTS `t_user_log`;
CREATE TABLE `t_user_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '操作员ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作员姓名',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作类型',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '操作详情',
  `ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP',
  `time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `classify` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类',
  `fun` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方法',
  `response` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '返回值',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_name`(`user_id`) USING BTREE,
  FULLTEXT INDEX `ft_type`(`type`, `ip`, `content`, `name`, `classify`, `fun`)
) ENGINE = InnoDB AUTO_INCREMENT = 5906 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_log
-- ----------------------------
INSERT INTO `t_user_log` VALUES (5906, 99, '测试', 'app登录', '[request:org.apache.shiro.web.servlet.ShiroHttpServletRequest@1c0e2e14 username:11157983341 password:1115798334 registrationId:mobile ]', '127.0.0.1', '2019-06-17 18:19:06', 'com.example.school.app.controller.LoginController', 'login', '{\"data\":{\"accessToken\":\"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMTE1Nzk4MzM0MSIsInVzZXJfaWQiOjk5LCJub25fZXhwaXJlZCI6ZmFsc2UsImV4cCI6MTU2MDg3NDc0NiwiaWF0IjoxNTYwNzY2NzQ2LCJub25fbG9ja2VkIjpmYWxzZSwianRpIjoiYjkzYWMyYmI2Y2EyNDk0Y2IxZjExNWY3MTM2Nzk4ZDAifQ.rF_Q9WTw2JrBkPJXHjO66sV45H18aSr7wcDpAUoJllo\"},\"meta\":{\"msg\":\"登录成功\",\"code\":0,\"success\":true,\"timestamp\":1560766746317}}');

-- ----------------------------
-- Table structure for t_user_registration
-- ----------------------------
DROP TABLE IF EXISTS `t_user_registration`;
CREATE TABLE `t_user_registration`  (
  `id` bigint(22) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(22) NULL DEFAULT NULL,
  `registration_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '极光推送id',
  `token` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户的token',
  `time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_registration
-- ----------------------------
INSERT INTO `t_user_registration` VALUES (3, 99, 'mobile', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMTE1Nzk4MzM0MSIsInVzZXJfaWQiOjk5LCJub25fZXhwaXJlZCI6ZmFsc2UsImV4cCI6MTU2MDg3NDc0NiwiaWF0IjoxNTYwNzY2NzQ2LCJub25fbG9ja2VkIjpmYWxzZSwianRpIjoiYjkzYWMyYmI2Y2EyNDk0Y2IxZjExNWY3MTM2Nzk4ZDAifQ.rF_Q9WTw2JrBkPJXHjO66sV45H18aSr7wcDpAUoJllo', '2019-06-17 18:19:06');

-- ----------------------------
-- Table structure for t_zan
-- ----------------------------
DROP TABLE IF EXISTS `t_zan`;
CREATE TABLE `t_zan`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `type_id` bigint(20) NOT NULL COMMENT '对应的作品或评论的id',
  `type` tinyint(2) NOT NULL COMMENT '点赞类型  1主体点赞  2 主体评论点赞。',
  `status` tinyint(2) NOT NULL COMMENT '点赞状态  0--取消赞   1--有效赞',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '点赞表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
