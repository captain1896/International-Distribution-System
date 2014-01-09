package com.thayer.idsservice.ids.agoda.update.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.thayer.fog2.bo.RateDataBO;
import com.thayer.fog2.entity.AvailAllow;
import com.thayer.fog2.entity.AvailRate;
import com.thayer.idsservice.bean.mapping.MappingEnum;
import com.thayer.idsservice.bean.mapping.MappingResultBean;
import com.thayer.idsservice.dao.EIdsCache;
import com.thayer.idsservice.dao.EIdsCacheKey;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.ids.agoda.Util.AgodaUtils;
import com.thayer.idsservice.ids.agoda.bean.AllotmentInfo;
import com.thayer.idsservice.ids.agoda.bean.AllotmentInfos;
import com.thayer.idsservice.ids.agoda.bean.RateInfo;
import com.thayer.idsservice.ids.agoda.bean.RateInfos;
import com.thayer.idsservice.ids.agoda.bean.RateTypeEnum;
import com.thayer.idsservice.ids.agoda.bean.Return;
import com.thayer.idsservice.ids.agoda.bean.SetAllotment;
import com.thayer.idsservice.ids.agoda.bean.SetRate;
import com.thayer.idsservice.ids.agoda.bean.YCSXMLDocument;
import com.thayer.idsservice.ids.agoda.bean.HeaderDataDocument.HeaderData;
import com.thayer.idsservice.task.update.bean.AvailBean;
import com.thayer.idsservice.task.update.bean.ErrorBean;
import com.thayer.idsservice.task.update.bean.RsStatusBean;
import com.thayer.idsservice.task.update.bean.UpdateInfoBean;
import com.thayer.idsservice.task.update.interf.AbstractUploadService;
import com.thayer.idsservice.util.CommonUtils;
import com.thayer.idsservice.util.Constents;
import com.thayer.idsservice.util.DateUtil;

public class AgodaUpdateService extends AbstractUploadService {
	private static Logger LOGGER = Logger.getLogger(AgodaUpdateService.class);

	private String agodaUserId;

	private String agodaUserPwd;

	public String getAgodaUserId() {
		return agodaUserId;
	}

	public void setAgodaUserId(String agodaUserId) {
		this.agodaUserId = agodaUserId;
	}

	public String getAgodaUserPwd() {
		return agodaUserPwd;
	}

	public void setAgodaUserPwd(String agodaUserPwd) {
		this.agodaUserPwd = agodaUserPwd;
	}

