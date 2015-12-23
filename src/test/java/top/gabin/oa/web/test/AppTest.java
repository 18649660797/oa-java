/**
 * Copyright (c) 2015 云智盛世
 * Created with AppTest.
 */
package top.gabin.oa.web.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gabin.oa.web.entity.EmployeeImpl;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;

import javax.annotation.Resource;

/**
 * Class description
 *
 *
 *
 * @author linjiabin  on  15/12/23
 */
public class AppTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(AppTest.class);
    @Resource
    private CriteriaQueryService queryService;

    @Test
    public void testFind() {
        Long count = queryService.count(EmployeeImpl.class, new CriteriaCondition());
        assert count >= 0;
        logger.info("employeeSize:" + count);
    }
}
