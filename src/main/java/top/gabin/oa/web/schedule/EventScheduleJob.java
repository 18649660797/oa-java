/**
 * Copyright (c) 2016 云智盛世
 * Created with EventScheduleJob.
 */
package top.gabin.oa.web.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gabin.oa.web.entity.Department;
import top.gabin.oa.web.service.DepartmentService;
import top.gabin.oa.web.tenant.utils.TenantUtils;
import top.gabin.oa.web.utils.SpringBeanUtils;

import java.util.List;

/**
 * Class description
 *
 *
 *
 * @author linjiabin on  16/4/18
 */
public class EventScheduleJob {
    private static DepartmentService departmentService;
    static {
        departmentService = SpringBeanUtils.getBean(DepartmentService.class);
    }

    private static Logger log = LoggerFactory.getLogger(EventScheduleJob.class);

    public void timingExecuteForEventsJob(){
        TenantUtils.setTenantId(1L);
        List<Department> all = departmentService.findAll();
    }
}
