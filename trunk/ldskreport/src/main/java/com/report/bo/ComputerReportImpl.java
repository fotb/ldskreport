package com.report.bo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.core.io.ClassPathResource;

import com.report.util.AppException;
import com.report.util.Constants;
import com.report.util.DateUtil;
import com.report.util.ReportPropertiesLocator;
import com.report.vo.CompSystemVO;
import com.report.vo.ComputerVO;
import com.report.vo.TcpVO;

public class ComputerReportImpl implements IComputerReport{
	private static final Logger logger = Logger.getLogger(ComputerReportImpl.class);
	
	private IReportBO reportBO;
	private ICompSystemBO compSystemBO;
	
	public ICompSystemBO getCompSystemBO() {
		return compSystemBO;
	}

	public void setCompSystemBO(ICompSystemBO compSystemBO) {
		this.compSystemBO = compSystemBO;
	}

	public IReportBO getReportBO() {
		return reportBO;
	}

	public void setReportBO(IReportBO reportBO) {
		this.reportBO = reportBO;
	}

	public HSSFWorkbook renderExcelReport() throws AppException {
        POIFSFileSystem fs;
        HSSFWorkbook wb = null;
        final FileInputStream fis = null;
        try {
        	final String templateFile = ReportPropertiesLocator.getInstance(true).getValue(Constants.REPORT_ALL_COMPUTER_KEY);
        	final ClassPathResource resource = new ClassPathResource(templateFile);
			fs = new POIFSFileSystem(resource.getInputStream());
			wb = new HSSFWorkbook(fs);

			final String sheetName = "Computer" + DateUtil.format(new Date(), "yyyyMMdd");
          
	        wb.setSheetName(0, sheetName);
	        final HSSFSheet sheet = wb.getSheetAt(0);
	        final HSSFRow firstRow = sheet.getRow(0);  
	        final HSSFWorkbookWriter writer = new HSSFWorkbookWriter(wb, firstRow);
	        
	        HSSFCellStyle cellStyle = firstRow.getCell(0).getCellStyle();
	        
	        
	        
	        final int computerCount = reportBO.findAllComputerCount();
	        final int dellCount = compSystemBO.findCountByModel("Dell");
	        final int lenovoCount = compSystemBO.findCountByModel("Lenovo");
	        final int hpCount = compSystemBO.findCountByModel("HP") + compSystemBO.findCountByModel("Hewlett-Packard");
	        writer.writeTo(0, 0, Short.valueOf("1").shortValue(), String.valueOf(dellCount));
	        writer.writeTo(0, 0, Short.valueOf("3").shortValue(), String.valueOf(lenovoCount));
	        writer.writeTo(0, 0, Short.valueOf("5").shortValue(), String.valueOf(hpCount));
	        writer.newRow();
	        writer.newCell(cellStyle);
	        writer.writeToCurrentCell("其他品牌:");
	        writer.newCell();
	        writer.writeToCurrentCell(String.valueOf(computerCount - dellCount - lenovoCount - hpCount));
	        writer.newRow();
	        writer.newCell(cellStyle);
	        writer.writeToCurrentCell("电脑总数:");
	        writer.newCell();
	        writer.writeToCurrentCell(String.valueOf(computerCount));
	        
	        
	        final Map branchMap = reportBO.getAllComputerWitchBranch();
	        final Map ipMap = reportBO.findAllIpAddress();
	        final Map macMap = reportBO.findAllMacAddress();
	        final Map positionMap = reportBO.findAllByMetaObjAttrRelationsIdn(ReportPropertiesLocator.getInstance(true).getValue(Constants.PROPERTIES_POSITION_KEY));
	        final Map modelMap = compSystemBO.findAllToMap();
	        for (Iterator iterator = branchMap.keySet().iterator(); iterator.hasNext();) {
				final String key = (String) iterator.next();
				List computerVOList = (List)branchMap.get(key);
				writer.newRow();
		        writer.newRow();
		        writer.newRow();
		        writer.newCell(cellStyle);
		        if(Constants.OTHER_DEPT_KEY.equals(key)) {
		        	writer.writeToCurrentCell("其他部门:");	
		        } else {
		        	writer.writeToCurrentCell(key + ":");
		        }
		        createHeader(writer, cellStyle);
		        int dCount = 0;
		        int lCount = 0;
		        int hCount = 0;
		        int otherCount = 0;
		        int index = 1;
		        for (Iterator iter = computerVOList.iterator(); iter.hasNext();) {
					ComputerVO vo = (ComputerVO) iter.next();
					writer.newRow();
					writer.newCell();
					writer.writeToCurrentCell(String.valueOf(index));
					writer.newCell();
					writer.writeToCurrentCell(vo.getDeviceName());
					writer.newCell();
					final TcpVO tcpVO = (TcpVO)ipMap.get(String.valueOf(vo.getComputerIdn()));
					writer.writeToCurrentCell(tcpVO.getAddress());
					writer.newCell();
					writer.writeToCurrentCell(macMap.get(String.valueOf(vo.getComputerIdn())));
					writer.newCell();
					CompSystemVO compSystemVO = (CompSystemVO)modelMap.get(String.valueOf(vo.getComputerIdn()));
					writer.writeToCurrentCell(compSystemVO.getModel());
					writer.newCell();
					writer.writeToCurrentCell(positionMap.get(String.valueOf(vo.getComputerIdn())));
					if(null != compSystemVO.getManufacturer() && compSystemVO.getManufacturer().indexOf("Dell") != -1) {
						dCount++;
					} else if(null != compSystemVO.getManufacturer() && compSystemVO.getManufacturer().indexOf("Lenovo") != -1) {
						lCount++;
					} else if(null != compSystemVO.getManufacturer() 
							&& (compSystemVO.getManufacturer().indexOf("HP") != -1
							|| compSystemVO.getManufacturer().indexOf("Hewlett-Packard") != -1)) {
						hCount++;
					}else {
						otherCount++;
					}
					index++;
				}
		        writer.newRow();
		        writer.newCell(cellStyle);
		        writer.writeToCurrentCell("Dell总数:");
		        writer.newCell();
		        writer.writeToCurrentCell(String.valueOf(dCount));
		        writer.newCell(cellStyle);
		        writer.writeToCurrentCell("Lenovo总数:");
		        writer.newCell();
		        writer.writeToCurrentCell(String.valueOf(lCount));
		        writer.newCell(cellStyle);
		        writer.writeToCurrentCell("HP总数:");
		        writer.newCell();
		        writer.writeToCurrentCell(String.valueOf(hCount));
		        writer.newRow();
		        writer.newCell(cellStyle);
		        writer.writeToCurrentCell("其他品牌:");
		        writer.newCell();
		        writer.writeToCurrentCell(String.valueOf(otherCount));
		        writer.newRow();
		        writer.newCell(cellStyle);
		        writer.writeToCurrentCell("分行总数:");
		        writer.newCell();
		        writer.writeToCurrentCell(String.valueOf(lCount + dCount + otherCount));
			}
	        
        } catch(Exception e) {
        	logger.error(e.getMessage(), e);
        } finally{
            try {
                if(fis != null){                    
                    fis.close();                    
                }
            } catch (IOException e1) {
                logger.error(e1.getMessage(), e1);
            }
        } 
		return wb;
	}
	
	private void createHeader(HSSFWorkbookWriter writer, HSSFCellStyle style) {
		//ReportPropertiesLocator properties = ReportPropertiesLocator.getInstance(true);
		writer.newRow();
		writer.newCell(style);
		writer.writeToCurrentCell("序号");
		writer.newCell(style);
		//writer.writeToCurrentCell(properties.getValue(Constants.REPORT_HEADER_COMPUTERNAME));
		writer.writeToCurrentCell("机器名");
		writer.newCell(style);
//		writer.writeToCurrentCell(properties.getValue(Constants.REPORT_HEADER_COMPUTERIP));
		writer.writeToCurrentCell("IP");
		writer.newCell(style);
	//	writer.writeToCurrentCell(properties.getValue(Constants.REPORT_HEADER_MAC));
		writer.writeToCurrentCell("MAC地址");
		writer.newCell(style);
		//writer.writeToCurrentCell(properties.getValue(Constants.REPORT_HEADER_BRANCHNAME));
		writer.writeToCurrentCell("机器类型");
		writer.newCell(style);
		//writer.writeToCurrentCell(properties.getValue(Constants.REPORT_HEADER_POSITION));
		writer.writeToCurrentCell("位置");
	}
}
