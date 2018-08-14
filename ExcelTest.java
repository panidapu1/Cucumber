package com.test;

import java.util.List;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.DataFormatter;

import cucumber.api.DataTable;
import gherkin.formatter.model.DataTableRow;
public class ExcelTest {
	
	public static void main(String[] args) {
	TreeMap<String,InputTable> tm = new TreeMap<String,InputTable>();
		// TODO Auto-generated method stub
		try {
			//ExcelUtils EU = new ExcelUtils();
			//ExcelUtils.setExcelFile("test.xlsx","Sheet1");
			//System.out.println(ExcelUtils.getCellData(0,0));
			ExcelDataToDataTable exceldata = new ExcelDataToDataTable();
			DataTable table = exceldata.transform("TestData.xlsx");
			//System.out.print(table.toString());
			
			List<List<String>> data=  table.raw();
		
			for(List<String> str : data){ 
				InputTable I1 = new InputTable();
				String Tkey = null;
				for(int i = 0; i < str.size(); i++){
					if (i == 0)
						Tkey = str.get(i);
					loadData(i,str.get(i),I1);
					System.out.print(" | " + str.get(i) );
					
				}
				tm.put(Tkey, I1);
				System.out.println(" | ");
			}
			
		//System.out.println(tm.get("FIN_D1").toString());	
		InputTable it = tm.get("FIN_D2");
		String s1 = it.getCostCentre("003");
		System.out.println(s1);
		System.out.println(it.getCategory());
		System.out.println(it.getDescription());
		System.out.println(it.getOhioBank());
		System.out.println(it.getArizonaBank());
		System.out.println(it.getUtahBank());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static void loadData(int j, String costc, InputTable i2 ) {
		
	
		switch (j) {
		case 0:
			i2.setCategory(costc);
		case 1:
			i2.setDescription(costc);
		case 2:
			i2.setOhioBank(costc);
		case 3:
			i2.setArizonaBank(costc);
		case 4:
			i2.setUtahBank(costc);
		default : 	
		}
		
	}

}
class InputTable{
	private String category;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOhioBank() {
		return ohioBank;
	}
	public void setOhioBank(String ohioBank) {
		this.ohioBank = ohioBank;
	}
	public String getArizonaBank() {
		return arizonaBank;
	}
	public void setArizonaBank(String arizonaBank) {
		this.arizonaBank = arizonaBank;
	}
	public String getUtahBank() {
		return utahBank;
	}
	public void setUtahBank(String utahBank) {
		this.utahBank = utahBank;
	}
	private String description;
	private String ohioBank;
	private String arizonaBank;
	private String utahBank;
	
	public InputTable() {
		
	}
	public String getCostCentre(String bank) {
		String costcenter = null;
		if (bank.equalsIgnoreCase("001") )
		costcenter = getOhioBank();
		if (bank.equalsIgnoreCase("002") )
			costcenter = getArizonaBank();
		if (bank.equalsIgnoreCase("003") )
			costcenter = getUtahBank();
		
//		switch(bank) {
//		case "001":
//			costcenter = getOhioBank();
//			System.out.println(costcenter);
//		case "002":
//			costcenter = getArizonaBank();
//			System.out.println(costcenter);
//		case "003":
//			costcenter = getUtahBank();
//			System.out.println(costcenter);
//		//default : costcenter = "";	
//		}
		return costcenter;
		
	}
}
