package com.talebase.cloud.os.login.bind;

/**
 * Created by suntree.xu on 2016-12-20.
 */
public class ImageInfo {
    private String code;
    private String base64Image;

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
