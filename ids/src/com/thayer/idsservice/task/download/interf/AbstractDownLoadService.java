package com.thayer.idsservice.task.download.interf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.thayer.idsservice.bean.MailFogResvBaseBean;
import com.thayer.idsservice.dao.EResvMap;
import com.thayer.idsservice.dao.EResvMapDAO;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.exception.TimeOut4FogException;
import com.thayer.idsservice.exception.TimeOut4ThirdException;
import com.thayer.idsservice.exception.TimeOutException;
import com.thayer.idsservice.service.interf.ICallFogService;
import com.thayer.idsservice.service.interf.IHttpClientService;
import com.thayer.idsservice.service.interf.IMapService;
import com.thayer.idsservice.task.download.bean.ErrorBean;
import com.thayer.idsservice.task.download.bean.HotelBean;
import com.thayer.idsservice.task.download.bean.ResultBean;
import com.thayer.idsservice.task.download.bean.SuccessBean;
import com.thayer.idsservice.task.download.bean.TResvBase;
import com.thayer.idsservice.task.download.bean.TResvRate;
import com.thayer.idsservice.util.DateUtil;
import com.thayer.idsservice.util.TimeSettingConfig;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Zolt Zhang<br>
 * @since : 2010-9-15<br>
 * @version : v1.0
 */
