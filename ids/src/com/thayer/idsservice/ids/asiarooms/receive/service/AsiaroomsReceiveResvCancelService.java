package com.thayer.idsservice.ids.asiarooms.receive.service;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.opentravel.ota.x2003.x05.CompanyNameType;
import org.opentravel.ota.x2003.x05.ErrorType;
import org.opentravel.ota.x2003.x05.ErrorsType;
import org.opentravel.ota.x2003.x05.OTACancelRQDocument;
import org.opentravel.ota.x2003.x05.OTACancelRSDocument;
import org.opentravel.ota.x2003.x05.SourceType;
import org.opentravel.ota.x2003.x05.TransactionStatusType;
import org.opentravel.ota.x2003.x05.UniqueIDType;
import org.opentravel.ota.x2003.x05.VerificationType;
import org.opentravel.ota.x2003.x05.OTACancelRQDocument.OTACancelRQ;
import org.opentravel.ota.x2003.x05.OTACancelRSDocument.OTACancelRS;
import org.opentravel.ota.x2003.x05.SourceType.RequestorID;

import com.thayer.idsservice.bean.MailFogResvBaseBean;
import com.thayer.idsservice.exception.BizException;
import com.thayer.idsservice.exception.MappingException;
import com.thayer.idsservice.task.download.bean.TResvBase;
import com.thayer.idsservice.task.receive.interf.AbstractReceiveResvService;
import com.thayer.idsservice.util.Constents;

public class AsiaroomsReceiveResvCancelService extends AbstractReceiveResvService {
	protected Logger LOGGER = Logger.getLogger(AsiaroomsReceiveResvCancelService.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1014793194249176219L;
	private String asiaroomsUserId;

	private String asiaroomsUserPwd;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thayer.idsservice.task.receive.interf.AbstractReceiveResvService#returnErrorMsg(java.lang.Throwable)
	 */
	@Override
	public String returnErrorMsg(Throwable e) {
		OTACancelRSDocument cancelRSDocument = OTACancelRSDocument.Factory.newInstance();
		OTACancelRS otaCancelRS = cancelRSDocument.addNewOTACancelRS();
		otaCancelRS.setStatus(TransactionStatusType.UNSUCCESSFUL);
		ErrorsType errors = otaCancelRS.addNewErrors();
		ErrorType error = errors.addNewError();
		error.setType("1");
		error.setCode("366");
		error.setRecordID("1");
		error.setStringValue("Error during processing, please retry");
		return cancelRSDocument.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.thayer.idsservice.task.receive.interf.AbstractReceiveResvService#buildRSXml(com.thayer.idsservice.bean.
	 * MailFogResvBaseBean, java.lang.String)
	 */
	@Override
	public String buildRSXml(MailFogResvBaseBean saveFogNewResvBean, String requestXml) {
		try {
			OTACancelRSDocument cancelRSDocument = OTACancelRSDocument.Factory.newInstance();
			OTACancelRS otaCancelRS = cancelRSDocument.addNewOTACancelRS();
			otaCancelRS.setStatus(TransactionStatusType.CANCELLED);
			otaCancelRS.addNewSuccess();
			OTACancelRQDocument cancelRQDocument = OTACancelRQDocument.Factory.parse(requestXml);
			UniqueIDType[] uniqueIDArray = cancelRQDocument.getOTACancelRQ().getUniqueIDArray();
			otaCancelRS.setUniqueIDArray(uniqueIDArray);
			return cancelRSDocument.toString();
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			return returnErrorMsg(e);
		}

	}

	@Override
	public TResvBase convertXml4fogbean(String requestXml) throws BizException, MappingException {
		try {
			OTACancelRQDocument cancelRQDocument = OTACancelRQDocument.Factory.parse(requestXml);
			OTACancelRQ otaCancelRQ = cancelRQDocument.getOTACancelRQ();
			SourceType sourceArray = otaCancelRQ.getPOS().getSourceArray(0);
			RequestorID requestorID = sourceArray.getRequestorID();
			String id = requestorID.getID();
			String messagePassword = requestorID.getMessagePassword();
			if (!id.equals(asiaroomsUserId) || !messagePassword.equals(asiaroomsUserPwd)) {
				throw new BizException("username and password is wrong!");
			}

			UniqueIDType[] uniqueIDArray = otaCancelRQ.getUniqueIDArray();
			String outcnfNum = "";
			for (UniqueIDType idType : uniqueIDArray) {
				if ("5".equals(idType.getType())) {
					outcnfNum = idType.getID();
					break;
				}
			}

			TResvBase tresvbase = new TResvBase();
			tresvbase.setIata(getIata());
			tresvbase.setResvtype(Constents.RESV_TYPE_CANCEL);
			tresvbase.setOutcnfnum(outcnfNum);

			VerificationType[] verificationArray = otaCancelRQ.getVerificationArray();
			CompanyNameType[] vendorArray = verificationArray[0].getVendorArray();
			for (CompanyNameType nameType : vendorArray) {
				if ("3".equals(nameType.getCode())) {
					tresvbase.setProp(nameType.getStringValue());
					break;
				}
			}

			return tresvbase;
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getFullStackTrace(e));
			throw new BizException(e);
		}

	}

	/**
	 * @return the asiaroomsUserId
	 */
	public String getAsiaroomsUserId() {
		return asiaroomsUserId;
	}

	/**
	 * @param asiaroomsUserId
	 *            the asiaroomsUserId to set
	 */
	public void setAsiaroomsUserId(String asiaroomsUserId) {
		this.asiaroomsUserId = asiaroomsUserId;
	}

	/**
	 * @return the asiaroomsUserPwd
	 */
	public String getAsiaroomsUserPwd() {
		return asiaroomsUserPwd;
	}

	/**
	 * @param asiaroomsUserPwd
	 *            the asiaroomsUserPwd to set
	 */
	public void setAsiaroomsUserPwd(String asiaroomsUserPwd) {
		this.asiaroomsUserPwd = asiaroomsUserPwd;
	}
}
