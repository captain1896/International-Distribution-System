package com.thayer.idsservice.task.check;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.thayer.idsservice.dao.EPropmap;
import com.thayer.idsservice.dao.EPropmapDAO;
import com.thayer.idsservice.dao.EResvMap;
import com.thayer.idsservice.dao.EResvMapDAO;
import com.thayer.idsservice.ids.expedia.download.service.ExpediaResvCallbackService;
import com.thayer.idsservice.service.interf.ICallFogService;
import com.thayer.idsservice.task.Thread.ExpediaResultFaildThread;
import com.thayer.idsservice.task.download.bean.SuccessBean;
import com.thayer.idsservice.task.download.bean.TResvBase;
import com.thayer.idsservice.task.download.interf.IResvCallbackService;

public class ExpediaFaildResultTask extends QuartzJobBean{
	private static Logger LOGGER = Logger.getLogger(ExpediaFaildResultTask.class);
	private String iata;
	
	private EResvMapDAO eresvMapDAO;
	private EPropmapDAO epropmapDAO;
	/**
	 * 订单信息回传服务
	 */
	private IResvCallbackService resvCallbackService;
	
	private ICallFogService callFogService;
	
	/**
	 * 处理返回结果失败的线程池
	 */
	private ExecutorService faildResultThreadPool = Executors.newFixedThreadPool(5);
	@Override
	public void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		LOGGER.info("检测Expedia未返回的订单..");
		//拉取没有订单号订单的Expedia订单
		List<EResvMap> expediaNoFogNumberList = eresvMapDAO.getNoFogNumByIata("EXPEDIA");
		
		//如果没有数据 就不处理
		if(expediaNoFogNumberList == null || expediaNoFogNumberList.isEmpty()){
			return;
		}
		
		
		//进行推送
		for (EResvMap eResvMap : expediaNoFogNumberList) {
			LOGGER.info("处理Expedia未返回的订单：" + eResvMap.getAgodaCnfnum());
			//查询FOG订单号
			String orderXml = callFogService.getOrderByOutNumber(eResvMap.getAgodaCnfnum(),iata);
			String fogNumber = this.getXmlValueByNode(orderXml, "confnum");
			if(fogNumber == null || fogNumber.trim().length() ==0){
				continue;
			}
			String fogProp =  this.getXmlValueByNode(orderXml, "PropID");
			String orderType = this.getXmlValueByNode(orderXml, "Status");//New Edit Cxl
			//根据酒店ID获取Expedia的账号密码
			EPropmap instance = new EPropmap();
			instance.setFogpropId(fogProp);
			instance.setExwebCode("960002");
			List<EPropmap> epropmaps = epropmapDAO.findByExample(instance);
			
			if(epropmaps == null || epropmaps.isEmpty()){
				LOGGER.error(fogProp+":Expedia没有对应酒店映射");
				continue;
			}
			EPropmap epropmap = epropmaps.get(0);
			
			SuccessBean successbean = new SuccessBean();
			successbean.setCnfnum(fogNumber);
			successbean.setIds_cnfnum(eResvMap.getAgodaCnfnum());
			successbean.setUsername(epropmap.getExusername());
			successbean.setPassword(epropmap.getExpassword());
			successbean.setIdsProp(epropmap.getExpropId());
			TResvBase tResvBase = new TResvBase();
			if("New".equals(orderType)){
				tResvBase.setResvtype("n");
			}else if("Edit".equals(orderType)){
				tResvBase.setResvtype("e");
			}else if("Cxl".equals(orderType)){
				tResvBase.setResvtype("c");
			}
			successbean.settResvBase(tResvBase);
			
			boolean success = resvCallbackService.resvCallback(successbean);
			if(!success){
				//如果上传失败 就启动线程池 进行重传
				faildResultThreadPool.execute(new ExpediaResultFaildThread(resvCallbackService,successbean));
			}
			
			//更新到数据库中
			eResvMap.setFogCnfnum(fogNumber);
			eresvMapDAO.merge(eResvMap);
		}
	}

	/**
	 * 通过XML的节点 得到值
	 * 注：如果有重复节点的话 不适用此方法
	 * @date 2013-2-20
	 * @param xml
	 * @param node
	 * @return
	 */
	private String getXmlValueByNode(String xml,String node){
		if(xml == null || xml.isEmpty()){
			return null;
		}
		
		String start = "<" + node + ">";
		String end = "</" + node + ">";
		
		int startIndex = xml.indexOf(start);
		int endIndex = xml.indexOf(end);
		
		if(startIndex < 0 || endIndex < 0){
			return null;
		}
		
		return StringUtils.trim(xml.substring(startIndex+start.length(), endIndex));
	}
	
	public EResvMapDAO getEresvMapDAO() {
		return eresvMapDAO;
	}
	public void setEresvMapDAO(EResvMapDAO eresvMapDAO) {
		this.eresvMapDAO = eresvMapDAO;
	}
	public IResvCallbackService getResvCallbackService() {
		return resvCallbackService;
	}
	public void setResvCallbackService(IResvCallbackService resvCallbackService) {
		this.resvCallbackService = resvCallbackService;
	}
	public ICallFogService getCallFogService() {
		return callFogService;
	}
	public void setCallFogService(ICallFogService callFogService) {
		this.callFogService = callFogService;
	}

	public String getIata() {
		return iata;
	}

	public void setIata(String iata) {
		this.iata = iata;
	}

	public EPropmapDAO getEpropmapDAO() {
		return epropmapDAO;
	}

	public void setEpropmapDAO(EPropmapDAO epropmapDAO) {
		this.epropmapDAO = epropmapDAO;
	}

	public ExecutorService getFaildResultThreadPool() {
		return faildResultThreadPool;
	}

	public void setFaildResultThreadPool(ExecutorService faildResultThreadPool) {
		this.faildResultThreadPool = faildResultThreadPool;
	}
	
	
}
