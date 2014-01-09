package com.thayer.idsservice.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for EPropmap entities. Transaction control of the
 * save(), update() and delete() operations can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.thayer.idsservice.dao.EPropmap
 * @author MyEclipse Persistence Tools
 */

public class EPropmapDAO extends HibernateDaoSupport implements Serializable {
	private static final Log log = LogFactory.getLog(EPropmapDAO.class);
	// property constants
	public static final String EXPROP_ID = "expropId";
	public static final String FOGPROP_ID = "fogpropId";
	public static final String EXWEB_DESC = "exwebDesc";
	public static final String FOGPROP_NAME = "fogpropName";
	public static final String EXWEB_CODE = "exwebCode";
	public static final String EXUSERNAME = "exusername";
	public static final String EXPASSWORD = "expassword";
	public static final String OTHER = "other";
	public static final String INPUTDATE = "inputdate";

	protected void initDao() {
		// do nothing
	}

	public void save(EPropmap transientInstance) {
		log.debug("saving EPropmap instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(EPropmap persistentInstance) {
		log.debug("deleting EPropmap instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public EPropmap findById(java.math.BigDecimal id) {
		log.debug("getting EPropmap instance with id: " + id);
		try {
			EPropmap instance = (EPropmap) getHibernateTemplate().get("com.thayer.idsservice.dao.EPropmap", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(EPropmap instance) {
		log.debug("finding EPropmap instance by example");
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
		log.debug("finding EPropmap instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from EPropmap as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByExpropId(Object expropId) {
		return findByProperty(EXPROP_ID, expropId);
	}

	public List findByFogpropId(Object fogpropId) {
		return findByProperty(FOGPROP_ID, fogpropId);
	}

	public List findByExwebDesc(Object exwebDesc) {
		return findByProperty(EXWEB_DESC, exwebDesc);
	}

	public List findByFogpropName(Object fogpropName) {
		return findByProperty(FOGPROP_NAME, fogpropName);
	}

	public List findByExwebCode(Object exwebCode) {
		return findByProperty(EXWEB_CODE, exwebCode);
	}

	public List findByExusername(Object exusername) {
		return findByProperty(EXUSERNAME, exusername);
	}

	public List findByExpassword(Object expassword) {
		return findByProperty(EXPASSWORD, expassword);
	}

	public List findByOther(Object other) {
		return findByProperty(OTHER, other);
	}

	public List findByInputdate(Object inputdate) {
		return findByProperty(INPUTDATE, inputdate);
	}

	public List findAll() {
		log.debug("finding all EPropmap instances");
		try {
			String queryString = "from EPropmap";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public EPropmap merge(EPropmap detachedInstance) {
		log.debug("merging EPropmap instance");
		try {
			EPropmap result = (EPropmap) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(EPropmap instance) {
		log.debug("attaching dirty EPropmap instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(EPropmap instance) {
		log.debug("attaching clean EPropmap instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static EPropmapDAO getFromApplicationContext(ApplicationContext ctx) {
		return (EPropmapDAO) ctx.getBean("EPropmapDAO");
	}
}