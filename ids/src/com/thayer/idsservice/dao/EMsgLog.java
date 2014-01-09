package com.thayer.idsservice.dao;

import java.util.Date;


/**
 * EMsgLog entity. @author MyEclipse Persistence Tools
 */
public class EMsgLog extends AbstractEMsgLog implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public EMsgLog() {
    }

	/** minimal constructor */
    public EMsgLog(String exwebCode, Date createtime, Date updatetime) {
        super(exwebCode, createtime, updatetime);        
    }
    
    /** full constructor */
    public EMsgLog(String exwebCode, String msgId, String msgStatus, String msgRq, String msgRs, String msgRsFinal, String msgErrorcode, String msgError, Date createtime, Date updatetime) {
        super(exwebCode, msgId, msgStatus, msgRq, msgRs, msgRsFinal, msgErrorcode, msgError, createtime, updatetime);        
    }
   
}
