package com.talebase.cloud.base.ms.common.enumes;

/**
 * Created by daorong.li on 2016-12-1.
 */
public enum TypeEnume {
    /**
     * 行业
     */
    INDUSTRY("Industry");

    private final String type;
    private TypeEnume(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
