package com.kuradeon.test;

import com.google.common.collect.Maps;
import com.kuradeon.freemarker.html.HtmlConverter;
import com.kuradeon.itext.pdf.PdfConverter;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author kuradeon
 * @date 2019-01-10 17:50
 */
public class PdfTest {

    @Test
    public void pdfTest() {
    }

    @Test
    public void getHtmlAndPdfFromHtmlTemplate() throws IOException {
        HtmlConverter htmlConverter = new HtmlConverter(HtmlConverter.getDefaultConfiguration());
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("Name", "小明");
        dataMap.put("Sex", 1);
        dataMap.put("Height", "175 CM");
        dataMap.put("Weight", "65 KG");
        String html = htmlConverter.getHtmlFromTemplate("template.html", dataMap);
        System.out.println(html);
        byte[] pdf = PdfConverter.htmlToPdf(html);
        try (FileOutputStream out = new FileOutputStream("pdf.pdf")) {
            out.write(pdf);
        }
    }
}
