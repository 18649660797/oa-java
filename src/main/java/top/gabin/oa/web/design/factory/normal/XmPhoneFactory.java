/**
 * Copyright (c) 2016 云智盛世
 * Created with XmPhoneFactory.
 */
package top.gabin.oa.web.design.factory.normal;

/**
 * 厦门手机组装工厂
 * @author linjiabin on  16/4/30
 */
public class XmPhoneFactory extends PhoneFactory {

    @Override
    public Phone createPhone() {
        Phone phone = new Phone() {
        };
        // 我希望使用厦门自己的元件工厂
        setComponentFactory(new XmComponentFactory());
        // 根据工厂抽象父类的组装方式,由于创建手机的方法是子类自己实现的,所以这里也可以自己组装,但是不让覆盖
        buildPhone(phone);
        return phone;
    }

}
