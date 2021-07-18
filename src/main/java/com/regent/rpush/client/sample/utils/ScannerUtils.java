package com.regent.rpush.client.sample.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Scanner;

/**
 * 输入相关
 *
 * @author 钟宝林
 * @since 2021/7/13/013 17:54
 **/
public final class ScannerUtils {

    public static String nextLine() {
        return nextLine("");
    }

    public static String nextLine(String tag) {
        if (StringUtils.isNotBlank(tag)) {
            System.out.print(tag);
        }
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

}
