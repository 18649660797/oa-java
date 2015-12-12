package top.gabin.oa.web.interceptor;

import freemarker.ext.beans.BeansWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class InitializeInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(InitializeInterceptor.class);
    @Autowired
    private FreeMarkerConfigurer cfg;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();
            if (modelAndView != null) {
                modelAndView.getModel().put("Static", ((BeansWrapper) (cfg.getConfiguration().getObjectWrapper())).getStaticModels());

                //增加原生request
                modelAndView.getModel().put("rawRequest",request);
            }

        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
    }
}
