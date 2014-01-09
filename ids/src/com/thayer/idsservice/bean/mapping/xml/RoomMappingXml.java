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

import com.thayer.idsservice.bean.mapping.PropMappingBean;
import com.thayer.idsservice.bean.mapping.RateMapBean;
import com.thayer.idsservice.bean.mapping.RoomMapBean;

/**
 * RoomMappingXml 房型映射
 * 
 * @author waterborn
 */
public class RoomMappingXml implements IMappingXml {

	private static Logger log = Logger.getLogger(RoomMappingXml.class);

	@SuppressWarnings("unchecked")
	public void parseXml(String xml) {
		fogPropRoomMap.clear();
		hotelRoomCodeMap.clear();
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
					fogPropRoomMap.put(strFogProp + "#" + strFogRoomCode, strHotelRoomCode);
					hotelRoomCodeMap.put(strHotelProp + "#" + strHotelRoomCode, strFogRoomCode);
					PropMappingBean propMappingBean = new PropMappingBean();
					propMappingBean.setFogProp(strFogProp);
					propMappingBean.setHotelCode(strHotelProp);
					propMappingBean.setPassword(password);
					propMappingBean.setUserId(userId);
					List<RoomMapBean> roomMapBeanList = propMappingBean.getRoomMapBean();
					propRoomBeanMap.put(strFogProp + "#" + strFogRoomCode, propMappingBean);
					RoomMapBean roomMapBean = new RoomMapBean();
					roomMapBean.setFogRoomCode(strFogRoomCode);
					roomMapBean.setHotelRoomCode(strHotelRoomCode);
					roomMapBeanList.add(roomMapBean);
					List<RateMapBean> rateMapBeanList = roomMapBean.getRateMapBean();
					List<HierarchicalConfiguration> listRate = subRoomConfig.configurationsAt("rate");
					for (HierarchicalConfiguration subRateConfig : listRate) {
						RateMapBean rateMapBean = new RateMapBean();
						String strFogRateCode = subRateConfig.getString("[@FogRateCode]");
						String strHotelRateCode = subRateConfig.getString("[@HotelRateCode]");
						rateMapBean.setFogRateCode(strFogRateCode);
						rateMapBean.setHotelRateCode(strHotelRateCode);
						rateMapBeanList.add(rateMapBean);
					}

				}
			}
		} catch (ConfigurationException e) {
			log.fatal("Fail parseXml " + xml + " , exception:" + e);
		}
	}

	private Map<String, String> fogPropRoomMap = new HashMap<String, String>();

	private Map<String, String> hotelRoomCodeMap = new HashMap<String, String>();

	private Map<String, PropMappingBean> propRoomBeanMap = new HashMap<String, PropMappingBean>();

	public Map<String, PropMappingBean> getPropRoomBeanMap() {
		return propRoomBeanMap;
	}

	/**
	 * Return the fogPropRoomMap.
	 * 
	 * @return the fogPropRoomMap.
	 */
	public Map<String, String> getFogPropRoomMap() {

		return fogPropRoomMap;
	}

	public Map<String, String> getFogPropRoomMap(String prop) {
		Map<String, String> result = new HashMap<String, String>();
		Set<String> keySet = fogPropRoomMap.keySet();
		for (String s : keySet) {
			if (s.startsWith(prop + "#")) {
				String room = s.substring((prop + "#").length());
				result.put(room, fogPropRoomMap.get(s));
			}
		}
		return result;
	}

	/**
	 * Return the hotelRoomCodeMap.
	 * 
	 * @return the hotelRoomCodeMap.
	 */
	public Map<String, String> getHotelRoomCodeMap() {
		return hotelRoomCodeMap;
	}

}
