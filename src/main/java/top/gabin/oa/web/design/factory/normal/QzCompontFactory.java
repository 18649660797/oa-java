/**
 * Copyright (c) 2016 云智盛世
 * Created with QzCompontFactory.
 */
package top.gabin.oa.web.design.factory.normal;

/**
 * 泉州元件工厂
 * @author linjiabin on  16/5/8
 */
public class QzCompontFactory extends ComponentFactory {
    @Override
    Component createScreen() {
        return new Screen();
    }

    @Override
    Component createCamera() {
        return new Camera();
    }
}
