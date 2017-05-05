package com.consult.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map;

import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class DocumentHandler {
	private Configuration configuration = null;

	public DocumentHandler() {
		configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
	}

	public void createDoc(Map<String, Object> dataMap, String fileName,String wordType,String sex)
			throws UnsupportedEncodingException {
		configuration.setClassForTemplateLoading(this.getClass(), "/template");
		Template t = null;
		String templateStr = "fctestpaper.ftl";
		try {
			if("0".equals(wordType)) {
				templateStr = "fctestpaper.ftl";
			} else {
				if("0".equals(sex)) {
					templateStr = "newOldWord0.ftl";
				} else {
					templateStr = "newOldWord1.ftl";
				}
			}
			t = configuration.getTemplate(templateStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File outFile = new File(fileName);
		Writer out = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outFile);
			OutputStreamWriter oWriter = new OutputStreamWriter(fos, "UTF-8");
			out = new BufferedWriter(oWriter);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			t.process(dataMap, out);
			out.close();
			fos.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
