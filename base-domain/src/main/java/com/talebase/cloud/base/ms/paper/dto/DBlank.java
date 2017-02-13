package com.talebase.cloud.base.ms.paper.dto;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DBlank  extends  DItem{
    /**
     *填空题题干
     */
    DBlankStemSetting dBlankStemSetting;
    /**
     *填空题分数
     */
    DBlankScoreSetting dBlankScoreSetting;
    /**
     *填空题样式
     */
    DBlankStyleSetting dBlankStyleSetting;

    public DBlankStemSetting getdBlankStemSetting() {
        return dBlankStemSetting;
    }

    public void setdBlankStemSetting(DBlankStemSetting dBlankStemSetting) {
        this.dBlankStemSetting = dBlankStemSetting;
    }

    public DBlankScoreSetting getdBlankScoreSetting() {
        return dBlankScoreSetting;
    }

    public void setdBlankScoreSetting(DBlankScoreSetting dBlankScoreSetting) {
        this.dBlankScoreSetting = dBlankScoreSetting;
    }


    public DBlankStyleSetting getdBlankStyleSetting() {
        return dBlankStyleSetting;
    }

    public void setdBlankStyleSetting(DBlankStyleSetting dBlankStyleSetting) {
        this.dBlankStyleSetting = dBlankStyleSetting;
    }

}
