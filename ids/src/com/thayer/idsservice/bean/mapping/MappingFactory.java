package com.thayer.idsservice.bean.mapping;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.thayer.idsservice.bean.mapping.xml.HotelMappingXml;
import com.thayer.idsservice.bean.mapping.xml.PlanMappingXml;
import com.thayer.idsservice.bean.mapping.xml.RateMappingXml;
import com.thayer.idsservice.bean.mapping.xml.RoomMappingXml;
import com.thayer.idsservice.bean.mapping.xml.XmlPathMapping;

/**
 * MappingFactory.java
 * 
 * 
 * @author waterborn
 */
@SuppressWarnings("unchecked")
public class MappingFactory {

	private static MappingFactory instance = new MappingFactory();

	private MappingFactory() {
		InputStream stream = null;
		try {
			XmlPathMapping pathXmlMapping = new XmlPathMapping();
			
			stream = MappingFactory.class.getClassLoader().getResourceAsStream("propXmlPathMapping.xml");
			pathXmlMapping.parseXml(stream);
			xmlPathMapping = pathXmlMapping;
			for (MappingEnum type : MappingEnum.values()) {
				HotelMappingXml mappingXml = new HotelMappingXml();
				String xml = getHotelInfoMappingXmlName(type, pathXmlMapping);
				if (mappingXml != null && xml != null) {
					mappingXml.parseXml(xml);
					mapHotelMapping.put(type.toString(), mappingXml);
				}
			}

			for (MappingEnum type : MappingEnum.values()) {
				RoomMappingXml mappingXml = new RoomMappingXml();
				String xml = getHotelInfoMappingXmlName(type, pathXmlMapping);
				if (mappingXml != null && xml != null) {
					mappingXml.parseXml(xml);
					mapRoomMapping.put(type.toString(), mappingXml);
				}
			}
			
			for (MappingEnum type : MappingEnum.values()) {
				RateMappingXml mappingXml = new RateMappingXml();
				String xml = getHotelInfoMappingXmlName(type, pathXmlMapping);
				if (mappingXml != null && xml != null) {
					mappingXml.parseXml(xml);
					mapRateMapping.put(type.toString(), mappingXml);
				}
			}

			for (MappingEnum type : MappingEnum.values()) {
				PlanMappingXml mappingXml = new PlanMappingXml();
				String xml = getHotelInfoMappingXmlName(type, pathXmlMapping);
				if (mappingXml != null && xml != null) {
					mappingXml.parseXml(xml);
					mapPlanMapping.put(type.toString(), mappingXml);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Map<String, HotelMappingXml> mapHotelMapping = new HashMap<String, HotelMappingXml>();

	private Map<String, RoomMappingXml> mapRoomMapping = new HashMap<String, RoomMappingXml>();

	private Map<String, RateMappingXml> mapRateMapping = new HashMap<String, RateMappingXml>();
	
	private Map<String, PlanMappingXml> mapPlanMapping = new HashMap<String, PlanMappingXml>();

	private XmlPathMapping xmlPathMapping = new XmlPathMapping();

	// private String getHotelMappingXmlName(Enum type) {
	// String xml = null;
	// if (MappingEnum.MAPPING_GREENTREE == type) {
	// xml = "properties/greentree/Hotel_mapping.xml";
	// } else if (MappingEnum.MAPPING_HOMEINNS == type) {
	// xml = "properties/homeinn/Hotel_mapping.xml";
	// } else if (MappingEnum.MAPPING_JJZX == type) {
	// xml = "properties/jjzx/Hotel_mapping.xml";
	// } else if (MappingEnum.MAPPING_SEVENDAYS == type) {
	// xml = "properties/sevendays/Hotel_mapping.xml";
	// }
	// return xml;
	// }
	private String getHotelInfoMappingXmlName(Enum type, XmlPathMapping pathXmlMapping) {
		String xml = null;
		if (pathXmlMapping.getXmlBeanByEnum(type.toString()) != null) {
			xml = (String) pathXmlMapping.getXmlBeanByEnum(type.toString()).getPathMapping().get("hotelInfoMapping");
		}

		return xml;
	}

	public static HotelMappingXml getHotelMapping(String type) {
		return instance.mapHotelMapping.get(type.toString());
	}

	public static RoomMappingXml getRoomMapping(Enum type) {
		return instance.mapRoomMapping.get(type.toString());
	}

	public static RateMappingXml getRateMapping(Enum type) {
		return instance.mapRateMapping.get(type.toString());
	}
	
	public static PlanMappingXml getPlanMapping(Enum type) {
		return instance.mapPlanMapping.get(type.toString());
	}

	public static XmlPathMapping getXmlMapping() {
		return instance.xmlPathMapping;
	}

	public static boolean flashMap() {
		try {
			instance.mapHotelMapping.clear();
			instance.mapRoomMapping.clear();
			instance.mapRateMapping.clear();
			instance = new MappingFactory();
			return true;
		} catch (Exception e) {
			System.out.println("error: " + e.getMessage());
			return false;
		}

	}

	public XmlPathMapping getXmlPathMapping() {
		return xmlPathMapping;
	}

	public void setXmlPathMapping(XmlPathMapping xmlPathMapping) {
		this.xmlPathMapping = xmlPathMapping;
	}

	public static void main(String[] args) {
		System.out.println(MappingFactory.getHotelMapping((MappingEnum.AGODA).toString()).getHotelCodeMap());
		System.out.println(MappingFactory.getRoomMapping(MappingEnum.AGODA).getHotelRoomCodeMap());
//		Map<String, PropMappingBean> propBeanMap = MappingFactory.getHotelMapping(MappingEnum.AGODA).getPropBeanMap();
//		PropMappingBean propMappingBean = propBeanMap.get("1346");
		Map<String, PropMappingBean> propRoomBeanMap = MappingFactory.getRoomMapping(MappingEnum.AGODA).getPropRoomBeanMap();
		PropMappingBean propMappingBean2 = propRoomBeanMap.get("1932#DD");
		
		Map<String, PropMappingBean> propRateBeanMap = MappingFactory.getRateMapping(MappingEnum.AGODA).getPropRateBeanMap();
		PropMappingBean propMappingBean3 = propRateBeanMap.get("1346#BAR1");
		
		Map<String, PropMappingBean> propPlanBeanMap = MappingFactory.getPlanMapping(MappingEnum.AGODA).getPropPlanBeanMap();
		PropMappingBean propMappingBean4 = propPlanBeanMap.get("1346#SQ#BAR1");
		System.out.println();
//		System.out.println(MappingFactory.getRoomMapping(MappingEnum.MAPPING_HOMEINNS).getFogPropRoomMap());
	}

}
