package top.gabin.oa.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.gabin.oa.web.dto.menus.MenusDTO;
import top.gabin.oa.web.service.MenusService;
import top.gabin.oa.web.utils.AuthUtils;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import java.util.List;

@Controller
@RequestMapping()
public class IndexController {
	@Resource(name = "menusService")
	private MenusService menusService;
	@RequestMapping(method = RequestMethod.GET, value = {"", "/index"})
	public String printWelcome(Model model) {
		List<MenusDTO> menusDTOList = menusService.findTopMenusByAdminName(AuthUtils.getCurrentLoginUserName());
		model.addAttribute("menus", menusDTOList);
		return "main";
	}
}