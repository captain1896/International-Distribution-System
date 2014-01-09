package com.thayer.idsservice.task.Thread;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.thayer.idsservice.task.download.bean.SuccessBean;
import com.thayer.idsservice.task.download.interf.IResvCallbackService;

public class ExpediaResultFaildThread implements Runnable {

	/**
	 * 订单信息回传服务
	 */
	private IResvCallbackService resvCallbackService;
	
	private SuccessBean successbean;
	private static Logger log = Logger.getLogger(ExpediaResultFaildThread.class);
	
	public ExpediaResultFaildThread(IResvCallbackService resvCallbackService,
			SuccessBean successbean) {
		super();
		this.resvCallbackService = resvCallbackService;
		this.successbean = successbean;
	}

	@Override
	public void run() {
		boolean success  = false;
		try {
			//暂停三分钟
			Thread.sleep(1000L*60*3);
			log.info("订单回传失败第一次重试");
			success = resvCallbackService.resvCallback(successbean);
		} catch (InterruptedException e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		
		if(!success){
			try {
				//如果上次没有成功 就暂停5分钟
				Thread.sleep(1000L*60*5);
				log.info("订单回传失败第二次重试");
				resvCallbackService.resvCallback(successbean);
			} catch (InterruptedException e) {
				log.error(ExceptionUtils.getFullStackTrace(e));
			}
		}
		
		
	}

	public IResvCallbackService getResvCallbackService() {
		return resvCallbackService;
	}

	public void setResvCallbackService(IResvCallbackService resvCallbackService) {
		this.resvCallbackService = resvCallbackService;
	}

	public SuccessBean getSuccessbean() {
		return successbean;
	}

	public void setSuccessbean(SuccessBean successbean) {
		this.successbean = successbean;
	}
	
	

}
