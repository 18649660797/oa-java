/**
 * Copyright (c) 2015 云智盛世
 * Created with EmailController.
 */
package top.gabin.oa.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.gabin.oa.web.utils.RenderUtils;
import top.gabin.oa.web.utils.mail.MailBean;
import top.gabin.oa.web.utils.mail.MailUtil;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * email 中转处理
 * @author linjiabin  on  15/12/21
 */
@Controller
@RequestMapping("/email")
public class EmailController {
    @RequestMapping("/test")
    public @ResponseBody Map<String, Object> test() throws IOException, MessagingException {
        //创建邮件
        MailBean mailBean = new MailBean();
        mailBean.setFrom("18649660797@163.com");
        mailBean.setSubject("hello world");
        mailBean.setToEmails(new String[]{"linjb@ecmaster.cn"});
        mailBean.setTemplate("hello ${userName} !<a href='www.baidu.com' >baidu</a>");
        Map map = new HashMap();
        map.put("userName", "Haley Wang");
        mailBean.setData(map);
        MailUtil.send(mailBean);
        return RenderUtils.SUCCESS_RESULT;
    }
}
