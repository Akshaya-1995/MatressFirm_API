package com.TEAF.framework;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.TEAF.Hooks.Hooks;
import com.galenframework.config.GalenConfig;
import com.galenframework.config.GalenProperty;

public class Execution {

	static Logger log = Logger.getLogger(Execution.class.getName());
	static String EPlatform;
	static String EBrowser;
	static String ETestName;

	public static void setup(String Platform, String Browser, String URL, String appType, String winAppLocation,
			String TestName, String jsonFileName) throws Throwable {
		try {
			EPlatform = Platform;
			EBrowser = Browser;
			
			ETestName = System.getProperty("TestName", TestName);



			File reportExcel = new File(System.getProperty("user.dir") + "/TestData/TestDatas.xlsx");
			if (TestName.toLowerCase().contains("chrome")) {
				File destination = new File(
						System.getProperty("user.dir") + "/src/test/java/com/Resources/TestDatas_Chrome.xlsx");
				FileUtils.copyFile(reportExcel, destination);
				System.setProperty("test.testdataFile", destination.getAbsolutePath());

			} else if (TestName.toLowerCase().contains("firefox")) {
				File destination = new File(
						System.getProperty("user.dir") + "/src/test/java/com/Resources/TestDatas_Firefox.xlsx");
				FileUtils.copyFile(reportExcel, destination);
				System.setProperty("test.testdataFile", destination.getAbsolutePath());

			} else if (TestName.toLowerCase().contains("edge")) {
				File destination = new File(
						System.getProperty("user.dir") + "/src/test/java/com/Resources/TestDatas_Edge.xlsx");
				FileUtils.copyFile(reportExcel, destination);
				System.setProperty("test.testdataFile", destination.getAbsolutePath());

			} else if (TestName.toLowerCase().contains("safari")) {
				File destination = new File(
						System.getProperty("user.dir") + "/src/test/java/com/Resources/TestDatas_safari.xlsx");
				FileUtils.copyFile(reportExcel, destination);
				System.setProperty("test.testdataFile", destination.getAbsolutePath());

			}

			// if (EBrowser.equals("DataBase Testing")) {
//				DataBaseSteps.i_connect_sql_db();
			// }

			PropertyConfigurator.configure(
					System.getProperty("user.dir") + "/src/test/java/com/TEAF/TestFiles/Config/log4j.properties");

			// LoadConfigurations
			TestConfig.LoadAllConfig();
			Browser = System.getProperty("BrowserName", Browser);
			appType = System.getProperty("AppType", appType);

			winAppLocation = System.getProperty("AppLocation", winAppLocation);
			URL = System.getProperty("URL", URL);
			String qMetrySummary = System.getProperty("TestSummary","Execution in Local");
			
			String sprintId = System.getProperty("SprintId","699");

			
			
			System.setProperty("test.qmetrySummary", qMetrySummary);
			System.setProperty("test.qmetrySprintId", sprintId);

			
			System.setProperty("test.platformName", Platform);
			System.setProperty("test.browserName", Browser);
			System.setProperty("test.jsonFileName", jsonFileName);

			System.setProperty("test.appType", appType);

			if (Browser.equals("BS-Chrome-Win10")) {
				Browser = "browserstack-desktop-browser-local";
				System.setProperty("test.browserName", "browserstack-desktop-browser-local");

				System.setProperty("test.bstack.browser", "Chrome");
				System.setProperty("test.bstack.browserVersion", "80.0");
				System.setProperty("test.bstack.os", "Windows");
				System.setProperty("test.bstack.osVersion", "10");
				System.setProperty("test.bstack.resolution", "1280x1024");
			} else if (Browser.equals("BS-Firefox-Win8")) {
				Browser = "browserstack-desktop-browser-local";

				System.setProperty("test.browserName", "browserstack-desktop-browser-local");

				System.setProperty("test.bstack.browser", "Firefox");
				System.setProperty("test.bstack.browserVersion", "73.0");
				System.setProperty("test.bstack.os", "Windows");
				System.setProperty("test.bstack.osVersion", "8");
				System.setProperty("test.bstack.resolution", "1280x1024");
			} else if (Browser.equals("BS-Edge-Win10")) {
				Browser = "browserstack-desktop-browser-local";

				System.setProperty("test.browserName", "browserstack-desktop-browser-local");

				System.setProperty("test.bstack.browser", "Edge");
				System.setProperty("test.bstack.browserVersion", "80.0");
				System.setProperty("test.bstack.os", "Windows");
				System.setProperty("test.bstack.osVersion", "10");
				System.setProperty("test.bstack.resolution", "1280x1024");
			} else if (Browser.equals("BS-Safari-OS-X")) {
				Browser = "browserstack-desktop-browser-local";

				System.setProperty("test.browserName", "browserstack-desktop-browser-local");

				System.setProperty("test.bstack.browser", "Safari");
				System.setProperty("test.bstack.browserVersion", "11.1");
				System.setProperty("test.bstack.os", "OS X");
				System.setProperty("test.bstack.osVersion", "High Sierra");
				System.setProperty("test.bstack.resolution", "1280x1024");
			} else if (Browser.equals("BS-Safari-OS-X")) {
				Browser = "browserstack-desktop-browser-local";

				System.setProperty("test.browserName", "browserstack-desktop-browser-local");

				System.setProperty("test.bstack.browser", "Safari");
				System.setProperty("test.bstack.browserVersion", "11.1");
				System.setProperty("test.bstack.os", "OS X");
				System.setProperty("test.bstack.osVersion", "High Sierra");
				System.setProperty("test.bstack.resolution", "1280x1024");
			} else if (Browser.equals("BS-Android-Pixel3")) {

				Browser = "bstack-mob-webapp-local";
				System.setProperty("test.browserName", "bstack-mob-webapp-local");

				System.setProperty("test.bstack.device", "Google Pixel 3");
				System.setProperty("test.bstack.mobOsVersion", "10.0");
				System.setProperty("test.bstack.browserName", "Android");
			} else if (Browser.equals("BS-Android-SamsungS20")) {

				Browser = "bstack-mob-webapp-local";
				System.setProperty("test.browserName", "bstack-mob-webapp-local");

				System.setProperty("test.bstack.device", "Samsung Galaxy S20");
				System.setProperty("test.bstack.mobOsVersion", "10.0");
				System.setProperty("test.bstack.browserName", "Android");
			} else if (Browser.equals("BS-Android-GalaxyS9")) {

				Browser = "bstack-mob-webapp-local";
				System.setProperty("test.browserName", "bstack-mob-webapp-local");

				System.setProperty("test.bstack.device", "Samsung Galaxy S9 Plus");
				System.setProperty("test.bstack.mobOsVersion", "9.0");
				System.setProperty("test.bstack.browserName", "Android");
			} else if (Browser.equals("BS-iOS-iPhone11")) {

				Browser = "bstack-mob-webapp-local";
				System.setProperty("test.browserName", "bstack-mob-webapp-local");

				System.setProperty("test.bstack.device", "iPhone 11");
				System.setProperty("test.bstack.mobOsVersion", "14");
				System.setProperty("test.bstack.browserName", "iPhone");
			} else if (Browser.equals("BS-iOS-iPadpro")) {

				Browser = "bstack-mob-webapp-local";
				System.setProperty("test.browserName", "bstack-mob-webapp-local");

				System.setProperty("test.bstack.device", "iPad Pro 12.9 2020");
				System.setProperty("test.bstack.mobOsVersion", "14");
				System.setProperty("test.bstack.browserName", "iPhone");
			} else if (Browser.equals("BS-iOS-iPhone11Pro")) {

				Browser = "bstack-mob-webapp-local";
				System.setProperty("test.browserName", "bstack-mob-webapp-local");

				System.setProperty("test.bstack.device", "iPhone 11 Pro");
				System.setProperty("test.bstack.mobOsVersion", "13");
				System.setProperty("test.bstack.browserName", "iPhone");
			} else if (Browser.equals("BS-iOS-iPhone12XR")) {

				Browser = "bstack-mob-webapp-local";
				System.setProperty("test.browserName", "bstack-mob-webapp-local");

				System.setProperty("test.bstack.device", "iPhone XR");
				System.setProperty("test.bstack.mobOsVersion", "12");
				System.setProperty("test.bstack.browserName", "iPhone");
			} else if (Browser.equals("BS-iOS-iPhone8")) {

				Browser = "bstack-mob-webapp-local";
				System.setProperty("test.browserName", "bstack-mob-webapp-local");

				System.setProperty("test.bstack.device", "iPhone 8");
				System.setProperty("test.bstack.mobOsVersion", "11");
				System.setProperty("test.bstack.browserName", "iPhone");
			}

			if (winAppLocation != null) {
				System.setProperty("test.winDesktopApp", winAppLocation);
			}
			if (URL != null) {
				System.setProperty("test.appUrl", URL);

			}
			System.setProperty("test.TestName", TestName);

			// Galen Property
			GalenConfig.getConfig().setProperty(GalenProperty.SCREENSHOT_FULLPAGE, "true");

			log.info("Platform: " + Platform);
			log.info("Browser : " + Browser);
			if (Platform.equals("desktop") && !Browser.equals("REST Service") && !Browser.equals("DataBase Testing")) {
				StepBase.setUp(Platform, Browser);
			} else if (Platform.equals("mobile") && !Browser.equals("REST Service")
					&& !Browser.equals("DataBase Testing")) {
				StepBase.setUp(Platform, Browser);
			} else {
				System.out.println("Enter valid platform choice: desktop / android / ios");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void tearDown(String successFulRecipients, String failureRecipients, String successFulCCRecipients,
			String failureCCRecipients) {

		try {

			if (EPlatform.equals("desktop") && !EBrowser.equals("REST Service")
					&& !EBrowser.equals("DataBase Testing")) {
				StepBase.tearDown();
			}
			
			if (EPlatform.equals("desktop") && EBrowser.equals("REST Service")
					&& !EBrowser.equals("DataBase Testing")) {
				StepBase.tearDown();
			}


			// if (EBrowser.equals("DataBase Testing")) {
//				DatabaseUtility.closeSQLDBConnection();
			// }

			Collection<String> values = com.TEAF.Hooks.Hooks.scenarioStatus.values();
			String toMailId = null;
			String ccMailId = null;
			boolean flag = false;
			if ((values.contains("FAILED") || values.contains("SKIPPED"))
					&& !(com.TEAF.Hooks.Hooks.code == 520 || com.TEAF.Hooks.Hooks.code == 521)) {
				flag = true;
			}

			log.info("Failure Present: " + flag);
			if (flag == true) {
				// Mailing only internal team to review the failures
				toMailId = successFulRecipients;
				ccMailId = successFulCCRecipients;
			} else {
				// Mailing everyone as results look good
				toMailId = failureRecipients;
				ccMailId = failureCCRecipients;
			}


			// Generation of Default Cucumber Reports
			if (!System.getProperty("test.disableCucumberReport").equalsIgnoreCase("true")) {

				GenerateCustomReport.generateCustomeReport(System.getProperty("test.browserName"),
						System.getProperty("test.platformName"), System.getProperty("test.jsonFileName"));
				HashMapContainer.ClearHM();
			}

			String zipReportFiles = ZipFiles.zipReportFiles(GenerateCustomReport.reportPath,
					System.getProperty("reports.FolderName"));

			// Prepare reports for Email
			if (System.getProperty("test.generateEmail").equalsIgnoreCase("true")) {
				String toMail = System.getProperty("ToMailID", toMailId);
				String ccMail = System.getProperty("CCMailID", ccMailId);
				log.info("To email id : " + toMail);
				log.info("Cc email id : " + ccMail);
				try {
					Utilities.auto_generation_Email(toMail, ccMail);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}


			if (ETestName.toLowerCase().contains("chrome")) {
				File reportExcel = new File(
						System.getProperty("user.dir") + "/src/test/java/com/Resources/TestDatas_Chrome.xlsx");
				File destination = new File(GenerateCustomReport.reportPath + "/TestDatas_Chrome.xlsx");
				FileUtils.copyFile(reportExcel, destination);

			} else if (ETestName.toLowerCase().contains("firefox")) {
				File reportExcel = new File(
						System.getProperty("user.dir") + "/src/test/java/com/Resources/TestDatas_Firefox.xlsx");
				File destination = new File(GenerateCustomReport.reportPath + "/TestDatas_Firefox.xlsx");
				FileUtils.copyFile(reportExcel, destination);
			} else if (ETestName.toLowerCase().contains("edge")) {
				File reportExcel = new File(
						System.getProperty("user.dir") + "/src/test/java/com/Resources/TestDatas_Edge.xlsx");
				File destination = new File(GenerateCustomReport.reportPath + "/TestDatas_Edge.xlsx");
				FileUtils.copyFile(reportExcel, destination);
			} else if (ETestName.toLowerCase().contains("safari")) {
				File reportExcel = new File(
						System.getProperty("user.dir") + "/src/test/java/com/Resources/TestDatas_Safari.xlsx");
				File destination = new File(GenerateCustomReport.reportPath + "/TestDatas_Safari.xlsx");
				FileUtils.copyFile(reportExcel, destination);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				Utilities.deleteZipFiles(System.getProperty("reports.FolderName"));
				Utilities.deleteZipFiles("TestExecution_UIReports");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
