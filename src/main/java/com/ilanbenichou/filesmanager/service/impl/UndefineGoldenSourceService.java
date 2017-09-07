package com.ilanbenichou.filesmanager.service.impl;

import java.nio.file.Path;

import org.apache.log4j.Logger;

import com.ilanbenichou.filesmanager.file.FilesHelper;
import com.ilanbenichou.filesmanager.report.ReportVisitor;
import com.ilanbenichou.filesmanager.service.ServicesEnum;
import com.ilanbenichou.filesmanager.service.SourceService;

public final class UndefineGoldenSourceService extends SourceService {

	private static final Logger LOGGER = Logger.getLogger(UndefineGoldenSourceService.class);

	public UndefineGoldenSourceService(final Path sourceDirectoryPath) {
		super(ServicesEnum.UNDEFINE_GOLDEN_SOURCE_SERVICE, sourceDirectoryPath, false, false);
	}

	@Override
	protected void innerExecute() {

		if (!super.isSourceDirectoryGoldenSource()) {

			UndefineGoldenSourceService.LOGGER.warn(String.format("The directory [%s] is already NOT the golden source one !", super.getSourceDirectory().getPath()));

		} else {

			Path sourceDirectoryGoldenSourcePath = super.buildSourceDirectoryGoldenSourcePath();

			FilesHelper.deleteFile(sourceDirectoryGoldenSourcePath, true);

			UndefineGoldenSourceService.LOGGER.info(String.format("The directory [%s] is NO LONGER the golden source one.", super.getSourceDirectory().getPath()));

		}

	}

	@Override
	public void accept(final ReportVisitor visitor) {
		// No report for this service
	}

}