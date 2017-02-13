package com.talebase.cloud.base.ms.project.dto;


import com.talebase.cloud.base.ms.project.domain.TTaskExaminer;

/**
 * Created by kanghong.zhao on 2016-12-13.
 */
public class DTaskExamier {

    private String account;
    private String name;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DTaskExamier(){}

    public DTaskExamier(TTaskExaminer taskExaminer){
        this.account = taskExaminer.getExaminer();
        this.name = taskExaminer.getName();
    }
}
