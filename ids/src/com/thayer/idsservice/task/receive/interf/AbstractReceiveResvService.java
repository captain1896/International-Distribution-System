package com.thayer.idsservice.task.receive.interf;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.thayer.idsservice.bean.MailFogResvBaseBean;
import com.thayer.idsservice.dao.EResvMap;
import com.thayer.idsservice.dao.EResvMapDAO;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.exception.TimeOut4FogException;
import com.thayer.idsservice.service.interf.ICallFogService;
import com.thayer.idsservice.service.interf.IMapService;
import com.thayer.idsservice.task.download.bean.ErrorBean;
import com.thayer.idsservice.task.download.bean.ResultBean;
import com.thayer.idsservice.task.download.bean.SuccessBean;
import com.thayer.idsservice.task.download.bean.TResvBase;
import com.thayer.idsservice.util.DateUtil;

public abstract class AbstractReceiveResvService implements IReceiveResvService {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4394534406411737262L;

	protected Logger LOGGER = Logger.getLogger(AbstractReceiveResvService.class);

	private EResvMapDAO eresvMapDAO;

	private ICallFogService callFogService;

	private IMapService mapService;

	private String iata;

	public String getIata() {
		return iata;
	}

	public void setIata(String iata) {
		this.iata = iata;
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

	public IMapService getMapService() {
		return mapService;
	}

	public void setMapService(IMapService mapService) {
		this.mapService = mapService;
	}

	/**
	 * 是否需要验证订单的每日价。默认:true
	 */
	private boolean validateResvRate = true;

	public boolean isValidateResvRate() {
		return validateResvRate;
	}

	public void setValidateResvRate(boolean validateResvRate) {
		this.validateResvRate = validateResvRate;
	}

	public ICallFogService getCallFogService() {
		return callFogService;
	}

	public void setCallFogService(ICallFogService callFogService) {
		this.callFogService = callFogService;
	}

	public EResvMapDAO getEresvMapDAO() {
		return eresvMapDAO;
	}

	public void setEresvMapDAO(EResvMapDAO eresvMapDAO) {
		this.eresvMapDAO = eresvMapDAO;
	}

	@Override
	public String downloadResv(String requestXml) {
		try {
			ResultBean resultBean = new ResultBean();
			resultBean.setIata(iata);
			resultBean.setThirdIataCode(thirdIataCode);
			resultBean.setCreateDate(DateUtil.formatDate(new Date()));
			MailFogResvBaseBean result = null;
			TResvBase fogbean = convertXml4fogbean(requestXml);
			if (fogbean != null) {
				result = saveResvToFog(resultBean, fogbean, requestXml);
			}
			return buildRSXml(result, requestXml);
		} catch (TimeOut4FogException e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			return returnErrorMsg(e);
		} catch (BizException e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			return returnErrorMsg(e);
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			return returnErrorMsg(e);
		}

	}

	/**
	 * 返回错误信息
	 * 
	 * @param e
	 * @return
	 */
	public abstract String returnErrorMsg(Throwable e);

	/**
	 * 返回成功信息
	 * 
	 * @param saveFogNewResvBean
	 * @param requestXml
	 * @return
	 */
	public abstract String buildRSXml(MailFogResvBaseBean saveFogNewResvBean, String requestXml);

	/**
	 * 转换为fogbean对象
	 * 
	 * @param responseXml
	 * @return
	 * @throws BizException
	 * @throws MappingException
	 */
	public abstract TResvBase convertXml4fogbean(String xml) throws BizException, MappingException;

	/**
	 * @param errorlist
	 * @param successlist
	 * @param tresvBase
	 * @throws TimeOut4FogException
	 */
	private MailFogResvBaseBean saveResvToFog(ResultBean resultBean, TResvBase tresvBase, String requestXml)
			throws TimeOut4FogException, BizException {
		List<ErrorBean> errorlist = resultBean.getErrorlist();
		List<SuccessBean> successlist = resultBean.getSuccesslist();
		String fogProp = tresvBase.getProp();
		String idsProp = tresvBase.getIdsProp();
		MailFogResvBaseBean saveFogNewResvBean = null;
		try {
			saveFogNewResvBean = callFogService.saveFogNewResvBean(tresvBase, validateResvRate);
			String cnfnum = saveFogNewResvBean.getCnfnum();
			SuccessBean successbean = new SuccessBean();
			successbean.settResvBase(tresvBase);
			successbean.setCnfnum(cnfnum);
			successbean.setFogProp(fogProp);
			successbean.setIdsProp(idsProp);
			successbean.setIds_cnfnum(tresvBase.getOutcnfnum());
			saveFogNewResvBean.setIata(resultBean.getIata());
			saveFogNewResvBean.setIataName(resultBean.getIataName());
			saveFogNewResvBean.setIndate(DateUtil.formatDate(tresvBase.getIndate()));
			saveFogNewResvBean.setOutdate(DateUtil.formatDate(tresvBase.getOutdate()));
			successbean.setMailFogResvBaseBean(saveFogNewResvBean);
			successlist.add(successbean);
			try {
				/**
				 * 保存成功 加订单
				 */
				EResvMap eResvMap = new EResvMap();
				eResvMap.setAgodaCnfnum(tresvBase.getOutcnfnum());
				List<EResvMap> eresvMapList = getEresvMapDAO().findByExample(eResvMap);
				if (eresvMapList != null && eresvMapList.size() > 0) {
					EResvMap eresvmap = eresvMapList.get(0);
					eresvmap.setFogCnfnum(cnfnum);
					eresvmap.setBk6(tresvBase.getIdsStatus());
					eresvMapDAO.attachDirty(eresvmap);
				} else {
					EResvMap eresvmap = new EResvMap();
					eresvmap.setAgodaCnfnum(tresvBase.getOutcnfnum());
					eresvmap.setCreatedate(new Date());
					eresvmap.setFogCnfnum(cnfnum);
					eresvmap.setRequesteddate(DateUtil.formatDate(new Date()));
					eresvmap.setBk5(resultBean.getThirdIataCode());
					eresvmap.setBk6(tresvBase.getIdsStatus());
					eresvMapDAO.attachDirty(eresvmap);
				}
			} catch (Exception e) {
				LOGGER.error(ExceptionUtils.getFullStackTrace(e));
				ErrorBean err = new ErrorBean();
				err.setFogProp(fogProp);
				err.setIdsProp(idsProp);
				err.setErroCode("IDS-0013");
				err.setErrorDesc("IDS-Alert: 订单下载成功！保存订单映射关系失败。");
				err.setIds_cnfnum(tresvBase.getOutcnfnum());
				err.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
				err.setXml(requestXml);
				err.settResvBase(tresvBase);
				errorlist.add(err);
			}
			LOGGER.debug("保存订单Cnfnum:" + cnfnum + "成功！");
			return saveFogNewResvBean;
		} catch (TimeOut4FogException e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			ErrorBean err = new ErrorBean();
			err.setFogProp(fogProp);
			err.setIdsProp(idsProp);
			err.setErroCode("IDS-0005");
			err.setErrorDesc("IDS-Alert: 调用FOG Service预订服务超时.");
			err.setIds_cnfnum(tresvBase.getOutcnfnum());
			err.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
			err.settResvBase(tresvBase);
			err.setXml(requestXml);
			errorlist.add(err);
			throw e;
		} catch (BizException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			ErrorBean err = new ErrorBean();
			err.setFogProp(fogProp);
			err.setIdsProp(idsProp);
			err.setErroCode("IDS-0006");
			err.setErrorDesc("IDS-Alert: 调用FOG Service预订服务失败.");
			err.setIds_cnfnum(tresvBase.getOutcnfnum());
			err.setErrorMessage(ExceptionUtils.getFullStackTrace(e));
			err.setXml(requestXml);
			err.settResvBase(tresvBase);
			errorlist.add(err);
			throw new BizException("调用FOG Service预订服务失败");
		}
	}
}
