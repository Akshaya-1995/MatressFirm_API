package com.TEAF.framework;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import org.apache.log4j.Logger;

public class TestConfig {

	public static Properties objConfig = new Properties();
	static Logger log = Logger.getLogger(TestConfig.class.getName());

	public static void LoadAllConfig() {
		try {
			objConfig = new Properties();
			objConfig.load(new FileInputStream(
					System.getProperty("user.dir") + "/src/test/java/com/TEAF/TestFiles/Config/Config.properties"));
			objConfig.setProperty("os.name", System.getProperty("os.name"));
			
			System.setProperty("test.appType", objConfig.getProperty("test.appType"));
			try {

				System.setProperty("test.appUrl", objConfig.getProperty("test.appUrl"));
				System.setProperty("test.platformName", objConfig.getProperty("test.platformName"));
				System.setProperty("test.browserName", objConfig.getProperty("test.browserName"));
				log.info("Using url specified in config file");

			} catch (Exception e) {
				log.error("URL needs to be specified in the locator excel file if not specified in config file");
			}
			System.setProperty("test.generateFTPTransfer", objConfig.getProperty("test.generateFTPTransfer"));

			
			System.setProperty("test.projectName", objConfig.getProperty("test.projectName"));
			System.setProperty("test.postScenarioTearDown", objConfig.getProperty("test.postScenarioTearDown"));
			System.setProperty("test.disableCucumberReport", objConfig.getProperty("test.disableCucumberReport"));
			System.setProperty("test.disableGalenReport", objConfig.getProperty("test.disableGalenReport"));
			System.setProperty("test.disableScreenshotCapture", objConfig.getProperty("test.disableScreenshotCapture"));
			System.setProperty("test.highlightElements", objConfig.getProperty("test.highlighElements"));
			System.setProperty("test.pageObjectMode", objConfig.getProperty("test.pageObjectMode"));
			System.setProperty("test.implicitlyWait", objConfig.getProperty("test.implicitlyWait"));
			System.setProperty("test.pageLoadTimeout", objConfig.getProperty("test.pageLoadTimeout"));
			System.setProperty("test.headless", objConfig.getProperty("test.headless"));
			System.setProperty("test.generateEmail", objConfig.getProperty("test.generateEmail"));
			System.setProperty("test.bstack.userName", objConfig.getProperty("test.bstack.userName"));
			System.setProperty("test.bstack.automateKey", objConfig.getProperty("test.bstack.automateKey"));
			System.setProperty("test.bstack.browserName", objConfig.getProperty("test.bstack.browserName"));
			System.setProperty("test.bstack.browser", objConfig.getProperty("test.bstack.browser"));
			System.setProperty("test.bstack.local", objConfig.getProperty("test.bstack.local"));
			System.setProperty("test.bstack.localIdentifier", objConfig.getProperty("test.bstack.localIdentifier"));
			System.setProperty("test.bstack.debug", objConfig.getProperty("test.bstack.debug"));
			System.setProperty("test.bstack.browserVersion", objConfig.getProperty("test.bstack.browserVersion"));
			System.setProperty("test.bstack.os", objConfig.getProperty("test.bstack.os"));
			System.setProperty("test.bstack.osVersion", objConfig.getProperty("test.bstack.osVersion"));
			System.setProperty("test.bstack.resolution", objConfig.getProperty("test.bstack.resolution"));
			System.setProperty("test.bstack.testName", objConfig.getProperty("test.bstack.testName"));
			System.setProperty("test.bstack.device", objConfig.getProperty("test.bstack.device"));
			System.setProperty("test.bstack.mobOsVersion", objConfig.getProperty("test.bstack.mobOsVersion"));

			System.setProperty("test.hubUrl", objConfig.getProperty("test.hubUrl"));
			System.setProperty("test.appium.url", objConfig.getProperty("test.appium.url"));
			System.setProperty("email.smtp.server", objConfig.getProperty("email.smtp.server"));
			System.setProperty("email.smtp.portNumber", objConfig.getProperty("email.smtp.portNumber"));
			System.setProperty("email.smtp.socketPortNumber", objConfig.getProperty("email.smtp.socketPortNumber"));
			System.setProperty("email.user.emailId", objConfig.getProperty("email.user.emailId"));
			System.setProperty("email.user.password", objConfig.getProperty("email.user.password"));
			System.setProperty("email.user.emailAddress", objConfig.getProperty("email.user.emailAddress"));

			System.setProperty("email.subject", objConfig.getProperty("email.subject"));
			System.setProperty("email.messageBody", objConfig.getProperty("email.messageBody"));
			System.setProperty("email.signature", objConfig.getProperty("email.signature"));
			
			System.setProperty("reports.ProjectName", objConfig.getProperty("reports.ProjectName"));
			System.setProperty("reports.BuildNumber", objConfig.getProperty("reports.BuildNumber"));
			System.setProperty("test.TestName", objConfig.getProperty("test.TestName"));

			System.setProperty("test.disableToastMessage", objConfig.getProperty("test.disableToastMessage"));
			System.setProperty("reports.FolderName", objConfig.getProperty("reports.FolderName"));

			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
