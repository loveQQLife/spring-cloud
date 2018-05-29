package com.kreken.util;

import com.alibaba.fastjson.JSONObject;
import com.kreken.exception.ExcelException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;

/**
 * @Desc :  POI  Interface  2007 2010的sheet最大可支持1048576行数据，2003只支持65536行
 * @Comments : 导入Excel工具类
 * @Version : 1.0.0
 * @Since : 2017-09-22 17:47
 */
public class PoiExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(PoiExcelUtil.class);

    /**
     *  POI 映射成对应的实体类 (Jxl 只支持2003，不支持2007)
     *  支持2003 2007 2010版本的xls xlsx
     *  如果每个sheet都有标题,默认不包含
     *
     * @param inputStream 文件流
     * @param headerMap
     * @param entityClazz
     * @param <T>
     * @return
     * @throws ExcelException
     */
    public static <T> List<T> excelToList(InputStream inputStream, LinkedHashMap<String, String> headerMap, Class<T> entityClazz) throws ExcelException {

        List<T> list = new ArrayList<T>();//返回的数据
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);
            int sheetCount = workbook.getNumberOfSheets();

            Row first_Sheet_Row = workbook.getSheetAt(0).getRow(0);

            // 获取Excel中的列名
            String[] excelFieldNames = new String[first_Sheet_Row.getPhysicalNumberOfCells()];
            for (int i = 0; i < first_Sheet_Row.getPhysicalNumberOfCells(); i++) {
                excelFieldNames[i] = first_Sheet_Row.getCell(i).getStringCellValue();
            }

            // 判断需要的字段在Excel中是否都存在
            boolean isExist = true;
            List<String> excelFieldList = Arrays.asList(excelFieldNames);
            for (String cnName : headerMap.keySet()) {
                if (!excelFieldList.contains(cnName)) {
                    isExist = false;
                    break;
                }
            }

            if (!isExist) {
                throw new ExcelException("Excel中缺少必要的字段，或字段名称有误");
            }

            // 将列号和列名放入Map中,这样通过列号就可以拿到列名
            LinkedHashMap<Integer, String> colMap = new LinkedHashMap<Integer, String>();
            for (int i = 0; i < excelFieldNames.length; i++) {
                colMap.put(first_Sheet_Row.getCell(i).getColumnIndex(), excelFieldNames[i] );
            }

            //Sheet
            for (int s = 0; s < sheetCount; s++) {
                Sheet sheet = workbook.getSheetAt(s);
                int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
                //行
                for (int r = 0; r < rowCount; r++) {
                    T entityClass = entityClazz.newInstance();
                    Row row = sheet.getRow(r);
                    if (0 == r && 0 == s) {//不包含标题
                        continue;
                    }
                    if (s != 0 && r==0){//假如每个sheet都有标题
                        String cellName = sheet.getRow(r).getCell(r).getStringCellValue();
                        Boolean containKey = headerMap.containsKey(cellName);
                        if (containKey){
                            continue;
                        }
                    }
                    int cellCount = row.getPhysicalNumberOfCells(); //获取总列数
                    //列
                    for (int c = 0; c < cellCount; c++) {
                        Cell cell = row.getCell(c);
                        String cnNormalName = colMap.get(c);
                        String enNormalName = headerMap.get(cnNormalName);
                        ClassUtil.setFieldValueByName(enNormalName,cell,entityClass);
                    }
                    list.add(entityClass);
                }
            }
            return list;
        } catch (Exception e) {
            logger.error(JSONObject.toJSONString(e));
            if (e instanceof ExcelException) {
                throw (ExcelException) e;
            } else {
                throw new ExcelException("处理Excel数据失败");
            }
        }
    }
}
