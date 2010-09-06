package com.report.bo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.report.util.ReportPropertiesLocator;
import com.report.vo.ComputerVO;
import com.report.vo.DeviceControlActionVO;
import com.report.vo.HipsActionVO;
import com.report.vo.SafeReportDTO;
import com.sun.corba.se.impl.orbutil.closure.Constant;

public class SafeReportImpl implements ISafeReport {
	private static final Logger logger = Logger.getLogger(SafeReportImpl.class);
	
	private IHipsActionBO hipsActionBO;
	private IDeviceControlActionBO deviceControlActionBO;
	private IReportBO reportBO;

	public IReportBO getReportBO() {
		return reportBO;
	}

	public void setReportBO(IReportBO reportBO) {
		this.reportBO = reportBO;
	}

	public IHipsActionBO getHipsActionBO() {
		return hipsActionBO;
	}

	public void setHipsActionBO(IHipsActionBO hipsActionBO) {
		this.hipsActionBO = hipsActionBO;
	}

	public IDeviceControlActionBO getDeviceControlActionBO() {
		return deviceControlActionBO;
	}

	public void setDeviceControlActionBO(
			IDeviceControlActionBO deviceControlActionBO) {
		this.deviceControlActionBO = deviceControlActionBO;
	}

	private List getDataSource() throws AppException {
		final Map branchMap = reportBO.getAllComputerWitchBranch();
		final Map hipsGroupByComputerIdnMap = hipsActionBO.getHipsActionGroupByComputerIdn();
		final Map deviceControlActionByComputerIdnMap = deviceControlActionBO.getHipsActionGroupByComputerIdn();
		Map hipsMap = new HashMap();
		Map deviceControlMap = new HashMap();
		for (final Iterator iterator = branchMap.keySet().iterator(); iterator.hasNext();) {
			final String branch = (String) iterator.next();
			final List computerVOList = (List) branchMap.get(branch);
			for (Iterator iter1 = computerVOList.iterator(); iter1.hasNext();) {
				final ComputerVO computerVO = (ComputerVO)iter1.next();
				final List list = (List)hipsGroupByComputerIdnMap.get(computerVO.getComputerIdn());
				if(null != list) {
					if(hipsMap.containsKey(branch)) {
						final List temp = (List)hipsMap.get(branch);
						temp.addAll(list);
					} else {
						List temp = new ArrayList();
						temp.addAll(list);
						hipsMap.put(branch, temp);
					}
				}
				
				final List dcList = (List) deviceControlActionByComputerIdnMap.get(computerVO.getComputerIdn());
				if (null != dcList) {
					if (deviceControlMap.containsKey(branch)) {
						final List temp = (List) deviceControlMap.get(branch);
						temp.addAll(dcList);
					} else {
						List temp = new ArrayList();
						temp.addAll(dcList);
						deviceControlMap.put(branch, temp);
					}
				}
			}
		}
		
		
		List result = new ArrayList();
		
		for (final Iterator iter = branchMap.keySet().iterator(); iter.hasNext();) {
			final String branchName = (String)iter.next();
			SafeReportDTO dto = new SafeReportDTO();
			dto.setBranchName(branchName);
			final List list = (List)hipsMap.get(branchName);
			dto.setHipsActionCount(list.size());
			Map hipsByCodeMap = new HashMap();
			for (Iterator iter2 = list.iterator(); iter2.hasNext();) {
				HipsActionVO hipsActionVO = (HipsActionVO) iter2.next();
				final String actionCode = String.valueOf(hipsActionVO.getActionCode().intValue());
				if(hipsByCodeMap.containsKey(actionCode)) {
					final List temp = (List)hipsByCodeMap.get(actionCode);
					temp.add(hipsActionVO);
				} else {
					List temp = new ArrayList();
					temp.add(hipsActionVO);
					hipsByCodeMap.put(actionCode, temp);
				}
			}
			
			dto.setHipsActionMap(hipsByCodeMap);
			final List dcList = (List)deviceControlMap.get(branchName);
			dto.setDeviceControlActionCount(dcList.size());
			Map dcByCodeMap = new HashMap();
			for (Iterator iter2 = dcList.iterator(); iter2.hasNext();) {
				DeviceControlActionVO vo = (DeviceControlActionVO) iter2.next();
				final String actionCode = String.valueOf(vo.getActionCode().intValue());
				if(dcByCodeMap.containsKey(actionCode)) {
					final List temp = (List)dcByCodeMap.get(actionCode);
					temp.add(vo);
				} else {
					List temp = new ArrayList();
					temp.add(vo);
					dcByCodeMap.put(actionCode, temp);
				}
			}
			dto.setDeviceControlActionMap(dcByCodeMap);
			result.add(dto);
		}
		return result;
	}

