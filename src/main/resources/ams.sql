SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `userName` varchar(32) DEFAULT NULL COMMENT '用户名',
  `passWord` varchar(32) DEFAULT NULL COMMENT '密码',
  `user_sex` varchar(32) DEFAULT NULL,
  `nick_name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for ams_sys_sequence
-- ----------------------------
DROP TABLE IF EXISTS `ams_sys_sequence`;
CREATE TABLE `ams_sys_sequence`  (
  `NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CURRENT_VALUE` int(11) NOT NULL DEFAULT 0,
  `INCREMENT` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ams_sys_sequence
-- ----------------------------
INSERT INTO `ams_sys_sequence` VALUES ('seq', 1, 1);

SET FOREIGN_KEY_CHECKS = 1;

CREATE DEFINER=`root`@`localhost` FUNCTION `_nextval`(n varchar(50)) RETURNS int(11)
begin  
declare _cur int;  
set _cur=(select current_value from ams_sys_sequence where name= n);  
update ams_sys_sequence  
 set current_value = _cur + increment  
 where name=n ;  
return _cur;  
end