package com.TEAF.framework;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

/**
 * @ScriptName    : GenerateCustomeReport
 * @Description   : This class generate test execution reports using net.masterthought 
 * @Author        : Swathin Ratheendren
 * @Creation Date : September 2016   @Modified Date:                       
 */
public class GenerateCustomReport
{
	// Local variables
	static public String reportPath;
	static private String jsonFilePath;
	static private String buildNumber;
	static private String buildProjectName;
	static private DateTimeFormatter dateFormat;
	
	
	public static void generateCustomeReport(String Browser, String Platform, String jsonPath)
	{
		try
		{
			dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd_HHmmss");
			String currentTimeStamp = dateFormat.print(new DateTime());
			reportPath = "src/test/java/com/TestResults/custom-report/"+System.getProperty("reports.FolderName", "Cucumber_Reports")+"_" + currentTimeStamp+"_"+Platform+"_"+Browser;
			jsonFilePath = "src/test/java/com/TestResults/cucumber-report/";
			//xmlFilePath = "src/test/java/com/TestResults/cucumber-report/cucumber.xml";

			List<String> jsonReportFiles = new ArrayList<String>();
			jsonReportFiles.add(jsonFilePath+ jsonPath+".json");
			
 			buildNumber = System.getProperty("reports.BuildNumber", "1");
			buildProjectName = System.getProperty("reports.ProjectName", "Royal Cyber Test Automation");
			//parallelTesting = true;
			/*ReportBuilder reportBuilder = new ReportBuilder(jsonReportFiles,
					new File(reportPath), pluginURLPath, buildNumber,
					buildProjectName, skippedFails, undefinedFails, flashCharts,
					runWithJenkins, artificatsEnabled, artifactsConfig, highCharts);*/
			Configuration configuration = new Configuration(new File(reportPath), buildProjectName);
	        // optionally only if you need
	       //configuration.setJenkinsBasePath(jenkinsBasePath);
	        configuration.setBuildNumber(buildNumber);
	     // addidtional metadata presented on main page
	        configuration.addClassifications("Platform", StepBase.testPlatform);
	        configuration.addClassifications("Browser", StepBase.testBrowser);
	        configuration.addClassifications("TestName", System.getProperty("test.TestName"));
			try {
				configuration.addClassifications("OperatingSystem", System.getProperty("test.TestOS"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}

	        if (Browser.contains("browserstack")) {
	        	configuration.addClassifications("test.bstack.browser", System.getProperty("test.bstack.browser"));
	        	configuration.addClassifications("test.bstack.browserVersion", System.getProperty("test.bstack.browserVersion"));
	        	configuration.addClassifications("test.bstack.os", System.getProperty("test.bstack.os"));
	        	configuration.addClassifications("test.bstack.osVersion", System.getProperty("test.bstack.osVersion"));
	        	configuration.addClassifications("test.bstack.resolution", System.getProperty("test.bstack.resolution"));
				
			}
	        ReportBuilder reportBuilder = new ReportBuilder(jsonReportFiles, configuration);
			reportBuilder.generateReports();

			//objUtilities.copyFileUsingStream(new File(jsonFilePath), new File(reportPath + "/cucumber.json"));
			//objUtilities.copyFileUsingStream(new File(xmlFilePath), new File(reportPath + "/cucumber.xml"));
			
		//	Runtime rTime = Runtime.getRuntime();
			String url = reportPath;
			String browser = "C:/Program Files (x86)/Google/Chrome/Application/chrome.exe ";
		//	Process pc = rTime.exec(browser +System.getProperty("user.dir")+"/"+ url);
		//	pc.waitFor();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		generateCustomeReport("chrome", "desktop", "cucumber_1");
	}
}
