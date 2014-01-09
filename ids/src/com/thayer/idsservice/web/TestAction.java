package com.thayer.idsservice.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.thayer.fog2.vo.RateMQVO;
import com.thayer.idsservice.dao.EMsgLogDAO;
import com.thayer.idsservice.service.interf.IEOrdermailInfoService;
import com.thayer.idsservice.service.interf.IHttpClientService;
import com.thayer.idsservice.task.update.UpdateBaseTask;

public class TestAction extends MultiActionController{
	private UpdateBaseTask task;
	private IEOrdermailInfoService eordermailInfoService;
	private EMsgLogDAO emsgLogDao;
	 private IHttpClientService httpClientService;
	
	public ModelAndView test(HttpServletRequest request, HttpServletResponse response){
		try {
			String s = httpClientService.postHttpRequest("https://supply-xml.booking.com/hotels/xml/availability", "1");
			System.out.println(s.length());
//			this.shangchuan();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void shangchuan(){
		task.setIata("960007");
		RateMQVO message = new RateMQVO();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		message.setProp("1064");
		try {
			message.setFromDate(sdf.parse("2013-06-25"));
			message.setToDate(sdf.parse("2013-06-25"));
			List<String> list = new ArrayList<String>();
			list.add("DTTW-MBAR2");
			message.setPlancodes(list);
			message.setWeeks(new int[]{1});
			message.setOpetype(4);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		task.onMessage(message);
	}

	public UpdateBaseTask getTask() {
		return task;
	}

	public void setTask(UpdateBaseTask task) {
		this.task = task;
	}

	public IEOrdermailInfoService getEordermailInfoService() {
		return eordermailInfoService;
	}

	public void setEordermailInfoService(
			IEOrdermailInfoService eordermailInfoService) {
		this.eordermailInfoService = eordermailInfoService;
	}

	public EMsgLogDAO getEmsgLogDao() {
		return emsgLogDao;
	}

	public void setEmsgLogDao(EMsgLogDAO emsgLogDao) {
		this.emsgLogDao = emsgLogDao;
	}

	public IHttpClientService getHttpClientService() {
		return httpClientService;
	}

	public void setHttpClientService(IHttpClientService httpClientService) {
		this.httpClientService = httpClientService;
	}
}
