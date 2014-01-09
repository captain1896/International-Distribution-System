package com.thayer.idsservice.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.thayer.idsservice.dao.EIdsCache;
import com.thayer.idsservice.dao.EIdsCacheDAO;
import com.thayer.idsservice.dao.EIdsCacheKey;
import com.thayer.idsservice.util.DateUtil;

public class DbCacheFilterService {
	private EIdsCacheDAO eIdsCacheDAO;
	
	public List<EIdsCache> getList(String idsType, String planCode,String prop,Date begin,Date end){
		return eIdsCacheDAO.findByProperty(idsType, planCode, prop, begin, end);
	}
	
	public Map<String,EIdsCache> getMap(String idsType, String planCode,String prop,Date begin,Date end){
		Map<String,EIdsCache> result = new HashMap<String, EIdsCache>();
		List<EIdsCache> list = eIdsCacheDAO.findByProperty(idsType, planCode, prop, begin, end);
		for(EIdsCache temp:list){
			result.put(DbCacheFilterService.getKey(idsType, planCode, prop, temp.geteIdsCacheKey().getAvailDate()), temp);
		}
		return result;
	}
	
	public void put(EIdsCacheKey key,EIdsCache value){
		EIdsCache eidsCache = eIdsCacheDAO.findByPK(key.getIdsType(), key.getPlanCode(), key.getProp(), key.getAvailDate());
		if(eidsCache != null){
			value.setCreateDate(eidsCache.getCreateDate());
			eIdsCacheDAO.update(value);
		}else{
			eIdsCacheDAO.save(value);
		}
	}
	
	public void put(List<EIdsCache> values){
		if(values == null || values.size()<=0){
			return;
		}
		EIdsCache tempEIdsCache = null;
		for(EIdsCache value:values){
			tempEIdsCache = eIdsCacheDAO.findByPK(value.geteIdsCacheKey().getIdsType(), value.geteIdsCacheKey().getPlanCode(), value.geteIdsCacheKey().getProp(), value.geteIdsCacheKey().getAvailDate());
			if(tempEIdsCache != null){
				value.setCreateDate(tempEIdsCache.getCreateDate());
				eIdsCacheDAO.update(value);
			}else{
				eIdsCacheDAO.save(value);
			}
			
		}
	}
	
	
	
	public static String  getKey(String idsType, String planCode,String prop,Date availDate){
		StringBuilder sb = new StringBuilder();
		if(StringUtils.isBlank(idsType) || StringUtils.isBlank(planCode)|| StringUtils.isBlank(prop) || availDate== null){
			return sb.toString();
		}
		sb.append(idsType+"#");
		sb.append(planCode+"#");
		sb.append(prop+"#");
		sb.append(DateUtil.formatDate(availDate));
		return sb.toString();
		
	}
	
	
	

	public EIdsCacheDAO geteIdsCacheDAO() {
		return eIdsCacheDAO;
	}

	public void seteIdsCacheDAO(EIdsCacheDAO eIdsCacheDAO) {
		this.eIdsCacheDAO = eIdsCacheDAO;
	}
	

}
