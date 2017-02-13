package com.talebase.cloud.base.ms.paper.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric.du on 2016-12-9.
 */
public class DWPage{

    /**
     * 页面样式
     */
    DPageStyleSetting dPageStyleSetting;


    /**
     * DOption, DBlank. DAttachment, DInstruction
     * 题目或者说明
     *I
     */
    List items ;


    public DPageStyleSetting getdPageStyleSetting() {
        return dPageStyleSetting;
    }

    public void setdPageStyleSetting(DPageStyleSetting dPageStyleSetting) {
        this.dPageStyleSetting = dPageStyleSetting;
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }

    public void setItem(DItem item) {
        if(this.items==null){
            this.items = new ArrayList();
        }
        this.items.add(item);
    }

}
