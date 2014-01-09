package com.thayer.idsservice.ids.genares.util;

import org.apache.log4j.Logger;

import com.thayer.idsservice.util.CommonUtils;
import com.thayer.idsservice.util.DateUtil;

public class AssemblyUtil {

	protected static Logger LOGGER = Logger.getLogger(AssemblyUtil.class);

	public static String getSoapHead(String wsaAction, String wsseUsername, String wssePassword) {
		String WSAMessageID = CommonUtils.getUuid();
		String WSUTimestampID = CommonUtils.getUuid();
		String WSUSecurityTokenID = CommonUtils.getUuid();
		String WSUCreated = DateUtil.formatNow();
		String WSUExpires = DateUtil.formatDateTime(DateUtil.dateAdd(DateUtil.TIME_UNIT_M, 5, DateUtil.dateValue(WSUCreated)));
		LOGGER.info("WSAAction:" + wsaAction + ",WSAMessageID:" + WSAMessageID + ",WSUTimestampID:" + WSUTimestampID + ",WSUSecurityTokenID:" + WSUSecurityTokenID + ",WSUCreated:" + WSUCreated
				+ ",WSUExpires:" + WSUExpires);
		String header = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"";
		header = header + " xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\"";
		header = header + " xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"";
		header = header + " xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">";
		header = header + "<soap:Header><wsa:MessageID>uuid:" + WSAMessageID + "</wsa:MessageID>";
		header = header + "<wsa:ReplyTo><wsa:Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</wsa:Address></wsa:ReplyTo>";
		header = header + "<wsa:To>https://www.genares.net/cgi/interfaces/ota/ota_server</wsa:To>";
		header = header + "<wsa:Action>" + wsaAction + "</wsa:Action>";
		header = header + "<wsse:Security soap:mustUnderstand=\"1\"><wsu:Timestamp wsu:Id=\"Timestamp-" + WSUTimestampID + "\">";
		header = header + "<wsu:Created>" + WSUCreated + "</wsu:Created>";
		header = header + "<wsu:Expires>" + WSUExpires + "</wsu:Expires></wsu:Timestamp>";
		header = header + "<wsse:UsernameToken xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"SecurityToken-" + WSUSecurityTokenID + "\">";
		header = header + "<wsse:Username>" + wsseUsername + "</wsse:Username>";
		header = header + "<wsse:Password Type=\"PasswordText\">" + wssePassword + "</wsse:Password></wsse:UsernameToken></wsse:Security></soap:Header>";
		return header;
	}
}
