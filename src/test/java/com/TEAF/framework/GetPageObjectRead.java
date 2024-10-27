package com.TEAF.framework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

import org.apache.log4j.Logger;
import org.apache.poi.EmptyFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;

import cucumber.runtime.CucumberException;

public class GetPageObjectRead {
	static String PageObjectFilesPath = "/src/test/java/com/TEAF/TestFiles/PageObjects";
	static ClassLoader cl;
	static Class<?> cls;
	public static BufferedReader OR;
	static By by;
	static String[] LocatorType;
	static String[] urlParameter;
	static String url;
	static String ClassPath;
	static String elementName;
	static String locator;
	public static String App;

	static Logger log = Logger.getLogger(GetPageObjectRead.class);

	public static String OR_GetURL(String app) throws Exception {
		try {
			App = app;
			if (System.getProperty("test.pageObjectMode").equalsIgnoreCase("xlsx")) {
				log.info("App: " + app);
				if (GetPageObjectRead.ReadExcel(app)) {
					log.info("Locators File read completed successfully!");
					// urlParameter = HashMapContainer.getPO(app.toLowerCase()).split(",");
					try {
						url = System.getProperty("test.appUrl");
					} catch (NullPointerException e) {
						// url = urlParameter[1];
						log.error("No URL Specified in Config File or Test Runner");
					}
					log.info("URL: " + url);
				} else {
					log.info("AUT Folder or Locator Files not available! ");
					log.info("New App Locator Folder Created: " + System.getProperty("user.dir")
							+ "/src/test/java/com/TEAF/TestFiles/PageObjects/" + app);
					url = System.getProperty("test.appUrl");
					log.info("URL: " + url);
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			throw new Exception(e);
		}
		return url.toString().trim();
	}

	public static By OR_GetElement(String element) {
		try {
			String Locator = HashMapContainer.getPO(element);
			if (System.getProperty("test.pageObjectMode").equalsIgnoreCase("xlsx")) {
				LocatorType = Locator.split(",", 2);
			} else if (System.getProperty("test.pageObjectMode").equalsIgnoreCase("class")) {
				log.debug("Element: " + element);
				log.debug("HM Value of Element: " + HashMapContainer.getPO(element));
				log.debug("Locator: " + Locator);
				LocatorType = Locator.split("=", 2);

			}
		} catch (Exception e) {
			// e.printStackTrace();
			log.info("Verify entries of PageObject data in your \"" + System.getProperty("test.pageObjectMode")
					+ "\" file!: " + element);
			throw new CucumberException(e);
		}
		return WrapperFunctions.setLocator(LocatorType[0].toString().trim(), LocatorType[1].toString().trim());
	}

	public void add(String Element) {
		String[] ElementRef = Element.split("::");
		HashMapContainer.addPO(ElementRef[0].toLowerCase(), ElementRef[1]);
	}

	public static boolean ReadExcel(String fileName) throws Exception {
		try {
			String xlsxPath = "";
			xlsxPath = System.getProperty("user.dir") + PageObjectFilesPath + "/" + fileName;
			File locatorFileFolder = new File(xlsxPath);
			if (!locatorFileFolder.exists()) {
				locatorFileFolder.mkdir();
				return false;
			}
			log.info(xlsxPath);
			File dir = new File(xlsxPath);
			File[] listFiles = dir.listFiles();
			if (listFiles.length == 0) {
				log.info("No Locator files present in folder:" + xlsxPath);
				return false;
			} else {
				for (int i = 0; i < listFiles.length; i++) {
					log.info("Page object File(Count and Path): " + (i + 1) + " : " + listFiles[i]);
				}
				for (File file : listFiles) {
					if (file.toString().endsWith(".xlsx")) {
						FileInputStream fin = new FileInputStream(file);
						XSSFWorkbook workbook;
						try {
							workbook = new XSSFWorkbook(fin);

							for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
								XSSFSheet sheet = workbook.getSheetAt(i);
								for (int j = 1; j < sheet.getPhysicalNumberOfRows(); j++) {
									Row row = sheet.getRow(j);
									Cell cell = row.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK);
									elementName = cell.getStringCellValue();
									elementName = elementName.toLowerCase();
									String sK = row.getCell(1, MissingCellPolicy.CREATE_NULL_AS_BLANK)
											.getStringCellValue();
									Cell cellV = row.getCell(2, MissingCellPolicy.CREATE_NULL_AS_BLANK);
									String sV = null;

									if (cellV.getCellType().equals(CellType.STRING)) {
										sV = row.getCell(2, MissingCellPolicy.CREATE_NULL_AS_BLANK)
												.getStringCellValue();
									} else if (cellV.getCellType().equals(CellType.NUMERIC)) {
										double d = row.getCell(2, MissingCellPolicy.CREATE_NULL_AS_BLANK)
												.getNumericCellValue();
										long l = (long) d;
										sV = String.valueOf(l);
									}
									log.debug("Locator: " + sK + " " + sV);
									locator = sK + "," + sV;
									if (locator.length() > 2) {
										log.debug("ElementName: " + elementName);
										log.debug("Locator: " + locator);
										HashMapContainer.addPO(elementName, locator);
									}
								}
							}
						} catch (EmptyFileException e) {
							// TODO Auto-generated catch block
							log.info("Locator File is Empty");
							return false;
						} catch (Exception e) {
							throw new CucumberException(e);
						}
					}
				}
			}
			log.info("Page Objects load completed!");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);

		}
		return true;
	}


}
