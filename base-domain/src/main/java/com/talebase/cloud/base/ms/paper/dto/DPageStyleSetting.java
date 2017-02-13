package com.talebase.cloud.base.ms.paper.dto;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DPageStyleSetting {
    /**
     * 题目乱序,0:否(no);1:是(yes)
     */
    Integer subjectOrder;
    /**
     * 选项乱序,0:否(no);1:是(yes)
     */
    Integer optionOrder;

    public Integer getSubjectOrder() {
        return subjectOrder;
    }

    public void setSubjectOrder(Integer subjectOrder) {
        this.subjectOrder = subjectOrder;
    }

    public Integer getOptionOrder() {
        return optionOrder;
    }

    public void setOptionOrder(Integer optionOrder) {
        this.optionOrder = optionOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DPageStyleSetting dPageStyleSetting = (DPageStyleSetting) o;

        if (subjectOrder != null && dPageStyleSetting.subjectOrder != null && !subjectOrder.equals(dPageStyleSetting.subjectOrder))
            return false;
        return optionOrder.equals(dPageStyleSetting.optionOrder);
    }

    @Override
    public int hashCode() {
        int result = optionOrder.hashCode();
        result = 31 * result + subjectOrder.hashCode();
        return result;
    }
}
