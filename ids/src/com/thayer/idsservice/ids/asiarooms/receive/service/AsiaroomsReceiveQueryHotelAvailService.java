/*****************************************************************<br>
 * <B>FILE :</B> AsiaroomsReceiveQueryHotelAvailService.java <br>
 * <B>CREATE DATE :</B> 2011-1-19 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.ids.asiarooms.receive.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.GDuration;
import org.apache.xmlbeans.XmlCalendar;
import org.opentravel.ota.x2003.x05.BasicPropertyInfoType;
import org.opentravel.ota.x2003.x05.DateTimeSpanType;
import org.opentravel.ota.x2003.x05.ErrorType;
import org.opentravel.ota.x2003.x05.ErrorsType;
import org.opentravel.ota.x2003.x05.GuestCountType;
import org.opentravel.ota.x2003.x05.HotelResResponseType;
import org.opentravel.ota.x2003.x05.HotelSearchCriteriaType;
import org.opentravel.ota.x2003.x05.HotelSearchCriterionType;
import org.opentravel.ota.x2003.x05.OTAHotelAvailRQDocument;
import org.opentravel.ota.x2003.x05.OTAHotelAvailRSDocument;
import org.opentravel.ota.x2003.x05.OTAHotelResRSDocument;
import org.opentravel.ota.x2003.x05.RateType;
import org.opentravel.ota.x2003.x05.RoomRateType;
import org.opentravel.ota.x2003.x05.RoomStayCandidateType;
import org.opentravel.ota.x2003.x05.SourceType;
import org.opentravel.ota.x2003.x05.TotalType;
import org.opentravel.ota.x2003.x05.TransactionStatusType;
import org.opentravel.ota.x2003.x05.AvailRequestSegmentsType.AvailRequestSegment;
import org.opentravel.ota.x2003.x05.AvailRequestSegmentsType.AvailRequestSegment.RoomStayCandidates;
import org.opentravel.ota.x2003.x05.GuestCountType.GuestCount;
import org.opentravel.ota.x2003.x05.ItemSearchCriterionType.HotelRef;
import org.opentravel.ota.x2003.x05.OTAHotelAvailRQDocument.OTAHotelAvailRQ;
import org.opentravel.ota.x2003.x05.OTAHotelAvailRQDocument.OTAHotelAvailRQ.AvailRequestSegments;
import org.opentravel.ota.x2003.x05.OTAHotelAvailRSDocument.OTAHotelAvailRS;
import org.opentravel.ota.x2003.x05.OTAHotelAvailRSDocument.OTAHotelAvailRS.RoomStays;
import org.opentravel.ota.x2003.x05.OTAHotelAvailRSDocument.OTAHotelAvailRS.RoomStays.RoomStay;
import org.opentravel.ota.x2003.x05.RateType.Rate;
import org.opentravel.ota.x2003.x05.RoomStayType.RoomRates;
import org.opentravel.ota.x2003.x05.SourceType.RequestorID;

import com.thayer.fog2.bo.RateDataBO;
import com.thayer.fog2.entity.AvailAllow;
import com.thayer.fog2.entity.AvailRate;
import com.thayer.idsservice.bean.ReceiveTypeEnum;
import com.thayer.idsservice.dao.ERatemap;
import com.thayer.idsservice.dao.ERatemapDAO;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.exception.TimeOut4FogException;
import com.thayer.idsservice.exception.TimeOut4ThirdException;
import com.thayer.idsservice.service.interf.ICallFogService;
import com.thayer.idsservice.task.receive.interf.IReceiveQueryHotelAvailService;
import com.thayer.idsservice.util.DateUtil;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-1-19<br>
 * @version : v1.0
 */
public class AsiaroomsReceiveQueryHotelAvailService implements IReceiveQueryHotelAvailService {

    private static Logger LOGGER = Logger.getLogger(AsiaroomsReceiveQueryHotelAvailService.class);

    private ICallFogService callFogService;

    private String asiaroomsUserId;

    private String asiaroomsUserPwd;
    
    private String iata;
    
    private ERatemapDAO eratemapDAO;

    public ICallFogService getCallFogService() {
        return callFogService;
    }

    public void setCallFogService(ICallFogService callFogService) {
        this.callFogService = callFogService;
    }

    /**
     * IDS代码：例如：AGODA，GENARES...
     */
    private String thirdIataCode;

    public String getThirdIataCode() {
        return thirdIataCode;
    }

    public void setThirdIataCode(String thirdIataCode) {
        this.thirdIataCode = thirdIataCode;
    }

    @Override
    public String queryHotelAvail(String xml, ReceiveTypeEnum type) throws BizException, TimeOut4ThirdException,
            TimeOut4FogException, MappingException {
        String responseXml = "";

        try {
            responseXml = parsHotelAvailRQ(xml);
        } catch (BizException e) {
            return returnErrorMsg(e);
        } catch (Exception e) {
            return returnErrorMsg(e);
        }

        return responseXml;
    }

