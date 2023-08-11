package com.dedalow.report;

import com.dedalow.SharedDependencies;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Configure Feature for reporting in TestLink
 */
public class TestSuite {
	public String name;
	public String testSuiteTL;
	public Map<String, TestCase> testCases = new HashMap();

  /**
   * Gets the name of the referenced Feature with the TestLink server
   * @param name TestSuite name
   */
	public TestSuite(String name) {
		this.name = name;
		this.testSuiteTL = SharedDependencies.prop.getProperty("Testlink.feature." + this.name);
	}
}