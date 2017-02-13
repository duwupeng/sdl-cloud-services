/*
Navicat MySQL Data Transfer

Source Server         : remote
Source Server Version : 50716
Source Host           : 192.168.1.158:3306
Source Database       : examerDb

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2017-02-09 15:55:27
*/
drop database  examerdb;

CREATE DATABASE IF NOT EXISTS examerdb DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

use examerdb;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_exercise
-- ----------------------------
DROP TABLE IF EXISTS `t_exercise`;
CREATE TABLE `t_exercise` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '考生ID',
  `task_id` int(11) DEFAULT NULL COMMENT '考试ID',
  `paper_id` int(11) NOT NULL COMMENT '试卷ID',
  `obj_score` decimal(11,1) DEFAULT NULL COMMENT '客观题总分',
  `sub_score` decimal(11,1) DEFAULT NULL COMMENT '主观题得分',
  `answer_score_detail` text,
  `start_time` datetime DEFAULT NULL COMMENT '测试开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '测试结束时间',
  `serial_no` int(11) DEFAULT NULL COMMENT '考生序号(根据交卷)',
  `submit_type` smallint(1) DEFAULT '0' COMMENT '交卷类型:0:考生交卷(examer_submit);1:定时交卷(system_submit)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `exercise_unique_id` (`user_id`,`task_id`),
  KEY `ref_excise_user_info` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_group_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_group_admin`;
CREATE TABLE `t_group_admin` (
  `account` varchar(50) NOT NULL COMMENT '账号',
  `org_code` varchar(50) NOT NULL COMMENT '组织结构',
  `company_id` int(11) NOT NULL COMMENT '公司id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_notify_record
-- ----------------------------
DROP TABLE IF EXISTS `t_notify_record`;
CREATE TABLE `t_notify_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志编号',
  `company_id` int(11) DEFAULT NULL COMMENT '公司编号',
  `send_subject` varchar(100) DEFAULT NULL COMMENT '发送主题',
  `send_content` text COMMENT '发送内容',
  `send_type` smallint(1) DEFAULT '0' COMMENT '发送类型,0:邮件(mail);1:短信(sms);2:微信(wechat);3:邮件与短信(mailAndSms)',
  `role_id` int(11) DEFAULT NULL COMMENT '角色编号',
  `project_id` int(11) DEFAULT NULL COMMENT '项目编号',
  `project_name` varchar(100) DEFAULT NULL COMMENT '项目名称',
  `task_id` int(11) DEFAULT NULL COMMENT '产品编号',
  `task_name` varchar(100) DEFAULT NULL COMMENT '产品名称',
  `account` varchar(50) DEFAULT NULL COMMENT '帐号',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机',
  `sender` varchar(50) DEFAULT NULL COMMENT '发送人',
  `send_date` datetime DEFAULT NULL COMMENT '发送时间',
  `send_status` smallint(1) DEFAULT '2' COMMENT '发送状态,0:发送失败(send_fail);1:发送成功(send_success);2:发送中(sending)',
  `send_count` int(11) DEFAULT '0' COMMENT '发送条数',
  `send_times` int(11) DEFAULT '1' COMMENT '发送次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_notify_template
