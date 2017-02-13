package com.talebase.cloud.base.ms.project.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-6.
 */
public class DProjectSelect {

    private Integer id;
    private String name;

    private List<DTaskSelect> tasks = new ArrayList<>();

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

    public List<DTaskSelect> getTasks() {
        return tasks;
    }

    public void setTasks(List<DTaskSelect> tasks) {
        this.tasks = tasks;
    }
}
