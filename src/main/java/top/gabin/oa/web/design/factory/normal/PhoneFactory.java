/**
 * Copyright (c) 2016 云智盛世
 * Created with PhoneFactory.
 */
package top.gabin.oa.web.design.factory.normal;

import java.util.ArrayList;
import java.util.List;

/**
 * 手机工厂
 * @author linjiabin on  16/4/30
 */
public abstract class PhoneFactory {
    // 元件
    private List<Component> componentList = new ArrayList<>();

    // 获取组件
    protected final List<Component> getComponentList() {
        componentList.add(new Screen());
        componentList.add(new Camera());
        return componentList;
    }

    // 订购手机
    protected final Phone orderPhone() {
        Phone phone = createPhone();
        return phone;
    }

    // 生产手机
    public abstract Phone createPhone();

}
