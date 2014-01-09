/*****************************************************************<br>
 * <B>FILE :</B> MsgLogQueryBean.java <br>
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
package com.thayer.idsservice.bean;

import java.util.Date;

/**
 * <B>Function :</B>消息日志查询类 <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-11-26<br>
 * @version : v1.0
 */
public class MsgLogQueryBean {
	private String exwebCode;
	private String msgId;
	private String msgStatus;
	private String msgErrorcode;
	private String expropId;
	private String fogpropId;
	private Date createtimeSince;
	private Date createtimeTill;
	private String createtimeQuerySince;
	private String createtimeQueryTill;

	/**
	 * @return the createtimeQuerySince
	 */
	public String getCreatetimeQuerySince() {
		return createtimeQuerySince;
	}

	/**
	 * @param createtimeQuerySince
	 *            the createtimeQuerySince to set
	 */
	public void setCreatetimeQuerySince(String createtimeQuerySince) {
		this.createtimeQuerySince = createtimeQuerySince;
	}

	/**
	 * @return the createtimeQueryTill
	 */
	public String getCreatetimeQueryTill() {
		return createtimeQueryTill;
	}

	/**
	 * @param createtimeQueryTill
	 *            the createtimeQueryTill to set
	 */
	public void setCreatetimeQueryTill(String createtimeQueryTill) {
		this.createtimeQueryTill = createtimeQueryTill;
	}

	/**
	 * @return the msgErrorcode
	 */
	public String getMsgErrorcode() {
		return msgErrorcode;
	}

	/**
	 * @param msgErrorcode
	 *            the msgErrorcode to set
	 */
	public void setMsgErrorcode(String msgErrorcode) {
		this.msgErrorcode = msgErrorcode;
	}

	/**
	 * @return the expropId
	 */
	public String getExpropId() {
		return expropId;
	}

	/**
	 * @param expropId
	 *            the expropId to set
	 */
	public void setExpropId(String expropId) {
		this.expropId = expropId;
	}

	/**
	 * @return the fogpropId
	 */
	public String getFogpropId() {
		return fogpropId;
	}

	/**
	 * @param fogpropId
	 *            the fogpropId to set
	 */
	public void setFogpropId(String fogpropId) {
		this.fogpropId = fogpropId;
	}

	/**
	 * 
	 */
	public MsgLogQueryBean() {
		// TODO Auto-generated constructor stub
	}

	public String getExwebCode() {
		return exwebCode;
	}

	public void setExwebCode(String exwebCode) {
		this.exwebCode = exwebCode;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMsgStatus() {
		return msgStatus;
	}

	public void setMsgStatus(String msgStatus) {
		this.msgStatus = msgStatus;
	}

	public Date getCreatetimeSince() {
		return createtimeSince;
	}

	public void setCreatetimeSince(Date createtimeSince) {
		this.createtimeSince = createtimeSince;
	}

	public Date getCreatetimeTill() {
		return createtimeTill;
	}

	public void setCreatetimeTill(Date createtimeTill) {
		this.createtimeTill = createtimeTill;
	}

}
