/**
 * Copyright (c) 2016 云智盛世
 * Created with TestAdapter.
 */
package top.gabin.oa.web.design.adapter;

import org.junit.Test;

/**
 * 测试适配器的效果
 *
 * @author linjiabin on  16/5/5
 */
public class TestAdapter {

    @Test
    public void testAdapter() {
        Cobbler cobbler = new Cobbler();
        VariousGeBright variousGeBright = new SmartAdapter(cobbler);
        String idea = variousGeBright.idea();
        System.out.println(idea);
    }

}
