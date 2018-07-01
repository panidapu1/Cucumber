package com.test;

import java.util.List;

import cucumber.api.DataTable;
import gherkin.formatter.model.DataTableRow;
public class ExcelTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//ExcelUtils EU = new ExcelUtils();
			//ExcelUtils.setExcelFile("test.xlsx","Sheet1");
			//System.out.println(ExcelUtils.getCellData(0,0));
			ExcelDataToDataTable exceldata = new ExcelDataToDataTable();
			DataTable table = exceldata.transform("TestData.xlsx");
			System.out.print(table.toString());
			
			List<List<String>> data=  table.raw();
			for(List<String> str : data){
				for(String str1 : str){
					System.out.print(" | " + str1 );
					
				}
				System.out.println(" | ");
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
