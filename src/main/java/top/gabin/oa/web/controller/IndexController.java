package top.gabin.oa.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.gabin.oa.web.dto.menus.MenusDTO;
import top.gabin.oa.web.entity.LeaveTypeCustom;
import top.gabin.oa.web.service.MenusService;
import top.gabin.oa.web.service.attendance.LeaveTypeService;
import top.gabin.oa.web.utils.AppUtils;
import top.gabin.oa.web.utils.AuthUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping()
public class IndexController {
	@Resource(name = "menusService")
	private MenusService menusService;
	@Resource(name = "leaveTypeService")
	private LeaveTypeService leaveTypeService;
	@RequestMapping(method = RequestMethod.GET, value = {"", "/index"})
	public String printWelcome(HttpServletRequest request, Model model) {
		if (AppUtils.isMobileBrowser(request)) {
			List<LeaveTypeCustom> typeCustoms = leaveTypeService.findAll();
			model.addAttribute("typeCustoms", typeCustoms);
			return "help/index";
		}
		List<MenusDTO> menusDTOList = menusService.findTopMenusByAdminName(AuthUtils.getCurrentLoginUserName());
		model.addAttribute("menus", menusDTOList);
		return "main";
	}
}