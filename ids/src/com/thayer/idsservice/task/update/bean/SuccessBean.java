package com.thayer.idsservice.task.update.bean;

import java.io.Serializable;

public class SuccessBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -371178706224229165L;
	private TProp tProp;

	public TProp gettProp() {
		return tProp;
	}

	public void settProp(TProp tProp) {
		this.tProp = tProp;
	}

}
