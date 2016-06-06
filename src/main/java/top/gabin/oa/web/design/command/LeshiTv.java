/**
 * Copyright (c) 2016 云智盛世
 * Created with LeshiTv.
 */
package top.gabin.oa.web.design.command;

/**
 * 乐视TV
 * @author linjiabin on  16/5/2
 */
public class LeshiTv implements Tv {
    @Override
    public void powerOn() {
        System.out.println("打开电视");
    }

    @Override
    public void powerOff() {
        System.out.println("关掉电视");
    }
}
