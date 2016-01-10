/**
 * Copyright (c) 2015 云智盛世
 * Created with BaseTest.
 */
package top.gabin.oa;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author linjiabin  on  15/12/23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(value = {
//    "file:src/main/webapp/WEB-INF/log4j.xml",
    "file:src/main/webapp/WEB-INF/applicationContext.xml",
    "file:src/main/webapp/WEB-INF/applicationContext-datasource-test.xml",
//    "file:src/main/webapp/WEB-INF/applicationContext-datasource.xml",
    "file:src/main/webapp/WEB-INF/applicationContext-flow.xml",
    "file:src/main/webapp/WEB-INF/applicationContext-security.xml",
    "file:src/main/webapp/WEB-INF/applicationContext-email.xml",
    "file:src/main/webapp/WEB-INF/applicationContext-filter.xml",
    "file:src/main/webapp/WEB-INF/applicationContext-servlet.xml",
    "file:src/main/webapp/WEB-INF/ehcache-application.xml",
//    "file:src/main/webapp/WEB-INF/jetty-web.xml",
//    "file:src/main/webapp/WEB-INF/jeøtty-env.xml"
})
@Transactional
public class BaseTest {

}
