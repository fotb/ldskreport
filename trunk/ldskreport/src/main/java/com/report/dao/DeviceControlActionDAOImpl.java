package com.report.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class DeviceControlActionDAOImpl extends HibernateDaoSupport implements
		IDeviceControlActionDAO {

	public List findAll() {
		return getHibernateTemplate().find("from DeviceControlActionVO order by computerIdn");
	}

}
