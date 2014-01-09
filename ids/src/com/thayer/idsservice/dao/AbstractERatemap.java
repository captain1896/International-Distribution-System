package com.thayer.idsservice.dao;

import java.math.BigDecimal;


/**
 * AbstractERatemap entity provides the base persistence definition of the ERatemap entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractERatemap  implements java.io.Serializable {


    // Fields    

     private BigDecimal keyid;
     private String exrateId;
     private String fograteId;
     private String exrateName;
     private String fograteName;
     private String exwebCode;
     private String fogpropId;


    // Constructors

    /** default constructor */
    public AbstractERatemap() {
    }

	/** minimal constructor */
    public AbstractERatemap(String exwebCode, String fogpropId) {
        this.exwebCode = exwebCode;
        this.fogpropId = fogpropId;
    }
    
    /** full constructor */
    public AbstractERatemap(String exrateId, String fograteId, String exrateName, String fograteName, String exwebCode, String fogpropId) {
        this.exrateId = exrateId;
        this.fograteId = fograteId;
        this.exrateName = exrateName;
        this.fograteName = fograteName;
        this.exwebCode = exwebCode;
        this.fogpropId = fogpropId;
    }

   
    // Property accessors

    public BigDecimal getKeyid() {
        return this.keyid;
    }
    
    public void setKeyid(BigDecimal keyid) {
        this.keyid = keyid;
    }

    public String getExrateId() {
        return this.exrateId;
    }
    
    public void setExrateId(String exrateId) {
        this.exrateId = exrateId;
    }

    public String getFograteId() {
        return this.fograteId;
    }
    
    public void setFograteId(String fograteId) {
        this.fograteId = fograteId;
    }

    public String getExrateName() {
        return this.exrateName;
    }
    
    public void setExrateName(String exrateName) {
        this.exrateName = exrateName;
    }

    public String getFograteName() {
        return this.fograteName;
    }
    
    public void setFograteName(String fograteName) {
        this.fograteName = fograteName;
    }

    public String getExwebCode() {
        return this.exwebCode;
    }
    
    public void setExwebCode(String exwebCode) {
        this.exwebCode = exwebCode;
    }

    public String getFogpropId() {
        return this.fogpropId;
    }
    
    public void setFogpropId(String fogpropId) {
        this.fogpropId = fogpropId;
    }
   








}