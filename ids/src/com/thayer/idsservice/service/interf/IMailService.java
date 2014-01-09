package com.thayer.idsservice.service.interf;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.thayer.idsservice.bean.MailBean;
import com.thayer.idsservice.bean.MailFogResvBaseBean;
import com.thayer.idsservice.task.download.bean.ResultBean;

import freemarker.template.TemplateException;

/**
 * <B>Function :</B> Mail Service Interface<br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-9-29<br>
 * @version : v1.0
 */
public interface IMailService extends Serializable{

	/**
	 * 发送简单信息的邮件
	 * 
	 * @param toMailList
	 * @param title
	 * @param html
	 * @param info
	 * @return
	 */
	boolean sendMail(List<String> toMailList, String title, String html, String info);

	/**
	 * 发送订单下载信息的邮件
	 * 
	 * @param toMailList
	 * @param title
	 * @param content
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	boolean sendResvMail(List<String> toMailList, String title, ResultBean resultBean) throws IOException, TemplateException;

	/**
	 * 发送更新信息的邮件
	 * 
	 * @param toMailList
	 * @param title
	 * @param content
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	boolean sendUpdateMail(List<String> toMailList, String title, com.thayer.idsservice.task.update.bean.ResultBean resultBean) throws IOException, TemplateException;

	/**
	 * 持久化邮件类 <br>
	 * 
	 * 当邮件发送失败后，将持久化邮件bean，等待重新发送。
	 * 
	 * @param mailBean
	 */
	void persistentMailBean(MailBean mailBean);
}
