package com.report.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HipsActionDAOImpl extends HibernateDaoSupport implements
		IHipsActionDAO {

	public List findAll() {
		return getHibernateTemplate().find("from HipsActionVO order by computerIdn");
	}
}
