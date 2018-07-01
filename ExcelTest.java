package com.automation.cucumber.steps;

import java.util.List;

import cucumber.api.DataTable;
import gherkin.formatter.model.DataTableRow;
import com.automation.cucumber.helper.*;
public class ExcelTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//ExcelUtils EU = new ExcelUtils();
			//ExcelUtils.setExcelFile("test.xlsx","Sheet1");
			//System.out.println(ExcelUtils.getCellData(0,0));
			FeatureOverright.overrideFeatureFiles("./src");
			
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
