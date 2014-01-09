package com.thayer.idsservice.task.update.interf;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.thayer.fog2.bo.RateDataBO;
import com.thayer.fog2.entity.AvailAllow;
import com.thayer.idsservice.bean.mapping.MappingEnum;
import com.thayer.idsservice.bean.mapping.MappingResultBean;
import com.thayer.idsservice.dao.EIdsCache;
import com.thayer.idsservice.dao.EMsgLog;
import com.thayer.idsservice.dao.EMsgLogDAO;
import com.thayer.idsservice.dao.ERatemapDAO;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.exception.TimeOut4ThirdException;
import com.thayer.idsservice.exception.TimeOutException;
import com.thayer.idsservice.ids.expedia.update.service.ExpediaUploadService;
import com.thayer.idsservice.service.DbCacheFilterService;
import com.thayer.idsservice.service.interf.ICacheFilterService;
import com.thayer.idsservice.service.interf.ICallFogService;
import com.thayer.idsservice.service.interf.IHttpClientService;
import com.thayer.idsservice.service.interf.IMapService;
import com.thayer.idsservice.task.update.bean.AvailBean;
import com.thayer.idsservice.task.update.bean.ErrorBean;
import com.thayer.idsservice.task.update.bean.FogMQBean;
import com.thayer.idsservice.task.update.bean.ResultBean;
import com.thayer.idsservice.task.update.bean.RsStatusBean;
import com.thayer.idsservice.task.update.bean.SuccessBean;
import com.thayer.idsservice.task.update.bean.UpdateInfoBean;

/**
 * <B>Function :</B>rate,avail 更新服务抽象类 <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Zolt Zhang<br>
 * @since : 2010-9-15<br>
 * @version : v1.0
 */
public abstract class AbstractUploadService implements IUpdateService {

    private static Logger log = Logger.getLogger(AbstractUploadService.class);
    
    private static long cntSend = 1;
    private static long cntfilter = 0;

    private String thirdRequestXmlURL;// 更新代理的酒店可用性的URL

    private String requestXmlKey;// 请求postXml的key值
    
   

	private String ideType;// 是否开启缓存

    private ICacheFilterService cacheFilterService;

    private IMapService mapService;

    private IHttpClientService httpClientService;
    


	private DbCacheFilterService dbCacheFilterService;

    private ICallFogService callFogService;
    private EMsgLogDAO emsgLogDAO;
    private boolean update3LevelAvail = false; // 是否更新三级房量

    private ERatemapDAO eratemapDAO;
    
    public boolean isUpdate3LevelAvail() {
        return update3LevelAvail;
    }

    public void setUpdate3LevelAvail(boolean update3LevelAvail) {
        this.update3LevelAvail = update3LevelAvail;
    }

    public ICallFogService getCallFogService() {
        return callFogService;
    }

    public void setCallFogService(ICallFogService callFogService) {
        this.callFogService = callFogService;
    }

    public IMapService getMapService() {
        return mapService;
    }

    public void setMapService(IMapService mapService) {
        this.mapService = mapService;
    }

    public String getRequestXmlKey() {
        return requestXmlKey;
    }

    public void setRequestXmlKey(String requestXmlKey) {
        this.requestXmlKey = requestXmlKey;
    }

