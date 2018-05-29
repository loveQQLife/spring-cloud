package com.kreken.util;

import com.kreken.exception.ExcelException;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * @Author : @William
 * @Desc  : JXL 对2007 2010得版本不兼容,只支持2003
 * @Comments : 导入导出Excel工具类
 * @Version : 1.0.0
 */

public class JxlExcelUtil {

	/**
	 * @MethodName : listToExcel
	 * @Description : 导出Excel（可以导出到本地文件系统，也可以导出到浏览器，可自定义工作表大小）
	 * @param list
	 *            数据源
	 * @param fieldMap
	 *            类的英文属性和Excel中的中文列名的对应关系 如果需要的是引用对象的属性，则英文属性使用类似于EL表达式的格式
	 *            如：list中存放的都是student，student中又有college属性，而我们需要学院名称，则可以这样写
	 *            fieldMap.put("college.collegeName","学院名称")
	 * @param sheetName
	 *            工作表的名称
	 * @param sheetSize
	 *            每个工作表中记录的最大个数
	 * @param out
	 *            导出流
	 * @throws ExcelException
	 */
	public static <T> void listToExcel(List<T> list, LinkedHashMap<String, String> fieldMap, String sheetName,
			int sheetSize, OutputStream out) throws ExcelException {

		if (list.size() == 0 || list == null) {
			throw new ExcelException("数据源中没有任何数据");
		}

		if (sheetSize > 65535 || sheetSize < 1) {
			sheetSize = 65535;
		}

		// 创建工作簿并发送到OutputStream指定的地方
		WritableWorkbook wwb;
		try {
			wwb = Workbook.createWorkbook(out);

			// 因为2003的Excel一个工作表最多可以有65536条记录，除去列头剩下65535条
			// 所以如果记录太多，需要放到多个工作表中，其实就是个分页的过程
			// 1.计算一共有多少个工作表
			double sheetNum = Math.ceil(list.size() / new Integer(sheetSize).doubleValue());

			// 2.创建相应的工作表，并向其中填充数据
			for (int i = 0; i < sheetNum; i++) {
				// 如果只有一个工作表的情况
				if (1 == sheetNum) {
					WritableSheet sheet = wwb.createSheet(sheetName, i);
					fillSheet(sheet, list, fieldMap, 0, list.size() - 1);

					// 有多个工作表的情况
				} else {
					WritableSheet sheet = wwb.createSheet(sheetName + (i + 1), i);

					// 获取开始索引和结束索引
					int firstIndex = i * sheetSize;
					int lastIndex = (i + 1) * sheetSize - 1 > list.size() - 1 ? list.size() - 1
							: (i + 1) * sheetSize - 1;
					// 填充工作表
					fillSheet(sheet, list, fieldMap, firstIndex, lastIndex);
				}
			}

			wwb.write();
			wwb.close();

		} catch (Exception e) {
			e.printStackTrace();
			// 如果是ExcelException，则直接抛出
			if (e instanceof ExcelException) {
				throw (ExcelException) e;

				// 否则将其它异常包装成ExcelException再抛出
			} else {
				throw new ExcelException("导出Excel失败");
			}
		}

	}

	/**
	 * @MethodName : listToExcel
	 * @Description : 导出Excel（可以导出到本地文件系统，也可以导出到浏览器，工作表大小为2003支持的最大值）
	 * @param list
	 *            数据源
	 * @param fieldMap
	 *            类的英文属性和Excel中的中文列名的对应关系
	 * @param out
	 *            导出流
	 * @throws ExcelException
	 */
	public static <T> void listToExcel(List<T> list, LinkedHashMap<String, String> fieldMap, String sheetName,
			OutputStream out) throws ExcelException {

		listToExcel(list, fieldMap, sheetName, 65535, out);

	}

