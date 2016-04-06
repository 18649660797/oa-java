package top.gabin.oa.think.in.java.session2;

import org.junit.Test;

/**
 * Created by linjiabin on 16/4/6.
 */
public class S1_P8 {

    @Test
    public void testSingleInstance() {
        S1_P7 s1P7 = new S1_P7();
        s1P7.testIncrement();
    }

}
