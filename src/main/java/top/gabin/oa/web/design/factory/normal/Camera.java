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
    // 像素
    private int pixel;

    public int getPixel() {
        return pixel;
    }

    public void setPixel(int pixel) {
        this.pixel = pixel;
    }

    @Override
    public void assembly(Phone phone) {
        phone.setCamera(this);
    }
}
