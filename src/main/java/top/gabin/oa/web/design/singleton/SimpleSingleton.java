/**
 * Copyright (c) 2016 云智盛世
 * Created with SimpleSingleton.
 */
package top.gabin.oa.web.design.singleton;

/**
 * 简单单例模式示例
 * @author linjiabin on  16/5/4
 */
public class SimpleSingleton {
    private static Object singleton = new Object();
    private static Object singleton2;

    public static Object getSingleton() {
        return singleton;
    }

    public static Object getSingleton2() {
        if (singleton2 == null) {
            singleton2 = new Object();
        }
        return singleton2;
    }

}
