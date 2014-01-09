package com.thayer.idsservice.context;

import java.io.Serializable;

import com.thayer.idsservice.task.update.bean.FogMQBean;
import com.thayer.idsservice.task.update.bean.ResultBean;

public class ServiceContext implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7796085461275601130L;
	public final static String SECURITY_CONTEXT_KEY = "com.thayer.idsservice.context.ServiceContext";
	private ResultBean resultBean;
	private FogMQBean fogMQBean;
	private String processId;

	/**
	 * @return the resultBean
	 */
	public ResultBean getResultBean() {
		return resultBean;
	}

	/**
	 * @param resultBean
	 *            the resultBean to set
	 */
	public void setResultBean(ResultBean resultBean) {
		this.resultBean = resultBean;
	}

	/**
	 * @return the fogMQBean
	 */
	public FogMQBean getFogMQBean() {
		return fogMQBean;
	}

	/**
	 * @param fogMQBean
	 *            the fogMQBean to set
	 */
	public void setFogMQBean(FogMQBean fogMQBean) {
		this.fogMQBean = fogMQBean;
	}

	/**
	 * @return the processId
	 */
	public String getProcessId() {
		return processId;
	}

	/**
	 * @param processId
	 *            the processId to set
	 */
	public void setProcessId(String processId) {
		this.processId = processId;
	}

}