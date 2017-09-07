package com.ilanbenichou.filesmanager.service.impl;

import java.nio.file.Path;

import org.apache.log4j.Logger;

import com.ilanbenichou.filesmanager.file.FilesHelper;
import com.ilanbenichou.filesmanager.report.ReportVisitor;
import com.ilanbenichou.filesmanager.service.ServicesEnum;
import com.ilanbenichou.filesmanager.service.SourceService;

public final class DefineGoldenSourceService extends SourceService {

	private static final Logger LOGGER = Logger.getLogger(DefineGoldenSourceService.class);

	public DefineGoldenSourceService(final Path sourceDirectoryPath) {
		super(ServicesEnum.DEFINE_GOLDEN_SOURCE_SERVICE, sourceDirectoryPath, false, false);
	}

	@Override
	protected void innerExecute() {

		if (super.isSourceDirectoryGoldenSource()) {

			DefineGoldenSourceService.LOGGER.warn(String.format("The directory [%s] is already a golden source one !", super.getSourceDirectory().getPath()));

		} else {

			Path sourceDirectoryGoldenSourcePath = super.buildSourceDirectoryGoldenSourcePath();

			FilesHelper.createNewFile(sourceDirectoryGoldenSourcePath);

			DefineGoldenSourceService.LOGGER.info(String.format("The directory [%s] is now a golden source one.", super.getSourceDirectory().getPath()));

		}

	}

	@Override
	public void accept(final ReportVisitor visitor) {
		// No report for this service
	}

}