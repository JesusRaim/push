package com.dedalow.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.google.common.base.Throwables;
import com.google.common.io.Files;

import com.dedalow.utils.Utils;
import com.dedalow.Launcher;
import com.dedalow.SharedDependencies;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

/**
 * Methods to make the different reports
 */
public class Report {

	private static String root = System.getProperty("user.dir") + SharedDependencies.fileSystem.getSeparator() + "logs";
	private static File rootFile = new File(root);
	private static Launcher launcher = new Launcher();
	private static JsonReport jsonReport = new JsonReport();
    private static File folderScreen = null;
	

  /**
   * Saves the results of each TestCase
   */
	public static void addResults() {
		TestSuite testSuite = jsonReport.testSuites.get(SharedDependencies.suiteName);
		testSuite =  new TestSuite(SharedDependencies.suiteName);

		TestCase testCase = new TestCase(SharedDependencies.caseName, SharedDependencies.results );
		TestCase testCaseExcel = new TestCase(SharedDependencies.caseName, SharedDependencies.results);
		testSuite.testCases.put(SharedDependencies.caseName, testCase);
		jsonReport.testSuites.put(SharedDependencies.suiteName, testSuite);
		jsonReport.aLtestSuites.add(testSuite);
		jsonReport.alTestCases.add(testCaseExcel);
		
	}

  /**
   * Puts execution logs in a log file
   * @param msg Message
   * @param log Type of log
   * @param wait Waiting time
   */
	
	public static void reportLog(String msg, String log, int wait) {
		try {
			rootFile.mkdirs();
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String logPath = SharedDependencies.folderTestCase + SharedDependencies.fileSystem.getSeparator() + "Log_" + SharedDependencies.caseName + ".log";
			File logFile = new File(logPath);
			FileWriter fw = new FileWriter(logFile, true);
			if (msg != "") {
				switch(log) {
					case "INFO":
						fw.write(df.format(new Date()) + " - " + log + " - " + msg + "\r\n");
						break;
					case "DEBUG":
						if (SharedDependencies.level.equals("DEBUG")) {
							fw.write(df.format(new Date()) + " - " + log + " - " + msg + "\r\n");
						} else {
							fw.write(df.format(new Date()) + " - INFO - More info changing LOG_LEVEL in confing.properties file\r\n");
						}
						break;
					case "ASYNCHRONOUS":
						if (SharedDependencies.level.equals("DEBUG")) {
							fw.write(df.format(new Date()) + " - DEBUG - " + msg + "\r\n");
						}
						break;
				}
			}

			if (wait > 0) {
				fw.write(df.format(new Date()) + " - " + log + " - " + "Thread sleep " + wait + "ms" + "\r\n");
			}
			
			fw.close();

		} catch (IllegalArgumentException | SecurityException | IOException e) {
			SharedDependencies.logger.severe(e.getMessage());
		}
	}

	

	

	

  /**
   * Performs log reports.
   *
   * @param msg Message
   * @param logLevel Log level
   * @param wait Waiting time
   * @param status Test result
   * @param isError Is an exception report
   * @param debugMsg Debug message
   */
	public static void reportLog (String msg, String logLevel, int wait, Status status,	boolean isError,
								String debugMsg) {
		try{
			if (isError) {
				failedStepReport(msg, logLevel, wait, status, debugMsg);
			} else {
				SharedDependencies.test.log(status, msg);
				reportLog(msg, logLevel, wait);
			}
		} catch (Exception e) {
			SharedDependencies.logger.severe(e.getMessage());
			SharedDependencies.test.log(status, msg);
		}
	}

	/**
	* Configures when you want to take a screenshot during test execution.
	*
	* @param screenShotFrequency Frequency of screenshots
	* @param takesScreenshot Driver or an HTML element that can capture a screenshot
	* @throws Exception Error conditions to capture
	*/
	public static void capScreenFrequency(String screenShotFrequency, TakesScreenshot takesScreenshot) throws Exception {
		switch(screenShotFrequency) {
			case "always":
				capScreen(takesScreenshot);
				break;
			case "only":
				List<String> listResults = Arrays.asList("BQ", "KO");
				String result = SharedDependencies.isAfter ? SharedDependencies.captureLog : SharedDependencies.finalResult;
				if (listResults.contains(result)) {
					capScreen(takesScreenshot);
				}
				break;
			default:
				break;
		}
	}

