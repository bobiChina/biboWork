package com.ligootech.webdtu.util;

import com.ligootech.webdtu.util.excel.Excel2003Reader;
import com.ligootech.webdtu.util.excel.Excel2007Reader;
import com.ligootech.webdtu.util.excel.IRowReader;
import com.ligootech.webdtu.util.excel.RowReaderImpl;

import java.util.List;

public class ExcelReaderUtil {

	//excel2003扩展名
	public static final String EXCEL03_EXTENSION = ".xls";
	//excel2007扩展名
	public static final String EXCEL07_EXTENSION = ".xlsx";

	/**
	 * 读取Excel文件，可能是03也可能是07版本
	 * @param fileName
	 * @throws Exception
	 * @author WLY
	 * @日期 2014-2-14 下午5:48:35
	 */
	public static List<List<String>> readExcel(String fileName) throws Exception{
		IRowReader rowReader = new RowReaderImpl();
		// 处理excel2003文件
		if (fileName.endsWith(EXCEL03_EXTENSION)){
			Excel2003Reader excel03 = new Excel2003Reader();
			excel03.setRowReader(rowReader);
			excel03.process(fileName);
			// 处理excel2007文件
		} else if (fileName.endsWith(EXCEL07_EXTENSION)){
			Excel2007Reader xlsx2csv = new Excel2007Reader();
			xlsx2csv.setRowReader(rowReader);
			xlsx2csv.process(fileName);
		} else {
			throw new  Exception("文件格式错误，fileName的扩展名只能是xls或xlsx。");
		}
		return rowReader.getRowList();
	}

	public static void main(String[] args) {
		//ExcelReaderUtil rd = new ExcelReaderUtil();
		String filePath = "";

		filePath = "E:\\ceshi.xlsx";
		filePath = "E:\\ceshi.xls";
		List<List<String>> rsList = null;
		List<List<String>> rsList2 = null;
		try {
			rsList = readExcel(filePath);
			rsList2 = readExcel("E:\\ceshi.xlsx");

		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("count=" + rd.count);
		System.out.println("SIZE=" + rsList.size());
		if (rsList != null && rsList.size() > 0) {
			for (int i = 0; i < rsList.size(); i++) {
				System.out.println("--------------------------------------------------");
				for (int j = 0; j < rsList.get(i).size(); j++) {
					System.out.print(rsList.get(i).get(j) + "==");
				}
				System.out.println();
			}
		}
		rsList.clear();

		System.out.println("第二个SIZE=" + rsList2.size());
		if (rsList2 != null && rsList2.size() > 0) {
			for (int i = 0; i < rsList2.size(); i++) {
				System.out.println("--------------------------------------------------");
				for (int j = 0; j < rsList2.get(i).size(); j++) {
					System.out.print(rsList2.get(i).get(j) + "==");
				}
				System.out.println();
			}
		}
		rsList2.clear();
	}
}
