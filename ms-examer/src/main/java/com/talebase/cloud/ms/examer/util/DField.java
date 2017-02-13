package com.talebase.cloud.ms.examer.util;

/**
 * Created by kanghong.zhao on 2016-12-13.
 */
public class DField{
    private String fieldKey;
    private String fieldName;
    private Object fieldValue;

    public DField() {
    }

    public DField(String fieldKey, String fieldName, Object fieldValue) {
        this.fieldKey = fieldKey;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
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

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }
}