    /**
     * 根据缓存，过滤重读的更新内容
     * 
     * @param getzRateMapList
     * @return
     */
    // private List<RateDataBO> filterRateMap(List<RateDataBO> getzRateMapList, String thirdIataCode) {
    // List<RateDataBO> result = new ArrayList<RateDataBO>();
    // for (RateDataBO rateData : getzRateMapList) {
    // List<AvailAllow> rateList = rateData.getRateList();
    // RateDataBO resultRateData = new RateDataBO();
    // resultRateData.setPlan(rateData.getPlan());
    // resultRateData.setPlanCode(rateData.getPlanCode());
    // resultRateData.setProp(rateData.getProp());
    // resultRateData.setResvCxlId(rateData.getResvCxlId());
    // resultRateData.setResvCxlrule(rateData.getResvCxlrule());
    // resultRateData.setResvGuaId(rateData.getResvGuaId());
    // resultRateData.setResvGuarule(rateData.getResvGuarule());
    // resultRateData.setResvStatus(rateData.getResvStatus());
    // resultRateData.setRoom(rateData.getRoom());
    //
    // result.add(resultRateData);
    // List<AvailAllow> resultRateList = new ArrayList<AvailAllow>();
    // resultRateData.setRateList(resultRateList);
    // for (AvailAllow availAllow : rateList) {
    // String availDate = DateUtil.formatDate(availAllow.getAvailDate());
    // String key = thirdIataCode + "-" + rateData.getProp() + "-" + rateData.getRoom().getRoomId() + "-" +
    // availAllow.getRateCode() + "-" + availDate;
    // log.info("cache key : " + key);
    // AvailAllow cache = (AvailAllow) serviceCache.getCache(key);
    // if (cache == null) {
    // resultRateList.add(availAllow);
    // } else {
    // try {
    // // 房量
    // if (!cache.getAvaLevel().equalsIgnoreCase(availAllow.getAvaLevel())) {
    // resultRateList.add(availAllow);
    // continue;
    // }
    //
    // // 房量
    // if (cache.getAllotment() != availAllow.getAllotment()) {
    // resultRateList.add(availAllow);
    // continue;
    // }
    //
    // // 可用性
    // if (!cache.getAvail().equalsIgnoreCase(availAllow.getAvail())) {
    // resultRateList.add(availAllow);
    // continue;
    // }
    // // 最大入住天数
    // if (availAllow.getMaxLos() != cache.getMaxLos()) {
    // resultRateList.add(availAllow);
    // continue;
    // }
    // // 最小入住天数
    // if (availAllow.getMinLos() != cache.getMinLos()) {
    // resultRateList.add(availAllow);
    // continue;
    // }
    // // 提前入住天数
    // if (availAllow.getLeadTime() != cache.getLeadTime()) {
    // resultRateList.add(availAllow);
    // continue;
    // }
    //
    // // 强制满房 1: 强制满房
    // if (availAllow.getPr() != cache.getPr()) {
    // resultRateList.add(availAllow);
    // continue;
    // }
    //
    // // 房间限制 1: 不限量
    // if (!availAllow.getAllotmenitLimit().equalsIgnoreCase(cache.getAllotmenitLimit())) {
    // resultRateList.add(availAllow);
    // continue;
    // }
    //
    // AvailRate baseAvailRate = availAllow.getBaseAvailRate();
    // AvailRate cacheAvailRate = cache.getBaseAvailRate();
    //
    // // 单人价
    // if (baseAvailRate.getSingleRate() != cacheAvailRate.getSingleRate()) {
    // resultRateList.add(availAllow);
    // continue;
    // }
    // // 双人价
    // if (baseAvailRate.getDoubleRate() != cacheAvailRate.getDoubleRate()) {
    // resultRateList.add(availAllow);
    // continue;
    // }
    // // 三人价
    // if (baseAvailRate.getTripleRate() != cacheAvailRate.getTripleRate()) {
    // resultRateList.add(availAllow);
    // continue;
    // }
    // // 四人价
    // if (baseAvailRate.getQuadRate() != cacheAvailRate.getQuadRate()) {
    // resultRateList.add(availAllow);
    // continue;
    // }
    //
    // // 加床价
    // if (baseAvailRate.getExteraRate() != cacheAvailRate.getExteraRate()) {
    // resultRateList.add(availAllow);
    // continue;
    // }
    // // 早餐价
    // if (baseAvailRate.getBreakfastNum().intValue() != cacheAvailRate.getBreakfastNum().intValue()) {
    // resultRateList.add(availAllow);
    // continue;
    // }
    // } catch (Exception e) {
    // log.error(ExceptionUtils.getFullStackTrace(e));
    // resultRateList.add(availAllow);
    // continue;
    // }
    //
    // }
    // }
    //
    // }
    // return result;
    // }

