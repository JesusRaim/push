package com.dedalow;

import com.dedalow.utils.*;
    import com.dedalow.utils.DriverInit;

import com.dedalow.SharedDependencies;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Logger;

import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.VncRecordingContainer.VncRecordingFormat;
import org.testcontainers.lifecycle.TestDescription;

/**
 * Configure web tests on docker
 */
public class ContainerManager {

    public static BrowserWebDriverContainer<?> container;
    private static Logger logger = Logger.getLogger("Log 1");
    public static String resources =  System.getProperty("user.dir") + SharedDependencies.fileSystem.getSeparator() + "resources";
    public static Launcher launcher = new Launcher();

	/**
	 * Initialize a container with the specific browser
   * @param testPath Location of reports
	 * @throws Exception Error conditions to capture
	 */
    public static void startContainer(File testPath) throws Exception {
        File folderScreen = new File(testPath + SharedDependencies.fileSystem.getSeparator() + "screenshots");
		container = new BrowserWebDriverContainer<>().withRecordingMode(
				BrowserWebDriverContainer.VncRecordingMode.RECORD_FAILING, folderScreen, VncRecordingFormat.MP4);
        container.withFileSystemBind(SharedDependencies.folderDownloads.getAbsolutePath(), "/home/seluser/Downloads");
        container.withCapabilities(SharedDependencies.driverInit.driverSelector());
        logger.info("Starting docker container..");
        container.start();
    }

    /**
     * Stops the docker container used for the execution of the test
     */
    public static void stopContainer() {
		if (null != container && container.isRunning()) {

			Optional<Throwable> optional = "OK".equalsIgnoreCase(SharedDependencies.finalResult)
                ? Optional.empty() : Optional.of(new AssertionError());

			container.afterTest(new TestDescription() {

				@Override
				public String getTestId() {
					return getFilesystemFriendlyName();
				}

				@Override
				public String getFilesystemFriendlyName() {
					return SharedDependencies.caseName;
				}

			}, optional);

			logger.info("Stop docker container..");
			container.stop();
		}
	}

    /**
     * Check before execution that the docker service is available.
     */
    public static void checkDocker() {
        try {
            Runtime.getRuntime().exec("docker --version");
        } catch (IOException e) {
            SharedDependencies.logger.severe("Cannot run program \"docker\": CreateProcess error=2, System can not find docker installed.");
        }
    }

}