	public HSSFWorkbook renderExcelReport() throws AppException {
		
        POIFSFileSystem fs;
        HSSFWorkbook wb = null;
        final FileInputStream fis = null;
        try {
        	final String templateFile = ReportPropertiesLocator.getInstance(true).getValue(Constants.REPORT_SAFE_INFO_KEY);
        	final ClassPathResource resource = new ClassPathResource(templateFile);
			fs = new POIFSFileSystem(resource.getInputStream());
			wb = new HSSFWorkbook(fs);

			final String sheet1Name = "安全汇总表";
			final String sheet2Name = "安全活动详细表";
          
	        wb.setSheetName(0, sheet1Name);
	        wb.setSheetName(1, sheet2Name);
	        
	        final HSSFSheet sheet = wb.getSheetAt(0);
	        final HSSFRow firstRow = sheet.getRow(0);  
	        final HSSFWorkbookWriter writer = new HSSFWorkbookWriter(wb, firstRow);
	        
	        HSSFCellStyle cellStyle = sheet.getRow(1).getCell(0).getCellStyle();
	        HSSFCellStyle tempCellStyle = sheet.getRow(1).getCell(1).getCellStyle();
	        
	        
	        writer.newRow();
	        writer.newRow();
	        writer.newRow();
	        List sourceList = getDataSource();
	        for (Iterator iter = sourceList.iterator(); iter.hasNext();) {
				SafeReportDTO dto = (SafeReportDTO) iter.next();
		        writer.newRow();
		        writer.newCell(cellStyle);
		        writer.writeToCurrentCell(dto.getBranchName());
		        
		        writer.newRow();
		        writer.newCell(cellStyle);
		        writer.writeToCurrentCell("主机侵入保护安全活动次数：");
		        writer.newCell(tempCellStyle);
		        writer.writeToCurrentCell(String.valueOf(dto.getHipsActionCount()));
		        
		        Map hipsByCodeMap = dto.getHipsActionMap();
		        List otherList = new ArrayList();
		        int index = 1;
		        for (Iterator iter1 = dto.getHipsActionMap().keySet().iterator(); iter1
						.hasNext();) {
					String actionCode = (String) iter1.next();
					List tempList = (List) hipsByCodeMap.get(actionCode);
					if(Constants.HIPS_ACTION_MAP.containsKey(actionCode)) {
						writer.newRow();
						writer.newCell(tempCellStyle);
						writer.newCell(tempCellStyle);
						writer.newCell(tempCellStyle);
						writer.writeToCurrentCell(String.valueOf(index));
						writer.newCell(cellStyle);
						writer.writeToCurrentCell(Constants.HIPS_ACTION_MAP.get(actionCode) + ":");					
						writer.newCell(tempCellStyle);
						writer.writeToCurrentCell(String.valueOf(tempList.size()));		
						
						index++;
					} else {
						otherList.addAll(tempList);
					}
				}
		        if(!otherList.isEmpty()) {
			        writer.newRow();
					writer.newCell(tempCellStyle);
					writer.newCell(tempCellStyle);
					writer.newCell(tempCellStyle);
					writer.writeToCurrentCell(String.valueOf(index++));
					writer.newCell(cellStyle);
					writer.writeToCurrentCell("其他事件:");					
					writer.newCell(tempCellStyle);
					writer.writeToCurrentCell(String.valueOf(otherList.size()));
		        }
		        writer.newRow();
		        
		        writer.newRow();
		        writer.newCell(cellStyle);
		        writer.writeToCurrentCell("设备控制次数：");
		        writer.newCell(tempCellStyle);
		        writer.writeToCurrentCell(String.valueOf(dto.getDeviceControlActionCount()));
		        
		        index = 1;
		        Map dcByCodeMap = dto.getDeviceControlActionMap();
		        otherList.clear();
		        for (Iterator iter1 = dcByCodeMap.keySet().iterator(); iter1
						.hasNext();) {
					String actionCode = (String) iter1.next();
					List tempList = (List) dcByCodeMap.get(actionCode);
					if(Constants.DEVICE_CONTROL_ACTION_MAP.containsKey(actionCode)) {
					writer.newRow();
					writer.newCell(tempCellStyle);
					writer.newCell(tempCellStyle);
					writer.newCell(tempCellStyle);
					writer.writeToCurrentCell(String.valueOf(index));
					writer.newCell(cellStyle);
					writer.writeToCurrentCell(Constants.DEVICE_CONTROL_ACTION_MAP.get(actionCode) + ":");					
					writer.newCell(tempCellStyle);
					writer.writeToCurrentCell(String.valueOf(tempList.size()));		
					
					index++;
					} else {
						otherList.addAll(tempList);
					}
				}
		        
		        if(!otherList.isEmpty()) {
			        writer.newRow();
					writer.newCell(tempCellStyle);
					writer.newCell(tempCellStyle);
					writer.newCell(tempCellStyle);
					writer.writeToCurrentCell(String.valueOf(index++));
					writer.newCell(cellStyle);
					writer.writeToCurrentCell("其他事件:");					
					writer.newCell(tempCellStyle);
					writer.writeToCurrentCell(String.valueOf(otherList.size()));
		        }
		        writer.newRow();
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
}
