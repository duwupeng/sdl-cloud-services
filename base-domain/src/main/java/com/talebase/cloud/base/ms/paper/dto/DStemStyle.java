package com.talebase.cloud.base.ms.paper.dto;

/**
 * Created by eric.du on 2017-1-7.
 */
public class DStemStyle {


    /**
     * 1：选择题样式变更{ "type":1, "isSingle ":0(单选);1(多选),"isObjective":null,"dOptionStyleSetting": 1,"dBlankStyleSetting",null,"changeAllType":false,"changeAllStyle":false, "pctType":null}
     * <p>
     * 2: 填空题样式变更{ "type":2, "isSingle ":null,"isObjective":0(主观);1(客观),"dOptionStyleSetting": null,"dBlankStyleSetting",dBlankStyleSetting,"changeAllType":false,"changeAllStyle":false, "pctType":null}
     * <p>
     * 3: 上传题样式变更{ "type":3, "isSingle ":null,"isObjective":null,"dOptionStyleSetting": null,"dBlankStyleSetting",null,"changeAllType":false,"changeAllStyle":false, "pctType":0:图片;1:文档;2:工作表}
     */
    Integer type;

    /**
     * 0(单选);1(多选)
     */
    Integer isSingle;

    /**
     * 选择题样式
     */
    Integer dOptionStyleSetting;

    /**
     * :0(主观);1(客观)
     */
    Integer isObjective;

    /**
     * 填空题样式
     */
    DBlankStyleSetting dBlankStyleSetting;

    /**
     * 题型
     * 是否勾选应用到全部题型
     */
    boolean changeAllType;

    /**
     * 样式
     * 是否勾选应用到全部题型
     */
    boolean changeAllStyle;

    /**
     * 上传题0:图片(pic);1:文档(word);2:工作表(excel)
     */
    String pctType;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsSingle() {
        return isSingle;
    }

    public void setIsSingle(Integer isSingle) {
        this.isSingle = isSingle;
    }

    public Integer getdOptionStyleSetting() {
        return dOptionStyleSetting;
    }

    public void setdOptionStyleSetting(Integer dOptionStyleSetting) {
        this.dOptionStyleSetting = dOptionStyleSetting;
    }

    public Integer getIsObjective() {
        return isObjective;
    }

    public void setIsObjective(Integer isObjective) {
        this.isObjective = isObjective;
    }

    public DBlankStyleSetting getdBlankStyleSetting() {
        return dBlankStyleSetting;
    }

    public void setdBlankStyleSetting(DBlankStyleSetting dBlankStyleSetting) {
        this.dBlankStyleSetting = dBlankStyleSetting;
    }

    public boolean isChangeAllType() {
        return changeAllType;
    }

    public void setChangeAllType(boolean changeAllType) {
        this.changeAllType = changeAllType;
    }

    public boolean isChangeAllStyle() {
        return changeAllStyle;
    }

    public void setChangeAllStyle(boolean changeAllStyle) {
        this.changeAllStyle = changeAllStyle;
    }

    public String getPctType() {
        return pctType;
    }

    public void setPctType(String pctType) {
        this.pctType = pctType;
    }
}
