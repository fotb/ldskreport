package com.report.bo;

import java.util.List;
import java.util.Map;

public interface IDeviceControlActionBO {
	List findAll();

	Map getDeviceControlActionWithActionCode();

	Map getHipsActionGroupByComputerIdn();
}
