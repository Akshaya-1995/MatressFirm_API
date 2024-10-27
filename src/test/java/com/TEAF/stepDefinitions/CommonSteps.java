package com.TEAF.stepDefinitions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.tools.ant.taskdefs.compilers.Sj;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.TEAF.Hooks.Hooks;
import com.TEAF.framework.GetPageObjectRead;
import com.TEAF.framework.HashMapContainer;
import com.TEAF.framework.StepBase;
import com.TEAF.framework.WrapperFunctions;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;

public class CommonSteps {

	static Logger log = Logger.getLogger(CommonSteps.class.getName());

	private static WebDriver driver;
	private static int elementWaitTime = Integer.parseInt(System.getProperty("test.implicitlyWait"));
	public static String appName = null;

	@Given("^My WebApp '(.*)' is open$")
	public static void my_webapp_is_open(String url) throws Exception {
		try {

			appName = url;
			driver = StepBase.getDriver();
			log.info("Driver value: " + driver);
			driver.get(GetPageObjectRead.OR_GetURL(url));
			WrapperFunctions.waitForPageToLoad();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	@When("^I scroll till the element$")
	 public static void i_scroll_down() {
		 JavascriptExecutor js = (JavascriptExecutor) driver;
		 HashMap<String, Object> params = new HashMap<String, Object>();
		 params.put("direction", "down");
		 params.put("element", driver.findElement(By.xpath("//h2[@class='position-absolute']")));
		 js.executeScript("mobile: swipe", params);
	}
	
	@Then("^I scroll to element '(.*)' - '(.*)'$")
	public static void I_scroll_element(String scrolltype, String element) throws Exception {

		try {
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("window.scrollBy(scrolltype,element)");
//			("window.scrollBy(scrolltype,element)");
//			WrapperFunctions.scroll(scrolltype, element);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I should see sub menu Header '(.*)' text present on PLP page at '(.*)'$")
	public static void I_should_see_sub_menu_header_text_present_on_page_At(String expectedText, String PLPPage) throws Exception
	{
		CommonSteps.I_should_see_text_present_on_page_At(expectedText, PLPPage);
		
	}
	
	@Then("^I switch to Opened Tab$")
	public static void i_switch_openedTab()
	 {
	ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
	int tabsOpen = tabs.size();
	driver.switchTo().window(tabs.get(0));
	}
	@Then("^I switch to new Window to verify social link URL '(.*)'$")
	public static void I_switch_To_Window(String linkUrl) throws Exception {

//		try {
			String parent=driver.getWindowHandle();
			Set<String>s=driver.getWindowHandles();
			Iterator<String> I1= s.iterator();
			while(I1.hasNext())
			{
			String child_window=I1.next();
			if(!parent.equals(child_window))
			{
			driver.switchTo().window(child_window);
//			CommonSteps.I_pause_for_seconds(5);
			System.out.println(driver.switchTo().window(child_window).getTitle());
			System.out.println(driver.switchTo().window(child_window).getCurrentUrl());
			Assert.assertEquals(driver.switchTo().window(child_window).getCurrentUrl(), linkUrl);
    		
		driver.close();
			}
			}
			driver.switchTo().window(parent);
		
					
	//			driver.switchTo().window(driver.getWindowHandle());
//			StepBase.embedScreenshot();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new Exception(e);
//		}

	}

	@Then("^I mouse over to '(.*)' and click Footer menu Head '(.*)'$")
	public static void I_mouse_over_and_click_footer_menu_head(String element,String element1) throws Exception {

		try {
			Actions action = new Actions(driver);
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
			GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			action.moveToElement(wElement).build().perform();
			WebElement wElement1 = WrapperFunctions.getElementByLocator(element1,
			GetPageObjectRead.OR_GetElement(element1), elementWaitTime);
			wElement1.click();
			CommonSteps.I_pause_for_seconds(3);
			CommonSteps.I_should_see_on_page("ResultHead");

		
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	
	
	}
	@Then("^I validate page navigation for below listed product categories available under '(.*)' and sub menu as '(.*)'$")
    public static void validate_pagenavigation(String element1,String submenu,DataTable table) throws Exception
    {
		 List<String> data = table.asList();
	     System.out.println(data.get(0));
	        int count =1;
    	    CommonSteps.I_pause_for_seconds(3);
	       
	        for (int i = 0; i <=data.size()-1; i++)
	        {
	    	    CommonSteps.I_pause_for_seconds(3);
	        	Actions action = new Actions(driver);
				WebElement wElement = WrapperFunctions.getElementByLocator(element1,
				GetPageObjectRead.OR_GetElement(element1), elementWaitTime);
				action.moveToElement(wElement).build().perform();
	    	    CommonSteps.I_pause_for_seconds(3);
				List<WebElement> element = driver.findElements(By.xpath("(((//li//a[contains(text(),'"+submenu+"')])[1]//following::ul[@class='level-3'])[1]//a)"));
    		WebElement ele =driver.findElement(By.xpath("(((//li//a[contains(text(),'"+submenu+"')])[1]//following::ul[@class='level-3'])[1]//a)["+count+"]"));
    		Assert.assertEquals(data.get(i), element.get(i).getText());
    		System.out.println("Data set value: "+data.get(i)+" "+"Submenu value: "+element.get(i).getText());
    		ele.click();
    		CommonSteps.I_pause_for_seconds(5);
    		CommonSteps.I_should_see_text_present_on_page(submenu);
//    		CommonSteps.I_should_see_text_contained_on_page_At("Home"+""+""+element1+""+""+submenu+""+""+element+")", "BreadCrumpValidation");
   	     	CommonSteps.I_should_see_on_page("FirstProduct");
    	    CommonSteps.I_pause_for_seconds(5);
    	    count = count+1;
    	   
		}
        
    
    } 
	@Then("^I validate page navigation for below listed products categories available under '(.*)' from Footer$")
    public static void footer_menu_category_validation(String footermenu,DataTable table) throws Exception
    {
    List<String> data = table.asList();
    System.out.println(data.get(0));
    String url= "https://www.brambleberry.com/"; 
    int count =1;
    for (int i = 0; i <=data.size()-1; i++)
    {
    List<WebElement> element = driver.findElements(By.xpath("((//div[contains(text(),'"+footermenu+"')]//following::ul[contains(@class,'menu-footer')])[1]//li//a)"));
    WebElement el1 = driver.findElement(By.xpath("((//div[contains(text(),'"+footermenu+"')]//following::ul[contains(@class,'menu-footer')])[1]/li/a)["+count+"]"));
    System.out.println("Data set value: "+data.get(i)+" "+"Submenu value: "+element.get(i).getText());
    Assert.assertEquals(data.get(i), element.get(i).getText());
    el1.click();
//    	CommonSteps.I_should_see_on_page("FirstProduct");
    	CommonSteps.i_navigate_to_url(url);
    	CommonSteps.I_scroll_thru_page();
    CommonSteps.I_pause_for_seconds(3);
    count = count+1;
  
    }
    }
	 
	@Then("^I validate page navigation for below listed product categories available under '(.*)' from Footer$")
    public static void footer_menu_cat_products_validations(String footermenu,DataTable table) throws Exception
    {
         List<String> data = table.asList();
     System.out.println(data.get(0));
        int count =1;
     for (int i = 0; i <=data.size()-1; i++)
     {
     List<WebElement> element = driver.findElements(By.xpath("((//div[contains(text(),'"+footermenu+"')]//following::ul[contains(@class,'menu-footer')])[1]//li//a)"));
     WebElement el1 = driver.findElement(By.xpath("((//div[contains(text(),'"+footermenu+"')]//following::ul[contains(@class,'menu-footer')])[1]/li/a)["+count+"]"));
     System.out.println("Data set value: "+data.get(i)+" "+"Submenu value: "+element.get(i).getText());
     Assert.assertEquals(data.get(i), element.get(i).getText());
     el1.click();
//     	CommonSteps.I_should_see_on_page("NavigationResult");
     	CommonSteps.I_scroll_thru_page();
     CommonSteps.I_pause_for_seconds(3);
     count = count+1;
   
     }
     }

	 @Then("^I should see footer '(.*)' with following social links$")
     public static void footermenu_social_link_validation(String Socaillink,DataTable table) throws Exception
     {
     List<String> data = table.asList();
     System.out.println(data.get(0));
        int count =1;
     for (int i = 0; i <=data.size()-1; i++)
     {
     List<WebElement> element = driver.findElements(By.xpath("//ul[@class='footer-sociallinks']//span[contains(text,'"+Socaillink+"')]"));
     WebElement social = driver.findElement(By.xpath("(//ul[@class='footer-sociallinks']//span[contains(text,'"+Socaillink+"')])["+count+"]"));
     System.out.println("Data set value: "+data.get(i)+" "+"Socaillink name: "+element.get(i).getText());
     Assert.assertEquals(data.get(i), element.get(i).getText());
     CommonSteps.I_pause_for_seconds(2);
//     social.click();
//     CommonSteps.I_pause_for_seconds(3); 
//     count = count+1;
     
     }
     }

	 
	 
	  @Then("^I should see footer menu shop '(.*)' with following products$")
     public static void footer_menu_shop_category_validation(String footermenu,DataTable table) throws Exception
     {
     List<String> data = table.asList();
     System.out.println(data.get(0));
        int count =1;
     for (int i = 0; i <=data.size()-1; i++)
     {
     List<WebElement> element = driver.findElements(By.xpath("((//div[contains(text(),'"+footermenu+"')]//following::ul[contains(@class,'menu-footer')])[1]//li//a)"));
     WebElement el1 = driver.findElement(By.xpath("((//div[contains(text(),'"+footermenu+"')]//following::ul[contains(@class,'menu-footer')])[1]/li/a)["+count+"]"));
     System.out.println("Data set value: "+data.get(i)+" "+"Submenu value: "+element.get(i).getText());
     Assert.assertEquals(data.get(i), element.get(i).getText());
     el1.click();
     CommonSteps.I_pause_for_seconds(2);
     	CommonSteps.I_should_see_on_page("FirstProduct");
     	CommonSteps.I_scroll_thru_page();
     CommonSteps.I_pause_for_seconds(3);
     count = count+1;
   
     }
     }

	 @Then("^I should see footer social '(.*)' with following products$")
     public static void footer_social_link_category_validation(String footermenu,DataTable table) throws Exception
     {
     List<String> data = table.asList();
     System.out.println(data.get(0));
        int count =1;
     for (int i = 0; i <=data.size()-1; i++)
     {
     List<WebElement> element = driver.findElements(By.xpath("((//div[contains(text(),'"+footermenu+"')]//following::ul[contains(@class,'menu-footer')])[1]//li//a)"));
     WebElement el1 = driver.findElement(By.xpath("((//div[contains(text(),'"+footermenu+"')]//following::ul[contains(@class,'menu-footer')])[1]/li/a)["+count+"]"));
     System.out.println("Data set value: "+data.get(i)+" "+"Submenu value: "+element.get(i).getText());
     Assert.assertEquals(data.get(i), element.get(i).getText());
     el1.click();
     CommonSteps.I_pause_for_seconds(2);
     	CommonSteps.I_should_see_on_page("FirstProduct");
     	CommonSteps.I_scroll_thru_page();
     CommonSteps.I_pause_for_seconds(5);
     count = count+1;
   
     }
     }
	 
	 
		@Then("^I validate page navigation for below listed products categories available under '(.*)' and sub menu as '(.*)'$")
	    public static void validate_page_navigation(String element1,String submenu,DataTable table) throws Exception
    {

			 List<String> data = table.asList();
		     System.out.println(data.get(0));
		        int count =1;
		        for (int i = 0; i <=data.size()-1; i++)
		        {
		        	Actions action = new Actions(driver);
					WebElement wElement = WrapperFunctions.getElementByLocator(element1,
					GetPageObjectRead.OR_GetElement(element1), elementWaitTime);
					action.moveToElement(wElement).build().perform();
					List<WebElement> element = driver.findElements(By.xpath("(((//li//a[contains(text(),'"+submenu+"')])[2]//following::ul[@class='level-3'])[1]//a)"));
	    		WebElement ele =driver.findElement(By.xpath("(((//li//a[contains(text(),'"+submenu+"')])[2]//following::ul[@class='level-3'])[1]//a)["+count+"]"));
	    		Assert.assertEquals(data.get(i), element.get(i).getText());
	    		System.out.println("Data set value: "+data.get(i)+" "+"Submenu value: "+element.get(i).getText());
	    		ele.click();
	   	     CommonSteps.I_pause_for_seconds(5);
	    		CommonSteps.I_should_see_text_present_on_page(submenu);
	    		CommonSteps.I_should_see_on_page("FirstProduct");
				CommonSteps.I_pause_for_seconds(5);
	    	     count = count+1;
	    	   
			}
	        
	    }
		@Then("^I validate page navigation for below listed product categories available under '(.*)' and sub menus as '(.*)'$")
	    public static void validate_page_navigation_product(String element1,String submenu,DataTable table) throws Exception
	    {

			 List<String> data = table.asList();
		     System.out.println(data.get(0));
		        int count =1;
		        for (int i = 0; i <=data.size()-1; i++)
		        {
		        	Actions action = new Actions(driver);
					WebElement wElement = WrapperFunctions.getElementByLocator(element1,
					GetPageObjectRead.OR_GetElement(element1), elementWaitTime);
					action.moveToElement(wElement).build().perform();
					List<WebElement> element = driver.findElements(By.xpath("(((//li//a[contains(text(),'"+submenu+"')])[1]//following::ul[@class='level-3'])[1]//a)"));
	    		WebElement ele =driver.findElement(By.xpath("(((//li//a[contains(text(),'"+submenu+"')])[1]//following::ul[@class='level-3'])[1]//a)["+count+"]"));
	    		Assert.assertEquals(data.get(i), element.get(i).getText());
	    		System.out.println("Data set value: "+data.get(i)+" "+"Submenu value: "+element.get(i).getText());
	    		ele.click();
	   	     CommonSteps.I_pause_for_seconds(5);
	    		CommonSteps.I_should_see_text_present_on_page(submenu);
				CommonSteps.I_pause_for_seconds(5);
	    	     count = count+1;
	    	   
			}
	        
	    }


		@Then("^I validate page on sale navigation for below listed product categories available under '(.*)' and sub menus as '(.*)'$")
	    public static void validate_on_sale_page_navigation_product(String Header,String submenu,DataTable table) throws Exception
	    {

			 List<String> data = table.asList();
		     System.out.println(data.get(0));
		        int count =1;
		        for (int i = 0; i <=data.size()-1; i++)
		        {
		        	Actions action = new Actions(driver);
					WebElement wElement = WrapperFunctions.getElementByLocator(Header,
					GetPageObjectRead.OR_GetElement(Header), elementWaitTime);
					action.moveToElement(wElement).build().perform();
					List<WebElement> element = driver.findElements(By.xpath("(((//li//a[contains(text(),'"+submenu+"')])[1]//following::ul[@class='level-3'])[1]/li/a)"));
		    		WebElement ele =driver.findElement(By.xpath("(((//li//a[contains(text(),'"+submenu+"')])[1]//following::ul[@class='level-3'])[1]/li/a)["+count+"]"));
		    		
					Assert.assertEquals(data.get(i), element.get(i).getText());
	    		System.out.println("Data set value: "+data.get(i)+" "+"Submenu value: "+element.get(i).getText());
	    		ele.click();
	   	     CommonSteps.I_pause_for_seconds(5);
	    		CommonSteps.I_should_see_text_present_on_page(submenu);
				CommonSteps.I_pause_for_seconds(5);
	    	     count = count+1;
	    	   
			}
	        
	    }



		@Then("^I verify HeaderMenuNavLinkItem$")
	public static void i_verify_top_banner_menu() throws InterruptedException {
		List<WebElement> lst = driver.findElements(By.xpath("//ul[@class=\"menu-category level-1\"]/li"));
		try {
			for (WebElement menu : lst) {
				Actions action = new Actions(driver);
				action.moveToElement(menu).build().perform();
				WrapperFunctions.highLightElement(menu);
				Thread.sleep(3000);
				System.out.println("Successfully found the Element" + menu.getText());
			}
		} catch (Exception e) {
			System.out.println("Element not found");
			e.printStackTrace();
		}
	}
	@Then("^I generate random email id '(.*)'$")
	public static void i_generate_random_email_id(String Locator) {
		WebElement emailTextBx = driver.findElement(GetPageObjectRead.OR_GetElement(Locator));
		emailTextBx.click();
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(1000);
		emailTextBx.sendKeys("shas" + randomInt + "@gmail.com");
	}
	
	@Then("^I generate the random email id '(.*)'$")
	public static void i_generate_email_id(String Locator) {
		WebElement emailTextBx = driver.findElement(GetPageObjectRead.OR_GetElement(Locator));
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(1000);
		emailTextBx.sendKeys("shas" + randomInt + "@gmail.com");
	}
	
	@Given("^I Enable the windows based Popup$")
	public static void i_enable_popup() throws Exception {
		try {
//			WebElement iframepopup = driver.findElement(By.xpath("//iframe[@class=\"affirm-sandbox-iframe\" and @id=\"affirm_overlay\"]"));
//			driver.switchTo().frame(iframepopup);

//			WebElement closepopup = driver.findElement(By.xpath("//button[@id=\"affirm-primary-action-button\" and contains(text(),'Continue')]"));
			WebElement closepopup = driver.findElement(By.xpath("//button[contains(text(),'Continue')]"));
			boolean displayed = closepopup.isDisplayed();
			log.info(displayed);
			closepopup.click();
			Thread.sleep(1000);

		} catch (Exception e) {

//			CommonSteps.I_should_see_on_page("");
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@When("^I navigate to '(.*)' application$")
	public static void i_navigate_to_application(String url) throws Exception {
		String methodName = "i_navigate_to_application '" + url + "'";

		try {
			
				driver.navigate().to(url);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

//	public static void main(String[] args) {
//		String property = System.getProperty("test.appUrl");
//		log.info(property);
//	String[] split = property.split(".",2);
//	String Navigateurl = split[0] + "." + "davidsbridal.ca";
//	log.info(Navigateurl);
//		driver.navigate().to(Navigateurl);
//	}

	@Given("^My NativeApp '(.*)' is open$")
	public static void my_nativeapp_is_open(String app) throws Exception {
		String methodName = "my_nativeapp_is_open '" + app + "'";

		try {
			driver = StepBase.getDriver();
			if (System.getProperty("test.pageObjectMode").equalsIgnoreCase("xlsx")) {
				GetPageObjectRead.ReadExcel(app);
			}
			Thread.sleep(15000);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I verify the url$")
	public static void i_verify_the_url() throws Exception {

		String methodName = "i_verify_the_url";

		try {
			String currentUrl = driver.getCurrentUrl();
			log.info(currentUrl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);

		}

	}

	@Then("I navigate to '(.*)' url")
	public static void i_navigate_to_url(String url) throws Exception {
		String methodName = "i_navigate_to_url '" + url + "'";

		try {
			driver.navigate().to(url);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I navigate to '(.*)' page$")
	public static void I_NavigateTo(String url) throws Exception {
		String methodName = "I_NavigateTo '" + url + "'";

		try {
			driver.navigate().to(HashMapContainer.getPO(url));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I refresh the WebPage$")
	public static void I_Refresh_WebPage() throws Exception {
		String methodName = "I_Refresh_WebPage";

		try {
			driver.navigate().refresh();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I focus and click '(.*)'$")
	public static void I_focus_click(String element) throws Exception {
		String methodName = "I_focus_click '" + element + "'";

		try {
			Actions ac = new Actions(driver);
			// WebElement findElement =
			// driver.findElement(GetPageObjectRead.OR_GetElement(element));
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			ac.moveToElement(wElement).click().build().perform();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Given("^I click on element for IOS '(.*)'$")
	public static void I_click_on_element_for_IOS(String element) throws Exception {
		String methodName = "I_click_on_element_for_IOS '" + element + "'";
		if (System.getProperty("test.disableSearchClickIOS").equalsIgnoreCase("false")) {
			try {

				if (element.length() > 1) {
					if (element.substring(0, 2).equals("$$")) {
						log.info("Fetching from HMcontainer!");
						element = HashMapContainer.get(element);
					}
				}
				WebElement wElement = WrapperFunctions.getElementByLocator(element,
						GetPageObjectRead.OR_GetElement(element), elementWaitTime);
				wElement.click();

			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception(e);
			}
		}
	}

	@Then("^I wait for '(.*)' seconds$")
	public static void I_pause_for_seconds(int seconds) throws Exception {
		String methodName = "I_pause_for_seconds '" + seconds + "'";

		try {
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I wait for visibility of element '(.*)'$")
	public static void I_wait_for_visibility_of_element(String element) throws Exception {
		String methodName = "I_wait_for_visibility_of_element '" + element + "'";
		try {
			WrapperFunctions.waitForElementVisibility(GetPageObjectRead.OR_GetElement(element));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);

		}
	}

	@Then("^I enter '(.*)' in field '(.*)'$")
	public static void I_enter_in_field(String value, String element) throws Exception {
		String methodName = "I_enter_in_field '" + value + "' '" + element + "'";

		try {
			if (value.length() > 3) {
				if (value.substring(0, 2).equals("$$")) {
					log.info("Fetching from HMcontainer!");
					value = HashMapContainer.get(value);
				}
			}
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			wElement.sendKeys(value);
			// TODO Dismiss keyboard for webView
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I enter '(.*)' in the feild '(.*)' using actions$")
	public static void i_enter_in_the_feild_using_actions(String value, String element) throws Exception {
		String methodName = "i_enter_in_the_feild_using_actions '" + value + "' '" + element + "'";

		try {
			WebElement inputElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			if (inputElement.isDisplayed() && inputElement.isEnabled()) {
				Actions ac = new Actions(driver);
				ac.sendKeys(inputElement, value).build().perform();

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);

		}
	}

	@Then("^I clear the text and enter '(.*)' in field '(.*)' by JS$")
	public static void i_clear_and_enter_the_text_js(String value, String element) throws Exception {
		String methodName = "i_clear_and_enter_the_text_js '" + value + "' '" + element + "'";

		try {
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('value', '" + value + "')", wElement);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@When("^I should see the selected value '(.*)' in the drop down '(.*)'$")
	public static void i_should_see_selected_value_in_the_dropdown(String value, String element) throws Exception {
		String methodName = "i_should_see_selected_value_in_the_dropdown '" + value + "' '" + element + "'";

		try {
			if (value.startsWith("$$")) {
				value = HashMapContainer.get(value);
			}
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);

			Select sc = new Select(wElement);
			String text = sc.getFirstSelectedOption().getText();
			Assert.assertEquals(value, text);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}

	}

	@Then("^I clear the text and enter '(.*)' in field '(.*)'$")
	public static void i_clear_and_enter_the_text(String value, String element) throws Exception {
		String methodName = "i_clear_and_enter_the_text '" + value + "' '" + element + "'";
		try {
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);

			wElement.clear();
			if (value.length() > 3) {
				if (value.substring(0, 2).equals("$$")) {
					log.info("Fetching from HMcontainer!");
					value = HashMapContainer.get(value);
				}
			}
			wElement.sendKeys(value);
		} catch (Exception e) {

			e.printStackTrace();

			throw new Exception(e);
		}
	}

	@Then("^I clear field '(.*)'$")
	public static void I_clear_Field(String element) throws Exception {
		String methodName = "I_clear_Field '" + element + "'";
		try {
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			wElement.clear();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I hit enter-key on element '(.*)'$")
	public static void I_hit_key_on_element(String element) throws Exception {
		String methodName = "I_hit_key_on_element '" + element + "'";

		try {

			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			wElement.sendKeys(Keys.ENTER);
			// TODO Dismiss keyboard for webView
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I hit down Arrow key on element '(.*)'$")
	public static void I_hit_Downkey_on_element(String element) throws Exception {
		String methodName = "I_hit_Downkey_on_element '" + element + "'";

		try {

			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			wElement.sendKeys(Keys.ARROW_DOWN);
			// TODO Dismiss keyboard for webView
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I mouse over '(.*)'$")
	public static void I_mouse_over(String element) throws Exception {
		String methodName = "I_mouse_over '" + element + "'";

		try {
			Actions action = new Actions(driver);
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			action.moveToElement(wElement).build().perform();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I mouse over to '(.*)' and click sub menu Head '(.*)'$")
	public static void I_mouse_over_and_click(String element,String element1) throws Exception {

		try {
			Actions action = new Actions(driver);
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
			GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			action.moveToElement(wElement).build().perform();
			WebElement wElement1 = WrapperFunctions.getElementByLocator(element1,
			GetPageObjectRead.OR_GetElement(element1), elementWaitTime);
			wElement1.click();
			CommonSteps.I_pause_for_seconds(3);
//			CommonSteps.I_should_see_on_page("BannerTitle");
//			CommonSteps.I_should_see_on_page("ResultHead");

		
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	
	
	}

	
	
	
	@Then("^I verify tool tip message '(.*)' for element '(.*)'$")
	public static void I_Verify_ToolTip_Message(String expectedMessage, String element) throws Exception {
		String methodName = "I_Verify_ToolTip_Message '" + expectedMessage + "' '" + element + "'";

		try {
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			/*
			 * Actions action = new Actions(driver); WebElement we =
			 * driver.findElement(GetPageObject.OR_GetElement(element));
			 * action.moveToElement(we).build().perform();
			 */
			String ToolTipMessage = wElement.getAttribute("title");
			Assert.assertEquals(expectedMessage, ToolTipMessage);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I verify value '(.*)' in field '(.*)'$")
	public static void I_Verify_value_inField(String expectedValue, String element) throws Exception {
		String methodName = "I_Verify_value_inField '" + expectedValue + "' '" + element + "'";
		try {

			if (expectedValue.length() > 1) {
				if (expectedValue.substring(0, 2).equals("$$")) {
					log.info("Fetching from HMcontainer!");
					expectedValue = HashMapContainer.get(expectedValue);
				}
			}
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);

			String ActualValue = wElement.getAttribute("value");
			Assert.assertEquals(expectedValue, ActualValue,
					"Actual value does not match the expected value in specified field!");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I verify element '(.*)' is disabled$")
	public static void I_Verify_Element_isDisabled(String element) throws Exception {
		String methodName = "I_Verify_Element_isDisabled '" + element + "'";
		try {
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			if (wElement.isEnabled()) {
				throw new Exception("Element is enabled!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I enter '(.*)' into RichText Editor '(.*)'$")
	public static void I_enter_into_RichTextEditor(String value, String element) throws Exception {
		String methodName = "I_enter_into_RichTextEditor '" + value + "' '" + element + "'";

		try {
			driver.switchTo().frame("tinymce");
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			wElement.clear();
			wElement.sendKeys(value);
			driver.switchTo().defaultContent();
			// TODO Dismiss keyboard for webView
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Given("^I click '(.*)'$")
	public static void I_click(String element) throws Exception {
		String methodName = "I_click '" + element + "'";
		try {
			if (element.length() > 1) {
				if (element.substring(0, 2).equals("$$")) {
					log.info("Fetching from HMcontainer!");
					element = HashMapContainer.get(element);
				}
			}
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			wElement.click();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Given("^I double-click '(.*)'$")
	public static void I_doubleClick(String element) throws Exception {
		String methodName = "I_doubleClick '" + element + "'";

		try {
			if (element.length() > 1) {
				if (element.substring(0, 2).equals("$$")) {
					log.info("Fetching from HMcontainer!");
					element = HashMapContainer.get(element);
				}
			}
			Actions act = new Actions(driver);
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			act.doubleClick(wElement).build().perform();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Given("^I take screenshot$")
	public static void take_Screenshot() throws Exception {
		String methodName = "take_Screenshot";

		try {

			Thread.sleep(1000);
			final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			Hooks.scenario.embed(screenshot, "image/png");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Given("^I click link '(.*)'$")
	public static void I_click_link(String linkText) throws Exception {
		String methodName = "I_click_link '" + linkText + "'";

		try {
			WebElement wElement = WrapperFunctions.getElementByLocator(linkText, By.linkText(linkText),
					elementWaitTime);
			wElement.click();
			WrapperFunctions.waitForPageToLoad();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I drag field '(.*)' and drop '(.*)'$")
	public static void I_drag_Field(String element, String element2) throws Exception {
		String methodName = "I_drag_Field '" + element + "' '" + element2 + "'";

		try {
			WebElement source = WrapperFunctions.getElementByLocator(element, GetPageObjectRead.OR_GetElement(element),
					elementWaitTime);
			WebElement target = WrapperFunctions.getElementByLocator(element2,
					GetPageObjectRead.OR_GetElement(element2), elementWaitTime);
			Actions ac = new Actions(driver);
			ac.dragAndDrop(source, target).build().perform();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Given("^I click alert accept$")
	public static void I_click_alert_accept() throws Exception {
		String methodName = "I_click_alert_accept";

		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I click by JS '(.*)'$")
	public static void I_clickJS(String element) throws Exception {
		String methodName = "I_clickJS '" + element + "'";

		try {
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			wElement.isEnabled();
			wElement.isDisplayed();
			WrapperFunctions.clickByJS(wElement);
			StepBase.embedScreenshot();
			WrapperFunctions.waitForPageToLoad();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I should see element '(.*)' present on page$")
	public static void I_should_see_on_page(String element) throws Exception {
		String methodName = "I_should_see_on_page '" + element + "'";

		try {
			if (element.length() > 1) {
				if (element.substring(0, 2).equals("$$")) {
					log.info("Fetching from HMcontainer!");
					element = HashMapContainer.get(element);
				}
			}
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			if (wElement.isDisplayed()) {
				WrapperFunctions.highLightElement(wElement);
			} else {
				throw new Exception("Element is not found! :" + element);

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);

		}
	}

	@Then("^I should see element '(.*)' is disabled on page$")
	public static void I_should_see_disbaled_on_page(String element) throws Exception {

		String methodName = "I_should_see_disbaled_on_page '" + element + "'";

		try {
			if (element.length() > 1) {
				if (element.substring(0, 2).equals("$$")) {
					log.info("Fetching from HMcontainer!");
					element = HashMapContainer.get(element);
				}
			}
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			if (!wElement.isEnabled()) {
				WrapperFunctions.highLightElement(wElement);
			} else {
				throw new Exception("Element is not disabled! :" + element);
			}
//
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);

		}
	}

	@Then("^I should see element '(.*)' present on page_$")
	public static void I_should_see_on_page_AndScreenshot(String element) throws Exception {

		String methodName = "I_should_see_on_page_AndScreenshot '" + element + "'";

		try {
			try {
				if (element.length() > 1) {
					if (element.substring(0, 2).equals("$$")) {
						log.info("Fetching from HMcontainer!");
						element = HashMapContainer.get(element);
					}
				}
				WebElement wElement = WrapperFunctions.getElementByLocator(element,
						GetPageObjectRead.OR_GetElement(element), elementWaitTime);
				if (wElement.isDisplayed()) {
					WrapperFunctions.highLightElement(driver.findElement(GetPageObjectRead.OR_GetElement(element)));
					StepBase.embedScreenshot();
					// Utilities.takeScreenshot(driver);
				}

			} catch (Exception e) {

				log.info("Element is not found");
				throw new ElementNotFoundException(element, "", "");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I should not see element '(.*)' present on page$")
	public static void I_should_not_see_on_page(String element) throws Exception {

		String methodName = "I_should_not_see_on_page '" + element + "'";

		try {
			if (element.length() > 1) {
				if (element.substring(0, 2).equals("$$")) {
					log.info("Fetching from HMcontainer!");
					element = HashMapContainer.get(element);
				}
			}
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			log.info("Element is displayed: " + wElement.isDisplayed());
			if (wElement.isDisplayed()) {
				StepBase.embedScreenshot();
				WrapperFunctions.highLightElement(driver.findElement(GetPageObjectRead.OR_GetElement(element)));
				throw new Exception("Element is found on page!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I should see text '(.*)' present on page$")
	public static void I_should_see_text_present_on_page(String expectedText) throws Exception {

		String methodName = "I_should_see_text_present_on_page '" + expectedText + "'";
		try {
			if (expectedText.length() > 1) {
				if (expectedText.substring(0, 2).equals("$$")) {
					log.info("Fetching from HMcontainer!");
					expectedText = HashMapContainer.get(expectedText);
				}
			}
			if (driver.getPageSource().contains(expectedText)) {
				log.info("Text " + expectedText + " found on page!");
				StepBase.embedScreenshot();
			} else {
				throw new ElementNotFoundException(expectedText, " ", " ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I verify checkbox '(.*)' is '(.*)'$")
	public static void I_verify_checkBox_is_Checked(String element, String status) throws Exception {

		String methodName = "I_verify_checkBox_is_Checked '" + element + "' '" + status + "'";

		try {
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			if (status.equalsIgnoreCase("checked")) {
				if (!wElement.isSelected()) {
					throw new Exception("Specified Checkbox is not checked!");
				}
			} else if (status.equalsIgnoreCase("unchecked")) {
				if (wElement.isSelected()) {
					throw new Exception("Specified Checkbox is checked!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I switch to iFrame '(.*)'$")
	public static void I_switchTo_iFrame(String FrameID) throws Exception {

		String methodName = "I_switchTo_iFrame '" + FrameID + "'";

		try {
			WebElement wElement = WrapperFunctions.getElementByLocator(FrameID,
					GetPageObjectRead.OR_GetElement(FrameID), elementWaitTime);
			driver.switchTo().frame(wElement);
			StepBase.embedScreenshot();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

	}

	@Then("^I switch to default content$")
	public static void I_switchTo_DefaultContent() throws Exception {

		String methodName = "I_switchTo_DefaultContent";

		try {
			driver.switchTo().defaultContent();
			StepBase.embedScreenshot();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

	}

	@Then("^I switch back to Main Window$")
	public static void I_switchTo_MainWindow() throws Exception {

		String methodName = "I_switchTo_MainWindow";

		try {
			driver.switchTo().window(driver.getWindowHandle());
			StepBase.embedScreenshot();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

	}

	@Then("^I should see text '(.*)' present on page at '(.*)'$")
	public static void I_should_see_text_present_on_page_At(String expectedText, String element) throws Exception {

		String methodName = "I_should_see_text_present_on_page_At '" + expectedText + "' '" + element + "'";

		try {
			if (expectedText.length() > 1) {
				if (expectedText.substring(0, 2).equals("$$")) {
					log.info("Fetching from HMcontainer!");
					expectedText = HashMapContainer.get(expectedText);
				}
			}
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			String actualText = wElement.getText();
			WrapperFunctions.highLightElement(wElement);
			Assert.assertEquals(expectedText, actualText);
			StepBase.embedScreenshot();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);

		}
	}

	@Then("^I should see text '(.*)' contained on page at '(.*)'$")
	public static void I_should_see_text_contained_on_page_At(String expectedText, String element) throws Exception {

		String methodName = "I_should_see_text_contained_on_page_At '" + expectedText + "' '" + element + "'";

		try {
			if (expectedText.length() > 1) {
				if (expectedText.substring(0, 2).equals("$$")) {
					log.info("Fetching from HMcontainer! " + HashMapContainer.get(expectedText));
					expectedText = HashMapContainer.get(expectedText);
				}
			}
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			String actualText = wElement.getText();
			WrapperFunctions.highLightElement(wElement);
			log.info("Actual " + actualText + " Expected: " + expectedText);

			try {
				Assert.assertTrue(expectedText.toLowerCase().contains((actualText.toLowerCase())));
			} catch (AssertionError e) {
				Assert.assertEquals(expectedText, actualText);
			}
			StepBase.embedScreenshot();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Given("^I should see variable '(.*)' value contained in expected value '(.*)'$")
	public static void i_should_see_element_text_present_on_expected_value(String expecString, String element)
			throws Exception {

		String methodName = "i_should_see_element_text_present_on_expected_value '" + expecString + "' '" + element
				+ "'";

		try {
			if (expecString.length() > 1) {
				if (expecString.substring(0, 2).equals("$$")) {
					log.info("Fetching from HMcontainer! " + HashMapContainer.get(expecString));

					expecString = HashMapContainer.get(expecString);
				}
			}
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			String actualText = wElement.getText();
			WrapperFunctions.highLightElement(wElement);
			log.info("Actual " + actualText + " Expected: " + expecString);

			try {
				Assert.assertTrue(actualText.toLowerCase().contains((expecString.toLowerCase())));
			} catch (AssertionError e) {
				Assert.assertEquals(expecString, actualText);

			}
			StepBase.embedScreenshot();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I click using actions '(.*)'$")
	public static void I_click_using_actions(String element) throws Exception {
		String methodName = "I_click_using_actions '" + element + "'";

		try {
			Actions ac = new Actions(driver);
			// WebElement findElement =
			// driver.findElement(GetPageObjectRead.OR_GetElement(element));
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			ac.click(wElement).build().perform();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I should see text matching regx '(.*)' present on page at '(.*)'$")
	public static void I_should_see_text_matching_regularExpression(String regx, String element) throws Exception {

		String methodName = "I_should_see_text_matching_regularExpression '" + regx + "' '" + element + "'";

		try {
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			String actualText = wElement.getText();
			// Assert.assertTrue(actualText.matches(regx),"Actual Text Found:
			// "+actualText+"|");
			Assert.assertTrue(actualText.matches(regx));
			StepBase.embedScreenshot();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

	}

	@Then("^I wait '(.*)' seconds for presence of element '(.*)'$")
	public static void I_wait_for_presence_of_element(int seconds, String element) throws Exception {

		String methodName = "I_wait_for_presence_of_element '" + seconds + "' '" + element + "'";

		try {
			WrapperFunctions.waitForElementPresence(GetPageObjectRead.OR_GetElement(element), seconds);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I switch to window with title '(.*)'$")
	public static void I_switch_to_window_with_title(String title) throws Exception {

		String methodName = "I_switch_to_window_with_title '" + title + "'";

		try {
			if (title.length() > 1) {
				if (title.substring(0, 2).equals("$$")) {
					log.info("Fetching from HMcontainer!");
					title = HashMapContainer.get(title);
				}
			}
			WrapperFunctions.switchToWindowUsingTitle(title);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

	}

	@Then("^I select option '(.*)' in dropdown '(.*)' by '(.*)'$")
	public static void I_select_option_in_dd_by(String option, String element, String optionType) throws Exception {

		String methodName = "I_select_option_in_dd_by '" + option + "' '" + element + "' '" + optionType + "'";

		try {
			Assert.assertTrue(WrapperFunctions.selectDropDownOption(GetPageObjectRead.OR_GetElement(element), option,
					optionType));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I scroll to '(.*)' - '(.*)'$")
	public static void I_scroll_to_element(String scrolltype, String element) throws Exception {

		String methodName = "I_scroll_to_element '" + scrolltype + "' '" + element + "'";

		try {
			WrapperFunctions.scroll(scrolltype, element);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}


	@Then("^I scroll through page$")
	public static void I_scroll_thru_page() throws Exception {

		String methodName = "I_scroll_thru_page";

		try {
			for (int i = 0; i <= 16; i++) {
				WrapperFunctions.scroll("coordinates", "0,200");
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I scroll to top of the page$")
	public static void i_scroll_through_top_page() throws Exception {

		String methodName = "i_scroll_through_top_page";

		try {
			WrapperFunctions.scroll("top", "");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I store text '(.*)' as '(.*)'$")
	public static void I_get_text_from(String TextValue, String ValueName) throws Exception {

		String methodName = "I_get_text_from '" + TextValue + "' '" + ValueName + "'";

		try {
			HashMapContainer.add("$$" + ValueName, TextValue);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I get text from '(.*)' and store$")
	public static void I_get_text_from_store(String element) throws Exception {

		String methodName = "I_get_text_from_store '" + element + "'";

		try {
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			String value = wElement.getText();
			HashMapContainer.add("$$" + element, value);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I enter from stored variable '(.*)' into feild '(.*)'$")
	public static void I_enter_from_StoredValue(String StoredValue, String element) throws Exception {

		String methodName = "I_enter_from_StoredValue '" + StoredValue + "' '" + element + "'";

		try {
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			wElement.sendKeys(HashMapContainer.get(StoredValue));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Then("^I compare '(.*)' with stored value '(.*)'$")
	public static void I_compare_with_StoredValue(String element, String storedValue) throws Exception {

		String methodName = "I_compare_with_StoredValue '" + element + "' '" + storedValue + "'";

		try {
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			String actualValue = wElement.getText();
			storedValue = HashMapContainer.get(storedValue);
			Assert.assertEquals(storedValue, actualValue,
					"I Compare " + actualValue + " with expected value " + storedValue);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	@Then("^I should see sub menu '(.*)' present on page at '(.*)'$")
	public static void I_should_see_sub_menu_present_on_page_At(String expectedText, String element) throws Exception {

		try {
			if (expectedText.length() > 1) {
				if (expectedText.substring(0, 2).equals("$$")) {
					log.info("Fetching from HMcontainer!");
					expectedText = HashMapContainer.get(expectedText);
				}
			}
			WebElement wElement = WrapperFunctions.getElementByLocator(element,
					GetPageObjectRead.OR_GetElement(element), elementWaitTime);
			String actualText = wElement.getText();
			WrapperFunctions.highLightElement(wElement);
			Assert.assertEquals(expectedText, actualText);
			StepBase.embedScreenshot();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);

		}
	}

	
	@Then("^I close browser$")
	public static void I_Close_Browser() throws Exception {

		try {
			driver.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

}
