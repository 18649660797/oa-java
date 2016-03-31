package top.gabin.oa.web.service.attendance;

import top.gabin.oa.web.dto.form.EditLeaveTypeForm;
import top.gabin.oa.web.entity.LeaveType;

/**
 * @author linjiabin  on  16/3/31
 */
public interface LeaveTypeService {
    LeaveType findById(Long id);

    LeaveType merge(LeaveType leaveType);

    void editSave(EditLeaveTypeForm form);

    void delete(Long id);

    void batchDelete(String ids);

}
