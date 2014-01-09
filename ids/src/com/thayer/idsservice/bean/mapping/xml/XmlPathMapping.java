package com.thayer.idsservice.bean.mapping.xml;

import hubs1.net.xmlpath.ConfDocument;
import hubs1.net.xmlpath.ConfDocument.Conf;
import hubs1.net.xmlpath.MappingDocument.Mapping;
import hubs1.net.xmlpath.PathMappingDocument.PathMapping;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.springframework.util.StringUtils;

import com.thayer.idsservice.bean.mapping.XmlPathMappingBean;

public class XmlPathMapping implements IMappingXml {
	private static Logger plog = Logger.getLogger(XmlPathMapping.class);

	public void parseXml(InputStream xml) {
		xmlGIDPathMap.clear();
		xmlEnumPathMap.clear();
		// File file = new File(xml);
		try {
			ConfDocument confDoc = ConfDocument.Factory.parse(xml);
			Conf configuration = confDoc.getConf();
			Mapping[] mappingList = configuration.getMappingArray();
			for (Mapping map : mappingList) {
				String groupid = map.getGroupID();
				String enumid = map.getPropEnum();
				XmlPathMappingBean xmlPathBean = new XmlPathMappingBean();
				if (StringUtils.hasText(groupid) && StringUtils.hasText(enumid)) {
					xmlPathBean.setGroupid(groupid);
					xmlPathBean.setPropEnum(enumid);
					PathMapping[] pathMappingList = map.getPathMappingArray();
					Map<String, String> xmlpathMap = new HashMap<String, String>();
					for (PathMapping pathMapBean : pathMappingList) {
						if (StringUtils.hasText(pathMapBean.getType())) {
							xmlpathMap.put(pathMapBean.getType(), pathMapBean.getXmlPath());
						}
					}// end for2
					xmlPathBean.setPathMapping(xmlpathMap);
				}// end if
				xmlGIDPathMap.put(groupid, xmlPathBean);
				xmlEnumPathMap.put(enumid, xmlPathBean);
			}// end for1
			plog.info("xmlPathMap init finished with (" + xmlGIDPathMap.size() + ") element!");
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void parseXml(String xml) {
		xmlGIDPathMap.clear();
		xmlEnumPathMap.clear();
		File file = new File(xml);
		try {
			ConfDocument confDoc = ConfDocument.Factory.parse(file);
			Conf configuration = confDoc.getConf();
			Mapping[] mappingList = configuration.getMappingArray();
			for (Mapping map : mappingList) {
				String groupid = map.getGroupID();
				String enumid = map.getPropEnum();
				XmlPathMappingBean xmlPathBean = new XmlPathMappingBean();
				if (StringUtils.hasText(groupid) && StringUtils.hasText(enumid)) {
					xmlPathBean.setGroupid(groupid);
					xmlPathBean.setPropEnum(enumid);
					PathMapping[] pathMappingList = map.getPathMappingArray();
					Map<String, String> xmlpathMap = new HashMap<String, String>();
					for (PathMapping pathMapBean : pathMappingList) {
						if (StringUtils.hasText(pathMapBean.getType())) {
							xmlpathMap.put(pathMapBean.getType(), pathMapBean.getXmlPath());
						}
					}// end for2
					xmlPathBean.setPathMapping(xmlpathMap);
				}// end if
				xmlGIDPathMap.put(groupid, xmlPathBean);
				xmlEnumPathMap.put(enumid, xmlPathBean);
			}// end for1
			plog.info("xmlPathMap init finished with (" + xmlGIDPathMap.size() + ") element!");
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Map<String, XmlPathMappingBean> xmlGIDPathMap = new HashMap<String, XmlPathMappingBean>();

	private Map<String, XmlPathMappingBean> xmlEnumPathMap = new HashMap<String, XmlPathMappingBean>();

	public XmlPathMappingBean getXmlBeanByGID(String groupid) {
		return this.xmlGIDPathMap.get(groupid);
	}

	public XmlPathMappingBean getXmlBeanByEnum(String EnumID) {

		return this.xmlEnumPathMap.get(EnumID);
	}
}
