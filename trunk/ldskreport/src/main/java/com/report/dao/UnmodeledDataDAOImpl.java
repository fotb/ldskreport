package com.report.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.report.vo.UnmodeledDataVO;

public class UnmodeledDataDAOImpl extends HibernateDaoSupport implements
		IUnmodeledDataDAO {

	public Map findAllByMetaObjAttrRelationsIdn(String metaObjAttrRelationsIdn) {
		final List list = getHibernateTemplate().find(
				"from UnmodeledDataVO vo where vo.metaObjAttrRelationsIdn = ?",
				Integer.valueOf(metaObjAttrRelationsIdn));
		Map map = new HashMap();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			UnmodeledDataVO vo = (UnmodeledDataVO) iterator.next();
			map.put(String.valueOf(vo.getComputerIdn()), vo.getDataString());
		}
		return map;
	}
}
