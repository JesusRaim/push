package com.dedalow.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
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

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.dedalow.RunnerTest;
import com.dedalow.report.Report;
import com.dedalow.SharedDependencies;
import com.aventstack.extentreports.Status;

/**
 * Functions for different utilities
 */
public class Utils {

    private static boolean check = true;
    private static Logger logger = Logger.getLogger(RunnerTest.class.getName());
    private static Handler consoleHandler = initHandler();
    public static Properties prop;

    /**
     * Read config.properties file
     * @return Properties
     * @throws Exception Error conditions to capture
     */
    public static Properties getConfigProperties() throws Exception {
        prop = new Properties();
        prop.load(new FileInputStream("config.properties"));
        return prop;
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
        logger = Logger.getLogger(RunnerTest.class.getName());
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        return logger;
    }

    /**
     * Reads the features and scenarios the user wants to launch
     * @return String
     */
    public static String selectExecution() {
		String execution = "@7067smim";
        try {
            SharedDependencies.prop = getConfigProperties();
            if (!SharedDependencies.prop.getProperty("FEATURE").isEmpty() || !SharedDependencies.prop.getProperty("SCENARIO").isEmpty()) {
                execution = "";
                if (!SharedDependencies.prop.getProperty("FEATURE").isEmpty()) {
                    String[] features = SharedDependencies.prop.getProperty("FEATURE").split(", | |,");
                    for (int i = 0; i < features.length; i++) {
                        execution += '@' + features[i] + ',';
                    }
                }
                if (!SharedDependencies.prop.getProperty("SCENARIO").isEmpty()) {
                    String[] scenarios = SharedDependencies.prop.getProperty("SCENARIO").split(", | |,");
                    for (int i = 0; i < scenarios.length; i++) {
                        execution += '@' + scenarios[i] + "Scen,";
                    }
                }
            }
        } catch (Exception e) {
			logger.warning("No connection established with properties file");
            logger.info("All test will be executed");
		}
		return execution;
	}

    /**
     * Reads user defined arguments
     * @param options Scenario selection options
     * @return String[]
     */
    public static String[] getArgumentsOptions(String[] options) {
        for (int i = 0; i < options.length; i++) {
            switch (options[i]) {
                case "-g":
                    String optionFeature = options[i+1];
                    options[i+1] = "dedalow." + optionFeature.substring(0,1).toLowerCase() + optionFeature.substring(1);

                    break;
                case "-t":
                    String optionScenario = options[i+1];

                    if (optionScenario.contains("@")) {
                        options[i+1] = "@" + optionScenario.substring(1,2).toUpperCase() + optionScenario.substring(2);
                    } else {
                        options[i+1] = "@" + optionScenario.substring(0,1).toUpperCase() + optionScenario.substring(1);
                    }

                    String extractScen = optionScenario.substring(optionScenario.length()-4, optionScenario.length());
                    if (!extractScen.equals("Scen")) {
                        options[i+1] = options[i+1] + "Scen";
                    }

                    break;
            }
        }

        return options;
    }

    /**
     * We read the SCREENSHOT property from the config.properties and we convert everything to lowercase
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

        if (!options.contains(result)) throw new Exception ("Selected option for variable SCREENSHOT in config.properties file is not correct");

        return result;
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