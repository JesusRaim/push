package com.dedalow.suite1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.logging.Level;
import java.time.Duration;


import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Throwables;
import com.dedalow.utils.*;
import com.dedalow.report.ExtentHtml;
import com.dedalow.report.Report;
import com.dedalow.Launcher;
import com.dedalow.SharedDependencies;

import com.aventstack.extentreports.Status;

import com.dedalow.actions.GivenaAction;



public class Test_TestCaseModel {

    private static String reportDescription = "";
    
	private static GivenaAction givenaAction;

    /**
     * Prepares the test for execution
     * @throws Exception Error conditions to capture
     */
    @BeforeEach
    public void beforeEach() throws Exception {
    try {
        setUp();
		SharedDependencies.setUpEnvironment("MAIN_CONTEXT");
        
    } catch (AssertionError | Exception e) {
        Report.reportConsoleLogs(e.getMessage(), Level.SEVERE);
        SharedDependencies.finalResult = "BQ";
        Report.reportLog(e.getMessage(), SharedDependencies.level, 0, Status.FAIL, true, Throwables.getStackTraceAsString(e));
        throw new Exception(e);
        }
    }

    

    @Test
	@DisplayName("Test_TestCaseModel")
	public void test() throws Exception {
        try {
            
            
            
			Report.reportLog("Start of execution", "INFO", 0, Status.PASS, false, "");

			givenaAction.doGivenaAction(null, null);
			Report.reportLog("Action GivenaAction finished","INFO", 0, Status.PASS, false, "");

        } catch (AssertionError | Exception e) {
            Report.reportConsoleLogs(e.getMessage(), Level.SEVERE);
            if (SharedDependencies.finalResult != "BQ") {
				SharedDependencies.finalResult = "KO";
			}
            Report.reportLog(e.getMessage(), SharedDependencies.level, 0, Status.FAIL, true, Throwables.getStackTraceAsString(e));
            throw new Exception(e);
        }
    }

    

    /**
     * Performs the final reports and closes the objects that have been used for the test.
     */
    @AfterEach
    public void afterEach()  {
        boolean screenShot = true;
        
        if (SharedDependencies.finalResult == "OK") {
            Report.reportLog("Result on Test_TestCaseModel: " + SharedDependencies.finalResult, "INFO", 0, Status.PASS, false, "");
        } else {
            Report.reportLog("Result on Test_TestCaseModel: " + SharedDependencies.finalResult, "INFO", 0, Status.FAIL, false, "");
        }
        SharedDependencies.logger.info("Result on Test_TestCaseModel: " + SharedDependencies.finalResult);
        SharedDependencies.initialize.flush();
        DriverInit.clearWebDrivers();
        SharedDependencies.results.add(SharedDependencies.finalResult);
        Report.addResults();
        Report.finalReports(screenShot);
        SharedDependencies.initialize.flush();
    }

    /**
     * Assign initial values to variables before execution
     * @throws Exception Error conditions to capture
     */
    
    public static void setUp() throws Exception {
        try {
            SharedDependencies.init();
            SharedDependencies.screenshot = SharedDependencies.utils.configScreenshot();
        SharedDependencies.timeout = Integer.parseInt(SharedDependencies.prop.getProperty("WEB_TIMEOUT"));
            SharedDependencies.defaultValues("Suite1", "Test_TestCaseModel", reportDescription);
            SharedDependencies.initialize = new ExtentHtml("Test_TestCaseModel");
            SharedDependencies.test = SharedDependencies.initialize.getTest();
			classInitialization();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    /**
     * Initializes the classes required for execution
     */
    public static void classInitialization() {
    	givenaAction = new GivenaAction();
		
  	}

}