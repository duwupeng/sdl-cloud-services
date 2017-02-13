package com.talebase.cloud.base.ms.examer.dto;

/**
 * Created by kanghong.zhao on 2017-2-8.
 */
public class DUserExamInfo {

    private Integer id;//userId
    private String name;
    private String account;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
