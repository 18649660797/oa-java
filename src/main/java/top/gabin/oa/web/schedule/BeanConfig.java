/**
 * Copyright (c) 2016 云智盛世
 * Created with BeanConfig.
 */
package top.gabin.oa.web.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Class description
 *
 *
 *
 * @author linjiabin on  16/4/18
 */
@ImportResource({"applicationContext-quartz-cron-local.xml"})
public class BeanConfig {
    //autowired from xml
    @Autowired JobDetailFactoryBean jobDetailFactory;
    @Autowired CronTriggerFactoryBean cronTriggerFactory;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(LocalContainerEntityManagerFactoryBean entityManagerFactory) {

        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setApplicationContextSchedulerContextKey("applicationContext");

        bean.setSchedulerName("MyScheduler");

        //used for the wiring
        Map<String, Object> schedulerContextAsMap = new HashMap<String, Object>();
        bean.setSchedulerContextAsMap(schedulerContextAsMap);
//        bean.setQuartzProperties(quartzProperties());

        return bean;
    }

}
