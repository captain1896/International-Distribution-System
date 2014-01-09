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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.thayer.idsservice.bean.MsgLogQueryBean;
import com.thayer.idsservice.bean.PageSortModel;
import com.thayer.idsservice.dao.EMsgLog;
import com.thayer.idsservice.dao.EMsgLogDAO;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-10-27<br>
 * @version : v1.0
 */
public class LogManagerAction extends MultiActionController {
	private String logView;
	private EMsgLogDAO emsgLogDAO;
	private String logMsgView;

	/**
	 * @return the logMsgView
	 */
	public String getLogMsgView() {
		return logMsgView;
	}

	/**
	 * @param logMsgView
	 *            the logMsgView to set
	 */
	public void setLogMsgView(String logMsgView) {
		this.logMsgView = logMsgView;
	}

	/**
	 * @return the logView
	 */
	public String getLogView() {
		return logView;
	}

	/**
	 * @param logView
	 *            the logView to set
	 */
	public void setLogView(String logView) {
		this.logView = logView;
	}

	/**
	 * @return the emsgLogDAO
	 */
	public EMsgLogDAO getEmsgLogDAO() {
		return emsgLogDAO;
	}

	/**
	 * @param emsgLogDAO
	 *            the emsgLogDAO to set
	 */
	public void setEmsgLogDAO(EMsgLogDAO emsgLogDAO) {
		this.emsgLogDAO = emsgLogDAO;
	}

	public ModelAndView queryData(HttpServletRequest request, HttpServletResponse response, MsgLogQueryBean msgLogQueryBean) throws IOException {
		int countnum = emsgLogDAO.findMsgLogsCountByQuery(msgLogQueryBean);

		String resvBaseTableId = "resvBaseTableId";
		int checkRecord = 0;
		checkRecord = countnum;
		PageSortModel sortModel = new PageSortModel(request, resvBaseTableId);
		sortModel.setTotalRows(checkRecord);
		int firstResult = sortModel.getRowStart();

		List<EMsgLog> results = emsgLogDAO.findMsgLogsByQuery(msgLogQueryBean, firstResult, sortModel.getPageSize());
		return new ModelAndView(logView, "queryLogData", results);
	}

	public ModelAndView queryMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		EMsgLog emsgLog = emsgLogDAO.findById(new Integer(id));
		String result = "";
		if ("msgRq".equals(type)) {
			result = emsgLog.getMsgRq();
		}
		if ("msgRs".equals(type)) {
			result = emsgLog.getMsgRs();
		}
		if ("msgRsFinal".equals(type)) {
			result = emsgLog.getMsgRsFinal();
		}

		return new ModelAndView(logMsgView, "detail", result);
	}
}