    public String returnErrorMsg(Throwable e) {
        OTAHotelResRSDocument hotelResRSDocument = OTAHotelResRSDocument.Factory.newInstance();
        HotelResResponseType hotelResRS = hotelResRSDocument.addNewOTAHotelResRS();
        hotelResRS.setResResponseType(TransactionStatusType.UNSUCCESSFUL);
        hotelResRS.setTimeStamp(Calendar.getInstance());
        ErrorsType errors = hotelResRS.addNewErrors();
        ErrorType error = errors.addNewError();
        error.setType("1");
        error.setCode("366");
        error.setRecordID("1");
        error.setShortText("Error during processing, please retry");
        error.setStringValue("Error during processing, please retry");
        LOGGER.info("return error xml: " + hotelResRSDocument.toString());
        return hotelResRSDocument.toString();
    }

    @SuppressWarnings("unchecked")
	private String parsHotelAvailRQ(String xml) throws MappingException, BizException {
        LOGGER.info("Asiarooms query Hotel Avail request xml: \n" + xml);
        try {
            SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
            OTAHotelAvailRQDocument parse = OTAHotelAvailRQDocument.Factory.parse(xml);
            OTAHotelAvailRQ otaHotelAvailRQ = parse.getOTAHotelAvailRQ();
            SourceType sourceArray = otaHotelAvailRQ.getPOS().getSourceArray(0);
            RequestorID requestorID = sourceArray.getRequestorID();
            String id = requestorID.getID();
            String messagePassword = requestorID.getMessagePassword();
            if (!id.equals(asiaroomsUserId) || !messagePassword.equals(asiaroomsUserPwd)) {
                throw new BizException("username and password is wrong!");
            }

            OTAHotelAvailRSDocument newInstance = OTAHotelAvailRSDocument.Factory.newInstance();
            OTAHotelAvailRS otaHotelAvailRS = newInstance.addNewOTAHotelAvailRS();
            otaHotelAvailRS.addNewSuccess();
            RoomStays addNewRoomStays = otaHotelAvailRS.addNewRoomStays();

            // RoomStay
            RoomStay addNewRoomStay = addNewRoomStays.addNewRoomStay();

            // AvailRequestSegment
            AvailRequestSegments availRequestSegments = otaHotelAvailRQ.getAvailRequestSegments();
            AvailRequestSegment[] availRequestSegmentArray = availRequestSegments.getAvailRequestSegmentArray();
            String fogPropId = "";
            for (AvailRequestSegment availRequestSegment : availRequestSegmentArray) {
                // AvailRequestSegment.StayDateRange
                DateTimeSpanType stayDateRange = availRequestSegment.getStayDateRange();

                Calendar start = stayDateRange.getStart();// First date room stay applies to Date, yyyy-mm-dd format
                Calendar end = stayDateRange.getEnd();
                // Number of days room stay
                // applies to.
                // Always in “P{0}D” format, e.g.
                // 1 day – P1D
                // 2 days – P2D
                // 3 days – P3D
                GDuration duration = (GDuration) stayDateRange.getDuration();
                int nights = duration.getDay();
                // int nights = Integer.parseInt(duration.substring(1, 2));
                HotelSearchCriteriaType hotelSearchCriteria = availRequestSegment.getHotelSearchCriteria();
                HotelSearchCriterionType[] criterionArray = hotelSearchCriteria.getCriterionArray();
                if (criterionArray.length > 0) {
                    HotelSearchCriterionType hotelSearchCriterionType = criterionArray[0];
                    HotelRef hotelRef = hotelSearchCriterionType.getHotelRef();
                    String exHotelCode = hotelRef.getHotelCode();// Id of hotel in providers system

                    fogPropId = exHotelCode;

                    // RoomStay.TimeSpan
                    DateTimeSpanType addNewTimeSpan = addNewRoomStay.addNewTimeSpan();
                    addNewTimeSpan.setStart(start);
                    addNewTimeSpan.setDuration(duration);
                    addNewTimeSpan.setEnd(end);

                    // AvailRequestSegment.RoomStayCandidates
                    RoomStayCandidates roomStayCandidates = availRequestSegment.getRoomStayCandidates();
                    RoomStayCandidateType[] roomStayCandidateArray = roomStayCandidates.getRoomStayCandidateArray();
                    int count = 0;
                    if (roomStayCandidateArray.length > 0) {
                        RoomStayCandidateType roomStayCandidateType = roomStayCandidateArray[0];
                        GuestCountType guestCounts = roomStayCandidateType.getGuestCounts();
                        GuestCount[] guestCountArray = guestCounts.getGuestCountArray();

                        GuestCountType addNewGuestCounts = addNewRoomStay.addNewGuestCounts();
                        addNewGuestCounts.setIsPerRoom(true);
                        for (GuestCount guestCount : guestCountArray) {
                            // AgeQualifyingCode See appendix 20.19 Integer, only “10” (adult) and “8”(child) supported.
                            String ageQualifyingCode = guestCount.getAgeQualifyingCode();
                            GuestCount addNewGuestCount = addNewGuestCounts.addNewGuestCount();
                            addNewGuestCount.setAgeQualifyingCode(ageQualifyingCode);
                            // Count Number of adults / children rates apply to
                            if ("10".equalsIgnoreCase(ageQualifyingCode)) {
                                count += guestCount.getCount();
                                addNewGuestCount.setCount(guestCount.getCount());
                            } else if ("8".equalsIgnoreCase(ageQualifyingCode)) {
                                count += guestCount.getCount();
                                addNewGuestCount.setCount(guestCount.getCount());
                            }
                        }
                    }

                    List<RateDataBO> zRateMap = new ArrayList<RateDataBO>();
                    ERatemap queryPara = new ERatemap();
                    queryPara.setExwebCode(iata);
                    queryPara.setFogpropId(fogPropId);
                    List<ERatemap> rateMapList = eratemapDAO.findByExample(queryPara);
                    if(CollectionUtils.isNotEmpty(rateMapList)){
                    	for(ERatemap rateMap : rateMapList){
                    		String planId = rateMap.getFograteId();
                    		if(StringUtils.isNotBlank(planId)){
                    			List<RateDataBO> tempList = callFogService.getZRateMap(fogPropId, start.getTime(), nights, planId.trim(), "Website");
                    			if(CollectionUtils.isNotEmpty(tempList)){
                    				zRateMap.addAll(tempList);
                    			}
                    		}
                    	}
                    }
                    
//                    List<RateDataBO> zRateMap = callFogService.getZRateMap(fogPropId, start.getTime(), nights, null,
//                            null, null, "Website");
                    LOGGER.debug("========================================================");
                    LOGGER.debug(zRateMap);
                    LOGGER.debug("========================================================");
                    for (RateDataBO rateDataBO : zRateMap) {
                        String planCode = rateDataBO.getPlanCode();
                        String prop = rateDataBO.getProp();
                        String roomId = rateDataBO.getRoom().getRoomId();

                        RoomRates addNewRoomRates = addNewRoomStay.addNewRoomRates();

                        RoomRateType addNewRoomRate = addNewRoomRates.addNewRoomRate();
                        addNewRoomRate.setRoomTypeCode(roomId);// RoomTypeCode Id of room
                        // type in providers
                        String[] planCodeSplit = planCode.split("-");
                        addNewRoomRate.setRatePlanCode(planCodeSplit[1]);// RatePlanCode Id of rate
                        // type in providers
                        // system
                        List<AvailAllow> rateList = rateDataBO.getRateList();
                        if (rateList == null || nights == 0 || rateList.size() != nights) {
                            otaHotelAvailRS.unsetRoomStays();
                            return newInstance.toString();
                        }
                        int i = 0;
                        while (i < rateList.size()) {
                            AvailAllow availAllow = rateList.get(i);
                            Date availDate = availAllow.getAvailDate();
                            Date availDateTo = availAllow.getAvailDate();
                            String avail = availAllow.getAvail();
                            boolean isClose = true;
                            if ("A".equalsIgnoreCase(avail) || "L".equalsIgnoreCase(avail)
                                    || "C".equalsIgnoreCase(avail)) {
                                isClose = false;

                            } else {
                                isClose = true;
                            }
                            int allot = availAllow.getAllotment() > 4999 ? 4999 : availAllow.getAllotment();
                            if (isClose) {
                                allot = 0;// 如果关闭的话设置房量为0
                            }

                            addNewRoomRate.setNumberOfUnits(allot);// NumberOfUnits Number of rooms available

                            AvailRate availRate = availAllow.getAvailRate();
                            String currencyCode = availRate.getCurrencyCode();
                            BigDecimal rate = new BigDecimal(0);
                            if (availRate != null) {
                                double doubleRate = 0.0;
                                switch (count) {
                                    case 1:
                                        doubleRate = availRate.getSingleRate();
                                        break;
                                    case 2:
                                        doubleRate = availRate.getDoubleRate();
                                        break;
                                    case 3:
                                        doubleRate = availRate.getTripleRate();
                                        break;
                                    default:
                                        doubleRate = 0.0;
                                }
                                LOGGER.debug("------------------------------------");
                                LOGGER.debug(availAllow.getRoomCode() + "---" + availAllow.getRateCode()
                                        + " Guest count:" + count + "  Price : " + doubleRate);
                                LOGGER.debug("------------------------------------");
                                BigDecimal bd1 = BigDecimal.valueOf(doubleRate);
                                rate = bd1.setScale(2, bd1.ROUND_HALF_UP);
                            }
                            int j = i + 1;
                            while (j < rateList.size()) {
                                AvailAllow availAllowNext = rateList.get(j);
                                Date availDateNext = availAllowNext.getAvailDate();
                                int allotNext = availAllowNext.getAllotment() > 4999 ? 4999 : availAllowNext
                                        .getAllotment();
                                String availNext = availAllowNext.getAvail();
                                boolean isCloseNext = true;
                                if ("A".equalsIgnoreCase(availNext) || "L".equalsIgnoreCase(availNext)
                                        || "C".equalsIgnoreCase(availNext)) {
                                    isCloseNext = false;
                                } else {
                                    isCloseNext = true;
                                }
                                if (isCloseNext) {
                                    allotNext = 0;
                                }
                                AvailRate availRateNext = availAllowNext.getAvailRate();
                                String currencyCodeNext = availRateNext.getCurrencyCode();
                                BigDecimal rateNext = new BigDecimal(0);
                                if (availRateNext != null) {
                                    double doubleRateNext = 0.0;
                                    switch (count) {
                                        case 1:
                                            doubleRateNext = availRateNext.getSingleRate();
                                            break;
                                        case 2:
                                            doubleRateNext = availRateNext.getDoubleRate();
                                            break;
                                        case 3:
                                            doubleRateNext = availRateNext.getTripleRate();
                                            break;
                                        default:
                                            doubleRateNext = 0.0;
                                    }
                                    BigDecimal bd1Next = BigDecimal.valueOf(doubleRateNext);
                                    rateNext = bd1Next.setScale(2, bd1Next.ROUND_HALF_UP);
                                }
                                if (allotNext == allot && currencyCodeNext.equals(currencyCode)
                                        && rateNext.doubleValue() == rate.doubleValue()) {
                                    availDateTo = availDateNext;
                                } else {
                                    break;
                                }
                                j++;
                            }

                            // int spiltNights = DateUtil.dateDiff(DateUtil.TIME_UNIT_D, availDate, availDateTo);
                            RateType addNewRates = addNewRoomRate.addNewRates();
                            Rate addNewRate = addNewRates.addNewRate();
                            TotalType addNewBase = addNewRate.addNewBase();
                            addNewBase.setAmountAfterTax(rate);// AmountAfterTax
                            // Cost of
                            // rate after tax
                            addNewBase.setCurrencyCode(currencyCode);

                            availDateTo = DateUtil.dateAdd(DateUtil.TIME_UNIT_D, 1, availDateTo);
                            XmlCalendar fromDate = new XmlCalendar(dateFmt.format(availDate));
                            XmlCalendar toDate = new XmlCalendar(dateFmt.format(availDateTo));

                            addNewRate.setEffectiveDate(fromDate);// EffectiveDate First date rate applies to
                            addNewRate.setExpireDate(toDate);// ExpireDate Date rate no longer applies
                            i = j;
                        }
                    }
                }
            }
            BasicPropertyInfoType addNewBasicPropertyInfo = addNewRoomStay.addNewBasicPropertyInfo();
            addNewBasicPropertyInfo.setHotelCode(fogPropId);
            LOGGER.info("Asiarooms query Hotel Avail response xml: \n" + newInstance.toString());
            return newInstance.toString();
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getFullStackTrace(e));
            throw new BizException("parsXml error for OTAHotelResRQDocument by xml:" + xml);
        }
    }

    /**
     * @return the asiaroomsUserId
     */
    public String getAsiaroomsUserId() {
        return asiaroomsUserId;
    }

    /**
     * @param asiaroomsUserId the asiaroomsUserId to set
     */
    public void setAsiaroomsUserId(String asiaroomsUserId) {
        this.asiaroomsUserId = asiaroomsUserId;
    }

    /**
     * @return the asiaroomsUserPwd
     */
    public String getAsiaroomsUserPwd() {
        return asiaroomsUserPwd;
    }

    /**
     * @param asiaroomsUserPwd the asiaroomsUserPwd to set
     */
    public void setAsiaroomsUserPwd(String asiaroomsUserPwd) {
        this.asiaroomsUserPwd = asiaroomsUserPwd;
    }

	/**
	 * @return the iata
	 */
	public String getIata() {
		return iata;
	}

	/**
	 * @param iata the iata to set
	 */
	public void setIata(String iata) {
		this.iata = iata;
	}

	/**
	 * @return the eratemapDAO
	 */
	public ERatemapDAO getEratemapDAO() {
		return eratemapDAO;
	}

	/**
	 * @param eratemapDAO the eratemapDAO to set
	 */
	public void setEratemapDAO(ERatemapDAO eratemapDAO) {
		this.eratemapDAO = eratemapDAO;
	}
    
	
    
}