	/**
	 * @MethodName : listToExcel
	 * @Description : 导出Excel（导出到浏览器，可以自定义工作表的大小）
	 * @param list
	 *            数据源
	 * @param fieldMap
	 *            类的英文属性和Excel中的中文列名的对应关系
	 * @param sheetSize
	 *            每个工作表中记录的最大个数
	 * @param response
	 *            使用response可以导出到浏览器
	 * @throws ExcelException
	 */
	public static <T> void listToExcel(List<T> list, LinkedHashMap<String, String> fieldMap, String sheetName,
			int sheetSize, HttpServletResponse response) throws ExcelException {

		// 设置默认文件名为当前时间：年月日时分秒
		String fileName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();

		// 设置response头信息
		response.reset();
		response.setContentType("application/vnd.ms-excel"); // 改成输出excel文件
		response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");

		// 创建工作簿并发送到浏览器
		try {

			OutputStream out = response.getOutputStream();
			listToExcel(list, fieldMap, sheetName, sheetSize, out);

		} catch (Exception e) {
			e.printStackTrace();

			// 如果是ExcelException，则直接抛出
			if (e instanceof ExcelException) {
				throw (ExcelException) e;

				// 否则将其它异常包装成ExcelException再抛出
			} else {
				throw new ExcelException("导出Excel失败");
			}
		}
	}

	/**
	 * @MethodName : listToExcel
	 * @Description : 导出Excel（导出到浏览器，工作表的大小是2003支持的最大值）
	 * @param list
	 *            数据源
	 * @param fieldMap
	 *            类的英文属性和Excel中的中文列名的对应关系
	 * @param response
	 *            使用response可以导出到浏览器
	 * @throws ExcelException
	 */
	public static <T> void listToExcel(List<T> list, LinkedHashMap<String, String> fieldMap, String sheetName,
			HttpServletResponse response) throws ExcelException {

		listToExcel(list, fieldMap, sheetName, 65535, response);
	}

