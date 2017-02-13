package com.talebase.cloud.base.ms.paper.dto;

/**
 * Created by eric.du on 2016-12-6.
 */
public class DOptionItem {
    /**
     * 唯一掩码
     */
    String maskCode;
    /**
     * 选项文字有可能包含图片路径 如果有路径  ， 以<filePath>baseDir/test.png<filePath/>
     */
    String label;
    /**
     * 是否是正确答案
     */
    boolean answer;

    public String getMaskCode() {
        return maskCode;
    }

    public void setMaskCode(String maskCode) {
        this.maskCode = maskCode;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
