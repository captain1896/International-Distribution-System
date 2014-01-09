package com.thayer.idsservice.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.thayer.idsservice.dao.EOrdermailInfo;
import com.thayer.idsservice.dao.EOrdermailInfoDAO;
import com.thayer.idsservice.dao.EResvMap;
import com.thayer.idsservice.dao.EResvMapDAO;
import com.thayer.idsservice.service.interf.IEOrdermailInfoService;
import com.thayer.idsservice.task.download.bean.ErrorBean;
import com.thayer.idsservice.task.download.bean.SuccessBean;

public class EOrdermailInfoService implements IEOrdermailInfoService{
	private static Logger log = Logger.getLogger(EOrdermailInfoService.class);
	private EOrdermailInfoDAO eordermailInfoDAO;
	private EResvMapDAO eresvMapDAO;
	
	@Override
	public void saveSuccessMailInfo(SuccessBean successBean,String thirdIataCode) {
		try {
			EOrdermailInfo mailInfo = new EOrdermailInfo();
			mailInfo.setCnfNum(successBean.getCnfnum());
			mailInfo.setOutcnfNum(successBean.gettResvBase().getOutcnfnum());
			mailInfo.setResvType(successBean.gettResvBase().getResvtype());
			mailInfo.setProp(successBean.getFogProp()==null?successBean.gettResvBase().getProp():successBean.getFogProp());
			mailInfo.setBookedTime(successBean.gettResvBase().getBookeddate()==null?new Date():successBean.gettResvBase().getBookeddate());
			mailInfo.setInDate(successBean.gettResvBase().getIndate());
			mailInfo.setIdsType(thirdIataCode);
			mailInfo.setResult("s");
			//如果不是新单的话，就去数据库里取入住时间和下单时间
			if(!"n".equals(successBean.gettResvBase().getResvtype())){
				List<EResvMap> oldOrder = eresvMapDAO.findByAgodaCnfnum(successBean.gettResvBase().getOutcnfnum());
				if(oldOrder !=null && oldOrder.size()>0){
					mailInfo.setBookedTime(oldOrder.get(0).getCreatedate());//创建订单时间
					mailInfo.setInDate(oldOrder.get(0).getBk3());//入住时间
				}
			}
			
			eordermailInfoDAO.save(mailInfo);
		} catch (Exception e) {
			log.error("增加订单邮件出错（success）："+successBean.gettResvBase().getOutcnfnum(),e);
		}
		
	}

	@Override
	public void saveFailMailInfo(ErrorBean errorBean,String thirdIataCode) {
		try {
			if(errorBean.gettResvBase() == null){
				return;
			}
			EOrdermailInfo mailInfo = new EOrdermailInfo();
			mailInfo.setOutcnfNum(errorBean.gettResvBase().getOutcnfnum());
			mailInfo.setResvType(errorBean.gettResvBase().getResvtype());
			mailInfo.setProp(errorBean.getFogProp()==null?errorBean.gettResvBase().getProp():errorBean.getFogProp());
			mailInfo.setBookedTime(errorBean.gettResvBase().getBookeddate()==null?new Date():errorBean.gettResvBase().getBookeddate());
			mailInfo.setInDate(errorBean.gettResvBase().getIndate());
			mailInfo.setIdsType(thirdIataCode);
			mailInfo.setResult("f");
			mailInfo.setErrCode(errorBean.getErroCode());
			mailInfo.setErrMsg(errorBean.getErrorDesc());
			//如果不是新单的话，就去数据库里取入住时间和下单时间
			if(!"n".equals(errorBean.gettResvBase().getResvtype())){
				List<EResvMap> oldOrder = eresvMapDAO.findByAgodaCnfnum(errorBean.gettResvBase().getOutcnfnum());
				if(oldOrder !=null && oldOrder.size()>0){
					mailInfo.setBookedTime(oldOrder.get(0).getCreatedate());//创建订单时间
					mailInfo.setInDate(oldOrder.get(0).getBk3());//入住时间
				}
			}
			
			
			eordermailInfoDAO.save(mailInfo);
		} catch (Exception e) {
			log.error("增加订单邮件出错（fail）："+errorBean.gettResvBase().getOutcnfnum(),e);
		}
	}

	public EOrdermailInfoDAO getEordermailInfoDAO() {
		return eordermailInfoDAO;
	}

	public void setEordermailInfoDAO(EOrdermailInfoDAO eordermailInfoDAO) {
		this.eordermailInfoDAO = eordermailInfoDAO;
	}

	public EResvMapDAO getEresvMapDAO() {
		return eresvMapDAO;
	}

	public void setEresvMapDAO(EResvMapDAO eresvMapDAO) {
		this.eresvMapDAO = eresvMapDAO;
	}
	

	
	
}
