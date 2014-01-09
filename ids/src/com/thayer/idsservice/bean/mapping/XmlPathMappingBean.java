package com.thayer.idsservice.bean.mapping;

import java.util.Map;

public class XmlPathMappingBean {
	private String groupid;

	private String propEnum;

	private Map<String, String> pathMapping;

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getPropEnum() {
		return propEnum;
	}

	public void setPropEnum(String propEnum) {
		this.propEnum = propEnum;
	}

	public Map getPathMapping() {
		return pathMapping;
	}

	public void setPathMapping(Map pathMapping) {
		this.pathMapping = pathMapping;
	}

}
