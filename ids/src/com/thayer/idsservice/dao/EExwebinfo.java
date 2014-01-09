package com.thayer.idsservice.dao;

import java.sql.Timestamp;


/**
 * EExwebinfo entity. @author MyEclipse Persistence Tools
 */
public class EExwebinfo extends AbstractEExwebinfo implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public EExwebinfo() {
    }

	/** minimal constructor */
    public EExwebinfo(String exwebCode) {
        super(exwebCode);        
    }
    
    /** full constructor */
    public EExwebinfo(String exwebCode, String serviceUrl, String desction, String exuser, String expassword, Timestamp createdate, String createby) {
        super(exwebCode, serviceUrl, desction, exuser, expassword, createdate, createby);        
    }
   
}
