/*
Navicat MySQL Data Transfer

Source Server         : remote
Source Server Version : 50716
Source Host           : 192.168.1.158:3306
Source Database       : adminDb

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2017-02-09 15:55:04
*/
drop database  admindb;

CREATE DATABASE IF NOT EXISTS admindb DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

use admindb;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE `t_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `account` varchar(50) NOT NULL COMMENT '用户账号',
  `password` varchar(150) NOT NULL COMMENT '用户密码',
  `name` varchar(50) NOT NULL COMMENT '用户名称',
  `email` varchar(50) DEFAULT NULL COMMENT '用户邮件',
  `status` smallint(1) NOT NULL DEFAULT '0' COMMENT '用户状态,0:无效(invalid);1:有效(effective);-1:删除(deleted)\n',
  `created_date` datetime NOT NULL COMMENT '创建日期',
  `creater` varchar(50) NOT NULL COMMENT '创建人',
  `modified_date` datetime DEFAULT NULL COMMENT '用户信息修改时间',
  `modifier` varchar(50) DEFAULT NULL COMMENT '用户信息修改人',
  `org_code` varchar(50) DEFAULT NULL COMMENT '用户权限',
  `company_id` int(11) NOT NULL COMMENT '公司id',
  `mobile` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ID_UNIQUE` (`id`),
  KEY `fk_admin_company_id` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_company
-- ----------------------------
DROP TABLE IF EXISTS `t_company`;
CREATE TABLE `t_company` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '企业id',
  `name` varchar(200) DEFAULT NULL COMMENT '公司名称',
  `short_name` varchar(100) DEFAULT NULL COMMENT '公司简称',
  `address` varchar(200) DEFAULT NULL COMMENT '公司地址',
  `logo` varchar(200) DEFAULT NULL COMMENT '公司logo',
  `domain` varchar(100) NOT NULL COMMENT '公司二级域名',
  `status` smallint(1) NOT NULL COMMENT '公司状态,0:无效(invalid);1:有效(effective);-1:删除(deleted)',
  `web_site` varchar(200) DEFAULT NULL COMMENT '公司网址',
  `post_code` varchar(100) DEFAULT NULL COMMENT '公司所在地邮件编码',
  `industry_id` int(11) DEFAULT NULL,
  `industry_name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ID_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_group
-- ----------------------------
DROP TABLE IF EXISTS `t_group`;
CREATE TABLE `t_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分组id',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '分组父id',
  `name` varchar(50) NOT NULL COMMENT '分组名称',
  `status` smallint(1) NOT NULL COMMENT '分组状态,0:无效(invalid);1:有效(effective);-1:删除(deleted)',
  `created_date` datetime NOT NULL COMMENT '创建时间',
  `creater` varchar(50) NOT NULL COMMENT '创建人',
  `modified_date` datetime DEFAULT NULL COMMENT '修改时间',
  `modifier` varchar(50) DEFAULT NULL COMMENT '分组修改人',
  `org_code` varchar(50) DEFAULT NULL COMMENT '权限代码',
  `company_id` int(11) NOT NULL COMMENT '公司id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ID_UNIQUE` (`id`),
  KEY `fk_group_company_id` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_group_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_group_admin`;
CREATE TABLE `t_group_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分组用户id',
  `admin_id` int(11) NOT NULL COMMENT '管理员id',
  `group_id` int(11) NOT NULL COMMENT '分组id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ID_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `code` varchar(100) NOT NULL COMMENT '编码',
  `type` smallint(1) NOT NULL COMMENT '类型,0:全局(global);1:项目(project);2:管理员(administrator);3:考试(examination)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ID_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `name` varchar(50) NOT NULL COMMENT '角色名称',
  `status` smallint(1) NOT NULL COMMENT '角色状态,0:无效(invalid);1:有效(effective);-1:删除(deleted)',
  `created_date` datetime NOT NULL COMMENT '角色创建时间',
  `creater` varchar(50) NOT NULL COMMENT '角色创建人',
  `modified_date` datetime DEFAULT NULL COMMENT '角色修改时间',
  `modifier` varchar(50) DEFAULT NULL COMMENT '角色修改人',
  `company_id` int(11) NOT NULL COMMENT '公司id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ID_UNIQUE` (`id`),
  KEY `fk_role_company_id` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_role_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_role_admin`;
CREATE TABLE `t_role_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `admin_id` int(11) NOT NULL COMMENT '管理员id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ID_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `permission_id` int(11) NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限表';
