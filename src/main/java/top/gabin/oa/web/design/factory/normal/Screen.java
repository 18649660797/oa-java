/**
 * Copyright (c) 2016 云智盛世
 * Created with Screen.
 */
package top.gabin.oa.web.design.factory.normal;

/**
 * 屏幕
 * @author linjiabin on  16/4/30
 */
public class Screen implements Component {
    @Override
    public void assembly(Phone phone) {
        phone.setScreen(this);
    }
}
