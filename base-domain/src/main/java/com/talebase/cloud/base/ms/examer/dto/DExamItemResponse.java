package com.talebase.cloud.base.ms.examer.dto;

import com.talebase.cloud.base.ms.paper.dto.DOptionItem;

import java.util.List;

/**
 * Created by eric.du on 2016-12-12.
 */
public class DExamItemResponse {
    String name;
    String taskStartDate;
    String taskEndDate;
    int total;
    int finished;
    long systemTime;
    long endTime;
    String timeLeft;
    List answered;
    DExamItem item;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(String taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public String getTaskEndDate() {
        return taskEndDate;
    }

    public void setTaskEndDate(String taskEndDate) {
        this.taskEndDate = taskEndDate;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public List getAnswered() {
        return answered;
    }

    public void setAnswered(List answered) {
        this.answered = answered;
    }

    public DExamItem getItem() {
        return item;
    }

    public void setItem(DExamItem item) {
        this.item = item;
    }

    public long getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(long systemTime) {
        this.systemTime = systemTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }
}
