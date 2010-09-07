package com.report.util;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.report.bo.IComputerReport;
import com.report.bo.ISafeReport;

public class ReportUtil {
	private static final Logger logger = Logger.getLogger(ReportUtil.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final IComputerReport computerReportBO = (IComputerReport) SpringUtil.getBean("computerReport");
			logger.info("开始生成电脑详细信息表...");
			final HSSFWorkbook workbook1 = computerReportBO.renderExcelReport();
			String reportFileName = "";
			if(null != workbook1) {
				final String reportPath = ReportPropertiesLocator.getInstance(true).getValue(Constants.REPORT_OUTPUT_PATH);
				reportFileName = reportPath + "ComputerReport_" + System.currentTimeMillis() + ".xls";
				generateExcelFile(workbook1, reportFileName);
			} else {
				throw new AppException("create excel failed!!");
			}
			logger.info("生成电脑详细信息表结束!请到以下目录查看:");
			logger.info(reportFileName);
		} catch (AppException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
			
		try {
			final ISafeReport safeReportBO = (ISafeReport) SpringUtil.getBean("safeReport");
			logger.info("开始生成安全信息报表...");
			final HSSFWorkbook workbook2 = safeReportBO.renderExcelReport();
			String reportFileName = "";
			if(null != workbook2) {
				final String reportPath = ReportPropertiesLocator.getInstance(true).getValue(Constants.REPORT_OUTPUT_PATH);
				reportFileName = reportPath + "SafeReport_" + System.currentTimeMillis() + ".xls";
				generateExcelFile(workbook2, reportFileName);
			} else {
				throw new AppException("create excel failed!!");
			}
			logger.info("生成安全信息报表结束!请到以下目录查看:");
			logger.info(reportFileName);
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
