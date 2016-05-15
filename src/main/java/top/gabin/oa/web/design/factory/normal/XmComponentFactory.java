/**
 * Copyright (c) 2016 云智盛世
 * Created with XmComponentFactory.
 */
package top.gabin.oa.web.design.factory.normal;

/**
 * 厦门元件工厂
 * @author linjiabin on  16/5/8
 */
public class XmComponentFactory extends ComponentFactory {
    @Override
    Component createScreen() {
        Screen screen = new Screen();
        screen.setBrand("三星");
        return screen;
    }

    @Override
    Component createCamera() {
        Camera camera = new Camera();
        // 1200万像素
        camera.setPixel(1200 * 10000);
        return null;
    }
}
