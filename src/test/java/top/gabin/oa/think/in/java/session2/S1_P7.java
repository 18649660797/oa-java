package top.gabin.oa.think.in.java.session2;

import org.junit.Test;

/**
 * Created by linjiabin on 16/4/6.
 */
public class S1_P7 {

    @Test
    public void testIncrement() {
        IncrementAble incrementAble = new IncrementAble();
        IncrementAble incrementAble1 = new IncrementAble();
        incrementAble.increment();
        System.out.println(incrementAble1.i);
        incrementAble1.increment();
        System.out.println(incrementAble.i);
    }

}
