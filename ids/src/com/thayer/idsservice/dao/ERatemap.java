package com.thayer.idsservice.dao;



/**
 * ERatemap entity. @author MyEclipse Persistence Tools
 */
public class ERatemap extends AbstractERatemap implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public ERatemap() {
    }

	/** minimal constructor */
    public ERatemap(String exwebCode, String fogpropId) {
        super(exwebCode, fogpropId);        
    }
    
    /** full constructor */
    public ERatemap(String exrateId, String fograteId, String exrateName, String fograteName, String exwebCode, String fogpropId) {
        super(exrateId, fograteId, exrateName, fograteName, exwebCode, fogpropId);        
    }
   
}
