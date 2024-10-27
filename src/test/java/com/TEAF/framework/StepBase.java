package com.TEAF.framework;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.javascript.host.file.File;

import cucumber.api.Scenario;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

public class StepBase {
	static WebDriver driver;
	static WebDriverWait webDriverWait;
	public static Scenario crScenario;
	static DesiredCapabilities capabilities = null;
	public static String testPlatform;
	public static String testBrowser;
	static String huburl;
	static Logger log = Logger.getLogger(StepBase.class.getName());

	public static void setScenario(Scenario cScenario) throws Exception {
		crScenario = cScenario;
	}

	public static void setUp(String Platform, String Browser) throws Exception {
		try {
			testPlatform = Platform;
			testBrowser = Browser;

			if (Platform.equalsIgnoreCase("desktop")
					&& !System.getProperty("test.appType").equalsIgnoreCase("windowsapp")) {
				if (Browser.toLowerCase().equals("chrome")) {

					System.setProperty("webdriver.chrome.driver",
							System.getProperty("user.dir") + "/src/test/java/com/Resources/chromedriver.exe");
					ChromeOptions Options = new ChromeOptions();
					Options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

					driver = new ChromeDriver(Options);
					driver.manage().window().maximize();
				} else if (Browser.toLowerCase().equals("node-chrome")) {
					huburl = System.getProperty("test.hubUrl");
					DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
					ChromeOptions options = new ChromeOptions();
					options.setCapability("platform", "LINUX");
					desiredCapabilities.setCapability("idleTimeout", 500);
					desiredCapabilities.setCapability("name", System.getProperty("test.projectName"));
					desiredCapabilities.setCapability("build", System.getProperty("test.projectName"));
					desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
					driver = new RemoteWebDriver(new URL(System.getProperty("test.hubUrl")), desiredCapabilities);
					driver.manage().window().maximize();
					driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					log.info("Driver=" + driver);
				} else if (Browser.toLowerCase().equals("grid-chrome")) {
					huburl = System.getProperty("test.hubUrl");

					Map<String, String> mobileEmulation = new HashMap<String, String>();
					mobileEmulation.put("deviceName", System.getProperty("test.mobilechromeEmulatorDevice"));

					DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
					ChromeOptions options = new ChromeOptions();
					desiredCapabilities.setPlatform(org.openqa.selenium.Platform.WINDOWS);
					desiredCapabilities.setBrowserName("chrome");
					options.setExperimentalOption("mobileEmulation", mobileEmulation);

					desiredCapabilities.setCapability("idleTimeout", 500);
					desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
					driver = new RemoteWebDriver(new URL("http://10.201.20.7:1487/wd/hub"), options);
					driver.manage().window().maximize();
					driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					log.info("Driver=" + driver);
				} else if (Browser.toLowerCase().equals("chrome-headless-linux")) {
					log.info("Running tests on Chrome Browser Headlessly on Linux");
					String chromeBinaryPath = "/usr/bin/google-chrome";
					String driverPath = System.getProperty("user.dir") + "/src/test/java/com/Resources/chromedriver";
					System.setProperty("webdriver.chrome.driver", driverPath);
					System.setProperty("test.disableScreenshotCapture", "true");
					ChromeOptions optionsLinux64 = new ChromeOptions();
					optionsLinux64.setAcceptInsecureCerts(true);
					optionsLinux64.setBinary(chromeBinaryPath);
					// optionsLinux64.addArguments("port=8087");
					optionsLinux64.addArguments("--headless");
					optionsLinux64.addArguments("--no-sandbox");
					optionsLinux64.addArguments("window-size=1980,1080");
					optionsLinux64.addArguments("--disable-gpu");
					optionsLinux64.addArguments("--headless");

					driver = new ChromeDriver(optionsLinux64);
				}

				else if (Browser.toLowerCase().equals("rd-chrome-headless-linux")) {
					log.info("Running tests on Remote Driver Chrome Browser Headlessly");
					URL serverurl = new URL("http://localhost:9515");
					log.info("Running tests on Chrome Browser Headlessly");
					String chromeBinaryPath = "/usr/bin/google-chrome";
					String driverPath = System.getProperty("user.dir") + "/src/test/java/com/Resources/chromedriver";
					System.setProperty("webdriver.chrome.driver", driverPath);
					System.setProperty("webdriver.chrome.verboseLogging", "true");
					System.setProperty("webdriver.chrome.logfile", "/data/Package/log/chromedriver.log");
					ChromeOptions optionsLinux64 = new ChromeOptions();
					optionsLinux64.setBinary(chromeBinaryPath);
					optionsLinux64.addArguments("port=8087");
					optionsLinux64.addArguments("--headless");
					optionsLinux64.addArguments("--no-sandbox");
					optionsLinux64.addArguments("--disable-gpu");
					optionsLinux64.addArguments("--headless");
					driver = new RemoteWebDriver(serverurl, capabilities);
				} else if (Browser.toLowerCase().equals("firefox")) {
					System.setProperty("webdriver.gecko.driver",
							System.getProperty("user.dir") + "/src/test/java/com/Resources/geckodriver.exe");
					log.info("Executing test on Firefox browser");
					driver = new FirefoxDriver();
					driver.manage().window().maximize();
				} else if (Browser.toLowerCase().equals("ie")) {
					System.setProperty("webdriver.ie.driver",
							System.getProperty("user.dir") + "/src/test/java/com/Resources/IEDriverServer.exe");
					log.info("Executing test on Internet Explorer browser");
					driver = new InternetExplorerDriver();
					driver.manage().window().maximize();
				} else if (Browser.toLowerCase().equals("htmlunit")) {
					log.info("Executing test Headlessly on HtmlUnit");
					driver = new HtmlUnitDriver(false);
				} else if (Browser.toLowerCase().equals("edge")) {
					System.setProperty("webdriver.edge.driver",
							System.getProperty("user.dir") + "/src/test/java/com/Resources/msedgedriver.exe");
					log.info("Executing test on Internet Explorer browser");
					driver = new EdgeDriver();
					driver.manage().window().maximize();
				} else if (Browser.toLowerCase().equals("safari")) {
					SafariOptions Options = new SafariOptions();
					Options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					log.info("Executing test on Safari browser");
					driver = new SafariDriver(Options);
					driver.manage().window().maximize();
				} else if (Browser.toLowerCase().equals("browserstack-desktop-browser-local")) {
					DesiredCapabilities caps = new DesiredCapabilities();
					caps.setCapability("browser", System.getProperty("test.bstack.browser"));
					caps.setCapability("browserstack.local", System.getProperty("test.bstack.local"));
					caps.setCapability("browserstack.localIdentifier",
							System.getProperty("test.bstack.localIdentifier"));
					caps.setCapability("browserstack.debug", true);
					caps.setCapability("autoGrantPermissions", "true");
					caps.setCapability("autoAcceptAlerts", "true");
					caps.setCapability("browser_version", System.getProperty("test.bstack.browserVersion"));
					caps.setCapability("os", System.getProperty("test.bstack.os"));
					caps.setCapability("os_version", System.getProperty("test.bstack.osVersion"));
					caps.setCapability("resolution", System.getProperty("test.bstack.resolution"));
					caps.setCapability("project", System.getProperty("test.bstack.projectName"));
					caps.setCapability("build", System.getProperty("test.bstack.buildName"));
					caps.setCapability("browserstack.console", "errors");
					caps.setCapability("browserstack.networkLogs", "true");
					caps.setCapability("browserstack.selenium_version", "3.141.59");
					caps.setCapability("browserstack.idleTimeout", "15");

					String USERNAME = System.getProperty("test.bstack.userName");
					String AUTOMATE_KEY = System.getProperty("test.bstack.automateKey");
					String bstackURL = "https://" + USERNAME + ":" + AUTOMATE_KEY
							+ "@hub-cloud.browserstack.com/wd/hub";
					driver = new RemoteWebDriver(new URL(bstackURL), caps);
					driver.manage().window().maximize();

				} else if (Browser.toLowerCase().equals("browserstack-desktop-browser-local-test")) {
					DesiredCapabilities caps = new DesiredCapabilities();
					caps.setCapability("browser", System.getProperty("test.bstack.browser"));
					caps.setCapability("browserstack.local", System.getProperty("test.bstack.local"));
					caps.setCapability("browserstack.localIdentifier",
							System.getProperty("test.bstack.localIdentifier"));
					caps.setCapability("browserstack.debug", true);
					caps.setCapability("browser_version", System.getProperty("test.bstack.browserVersion"));
					caps.setCapability("os", System.getProperty("test.bstack.os"));
					caps.setCapability("os_version", System.getProperty("test.bstack.osVersion"));
					caps.setCapability("resolution", System.getProperty("test.bstack.resolution"));
					String USERNAME = System.getProperty("test.bstack.userName");
					String AUTOMATE_KEY = System.getProperty("test.bstack.automateKey");
					String bstackURL = "https://" + USERNAME + ":" + AUTOMATE_KEY
							+ "@hub-cloud.browserstack.com/wd/hub";
					driver = new RemoteWebDriver(new URL(bstackURL), caps);
					driver.manage().window().maximize();

				}

				else if (Browser.toLowerCase().equals("browserstack-desktop-browser")) {
					DesiredCapabilities caps = new DesiredCapabilities();
					caps.setCapability("browser", System.getProperty("test.bstack.browser"));
					caps.setCapability("browserstack.debug", true);
					caps.setCapability("browser_version", System.getProperty("test.bstack.browserVersion"));
					caps.setCapability("os", System.getProperty("test.bstack.os"));
					caps.setCapability("os_version", System.getProperty("test.bstack.osVersion"));
					caps.setCapability("resolution", System.getProperty("test.bstack.resolution"));
					caps.setCapability("autoGrantPermissions", "true");
					caps.setCapability("autoAcceptAlerts", "true");
					caps.setCapability("project", System.getProperty("test.bstack.projectName"));
					caps.setCapability("build", System.getProperty("test.bstack.buildName"));
					caps.setCapability("browserstack.console", "errors");
					caps.setCapability("browserstack.networkLogs", "true");
					caps.setCapability("browserstack.selenium_version", "3.141.59");
					caps.setCapability("browserstack.idleTimeout", "15");
					String USERNAME = System.getProperty("test.bstack.userName");
					String AUTOMATE_KEY = System.getProperty("test.bstack.automateKey");
					String bstackURL = "https://" + USERNAME + ":" + AUTOMATE_KEY
							+ "@hub-cloud.browserstack.com/wd/hub";
					log.info(bstackURL);
					driver = new RemoteWebDriver(new URL(bstackURL), caps);
					driver.manage().window().maximize();

				}
				driver.manage().deleteAllCookies();
				driver.manage().timeouts().implicitlyWait(Integer.parseInt(System.getProperty("test.implicitlyWait")),
						TimeUnit.SECONDS);
				driver.manage().timeouts().pageLoadTimeout(Integer.parseInt(System.getProperty("test.pageLoadTimeout")),
						TimeUnit.SECONDS);

			} else if (Platform.equalsIgnoreCase("mobile")) {
				if (Browser.toLowerCase().equals("appium-nativeapp")) {
					// Set up desired capabilities and pass the Android app-activity and app-package
					// to Appium
					DesiredCapabilities capabilities = new DesiredCapabilities();
					capabilities.setCapability("browserName", "Android");
					capabilities.setCapability("VERSION", "9.0");
					capabilities.setCapability("deviceName", "OnePlus5");
					capabilities.setCapability("platformName", "Android");
					capabilities.setCapability("appPackage", "in.amazon.mShop.android.shopping");
					capabilities.setCapability("appActivity", "com.amazon.mShop.home.HomeActivity");
					driver = new RemoteWebDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
				} else if (Browser.toLowerCase().equals("mobile-chromeemulator")) {
					System.setProperty("webdriver.chrome.driver",
							System.getProperty("user.dir") + "/src/test/java/com/Resources/chromedriver.exe");
					Map<String, String> mobileEmulation = new HashMap<String, String>();
					mobileEmulation.put("deviceName", System.getProperty("test.mobilechromeEmulatorDevice"));
					ChromeOptions chromeOptions = new ChromeOptions();
					chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
//					chromeOptions.addExtensions(new java.io.File(
//							System.getProperty("user.dir") + "\\src\\test\\java\\com\\Resources\\modheader.crx"));
//					
					driver = new ChromeDriver(chromeOptions);
					driver.manage().window().maximize();

				} else if (Browser.toLowerCase().equals("bstack-mob-webapp-local")) {
					DesiredCapabilities caps = new DesiredCapabilities();
					caps.setCapability("device", System.getProperty("test.bstack.device"));
					caps.setCapability("os_version", System.getProperty("test.bstack.mobOsVersion"));
					caps.setCapability("name", System.getProperty("test.bstack.testName"));
					caps.setCapability("browserName", System.getProperty("test.bstack.browserName"));
					caps.setCapability("realMobile", "true");
					caps.setCapability("browserstack.local", System.getProperty("test.bstack.local"));
					caps.setCapability("browserstack.localIdentifier",
							System.getProperty("test.bstack.localIdentifier"));
					caps.setCapability("Connect Hardware Keyboard", true);
					caps.setCapability(IOSMobileCapabilityType.CONNECT_HARDWARE_KEYBOARD, true);

					caps.setCapability("autoGrantPermissions", "true");
					caps.setCapability("autoAcceptAlerts", "true");
					caps.setCapability("browserstack.safari.enablePopups", "true");
					caps.setCapability("project", System.getProperty("test.bstack.projectName"));
					caps.setCapability("build", System.getProperty("test.bstack.buildName"));
					caps.setCapability("browserstack.console", "errors");
					caps.setCapability("browserstack.networkLogs", "true");
					caps.setCapability("browserstack.selenium_version", "3.141.59");
					caps.setCapability("browserstack.idleTimeout", "200");

					caps.setCapability("browserstack.debug", true);
					String USERNAME = System.getProperty("test.bstack.userName");
					String AUTOMATE_KEY = System.getProperty("test.bstack.automateKey");
					String bstackURL = "https://" + USERNAME + ":" + AUTOMATE_KEY
							+ "@hub-cloud.browserstack.com/wd/hub";

					driver = new RemoteWebDriver(new URL(bstackURL), caps);
				} else if (Browser.toLowerCase().equals("bstack-droid-webapp")) {
					DesiredCapabilities caps = new DesiredCapabilities();
					caps.setCapability("device", System.getProperty("test.bstack.device"));
					caps.setCapability("os_version", System.getProperty("test.bstack.mobOsVersion"));
					caps.setCapability("name", System.getProperty("test.bstack.testName"));
					caps.setCapability("browserName", System.getProperty("test.bstack.browserName"));
					caps.setCapability("realMobile", "true");
					caps.setCapability("browserstack.debug", true);
					caps.setCapability("autoGrantPermissions", "true");
					caps.setCapability("autoAcceptAlerts", "true");
					String USERNAME = System.getProperty("test.bstack.userName");
					String AUTOMATE_KEY = System.getProperty("test.bstack.automateKey");
					String bstackURL = "https://" + USERNAME + ":" + AUTOMATE_KEY
							+ "@hub-cloud.browserstack.com/wd/hub";

					driver = new RemoteWebDriver(new URL(bstackURL), caps);
				} else if (Browser.toLowerCase().equals("real-droid-nativeapp")) {
					DesiredCapabilities caps = new DesiredCapabilities();
					caps.setCapability(MobileCapabilityType.DEVICE_NAME, System.getProperty("test.real.deviceName"));
					caps.setCapability(MobileCapabilityType.PLATFORM_NAME, org.openqa.selenium.Platform.ANDROID);
					caps.setCapability(MobileCapabilityType.PLATFORM_VERSION,
							System.getProperty("test.real.platformVersion"));
					caps.setCapability("noReset", true);
					caps.setCapability("adbExecTimeout", 70000);
					caps.setCapability("�session-override", true);
					caps.setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
							System.getProperty("test.real.app.package"));
					caps.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
							System.getProperty("test.real.app.activity"));
					String appiumUrl = System.getProperty("test.appium.url");
					driver = new AppiumDriver<MobileElement>(new URL(appiumUrl), caps);
				} else if (Browser.toLowerCase().equals("real-ios-nativeapp")) {
					capabilities = new DesiredCapabilities();
					capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, org.openqa.selenium.Platform.IOS);
					capabilities.setCapability(MobileCapabilityType.VERSION,
							System.getProperty("test.real.platformVersion"));
					capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,
							System.getProperty("test.real.deviceName"));
					capabilities.setCapability(MobileCapabilityType.UDID, System.getProperty("test.udid"));
					// capabilities.setCapability("autoAcceptAlerts", true);
					// capabilities.setCapability("autoWebview", true);
					capabilities.setCapability("autoDismissAlerts", true);
					capabilities.setCapability("showIOSLog", true);
					capabilities.setCapability("xcodeOrgId", System.getProperty("test.xcodeOrgId"));
					capabilities.setCapability("xcodeSigningId", System.getProperty("test.xcodeSigningId"));
					capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
					capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "480");
					// capabilities.setCapability(MobileCapabilityType.LAUNCH_TIMEOUT, "480000");
					capabilities.setCapability("bundleId", System.getProperty("test.appBundleID"));
					String appiumUrl = System.getProperty("test.appium.url");

					driver = new RemoteWebDriver(new URL(appiumUrl), capabilities);
					Thread.sleep(10000);
				}

				else if (Browser.toLowerCase().equals("real-droid-chrome")) {
					DesiredCapabilities caps = new DesiredCapabilities();
					caps.setCapability(MobileCapabilityType.DEVICE_NAME, System.getProperty("test.real.deviceName"));
					caps.setCapability(MobileCapabilityType.PLATFORM_NAME, org.openqa.selenium.Platform.ANDROID);
					caps.setCapability(MobileCapabilityType.PLATFORM_VERSION,
							System.getProperty("test.real.platformVersion"));
					caps.setCapability("noReset", true);
					caps.setCapability(MobileCapabilityType.BROWSER_NAME, "CHROME");
					caps.setBrowserName(BrowserType.CHROME);
					String appiumUrl = System.getProperty("test.appium.url");
					driver = new AppiumDriver<MobileElement>(new URL(appiumUrl), caps);
				}

				driver.manage().timeouts().implicitlyWait(Integer.parseInt(System.getProperty("test.implicitlyWait")),
						TimeUnit.SECONDS);
				if (System.getProperty("test.appType").equalsIgnoreCase("webapp")) {
					driver.manage().timeouts().pageLoadTimeout(
							Integer.parseInt(System.getProperty("test.pageLoadTimeout")), TimeUnit.SECONDS);

				}
			}

			else {
				log.info("Enter Valid - Platform|Browser Values");
			}
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	public static WebDriver getDriver() {
		return driver;
	}

	public static void tearDown() {
		try {

			if (driver != null) {
				if (System.getProperty("test.appType").equalsIgnoreCase("webapp")) {
					driver.manage().deleteAllCookies();
				}
				driver.quit();
				driver = null;
				Thread.sleep(3000);
			}
		} catch (Exception e) {
			log.error(e);
		}
	}

	public static void embedScreenshot() {
		try {

			if (System.getProperty("test.disableScreenshotCapture").equalsIgnoreCase("false")
					&& !System.getProperty("test.appType").equalsIgnoreCase("windowsapp")) {
				Thread.sleep(1000);
				final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
				crScenario.embed(screenshot, "image/png");
			} else {
				log.info("Test Property - test.ScreenShotCapture is disabled!");
			}
		} catch (Exception e) {
			log.error(e);
		}
	}
}
