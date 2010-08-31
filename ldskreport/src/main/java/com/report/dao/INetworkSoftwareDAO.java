package com.report.dao;

import java.util.Map;

public interface INetworkSoftwareDAO {
	/**
	 * Find all data to map
	 * key:computerIdn
	 * value:nicAddress
	 * @return
	 */
	Map findAll();
}
