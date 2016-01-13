package top.gabin.oa.web.utils.http;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import top.gabin.oa.web.utils.xml.XmlBeanUtils;

import java.io.IOException;

/**
 * Created by linjiabin on 15/1/7.
 */
public class HttpUtils {

    private static String get(String url) {
        HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        httpClient.getParams().setIntParameter("http.socket.timeout", 20000);
        httpClient.getParams().setIntParameter("http.connection.timeout", 15000);
        HttpMethod getMethod = new GetMethod(url);
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode == HttpStatus.SC_OK) {
                String sms = getMethod.getResponseBodyAsString();
                return sms;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T get(String url, Class<T> clazz) {
        String responseText = get(url);
        if (StringUtils.isBlank(responseText)) {
            return null;
        }
        return XmlBeanUtils.xml2Bean(clazz, responseText);
    }

}
