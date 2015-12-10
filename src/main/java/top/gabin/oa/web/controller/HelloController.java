package top.gabin.oa.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.gabin.oa.web.entity.Employee;
import top.gabin.oa.web.entity.EmployeeImpl;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.service.criteria.dto.PageDTO;

import javax.annotation.Resource;

@Controller
@RequestMapping("/")
public class HelloController {
	@Resource
	private CriteriaQueryService criteriaQueryService;
	@RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		PageDTO<EmployeeImpl> employeePageDTO = criteriaQueryService.queryPage(EmployeeImpl.class, new CriteriaCondition());
		for (Employee employee : employeePageDTO.getContent()) {
			System.out.println(employee.getRealName());
		}
		return "hello";
	}
}