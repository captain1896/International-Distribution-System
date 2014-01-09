package com.thayer.idsservice.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.hubs1.gxml.ccnewresv.BookedrateDocument.Bookedrate;
import net.hubs1.gxml.ccnewresv.BookedratesDocument.Bookedrates;
import net.hubs1.gxml.ccnewresv.CcinfoDocument.Ccinfo;
import net.hubs1.gxml.ccnewresv.CrsmessageDocument;
import net.hubs1.gxml.ccnewresv.CrsmessageDocument.Crsmessage;
import net.hubs1.gxml.ccnewresv.GuestinfoDocument.Guestinfo;
import net.hubs1.gxml.ccnewresv.MiscinfoDocument.Miscinfo;
import net.hubs1.gxml.ccnewresv.RemarksDocument.Remarks;
import net.hubs1.gxml.ccnewresv.ReservationDocument.Reservation;
import net.hubs1.gxml.ccnewresv.ServicepriceDocument.Serviceprice;
import net.hubs1.gxml.ccnewresv.ServicepricesDocument.Serviceprices;
import net.hubs1.gxml.ccnewresv.StaydetailDocument.Staydetail;
import net.hubs1.gxml.getexratemapRS.RatedataDocument.Ratedata;
import net.hubs1.gxml.getexratemapRS.RatedetailDocument.Ratedetail;
import net.hubs1.gxml.getexratemapRS.RatemapDocument.Ratemap;
import net.hubs1.gxml.getexratemapRS.SrvmessageDocument.Srvmessage;
import net.hubs1.gxml.newresvRS.ResvdetailDocument.Resvdetail;
import net.hubs1.gxml.newresvRS.ResvdetailDocument.Resvdetail.Guarruledetail;
import net.hubs1.gxml.newresvRS.SrvmessageDocument;
import net.hubs1.utils.LogUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlString;
import org.springframework.util.StringUtils;

import com.thayer.fog2.bo.RateDataBO;
import com.thayer.fog2.entity.AvailAllow;
import com.thayer.fog2.vo.RateMQVO;
import com.thayer.fogservice.webservice.crs.AvailExchangeRateWebService;
import com.thayer.fogservice.webservice.crs.AvailWebService;
import com.thayer.idsservice.bean.MailFogResvBaseBean;
import com.thayer.idsservice.bean.PriceValidResult;
import com.thayer.idsservice.dao.ERatemap;
import com.thayer.idsservice.dao.ERatemapDAO;
import com.thayer.idsservice.dao.EResvMap;
import com.thayer.idsservice.dao.EResvMapDAO;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.TimeOut4FogException;
import com.thayer.idsservice.exception.TimeOutException;
import com.thayer.idsservice.service.interf.ICallFogService;
import com.thayer.idsservice.service.interf.IHttpClientService;
import com.thayer.idsservice.task.download.bean.TResvBase;
import com.thayer.idsservice.task.download.bean.TResvRate;
import com.thayer.idsservice.task.download.interf.AbstractDownLoadService;
import com.thayer.idsservice.task.update.bean.AvailBean;
import com.thayer.idsservice.task.update.bean.FogMQBean;
import com.thayer.idsservice.util.Constents;
import com.thayer.idsservice.util.DateUtil;

/**
 * <B>Function :</B>调用FOG服务类 <br>
 * <B>General Usage :</B><br>
 * 必须设置参数:<br>
 * fogExUrl ： FOG EX服务地址<br>
 * fogUser ： FOG EX服务用户名<br>
 * fogPassword : FOG EX服务密码<br>
 * <B>Special Usage :</B> <br>
 * 参数 needTrySpecResv 决定是否使用特殊单接口，默认为false<br>
 * 参数 channelCode 为渠道代码，默认为 "Website"。对于GDS服务，一般设置为"!DEFAULT!"
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-3-17<br>
 * @version : v1.0
 */
public class SimpleCallFogService implements ICallFogService {

    /**
	 * 
	 */
    private static final String CXLSPECRESV = "cxlspecresv";

    /**
	 * 
	 */
    private static final String MODSPECRESV = "modspecresv";

    /**
	 * 
	 */
    private static final String NEWSPECRESV = "newspecresv";//特殊单
    
    /**
     * BOOKINGCOM渠道上的IATA号,IATA可以理解为渠道编号，是个字面量为数值的字符串。
     * EXPEDIA渠道上的IATA号,IATA可以理解为渠道编号，是个字面量为数值的字符串。
     */
    private static final String BOOKINGCOM_IATA = "970003";
    private static final String EXPEDIA_IATA = "960002";
    
    /**
     * 存入FOG，满房情况下的状态码
     */
    private static final String BOOKINGCOM_FULLROOM_CODE = "FORCE_FULL_ROOM";
    
    private static final String FOG_USER_BOOKING = "BOOKINGXML";

    protected static Log log = LogFactory.getLog(SimpleCallFogService.class);

    /**
	 * 
	 */
    private static final long serialVersionUID = -4457880801987977391L;
    private String newResvMsgtype = "ccnewresv";//IDS普通单
    private String cancelResvMsgtype = "cccxlresv";//IDS普通取消单

    /**
     * 渠道名,目前genares的计划级别用的是!DEFAULT!,其他的ids接口都用Website
     */
    private String channelCode = "Website";

    private EResvMapDAO eresvMapDAO;

    private String fogExUrl;

    private String fogLanguage;

    private String fogPassword;

    private transient AvailWebService fogServiceAvailWSClient;

    private transient AvailExchangeRateWebService fogServiceWSClient;

    private String fogUser;

    private IHttpClientService httpClientService;

    private String modResvMsgtype = "ccmodresv";
    
    private ERatemapDAO eratemapDAO;

    /**
     * 如果cc接口不能成功，是否使用特殊单接口下单。
     */
    private boolean needTrySpecResv = false;

   

