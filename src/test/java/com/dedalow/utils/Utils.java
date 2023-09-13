package com.dedalow.utils;

import java.io.FileInputStream;
import java.io.IOException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.ErrorManager;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.text.SimpleDateFormat;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.dedalow.Launcher;
import com.dedalow.report.Report;
import com.dedalow.SharedDependencies;
import com.aventstack.extentreports.Status;

/**
 * Functions for different utilities
 */
public class Utils {

    private static Logger logger = Logger.getLogger(Launcher.class.getName());
    private static Handler consoleHandler = initHandler();
    public static Properties prop;

    /**
     * Read config.properties file
     * @return prop
     * @throws Exception Error conditions to capture
     */
    public static Properties getConfigProperties() throws Exception {
        try {
            prop = new Properties();
            prop.load(new FileInputStream("config.properties"));
            return prop;
        } catch (Exception e) {
            throw new Exception ("Can not find config.properties file");
        }
    }

    /**
     * Check if the element is enabled
     * @param element Element identifier
     * @return boolean
     */
    public static boolean isElementEnabled(WebElement element) {
        turnOffImplicitWaits();
        boolean result = element.isEnabled();
        turnOnImplicitWaits();
        return result;
    }

    /**
     * Deactivate implicit waits
     */
    private static void turnOffImplicitWaits() {
        SharedDependencies.driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    }

