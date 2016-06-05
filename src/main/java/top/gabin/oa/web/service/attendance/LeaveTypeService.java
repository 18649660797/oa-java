package top.gabin.oa.web.service.attendance;

import top.gabin.oa.web.dto.form.EditLeaveTypeForm;
import top.gabin.oa.web.entity.LeaveTypeCustom;

/**
 * @author linjiabin  on  16/3/31
 */
public interface LeaveTypeService {
    LeaveTypeCustom findById(Long id);

    LeaveTypeCustom merge(LeaveTypeCustom leaveType);

    void editSave(EditLeaveTypeForm form);

    void delete(Long id);

    void batchDelete(String ids);

}