-- ----------------------------
DROP TABLE IF EXISTS `t_notify_template`;
CREATE TABLE `t_notify_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '模板编号',
  `company_id` int(11) DEFAULT NULL COMMENT '企业编号',
  `name` varchar(200) DEFAULT NULL COMMENT '模板名称',
  `content` text COMMENT '模板内容',
  `subject` varchar(100) DEFAULT NULL COMMENT '邮件主题',
  `sign` text COMMENT '邮件签名',
  `type` smallint(1) DEFAULT '1' COMMENT '类型,0:系统(system);1:自定义(customize)',
  `method` smallint(1) DEFAULT '0' COMMENT '发送方法,0:邮件(mail);1:短信(sms);2:微信(wechat)',
  `status` smallint(1) DEFAULT '1' COMMENT '状态,0:禁用(disable);1:启用(enable);2:删除(delete)',
  `whether_default` smallint(1) DEFAULT '0' COMMENT '是否默认,0:否(no);1:是(yes)',
  `creator` varchar(100) DEFAULT NULL COMMENT '创建人',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifier` varchar(100) DEFAULT NULL COMMENT '修改人',
  `modified_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_project
-- ----------------------------
DROP TABLE IF EXISTS `t_project`;
CREATE TABLE `t_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '项目名称(企业惟一)',
  `description` text COMMENT '项目描述',
  `company_id` int(11) NOT NULL COMMENT '所属公司id',
  `creater` varchar(50) NOT NULL COMMENT '创建人',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(50) DEFAULT NULL COMMENT '修改人',
  `modified_date` datetime DEFAULT NULL COMMENT '修改时间',
  `status` smallint(1) NOT NULL COMMENT '项目状态,0:禁用(disable);1:启用(enable);2:删除(delete)',
  `start_date` datetime DEFAULT NULL COMMENT '项目开始时间',
  `end_date` datetime DEFAULT NULL COMMENT '项目结束时间',
  `scan_max` int(11) DEFAULT NULL COMMENT '扫码人数上限',
  `scan_now` int(11) DEFAULT NULL COMMENT '当前扫码人数',
  `scan_account_pre` varchar(50) DEFAULT NULL COMMENT '扫码生成账号前缀',
  `scan_start_date` datetime DEFAULT NULL COMMENT '扫码有效日期-开始',
  `scan_end_date` datetime DEFAULT NULL COMMENT '扫码有效日期-结束',
  `scan_enable` smallint(1) DEFAULT '0' COMMENT '是否开启扫码,0:否(disable);1:开启(enable)',
  `scan_image` blob COMMENT '二维码base64',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_project_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_project_admin`;
CREATE TABLE `t_project_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL,
  `account` varchar(50) NOT NULL COMMENT '管理员账号',
  `name` varchar(50) NOT NULL COMMENT '管理员名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_project_err_notify
