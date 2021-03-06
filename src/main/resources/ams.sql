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
end;

DROP TABLE IF EXISTS `ams_sys_user`;
CREATE TABLE `ams_sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `name` varchar(100) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `deptId` bigint(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(255) DEFAULT NULL COMMENT '状态 0:禁用，1:正常',
  `sex` bigint(32) DEFAULT NULL COMMENT '性别',
  `birth` datetime DEFAULT NULL COMMENT '出身日期',
  `user_create` varchar(20),
  `user_modified` varchar(20),
  `gmt_create` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=138 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `ams_sys_log`;
CREATE TABLE `ams_sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `time` int(11) DEFAULT NULL COMMENT '响应时间',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `user_create` varchar(20),
  `user_modified` varchar(20),
  `gmt_create` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1032165522528243715 DEFAULT CHARSET=utf8 COMMENT='系统日志';

DROP TABLE IF EXISTS `ams_sys_menu`;
CREATE TABLE `ams_sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `user_create` varchar(20),
  `user_modified` varchar(20),
  `gmt_create` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=212 DEFAULT CHARSET=utf8 COMMENT='菜单管理';

DROP TABLE IF EXISTS `ams_sys_role`;
CREATE TABLE `ams_sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `roleName` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `roleSign` varchar(100) DEFAULT NULL COMMENT '角色标识',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `user_create` varchar(20),
  `user_modified` varchar(20),
  `gmt_create` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8 COMMENT='角色';

DROP TABLE IF EXISTS `ams_sys_role_menu`;
CREATE TABLE `ams_sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  `user_create` varchar(20),
  `user_modified` varchar(20),
  `gmt_create` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4544 DEFAULT CHARSET=utf8 COMMENT='角色与菜单对应关系';

DROP TABLE IF EXISTS `ams_sys_user_role`;
CREATE TABLE `ams_sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `roleId` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `user_create` varchar(20),
  `user_modified` varchar(20),
  `gmt_create` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=132 DEFAULT CHARSET=utf8 COMMENT='用户与角色对应关系';

DROP TABLE IF EXISTS `ams_bus_file`;
CREATE TABLE `ams_bus_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `prjSN` varchar(200) DEFAULT NULL COMMENT '许可证号',
  `updateTime` datetime DEFAULT NULL,
  `fileName` varchar(200) DEFAULT NULL,
  `urlName` varchar(200) DEFAULT NULL,
  `fileType` varchar(20) DEFAULT NULL,
  `user_create` varchar(20) DEFAULT NULL,
  `user_modified` varchar(20) DEFAULT NULL,
  `gmt_create` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目文档表';
    
DROP TABLE IF EXISTS `ams_bus_dxf_entity`;
CREATE TABLE `ams_bus_dxf_entity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `constrID` bigint(20) DEFAULT NULL COMMENT '图元ID',
  `prjSN` varchar(255) DEFAULT NULL COMMENT '许可证号',
  `fileName` varchar(255) DEFAULT NULL COMMENT '提取文件名',
  `longlatV` text DEFAULT NULL COMMENT '经纬度',
  `user_create` varchar(20) DEFAULT NULL,
  `user_modified` varchar(20) DEFAULT NULL,
  `gmt_create` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='图元表';

