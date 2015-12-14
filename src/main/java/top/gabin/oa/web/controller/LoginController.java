/**
 * Copyright (c) 2015 云智盛世
 * Created with LoginController.
 */
package top.gabin.oa.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.gabin.oa.web.utils.AuthUtils;

/**
 * @author linjiabin  on  15/12/14
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private String dir = "auth";
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String login() {
        logger.info(AuthUtils.getCurrentLoginUserName());
        return dir + "/login";
    }
}
