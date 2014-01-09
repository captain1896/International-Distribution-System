package com.thayer.idsservice.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * AbstractEExwebinfo entity provides the base persistence definition of the EExwebinfo entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractEExwebinfo  implements java.io.Serializable {


    // Fields    

     private BigDecimal keyid;
     private String exwebCode;
     private String serviceUrl;
     private String desction;
     private String exuser;
     private String expassword;
     private Timestamp createdate;
     private String createby;


    // Constructors

    /** default constructor */
    public AbstractEExwebinfo() {
    }

	/** minimal constructor */
    public AbstractEExwebinfo(String exwebCode) {
        this.exwebCode = exwebCode;
    }
    
    /** full constructor */
    public AbstractEExwebinfo(String exwebCode, String serviceUrl, String desction, String exuser, String expassword, Timestamp createdate, String createby) {
        this.exwebCode = exwebCode;
        this.serviceUrl = serviceUrl;
        this.desction = desction;
        this.exuser = exuser;
        this.expassword = expassword;
        this.createdate = createdate;
        this.createby = createby;
    }

   
    // Property accessors

    public BigDecimal getKeyid() {
        return this.keyid;
    }
    
    public void setKeyid(BigDecimal keyid) {
        this.keyid = keyid;
    }

    public String getExwebCode() {
        return this.exwebCode;
    }
    
    public void setExwebCode(String exwebCode) {
        this.exwebCode = exwebCode;
    }

    public String getServiceUrl() {
        return this.serviceUrl;
    }
    
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getDesction() {
        return this.desction;
    }
    
    public void setDesction(String desction) {
        this.desction = desction;
    }

    public String getExuser() {
        return this.exuser;
    }
    
    public void setExuser(String exuser) {
        this.exuser = exuser;
    }

    public String getExpassword() {
        return this.expassword;
    }
    
    public void setExpassword(String expassword) {
        this.expassword = expassword;
    }

    public Timestamp getCreatedate() {
        return this.createdate;
    }
    
    public void setCreatedate(Timestamp createdate) {
        this.createdate = createdate;
    }

    public String getCreateby() {
        return this.createby;
    }
    
    public void setCreateby(String createby) {
        this.createby = createby;
    }
   








}