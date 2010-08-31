package com.report.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ComputerDAOImpl extends HibernateDaoSupport implements IComputerDAO {

	public int findAllCount() {
		Long count = (Long)getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate( Session session) throws HibernateException,SQLException {
				Query query = session.createQuery("select count(*) from ComputerVO");
				return ((Long)query.iterate().next());
			}
		});
		return count.intValue();
	}

	public int findCountByModel(String model) {
		final List list = getHibernateTemplate().find("from ComputerVO where model like ?", "%" + model + "%");
		return list.size();
	}
	
	public List findAll() {
		return getHibernateTemplate().find("from ComputerVO");
	}
	
}
