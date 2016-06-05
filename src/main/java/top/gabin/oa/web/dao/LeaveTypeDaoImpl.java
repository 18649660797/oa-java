package top.gabin.oa.web.dao;

import org.springframework.stereotype.Repository;
import top.gabin.oa.web.entity.LeaveTypeCustom;
import top.gabin.oa.web.entity.LeaveTypeCustomImpl;

/**
 * @author linjiabin  on  16/3/31
 */
@Repository("leaveTypeDao")
public class LeaveTypeDaoImpl extends CommonBaseDaoImpl<LeaveTypeCustom, LeaveTypeCustomImpl> implements LeaveTypeDao {
}
