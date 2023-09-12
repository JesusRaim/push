package com.dedalow.report;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Configuration for JSON reports
 */
public class JsonReport {
	protected String url;
	protected String devKey;
	protected String projectName;
	protected String testPlanName;
	protected String buildName;
	protected String platform;

	public ArrayList<TestSuite> aLtestSuites = new ArrayList<TestSuite>();
	public LinkedHashMap<String, TestSuite> testSuites = new LinkedHashMap<String, TestSuite>();
	public ArrayList<TestCase> alTestCases = new ArrayList<TestCase>();
}