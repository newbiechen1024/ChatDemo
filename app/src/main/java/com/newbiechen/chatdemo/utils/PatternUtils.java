package com.newbiechen.chatdemo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by PC on 2016/11/20.
 */

public class PatternUtils {

    public static Matcher pattern(String str,PatternType type){
        Pattern p = Pattern.compile(type.getRegularString());
        return p.matcher(str);
    }

    public enum PatternType{
        PATTERN_WWW("www.([\\w-]+.)+(com|net|org|htm|cn|html)"+
                "(/[\\w.+-]+)*([?][\\w+=&-]+){0,1}"),
        PATTERN_HTTP("(http|ftp|https)://([\\w-]+.)+(com|net|org|htm|cn|html)"+
                "(/[\\w.+-]+)*([?][\\w+=&-]+){0,1}"),
        PATTERN_PHONE("(\\d{3,4}-)?\\d{8,11}"),
        PATTERN_EMAIL("[a-zA-Z._0-9]*@([^\\s.]+.)(com|net|org|htm|cn|html)");
        private String regular;

        private PatternType (String regular){
            this.regular = regular;
        }

        public String getRegularString(){
            return regular;
        }
    }
}
