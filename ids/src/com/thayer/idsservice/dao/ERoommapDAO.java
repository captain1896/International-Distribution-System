package com.thayer.idsservice.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for ERoommap entities. Transaction control of the
 * save(), update() and delete() operations can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.thayer.idsservice.dao.ERoommap
 * @author MyEclipse Persistence Tools
 */

public class ERoommapDAO extends HibernateDaoSupport implements Serializable {
	private static final Log log = LogFactory.getLog(ERoommapDAO.class);
	// property constants
	public static final String EXROOM_ID = "exroomId";
	public static final String FOGROOM_ID = "fogroomId";
	public static final String EXROOM_NAME = "exroomName";
	public static final String FOGROOM_NAME = "fogroomName";
	public static final String EXWEB_CODE = "exwebCode";
	public static final String FOGPROP_ID = "fogpropId";
	public static final String OTHER = "other";

	protected void initDao() {
		// do nothing
	}

	public void save(ERoommap transientInstance) {
		log.debug("saving ERoommap instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ERoommap persistentInstance) {
		log.debug("deleting ERoommap instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ERoommap findById(java.math.BigDecimal id) {
		log.debug("getting ERoommap instance with id: " + id);
		try {
			ERoommap instance = (ERoommap) getHibernateTemplate().get("com.thayer.idsservice.dao.ERoommap", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ERoommap instance) {
		log.debug("finding ERoommap instance by example");
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
		log.debug("finding ERoommap instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from ERoommap as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByExroomId(Object exroomId) {
		return findByProperty(EXROOM_ID, exroomId);
	}

	public List findByFogroomId(Object fogroomId) {
		return findByProperty(FOGROOM_ID, fogroomId);
	}

	public List findByExroomName(Object exroomName) {
		return findByProperty(EXROOM_NAME, exroomName);
	}

	public List findByFogroomName(Object fogroomName) {
		return findByProperty(FOGROOM_NAME, fogroomName);
	}

	public List findByExwebCode(Object exwebCode) {
		return findByProperty(EXWEB_CODE, exwebCode);
	}

	public List findByFogpropId(Object fogpropId) {
		return findByProperty(FOGPROP_ID, fogpropId);
	}

	public List findByOther(Object other) {
		return findByProperty(OTHER, other);
	}

	public List findAll() {
		log.debug("finding all ERoommap instances");
		try {
			String queryString = "from ERoommap";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ERoommap merge(ERoommap detachedInstance) {
		log.debug("merging ERoommap instance");
		try {
			ERoommap result = (ERoommap) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ERoommap instance) {
		log.debug("attaching dirty ERoommap instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ERoommap instance) {
		log.debug("attaching clean ERoommap instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ERoommapDAO getFromApplicationContext(ApplicationContext ctx) {
		return (ERoommapDAO) ctx.getBean("ERoommapDAO");
	}
}