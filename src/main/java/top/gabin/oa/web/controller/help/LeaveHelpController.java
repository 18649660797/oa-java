/**
 * Copyright (c) 2016 云智盛世
 * Created with LeaveHelpController.
 */
package top.gabin.oa.web.controller.help;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.gabin.oa.web.constant.attendance.LeaveTypeEnum;
import top.gabin.oa.web.dto.form.EditLeaveTypeForm;
import top.gabin.oa.web.entity.LeaveImpl;
import top.gabin.oa.web.service.LeaveService;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.utils.RenderUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * @author linjiabin  on  16/3/31
 */
@Controller("leaveHelpController")
@RequestMapping("/help/leave")
public class LeaveHelpController {
    @Resource(name = "criteriaQueryService")
    private CriteriaQueryService criteriaQueryService;
    @Resource(name = "leaveService")
    private LeaveService leaveService;
    private String dir = "help/leave";

    @RequestMapping("/list")
    public String list(Model model) {
        model.addAttribute("leaveTypeEnums", LeaveTypeEnum.values());
        return  dir + "/list";
    }

    @RequestMapping("/edit")
    public String edit(Model model, Long id) {
        if (id != null) {
            model.addAttribute("entity", leaveService.findById(id));
        }
        model.addAttribute("leaveTypeEnums", LeaveTypeEnum.values());
        return  dir + "/edit";
    }

    @RequestMapping(value = "grid", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> grid(HttpServletRequest request) {
        return criteriaQueryService.queryPage(LeaveImpl.class, request, "id,beginDate,endDate,type.label type,employee.name realName,employee.department.name department,remark");
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> saveRule(EditLeaveTypeForm form) {
//        leaveService.editSave(form);
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> saveRule(String ids) {
        try {
            leaveService.batchDelete(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return RenderUtils.getFailMap("删除的类型中已有数据关联,无法删除");
        }
        return RenderUtils.SUCCESS_RESULT;
    }
}
