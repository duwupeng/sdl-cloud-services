package com.talebase.cloud.base.ms.examer.domain;

import java.sql.Timestamp;

/**
 * @author auto-tool
 */public class TUserShowField {
    /**
     * 字段显示id
     */
    Integer id;

    /**
     * 字段中文名称
     */
    String fieldName;

    /**
     * 字段英文名称
     */
    String fieldKey;

    /**
     * 显示状态,0:隐藏(hidden);1:显示(display)
     */
    Integer isshow;

    /**
     * 必填状态,0:非强制(unmandatory);1:强制(mandatory)
     */
    Integer ismandatory;

    /**
     * 是否唯一状态,0:非唯一(ununique),1:唯一(unique)
     */
    Integer isunique;

    /**
     * 任务id
     */
    Integer taskId;

    /**
     * 项目id
     */
    Integer projectId;

    /**
     * 是否扩展字段,0:非扩展(unextension);1:扩展(extension)
     */
    Integer isextension;

    /**
     * 排序字段
     */
    Integer sortnum;

    /**
     * 公司id
     */
    Integer companyId;

    /**
     * 创建时间
     */
    java.sql.Timestamp createDate;

    /**
     * 创建人
     */
    String creater;

    /**
     * 自定义类型,1:输入框(input);2:日期类型(dateType);3:下拉框(selectType)
     */
    Integer type;

    /**
     * 扩展字段，保存json
     */
    String selectValue;

    /**
     * 修改时间
     */
    java.sql.Timestamp modifyDate;
    /**
     * 修改人
     */
    String modifier;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public Integer getIsshow() {
        return isshow;
    }

    public void setIsshow(Integer isshow) {
        this.isshow = isshow;
    }

    public Integer getIsmandatory() {
        return ismandatory;
    }

    public void setIsmandatory(Integer ismandatory) {
        this.ismandatory = ismandatory;
    }

    public Integer getIsunique() {
        return isunique;
    }

    public void setIsunique(Integer isunique) {
        this.isunique = isunique;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getIsextension() {
        return isextension;
    }

    public void setIsextension(Integer isextension) {
        this.isextension = isextension;
    }

    public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSelectValue() {
        return selectValue;
    }

    public void setSelectValue(String selectValue) {
        this.selectValue = selectValue;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
