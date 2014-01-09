/*****************************************************************<br>
 * <B>FILE :</B> ExpediaResvCallbackService.java <br>
 * <B>CREATE DATE :</B> 2011-4-2 <br>
 * <B>DESCRIPTION :</B> <br>
 *
 * <B>CHANGE HISTORY LOG</B><br>
 *---------------------------------------------------------------<br>
 * NO.  |  DATE |   NAME   |   REASON   |  DESCRIPTION           <br>
 *---------------------------------------------------------------<br>
 *          
 *****************************************************************<br>
 */
package com.thayer.idsservice.ids.expedia.download.service;

import java.math.BigInteger;
import java.util.Calendar;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.expediaconnect.eqc.bc.x2007.x08.BookingConfirmRSDocument;
import com.expediaconnect.eqc.bc.x2007.x08.BookingConfirmRSDocument.BookingConfirmRS;
import com.expediaconnect.eqc.bc.x2007.x08.BookingConfirmRSDocument.BookingConfirmRS.Success;
import com.expediaconnect.eqc.bc.x2007.x09.BookingConfirmRQDocument;
import com.expediaconnect.eqc.bc.x2007.x09.BookingConfirmRQDocument.BookingConfirmRQ;
import com.expediaconnect.eqc.bc.x2007.x09.BookingConfirmRQDocument.BookingConfirmRQ.Authentication;
import com.expediaconnect.eqc.bc.x2007.x09.BookingConfirmRQDocument.BookingConfirmRQ.BookingConfirmNumbers;
import com.expediaconnect.eqc.bc.x2007.x09.BookingConfirmRQDocument.BookingConfirmRQ.Hotel;
import com.expediaconnect.eqc.bc.x2007.x09.BookingConfirmRQDocument.BookingConfirmRQ.BookingConfirmNumbers.BookingConfirmNumber;
import com.expediaconnect.eqc.bc.x2007.x09.BookingConfirmRQDocument.BookingConfirmRQ.BookingConfirmNumbers.BookingConfirmNumber.BookingType;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.task.download.bean.SuccessBean;
import com.thayer.idsservice.task.download.interf.AbstractResvCallbackService;

/**
 * <B>Function :</B> <br>
 * <B>General Usage :</B> <br>
 * <B>Special Usage :</B> <br>
 * 
 * @author : Jean.Jiang<br>
 * @since : 2011-4-2<br>
 * @version : v1.0
 */
public class ExpediaResvCallbackService extends AbstractResvCallbackService {
	private static Logger LOGGER = Logger.getLogger(ExpediaResvCallbackService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thayer.idsservice.task.download.interf.AbstractResvCallbackService#buildResvCallbackRQ(com.thayer.idsservice
	 * .task.download.bean.SuccessBean)
	 */
	@Override
	public String buildResvCallbackRQ(SuccessBean successBean) {
		BookingConfirmRQDocument bookingConfirmRQDocument = BookingConfirmRQDocument.Factory.newInstance();
		BookingConfirmRQ bookingConfirmRQ = bookingConfirmRQDocument.addNewBookingConfirmRQ();
		Authentication authentication = bookingConfirmRQ.addNewAuthentication();
		authentication.setUsername(successBean.getUsername());
		authentication.setPassword(successBean.getPassword());
		Hotel hotel = bookingConfirmRQ.addNewHotel();
		hotel.setId(new BigInteger(successBean.getIdsProp()));
		BookingConfirmNumbers bookingConfirmNumbers = bookingConfirmRQ.addNewBookingConfirmNumbers();
		BookingConfirmNumber bookingConfirmNumber = bookingConfirmNumbers.addNewBookingConfirmNumber();
		bookingConfirmNumber.setBookingID(new BigInteger(successBean.getIds_cnfnum()));
		bookingConfirmNumber.setConfirmNumber(successBean.getCnfnum());
		// booked type
		String resvtype = successBean.gettResvBase().getResvtype();
		if ("n".equals(resvtype)) {
			bookingConfirmNumber.setBookingType(BookingType.BOOK);
		} else if ("e".equals(resvtype)) {
			bookingConfirmNumber.setBookingType(BookingType.MODIFY);
		} else if ("c".equals(resvtype)) {
			bookingConfirmNumber.setBookingType(BookingType.CANCEL);
		}
		// confirmTime

		bookingConfirmNumber.setConfirmTime(Calendar.getInstance());

		String result = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> " + bookingConfirmRQDocument.toString();
		LOGGER.info("start call back resv info request...............\n" + result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.task.download.interf.AbstractResvCallbackService#parseResponseXml(java.lang.String)
	 */
	@Override
	public boolean parseResponseXml(String resvCallbackRS) throws BizException {
		LOGGER.info("call back resv info response...............\n" + resvCallbackRS);
		try {
			BookingConfirmRSDocument bookingConfirmRSDocument = BookingConfirmRSDocument.Factory.parse(resvCallbackRS);
			BookingConfirmRS bookingConfirmRS = bookingConfirmRSDocument.getBookingConfirmRS();
			if (bookingConfirmRS.getErrorArray() != null && bookingConfirmRS.getErrorArray().length > 0) {
				return false;
			}

			Success success = bookingConfirmRS.getSuccess();
			if (success.getWarningArray() != null && success.getWarningArray().length > 0) {
				return false;
			}

			return true;
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
		}
		return false;
	}

}
