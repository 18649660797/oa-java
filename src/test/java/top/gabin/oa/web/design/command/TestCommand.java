/**
 * Copyright (c) 2016 云智盛世
 * Created with TestCommand.
 */
package top.gabin.oa.web.design.command;

import org.junit.Test;

/**
 * 命令测试类
 * @author linjiabin on  16/5/2
 */
public class TestCommand {

    @Test
    public void testCommand() {
        RemoteControl remoteControl = new RemoteControl();
        remoteControl.powerButtonOn();
        remoteControl.powerButtonOff();
    }

}
