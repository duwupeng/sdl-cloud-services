package com.talebase.cloud.base.ms.paper.dto;

/**
 * Created by bin.yang on 2016-12-8.
 */
public class DBlankStyleSetting {
    /**
     * 输入框高度
     */
    Integer height;
    /**
     * 输入框宽度
     */
    Integer width;
    /**
     * 字数上限
     */
    String limit;

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBlankStyleSetting dBlankStyleSetting = (DBlankStyleSetting) o;

        if (limit != null && dBlankStyleSetting.limit != null && !limit.equals(dBlankStyleSetting.limit))
            return false;
        if (width != null && dBlankStyleSetting.width != null && !width.equals(dBlankStyleSetting.width))
            return false;
        return height.equals(dBlankStyleSetting.height);
    }

    @Override
    public int hashCode() {
        int result = height.hashCode();
        result = 31 * result + width.hashCode();
        result = 31 * result + limit.hashCode();
        return result;
    }
}
