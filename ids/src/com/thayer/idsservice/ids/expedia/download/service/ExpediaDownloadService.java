package com.thayer.idsservice.ids.expedia.download.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.StringUtils;

import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRQDocument;
import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRSDocument;
import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRQDocument.BookingRetrievalRQ;
import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRQDocument.BookingRetrievalRQ.Authentication;
import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRQDocument.BookingRetrievalRQ.Hotel;
import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRSDocument.BookingRetrievalRS;
import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRSDocument.BookingRetrievalRS.Bookings;
import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRSDocument.BookingRetrievalRS.Bookings.Booking;
import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRSDocument.BookingRetrievalRS.Bookings.Booking.PrimaryGuest;
import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRSDocument.BookingRetrievalRS.Bookings.Booking.RewardProgram;
import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRSDocument.BookingRetrievalRS.Bookings.Booking.RoomStay;
import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRSDocument.BookingRetrievalRS.Bookings.Booking.SpecialRequest;
import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRSDocument.BookingRetrievalRS.Bookings.Booking.PrimaryGuest.Name;
import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRSDocument.BookingRetrievalRS.Bookings.Booking.PrimaryGuest.Phone;
import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRSDocument.BookingRetrievalRS.Bookings.Booking.RoomStay.GuestCount;
import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRSDocument.BookingRetrievalRS.Bookings.Booking.RoomStay.PaymentCard;
import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRSDocument.BookingRetrievalRS.Bookings.Booking.RoomStay.PerDayRates;
import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRSDocument.BookingRetrievalRS.Bookings.Booking.RoomStay.StayDate;
import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRSDocument.BookingRetrievalRS.Bookings.Booking.RoomStay.Total;
import com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRSDocument.BookingRetrievalRS.Bookings.Booking.RoomStay.PerDayRates.PerDayRate;
import com.thayer.idsservice.bean.mapping.MappingEnum;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.ids.expedia.beans.ExpediaResv;
import com.thayer.idsservice.task.download.bean.ErrorBean;
import com.thayer.idsservice.task.download.bean.HotelBean;
import com.thayer.idsservice.task.download.bean.ResultBean;
import com.thayer.idsservice.task.download.bean.Serviceprices;
import com.thayer.idsservice.task.download.bean.TResvBase;
import com.thayer.idsservice.task.download.bean.TResvRate;
import com.thayer.idsservice.task.download.interf.AbstractDownLoadService;
import com.thayer.idsservice.util.DateUtil;

public class ExpediaDownloadService extends AbstractDownLoadService {

    /**
	 * 
	 */
    private static final long serialVersionUID = 23481870076351524L;

    private static Logger LOGGER = Logger.getLogger(ExpediaDownloadService.class);

    private transient VelocityEngine velocityEngine;

    @Override
    public String getBookingListRequestXml(HotelBean hotelBean) throws BizException {

        String thirdHotelCode = hotelBean.getThirdHotelCode();
        if (!StringUtils.hasText(thirdHotelCode)) {
            throw new BizException("hoteCode is null!");
        }

        BookingRetrievalRQDocument brRqDoc = BookingRetrievalRQDocument.Factory.newInstance();
        BookingRetrievalRQ addNewBookingRetrievalRQ = brRqDoc.addNewBookingRetrievalRQ();

        // Authentication
        Authentication autDoc = addNewBookingRetrievalRQ.addNewAuthentication();

        autDoc.setUsername(hotelBean.getUsername());
        autDoc.setPassword(hotelBean.getPassword());

        // Hotel
        Hotel addNewHotel = addNewBookingRetrievalRQ.addNewHotel();
        addNewHotel.setId(new BigInteger(thirdHotelCode));

        XmlOptions opt = new XmlOptions();
        opt.setUseDefaultNamespace();
        opt.setSavePrettyPrint();

        brRqDoc.xmlText(opt);
        LOGGER.info("start getBookingListRequestXml : \n" + "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> "
                + brRqDoc.toString());
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> " + brRqDoc.toString();
    }