    /**
     * 根据缓存，过滤重读的更新内容
     * 
     * @param getzRateMapList
     * @return
     */
    // private List<AvailAllow> filterAvailAllow(List<AvailAllow> availAllowList, String thirdIataCode) {
    // List<AvailAllow> result = new ArrayList<AvailAllow>();
    // for (AvailAllow availallow : availAllowList) {
    // String avaLevel = availallow.getAvaLevel();
    // AvailAllow cache = null;
    // // 酒店级
    // if ("A".equalsIgnoreCase(avaLevel)) {
    //
    // String availDate = DateUtil.formatDate(availallow.getAvailDate());
    // String key = thirdIataCode + "-" + availallow.getProp() + "-" + availDate;
    // cache = (AvailAllow) serviceCache.getCache(key);
    //
    // }
    // // 房型级
    // if ("B".equalsIgnoreCase(avaLevel)) {
    // String availDate = DateUtil.formatDate(availallow.getAvailDate());
    // String key = thirdIataCode + "-" + availallow.getProp() + "-" + availallow.getRoomCode() + "-" + availDate;
    // cache = (AvailAllow) serviceCache.getCache(key);
    // }
    //
    // if (cache == null) {
    // result.add(availallow);
    // } else {
    // try {
    // // 房量
    // if (cache.getAllotment() != availallow.getAllotment()) {
    // result.add(availallow);
    // }
    //
    // // 房量
    // if (!cache.getAvaLevel().equalsIgnoreCase(availallow.getAvaLevel())) {
    // result.add(availallow);
    // continue;
    // }
    //
    // // 可用性
    // if (!cache.getAvail().equalsIgnoreCase(availallow.getAvail())) {
    // result.add(availallow);
    // continue;
    // }
    // // 最大入住天数
    // if (availallow.getMaxLos() != cache.getMaxLos()) {
    // result.add(availallow);
    // continue;
    // }
    // // 最小入住天数
    // if (availallow.getMinLos() != cache.getMinLos()) {
    // result.add(availallow);
    // continue;
    // }
    // // 提前入住天数
    // if (availallow.getLeadTime() != cache.getLeadTime()) {
    // result.add(availallow);
    // continue;
    // }
    //
    // // 强制满房 1: 强制满房
    // if (availallow.getPr() != cache.getPr()) {
    // result.add(availallow);
    // continue;
    // }
    //
    // // 房间限制 1: 不限量
    // if (!availallow.getAllotmenitLimit().equalsIgnoreCase(cache.getAllotmenitLimit())) {
    // result.add(availallow);
    // continue;
    // }
    // } catch (Exception e) {
    // log.error(ExceptionUtils.getFullStackTrace(e));
    // result.add(availallow);
    // continue;
    // }
    //
    // }
    //
    // }
    // return result;
    // }
    
    
    /**
     * 相同性的比较
     * @param dist 缓存对象
     * @param src  比较对象
     */
    protected abstract boolean validateEq(EIdsCache dist,AvailAllow src,AvailBean srcAvailBean);
	
    /**
     * 星期数组转换成weeks字符串
     * @return
     */
    protected String weeksArrayToString(int [] weeks) {
    	String result ="1,2,3,4,5,6,7";
    	Map<String, String> m = new LinkedHashMap<String, String>();
    	if(weeks != null && weeks.length>0){
    		for(int week:weeks){
    			if(week>0 && week<8){
    				m.put(new Integer(week).toString(), "ok");
    			}
    		}
    		if(m == null || m.size()<=0){
    			return result;
    		}
    		StringBuilder sb = new StringBuilder();
    		Iterator<String> i = m.keySet().iterator();
    		while(i.hasNext()){
    			sb.append(i.next());
    			if (i.hasNext()) {
    				sb.append(",");
    			}
    			
    		}
    		result = sb.toString();
    	}
    	return result;
	}
    
    /**
     * 将list加入缓存列表（用于集合处理）
     * @param list
     * @param plan
     * @param prop
     * @param ideType
     * @param updateCacheList
     */
    protected void addAvailAllowListToCacheList(List<AvailAllow> list,AvailBean availBean, String plan,
			String prop, String ideType, List<EIdsCache> updateCacheList) {
		if(list== null || list.size()<=0){
			return;
		}
		for(AvailAllow bean:list){
			addAvailAllowToCacheList(bean, availBean,plan, prop, ideType, updateCacheList);
		}
		
	}
    
    
    /**
     * 将list加入缓存列表（用于单个对象处理）
     * @param bean
     * @param plan
     * @param prop
     * @param ideType
     * @param updateCacheList
     */
    protected abstract void addAvailAllowToCacheList(AvailAllow bean,AvailBean availBean,String plan,String prop,String ideType,List<EIdsCache> updateCacheList);
    
