package com.thayer.idsservice.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;

/**
 * A data access object (DAO) providing persistence and search support for ERatemap entities. Transaction control of the
 * save(), update() and delete() operations can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.thayer.idsservice.dao.ERatemap
 * @author MyEclipse Persistence Tools
 */

public class ERatemapDAO extends HibernateDaoSupport implements Serializable {
	private static final Log log = LogFactory.getLog(ERatemapDAO.class);
	// property constants
	public static final String EXRATE_ID = "exrateId";
	public static final String FOGRATE_ID = "fograteId";
	public static final String EXRATE_NAME = "exrateName";
	public static final String FOGRATE_NAME = "fograteName";
	public static final String EXWEB_CODE = "exwebCode";
	public static final String FOGPROP_ID = "fogpropId";

	protected void initDao() {
		// do nothing
	}

	public void save(ERatemap transientInstance) {
		log.debug("saving ERatemap instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ERatemap persistentInstance) {
		log.debug("deleting ERatemap instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ERatemap findById(java.math.BigDecimal id) {
		log.debug("getting ERatemap instance with id: " + id);
		try {
			ERatemap instance = (ERatemap) getHibernateTemplate().get("com.thayer.idsservice.dao.ERatemap", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ERatemap instance) {
		log.debug("finding ERatemap instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByPara(String exwebCode,String exrateId, String fogpropId, String fogRoomId) {
		log.debug("finding ERatemap instance by example");
		try {

			DetachedCriteria criteria = DetachedCriteria.forClass(ERatemap.class);
			if (StringUtils.hasText(exwebCode)) {
				criteria.add(Restrictions.eq(EXWEB_CODE, exwebCode));
			} else {
				throw new RuntimeException("exwebCode is null!");
			}
			if (StringUtils.hasText(fogpropId)) {
				criteria.add(Restrictions.eq(FOGPROP_ID, fogpropId));
			} else {
				throw new RuntimeException("fogpropId is null!");
			}
			if (StringUtils.hasText(fogRoomId)) {
				criteria.add(Restrictions.like(FOGRATE_ID, fogRoomId, MatchMode.START));
			} else {
				throw new RuntimeException("fogRoomId is null!");
			}
			if (StringUtils.hasText(exrateId)) {
					criteria.add(Restrictions.eq(EXRATE_ID, exrateId));
			} else {
				throw new RuntimeException("exrateId is null!");
			}
			
			List results = getHibernateTemplate().findByCriteria(criteria);
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	public List findByProperty(String propertyName, Object value) {
		log.debug("finding ERatemap instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from ERatemap as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByExrateId(Object exrateId) {
		return findByProperty(EXRATE_ID, exrateId);
	}

	public List findByFograteId(Object fograteId) {
		return findByProperty(FOGRATE_ID, fograteId);
	}

	public List findByExrateName(Object exrateName) {
		return findByProperty(EXRATE_NAME, exrateName);
	}

	public List findByFograteName(Object fograteName) {
		return findByProperty(FOGRATE_NAME, fograteName);
	}

	public List findByExwebCode(Object exwebCode) {
		return findByProperty(EXWEB_CODE, exwebCode);
	}

	public List findByFogpropId(Object fogpropId) {
		return findByProperty(FOGPROP_ID, fogpropId);
	}

	public List findAll() {
		log.debug("finding all ERatemap instances");
		try {
			String queryString = "from ERatemap";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ERatemap merge(ERatemap detachedInstance) {
		log.debug("merging ERatemap instance");
		try {
			ERatemap result = (ERatemap) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ERatemap instance) {
		log.debug("attaching dirty ERatemap instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ERatemap instance) {
		log.debug("attaching clean ERatemap instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ERatemapDAO getFromApplicationContext(ApplicationContext ctx) {
		return (ERatemapDAO) ctx.getBean("ERatemapDAO");
	}
}