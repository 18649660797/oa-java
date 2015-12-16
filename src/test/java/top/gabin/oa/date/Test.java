/**
 * Copyright (c) 2015 云智盛世
 * Created with Test.
 */
package top.gabin.oa.date;

import org.apache.commons.lang3.time.DateUtils;
import top.gabin.oa.web.utils.date.TimeUtils;

import java.util.Date;

/**
 * Class description
 *
 *
 *
 * @author linjiabin  on  15/12/15
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(TimeUtils.parseDate("2015-11-12 09:00:00").getTime() < TimeUtils.parseDate("2015-11-12 12:00:00").getTime());
        System.out.print(TimeUtils.parseDate("2015-11-12 10:00:00").getTime() > TimeUtils.parseDate("2015-11-12 13:30:00").getTime());


    }
}
