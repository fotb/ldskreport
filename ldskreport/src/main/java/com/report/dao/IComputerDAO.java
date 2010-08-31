package com.report.dao;

import java.util.List;

public interface IComputerDAO {
	int findAllCount();
	int findCountByModel(String model);
	List findAll();
}
