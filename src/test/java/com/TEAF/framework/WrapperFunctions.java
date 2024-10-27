package com.TEAF.framework;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WrapperFunctions {
	static By by;
	private static WebDriver driver = StepBase.getDriver();
	static Logger log = Logger.getLogger(WrapperFunctions.class.getName());

	public void waitForElementToBeClickable(By locator, int waitInSeconds) {
		try {
			Wait<WebDriver> wait = new WebDriverWait(driver, waitInSeconds)
					.ignoring(StaleElementReferenceException.class);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
		} catch (Exception e) {
			log.error(e);
		}
	}

	public static void highLightElement(WebElement element) throws Exception {
		try {
			if (System.getProperty("test.highlightElements").equals("true")) {
				JavascriptExecutor js = (JavascriptExecutor) driver;
				for (int i = 0; i < 2; i++) {
					js.executeScript(
							"arguments[0].setAttribute('style', 'background: green; border: 3px solid green;');",
							element);
					Thread.sleep(50);
					js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
					Thread.sleep(50);
					js.executeScript(
							"arguments[0].setAttribute('style', 'background: green; border: 3px solid green;');",
							element);
					Thread.sleep(50);
					js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
					Thread.sleep(50);
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
	}

	public static void waitForPageToLoad() throws Exception {
		ExpectedCondition<Boolean> expect = null;

		Wait<WebDriver> wait = new WebDriverWait(driver, Integer.parseInt(System.getProperty("test.pageLoadTimeout")))
				.ignoring(StaleElementReferenceException.class);

		// Condition to check page load complete
		if (System.getProperty("test.appType").equalsIgnoreCase("webapp")) {
			expect = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver dr) {
					return ((JavascriptExecutor) dr).executeScript("return document.readyState").equals("complete");
				}
			};
			wait.until(expect);
		}

	}

	public static boolean waitForElementPresence(By locator, int waitInSeconds) {
		try {
			Wait<WebDriver> wait = new WebDriverWait(driver, waitInSeconds)
					.ignoring(StaleElementReferenceException.class);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean waitForElementVisibility(By locator) {
		try {
			Wait<WebDriver> wait = new WebDriverWait(driver, 90).ignoring(StaleElementReferenceException.class);
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void waitForElementPresence(By locator) {
		int timeOut = Integer.parseInt(System.getProperty("test.implicitlyWait"));
		Wait<WebDriver> wait = new WebDriverWait(driver, timeOut).ignoring(StaleElementReferenceException.class);
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	public boolean checkElementExistence(By locator, int... sTimeInSecond) {
		try {
			WebDriverWait wait = null;
			if (sTimeInSecond.length > 0) {
				wait = new WebDriverWait(driver, sTimeInSecond[0]);
			} else {
				wait = new WebDriverWait(driver, 10);
			}
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			WebElement ele = driver.findElement(locator);
			return ele.isDisplayed();
		} catch (Exception e) {
			log.error(e);

			return false;
		}
	}

	public static boolean clickByJS(WebElement webElement) {
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", webElement);
			return true;
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}

	public boolean doubleClick(By locator) {
		waitForElementPresence(locator, 10);
		WebElement webElement = driver.findElement(locator);
		try {
			Actions actionBuilder = new Actions(driver);
			actionBuilder.doubleClick(webElement).build().perform();
			return true;
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}

	public boolean setText(By locator, String fieldValue) {
		waitForElementPresence(locator, 10);
		WebElement webElement = driver.findElement(locator);
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", webElement);
			webElement.sendKeys(Keys.chord(Keys.CONTROL, "a"));
			webElement.sendKeys(Keys.DELETE);
			webElement.clear();
			webElement.sendKeys(fieldValue);
			return true;
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}

	public String getText(By locator, String textBy) {
		waitForElementPresence(locator, 10);
		WebElement webElement = driver.findElement(locator);
		try {
			String strText = "";
			if (textBy.equals("value"))
				strText = webElement.getAttribute("value");
			else if (textBy.equalsIgnoreCase("text"))
				strText = webElement.getText();
			return strText;
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

	public boolean selectCheckBox(By locator, boolean status) {
		waitForElementPresence(locator, 10);
		WebElement webElement = driver.findElement(locator);
		try {
			if (webElement.getAttribute("type").equals("checkbox")) {
				if ((webElement.isSelected() && !status) || (!webElement.isSelected() && status))
					webElement.click();
				return true;
			} else
				return false;
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}

	public boolean isCheckBoxSelected(By locator, boolean status) {
		waitForElementPresence(locator, 10);
		WebElement webElement = driver.findElement(locator);
		boolean state = false;
		try {
			if (webElement.getAttribute("type").equals("checkbox"))
				state = webElement.isSelected();

			return state;
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}

	public boolean selectRadioButton(By locator, boolean status) {
		waitForElementPresence(locator, 10);
		WebElement webElement = driver.findElement(locator);
		try {
			if (webElement.getAttribute("type").equals("radio")) {
				if ((webElement.isSelected() && !status) || (!webElement.isSelected() && status))
					webElement.click();
				return true;
			} else
				return false;
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}

	public boolean mouseHover(By locator) {
		WebElement webElement = driver.findElement(locator);
		try {
			Actions actionBuilder = new Actions(driver);
			actionBuilder.moveToElement(webElement).build().perform();
			return true;
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}

	public boolean clickByAction(By locator) {
		WebElement webElement = driver.findElement(locator);
		try {
			Actions actionBuilder = new Actions(driver);
			actionBuilder.moveToElement(webElement).click().build().perform();

			return true;
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}

	public static boolean scroll(String scrollType, String target) {
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;

			if (scrollType.equalsIgnoreCase("element")) {
				waitForElementPresence(GetPageObjectRead.OR_GetElement(target));
				executor.executeScript("arguments[0].scrollIntoView();",
						driver.findElement(GetPageObjectRead.OR_GetElement(target)));
			} else if (scrollType.equalsIgnoreCase("coordinates")) {
				String[] coordinates = target.split(",");
				String x = coordinates[0];
				String y = coordinates[1];
				executor.executeScript("window.scrollBy(" + x + "," + y + ")");
			}
			return true;
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}

	public static boolean switchToWindowUsingTitle(String windowTitle) {
		try {
			String mainWindowHandle = driver.getWindowHandle();
			Set<String> openWindows = driver.getWindowHandles();

			if (!openWindows.isEmpty()) {
				for (String windows : openWindows) {
					String window = driver.switchTo().window(windows).getTitle();
					if (windowTitle.equals(window))
						return true;
					else
						driver.switchTo().window(mainWindowHandle);
				}
			}
			return false;
		} catch (Exception e) {
			log.error(e);
			return false;
		}
	}

	public static boolean selectDropDownOption(By locator, String option, String... selectType) {
		try {
			waitForElementPresence(locator, 10);
			WebElement webElement = driver.findElement(locator);
			Select sltDropDown = new Select(webElement);

			if (selectType.length > 0 && !selectType[0].equals("")) {
				if (selectType[0].equalsIgnoreCase("Value"))
					sltDropDown.selectByValue(option);
				else if (selectType[0].equalsIgnoreCase("Text"))
					sltDropDown.selectByVisibleText(option);
				else if (selectType[0].equalsIgnoreCase("Index"))
					sltDropDown.selectByIndex(Integer.parseInt(option));

				return true;
			} else {
				// Web elements from dropdown list
				List<WebElement> options = sltDropDown.getOptions();
				boolean blnOptionAvailable = false;
				int iIndex = 0;
				for (WebElement weOptions : options) {
					if (weOptions.getText().trim().equals(option)) {
						sltDropDown.selectByIndex(iIndex);
						blnOptionAvailable = true;
					} else
						iIndex++;
					if (blnOptionAvailable)
						break;
				}
				if (blnOptionAvailable)
					return true;
				else
					return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			return false;
		}
	}

	public String getSelectedValueFormDropDown(By locator) {
		try {
			waitForElementPresence(locator, 10);
			Select selectDorpDown = new Select(driver.findElement(locator));
			String selectedDorpDownValue = selectDorpDown.getFirstSelectedOption().getText();
			return selectedDorpDownValue;
		} catch (Exception e) {
			log.error(e);
			return null;
		}

	}

	public static By setLocator(String locatorType, String locator) {

		if (locatorType.equalsIgnoreCase("id")) {
			by = By.id(locator);
		} else if (locatorType.equalsIgnoreCase("classname")) {
			by = By.className(locator);
		} else if (locatorType.equalsIgnoreCase("name")) {
			by = By.name(locator);
		} else if (locatorType.equalsIgnoreCase("linktext")) {
			by = By.linkText(locator);
		} else if (locatorType.equalsIgnoreCase("partiallinktext")) {
			by = By.partialLinkText(locator);
		} else if (locatorType.equalsIgnoreCase("cssselector")) {
			by = By.cssSelector(locator);
		} else if (locatorType.equalsIgnoreCase("xpath")) {
			by = By.xpath(locator);
		} else if (locatorType.equalsIgnoreCase("tagname")) {
			by = By.tagName(locator);
		}
		return by;
	}

	public static WebElement getElementByLocator(final String elementName, final By locator, final int waitTime)
			throws Exception {
		final long startTime = System.currentTimeMillis();
		boolean found = false;
		int retryCount = 1;
		WebElement wElement = null;
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(waitTime))
				.pollingEvery(Duration.ofSeconds(3)).ignoring(NoSuchElementException.class);
		while ((System.currentTimeMillis() - startTime) < 30000) {
			try {
				wElement = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
				found = true;
				break;
			} catch (Exception e) {

				while (retryCount <= 3 && found == false) {
					log.error("Retry - " + retryCount);
					try {
						wElement = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
						found = true;
						break;
					} catch (TimeoutException timeoutException) {

						log.error(
								"Retry for No Such Element - " + retryCount + " Failed " + timeoutException.toString());
					}
					retryCount++;
				}
			}
		}
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		boolean elementFound = false;

		if (found) {
			log.info("Successfully found the element: " + elementName + " - waited for: " + totalTime + " ms");
			try {
				wElement = wait.until(ExpectedConditions
						.visibilityOf(driver.findElement(GetPageObjectRead.OR_GetElement(elementName))));
				elementFound = true;
			} catch (TimeoutException e) {
				while (retryCount <= 3 && elementFound == false) {
					log.error("Retry - " + retryCount);
					try {
						wElement = wait.until(ExpectedConditions
								.visibilityOf(driver.findElement(GetPageObjectRead.OR_GetElement(elementName))));
						elementFound = true;
						break;
					} catch (TimeoutException timeoutException) {
						log.error("Retry for Element Visibility - " + retryCount + " Failed "
								+ timeoutException.toString());
					}
					retryCount++;
				}
			} catch (StaleElementReferenceException e) {
				boolean result = false;
				int attempts = 0;
				while (attempts < 3 && elementFound == false) {
					try {
						new WebDriverWait(driver, 10).ignoring(StaleElementReferenceException.class)
								.until(ExpectedConditions.visibilityOf(
										driver.findElement(GetPageObjectRead.OR_GetElement(elementName))));
						result = true;
						break;
					} catch (StaleElementReferenceException e1) {
						log.error("Attempt Count " + attempts + " for StaleElement exception");
					}
					attempts++;
				}
				if (result) {
					log.info("Handled Stale Element Exception");
				}
			}
			if (elementFound) {
				log.info("Element Visible on the Page: " + elementName + " - waited for: " + totalTime + " ms");
			} else {
				log.error(
						"ELEMENT NOT FOUND ON THE PAGE: " + elementName + " - after waiting for: " + totalTime + " ms");
				throw new Exception("Element Not found on the Page: " + elementName);
			}

		}

		else {
			log.error("COULD NOT FIND THE ELEMENT: " + elementName + " - after waiting for: " + totalTime + " ms");
			throw new Exception("No Such Element Found on the Page: " + elementName);
		}
		return wElement;
	}

}