public abstract class AbstractDownLoadService implements IDownLoadService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4493727983808501951L;
	/**
	 * 
	 */
	private IHttpClientService httpClientService;
	/**
	 * 订单查询请求地址。(必须)
	 */
	private String bookingListUrl;

	/**
	 * 订单详情接口地址<br/>
	 * 不设置，将忽略调用
	 */
	private String bookingDetailUrl;

	/**
	 * 记录时间戳的key值
	 */
	private String timeSetKey;

	/**
	 * 时间戳的Key值是否根据酒店ID区分
	 */
	private boolean timeSetKeySplit = false;
	/**
	 * 发送请求的参数名，发送形式: key = value <br/>
	 * 不设置时，将只发送内容。
	 */
	private String requestXmlKey;// 请求postXml的key值

	/**
	 * 时间戳设置类
	 */
	private TimeSettingConfig timeSettingConfig;

	/**
	 * 订单下载时间戳文件设置<br/>
	 * 不设置时，系统将不使用时间戳
	 */
	private String settingFile;// timeSetting文件路径

	private ICallFogService callFogService;

	protected EResvMapDAO eresvMapDAO;

	/**
	 * 是否需要验证订单的每日价。默认:true
	 */
	private boolean validateResvRate = true;

	private IMapService mapService;

	public boolean isValidateResvRate() {
		return validateResvRate;
	}

	public void setValidateResvRate(boolean validateResvRate) {
		this.validateResvRate = validateResvRate;
	}

	public EResvMapDAO getEresvMapDAO() {
		return eresvMapDAO;
	}

	public void setEresvMapDAO(EResvMapDAO eresvMapDAO) {
		this.eresvMapDAO = eresvMapDAO;
	}

	public String getSettingFile() {
		return settingFile;
	}

	public void setSettingFile(String settingFile) {
		this.settingFile = settingFile;
	}

	public TimeSettingConfig getTimeSettingConfig() {
		return timeSettingConfig;
	}

	public void setTimeSettingConfig(TimeSettingConfig timeSettingConfig) {
		this.timeSettingConfig = timeSettingConfig;
	}

	public String getRequestXmlKey() {
		return requestXmlKey;
	}

	public void setRequestXmlKey(String requestXmlKey) {
		this.requestXmlKey = requestXmlKey;
	}

	public String getTimeSetKey() {
		return timeSetKey;
	}

	public void setTimeSetKey(String timeSetKey) {
		this.timeSetKey = timeSetKey;
	}

	private static Logger log = Logger.getLogger(AbstractDownLoadService.class);

	public String getBookingDetailUrl() {
		return bookingDetailUrl;
	}

	public void setBookingDetailUrl(String bookingDetailUrl) {
		this.bookingDetailUrl = bookingDetailUrl;
	}

	public String getBookingListUrl() {
		return bookingListUrl;
	}

	public void setBookingListUrl(String bookingListUrl) {
		this.bookingListUrl = bookingListUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.task.download.interf.IDownLoadService#downLoad(java.lang.String,
	 * com.thayer.idsservice.task.download.bean.ResultBean)
	 */
	public void downLoad(HotelBean hotelBean, ResultBean resultBean) throws BizException, TimeOut4ThirdException,
			TimeOut4FogException, MappingException {

		String fogProp = hotelBean.getFogHotelCode();
		String idsProp = hotelBean.getThirdHotelCode();
		List<ErrorBean> errorlist = resultBean.getErrorlist();
		List<SuccessBean> successlist = resultBean.getSuccesslist();
		Date since = null, till = null;
		if (StringUtils.hasText(settingFile)) {
			if (timeSetKeySplit) {
				since = timeSettingConfig.getTimeSettingValue(settingFile, timeSetKey + "_" + idsProp, DateUtil
						.formatDate(new Date()));
			} else {
				since = timeSettingConfig.getTimeSettingValue(settingFile, timeSetKey, DateUtil.formatDate(new Date()));
			}
			till = new Date();
			log.debug("get sinceDate:" + DateUtil.formatDate(since));
		}

		hotelBean.setRequestSince(since);

		hotelBean.setRequestTill(till);

		log.debug("订单下载开始！！");
		String bookingListRequestXml = "";
		try {
			bookingListRequestXml = getBookingListRequestXml(hotelBean);
			log.debug("send resv download request xml: \n" + bookingListRequestXml);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			ErrorBean errorBean = new ErrorBean();
			errorBean.setFogProp(fogProp);
			errorBean.setIdsProp(idsProp);
			errorBean.setErroCode("IDS-0001");
			errorBean.setErrorDesc(mapService.getAlertDesc("IDS-0001"));
			errorBean.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
			errorlist.add(errorBean);
			throw new BizException(e);
		}
		log.debug("取得订单列表的请求xml!\nbookingListRequestXml:" + bookingListRequestXml);
		if (StringUtils.hasText(bookingListRequestXml)) {
			String bookingListResponseXml = "";

			try {
				if (StringUtils.hasText(requestXmlKey)) {
					Map parms = new HashMap();
					parms.put(requestXmlKey, bookingListRequestXml);

					bookingListResponseXml = httpClientService.postHttpRequest(bookingListUrl, parms);

				} else {

					bookingListResponseXml = httpClientService.postHttpRequest(bookingListUrl, bookingListRequestXml);

				}

			} catch (TimeOutException e) {
				log.warn(ExceptionUtils.getFullStackTrace(e) + "bookingListRequestXml:" + bookingListRequestXml);
				ErrorBean errorBean = new ErrorBean();
				errorBean.setFogProp(fogProp);
				errorBean.setIdsProp(idsProp);
				errorBean.setErroCode("IDS-0002");
				errorBean.setErrorDesc(mapService.getAlertDesc("IDS-0002"));
				errorBean.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
				errorlist.add(errorBean);

				throw new TimeOut4ThirdException(e);
			} catch (Exception e) {
				log.error("bookingListRequestXml:" + bookingListRequestXml + "    /n"
						+ ExceptionUtils.getFullStackTrace(e));
				ErrorBean errorBean = new ErrorBean();
				errorBean.setFogProp(fogProp);
				errorBean.setIdsProp(idsProp);
				errorBean.setErroCode("IDS-0003");
				errorBean.setErrorDesc(mapService.getAlertDesc("IDS-0003"));
				errorBean.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
				errorlist.add(errorBean);

				throw new BizException(e);
			}
			
//			bookingListResponseXml="<reservations>  <reservation>    <commissionamount>272.16</commissionamount>    <currencycode>CNY</currencycode>    <customer>      <address>plaza de tres carabelas</address>      <cc_cvc>254</cc_cvc>      <cc_expiration_date>07/2015</cc_expiration_date>      <cc_name>aaef</cc_name>      <cc_number>5489018063560222</cc_number>      <cc_type>MasterCard</cc_type>      <city>caiz</city>      <company></company>      <countrycode>es</countrycode>      <dc_issue_number></dc_issue_number>      <dc_start_date></dc_start_date>      <email>taoscofield@hotmail.com</email>      <first_name>宏陶</first_name>      <last_name>高</last_name>      <remarks></remarks>      <telephone>+34672970155</telephone>      <zip>11004</zip>    </customer>    <date>2013-12-11</date>    <hotel_id>66655</hotel_id>    <hotel_name>Jin Jiang Park Hotel</hotel_name>    <id>852352670</id>    <room>      <arrival_date>2013-12-12</arrival_date>      <commissionamount>272.16</commissionamount>      <currencycode>CNY</currencycode>      <departure_date>2013-12-26</departure_date>      <extra_info>This air-conditioned room comes with a flat-screen TV, a personal safe and minibar. Ironing facilities and a fax machine are available upon request. En suite bathroom has a bathtub.</extra_info>      <facilities>Tea/Coffee Maker, Minibar, Shower, Bath, Safety Deposit Box, Telephone, Air Conditioning, Hairdryer, Wake Up Service/Alarm Clock, Bathrobe, Desk, Seating Area, Free toiletries, Toilet, Bathroom, Satellite Channels, Cable Channels, Carpeted Floor, Flat-screen TV</facilities>      <guest_name>宏陶 高</guest_name>      <id>6665502</id>      <info>No meal is included in this room rate. Children and Extra Bed Policy: All children are welcome. All children under 12 years stay free of charge when using existing beds. One older child or adult is charged CNY 250 per person per night  in an extra bed. One child under 2 years stays free of charge in a child's cot/crib. The maximum number of extra beds/children's cots permitted in a room is 1.  Deposit Policy: No deposit will be charged.  Cancellation Policy: If cancelled  up to 1 day before the date of arrival,  no fee will be charged. If cancelled  later or in case of no-show, 100 percent of the first night will be charged. </info>      <max_children>0</max_children>      <meal_plan>No meal is included in this room rate.</meal_plan>      <name>Standard Twin Room</name>      <numberofguests>2</numberofguests>      <facilities>Tea/Coffee Maker, Minibar, Shower, Bath, Safety Deposit Box, Telephone, Air Conditioning, Hairdryer, Wake Up Service/Alarm Clock, Bathrobe, Desk, Seating Area, Free toiletries, Toilet, Bathroom, Satellite Channels, Cable Channels, Carpeted Floor, Flat-screen TV</facilities>      <guest_name>宏陶 高</guest_name>      <id>6665502</id>      <info>No meal is included in this room rate. Children and Extra Bed Policy: All children are welcome. All children under 12 years stay free of charge when using existing beds. One older child or adult is charged CNY 250 per person per night  in an extra bed. One child under 2 years stays free of charge in a child's cot/crib. The maximum number of extra beds/children's cots permitted in a room is 1.  Deposit Policy: No deposit will be charged.  Cancellation Policy: If cancelled  up to 1 day before the date of arrival,  no fee will be charged. If cancelled  later or in case of no-show, 100 percent of the first night will be charged. </info>      <max_children>0</max_children>      <meal_plan>No meal is included in this room rate.</meal_plan>      <name>Standard Twin Room</name>      <numberofguests>2</numberofguests>      <price date=\"2013-12-22\"             rate_id=\"2352634\">486</price>      <price date=\"2013-12-23\"             rate_id=\"2352634\">486</price>      <price date=\"2013-12-24\"             rate_id=\"2352634\">486</price>      <price date=\"2013-12-25\"             rate_id=\"2352634\">486</price>      <remarks></remarks>      <roomreservation_id>381774728</roomreservation_id>      <smoking>0</smoking>      <totalprice>1944</totalprice>    </room>    <status>new</status>    <time>17:41:08</time>    <totalprice>1944</totalprice>  </reservation></reservations>";
//			bookingListResponseXml="<reservations>  <reservation>    <commissionamount>272.16</commissionamount>    <currencycode>CNY</currencycode>    <customer>      <address>plaza de tres carabelas</address>      <cc_cvc>254</cc_cvc>      <cc_expiration_date>07/2015</cc_expiration_date>      <cc_name>?abc</cc_name>      <cc_number>5489018063560222</cc_number>      <cc_type>MasterCard</cc_type>      <city>caiz</city>      <company></company>      <countrycode>es</countrycode>      <dc_issue_number></dc_issue_number>      <dc_start_date></dc_start_date>      <email>taoscofield@hotmail.com</email>      <first_name>宏陶</first_name>      <last_name>高</last_name>      <remarks></remarks>      <telephone>+34672970155</telephone>      <zip>11004</zip>    </customer>    <date>2013-12-09</date>    <hotel_id>66655</hotel_id>    <hotel_name>Jin Jiang Park Hotel</hotel_name>    <id>1167088</id>    <room>      <arrival_date>2013-12-12</arrival_date>      <commissionamount>272.16</commissionamount>      <currencycode>CNY</currencycode>      <departure_date>2013-12-26</departure_date>      <extra_info>This air-conditioned room comes with a flat-screen TV, a personal safe and minibar. Ironing facilities and a fax machine are available upon request. En suite bathroom has a bathtub.</extra_info>      <facilities>Tea/Coffee Maker, Minibar, Shower, Bath, Safety Deposit Box, Telephone, Air Conditioning, Hairdryer, Wake Up Service/Alarm Clock, Bathrobe, Desk, Seating Area, Free toiletries, Toilet, Bathroom, Satellite Channels, Cable Channels, Carpeted Floor, Flat-screen TV</facilities>      <guest_name>宏陶 高</guest_name>      <id>6665502</id>      <info>No meal is included in this room rate. Children and Extra Bed Policy: All children are welcome. All children under 12 years stay free of charge when using existing beds. One older child or adult is charged CNY 250 per person per night  in an extra bed. One child under 2 years stays free of charge in a child's cot/crib. The maximum number of extra beds/children's cots permitted in a room is 1.  Deposit Policy: No deposit will be charged.  Cancellation Policy: If cancelled  up to 1 day before the date of arrival,  no fee will be charged. If cancelled  later or in case of no-show, 100 percent of the first night will be charged. </info>      <max_children>0</max_children>      <meal_plan>No meal is included in this room rate.</meal_plan>      <name>Standard Twin Room</name>      <numberofguests>2</numberofguests>      <facilities>Tea/Coffee Maker, Minibar, Shower, Bath, Safety Deposit Box, Telephone, Air Conditioning, Hairdryer, Wake Up Service/Alarm Clock, Bathrobe, Desk, Seating Area, Free toiletries, Toilet, Bathroom, Satellite Channels, Cable Channels, Carpeted Floor, Flat-screen TV</facilities>      <guest_name>宏陶 高</guest_name>      <id>6665502</id>      <info>No meal is included in this room rate. Children and Extra Bed Policy: All children are welcome. All children under 12 years stay free of charge when using existing beds. One older child or adult is charged CNY 250 per person per night  in an extra bed. One child under 2 years stays free of charge in a child's cot/crib. The maximum number of extra beds/children's cots permitted in a room is 1.  Deposit Policy: No deposit will be charged.  Cancellation Policy: If cancelled  up to 1 day before the date of arrival,  no fee will be charged. If cancelled  later or in case of no-show, 100 percent of the first night will be charged. </info>      <max_children>0</max_children>      <meal_plan>No meal is included in this room rate.</meal_plan>      <name>Standard Twin Room</name>      <numberofguests>2</numberofguests>      <price date=\"2013-12-22\"             rate_id=\"2352634\">1111</price>            <remarks></remarks>      <roomreservation_id>381774728</roomreservation_id>      <smoking>0</smoking>      <totalprice>1111</totalprice>    </room>    <status>new</status>    <time>17:41:08</time>    <totalprice>1111</totalprice>  </reservation></reservations>";
			
			if (StringUtils.hasText(bookingListResponseXml)) {
				/**
				 * handleResponseXml4detailXml方法如果没有返回 则调用handleResponseXml4fogbean<br>
				 * 否则则说明有订单详细信息 需要循环调用handleResponseXml4fogbean
				 */
				String[] handleResponseXml4detailRequestXml = null;
				try {
					handleResponseXml4detailRequestXml = handleResponseXml4detailXml(hotelBean, bookingListResponseXml);
				} catch (Exception e) {
					log.error(ExceptionUtils.getFullStackTrace(e));
					log.error("bookingListResponseXml:" + bookingListResponseXml + "    /n"
							+ ExceptionUtils.getFullStackTrace(e));

					ErrorBean errorBean = new ErrorBean();
					errorBean.setFogProp(fogProp);
					errorBean.setIdsProp(idsProp);
					errorBean.setErroCode("IDS-0004");
					errorBean.setErrorDesc(mapService.getAlertDesc("IDS-0004"));
					errorBean.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
					errorBean.setXml(bookingListResponseXml);
					errorlist.add(errorBean);

					throw new BizException(bookingListResponseXml, e);
				}
				// 返回的是订单详情
				if (handleResponseXml4detailRequestXml == null) {
					try {
						List<TResvBase> handleResponseXml4fogbeanList = handleResponseXml4fogbeanList(hotelBean,
								bookingListResponseXml, resultBean);
						for (TResvBase tresvBase : handleResponseXml4fogbeanList) {
							saveResvToFog(resultBean, fogProp, idsProp, tresvBase, bookingListResponseXml);
						}
					} catch (BizException e) {
						log.error(ExceptionUtils.getFullStackTrace(e));
						log.error("bookingListResponseXml:" + bookingListResponseXml + "    /n"
								+ ExceptionUtils.getFullStackTrace(e));
						ErrorBean err = new ErrorBean();
						err.setFogProp(fogProp);
						err.setIdsProp(idsProp);
						err.setErroCode("IDS-0007");
						err.setErrorDesc(mapService.getAlertDesc("IDS-0007"));
						err.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
						err.setXml(bookingListResponseXml);
						errorlist.add(err);
						throw new BizException(bookingListResponseXml, e);
					}
				} else {
					// 返回的是订单列表，需要在循环调用接口，获取订单的具体信息,处理某条定订出错发邮件,继续循环下一条定单
					for (String str : handleResponseXml4detailRequestXml) {
						if (!StringUtils.hasText(str)) {
							continue;
						}
						String bookingDetailResponseXml = "";
						if (StringUtils.hasText(bookingDetailUrl)) {
							try {
								if (StringUtils.hasText(requestXmlKey)) {
									Map parms = new HashMap();
									parms.put(requestXmlKey, str);

									bookingDetailResponseXml = httpClientService.postHttpRequest(bookingDetailUrl,
											parms);

								} else {

									bookingDetailResponseXml = httpClientService.postHttpRequest(bookingDetailUrl, str);

								}
							} catch (TimeOutException e) {
								log.error("handleResponseXml4detailRequestXml:" + str + "    /n"
										+ ExceptionUtils.getFullStackTrace(e));
								ErrorBean errorBean = new ErrorBean();
								errorBean.setFogProp(fogProp);
								errorBean.setIdsProp(idsProp);
								errorBean.setErroCode("IDS-0002");
								errorBean.setErrorDesc(mapService.getAlertDesc("IDS-0002"));
								errorBean.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
								errorlist.add(errorBean);
								throw new TimeOut4ThirdException(e);
							} catch (Exception e) {
								log.error("handleResponseXml4detailRequestXml:" + str + "    /n"
										+ ExceptionUtils.getFullStackTrace(e));
								ErrorBean errorBean = new ErrorBean();
								errorBean.setFogProp(fogProp);
								errorBean.setIdsProp(idsProp);
								errorBean.setErroCode("IDS-0003");
								errorBean.setErrorDesc(mapService.getAlertDesc("IDS-0003"));
								errorBean.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
								errorlist.add(errorBean);
							}
						} else {
							bookingDetailResponseXml = str;
						}
						try {
							TResvBase handleResponseXml4fogbean = handleResponseXml4fogbean(hotelBean,
									bookingDetailResponseXml);
							saveResvToFog(resultBean, fogProp, idsProp, handleResponseXml4fogbean,
									bookingDetailResponseXml);

						} catch (MappingException e) {
							log.error(ExceptionUtils.getFullStackTrace(e));
							log.error("bookingListResponseXml:" + bookingDetailResponseXml + "    /n"
									+ ExceptionUtils.getFullStackTrace(e));
							ErrorBean errorBean = new ErrorBean();
							errorBean.setFogProp(fogProp);
							errorBean.setIdsProp(idsProp);
							errorBean.setErroCode("IDS-0014");
							errorBean.setErrorDesc(mapService.getAlertDesc("IDS-0014"));
							errorBean.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
							errorBean.setXml(bookingDetailResponseXml);
							errorlist.add(errorBean);
						} catch (BizException e) {
							log.error(ExceptionUtils.getFullStackTrace(e));
							log.error("bookingListResponseXml:" + bookingDetailResponseXml + "    /n"
									+ ExceptionUtils.getFullStackTrace(e));
							ErrorBean errorBean = new ErrorBean();
							errorBean.setFogProp(fogProp);
							errorBean.setIdsProp(idsProp);
							errorBean.setErroCode("IDS-0007");
							errorBean.setErrorDesc(mapService.getAlertDesc("IDS-0007"));
							errorBean.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
							errorBean.setXml(bookingDetailResponseXml);
							errorlist.add(errorBean);
						}
					}
				}
			}

		} else {
			ErrorBean errorBean = new ErrorBean();
			errorBean.setFogProp(fogProp);
			errorBean.setIdsProp(idsProp);
			errorBean.setErroCode("IDS-0001");
			errorBean.setErrorDesc(mapService.getAlertDesc("IDS-0001"));
			errorlist.add(errorBean);
			throw new BizException("resvRequestXml is null for hotelCode:" + hotelBean.getThirdHotelCode());
		}

		if (StringUtils.hasText(settingFile)) {
			log.info("wirte to file with tillDate:" + DateUtil.formatDate(hotelBean.getRequestTill()));
			if (timeSetKeySplit) {
				timeSettingConfig.setTimeSettingValue(settingFile, timeSetKey + "_" + idsProp, hotelBean
						.getRequestTill());
			} else {
				timeSettingConfig.setTimeSettingValue(settingFile, timeSetKey, hotelBean.getRequestTill());
			}
		}
		log.debug("订单下载完毕！！");
	}

	/**
	 * save resv info to fog.
	 * 
	 * @param errorlist
	 * @param successlist
	 * @param tresvBease
	 * @throws TimeOut4FogException
	 */
	private void saveResvToFog(ResultBean resultBean, String fogProp, String idsProp, TResvBase tresvBease,
			String responseXML) throws TimeOut4FogException {
		List<ErrorBean> errorlist = resultBean.getErrorlist();
		List<SuccessBean> successlist = resultBean.getSuccesslist();
		MailFogResvBaseBean saveFogNewResvBean = null;
		try {
			saveFogNewResvBean = callFogService.saveFogNewResvBean(tresvBease, validateResvRate);
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
		
		try {
			/**
			 * 保存成功 加订单
			 */
			EResvMap eResvMap = new EResvMap();
			eResvMap.setAgodaCnfnum(tresvBease.getOutcnfnum());
			
			String cnfnum = saveFogNewResvBean==null?null:saveFogNewResvBean.getCnfnum();
			
			List<EResvMap> eresvMapList = getEresvMapDAO().findByExample(eResvMap);
			if (eresvMapList != null && eresvMapList.size() > 0) {
				EResvMap eresvmap = eresvMapList.get(0);
				eresvmap.setFogCnfnum(cnfnum);
				eresvmap.setPriceInfo(getPriceInfoByTResvRate(tresvBease));
				eresvMapDAO.attachDirty(eresvmap);
			} else {
				EResvMap eresvmap = new EResvMap();
				eresvmap.setAgodaCnfnum(tresvBease.getOutcnfnum());
				eresvmap.setCreatedate(new Date());
				eresvmap.setFogCnfnum(cnfnum);
				eresvmap.setRequesteddate(DateUtil.formatDate(new Date()));
				eresvmap.setBk3(tresvBease.getIndate());//入住时间
				eresvmap.setBk5(resultBean.getThirdIataCode());
				eresvmap.setPriceInfo(getPriceInfoByTResvRate(tresvBease));
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

	}

	/**
	 * 拼接订单价格信息
	 * @author Denile.Zhang
	 * @date 2012-8-21
	 * @param tresvBease
	 * @return
	 */
	public static String getPriceInfoByTResvRate(TResvBase tresvBease) {
		//保存价格信息 做对比用
		StringBuilder priceInfo = new StringBuilder();//ex:日期=当日价格|日期=当日价格
		List<TResvRate> rates = tresvBease.getResvRateList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(rates == null || rates.isEmpty()){
			return null;
		}
		for (TResvRate tResvRate : rates) {
			priceInfo.append(sdf.format(tResvRate.getBookendate()));
			priceInfo.append("=");
			priceInfo.append(tResvRate.getProprevenue());
			priceInfo.append("|");
		}
		return priceInfo.toString();
	}

	/**
	 * 返回fogbean的list
	 * 
	 * @param bookingListResponseXml
	 * @return
	 */
	public abstract List<TResvBase> handleResponseXml4fogbeanList(HotelBean hotelBean, String bookingListResponseXml,
			ResultBean resultBean) throws BizException;

	/**
	 * 返回详细订单的请求xml
	 * 
	 * @param bookingListResponseXml
	 * @return
	 */
	public abstract String[] handleResponseXml4detailXml(HotelBean hotelBean, String bookingListResponseXml)
			throws BizException;

	/**
	 * 返回fogbean
	 * 
	 * @param bookingListResponseXml
	 * @return
	 */
	public abstract TResvBase handleResponseXml4fogbean(HotelBean hotelBean, String bookingResponseXml)
			throws BizException, MappingException;

	/**
	 * 组合生成请求订单列表的xml<br>
	 * 根据代理的具体情况
	 * 
	 * @return
	 */
	public abstract String getBookingListRequestXml(HotelBean hotelBean) throws BizException;

	public ICallFogService getCallFogService() {
		return callFogService;
	}

	public void setCallFogService(ICallFogService callFogService) {
		this.callFogService = callFogService;
	}

	public IHttpClientService getHttpClientService() {
		return httpClientService;
	}

	public void setHttpClientService(IHttpClientService httpClientService) {
		this.httpClientService = httpClientService;
	}

	/**
	 * @return the mapService
	 */
	public IMapService getMapService() {
		return mapService;
	}

	/**
	 * @param mapService
	 *            the mapService to set
	 */
	public void setMapService(IMapService mapService) {
		this.mapService = mapService;
	}

	/**
	 * @return the timeSetKeySplit
	 */
	public boolean isTimeSetKeySplit() {
		return timeSetKeySplit;
	}

	/**
	 * @param timeSetKeySplit
	 *            the timeSetKeySplit to set
	 */
	public void setTimeSetKeySplit(boolean timeSetKeySplit) {
		this.timeSetKeySplit = timeSetKeySplit;
	}

}
