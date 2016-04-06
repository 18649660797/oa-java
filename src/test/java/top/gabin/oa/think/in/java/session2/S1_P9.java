package top.gabin.oa.think.in.java.session2;

import org.junit.Test;

/**
 * Created by linjiabin on 16/4/6.
 */
public class S1_P9 {

    @Test
    public void testAutoPackType() {
        Byte b = 1;
        Byte b1 = b;
        System.out.println("byte:" + b1);
        Short short_ = 2;
        Short short_2 = short_;
        System.out.println("short:" + short_2);
    }

}