    /**
     * 判断是否进行mapping过滤
     * @param plan
     * @param prop
     * @return
     */
    private boolean checkByMapping(String plan,String prop){
    	boolean result = true;
    	if(!StringUtils.hasText(ideType)){
    		return false;
    	}
    	MappingResultBean mappingResultBean = null;
    	try {
    		//根据不同的ideType查询mapping
    		if("AGODA".equals(ideType)){
    			mappingResultBean = getMapService().getThirdHotelRateCodeByPlanLevel(
    					MappingEnum.AGODA, prop, plan);
    		}else if("BOOKINGCOM".equals(ideType)){
    			mappingResultBean = getMapService().getThirdHotelRateCodeByPlanLevel(
    					MappingEnum.BOOKINGCOM, prop, plan);
    		}else if("VENERE".equals(ideType)){
    			mappingResultBean = getMapService().getThirdHotelRateCodeByPlanLevel(
    					MappingEnum.VENERE, prop, plan);
    		}else if("EXPEDIA".equals(ideType)){
    			mappingResultBean = getMapService().getThirdHotelRateCodeByPlanLevel(
    					MappingEnum.VENERE, prop, plan);
    		}
    		
		}catch (MappingException e) {
			log.error("MapService read error", e);
		}
		//如果是AGODA,BOOKINGCOM,VENERE,EXPEDIA中的一个而且 mappingResultBean ==空 此条被过滤
		if(("AGODA".equals(ideType) || "BOOKINGCOM".equals(ideType) || "VENERE".equals(ideType) || "EXPEDIA".equals(ideType) )&&
				mappingResultBean == null){
			result = false;
		}
    	return result;
    }
    
    /**
     * 在结果集中过滤重复的数据（目前只用于rateList过滤）
     * @param bean 结果集
     * @param fogMQBean 声称该结果集的查询条件
     * @param updateCacheList 需要更新或插入的缓存列表
     * @return  过滤以后的结果集
     */
    private AvailBean filterEq(AvailBean bean,FogMQBean fogMQBean ,List<EIdsCache> updateCacheList){
    	if(bean == null || fogMQBean == null || updateCacheList == null || ideType == null || dbCacheFilterService == null){
    		return bean;
    	}
    	
    	if (org.apache.commons.lang.StringUtils.isEmpty(ideType)) {
    		return bean;
    	}
    	
    	
    	List<RateDataBO> rateDataBOList= bean.getRateDataBOList();
    	if(rateDataBOList == null || rateDataBOList.size()<=0){
    		return bean;
    	}
    	Map<String,EIdsCache> map = null;
    	String plan = null;
    	String prop = null;
    	List<AvailAllow> resTempAAList = null; 
    	int afterfilter = 0;
    	int availAllowSize = 0 ;
    	int  isAA =0;
    	for(RateDataBO tempRateDataBO:rateDataBOList){
    		map = null;
    		resTempAAList = new ArrayList<AvailAllow>();
    		isAA =0;
    		plan = tempRateDataBO.getPlanCode();
    		prop = tempRateDataBO.getProp();
    		//验证该渠道是否要过滤 false为过滤
    		if(!checkByMapping(plan, prop)){
    			continue;
    		}
    		
    		List<AvailAllow> availAllowList = tempRateDataBO.getRateList();
    		if(availAllowList == null ||availAllowList.size()<=0){
    			return bean;
    		}
    		
    		for(AvailAllow tempAvailAllow:availAllowList){
    			//获得 时间 段的缓存
    			if(map == null){
    				map = dbCacheFilterService.getMap(ideType, plan, prop, fogMQBean.getRateMQVO().getFromDate(), fogMQBean.getRateMQVO().getToDate());
    			}
    			//是否存在 缓存 (没缓存所有都要发送)
    			if(map.size() == 0){
    				//insert data to cache
    				addAvailAllowListToCacheList(availAllowList,bean, plan, prop, ideType, updateCacheList);
    				break;
    			} 
    			
    			EIdsCache cacheAvailallow = map.get(DbCacheFilterService.getKey(ideType, plan, prop, tempAvailAllow.getAvailDate()));
    			
    			if(cacheAvailallow != null){
    				 //验证内容是否一致
    				if(!validateEq(cacheAvailallow, tempAvailAllow,bean)){
    					resTempAAList.add(tempAvailAllow);
    					//update data to cache
    					addAvailAllowToCacheList(tempAvailAllow,bean, plan, prop, ideType, updateCacheList);
    				}
    			}else{
    				resTempAAList.add(tempAvailAllow);
    				addAvailAllowToCacheList(tempAvailAllow,bean, plan, prop, ideType, updateCacheList);
    			}
    			//记录是否经过上面验证
    			isAA ++;
    			
    		}
    		
    		availAllowSize += availAllowList.size();
    		//先注掉
//    		afterfilter += (resTempAAList==null?availAllowList.size():resTempAAList.size());
			
    		//在有验证的情况下要更新原数据
    		if(isAA>0 && resTempAAList!= null){
    			tempRateDataBO.setRateList(resTempAAList);
    		}
    	}
    	//需要更新到db的值就是未过滤的值(需要发送的值)
    	afterfilter += (updateCacheList==null?0:updateCacheList.size());
    	cntSend += availAllowSize;
    	cntfilter = cntfilter +(availAllowSize - afterfilter);
    	double filterpercent = cntfilter * 100.0d / cntSend;
    	log.info("DbCacheFilter:"+availAllowSize+"->"+afterfilter+" ["+filterpercent+"%]" );
    	return bean;
    }

