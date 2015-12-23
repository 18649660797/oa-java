/**
 * Copyright (c) 2015 云智盛世
 * Created with TestMvel.
 */
package top.gabin.oa.web.test.utils.mvel;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * @author linjiabin  on  15/12/22
 */
public class TestMvel {

    @Test
    public void testExecute() {
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("a", 2);
        vars.put("b", 1);
        assert MvelUtils.eval("a > b", vars);
        System.out.println("测试成功");
    }

}