    @Override
    public String[] handleResponseXml4detailXml(HotelBean hotelBean, String bookingListResponseXml) throws BizException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TResvBase handleResponseXml4fogbean(HotelBean hotelBean, String bookingResponseXml) throws BizException,
            MappingException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TResvBase> handleResponseXml4fogbeanList(HotelBean hotelBean, String bookingListResponseXml,
            ResultBean resultBean) throws BizException {
        LOGGER.info(" bookingListResponseXml : \n" + bookingListResponseXml);
        List<TResvBase> retunList = new ArrayList<TResvBase>();
        List<ErrorBean> errorList = resultBean.getErrorlist();
        try {
            String title = "";
            // String xmlns = "xmlns=\"http://www.expediaconnect.com/EQC/BR/2007/02\"";

            // String xmlStr = bookingListResponseXml.replaceAll(xmlns, "");
            String xmlStr = bookingListResponseXml;
            xmlStr = xmlStr.replaceAll("&", "");
            BookingRetrievalRSDocument rsDoc = BookingRetrievalRSDocument.Factory.parse(xmlStr);
            BookingRetrievalRS rsDetial = rsDoc.getBookingRetrievalRS();
            com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRSDocument.BookingRetrievalRS.Error[] erros = rsDetial
                    .getErrorArray();
            if (erros != null && erros.length > 0) {
                String errorStr = "";
                for (int i = 0; i < erros.length; i++) {
                    com.expediaconnect.eqc.br.x2007.x02.BookingRetrievalRSDocument.BookingRetrievalRS.Error erroDetail = erros[i];
                    LOGGER.error("BR expedia response error code:" + erroDetail.getCode() + "->"
                            + erroDetail.getStringValue());
                    title = "BR expedia response error code:" + erroDetail.getCode() + "->"
                            + erroDetail.getStringValue();
                    errorStr += " BR expedia response error code:" + erroDetail.getCode() + "->"
                            + erroDetail.getStringValue() + " \n";
                }

                throw new BizException(errorStr);
            }

            Bookings bookings = rsDetial.getBookings();
            if (bookings != null) {

                Booking[] bookDtails = bookings.getBookingArray();

                for (int i = 0; bookDtails != null && i < bookDtails.length; i++) {

                    Booking booking = bookDtails[i];

                    try {
                        TResvBase parseResponse = parseResponse(hotelBean, booking);
                        if (parseResponse != null) {
                            retunList.add(parseResponse);
                        }
                    } catch (Exception e) {
                        ErrorBean errorBean = new ErrorBean();
                        errorBean.setErroCode("IDS-0004");
                        errorBean.setErrorDesc(getMapService().getAlertDesc("IDS-0004"));
                        errorBean.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
                        errorBean.setXml(formatResvInfo(hotelBean, booking));
                        errorBean.setNeedFormat(false);
                        errorBean.setIdsProp(booking.getHotel().getId().toString());
                        errorBean.setIds_cnfnum(booking.getId().toString());
                        errorList.add(errorBean);
                        LOGGER.error(ExceptionUtils.getFullStackTrace(e));
                        continue;
                    }
                }
            }
        } catch (XmlException e) {
            LOGGER.error("parse xml error:" + ExceptionUtils.getFullStackTrace(e));
            throw new BizException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("parse xml error:" + ExceptionUtils.getFullStackTrace(e));
            throw new BizException(e.getMessage());
        }
        return retunList;
    }

