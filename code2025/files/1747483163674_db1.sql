/*
 Navicat Premium Dump SQL

 Source Server         : teacher
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : db

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 17/04/2025 08:39:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for businessarchive
-- ----------------------------
DROP TABLE IF EXISTS `businessarchive`;
CREATE TABLE `businessarchive`  (
  `档案ID` int NOT NULL,
  `教师ID` int NULL DEFAULT NULL,
  `档案类型` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `档案内容` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL,
  `上传时间` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`档案ID`) USING BTREE,
  INDEX `教师ID`(`教师ID` ASC) USING BTREE,
  CONSTRAINT `businessarchive_ibfk_1` FOREIGN KEY (`教师ID`) REFERENCES `teacherinfo` (`教师ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of businessarchive
-- ----------------------------
INSERT INTO `businessarchive` VALUES (1, 1, '教学成果', '详细的教学成果报告。', '2023-01-15 10:00:00');
INSERT INTO `businessarchive` VALUES (2, 2, '科研项目', '科研项目申请书及进展报告。', '2023-02-20 14:30:00');
INSERT INTO `businessarchive` VALUES (3, 3, '学术论文', '在知名期刊上发表的学术论文。', '2023-03-10 09:45:00');

-- ----------------------------
-- Table structure for systemlog
-- ----------------------------
DROP TABLE IF EXISTS `systemlog`;
CREATE TABLE `systemlog`  (
  `日志ID` int NOT NULL,
  `用户ID` int NULL DEFAULT NULL,
  `操作类型` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `操作时间` datetime NULL DEFAULT NULL,
  `操作内容` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL,
  `结果状态` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  PRIMARY KEY (`日志ID`) USING BTREE,
  INDEX `用户ID`(`用户ID` ASC) USING BTREE,
  CONSTRAINT `systemlog_ibfk_1` FOREIGN KEY (`用户ID`) REFERENCES `user` (`用户ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of systemlog
-- ----------------------------
INSERT INTO `systemlog` VALUES (1, 1, '登录', '2023-04-01 08:00:00', '用户admin登录系统。', '成功');
INSERT INTO `systemlog` VALUES (2, 2, '上传档案', '2023-04-02 10:30:00', '教师teacher1上传了教学成果档案。', '成功');
INSERT INTO `systemlog` VALUES (3, 3, '查看档案', '2023-04-03 14:45:00', '用户user1查看了教师teacher1的教学成果档案。', '成功');

-- ----------------------------
-- Table structure for teacherinfo
-- ----------------------------
DROP TABLE IF EXISTS `teacherinfo`;
CREATE TABLE `teacherinfo`  (
  `教师ID` int NOT NULL,
  `姓名` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `性别` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `年龄` int NULL DEFAULT NULL,
  `职称` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `学历` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `联系方式` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `评职称单位` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  PRIMARY KEY (`教师ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacherinfo
-- ----------------------------
INSERT INTO `teacherinfo` VALUES (1, '张三', '男', 35, '教授', '博士', '13800138000', 'XX大学人事处');
INSERT INTO `teacherinfo` VALUES (2, '李四', '女', 42, '副教授', '硕士', '13900139000', NULL);
INSERT INTO `teacherinfo` VALUES (3, '王五', '男', 29, '讲师', '学士', '13700137000', NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `用户ID` int NOT NULL,
  `用户名` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `密码` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `邮箱` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `角色` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `权限列表` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL,
  PRIMARY KEY (`用户ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'hashed_password_1', 'admin@example.com', '管理员', '{\"read\": true, \"write\": true, \"delete\": true}');
INSERT INTO `user` VALUES (2, 'teacher1', 'hashed_password_2', 'teacher1@example.com', '教师', '{\"read\": true, \"write\": true}');
INSERT INTO `user` VALUES (3, 'user1', 'hashed_password_3', 'user1@example.com', '用户', '{\"read\": true}');

SET FOREIGN_KEY_CHECKS = 1;
