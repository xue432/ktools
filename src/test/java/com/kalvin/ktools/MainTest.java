package com.kalvin.ktools;


import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainTest {

    public static void main(String[] args) {
        System.out.println("isChineseByScript(\"你好\") = " + isChineseByScript("-".charAt(0)));
        System.out.println("isChinesePunctuation(\"-\") = " + isChinesePunctuation(",".charAt(0)));
        System.out.println("isCp() = " + isCp());
    }

    //使用UnicodeScript方法判断
    public static boolean isChineseByScript(char c) {
        System.out.println("c = " + c);
        Character.UnicodeScript sc = Character.UnicodeScript.of(c);
        return sc == Character.UnicodeScript.HAN;
    }

    // 根据UnicodeBlock方法判断中文标点符号
    public static boolean isChinesePunctuation(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                || ub == Character.UnicodeBlock.VERTICAL_FORMS;
    }

    public static String isCp() {
        String testStr = "你好，;；。./--’Lin测 nmb试p。‘’“”";
        testStr = testStr.replaceAll("[\\pP]", "");
        Pattern pt = Pattern.compile("[^[A-Za-z]+$]");
        Matcher matcher = pt.matcher(testStr);
        while (matcher.find()) {
            MatchResult mr = matcher.toMatchResult();
            System.out.println("matches = " + mr.group());
        }

        return testStr;
    }
}
