package com.thayer.idsservice.ids.asiarooms.receive.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.GDuration;
import org.apache.xmlbeans.XmlCalendar;
import org.apache.xmlbeans.XmlException;
import org.opentravel.ota.x2003.x05.BasicPropertyInfoType;
import org.opentravel.ota.x2003.x05.CustomerType;
import org.opentravel.ota.x2003.x05.DateTimeSpanType;
import org.opentravel.ota.x2003.x05.ErrorType;
import org.opentravel.ota.x2003.x05.ErrorsType;
import org.opentravel.ota.x2003.x05.GuaranteeType;
import org.opentravel.ota.x2003.x05.GuestCountType;
import org.opentravel.ota.x2003.x05.HotelResRequestType;
import org.opentravel.ota.x2003.x05.HotelResResponseType;
import org.opentravel.ota.x2003.x05.HotelReservationIDsType;
import org.opentravel.ota.x2003.x05.HotelReservationType;
import org.opentravel.ota.x2003.x05.HotelReservationsType;
import org.opentravel.ota.x2003.x05.OTAHotelResRQDocument;
import org.opentravel.ota.x2003.x05.OTAHotelResRSDocument;
import org.opentravel.ota.x2003.x05.PaymentCardType;
import org.opentravel.ota.x2003.x05.PersonNameType;
import org.opentravel.ota.x2003.x05.ProfileType;
import org.opentravel.ota.x2003.x05.ProfilesType;
import org.opentravel.ota.x2003.x05.RatePlanType;
import org.opentravel.ota.x2003.x05.ResGlobalInfoType;
import org.opentravel.ota.x2003.x05.ResGuestsType;
import org.opentravel.ota.x2003.x05.RoomRateType;
import org.opentravel.ota.x2003.x05.RoomStaysType;
import org.opentravel.ota.x2003.x05.RoomTypeType;
import org.opentravel.ota.x2003.x05.SourceType;
import org.opentravel.ota.x2003.x05.TelephoneInfoType;
import org.opentravel.ota.x2003.x05.TimeUnitType;
import org.opentravel.ota.x2003.x05.TotalType;
import org.opentravel.ota.x2003.x05.TransactionActionType;
import org.opentravel.ota.x2003.x05.TransactionStatusType;
import org.opentravel.ota.x2003.x05.CustomerType.Address;
import org.opentravel.ota.x2003.x05.GuestCountType.GuestCount;
import org.opentravel.ota.x2003.x05.HotelReservationIDsType.HotelReservationID;
import org.opentravel.ota.x2003.x05.ProfilesType.ProfileInfo;
import org.opentravel.ota.x2003.x05.RateType.Rate;
import org.opentravel.ota.x2003.x05.ResGuestsType.ResGuest;
import org.opentravel.ota.x2003.x05.RoomStayType.RatePlans;
import org.opentravel.ota.x2003.x05.RoomStayType.RoomRates;
import org.opentravel.ota.x2003.x05.RoomStayType.RoomTypes;
import org.opentravel.ota.x2003.x05.RoomStaysType.RoomStay;
import org.opentravel.ota.x2003.x05.SourceType.RequestorID;
import org.opentravel.ota.x2003.x05.TransactionActionType.Enum;
import org.springframework.util.StringUtils;

import com.thayer.idsservice.bean.MailFogResvBaseBean;
import com.thayer.idsservice.bean.PriceValidResult;
import com.thayer.idsservice.dao.EResvMap;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.task.download.bean.TResvBase;
import com.thayer.idsservice.task.receive.interf.AbstractReceiveResvService;
import com.thayer.idsservice.util.Constents;
import com.thayer.idsservice.util.DateUtil;

public class AsiaroomsReceiveResvService extends AbstractReceiveResvService {

    protected Logger LOGGER = Logger.getLogger(AsiaroomsReceiveResvService.class);

    /**
	 * 
	 */
    private static final long serialVersionUID = 1014793194249176219L;

    private String asiaroomsUserId;

    private String asiaroomsUserPwd;

