package com.report.bo;

import java.util.List;
import java.util.Map;

public interface IHipsActionBO {
	List findAll();

	Map getHipsActionGroupByComputerIdn();

	Map getHipsActionWithActionCode();
}
