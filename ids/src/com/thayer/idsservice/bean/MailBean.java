/*****************************************************************<br>
 * <B>FILE :</B> MailBean.java <br>
 * <B>CREATE DATE :</B> 2010-10-15 <br>
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

import java.io.Serializable;
import java.util.List;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-10-15<br>
 * @version : v1.0
 */
public class MailBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5987375487651613795L;
	private List<String> mailList;
	private String mailTitle;
	private Object resultBean;

	public MailBean() {

	}

	public MailBean(List<String> mailList, String mailTitle, Object resultBean) {
		this.mailList = mailList;
		this.mailTitle = mailTitle;
		this.resultBean = resultBean;
	}

	public List<String> getMailList() {
		return mailList;
	}

	public void setMailList(List<String> mailList) {
		this.mailList = mailList;
	}

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public Object getResultBean() {
		return resultBean;
	}

	public void setResultBean(Object resultBean) {
		this.resultBean = resultBean;
	}

}
