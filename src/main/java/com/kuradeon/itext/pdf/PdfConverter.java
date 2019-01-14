package com.kuradeon.itext.pdf;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * pdf转换
 * @author kuradeon
 * @date 2019-01-10 16:23
 */
@Log4j2
public class PdfConverter {

    private static final Logger LOGGER = LogManager.getLogger(PdfConverter.class);

    /**
     * 通过pdf模板生成pdf
     * @param templatePath 模板路径
     * @param dataMap 数据map
     * @return pdf byte[]
     */
    public static byte[] getPdfWithTemplate(String templatePath, Map<String, Object> dataMap) {
        if (StringUtils.isBlank(templatePath)) {
            return new byte[0];
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PdfReader reader = new PdfReader(templatePath);
             PdfWriter writer = new PdfWriter(out)) {
            PdfDocument pdfDocument = new PdfDocument(reader, writer);

            // 字体，解决中文乱码
            PdfFont pdfFont = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H", true);
            // 获取变量表格
            PdfAcroForm pdfAcroForm = PdfAcroForm.getAcroForm(pdfDocument, true);
            Map<String, PdfFormField> formFiledMap = pdfAcroForm.getFormFields();
            if (MapUtils.isNotEmpty(formFiledMap) && MapUtils.isNotEmpty(dataMap)) {
                // 填充数据
                for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                    PdfFormField pdfFormField = formFiledMap.get(entry.getKey());
                    if (pdfFormField != null && entry.getValue() != null) {
                        pdfFormField.setFont(pdfFont);
                        pdfFormField.setValue(entry.getValue().toString());
                    }
                }
            }
            // 设置表单不可编辑
            pdfAcroForm.flattenFields();
            pdfDocument.close();
            return out.toByteArray();
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return new byte[0];
    }

    /**
     * pdf byte[] 写入文件
     * @param pdfBytes PDF byte[]
     * @param targetFilePath 目标文件路径
     * @param targetFileName 目标文件名
     */
    public void generatePdfFile(byte[] pdfBytes, String targetFilePath, String targetFileName) {
        if (pdfBytes == null || pdfBytes.length <= 0
                || StringUtils.isBlank(targetFilePath) || StringUtils.isBlank(targetFileName)) {
            return;
        }
        try (FileOutputStream out = new FileOutputStream(targetFilePath + File.separator + targetFileName)) {
            out.write(pdfBytes);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    /**
     * html转pdf
     * @param html html代码
     * @return pdf byte[]
     * @throws IOException IOException
     */
    public static byte[] htmlToPdf(String html) throws IOException {
        if (StringUtils.isBlank(html)) {
            return new byte[0];
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ConverterProperties properties = new ConverterProperties();
            FontProvider fontProvider = new DefaultFontProvider(true,  true,true);
            properties.setFontProvider(fontProvider);
            HtmlConverter.convertToPdf(html, out, properties);
            return out.toByteArray();
        }
    }
}
