package com.thayer.idsservice.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.thayer.idsservice.util.DateUtil;

public class EIdsCacheDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(EIdsCacheDAO.class);
	public static final String IDS_TYPE = "eIdsCacheKey.idsType";
	public static final String PLAN_CODE = "eIdsCacheKey.planCode";
	public static final String PROP = "eIdsCacheKey.prop";
	public static final String AVAIL_DATE = "eIdsCacheKey.availDate";

	public void save(EIdsCache transientInstance) {
		log.debug("saving EExwebinfo instance");
		try {
			Calendar c = Calendar.getInstance();
			Calendar tempc = Calendar.getInstance();
			if (transientInstance != null) {
				// Date date = ;
				transientInstance.geteIdsCacheKey().setAvailDate(
						DateUtil.dateValue(DateUtil
								.formatDate(transientInstance.geteIdsCacheKey()
										.getAvailDate())));
				transientInstance.setCreateDate(c.getTime());
				transientInstance.setUpdateDate(c.getTime());
				getHibernateTemplate().save(transientInstance);
				log.debug("save successful");
			}
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void update(EIdsCache transientInstance) {
		log.debug("saving EExwebinfo instance");
		try {
			Calendar c = Calendar.getInstance();
			if (transientInstance != null) {
				transientInstance.geteIdsCacheKey().setAvailDate(
						DateUtil.dateValue(DateUtil
								.formatDate(transientInstance.geteIdsCacheKey()
										.getAvailDate())));
				transientInstance.setUpdateDate(c.getTime());
				getHibernateTemplate().saveOrUpdate(transientInstance);
				log.debug("update successful");
			}
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public EIdsCache findByPK(String idsType, String planCode, String prop,
			Date availDate) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EIdsCache.class);
		if (StringUtils.isBlank(idsType) || StringUtils.isBlank(planCode)
				|| StringUtils.isBlank(prop) || availDate == null) {
			return null;
		}
		if (StringUtils.isNotBlank(idsType)) {
			criteria.add(Restrictions.eq(IDS_TYPE, idsType));
		}
		if (StringUtils.isNotBlank(planCode)) {
			criteria.add(Restrictions.eq(PLAN_CODE, planCode));
		}
		if (StringUtils.isNotBlank(prop)) {
			criteria.add(Restrictions.eq(PROP, prop));
		}
		if (availDate != null) {
			criteria.add(Restrictions.eq(AVAIL_DATE, DateUtil.dateValue(DateUtil.formatDate(availDate))));
		}
		List<EIdsCache> list = getHibernateTemplate().findByCriteria(criteria);
		if (list != null && list.size() == 1) {
			return list.get(0);

		}
		return null;
	}

	public List<EIdsCache> findByProperty(String idsType, String planCode,
			String prop, Date begin, Date end) {
		log.debug("finding from E_IDS_CACHE instance with property: "
				+ "idsType" + ", value: " + idsType + "planCode" + ", value: "
				+ planCode + "prop" + ", value: " + prop + "begin"
				+ ", value: " + begin + "end" + ", value: " + end);
		try {

			DetachedCriteria criteria = DetachedCriteria
					.forClass(EIdsCache.class);
			if (StringUtils.isNotBlank(idsType)) {
				criteria.add(Restrictions.eq(IDS_TYPE, idsType));
			}
			if (StringUtils.isNotBlank(planCode)) {
				criteria.add(Restrictions.eq(PLAN_CODE, planCode));
			}
			if (StringUtils.isNotBlank(prop)) {
				criteria.add(Restrictions.eq(PROP, prop));
			}
			if (begin != null) {
				criteria.add(Restrictions.ge(AVAIL_DATE, DateUtil.dateValue(DateUtil.formatDate(begin))));
			}
			if (end != null) {
				criteria.add(Restrictions.le(AVAIL_DATE, DateUtil.dateValue(DateUtil.formatDate(end))));
			}
			return getHibernateTemplate().findByCriteria(criteria);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

}
