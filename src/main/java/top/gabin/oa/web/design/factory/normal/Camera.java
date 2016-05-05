/**
 * Copyright (c) 2016 云智盛世
 * Created with Camera.
 */
package top.gabin.oa.web.design.factory.normal;

/**
 * 摄像头
 * @author linjiabin on  16/4/30
 */
public class Camera implements Component {
    @Override
    public void assembly(Phone phone) {
        phone.setCamera(this);
    }
}
