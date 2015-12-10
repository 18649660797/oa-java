/**
 * Copyright (c) 2015 云智盛世
 * Created with CriteriaQueryUtils.
 */
package top.gabin.oa.web.service.criteria;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用来组装查询条件Map
 * @author linjiabin  on  15/11/13
 */
public class CriteriaQueryUtils {

    public static CriteriaCondition parseCondition(HttpServletRequest request, Map other) {
        CriteriaCondition condition = new CriteriaCondition();
        Map params = buildPageQueryMap(request, other);
        condition.setConditions(params);
        condition.setDistinct("true".equals(request.getParameter("distinct")));
        condition.setSort(request.getParameter("sort"));
        String limitStr = request.getParameter("limit");
        if (StringUtils.isNotBlank(limitStr) && StringUtils.isNumeric(limitStr)) {
            condition.setLimit(Integer.parseInt(limitStr));
        }
        String startStr = request.getParameter("start");
        if (StringUtils.isNotBlank(startStr) && StringUtils.isNumeric(startStr)) {
            condition.setStart(Integer.parseInt(startStr));
        }
        String targetPath = request.getParameter("targetPath");
        if (StringUtils.isNotBlank(targetPath)) {
            condition.setTargetPath(targetPath);
        }
        condition.setDistinct(Boolean.valueOf(request.getParameter("distinct")));
        return condition;
    }

    public static CriteriaCondition parseCondition(HttpServletRequest request) {
        Map<String, Object> params = null;
        return parseCondition(request, params);
    }

    public static CriteriaCondition parseCondition(HttpServletRequest request, String... other) {
        Map<String, String> otherMap = new HashMap<String, String>();
        if (other != null) {
            int size = other.length / 2;
            for (int i = 0; i < size; i++) {
                otherMap.put(other[i * 2], other[i * 2 + 1]);
            }
        }
        return parseCondition(request, otherMap);
    }

    public static CriteriaCondition parseCondition(HttpServletRequest request, Map otherHashMap, String... other) {
        Map<String, String> otherMap = new HashMap<String, String>();
        if (other != null) {
            int size = other.length / 2;
            for (int i = 0; i < size; i++) {
                otherMap.put(other[i * 2], other[i * 2 + 1]);
            }
        }
        if (otherHashMap != null) {
            otherMap.putAll(otherHashMap);
        }
        return parseCondition(request, otherMap);
    }

    public static Map buildPageQueryMap(HttpServletRequest request, Map other) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (request != null) {
            params = WebUtils.getParametersStartingWith(request, "");
        }
        if (other != null) {
            params.putAll(other);
        }
        return params;
    }
    public static Map buildPageQueryMap(HttpServletRequest request) {
        Map params = null;
        return buildPageQueryMap(request, params);
    }
    public static Map buildPageQueryMap(HttpServletRequest request, String... other) {
        Map<String, String> otherMap = new HashMap<String, String>();
        if (other != null) {
            int size = other.length / 2;
            for (int i = 0; i < size; i++) {
                otherMap.put(other[i * 2], other[i * 2 + 1]);
            }
        }
        return buildPageQueryMap(request, otherMap);
    }

    public static Map buildPageQueryMap(HttpServletRequest request, Map otherHashMap, String... other) {
        Map<String, String> otherMap = new HashMap<String, String>();
        if (other != null) {
            int size = other.length / 2;
            for (int i = 0; i < size; i++) {
                otherMap.put(other[i * 2], other[i * 2 + 1]);
            }
        }
        if (otherHashMap != null) {
            otherMap.putAll(otherHashMap);
        }
        return buildPageQueryMap(request, otherMap);
    }

}
