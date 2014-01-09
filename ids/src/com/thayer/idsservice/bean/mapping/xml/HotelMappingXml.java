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
 * HotelMappingXml 酒店映射
 * 
 * @author waterborn
 */
public class HotelMappingXml implements IMappingXml {

    private static Logger log = Logger.getLogger(HotelMappingXml.class);

    @SuppressWarnings("unchecked")
    public void parseXml(String xml) {
        fogPropMap.clear();
        hotelCodeMap.clear();
        propBeanMap.clear();
        try {
            XMLConfiguration config = new XMLConfiguration(xml);
            List fields = config.configurationsAt("mapping");
            for (Iterator it = fields.iterator(); it.hasNext();) {
                HierarchicalConfiguration sub = (HierarchicalConfiguration) it.next();
                String strFogProp = sub.getString("[@FogProp]");
                String strHotelCode = sub.getString("[@HotelCode]");
                String password = sub.getString("[@Password]");
                String userId = sub.getString("[@UserId]");
                fogPropMap.put(strFogProp, strHotelCode);
                hotelCodeMap.put(strHotelCode, strFogProp);
                PropMappingBean propMappingBean=new PropMappingBean();
                propMappingBean.setFogProp(strFogProp);
                propMappingBean.setHotelCode(strHotelCode);
                propMappingBean.setPassword(password);
                propMappingBean.setUserId(userId);
                propBeanMap.put(strFogProp, propMappingBean);
                List listRoom = sub.configurationsAt("room");
                List<RoomMapBean> roomMapBeanList = propMappingBean.getRoomMapBean();
                for (Iterator iter = listRoom.iterator(); iter.hasNext();) {
					HierarchicalConfiguration subRoomConfig = (HierarchicalConfiguration) iter.next();
					String strFogRoomCode = subRoomConfig.getString("[@FogRoomCode]");
					String strHotelRoomCode = subRoomConfig.getString("[@HotelRoomCode]");
					
					RoomMapBean roomMapBean = new RoomMapBean();
					roomMapBean.setFogRoomCode(strFogRoomCode);
					roomMapBean.setHotelRoomCode(strHotelRoomCode);
					roomMapBeanList.add(roomMapBean);
					List<RateMapBean> rateMapBeanList = roomMapBean.getRateMapBean();
					List<HierarchicalConfiguration> listRate = subRoomConfig.configurationsAt("rate");
					for (HierarchicalConfiguration subRateConfig : listRate) {
						RateMapBean rateMapBean=new RateMapBean();
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

    private Map<String, String> fogPropMap = new HashMap<String, String>();

    private Map<String, String> hotelCodeMap = new HashMap<String, String>();
    
    private Map<String, PropMappingBean> propBeanMap = new HashMap<String, PropMappingBean>();    


	/**
     * @return
     */
    public Map<String, PropMappingBean> getPropBeanMap() {
		return propBeanMap;
	}

	/**
     * Return the fogPropMap.
     * 
     * @return the fogPropMap.
     */
    public Map<String, String> getFogPropMap() {
        return fogPropMap;
    }

    /**
     * Return the hotelCodeMap.
     * 
     * @return the hotelCodeMap.
     */
    public Map<String, String> getHotelCodeMap() {
        return hotelCodeMap;
    }

}
