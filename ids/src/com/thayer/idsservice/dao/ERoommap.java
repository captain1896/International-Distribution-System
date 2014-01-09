package com.thayer.idsservice.dao;



/**
 * ERoommap entity. @author MyEclipse Persistence Tools
 */
public class ERoommap extends AbstractERoommap implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public ERoommap() {
    }

	/** minimal constructor */
    public ERoommap(String exwebCode, String fogpropId) {
        super(exwebCode, fogpropId);        
    }
    
    /** full constructor */
    public ERoommap(String exroomId, String fogroomId, String exroomName, String fogroomName, String exwebCode, String fogpropId, String other) {
        super(exroomId, fogroomId, exroomName, fogroomName, exwebCode, fogpropId, other);        
    }
   
}
