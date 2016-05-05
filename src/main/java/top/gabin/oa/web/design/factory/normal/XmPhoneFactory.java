/**
 * Copyright (c) 2016 云智盛世
 * Created with XmPhoneFactory.
 */
package top.gabin.oa.web.design.factory.normal;

import java.util.List;

/**
 *
 * @author linjiabin on  16/4/30
 */
public class XmPhoneFactory extends PhoneFactory {

    @Override
    public Phone createPhone() {
        Phone phone = new Phone() {
        };
        // 获取所有元件,
        List<Component> componentList = getComponentList();
        for (Component component : componentList) {
            // 组装
            component.assembly(phone);
        }
        return phone;
    }

}