	@Override
	public List<UpdateInfoBean> getThirdRequestXml(AvailBean availbean, List<ErrorBean> errorlist) throws BizException,
			MappingException {
		List<RateDataBO> getzRateMapList = availbean.getRateDataBOList();
		// 如果RateDataBOList为空则返回空list；
		if (getzRateMapList == null || getzRateMapList.size() == 0) {
			return new ArrayList<UpdateInfoBean>();
		}
		YCSXMLDocument ycsxmldocument = YCSXMLDocument.Factory.newInstance();
		SetAllotment addNewSetAllotment = ycsxmldocument.addNewYCSXML().addNewSetAllotment();

		YCSXMLDocument rateYcsxmldocument = YCSXMLDocument.Factory.newInstance();
		SetRate addNewSetRate = rateYcsxmldocument.addNewYCSXML().addNewSetRate();

		{
			/**
			 * ycsxmldocument.addNewSetAllotment
			 */

			int i = 0;
			for (RateDataBO rateDataBO : getzRateMapList) {
				try {
					String planCode = rateDataBO.getPlanCode();
					String prop = rateDataBO.getProp();
					List<AvailAllow> rateList = rateDataBO.getRateList();

					MappingResultBean mappingResultBean = getMapService().getThirdHotelRateCodeByPlanLevel(
							MappingEnum.AGODA, prop, planCode);
					if (mappingResultBean == null || rateList == null || rateList.size() == 0) {
						continue;
					}

					if (i == 0) {
						HeaderData addNewHeaderData = addNewSetAllotment.addNewHeaderData();
						addNewHeaderData.setPassword(agodaUserPwd);
						addNewHeaderData.setUserID(agodaUserId);
						addNewHeaderData.setHotelID(new BigInteger(mappingResultBean.getHotelCode()));
						addNewSetAllotment.addNewAllotmentInfos();
					}

					AllotmentInfos addNewAllotmentInfos = addNewSetAllotment.getAllotmentInfos();

					int k = 0;
					while (k < rateList.size()) {
						AvailAllow availAllow = rateList.get(k);
						String availDate = DateUtil.formatDate(availAllow.getAvailDate());
						String availDateTo = DateUtil.formatDate(availAllow.getAvailDate());
						int allot = availAllow.getAllotment() > 99 ? 99 : availAllow.getAllotment();
						allot = allot < 0 ? 0 : allot;
						String avail = "";

						String allowAvail = availAllow.getAvail();
						String noArrival = "";
						if ("A".equalsIgnoreCase(allowAvail) || "L".equalsIgnoreCase(allowAvail)
								|| "C".equalsIgnoreCase(allowAvail)) {
							avail = "0";
						} else {
							avail = "1";
						}

						if ("C".equalsIgnoreCase(allowAvail)) {
							noArrival = "0";
						} else {
							noArrival = "1";
						}

						int leadTime = availAllow.getLeadTime();
						long cutoffday = leadTime % 24 == 0 ? leadTime / 24 : leadTime / 24 + 1;

						/**
						 * addNewAllotmentInfos
						 */
						AllotmentInfo addNewAllotmentInfo = addNewAllotmentInfos.addNewAllotmentInfo();
						addNewAllotmentInfo.setRoomTypeID(Integer.parseInt(mappingResultBean.getHotelRateCode()));

						addNewAllotmentInfo.setCutOffDay(BigInteger.valueOf(cutoffday));
						addNewAllotmentInfo.setAvailableAllotment(BigInteger.valueOf(allot));

						if ("0".equalsIgnoreCase(avail)) {
							addNewAllotmentInfo.setCloseOut(false);
						} else {
							addNewAllotmentInfo.setCloseOut(true);
						}

						if ("0".equalsIgnoreCase(noArrival)) {
							addNewAllotmentInfo.setNoArrival(true);
						} else {
							addNewAllotmentInfo.setNoArrival(false);
						}

						int j = k + 1;
						while (j < rateList.size()) {
							AvailAllow availAllowNext = rateList.get(j);
							AvailAllow temp = rateList.get(j-1);
							String availDateNext = DateUtil.formatDate(availAllowNext.getAvailDate());
							int allotNext = availAllowNext.getAllotment() > 99 ? 99 : availAllowNext.getAllotment();
							allotNext = allotNext < 0 ? 0 : allotNext;
							String availNext = "";
							if ("A".equalsIgnoreCase(availAllowNext.getAvail())
									|| "L".equalsIgnoreCase(availAllowNext.getAvail())
									|| "C".equalsIgnoreCase(availAllowNext.getAvail())) {
								availNext = "0";
							} else {
								availNext = "1";
							}
							String noArrivalNext = "";
							if ("C".equalsIgnoreCase(availAllowNext.getAvail())) {
								noArrivalNext = "0";
							} else {
								noArrivalNext = "1";
							}

							int leadTimeNext = availAllowNext.getLeadTime();
							long cutoffdayNext = leadTimeNext % 24 == 0 ? leadTimeNext / 24 : leadTimeNext / 24 + 1;

							if ((allotNext == allot) && availNext.equals(avail) && noArrivalNext.equals(noArrival)
									&& cutoffdayNext == cutoffday
									&& DateUtil.dateDiff(DateUtil.TIME_UNIT_D, temp.getAvailDate(), availAllowNext.getAvailDate()) == 1) {
								availDateTo = availDateNext;
							} else {
								break;
							}
							j++;
						}
						if (availDate.equals(availDateTo)) {
							Calendar c1 = Calendar.getInstance();
							c1.setTime(DateUtil.dateValue(availDate));
							addNewAllotmentInfo.setDate(c1);

						} else {
							Calendar c1 = Calendar.getInstance();
							c1.setTime(DateUtil.dateValue(availDate));
							Calendar c2 = Calendar.getInstance();
							c2.setTime(DateUtil.dateValue(availDateTo));
							addNewAllotmentInfo.setDateFrom(c1);
							addNewAllotmentInfo.setDateTo(c2);

						}
						k = j;
					}
				} catch (MappingException e) {
					throw new MappingException(e);
				} catch (Exception e) {
					ErrorBean errorBean = new ErrorBean();
					errorBean.setErroCode("IDS-0012");
					errorBean.setErrorDesc(getMapService().getAlertDesc("IDS-0012"));
					errorBean.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
					errorBean.setXml(rateDataBO.toString());
					errorlist.add(errorBean);
					LOGGER.error("parse getThirdRequestXml error:" + ExceptionUtils.getFullStackTrace(e));

				}
				i++;
			}

			if (i == 0) {
				return new ArrayList<UpdateInfoBean>();
			}
		}

		{

			/**
			 * rateYcsxmldocument.addNewSetRate
			 */
			int i = 0;
			for (RateDataBO rateDataBO : getzRateMapList) {
				try {
					String planCode = rateDataBO.getPlanCode();
					String prop = rateDataBO.getProp();
					List<AvailAllow> rateList = rateDataBO.getRateList();

					MappingResultBean mappingResultBean = getMapService().getThirdHotelRateCodeByPlanLevel(
							MappingEnum.AGODA, prop, planCode);
					if (mappingResultBean == null || rateList == null || rateList.size() == 0) {
						// LOGGER.warn(MappingEnum.AGODA + ",prop:" + prop + ",planCode:" + planCode);
						continue;
					}

					if (i == 0) {
						HeaderData addRateNewHeaderData = addNewSetRate.addNewHeaderData();
						addRateNewHeaderData.setPassword(agodaUserPwd);
						addRateNewHeaderData.setUserID(agodaUserId);
						addRateNewHeaderData.setHotelID(new BigInteger(mappingResultBean.getHotelCode()));
						addNewSetRate.setRateType(RateTypeEnum.NET);
						addNewSetRate.addNewRateInfos();
					}

					RateInfos addNewRateInfos = addNewSetRate.getRateInfos();

					int k = 0;
					while (k < rateList.size()) {
						AvailAllow availAllow = rateList.get(k);
						String availDate = DateUtil.formatDate(availAllow.getAvailDate());
						String availDateTo = DateUtil.formatDate(availAllow.getAvailDate());
						int allot = availAllow.getAllotment() > 99 ? 99 : availAllow.getAllotment();
						allot = allot < 0 ? 0 : allot;
						String avail = "";

						String minLos = String.valueOf(availAllow.getMinLos());

						int leadTime = availAllow.getLeadTime();
						long cutoffday = leadTime % 24 == 0 ? leadTime / 24 : leadTime / 24 + 1;

						/**
						 * addNewRateInfos
						 */
						RateInfo addNewRateInfo = addNewRateInfos.addNewRateInfo();
						addNewRateInfo.setRoomTypeID(Integer.parseInt(mappingResultBean.getHotelRateCode()));

						AvailRate availRate = availAllow.getAvailRate();

						double singleRate = availRate.getSingleRate();
						double doubleRate = 0;
						if (availAllow.getRoomsize() > 1) {
							doubleRate = availRate.getDoubleRate();
						}
						addNewRateInfo.setSingleRate(BigDecimal.valueOf(singleRate));
						addNewRateInfo.setDoubleRate(BigDecimal.valueOf(doubleRate));
						addNewRateInfo.setTripleRate(BigDecimal.valueOf(0));
						addNewRateInfo.setAvailableAllotment(BigInteger.valueOf(allot));
						Integer breakfastNum = availRate.getBreakfastNum();
						boolean breakfastNumBool;
						if (breakfastNum != null && breakfastNum >= 2) {
							breakfastNumBool = true;
						} else {
							breakfastNumBool = false;
						}
						addNewRateInfo.setBreakfastIncluded(breakfastNumBool);

						addNewRateInfo.setCutOffDay(BigInteger.valueOf(cutoffday));
						addNewRateInfo.setMinimumNightStay(BigInteger.valueOf(availAllow.getMinLos()));

						int j = k + 1;
						while (j < rateList.size()) {
							AvailAllow availAllowNext = rateList.get(j);
							AvailAllow temp = rateList.get(j-1);
							String availDateNext = DateUtil.formatDate(availAllowNext.getAvailDate());
							int allotNext = availAllowNext.getAllotment() > 99 ? 99 : availAllowNext.getAllotment();
							allotNext = allotNext < 0 ? 0 : allotNext;

							String minLosNext = String.valueOf(availAllowNext.getMinLos());
							AvailRate availRateNext = availAllowNext.getAvailRate();
							double singleRateNext = availRateNext.getSingleRate();
							double doubleRateNext = availRateNext.getDoubleRate();
							Integer breakfastNumNext = availRateNext.getBreakfastNum();
							boolean breakfastNumBoolNext;
							if (breakfastNumNext != null && breakfastNumNext >= 2) {
								breakfastNumBoolNext = true;
							} else {
								breakfastNumBoolNext = false;
							}
							int leadTimeNext = availAllowNext.getLeadTime();
							long cutoffdayNext = leadTimeNext % 24 == 0 ? leadTimeNext / 24 : leadTimeNext / 24 + 1;

							if ((allotNext == allot) && minLosNext.equals(minLos) && singleRateNext == singleRate
									&& doubleRateNext == doubleRate && cutoffdayNext == cutoffday
									&& breakfastNumBoolNext == breakfastNumBool
									&& DateUtil.dateDiff(DateUtil.TIME_UNIT_D, temp.getAvailDate(), availAllowNext.getAvailDate()) == 1) {
								availDateTo = availDateNext;
							} else {
								break;
							}
							j++;
						}
						if (availDate.equals(availDateTo)) {
							Calendar c1 = Calendar.getInstance();
							c1.setTime(DateUtil.dateValue(availDate));

							addNewRateInfo.setDateFrom(c1);
							addNewRateInfo.setDateTo(c1);
						} else {
							Calendar c1 = Calendar.getInstance();
							c1.setTime(DateUtil.dateValue(availDate));
							Calendar c2 = Calendar.getInstance();
							c2.setTime(DateUtil.dateValue(availDateTo));

							addNewRateInfo.setDateFrom(c1);
							addNewRateInfo.setDateTo(c2);
						}
						k = j;
					}
				} catch (MappingException e) {
					throw new MappingException(e);
				} catch (Exception e) {
					ErrorBean errorBean = new ErrorBean();
					errorBean.setErroCode("IDS-0012");
					errorBean.setErrorDesc(getMapService().getAlertDesc("IDS-0012"));
					errorBean.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
					errorBean.setXml(rateDataBO.toString());
					errorlist.add(errorBean);
					LOGGER.error("parse getThirdRequestXml error:" + ExceptionUtils.getFullStackTrace(e));

				}
				i++;
			}

			if (i == 0) {
				return new ArrayList<UpdateInfoBean>();
			}
		}

		List<UpdateInfoBean> updateInfoBeanList = new ArrayList();
		{
			String ycsxmldocumentstr = ycsxmldocument.toString();
			ycsxmldocumentstr = ycsxmldocumentstr.replaceAll("\\+08:00", "");
			UpdateInfoBean updateInfoBean = new UpdateInfoBean();
			updateInfoBean.setType(Constents.IDS_UPDATE_REQUEST_TYPE1);
			updateInfoBean.setRequestXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + ycsxmldocumentstr);
			updateInfoBean.setReqestURL(getThirdRequestXmlURL());
			LOGGER.info("getThirdRequestXml :\n" + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + ycsxmldocumentstr);
			updateInfoBeanList.add(updateInfoBean);
		}
		{
			String rateYcsxmldocumentstr = rateYcsxmldocument.toString();
			rateYcsxmldocumentstr = rateYcsxmldocumentstr.replaceAll("\\+08:00", "");
			UpdateInfoBean updateInfoBean = new UpdateInfoBean();
			updateInfoBean.setType(Constents.IDS_UPDATE_REQUEST_TYPE1);

			String requestXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + rateYcsxmldocumentstr;
			LOGGER.info("getThirdRequestXml :\n" + requestXml);
			updateInfoBean.setRequestXml(requestXml);
			updateInfoBean.setReqestURL(getThirdRequestXmlURL());
			updateInfoBeanList.add(updateInfoBean);

		}

