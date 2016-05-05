/**
 * Copyright (c) 2016 云智盛世
 * Created with TestObserver.
 */
package top.gabin.oa.web.design.observer;

import org.junit.Test;

/**
 * 测试类
 * @author linjiabin on  16/4/29
 */
public class TestObserver {

    @Test
    public void testObserver() {
        Kuaidi100Subject subject = new Kuaidi100Subject();
        TaobaoObserver taobaoObserver = new TaobaoObserver();
        subject.addObserver(taobaoObserver);
        subject.addObserver(new JingdongObserver());
        subject.setChanged();
        subject.notifyObservers("你的货已经到了");
    }

}
