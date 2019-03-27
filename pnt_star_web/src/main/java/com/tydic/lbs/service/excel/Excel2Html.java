package com.tydic.lbs.service.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Range;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;

import com.tydic.lbs.util.StringCommon;

public class Excel2Html {
	public static void main(String args[]) throws Exception {
		Excel2Html.removeColumnTest();
	}

	
	public static JSONObject convertExcel2Extjs(String excelFilePath)
			throws Exception {
		JSONObject resultJson = new JSONObject();
		File excelFile = new File(excelFilePath);
		InputStream is = null;
		try {
			if (excelFile.exists()) {
				char s = 'A';
				is = new FileInputStream(excelFile);
				HSSFWorkbook workBook = new HSSFWorkbook(is);
				HSSFSheet s1 = workBook.getSheetAt(0);
				int realRowNum = s1.getPhysicalNumberOfRows();
				int rowNum = realRowNum - 4;//字段行信息，最后4行不处理，属于配置类的信息
				int columnNum = s1.getRow(1).getPhysicalNumberOfCells();
				System.out.println("rowNum=" + rowNum);
				System.out.println("columnNum=" + columnNum);
				//获取合并单元格
				int merges = s1.getNumMergedRegions();
				//初始化数据
				List<List<ExcelCellMeta>> totalCellMet = new ArrayList<List<ExcelCellMeta>>();
				for (int i = 0; i < rowNum; i++) {//按行循环
					HSSFRow row = s1.getRow(i);
					List<ExcelCellMeta> rowList = new ArrayList<ExcelCellMeta>();
					for (int j = 0; j < columnNum; j++) {//按列循环
						HSSFCell cell = row.getCell(j);
						if (cell == null) {
							continue;
						}
						cell.setCellType(Cell.CELL_TYPE_STRING);
						String value = cell.getStringCellValue();
						ExcelCellMeta cellMeta = new ExcelCellMeta();
						cellMeta.setHeader(value);
						//colspan先设置初始值1，rowspan也设置初始值1
						cellMeta.setColspan(1);
						cellMeta.setRowspan(1);
						rowList.add(cellMeta);
						System.out.print((char) (s + j) + "" + (i + 1) + "=="
								+ value + "\t");
					}
					totalCellMet.add(rowList);
					System.out.println();
				}
				//开始处理合并单元格
				for (int i = 0; i < merges; i++) {
					CellRangeAddress addr = s1.getMergedRegion(i);
					int fr = addr.getFirstRow();
					int fc = addr.getFirstColumn();
					int lr = addr.getLastRow();
					int lc = addr.getLastColumn();
					System.out.println(fr + "" + fc + "" + lr + "" + lc);
					//按行循环
					for (int j = fr; j <= lr; j++) {
						for (int k = fc; k <= lc; k++) {
							System.out.println(j + "" + k + "");
							ExcelCellMeta cellM = totalCellMet.get(j).get(k);
							//当前行的数据,第一列保留，其他列都忽略掉
							if (j == fr) {
								//非第一列的全部无效,第一列有效
								if (k != fc) {
									cellM.setInMerge(true);
								} else {
									cellM.setInMerge(false);
									cellM.setColspan((lc - fc + 1));
									cellM.setRowspan(lr-fr+1);
								}
							}else {
								//其他行都设置为无效
								cellM.setInMerge(true);
							}
						}
					}
					//处理特殊的列，最后一行也是合并列的
					if (lr + 1 == rowNum) {
						//第一行的数据全部设置为空
						for (int j = fr; j <= lr; j++) {
							for (int k = fc; k <= lc; k++) {
								ExcelCellMeta cellM = totalCellMet.get(j)
										.get(k);
								if (j == fr) {
									//第一行，非第一列的全部无效,第一列设置为空
									if (k != fc) {
										cellM.setInMerge(true);
									} else {
										cellM.setInMerge(false);
										cellM.setColspan((lc - fc + 1));
										cellM.setHeader("");
									}
								} else if (j == lr) {
									//最后一行全部有效
									cellM.setInMerge(false);
									cellM.setHeader(s1.getRow(fr).getCell(fc)
											.getStringCellValue());
								}
							}
						}
					}

				}
				JSONArray groupRow = new JSONArray();
				//循环输出，分组行信息
				for (int i = 0; i < rowNum - 1; i++) {
					JSONArray jsonArray = new JSONArray();
					List<ExcelCellMeta> list = totalCellMet.get(i);
					for (ExcelCellMeta excelCellMeta : list) {
						if (excelCellMeta.isInMerge()) {
							continue;
						}
						JSONObject obj = JSONObject.fromObject(excelCellMeta);
						jsonArray.add(obj);
					}
					groupRow.add(jsonArray);
				}
				//最后一行字段信息
				JSONArray gridRow = new JSONArray();
				List<ExcelCellMeta> list = totalCellMet.get(rowNum - 1);
				HSSFRow dataIndexRow = s1.getRow(rowNum);//对应的数据库字段
				HSSFRow colTypeRow = s1.getRow(rowNum + 1);//对应的列类型，0隐藏，1数字，3文本
				HSSFRow urlRow = s1.getRow(rowNum + 2);//对应的url参数
				HSSFRow widthRow = s1.getRow(rowNum + 3);//对应的宽度参数
				int i = 0;
				for (ExcelCellMeta excelCellMeta : list) {
					if (excelCellMeta.isInMerge()) {
						i++;
						continue;
					}
					HSSFCell dataIndexCell = dataIndexRow.getCell(i);
					dataIndexCell.setCellType(Cell.CELL_TYPE_STRING);

					HSSFCell colTypeCell = colTypeRow.getCell(i);
					colTypeCell.setCellType(Cell.CELL_TYPE_STRING);

					HSSFCell urlCell = urlRow.getCell(i);
					urlCell.setCellType(Cell.CELL_TYPE_STRING);

					HSSFCell widthCell = widthRow.getCell(i);
					widthCell.setCellType(Cell.CELL_TYPE_STRING);
					
					if (StringCommon.isNull(dataIndexCell.getStringCellValue())) {
						excelCellMeta.setDataIndex("V" + (i + 1));
					} else {
						excelCellMeta.setDataIndex("V"
								+ dataIndexCell.getStringCellValue());
					}
					
					//添加field字段
					if (StringCommon.isNull(dataIndexCell.getStringCellValue())) {
						excelCellMeta.setField("");
					} else {
						excelCellMeta.setField(dataIndexCell.getStringCellValue());
					}
					
					excelCellMeta.setColtype(colTypeCell.getStringCellValue());
					if ("0".equals(excelCellMeta.getColtype())) {
						excelCellMeta.setHidden(true);
					} else {
						excelCellMeta.setHidden(false);
					}
					JSONObject obj = JSONObject.fromObject(excelCellMeta);
					//设置url参数
					String urlStr = urlCell.getStringCellValue();
					if (!StringCommon.isNull(urlStr)) {
						JSONObject urlJson = JSONObject.fromObject(urlStr);
						obj.put("url", urlJson.getString("url"));
						obj.put("param", urlJson.getJSONObject("param"));
					}
					//设置宽度参数
					String widthStr = widthCell.getStringCellValue();
					//隐藏列设置为0
					if ("0".equals(excelCellMeta.getColtype())) {
						obj.put("width", 0);
					}else {
						//没填的话则设置为80
						if (!StringCommon.isNull(widthStr)) {
							obj.put("width", Integer.parseInt(widthStr));
						}else{
							obj.put("width", 80);
						}
					}
					gridRow.add(obj);
					i++;
				}
				resultJson.put("groupcolumn", groupRow);
				resultJson.put("gridcolumn", gridRow);
				//总行数
				resultJson.put("headRow", totalCellMet.size());
				
				
				/******************************************************
				 * 开始生成html多层表头标签
				 *******************************************************/
				//把totalCellMet中colspan=0,rowspan=0,inMerge=true的元素剔除掉，生成原始的html标签
				List<List<ExcelCellMeta>> tableHtmlList=new ArrayList<List<ExcelCellMeta>>();
				for (int j = 0; j < totalCellMet.size(); j++) {
					List<ExcelCellMeta> cur_row = totalCellMet.get(j);
					List<ExcelCellMeta> copyRow=new ArrayList<ExcelCellMeta>();
					for (int k = 0; k < cur_row.size(); k++) {
						    ExcelCellMeta excelCellMeta = cur_row.get(k);
						    if(excelCellMeta.isInMerge()||excelCellMeta.getColspan()==0||excelCellMeta.getRowspan()==0) {
						    	
						    }else {
						    	copyRow.add(excelCellMeta);
						    }
					}
					tableHtmlList.add(copyRow);
				}
				//生成原始的html标签
				StringBuffer tableHtmlStr=new StringBuffer();
				//保留最后一行，最后一行从gridcolumn中取，主要是隐藏列头用
				
				if(tableHtmlList.size()>1) {
					for (int j = 0; j < tableHtmlList.size()-1; j++) {
						tableHtmlStr.append("<tr>");
						List<ExcelCellMeta> cur_row = tableHtmlList.get(j);
						for (int k = 0; k < cur_row.size(); k++) {
							tableHtmlStr.append("<th colspan='"+cur_row.get(k).getColspan()+"' rowspan='"+cur_row.get(k).getRowspan()+"'>");
							tableHtmlStr.append(cur_row.get(k).getHeader());
							tableHtmlStr.append("</th>");
						}
						tableHtmlStr.append("</tr>");
					}
				}
				
				//从gridcolumn中取，主要是隐藏列头用
				tableHtmlStr.append("<tr>");
				for (int k = 0; k < gridRow.size(); k++) {
					JSONObject curRow=gridRow.getJSONObject(k);
					//隐藏列头
					if(curRow.getBoolean("hidden")) {
						tableHtmlStr.append("<th  style='display:none;' colspan='"+curRow.getString("colspan")+"' rowspan='"+curRow.getString("rowspan")+"'> ");
						tableHtmlStr.append(curRow.getString("header"));
						tableHtmlStr.append("</th>");
					}else {
						tableHtmlStr.append("<th   colspan='"+curRow.getString("colspan")+"' rowspan='"+curRow.getString("rowspan")+"'>");
						tableHtmlStr.append(curRow.getString("header"));
						tableHtmlStr.append("</th>");
					}
				}
				tableHtmlStr.append("</tr>");
				resultJson.put("htmlHead", tableHtmlStr.toString());
				System.out.println(resultJson.toString());
			}
		} finally {
			is.close();
		}
		return resultJson;
	}
	

