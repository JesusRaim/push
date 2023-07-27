# PROJECT: 7068cuvv
    This project contains automated tests based on a Java Selenium, Gherkin-Cucucmber and Maven architecture.
	In this project we can find features like:
	Logs generation, automated screenshot creation and testlink integration.

### Requirements
- In order to run this project from an IDE, the JAVA Virtual Machine or a JAVA code interpreter should be installed.

- In case of JAR execution only JAVA code interprester would be neccesary.

- In case of using maven to run the execution, MAVEN should be installed.

- The minimum Java version required is 1.8. Compatibility with higher versions is not guaranteed.

### How configure tests
By default, *config.properties* file it's automatcally configured by Dedalow. If a modification of config would be neccessary, it can be done as follows:

**	Configuring Driver Options:**

Below, some of the available parameters when executing a test using Chrome (we can get the whole list using the command chromedriver –help) :

	- start-maximized: Opens Chrome in maximize mode
	- incognito: Opens Chrome in incognito mode
	- headless: Opens Chrome in headless mode
	- disable-extensions: Disables existing extensions on Chrome browser
	- disable-popup-blocking: Disables pop-ups displayed on Chrome browser
	- make-default-browser: Makes Chrome the default browser
	- version: Prints chrome browser version
	- disable-infobars: Prevents Chrome from displaying the notification 'Chrome is being controlled by automated software
	- window-size: To stablish desired window size

For using one of this parameters:

	WebDriver.DRIVER_OPTIONS = --no-sandbox

For using multiple parameters, you should separate all parameters with comma and space:

	WebDriver.DRIVER_OPTIONS = --no-sandbox, --window-size=1920,1080, --incognito

**Configuring Browser:**

We can choose between *Chrome, Firefox, Edge or Explorer* to run our Test Plan.

	Webdriver.BROWSER = Chrome

We can select the driver version that we want, writting the number version.

	Webdriver.DRIVER_VERSION = 95.0.4638.69

#####If you choose *Explorer* you have to do a few changes on Internet Explorer to run our Test Cases:

We have to maximize internet explorer's zoom:

	Settings->Zoom->100%

Then, we have to change Internet Explorer's security:

	Settings->Internet options->Security.
	Now we have to activate the check "Enable Protected Mode" in each zones (Internet, Local intranet, Trued Sites, Restricted Sites)

**Configuring Home page**

We can change the home page URL for your Test Plan

    WEB_URL = https://dedalow.com/

**Configuring download folder path:**

We can select any path, but if we don't change it, it will select it by default

	FOLDER_DOWNLOAD = default

**Configuring the screenshot**

We have 3 options for taking screenshots: Always, Only on error and Never. Any other value will generate an error and tests will not be executed. By default, Always will be selected

    Application example: SCREENSHOT = Always.

**Logger level:**

We can choose between *INFO* or *DEBUG* options, depending on detailed information we want to show

	LOG_LEVEL = INFO

**Timeout**

We can modify timeout value for all Test Plan

	WEB.TIMEOUT = 20

**Backend Home page**

We can change backend's home page URL for your api tests

    BACKEND_ENDPOINT = https://dedalow.com/

**Backend Timeout**

We can modify backend's timeout value for all api tests

    BACKEND_TIMEOUT = 20

**Configurating Testlink:**

Here we have to set our Testlink URL

    Testlink.TESTLINK = https://steps.everis.com/testlink

Here we have to set our Testlink APIKEY

    Testlink.APIKEY = xxxxx

Here we have to set our Testlink projectName

    Testlink.PROJECT_NAME_TESTLINK = projectName

Here we have to set our Testlink buildName

    Testlink.BUILD_NAME = buildName

Here we have to set our Testlink patform

    Testlink.PLATFORM_NAME = chrome

We have one for each suite to set testlink suite name configuration

    Testlink.suite.nameSuite = nameSuite

We have one for each testCase to set testlink testCase name configuration

    Testlink.testCase.Test_caseName = caseName

### How execute using JAVA/CUCUMBER

**Config.properties configuration**

Selecting the only a scenario that we want to execute (scenarioName = name of the Scenario without spaces):

    SCENARIO = scenarioName

Selecting various Scenarios that we want to execute (Separation using space or coma):

    SCENARIO = scenarioName1 scenarioName2

Selecting one feature from the Test Plan (featureName = name of the Feature without spaces):

    FEATURE = featureName

Selecting various Suites from the Test Plan (Separation using space or coma)

    FEATURE = featureName1, featureName2

Selecting all Test Plan, set blank both properties

    FEATURE =
    SCENARIO =

**Run jar file reading config.properties file configuration, execute this command from the target folder**

	java -jar 7068cuvvExe.jar

### How execute using MAVEN

**Run the entire project (All features included with no parameters), execute this command from the root folder**

	mvn test

**Run using parametrized execution, execute these commands from the root folder**

Run by selecting the only Scenario that we want to execute (scenarioName = name of the Scenario without spaces)

    mvn test -Dcucumber.options="--tags @scenarioNameScen"

Run by selecting various Scenarios that we want to execute

    mvn test -Dcucumber.options="--tags @scenarioName1Scen,@scenarioName2Scen,@scenarioName3Scen"

Run only one Feature from the Test Plan (featureName = name of the Feature without spaces)

    mvn test -Dcucumber.options="--tags @featureName"

Run by selecting various Features from the Test Plan

    mvn test -Dcucumber.options="--tags @featureName1,@featureName2"

### FAQ
Test is not executed because the driver is not updated and no message is displayed

	Update the driver and try again

I don’t know how to open the console in the unzipped folder

	Hold Shift + Right Click on the folder you want it opened on, and click on "open command window here". That should do the trick!
	OR
	You can also type cmd into the Windows File Explorer address bar (use Ctrl+L to focus the address bar) and then press Enter to open the shell.


## This project has follow Features & Scenarios available
**Feature**
- Scenario
