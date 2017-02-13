package com.talebase.cloud.base.ms.paper.dto;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DAttachment extends  DItem{


    /**
     * 题干设置
     */
    DAttachmentStemSetting dAttachmentStemSetting;

    /**
     * 分数设置
     */
    DAttachmentScoreSetting dAttachmentScoreSetting;

    public DAttachmentStemSetting getdAttachmentStemSetting() {
        return dAttachmentStemSetting;
    }

    public void setdAttachmentStemSetting(DAttachmentStemSetting dAttachmentStemSetting) {
        this.dAttachmentStemSetting = dAttachmentStemSetting;
    }

    public DAttachmentScoreSetting getdAttachmentScoreSetting() {
        return dAttachmentScoreSetting;
    }

    public void setdAttachmentScoreSetting(DAttachmentScoreSetting dAttachmentScoreSetting) {
        this.dAttachmentScoreSetting = dAttachmentScoreSetting;
    }
}
