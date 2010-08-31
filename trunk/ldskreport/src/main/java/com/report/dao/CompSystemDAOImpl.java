package com.report.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.report.vo.CompSystemVO;

public class CompSystemDAOImpl extends HibernateDaoSupport implements
		ICompSystemDAO {

	public Map findAllToMap() {
		final List list = getHibernateTemplate().find("from CompSystemVO");
		Map map = new HashMap();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			CompSystemVO vo = (CompSystemVO) iterator.next();
			map.put(String.valueOf(vo.getComputerIdn()), vo);
		}
		return map;
	}

	public int findCountByModel(String model) {
		final List list = getHibernateTemplate().find(
				"from CompSystemVO where manufacturer like ?", "%" + model + "%");
		return list.size();
	}

}
