package com.thayer.idsservice.task.mail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.thayer.idsservice.bean.MailBean;
import com.thayer.idsservice.service.interf.IMailService;
import com.thayer.idsservice.task.download.bean.ResultBean;
import com.thayer.idsservice.task.interf.IRecoverMailTask;
import com.thayer.idsservice.util.BeanUtil;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-10-15<br>
 * @version : v1.0
 */
public class RecoverSendMailTask extends QuartzJobBean implements IRecoverMailTask {
	protected static Logger log = Logger.getLogger(RecoverSendMailTask.class);
	private IMailService mailService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info("start Recover SendMail Task.................................");
		ClassPathResource resource = new ClassPathResource("/notify");
		File mailDir;
		try {
			mailDir = resource.getFile();
			String[] files = mailDir.list();
			for (String fileName : files) {
				try {
					log.info("Handle recover file:" + fileName);
					String filePath = resource.getFile().getAbsolutePath() + File.separator + fileName;
					File file = new File(filePath);
					Object obj = BeanUtil.unpersistentToObject(FileUtils.readFileToByteArray(file));
					if (obj instanceof MailBean) {
						MailBean mailBean = (MailBean) obj;
						Object resultObj = mailBean.getResultBean();
						// donwload mail alert
						if (resultObj instanceof ResultBean) {
							ResultBean resultBean = (ResultBean) resultObj;
							boolean result = mailService.sendResvMail(mailBean.getMailList(), mailBean.getMailTitle(),
									resultBean);
							if (result) {
								file.delete();
							}
						}
						if (resultObj instanceof com.thayer.idsservice.task.update.bean.ResultBean) {
							com.thayer.idsservice.task.update.bean.ResultBean resultBean = (com.thayer.idsservice.task.update.bean.ResultBean) resultObj;
							boolean result = mailService.sendUpdateMail(mailBean.getMailList(),
									mailBean.getMailTitle(), resultBean);
							if (result) {
								file.delete();
							}
						}
						if (resultObj instanceof String) {
							String content = (String) resultObj;
							boolean result = mailService.sendMail(mailBean.getMailList(), mailBean.getMailTitle(),
									content, "");
							if (result) {
								file.delete();
							}
						}

					}
				} catch (Exception e) {
					log.error(ExceptionUtils.getFullStackTrace(e));
				}
			}
		} catch (IOException e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		log.info("finish Recover SendMail Task.................................");
	}

	public static void main(String[] args) {
		RecoverSendMailTask task = new RecoverSendMailTask();
		try {
			task.execute(null);
		} catch (JobExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public IMailService getMailService() {
		return mailService;
	}

	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}
}
