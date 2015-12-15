package top.gabin.oa.web.dao;

import top.gabin.oa.web.entity.Leave;
import top.gabin.oa.web.entity.LeaveImpl;

/**
 * @author linjiabin  on  15/12/14
 */
public interface LeaveDao extends  CommonBaseDao<Leave, LeaveImpl> {
    /**
     * 清除月份的请假数据
     * @param month
     */
    void clearMonth(String month);
}
