package com.thayer.idsservice.dao;

import java.math.BigDecimal;


/**
 * AbstractEPropmap entity provides the base persistence definition of the EPropmap entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractEPropmap  implements java.io.Serializable {


    // Fields    

     private BigDecimal keyid;
     private String expropId;
     private String fogpropId;
     private String exwebDesc;
     private String fogpropName;
     private String exwebCode;
     private String exusername;
     private String expassword;
     private String other;
     private String inputdate;


    // Constructors

    /** default constructor */
    public AbstractEPropmap() {
    }

	/** minimal constructor */
    public AbstractEPropmap(String expropId) {
        this.expropId = expropId;
    }
    
    /** full constructor */
    public AbstractEPropmap(String expropId, String fogpropId, String exwebDesc, String fogpropName, String exwebCode, String exusername, String expassword, String other, String inputdate) {
        this.expropId = expropId;
        this.fogpropId = fogpropId;
        this.exwebDesc = exwebDesc;
        this.fogpropName = fogpropName;
        this.exwebCode = exwebCode;
        this.exusername = exusername;
        this.expassword = expassword;
        this.other = other;
        this.inputdate = inputdate;
    }

   
    // Property accessors

    public BigDecimal getKeyid() {
        return this.keyid;
    }
    
    public void setKeyid(BigDecimal keyid) {
        this.keyid = keyid;
    }

    public String getExpropId() {
        return this.expropId;
    }
    
    public void setExpropId(String expropId) {
        this.expropId = expropId;
    }

    public String getFogpropId() {
        return this.fogpropId;
    }
    
    public void setFogpropId(String fogpropId) {
        this.fogpropId = fogpropId;
    }

    public String getExwebDesc() {
        return this.exwebDesc;
    }
    
    public void setExwebDesc(String exwebDesc) {
        this.exwebDesc = exwebDesc;
    }

    public String getFogpropName() {
        return this.fogpropName;
    }
    
    public void setFogpropName(String fogpropName) {
        this.fogpropName = fogpropName;
    }

    public String getExwebCode() {
        return this.exwebCode;
    }
    
    public void setExwebCode(String exwebCode) {
        this.exwebCode = exwebCode;
    }

    public String getExusername() {
        return this.exusername;
    }
    
    public void setExusername(String exusername) {
        this.exusername = exusername;
    }

    public String getExpassword() {
        return this.expassword;
    }
    
    public void setExpassword(String expassword) {
        this.expassword = expassword;
    }

    public String getOther() {
        return this.other;
    }
    
    public void setOther(String other) {
        this.other = other;
    }

    public String getInputdate() {
        return this.inputdate;
    }
    
    public void setInputdate(String inputdate) {
        this.inputdate = inputdate;
    }
   








}