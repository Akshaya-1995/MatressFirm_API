package com.TEAF.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GalenConfig {
	
	public static WebDriver driver = StepBase.getDriver();
private final static Logger LOG = LoggerFactory.getLogger(GalenConfig.class);
    
    public final static GalenConfig instance = new GalenConfig();
    public static final String SCREENSHOT_AUTORESIZE = "galen.screenshot.autoresize";
    public static final String SCREENSHOT_FULLPAGE = "galen.browser.screenshots.fullPage";
    // smart waiting for scroll position, but with a timeout, set to zero to turn off smart wait
    public static final String SCREENSHOT_FULLPAGE_SCROLLTIMEOUT= "galen.browser.screenshots.fullPage.scrollTimeout";
    // hard wait during scroll
    public static final String SCREENSHOT_FULLPAGE_SCROLLWAIT = "galen.browser.screenshots.fullPage.scrollWait";
    public static final String SPEC_IMAGE_TOLERANCE = "galen.spec.image.tolerance";
    public static final String SPEC_IMAGE_ERROR_RATE = "galen.spec.image.error";
    public static final String SPEC_GLOBAL_VISIBILITY_CHECK = "galen.spec.global.visibility";

    public static final String TEST_JS_SUFFIX = "galen.test.js.file.suffix";
    public static final String TEST_SUFFIX = "galen.test.file.suffix";
    private int rangeApproximation;
    private List<String> reportingListeners;
    private String defaultBrowser;
    private Properties properties;
    
    private GalenConfig() {
        try {
          loadConfig();
        }
        
        catch (Exception e) {
            LOG.trace("Unknown error during Galen Config", e);
        }
    }
    
    private void loadConfig() throws IOException {
        this.properties = new Properties();

        // TODO use constant
        File configFile = new File(System.getProperty("galen.config.file", "config"));
        
        if (configFile.exists() && configFile.isFile()) {
            InputStream in = new FileInputStream(configFile);
            properties.load(in);
            in.close();
        }

        // TODO use constant
        rangeApproximation = Integer.parseInt(readProperty("galen.range.approximation", "5"));
        reportingListeners = converCommaSeparatedList(readProperty("galen.reporting.listeners", ""));
        defaultBrowser = readProperty("galen.default.browser", "firefox");
    }

    private List<String> converCommaSeparatedList(String text) {
        String[] arr = text.split(",");
        
        List<String> list = new LinkedList<String>();
        for (String item : arr) {
            String itemText = item.trim();
            if (!itemText.isEmpty()) {
                list.add(itemText);
            }
        }
        return list;
    }

    public String readProperty(String name, String defaultValue) {
        return properties.getProperty(name, System.getProperty(name, defaultValue));
    }
    
    public String readProperty(String name) {
        return properties.getProperty(name, System.getProperty(name));
    }
    
    public String readMandatoryProperty(String name) {
        String value = properties.getProperty(name, System.getProperty(name));
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Missing property: " + name);
        }
        return value;
    }


    public synchronized static GalenConfig getConfig() {
        return instance;
    }
    
    public void reset() throws IOException {
        loadConfig();
    }

    public int getRangeApproximation() {
        return this.rangeApproximation;
    }

    public List<String> getReportingListeners() {
        return this.reportingListeners;
    }

    public String getDefaultBrowser() {
        return defaultBrowser;
    }

    public Integer getIntProperty(String name, int defaultValue) {
        String value = readProperty(name);
        if (value == null) {
            return defaultValue;
        }
        else {
            try {
                return Integer.parseInt(value);
            }
            catch (Exception e) {
                throw new RuntimeException(String.format("Couldn't parse property \"%s\" from config file", name));
            }
        }
    }

    
    public int getIntProperty(String name, int defaultValue, int min, int max) {
        int value = getIntProperty(name, defaultValue);
        if (value >= min && value <=max) {
            return value;
        }
        else {
            throw new RuntimeException(String.format("Property \"%s\"=%d in config file is not in allowed range [%d, %d]", name, value, min, max));
        }
    }

    public boolean getBooleanProperty(String name, boolean defaultValue) {
        String value = readProperty(name);
        if (value == null) {
            return defaultValue;
        }
        else {
            return Boolean.parseBoolean(value);
        }
    }

    public int getLogLevel() {

        // TODO use constant
        String value = readProperty("galen.log.level", "10");
        if (StringUtils.isNumeric(value)) {
            return Integer.parseInt(value);
        }
        else return 10;
    }
    
    public boolean getUseFailExitCode() {

        // TODO use constant
        String value = readProperty("galen.use.fail.exit.code");
        if (value != null && !value.trim().isEmpty()) {
            return Boolean.parseBoolean(value);
        }
        else return false;
    }

    public String getTestJsSuffix() {
        return properties.getProperty(TEST_JS_SUFFIX, ".test.js");
    }

    public boolean shouldAutoresizeScreenshots() {
        return getBooleanProperty(GalenConfig.SCREENSHOT_AUTORESIZE, true);
    }

    public boolean shouldCheckVisibilityGlobally() {
        return getBooleanProperty(GalenConfig.SPEC_GLOBAL_VISIBILITY_CHECK, true);
    }

    public int getImageSpecDefaultTolerance() {
        return getIntProperty(GalenConfig.SPEC_IMAGE_TOLERANCE, 25);
    }

/*  public SpecImage.ErrorRate getImageSpecDefaultErrorRate() {
        String errorRateText = readProperty(SPEC_IMAGE_ERROR_RATE, "0px");
        return SpecImage.ErrorRate.fromString(errorRateText);
    }*/

    public void setProperty(String name, String value) {
        properties.setProperty(name, value);
    }

    public String getTestSuffix() {
        return properties.getProperty(TEST_SUFFIX, ".test");
    }
    
    public static void javaScriptHeader() {
    	WebElement findElement = driver.findElement(By.cssSelector("header"));
    	JavascriptExecutor js = (JavascriptExecutor) driver;
    	js.executeScript("arguments[0].setAttribute('hidden', 'true')", findElement);
    	
	}
}
