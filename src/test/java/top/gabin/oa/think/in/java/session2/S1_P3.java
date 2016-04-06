package top.gabin.oa.think.in.java.session2;

import org.junit.Test;

/**
 * Created by linjiabin on 16/4/6.
 */
public class S1_P3 {

    class ATypeName {
        public void talk () {
            System.out.println("I'm only a new type.I don't know what can I do for your!");
        }
    }

    @Test
    public void TestATypeNameInstance() {
        ATypeName aTypeName = new ATypeName();
        aTypeName.talk();
    }

}
