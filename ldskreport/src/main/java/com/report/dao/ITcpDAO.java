package com.report.dao;

import java.util.Map;

public interface ITcpDAO {
	/**
	 * Find all data to map
	 * key:computeIdn
	 * value:TcpVO
	 * @return
	 */
	Map findAll();
}