    public TResvBase parseResponse(HotelBean hotelBean, Booking booking) throws Exception {
        TResvBase tresvbase = new TResvBase();
        String bookTypeName = "";
        try {
            @SuppressWarnings("unused")
            String title = "";
            String fogPropId = "";
            String bookType = booking.getType() == null ? "" : String.valueOf(booking.getType().toString());
            String fogHotelCode = hotelBean.getFogHotelCode();

            /*
             * EXPEDIA下单失败都是自动调用特殊单接口，
             * 下EBAR的Rate Code（包括EXPEDIA的促销），
             * 但我们和华亭宾馆的PMS直连是Rate Code一对一的对应关系。
             * 所以就做了一个特殊规则，1052所有订单都处理为下载失败，由人工跟进。
             */
            String[] props = { "1052" };
            for (String p : props) {
                if (p.equals(fogHotelCode)) {
                    bookType = "";
                }
            }

            if ("Book".equals(bookType)) {
                bookTypeName = "Expedia\u65b0\u5355";
                tresvbase.setResvtype("n");
            } else if ("Modify".equals(bookType)) {
                bookTypeName = "Expedia\u4fee\u6539\u5355";
                tresvbase.setResvtype("e");
            } else if ("Cancel".equals(bookType)) {
                bookTypeName = "Expedia\u53d6\u6d88\u5355";
                tresvbase.setResvtype("c");
            }

            String expBookId = booking.getId().toString();
            // expedia�
            tresvbase.setOutcnfnum(expBookId);
            // String createDate = booking.getCreateDateTime();

            @SuppressWarnings("unused")
            String fogPropName = "";
            String fogRoomType = "";
            String fogPlanId = "";

            tresvbase.setProp(fogHotelCode);

            RoomStay roomStay = booking.getRoomStay();

            String disCountStr = "";
            if (roomStay != null) {
                // /////type
                String roomType = String.valueOf(roomStay.getRoomTypeID());
                String ratePlan = String.valueOf(roomStay.getRatePlanID());
                String fogroomtype = getMapService().getRoomRateMap(roomType, fogHotelCode, MappingEnum.EXPEDIA,
                        "roomType");

                tresvbase.setRoomcode(fogroomtype);
                String fograteClass = "";

                fograteClass = getMapService().getRoomRateMap(roomType + "#" + ratePlan, fogHotelCode,
                        MappingEnum.EXPEDIA, "rateType");

                tresvbase.setRateClass(fograteClass);
                
                // Set ratePlanID
                tresvbase.setPlancode(ratePlan);
                
                StayDate stayDate = roomStay.getStayDate();
                Date arrvDate = stayDate.getArrival().getTime();
                Date deparDate = stayDate.getDeparture().getTime();
                PaymentCard payment= roomStay.getPaymentCard();
                if(payment != null){
                    tresvbase.setCctype(payment.getCardCode());
                    tresvbase.setCcnumber(payment.getCardNumber());
                    tresvbase.setCcexpiration(payment.getExpireDate());
                    if(payment.getCardHolder() != null){
                        tresvbase.setCcname(payment.getCardHolder().getName());
                    }
                }
                // expediaResv.setInDate(ToolUtil.formatDateYyyyMmDd(arrvDate));
                // expediaResv.setOutDate(ToolUtil.formatDateYyyyMmDd(deparDate));
                tresvbase.setIndate(arrvDate);
                tresvbase.setOutdate(deparDate);
                tresvbase.setNights(DateUtil.dateDiff(DateUtil.TIME_UNIT_D, arrvDate, deparDate));

                GuestCount guestCountDoc = roomStay.getGuestCount();
                if (guestCountDoc != null) {
                    tresvbase.setAdults(guestCountDoc.getAdult());
                    tresvbase.setChildren(guestCountDoc.getChild2());
                }
                tresvbase.setRooms(new Integer("1"));

                String bookRateStr = "";
                float extFee = 0.00f;
                float serFee = 0.00f;
                PerDayRates perDayRates = roomStay.getPerDayRates();
                List<TResvRate> resvRateList = new ArrayList<TResvRate>();
                tresvbase.setResvRateList(resvRateList);
                if (perDayRates != null) {
                    PerDayRate[] perDayRate = perDayRates.getPerDayRateArray();
                    if (perDayRate != null) {
                        for (PerDayRate perRate : perDayRate) {
                            if (disCountStr == null || disCountStr.equals("")) {
                                disCountStr = perRate.getPromoName();
                            }

                            float totExteR = 0.00f;
                            BigDecimal extraPersonFees = perRate.getExtraPersonFees();
                            if (extraPersonFees != null) {
                                totExteR += extraPersonFees.floatValue();
                                extFee = extFee + extraPersonFees.floatValue();
                            }
                            BigDecimal hotelServiceFees = perRate.getHotelServiceFees();
                            if (hotelServiceFees != null) {
                                totExteR += hotelServiceFees.floatValue();
                                serFee = serFee + hotelServiceFees.floatValue();
                            }

                            float totalR = perRate.getBaseRate().floatValue();// + totExteR;

                            if ("".equals(bookRateStr)) {

                                bookRateStr = (perRate.getStayDate() == null ? "" : perRate.getStayDate()) + "="
                                        + totalR + "+" + totExteR;

                            } else {
                                bookRateStr += "#" + (perRate.getStayDate() == null ? "" : perRate.getStayDate()) + "="
                                        + totalR + "+" + totExteR;

                            }
                            TResvRate tResvRate = new TResvRate();
                            tResvRate.setBookendate(perRate.getStayDate().getTime());
                            tResvRate.setBookendatestr(DateUtil.formatDate(perRate.getStayDate().getTime()));
                            // TODO 货币代码
                            tResvRate.setProprevenue(Double.valueOf(totalR));
                            tResvRate.setExtrate(Double.valueOf(totExteR));

                            resvRateList.add(tResvRate);

                        }
                    }
                }

                if (serFee > 0) {
                    /**
                     * serv.setCount(serFee);<br>
                     *serv.setFreq("RS");<br>
                     *serv.setInclude("1");<br>
                     *serv.setName("Service Fee");<br>
                     *serv.setType("S");<br>
                     *serv.setUnit("M");<br>
                     */
                    tresvbase.setServiceprice(StringUtils.hasText(tresvbase.getServiceprice()) ? tresvbase
                            .getServiceprice()
                            + "|" + "S=" + serFee : "S=" + serFee);
                    List<Serviceprices> serviceprices = tresvbase.getServiceprices();
                    Serviceprices serv = new Serviceprices();
                    serv.setCount(String.valueOf(serFee));
                    serv.setFreq("RS");
                    serv.setInclude("1");
                    serv.setName("Service Fee");
                    serv.setType("S");
                    serv.setUnit("M");
                    serviceprices.add(serv);
                }

                tresvbase.setBookeddate(new Date());

                tresvbase.setRemark("");
                // remark information
                ArrayList<String> remarkLst = new ArrayList<String>();
                Total totalDoc = roomStay.getTotal();

                SpecialRequest[] specRequest = booking.getSpecialRequestArray();
                int index = 0;
                String allRemarks = "";
                String ietmRemark = "";
                // discount
                if (disCountStr != null && !disCountStr.equals("")) {
                    index++;
                    ietmRemark = index + ".\u4f18\u60e0 Discount:" + disCountStr + " ";

                    tresvbase.setRemark(tresvbase.getRemark() + ietmRemark);
                }
                if (specRequest != null && specRequest.length > 0) {
                    index++;
                    ietmRemark = index + ".\u7279\u6b8a\u8981\u6c42Special Request:";
                    for (int s = 0; s < specRequest.length; s++) {
                        SpecialRequest sp = specRequest[s];
                        if (s > 0) {
                            ietmRemark = ietmRemark + "," + sp.getStringValue();
                        } else {
                            ietmRemark = ietmRemark + sp.getStringValue();
                        }
                    }
                    tresvbase.setRemark(tresvbase.getRemark() + ietmRemark);
                }
                index++;
                ietmRemark = index + ".Expedia Booking ID:" + expBookId + ",";
                tresvbase.setRemark(tresvbase.getRemark() + ietmRemark);
                if (extFee > 0) {
                    index++;
                    ietmRemark = index + ".Extra person:" + extFee + ",";// \u52a0\u5e8a\u8d39
                    tresvbase.setRemark(tresvbase.getRemark() + ietmRemark);
                }
                if (serFee > 0) {
                    index++;
                    ietmRemark = index + ".Extra charges:" + serFee + ",";// \u670d\u52a1\u8d39
                    tresvbase.setRemark(tresvbase.getRemark() + ietmRemark);
                }
                index++;
                ietmRemark = index + ".\u5B50\u6E20\u9053\u6765\u6E90：" + booking.getSource() + ",";
                tresvbase.setRemark(tresvbase.getRemark() + ietmRemark);
                
                index++;
                ietmRemark = index + "." + "Amount of taxes:"
                        + (totalDoc.getAmountOfTaxes() == null ? "" : totalDoc.getAmountOfTaxes())
                        + "\u623f\u8d39\u603b\u6570 (Total amount after taxes:" + totalDoc.getAmountAfterTaxes() + ")";
                tresvbase.setRemark(tresvbase.getRemark() + ietmRemark);
                RewardProgram[] rewardDetail = booking.getRewardProgramArray();
                if (rewardDetail != null && rewardDetail.length > 0) {
                    index++;
                    ietmRemark = index + ".\u4fc3\u9500 Reward Program:";
                    for (int r = 0; r < rewardDetail.length; r++) {
                        RewardProgram rd = rewardDetail[r];
                        ietmRemark += rd.getCode() + "(CODE)-" + rd.getNumber() + "(Number)";
                    }
                    tresvbase.setRemark(tresvbase.getRemark() + ietmRemark);
                }

                // remark�
                index++;
                ietmRemark = index
                        + ".\u623f\u8d39\u548c\u670d\u52a1\u8d39\u5df2\u9884\u4ed8\uff0c\u8bf7\u52ff\u5411\u5ba2\u4eba\u6536\u53d6\uff0c\u5176\u4ed6\u8d39\u7528\u5ba2\u4eba\u81ea\u7406\u3002\u4efb\u4f55\u60c5\u51b5\u4e0b\u90fd\u4e0d\u80fd\u5411\u5ba2\u4eba\u900f\u6f0f\u623f\u4ef7\u4fe1\u606f.";
                tresvbase.setRemark(tresvbase.getRemark() + ietmRemark);
                // total
                if (totalDoc != null) {
                    tresvbase.setTotalrevenue(totalDoc.getAmountAfterTaxes().doubleValue());
                    tresvbase.setServiceprice(StringUtils.hasText(tresvbase.getServiceprice()) ? tresvbase
                            .getServiceprice()
                            + "|" + "T=" + totalDoc.getAmountOfTaxes() : "T=" + totalDoc.getAmountOfTaxes());
//                    List<Serviceprices> serviceprices = tresvbase.getServiceprices();
//                    Serviceprices serv = new Serviceprices();
//                    serv.setCount(String.valueOf(totalDoc.getAmountOfTaxes()));
////                    serv.setFreq("RS");
////                    serv.setInclude("0");
////                    serv.setName("Tax");
////                    serv.setType("T");
////                    serv.setUnit("M");
//                    
//                    serv.setFreq("RN");
//                    serv.setInclude("0");
//                    serv.setName("服务费");
//                    serv.setType("S");
//                    serv.setUnit("P");
//                    serviceprices.add(serv);
                }

            }

            // Miscinfo
            tresvbase.setIata(hotelBean.getFogIataCode());
            // expediaResv.setIataName("Expedia/Hotel.com");
            // expediaResv.setIataPhone("001-702-9392667");
            PrimaryGuest primaryGuest = booking.getPrimaryGuest();
            // Guestinfo
            if (primaryGuest != null) {
                Name guestNameDoc = primaryGuest.getName();
                if (guestNameDoc != null) {
                    // ri.setGuest_name(guestNameDoc.getGivenName() + " "+
                    // guestNameDoc.getSurname());
                    tresvbase.setGfirstname(guestNameDoc.getGivenName());
                    tresvbase.setGlastname(guestNameDoc.getSurname());
                    
                    //客人姓名的处理
        			StringBuffer guestName = new StringBuffer();
        			guestName.append(guestNameDoc.getGivenName()).append(" ").append(guestNameDoc.getSurname());
        			tresvbase.setGuestname(guestName.toString());
        			List<String> guestList = new ArrayList<String>();
        			guestList.add(guestName.toString());
        			tresvbase.setGuestList(guestList);
                    
                }
                Phone phoneDoc = primaryGuest.getPhone();
                if (phoneDoc != null) {
                    tresvbase.setGuestaddress("");

                    tresvbase.setGuestphone(phoneDoc.getNumber());
                    tresvbase.setGuestcity(String.valueOf(phoneDoc.getCityAreaCode()));
                    tresvbase.setGuestcountry(String.valueOf(phoneDoc.getCountryCode()));

                }
            }
            return tresvbase;
        } catch (Exception e) {
            LOGGER.error("parseResponse error---------------------->" + ExceptionUtils.getFullStackTrace(e));
            throw e;
        }
    }

