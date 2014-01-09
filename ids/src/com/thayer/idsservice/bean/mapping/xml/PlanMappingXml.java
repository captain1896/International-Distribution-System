package com.thayer.idsservice.bean.mapping.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import com.thayer.idsservice.bean.mapping.MappingResultBean;
import com.thayer.idsservice.bean.mapping.PropMappingBean;
import com.thayer.idsservice.bean.mapping.RateMapBean;
import com.thayer.idsservice.bean.mapping.RoomMapBean;

/**
 * RoomMappingXml 房型映射
 * 
 * @author waterborn
 */
public class PlanMappingXml implements IMappingXml {

	private static Logger log = Logger.getLogger(PlanMappingXml.class);

	@SuppressWarnings("unchecked")
	public void parseXml(String xml) {
		fogPropRateMap.clear();
		hotelRateMap.clear();
		try {
			XMLConfiguration config = new XMLConfiguration(xml);
			List listMapping = config.configurationsAt("mapping");
			for (Iterator it = listMapping.iterator(); it.hasNext();) {
				HierarchicalConfiguration subMappingConfig = (HierarchicalConfiguration) it.next();
				String strFogProp = subMappingConfig.getString("[@FogProp]");
				String strHotelProp = subMappingConfig.getString("[@HotelCode]");
				String password = subMappingConfig.getString("[@Password]");
				String userId = subMappingConfig.getString("[@UserId]");
				List listRoom = subMappingConfig.configurationsAt("room");
				for (Iterator iter = listRoom.iterator(); iter.hasNext();) {
					HierarchicalConfiguration subRoomConfig = (HierarchicalConfiguration) iter.next();
					String strFogRoomCode = subRoomConfig.getString("[@FogRoomCode]");
					String strHotelRoomCode = subRoomConfig.getString("[@HotelRoomCode]");

					List<HierarchicalConfiguration> listRate = subRoomConfig.configurationsAt("rate");

					for (HierarchicalConfiguration subRateConfig : listRate) {
						PropMappingBean propMappingBean = new PropMappingBean();
						propMappingBean.setFogProp(strFogProp);
						propMappingBean.setHotelCode(strHotelProp);
						propMappingBean.setPassword(password);
						propMappingBean.setUserId(userId);
						List<RoomMapBean> roomMapBeanList = propMappingBean.getRoomMapBean();
						RoomMapBean roomMapBean = new RoomMapBean();
						roomMapBean.setFogRoomCode(strFogRoomCode);
						roomMapBean.setHotelRoomCode(strHotelRoomCode);
						roomMapBeanList.add(roomMapBean);
						List<RateMapBean> rateMapBeanList = roomMapBean.getRateMapBean();
						RateMapBean rateMapBean = new RateMapBean();
						rateMapBeanList.add(rateMapBean);
						String strFogRateCode = subRateConfig.getString("[@FogRateCode]");
						String strHotelRateCode = subRateConfig.getString("[@HotelRateCode]");
						rateMapBean.setFogRateCode(strFogRateCode);
						rateMapBean.setHotelRateCode(strHotelRateCode);
						fogPropRateMap.put(strFogProp + "#" + strFogRoomCode + "#" + strFogRateCode, strHotelRateCode);
						hotelRateMap
								.put(strHotelProp + "#" + strHotelRoomCode + "#" + strHotelRateCode, strFogRateCode);
						propPlanBeanMap.put(strFogProp + "#" + strFogRoomCode + "#" + strFogRateCode, propMappingBean);
						MappingResultBean mappingResultBean=new MappingResultBean();
						
						mappingResultBean.setFogProp(strFogProp);
						mappingResultBean.setFogRateCode(strFogRateCode);
						mappingResultBean.setFogRoomCode(strFogRoomCode);
						mappingResultBean.setHotelCode(strHotelProp);
						mappingResultBean.setHotelRateCode(strHotelRateCode);
						mappingResultBean.setHotelRoomCode(strHotelRoomCode);
						mappingResultBean.setPassword(password);
						mappingResultBean.setUserId(userId);
						
						propPlanMap.put(strFogProp + "#" + strFogRoomCode + "#" + strFogRateCode, mappingResultBean);

					}

				}
			}
		} catch (ConfigurationException e) {
			log.fatal("Fail parseXml " + xml + " , exception:" + e);
		}
	}

	private Map<String, String> fogPropRateMap = new HashMap<String, String>();

	private Map<String, String> hotelRateMap = new HashMap<String, String>();

	private Map<String, PropMappingBean> propPlanBeanMap = new HashMap<String, PropMappingBean>();

	private Map<String, MappingResultBean> propPlanMap = new HashMap<String, MappingResultBean>();

	public Map<String, PropMappingBean> getPropPlanBeanMap() {
		return propPlanBeanMap;
	}

	public Map<String, MappingResultBean> getPropPlanMap() {
		return propPlanMap;
	}

	/**
	 * Return the fogPropRoomMap.
	 * 
	 * @return the fogPropRoomMap.
	 */
	public Map<String, String> getFogPropRateMap() {

		return fogPropRateMap;
	}

	public Map<String, String> getFogPropRateMap(String prop, String room) {
		Map<String, String> result = new HashMap<String, String>();
		Set<String> keySet = fogPropRateMap.keySet();
		for (String s : keySet) {
			if (s.startsWith(prop + "#" + room + "#")) {
				result.put(s.substring((prop + "#" + room + "#").length()), fogPropRateMap.get(s));
			}
		}
		return result;
	}

	/**
	 * Return the hotelRoomCodeMap.
	 * 
	 * @return the hotelRoomCodeMap.
	 */
	public Map<String, String> getHotelRateMap() {
		return hotelRateMap;
	}

}
