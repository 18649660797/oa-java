/**
 * Copyright (c) 2016 云智盛世
 * Created with ComponentFactory.
 */
package top.gabin.oa.web.design.factory.normal;

/**
 * 抽象工厂:创建一组有关联的产品家族
 * 一般这个产品族是比较固定的,因为想扩展这个抽象类,子类都必须重新覆盖
 * @author linjiabin on  16/5/8
 */
public abstract class ComponentFactory {
    abstract Component createScreen();
    abstract Component createCamera();
}
