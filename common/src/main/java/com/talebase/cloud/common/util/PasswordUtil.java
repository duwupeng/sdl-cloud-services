package com.talebase.cloud.common.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by daorong.li on 2016-12-12.
 */
public class PasswordUtil {

    public static Integer SIX_BITS = 6;
    /**
     * 生成随机密码
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyz!@#-");
        StringBuffer letter = new StringBuffer("abcdefghijklmnopqrstuvwxyz");
        StringBuffer number = new StringBuffer("0123456789");
        StringBuffer specialCharacter = new StringBuffer("!@#-");
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = buffer.length();
        sb.append(letter.charAt(r.nextInt(letter.length())));
        sb.append(number.charAt(r.nextInt(number.length())));
        sb.append(specialCharacter.charAt(r.nextInt(specialCharacter.length())));
        for (int i = 0; i < length - 3; i ++) {
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }

    //检查是否字符串中包含数字和字母和字符串
    public static Boolean containLetterAndNumber(String str){
        /*boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母

        for(int i=0 ; i<str.length() ; i++){ //循环遍历字符串
            if(Character.isDigit(str.charAt(i))){     //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }else if(Character.isLetter(str.charAt(i))){   //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }*/
        String reg = "^(?![^a-zA-Z]+$)(?!\\d+$)(?![^;:'\",_`~@#%&\\{\\}\\[\\]\\|\\\\\\<\\>\\?\\/\\.\\-\\+\\=\\!\\$\\^\\*\\(\\)]+$).{6,}$";
        return str.matches(reg);
    }

    //检查是否字符串中包含汉字
    public static Boolean containChinese(String str){
        boolean temp = false;
        Pattern p=Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m=p.matcher(str);
        if(m.find()){
            temp =  true;
        }
        return temp;
    }

}
