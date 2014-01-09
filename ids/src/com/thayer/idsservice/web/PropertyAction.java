/*****************************************************************<br>
 * <B>FILE :</B> PropertyAction.java <br>
 * <B>CREATE DATE :</B> 2011-5-9 <br>
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.thayer.fog2.bo.RoomImageBo;
import com.thayer.fog2.entity.Prop;
import com.thayer.fog2.entity.Rate;
import com.thayer.fogservice.webservice.crs.PropWebService;
import com.thayer.fogservice.webservice.crs.RateWebService;
import com.thayer.fogservice.webservice.crs.RoomWebService;
import com.thayer.idsservice.bean.PropertyInfo;
import com.thayer.idsservice.service.interf.ITransPropertyInfoService;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-5-9<br>
 * @version : v1.0
 */
public class PropertyAction extends MultiActionController {
	private String propertyView;
	private PropWebService fogServicePropWSClient;
	private RoomWebService fogServiceRoomWSClient;
	private RateWebService fogServiceRateWSClient;

	public ModelAndView queryProperty(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String exwebCode = request.getParameter("exwebCode");
		String propId = request.getParameter("prop");
		Prop propInfo = fogServicePropWSClient.findPropById(propId);
		List<RoomImageBo> rooms = fogServiceRoomWSClient.findRoomImageByProp(propId, "A");
		List<Rate> ratesBase = fogServiceRateWSClient.findBaseCodeRateByProp(propId);
		List<Rate> rateNotBase = fogServiceRateWSClient.findRatesByPropNotBase(propId);
		List<Rate> rates = new ArrayList<Rate>();
		rates.addAll(ratesBase);
		rates.addAll(rateNotBase);
		Map paras = new HashMap();
		paras.put("exwebCode", exwebCode);
		paras.put("propInfo", propInfo);
		paras.put("rooms", rooms);
		paras.put("rates", rates);
		return new ModelAndView(propertyView, paras);
	}

	public ModelAndView transProperty(HttpServletRequest request, HttpServletResponse response,
			PropertyInfo propertyInfo) throws Exception {
		String exwebCode = request.getParameter("exwebCode");
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession()
				.getServletContext());
		String transBeanName = exwebCode + "_transPropertyService";
		if (context.containsBean(transBeanName)) {
			ITransPropertyInfoService propertyInfoService = (ITransPropertyInfoService) context.getBean(transBeanName);
			try {
				propertyInfoService.transPropertyInfo(propertyInfo);
			} catch (Exception e) {
				response.getWriter().write("Transfer property info failed! Caused by : " + e.getMessage());
				return null;
			}
			response.getWriter().write("Transfer property info success!");
			return null;
		}

		response.getWriter().write(
				"Transfer property info failed! Caused by : " + exwebCode + " transfer not supported!");
		return null;
	}

	/**
	 * @return the propertyView
	 */
	public String getPropertyView() {
		return propertyView;
	}

	/**
	 * @param propertyView
	 *            the propertyView to set
	 */
	public void setPropertyView(String propertyView) {
		this.propertyView = propertyView;
	}

	/**
	 * @return the fogServicePropWSClient
	 */
	public PropWebService getFogServicePropWSClient() {
		return fogServicePropWSClient;
	}

	/**
	 * @param fogServicePropWSClient
	 *            the fogServicePropWSClient to set
	 */
	public void setFogServicePropWSClient(PropWebService fogServicePropWSClient) {
		this.fogServicePropWSClient = fogServicePropWSClient;
	}

	/**
	 * @return the fogServiceRoomWSClient
	 */
	public RoomWebService getFogServiceRoomWSClient() {
		return fogServiceRoomWSClient;
	}

	/**
	 * @param fogServiceRoomWSClient
	 *            the fogServiceRoomWSClient to set
	 */
	public void setFogServiceRoomWSClient(RoomWebService fogServiceRoomWSClient) {
		this.fogServiceRoomWSClient = fogServiceRoomWSClient;
	}

	/**
	 * @return the fogServiceRateWSClient
	 */
	public RateWebService getFogServiceRateWSClient() {
		return fogServiceRateWSClient;
	}

	/**
	 * @param fogServiceRateWSClient
	 *            the fogServiceRateWSClient to set
	 */
	public void setFogServiceRateWSClient(RateWebService fogServiceRateWSClient) {
		this.fogServiceRateWSClient = fogServiceRateWSClient;
	}
}
