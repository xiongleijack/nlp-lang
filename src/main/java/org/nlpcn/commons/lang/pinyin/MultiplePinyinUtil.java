package org.nlpcn.commons.lang.pinyin;

import org.nlpcn.commons.lang.tire.SmartGetWord;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.util.StringUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 多音字拼音枚举
 *
 * @author xionglei
 * @version 1.0
 * @date 2022/6/13 23:00
 */
public enum MultiplePinyinUtil {

    INSTANCE;

    public static final String PINYIN_MAPPING_FILE = "/pinyin.txt";

    public static final String SHARP = "#";
    public static final String EQUAL = "=";

    private SmartForest<String[]> polyPhoneDict = null;

    MultiplePinyinUtil() {
        polyPhoneDict = new SmartForest<>();
        loadPinyinMapping();
    }

    public void loadPinyinMapping() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new BufferedInputStream(Objects.requireNonNull(getClass().getResourceAsStream(PINYIN_MAPPING_FILE))),
                    StandardCharsets.UTF_8));
            String line;
            while (null != (line = in.readLine())) {
                if (line.length() == 0 || line.startsWith(SHARP)) {
                    continue;
                }
                String[] pair = line.split(EQUAL);
                if (pair.length == 2 && StringUtil.isNotBlank(pair[1])) {
                    polyPhoneDict.add(pair[0], pair[1].split(","));
                }

            }

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<String> convert(String str, PinyinFormatter.TYPE format) {

        if (StringUtil.isBlank(str)) {
            return Collections.emptyList();
        }

        SmartGetWord<String[]> word = polyPhoneDict.getWord(str);

        List<String> lists = new LinkedList<>();

        String temp = null;
        int beginOffe = 0;
        while ((temp = word.getFrontWords()) != null) {

            for (int i = beginOffe; i < word.offe; i++) {
                lists.add(null);
            }

            if (temp.length() == 1) { //多音字用" "链接
                String[] filterParam = filterParams(word.getParam(), format);
                lists.add(String.join(" ", filterParam));

            } else {
                for (String t : word.getParam()) {
                    lists.add(PinyinFormatter.formatPinyin(t, format));

                }
            }

            beginOffe = word.offe + temp.length();
        }

        if (beginOffe < str.length()) {
            for (int i = beginOffe; i < str.length(); i++) {
                lists.add(null);
            }
        }


        return lists;

    }

    public String[] filterParams(String[] params, PinyinFormatter.TYPE format) {

        Set<String> set = new LinkedHashSet<>();
        List<String> newParams = new ArrayList<>();

        for (String param : params) {
            int lenBefore = set.size();
            String formatParam = PinyinFormatter.formatPinyin(param, format);
            set.add(formatParam);
            if (lenBefore != set.size()) {
                newParams.add(formatParam);
            }
        }
        return newParams.toArray(new String[0]);
    }


}
