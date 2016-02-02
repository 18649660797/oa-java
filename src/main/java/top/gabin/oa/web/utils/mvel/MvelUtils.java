/**
 * Copyright (c) 2015 云智盛世
 * Created with MvelUtils.
 */
package top.gabin.oa.web.utils.mvel;

import org.mvel2.MVEL;
import top.gabin.oa.web.entity.AttendanceRule;

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

    public static String eval(AttendanceRule attendanceRule) {
        long beginTime = attendanceRule.getBeginDate().getTime();
        long endTime = attendanceRule.getEndDate().getTime();
        switch (attendanceRule.getType()) {
            case WORK_FIT:
            case LEAVE_FIT:
            case WORK_UN_FIT:
            case LEAVE_UN_FIT:
                return "attendance.workDate.getTime() <= " + endTime + "&&" + "attendance.workDate.getTime() >= " + beginTime;
        }
        return "";
    }


}
