package com.thayer.idsservice.dao;

import java.math.BigDecimal;


/**
 * AbstractERoommap entity provides the base persistence definition of the ERoommap entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractERoommap  implements java.io.Serializable {


    // Fields    

     private BigDecimal keyid;
     private String exroomId;
     private String fogroomId;
     private String exroomName;
     private String fogroomName;
     private String exwebCode;
     private String fogpropId;
     private String other;


    // Constructors

    /** default constructor */
    public AbstractERoommap() {
    }

	/** minimal constructor */
    public AbstractERoommap(String exwebCode, String fogpropId) {
        this.exwebCode = exwebCode;
        this.fogpropId = fogpropId;
    }
    
    /** full constructor */
    public AbstractERoommap(String exroomId, String fogroomId, String exroomName, String fogroomName, String exwebCode, String fogpropId, String other) {
        this.exroomId = exroomId;
        this.fogroomId = fogroomId;
        this.exroomName = exroomName;
        this.fogroomName = fogroomName;
        this.exwebCode = exwebCode;
        this.fogpropId = fogpropId;
        this.other = other;
    }

   
    // Property accessors

    public BigDecimal getKeyid() {
        return this.keyid;
    }
    
    public void setKeyid(BigDecimal keyid) {
        this.keyid = keyid;
    }

    public String getExroomId() {
        return this.exroomId;
    }
    
    public void setExroomId(String exroomId) {
        this.exroomId = exroomId;
    }

    public String getFogroomId() {
        return this.fogroomId;
    }
    
    public void setFogroomId(String fogroomId) {
        this.fogroomId = fogroomId;
    }

    public String getExroomName() {
        return this.exroomName;
    }
    
    public void setExroomName(String exroomName) {
        this.exroomName = exroomName;
    }

    public String getFogroomName() {
        return this.fogroomName;
    }
    
    public void setFogroomName(String fogroomName) {
        this.fogroomName = fogroomName;
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

    public String getOther() {
        return this.other;
    }
    
    public void setOther(String other) {
        this.other = other;
    }
   








}