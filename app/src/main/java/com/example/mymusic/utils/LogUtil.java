package com.example.mymusic.utils;

/**
 * Created by wn123 on 2017/2/28.
 */

public enum LogUtil {
        itSelf;

        LogUtil() {
            System.out.println("开始测试");
        }

        public void Log(String msg) {
            System.out.println(msg);
        }
}
