/**
 * Copyright (c) 2016 云智盛世
 * Created with TestSimpleFactory.
 */
package top.gabin.oa.web.design.factory;

import org.junit.Test;
import top.gabin.oa.web.design.factory.simple.Food;
import top.gabin.oa.web.design.factory.simple.SimpleFactory;

/**
 * 简单工厂测试类
 * @author linjiabin on  16/4/30
 */
public class TestSimpleFactory {

    @Test
    public void testSimple() {
        Food noodles = SimpleFactory.createFood("Noodles");
        if (noodles != null) {
            System.out.println(noodles.getName());
        }
    }
}
