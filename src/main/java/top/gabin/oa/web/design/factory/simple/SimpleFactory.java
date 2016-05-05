/**
 * Copyright (c) 2016 云智盛世
 * Created with SimpleFactory.
 */
package top.gabin.oa.web.design.factory.simple;

/**
 * 食物简单工厂类
 * @author linjiabin on  16/4/30
 */
public class SimpleFactory {

    public static Food createFood(String foodName) {
        if (foodName != null) {
            if (foodName.equals("Noodles")) {
                return new Noodles();
            } else if (foodName.equals("Rice")) {
                return new Rice();
            }
        }
        return null;
    }

}
