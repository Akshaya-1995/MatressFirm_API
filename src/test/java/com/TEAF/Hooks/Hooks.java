package com.TEAF.Hooks;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.TEAF.framework.StepBase;
import com.TEAF.framework.Utilities;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.AfterStep;
import cucumber.api.java.Before;

public class Hooks {
	private static WebDriver driver = StepBase.getDriver();
	public static int code;

	public static LinkedHashMap<String, String> scenarioStatus = new LinkedHashMap<String, String>();
	static Logger log = Logger.getLogger(Hooks.class.getName());
	public static Scenario scenario;
	static int scenarioOutline;


	@Before
	public void BeforeScenarioSteps(Scenario sc) {
		
		scenario = sc;
		try {
			if (!System.getProperty("t est.browserName").equalsIgnoreCase("REST Service")) {

				StepBase.setScenario(scenario);
				if (System.getProperty("test.postScenarioTearDown").equals("true")) {
					StepBase.setUp(System.getProperty("test.platformName"), System.getProperty("test.browserName"));
				}

				try {
					if (!StepBase.testPlatform.equalsIgnoreCase("mobile")) {
						if (System.getProperty("test.disableToastMessage").equals("false")) {

							Utilities.testStatusToastMessage(scenario.getName());

						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error(e);
				}
			} else {

			}
		} catch (Exception e) {
			log.error(e);
		}
	}

	
	@After
	public void AfterScenarioSteps(Scenario scenario) {
		try {

				if (!System.getProperty("test.browserName").equalsIgnoreCase("REST Service")) {
					if (!StepBase.testPlatform.equalsIgnoreCase("mobile")) {
						if (System.getProperty("test.disableToastMessage").equals("false")) {

							Utilities.testStatusFailToastMessage(scenario.getName());
						}
					}
					if (!System.getProperty("test.appType").equalsIgnoreCase("windowsapp")
							|| !StepBase.testPlatform.equalsIgnoreCase("mobile")
							|| !StepBase.testBrowser.equalsIgnoreCase("REST Service")) {
						final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
						scenario.embed(screenshot, "image/png"); // Stick it to HTML report
						Utilities.takeScreenshot(driver);

					}
				}
			
		
			
			String name = scenario.getName();
			log.debug(name);
			String status = scenario.getStatus().toString();
			log.info("Scenario - " + name + " : status - " + status);

			if (scenarioStatus.containsKey(name)) {
				name = name + " Examples " + scenarioOutline++;
			}
			scenarioStatus.put(name, status);

			if (!System.getProperty("test.browserName").equalsIgnoreCase("REST Service")) {

				if (System.getProperty("test.postScenarioTearDown").equals("true")) {
					StepBase.tearDown();
				}
				if (scenario.getStatus().toString().equalsIgnoreCase("PASSED")
						&& !StepBase.testPlatform.equalsIgnoreCase("mobile")) {
					if (System.getProperty("test.disableToastMessage").equals("false")) {
						Utilities.testStatusToastPass(scenario.getName());

					}
				}
			}
		} catch (Exception e) {
			log.error(e);

		}
	}
}
