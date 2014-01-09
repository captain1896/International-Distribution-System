/*****************************************************************<br>
 * <B>FILE :</B> TaskManagerAction.java <br>
 * <B>CREATE DATE :</B> 2010-10-27 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.ids.asiarooms.receive;

import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.opentravel.ota.x2003.x05.OTACancelRQDocument;
import org.opentravel.ota.x2003.x05.OTAHotelAvailRQDocument;
import org.opentravel.ota.x2003.x05.OTAHotelResRQDocument;

import com.thayer.idsservice.bean.ReceiveTypeEnum;
import com.thayer.idsservice.task.receive.interf.AbstractReceiveAction;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2010-10-27<br>
 * @version : v1.0
 */
public class AsiaroomsReceiveAction extends AbstractReceiveAction {

	@Override
	public ReceiveTypeEnum getReceiveType(String xml) {
		List validationErrors = new ArrayList();
		XmlOptions validationOptions = new XmlOptions();
		validationOptions.setErrorListener(validationErrors);

		try {
			OTAHotelAvailRQDocument otahotelAvailRQDocument = OTAHotelAvailRQDocument.Factory.parse(xml);

			logger.info("receive Asiarooms Avail request..........");
			return ReceiveTypeEnum.OTA_HOTELAVAIL;

		} catch (XmlException e) {

		}

		try {
			OTAHotelResRQDocument otahotelResRQDocument = OTAHotelResRQDocument.Factory.parse(xml);

			logger.info("receive Asiarooms resv request..........");
			return ReceiveTypeEnum.OTA_HOTELRES;

		} catch (XmlException e) {

		}

		try {

			OTACancelRQDocument cancelRQDocument = OTACancelRQDocument.Factory.parse(xml);

			logger.info("receive Asiarooms cancel resv request..........");
			return ReceiveTypeEnum.OTA_CANCEL;

		} catch (XmlException e) {

		}

		logger.info("receive Asiarooms no type request..........");
		return null;
	}

}
