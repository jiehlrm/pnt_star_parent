package com.tydic.pntstar.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.tydic.pntstar.util.DateUtil;

/**
 * 表格导出工具类类
 * 
 * @author hxc
 *
 */

public class ExcelExportUtil {

    // 层级列表
    static ArrayList<ArrayList<JSONObject>> rowList = null;
    // 索引map，保存节点对应信息，便于取值
    static Map<String, Map<String, Integer>> indexMap = null;
    // id列表，用于保存标题对应id
    static ArrayList<String> idList = null;

    /**
     * 生成服务器上excel
     * 
     * @param table_title
     * @param dataList
     * @param realPath
     */
    public static String exportExcel(List<Map<String, Object>> dataList, String table_title, String realPath) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        String sheetName = "表单数据";
        HSSFSheet sheet = workbook.createSheet(sheetName);

        // 获取标题数组
        JSONArray json_array = null;
        json_array = JSONArray.parseArray(table_title);
        // 获取表头信息
        getTitleInfo(json_array);
        // 设置表头信息
        setTitle(sheet);
        // 设置数据
        setData(sheet, dataList);

        // 生成excel,uuid防止资源冲突
        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString().replaceAll("-", "") + "_"
                + DateUtil.getNowTime().replaceAll(" ", "").replaceAll(":", "").replaceAll("-", "") + "_"
                + "data_result.xls";
        String filePath = realPath + "html/pointQuery/" + fileName;
        File file = new File(filePath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            workbook.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        String path = "html/pointQuery/" + fileName;
        return path;
    }

    private static void getTitleInfo(JSONArray json_array) {
        // 数据结构重置
        rowList = new ArrayList<>();
        indexMap = new HashMap<>();
        idList = new ArrayList<>();
        // 第一步，将同层节点加入到同一级层级列表中
        getRowList(json_array, 0, null);
        // 第二步，获取每个表头对应占据的行数和列数
        getRowAndColNum(rowList);
        // 第三步，获取每个表头对应的位置参数
        getPoisition(json_array, 0, 0);
    }

    // 第一步，将同层节点加入到同一级层级列表中
    private static void getRowList(JSONArray json_array, Integer row, String father) {
        ArrayList<JSONObject> list = null;
        if (rowList.size() <= row) {
            // 当前层级列表不存在时，生成
            list = new ArrayList<>();
            rowList.add(list);
        } else {
            list = rowList.get(row);
        }
        // 进入递归，遍历list
        for (int i = 0; i < json_array.size(); i++) {
            JSONObject json_object = json_array.getJSONObject(i);
            String id = "";
            JSONArray array = null;
            if (json_object != null) {
                id = json_object.getString("id");
                array = json_object.getJSONArray("children");
            }
            if (array != null) {
                getRowList(array, row + 1, id);
            } else {
                // 如果没有arry，说明没有子节点，其需要写入数据，将id保存至idList中
                idList.add(id);
            }
            // 将节点加入层级列表
            json_object.put("father", father);
            json_object.put("rowStart", row);
            list.add(json_object);
        }
    }

    // 第二步，获取每个表头对应占据的行数和列数
    static private void getRowAndColNum(ArrayList<ArrayList<JSONObject>> list) {
        int len = rowList.size();
        Map<String, Integer> map = new HashMap<>();
        // 从底层数据开始遍历，向上生成父节点的对应行列数
        for (int i = len - 1; i > -1; i--) {
            ArrayList<JSONObject> l1 = list.get(i);
            int length = l1.size();
            // 遍历
            for (int j = 0; j < length; j++) {
                JSONObject object = l1.get(j);
                String id = object.getString("id");
                String father = object.getString("father");
                int rowNum = 0;
                int colNum = 0;
                // 所占行数
                // 如果当前map中无节点与其对应，说明其无子节点
                // 列数为1，行数为总层数减其所在层
                if (map.get(id) == null) {
                    colNum = 1;
                    rowNum = len - i;
                } else {
                    // 如果当前map中有节点与其对应，说明其有子节点
                    // 列数为map中的值，行数为1
                    colNum = map.get(id);
                    rowNum = 1;
                }
                // 所占列数
                // 如果当前节点有父节点
                if (father != null) {
                    // 如果此时父节点的列数不存在，父节点列数置为当前节点所占列数（赋初值）
                    if (map.get(father) == null) {
                        map.put(father, colNum);
                        // 如果存在，则父节点列数加当前节点所占列数（计算父节点共占列数）
                    } else {
                        map.put(father, (colNum + map.get(father)));
                    }
                }
                // 将行列数放到索引map中
                Map<String, Integer> m = new HashMap<>();
                m.put("rowNum", rowNum);
                m.put("colNum", colNum);
                indexMap.put(id, m);
            }
        }
    }

