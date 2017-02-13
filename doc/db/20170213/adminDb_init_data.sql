use admindb;
ALTER table t_role add role_type smallint(1) not null DEFAULT 0 COMMENT '角色类型:0:其他(other);1:超级管理员(super_admin);2:评卷人(mark_admin);3:顾问(advisor)';

use examerdb;
update t_user_show_field set field_key = 'highest_education' where field_key = 'high_education';
update t_user_show_field set field_key = 'profession' where field_key = 'profression';