		return updateInfoBeanList;
	}

	@Override
	public RsStatusBean parseResponseXml(String updateResponseXml, String type) throws BizException {

		LOGGER.info("parseResponseXml : \n" + updateResponseXml);
		RsStatusBean rsStatusBean = new RsStatusBean();
		try {
			String rsXml = AgodaUtils.reHandleRS(updateResponseXml);
			YCSXMLDocument ycsxmldocument = YCSXMLDocument.Factory.parse(rsXml);
			Return returnMsg = ycsxmldocument.getYCSXML().getReturn();
			rsStatusBean.setRsResult(returnMsg.getValid());
			rsStatusBean.setRsMsgId(returnMsg.getTicketID());
			rsStatusBean.setRsStatus(returnMsg.getTicketStatus());
			rsStatusBean.setRsDesc(returnMsg.getDescription());
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			rsStatusBean.setRsResult(false);
			rsStatusBean.setRsMsgId(CommonUtils.createRequestID());
			rsStatusBean.setRsStatus("ERROR");
		}

		return rsStatusBean;

	}


	@Override
	protected void addAvailAllowToCacheList(AvailAllow bean,AvailBean availBean, String plan,
			String prop, String ideType, List<EIdsCache> updateCacheList) {
		EIdsCache cache = new EIdsCache();
		EIdsCacheKey key = new EIdsCacheKey();
		key.setPlanCode(plan);
		key.setProp(prop);
		key.setIdsType(ideType);
		key.setAvailDate(bean.getAvailDate());
		cache.seteIdsCacheKey(key);
		cache.setAllotment(bean.getAllotment());
		cache.setAvail(bean.getAvail());
		cache.setMinLos(bean.getMinLos());
		cache.setLeadTime(bean.getLeadTime());
		cache.setSingleRate(bean.getAvailRate().getSingleRate());
		cache.setDoubleRate(bean.getAvailRate().getDoubleRate());
		cache.setQuadRate(bean.getAvailRate().getDoubleRate());
		cache.setTripleRate(bean.getAvailRate().getDoubleRate());
		cache.setBreakfastNum(bean.getAvailRate().getBreakfastNum());
		updateCacheList.add(cache);

		
	}

	@Override
	protected boolean validateEq(EIdsCache dist, AvailAllow src,AvailBean srcAvailBean) {
		boolean result = true;
		if(dist == null || src==null){
			return false;
		}
		
		if(dist.getDoubleRate() != src.getAvailRate().getDoubleRate()){
			result = false;
			return result;
		}
		if(dist.getSingleRate() != src.getAvailRate().getSingleRate()){
			result = false;
			return result;
		}
		if(dist.getBreakfastNum() != src.getAvailRate().getBreakfastNum()){
			result = false;
			return result;
		}
		if(dist.getMinLos() != src.getMinLos()){
			result = false;
			return result;
		}
		
		int srcLeadTime = src.getLeadTime();
		int distLeadTime = dist.getLeadTime();
		long srcCutoffday = srcLeadTime % 24 == 0 ? srcLeadTime / 24 : srcLeadTime / 24 + 1;
		long distCutoffday = distLeadTime % 24 == 0 ? distLeadTime / 24 : distLeadTime / 24 + 1;
		if(srcCutoffday != distCutoffday){
			result = false;
			return result;
		}
		
		int distAllot = dist.getAllotment() > 99 ? 99 : dist.getAllotment();
		distAllot = distAllot < 0 ? 0 : distAllot;
		int srctAllot = src.getAllotment() > 99 ? 99 : src.getAllotment();
		srctAllot = srctAllot < 0 ? 0 : srctAllot;
		if(distAllot != srctAllot){
			result = false;
			return result;
		}
		String distAvail ="2";
		if ("A".equalsIgnoreCase(dist.getAvail()) || "L".equalsIgnoreCase(dist.getAvail()) || "C".equalsIgnoreCase(dist.getAvail())) {
			distAvail = "0";
		} else {
			distAvail = "1";
		}
		String srcAvail ="2";
		if ("A".equalsIgnoreCase(src.getAvail()) || "L".equalsIgnoreCase(src.getAvail()) || "C".equalsIgnoreCase(src.getAvail())) {
			srcAvail = "0";
		} else {
			srcAvail = "1";
		}
		if(!distAvail.equals(srcAvail)){
			result = false;
			return result;
		}
		return result;
	}

}
