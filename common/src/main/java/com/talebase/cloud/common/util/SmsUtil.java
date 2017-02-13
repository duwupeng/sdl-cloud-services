package com.talebase.cloud.common.util;

/**
 * Created by bin.yang on 2017-1-14.
 */
public class SmsUtil {

    /**
     * 计算短信条数，内容<=70个字符 算1条；如果大于70，则从第二条开始，每67个字符算一条
     *
     * @param content
     * @return
     */
    public static Integer calculateSmsCount(String content) {
        int count = 0;
        if (!StringUtil.isEmpty(content)) {
            if (content.length() > 70) {
                int secend = content.length() - 70;
                count = 1;
                count = count + (secend % 67 == 0 ? secend / 67 : secend / 67 + 1);
            } else {
                count = 1;
            }
        }
        return count;
    }
}
