/**
 * Copyright (c) 2015 云智盛世
 * Created with BaseTest.
 */
package top.gabin.oa;

import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
    @Resource(name = "dataSource")
    private DataSource dataSource;
    @Before
    public void before() {
        try {
            Flyway flyway = new Flyway();
            flyway.setBaselineOnMigrate(true);//指定生成一条初始版本的记录
            flyway.setBaselineVersion("2015.01.01.01.01.00");
            flyway.setBaselineDescription("初始版本");
            flyway.setValidateOnMigrate(true);
            flyway.setOutOfOrder(true);//指定如果有版本比现在早的也同时执行
            flyway.setEncoding("utf-8");
            flyway.setDataSource(dataSource);
            flyway.migrate();
        } catch (Exception e) {
            throw new RuntimeException("执行数据库更新时出错。请处理，不然启动不了",e);
        }
    }

    @After
    public void after() throws SQLException {

    }

}
