/**
 * Copyright (c) 2015 云智盛世
 * Created with MvelUtils.
 */
package top.gabin.oa.web.utils.mvel;

import org.mvel2.MVEL;

import java.util.Map;

/**
 *
 * @author linjiabin  on  15/12/21
 */
public class MvelUtils {
    public static boolean eval(String expression, Map vars) {
        Boolean result = (Boolean) MVEL.eval(expression, vars);
        return result;
    }
}