    /*
     * (non-Javadoc)
     * 
     * @seecom.thayer.idsservice.task.receive.interf.AbstractReceiveResvService#buildRSXml(com.thayer.idsservice.bean.
     * MailFogResvBaseBean, java.lang.String)
     */
    @Override
    public String buildRSXml(MailFogResvBaseBean saveFogNewResvBean, String requestXml) {

        try {

            OTAHotelResRSDocument hotelResRSDocument = OTAHotelResRSDocument.Factory.newInstance();
            HotelResResponseType addNewOTAHotelResRS = hotelResRSDocument.addNewOTAHotelResRS();
            addNewOTAHotelResRS.setTimeStamp(Calendar.getInstance());
            addNewOTAHotelResRS.addNewSuccess();

            OTAHotelResRQDocument otahotelResRQDocument = OTAHotelResRQDocument.Factory.parse(requestXml);
            HotelResRequestType otaHotelResRQ = otahotelResRQDocument.getOTAHotelResRQ();
            Enum resStatus = otaHotelResRQ.getResStatus();

            if (TransactionActionType.IGNORE.intValue() == resStatus.intValue()) {
                addNewOTAHotelResRS.setResResponseType(TransactionStatusType.IGNORED);
            }
            if (TransactionActionType.CANCEL.intValue() == resStatus.intValue()) {
                addNewOTAHotelResRS.setResResponseType(TransactionStatusType.CANCELLED);
            }
            if (TransactionActionType.INITIATE.intValue() == resStatus.intValue()
                    || TransactionActionType.MODIFY.intValue() == resStatus.intValue()) {

                addNewOTAHotelResRS.setResResponseType(TransactionStatusType.PENDING);
                addNewOTAHotelResRS.setHotelReservations(otaHotelResRQ.getHotelReservations());
                HotelReservationType hotelReservationArray = addNewOTAHotelResRS.getHotelReservations()
                        .getHotelReservationArray(0);

                RoomStay[] roomStayArray = hotelReservationArray.getRoomStays().getRoomStayArray();
                for (RoomStay roomStay : roomStayArray) {
                    RoomRates roomRates = roomStay.addNewRoomRates();
                    RoomRateType roomRate = roomRates.addNewRoomRate();
                    roomRate.setRoomTypeCode(saveFogNewResvBean.getRoomcode());
                    roomRate.setRatePlanCode(saveFogNewResvBean.getRateCode());
                    String indate = saveFogNewResvBean.getIndate();
                    String outdate = saveFogNewResvBean.getOutdate();
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-mm-dd");
                    LOGGER.info("indate: " + indate);
                    XmlCalendar inDateCal = new XmlCalendar(indate);
                    roomRate.setEffectiveDate(inDateCal);
                    LOGGER.info("outdate: " + outdate);
                    XmlCalendar outDateCal = new XmlCalendar(outdate);
                    roomRate.setExpireDate(outDateCal);
                    Rate rate = roomRate.addNewRates().addNewRate();
                    rate.setRateTimeUnit(TimeUnitType.DAY);
                    rate.setUnitMultiplier(new BigInteger("1", 10));
                    rate.setEffectiveDate(roomRate.getEffectiveDate());
                    rate.setExpireDate(roomRate.getExpireDate());
                    TotalType base = rate.addNewBase();
                    base.setCurrencyCode(saveFogNewResvBean.getCurrencyCode());
                    base.setAmountAfterTax(new BigDecimal(saveFogNewResvBean.getProptotalrevenue()
                            / saveFogNewResvBean.getRooms()));
                }

                ResGlobalInfoType resGlobalInfo = hotelReservationArray.getResGlobalInfo();
                TotalType total = resGlobalInfo.addNewTotal();
                total.setAmountAfterTax(new BigDecimal(saveFogNewResvBean.getProptotalrevenue()));
                total.setCurrencyCode(saveFogNewResvBean.getCurrencyCode());
                HotelReservationID fogHotelReservationID = resGlobalInfo.getHotelReservationIDs()
                        .addNewHotelReservationID();
                fogHotelReservationID.setResIDType("14");
                fogHotelReservationID.setResIDValue(saveFogNewResvBean.getCnfnum());
                fogHotelReservationID.setResIDSource("Hubs1");
                fogHotelReservationID.setResIDDate(Calendar.getInstance());

            }
            String fogCnfnum = "";
            String asiaroomsCnfnum = "";
            if (TransactionActionType.COMMIT.intValue() == resStatus.intValue()) {
                addNewOTAHotelResRS.setResResponseType(TransactionStatusType.COMMITTED);
                HotelReservationType addNewHotelReservation = addNewOTAHotelResRS.addNewHotelReservations()
                        .addNewHotelReservation();
                ResGlobalInfoType resGlobalInfo = otaHotelResRQ.getHotelReservations().getHotelReservationArray(0)
                        .getResGlobalInfo();
                HotelReservationIDsType hotelReservationIDs = resGlobalInfo.getHotelReservationIDs();
                resGlobalInfo.unsetGuarantee();
                HotelReservationID[] hotelReservationIDArray = hotelReservationIDs.getHotelReservationIDArray();

                for (HotelReservationID hotelReservationID : hotelReservationIDArray) {
                    String resIDType = hotelReservationID.getResIDType();
                    /**
                     * <HotelReservationID ResID_Type="5" <br>
                     * ResID_Value="11726439R"<br>
                     * ResID_Source="Laterooms"<br>
                     * ResID_Date="2010-11-23T00:00:00"/><br>
                     * <HotelReservationID ResID_Type="14"<br>
                     * ResID_Value="32146585"<br>
                     * ResID_Source="ProviderName"<br>
                     * ResID_Date="2010-11-23T01:16:16.149Z"/><br>
                     */
                    if ("5".equalsIgnoreCase(resIDType)) {
                        asiaroomsCnfnum = hotelReservationID.getResIDValue();
                    } else if ("14".equalsIgnoreCase(resIDType)) {
                        fogCnfnum = hotelReservationID.getResIDValue();
                    }
                }
                addNewHotelReservation.setResGlobalInfo(resGlobalInfo);
            }

            try {
                EResvMap eResvMap = new EResvMap();
                eResvMap.setBk5(getThirdIataCode());
                if (saveFogNewResvBean == null) {
                    eResvMap.setAgodaCnfnum(asiaroomsCnfnum);
                    eResvMap.setFogCnfnum(fogCnfnum);
                } else {
                    eResvMap.setAgodaCnfnum(saveFogNewResvBean.getOutcnfnum());
                    eResvMap.setFogCnfnum(saveFogNewResvBean.getCnfnum());
                }
                List<EResvMap> eresvMapList = getEresvMapDAO().findByExample(eResvMap);
                if (eresvMapList != null && eresvMapList.size() > 0) {
                    EResvMap eresvmap = eresvMapList.get(0);
                    eresvmap.setBk6(String.valueOf(addNewOTAHotelResRS.getResResponseType().intValue()));
                    getEresvMapDAO().attachDirty(eresvmap);
                }
            } catch (Exception e) {
                LOGGER.error(ExceptionUtils.getFullStackTrace(e));
            }

            LOGGER.info("Asiarooms return xml: " + hotelResRSDocument.toString());
            return hotelResRSDocument.toString();
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getFullStackTrace(e));
            return returnErrorMsg(e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.thayer.idsservice.task.receive.interf.AbstractReceiveResvService#returnErrorMsg(java.lang.Throwable)
     */
    @Override
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

    @Override
    public TResvBase convertXml4fogbean(String requestXml) throws BizException, MappingException {
        try {
            TResvBase tresvbase = new TResvBase();
            tresvbase.setIata(getIata());
            String fogPropId = "";
            OTAHotelResRQDocument otahotelResRQDocument = OTAHotelResRQDocument.Factory.parse(requestXml);
            HotelResRequestType otaHotelResRQ = otahotelResRQDocument.getOTAHotelResRQ();

            SourceType sourceArray = otaHotelResRQ.getPOS().getSourceArray(0);
            RequestorID requestorID = sourceArray.getRequestorID();
            String id = requestorID.getID();
            String messagePassword = requestorID.getMessagePassword();
            if (!id.equals(asiaroomsUserId) || !messagePassword.equals(asiaroomsUserPwd)) {
                throw new BizException("username and password is wrong!");
            }

            HotelReservationsType hotelReservations = otaHotelResRQ.getHotelReservations();
            HotelReservationType[] hotelReservationArray = hotelReservations.getHotelReservationArray();
            if (hotelReservationArray == null) {
                throw new BizException("No reservation info!");
            }
            if (hotelReservationArray.length > 1) {
                throw new BizException("Can not process more reservations request!");
            }
            HotelReservationType hotelReservationType = hotelReservationArray[0];
            RoomStaysType roomStays = hotelReservationType.getRoomStays();
            RoomStay[] roomStayArray = roomStays.getRoomStayArray();
            String roomTypeTmp = null;
            String rateClassTmp = null;
            if (roomStayArray.length > 1) {
                for (RoomStay roomStay : roomStayArray) {
                    if (StringUtils.hasText(roomTypeTmp)
                            && !roomTypeTmp.equals(roomStay.getRoomTypes().getRoomTypeArray(0).getRoomTypeCode())) {
                        throw new BizException("Can not process more room type request!");
                    } else {
                        roomTypeTmp = roomStay.getRoomTypes().getRoomTypeArray(0).getRoomTypeCode();
                    }

                    if (StringUtils.hasText(rateClassTmp)
                            && !rateClassTmp.equals(roomStay.getRatePlans().getRatePlanArray(0).getRatePlanCode())) {
                        throw new BizException("Can not process more rate type request!");
                    } else {
                        rateClassTmp = roomStay.getRatePlans().getRatePlanArray(0).getRatePlanCode();
                    }
                }

            }

            Enum resStatus = otaHotelResRQ.getResStatus();
            // commit操作直接返回
            if (TransactionActionType.COMMIT.intValue() == resStatus.intValue()) {
                tresvbase.setIdsStatus(String.valueOf(resStatus.intValue()));
                return null;
            } else {
                tresvbase.setIdsStatus(String.valueOf(resStatus.intValue()));
            }

            // ResGlobalInfo
            ResGlobalInfoType resGlobalInfo = hotelReservationType.getResGlobalInfo();
            // ResGlobalInfo.HotelReservationIDs
            HotelReservationIDsType hotelReservationIDs = resGlobalInfo.getHotelReservationIDs();
            HotelReservationID[] hotelReservationIDArray = hotelReservationIDs.getHotelReservationIDArray();
            String fogCnfnum = "";
            String asiaroomsCnfnum = "";
            for (HotelReservationID hotelReservationID : hotelReservationIDArray) {
                String resIDType = hotelReservationID.getResIDType();
                /**
                 * <HotelReservationID ResID_Type="5" <br>
                 * ResID_Value="11726439R"<br>
                 * ResID_Source="Laterooms"<br>
                 * ResID_Date="2010-11-23T00:00:00"/><br>
                 * <HotelReservationID ResID_Type="14"<br>
                 * ResID_Value="32146585"<br>
                 * ResID_Source="ProviderName"<br>
                 * ResID_Date="2010-11-23T01:16:16.149Z"/><br>
                 */
                if ("5".equalsIgnoreCase(resIDType)) {
                    asiaroomsCnfnum = hotelReservationID.getResIDValue();
                } else if ("14".equalsIgnoreCase(resIDType)) {
                    fogCnfnum = hotelReservationID.getResIDValue();
                }
            }

            RoomStay roomStay = roomStayArray[0];
            // RoomStay.BasicPropertyInfo
            BasicPropertyInfoType basicPropertyInfo = roomStay.getBasicPropertyInfo();
            fogPropId = basicPropertyInfo.getHotelCode();// hotelCode
            tresvbase.setProp(fogPropId);

            tresvbase.setCnfnum(fogCnfnum);
            tresvbase.setOutcnfnum(asiaroomsCnfnum);

            if (TransactionActionType.CANCEL.intValue() == resStatus.intValue()
                    || TransactionActionType.IGNORE.intValue() == resStatus.intValue()) {
                tresvbase.setResvtype(Constents.RESV_TYPE_CANCEL);
                return tresvbase;
            }
            if (TransactionActionType.INITIATE.intValue() == resStatus.intValue()) {
                tresvbase.setResvtype(Constents.RESV_TYPE_NEW);
            }

            if (TransactionActionType.MODIFY.intValue() == resStatus.intValue()) {
                tresvbase.setResvtype(Constents.RESV_TYPE_EDIT);
            }

            // ResGlobalInfo.GuestCounts
            GuestCountType guestCounts = resGlobalInfo.getGuestCounts();
            GuestCount[] guestCountArray = guestCounts.getGuestCountArray();
            for (GuestCount guestCount : guestCountArray) {
                String ageQualifyingCode = guestCount.getAgeQualifyingCode();
                int count = guestCount.getCount();
                if ("10".equalsIgnoreCase(ageQualifyingCode)) {
                    // adult
                    tresvbase.setAdults(count);
                } else if ("8".equalsIgnoreCase(ageQualifyingCode)) {
                    // child
                    tresvbase.setChildren(count);
                } else {

                }
            }

            // ResGlobalInfo.TimeSpan
            DateTimeSpanType timeSpan = resGlobalInfo.getTimeSpan();
            Calendar start = timeSpan.getStart();// First stay date of booking
            /**
             * Always in “P{0}D” format, e.g.<br>
             * P1D – 1 day<br>
             * P2D – 2 days
             */
            GDuration duration = (GDuration) timeSpan.getDuration();
            int daysint = duration.getDay();// Duration of booking

            tresvbase.setIndate(start.getTime());
            Calendar outdate = Calendar.getInstance();
            outdate.setTime(start.getTime());
            outdate.add(Calendar.DAY_OF_YEAR, daysint);
            tresvbase.setOutdate(outdate.getTime());

            // RoomStays

            int rooms = 0;

            // RoomStay.RoomType
            RoomTypes roomTypes = roomStay.getRoomTypes();
            RoomTypeType[] roomTypeArray = roomTypes.getRoomTypeArray();
            if (roomTypeArray == null || roomTypeArray.length == 0) {
                throw new BizException("Asiarooms Receive Reservation Error:" + "roomTypeArray is empty!");
            }
            if (roomTypeArray.length > 1) {
                throw new BizException("Asiarooms Receive Reservation Error:" + "exisit more than two RoomTypes!");
            }
            RoomTypeType roomTypeType = roomTypeArray[0];
            String roomTypeCode = roomTypeType.getRoomTypeCode();
            int numberOfUnits = roomTypeType.getNumberOfUnits();
            rooms = numberOfUnits * roomStayArray.length;
            String fogroomtype = roomTypeCode;
            tresvbase.setRoomcode(fogroomtype);

            // RoomStay.RatePlan
            RatePlans ratePlans = roomStay.getRatePlans();
            RatePlanType[] ratePlanArray = ratePlans.getRatePlanArray();
            if (ratePlanArray == null || ratePlanArray.length == 0) {
                throw new BizException("Asiarooms Receive Reservation Error:" + "ratePlanArray is empty!");
            }
            if (ratePlanArray.length > 1) {
                throw new BizException("Asiarooms Receive Reservation Error:" + "exisit more than two ratePlans!");
            }
            RatePlanType RatePlanType = ratePlanArray[0];
            String ratePlanCode = RatePlanType.getRatePlanCode();
            String fograteClass = ratePlanCode;
            tresvbase.setRateClass(fograteClass);

            GuaranteeType[] guaranteeArray = roomStay.getGuaranteeArray();
            for (GuaranteeType guaranteeType : guaranteeArray) {
                String guaranteeCode = guaranteeType.getGuaranteeCode();
                if ("CC".equalsIgnoreCase(guaranteeCode)) {
                    PaymentCardType paymentCard = guaranteeType.getGuaranteesAccepted().getGuaranteeAcceptedArray(0)
                            .getPaymentCard();

                    tresvbase.setCctype(paymentCard.getCardCode());
                    tresvbase.setCcnumber(paymentCard.getCardNumber());
                    tresvbase.setCvv(paymentCard.getSeriesCode());
                    tresvbase.setCcexpiration(paymentCard.getExpireDate());
                    tresvbase.setCcname(paymentCard.getCardHolderName());

                } else if ("FP".equalsIgnoreCase(guaranteeCode)) {

                }
            }

            tresvbase.setNights(daysint);
            tresvbase.setRooms(rooms);

            ResGuestsType resGuests = hotelReservationType.getResGuests();
            ResGuest[] resGuestArray = resGuests.getResGuestArray();
            ResGuest resGuest = resGuestArray[0];
            if (resGuest.getArrivalTime() != null) {
                try {
                    tresvbase.setArrivaltime(DateUtil.getHoldTimeDate(resGuest.getArrivalTime().toString().substring(0,
                            5)));
                } catch (Exception e) {
                }
            }
            ProfilesType profiles = resGuest.getProfiles();
            ProfileInfo[] profileInfoArray = profiles.getProfileInfoArray();
            for (ProfileInfo profileInfo : profileInfoArray) {
                ProfileType profile = profileInfo.getProfile();
                CustomerType customer = profile.getCustomer();
                PersonNameType personName = customer.getPersonName();
                String surname = personName.getSurname();
                String[] givenNameArray = personName.getGivenNameArray();
                String givenName = "";
                if (givenNameArray.length > 0) {
                    givenName = givenNameArray[0];
                }
                tresvbase.setGfirstname(givenName);
                tresvbase.setGlastname(surname);
                TelephoneInfoType[] telephoneArray = customer.getTelephoneArray();
                if (telephoneArray.length > 0) {
                    TelephoneInfoType telephoneInfoType = telephoneArray[0];
                    String phoneNumber = telephoneInfoType.getPhoneNumber();
                    tresvbase.setGuestphone(phoneNumber);
                }

                Address[] addressArray = customer.getAddressArray();
                if (addressArray.length > 0) {
                    Address address = addressArray[0];
                    String[] addressLineArray = address.getAddressLineArray();
                    String addressstr = "";
                    for (String addre : addressLineArray) {
                        addressstr += addre + " ";
                    }
                    tresvbase.setGuestaddress(addressstr);
                }

            }

            // total price valid
            RoomRateType roomRateArray = roomStay.getRoomRates().getRoomRateArray(0);
            TotalType total = roomRateArray.getRates().getRateArray(0).getTotal();
            BigDecimal amountAfterTax = new BigDecimal(total.getAmountAfterTax().doubleValue() * rooms);

            // remark
            StringBuilder remark = new StringBuilder();
            remark.append("1.Laterooms ID: <" + tresvbase.getOutcnfnum() + ">。");
            remark.append("2. 特殊要求: ");
            if (requestXml.contains("NonSmoking=\"true\"")) {
                remark.append("无烟房。");
            }
            remark.append("3.房费及杂费，均由客人前台现付。");
            remark.append("4.客人已提供信用卡做担保,请在收到预订后马上进行验证并为客人保留房间。");
            try {
                remark.append("5.客人其它要求："
                        + roomStay.getSpecialRequests().getSpecialRequestArray(0).getTextArray(0).getStringValue());
            } catch (Exception e) {

            }

            tresvbase.setRemark(remark.toString());
            PriceValidResult validTotalPrice = getCallFogService().validTotalPrice(amountAfterTax.doubleValue(),
                    tresvbase);
            if (!validTotalPrice.isVaildResult()) {
                throw new BizException("Error occured during total price is no same................. ");
            }

            tresvbase.setResvRateList(validTotalPrice.getResvRateList());

            return tresvbase;
        } catch (BizException e) {
            LOGGER.error(ExceptionUtils.getFullStackTrace(e));
            throw e;
        } catch (XmlException e) {
            LOGGER.error("parse xml error:" + ExceptionUtils.getFullStackTrace(e));
            throw new BizException(e);
        } catch (Exception e) {
            LOGGER.error("parse xml error:" + ExceptionUtils.getFullStackTrace(e));
            throw new BizException(e);
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
}
