package com.ilanbenichou.filesmanager.context;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;

import com.ilanbenichou.filesmanager.bean.FmContext;
import com.ilanbenichou.filesmanager.exception.WrappedRuntimeException;
import com.ilanbenichou.filesmanager.file.FilesHelper;
import com.ilanbenichou.filesmanager.service.ServicesEnum;

public final class ContextHelper {

	private static final Logger LOGGER = Logger.getLogger(ContextHelper.class);

	private static final String PARAMETERS_PARSING_ERROR = "An error occurred while parsing parameters !";

	private ContextHelper() {
	}

	private static String getOptionValue(final CommandLine commandLine, final OptionsEnum option, final List<String> errorsList) {

		if (commandLine.hasOption(option.getOpt())) {

			return commandLine.getOptionValue(option.getOpt());

		} else {

			if (errorsList != null) {
				errorsList.add(String.format("'%s' option is mandatory. Option -%s !", option.getLongOpt(), option.getOpt()));
			}

			return null;

		}

	}

	private static boolean directoryExists(final Path directoryPath, final List<String> errorsList) {

		boolean directoryExists = FilesHelper.directoryExists(directoryPath);

		if (!directoryExists) {
			errorsList.add(String.format("The directory [%s] does NOT exist !", directoryPath));
		}

		return directoryExists;

	}

	public static FmContext buildContext(final String... args) {

		CommandLineParser parser = new DefaultParser();

		Options options = new Options();

		for (OptionsEnum option : OptionsEnum.values()) {
			options.addOption(option.getOpt(), option.getLongOpt(), option.hasArg(), option.getDescription());
		}

		CommandLine commandLine = null;
		try {
			commandLine = parser.parse(options, args);
		} catch (ParseException parseException) {
			throw WrappedRuntimeException.wrap(ContextHelper.PARAMETERS_PARSING_ERROR, parseException);
		}

		FmContext context = new FmContext();

		List<String> errorsList = new ArrayList<>();

		String serviceOptionValue = ContextHelper.getOptionValue(commandLine, OptionsEnum.SERVICE, errorsList);

		if (serviceOptionValue != null) {

			ServicesEnum serviceOptionValueEnum = ServicesEnum.getByOptionValue(serviceOptionValue);
			context.setServiceOptionValuesEnum(serviceOptionValueEnum);

			if (serviceOptionValueEnum != null) {

				String sourceOptionValue = ContextHelper.getOptionValue(commandLine, OptionsEnum.SOURCE, errorsList);

				Path sourceDirectoryPath = null;

				if (sourceOptionValue != null) {

					sourceDirectoryPath = Paths.get(sourceOptionValue);
					context.setSourceDirectoryPath(sourceDirectoryPath);
					ContextHelper.directoryExists(sourceDirectoryPath, errorsList);

				}

				if (serviceOptionValueEnum.isTargetDirectoryRequired()) {

					String targetOptionValue = ContextHelper.getOptionValue(commandLine, OptionsEnum.TARGET, errorsList);

					if (targetOptionValue != null) {

						Path targetDirectoryPath = Paths.get(targetOptionValue);
						context.setTargetDirectoryPath(targetDirectoryPath);
						boolean targetDirectoryExists = ContextHelper.directoryExists(targetDirectoryPath, errorsList);

						if (sourceDirectoryPath != null && targetDirectoryExists && sourceDirectoryPath.equals(targetDirectoryPath)) {
							errorsList.add("Target directory and source directory MUST be different !");
						}

					}

				}

			} else {
				errorsList.add(String.format("Service specified [%s] is unknown !", serviceOptionValue));
			}

		}

		if (!errorsList.isEmpty()) {
			errorsList.forEach(ContextHelper.LOGGER::error);
			throw WrappedRuntimeException.wrap(ContextHelper.PARAMETERS_PARSING_ERROR);
		}

		ContextHelper.LOGGER.info("Context datas are :");
		ContextHelper.LOGGER.info(String.format("\tService.............................................. [%s]", context.getServiceOptionValueEnum().getDescription()));
		ContextHelper.LOGGER.info(String.format("\tSource directory .................................... [%s]", context.getSourceDirectoryPath()));

		if (context.getTargetDirectoryPath() != null) {
			ContextHelper.LOGGER.info(String.format("\tTarget directory .................................... [%s]", context.getTargetDirectoryPath()));
		}

		return context;

	}

}