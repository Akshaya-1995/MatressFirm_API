package com.TEAF.TestFiles.Runner;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.TEAF.framework.Execution;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@CucumberOptions(dryRun = false, plugin = { "pretty",
		// "html:CukeNativeHTMLReport/",
		// "junit:src/test/java/com/TestResults/cucumber-report/cucumber_1.xml",
		"json:src/test/java/com/TestResults/cucumber-report/cucumber_1.json"}, strict = true, junit = "--step-notifications", features = {
				"src/test/java/com/TEAF/TestFiles/Features" }, glue = { "com.TEAF.Hooks",
						"com.TEAF.stepDefinitions" }, 
						tags = {"@Demo_get", "not @ignore" }, monochrome = true)

@RunWith(Cucumber.class)	
public class TestRunner_1 {
 
	public static String Platform = "desktop"; 		
	public static String Browser = "REST Service";		 
	public static String TestName = "RC TEAF";
	static String appType = "webapp";
	static String winAppLocation = null;	  
	static String URL = null;			
	static Logger log = Logger.getLogger(TestRunner_1.class.getName());		

	@BeforeClass
	public static void setUp() throws Throwable {
		try {
			String jsonFileName = "cucumber_1";
			Execution.setup(Platform, Browser, URL, appType, winAppLocation, TestName, jsonFileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

//	@AfterClass
////	public static void tearDown() throws Throwable {
////		String message = "RC TEAF";
////		System.setProperty("email.messageBody", message);
////		String successFulRecipients = "premkumar.g@royalcyber.com";
////		String successFulCCRecipients = "premkumar.g@royalcyber.com";
////		String failureRecipients = "premkumar.g@royalcyber.com";
////		String failureCCRecipients = "premkumar.g@royalcyber.com";
////		Execution.tearDown(successFulRecipients, successFulCCRecipients, failureRecipients, failureCCRecipients);
//	}

}