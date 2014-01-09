package com.thayer.idsservice.dao;

import java.util.Date;


/**
 * EMsgLog entity. @author MyEclipse Persistence Tools
 */
public class EHttpLog  implements java.io.Serializable {

	private Integer requid;
	private Date requtime;
	private Date resptime;
	private String idstype;
	private String timeout;

    /** default constructor */
    public EHttpLog() {
    }

	public EHttpLog(Integer requid, Date requtime, Date resptime,
			String idstype, String timeout) {
		super();
		this.requid = requid;
		this.requtime = requtime;
		this.resptime = resptime;
		this.idstype = idstype;
		this.timeout = timeout;
	}

	public Integer getRequid() {
		return requid;
	}

	public void setRequid(Integer requid) {
		this.requid = requid;
	}

	public Date getRequtime() {
		return requtime;
	}

	public void setRequtime(Date requtime) {
		this.requtime = requtime;
	}

	public Date getResptime() {
		return resptime;
	}

	public void setResptime(Date resptime) {
		this.resptime = resptime;
	}

	public String getIdstype() {
		return idstype;
	}

	public void setIdstype(String idstype) {
		this.idstype = idstype;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
    
    
    
    


   
}
