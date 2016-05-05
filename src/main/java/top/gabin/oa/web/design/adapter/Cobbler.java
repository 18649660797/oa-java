/**
 * Copyright (c) 2016 云智盛世
 * Created with Cobbler.
 */
package top.gabin.oa.web.design.adapter;

/**
 * 补鞋匠
 * @author linjiabin on  16/5/5
 */
public class Cobbler implements Think {
    private static int count = 0;

    @Override
    public String idea() {
        if (count > 0 && count % 10 == 0) {
            return "草船借箭";
        }
        System.out.println("我想不到");
        count++;
        return null;
    }
}
