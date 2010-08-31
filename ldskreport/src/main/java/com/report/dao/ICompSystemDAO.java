package com.report.dao;

import java.util.Map;

public interface ICompSystemDAO {
	public int findCountByModel(String model);

	public Map findAllToMap();
}
