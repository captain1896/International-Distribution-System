package com.thayer.idsservice.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hubs1.bsi.webservice.rq.MailRQ;
import net.hubs1.bsi.webservice.rs.Result;
import net.hubs1.bsiclient.client.MailClient;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.thayer.idsservice.bean.MailBean;
import com.thayer.idsservice.service.interf.IMailService;
import com.thayer.idsservice.task.download.bean.ResultBean;
import com.thayer.idsservice.util.BeanUtil;
import com.thayer.idsservice.util.CommonUtils;

import freemarker.template.TemplateException;

public class MailService implements IMailService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2221720947402783621L;

	private static final Logger logger = Logger.getLogger(MailService.class);

	private transient VelocityEngine velocityEngine;

	public boolean sendMail(List<String> toMailList, String title, String html, String info) {
		try {
			MailRQ rq = new MailRQ();
			rq.setBizcode("LOGINFO");
			rq.setReceivemails(toMailList);
			rq.setMailtitle(title);
			rq.setMailmsg(html);
			Result result = MailClient.sendHtmlMailFilter(rq);
			if (!result.getSuccess()) {
				logger.error("SendMail Error : " + info + result);
				return false;
			}
			logger.debug("html:" + html);
			logger.info("SendMail successfully!!");
			return true;
		} catch (Exception e) {
			logger.error("SendMail Error : " + info + " \n" + ExceptionUtils.getFullStackTrace(e));
			return false;
		}
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public boolean sendResvMail(List<String> toMailList, String title, ResultBean resultBean) throws IOException,
			TemplateException {
		if (resultBean != null
				&& ((resultBean.getSuccesslist() != null && resultBean.getSuccesslist().size() != 0) || (resultBean
						.getErrorlist() != null && resultBean.getErrorlist().size() != 0))) {
			String vm = "resv.vm";
			Map model = new HashMap();
			model.put("resultBean", resultBean);
			/* Merge data-model with template */
			String out = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, vm, model);
			// title += " Success：" + resultBean.getSuccesslist().size() + " Error: " +
			// resultBean.getErrorlist().size();
			return sendMail(toMailList, title, out, "");
		} else {
			return true;
		}

	}

	public boolean sendUpdateMail(List<String> toMailList, String title,
			com.thayer.idsservice.task.update.bean.ResultBean resultBean) throws IOException, TemplateException {
		if (resultBean != null
				&& ((resultBean.getSuccesslist() != null && resultBean.getSuccesslist().size() != 0) || (resultBean
						.getErrorlist() != null && resultBean.getErrorlist().size() != 0))) {
			String vm = "update.vm";
			Map model = new HashMap();
			model.put("resultBean", resultBean);
			/* Merge data-model with template */
			String out = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, vm, model);
			title += " Error: " + resultBean.getErrorlist().size();
			return sendMail(toMailList, title, out, "");
		} else {
			return true;
		}
	}

	public void persistentMailBean(MailBean mailBean) {
		try {
			byte[] obj = BeanUtil.persistentObject(mailBean);
			ClassPathResource resource = new ClassPathResource("/notify");
			// logger.info("persistentMailBean to :" + resource.getFile().getAbsolutePath());
			File file = new File(resource.getFile().getAbsolutePath() + File.separator + CommonUtils.getUuid());
			logger.info("persistentMailBean to :" + file.getAbsolutePath());
			FileUtils.writeByteArrayToFile(file, obj);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}

	}

	public static void main(String[] args) {
		MailService mailService = new MailService();
		mailService.persistentMailBean(new MailBean());
	}

}
