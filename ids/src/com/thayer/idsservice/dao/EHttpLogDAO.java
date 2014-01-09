package com.thayer.idsservice.dao;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class EHttpLogDAO  extends HibernateDaoSupport implements Serializable {
	private static final Log log = LogFactory.getLog(EHttpLogDAO.class);
	
	public void save(EHttpLog transientInstance) {
		log.debug("saving EPropmap instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
}
