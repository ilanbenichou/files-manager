package com.ilanbenichou.filesmanager.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.ilanbenichou.filesmanager.file.FilesHelper;
import com.ilanbenichou.filesmanager.report.ReportVisitor;
import com.ilanbenichou.filesmanager.service.ServicesEnum;
import com.ilanbenichou.filesmanager.service.SourceService;

public final class RenameFilesService extends SourceService {

	private static final Logger LOGGER = Logger.getLogger(RenameFilesService.class);

	private static final String FILE_TO_RENAME_PREFIX = "_";

	public RenameFilesService(final Path sourceDirectoryPath) {
		super(ServicesEnum.RENAME_FILES_SERVICE, sourceDirectoryPath, false, true);
	}

	@Override
	protected void innerExecute() {

		Stream<Path> sourceDirectoryFilesStream = super.buildSourceDirectoryFilesStream();

		sourceDirectoryFilesStream.forEach(

				oldFilePath -> {

					if (FilesHelper.pathNotExists(oldFilePath)) {
						return;
					}

					String newFileName = oldFilePath.getFileName().toString();

					Path newFilePath = null;

					while (newFilePath == null || FilesHelper.pathExists(newFilePath)) {
						newFileName = new StringBuilder(RenameFilesService.FILE_TO_RENAME_PREFIX).append(newFileName).toString();
						newFilePath = Paths.get(oldFilePath.getParent().toString(), newFileName);
					}

					RenameFilesService.LOGGER.info(String.format("Renaming file [%s] to [%s] ...", oldFilePath, newFilePath.getFileName()));

					FilesHelper.moveFile(oldFilePath, newFilePath);

					RenameFilesService.LOGGER.info(String.format("File [%s] has been renamed to [%s].", oldFilePath, newFilePath.getFileName()));

				}

		);

	}

	@Override
	public void accept(final ReportVisitor visitor) {
		// No report for this service
	}

}