package com.thayer.idsservice.bean.mapping.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
public class RateMappingXml implements IMappingXml {

	private static Logger log = Logger.getLogger(RateMappingXml.class);

	@SuppressWarnings("unchecked")
	public void parseXml(String xml) {

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
						if (propRateBeanMap.get(strFogProp + "#" + strFogRateCode) != null) {
							PropMappingBean propMappingBean2 = propRateBeanMap.get(strFogProp + "#" + strFogRateCode);
							List<RoomMapBean> roomMapBean2 = propMappingBean2.getRoomMapBean();
							roomMapBean2.add(roomMapBean);
							propRateBeanMap.put(strFogProp + "#" + strFogRateCode, propMappingBean2);
						}else{
							propRateBeanMap.put(strFogProp + "#" + strFogRateCode, propMappingBean);
						}
					}

				}
			}
		} catch (ConfigurationException e) {
			log.fatal("Fail parseXml " + xml + " , exception:" + e);
		}
	}

	private Map<String, PropMappingBean> propRateBeanMap = new HashMap<String, PropMappingBean>();

	public Map<String, PropMappingBean> getPropRateBeanMap() {
		return propRateBeanMap;
	}

	

}
