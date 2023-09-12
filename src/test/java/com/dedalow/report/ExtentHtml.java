package com.dedalow.report;

import com.dedalow.SharedDependencies;

import java.util.logging.Logger;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import com.dedalow.Launcher;
import com.dedalow.utils.*;

  /**
   * HTML Report
   */
	public class ExtentHtml {
		public static String resources =  System.getProperty("user.dir") + SharedDependencies.fileSystem.getSeparator() + "resources";
		public static Launcher launcher = new Launcher();
		public static ExtentReports extent = new ExtentReports();
		public static boolean isNotInitializated = false;
		public static ExtentTest extentSuiteName;
		private static ExtentHtmlReporter htmlReporter;

      /**
       * Gets the test.
       * @return ExtentTest
       */
			public ExtentTest getTest() {
				return SharedDependencies.test;
			}

      /**
       * Writes test information from the started reporters to their output view
       */
			public void flush() {
				extent.flush();
			}

    /**
     * Initializes the HTML construction
     * @param caseName Test case name
     */
		public ExtentHtml (String caseName) {
			if (!isNotInitializated) {
				createTest();
				isNotInitializated = true;
			}
			try {
				SharedDependencies.test = extent.createTest(caseName, SharedDependencies.reportDescription.replace("\n", "<br>"));
				SharedDependencies.test.assignCategory(SharedDependencies.suiteName);
			} catch (IllegalArgumentException | SecurityException e) {
				SharedDependencies.logger.severe(e.getMessage());
			}


		}

    /**
     * In the HTML, creates the section of the test where its report will be added.
     */
		public static void createTest() {
			LoggerContext context = (LoggerContext) LogManager.getContext(false);
			File file = new File(resources + SharedDependencies.fileSystem.getSeparator() + "extentReports" + SharedDependencies.fileSystem.getSeparator() +"log4j.properties");
			context.setConfigLocation(file.toURI());
			htmlReporter = new ExtentHtmlReporter(SharedDependencies.folderLogs + SharedDependencies.fileSystem.getSeparator() + "7324tayzResults.html");
			htmlReporter.loadXMLConfig(resources + SharedDependencies.fileSystem.getSeparator()+ "extentReports" + SharedDependencies.fileSystem.getSeparator() + "extent_config.xml");
			customizeHtml(htmlReporter);
			isNotInitializated = true;
		}

    /**
     * Customize HTML
     *  @param htmlReporter HTML report
     */
		private static void customizeHtml(ExtentHtmlReporter htmlReporter) {
      String username = System.getProperty("user.name");
      String os = System.getProperty("os.name");
      String arch = System.getProperty("os.arch");
      String javaVersion = System.getProperty("java.specification.version");

      extent.setSystemInfo("Tester name", username);
      extent.setSystemInfo("Operative System", os);
      extent.setSystemInfo("Java Version", javaVersion);
      extent.attachReporter(htmlReporter);
		}
	}