    protected ExpediaResv getExpediaResv(HotelBean hotelBean, Booking booking) throws Exception {
        String expBookId = booking.getId().toString();
        ExpediaResv expediaResv = new ExpediaResv();
        // expedia�
        expediaResv.setCnfNum(expBookId);
        // String createDate = booking.getCreateDateTime();

        String propId = booking.getHotel().getId().toString();

        String fogPropId = hotelBean.getFogHotelCode();
        String fogPropName = "";
        String fogRoomType = "";
        String fogPlanId = "";

        expediaResv.setPropId(fogPropId);
        expediaResv.setPropName(fogPropName);
        expediaResv.setGds("Website");
        RoomStay roomStay = booking.getRoomStay();

        String disCountStr = "";
        if (roomStay != null) {
            String roomType = roomStay.getRoomTypeID().toString();
            String ratePlan = roomStay.getRatePlanID().toString();
            fogRoomType = getMapService().getRoomRateMap(booking.getRoomStay().getRoomTypeID().toString(),
                    hotelBean.getFogHotelCode(), MappingEnum.EXPEDIA, "roomType");
            if (!StringUtils.hasText(fogRoomType)) {
                fogRoomType = "expedia:" + roomType;
            }

            fogPlanId = getMapService().getRoomRateMap(roomType + "#" + ratePlan, hotelBean.getFogHotelCode(),
                    MappingEnum.EXPEDIA, "rateType");
            if (!StringUtils.hasText(fogPlanId)) {
                fogPlanId = "expedia:" + ratePlan;
            }

            expediaResv.setRoomtype(fogRoomType);
            expediaResv.setPaln(fogPlanId);
            StayDate stayDate = roomStay.getStayDate();
            Date arrvDate = stayDate.getArrival().getTime();
            Date deparDate = stayDate.getDeparture().getTime();

            expediaResv.setInDate(DateUtil.formatDate(arrvDate));
            expediaResv.setOutDate(DateUtil.formatDate(deparDate));

            expediaResv.setNights(DateUtil.dateDiff(DateUtil.TIME_UNIT_D, arrvDate, deparDate) + "");
            GuestCount guestCountDoc = roomStay.getGuestCount();
            if (guestCountDoc != null) {
                expediaResv.setAdults(String.valueOf(guestCountDoc.getAdult()));
                expediaResv.setChildren(String.valueOf(guestCountDoc.getChild2()));
            }
            expediaResv.setRooms("1");

            String bookRateStr = "";
            float extFee = 0.00f;
            float serFee = 0.00f;
            PerDayRates perDayRates = roomStay.getPerDayRates();
            if (perDayRates != null) {
                PerDayRate[] perDayRate = perDayRates.getPerDayRateArray();
                if (perDayRate != null) {
                    for (int p = 0; p < perDayRate.length; p++) {
                        PerDayRate perRate = perDayRate[p];
                        if (disCountStr == null || disCountStr.equals("")) {
                            disCountStr = perRate.getPromoName();
                        }

                        float totExteR = 0.00f;
                        BigDecimal extraPersonFees = perRate.getExtraPersonFees();
                        if (extraPersonFees != null) {
                            totExteR += extraPersonFees.floatValue();
                            extFee = extFee + extraPersonFees.floatValue();
                        }
                        BigDecimal hotelServiceFees = perRate.getHotelServiceFees();
                        if (hotelServiceFees != null) {
                            totExteR += hotelServiceFees.floatValue();
                            serFee = serFee + hotelServiceFees.floatValue();
                        }

                        float totalR = perRate.getBaseRate().floatValue();// + totExteR;

                        if ("".equals(bookRateStr)) {

                            bookRateStr = (perRate.getStayDate() == null ? "" : perRate.getStayDate()) + "=" + totalR
                                    + "+" + totExteR;

                        } else {
                            bookRateStr += "#" + (perRate.getStayDate() == null ? "" : perRate.getStayDate()) + "="
                                    + totalR + "+" + totExteR;

                        }
                    }
                }
            }

            if (serFee > 0) {
                expediaResv.setAmountSerFee(serFee + "");
            }

            expediaResv.setDateBooked(DateUtil.formatDateTime(new Date()));
            expediaResv.setBookedRates(bookRateStr);
            // remark information
            ArrayList<String> remarkLst = new ArrayList<String>();
            Total totalDoc = roomStay.getTotal();

            SpecialRequest[] specRequest = booking.getSpecialRequestArray();
            int index = 0;
            String allRemarks = "";
            String ietmRemark = "";
            // discount
            if (disCountStr != null && !disCountStr.equals("")) {
                index++;
                ietmRemark = index + ".\u4f18\u60e0 Discount:" + disCountStr + " ";
                remarkLst.add(ietmRemark);
                allRemarks += ietmRemark;
            }
            if (specRequest != null && specRequest.length > 0) {
                index++;
                ietmRemark = index + ".\u7279\u6b8a\u8981\u6c42Special Request:";
                for (int s = 0; s < specRequest.length; s++) {
                    SpecialRequest sp = (SpecialRequest) specRequest[s];
                    if (s > 0) {
                        ietmRemark = ietmRemark + "," + sp.getStringValue();
                    } else {
                        ietmRemark = ietmRemark + sp.getStringValue();
                    }
                }
                remarkLst.add(ietmRemark);
                allRemarks += ietmRemark;
            }
            index++;
            ietmRemark = index + ".Expedia Booking ID:" + expBookId + ",";
            remarkLst.add(ietmRemark);
            allRemarks += ietmRemark;
            if (extFee > 0) {
                index++;
                ietmRemark = index + ".Extra person:" + extFee + ",";// \u52a0\u5e8a\u8d39
                remarkLst.add(ietmRemark);
                allRemarks += ietmRemark;
            }
            if (serFee > 0) {
                index++;
                ietmRemark = index + ".Extra charges:" + serFee + ",";// \u670d\u52a1\u8d39
                remarkLst.add(ietmRemark);
                allRemarks += ietmRemark;
            }
            index++;
            ietmRemark = index + ".\u5B50\u6E20\u9053\u6765\u6E90：" + booking.getSource() + ",";
            remarkLst.add(ietmRemark);
            allRemarks += ietmRemark;
            index++;
            ietmRemark = index + "." + "Amount of taxes:"
                    + (totalDoc.getAmountOfTaxes() == null ? "" : totalDoc.getAmountOfTaxes())
                    + "\u623f\u8d39\u603b\u6570 (Total amount after taxes:" + totalDoc.getAmountAfterTaxes() + ")";
            remarkLst.add(ietmRemark);
            allRemarks += ietmRemark;
            RewardProgram[] rewardDetail = booking.getRewardProgramArray();
            if (rewardDetail != null && rewardDetail.length > 0) {
                index++;
                ietmRemark = index + ".\u4fc3\u9500 Reward Program:";
                for (int r = 0; r < rewardDetail.length; r++) {
                    RewardProgram rd = (RewardProgram) rewardDetail[r];
                    ietmRemark += rd.getCode() + "(CODE)-" + rd.getNumber() + "(Number)";
                }
                remarkLst.add(ietmRemark);
                allRemarks += ietmRemark;
            }

            // remark�
            index++;
            ietmRemark = index
                    + ".\u623f\u8d39\u548c\u670d\u52a1\u8d39\u5df2\u9884\u4ed8\uff0c\u8bf7\u52ff\u5411\u5ba2\u4eba\u6536\u53d6\uff0c\u5176\u4ed6\u8d39\u7528\u5ba2\u4eba\u81ea\u7406\u3002\u4efb\u4f55\u60c5\u51b5\u4e0b\u90fd\u4e0d\u80fd\u5411\u5ba2\u4eba\u900f\u6f0f\u623f\u4ef7\u4fe1\u606f.";
            remarkLst.add(ietmRemark);
            allRemarks += ietmRemark;
            expediaResv.setRemarkLst(remarkLst);
            expediaResv.setRemarks(allRemarks);
            // total
            if (totalDoc != null) {
                expediaResv.setTotalRevenue(totalDoc.getAmountAfterTaxes().toString());
                expediaResv.setAmountTax(totalDoc.getAmountOfTaxes().toString());

            }

        }

        // Miscinfo
        expediaResv.setIata(hotelBean.getThirdIataCode());
        expediaResv.setIataName("Expedia/Hotel.com");
        expediaResv.setIataPhone("001-702-9392667");
        PrimaryGuest primaryGuest = booking.getPrimaryGuest();
        // Guestinfo
        if (primaryGuest != null) {
            Name guestNameDoc = primaryGuest.getName();
            if (guestNameDoc != null) {
                // ri.setGuest_name(guestNameDoc.getGivenName() + " "+
                // guestNameDoc.getSurname());
                expediaResv.setFirstName(guestNameDoc.getGivenName());
                expediaResv.setLastName(guestNameDoc.getSurname());
            }
            Phone phoneDoc = primaryGuest.getPhone();
            if (phoneDoc != null) {
                expediaResv.setGuestAddressLine1("");
                expediaResv.setGuestCity(String.valueOf(phoneDoc.getCityAreaCode()));
                expediaResv.setGuestEmail("");
                expediaResv.setGuestPhone(phoneDoc.getNumber());
                expediaResv.setGuestCountry(String.valueOf(phoneDoc.getCountryCode()));
            }
        }
        // Ccinfo
        expediaResv.setCcType("Pre-Paid");
        expediaResv.setCcExpiration("");
        expediaResv.setCcName("");
        expediaResv.setCcNumber("");
        return expediaResv;

    }

