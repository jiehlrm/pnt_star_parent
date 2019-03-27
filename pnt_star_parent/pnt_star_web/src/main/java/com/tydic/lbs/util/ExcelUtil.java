package com.tydic.lbs.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	
	/**
	 * 解析excel文件
	 * @param filePath
	 * @param columns
	 * @param dataList
	 */
public static void parseImport(String filePath,List<Map<String,Object>> columns,Map<String,Object> params,List<Map<String,Object>> dataList) throws Exception{
		
		// 文件输入流
		FileInputStream is = new FileInputStream(filePath);
		
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		
		// 行数
		int rowNum = sheet.getLastRowNum();
		// 列数
		int columnNum = sheet.getRow(1).getPhysicalNumberOfCells();		
		
		if(columns.size() != columnNum){
			throw new Exception("导入excel列数和列名数量不一致!");
		}
		
		//导入文件内容按照列名的key放入dataList
		for (int i = 1; i <= rowNum; i++) {
			Map<String,Object> data = new HashMap<String,Object>();
			HSSFRow row = sheet.getRow(i);
			for (int j = 0; j < columnNum; j++) {
				data.put(columns.get(j).get("COL_NAME")+"", getCellValue(row.getCell(j),true)) ;
			}
			data.putAll(params);
			dataList.add(data);
		}
		
	}

/*
 * 手工调账导入
 */
public static void parseImport_MssAdj(String filePath,List<Map<String,Object>> columns,Map<String,Object> params,List<Map<String,Object>> dataList) throws Exception{
	
	// 文件输入流
	FileInputStream is = new FileInputStream(filePath);
	
	HSSFWorkbook workbook = new HSSFWorkbook(is);
	HSSFSheet sheet = workbook.getSheetAt(0);
	
	// 行数
	int rowNum = sheet.getLastRowNum();
	// 列数
	int columnNum = sheet.getRow(1).getPhysicalNumberOfCells();		
	
	String service_type_name = params.get("service_type_name").toString();
	
	if(!"".equals(service_type_name)){ //通用模板按业务类型校验模板是否正确
		Row row = sheet.getRow(0);
		String firstRowValue = getCellValue(row.getCell(0),true);
		if(firstRowValue.indexOf(service_type_name)==-1){
			throw new Exception("模板不正确!");
		}
	}
	if(columns.size() != columnNum){
		throw new Exception("导入excel列数和列名数量不一致!");
	}
	//导入文件内容按照列名的key放入dataList
	for (int i = 4; i <= rowNum; i++) {
		Map<String,Object> data = new HashMap<String,Object>();
		HSSFRow row = sheet.getRow(i);
		for (int j = 0; j < columnNum; j++) {
			data.put(columns.get(j).get("COL_NAME")+"", getCellValue(row.getCell(j),true)) ;
		}
		data.putAll(params);
		dataList.add(data);
	}
	
}


/*
 * 民资营业厅维护导入
 */
public static void parseImport_MZBusi(String filePath,List<Map<String,Object>> columns,Map<String,Object> params,List<Map<String,Object>> dataList) throws Exception{
	
	// 文件输入流
	FileInputStream is = new FileInputStream(filePath);
	
	HSSFWorkbook workbook = new HSSFWorkbook(is);
	HSSFSheet sheet = workbook.getSheetAt(0);
	
	// 行数
	int rowNum = sheet.getLastRowNum();
	// 列数
	int columnNum = sheet.getRow(1).getPhysicalNumberOfCells();		
	
	String service_type_name = params.get("service_type_name").toString();
	
	if(!"".equals(service_type_name)){ //通用模板按业务类型校验模板是否正确
		Row row = sheet.getRow(0);
		String firstRowValue = getCellValue(row.getCell(0),true);
		if(firstRowValue.indexOf(service_type_name)==-1){
			throw new Exception("模板不正确!");
		}
	}
	if(columns.size() != columnNum){
		throw new Exception("导入excel列数和列名数量不一致!");
	}
	//导入文件内容按照列名的key放入dataList
	for (int i = 2; i <= rowNum; i++) {
		Map<String,Object> data = new HashMap<String,Object>();
		HSSFRow row = sheet.getRow(i);
		for (int j = 0; j < columnNum; j++) {
			data.put(columns.get(j).get("COL_NAME")+"", getCellValue(row.getCell(j),true)) ;
		}
		data.putAll(params);
		dataList.add(data);
	}
	
}

