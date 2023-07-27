package com.dedalow.feature;

import com.dedalow.SharedDependencies;
import com.dedalow.report.*;
import com.dedalow.actions.*;
import com.dedalow.utils.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.Arrays;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.TimeoutException;
import com.google.common.base.Throwables;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.And;
import cucumber.api.java.en.But;
import cucumber.api.java.en.Then;



public class StepDef {

    
    private G_EvidenciasdelatareaAction g_EvidenciasdelatareaAction = g_EvidenciasdelatareaAction = new G_EvidenciasdelatareaAction();
		

    

    /**
     * Actions before Test execution
     * @param scenario Scenario to be executed
     */
    @Before({"@Feature"})
    public void before(Scenario scenario)  {
        try {
            setUp(scenario.getName());
			SharedDependencies.setUpEnvironment("MAIN_CONTEXT");
            
        } catch (Exception e) {
            SharedDependencies.finalResult = "BQ";
            SharedDependencies.parentTest = SharedDependencies.test.createNode("<b>Error in project configuration</b>");
            Report.reportLog(e.getMessage(), SharedDependencies.level, 0, Status.FAIL, true, Throwables.getStackTraceAsString(e));
            Report.reportConsoleLogs(e.getMessage(), Level.SEVERE);
        }
    }

    /**
     * Actions after Test execution
     */
    @After({"@Feature"})
    public void after() {
        SharedDependencies.logger.info("Result on " + SharedDependencies.scenarioName + ":" + SharedDependencies.finalResult);
        SharedDependencies.results.add(0, SharedDependencies.finalResult);
        Report.addResults();
        SharedDependencies.initialize.flush();
        DriverInit.clearWebDrivers();
        Report.finalReports();
    }

    
    @Given("^Evidencias de la tarea$")
    public void TestAction() throws Exception {
        try {
            

            

            SharedDependencies.parentTest = SharedDependencies.test.createNode("<b>Given</b> Evidencias de la tarea");
            Report.reportLog("Evidencias de la tarea testAction is going to start", "INFO", 0, Status.PASS, false, "");
            		
			g_EvidenciasdelatareaAction.doG_EvidenciasdelatareaAction(null, null);
			Report.reportLog("Action G_EvidenciasdelatareaAction finished","INFO", 0, Status.PASS, false, "");
        } catch (TimeoutException te) {
            SharedDependencies.finalResult = "KO";
            Report.reportLog(te.getMessage(), SharedDependencies.level, 0, Status.FAIL, true, Throwables.getStackTraceAsString(te));
            throw new Exception(te.getMessage());
        } catch (Exception | AssertionError e) {
            SharedDependencies.finalResult = "KO";
            Report.reportLog(e.getMessage(), SharedDependencies.level, 0, Status.FAIL, true, Throwables.getStackTraceAsString(e));
            Report.reportConsoleLogs(e.getMessage(), Level.SEVERE);
            throw new Exception(e.getMessage());
        }
    }

	

    /**
     * Scenario configuration
     * @param scenarioName Scenario name
     * @throws Exception Error conditions to capture
     */
    public void setUp(String scenarioName) throws Exception {
      SharedDependencies.defaultValues("Feature", scenarioName);
      SharedDependencies.initialize = new ExtentHtml(scenarioName);
      SharedDependencies.test = SharedDependencies.initialize.getTest();
	}


}