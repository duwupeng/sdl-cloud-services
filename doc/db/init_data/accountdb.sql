/*
Navicat MySQL Data Transfer

Source Server         : remote
Source Server Version : 50716
Source Host           : 192.168.1.158:3306
Source Database       : accountDb

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2017-02-09 15:55:11
*/
drop database  accountdb;

CREATE DATABASE IF NOT EXISTS accountdb DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

use accountdb;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_account
-- ----------------------------
DROP TABLE IF EXISTS `t_account`;
CREATE TABLE `t_account` (
  `company_id` int(11) NOT NULL COMMENT '公司id',
  `point_balance` int(8) NOT NULL DEFAULT '0' COMMENT '点数余额',
  `sms_balance` int(8) NOT NULL DEFAULT '0' COMMENT '短信余额',
  `vail_code` varchar(45) DEFAULT NULL COMMENT '哈希验证码；若验证码与当前记录不符则会锁号，不允许操作（暂不使用）',
  `modified_date` datetime NOT NULL COMMENT '最近修改时间',
  `modifier` varchar(45) DEFAULT NULL COMMENT '最近修改人',
  `company_validate` datetime DEFAULT NULL COMMENT '公司有效期',
  `point_valid` int(11) DEFAULT NULL COMMENT 'T币伐值',
  `sms_valid` int(11) DEFAULT NULL COMMENT '短信阀值',
  `peraccount_valid` int(11) DEFAULT NULL COMMENT '开通一个账号扣除的点数',
  PRIMARY KEY (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公司账户表';

-- ----------------------------
-- Table structure for t_account_line
-- ----------------------------
DROP TABLE IF EXISTS `t_account_line`;
CREATE TABLE `t_account_line` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company_id` int(11) NOT NULL COMMENT '公司id',
  `modifier` varchar(45) DEFAULT NULL COMMENT '操作人账号',
  `modified_date` datetime NOT NULL COMMENT '操作时间',
  `remark` varchar(200) DEFAULT NULL COMMENT '操作备注',
  `type` smallint(6) NOT NULL COMMENT '流水类型,1:消费(comsume);2:充值(recharge)',
  `point_var` decimal(8,0) NOT NULL COMMENT '本次流水点数变量',
  `sms_var` decimal(8,0) NOT NULL COMMENT '本次流水短信变量',
  `project_id` int(11) DEFAULT NULL COMMENT '相关操作的项目id',
  `task_id` int(11) DEFAULT NULL COMMENT '相关操作的任务d',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账户流水表';
