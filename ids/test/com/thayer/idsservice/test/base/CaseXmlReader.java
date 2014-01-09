package com.thayer.idsservice.test.base;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CaseXmlReader {

	public static String getXml(String packagename, String filename) {
		String s = packagename + "/" + filename;
		StringBuffer xml = new StringBuffer();
		try {
			InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(s);
			BufferedReader in = new BufferedReader(new InputStreamReader(ins, "UTF8"));
			String str = in.readLine();
			while (str != null) {
				xml.append(str);
				str = in.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xml.toString();
	}

	public static void main(String[] args) {
		System.out.println(CaseXmlReader.getXml("testdata/bookingcom", "bookingcom02.xml"));
	}
}
