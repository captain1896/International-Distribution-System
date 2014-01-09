package com.thayer.idsservice.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;

import com.thayer.idsservice.bean.MsgLogQueryBean;
import com.thayer.idsservice.util.DateUtil;

/**
 * A data access object (DAO) providing persistence and search support for EMsgLog entities. Transaction control of the
 * save(), update() and delete() operations can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.thayer.idsservice.dao.EMsgLog
 * @author MyEclipse Persistence Tools
 */

public class EMsgLogDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(EMsgLogDAO.class);
	// property constants
	public static final String EXWEB_CODE = "exwebCode";
	public static final String MSG_ID = "msgId";
	public static final String MSG_STATUS = "msgStatus";
	public static final String MSG_RQ = "msgRq";
	public static final String MSG_RS = "msgRs";
	public static final String MSG_RS_FINAL = "msgRsFinal";
	public static final String MSG_ERRORCODE = "msgErrorcode";
	public static final String MSG_ERROR = "msgError";

	protected void initDao() {
		// do nothing
	}

	public void save(EMsgLog transientInstance) {
		log.debug("saving EMsgLog instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(EMsgLog persistentInstance) {
		log.debug("deleting EMsgLog instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public EMsgLog findById(java.lang.Integer id) {
		log.debug("getting EMsgLog instance with id: " + id);
		try {
			EMsgLog instance = (EMsgLog) getHibernateTemplate().get("com.thayer.idsservice.dao.EMsgLog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(EMsgLog instance) {
		log.debug("finding EMsgLog instance by example");
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
		log.debug("finding EMsgLog instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from EMsgLog as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByExwebCode(Object exwebCode) {
		return findByProperty(EXWEB_CODE, exwebCode);
	}

	public List findByMsgId(Object msgId) {
		return findByProperty(MSG_ID, msgId);
	}

	public List findByMsgStatus(Object msgStatus) {
		return findByProperty(MSG_STATUS, msgStatus);
	}

	public List findByMsgRq(Object msgRq) {
		return findByProperty(MSG_RQ, msgRq);
	}

	public List findByMsgRs(Object msgRs) {
		return findByProperty(MSG_RS, msgRs);
	}

	public List findByMsgRsFinal(Object msgRsFinal) {
		return findByProperty(MSG_RS_FINAL, msgRsFinal);
	}

	public List findByMsgErrorcode(Object msgErrorcode) {
		return findByProperty(MSG_ERRORCODE, msgErrorcode);
	}

	public List findByMsgError(Object msgError) {
		return findByProperty(MSG_ERROR, msgError);
	}

	public List findAll() {
		log.debug("finding all EMsgLog instances");
		try {
			String queryString = "from EMsgLog";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public EMsgLog merge(EMsgLog detachedInstance) {
		log.debug("merging EMsgLog instance");
		try {
			EMsgLog result = (EMsgLog) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(EMsgLog instance) {
		log.debug("attaching dirty EMsgLog instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(EMsgLog instance) {
		log.debug("attaching clean EMsgLog instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/**
	 * 根据查询类 查询Log
	 * 
	 * @param resvInfoQueryBean
	 * @return
	 */
	public List<EMsgLog> findMsgLogsByQuery(MsgLogQueryBean logQueryBean) {
		if (logQueryBean == null) {
			return new ArrayList<EMsgLog>();
		}
		DetachedCriteria criteria = buildLogQuery(logQueryBean);

		List<EMsgLog> result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}

	/**
	 * 根据查询类 分页查询Log
	 * 
	 * @param resvInfoQueryBean
	 * @return
	 */
	public List<EMsgLog> findMsgLogsByQuery(MsgLogQueryBean logQueryBean, int rowfrom, int rowto) {
		if (logQueryBean == null) {
			return new ArrayList<EMsgLog>();
		}
		DetachedCriteria criteria = buildLogQuery(logQueryBean);

		List<EMsgLog> result = getHibernateTemplate().findByCriteria(criteria);
		return result;
	}

	/**
	 * 根据查询类 查询Log记录数
	 * 
	 * @param resvInfoQueryBean
	 * @return
	 */
	public Integer findMsgLogsCountByQuery(MsgLogQueryBean logQueryBean) {
		if (logQueryBean == null) {
			return 0;
		}
		DetachedCriteria criteria = buildLogQuery(logQueryBean);
		criteria.setProjection(Projections.rowCount());
		return (Integer) getHibernateTemplate().findByCriteria(criteria).get(0);
	}

	/**
	 * @param resvInfoQueryBean
	 * @return
	 */
	private DetachedCriteria buildLogQuery(MsgLogQueryBean logQueryBean) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EMsgLog.class);
		if (StringUtils.hasText(logQueryBean.getExwebCode())) {
			criteria.add(Restrictions.eq(EMsgLogDAO.EXWEB_CODE, logQueryBean.getExwebCode()));
		}

		if (StringUtils.hasText(logQueryBean.getMsgId())) {
			criteria.add(Restrictions.eq(EMsgLogDAO.MSG_ID, logQueryBean.getMsgId()));
		}

		if (StringUtils.hasText(logQueryBean.getMsgStatus())) {
			criteria.add(Restrictions.eq(EMsgLogDAO.MSG_STATUS, logQueryBean.getMsgStatus()));
		}

		if (logQueryBean.getCreatetimeSince() != null) {
			criteria.add(Restrictions.gt("createtime", logQueryBean.getCreatetimeSince()));
		}

		if (logQueryBean.getCreatetimeTill() != null) {
			criteria.add(Restrictions.lt("createtime", logQueryBean.getCreatetimeTill()));
		}

		if (StringUtils.hasText(logQueryBean.getCreatetimeQuerySince())) {
			criteria.add(Restrictions.gt("createtime", DateUtil.dateValue(logQueryBean.getCreatetimeQuerySince())));
		}

		if (StringUtils.hasText(logQueryBean.getCreatetimeQueryTill())) {
			criteria.add(Restrictions.lt("createtime", DateUtil.dateValue(logQueryBean.getCreatetimeQueryTill())));
		}

		if (StringUtils.hasText(logQueryBean.getExpropId())) {
			criteria.add(Restrictions.eq("expropId", logQueryBean.getExpropId()));
		}

		if (StringUtils.hasText(logQueryBean.getFogpropId())) {
			criteria.add(Restrictions.eq("fogpropId", logQueryBean.getFogpropId()));
		}

		if (StringUtils.hasText(logQueryBean.getMsgErrorcode())) {
			criteria.add(Restrictions.eq(EMsgLogDAO.MSG_ERRORCODE, logQueryBean.getMsgErrorcode()));
		}
		return criteria;
	}

	public static EMsgLogDAO getFromApplicationContext(ApplicationContext ctx) {
		return (EMsgLogDAO) ctx.getBean("EMsgLogDAO");
	}
}