public static void parseImport_mss(String filePath,List<Map<String,Object>> columns,Map<String,Object> params,List<Map<String,Object>> dataList) throws Exception{
		
		// 文件输入流
		FileInputStream is = new FileInputStream(filePath);
		
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		
		// 行数
		int rowNum = sheet.getLastRowNum();
		// 列数
		int columnNum = sheet.getRow(1).getPhysicalNumberOfCells();	
		int total_columns = columns.size();//总列宽
		String imp_flag = params.get("imp_flag")+"";
		String service_name = params.get("service_name") + "";
	    String manutypeflag = "";//供应商标识
	    String taxtypeflag = "";//税率标识
		if(imp_flag.equals("MSS_MANU")){//网点与供应商关系
			//网点与供应商关系导入-社会渠道
			String manutype = getCellValue(sheet.getRow(2).getCell(5),true);
			if(manutype.equals("[1]社会渠道")){
				columnNum = columnNum - 4;
				total_columns = total_columns - 4;
				manutypeflag = "1";
				params.put("service_name", service_name + "_1");
				params.put("MANUTYPE", 1);
			}else{
				manutypeflag = "3";
				params.put("service_name", service_name + "_3");
				params.put("MANUTYPE", 3);
			}
		}else if(imp_flag.equals("MSS_TAX")){//网点与会计科目税率关系
			String taxtype = getCellValue(sheet.getRow(2).getCell(3),true);
			if(taxtype.equals("1")){//网点税率
				columnNum = columnNum - 1;
				total_columns = total_columns - 1;
				taxtypeflag = "1";
				params.put("service_name", service_name + "_1");
				params.put("TAXTYPE", 1);
			}else{//民资税率
				taxtypeflag = "3";
				params.put("service_name", service_name + "_3");
				params.put("TAXTYPE", 3);
			}
		}
		
		if(total_columns != columnNum){
			throw new Exception("导入excel列数和列名数量不一致!");
		}
		
		//导入文件内容按照列名的key放入dataList
		for (int i = 2; i <= rowNum; i++) { //表头两行，从第三行开始读取
			Map<String,Object> data = new HashMap<String,Object>();
			HSSFRow row = sheet.getRow(i);
			for (int j = 0; j < columnNum; j++) {
				data.put(columns.get(j).get("COL_NAME")+"", getCellValue(row.getCell(j),true)) ;
			}
			if(manutypeflag.equals("1")){
				data.put("PARTNERID", "0");
				data.put("BANKNAME", "");
				data.put("BANKADDRESS", "");
				data.put("BANKACCOUNT", "");
			}
			data.putAll(params);
			dataList.add(data);
		}
		
	}

/**
 * 报账新增文件导入
 * @param filePath
 * @param columns
 * @param params
 * @param dataList
 * @throws Exception
 */
public static void parseImport_mssprod(String filePath,List<Map<String,Object>> columns,Map<String,Object> params,List<Map<String,Object>> dataList) throws Exception{
	
	// 文件输入流
	FileInputStream is = new FileInputStream(filePath);
	
	HSSFWorkbook workbook = new HSSFWorkbook(is);
	HSSFSheet sheet = workbook.getSheetAt(0);
	
	// 行数
	int rowNum = sheet.getLastRowNum();
	// 列数
	int columnNum = sheet.getRow(1).getPhysicalNumberOfCells();	
	int total_columns = columns.size();//总列宽
	String impFlag = params.get("imp_flag") + "";//导入标志
	if(impFlag.equals("MSSPROD")){
		String commission_type = params.get("COMMISSITION_TYPE") + "";//佣金类型
		String service_name = params.get("service_name") + "";
		if(commission_type.equals("1")){
			total_columns = columns.size() - 1;
			columnNum = columnNum - 1;
		}else if(commission_type.equals("3")){
			total_columns = columns.size();
		}
		service_name = service_name + "_" + commission_type;
		params.put("service_name", service_name);
	}
	
	if(total_columns != columnNum){
		throw new Exception("导入excel列数和列名数量不一致!");
	}
	
	//导入文件内容按照列名的key放入dataList
	for (int i = 1; i <= rowNum; i++) {
		Map<String,Object> data = new HashMap<String,Object>();
		HSSFRow row = sheet.getRow(i);
		for (int j = 0; j < columnNum; j++) {
			data.put(columns.get(j).get("COL_NAME")+"", getCellValue(row.getCell(j),true)) ;
		}
		//如果是社会渠道则账期为页面选择账期
//		if(impFlag.equals("MSSPROD")){
//			String commission_type = params.get("COMMISSITION_TYPE") + "";//佣金类型
//			String billing_cycle_id = params.get("SEL_BILLING_CYCLE_ID") + "";//页面选择的账期
//			if(commission_type.equals("1")){
//				data.put("BILLING_CYCLE", billing_cycle_id);
//				data.put("PARTNER_ID", "");
//			}
//		}
		data.putAll(params);
		dataList.add(data);
	}
	
}


   /**
    * 通用导入数据解析
    * @param filePath
    * @param columns
    * @param params
    * @param dataList
    * @throws Exception
    */
