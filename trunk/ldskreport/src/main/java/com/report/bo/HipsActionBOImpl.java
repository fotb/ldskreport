package com.report.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.report.dao.IHipsActionDAO;
import com.report.vo.HipsActionVO;

public class HipsActionBOImpl implements IHipsActionBO {
	private IHipsActionDAO hipsActionDAO;

	public IHipsActionDAO getHipsActionDAO() {
		return hipsActionDAO;
	}

	public void setHipsActionDAO(IHipsActionDAO hipsActionDAO) {
		this.hipsActionDAO = hipsActionDAO;
	}

	public List findAll() {
		return hipsActionDAO.findAll();
	}

	public Map getHipsActionWithActionCode() {
		Map map = new HashMap();
		final List list = hipsActionDAO.findAll();
		for (final Iterator iter = list.iterator(); iter.hasNext();) {
			final HipsActionVO vo = (HipsActionVO) iter.next();
			if (map.containsKey(vo.getActionCode())) {
				final List temp = (List) map.get(vo.getActionCode());
				temp.add(vo);
			} else {
				List temp = new ArrayList();
				temp.add(vo);
				map.put(vo.getActionCode(), vo);
			}
		}
		return map;
	}
}