    // 第三步，获取每个表头对应的位置参数
    static private void getPoisition(JSONArray json_array, int row, int col) {
        for (int i = 0; i < json_array.size(); i++) {
            JSONObject json_object = json_array.getJSONObject(i);
            String id = "";
            JSONArray array = null;
            if (json_object != null) {
                id = json_object.getString("id");
                array = json_object.getJSONArray("children");
            }
            if (array != null) {
                getPoisition(array, row + 1, col);
            }
            Map<String, Integer> map = indexMap.get(id);
            int colnum = map.get("colNum");
            // 当前col位置加colnum，此时为下一组节点的colStart
            col += colnum;
            // 控制合并的四个参数
            int rowStart = row;
            int rowEnd = row + map.get("rowNum") - 1;
            int colStart = col - colnum;
            int colEnd = col - 1;
            // 将四个参数放到索引map中
            map.put("rowStart", rowStart);
            map.put("rowEnd", rowEnd);
            map.put("colStart", colStart);
            map.put("colEnd", colEnd);
        }
    }

    // 遍历层级列表，从索引map中找出表头对应位置信息，进行创建
    static private void setTitle(HSSFSheet sheet) {
        // 第一步，先添加合并单元格
        // 直接从索引map中取得所有的位置信息，不进行遍历层级列表
        Set<String> set = indexMap.keySet();
        for (String s : set) {
            Map<String, Integer> map = indexMap.get(s);
            int rowStart = map.get("rowStart");
            int rowEnd = map.get("rowEnd");
            int colStart = map.get("colStart");
            int colEnd = map.get("colEnd");
            CellRangeAddress region = new CellRangeAddress(rowStart, rowEnd, colStart, colEnd);
            sheet.addMergedRegion(region);
        }

        // 第二部，给单元格赋值（表头的display）
        for (int i = 0; i < rowList.size(); i++) {
            Row row = sheet.createRow(i);
            ArrayList<JSONObject> array = rowList.get(i);
            for (int j = 0; j < array.size(); j++) {
                JSONObject object = array.get(j);
                String display = "";
                //多表头的传递时json中的display
                if (object.getString("display1") != null) {
                    display = object.getString("display1");
                //单表头传递时json中的name
                } else if (object.getString("name") != null) {
                    display = object.getString("name");
                }
                String id = object.getString("id");
                Map<String, Integer> map = indexMap.get(id);
                int colStart = map.get("colStart");
                Cell cell = row.createCell(colStart);
                cell.setCellValue(display);
            }
        }
    }

    static private void setData(HSSFSheet sheet, List<Map<String, Object>> dataList) {
        // excel数据
        // 跳过前l.size行
        int len = rowList.size();
        for (int j = 0; j < dataList.size(); j++) {
            int index = j + len;
            Row row = sheet.createRow(index);
            Map<String, Object> map = dataList.get(j);
            int temp = 0;
            for (int k = 0; k < idList.size(); k++) {
                Cell cell = row.createCell(temp);
                String id = idList.get(k);
                //匹配对应列进行赋值
                if (!"".equals(id)) {
                    if (map.get(id) == null) {
                        map.put(id, "");
                    } else {
                        cell.setCellValue(new HSSFRichTextString(map.get(id).toString()));
                    }
                }
                temp++;
            }
        }
    }
}
