/*****************************************************************<br>
 * <B>FILE :</B> AsiaroomsReceiveQueryHotelAvailServiceTest.java <br>
 * <B>CREATE DATE :</B> Mar 19, 2012 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.thayer.fog2.bo.RateDataBO;
import com.thayer.idsservice.dao.ERatemap;
import com.thayer.idsservice.dao.ERatemapDAO;
import com.thayer.idsservice.service.interf.ICallFogService;
import com.thayer.idsservice.test.base.BaseTest;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Johnny.Chen <br>
 * @since : Mar 19, 2012 <br>
 * @version : v1.0
 */
public class AsiaroomsReceiveQueryHotelAvailServiceTest extends BaseTest {

	private ERatemapDAO eratemapDAO;
	private ICallFogService asiarooms_callFogService;

	@SuppressWarnings("unchecked")
	public void testGetZRateMap() throws Exception {
		String iata = "970011";
		String fogPropId = "1074";
		Calendar start = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		start.setTime(df.parse("2012-03-27"));
		int nights = 1;
		
		 List<RateDataBO> zRateMap = new ArrayList<RateDataBO>();
         ERatemap queryPara = new ERatemap();
         queryPara.setExwebCode(iata);
         queryPara.setFogpropId(fogPropId);
         List<ERatemap> rateMapList = eratemapDAO.findByExample(queryPara);
         if(CollectionUtils.isNotEmpty(rateMapList)){
         	for(ERatemap rateMap : rateMapList){
         		String planId = rateMap.getFograteId();
         		if(StringUtils.isNotBlank(planId)){
         			List<RateDataBO> tempList = asiarooms_callFogService.getZRateMap(fogPropId, start.getTime(), nights, planId.trim(), "Website");
         			if(CollectionUtils.isNotEmpty(tempList)){
         				zRateMap.addAll(tempList);
         			}
         		}
         	}
         }
		
		System.out.println("zRateMap size: " + zRateMap.size());

	}
	
	@SuppressWarnings("unchecked")
	public void testFilter(){
		//过滤ERateMap表中没有映射关系的
		String iata = "970011";
		String fogpropId = "1074";
		if(StringUtils.isNotBlank(fogpropId)){
			ERatemap queryPara = new ERatemap();
			queryPara.setExwebCode(iata);
			queryPara.setFogpropId(fogpropId.trim());
            List<ERatemap> rateMapList = eratemapDAO.findByExample(queryPara);
            if(CollectionUtils.isNotEmpty(rateMapList)){
//            	getzRateMapList.add(bo);
            	System.out.println("rateMapList is not empty");
            }
            System.out.println("rateMapList: " + rateMapList.size());
		}
	}

	/**
	 * @return the eratemapDAO
	 */
	public ERatemapDAO getEratemapDAO() {
		return eratemapDAO;
	}

	/**
	 * @param eratemapDAO
	 *            the eratemapDAO to set
	 */
	public void setEratemapDAO(ERatemapDAO eratemapDAO) {
		this.eratemapDAO = eratemapDAO;
	}

	/**
	 * @return the asiarooms_callFogService
	 */
	public ICallFogService getAsiarooms_callFogService() {
		return asiarooms_callFogService;
	}

	/**
	 * @param asiarooms_callFogService the asiarooms_callFogService to set
	 */
	public void setAsiarooms_callFogService(ICallFogService asiarooms_callFogService) {
		this.asiarooms_callFogService = asiarooms_callFogService;
	}
	
}
