package com.report.dao;

import java.util.List;
import java.util.Map;

public interface IComputerDAO {
	int findAllCount();
	int findCountByModel(String model);
	List findAll();
	Map findAllToMap();
}
