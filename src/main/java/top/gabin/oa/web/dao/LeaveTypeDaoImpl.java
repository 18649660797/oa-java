package top.gabin.oa.web.dao;

import org.springframework.stereotype.Repository;
import top.gabin.oa.web.entity.LeaveType;
import top.gabin.oa.web.entity.LeaveTypeImpl;

/**
 * @author linjiabin  on  16/3/31
 */
@Repository("leaveTypeDao")
public class LeaveTypeDaoImpl extends CommonBaseDaoImpl<LeaveType, LeaveTypeImpl> implements LeaveTypeDao {
}
