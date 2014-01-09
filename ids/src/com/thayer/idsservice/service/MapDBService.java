/*****************************************************************<br>
 * <B>FILE :</B> MapDBService.java <br>
 * <B>CREATE DATE :</B> 2010-10-25 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import com.thayer.idsservice.bean.mapping.MappingResultBean;
import com.thayer.idsservice.dao.EExwebinfo;
import com.thayer.idsservice.dao.EExwebinfoDAO;
import com.thayer.idsservice.dao.EPropmap;
import com.thayer.idsservice.dao.EPropmapDAO;
import com.thayer.idsservice.dao.ERatemap;
import com.thayer.idsservice.dao.ERatemapDAO;
import com.thayer.idsservice.dao.ERoommap;
import com.thayer.idsservice.dao.ERoommapDAO;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.service.interf.IMapService;
import com.thayer.idsservice.task.download.bean.CnfnumBean;
import com.thayer.idsservice.task.download.bean.HotelBean;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-10-25<br>
 * @version : v1.0
 */
public class MapDBService implements IMapService {
	private EPropmapDAO epropmapDAO;

	private ERoommapDAO eroommapDAO;

	private ERatemapDAO eratemapDAO;

	private EExwebinfoDAO eexwebinfoDAO;
	private static final Logger logger = Logger.getLogger(MapDBService.class);
	private static long lastModified = 0;

	private static Map<String, String> alertMap = new HashMap<String, String>();

