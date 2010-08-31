package com.report.bo;

import java.util.Map;

import com.report.util.AppException;

public interface IReportBO {
	int findAllComputerCount();

	Map findAllMacAddress();

	Map findAllIpAddress();

	Map findAllByMetaObjAttrRelationsIdn(String metaObjAttrRelationsIdn);

	Map getAllComputerWitchBranch() throws AppException;

}
