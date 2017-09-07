package com.ilanbenichou.filesmanager.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ilanbenichou.filesmanager.exception.WrappedRuntimeException;

public final class PropertiesLoader {

	private static final Logger LOGGER = Logger.getLogger(PropertiesLoader.class);

	private static final Path PROPERTIES_FILE_PATH = Paths.get("./config/files-manager.properties");

	private static PropertiesLoader instance;

	private FmProperties properties;

	private PropertiesLoader() {

		File propertiesFile = new File(PropertiesLoader.PROPERTIES_FILE_PATH.toString());

		if (!propertiesFile.exists()) {
			throw WrappedRuntimeException.wrap(String.format("The properties file [%s] doesn't exist !", propertiesFile.getAbsolutePath()));
		}

		try (InputStream input = new FileInputStream(propertiesFile)) {

			Properties props = new Properties();

			props.load(input);

			this.properties = new FmProperties();

			this.properties.setWaitingTimeToWriteIndexFileSec(PropertiesLoader.buildStrictlyPositiveIntProperty(props, "waiting_time_to_write_index_file_sec", 60));

			this.properties.setReportsDirectoryPath(PropertiesLoader.buildPathProperty(props, "reports_directory_path", Paths.get("report"), true));

			this.properties.setReportsResourcesDirectoryPath(PropertiesLoader.buildPathProperty(props, "reports_resources_directory_path", Paths.get("../resource"), false));

		} catch (final IOException ioException) {
			throw WrappedRuntimeException.wrap(String.format("An error occurred while reading properties file [%s] !", PropertiesLoader.PROPERTIES_FILE_PATH), ioException);
		}

	}

	public static PropertiesLoader getInstance() {

		if (PropertiesLoader.instance == null) {
			PropertiesLoader.instance = new PropertiesLoader();
		}

		return PropertiesLoader.instance;

	}

	public void printProperties() {

		PropertiesLoader.LOGGER.info(String.format("Values of properties file [%s] are :", PropertiesLoader.PROPERTIES_FILE_PATH));
		PropertiesLoader.LOGGER.info(String.format("\tWaiting time to write index file (in seconds) ....... [%s]", this.properties.getWaitingTimeToWriteIndexFileSec()));
		PropertiesLoader.LOGGER.info(String.format("\tReports resources directory path .................... [%s]", this.properties.getReportsResourcesDirectoryPath()));
		PropertiesLoader.LOGGER.info(String.format("\tReports directory path .............................. [%s]", this.properties.getReportsDirectoryPath()));

	}

	private static int buildStrictlyPositiveIntProperty(final Properties properties, final String key, final int defaultValue) {

		String valueString = properties.getProperty(key);

		if (valueString == null) {
			return defaultValue;
		}

		try {

			int valueInt = Integer.parseInt(valueString);

			if (valueInt < 1) {
				throw WrappedRuntimeException.wrap(String.format("The property [%s] must be strictly positive !", key));
			}

			return valueInt;

		} catch (final NumberFormatException numberFormatException) {
			throw WrappedRuntimeException.wrap( //
					String.format("An error occurred while converting string [%s] to int value for property [%s] !", valueString, key), //
					numberFormatException //
			);
		}

	}

	private static Path buildPathProperty(final Properties properties, final String key, final Path defaultValue, final boolean checkIfExists) {

		String valuePath = properties.getProperty(key);

		if (valuePath == null) {
			valuePath = defaultValue.toString();
		}

		File file = new File(valuePath);

		if (!checkIfExists || file.exists()) {

			return file.toPath();

		} else {
			throw WrappedRuntimeException.wrap(String.format("The file with path [%s] doesn't exist for property [%s] !", file.getAbsolutePath(), key));
		}

	}

	public FmProperties getProperties() {
		return this.properties;
	}

}