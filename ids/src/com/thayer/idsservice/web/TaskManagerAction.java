/*****************************************************************<br>
 * <B>FILE :</B> TaskManagerAction.java <br>
 * <B>CREATE DATE :</B> 2010-10-27 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.SchedulerException;
import org.quartz.impl.StdScheduler;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-10-27<br>
 * @version : v1.0
 */
public class TaskManagerAction extends MultiActionController {

	public ModelAndView pauseTask(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("utf-8");
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession()
				.getServletContext());
		StdScheduler scheduler = (StdScheduler) wac.getBean("schedulerFactoryBean");
		String triggerName = request.getParameter("triggerName");
		String groupName = request.getParameter("groupName");
		try {
			scheduler.pauseTrigger(triggerName, groupName);
			response.getWriter().print("暂停任务成功!");
		} catch (SchedulerException e) {
			logger.error(e);
			response.getWriter().print("暂停任务失败!");
		}
		return null;
	}

	public ModelAndView resumeTask(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("utf-8");
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession()
				.getServletContext());
		StdScheduler scheduler = (StdScheduler) wac.getBean("schedulerFactoryBean");
		String triggerName = request.getParameter("triggerName");
		String groupName = request.getParameter("groupName");
		try {
			scheduler.resumeTrigger(triggerName, groupName);
			response.getWriter().print("恢复任务成功!");
		} catch (SchedulerException e) {
			logger.error(e);
			response.getWriter().print("恢复任务失败!");
		}
		return null;
	}

	public ModelAndView resumeUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("utf-8");
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession()
				.getServletContext());

		String idsName = request.getParameter("idsName");
		DefaultMessageListenerContainer agodaContainer = (DefaultMessageListenerContainer) wac.getBean(idsName
				+ "_queueListenerContainer");
		try {
			agodaContainer.start();
			response.getWriter().print("恢复任务成功!");
		} catch (Exception e) {
			logger.error(e);
			response.getWriter().print("恢复任务失败!");
		}
		return null;
	}

	public ModelAndView pauseUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("utf-8");
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession()
				.getServletContext());

		String idsName = request.getParameter("idsName");
		DefaultMessageListenerContainer agodaContainer = (DefaultMessageListenerContainer) wac.getBean(idsName
				+ "_queueListenerContainer");
		try {
			agodaContainer.stop();
			response.getWriter().print("暂停任务成功!");
		} catch (Exception e) {
			logger.error(e);
			response.getWriter().print("暂停任务失败!");
		}
		return null;
	}
}
