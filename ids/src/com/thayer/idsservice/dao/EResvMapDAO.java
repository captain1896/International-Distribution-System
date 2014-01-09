package com.thayer.idsservice.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * A data access object (DAO) providing persistence and search support for EResvMap entities. Transaction control of the
 * save(), update() and delete() operations can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.thayer.idsservice.dao.EResvMap
 * @author MyEclipse Persistence Tools
 */

public class EResvMapDAO extends HibernateDaoSupport implements Serializable {
	private static final Log log = LogFactory.getLog(EResvMapDAO.class);
	// property constants
	public static final String FOG_CNFNUM = "fogCnfnum";
	public static final String AGODA_CNFNUM = "agodaCnfnum";
	public static final String REQUESTEDDATE = "requesteddate";
	public static final String CREATOR = "creator";
	public static final String BK5 = "bk5";
	public static final String BK6 = "bk6";

	protected void initDao() {
		// do nothing
	}

	public void save(EResvMap transientInstance) {
		log.debug("saving EResvMap instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(EResvMap persistentInstance) {
		log.debug("deleting EResvMap instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public EResvMap findById(java.math.BigDecimal id) {
		log.debug("getting EResvMap instance with id: " + id);
		try {
			EResvMap instance = (EResvMap) getHibernateTemplate().get("com.thayer.idsservice.dao.EResvMap", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(EResvMap instance) {
		log.debug("finding EResvMap instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<EResvMap> findPendingResv(EResvMap instance) {
		log.debug("finding EResvMap instance by example");
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(EResvMap.class);
			criteria.add(Restrictions.eq(BK5, instance.getBk5()));
			criteria.add(Restrictions.eq(BK6, instance.getBk6()));
			criteria.add(Restrictions.lt("createdate", instance.getCreatedate()));
			return getHibernateTemplate().findByCriteria(criteria);
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding EResvMap instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from EResvMap as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByFogCnfnum(Object fogCnfnum) {
		return findByProperty(FOG_CNFNUM, fogCnfnum);
	}

	public List findByAgodaCnfnum(Object agodaCnfnum) {
		return findByProperty(AGODA_CNFNUM, agodaCnfnum);
	}

	public List findByRequesteddate(Object requesteddate) {
		return findByProperty(REQUESTEDDATE, requesteddate);
	}

	public List findByCreator(Object creator) {
		return findByProperty(CREATOR, creator);
	}

	public List findByBk5(Object bk5) {
		return findByProperty(BK5, bk5);
	}

	public List findByBk6(Object bk6) {
		return findByProperty(BK6, bk6);
	}

	public List findAll() {
		log.debug("finding all EResvMap instances");
		try {
			String queryString = "from EResvMap";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public EResvMap merge(EResvMap detachedInstance) {
		log.debug("merging EResvMap instance");
		try {
			EResvMap result = (EResvMap) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(EResvMap instance) {
		log.debug("attaching dirty EResvMap instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(EResvMap instance) {
		log.debug("attaching clean EResvMap instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static EResvMapDAO getFromApplicationContext(ApplicationContext ctx) {
		return (EResvMapDAO) ctx.getBean("EResvMapDAO");
	}
	
	/**
	 * 得到没有FOG订单号的
	 * 没有FOG订单号 认为下单失败
	 * @date 2013-12-11
	 * @param iata
	 * @return
	 */
	public List<EResvMap> getNoFogNumByIata(String iata){
		String  queryString = "from EResvMap as model where model.fogCnfnum is null and model.bk5 = ?";
		return getHibernateTemplate().find(queryString, iata);
	}
}