/**
 * Copyright (c) 2016 云智盛世
 * Created with TaobaoObserver.
 */
package top.gabin.oa.web.design.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * 淘宝订阅者
 * @author linjiabin on  16/4/29
 */
public class TaobaoObserver implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        System.out.print("I'm 淘宝");
        System.out.println(arg);
    }
}
