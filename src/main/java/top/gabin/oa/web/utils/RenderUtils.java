package top.gabin.oa.web.utils;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import top.gabin.oa.web.dto.PageDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HttpServletResponse 数据输出 工具类
 */
public class RenderUtils {
    public static final Map<String, Object> SUCCESS_RESULT = getSuccessMap();

    /**
     * 通过指定转成的属性,props：多个以,分隔
     *
     * @param objList
     * @param props
     * @return
     */
    public static List<Map<String, Object>> transListProp(List<? extends Object> objList, String props) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        if (objList != null) {
            for (Object obj : objList) {
                Map<String, Object> map = objectChangeToMap(obj, props);
                mapList.add(map);
            }
        }
        return mapList;
    }

    /**
     * 把 obj 按照 属性-->值 放到 map里
     *
     * @param obj
     * @param props
     * @return
     */
    public static Map<String, Object> objectChangeToMap(Object obj, String props) {
        Map<String, Object> map = new HashMap<String, Object>();
        String[] propArr = props.split(",");
        for (String prop : propArr) {
            if (StringUtils.isNotBlank(prop)) {
                try {
                    String key = prop;
                    if (prop.indexOf(" ") > -1) {
                        String[] split = prop.split(" ");
                        if (StringUtils.isNotBlank(split[0]) && StringUtils.isNotBlank(split[1])) {
                            key = split[1];
                            prop = split[0];
                        }
                    }
                    key = key.trim();
                    prop = prop.trim();
                    map.put(key, PropertyUtils.getProperty(obj, prop));
                } catch (NestedNullException exception) {
                    map.put(prop, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }
        return map;
    }

    /**
     * 返回包含 分页信息 的 map
     *
     * @param list
     * @param props
     * @return
     */
    public static List filterPageData(List list, String props) {
        if (StringUtils.isNotBlank(props)) {
            list = transListProp(list, props);
        }
        return list;
    }

    public static Map<String, Object> filterPageDataResult(PageDTO<? extends Object> page, String props) {
        return filterPageDataResult(page, props, SUCCESS_RESULT);
    }

    /**
     * 返回分页信息 的 map
     *
     * @param page
     * @param props
     * @param message
     * @return
     */
    private static Map<String, Object> filterPageDataResult(PageDTO<? extends Object> page, String props, Map<String, Object> message) {
        Map<String, Object> result = new HashMap<String, Object>();
        List gridData = filterPageData(page.getContent(), props);
        result.put("rows", gridData);
        message.put("results", page.getTotalSize());
        if (message != null && !message.isEmpty()) result.putAll(message);
        return result;
    }

    /**
     * 返回 成功的 map
     *
     * @return
     */
    public static Map<String, Object> getSuccessMap() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", true);
        return result;
    }

    /**
     * 返回 失败的 结果 map
     *
     * @param message
     * @return
     */
    public static Map<String, Object> getFailMap(String message) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", false);
        result.put("message", message);
        return result;
    }

    /**
     * 返回 数据map
     *
     * @param data
     * @return
     */
    public static Map<String, Object> getSuccessResult(Object data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", true);
        result.put("data", data);
        return result;
    }

    /**
     * 缥 Model 中填充 参数
     *
     * @param modelAttrs
     * @param model
     * @return
     */
    public static Model fillModel(Map<String, Object> modelAttrs, Model model) {
        if (modelAttrs == null || model == null) {
            return model;
        }
        for (String key : modelAttrs.keySet()) {
            model.addAttribute(key, modelAttrs.get(key));
        }
        return model;
    }

    /**
     * 给 ModelAndView 中 填充参数
     *
     * @param modelAttrs
     * @param model
     * @return
     */
    public static ModelAndView fillModel(Map<String, Object> modelAttrs, ModelAndView model) {
        if (modelAttrs == null || model == null) {
            return model;
        }
        for (String key : modelAttrs.keySet()) {
            model.addObject(key, modelAttrs.get(key));
        }
        return model;
    }

    /**
     * 用户API列表数据获取
     *
     * @param page
     * @param props
     * @return
     */
    public static Map<String, Object> filterApiPageData(PageDTO<? extends Object> page, String props) {
        Map<String, Object> message = new HashMap<String, Object>();
        Map<String, Object> pageInfo = new HashMap<String, Object>();
        message.put("res_data", pageInfo);
        pageInfo.put("total", page.getTotalSize());
        pageInfo.put("page_no", page.getPage());
        pageInfo.put("page_size", page.getPageSize());
        pageInfo.put("data_list", transListProp(page.getContent(), props));
        return pageInfo;
    }

}
