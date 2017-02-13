package com.talebase.cloud.base.ms.admin.enums;

/**
 * Created by daorong.li on 2016-12-5.
 */
public enum  PermissionCode {
    //管理员设置
    ADMIN_LIST_RIGHT("admin_list","管理员列表");

    private String code;
    private String name;
    private PermissionCode(String code ,String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