	/**
	* Take a screenshot
	*
	* @param takesScreenshot Driver or an HTML element that can capture a screenshot
	*/
	public static void capScreen(TakesScreenshot takesScreenshot) {
		try {
			String timeStamp = new SimpleDateFormat("HH.mm.ss.SSS").format(Calendar.getInstance().getTime());
			String name = "";

			if(SharedDependencies.isAfter) {
				name = SharedDependencies.captureLog + "_" + SharedDependencies.caseName;
			} else {
				name = SharedDependencies.finalResult + "_" + SharedDependencies.caseName;
			}
			File sourcePath = takesScreenshot.getScreenshotAs(OutputType.FILE);
			folderScreen = new File(SharedDependencies.folderTestCase + SharedDependencies.fileSystem.getSeparator() + "screenshots");
			folderScreen.mkdir();
			String path = folderScreen + SharedDependencies.fileSystem.getSeparator() + name + "_" + timeStamp + ".png";
			

			File destination = new File(path);

			if (name.contains("BQ") || name.contains("KO")) {
				try {
					String relativePath = path.split(SharedDependencies.dat)[1].substring(1);
					SharedDependencies.test.addScreenCaptureFromPath(relativePath);
				} catch (IOException e) {
					SharedDependencies.logger.log(Level.SEVERE, e.getMessage(), e);
				}
			}

			Files.copy(sourcePath, destination);

		} catch (Exception e) {
			SharedDependencies.logger.log(Level.SEVERE, e.getMessage(), e);
			SharedDependencies.logger.severe(e.getMessage());
		}
	}

	
	

  /**
   * Report failed steps
   * @param msg Message
   * @param log Type of log
   * @param wait Waiting time
   * @param status Statement of income
   * @param debugMsg Debug message
   * @throws Exception Error conditions to capture
   */
	private static void failedStepReport(String msg, String log, int wait,
		Status status, String debugMsg) throws Exception {

		rootFile.mkdirs();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String logPath = SharedDependencies.folderTestCase + SharedDependencies.fileSystem.getSeparator() + "Log_" + SharedDependencies.caseName + ".log";
		File logFile = new File(logPath);
		FileWriter fw = new FileWriter(logFile, true);

		if (SharedDependencies.level.equals("DEBUG")) {
			fw.write(df.format(new Date()) + " - " + "ERROR" + " - " + msg + "\r\n");
			fw.write(df.format(new Date()) + " - " + log + " - " + debugMsg + "\r\n");
		} else {
			fw.write(df.format(new Date()) + " - " + "ERROR" + " - " + msg + "\r\n");
			fw.write(df.format(new Date()) + " - " + "INFO" +
			" - " + "More info changing LOG_LEVEL in confing.properties file\r\n");
		}
		if (wait > 0) {
			fw.write(df.format(new Date()) + " - " + log + " - " + "Thread sleep " + wait + "ms" + "\r\n");
		}
		
		fw.close();

		if (SharedDependencies.level.equals("INFO")) {
			msg = StringEscapeUtils.escapeHtml4(msg);
			SharedDependencies.test.log(status, msg);
		} else {
			debugMsg = StringEscapeUtils.escapeHtml4(debugMsg);
			SharedDependencies.test.log(status, debugMsg);
		}
		if (!SharedDependencies.capScreenExempt && !debugMsg.contains("SQLException")) {
			String screenshotFrequency;
			TakesScreenshot takesScreenshot;
			screenshotFrequency = SharedDependencies.screenshot;
			takesScreenshot = (TakesScreenshot) SharedDependencies.driver;
			if (screenshotFrequency != null && takesScreenshot != null) {
				capScreenFrequency(screenshotFrequency, takesScreenshot);
			}
		}
	}

  /**
   * Reports steps whose result has been ok
   * @param msg Message
   * @param logginLevel Report level
   */
	public static void reportConsoleLogs(String msg, Level logginLevel) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                FileWriter fw = new FileWriter(SharedDependencies.consoleLogFile, true);
                if (logginLevel.equals(Level.SEVERE)) {
                    fw.write(df.format(new Date()) + " - " + "ERROR" + " - " + "\n" + msg + "\r\n");
                    SharedDependencies.logger.severe("\n" + msg);
                } else {
                    fw.write(df.format(new Date()) + " - " + "INFO" + " - " + "\n" + msg + "\r\n");
                    SharedDependencies.logger.info("\n" + msg);
                }
                fw.close();
            } catch (Exception e) {
                SharedDependencies.logger.severe("Error creating errors file");
            }
        }

  /**
   * Makes the final reports when the execution has been completed.
   * @param screenShot Configuration of screenshots
   */
  public static void finalReports(boolean screenShot) {
        try {
          
        } catch (Exception e) {
          Report.reportConsoleLogs(e.getMessage(), Level.SEVERE);
        }
      }
}
