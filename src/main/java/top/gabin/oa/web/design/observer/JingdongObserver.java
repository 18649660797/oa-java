/**
 * Copyright (c) 2016 云智盛世
 * Created with JingdongObserver.
 */
package top.gabin.oa.web.design.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * 京东订阅者
 * @author linjiabin on  16/4/29
 */
public class JingdongObserver implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        System.out.print("I'm 京东");
        System.out.println(arg);
    }
}
