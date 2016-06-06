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
import top.gabin.oa.web.dto.LeaveDTO;
import top.gabin.oa.web.dto.PageDTO;
import top.gabin.oa.web.entity.Department;
import top.gabin.oa.web.entity.Employee;
import top.gabin.oa.web.entity.LeaveImpl;
import top.gabin.oa.web.entity.LeaveTypeCustom;
import top.gabin.oa.web.service.DepartmentService;
import top.gabin.oa.web.service.EmployeeService;
import top.gabin.oa.web.service.LeaveService;
import top.gabin.oa.web.service.attendance.LeaveTypeService;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.service.criteria.CriteriaQueryUtils;
import top.gabin.oa.web.utils.AuthUtils;
import top.gabin.oa.web.utils.RenderUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
    @Resource(name = "employeeService")
    private EmployeeService employeeService;
    @Resource(name = "departmentService")
    private DepartmentService departmentService;
    @Resource(name = "leaveTypeService")
    private LeaveTypeService leaveTypeService;
    private String dir = "help/leave";

    @RequestMapping("/list")
    public String list(Model model) {
        model.addAttribute("leaveTypeEnums", LeaveTypeEnum.values());
        List<Department> departmentList = departmentService.findAll();
        model.addAttribute("departmentList", departmentList);
        List<LeaveTypeCustom> typeCustomList = leaveTypeService.findAll();
        model.addAttribute("typeCustomList", typeCustomList);
        return  dir + "/list";
    }

    @RequestMapping("/edit")
    public String edit(Model model, Long id) {
        if (id != null) {
            model.addAttribute("entity", leaveService.findById(id));
        }
        String cn = AuthUtils.getCurrentLoginUserName();
        Employee employee = employeeService.findByAttendanceCN(cn);
        model.addAttribute("employee", employee);
        model.addAttribute("leaveTypeEnums", LeaveTypeEnum.values());
        List<LeaveTypeCustom> typeCustomList = leaveTypeService.findAll();
        model.addAttribute("typeCustomList", typeCustomList);
        return  dir + "/edit";
    }

    @RequestMapping(value = "grid", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> grid(HttpServletRequest request) {
        String currentLoginUserName = AuthUtils.getCurrentLoginUserName();
        CriteriaCondition criteriaCondition = CriteriaQueryUtils.parseCondition(request, "eq_employee.attendanceCN", currentLoginUserName);
        PageDTO<LeaveImpl> adminPageDTO = criteriaQueryService.queryPage(LeaveImpl.class, criteriaCondition);
        return RenderUtils.filterPageDataResult(adminPageDTO, "id,beginDate,endDate,leaveTypeCustom.label type,employee.name realName,employee.department.name department,remark");
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveRule(LeaveDTO leaveDTO) {
        leaveService.merge(leaveDTO);
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveRule(String ids) {
        try {
            leaveService.batchDelete(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return RenderUtils.getFailMap("删除的类型中已有数据关联,无法删除");
        }
        return RenderUtils.SUCCESS_RESULT;
    }
}
