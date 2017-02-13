/*
Navicat MySQL Data Transfer

Source Server         : remote
Source Server Version : 50716
Source Host           : 192.168.1.158:3306
Source Database       : questionDb

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2017-02-09 15:55:40
*/
drop database  questiondb;

CREATE DATABASE IF NOT EXISTS questiondb DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

use questiondb;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_attachment
-- ----------------------------
DROP TABLE IF EXISTS `t_attachment`;
CREATE TABLE `t_attachment` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长ID',
  `question` text COMMENT '题干',
  `score` decimal(5,1) DEFAULT NULL COMMENT '总分',
  `score_rule` varchar(10000) DEFAULT NULL COMMENT '评分标准',
  `type` varchar(10) NOT NULL DEFAULT '1' COMMENT '上传题目类型,1:图片(pic);2:文档(word);3:工作表(excel)',
  `created_date` datetime DEFAULT NULL COMMENT '创建人',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建日期',
  `unicode` varchar(100) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_blank
-- ----------------------------
DROP TABLE IF EXISTS `t_blank`;
CREATE TABLE `t_blank` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长ID',
  `question` text COMMENT '题干',
  `answer` text,
  `type` smallint(1) DEFAULT NULL COMMENT '题型,0:主观(subjective );1:客观(objective)',
  `score` text,
  `score_rule` smallint(1) DEFAULT NULL COMMENT '计分规则,0:完全一致(inFullAccord);1:仅顺序不一致(sequentialInconsistency);2:仅供参考(forReferenceOnly)',
  `created_date` datetime DEFAULT NULL COMMENT '创建日期',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `unicode` varchar(100) DEFAULT NULL,
  `explanation` text COMMENT '题目解析',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `style` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_instruction
-- ----------------------------
DROP TABLE IF EXISTS `t_instruction`;
CREATE TABLE `t_instruction` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一标识码',
  `comment` text COMMENT '说明描述',
  `file_path` varchar(2000) DEFAULT NULL COMMENT '文件路径',
  `created_date` datetime DEFAULT NULL COMMENT '创建日期',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `unicode` varchar(100) DEFAULT NULL COMMENT 'unicode编码',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_option
-- ----------------------------
DROP TABLE IF EXISTS `t_option`;
CREATE TABLE `t_option` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '选项组',
  `question` text,
  `options` text,
  `answer` text,
  `sub_score` decimal(5,1) DEFAULT NULL,
  `score` decimal(5,1) DEFAULT NULL,
  `score_rule` smallint(1) DEFAULT NULL COMMENT '计分规则,0:全部答对(all);1:部分答对统一给分(partUnity);1:部分答对平均给分(partAvg)',
  `type` smallint(1) NOT NULL DEFAULT '0' COMMENT '题型,4:单选题(singleChoice);5:多选题(multipleChoice);2:判断题(true_false)',
  `created_date` datetime DEFAULT NULL COMMENT '创建日期',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `unicode` varchar(100) DEFAULT NULL,
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `version_type` smallint(1) DEFAULT NULL COMMENT '版本类型.1:主版本,0 小版本',
  `style` varchar(5000) DEFAULT NULL COMMENT '样式',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_page
-- ----------------------------
DROP TABLE IF EXISTS `t_page`;
CREATE TABLE `t_page` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一标识码',
  `unicode` varchar(100) DEFAULT NULL,
  `subject_order` smallint(1) DEFAULT NULL COMMENT '题目乱序,0:否(no);1:是(yes)',
  `option_order` smallint(1) DEFAULT NULL COMMENT '选项乱序,0:否(no);1:是(yes)',
  `version` int(11) DEFAULT NULL COMMENT '页码版本',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `created_date` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_paper
-- ----------------------------
DROP TABLE IF EXISTS `t_paper`;
CREATE TABLE `t_paper` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键',
  `name` varchar(100) NOT NULL COMMENT '试卷名',
  `company_id` int(11) DEFAULT NULL COMMENT '公司编号',
  `unicode` varchar(100) NOT NULL COMMENT '考卷编号',
  `status` smallint(1) DEFAULT '1' COMMENT '状态,0:未启用(disabled);1:已启用(enabled);2:已删除(deleted)',
  `mode` smallint(1) DEFAULT NULL COMMENT '模式,1:创建中(creating);2:修改中(modifying);3:完成(completed)',
  `version` decimal(11,2) NOT NULL DEFAULT '1.00' COMMENT '版本',
  `version_type` smallint(1) NOT NULL DEFAULT '0' COMMENT '版本类型.1:主版本,0 小版本',
  `composer` text,
  `type` smallint(1) DEFAULT '2' COMMENT '卷类型,0:心理测评(psychological);1:360测评(evaluation360);2:考试(exam);3:敬业度(engagement);4:调研(research)',
  `mark` smallint(1) DEFAULT '0' COMMENT '是否需要阅卷,0:否(no);1:是(yes)',
  `score` decimal(11,1) DEFAULT NULL,
  `usage` int(11) DEFAULT '0' COMMENT '使用次数',
  `total_num` int(11) DEFAULT '0' COMMENT '总题量',
  `subject_num` int(11) DEFAULT '0' COMMENT '主观题量',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `duration` int(11) DEFAULT NULL COMMENT '限时',
  `comment` text,
  PRIMARY KEY (`id`,`unicode`,`version`),
  KEY `paper_no` (`unicode`),
  KEY `version` (`version`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_paper_remark
-- ----------------------------
DROP TABLE IF EXISTS `t_paper_remark`;
CREATE TABLE `t_paper_remark` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一标识码',
  `start_score` decimal(11,1) DEFAULT NULL,
  `end_score` decimal(11,1) DEFAULT NULL,
  `description` text COMMENT '结束语',
  `unicode` varchar(100) DEFAULT NULL,
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `creator` varchar(50) DEFAULT NULL COMMENT '创建人',
  `created_date` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;