//	public static void parseImport2(String filePath,List<Map<String,Object>> columns,Map<String,Object> params,List<Map<String,Object>> dataList,
//			List<Map<String,Object>> checkdataList,String batch_no,double sum_total_charge,int rec_count) throws Exception{
//		
//		// 文件输入流
//		FileInputStream is = new FileInputStream(filePath);
//		
////		HSSFWorkbook workbook = new HSSFWorkbook(is);
////		HSSFSheet sheet = workbook.getSheetAt(0);
//		Workbook workbook = getWorkbook(filePath);
//		Sheet  sheet = workbook.getSheetAt(0);
//		
//		// 行数
//		int rowNum = sheet.getLastRowNum();
//		// 列数
//		int columnNum = sheet.getRow(2).getPhysicalNumberOfCells();
//		
//		String service_type_name = params.get("SERVICE_TYPE_NAME") + ""; //业务类型名称
//		String data_start_row = params.get("DATA_START_ROW") + "";//数据开始行
//		String total_charge_key = params.get("TOTAL_CHARGE_KEY") + "";//汇总金额值
//		
//		if(!"".equals(service_type_name)){ //通用模板按业务类型校验模板是否正确
//			Row row = sheet.getRow(0);
//			String firstRowValue = getCellValue(row.getCell(0),true);
//			if(firstRowValue.indexOf(service_type_name)==-1){
//				throw new Exception("文件格式不正确，请先下载模板，然后使用模板进行数据导入!");
//			}
//		}
//		
//		if(columns.size() != columnNum){
//			throw new Exception("导入excel列数和列名数量不一致!");
//		}
//		
//		//导入文件内容按照列名的key放入dataList
//		if(!"".equals(data_start_row)){//通用导入设置开始读取行
//			for (int i = Integer.parseInt(data_start_row)-1; i <= rowNum; i++) { //从表头开始读
//				//Map<String,Object> data = new HashMap<String,Object>();
//				int head_num = Integer.parseInt(data_start_row)-2;//表头开始行
//				Row rowHead = sheet.getRow(head_num);//表头配置行
//				Row row = sheet.getRow(i);//数据开始行
//				for (int j = 0; j < columnNum; j++) {
//					Map<String,Object> data = new HashMap<String,Object>();
//					data.put("IMPORT_BATCH_NO", batch_no);
//					data.put("ROW_IDX", i+1);
//					data.put("COL_NO", j+1);
//					data.put("COL_CODE", columns.get(j).get("COL_NAME")+"");
//					data.put("COL_VALUE", getCellValue(row.getCell(j),true));
//					data.put("COL_DESC", getCellValue(rowHead.getCell(j),true));
//					if(total_charge_key.equals(columns.get(j).get("COL_NAME")+"")){//对累计字段金额进行累加
//						sum_total_charge +=Double.parseDouble(getCellValue(row.getCell(j),true))*100;//金额转换为分
//					}
//					data.putAll(params);
//					dataList.add(data);
//				}
//				
//				
//				//校验map
//				Map<String,Object> checkdata = new HashMap<String,Object>();
//				for (int j = 0; j < columnNum; j++) {
//					checkdata.put(columns.get(j).get("COL_NAME")+"", getCellValue(row.getCell(j),true)) ;
//				}
//				checkdata.putAll(params);
//				checkdataList.add(checkdata);
//			}
//			//总记录数
//			rec_count=rowNum-(Integer.parseInt(data_start_row)-1)+1;
//		}else{//默认从第三行开始读取数据
//			for (int i = 2; i <= rowNum; i++) {
//				//Map<String,Object> data = new HashMap<String,Object>();
//				Row rowHead = sheet.getRow(1);//表头配置行
//				Row row = sheet.getRow(i);
//				for (int j = 0; j < columnNum; j++) {
//					//data.put(columns.get(j).get("COL_NAME")+"", getCellValue(row.getCell(j),true)) ;
//					Map<String,Object> data = new HashMap<String,Object>();
//					data.put("IMPORT_BATCH_NO", batch_no);
//					data.put("ROW_IDX", i+1);
//					data.put("COL_NO", j+1);
//					data.put("COL_CODE", columns.get(j).get("COL_NAME")+"");
//					data.put("COL_VALUE", getCellValue(row.getCell(j),true));
//					data.put("COL_DESC", getCellValue(rowHead.getCell(j),true));
//					if(total_charge_key.equals(columns.get(j).get("COL_NAME")+"")){//对累计字段金额进行累加
//						sum_total_charge +=Double.parseDouble(getCellValue(row.getCell(j),true))*100;//金额转换为分
//					}
//					data.putAll(params);
//					dataList.add(data);
//				}
//				
//				//校验map
//				Map<String,Object> checkdata = new HashMap<String,Object>();
//				for (int j = 0; j < columnNum; j++) {
//					checkdata.put(columns.get(j).get("COL_NAME")+"", getCellValue(row.getCell(j),true)) ;
//				}
//				checkdata.putAll(params);
//				checkdataList.add(checkdata);
//			}
//			 //总记录数
//		    rec_count = rowNum -2 +1;
//		}
//		 
//		
//	}
public static String parseImport2(String filePath,List<Map<String,Object>> columns,Map<String,Object> params,List<Map<String,Object>> dataList,
		String batch_no,double sum_total_charge,int rec_count,String settBilling_cycle) throws Exception{
	
	// 文件输入流
	FileInputStream is = new FileInputStream(filePath);
	
	Workbook workbook = getWorkbook(filePath);
	Sheet  sheet = workbook.getSheetAt(0);
	
	// 行数
	int rowNum = sheet.getPhysicalNumberOfRows()-1;
	// 列数
	int columnNum = sheet.getRow(2).getPhysicalNumberOfCells();
	
	String service_type_name = params.get("SERVICE_TYPE_NAME") + ""; //业务类型名称
	String data_start_row = params.get("DATA_START_ROW") + "";//数据开始行
	String total_charge_key = params.get("TOTAL_CHARGE_KEY") + "";//汇总金额值
	
	if(!"".equals(service_type_name)){ //通用模板按业务类型校验模板是否正确
		Row row = sheet.getRow(0);
		String firstRowValue = getCellValue(row.getCell(0),true);
		if(firstRowValue.indexOf(service_type_name)==-1){
			throw new Exception("文件格式不正确，请先下载模板，然后使用模板进行数据导入!");
		}
	}
	
	if(columns.size() != columnNum){
		throw new Exception("导入excel列数和列名数量不一致!");
	}
	
	//导入文件内容按照列名的key放入dataList
	if(!"".equals(data_start_row)){//通用导入设置开始读取行
		for (int i = Integer.parseInt(data_start_row)-1; i <= rowNum; i++) { //从表头开始读
			Row row = sheet.getRow(i);//数据开始行
			//校验map
			Map<String,Object> data = new HashMap<String,Object>();
			data.putAll(params);
			for (int j = 0; j < columnNum; j++) {
				data.put(columns.get(j).get("COL_NAME")+"", getCellValue(row.getCell(j),true)) ;
				if(total_charge_key.equals(columns.get(j).get("COL_NAME")+"")){//对累计字段金额进行累加
					String money = getCellValue(row.getCell(j),true);
					if(money=="") {money="0";}
					if(!money.matches("^(-?\\d+)(\\.\\d+)?$")) {money="0";}
					sum_total_charge +=Double.parseDouble(money)*100;//金额转换为分
				}
				//针对手工调整新增结算帐期入库，其他业务类型默认为空，供后续itsm反馈审批结果时使用
				if((i == Integer.parseInt(data_start_row)-1) && "SETT_BILLING_CYCLE".equals(columns.get(j).get("COL_NAME")+"")){
					settBilling_cycle = getCellValue(row.getCell(j),true);
				}
				data.put("BATCH_NO", batch_no);
			}
			dataList.add(data);
		}
		//总记录数
		rec_count=rowNum-(Integer.parseInt(data_start_row)-1)+1;
	}else{//默认从第三行开始读取数据
		for (int i = 2; i <= rowNum; i++) {
			Row row = sheet.getRow(i);
			//校验map
			Map<String,Object> data = new HashMap<String,Object>();
			for (int j = 0; j < columnNum; j++) {
				data.putAll(params);
				data.put(columns.get(j).get("COL_NAME")+"", getCellValue(row.getCell(j),true)) ;
				if(total_charge_key.equals(columns.get(j).get("COL_NAME")+"")){//对累计字段金额进行累加
					sum_total_charge +=Double.parseDouble(getCellValue(row.getCell(j),true))*100;//金额转换为分
				}
				data.put("BATCH_NO", batch_no);
			}
			dataList.add(data);
		}
		 //总记录数
	    rec_count = rowNum -2 +1;
	}
	params.put("SUM_TOTAL_CHARGE", sum_total_charge);
	params.put("REC_COUNT", rec_count);
	return settBilling_cycle;
}
	
	/**
	 * 兼容03,07格式
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	private static Workbook getWorkbook(String filePath) throws IOException {
        Workbook workbook = null;
        System.out.println("getWork:"+filePath);
        InputStream is = new FileInputStream(filePath);
        if (filePath.endsWith("xls")) {
            workbook = new HSSFWorkbook(is);
        } else if (filePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(is);
        }
        return workbook;
}
	
	
	/**
     * 取单元格的值
     * @param cell 单元格对象
     * @param treatAsStr 为true时，当做文本来取值 (取到的是文本，不会把“1”取成“1.0”)
     * @return
     */
    private static String getCellValue(Cell cell, boolean treatAsStr) {
        if (cell == null) {
            return "";
        }

        if (treatAsStr) {
            // 虽然excel中设置的都是文本，但是数字文本还被读错，如“1”取成“1.0”
            // 加上下面这句，临时把它当做文本来读取
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }

        if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return String.valueOf(cell.getStringCellValue().trim());
        }
    }
	
	/**
     * 获取单元格数据内容为字符串类型的数据
     * 
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private static String getStringCellValue(HSSFCell cell) {
        String strCell = "";
        switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_STRING:
            strCell = cell.getStringCellValue();
            break;
        case HSSFCell.CELL_TYPE_NUMERIC:
            strCell = String.valueOf(cell.getNumericCellValue());
            break;
        case HSSFCell.CELL_TYPE_BOOLEAN:
            strCell = String.valueOf(cell.getBooleanCellValue());
            break;
        case HSSFCell.CELL_TYPE_BLANK:
            strCell = "";
            break;
        default:
            strCell = "";
            break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        if (cell == null) {
            return "";
        }
        return strCell;
    }

    /**
     * 获取单元格数据内容为日期类型的数据
     * 
     * @param cell
     *            Excel单元格
     * @return String 单元格数据内容
     */
    private static String getDateCellValue(HSSFCell cell) {
        String result = "";
        try {
            int cellType = cell.getCellType();
            if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
                Date date = cell.getDateCellValue();
                result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1)
                        + "-" + date.getDate();
            } else if (cellType == HSSFCell.CELL_TYPE_STRING) {
                String date = getStringCellValue(cell);
                result = date.replaceAll("[年月]", "-").replace("日", "").trim();
            } else if (cellType == HSSFCell.CELL_TYPE_BLANK) {
                result = "";
            }
        } catch (Exception e) {
            System.out.println("日期格式不正确!");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据HSSFCell类型设置数据
     * @param cell
     * @return
     */
    private static String getCellFormatValue(HSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
            case HSSFCell.CELL_TYPE_NUMERIC:
            case HSSFCell.CELL_TYPE_FORMULA: {
                // 判断当前的cell是否为Date
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式
                    
                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();
                    
                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellvalue = sdf.format(date);
                    
                }
                // 如果是纯数字
                else {
                    // 取得当前Cell的数值
                    cellvalue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            }
            // 如果当前Cell的Type为STRIN
            case HSSFCell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            // 默认的Cell值
            default:
                cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }

}
