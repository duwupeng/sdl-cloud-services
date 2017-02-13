use admindb;
INSERT INTO `admindb`.`t_role` (`name`, `status`, `creater`, `modifier`, `company_id`,`created_date`, `modified_date`) VALUES ('超级管理员', '1', 'system', 'system', '0',now(),now());
INSERT INTO `admindb`.`t_role` (`name`, `status`, `creater`, `modifier`, `company_id`,`created_date`, `modified_date`) VALUES ('顾问', '1', 'system', 'system', '0',now(),now());
INSERT INTO `admindb`.`t_role` (`name`, `status`, `creater`, `modifier`, `company_id`,`created_date`, `modified_date`) VALUES ('评卷人', '1', 'system', 'system', '0',now(),now());
