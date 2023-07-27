package com.dedalow.report;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.dedalow.utils.Utils;
import com.dedalow.report.Report;
import com.dedalow.SharedDependencies;

/**
 * Configure TestCase for reporting in TestLink
 */
public class TestCase {
	public String name;
    public ArrayList<String> result;
    public String externalId;
    public ArrayList<String> notes;
    public ArrayList<String> screenShootsPaths;

    /**
     * Gets the name of the reference TestSuite with TestLink server with screenshots
     * @param name TestCase name
     * @param result TestCase Results
     * @param notes Notes
     * @param screenShootsPaths List of screenshots
     */
    public TestCase(String name, ArrayList<String> result, ArrayList<String> notes, ArrayList<String> screenShootsPaths) {
        this.name = name;
        this.result = result;
        this.externalId = SharedDependencies.prop.getProperty("Testlink.testCase." + this.name);
        this.notes = notes;
        this.screenShootsPaths = screenShootsPaths;
    }

    /**
     * Gets the name of the referenced TestCase with the TestLink server
     * @param name TestCase name
     * @param result TestCase Results
     */
    public TestCase(String name, ArrayList<String> result) {
    	this(name, result, null, null);
    }
}