    /**
     * 组装取消单的xml
     * 
     * @param tresvbase
     * @return
     * @throws BizException
     */
    private String buildCancelresv(TResvBase tresvbase) throws BizException {

        net.hubs1.gxml.cxlresv.CrsmessageDocument crsmessagedocument = net.hubs1.gxml.cxlresv.CrsmessageDocument.Factory
                .newInstance();
        /**
         * Node :Crsmessage
         */
        net.hubs1.gxml.cxlresv.CrsmessageDocument.Crsmessage addNewCrsmessage = crsmessagedocument.addNewCrsmessage();
        addNewCrsmessage.setUser(fogUser);
        addNewCrsmessage.setPass(fogPassword);
        addNewCrsmessage.setMsgtype(cancelResvMsgtype);
        if(tresvbase.getOrderMsgType() !=null && tresvbase.getOrderMsgType().endsWith("specresv")){//如果如果新单 是特殊单的话，就使用特殊单下修改单
        	addNewCrsmessage.setMsgtype(CXLSPECRESV);
        }
        
        addNewCrsmessage.setVersion("1.2");
        addNewCrsmessage.setPropID(tresvbase.getProp());

        /**
         * Node :Crsmessage/Reservation
         */
        net.hubs1.gxml.cxlresv.ReservationDocument.Reservation addNewReservation = addNewCrsmessage.addNewReservation();
        String outcnfnum = tresvbase.getOutcnfnum();

        List resvMapBean = eresvMapDAO.findByAgodaCnfnum(outcnfnum);
        if (resvMapBean.size() == 0) {
            throw new BizException("find no resvMap by outcnfnum:" + outcnfnum + " !!");
        }
        EResvMap resvBean = (EResvMap) resvMapBean.get(0);
        if (!StringUtils.hasText(resvBean.getFogCnfnum())) {
            throw new BizException("find no resvMap by outcnfnum:" + outcnfnum + " !!");
        }
        addNewReservation.setConfnum(resvBean.getFogCnfnum());

        String crsmessagexml = crsmessagedocument.toString();
        crsmessagexml = crsmessagexml.replaceAll("xmlns=\"http://www.hubs1.net/gxml/" + "cccxlresv" + "\"", "");
        crsmessagexml = crsmessagexml.replaceAll("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "");
        return crsmessagexml;
    }

    /**
     * 组装修改订单的xml
     * 
     * @param tresvbase
     * @return
     * @throws BizException
     */
    private String buildEditresv(TResvBase tresvbase) throws BizException {
        String confnum = "";

        net.hubs1.gxml.modresv.CrsmessageDocument crsmessagedocument = net.hubs1.gxml.modresv.CrsmessageDocument.Factory
                .newInstance();
        /**
         * Node :Crsmessage
         */
        net.hubs1.gxml.modresv.CrsmessageDocument.Crsmessage addNewCrsmessage = crsmessagedocument.addNewCrsmessage();
        addNewCrsmessage.setUser(fogUser);
        addNewCrsmessage.setPass(fogPassword);

        addNewCrsmessage.setMsgtype(modResvMsgtype);
        if(tresvbase.getOrderMsgType() !=null && tresvbase.getOrderMsgType().endsWith("specresv")){//如果如果新单 是特殊单的话，就使用特殊单下修改单
        	addNewCrsmessage.setMsgtype(MODSPECRESV);
        }
        
        addNewCrsmessage.setVersion("1.2");
        addNewCrsmessage.setPropID(tresvbase.getProp());

        /**
         * Node :Crsmessage/Reservation
         */
        net.hubs1.gxml.modresv.ReservationDocument.Reservation addNewReservation = addNewCrsmessage.addNewReservation();
        String outcnfnum = tresvbase.getOutcnfnum();
        List resvMapBean = eresvMapDAO.findByAgodaCnfnum(outcnfnum);
        if (resvMapBean.size() == 0) {
            throw new BizException("find no resvMap by outcnfnum:" + outcnfnum + " !!");
        }
        EResvMap resvBean = (EResvMap) resvMapBean.get(0);
        if (!StringUtils.hasText(resvBean.getFogCnfnum())) {
            throw new BizException("find no resvMap by outcnfnum:" + outcnfnum + " !!");
        }

        addNewReservation.setOutconfnum(outcnfnum);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        /**
         * Node :Crsmessage/Reservation/Staydetail
         */
        net.hubs1.gxml.modresv.StaydetailDocument.Staydetail addNewStaydetail = addNewReservation.addNewStaydetail();
        String checkIndate = simpleDateFormat2.format(tresvbase.getIndate());
        addNewStaydetail.setDate(checkIndate);
        String nights = String.valueOf(tresvbase.getNights());
        addNewStaydetail.setNights(nights);
        String rooms = String.valueOf(tresvbase.getRooms());
        addNewStaydetail.setRooms(rooms);
        int adult = 0;
        if (tresvbase.getRooms() != 0) {
            int num1 = tresvbase.getAdults() / tresvbase.getRooms();
            if (tresvbase.getAdults() % tresvbase.getRooms() == 0) {
                adult = num1;
            } else {
                adult = num1 + 1;
            }
        }
        addNewStaydetail.setAdults(String.valueOf(adult));

        addNewStaydetail.setChildren(String.valueOf(tresvbase.getChildren()));
        addNewStaydetail.setRoomtype(tresvbase.getRoomcode());
        addNewStaydetail.setRateclass(tresvbase.getRateClass());

        if (StringUtils.hasText(tresvbase.getChannel())) {
            addNewStaydetail.setChannel(tresvbase.getChannel());
        }
        /**
         * Node :Crsmessage/Reservation/Bookedrates
         */
        net.hubs1.gxml.modresv.BookedratesDocument.Bookedrates addNewBookedrates = addNewReservation
                .addNewBookedrates();
        net.hubs1.gxml.modresv.CcinfoDocument.Ccinfo addNewCcinfo = addNewReservation.addNewCcinfo();
        addNewCcinfo.setCctype(tresvbase.getCctype());
        addNewCcinfo.setCcname(tresvbase.getCcname());
        addNewCcinfo.setCcnum(tresvbase.getCcnumber());
        addNewCcinfo.setCcexp(tresvbase.getCcexpiration());

        /**
         * Node :Crsmessage/Reservation/Guestinfo
         */
        net.hubs1.gxml.modresv.GuestinfoDocument.Guestinfo addNewGuestinfo = addNewReservation.addNewGuestinfo();
//        if (StringUtils.hasText(tresvbase.getGfirstname())) {
//            addNewGuestinfo.setFirstname(tresvbase.getGfirstname());
//        }
//
//        if (StringUtils.hasText(tresvbase.getGlastname())) {
//            addNewGuestinfo.setLastname(tresvbase.getGlastname());
//        }
        List<String> guestList = tresvbase.getGuestList();
        if(CollectionUtils.isNotEmpty(guestList)){
        	addNewGuestinfo.setLastname(guestList.get(0));
        }

        if (StringUtils.hasText(tresvbase.getGuaCode()) || StringUtils.hasText(tresvbase.getGuadesc())) {
            net.hubs1.gxml.modresv.GuarruledetailDocument.Guarruledetail addNewGuarruledetail = addNewReservation
                    .addNewGuarruledetail();
            addNewGuarruledetail.setRule(tresvbase.getGuaCode());
            addNewGuarruledetail.setDescription(tresvbase.getGuadesc());
        }

        try {
            addNewGuestinfo.setPhone(String.valueOf(tresvbase.getGuestphone()));
        } catch (Exception e) {
            addNewGuestinfo.setPhone("02161226688*7800");
        }
        try {
            addNewGuestinfo.setEmail(String.valueOf(tresvbase.getGuestmail()));
        } catch (Exception e) {
            addNewGuestinfo.setEmail("service@hubs1.net");
        }

        /**
         * Node :Crsmessage/Reservation/Remarks
         */
        net.hubs1.gxml.modresv.RemarksDocument.Remarks addNewRemarks = addNewReservation.addNewRemarks();
        if (StringUtils.hasText(tresvbase.getRemark())) {
            XmlString addNewRemark = addNewRemarks.addNewRemark();
            addNewRemark.setStringValue(tresvbase.getRemark());
        }

        /**
         * Node :Crsmessage/Reservation/Serviceprices
         */
        List<com.thayer.idsservice.task.download.bean.Serviceprices> serviceprices = tresvbase.getServiceprices();

        if (serviceprices.size() > 0) {
            net.hubs1.gxml.modresv.ServicepricesDocument.Serviceprices addNewServiceprices = addNewReservation
                    .addNewServiceprices();
            for (com.thayer.idsservice.task.download.bean.Serviceprices serviceprice : serviceprices) {
                net.hubs1.gxml.modresv.ServicepriceDocument.Serviceprice serv = addNewServiceprices
                        .addNewServiceprice();
                serv.setCount(serviceprice.getCount());
                serv.setFreq(serviceprice.getFreq());
                serv.setInclude(serviceprice.getInclude());
                serv.setName(serviceprice.getName());
                serv.setType(serviceprice.getType());
                serv.setUnit(serviceprice.getUnit());
            }
        }

        /**
         * Node :Crsmessage/Reservation/Miscinfo
         */
        net.hubs1.gxml.modresv.MiscinfoDocument.Miscinfo addNewMiscinfo = addNewReservation.addNewMiscinfo();
        addNewMiscinfo.setIATA(tresvbase.getIata());

        List<TResvRate> resvRateList = tresvbase.getResvRateList();
        Map<String, TResvRate> filterMap = new LinkedHashMap<String, TResvRate>();
        for(TResvRate tResvRate : resvRateList){
        	filterMap.put(DateUtil.formatDate(tResvRate.getBookendate()), tResvRate);
        }
        for (TResvRate tResvRate : new ArrayList<TResvRate>(filterMap.values())) {
            /**
             * Node :Crsmessage/Reservation/Bookedrates/Bookedrate
             */
            net.hubs1.gxml.modresv.BookedrateDocument.Bookedrate addNewBookedrate = addNewBookedrates
                    .addNewBookedrate();

            if (tResvRate.getExtrate() != null) {
                addNewBookedrate.setExtracharge(tResvRate.getExtrate().toString());
            }
            addNewBookedrate.setDate(DateUtil.formatDate(tResvRate.getBookendate()));
            addNewBookedrate.setRate(tResvRate.getProprevenue().toString());
            // addNewBookedrate.setCurrencycode(tResvRate.getPropCurrency());

        }
        // Boolean checkBookRate = checkBookRate(resvRateList);
        // if (!checkBookRate) {
        // throw new ThirdPriceNotMatch("agoda价格与fog价格不符！");
        // }
        String crsmessagexml = crsmessagedocument.toString();
        crsmessagexml = crsmessagexml.replaceAll("xmlns=\"http://www.hubs1.net/gxml/" + "ccmodresv" + "\"", "");
        crsmessagexml = crsmessagexml.replaceAll("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "");
        return crsmessagexml;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.thayer.idsservice.service.interf.ICallFogService#validTotalPrice(double, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public PriceValidResult validTotalPrice(double ValidTotalPrice, TResvBase tresvbase) {
        PriceValidResult result = new PriceValidResult();
        result.setVaildResult(false);
        try {
            Map<String, String> fogRateMap = getExRateMap(tresvbase.getProp(), tresvbase.getIata(), tresvbase
                    .getPropCurrency(), DateUtil.formatDate(tresvbase.getIndate()), tresvbase.getRoomcode(), tresvbase
                    .getRateClass(), tresvbase.getRooms().toString(), tresvbase.getNights().toString(), tresvbase
                    .getAdults() == null ? "0" : String.valueOf(tresvbase.getAdults().intValue()
                    / tresvbase.getRooms().intValue()), tresvbase.getChildren() == null ? "" : tresvbase.getChildren()
                    .toString());
            double fogTotalPrice = 0;
            if (fogRateMap != null && fogRateMap.size() > 0) {
                for (String key : fogRateMap.keySet()) {
                    String fogRate = fogRateMap.get(key);
                    double fogRateDouble = Double.parseDouble(fogRate) * tresvbase.getRooms();
                    fogTotalPrice += fogRateDouble;
                    TResvRate resvRate = new TResvRate();
                    resvRate.setBookendate(DateUtil.dateValue(key));
                    resvRate.setProprevenue(new Double(fogRate));
                    result.getResvRateList().add(resvRate);
                }
            }
            if (fogTotalPrice == ValidTotalPrice) {
                result.setVaildResult(true);
            }
            return result;
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            return result;
        }
    }

    /**
     * 组装新单的xml
     * 
     * @param tresvbase
     * @param validateBookRate
     * @param isSpecialOrder TODO
     * @return
     * @throws BizException
     */
    private String buildNewresv(TResvBase tresvbase, boolean validateBookRate, boolean isSpecialOrder) throws BizException {
        /**
         * validateBookRate:true需要验证
         */
        boolean useSpecResv = false;
        if (validateBookRate) {
            try {
                List<TResvRate> resvRateList = tresvbase.getResvRateList();
                Map<String, String> exRateMap = new HashMap<String, String>();
                for (TResvRate resvRate : resvRateList) {
                    exRateMap.put(DateUtil.formatDate(resvRate.getBookendate()), resvRate.getProprevenue().toString());
                }
                // fog
                Map<String, String> fogRateMap = getExRateMap(tresvbase.getProp(), tresvbase.getIata(), tresvbase
                        .getPropCurrency(), DateUtil.formatDate(tresvbase.getIndate()), tresvbase.getRoomcode(),
                        tresvbase.getRateClass(), tresvbase.getRooms().toString(), tresvbase.getNights().toString(),
                        tresvbase.getAdults() == null ? "0" : tresvbase.getAdults().toString(),
                        tresvbase.getChildren() == null ? "" : tresvbase.getChildren().toString());
                if (fogRateMap != null && fogRateMap.size() > 0) {
                    for (String key : fogRateMap.keySet()) {

                        String fogRate = fogRateMap.get(key);
                        String exRate = exRateMap.get(key);
                        double fogRateDouble = Double.parseDouble(fogRate);
                        double exRateDouble = Double.parseDouble(exRate);
                        if (fogRateDouble != exRateDouble) {
                            log.info("fogRateDouble:" + fogRateDouble + "  exRateDouble:" + exRateDouble);
                            if (!needTrySpecResv) {
                                throw new BizException("价格验证失败!");
                            } else {
                                useSpecResv = true;
                            }
                        }
                    }
                } else {
                    if (!needTrySpecResv) {
                        throw new BizException("价格验证失败! Fog没有返回价格信息！");
                    } else {
                        useSpecResv = true;
                    }
                }
            } catch (BizException e) {
                log.error(ExceptionUtils.getFullStackTrace(e));
                throw e;
            } catch (Exception ex) {
                log.error(ExceptionUtils.getFullStackTrace(ex));
                throw new BizException(ex);
            }
        }

        String confnum = "";

        CrsmessageDocument crsmessagedocument = CrsmessageDocument.Factory.newInstance();
        /**
         * Node :Crsmessage
         */
        Crsmessage addNewCrsmessage = crsmessagedocument.addNewCrsmessage();
        addNewCrsmessage.setUser(fogUser);
        addNewCrsmessage.setPass(fogPassword);
        if (useSpecResv || isSpecialOrder) {
            addNewCrsmessage.setMsgtype(NEWSPECRESV);
        } else {
            addNewCrsmessage.setMsgtype(newResvMsgtype);
        }
        addNewCrsmessage.setVersion("1.2");
        addNewCrsmessage.setPropID(tresvbase.getProp());

        /**
         * Node :Crsmessage/Reservation
         */
        Reservation addNewReservation = addNewCrsmessage.addNewReservation();
        addNewReservation.setOutconfnum(tresvbase.getOutcnfnum());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        /**
         * Node :Crsmessage/Reservation/Staydetail
         */
        Staydetail addNewStaydetail = addNewReservation.addNewStaydetail();
        try {
            if ("EXPEDIAXML".equals(fogUser)) {
                net.hubs1.gxml.ccnewresv.GuarruledetailDocument.Guarruledetail gua = addNewReservation
                        .addNewGuarruledetail();
                gua.setRule("TP");
            }
        } catch (Exception e1) {
            log.error(e1);
        }
        
        //TODO 特殊单接口传入保证金制度：CC，取消制度：RN1D1T6
        if(isSpecialOrder && FOG_USER_BOOKING.equals(fogUser)) {
        	net.hubs1.gxml.ccnewresv.GuarruledetailDocument.Guarruledetail gua = addNewReservation.getGuarruledetail();
        	//保证金制度
        	gua.setRule("CC");
        	//取消制度
        	addNewReservation.setCxlnum("RN1D1T6");
        }
        
        
        String checkIndate = simpleDateFormat2.format(tresvbase.getIndate());
        addNewStaydetail.setDate(checkIndate);
        String nights = String.valueOf(tresvbase.getNights());
        addNewStaydetail.setNights(nights);
        String rooms = String.valueOf(tresvbase.getRooms());
        addNewStaydetail.setRooms(rooms);
        int adult = 0;
        if (tresvbase.getRooms() != 0) {
            int num1 = tresvbase.getAdults() / tresvbase.getRooms();
            if (tresvbase.getAdults() % tresvbase.getRooms() == 0) {
                adult = num1;
            } else {
                adult = num1 + 1;
            }
        }
        addNewStaydetail.setAdults(String.valueOf(adult));

        addNewStaydetail.setChildren(String.valueOf(tresvbase.getChildren()));
        addNewStaydetail.setRoomtype(tresvbase.getRoomcode());
        addNewStaydetail.setRateclass(tresvbase.getRateClass());

        if (StringUtils.hasText(tresvbase.getChannel())) {
            addNewStaydetail.setChannel(tresvbase.getChannel());
        }
        /**
         * Node :Crsmessage/Reservation/Bookedrates
         */
        Bookedrates addNewBookedrates = addNewReservation.addNewBookedrates();
        Ccinfo addNewCcinfo = addNewReservation.addNewCcinfo();
        addNewCcinfo.setCctype(tresvbase.getCctype());
        addNewCcinfo.setCvv(tresvbase.getCvv());
        addNewCcinfo.setCcnum(tresvbase.getCcnumber());
        addNewCcinfo.setCcexp(tresvbase.getCcexpiration());
        addNewCcinfo.setCcname(tresvbase.getCcname());
        /**
         * Node :Crsmessage/Reservation/Guestinfo
         */
        Guestinfo addNewGuestinfo = addNewReservation.addNewGuestinfo();
//        if (StringUtils.hasText(tresvbase.getGfirstname())) {
//            addNewGuestinfo.setFirstname(tresvbase.getGfirstname());
//        }
//
//        if (StringUtils.hasText(tresvbase.getGlastname())) {
//            addNewGuestinfo.setLastname(tresvbase.getGlastname());
//        }

        List<String> guestList = tresvbase.getGuestList();
        if(CollectionUtils.isNotEmpty(guestList)){
        	addNewGuestinfo.setLastname(guestList.get(0));
        	int guestSize = guestList.size();
        	if(guestSize > 1){
        		Guestinfo.Otherguest others = addNewGuestinfo.addNewOtherguest();
        		for(int i=1; i<guestSize; i++){
        			others.addGuestname(guestList.get(i));
        		}
        	}
        }

        if (StringUtils.hasText(tresvbase.getGuaCode()) || StringUtils.hasText(tresvbase.getGuadesc())) {
            net.hubs1.gxml.ccnewresv.GuarruledetailDocument.Guarruledetail addNewGuarruledetail = addNewReservation
                    .addNewGuarruledetail();
            addNewGuarruledetail.setRule(tresvbase.getGuaCode());
            addNewGuarruledetail.setDescription(tresvbase.getGuadesc());
        }
        try {
            addNewGuestinfo.setPhone(String.valueOf(tresvbase.getGuestphone()));
        } catch (Exception e) {
            addNewGuestinfo.setPhone("02161226688*7800");
        }
        try {
            addNewGuestinfo.setEmail(String.valueOf(tresvbase.getGuestmail()));
        } catch (Exception e) {
            addNewGuestinfo.setEmail("service@hubs1.net");
        }
        try {
            if (tresvbase.getArrivaltime() != null) {
                addNewGuestinfo.setHoldTime(DateUtil.formatTimeHHmm(tresvbase.getArrivaltime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * Node :Crsmessage/Reservation/Remarks
         */
        Remarks addNewRemarks = addNewReservation.addNewRemarks();
        if (StringUtils.hasText(tresvbase.getRemark())) {
            XmlString addNewRemark = addNewRemarks.addNewRemark();
            addNewRemark.setStringValue(tresvbase.getRemark());
        }

        /**
         * Node :Crsmessage/Reservation/Serviceprices
         */
        List<com.thayer.idsservice.task.download.bean.Serviceprices> serviceprices = tresvbase.getServiceprices();

        if (serviceprices.size() > 0) {
            Serviceprices addNewServiceprices = addNewReservation.addNewServiceprices();
            for (com.thayer.idsservice.task.download.bean.Serviceprices serviceprice : serviceprices) {
                Serviceprice serv = addNewServiceprices.addNewServiceprice();
                serv.setCount(serviceprice.getCount());
                serv.setFreq(serviceprice.getFreq());
                serv.setInclude(serviceprice.getInclude());
                serv.setName(serviceprice.getName());
                serv.setType(serviceprice.getType());
                serv.setUnit(serviceprice.getUnit());
            }
        }

        /**
         * Node :Crsmessage/Reservation/Miscinfo
         */
        Miscinfo addNewMiscinfo = addNewReservation.addNewMiscinfo();
        addNewMiscinfo.setIATA(tresvbase.getIata());

        List<TResvRate> resvRateList = tresvbase.getResvRateList();
        Map<String, TResvRate> filterMap = new LinkedHashMap<String, TResvRate>();
        for(TResvRate tResvRate : resvRateList){
        	filterMap.put(DateUtil.formatDate(tResvRate.getBookendate()), tResvRate);
        }
        for (TResvRate tResvRate : new ArrayList<TResvRate>(filterMap.values())) {
            /**
             * Node :Crsmessage/Reservation/Bookedrates/Bookedrate
             */
            Bookedrate addNewBookedrate = addNewBookedrates.addNewBookedrate();
            if (tResvRate.getExtrate() != null) {
                addNewBookedrate.setExtracharge(tResvRate.getExtrate().toString());
            }
            addNewBookedrate.setDate(DateUtil.formatDate(tResvRate.getBookendate()));

            addNewBookedrate.setRate(tResvRate.getProprevenue().toString());
            addNewBookedrate.setCurrencycode(tResvRate.getPropCurrency());

        }
        
        /**
         * Node :Crsmessage/Reservation/ISASSURE
         * BOOKING接口下单,传入担保节点<isassure>=5;
         * EXPEDIA接口下单,增加expedia的ratePlanID判断，如果对方的ratePlanID是以A结尾，传担保节点<isassure>=5;
         * 如果不是以A结尾，请传担保节点<isassure>=4
         */
        if(BOOKINGCOM_IATA.equalsIgnoreCase(tresvbase.getIata())) {
        	addNewReservation.setIsassure("5");
        }else if (EXPEDIA_IATA.equalsIgnoreCase(tresvbase.getIata())) {
        	addNewReservation.setIsassure("4");
        	String ratePlanID = tresvbase.getPlancode();
        	if(ratePlanID.endsWith("A")) {
        		addNewReservation.setIsassure("5");
        	}
        }
        
        // Boolean checkBookRate = checkBookRate(resvRateList);
        // if (!checkBookRate) {
        // throw new ThirdPriceNotMatch("agoda价格与fog价格不符！");
        // }
        String crsmessagexml = crsmessagedocument.toString();
        crsmessagexml = crsmessagexml.replaceAll("xmlns=\"http://www.hubs1.net/gxml/" + "ccnewresv" + "\"", "");
        crsmessagexml = crsmessagexml.replaceAll("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "");
        return crsmessagexml;
    }

    private MailFogResvBaseBean convert2fogCxlResvMailBean(net.hubs1.gxml.cxlresvRS.SrvmessageDocument srvmeaasge,
            TResvBase tresvbase) {
        MailFogResvBaseBean mailbean = new MailFogResvBaseBean();

        List<TResvRate> resvRateList = mailbean.getResvRateList();

        if (srvmeaasge != null) {
            net.hubs1.gxml.cxlresvRS.ResvdetailDocument.Resvdetail resvdetail = srvmeaasge.getSrvmessage()
                    .getResvdata().getResvdetailArray()[0];

            mailbean.setCnfnum(resvdetail.getConfnum());

            mailbean.setGfirstname(resvdetail.getFirstname());
            mailbean.setGlastname(resvdetail.getLastname());
            mailbean.setStatus(resvdetail.getStatus());
            mailbean.setResvtype("取消单");
            mailbean.setRoomcode(tresvbase.getRoomcode());
            mailbean.setPlancode(resvdetail.getPlanid());
            mailbean.setBookeddate(DateUtil.dateValue(resvdetail.getBookdate()));

            mailbean.setRemark(tresvbase.getRemark());
            mailbean.setRealindate(DateUtil.formatDate(tresvbase.getRealindate()));
            mailbean.setRealoutdate(DateUtil.formatDate(tresvbase.getRealoutdate()));
            mailbean.setRooms(tresvbase.getRooms());
            mailbean.setNights(tresvbase.getNights());
            mailbean.setAdults(tresvbase.getAdults());
            mailbean.setChildren(tresvbase.getChildren());
            mailbean.setChannel(tresvbase.getChannel());
            mailbean.setPayment(tresvbase.getPayment());
            mailbean.setCcname(tresvbase.getCcname());
            mailbean.setCcnumber(tresvbase.getCcnumber());
            mailbean.setCctype(tresvbase.getCctype());
            mailbean.setCcexpiration(tresvbase.getCcexpiration());
            mailbean.setLastmodifydate(DateUtil.formatDate(tresvbase.getLastmodifydate()));
            mailbean.setIndate(DateUtil.formatDate(tresvbase.getIndate()));
            mailbean.setOutdate(DateUtil.formatDate(tresvbase.getOutdate()));
            mailbean.setGuestphone(tresvbase.getGuestphone());
            mailbean.setGuestmail(tresvbase.getGuestmail());
            mailbean.setGuestaddress(tresvbase.getGuestaddress());
            mailbean.setGuaCode(tresvbase.getGuaCode());

        } else {
            return null;
        }

        return mailbean;
    }

    private MailFogResvBaseBean convert2fogModResvMailBean(net.hubs1.gxml.modresvRS.SrvmessageDocument srvmeaasge,
            TResvBase tresvbase) {
        MailFogResvBaseBean mailbean = new MailFogResvBaseBean();

        List<TResvRate> resvRateList = mailbean.getResvRateList();
        if (srvmeaasge != null) {
            net.hubs1.gxml.modresvRS.ResvdetailDocument.Resvdetail resvdetail = srvmeaasge.getSrvmessage()
                    .getResvdata().getResvdetailArray()[0];

            mailbean.setCnfnum(resvdetail.getConfnum());

            mailbean.setGfirstname(resvdetail.getFirstname());
            mailbean.setGlastname(resvdetail.getLastname());
            mailbean.setStatus(resvdetail.getStatus());
            mailbean.setResvtype("修改单");
            mailbean.setRoomcode(tresvbase.getRoomcode());
            mailbean.setPlancode(resvdetail.getPlanid());
            mailbean.setBookeddate(DateUtil.dateValue(resvdetail.getBookdate()));

            mailbean.setRemark(tresvbase.getRemark());
            mailbean.setRealindate(DateUtil.formatDate(tresvbase.getRealindate()));
            mailbean.setRealoutdate(DateUtil.formatDate(tresvbase.getRealoutdate()));
            mailbean.setRooms(tresvbase.getRooms());
            mailbean.setNights(tresvbase.getNights());
            mailbean.setAdults(tresvbase.getAdults());
            mailbean.setChildren(tresvbase.getChildren());
            mailbean.setChannel(tresvbase.getChannel());
            mailbean.setPayment(tresvbase.getPayment());
            mailbean.setCcname(tresvbase.getCcname());
            mailbean.setCcnumber(tresvbase.getCcnumber());
            mailbean.setCctype(tresvbase.getCctype());
            mailbean.setCcexpiration(tresvbase.getCcexpiration());
            mailbean.setLastmodifydate(DateUtil.formatDate(tresvbase.getLastmodifydate()));
            mailbean.setIndate(DateUtil.formatDate(tresvbase.getIndate()));
            mailbean.setOutdate(DateUtil.formatDate(tresvbase.getOutdate()));
            mailbean.setGuestphone(tresvbase.getGuestphone());
            mailbean.setGuestmail(tresvbase.getGuestmail());
            mailbean.setGuestaddress(tresvbase.getGuestaddress());
            mailbean.setGuaCode(tresvbase.getGuaCode());
        } else {
            return null;
        }

        return mailbean;
    }

    private MailFogResvBaseBean convert2fogNewResvMailBean(SrvmessageDocument srvmeaasge, TResvBase tresvbase) {
        MailFogResvBaseBean mailbean = new MailFogResvBaseBean();

        List<TResvRate> resvRateList = mailbean.getResvRateList();
        if (srvmeaasge != null) {
            Resvdetail resvdetail = srvmeaasge.getSrvmessage().getResvdata().getResvdetailArray()[0];

            mailbean.setCnfnum(resvdetail.getConfnum());
            mailbean.setOutcnfnum(tresvbase.getOutcnfnum());
            mailbean.setGfirstname(resvdetail.getFirstname());
            mailbean.setGlastname(resvdetail.getLastname());
            mailbean.setStatus(resvdetail.getStatus());
            mailbean.setResvtype("新单");
            mailbean.setRoomcode(tresvbase.getRoomcode());
            mailbean.setPlancode(resvdetail.getPlanid());
            mailbean.setRateCode(tresvbase.getRateClass());
            mailbean.setBookeddate(DateUtil.dateValue(resvdetail.getBookdate()));
            mailbean.setGuaCode(tresvbase.getGuaCode());
            /**
             * BookenRates
             */
            net.hubs1.gxml.newresvRS.ResvdetailDocument.Resvdetail.Bookedrates bookedrates = resvdetail
                    .getBookedrates();
            String currency = bookedrates.getCurrency();
            String totalrevenue = bookedrates.getTotalrevenue();
            net.hubs1.gxml.newresvRS.ResvdetailDocument.Resvdetail.Bookedrates.Bookedrate bookedrate = bookedrates
                    .getBookedrate();
            mailbean.setProptotalrevenue(Double.parseDouble(totalrevenue));
            mailbean.setCurrencyCode(currency);
            int i = 0;
            for (String rate : bookedrate.getRateArray()) {
                TResvRate tResvRate = new TResvRate();
                tResvRate.setBookendate(DateUtil.dateAdd(DateUtil.TIME_UNIT_D, i, DateUtil.dateValue(resvdetail
                        .getArrival())));
                tResvRate.setBookendatestr(DateUtil.formatDate(tResvRate.getBookendate()));
                tResvRate.setPropCurrency(currency);
                tResvRate.setProprevenue(Double.parseDouble(rate));
                resvRateList.add(tResvRate);
                i++;
            }

            Guarruledetail guarruledetail = resvdetail.getGuarruledetail();
            mailbean.setGuadesc(guarruledetail.getRule());

            mailbean.setCxldesc(resvdetail.getCxlruledetail().getRule());

            mailbean.setRemark(tresvbase.getRemark());
            mailbean.setRealindate(DateUtil.formatDate(tresvbase.getRealindate()));
            mailbean.setRealoutdate(DateUtil.formatDate(tresvbase.getRealoutdate()));
            mailbean.setRooms(tresvbase.getRooms());
            mailbean.setNights(tresvbase.getNights());
            mailbean.setAdults(tresvbase.getAdults());
            mailbean.setChildren(tresvbase.getChildren());
            mailbean.setChannel(tresvbase.getChannel());
            mailbean.setPayment(tresvbase.getPayment());
            mailbean.setCcname(tresvbase.getCcname());
            mailbean.setCcnumber(tresvbase.getCcnumber());
            mailbean.setCctype(tresvbase.getCctype());
            mailbean.setCcexpiration(tresvbase.getCcexpiration());
            mailbean.setLastmodifydate(DateUtil.formatDate(tresvbase.getLastmodifydate()));
            mailbean.setIndate(DateUtil.formatDate(tresvbase.getIndate()));
            mailbean.setOutdate(DateUtil.formatDate(tresvbase.getOutdate()));
            mailbean.setGuestphone(tresvbase.getGuestphone());
            mailbean.setGuestmail(tresvbase.getGuestmail());
            mailbean.setGuestaddress(tresvbase.getGuestaddress());

        } else {
            return null;
        }

        return mailbean;
    }

    /**
     * @return the cancelResvMsgtype
     */
    public String getCancelResvMsgtype() {
        return cancelResvMsgtype;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public EResvMapDAO getEresvMapDAO() {
        return eresvMapDAO;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.thayer.idsservice.service.interf.ICallFogService#getExRateMap(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public Map getExRateMap(String propid, String fogIata, String currencycode, String date, String roomcode,
            String rateCode, String rooms, String nights, String adults, String children) throws BizException {
        try {
            net.hubs1.gxml.getexratemap.CrsmessageDocument newInstance = net.hubs1.gxml.getexratemap.CrsmessageDocument.Factory
                    .newInstance();
            /**
             * Node :Crsmessage
             */
            net.hubs1.gxml.getexratemap.CrsmessageDocument.Crsmessage addNewCrsmessage = newInstance.addNewCrsmessage();
            addNewCrsmessage.setIata(fogIata);
            addNewCrsmessage.setPropID(propid);
            addNewCrsmessage.setLanguage(fogLanguage);
            addNewCrsmessage.setPass(fogPassword);
            addNewCrsmessage.setUser(fogUser);
            addNewCrsmessage.setVersion("1.2");
            addNewCrsmessage.setMsgtype("getexratemap");

            /**
             * Node :Crsmessage/Staydetail
             */
            net.hubs1.gxml.getexratemap.StaydetailDocument.Staydetail addNewStaydetail = addNewCrsmessage
                    .addNewStaydetail();
            addNewStaydetail.setCurrencycode(currencycode);
            SimpleDateFormat f = new SimpleDateFormat("");
            addNewStaydetail.setDate(date);
            addNewStaydetail.setRoomcode(roomcode);
            addNewStaydetail.setRatecode(rateCode);
            addNewStaydetail.setRooms(rooms);
            addNewStaydetail.setNights(nights);
            addNewStaydetail.setChannel(channelCode);
            addNewStaydetail.setAdults(adults);
            addNewStaydetail.setChildren(children);
            addNewStaydetail.setFilter("0");

            String rateMapRequest = newInstance.toString();

            rateMapRequest = rateMapRequest.replaceAll("xmlns=\"http://www.hubs1.net/gxml/getexratemap\"", "");
            rateMapRequest = rateMapRequest.replaceAll("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "");
            log.info("request Fog Rate : " + rateMapRequest);
            Map parms = new HashMap();
            parms.put("Message", rateMapRequest);
            String post2FogExMessage = httpClientService.postHttpRequest(fogExUrl, parms);
            log.info("response Fog Rate : " + post2FogExMessage);

            post2FogExMessage = post2FogExMessage.replaceAll("<srvmessage ",
                    "<srvmessage xmlns=\"http://www.hubs1.net/gxml/getexratemapRS\" ");

            net.hubs1.gxml.getexratemapRS.SrvmessageDocument rateMapResponse = net.hubs1.gxml.getexratemapRS.SrvmessageDocument.Factory
                    .parse(post2FogExMessage);
            Srvmessage srvmessage = rateMapResponse.getSrvmessage();
            String result = srvmessage.getResult();

            Map fogRateMap = new LinkedHashMap<String, String>();
            if (StringUtils.hasText(result) && result.equalsIgnoreCase("success")) {
                Ratemap ratemap = srvmessage.getRatemap();
                Ratedata[] ratedataArray = ratemap.getRatedataArray();
                for (int i = 0; i < ratedataArray.length; i++) {
                    Ratedata ratedata = ratedataArray[i];
                    for (int j = 0; j < ratedata.getRatedetailArray().length; j++) {
                        Ratedetail ratedetail = ratedata.getRatedetailArray()[j];
                        String rateSingle = ratedetail.getTotal();
                        String rateDetailDate = ratedetail.getDate();
                        fogRateMap.put(rateDetailDate, rateSingle);
                    }
                }
                return fogRateMap;
            } else {
                throw new Exception("获取rateMap失败！");
            }
        } catch (Exception e) {
            throw new BizException(e);
        }

    }

    public String getFogExUrl() {
        return fogExUrl;
    }

    public String getFogLanguage() {
        return fogLanguage;
    }

    public String getFogPassword() {
        return fogPassword;
    }

    public AvailWebService getFogServiceAvailWSClient() {
        return fogServiceAvailWSClient;
    }

    public AvailExchangeRateWebService getFogServiceWSClient() {
        return fogServiceWSClient;
    }

    public String getFogUser() {
        return fogUser;
    }

    public IHttpClientService getHttpClientService() {
        return httpClientService;
    }

    /**
     * @return the modResvMsgtype
     */
    public String getModResvMsgtype() {
        return modResvMsgtype;
    }

    public String getNewResvMsgtype() {
        return newResvMsgtype;
    }

    
    @Override
    public List<RateDataBO> getZRateMap(String prop, Date start, int nights, String planId, String channelCode) throws BizException {
        try {
            return fogServiceWSClient.getZRateMap(prop, start, nights, null, null, planId, channelCode);
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            throw new BizException(e);
        }
    }

    public AvailBean getzRateMapList(FogMQBean fogMQBean) throws BizException {
        AvailBean availBean = new AvailBean();

        boolean update3LevelAvail = fogMQBean.isUpdate3LevelAvail();
        RateMQVO rateMQVO = fogMQBean.getRateMQVO();
        
       
        /**将酒店级的变化 ，房型级的变化拆分成计划级的计划查询*/
        
      //酒店级的变化 都分解成计划级的 去读取fog内容
        if(fogMQBean.getRateMQVO().getOpetype() == 1 && !update3LevelAvail){
        	//改成 计划及
        	fogMQBean.getRateMQVO().setOpetype(4);
        	//从数据库读取房型计划 放入fogMQBean中
        	ERatemap searchRate = new ERatemap();
        	searchRate.setFogpropId(fogMQBean.getRateMQVO().getProp());
        	searchRate.setExwebCode(fogMQBean.getIata());
        	
        	List<ERatemap> rates = eratemapDAO.findByExample(searchRate);
        	Collection<String> planCodes = new ArrayList<String>();
        	if(rates != null && !rates.isEmpty()){
        		for (ERatemap rate : rates) {
        			planCodes.add(rate.getFograteId());
				}
        	}
        	fogMQBean.getRateMQVO().setPlancodes(planCodes);
        	log.info("酒店级转化为计划级，"+fogMQBean.getRateMQVO());
        }
        
        if(fogMQBean.getRateMQVO().getOpetype() == 2 && !update3LevelAvail){//如果是房型级的 也改成计划级的
        	//改成 计划及
        	fogMQBean.getRateMQVO().setOpetype(4);
        	//从数据库读取房型计划 放入fogMQBean中
        	ERatemap searchRate = new ERatemap();
        	searchRate.setFogpropId(fogMQBean.getRateMQVO().getProp());
        	searchRate.setExwebCode(fogMQBean.getIata());
        	
        	List<ERatemap> rates = eratemapDAO.findByExample(searchRate);
        	Collection<String> planCodes = new ArrayList<String>();
        	if(rates != null && !rates.isEmpty()){
        		for (ERatemap rate : rates) {
        			String planCode = rate.getFograteId();
        			//房型级变化 把此房型对应的计划罗列出来
        			for (String roomCode : rateMQVO.getRoomcodes()) {
						//计划名称ex：DT-ESAVE 
        				if(planCode.startsWith(roomCode+"-")){
        					planCodes.add(planCode);
        				}
					}
				}
        	}
        	fogMQBean.getRateMQVO().setPlancodes(planCodes);
        	log.info("房型级级转化为计划级，"+fogMQBean.getRateMQVO());
        }
        
        String channel = rateMQVO.getChannel();
        Date fromDate = rateMQVO.getFromDate();
        Date toDate = rateMQVO.getToDate();
        int opetype = rateMQVO.getOpetype();// 0:不处理 1:酒店级 2:房型级3： rate级 4:计划级
        int[] weeks = rateMQVO.getWeeks();
        String prop = rateMQVO.getProp();
        Collection<String> roomcodes = rateMQVO.getRoomcodes();
        Collection<String> ratecodes = rateMQVO.getRatecodes();
        Collection<String> plancodes = rateMQVO.getPlancodes();
        List<RateDataBO> zRateMapList = availBean.getRateDataBOList();
        List<AvailAllow> availAllowList = availBean.getAvailAllowList();
        
        
        
        switch (opetype) {
            case 0:
                break;
            case 1://上层代码控制 所有酒店级变化 都 分开按计划级读取
                try {
                    if (!update3LevelAvail) {
                    	//酒店级更改通知不处理
                        //zRateMapList = fogServiceWSClient.getZRateMap(prop, fromDate, DateUtil.dateDiff(
                        //        DateUtil.TIME_UNIT_D, fromDate, toDate) + 1, null, null, null, channelCode);
                        //availBean.setRateDataBOList(zRateMapList);
                    }
                    if (update3LevelAvail) {
                        availAllowList = fogServiceAvailWSClient.findAvailAllowsByLevelProp(prop, fromDate, toDate);
                        availBean.setAvailAllowList(availAllowList);
                    }
                } catch (Exception e) {
                	LogUtil.error(log, "fogServiceAvailWSClient.findAvailAllowsByLevelProp", e, "", prop, fromDate, toDate);
                    throw new BizException(e);
                }

                break;
            case 2:
                try {
                    if (!update3LevelAvail) {
                        for (String roomCode : roomcodes) {
                            List<RateDataBO> zRateMap = fogServiceWSClient.getZRateMap(prop, fromDate, DateUtil
                                    .dateDiff(DateUtil.TIME_UNIT_D, fromDate, toDate) + 1, roomCode, null, null,
                                    channelCode);
                            if (zRateMap != null) {
                                zRateMapList.addAll(zRateMap);
                            }

                        }
                    }
                    if (update3LevelAvail) {
                        for (String roomCode : roomcodes) {
                            List<AvailAllow> availAllowRoomList = fogServiceAvailWSClient.findAvailAllowsByLevelRoom(
                                    prop, fromDate, toDate, roomCode);
                            availAllowList.addAll(availAllowRoomList);
                        }
                        availBean.setAvailAllowList(availAllowList);

                    }
                } catch (Exception e) {
                	LogUtil.error(log, "getZRateMap", e, "ROOM_LEVEL", prop, fromDate, toDate,roomcodes);
                    throw new BizException(e);
                }
                break;
            case 3:
            	 //暂无RATE级别价格变更通知 2012/6/9
//                try {
//                    for (String rateCode : ratecodes) {
//                        List<RateDataBO> zRateMap = fogServiceWSClient.getZRateMap(prop, fromDate, DateUtil.dateDiff(
//                                DateUtil.TIME_UNIT_D, fromDate, toDate) + 1, null, rateCode, null, channelCode);
//                        if (zRateMap != null) {
//                            zRateMapList.addAll(zRateMap);
//                        }
//                    }
//
//                } catch (Exception e) {
//                    throw new BizException(e);
//                }
                break;
            case 4:
                try {
                    for (String planCode : plancodes) {
                        log.debug(DateUtil.formatDate(fromDate));
                        log.debug(DateUtil.formatDate(toDate));
                        log.debug(DateUtil.dateDiff(DateUtil.TIME_UNIT_D, fromDate, toDate));
                        log.debug("channelCode:" + channelCode);
                        List<String> months = DateUtil.splitDateToMonth(fromDate, toDate);
                        log.info(fromDate + ","+toDate+"按月分割日期：" +months);
                        for (String string : months) {//分月获取数据
                        	String[] daysArr = string.split("#");
                        	
                        	Date frist = DateUtil.dateValue(daysArr[0]) ;
                        	Date last = DateUtil.dateValue(daysArr[1]) ;
                        	
                        	List<RateDataBO> zRateMap = fogServiceWSClient.getZRateMap(prop, frist, DateUtil.dateDiff(
                                    DateUtil.TIME_UNIT_D, frist, last) + 1, null, null, planCode, channelCode);
                            if (zRateMap != null) {
                                zRateMapList.addAll(zRateMap);
                            }
						}
//                        log.info("查询FOG数据条数:"+zRateMapList.get(0).getRateList().size());
                    }

                } catch (Exception e) {
                	LogUtil.error(log, "getZRateMap", e, "PLAN_LEVEL", prop, fromDate, toDate , plancodes);
                    throw new BizException(e);
                }
                break;
            default:
                break;
        }

        return availBean;
    }
    
    
    
    public static void main(String[] args) throws Exception{
    	Date frist = DateUtil.dateValue("2012-11-01") ;
    	Date last = DateUtil.dateValue("2012-11-30") ;
		int i = DateUtil.dateDiff(DateUtil.TIME_UNIT_D, frist, last)+1;   
		System.out.println(i);
	}

    /**
     * @return the needTrySpecResv
     */
    public boolean isNeedTrySpecResv() {
        return needTrySpecResv;
    }

    private MailFogResvBaseBean parseCxlResvRS(TResvBase tresvbase, String infostr, String post2FogExMessage)
            throws XmlException, BizException {
        String confnum;
        net.hubs1.gxml.cxlresvRS.SrvmessageDocument srvmeaasge = net.hubs1.gxml.cxlresvRS.SrvmessageDocument.Factory
                .parse(post2FogExMessage);
        String result = srvmeaasge.getSrvmessage().getResult();
        confnum = srvmeaasge.getSrvmessage().getResvdata().getResvdetailArray()[0].getConfnum();
        if (StringUtils.hasText(result) && result.equalsIgnoreCase("success")) {
            MailFogResvBaseBean convert2fogResvMailBean = convert2fogCxlResvMailBean(srvmeaasge, tresvbase);
            return convert2fogResvMailBean;
        } else {
            log.error(infostr + srvmeaasge.toString());
            throw new BizException(srvmeaasge.toString());
        }
    }

    private MailFogResvBaseBean parseModResvRS(TResvBase tresvbase, String infostr, String post2FogExMessage)
            throws XmlException, BizException {
        String confnum;
        net.hubs1.gxml.modresvRS.SrvmessageDocument srvmeaasge = net.hubs1.gxml.modresvRS.SrvmessageDocument.Factory
                .parse(post2FogExMessage);
        String result = srvmeaasge.getSrvmessage().getResult();
        confnum = srvmeaasge.getSrvmessage().getResvdata().getResvdetailArray()[0].getConfnum();
        if (StringUtils.hasText(result) && result.equalsIgnoreCase("success")) {
            MailFogResvBaseBean convert2fogResvMailBean = convert2fogModResvMailBean(srvmeaasge, tresvbase);
            return convert2fogResvMailBean;
        } else {
            log.error(infostr + srvmeaasge.toString());
            throw new BizException(srvmeaasge.toString());
        }
    }

    private MailFogResvBaseBean parseNewResvRS(TResvBase tresvbase, String infostr, String post2FogExMessage)
            throws XmlException, BizException {
        String confnum;
        SrvmessageDocument srvmeaasge = SrvmessageDocument.Factory.parse(post2FogExMessage);
        String result = srvmeaasge.getSrvmessage().getResult();
        confnum = srvmeaasge.getSrvmessage().getResvdata().getResvdetailArray()[0].getConfnum();
        if (StringUtils.hasText(result) && result.equalsIgnoreCase("success")) {
            MailFogResvBaseBean convert2fogResvMailBean = convert2fogNewResvMailBean(srvmeaasge, tresvbase);
            return convert2fogResvMailBean;
        } else {
            log.error(infostr + srvmeaasge.toString());
            throw new BizException(srvmeaasge.toString());
        }
    }

    private String getMsgType(String orderXml){
    	if(org.apache.commons.lang.StringUtils.isEmpty(orderXml)){
    		return null;
    	}
    	String msgtype = "";
		int sIndex = orderXml.indexOf("msgtype=\"") + "msgtype=\"".length();
		if(sIndex < 0){
			return null;
		}
		int eIndex = orderXml.indexOf("\"", sIndex);
		msgtype = orderXml.substring(sIndex, eIndex);
    	return msgtype;
    }
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.thayer.idsservice.service.interf.ICallFogService#saveFogNewResvBean(com.thayer.idsservice.task.download.bean
     * .TResvBase)
     */
    public MailFogResvBaseBean saveFogNewResvBean(TResvBase tresvbase, boolean validateBookRate) throws BizException,
            TimeOut4FogException {
        String confnum;
        String resvtype = tresvbase.getResvtype();
        String crsmessagexml = "";
        String infostr = "";
        if (Constents.RESV_TYPE_NEW.equalsIgnoreCase(resvtype)) {
            crsmessagexml = buildNewresv(tresvbase, validateBookRate, false);
            infostr = "fogNewResvResponseXML:";
        } else if (Constents.RESV_TYPE_EDIT.equalsIgnoreCase(resvtype)) {
            crsmessagexml = buildEditresv(tresvbase);
            infostr = "fogEditResvResponseXML:";
        } else if (Constents.RESV_TYPE_CANCEL.equalsIgnoreCase(resvtype)) {
            crsmessagexml = buildCancelresv(tresvbase);
            infostr = "fogCancelResvResponseXML:";
        } else {
            throw new BizException("Not set resvtype!resvtype="+resvtype);
        }

        //TODO 获取下单接口
        String msgType = this.getMsgType(crsmessagexml);
        tresvbase.setOrderMsgType(msgType);
        
        log.info("crsmessagexml:" + crsmessagexml);
        String post2FogExMessage = "";

        try {
            Map parms = new HashMap();
            parms.put("Message", crsmessagexml);
            post2FogExMessage = httpClientService.postHttpRequest(fogExUrl, parms);
            log.info("post2FogExMessage:" + post2FogExMessage);
            if (post2FogExMessage.toLowerCase().contains("fail")) {
            	
            	
//            	if (Constents.RESV_TYPE_EDIT.equalsIgnoreCase(resvtype)) {
//            		// 0017011: X201311-3 IDS接口订单修改增加价格校验
//                	// 放FOG 做了	
//            		throw new BizException(post2FogExMessage);
//            	}
//            	
//            	if (Constents.RESV_TYPE_NEW.equalsIgnoreCase(resvtype)) {
//            		// TODO 0017012: X201311-4 IDS接口(Booking.com)订单增加自动调用特殊单接口
//            		
//            	} 
            	
            	//Booking.com渠道上的特殊单处理
            	if(BOOKINGCOM_IATA.equalsIgnoreCase(tresvbase.getIata())) {
            		
                	//如FOG设置了满房或停售，将不采用直接调用特殊单接口进行下单，改为下单失败方式进行邮件通知CC处理。
					if (post2FogExMessage.contains(BOOKINGCOM_FULLROOM_CODE)) {
                		throw new BizException("FOG设置了满房,将不采用直接调用特殊单接口进行下单，改为下单失败方式进行邮件通知CC处理。");
                	}
					
                	//Booking.com渠道存在多个房型订单，多个房型订单仍采用邮件通知CC处理
					if (tresvbase.isMultiRoomType()) {
                		throw new BizException("It is a multiRoomType,send mail to CC and CC handle it!");
                	}
            	}
            	
            	
                if (needTrySpecResv) {
                	
                    if (Constents.RESV_TYPE_NEW.equalsIgnoreCase(resvtype)) {
                        crsmessagexml = buildNewresv(tresvbase, false, true);
                        infostr = "fogNewResvResponseXML:";
                    } else if (Constents.RESV_TYPE_EDIT.equalsIgnoreCase(resvtype)) {
                        crsmessagexml = buildEditresv(tresvbase);
                        infostr = "fogEditResvResponseXML:";
                    } else if (Constents.RESV_TYPE_CANCEL.equalsIgnoreCase(resvtype)) {
                        crsmessagexml = buildCancelresv(tresvbase);
                        infostr = "fogCancelResvResponseXML:";
                    } else {
                        throw new BizException("Not set resvtype!resvtype="+resvtype);
                    }

                	
                	
                    log.info("try to use specresv................");
                    String speccrsmessagexml = null;
                    if (Constents.RESV_TYPE_NEW.equalsIgnoreCase(resvtype)) {
                        speccrsmessagexml = crsmessagexml.replaceAll("msgtype=\"" + newResvMsgtype + "\"", "msgtype=\""
                                + NEWSPECRESV + "\"");
                    } else if (Constents.RESV_TYPE_EDIT.equalsIgnoreCase(resvtype)) {
                        speccrsmessagexml = crsmessagexml.replaceAll("msgtype=\"" + modResvMsgtype + "\"", "msgtype=\""
                                + MODSPECRESV + "\"");
                    } else if (Constents.RESV_TYPE_CANCEL.equalsIgnoreCase(resvtype)) {
                        speccrsmessagexml = crsmessagexml.replaceAll("msgtype=\"" + cancelResvMsgtype + "\"",
                                "msgtype=\"" + CXLSPECRESV + "\"");
                    }

                    parms.put("Message", speccrsmessagexml);
                    post2FogExMessage = httpClientService.postHttpRequest(fogExUrl, parms);
                    
                    if (post2FogExMessage.toLowerCase().contains("fail")) {
                        log.error(infostr + post2FogExMessage);
                        throw new BizException(post2FogExMessage);
                    }
                    
                    msgType = this.getMsgType(speccrsmessagexml);
                    tresvbase.setOrderMsgType(msgType);

                } else {
                    log.error(infostr + post2FogExMessage);
                    throw new BizException(post2FogExMessage);
                }
            }
            MailFogResvBaseBean parseNewResvRS = new MailFogResvBaseBean();
            if (post2FogExMessage.indexOf("xmlns=") == -1) {

                if (Constents.RESV_TYPE_NEW.equalsIgnoreCase(resvtype)) {
                    post2FogExMessage = post2FogExMessage.replaceAll("<srvmessage ",
                            "<srvmessage xmlns=\"http://www.hubs1.net/gxml/newresvRS\" ");
                    parseNewResvRS = parseNewResvRS(tresvbase, infostr, post2FogExMessage);
                } else if (Constents.RESV_TYPE_EDIT.equalsIgnoreCase(resvtype)) {
                    post2FogExMessage = post2FogExMessage.replaceAll("<srvmessage ",
                            "<srvmessage xmlns=\"http://www.hubs1.net/gxml/modresvRS\" ");
                    parseNewResvRS = parseModResvRS(tresvbase, infostr, post2FogExMessage);
                } else if (Constents.RESV_TYPE_CANCEL.equalsIgnoreCase(resvtype)) {
                    post2FogExMessage = post2FogExMessage.replaceAll("<srvmessage ",
                            "<srvmessage xmlns=\"http://www.hubs1.net/gxml/cxlresvRS\" ");
                    parseNewResvRS = parseCxlResvRS(tresvbase, infostr, post2FogExMessage);
                }

            }
            return parseNewResvRS;
        } catch (TimeOutException e) {
            log.error(infostr + post2FogExMessage + "    /n" + ExceptionUtils.getFullStackTrace(e));
            throw new TimeOut4FogException(e);
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            log.error(infostr + post2FogExMessage + "    /n" + ExceptionUtils.getFullStackTrace(e));
            throw new BizException(e);
        }

    }
    
    public String getOrderByOutNumber(String outNumber,String iata){
    	String requestXml = 
			"<crsmessage user=\"%1$s\" pass=\"%2$s\" msgtype=\"getresv\" version=\"1.2\">"+
				"<options detail=\"\"/>"+
				"<reservation>"+
					"<outconfnums>"+
						"<outconfnum>%3$s</outconfnum>"+
					"</outconfnums>"+
					"<miscinfo>"+
					"<IATA>%4$s</IATA>"+
					"</miscinfo>"+
				"</reservation>"+
			"</crsmessage>";
		requestXml = String.format(
				requestXml,	//要拼的xml
				fogUser,	//1
				fogPassword,	//2
				outNumber,	//3
				iata    //4
				);
		 Map parms = new HashMap();
         parms.put("Message", requestXml);
         String post2FogExMessage = "";
         try {
        	 post2FogExMessage = httpClientService.postHttpRequest(fogExUrl, parms);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return post2FogExMessage;
		
    }

    /**
     * @param cancelResvMsgtype the cancelResvMsgtype to set
     */
    public void setCancelResvMsgtype(String cancelResvMsgtype) {
        this.cancelResvMsgtype = cancelResvMsgtype;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public void setEresvMapDAO(EResvMapDAO eresvMapDAO) {
        this.eresvMapDAO = eresvMapDAO;
    }

    public void setFogExUrl(String fogExUrl) {
        this.fogExUrl = fogExUrl;
    }

    public void setFogLanguage(String fogLanguage) {
        this.fogLanguage = fogLanguage;
    }

    public void setFogPassword(String fogPassword) {
        this.fogPassword = fogPassword;
    }

    public void setFogServiceAvailWSClient(AvailWebService fogServiceAvailWSClient) {
        this.fogServiceAvailWSClient = fogServiceAvailWSClient;
    }

    public void setFogServiceWSClient(AvailExchangeRateWebService fogServiceWSClient) {
        this.fogServiceWSClient = fogServiceWSClient;
    }

    public void setFogUser(String fogUser) {
        this.fogUser = fogUser;
    }

    public void setHttpClientService(IHttpClientService httpClientService) {
        this.httpClientService = httpClientService;
    }

    /**
     * @param modResvMsgtype the modResvMsgtype to set
     */
    public void setModResvMsgtype(String modResvMsgtype) {
        this.modResvMsgtype = modResvMsgtype;
    }

    /**
     * @param needTrySpecResv the needTrySpecResv to set
     */
    public void setNeedTrySpecResv(boolean needTrySpecResv) {
        this.needTrySpecResv = needTrySpecResv;
    }

    public void setNewResvMsgtype(String newResvMsgtype) {
        this.newResvMsgtype = newResvMsgtype;
    }

	public ERatemapDAO getEratemapDAO() {
		return eratemapDAO;
	}

	public void setEratemapDAO(ERatemapDAO eratemapDAO) {
		this.eratemapDAO = eratemapDAO;
	}

}
