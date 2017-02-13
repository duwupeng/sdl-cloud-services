package com.talebase.cloud.base.ms.paper.dto;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DOptionStyleSetting {
    /**
     * PC端布局转换
     */
    Integer optionSetting;

    public Integer getOptionSetting() {
        return optionSetting;
    }

    public void setOptionSetting(Integer optionSetting) {
        this.optionSetting = optionSetting;
    }

    public DOptionStyleSetting(Integer optionSetting) {
        this.optionSetting = optionSetting;
    }
    public DOptionStyleSetting() {
    }
}