    /**
     * Activate implicit waits
     */
    private static void turnOnImplicitWaits() {
        SharedDependencies.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    /**
     * Reads a json file
     * @param JSONPath JSONPath to read result from a json
     * @return String
     * @throws IOException Error conditions to capture
     */
    public static String generateJSONBody (String JSONPath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(JSONPath)));
    }

    /**
     * Gets the handler from the browser
     * @return Handler
     */
    public static Handler initHandler() {
        return new Handler() {
            @Override
            public void publish(LogRecord record) {
                if (getFormatter() == null) {
                    setFormatter(new SimpleFormatter());
                }

                try {
                    String message = getFormatter().format(record);
                    if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
                        System.err.write(message.getBytes());
                    } else {
                        System.out.write(message.getBytes());
                    }
                } catch (Exception exception) {
                    reportError(null, exception, ErrorManager.FORMAT_FAILURE);
                }
            }

            @Override
            public void close() throws SecurityException {
            }

            @Override
            public void flush() {
            }
        };
    }

    /**
     * Configure the logger
     * @return Logger
     */
    public static Logger logger() {
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }
        logger = Logger.getLogger(Launcher.class.getName());
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        return logger;
    }

    /**
     * Reads and convert to lower cases the SCREENSHOT property of the config.properties file
     * @return String
     * @throws Exception Error conditions to capture
     */
    public String configScreenshot() throws Exception {
        List<String> options = Arrays.asList("always", "only", "never");
        String screenshot = SharedDependencies.prop.getProperty("SCREENSHOT");
        int spacePosition = screenshot.indexOf(" ");
        String result = screenshot.toLowerCase();

        if (spacePosition > 0) {
            result = result.substring(0, spacePosition);
        }

        if (!options.contains(result)) throw new Exception ("The option of the variable SCREENSHOT in the file config.properties is not correct. "
				+ "It must contain one of these options: Always, Only on error or Never");

        return result;
    }

    /**
     * Gets the TestCases to launch
     * @return ArrayList with the names of the TestCase to be launched
     * @throws Exception Error conditions to capture
     */
    public static ArrayList<String> getTestCasesSelected() throws Exception {
    ArrayList<String> testCasesSelected = new ArrayList<String>();
    if (!SharedDependencies.prop.getProperty("TESTSUITES").isEmpty() || !SharedDependencies.prop.getProperty("TESTCASES").isEmpty()) {

        if (!SharedDependencies.prop.getProperty("TESTSUITES").isEmpty()) {
            String[] testSuites = SharedDependencies.prop.getProperty("TESTSUITES").split(", | |,");
            for (String suite : testSuites) {
                String nameSuite = suite.substring(0, 1).toLowerCase() + suite.substring(1);
                testCasesSelected = getTestCases(nameSuite, testCasesSelected);
            }
        }

        if(!SharedDependencies.prop.getProperty("TESTCASES").isEmpty()) {
            String[] testCases = SharedDependencies.prop.getProperty("TESTCASES").split(", | |,");
            for (String testCase : testCases) {
                ArrayList<String> listTestCases = new ArrayList<String>();
                boolean testCaseExist = false;
                String nameCase = testCase.substring(0, 1).toUpperCase() + testCase.substring(1);

                listTestCases = getTestCases("complete", listTestCases);
                for (String listCase : listTestCases) {
                    if (listCase.matches(".+Test_" + nameCase)) {
                        testCasesSelected.add(listCase);
                        testCaseExist = true;
                    }
                }
                if (!testCaseExist) {
                    throw new Exception ("The TestCase " + nameCase + " does not exist");
                }
            }
        }

    } else {
        testCasesSelected = getTestCases("complete", testCasesSelected);
    }

    return testCasesSelected;
}

    /**
     * Gets the names of the TestCases
     * @param option TestCases selection option
     * @param testCases List of TestCases of the project
     * @return ArrayList with the names of the TestCases
     * @throws Exception Error conditions to capture
     */
    public static ArrayList<String> getTestCases(String option, ArrayList<String> testCases) throws Exception {
        switch (option) {
            case "asdasd":
            	testCases.add("com.dedalow.asdasd.Test_TestCaseModel");
			
            break;
			
            case "complete":
                	testCases.add("com.dedalow.asdasd.Test_TestCaseModel");
			
                break;
            default:
                throw new Exception ("The TestSuite " + option + " does not exist");
        }

        return testCases;
    }

    /**
     * Check if the file has been downloaded correctly.
     * @param path Download directory
     * @param directoryLength File size of the directory
     * @param directoryPath File directory
     * @return Boolean Check the directory to perform a download
     * @throws Exception Error conditions to capture
     */
    public static Boolean checkDownload(String path,
		Integer directoryLength, File directoryPath) throws Exception {
		long start = System.currentTimeMillis();
		long end = start + Long.parseLong(SharedDependencies.prop.getProperty("WEB_TIMEOUT"))*1000;

		while (System.currentTimeMillis() <= end) {
			if (directoryLength != directoryPath.listFiles().length) {
				if (!isDownloadInProgress(directoryPath)) {
					Report.reportLog("File downloaded in " + path, "INFO", 0, Status.PASS, false, "");
					return true;
				}
			}
		}

		Report.reportLog("Reached timeOut. Specify more time in config.properties file", "INFO", 0,
				Status.FAIL, false, "");

		return false;
	}

    /**
     * Checks if a download is in progress
     * @param directoryPath Download directory location
     * @return Boolean
     */
    public static Boolean isDownloadInProgress (File directoryPath) {
        String [] partialDownloadExtensions = {".crdownload", ".tmp", ".part"};
        File[] files  = directoryPath.listFiles();
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
		try {
            String lastFile = files[0].toString();
            return Arrays.stream(partialDownloadExtensions).anyMatch(extension -> lastFile.endsWith(extension));
		} catch (IndexOutOfBoundsException e) {
            return true;
		}
    }

    /**
     * Set the type of encoding
     */
    public static void setEncoding() {
		try {
			System.setProperty("file.encoding", "UTF-8");
			Field charset = Charset.class.getDeclaredField("defaultCharset");
			charset.setAccessible(true);
			charset.set(null, null);
		} catch (Exception e) {
			Report.reportConsoleLogs(e.getMessage(), Level.SEVERE);
		}
	}

    /**
     * Gets the values stored in variables using ${}
     * @param value Value
     * @param variableList List of variables defined in the project
     * @param Variables Variables used at this time
     * @param type File type
     * @return String
     */
    public static String getVariables(String value, String[] variableList, HashMap<String, String> Variables, String type) {
      for (int i = 0; i < variableList.length; i++) {
        if (type.equals("json") && !value.contains("\"${" + variableList[i] + "}\"")) {
          value = value.replace("${" + variableList[i] + "}", "\"" + Variables.get(variableList[i]) + "\"");
        } else {
          value = value.replace("${" + variableList[i] + "}", Variables.get(variableList[i]));
        }
      }
      return value;
    }

}