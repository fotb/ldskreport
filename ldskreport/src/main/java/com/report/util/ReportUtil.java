package com.report.util;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.report.bo.ISafeReport;

public class ReportUtil {
	private static final Logger logger = Logger.getLogger(ReportUtil.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			final IComputerReport bo = (IComputerReport) SpringUtil.getBean("computerReport");
//			final HSSFWorkbook workbook = bo.renderExcelReport();
//			if(null != workbook) {
//				final String reportPath = ReportPropertiesLocator.getInstance(true).getValue(Constants.REPORT_OUTPUT_PATH);
//				final String reportFileName = reportPath + "report_" + System.currentTimeMillis() + ".xls";
//				generateExcelFile(workbook, reportFileName);
//			} else {
//				logger.error("create excel failed!!");
//			}
			
			
			final ISafeReport bo = (ISafeReport) SpringUtil.getBean("safeReport");
			final HSSFWorkbook workbook = bo.renderExcelReport();
			if(null != workbook) {
				final String reportPath = ReportPropertiesLocator.getInstance(true).getValue(Constants.REPORT_OUTPUT_PATH);
				final String reportFileName = reportPath + "report_" + System.currentTimeMillis() + ".xls";
				generateExcelFile(workbook, reportFileName);
			} else {
				logger.error("create excel failed!!");
			}
		} catch (AppException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	
	
	protected static void generateExcelFile(HSSFWorkbook wb, String fileName) throws Exception {    	
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            wb.write(fos);
        } catch (Exception e) {
            logger.error("generateExcelFile error: " + e.getMessage(), e);
            throw e;
        } finally {
            try {
            	if(fos != null) {
            		fos.close();
            	}
            } catch (IOException ioe) {
            	logger.error("close FileOutputStream error: " + ioe.getMessage(), ioe);
                throw ioe;
            }
        }
    }
}
