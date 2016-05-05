/**
 * Copyright (c) 2016 云智盛世
 * Created with PowerCommand.
 */
package top.gabin.oa.web.design.command;

/**
 * 电源开关
 * @author linjiabin on  16/5/2
 */
public class PowerCommand implements Command{
    private TV tv;

    public PowerCommand(TV tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.powerOn();
    }

    @Override
    public void undo() {
        tv.powerOff();
    }
}
