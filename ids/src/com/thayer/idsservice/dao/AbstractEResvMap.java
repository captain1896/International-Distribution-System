package com.thayer.idsservice.dao;

import java.math.BigDecimal;
import java.util.Date;


/**
 * AbstractEResvMap entity provides the base persistence definition of the EResvMap entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractEResvMap  implements java.io.Serializable {


    // Fields    

     private Long id;
     private String fogCnfnum;
     private String agodaCnfnum;
     private String requesteddate;
     private Date createdate;
     private String creator;
     private BigDecimal bk1;
     private BigDecimal bk2;
     private Date bk3;//入住时间
     private Date bk4;
     private String bk5;
     private String bk6;
     private String priceInfo;//价格信息


    // Constructors

    /** default constructor */
    public AbstractEResvMap() {
    }

	/** minimal constructor */
    public AbstractEResvMap(String agodaCnfnum) {
        this.agodaCnfnum = agodaCnfnum;
    }
    
    /** full constructor */
    public AbstractEResvMap(String fogCnfnum, String agodaCnfnum, String requesteddate, Date createdate, String creator, BigDecimal bk1, BigDecimal bk2, Date bk3, Date bk4, String bk5, String bk6,String priceInfo) {
        this.fogCnfnum = fogCnfnum;
        this.agodaCnfnum = agodaCnfnum;
        this.requesteddate = requesteddate;
        this.createdate = createdate;
        this.creator = creator;
        this.bk1 = bk1;
        this.bk2 = bk2;
        this.bk3 = bk3;
        this.bk4 = bk4;
        this.bk5 = bk5;
        this.bk6 = bk6;
        this.priceInfo = priceInfo;
    }

   
    // Property accessors

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getFogCnfnum() {
        return this.fogCnfnum;
    }
    
    public void setFogCnfnum(String fogCnfnum) {
        this.fogCnfnum = fogCnfnum;
    }

    public String getAgodaCnfnum() {
        return this.agodaCnfnum;
    }
    
    public void setAgodaCnfnum(String agodaCnfnum) {
        this.agodaCnfnum = agodaCnfnum;
    }

    public String getRequesteddate() {
        return this.requesteddate;
    }
    
    public void setRequesteddate(String requesteddate) {
        this.requesteddate = requesteddate;
    }

    public Date getCreatedate() {
        return this.createdate;
    }
    
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getCreator() {
        return this.creator;
    }
    
    public void setCreator(String creator) {
        this.creator = creator;
    }

    public BigDecimal getBk1() {
        return this.bk1;
    }
    
    public void setBk1(BigDecimal bk1) {
        this.bk1 = bk1;
    }

    public BigDecimal getBk2() {
        return this.bk2;
    }
    
    public void setBk2(BigDecimal bk2) {
        this.bk2 = bk2;
    }

    public Date getBk3() {
        return this.bk3;
    }
    
    public void setBk3(Date bk3) {
        this.bk3 = bk3;
    }

    public Date getBk4() {
        return this.bk4;
    }
    
    public void setBk4(Date bk4) {
        this.bk4 = bk4;
    }

    public String getBk5() {
        return this.bk5;
    }
    
    public void setBk5(String bk5) {
        this.bk5 = bk5;
    }

    public String getBk6() {
        return this.bk6;
    }
    
    public void setBk6(String bk6) {
        this.bk6 = bk6;
    }

	public String getPriceInfo() {
		return priceInfo;
	}

	public void setPriceInfo(String priceInfo) {
		this.priceInfo = priceInfo;
	}
   








}