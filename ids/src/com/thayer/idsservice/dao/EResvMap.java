package com.thayer.idsservice.dao;

import java.math.BigDecimal;
import java.util.Date;


/**
 * EResvMap entity. @author MyEclipse Persistence Tools
 */
public class EResvMap extends AbstractEResvMap implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public EResvMap() {
    }

	/** minimal constructor */
    public EResvMap(String agodaCnfnum) {
        super(agodaCnfnum);        
    }
    
    /** full constructor */
    public EResvMap(String fogCnfnum, String agodaCnfnum, String requesteddate, Date createdate, String creator, BigDecimal bk1, BigDecimal bk2, Date bk3, Date bk4, String bk5, String bk6,String priceInfo) {
        super(fogCnfnum, agodaCnfnum, requesteddate, createdate, creator, bk1, bk2, bk3, bk4, bk5, bk6,priceInfo);        
    }
   
}
