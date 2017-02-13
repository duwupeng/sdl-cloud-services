package com.talebase.cloud.base.ms.paper.dto;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DOption extends DItem{

    /**
     * 题干设置
     */
    DOptionStemSetting dOptionStemSetting;

    /**
     * 分数设置
     */
    DOptionScoreSetting dOptionScoreSetting;

    /**
     * 样式
     */
    DOptionStyleSetting dOptionStyleSetting;

    public DOptionStemSetting getdOptionStemSetting() {
        return dOptionStemSetting;
    }

    public void setdOptionStemSetting(DOptionStemSetting dOptionStemSetting) {
        this.dOptionStemSetting = dOptionStemSetting;
    }

    public DOptionScoreSetting getdOptionScoreSetting() {
        return dOptionScoreSetting;
    }

    public void setdOptionScoreSetting(DOptionScoreSetting dOptionScoreSetting) {
        this.dOptionScoreSetting = dOptionScoreSetting;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public DOptionStyleSetting getdOptionStyleSetting() {
        return dOptionStyleSetting;
    }

    public void setdOptionStyleSetting(DOptionStyleSetting dOptionStyleSetting) {
        this.dOptionStyleSetting = dOptionStyleSetting;
    }
}
