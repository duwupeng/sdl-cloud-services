package com.talebase.cloud.common.protocal;

/**
 * Created by eric on 16/11/14.
 */
/**
 * 调用源
 *
 */
public enum CallerFrom{
    /**
     * 网站
     *
     */
    web(0),

    /**
     *  * 手机App
     * *
     */
    app(2);

    private final int value;

    private CallerFrom(int value) {
        this.value = value;
    }

    /**
     * Get the integer value of this enum value, as defined in the Thrift IDL.
     */
    public int getValue() {
        return value;
    }

    /**
     * Find a the enum type by its integer value, as defined in the Thrift IDL.
     * @return null if the value is not found.
     */
    public static CallerFrom findByValue(int value) {
        switch (value) {
            case 0:
                return web;
            case 1:
                return app;
            default:
                return null;
        }
    }

}