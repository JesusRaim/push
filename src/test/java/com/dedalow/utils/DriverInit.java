package com.dedalow.utils;

import com.dedalow.SharedDependencies;
import com.dedalow.report.Report;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.Capabilities;
import java.util.concurrent.TimeUnit;
import com.dedalow.ContainerManager;

public class DriverInit {
    public String driverType;
    public String driverPath;
    public String[] driverOptions;
    public String pathFolderDownloads;
    public int timeOut;
    public List<String> listNamesChrome = Arrays.asList("testcontainerchrome", "testcontainergooglechrome");
    public List<String> listNamesFirefox = Arrays.asList("testcontainerfirefox", "testcontainermozilla",
        "testcontainermozillafirefox", "testcontainergecko");

    
    /**
     * Initialize a container with the driver that has been defined
     * @param nameDriver Driver identifier
     * @param testPath Location of reports
     * @return WebDriver
     * @throws Exception Error conditions to capture
     */
    public WebDriver initDockerDriver(String nameDriver, File testPath) throws Exception {
        try {
            if (SharedDependencies.contextsDriver.get(nameDriver) != null) {
                SharedDependencies.driver = SharedDependencies.contextsDriver.get(nameDriver);
            }
            else {
                ContainerManager.startContainer(testPath);
                SharedDependencies.driver = ContainerManager.container.getWebDriver();
                SharedDependencies.driver.manage().timeouts().implicitlyWait(Integer.parseInt(SharedDependencies.prop.getProperty("WEB_TIMEOUT")), TimeUnit.SECONDS);
                SharedDependencies.contextsDriver.put(nameDriver, SharedDependencies.driver);
            }
            return SharedDependencies.driver;
        } catch (Exception e) {
            throw new Exception ("Error starting docker container. " + e.getMessage());
        }
    }

    /**
	 * Call the corresponding function depending on the selected driver
	 * @return Capabilities
	 * @throws Exception Error conditions to capture
	 */
	public Capabilities driverSelector() throws Exception {
		driverType = SharedDependencies.prop.getProperty("WebDriver.BROWSER").toLowerCase().replace(" ", "");
		driverOptions = SharedDependencies.prop.getProperty("WebDriver.DRIVER_OPTIONS").split(", ");
		if (listNamesChrome.contains(driverType)) {
			return dockerChromeOptions();
		} else if (listNamesFirefox.contains(driverType)) {
			return dockerFirefoxOptions();
		} else {
			SharedDependencies.logger.info(
					"The indicated options does not match the available TestContainer [Chrome, Firefox], it is launched by default on TestContainer Chrome");
			return dockerChromeOptions();
		}
	}

    /**
	 * Configure options for the chrome driver
	 * @return ChromeOptions
	 * @throws Exception Error conditions to capture
	 */
	public ChromeOptions dockerChromeOptions() throws Exception {
		try {
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.default_directory", SharedDependencies.folderDownloads.getAbsolutePath());
			ChromeOptions options = new ChromeOptions();
			if (!SharedDependencies.prop.getProperty("WebDriver.DRIVER_OPTIONS").isEmpty()) {
				options.addArguments(driverOptions);
			}
			options.addArguments("--disable-dev-shm-usage");
			options.addArguments("--allow-file-access-from-files");
			options.setExperimentalOption("prefs", chromePrefs);
			return options;
		} catch (Exception e) {
			throw new Exception("Error configuring Chrome. " + e.getMessage());
		}
	}

    /**
	 * Configure options for the firefox driver
	 * @return FirefoxOptions
	 * @throws Exception Error conditions to capture
	 */
    public FirefoxOptions dockerFirefoxOptions() throws Exception {
		try {
			driverOptions = SharedDependencies.prop.getProperty("WebDriver.DRIVER_OPTIONS").split(", ");
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("browser.download.manager.useWindow", false);
			profile.setPreference("browser.download.dir", SharedDependencies.folderDownloads.getAbsolutePath());
			profile.setPreference("browser.download.manager.showAlertOnComplete", true);
			profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
					"text/plain, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/octet-stream,"
							+ "application/binary, text/csv, application/csv, application/excel,"
							+ "text/comma-separated-values, text/xml, application/xml");
			profile.setPreference("browser.download.folderList", 2);
			FirefoxOptions optionsFirefox = new FirefoxOptions();
			if (!SharedDependencies.prop.getProperty("WebDriver.DRIVER_OPTIONS").isEmpty()) {
				optionsFirefox.addArguments(driverOptions);
			}
			optionsFirefox.setProfile(profile);
			return optionsFirefox;
		} catch (Exception e) {
			throw new Exception("Error configuring Firefox. " + e.getMessage());
		}
	}

  /**
   * Closes drivers that have been opened during TestCase execution.
   */
  public static void clearWebDrivers() {
    try {
        String driverType = SharedDependencies.prop.getProperty("WebDriver.BROWSER").toLowerCase().replace(" ", "");
        List<String> listNamesFirefox = Arrays.asList("testcontainerfirefox", "testcontainermozilla", "testcontainermozillafirefox", "testcontainergecko");

        for (Map.Entry<String, WebDriver> context : SharedDependencies.contextsDriver.entrySet()) {
            if (!listNamesFirefox.contains(driverType)) {
                context.getValue().close();
            }
            context.getValue().quit();
        }
        SharedDependencies.contextsDriver.clear();
    } catch (Exception e) {
        Report.reportConsoleLogs(e.getMessage(), Level.SEVERE);
    }
  }

}