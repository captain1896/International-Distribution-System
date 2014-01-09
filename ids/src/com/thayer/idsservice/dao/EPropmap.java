package com.thayer.idsservice.dao;



/**
 * EPropmap entity. @author MyEclipse Persistence Tools
 */
public class EPropmap extends AbstractEPropmap implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public EPropmap() {
    }

	/** minimal constructor */
    public EPropmap(String expropId) {
        super(expropId);        
    }
    
    /** full constructor */
    public EPropmap(String expropId, String fogpropId, String exwebDesc, String fogpropName, String exwebCode, String exusername, String expassword, String other, String inputdate) {
        super(expropId, fogpropId, exwebDesc, fogpropName, exwebCode, exusername, expassword, other, inputdate);        
    }
   
}
