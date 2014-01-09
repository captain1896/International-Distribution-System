/*****************************************************************<br>
 * <B>FILE :</B> RsStatusBean.java <br>
 * <B>CREATE DATE :</B> 2010-11-26 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.task.update.bean;

import java.io.Serializable;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-11-26<br>
 * @version : v1.0
 */
public class RsStatusBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9188006979886631969L;

	/**
	 * 是否上传成功
	 */
	private boolean rsResult;

	/**
	 * 上传状态
	 */
	private String rsStatus;

	/**
	 * 上传消息ID号
	 */
	private String rsMsgId;

	/**
	 * 上传信息描述
	 */
	private String rsDesc;

	/**
	 * IDS酒店号
	 */
	private String expropId;
	/**
	 * FOG酒店号
	 */
	private String fogpropId;

	/**
	 * 
	 */
	public RsStatusBean() {
		// TODO Auto-generated constructor stub
	}

	public boolean isRsResult() {
		return rsResult;
	}

	public void setRsResult(boolean rsResult) {
		this.rsResult = rsResult;
	}

	public String getRsStatus() {
		return rsStatus;
	}

	public void setRsStatus(String rsStatus) {
		this.rsStatus = rsStatus;
	}

	public String getRsMsgId() {
		return rsMsgId;
	}

	public void setRsMsgId(String rsMsgId) {
		this.rsMsgId = rsMsgId;
	}

	public String getRsDesc() {
		return rsDesc;
	}

	public void setRsDesc(String rsDesc) {
		this.rsDesc = rsDesc;
	}

}
