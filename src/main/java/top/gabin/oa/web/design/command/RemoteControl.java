/**
 * Copyright (c) 2016 云智盛世
 * Created with RemoteControl.
 */
package top.gabin.oa.web.design.command;

/**
 * 遥控器
 * @author linjiabin on  16/5/2
 */
public class RemoteControl {
    // 最简单的单例模式
    private static Tv tv = new LeshiTv();

    public void powerButtonOn() {
        PowerCommand powerCommand = new PowerCommand(tv);
        powerCommand.execute();
    }

    public void powerButtonOff() {
        PowerCommand powerCommand = new PowerCommand(tv);
        powerCommand.undo();
    }

}
