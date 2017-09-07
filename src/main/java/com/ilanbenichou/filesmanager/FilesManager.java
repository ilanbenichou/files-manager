package com.ilanbenichou.filesmanager;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.ilanbenichou.filesmanager.bean.FmContext;
import com.ilanbenichou.filesmanager.config.PropertiesLoader;
import com.ilanbenichou.filesmanager.context.ContextHelper;
import com.ilanbenichou.filesmanager.report.html.HtmlReportVisitor;
import com.ilanbenichou.filesmanager.report.html.builder.HtmlReportBuilderHelper;
import com.ilanbenichou.filesmanager.service.Service;
import com.ilanbenichou.filesmanager.service.ServiceFactory;

public final class FilesManager {

	private static final Logger LOGGER = Logger.getLogger(FilesManager.class);

	private final String[] args;

	private FilesManager(final String... args) {
		this.args = args;
	}

	public static void main(final String... args) {
		new FilesManager(args).execute();
	}

	private void execute() {

		FilesManager.LOGGER.info(String.format("Starting Files Manager program [Version %s] ...", FilesManager.buildVersion()));

		PropertiesLoader.getInstance().printProperties();

		FmContext context = ContextHelper.buildContext(this.args);

		FilesManager.LOGGER.info("Executing service ...");

		Service service = ServiceFactory.buildServiceFromContext(context);

		service.execute();

		HtmlReportVisitor htmlReportVisitor = new HtmlReportVisitor();

		service.accept(htmlReportVisitor);

		htmlReportVisitor.writeAndOpenReport();

		FilesManager.LOGGER.info(String.format("End of Files Manager program (%s).", service.getProcessingTime()));

	}

	public static String buildVersion() {

		Package pkg = HtmlReportBuilderHelper.class.getPackage();

		String version = null;

		if (pkg != null) {

			version = pkg.getImplementationVersion();
			version = StringUtils.isNotBlank(version) ? version : pkg.getSpecificationVersion();

		}

		return StringUtils.isNotBlank(version) ? version : "<unknown>";

	}

}