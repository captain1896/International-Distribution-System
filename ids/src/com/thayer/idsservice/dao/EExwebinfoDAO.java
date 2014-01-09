package com.thayer.idsservice.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for EExwebinfo entities. Transaction control of
 * the save(), update() and delete() operations can directly support Spring container-managed transactions or they can
 * be augmented to handle user-managed Spring transactions. Each of these methods provides additional information for
 * how to configure it for the desired type of transaction control.
 * 
 * @see com.thayer.idsservice.dao.EExwebinfo
 * @author MyEclipse Persistence Tools
 */

public class EExwebinfoDAO extends HibernateDaoSupport implements Serializable {
	private static final Log log = LogFactory.getLog(EExwebinfoDAO.class);
	// property constants
	public static final String EXWEB_CODE = "exwebCode";
	public static final String SERVICE_URL = "serviceUrl";
	public static final String DESCTION = "desction";
	public static final String EXUSER = "exuser";
	public static final String EXPASSWORD = "expassword";
	public static final String CREATEBY = "createby";

	protected void initDao() {
		// do nothing
	}

	public void save(EExwebinfo transientInstance) {
		log.debug("saving EExwebinfo instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(EExwebinfo persistentInstance) {
		log.debug("deleting EExwebinfo instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public EExwebinfo findById(java.math.BigDecimal id) {
		log.debug("getting EExwebinfo instance with id: " + id);
		try {
			EExwebinfo instance = (EExwebinfo) getHibernateTemplate().get("com.thayer.idsservice.dao.EExwebinfo", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(EExwebinfo instance) {
		log.debug("finding EExwebinfo instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding EExwebinfo instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from EExwebinfo as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByExwebCode(Object exwebCode) {
		return findByProperty(EXWEB_CODE, exwebCode);
	}

	public List findByServiceUrl(Object serviceUrl) {
		return findByProperty(SERVICE_URL, serviceUrl);
	}

	public List findByDesction(Object desction) {
		return findByProperty(DESCTION, desction);
	}

	public List findByExuser(Object exuser) {
		return findByProperty(EXUSER, exuser);
	}

	public List findByExpassword(Object expassword) {
		return findByProperty(EXPASSWORD, expassword);
	}

	public List findByCreateby(Object createby) {
		return findByProperty(CREATEBY, createby);
	}

	public List findAll() {
		log.debug("finding all EExwebinfo instances");
		try {
			String queryString = "from EExwebinfo";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public EExwebinfo merge(EExwebinfo detachedInstance) {
		log.debug("merging EExwebinfo instance");
		try {
			EExwebinfo result = (EExwebinfo) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(EExwebinfo instance) {
		log.debug("attaching dirty EExwebinfo instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(EExwebinfo instance) {
		log.debug("attaching clean EExwebinfo instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static EExwebinfoDAO getFromApplicationContext(ApplicationContext ctx) {
		return (EExwebinfoDAO) ctx.getBean("EExwebinfoDAO");
	}
}