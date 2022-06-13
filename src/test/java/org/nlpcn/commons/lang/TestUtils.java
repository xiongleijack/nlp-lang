package org.nlpcn.commons.lang;

import java.util.List;

import org.nlpcn.commons.lang.pinyin.Pinyin;

public class TestUtils {

    public static String mainResources(final String file) {
        return System.getProperties().getProperty("user.dir") + "/src/main/resources"
                + (file.startsWith("/") ? file : "/" + file);
    }

    public static String testResources(final String file) {
        return System.getProperties().getProperty("user.dir") + "/src/test/resources"
                + (file.startsWith("/") ? file : "/" + file);
    }

    public static void main(String[] args) {
        List<String> parseStr = Pinyin.multiplePinyin("行情");
        System.out.println(parseStr);
        parseStr = Pinyin.multipleFirstChar("行情");
        System.out.println(parseStr);
        parseStr = Pinyin.multipleUnicodePinyin("行情");
        System.out.println(parseStr);
        parseStr = Pinyin.multipleTonePinyin("行情");
        System.out.println(parseStr);
    }
}
