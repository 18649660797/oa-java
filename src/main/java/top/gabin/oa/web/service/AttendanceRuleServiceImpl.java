/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceRuleServiceImpl.
 */
package top.gabin.oa.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.oa.web.constant.AttendanceRuleType;
import top.gabin.oa.web.constant.ConfigStatus;
import top.gabin.oa.web.dao.AttendanceRuleDao;
import top.gabin.oa.web.dao.AttendanceRuleDetailDao;
import top.gabin.oa.web.dto.form.EditAttendanceRuleForm;
import top.gabin.oa.web.entity.*;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.utils.date.TimeUtils;
import top.gabin.oa.web.utils.mvel.MvelUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linjiabin  on  15/12/21
 */
@Service("attendanceRuleService")
public class AttendanceRuleServiceImpl implements AttendanceRuleService {
    @Resource(name = "attendanceRuleDao")
    private AttendanceRuleDao attendanceRuleDao;
    @Resource(name = "attendanceRuleDetailDao")
    private AttendanceRuleDetailDao attendanceRuleDetailDao;
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

    @Transactional("transactionManager")
    @Override
    public void setAttendanceRule(EditAttendanceRuleForm form) {
        if (form != null) {
            Long id = form.getId();
            AttendanceRule attendanceRule;
            if (id != null) {
                attendanceRule = attendanceRuleDao.findById(id);
            } else {
                attendanceRule = new AttendanceRuleImpl();
            }
            attendanceRule.setBeginDate(TimeUtils.parseDate(form.getBeginDate(), "yyyy-MM-dd"));
            attendanceRule.setEndDate(TimeUtils.parseDate(form.getEndDate(), "yyyy-MM-dd"));
            attendanceRule.setType(AttendanceRuleType.instance(form.getType()));
            attendanceRule.setStatus(ConfigStatus.instance(form.getStatus()));
            attendanceRule.setExtraData(form.getData());
            attendanceRule.setName(form.getName());
            Map<String, AttendanceRuleDetail> attendanceRuleDetailMap = attendanceRule.getAttendanceRuleDetailMap();
            AttendanceRuleType attendanceRuleType = AttendanceRuleType.instance(form.getType());
            AttendanceRuleDetail attendanceRuleDetail;
            String ruleType = attendanceRuleType.toString();
            String rule = MvelUtils.eval(attendanceRule);
            if (attendanceRuleDetailMap.containsKey(ruleType)) {
                attendanceRuleDetail = attendanceRuleDetailMap.get(ruleType);
                attendanceRuleDetail.setRule(rule);
            } else {
                attendanceRuleDetail = new AttendanceRuleDetailImpl();
                attendanceRuleDetail.setRule(rule);
                attendanceRuleDetailDao.persist(attendanceRuleDetail);
                attendanceRuleDetailMap.put(ruleType, attendanceRuleDetail);
            }
            attendanceRuleDao.persist(attendanceRule);
        }
    }

    @Override
    public AttendanceRule findById(Long id) {
        return attendanceRuleDao.findById(id);
    }

    @Transactional("transactionManager")
    @Override
    public void batchDelete(String ids) {
        if (StringUtils.isNotBlank(ids)) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("in_id", ids);
            CriteriaCondition condition = new CriteriaCondition();
            condition.setConditions(params);
            List<AttendanceRuleImpl> attendanceRuleList = queryService.query(AttendanceRuleImpl.class, condition);
            if (attendanceRuleList == null) {
                return;
            }
            for (AttendanceRule rule : attendanceRuleList) {
                Map<String, AttendanceRuleDetail> attendanceRuleDetailMap = rule.getAttendanceRuleDetailMap();
                for (String key : attendanceRuleDetailMap.keySet()) {
                    attendanceRuleDetailDao.delete(attendanceRuleDetailMap.get(key));
                }
                attendanceRuleDao.delete(rule);
            }
        }
    }

}