	public MapDBService() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.IMapService#getCnfnumMapByThirdCnfnum(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public CnfnumBean getCnfnumMapByThirdCnfnum(String thirdIataCode, String thirdCnfnum) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.IMapService#getHotelMapList(java.lang.String)
	 */
	@Override
	public List<HotelBean> getHotelMapList(String thirdIataCode) throws MappingException {
		try {
			List<HotelBean> hotelBeanlist = new ArrayList<HotelBean>();
			List result = eexwebinfoDAO.findByExwebCode(thirdIataCode);
			if (result.size() > 0) {
				EExwebinfo exwebInfo = (EExwebinfo) result.get(0);
				BigDecimal iata = exwebInfo.getKeyid();
				List<EPropmap> propList = epropmapDAO.findByExwebCode(iata.toString());
				for (EPropmap ePropmap : propList) {
					HotelBean hotelBean = new HotelBean();
					hotelBean.setFogHotelCode(ePropmap.getFogpropId());
					hotelBean.setThirdHotelCode(ePropmap.getExpropId());
					hotelBean.setThirdIataCode(thirdIataCode);
					hotelBean.setFogIataCode(iata.toString());
					String exusername = ePropmap.getExusername();
					String expassword = ePropmap.getExpassword();
					hotelBean.setUsername(exusername);
					hotelBean.setPassword(expassword);
					hotelBeanlist.add(hotelBean);
				}
			}
			return hotelBeanlist;
		} catch (Exception e) {
			throw new MappingException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.IMapService#getRoomRateMap(java.lang.String, java.lang.String,
	 * java.lang.Enum, java.lang.String)
	 */
	@Override
	public String getRoomRateMap(String exrateId, String fogPropId, Enum thirdIataCode, String type)
			throws MappingException {
		try {
			List<EExwebinfo> result = eexwebinfoDAO.findByExwebCode(thirdIataCode.toString());
			String iata = "";
			if (result.size() > 0) {
				EExwebinfo eexwebinfo = result.get(0);
				iata = eexwebinfo.getKeyid().toString();

			}

			if ("roomType".equalsIgnoreCase(type)) {
				ERoommap eroommap = new ERoommap();
				eroommap.setExwebCode(iata);
				eroommap.setFogpropId(fogPropId);
				eroommap.setExroomId(exrateId);
				List<ERoommap> eroommapResult = eroommapDAO.findByExample(eroommap);
				if (eroommapResult.size() > 0) {
					ERoommap eR = eroommapResult.get(0);
					return eR.getFogroomId();
				}
				return null;
			} else if ("rateType".equalsIgnoreCase(type)) {
				String[] split = exrateId.split("#");
				if (split.length == 2) {
					ERoommap eroommap = new ERoommap();
					eroommap.setExwebCode(iata);
					eroommap.setFogpropId(fogPropId);
					eroommap.setExroomId(split[0]);
					List<ERoommap> eroommapResult = eroommapDAO.findByExample(eroommap);
					if (eroommapResult.size() > 0) {
						ERoommap eR = eroommapResult.get(0);
						List<ERatemap> eratemapresult = eratemapDAO.findByPara(iata, split[1], fogPropId, eR
								.getFogroomId());
						if (eratemapresult.size() > 0) {
							ERatemap eRatemaprs = eratemapresult.get(0);
							String fograteId = eRatemaprs.getFograteId();
							String[] split2 = fograteId.split("-");
							if (split2.length == 2) {
								return split2[1];
							}

						}
					}

				}
			}
			return null;
		} catch (Exception e) {
			throw new MappingException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.IMapService#getThirdHotelRateCodeByPlanLevel(java.lang.Enum,
	 * java.lang.String, java.lang.String)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.IMapService#getThirdHotelRateCodeByPlanLevel(java.lang.Enum,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public MappingResultBean getThirdHotelRateCodeByPlanLevel(Enum type, String fogProp, String planCode)
			throws MappingException {
		try {
			List result = eexwebinfoDAO.findByExwebCode(type.toString());
			String fogRoomCode = "";

			String[] plansplit = planCode.split("-");
			if (plansplit.length != 2) {
				return null;
			} else {
				fogRoomCode = plansplit[0];
			}
			if (result.size() > 0) {
				MappingResultBean hotelBean = new MappingResultBean();
				EExwebinfo exwebInfo = (EExwebinfo) result.get(0);
				BigDecimal iata = exwebInfo.getKeyid();
				EPropmap epropmap = new EPropmap();
				epropmap.setExwebCode(iata.toString());
				epropmap.setFogpropId(fogProp);
				List<EPropmap> proplist = epropmapDAO.findByExample(epropmap);
				if (proplist.size() > 0) {
					EPropmap ePropmap = proplist.get(0);

					hotelBean.setFogProp(ePropmap.getFogpropId());
					hotelBean.setHotelCode(ePropmap.getExpropId());
					hotelBean.setPassword(ePropmap.getExpassword());
					hotelBean.setUserId(ePropmap.getExusername());

				} else {
					return null;
				}

				ERoommap eroommap = new ERoommap();
				eroommap.setExwebCode(iata.toString());
				eroommap.setFogpropId(fogProp);
				eroommap.setFogroomId(plansplit[0]);
				List<ERoommap> eroommapResult = eroommapDAO.findByExample(eroommap);
				if (eroommapResult.size() > 0) {
					ERoommap eRoommaprs = eroommapResult.get(0);
					hotelBean.setFogRoomCode(fogRoomCode);
					hotelBean.setHotelRoomCode(eRoommaprs.getExroomId());
				} else {
					return null;
				}

				ERatemap eratemap = new ERatemap();
				eratemap.setFogpropId(fogProp);
				eratemap.setExwebCode(iata.toString());
				eratemap.setFograteId(planCode);
				List<ERatemap> eratemapresult = eratemapDAO.findByExample(eratemap);
				if (eratemapresult.size() > 0) {
					ERatemap eRatemaprs = eratemapresult.get(0);
					hotelBean.setFogRateCode(plansplit[1]);
					hotelBean.setHotelRateCode(eRatemaprs.getExrateId());
				} else {
					return null;
				}
				return hotelBean;
			}
			return null;
		} catch (Exception e) {
			throw new MappingException(e);
		}
	}

	public List<MappingResultBean> getThirdHotelRateCodeByPlanLevel4Expedia(Enum type, String fogProp, String planCode)
			throws MappingException {
		try {
			List<EExwebinfo> result = eexwebinfoDAO.findByExwebCode(type.toString());
			String fogRoomCode = "";

			String[] plansplit = planCode.split("-");
			if (plansplit.length != 2) {
				return null;
			} else {
				fogRoomCode = plansplit[0];
			}
			List<MappingResultBean> hotelBeanList = new ArrayList<MappingResultBean>();
			for (EExwebinfo exwebInfo : result) {
				BigDecimal iata = exwebInfo.getKeyid();
				ERatemap eratemap = new ERatemap();
				eratemap.setFogpropId(fogProp);
				eratemap.setExwebCode(iata.toString());
				eratemap.setFograteId(planCode);
				List<ERatemap> eratemapresult = eratemapDAO.findByExample(eratemap);
				for (ERatemap eRatemaprs : eratemapresult) {

					MappingResultBean hotelBean = new MappingResultBean();

					EPropmap epropmap = new EPropmap();
					epropmap.setExwebCode(iata.toString());
					epropmap.setFogpropId(fogProp);
					List<EPropmap> proplist = epropmapDAO.findByExample(epropmap);
					if (proplist.size() > 0) {
						EPropmap ePropmap = proplist.get(0);
						hotelBean.setFogProp(ePropmap.getFogpropId());
						hotelBean.setHotelCode(ePropmap.getExpropId());
						hotelBean.setPassword(ePropmap.getExpassword());
						hotelBean.setUserId(ePropmap.getExusername());
					} else {
						return null;
					}

					ERoommap eroommap = new ERoommap();
					eroommap.setExwebCode(iata.toString());
					eroommap.setFogpropId(fogProp);
					eroommap.setFogroomId(plansplit[0]);
					List<ERoommap> eroommapResult = eroommapDAO.findByExample(eroommap);
					if (eroommapResult.size() > 0) {
						ERoommap eRoommaprs = eroommapResult.get(0);
						hotelBean.setFogRoomCode(fogRoomCode);
						hotelBean.setHotelRoomCode(eRoommaprs.getExroomId());
					} else {
						return null;
					}
					hotelBean.setFogRateCode(plansplit[1]);
					hotelBean.setHotelRateCode(eRatemaprs.getExrateId());
					hotelBeanList.add(hotelBean);
				}

			}

			return hotelBeanList;
		} catch (Exception e) {
			throw new MappingException(e);
		}
	}

	public List<MappingResultBean> getThirdHotelRateCodeByPlanLevel4Venere(Enum type, String fogProp, String planCode)
			throws MappingException {
		try {
			List<EExwebinfo> result = eexwebinfoDAO.findByExwebCode(type.toString());
			String fogRoomCode = "";

			String[] plansplit = planCode.split("-");
			if (plansplit.length != 2) {
				return null;
			} else {
				fogRoomCode = plansplit[0];
			}
			List<MappingResultBean> hotelBeanList = new ArrayList<MappingResultBean>();
			for (EExwebinfo exwebInfo : result) {
				BigDecimal iata = exwebInfo.getKeyid();
				ERatemap eratemap = new ERatemap();
				eratemap.setFogpropId(fogProp);
				eratemap.setExwebCode(iata.toString());
				eratemap.setFograteId(planCode);
				List<ERatemap> eratemapresult = eratemapDAO.findByExample(eratemap);
				for (ERatemap eRatemaprs : eratemapresult) {

					MappingResultBean hotelBean = new MappingResultBean();

					EPropmap epropmap = new EPropmap();
					epropmap.setExwebCode(iata.toString());
					epropmap.setFogpropId(fogProp);
					List<EPropmap> proplist = epropmapDAO.findByExample(epropmap);
					if (proplist.size() > 0) {
						EPropmap ePropmap = proplist.get(0);
						hotelBean.setFogProp(ePropmap.getFogpropId());
						hotelBean.setHotelCode(ePropmap.getExpropId());
						hotelBean.setPassword(ePropmap.getExpassword());
						hotelBean.setUserId(ePropmap.getExusername());
					} else {
						return null;
					}
					String exrateId = eRatemaprs.getExrateId();
					String[] exrateIdSplit = exrateId.split("-");
					hotelBean.setHotelRoomCode(exrateIdSplit[0]);
					hotelBean.setFogRoomCode(fogRoomCode);
					hotelBean.setFogRateCode(plansplit[1]);
					hotelBean.setHotelRateCode(exrateId);
					hotelBeanList.add(hotelBean);
				}

			}

			return hotelBeanList;
		} catch (Exception e) {
			throw new MappingException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.IMapService#saveCnfNumMap(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void saveCnfNumMap(String thirdIataCode, String fogCnfnum, String thirdCnfnum) {
		// TODO Auto-generated method stub

	}

	public EPropmapDAO getEpropmapDAO() {
		return epropmapDAO;
	}

	public void setEpropmapDAO(EPropmapDAO epropmapDAO) {
		this.epropmapDAO = epropmapDAO;
	}

	public ERoommapDAO getEroommapDAO() {
		return eroommapDAO;
	}

	public void setEroommapDAO(ERoommapDAO eroommapDAO) {
		this.eroommapDAO = eroommapDAO;
	}

	public ERatemapDAO getEratemapDAO() {
		return eratemapDAO;
	}

	public void setEratemapDAO(ERatemapDAO eratemapDAO) {
		this.eratemapDAO = eratemapDAO;
	}

	public EExwebinfoDAO getEexwebinfoDAO() {
		return eexwebinfoDAO;
	}

	public void setEexwebinfoDAO(EExwebinfoDAO eexwebinfoDAO) {
		this.eexwebinfoDAO = eexwebinfoDAO;
	}

	@Override
	public String getExRoomRateMap(String fogroomId, String fogPropId, Enum thirdIataCode, String type)
			throws MappingException {
		try {
			List<EExwebinfo> result = eexwebinfoDAO.findByExwebCode(thirdIataCode.toString());
			String iata = "";
			if (result.size() > 0) {
				EExwebinfo eexwebinfo = result.get(0);
				iata = eexwebinfo.getKeyid().toString();

			}

			if ("roomType".equalsIgnoreCase(type)) {
				ERoommap eroommap = new ERoommap();
				eroommap.setExwebCode(iata);
				eroommap.setFogpropId(fogPropId);
				eroommap.setFogroomId(fogroomId);
				List<ERoommap> eroommapResult = eroommapDAO.findByExample(eroommap);
				if (eroommapResult.size() > 0) {
					ERoommap eR = eroommapResult.get(0);
					return eR.getExroomId();
				}
				return null;
			} else if ("rateType".equalsIgnoreCase(type)) {

			}
			return null;
		} catch (Exception e) {
			throw new MappingException(e);
		}
	}

	@Override
	public String getEXHotelCode(String thirdIataCode, String fogPropId) throws MappingException {
		try {
			List<HotelBean> hotelBeanlist = new ArrayList<HotelBean>();
			List result = eexwebinfoDAO.findByExwebCode(thirdIataCode);
			if (result.size() > 0) {
				EExwebinfo exwebInfo = (EExwebinfo) result.get(0);
				BigDecimal iata = exwebInfo.getKeyid();
				List<EPropmap> propList = epropmapDAO.findByExwebCode(iata.toString());
				for (EPropmap ePropmap : propList) {
					if (ePropmap.getFogpropId().equals(fogPropId)) {
						return ePropmap.getExpropId();
					}
				}
			}
			return null;
		} catch (Exception e) {
			throw new MappingException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.IMapService#getFogPropId(java.lang.String, java.lang.String)
	 */
	@Override
	public String getFogPropId(String thirdIataCode, String exHotelCode) throws MappingException {
		try {
			List<HotelBean> hotelBeanlist = new ArrayList<HotelBean>();
			List result = eexwebinfoDAO.findByExwebCode(thirdIataCode);
			if (result.size() > 0) {
				EExwebinfo exwebInfo = (EExwebinfo) result.get(0);
				BigDecimal iata = exwebInfo.getKeyid();
				List<EPropmap> propList = epropmapDAO.findByExwebCode(iata.toString());
				for (EPropmap ePropmap : propList) {
					if (ePropmap.getExpropId().equals(exHotelCode)) {
						return ePropmap.getFogpropId();
					}
				}
			}
			return null;
		} catch (Exception e) {
			throw new MappingException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.service.interf.IMapService#getAlertDesc(java.lang.String)
	 */
	@Override
	public String getAlertDesc(String alertCode) {
		try {
			ClassPathResource resource = new ClassPathResource("/errorcode.properties");
			File file = resource.getFile();
			if (file.exists()) {
				if (lastModified != file.lastModified()) {
					initMap(file);
				}
				return alertMap.get(alertCode);
			}
		} catch (IOException e) {
			logger.error("Init alertMap error, user default! ------------------------> error message: " + e);
		}

		return null;
	}

	private void initMap(File file) {
		alertMap.clear();
		InputStream inputStream = null;
		try {

			Properties properties = new Properties();
			inputStream = new FileInputStream(file);
			properties.load(inputStream);
			Set<Object> keySet = properties.keySet();
			Iterator<Object> iterator = keySet.iterator();
			while (iterator.hasNext()) {
				String next = (String) iterator.next();
				alertMap.put(next, (String) properties.get(next));
			}

			lastModified = file.lastModified();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Init dispatcher map error, user default! ------------------------> error message: " + e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(e);
				}
			}
		}

	}

	/**
	 * @return the alertMap
	 */
	public Map<String, String> getAlertMap() {
		return alertMap;
	}

	/**
	 * @param alertMap
	 *            the alertMap to set
	 */
	public void setAlertMap(Map<String, String> alertMap) {
		this.alertMap = alertMap;
	}

}
