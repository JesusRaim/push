package com.dedalow.pages;

import java.util.logging.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.aventstack.extentreports.Status;
import com.dedalow.report.Report;
import com.dedalow.utils.Utils;
import com.dedalow.SharedDependencies;
import static org.junit.jupiter.api.Assertions.assertTrue;



/**
 * Class that encapsulates the Page DedalowPage
 */
public class DedalowPage {

    
	public DedalowPage FrontEndAction() throws Exception {
		
		By FrontEndAction = By.xpath("//button[@id='login']");
	
		By SingleParameterValue = By.xpath("//input[@id='id_username']");
		
		SharedDependencies.driver.findElement(SingleParameterValue).clear();
		SharedDependencies.driver.findElement(SingleParameterValue).sendKeys("jperepei");
		Report.frontScreenshotReportLog("Typed " + "jperepei in SingleParameterValue", "INFO", 0, Status.PASS, false, "");

		
		new WebDriverWait(SharedDependencies.driver, Duration.ofSeconds(SharedDependencies.timeout)).until(ExpectedConditions.elementToBeClickable(FrontEndAction));
		Report.reportLog("Condition FrontEndAction isClickable finished", "ASYNCHRONOUS", 0);
		
		SharedDependencies.driver.findElement(FrontEndAction).click();
		Report.frontScreenshotReportLog("Clicked FrontEndAction", "INFO", 0, Status.PASS, false, "");
		return this;
	}

    
}