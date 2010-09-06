package com.report.bo;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.report.util.AppException;

public interface ISafeReport {
	HSSFWorkbook renderExcelReport() throws AppException;
}
