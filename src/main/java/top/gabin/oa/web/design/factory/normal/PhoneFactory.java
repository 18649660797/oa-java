/**
 * Copyright (c) 2016 云智盛世
 * Created with PhoneFactory.
 */
package top.gabin.oa.web.design.factory.normal;

import java.util.ArrayList;
import java.util.List;

/**
 * 手机组装工厂
 * @author linjiabin on  16/4/30
 */
public abstract class PhoneFactory {
    // 元件工厂, 这边直接初始化, 默认是泉州元件工厂
    private ComponentFactory componentFactory = new QzCompontFactory();
    // 元件
    private List<Component> componentList = new ArrayList<>();

    // 获取组件
    protected final List<Component> getComponentList() {
        componentList.add(componentFactory.createScreen());
        componentList.add(componentFactory.createCamera());
        return componentList;
    }

    public void setComponentFactory(ComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
    }

    // 订购手机
    protected final Phone orderPhone() {
        Phone phone = createPhone();
        return phone;
    }

    // 生产手机
    public abstract Phone createPhone();

    protected final void buildPhone(Phone phone) {
        // 获取所有元件,
        List<Component> componentList = getComponentList();
        for (Component component : componentList) {
            // 组装
            component.assembly(phone);
        }
    }

}
