package com.TEAF.framework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.TEAF.Hooks.Hooks;
import com.jayway.jsonpath.JsonPath;

public class Utilities {
	static Logger log = Logger.getLogger(Utilities.class.getName());
	public static WebDriver driver = StepBase.getDriver();

	public static byte[] takeScreenshotByte(WebDriver driver) {
		try {
			byte[] scrFile = null;
			if (System.getProperty("test.disableScreenshotCapture").equalsIgnoreCase("false")) {
				Thread.sleep(1000);
				scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			}
			return scrFile;
		} catch (Exception e) {

			throw new RuntimeException(e);

		}
	}

	public static String getRequiredDate(int incrementDays, String expectedDateFormat, String timeZoneId) {
		try {
			DateFormat dateFormat;
			Calendar calendar = Calendar.getInstance();
			dateFormat = new SimpleDateFormat(expectedDateFormat);
			if (timeZoneId != null && !timeZoneId.equals(""))
				dateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneId));
			calendar.add(Calendar.DAY_OF_MONTH, incrementDays);
			Date tomorrow = calendar.getTime();
			String formattedDate = dateFormat.format(tomorrow);
			return formattedDate;
		} catch (Exception e) {

			return null;
		}
	}

	public static String takeScreenshot(WebDriver driver) {
		try {
			String SSPath = "";
			if (System.getProperty("test.disableScreenshotCapture").equalsIgnoreCase("false")) {
				Thread.sleep(1000);
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				File directory = new File(String.valueOf("Screenshots"));
				if (!directory.exists()) {
					directory.mkdir();
				}
				SSPath = "Screenshots/" + getRequiredDate(0, "yyyy_MM_dd_hh", null) + "/screenshot_"
						+ getRequiredDate(0, "yyyy_MM_dd_hh_mm_ss", null) + ".png";
				FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") + "/output/" + SSPath));
			}
			return SSPath;
		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}

	public void waitFor(final Long timeInMilliseconds) {
		try {
			Thread.sleep(timeInMilliseconds);
		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}

	public static void waittextToBePresentInElement(String location, String expectedText) {
		try {
			WebDriverWait wb = new WebDriverWait(driver, 50);

			wb.until(ExpectedConditions.textToBePresentInElement(
					driver.findElement(GetPageObjectRead.OR_GetElement(location)), expectedText));
		} catch (Exception e) {

			throw new RuntimeException("expected text");

		}
	}

	public static void scrollToTopOfPage() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(0,-document.body.scrollHeight);");
		} catch (Exception e) {

			throw new RuntimeException(e);

		}
	}

	public void waitForPageLoad() {
		try {
			WebDriverWait wait = new WebDriverWait(StepBase.getDriver(), 180);
			final JavascriptExecutor javascript = (JavascriptExecutor) (StepBase
					.getDriver() instanceof JavascriptExecutor ? StepBase.getDriver() : null);
			wait.until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					return javascript.executeScript("return document.readyState").equals("complete");
				}
			});

			StepBase.getDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}

	public static void deleteZipFiles(String desFN) {
		try {
			String zipName = System.getProperty("user.dir") + "/" + desFN + ".zip";
			String folderName = System.getProperty("user.dir") + "/" + desFN + ".zip";

			FileUtils.forceDelete(new File(zipName));
			FileUtils.forceDelete(new File(folderName));

		} catch (Exception e) {

		}
	}

	public static void deleteFiles(String desFN) {
		try {
			String zipName = System.getProperty("user.dir") + "/" + desFN;
			FileUtils.deleteDirectory(new File(zipName));

		} catch (Exception e) {

		}
	}

	public static void reportstoZipFile(String srcfolder, String desFN) throws Exception {

		try {

			// Use the following paths for windows
			String folderToZip = System.getProperty("user.dir") + "/" + srcfolder;
			String zipName = System.getProperty("user.dir") + "/" + desFN + ".zip";

			File f = new File(folderToZip);
			if (f.isDirectory()) {

				final Path sourceFolderPath = Paths.get(folderToZip);
				Path zipPath = Paths.get(zipName);
				final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()));
				Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(file).toString()));
						Files.copy(file, zos);
						zos.closeEntry();
						return FileVisitResult.CONTINUE;
					}
				});
				zos.close();
			}
		} catch (Exception e) {
			//
		}

	}

	public static void auto_generation_Email(String mailId, String ccMail) throws Exception {

		// Create object of Property file
		Properties props = new Properties();
		props.put("mail.smtp.host", System.getProperty("email.smtp.server"));
		props.put("mail.smtp.socketFactory.port", System.getProperty("email.smtp.socketPortNumber"));
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", System.getProperty("email.smtp.portNumber"));
		props.put("mail.smtp.starttls.enable", "true");
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(System.getProperty("email.user.emailId"),
						System.getProperty("email.user.password"));
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(System.getProperty("email.user.emailAddress")));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailId));
			message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(ccMail));
			String lDate = LocalDate.now().toString();
			String lTime = LocalTime.now().toString();

			Collection<String> values = com.TEAF.Hooks.Hooks.scenarioStatus.values();
			String flag;
			int failCount = 0;
			int passCount = 0;
			int skipCount = 0;
			for (String x : values) {
				if (x.equalsIgnoreCase("Failed")) {
					failCount++;
				} else if (x.equalsIgnoreCase("Passed")) {
					passCount++;
				} else {
					skipCount++;
				}
			}
			if (values.contains("FAILED") || values.contains("SKIPPED")) {
				flag = "Build Failed";
			} else {
				flag = "Build Passed";
			}

			String subject = null;
			if (System.getProperty("email.subject").length() > 2) {
				subject = System.getProperty("email.subject") + ":" + " on " + lDate + ": " + lTime + " :" + flag;
			} else {
				subject = "RC TEAF Execution Report " + ":" + " on " + lDate + ": " + lTime + " :" + flag;

			}

			message.setSubject(subject);
			MimeMultipart multipart = new MimeMultipart("related");

			BodyPart messageBodyPart1 = new MimeBodyPart();
			StringBuffer ScenarioTable = new StringBuffer();
			LinkedHashMap<String, String> mp = new LinkedHashMap<String, String>();
			mp.putAll(com.TEAF.Hooks.Hooks.scenarioStatus);
			Set<Entry<String, String>> entrySet = mp.entrySet();

			for (Entry<String, String> entry : entrySet) {

				if (entry.getValue().equalsIgnoreCase("passed")) {
					ScenarioTable.append("<TR ALIGN='CENTER' bgcolor ='#f2f2f2'><TD>" + entry.getKey()
							+ "</TD><TD bgcolor= '#419c4d' >" + entry.getValue() + "</TD> " + "</TR>");
				} else {
					ScenarioTable.append("<TR ALIGN='CENTER' bgcolor ='#f2f2f2'><TD>" + entry.getKey()
							+ "</TD><TD bgcolor= '#d63a29' >" + entry.getValue() + "</TD> " + "</TR>");
				}

			}

			String messageBody = null;
			if (System.getProperty("email.messageBody").length() > 2) {
				messageBody = System.getProperty("email.messageBody");
			} else {
				messageBody = "";

			}

			String signature = null;
			if (System.getProperty("email.signature").length() > 2) {
				signature = System.getProperty("email.signature");
			} else {
				signature = "RC QA";

			}

			File f = new File(System.getProperty("user.dir") + "/src/test/java/com/TestResults/cucumber-report/"
					+ System.getProperty("test.jsonFileName") + ".json");
			List<String> listSteps = JsonPath.read(f, "$..steps[*].result.status");
			int stepPassed = 0;
			int stepFailed = 0;
			int stepSkipped = 0;
			for (String x : listSteps) {
				if (x.toLowerCase().contains("pass")) {
					stepPassed++;
				} else if (x.toLowerCase().contains("fail")) {
					stepFailed++;
				} else if (x.toLowerCase().contains("skip")) {
					stepSkipped++;
				}
			}

			// Set the body of email
			String htmlText = "<H3><img src=\"cid:image\">   Script Execution Summary | Email Report </H>" + "<H4></H4>"
					+ "<TABLE WIDTH='75%' CELLPADDING='8' CELLSPACING='1'>"
					+ "<TR> <TH bgcolor = '#a8a7a7' COLSPAN='8'><H2>Test Execution Summary</H2></TH></TR><TR><TH>Total Test Steps: </TH><TH>Total Test Steps Passed: </TH><TH>Total Test Steps Failed: </TH><TH>Total Test Steps Skipped: </TH><TH>Total Test Cases: </TH><TH>Total Test cases Passed: </TH><TH>Total Test cases Failed: </TH><TH>Total Test cases Skipped: </TH>"
					+ "</TR>" + "<TR ALIGN='CENTER' bgcolor ='#f2f2f2'>" + "<TD>" + listSteps.size() + "</TD>"
					+ "<TD bgcolor= '#419c4d'>" + stepPassed + "</TD>" + "<TD  bgcolor= '#d63a29'>" + stepFailed
					+ "</TD>" + "<TD bgcolor= '#6bbbc7'>" + stepSkipped + "</TD>"

					+ "<TD>" + values.size() + "</TD><TD bgcolor= '#419c4d' >" + passCount
					+ "</TD><TD bgcolor= '#d63a29' >" + failCount + "</TD><TD bgcolor= '#6bbbc7' >" + skipCount
					+ "</TD>" + "</TR>" + "</TABLE><H4>" + messageBody + "<BR>" + "<BR>"
					+ "<p> Please find attached a detailed Test Automation execution report using RC TEAF with this email\r\n"
					+ " </p>" + "</H4> " + "\n" + "\n" + "<H4>Test Scenarios executed:</H4>"
					+ "<TABLE WIDTH='60%' CELLPADDING='4' CELLSPACING='1'>"
					+ "<TR ALIGN='CENTER' bgcolor ='#3767B6'><TD color: 'white'> Scenario Name </TD><TD bgcolor ='#3767B6' color: 'white'>Result</TD></TR>"
					+ ScenarioTable + "</TABLE>" + "\n" + "\n" +

					"Thanks, " + "\n" + "\n" + signature;
			messageBodyPart1.setContent(htmlText, "text/html");
			// add it
			multipart.addBodyPart(messageBodyPart1);

			// second part (the image)
			messageBodyPart1 = new MimeBodyPart();
			DataSource fds = new FileDataSource(
					System.getProperty("user.dir") + "/src/test/java/com/Resources/teaf.jpeg");

			messageBodyPart1.setDataHandler(new DataHandler(fds));
			messageBodyPart1.setHeader("Content-ID", "<image>");

			// add image to the multipart
			multipart.addBodyPart(messageBodyPart1);

			// Create another object to add another content
			MimeBodyPart messageBodyPart2 = new MimeBodyPart();
			MimeBodyPart messageBodyPart3 = new MimeBodyPart();

			// Mention the file which you want to send
			String fEN = System.getProperty("user.dir") + "/" + System.getProperty("reports.FolderName") + ".zip";
			String fUI = System.getProperty("user.dir") + "/TestExecution_UIReports.zip";

			multipart.addBodyPart(messageBodyPart1);

			if (java.nio.file.Files.exists(Paths.get(fEN), LinkOption.NOFOLLOW_LINKS)) {
				DataSource src = new FileDataSource(fEN);
				// set the handler
				messageBodyPart2.setDataHandler(new DataHandler(src));
				// set the file
				messageBodyPart2.setFileName(fEN);
				multipart.addBodyPart(messageBodyPart2);
			}

			if (java.nio.file.Files.exists(Paths.get(fUI), LinkOption.NOFOLLOW_LINKS)) {
				DataSource source1 = new FileDataSource(fUI);

				// set the handler
				messageBodyPart3.setDataHandler(new DataHandler(source1));
				// set the file
				messageBodyPart3.setFileName(fUI);
				multipart.addBodyPart(messageBodyPart3);

			}
			message.setContent(multipart);
			// finally send the email
			Transport.send(message);

			log.info("\n =====Reports Sent through Email from " + System.getProperty("email.user.emailAddress")
					+ "=====");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static void testStatusToastMessage(String message) throws InterruptedException {

		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			// Check for jQuery on the page, add it if need be
			js.executeScript("if (!window.jQuery) {"
					+ "var jquery = document.createElement('script'); jquery.type = 'text/javascript';"
					+ "jquery.src = 'https://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js';"
					+ "document.getElementsByTagName('head')[0].appendChild(jquery);" + "}");
			Thread.sleep(1000);
			// Use jQuery to add jquery-growl to the page
			js.executeScript("$.getScript('https://the-internet.herokuapp.com/js/vendor/jquery.growl.js')");

			// Use jQuery to add jquery-growl styles to the page
			js.executeScript("$('head').append('<link id=\"scenariotoast\" rel=\"stylesheet\" "
					+ "href=\"https://the-internet.herokuapp.com/css/jquery.growl.css\" " + "type=\"text/css\" />');");
			Thread.sleep(1000);
			// jquery-growl w/ no frills
			js.executeScript("$.growl({ title: 'Scenario', message: '" + message + "' });");

			Thread.sleep(3000);

			js.executeScript("var element = document.getElementById('scenariotoast');"
					+ "element.parentNode.removeChild(element);");
			Thread.sleep(1000);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.debug(e.getMessage());

		}

	}

	public static void testStatusFailToastMessage(String message) throws InterruptedException {

		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			// Check for jQuery on the page, add it if need be
			js.executeScript("if (!window.jQuery) {"
					+ "var jquery = document.createElement('script'); jquery.type = 'text/javascript';"
					+ "jquery.src = 'https://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js';"
					+ "document.getElementsByTagName('head')[0].appendChild(jquery);" + "}");
			Thread.sleep(1000);
			// Use jQuery to add jquery-growl to the page
			js.executeScript("$.getScript('https://the-internet.herokuapp.com/js/vendor/jquery.growl.js')");
			js.executeScript("$('head').append('<link id=\"failtoast\" rel=\"stylesheet\" "
					+ "href=\"https://the-internet.herokuapp.com/css/jquery.growl.css\" " + "type=\"text/css\" />');");
			Thread.sleep(1000);
			// jquery-growl w/ no frills
			js.executeScript("$.growl.error({ title: 'Failed', message: '" + message + "' });");

			Thread.sleep(1000);

			js.executeScript(
					"var element = document.getElementById('failtoast');" + "element.parentNode.removeChild(element);");
			Thread.sleep(1000);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.debug(e.getMessage());

		}

	}

	public static void testStatusToastPass(String message) throws InterruptedException {

		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			// Check for jQuery on the page, add it if need be
			js.executeScript("if (!window.jQuery) {"
					+ "var jquery = document.createElement('script'); jquery.type = 'text/javascript';"
					+ "jquery.src = 'https://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js';"
					+ "document.getElementsByTagName('head')[0].appendChild(jquery);" + "}");
			Thread.sleep(1000);
			js.executeScript("$.getScript('https://the-internet.herokuapp.com/js/vendor/jquery.growl.js')");

			js.executeScript("$('head').append('<link id=\"passtoast\" rel=\"stylesheet\" "
					+ "href=\"https://the-internet.herokuapp.com/css/jquery.growl.css\" " + "type=\"text/css\" />');");
			Thread.sleep(1000);
			js.executeScript("$.growl.notice({ title: 'Passed', message: '" + message + "' });");
			Thread.sleep(3000);

			js.executeScript(
					"var element = document.getElementById('passtoast');" + "element.parentNode.removeChild(element);");
			Thread.sleep(1000);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.debug(e.getMessage());

		}

	}

}
