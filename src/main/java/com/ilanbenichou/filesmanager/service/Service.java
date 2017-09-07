package com.ilanbenichou.filesmanager.service;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.ilanbenichou.filesmanager.bean.FmDirectory;
import com.ilanbenichou.filesmanager.bean.FmFile;
import com.ilanbenichou.filesmanager.bean.FmIndex;
import com.ilanbenichou.filesmanager.date.DateHelper;
import com.ilanbenichou.filesmanager.exception.WrappedRuntimeException;
import com.ilanbenichou.filesmanager.file.FilesEnum;
import com.ilanbenichou.filesmanager.file.FilesHelper;
import com.ilanbenichou.filesmanager.index.IndexHelper;
import com.ilanbenichou.filesmanager.report.ReportVisitable;

public abstract class Service implements ReportVisitable {

	private static final Logger LOGGER = Logger.getLogger(Service.class);

	private final ServicesEnum serviceEnum;

	private String processingTime;

	protected Service(final ServicesEnum serviceEnum) {
		this.serviceEnum = serviceEnum;
	}

	public final void execute() {

		long startTime = DateHelper.nowTime();

		this.preHandle();

		String logMessageSuffix = this.serviceEnum.getDescription().toLowerCase();

		Service.LOGGER.info(String.format("Starting to %s ...", logMessageSuffix));

		this.innerExecute();

		this.postHandle();

		Service.LOGGER.info(String.format("Finished successfully to %s.", logMessageSuffix));

		long endTime = DateHelper.nowTime();

		this.processingTime = DateHelper.timeToString(endTime - startTime);

	}

	protected abstract void innerExecute();

	protected abstract void preHandle();

	protected abstract void postHandle();

	protected void initDirectory(final FmDirectory directory, final boolean isDirectoryGoldenSource) {

		if (isDirectoryGoldenSource) {
			return;
		}

		Arrays.asList(FilesEnum.OLD_FILES_META_DIFF_DIRECTORY, FilesEnum.OLD_FILES_DIRECTORY).forEach(

				directoryEnum -> this.moveDirectoryFiles(directory, this.resolvePathWithDirectory(directory, directoryEnum.getName()))

		);

		Arrays.asList(FilesEnum.DUPLICATE_FILES_DIRECTORY, FilesEnum.DUPLICATE_FILES_META_DIFF_DIRECTORY).forEach(

				directoryEnum -> {

					Path duplicatesDirectoryPath = this.resolvePathWithDirectory(directory, directoryEnum.getName());

					File duplicatesDirectoryPathFile = duplicatesDirectoryPath.toFile();

					if (duplicatesDirectoryPathFile.exists() && duplicatesDirectoryPathFile.isDirectory()) {
						FilesHelper.buildDirectoryAllDirectoriesStream(duplicatesDirectoryPath, 1).forEach(subDirectory -> this.moveDirectoryFiles(directory, subDirectory));
					}

				}

		);

	}

	private void moveDirectoryFiles(final FmDirectory directory, final Path directoryPath) {

		File directoryPathFile = directoryPath.toFile();

		if (!directoryPathFile.exists() || !directoryPathFile.isDirectory()) {
			return;
		}

		FilesHelper.buildDirectoryAllFilesStream(directoryPath).forEach(

				oldFilePath -> {

					if (FilesHelper.pathNotExists(oldFilePath)) {
						return;
					}

					Path fileRelativePath = FilesHelper.buildPathRelativePath(directoryPath, oldFilePath);

					Service.LOGGER.info(String.format("\tMoving file [%s] to directory [%s] ...", oldFilePath, directory.getPath()));

					Path newFilePath = this.resolvePathWithDirectory(directory, fileRelativePath);

					FilesHelper.moveFile(oldFilePath, newFilePath, false);

				}

		);

	}

	private final FmIndex buildDirectoryIndex(final FmDirectory directory) {

		FmIndex directoryIndex = directory.getIndex();

		if (directoryIndex == null) {

			Service.LOGGER.info(String.format("Building index for [%s] directory ...", directory.getAlias()));

			directoryIndex = IndexHelper.buildDirectoryIndex(directory.getPath());
			directory.setIndex(directoryIndex);

			Service.LOGGER.info(String.format("Index for [%s] directory is ready, contains [%d] entries.", directory.getAlias(), directoryIndex.size()));

		}

		return directoryIndex;

	}

	final Stream<Path> buildDirectoryFilesStream(final FmDirectory directory) {

		Service.LOGGER.info(String.format("Building files stream for [%s] directory ...", directory.getAlias()));

		Stream<Path> directoryFilesStream = FilesHelper.buildDirectoryFilesStream(directory.getPath());

		Service.LOGGER.info(String.format("Files stream for [%s] directory is ready.", directory.getAlias()));

		return directoryFilesStream;

	}

	final Map<Path, FmFile> buildDirectoryFilesMapByRelativePath(final FmDirectory directory) {
		return IndexHelper.buildMapByRelativePath(this.buildDirectoryIndex(directory));
	}

	final Map<String, Set<FmFile>> buildDirectoryFilesMapByHash(final FmDirectory directory) {
		return IndexHelper.buildMapByHash(this.buildDirectoryIndex(directory));
	}

	final Map<String, Set<FmFile>> buildDirectoryFilesMapByAudioHash(final FmDirectory directory) {
		return IndexHelper.buildMapByAudioHash(this.buildDirectoryIndex(directory));
	}

	final Path resolvePathWithDirectory(final FmDirectory directory, final String relativePathString) {
		return directory.getPath().resolve(relativePathString);
	}

	final Path resolvePathWithDirectory(final FmDirectory directory, final Path relativePath) {
		return directory.getPath().resolve(relativePath);
	}

	final void deleteDirectoryEmptyDirectories(final FmDirectory directory) {
		FilesHelper.buildDirectoryAllDirectoriesStream(directory.getPath()).forEach(FilesHelper::deleteDirectoryIfEmpty);
	}

	final Path buildDirectoryGoldenSourcePath(final FmDirectory directory) {
		return this.resolvePathWithDirectory(directory, FilesEnum.GOLDEN_SOURCE_FILE.getName());
	}

	final boolean isDirectoryGoldenSource(final FmDirectory directory) {
		return FilesHelper.pathExists(this.buildDirectoryGoldenSourcePath(directory));
	}

	final void checkDirectoryIsNotGoldenSource(final FmDirectory directory) {

		if (this.isDirectoryGoldenSource(directory)) {
			throw WrappedRuntimeException.wrap( //
					String.format( //
							"Unable to execute this service because [%s] directory [%s] is the golden source directory !", directory.getAlias(), //
							directory.getPath() //
					) //
			);
		}

	}

	public ServicesEnum getServiceEnum() {
		return this.serviceEnum;
	}

	public String getProcessingTime() {
		return this.processingTime;
	}

}