-- ----------------------------
DROP TABLE IF EXISTS `t_project_err_notify`;
CREATE TABLE `t_project_err_notify` (
  `project_id` int(11) NOT NULL COMMENT '项目id',
  `err_num` int(11) DEFAULT NULL COMMENT '待告知发送失败条数',
  PRIMARY KEY (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_score
-- ----------------------------
DROP TABLE IF EXISTS `t_score`;
CREATE TABLE `t_score` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '得分id',
  `serial_no` varchar(100) DEFAULT NULL,
  `paper_id` int(11) NOT NULL COMMENT '试卷id',
  `score` text COMMENT '得分',
  `exam_id` int(11) NOT NULL,
  `creater` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify` varchar(50) DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `done` int(11) DEFAULT '0' COMMENT '已打分空格数',
  `total` int(11) DEFAULT '0' COMMENT '总共打分空格数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE,
  UNIQUE KEY `score_unique_id` (`serial_no`,`paper_id`,`exam_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_task
-- ----------------------------
DROP TABLE IF EXISTS `t_task`;
CREATE TABLE `t_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL COMMENT '项目id',
  `company_id` int(11) NOT NULL COMMENT '公司id',
  `name` varchar(100) NOT NULL COMMENT '任务名称',
  `creater` varchar(50) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modifier` varchar(50) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `status` smallint(1) NOT NULL COMMENT '项目状态,0:禁用(disable);1:启用(enable);2:删除(delete)',
  `page_change_limit` int(11) DEFAULT NULL COMMENT '页面切换次数上限;默认为null不限制',
  `paper_id` int(11) NOT NULL COMMENT '对应试卷id',
  `need_marking_num` int(11) DEFAULT '0' COMMENT '需要阅卷题数，默认为0，即不需要阅卷',
  `start_date` datetime DEFAULT NULL COMMENT '开考时间',
  `latest_start_date` datetime DEFAULT NULL COMMENT '最晚开考时间',
  `end_date` datetime DEFAULT NULL COMMENT '任务结束时间',
  `exam_time` int(5) DEFAULT NULL COMMENT '考试时长(单位：分钟)',
  `paper_unicode` varchar(100) DEFAULT NULL COMMENT '试卷编号',
  `paper_version` int(11) DEFAULT NULL COMMENT '试卷版本',
  `paper_num` int(11) DEFAULT NULL COMMENT '试卷总题目数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_task_examiner
-- ----------------------------
DROP TABLE IF EXISTS `t_task_examiner`;
CREATE TABLE `t_task_examiner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) NOT NULL COMMENT '任务id',
  `examiner` varchar(50) NOT NULL COMMENT '考官账号',
  `name` varchar(50) DEFAULT NULL COMMENT '考官名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_task_progress
-- ----------------------------
DROP TABLE IF EXISTS `t_task_progress`;
CREATE TABLE `t_task_progress` (
  `task_id` int(11) NOT NULL,
  `pre_num` int(11) DEFAULT '0' COMMENT '预考人数(考生账号已与任务关联)',
  `in_num` int(11) DEFAULT '0' COMMENT '已进入考试人数',
  `finish_num` int(11) DEFAULT '0' COMMENT '交卷人数',
  `marked_num` int(11) DEFAULT '0' COMMENT '已评卷人数',
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_user_exam
-- ----------------------------
DROP TABLE IF EXISTS `t_user_exam`;
CREATE TABLE `t_user_exam` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '测试id',
  `type` smallint(1) NOT NULL COMMENT '测试类型,1:考试(exam);2:360测试(360test);3:在线心理测评(psychological_test);4:调用(research)',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `project_id` int(11) DEFAULT NULL COMMENT '项目id',
  `task_id` int(11) DEFAULT NULL COMMENT '任务id',
  `status` smallint(1) DEFAULT NULL COMMENT '测试状态,-1:删除(delete);0:禁用(disabled);1:启用(enabled)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `creater` varchar(50) DEFAULT NULL COMMENT '创建人',
  `company_id` int(11) NOT NULL COMMENT '公司id',
  `send_email_status` smallint(1) DEFAULT NULL COMMENT '发送邮件状态,1:发送中(email_sending);2:发送失败(email_failure);3:发送成功(email_success)',
  `send_sms_status` smallint(1) DEFAULT NULL COMMENT '短信发送状态,1:发送中(sms_sending);2:发送失败(sms_failure);3:发送成功(sms_success)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_user_import_log
-- ----------------------------
DROP TABLE IF EXISTS `t_user_import_log`;
CREATE TABLE `t_user_import_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `batch_no` varchar(50) NOT NULL COMMENT '导入批次号',
  `status` smallint(1) NOT NULL COMMENT '导入状态,0:进行中(OnDoing);1:已完成(Done);2:解析失败(Fail)',
  `succ_num` int(11) NOT NULL DEFAULT '0' COMMENT '成功导入记录',
  `fail_num` int(11) NOT NULL DEFAULT '0' COMMENT '失败记录',
  `creater` varchar(50) NOT NULL COMMENT '操作管理员账号',
  `created_date` datetime NOT NULL COMMENT '开始导入时间',
  `finished_date` datetime DEFAULT NULL COMMENT '完成导入时间',
  `project_id` int(11) DEFAULT NULL COMMENT '相关项目id',
  `task_id` int(11) DEFAULT NULL COMMENT '相关任务id',
  `company_id` int(11) NOT NULL,
  `title_json` text COMMENT '表单头',
  PRIMARY KEY (`id`),
  UNIQUE KEY `batch_no_UNIQUE` (`batch_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_user_import_record
-- ----------------------------
DROP TABLE IF EXISTS `t_user_import_record`;
CREATE TABLE `t_user_import_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `batch_no` varchar(50) NOT NULL COMMENT '导入所属批次号',
  `examinee` varchar(50) DEFAULT NULL COMMENT '考生号',
  `status` smallint(1) NOT NULL COMMENT '导入状态,0:失败(fail);1:成功(success)',
  `remark` varchar(200) NOT NULL COMMENT '备注',
  `detail_json` text COMMENT '失败才要保存；xls的字段内容；主要用于导出',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_user_info
-- ----------------------------
DROP TABLE IF EXISTS `t_user_info`;
CREATE TABLE `t_user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '测试用户id',
  `account` varchar(50) NOT NULL COMMENT '用户账号',
  `name` varchar(50) DEFAULT NULL COMMENT '用户姓名',
  `email` varchar(50) DEFAULT NULL,
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `gender` varchar(10) DEFAULT NULL,
  `password` varchar(150) DEFAULT NULL COMMENT '密码',
  `highest_education` varchar(50) DEFAULT NULL COMMENT '最高学历',
  `identity_num` varchar(18) DEFAULT NULL COMMENT '身份证号码',
  `birthday` datetime DEFAULT NULL COMMENT '出生年月',
  `work_years` varchar(20) DEFAULT NULL,
  `industry_name` varchar(100) DEFAULT NULL COMMENT '行业',
  `political_status` varchar(50) DEFAULT NULL COMMENT '政治面貌',
  `dept_id` int(11) DEFAULT NULL COMMENT '部门id',
  `dept_name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `position` varchar(50) DEFAULT NULL COMMENT '职位',
  `school` varchar(50) DEFAULT NULL COMMENT '毕业院校',
  `profession` varchar(50) DEFAULT NULL COMMENT '专业',
  `status` smallint(1) DEFAULT '1' COMMENT '用户状态,0:禁用(disabled);1:启用(enabled)',
  `company_id` int(11) NOT NULL COMMENT '所属公司id',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` varchar(50) DEFAULT NULL COMMENT '创建人',
  `extension_field` text COMMENT '扩展字段，保存json内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_company_UNIQUE` (`account`,`company_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_user_show_field
-- ----------------------------
DROP TABLE IF EXISTS `t_user_show_field`;
CREATE TABLE `t_user_show_field` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '字段显示id',
  `field_name` varchar(50) DEFAULT NULL COMMENT '字段中文名称',
  `field_key` varchar(100) DEFAULT NULL COMMENT '字段英文名称',
  `isshow` smallint(1) DEFAULT '0' COMMENT '显示状态,0:隐藏(hidden);1:显示(display)',
  `ismandatory` smallint(1) DEFAULT '0' COMMENT '必填状态,0:非强制(unmandatory);1:强制(mandatory)',
  `isunique` smallint(1) DEFAULT '0' COMMENT '是否唯一状态,0:非唯一(ununique),1:唯一(unique)',
  `task_id` int(11) DEFAULT NULL COMMENT '任务id',
  `project_id` int(11) DEFAULT NULL COMMENT '项目id',
  `isextension` smallint(1) DEFAULT '0' COMMENT '是否扩展字段,0:非扩展(unextension);1:扩展(extension)',
  `sortnum` int(11) DEFAULT NULL COMMENT '排序字段',
  `company_id` int(11) NOT NULL COMMENT '公司id',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` varchar(50) DEFAULT NULL COMMENT '创建人',
  `type` smallint(1) DEFAULT NULL COMMENT '自定义类型,1:输入框(input);2:日期类型(dateType);3:下拉框(selectType)',
  `select_value` text COMMENT '扩展字段，用逗号隔开',
  `modify_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改日期',
  `modifier` varchar(50) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- View structure for v_task_progress
-- ----------------------------
DROP VIEW IF EXISTS `v_task_progress`;
CREATE ALGORITHM=UNDEFINED DEFINER=`bzceshi`@`%` SQL SECURITY DEFINER VIEW `v_task_progress` AS select `ue`.`user_id` AS `user_id`,`ue`.`id` AS `exam_id`,`ue`.`company_id` AS `company_id`,`ue`.`project_id` AS `project_id`,`ue`.`task_id` AS `task_id`,`ue`.`creater` AS `creater`,(case when (`ue`.`id` > 0) then 1 else 0 end) AS `pre_num`,(case when (`e`.`id` > 0) then 1 else 0 end) AS `in_num`,(case when (`e`.`end_time` is not null) then 1 else 0 end) AS `finish_num`,(case when (`e`.`sub_score` is not null) then 1 else 0 end) AS `marked_num` from (`t_user_exam` `ue` left join `t_exercise` `e` on(((`ue`.`user_id` = `e`.`user_id`) and (`ue`.`task_id` = `e`.`task_id`)))) where (`ue`.`status` <> -(1)) ;