	public static void export2db(String service_name, List<Map<String,Object>> list,File destFile)
			throws Exception {
		String export_service_name = service_name.substring(0, service_name.indexOf("_EXP"));
		if("RETROSPECTALL_EXP".equals(service_name)){
			export_service_name="RETROSPECT";
		}
		String excelFile = "exceltemplate/"+export_service_name+".xls";
		String excelFilePath = Thread.currentThread().getContextClassLoader().getResource(excelFile).getPath();
		excelFilePath = URLDecoder.decode(excelFilePath, "UTF-8"); // 通过转码去掉项目路径中的空格
		//logger.info("monthbill export excelFilePath:"+excelFilePath);
		System.out.println("month export excelFilePath From:"+excelFilePath);
		fileChannelCopy(new File(excelFilePath), destFile);
		//STEP1 读取配置信息
		//工作簿 
		HSSFWorkbook hssfworkbook = new HSSFWorkbook(new FileInputStream(
				destFile));
		//获取sheet页 
		HSSFSheet hssfsheet = hssfworkbook.getSheetAt(0);
		int realRowNum = hssfsheet.getPhysicalNumberOfRows();
		int startRownum;
		startRownum = realRowNum - 3;//字段行信息，最后3行不处理，属于配置类的信息
		int columnNum = hssfsheet.getRow(1).getPhysicalNumberOfCells();
		HSSFRow dataIndexRow = hssfsheet.getRow(startRownum-1);//对应的数据库字段
		HSSFRow colTypeRow = hssfsheet.getRow(startRownum );//对应的列类型，0隐藏，1数字，3文本
		//获取列对应的数据库字段，和列的类型
		ExcelCellMeta[] config = new ExcelCellMeta[columnNum];
		for (int i = 0; i < columnNum; i++) {
			ExcelCellMeta excelCellMeta = new ExcelCellMeta();
			HSSFCell dataIndexCell = dataIndexRow.getCell(i);
			HSSFCell colTypeCell = colTypeRow.getCell(i);
			dataIndexCell.setCellType(Cell.CELL_TYPE_STRING);
			colTypeCell.setCellType(Cell.CELL_TYPE_STRING);
			if (StringCommon.isNull(dataIndexCell.getStringCellValue())) {
				excelCellMeta.setDataIndex((i + 1) + "");
			} else {
				excelCellMeta.setDataIndex(dataIndexCell.getStringCellValue());
			}
			//设置filed
			if (StringCommon.isNull(dataIndexCell.getStringCellValue())) {
				excelCellMeta.setField("");;
			} else {
				excelCellMeta.setField(dataIndexCell.getStringCellValue());;
			}
			
			if (StringCommon.isNull(colTypeCell.getStringCellValue())) {
				excelCellMeta.setColtype("3");
			} else {
				excelCellMeta.setColtype(colTypeCell.getStringCellValue());
			}
			config[i] = excelCellMeta;
		}
		//删除多余的行
		hssfsheet.removeRow(hssfsheet.getRow(realRowNum - 1));
		hssfsheet.removeRow(hssfsheet.getRow(realRowNum - 2));
		hssfsheet.removeRow(hssfsheet.getRow(realRowNum - 3));
		if("NEWMONTHBILLT1_EXP".equals(service_name)||"RETROSPECT_EXP".equals(service_name)||"RETROSPECTALL_EXP".equals(service_name))//按代理网点-查询汇总导出展示列编码问题处理
		{
			hssfsheet.removeRow(hssfsheet.getRow(realRowNum - 4));
			startRownum = realRowNum-4;
		}
			
		//由于POI没有删除列的功能，jxl删除列有bug无法合并单元格，隐藏采用隐藏的方式
		for (int i = 0; i < columnNum; i++) {
			ExcelCellMeta excelCellMeta = config[i];
			if (excelCellMeta.getColtype().equals("0")) {
				hssfsheet.setColumnHidden(i, true);
			}
		}
		OutputStream os = new FileOutputStream(destFile);
		hssfworkbook.write(os);
		
		//使用jxl导出数据到excel
		Workbook rwb = Workbook.getWorkbook(destFile);
		//打开一个文件的副本，并且指定数据写回到原文件        
		WritableWorkbook wwb = Workbook.createWorkbook(destFile,rwb);//copy   
		WritableSheet ws = wwb.getSheet(0);
		ws.setName("导出结果【0】");
		//读取数据库数据
		try {
			/** **********创建工作簿************ */
			int  EXCELSIZE = 60000;
			//设置字体
			WritableFont bodyFont=new WritableFont(WritableFont.createFont("宋体"),12,WritableFont.NO_BOLD );
			//数字格式
			jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#############0.###"); 
	        jxl.write.WritableCellFormat numbercf = new jxl.write.WritableCellFormat(nf); 
	        //设置单元格线条
	        numbercf.setBorder(Border.ALL, BorderLineStyle.THIN); 
            //设置字体样式
	        numbercf.setFont(bodyFont);
	        
	      //数字格式
			jxl.write.NumberFormat intf = new jxl.write.NumberFormat("#############0"); 
	        jxl.write.WritableCellFormat intcf = new jxl.write.WritableCellFormat(intf); 
	        //设置单元格线条
	        intcf.setBorder(Border.ALL, BorderLineStyle.THIN); 
            //设置字体样式
	        intcf.setFont(bodyFont);
	        
	        
	        //设置文本格式
	        WritableCellFormat lablecf = new jxl.write.WritableCellFormat(); 
	        //设置单元格线条
	        lablecf.setBorder(Border.ALL, BorderLineStyle.THIN); 
            //设置字体样式
	        lablecf.setFont(bodyFont);
	        //设置自动换行
	        lablecf.setWrap(true);
	        
	        //文本形式的数字
	        WritableCellFormat  contentFromart   =   new   WritableCellFormat(NumberFormats.TEXT); 
	        //设置单元格线条
	        contentFromart.setBorder(Border.ALL, BorderLineStyle.THIN); 
            //设置字体样式
	        contentFromart.setFont(bodyFont);
		  {
			    /** **设置Excel标题**** */
				for (int m = 1; m <= list.size()/EXCELSIZE; m++) {
					wwb.copySheet(0, "导出结果【"+m+"】", m);
				}
				/** **设置Excel内容**** */
				for (int i = 0; i < list.size(); i++) {
					//String[] dataArr = list.get(i);
					Map<String,Object> dataArr = list.get(i);
					WritableSheet sheetcur = wwb.getSheet(i / EXCELSIZE);
					int colIndx = 0;
					for (int j = 0; j < columnNum; j++) {
						//列配置信息
						ExcelCellMeta cellMeta = config[j];
						String field = cellMeta.getField();
						//当前数据
						//String tmpData = dataArr[dataIndex];
						String tmpData = dataArr.get(field)+"";
						//如果是隐藏列，则直接跳过，如果不是则写入数据
						if (!"0".equals(cellMeta.getColtype())) {
							if ("1".equals(cellMeta.getColtype())) {
								// 判断列是否为数字 col_type=1
								if (StringCommon.isInt(tmpData)) {
									Double d = Double.parseDouble(tmpData);
									jxl.write.Number n = new jxl.write.Number(
											colIndx, startRownum-1+i + 1 - (i / EXCELSIZE)
													* EXCELSIZE, d, intcf);
									sheetcur.addCell(n);
								} else if (StringCommon.isNumber(tmpData)) {
									Double d = Double.parseDouble(tmpData);
									jxl.write.Number n = new jxl.write.Number(
											colIndx, startRownum-1+i + 1 - (i / EXCELSIZE)
													* EXCELSIZE, d, numbercf);
									sheetcur.addCell(n);
								}else {
									sheetcur.addCell(new Label(colIndx,startRownum-1+ i + 1
											- (i / EXCELSIZE) * EXCELSIZE,
											StringCommon.trimNull(tmpData),
											contentFromart));
								}
							}else if ("3".equals(cellMeta.getColtype())) {
								//强制使用文本形式
								sheetcur.addCell(new Label(colIndx, startRownum-1+i + 1
										- (i / EXCELSIZE) * EXCELSIZE,
										StringCommon.trimNull(tmpData),
										contentFromart));
							}
							else{
								// 判断列是否为数字 col_type=1
								if (StringCommon.isInt(tmpData)) {
									Double d = Double.parseDouble(tmpData);
									jxl.write.Number n = new jxl.write.Number(
											colIndx, startRownum-1+i + 1 - (i / EXCELSIZE)
													* EXCELSIZE, d, intcf);
									sheetcur.addCell(n);
								} else if (StringCommon.isNumber(tmpData)) {
									Double d = Double.parseDouble(tmpData);
									jxl.write.Number n = new jxl.write.Number(
											colIndx,startRownum-1+ i + 1 - (i / EXCELSIZE)
													* EXCELSIZE, d, numbercf);
									sheetcur.addCell(n);
								}else {
									sheetcur.addCell(new Label(colIndx, startRownum-1+i + 1
											- (i / EXCELSIZE) * EXCELSIZE,
											StringCommon.trimNull(tmpData),
											contentFromart));
								}
							}
						}
						colIndx++;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("在输出到EXCEL的过程中出现错误，错误原因：" + e.toString());
		}finally{
			//
			wwb.write();
			wwb.close();
			rwb.close();
			//手动清空list对象
			if(list!=null){
				list.clear();
				list=null;
			}
		}
	}
	
	
   /****
    * 删除列演示
    */
	public static  void removeColumnTest(){
		String destFile = "D:/2.xls"; 
		
		//工作簿 
		try {
			//使用jxl导出数据到excel
			Workbook rwb = Workbook.getWorkbook(new File(destFile));
			//打开一个文件的副本，并且指定数据写回到原文件        
			WritableWorkbook wwb = Workbook.createWorkbook(new File("D:/3.xls"),rwb);//copy   
			WritableSheet ws = wwb.getSheet(0);
			ws.setName("导出结果【0】");
			
			//先获取所有合并单元格
			Range range[]=ws.getMergedCells();
			List<MergeInfo> mergeCell=new ArrayList<MergeInfo>();
			for(int i=0;i<range.length;i++){
				jxl.Cell cellTopLeft=range[i].getTopLeft();
				jxl.Cell cellBottomRight=range[i].getBottomRight();
				MergeInfo mi=new MergeInfo();
				mi.fr=cellTopLeft.getRow();
				mi.fc=cellTopLeft.getColumn();
				mi.lr=cellBottomRight.getRow();
				mi.lc=cellBottomRight.getColumn();
				mi.name=cellTopLeft.getContents();
				mergeCell.add(mi);
				//解除合并单元格
				ws.unmergeCells(range[i]);
			}
			//删除列
			removeColumn(ws,mergeCell,new int[]{6,9});
			//
			wwb.write();
			wwb.close();
			rwb.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/***
	 * 删除列，先要获取所有的合并单元格
	 * //先获取所有合并单元格
			Range range[]=ws.getMergedCells();
			List<MergeInfo> mergeCell=new ArrayList<MergeInfo>();
			for(int i=0;i<range.length;i++){
				jxl.Cell cellTopLeft=range[i].getTopLeft();
				jxl.Cell cellBottomRight=range[i].getBottomRight();
				MergeInfo mi=new MergeInfo();
				mi.fr=cellTopLeft.getRow();
				mi.fc=cellTopLeft.getColumn();
				mi.lr=cellBottomRight.getRow();
				mi.lc=cellBottomRight.getColumn();
				mi.name=cellTopLeft.getContents();
				mergeCell.add(mi);
				//解除合并单元格
				ws.unmergeCells(range[i]);
			}
			//删除列
			removeColumn(ws,mergeCell,new int[]{6,9});
	 * @param ws
	 * @param mergeCell 所有的合并单元格
	 * @param cols
	 * @throws Exception
	 */
	private static void removeColumn(WritableSheet ws,List<MergeInfo> mergeCell,int[] cols) throws Exception{
		//检测是否在合并单元格中，如果在，对合并单元格lc=lc-1
		for(int col:cols){
			for(MergeInfo info:mergeCell){
				//如果删除的列在中间的话，最后一列-1
				if(col>info.fc&&col<info.lc){
					info.lc-=1;
				}else if(col<=info.fc){
					//如果在左边fc-1,lc-1
					info.fc-=1;
					info.lc-=1;
				}else if(col>info.lc){
					//如果在右边,不影响合并单元格
				}else if(col==info.lc){
					//lc-1
					info.lc-=1;
				} 
				if(info.lc<info.fc){
					info.valid=false;
				}
			}
		}
		//开始合并单元格
		for(MergeInfo info:mergeCell){
			if(info.lc>=info.fc){
				jxl.write.Label lbl = new jxl.write.Label(info.fc, info.fr, info.name);//将第一个单元格的值改为“修改後的值”
				WritableCell cell=ws.getWritableCell(info.fc, info.fr);
				jxl.format.CellFormat cf = cell.getCellFormat();//获取第一个单元格的格式		
				lbl.setCellFormat(cf);//将修改后的单元格的格式设定成跟原来一样			
				ws.addCell(lbl);//将改过的单元格保存到sheet
				ws.mergeCells(info.fc, info.fr, info.lc, info.lr);
			}
		}
		
	}
	
	/**

	 * 使用文件通道的方式复制文件
	 * 
	 * @param s
	 *            源文件
	 * @param t
	 *            复制到的新文件
	 */
	public static void fileChannelCopy(File s, File t) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();//得到对应的文件通道
			out = fo.getChannel();//得到对应的文件通道
			in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
