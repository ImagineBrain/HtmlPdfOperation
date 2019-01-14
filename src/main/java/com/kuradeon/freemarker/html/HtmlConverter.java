package com.kuradeon.freemarker.html;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @author Kuradeon
 * @date 2019-01-11 14:30
 */
@Log4j2
public class HtmlConverter {

    private static final Logger LOGGER = LogManager.getLogger(HtmlConverter.class);

    /**
     * freemarker 配置
     */
    @NonNull
    private Configuration configuration;

    public HtmlConverter(@NonNull Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 从html模板生成html
     * @param templateName 模板名
     * @param dataMap 数据map
     * @return html
     */
    public String getHtmlFromTemplate(String templateName, Map<String, Object> dataMap) {
        try (CharArrayWriter writer = new CharArrayWriter()){
            Template template = configuration.getTemplate(templateName);
            template.process(dataMap, writer);
            return new String(writer.toCharArray());
        } catch (IOException | TemplateException e) {
            LOGGER.error(e);
        }
        return null;
    }

    /**
     * 获取默认配置
     * @return 配置
     * @throws IOException IOException
     */
    public static Configuration getDefaultConfiguration() throws IOException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDefaultEncoding("UTF-8");
        // 从远程加载
//        configuration.setTemplateLoader(new RemoteTemplateLoader());
        // 从本地模板文件路径加载
//        configuration.setDirectoryForTemplateLoading(new File("/template" + File.separator + "html"));
        // 从class模板路径加载
        configuration.setClassForTemplateLoading(HtmlConverter.class, "/template/html");
        return configuration;
    }
}
