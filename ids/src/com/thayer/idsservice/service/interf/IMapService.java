package com.thayer.idsservice.service.interf;

import java.io.Serializable;
import java.util.List;

import com.thayer.idsservice.bean.mapping.MappingResultBean;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.task.download.bean.CnfnumBean;
import com.thayer.idsservice.task.download.bean.HotelBean;

public interface IMapService extends Serializable {
	/**
	 * 根据thirdIataCode 获得代理的所有酒店列表<br>
	 * thirdIataCode: AGODA
	 * 
	 * @param iataCode
	 * @return HotelBean
	 */
	public List<HotelBean> getHotelMapList(String thirdIataCode) throws MappingException;

	/**
	 * 根据代理号+代理的订单号码 找到订单号对应
	 * 
	 * @param thirdIataCode
	 * @param thirdCnfnum
	 * @return
	 */
	public CnfnumBean getCnfnumMapByThirdCnfnum(String thirdIataCode, String thirdCnfnum) throws MappingException;

	/**
	 * 保存订单映射关系
	 * 
	 * @param thirdIataCode
	 * @param fogCnfnum
	 * @return
	 */
	public void saveCnfNumMap(String thirdIataCode, String fogCnfnum, String thirdCnfnum) throws MappingException;

	/**
	 * 根据 Exrate_id|FogProp_id|Exweb_code 返回 FogRate_id
	 * 
	 * @param exrate_id
	 * @param fogProp_id
	 * @param exweb_code
	 * @param type
	 * @return
	 */
	public String getRoomRateMap(String exrateId, String fogPropId, Enum thirdIataCode, String type)
			throws MappingException;

	/**
	 * 根据fograte_id|FogProp_id|Exweb_code 返回 ExRate_id
	 * 
	 * @param fogRoomRate
	 * @param fogPropId
	 * @param thirdIataCode
	 * @param type
	 * @return
	 * @throws MappingException
	 */
	public String getExRoomRateMap(String fogRoomRate, String fogPropId, Enum thirdIataCode, String type)
			throws MappingException;

	/**
	 * @param type
	 * @param fogProp
	 * @param planCode
	 *            ST-BAR
	 * @return
	 */
	public MappingResultBean getThirdHotelRateCodeByPlanLevel(Enum type, String fogProp, String planCode)
			throws MappingException;

	/**
	 * For Expedia mapping
	 * 
	 * @param type
	 * @param fogProp
	 * @param planCode
	 * @return
	 * @throws MappingException
	 */
	public List<MappingResultBean> getThirdHotelRateCodeByPlanLevel4Expedia(Enum type, String fogProp, String planCode)
			throws MappingException;

	/**
	 * For Venere mapping
	 * 
	 * @param type
	 * @param fogProp
	 * @param planCode
	 * @return
	 * @throws MappingException
	 */
	public List<MappingResultBean> getThirdHotelRateCodeByPlanLevel4Venere(Enum type, String fogProp, String planCode)
			throws MappingException;

	/**
	 * 根据thirdIataCode和fog酒店ID号， 获得代理的酒店id<br>
	 * thirdIataCode: AGODA
	 * 
	 * @param iataCode
	 * @return HotelBean
	 */
	public String getEXHotelCode(String thirdIataCode, String fogProp_Id) throws MappingException;

	/**
	 * 根据thirdIataCode和IDS酒店ID号， 获得FOG酒店id<br>
	 * thirdIataCode: AGODA
	 * 
	 * @param thirdIataCode
	 * @param exHotelCode
	 * @return FogPropId
	 */
	public String getFogPropId(String thirdIataCode, String exHotelCode) throws MappingException;

	/**
	 * 根据alertcode，获取alert描述.
	 * 
	 * @param alertCode
	 * @return
	 * @throws MappingException
	 */
	public String getAlertDesc(String alertCode);
}