	/**
	 * @MethodName : excelToList
	 * @Description : 将Excel转化为List
	 * @param in
	 *            ：承载着Excel的输入流
	 * @param entityClass
	 *            ：List中对象的类型（Excel中的每一行都要转化为该类型的对象）
	 * @param fieldMap
	 *            ：Excel中的中文列头和类的英文属性的对应关系Map
	 * @param uniqueFields
	 *            ：指定业务主键组合（即复合主键），这些列的组合不能重复
	 * @return ：List
	 * @throws ExcelException
	 */
	public static <T> List<T> excelToList(InputStream in, String sheetName, Class<T> entityClass,
			LinkedHashMap<String, String> fieldMap, String[] uniqueFields) throws ExcelException {

		// 定义要返回的list
		List<T> resultList = new ArrayList<T>();

		try {

			// 根据Excel数据源创建WorkBook
			Workbook wb = Workbook.getWorkbook(in);
			// 获取工作表
			Sheet sheet = wb.getSheet(sheetName);

			// 获取工作表的有效行数
			int realRows = 0;
			for (int i = 0; i < sheet.getRows(); i++) {

				int nullCols = 0;
				for (int j = 0; j < sheet.getColumns(); j++) {
					Cell currentCell = sheet.getCell(j, i);
					if (currentCell == null || "".equals(currentCell.getContents().toString())) {
						nullCols++;
					}
				}

				if (nullCols == sheet.getColumns()) {
					break;
				} else {
					realRows++;
				}
			}

			// 如果Excel中没有数据则提示错误
			if (realRows <= 1) {
				throw new ExcelException("Excel文件中没有任何数据");
			}

			Cell[] firstRow = sheet.getRow(0);

			String[] excelFieldNames = new String[firstRow.length];

			// 获取Excel中的列名
			for (int i = 0; i < firstRow.length; i++) {
				excelFieldNames[i] = firstRow[i].getContents().toString().trim();
			}

			// 判断需要的字段在Excel中是否都存在
			boolean isExist = true;
			List<String> excelFieldList = Arrays.asList(excelFieldNames);
			for (String cnName : fieldMap.keySet()) {
				if (!excelFieldList.contains(cnName)) {
					isExist = false;
					break;
				}
			}

			// 如果有列名不存在，则抛出异常，提示错误
			if (!isExist) {
				throw new ExcelException("Excel中缺少必要的字段，或字段名称有误");
			}

			// 将列名和列号放入Map中,这样通过列名就可以拿到列号
			LinkedHashMap<String, Integer> colMap = new LinkedHashMap<String, Integer>();
			for (int i = 0; i < excelFieldNames.length; i++) {
				colMap.put(excelFieldNames[i], firstRow[i].getColumn());
			}

			// 判断是否有重复行
			// 1.获取uniqueFields指定的列
			Cell[][] uniqueCells = new Cell[uniqueFields.length][];
			for (int i = 0; i < uniqueFields.length; i++) {
				int col = colMap.get(uniqueFields[i]);
				uniqueCells[i] = sheet.getColumn(col);
			}

			// 2.从指定列中寻找重复行
			for (int i = 1; i < realRows; i++) {
				int nullCols = 0;
				for (int j = 0; j < uniqueFields.length; j++) {
					String currentContent = uniqueCells[j][i].getContents();
					Cell sameCell = sheet.findCell(currentContent, uniqueCells[j][i].getColumn(),
							uniqueCells[j][i].getRow() + 1, uniqueCells[j][i].getColumn(),
							uniqueCells[j][realRows - 1].getRow(), true);
					if (sameCell != null) {
						nullCols++;
					}
				}

				if (nullCols == uniqueFields.length) {
					throw new ExcelException("Excel中有重复行，请检查");
				}
			}

			// 将sheet转换为list
			for (int i = 1; i < realRows; i++) {
				// 新建要转换的对象
				T entity = entityClass.newInstance();

				// 给对象中的字段赋值
				for (Entry<String, String> entry : fieldMap.entrySet()) {
					// 获取中文字段名
					String cnNormalName = entry.getKey();
					// 获取英文字段名
					String enNormalName = entry.getValue();
					// 根据中文字段名获取列号
					int col = colMap.get(cnNormalName);

					// 获取当前单元格中的内容
					String content = sheet.getCell(col, i).getContents().toString().trim();

					// 给对象赋值
					ClassUtil.setFieldValueByName(enNormalName, content, entity);
				}

				resultList.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 如果是ExcelException，则直接抛出
			if (e instanceof ExcelException) {
				throw (ExcelException) e;

				// 否则将其它异常包装成ExcelException再抛出
			} else {
				e.printStackTrace();
				throw new ExcelException("导入Excel失败");
			}
		}
		return resultList;
	}

	/*
	 * <-------------------------辅助的私有方法----------------------------------------
	 * ------->
	 */

	/**
	 * @MethodName : setColumnAutoSize
	 * @Description : 设置工作表自动列宽和首行加粗
	 * @param ws
	 */
	private static void setColumnAutoSize(WritableSheet ws, int extraWith) {
		// 获取本列的最宽单元格的宽度
		for (int i = 0; i < ws.getColumns(); i++) {
			int colWith = 0;
			for (int j = 0; j < ws.getRows(); j++) {
				String content = ws.getCell(i, j).getContents().toString();
				int cellWith = content.length();
				if (colWith < cellWith) {
					colWith = cellWith;
				}
			}
			// 设置单元格的宽度为最宽宽度+额外宽度
			ws.setColumnView(i, colWith + extraWith);
		}

	}

	/**
	 * @MethodName : fillSheet
	 * @Description : 向工作表中填充数据
	 * @param sheet
	 *            工作表
	 * @param list
	 *            数据源
	 * @param fieldMap
	 *            中英文字段对应关系的Map
	 * @param firstIndex
	 *            开始索引
	 * @param lastIndex
	 *            结束索引
	 */
	private static <T> void fillSheet(WritableSheet sheet, List<T> list, LinkedHashMap<String, String> fieldMap,
			int firstIndex, int lastIndex) throws Exception {

		// 定义存放英文字段名和中文字段名的数组
		String[] enFields = new String[fieldMap.size()];
		String[] cnFields = new String[fieldMap.size()];

		// 填充数组
		int count = 0;
		for (Entry<String, String> entry : fieldMap.entrySet()) {
			enFields[count] = entry.getKey();
			cnFields[count] = entry.getValue();
			count++;
		}
		// 填充表头
		for (int i = 0; i < cnFields.length; i++) {
			Label label = new Label(i, 0, cnFields[i]);
			sheet.addCell(label);
		}

		// 填充内容
		int rowNo = 1;
		for (int index = firstIndex; index <= lastIndex; index++) {
			// 获取单个对象
			T item = list.get(index);
			for (int i = 0; i < enFields.length; i++) {
				Object objValue = ClassUtil.getFieldValueByNameSequence(enFields[i], item);
				String fieldValue = objValue == null ? "" : objValue.toString();
				Label label = new Label(i, rowNo, fieldValue);
				sheet.addCell(label);
			}

			rowNo++;
		}

		// 设置自动列宽
		setColumnAutoSize(sheet, 5);
	}

}