    public void update(FogMQBean fogMQBean, ResultBean resultBean) throws BizException, TimeOut4ThirdException,
            MappingException {
    	long s1,s2;
        fogMQBean.setUpdate3LevelAvail(update3LevelAvail);

        log.debug("UpdateAbstractService begin!! ");
        List<ErrorBean> errorlist = resultBean.getErrorlist();
        List<SuccessBean> successlist = resultBean.getSuccesslist();
        AvailBean getzRateMapList = null;
        List<RateDataBO> updateRateMapList = new ArrayList<RateDataBO>();
        List<AvailAllow> availAllowList = new ArrayList<AvailAllow>();
        //dbcache 需要更新的集合
        List<EIdsCache> updateCacheList = new ArrayList<EIdsCache>();
        try {
            // 调用Fog获取更新内容
        	s1 = System.currentTimeMillis();
            getzRateMapList = callFogService.getzRateMapList(fogMQBean);
            s2 = System.currentTimeMillis();
            log.info("调试-获取FOG用时:" + (s2-s1));
            //dbcache 过滤结果集只有配了ideType才会起作用
            
            s1 = System.currentTimeMillis();
            getzRateMapList = filterEq(getzRateMapList, fogMQBean, updateCacheList);
            s2 = System.currentTimeMillis();
            log.info("调试-过滤缓存用时:" + (s2-s1));
            updateRateMapList = getzRateMapList.getRateDataBOList();

            if (update3LevelAvail) {
                availAllowList = getzRateMapList.getAvailAllowList();
            }

            getzRateMapList.setRateDataBOList(updateRateMapList);
            getzRateMapList.setAvailAllowList(availAllowList);
            if (fogMQBean.getRateMQVO().getWeeks() != null) {
                getzRateMapList.setWeeks(fogMQBean.getRateMQVO().getWeeks());
            } else { //WEEKS为空给默认值
            	getzRateMapList.setWeeks(new int[]{1,2,3,4,5,6,7});
            }
        } catch (BizException e) {
            log.error("Call fog service for rateMap info error: \n" + ExceptionUtils.getFullStackTrace(e));
            ErrorBean errorBean = new ErrorBean(); 
            errorBean.setErroCode("IDS-0011");
            errorBean.setErrorDesc(mapService.getAlertDesc("IDS-0011"));
            errorBean.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
            errorBean.setXml(fogMQBean.getRateMQVO().toString());
            errorlist.add(errorBean);
            throw new BizException(e);
        }

        List<UpdateInfoBean> updateInfoBeanList = null;
        try {
            updateInfoBeanList = getThirdRequestXml(getzRateMapList, errorlist);
        } catch (MappingException e1) {
            throw new MappingException(e1);
        }
        
        boolean isUpload = true;
        if (updateInfoBeanList != null) {
            for (UpdateInfoBean updateInfoBean : updateInfoBeanList) {
            	//key:消息+要更新的id信息
            	String cacheKey = cacheFilterService.getCacheKeyByMessage(fogMQBean) + "_" + updateInfoBean.getKey();
                try {
                    String updateResponseXml = "";
                    String type = updateInfoBean.getType();
                    String requestxml = updateInfoBean.getRequestXml();
                    String reqestURL = updateInfoBean.getReqestURL();
                    log.info("requestxml:"+requestxml);
                    
                    //dbcache 对配了ideType才会起作用，如果起作用就不要需要oscache的验证
                    if(org.apache.commons.lang.StringUtils.isBlank(ideType)){
                    	//2012-11-10 启用新的缓存规则 通过mq消息 提取key
                    	if (cacheFilterService.containsXml(cacheKey, requestxml,updateInfoBean.getType())) {
                    		log.info("cache containsXml:"+cacheKey+ requestxml);
                    		continue;
                    	}
//                    	if (cacheFilterService.containsXml(updateInfoBean.getType(), requestxml)) {
//                    		log.info("cache containsXml:"+ requestxml);
//                    		continue;
//                    	}
                    }
                    s1 = System.currentTimeMillis();
                    if (StringUtils.hasText(requestXmlKey)) {
                        Map parms = new HashMap();
                        parms.put(requestXmlKey, requestxml);
                        updateResponseXml = httpClientService.postHttpRequest(reqestURL, parms);
                    } else {
                        updateResponseXml = httpClientService.postHttpRequest(reqestURL, requestxml);
                    }
                    s2 = System.currentTimeMillis();
                    log.info("用时:" + (s2-s1));
                    log.info("updateResponseXml:" + updateResponseXml);
                    RsStatusBean rsStatusBean = parseResponseXml(updateResponseXml, type);
                    if (rsStatusBean.isRsResult()) {
                        log.info("update successfully! updateResponseXml: \n" + updateResponseXml);
                        
                        //dbcache 对配了ideType才会起作用，如果配了,不使用oscache
                        if(org.apache.commons.lang.StringUtils.isBlank(ideType)){
                        	//2012-11-10 启用新的缓存规则 通过mq消息 提取key
                        	cacheFilterService.addCache(cacheKey, requestxml,updateInfoBean.getType());
//                        	cacheFilterService.addCache(updateInfoBean.getType(), requestxml);
                        }
                    } else {
                    	isUpload = false;
//                        log.warn("update failed! \n" + updateResponseXml);
                        ErrorBean errorBean = new ErrorBean();
                        errorBean.setErroCode("IDS-0008");
                        errorBean.setErrorDesc(mapService.getAlertDesc("IDS-0008"));
                        errorBean.setErrorMessage(updateResponseXml);
                        errorBean.setXml(updateResponseXml);
                        errorBean.setMsgId(rsStatusBean.getRsMsgId());
                        errorlist.add(errorBean);
                    }
                    try {
                        EMsgLog eMsgLog = new EMsgLog();
                        eMsgLog.setCreatetime(new Date());
                        eMsgLog.setExwebCode(resultBean.getIata());
                        eMsgLog.setMsgRq(requestxml);
                        eMsgLog.setMsgRs(updateResponseXml);
                        eMsgLog.setUpdatetime(new Date());
                        eMsgLog.setMsgId(rsStatusBean.getRsMsgId());
                        eMsgLog.setMsgStatus(org.apache.commons.lang.StringUtils.lowerCase(rsStatusBean.getRsStatus()));
                        eMsgLog.setFogpropId(resultBean.getFogProp());
                        String hotelPropCode = mapService.getEXHotelCode(resultBean.getThirdIataCode(), resultBean
                                .getFogProp());
                        eMsgLog.setExpropId(hotelPropCode);
                        emsgLogDAO.attachDirty(eMsgLog);
                    } catch (Exception e) {
                        log.error(ExceptionUtils.getFullStackTrace(e));
                    }

                } catch (TimeOutException e) {
                    log.warn(ExceptionUtils.getFullStackTrace(e));
                    // ErrorBean errorBean = new ErrorBean();
                    // errorBean.setErroCode("IDS-0009");
                    // errorBean.setErrorDesc(mapService.getAlertDesc("IDS-0009"));
                    // errorBean.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
                    // errorBean.setXml(updateInfoBean.getRequestXml());
                    // errorlist.add(errorBean);
                    throw new TimeOut4ThirdException(e);

                } catch (Exception e) {
                    log.warn(ExceptionUtils.getFullStackTrace(e));
                    ErrorBean errorBean = new ErrorBean();
                    errorBean.setErroCode("IDS-0010");
                    errorBean.setErrorDesc(mapService.getAlertDesc("IDS-0010"));
                    errorBean.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
                    errorBean.setXml(updateInfoBean.getRequestXml());
                    errorlist.add(errorBean);
                    throw new BizException(e);
                }

            }
            //dbcache 对配了ideType才会起作用，如果配了 去更新在dbcache内不同的数据
            if(org.apache.commons.lang.StringUtils.isNotBlank(ideType)
               && updateInfoBeanList!=null && updateInfoBeanList.size()>0 && isUpload	
            ){
            	dbCacheFilterService.put(updateCacheList);
            }
            
            
        }
        log.debug("UpdateAbstractService end!! ");

    }

