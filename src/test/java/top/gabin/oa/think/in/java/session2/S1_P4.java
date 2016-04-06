package top.gabin.oa.think.in.java.session2;

import org.junit.Test;

/**
 * Created by linjiabin on 16/4/6.
 */
public class S1_P4 {
    class DataOnly {
        int i;
        double d;
        boolean b;

        @Override
        public String toString() {
            return "DataOnly{" +
                    "i=" + i +
                    ", d=" + d +
                    ", b=" + b +
                    '}';
        }
    }

    @Test
    public void testDataOnly() {
        DataOnly dataOnly = new DataOnly();
        dataOnly.b = true;
        dataOnly.d = 1314;
        dataOnly.i = 520;
        System.out.println(dataOnly);
    }

}
