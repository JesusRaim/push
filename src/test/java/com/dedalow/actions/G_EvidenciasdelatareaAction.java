package com.dedalow.actions;

import com.dedalow.report.Report;
import com.dedalow.SharedDependencies;


import com.dedalow.pages.DedalowPage;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.ArrayList;
import java.time.Duration;

import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;
import com.aventstack.extentreports.Status;
import static org.junit.jupiter.api.Assertions.assertTrue;
import io.restassured.response.Response;


public class G_EvidenciasdelatareaAction {
    
    
    
    private DedalowPage dedalowPage = new DedalowPage();


    


	public G_EvidenciasdelatareaAction FrontEndNavigate () throws Exception {
		
		
		SharedDependencies.driver.get(SharedDependencies.prop.getProperty("WEB_URL") + "");
		Report.frontScreenshotReportLog("Navigated to " + SharedDependencies.prop.getProperty("WEB_URL") + "", "INFO", 0, Status.PASS, false, "");
		return this;
	}


    /**
     * This method executes the steps in the TestActionModel
     * @param variableList List of project variables
     * @param parameterBinding Name of linked variables
     * @throws Exception Error conditions to capture
     */
    public void doG_EvidenciasdelatareaAction(HashMap<String, String> variableList, Map<String, String> parameterBinding) throws Exception {
		
		

		FrontEndNavigate();

		dedalowPage.FrontEndAction();
        
    }
}