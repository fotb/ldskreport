package com.report.bo;

import java.util.List;

import com.report.dao.IDeviceControlActionDAO;

public class DeviceControlActionBOImpl implements IDeviceControlActionBO {
	private IDeviceControlActionDAO deviceControlActionDAO;

	public IDeviceControlActionDAO getDeviceControlActionDAO() {
		return deviceControlActionDAO;
	}

	public void setDeviceControlActionDAO(
			IDeviceControlActionDAO deviceControlActionDAO) {
		this.deviceControlActionDAO = deviceControlActionDAO;
	}

	public List findAll() {
		return deviceControlActionDAO.findAll();
	}

}
