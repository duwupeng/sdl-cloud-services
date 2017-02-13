/*
Navicat MySQL Data Transfer

Source Server         : remote
Source Server Version : 50716
Source Host           : 192.168.1.158:3306
Source Database       : commonDb

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2017-02-09 15:55:18
*/
drop database  commondb;

CREATE DATABASE IF NOT EXISTS commondb DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

use commondb;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_code
-- ----------------------------
DROP TABLE IF EXISTS `t_code`;
CREATE TABLE `t_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(100) NOT NULL DEFAULT 'null' COMMENT '代码值',
  `type` varchar(100) NOT NULL COMMENT '类型',
  `name` varchar(200) NOT NULL COMMENT '名称',
  `status` int(11) NOT NULL COMMENT '状态,0:禁用(disabled);1:启用(Enabled)',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `creater` varchar(100) DEFAULT 'system',
  `sortNum` int(11) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_email_log
-- ----------------------------
DROP TABLE IF EXISTS `t_email_log`;
CREATE TABLE `t_email_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `table_name` varchar(45) NOT NULL COMMENT '表明',
  `table_id` int(11) NOT NULL COMMENT '表id',
  `send_content` text COMMENT '发送内容',
  `send_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  `sender` varchar(45) NOT NULL DEFAULT 'system' COMMENT '发送人',
  `sendTimes` int(11) DEFAULT '1' COMMENT '发送次数,预留字段',
  `status` smallint(6) NOT NULL COMMENT '发送状态,0:失败(failure);1:成功(success)',
  `subject` varchar(200) NOT NULL COMMENT '发送标题',
  `email` varchar(200) NOT NULL COMMENT '发送邮件',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=241 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_sms_channel
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_channel`;
CREATE TABLE `t_sms_channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `provider_name` varchar(50) NOT NULL COMMENT '供应商名称',
  `status` int(11) NOT NULL COMMENT '通道状态(0-禁用; 1-启用)',
  `server_url` varchar(255) DEFAULT NULL COMMENT '负责该通道的微服务URL',
  `server_name` varchar(255) DEFAULT NULL COMMENT '负责该通道的微服务名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='短信通道表';

-- ----------------------------
-- Table structure for t_sms_gateway
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_gateway`;
CREATE TABLE `t_sms_gateway` (
  `config_id` int(11) NOT NULL AUTO_INCREMENT,
  `gateway_name` varchar(255) NOT NULL COMMENT '短信管理网关名称',
  `channel_id` int(11) NOT NULL COMMENT '短信网关使用的通道',
  `weight` double DEFAULT NULL COMMENT '该通道在短信网关中所占权重',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_sms_send
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_send`;
CREATE TABLE `t_sms_send` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `GUID` varchar(128) NOT NULL COMMENT 'GUID用于标识短信在整个系统中的唯一性，由发送方指定，作为查询、回调凭据',
  `sendto` varchar(30) NOT NULL COMMENT '接收方号码',
  `content` varchar(1024) DEFAULT NULL COMMENT '短信内容',
  `send_time` datetime NOT NULL COMMENT '发送时间，如果有重试，则为最近重试时间',
  `status` int(11) NOT NULL COMMENT '短信状态(0-未发送;1-发送成功;2-失败等待重发;3-发送失败;4-消费失败;5-对方已接收)',
  `send_count` int(11) DEFAULT NULL COMMENT '已发次数（如果成功应该为1次，如果有重发，则>1）',
  `received_time` datetime DEFAULT NULL COMMENT '用户接收时间',
  `channel_name` varchar(50) DEFAULT NULL COMMENT '发送渠道(供应商)',
  `has_feedback` tinyint(1) NOT NULL COMMENT '是否已经将发送结果反馈给调用者',
  `send_id` varchar(50) DEFAULT NULL COMMENT '供应商对短信的标识ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='短信记录表';
