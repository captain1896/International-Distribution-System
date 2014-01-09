package com.thayer.idsservice.test.bookingcom;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thayer.idsservice.bean.MailFogResvBaseBean;
import com.thayer.idsservice.dao.EResvMap;
import com.thayer.idsservice.dao.EResvMapDAO;
import com.thayer.idsservice.exception.TimeOut4FogException;
import com.thayer.idsservice.ids.bookingcom.download.service.BookingcomDownloadService;
import com.thayer.idsservice.service.interf.ICallFogService;
import com.thayer.idsservice.service.interf.IMapService;
import com.thayer.idsservice.task.download.bean.ErrorBean;
import com.thayer.idsservice.task.download.bean.HotelBean;
import com.thayer.idsservice.task.download.bean.ResultBean;
import com.thayer.idsservice.task.download.bean.SuccessBean;
import com.thayer.idsservice.task.download.bean.TResvBase;
import com.thayer.idsservice.task.download.interf.IDownLoadService;
import com.thayer.idsservice.test.base.BaseTest;
import com.thayer.idsservice.test.base.CaseXmlReader;
import com.thayer.idsservice.util.DateUtil;

public class BookingComTest extends BaseTest {

	private IDownLoadService bookingcom_downLoadService;
	
	private boolean validateResvRate = false;
	
	private Log log = LogFactory.getLog(BookingComTest.class);

	public void testHandleResponseXml4fogbeanList() throws Exception {
		
		String xml = CaseXmlReader.getXml("testdata/bookingcom", "bookingcom-guestname.xml");
		String fogIata = "970003";
		String thirdIataCode = "BOOKINGCOM";
		String fogHotelCode = "1494";
		String thirdHotelCode = "67424";
		
		
		ResultBean resultBean = new ResultBean();
		resultBean.setIata(fogIata);
		resultBean.setIataName("Bookingcom");
		resultBean.setThirdIataCode(thirdIataCode);
		
		HotelBean hotelBean = new HotelBean();
		hotelBean.setThirdHotelCode(thirdHotelCode);
		hotelBean.setFogHotelCode(fogHotelCode);
		hotelBean.setFogIataCode(fogIata);
		hotelBean.setThirdIataCode(thirdIataCode);
		
		List<TResvBase> resvList = ((BookingcomDownloadService) bookingcom_downLoadService)
		        .handleResponseXml4fogbeanList(hotelBean, xml, resultBean);
		for (TResvBase tresvBase : resvList) {
			saveResvToFog(resultBean, fogHotelCode, thirdHotelCode, tresvBase, xml);
		}
		System.out.println("resultBean.getErrorlist().size()" + resultBean.getErrorlist().size());
		System.out.println("resultBean.getErrorlist().size()" + resultBean.getSuccesslist().size());
	}
	
