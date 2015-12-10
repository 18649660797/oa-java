package top.gabin.oa.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HelloController {
	private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
	@RequestMapping(method = RequestMethod.GET, value = "hello")
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		logger.info("hello");
		return "hello";
	}
}