    public String getThirdRequestXmlURL() {
        return thirdRequestXmlURL;
    }

    public void setThirdRequestXmlURL(String thirdRequestXmlURL) {
        this.thirdRequestXmlURL = thirdRequestXmlURL;
    }

    /**
     * 根据FogMQBean 转换成请求代理更新酒店可用性的xml
     * 
     * @return
     * @throws BizException
     */
    public abstract List<UpdateInfoBean> getThirdRequestXml(AvailBean availBean, List<ErrorBean> errorlist)
            throws BizException, MappingException;

    /**
     * 解析thrid网站返回的结果xml 返回成功失败的结果
     * 
     * @param updateResponseXml
     * @return true update success; otherwise false
     */
    public abstract RsStatusBean parseResponseXml(String updateResponseXml, String type) throws BizException;

    public IHttpClientService getHttpClientService() {
        return httpClientService;
    }

    public void setHttpClientService(IHttpClientService httpClientService) {
        this.httpClientService = httpClientService;
    }

    public EMsgLogDAO getEmsgLogDAO() {
        return emsgLogDAO;
    }

    public void setEmsgLogDAO(EMsgLogDAO emsgLogDAO) {
        this.emsgLogDAO = emsgLogDAO;
    }

    /**
     * @return the cacheFilterService
     */
    public ICacheFilterService getCacheFilterService() {
        return cacheFilterService;
    }

    /**
     * @param cacheFilterService the cacheFilterService to set
     */
    public void setCacheFilterService(ICacheFilterService cacheFilterService) {
        this.cacheFilterService = cacheFilterService;
    }
    
    public String getIdeType() {
		return ideType;
	}

	public void setIdeType(String ideType) {
		this.ideType = ideType;
	}
	
    public DbCacheFilterService getDbCacheFilterService() {
		return dbCacheFilterService;
	}

	public void setDbCacheFilterService(DbCacheFilterService dbCacheFilterService) {
		this.dbCacheFilterService = dbCacheFilterService;
	}
	
	

	public ERatemapDAO getEratemapDAO() {
		return eratemapDAO;
	}

	public void setEratemapDAO(ERatemapDAO eratemapDAO) {
		this.eratemapDAO = eratemapDAO;
	}

	public static void main(String[] args) {
	    AbstractUploadService upload = new ExpediaUploadService();
	    int[] weeks= new int [] {1,8,1,2,5};
		String s = upload.weeksArrayToString(weeks);
		System.out.println(s);
    }
}
