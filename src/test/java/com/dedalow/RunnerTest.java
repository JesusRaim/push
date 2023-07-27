package com.dedalow;

import com.dedalow.utils.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.net.InetAddress;
import java.net.MalformedURLException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dedalow.report.Report;
import com.dedalow.SharedDependencies;
import cucumber.api.cli.Main;
import org.junit.runner.RunWith;
import cucumber.api.junit.Cucumber;
import cucumber.api.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "resources/features", monochrome = true, glue = {
			"com.dedalow.feature",
			 })
/**
 * Main class of the project
 */
public class RunnerTest {

	public static byte executionStatus;

  /**
   * Inits the project execution
   * @param args Execution arguments
   */
	public static void main(String[] args) {

		if (args.length == 0) {
			args = new String[]{ "-s", "-m",
				"-p", "pretty",
				"-p", "html:" + SharedDependencies.folderLogs + "/Cucumber/ReportHTML",
				"-p", "json:" + SharedDependencies.folderLogs + "/Cucumber/ReportJSON.json",
				"-p", "junit:" + SharedDependencies.folderLogs + "/Cucumber/ReportXML.xml",
				"resources/features",
				"-g", "com.dedalow.feature",
				"-t", Utils.selectExecution()
			};
		} else {
			args = Utils.getArgumentsOptions(args);
		}

		try {
			executionStatus = Main.run(args, Thread.currentThread().getContextClassLoader());
		} catch (IOException e) {
			Report.reportConsoleLogs("Error runnig test: " + e.getMessage(), Level.SEVERE);
		}

		/**
		 * Make a complete report of the project execution.
		 * TestLink, Excel
		 */
		executionReport();
		System.exit(executionStatus);
	}

  /**
   * Execute the last reports, after project execution
   */
	public static void executionReport() {
		SharedDependencies.logger.info("Logs can be consulted at the following dir: " + SharedDependencies.root);
		SharedDependencies.logger.info("******************************** FEATURES execution finished ********************************");

	}
}