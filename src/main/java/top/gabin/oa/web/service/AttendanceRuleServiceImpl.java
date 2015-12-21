/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceRuleServiceImpl.
 */
package top.gabin.oa.web.service;

import org.springframework.stereotype.Service;
import top.gabin.oa.web.entity.AttendanceRuleImpl;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linjiabin  on  15/12/21
 */
@Service("attendanceRuleService")
public class AttendanceRuleServiceImpl implements AttendanceRuleService {
    @Resource
    private CriteriaQueryService queryService;
    @Override
    public List<AttendanceRuleImpl> findRulesByMonth(String month) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ge_beginDate", month + "-01 00:00:00");
        params.put("le_endDate", month + "-31 23:59:59");
        CriteriaCondition criteriaCondition = new CriteriaCondition(params);
        return queryService.query(AttendanceRuleImpl.class, criteriaCondition);
    }
}