    protected String formatResvInfo(HotelBean hotelBean, Booking booking) {

        String out = null;
        try {
            ExpediaResv expediaResv = getExpediaResv(hotelBean, booking);
            String vm = "expediaFormat.vm";
            Map model = new HashMap();
            model.put("ObjectTO", expediaResv);
            out = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, vm, model);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getFullStackTrace(e));
            out = booking.toString();
        }
        return out;
    }

    /**
     * @return the velocityEngine
     */
    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    /**
     * @param velocityEngine the velocityEngine to set
     */
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public static void main(String[] args) {
        // ExpediaDownloadService expedia = new ExpediaDownloadService();
        // String bookingListResponseXml="";
        // HotelBean hotelBean=new HotelBean();
        // ResultBean resultBean=new ResultBean();
        //		
        // bookingListResponseXml+="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        // bookingListResponseXml+="<BookingRetrievalRS xmlns=\"http://www.expediaconnect.com/EQC/BR/2007/02\">";
        // bookingListResponseXml+="<Bookings><Booking id=\"234496901\" type=\"Book\" createDateTime=\"2011-01-17T13:05:00Z\" source=\"Hotels.com\">";
        // bookingListResponseXml+="<Hotel id=\"1430197\"/><RoomStay roomTypeID=\"329863\" ratePlanID=\"1017434\">";
        // bookingListResponseXml+="<StayDate arrival=\"2011-01-24\" departure=\"2011-01-26\"/>";
        // bookingListResponseXml+="<GuestCount adult=\"1\"/><PerDayRates currency=\"CNY\">";
        // bookingListResponseXml+="<PerDayRate stayDate=\"2011-01-24\" baseRate=\"312.00\" promoName=\"Stay min 2 nights, save 20 \"/>";
        // bookingListResponseXml+="<PerDayRate stayDate=\"2011-01-25\" baseRate=\"312.00\" promoName=\"Stay min 2 nights, save 20 \"/>";
        // bookingListResponseXml+="</PerDayRates><Total amountAfterTaxes=\"717.60\" amountOfTaxes=\"93.60\" currency=\"CNY\"/>";
        // bookingListResponseXml+="</RoomStay><PrimaryGuest><Name givenName=\"Shao Feng\" surname=\"Li\"/>";
        // bookingListResponseXml+="</PrimaryGuest><SpecialRequest code=\"1.15\">1 queen</SpecialRequest>";
        // bookingListResponseXml+="<SpecialRequest code=\"2.1\">Non-Smoking</SpecialRequest>";
        // bookingListResponseXml+="</Booking></Bookings></BookingRetrievalRS>";
        //
        //		                    
        //
        //		
        // try {
        // expedia.handleResponseXml4fogbeanList(hotelBean, bookingListResponseXml, resultBean);
        // } catch (BizException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }
}
