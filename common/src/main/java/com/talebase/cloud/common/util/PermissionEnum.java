package com.talebase.cloud.common.util;

import com.talebase.cloud.common.exception.BizEnums;

/**
 * Created by kanghong.zhao on 2017-1-6.
 */
public enum PermissionEnum {

    c0_0(1,"全局帐号设定", 0, "POST-/osexamer/examer/saveGlobalAll;DELETE-/osexamer/examer/del;POST-/osexamer/examer/add;GET-/osexamer/examers"),
    c0_1(2,"通知模板",0,"POST-/osproject/mailTemplates;POST-/osproject/smsTemplates;POST-/osproject/mailTemplate/check;POST-/osproject/smsTemplate/check;" +
            "POST-/osproject/mailTemplate/del;POST-/osproject/smsTemplate/del;POST-/osproject/mailTemplate;POST-/osproject/smsTemplate;" +
            "PUT-/osproject/mailTemplate;PUT-/osproject/smsTemplate;PUT-/osproject/mailTemplate/status/;PUT-/osproject/smsTemplate/status/"),
    c0_2(3,"使用统计",0,"POST-/osconsumption/consumecenter/getPays;POST-/osconsumption/consumecenter/getConsumes;GET-/osconsumption/consumecenter/exportPays;" +
            "GET-/osconsumption/consumecenter/exportConsume"),
    c1_0(4,"创建/修改项目",1,"GET-/osproject/projects;PUT-/osproject/project/tasks/;GET-/osproject/project/tasks/;PUT-/osproject/project/;POST-/osproject/project;GET-/osproject/project/edit/;" +
            "DELETE-/osproject/task/;GET-/osproject/project/admins/rest;GET-/osproject/project/exportImage/"),
    c1_1(5,"启用/禁用项目",1,"GET-/osproject/projects;PUT-/osproject/project/status/;PUT-/osproject/task/status/"),
    c1_2(6,"删除项目",1,"GET-/osproject/projects;DELETE-/osproject/project/"),
    c1_3(7,"通知日志",1,"GET-/osproject/projects;GET-/osproject/notifyRecord/export/;POST-/osproject/notify/reSend/sms;POST-/osproject/notify/reSend/email;GET-/osproject/notifyRecord/select;POST-/osproject/notifyRecords"),
    c1_4(8,"帐号管理",1,"GET-/osproject/projects;GET-/osexamer/examers/project"),
    c1_5(9,"新建/修改帐号",1,"GET-/osproject/projects;POST-/osexamer/examer;POST-/osexamer/examers;PUT-/osexamer/examer/;POST-/osexamer/examers;GET-/osexamer/examer/demo/download;" +
            "POST-/osexamer/examer/modifyUserForPerfect;" +
            "GET-/osexamer/examer/importLog/query;PUT-/osproject/project/;POST-/osexamer/examer/import;" +
            "GET-/osexamer/examer/failLog/export;GET-/osexamer/examer/project/newField;GET-/osexamer/examer/project/userAccount"),
    c1_6(10,"重置密码",1,"GET-/osproject/projects;POST-/osexamer/examer/project/reSetPassword"),
    c1_7(11,"发送通知",1,"GET-/osproject/projects;POST-/osproject/notify/toSend/examinee;GET-/osproject/notify/toSend/examiner/;POST-/osproject/notify/send/examinee;POST-/osproject/notify/send/examiner;POST-/osproject/notify/preview/examinee;POST-/osproject/notify/preview/examiner"),
    c1_8(12,"导出帐号",1,"GET-/osproject/projects;GET-/osexamer/examer/project/export/all"),
    c1_9(13,"移除帐号",1,"GET-/osproject/projects;POST-/osexamer/examer/project/del"),
    c1_10(14,"帐号信息设定",1,"GET-/osproject/projects;GET-/osexamer/examer/project/fields;POST-/osexamer/examer/project/saveAll;GET-/osexamer/examer/project/modifyField"),
    c1_11(15,"数据管理",1,"GET-/osproject/projects;GET-/osproject/project/task/findTaskExAndDataManagement"),
    c1_12(16,"导出详细数据",1,"GET-/osproject/projects;GET-/osexamer/question/paper/export/"),
    c3_0(17,"创建/修改试卷",3,"POST-/ospaper/question/papers/;PUT-/ospaper/question/stem/;DELETE-/ospaper/question/item/;POST-/ospaper/question/flush/;" +
            "POST-/ospaper/question/stem/;GET-/ospaper/question/remark/;GET-/ospaper/question/paper/examer/;GET-/ospaper/question/paper/;" +
            "GET-/ospaper/question/scores/;POST-/ospaper/question/scores/;GET-/ospaper/question/stems/;POST-/ospaper/question/sequence/;" +
            "POST-/ospaper/question/score/;PUT-/ospaper/question/stem/style/;PUT-/ospaper/question/paper/status/;" +
            "POST-/ospaper/question/paper/checkName/;POST-/ospaper/question/paper/import/;GET-/question/paper/mode/"),
    c3_1(18,"复制/预览试卷",3,"POST-/ospaper/question/papers/;POST-/ospaper/question/paper/copy/;GET-/ospaper/question/paper/preview/"),
    c3_2(19,"导出试卷",3,"POST-/ospaper/question/papers/;GET-/question/paper/export/"),
    c3_3(20,"删除试卷",3,"POST-/ospaper/question/papers/;POST-/ospaper/question/paper/del/"),
    c2_0(21,"分组管理",2,"PUT-/osadmin/group/name/;DELETE-/osadmin/groups/delete;POST-/osadmin/group"),
    c2_1(22,"管理员列表",2,"PUT-/osadmin/admin;POST-/osadmin/admin;DELETE-/osadmin/admin;GET-/osadmin/admin/export;" +
            "PUT-/osadmin/admin/status;GET-/osadmin/admin/edit/;PUT-/osadmin/admin/password"),
    c2_2(23,"角色管理",2,"PUT-/osadmin/role/name/;DELETE-/osadmin/roles/delete;POST-/osadmin/role"),
    c2_3(24,"角色-权限管理",2,"PUT-/osadmin/role/permissions/;GET-/osadmin/role/permissionOfRole/"),
    c2_4(25,"企业信息",2,"POST-/osadmin/company/upload;GET-/osadmin/company"),
    c2_5(26,"修改个人密码",2,"PUT-/osadmin/company"),
    c99_1(27,"通知模板(顾问)",99,"POST-/osproject/mailTemplates;POST-/osproject/smsTemplates;POST-/osproject/mailTemplate/check;POST-/osproject/smsTemplate/check;" +
            "POST-/osproject/mailTemplate/del;POST-/osproject/smsTemplate/del;POST-/osproject/mailTemplate;POST-/osproject/smsTemplate;" +
            "PUT-/osproject/mailTemplate;PUT-/osproject/smsTemplate;PUT-/osproject/mailTemplate/status/;PUT-/osproject/smsTemplate/status/"),
    c99_2(28,"移除帐号(顾问)",99,"POST-/osexamer/examer/project/del"),
    c99_3(29,"创建/修改试卷(顾问)",99,"PUT-/ospaper/question/stem/;DELETE-/ospaper/question/item/;POST-/ospaper/question/flush/;" +
            "POST-/ospaper/question/stem/;GET-/ospaper/question/remark/;GET-/ospaper/question/paper/examer/;GET-/ospaper/question/paper/;" +
            "GET-/ospaper/question/scores/;POST-/ospaper/question/scores/;GET-/ospaper/question/stems/;POST-/ospaper/question/sequence/;" +
            "POST-/ospaper/question/score/;PUT-/ospaper/question/stem/style/;PUT-/ospaper/question/paper/status/;" +
            "POST-/ospaper/question/paper/checkName/;POST-/ospaper/question/paper/import/;GET-/question/paper/mode/"),
    c99_4(30,"删除试卷(顾问)",99,"POST-/ospaper/question/paper/del/"),
    c99_5(31,"更新试卷权限(顾问)",99,"PUT-/osproject/project/task/paper/"),
    c4_1(32,"考官",4,"GET-/osexamer/evaluate/taskDetail/;GET-/osexamer/evaluate/taskList;GET-/osexamer/exam/examer;POST-/osexamer/exam/score/save;" +
            "POST-/osexamer/exam/score/check;GET-/osexamer/evaluate/examer/markList;" +
            "GET-/osexamer/evaluate/stem/markList"),
    c1_13(33,"复制项目",1,"GET-/osproject/projects;POST-/osproject/project/copy"),
    c5_1(34,"管理后台基础权限",5,"GET-/osconsumption/consumecenter/qureyAccount/;GET-/osadmin/groups/query;GET-/osadmin/subordinate;GET-/osadmin/examiners;GET-/osadmin/admins;" +
            "GET-/osadmin/roleAndGroup;GET-/osadmin/roles/queryByPage;GET-/osadmin/roles/query;GET-/osexamer/examer/project/fields;");


    public Integer id;
    public String name;
    public Integer type;
    public String urls;

    private PermissionEnum(Integer id, String name, Integer type, String urls) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.urls = urls;
    }

    public static PermissionEnum findByName(String name){
        PermissionEnum t = null;
        try{
            t = PermissionEnum.valueOf(name);
        }catch (Exception e){}
        return t;
    }

//    public PermissionEnum getPermissionEnumByCode(int code){
//        for(BizEnums enums : BizEnums.values()){
//            if(enums.getCode() == code)
//                return enums;
//        }
//        return null;
//    }
}
