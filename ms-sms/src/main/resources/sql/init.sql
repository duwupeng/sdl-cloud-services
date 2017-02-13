/*
Navicat MySQL Data Transfer

Source Server         : test
Source Server Version : 50716
Source Host           : 192.168.99.100:33060
Source Database       : TAS01

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2016-11-23 16:52:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_sms_channel
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_channel`;
CREATE TABLE `t_sms_channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `provider_name` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '供应商名称',
  `status` int(11) NOT NULL COMMENT '通道状态(0-禁用; 1-启用)',
  `server_url` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '负责该通道的微服务URL',
  `server_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '负责该通道的微服务名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='短信通道表';

-- ----------------------------
-- Records of t_sms_channel
-- ----------------------------
INSERT INTO `t_sms_channel` VALUES ('1', '昊博', '1', 'http://localhost:10006/sms/post', 'ms-sms-haobo');

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_sms_gateway
-- ----------------------------
INSERT INTO `t_sms_gateway` VALUES ('1', 'ms-sms', '1', '1');

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
  `pay_code` varchar(128) NOT NULL COMMENT '消费中心提供的授权码',
  `send_id` varchar(50) DEFAULT NULL COMMENT '供应商对短信的标识ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='短信记录表';

-- ----------------------------
-- Records of t_sms_send
-- ----------------------------
