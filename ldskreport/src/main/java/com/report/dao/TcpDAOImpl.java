package com.report.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.report.vo.TcpVO;

public class TcpDAOImpl extends HibernateDaoSupport implements ITcpDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.report.dao.ITcpDAO#findAll()
	 */
	public Map findAll() {
		final List list = getHibernateTemplate().find("from TcpVO");
		Map map = new HashMap();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			TcpVO vo = (TcpVO) iterator.next();
			map.put(String.valueOf(vo.getComputerIdn()), vo);
		}
		return map;
	}

}