	private void saveResvToFog(ResultBean resultBean, String fogProp, String idsProp, TResvBase tresvBease,
			String responseXML) throws TimeOut4FogException {
		List<ErrorBean> errorlist = resultBean.getErrorlist();
		List<SuccessBean> successlist = resultBean.getSuccesslist();
		try {
			MailFogResvBaseBean saveFogNewResvBean = bookingcom_callFogService.saveFogNewResvBean(tresvBease, validateResvRate);
			log.error("After Calling callFogService.saveFogNewResvBean...");
			String cnfnum = saveFogNewResvBean.getCnfnum();
			SuccessBean successbean = new SuccessBean();
			successbean.settResvBase(tresvBease);
			successbean.setCnfnum(cnfnum);
			successbean.setFogProp(fogProp);
			successbean.setIdsProp(idsProp);
			successbean.setIds_cnfnum(tresvBease.getOutcnfnum());
			successbean.setThirdIataCode(resultBean.getThirdIataCode());
			successbean.setUsername(resultBean.getUsername());
			successbean.setPassword(resultBean.getPassword());
			saveFogNewResvBean.setIata(resultBean.getIata());
			saveFogNewResvBean.setIataName(resultBean.getIataName());
			successbean.setMailFogResvBaseBean(saveFogNewResvBean);

			successlist.add(successbean);
			try {
				/**
				 * 保存成功 加订单
				 */
				EResvMap eResvMap = new EResvMap();
				eResvMap.setAgodaCnfnum(tresvBease.getOutcnfnum());
				List<EResvMap> eresvMapList = getEresvMapDAO().findByExample(eResvMap);
				if (eresvMapList != null && eresvMapList.size() > 0) {
					EResvMap eresvmap = eresvMapList.get(0);
					eresvmap.setFogCnfnum(cnfnum);
					eresvMapDAO.attachDirty(eresvmap);
				} else {
					EResvMap eresvmap = new EResvMap();
					eresvmap.setAgodaCnfnum(tresvBease.getOutcnfnum());
					eresvmap.setCreatedate(new Date());
					eresvmap.setFogCnfnum(cnfnum);
					eresvmap.setRequesteddate(DateUtil.formatDate(new Date()));
					eresvmap.setBk5(resultBean.getThirdIataCode());
					eresvMapDAO.attachDirty(eresvmap);
				}
			} catch (Exception e) {
				log.error(ExceptionUtils.getFullStackTrace(e));
				ErrorBean err = new ErrorBean();
				err.setFogProp(fogProp);
				err.setIdsProp(idsProp);
				err.setErroCode("IDS-0013");
				err.setErrorDesc(mapService.getAlertDesc("IDS-0013"));
				err.setIds_cnfnum(tresvBease.getOutcnfnum());
				err.setErrorMessage(ExceptionUtils.getFullStackTrace(e));

				err.setXml(responseXML);
				err.settResvBase(tresvBease);
				errorlist.add(err);
			}

			log.debug("保存订单Cnfnum:" + cnfnum + "成功！");
		} catch (TimeOut4FogException e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			ErrorBean err = new ErrorBean();
			err.setFogProp(fogProp);
			err.setIdsProp(idsProp);
			err.setErroCode("IDS-0005");
			err.setErrorDesc(mapService.getAlertDesc("IDS-0005"));
			err.setIds_cnfnum(tresvBease.getOutcnfnum());
			err.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
			err.settResvBase(tresvBease);
			err.setXml(responseXML);
			errorlist.add(err);
			throw new TimeOut4FogException(e);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			ErrorBean err = new ErrorBean();
			err.setFogProp(fogProp);
			err.setIdsProp(idsProp);
			err.setErroCode("IDS-0006");
			err.setErrorDesc(mapService.getAlertDesc("IDS-0006"));
			err.setIds_cnfnum(tresvBease.getOutcnfnum());
			err.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
			err.setXml(responseXML);
			err.settResvBase(tresvBease);
			errorlist.add(err);
		}
	}
	
	private ICallFogService bookingcom_callFogService;
	
	private IMapService mapService;
	
	private EResvMapDAO eresvMapDAO;

	
	/**
	 * @return the bookingcom_downLoadService
	 */
	public IDownLoadService getBookingcom_downLoadService() {
		return bookingcom_downLoadService;
	}

	/**
	 * @param bookingcom_downLoadService the bookingcom_downLoadService to set
	 */
	public void setBookingcom_downLoadService(
			IDownLoadService bookingcom_downLoadService) {
		this.bookingcom_downLoadService = bookingcom_downLoadService;
	}

	/**
	 * @return the bookingcom_callFogService
	 */
	public ICallFogService getBookingcom_callFogService() {
		return bookingcom_callFogService;
	}

	/**
	 * @param bookingcom_callFogService the bookingcom_callFogService to set
	 */
	public void setBookingcom_callFogService(
			ICallFogService bookingcom_callFogService) {
		this.bookingcom_callFogService = bookingcom_callFogService;
	}

	/**
	 * @return the eresvMapDAO
	 */
	public EResvMapDAO getEresvMapDAO() {
		return eresvMapDAO;
	}

	/**
	 * @param eresvMapDAO the eresvMapDAO to set
	 */
	public void setEresvMapDAO(EResvMapDAO eresvMapDAO) {
		this.eresvMapDAO = eresvMapDAO;
	}

	/**
	 * @return the mapService
	 */
	public IMapService getMapService() {
		return mapService;
	}

	/**
	 * @param mapService the mapService to set
	 */
	public void setMapService(IMapService mapService) {
		this.mapService = mapService;
	}
	
	
	
	
}