DROP TABLE IF EXISTS `ams_bus_classifi_dic`;
CREATE TABLE `ams_bus_classifi_dic` (
  `id` varchar(50) NOT NULL,
  `parentID` varchar(50) DEFAULT NULL COMMENT '父级ID',
  `type` varchar(255) DEFAULT NULL,
  `code` varchar(200) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
   `sort` bigint(20) DEFAULT NULL ,
  `other` varchar(255) DEFAULT NULL ,
  
  `user_create` varchar(20) DEFAULT NULL,
  `user_modified` varchar(20) DEFAULT NULL,
  `gmt_create` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表';

DROP TABLE IF EXISTS `ams_bus_xmjbxx`;
CREATE TABLE `ams_bus_xmjbxx` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `prjSN` varchar(200) DEFAULT NULL,
  `prjUnit` varchar(1000) DEFAULT NULL,
  `prjAdr` varchar(1000) DEFAULT NULL,
  `prjAdrCode` varchar(50) DEFAULT NULL,
  `prjAdrDetail` varchar(1000) DEFAULT NULL,
  `prjName` varchar(1000) DEFAULT NULL,
  `prjType` varchar(255) DEFAULT NULL,
  `contacts` varchar(255) DEFAULT NULL,
  `contactInf` varchar(255) DEFAULT NULL,
  `prjTemSN` varchar(255) DEFAULT NULL,
  `specialNotifi` varchar(2000) DEFAULT NULL,
  `noticeTime` varchar(50) DEFAULT NULL,
  `effectiveTime` varchar(255) DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL,
   `delaySN` varchar(255) DEFAULT NULL,
  `delayCountDay` varchar(50) DEFAULT NULL,
  `correctionSN` varchar(255) DEFAULT NULL,
  `correctionDate` varchar(50) DEFAULT NULL,
  
  `prjSNType` varchar(255) DEFAULT NULL,
  `prjStatus` varchar(255) DEFAULT NULL,
  `prjMark` varchar(50) DEFAULT NULL,
  `prjYear` varchar(50) DEFAULT NULL,
  
  `user_create` varchar(20) DEFAULT NULL,
  `user_modified` varchar(20) DEFAULT NULL,
  `gmt_create` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目基本信息';

DROP TABLE IF EXISTS `ams_bus_xmsx`;
CREATE TABLE `ams_bus_xmsx` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `prjSN` varchar(200) DEFAULT NULL,
  `serialNumber` bigint(20) DEFAULT NULL,
  `prjNature` varchar(255) DEFAULT NULL,
  `prjAttr` varchar(255) DEFAULT NULL,
  `strucType` varchar(255) DEFAULT NULL,
  `peacetimeUses` varchar(2000) DEFAULT NULL,
  `aboveGroundLev` bigint(20)  DEFAULT NULL,
  `underGroundLev` bigint(20)  DEFAULT NULL,
  `aboveGroundHet` DOUBLE DEFAULT NULL,
  `underGroundHet` DOUBLE DEFAULT NULL,
  `buildings` bigint(20)  DEFAULT NULL,
  `housingStockNum` bigint(20)  DEFAULT NULL,
  `checkDocSN` varchar(255) DEFAULT NULL,
  `checkDocDate` varchar(50) DEFAULT NULL,
  `checkSN` varchar(255) DEFAULT NULL,
  `checkDate` varchar(50) DEFAULT NULL,
  `cancelSN` varchar(255) DEFAULT NULL,
  `cancelDate` varchar(50) DEFAULT NULL,
  `imgJudgeRes` varchar(500) DEFAULT NULL,
  `exproprInfo` varchar(2000) DEFAULT NULL,
  `remark` varchar(2000) DEFAULT NULL,
  `buldStatus` varchar(255) DEFAULT NULL,
  
  `user_create` varchar(20) DEFAULT NULL,
  `user_modified` varchar(20) DEFAULT NULL,
  `gmt_create` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目属性';

DROP TABLE IF EXISTS `ams_bus_xmmx`;
CREATE TABLE `ams_bus_xmmx` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `prjSN` varchar(200) DEFAULT NULL,
  `serialNumber` bigint(20) NOT NULL,
  `serialFunct` varchar(255) DEFAULT NULL,
  `aboveGroundArea` DOUBLE DEFAULT NULL,
  `underGroundArea` DOUBLE DEFAULT NULL,
  `blendArea` DOUBLE DEFAULT NULL,
  `aboveGroundLen` DOUBLE DEFAULT NULL,
  `prjClasfiCode` varchar(255) DEFAULT NULL,
  
  `prjClasfiName1` varchar(255) DEFAULT "",
  `prjClasfiName2` varchar(255) DEFAULT "",
  `prjClasfiName3` varchar(255) DEFAULT "",
  `prjClasfiName4` varchar(255) DEFAULT "",
  `prjClasfiName5` varchar(255) DEFAULT "",
  
  `user_create` varchar(20) DEFAULT NULL,
  `user_modified` varchar(20) DEFAULT NULL,
  `gmt_create` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目明细信息';

DROP TABLE IF EXISTS `ams_bus_user_operation`;
CREATE TABLE `ams_bus_user_operation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userID` varchar(50) DEFAULT NULL,
  `userName` varchar(200) DEFAULT NULL,
  `operDesc` varchar(500) DEFAULT NULL,
  `operAction` varchar(255) DEFAULT NULL,
  `prjSN` varchar(500) DEFAULT NULL,
  
  `user_create` varchar(20) DEFAULT NULL,
  `user_modified` varchar(20) DEFAULT NULL,
  `gmt_create` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户操作日志表';

-- ----------------------------------------------------------------------视图表-----------------------------------------------------
CREATE VIEW
    bb001v
    (
        prjYear,
        prjSNType,
        prjType,
        prjUnit,
        prjStatus,
        buldStatus,
        prjAdr,
        prjClasfiName1,
        prjClasfiName2,
        prjClasfiName3,
        prjClasfiName4,
        prjClasfiName5,
        sxid,
        mxid
    ) AS
    (
        SELECT DISTINCT
            `a`.`prjYear`        AS `prjYear`,
            `a`.`prjSNType`      AS `prjSNType`,
            `a`.`prjType`        AS `prjType`,
            `a`.`prjUnit`        AS `prjUnit`,
            `a`.`prjStatus`      AS `prjStatus`,
            `a`.`buldStatus`     AS `buldStatus`,
            `a`.`prjAdr`         AS `prjAdr`,
            `a`.`prjClasfiName1` AS `prjClasfiName1`,
            `a`.`prjClasfiName2` AS `prjClasfiName2`,
            `a`.`prjClasfiName3` AS `prjClasfiName3`,
            `a`.`prjClasfiName4` AS `prjClasfiName4`,
            `a`.`prjClasfiName5` AS `prjClasfiName5`,
            `a`.`sxid`           AS `sxid`,
            `a`.`mxid`           AS `mxid`
        FROM
            (
                SELECT
                    `jb`.`prjYear`        AS `prjYear`,
                    `jb`.`prjSNType`      AS `prjSNType`,
                    `jb`.`prjType`        AS `prjType`,
                    `jb`.`prjUnit`        AS `prjUnit`,
                    `jb`.`prjStatus`      AS `prjStatus`,
                    `sx`.`buldStatus`     AS `buldStatus`,
                    `jb`.`prjAdr`         AS `prjAdr`,
                    `mx`.`prjClasfiName1` AS `prjClasfiName1`,
                    `mx`.`prjClasfiName2` AS `prjClasfiName2`,
                    `mx`.`prjClasfiName3` AS `prjClasfiName3`,
                    `mx`.`prjClasfiName4` AS `prjClasfiName4`,
                    `mx`.`prjClasfiName5` AS `prjClasfiName5`,
                    `sx`.`id`             AS `sxid`,
                    `mx`.`id`             AS `mxid`
                FROM
                    ((`ams`.`ams_bus_xmjbxx` `jb`
                LEFT JOIN
                    `ams`.`ams_bus_xmsx` `sx`
                ON
                    (
                        `jb`.`prjSN` = `sx`.`prjSN`))
                LEFT JOIN
                    `ams`.`ams_bus_xmmx` `mx`
                ON
                    (
                        `jb`.`prjSN` = `mx`.`prjSN`
                    AND `sx`.`serialNumber` = `mx`.`serialNumber`
                    AND `mx`.`prjClasfiName5` <> '人防工程（FS）'))) `a`
        WHERE
            `a`.`sxid` IS NOT NULL
    );
-- ------------------------------------------------------索引---------------------------------------------------------
ALTER TABLE `ams_bus_xmjbxx` ADD INDEX prjSN ( `prjSN` );
ALTER TABLE `ams_bus_xmjbxx` ADD INDEX prjUnit ( `prjUnit` );
ALTER TABLE `ams_bus_xmjbxx` ADD INDEX prjAdr ( `prjAdr` );
ALTER TABLE `ams_bus_xmjbxx` ADD INDEX prjName ( `prjName` );
ALTER TABLE `ams_bus_xmjbxx` ADD INDEX prjType ( `prjType` );
ALTER TABLE `ams_bus_xmjbxx` ADD INDEX prjSNType ( `prjSNType` );
ALTER TABLE `ams_bus_xmjbxx` ADD INDEX prjStatus ( `prjStatus` );
ALTER TABLE `ams_bus_xmjbxx` ADD INDEX prjMark ( `prjMark` );
ALTER TABLE `ams_bus_xmjbxx` ADD INDEX prjYear ( `prjYear` );