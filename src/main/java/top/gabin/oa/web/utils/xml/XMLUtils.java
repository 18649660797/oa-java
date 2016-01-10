package top.gabin.oa.web.utils.xml;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlUtils {


    /**
     *  bean对象 转为 xml字符串
     *
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> String bean2Xml(T bean) {
        String xmlStr = null;
        StringWriter writer = null;
        try {
            JAXBContext context = JAXBContext.newInstance(bean.getClass());
            Marshaller marshaller = context.createMarshaller();
            writer = new StringWriter();
            marshaller.marshal(bean, writer);
            xmlStr = writer.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return xmlStr;
    }

    /**
     *  xml字符串 转为 bean对象
     *
     * @param bean
     * @param xmlStr
     * @param <T>
     * @return
     */
    public static <T> T xml2Bean(Class<T> bean, String xmlStr) {
        T s = null;
        try {
            JAXBContext context = JAXBContext.newInstance(bean);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            s = (T)unmarshaller.unmarshal(new StringReader(xmlStr));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return s;
    }

}
