/**
 * Copyright (c) 2016 云智盛世
 * Created with LeaveTypeServiceImpl.
 */
package top.gabin.oa.web.service.attendance;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.oa.web.constant.attendance.LeaveTypeEnum;
import top.gabin.oa.web.dao.LeaveTypeDao;
import top.gabin.oa.web.dto.form.EditLeaveTypeForm;
import top.gabin.oa.web.entity.*;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author linjiabin  on  16/3/31
 */
@Service("leaveTypeService")
public class LeaveTypeServiceImpl implements LeaveTypeService {

    @Resource(name = "criteriaQueryService")
    private CriteriaQueryService criteriaQueryService;
    @Resource(name = "leaveTypeDao")
    private LeaveTypeDao leaveTypeDao;

    @Override
    public LeaveType findById(Long id) {
        return leaveTypeDao.findById(id);
    }

    @Override
    @Transactional("transactionManager")
    public LeaveType merge(LeaveType leaveType) {
        return leaveTypeDao.saveOrUpdate(leaveType);
    }

    @Override
    @Transactional("transactionManager")
    public void editSave(EditLeaveTypeForm form) {
        if (form != null) {
            Long id = form.getId();
            LeaveType leaveType = findById(id);
            if (leaveType == null) {
                leaveType = new LeaveTypeImpl();
            }
            leaveType.setLabel(form.getLabel());
            leaveType.setType(LeaveTypeEnum.instance(form.getType()));
            leaveTypeDao.saveOrUpdate(leaveType);
        }
    }

    @Override
    @Transactional("transactionManager")
    public void delete(Long id) {
        leaveTypeDao.deleteById(id);
    }

    @Override
    @Transactional("transactionManager")
    public void batchDelete(String ids) {
        if (StringUtils.isNotBlank(ids)) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("in_id", ids);
            CriteriaCondition condition = new CriteriaCondition();
            condition.setConditions(params);
            List<LeaveTypeImpl> leaveTypeList = criteriaQueryService.query(LeaveTypeImpl.class, condition);
            if (leaveTypeList == null) {
                return;
            }
            for (LeaveType leaveType : leaveTypeList) {
                leaveTypeDao.delete(leaveType);
            }
        }
    }
}
