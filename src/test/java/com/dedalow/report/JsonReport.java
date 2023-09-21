package com.dedalow.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JsonReport {
	public String url;
	public String devKey;
	public String projectName;
	public String testPlanName;
	public String buildName;
	public String platform;
	
	public ArrayList<TestSuite> aLtestSuites = new ArrayList<TestSuite>();
	public Map<String, TestSuite> testSuites = new HashMap();
	public ArrayList<TestCase> alTestCases = new ArrayList<TestCase>();
}