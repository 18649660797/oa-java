/**
 * Copyright (c) 2015 云智盛世
 * Created with MvelTests.
 */
package top.gabin.oa.web.utils.mvel;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author linjiabin  on  15/12/22
 */
public class MvelTests {
    protected static final Logger logger = LoggerFactory.getLogger(MvelTests.class);
    @Test
    public void testExecute() {
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("a", 2);
        vars.put("b", 1);
        assert MvelUtils.eval("a > b", vars);
    }

}
