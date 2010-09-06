package com.report.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SafeReportDTO implements Serializable {

	private static final long serialVersionUID = -7576680445895779033L;

	private String branchName;
	private int hipsActionCount = 0;
	private int deviceControlActionCount = 0;
	private Map hipsActionMap = new HashMap();
	private Map deviceControlActionMap = new HashMap();

	public int getHipsActionCount() {
		return hipsActionCount;
	}

	public void setHipsActionCount(int hipsActionCount) {
		this.hipsActionCount = hipsActionCount;
	}

	public int getDeviceControlActionCount() {
		return deviceControlActionCount;
	}

	public void setDeviceControlActionCount(int deviceControlActionCount) {
		this.deviceControlActionCount = deviceControlActionCount;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Map getHipsActionMap() {
		return hipsActionMap;
	}

	public void setHipsActionMap(Map hipsActionMap) {
		this.hipsActionMap = hipsActionMap;
	}

	public Map getDeviceControlActionMap() {
		return deviceControlActionMap;
	}

	public void setDeviceControlActionMap(Map deviceControlActionMap) {
		this.deviceControlActionMap = deviceControlActionMap;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((branchName == null) ? 0 : branchName.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SafeReportDTO other = (SafeReportDTO) obj;
		if (branchName == null) {
			if (other.branchName != null)
				return false;
		} else if (!branchName.equals(other.branchName))
			return false;
		return true;
	}

}
