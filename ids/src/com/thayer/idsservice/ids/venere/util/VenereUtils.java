package com.thayer.idsservice.ids.venere.util;

public class VenereUtils {

	public static String buildFullXmlRQ(String orgid, String userid, String password, String requestxml) {
		StringBuilder newXml = new StringBuilder();
		newXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		newXml
				.append("<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		newXml.append("<Header><Authentication xmlns=\"http://www.venere.com/XHI\">");
		newXml.append("<UserOrgID>" + orgid + "</UserOrgID>");
		newXml.append("<UserID>" + userid + "</UserID>");
		newXml.append("<UserPSW>" + password + "</UserPSW>");
		newXml.append("</Authentication>");
		newXml.append("</Header>");
		newXml.append("<Body>");
		newXml.append(requestxml);
		newXml.append("</Body>");
		newXml.append("</Envelope>");
		return newXml.toString();
	}

	public static String buildFullXmlRS(String responsexml) {
		String result = responsexml.substring(responsexml.indexOf("<soap:Body>") + "<soap:Body>".length(), responsexml
				.indexOf("</soap:Body>"));
		return result;
	}
}
