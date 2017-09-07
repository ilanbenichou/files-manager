package com.ilanbenichou.filesmanager.file;

import java.nio.file.Path;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.ilanbenichou.filesmanager.bean.FmFile;

public final class FmFilesHelper {

	private static final Logger LOGGER = Logger.getLogger(FmFilesHelper.class);

	private FmFilesHelper() {

	}

	public static void moveFile(final FmFile file) {
		FilesHelper.moveFile(FmFilesHelper.buildFmFileInitialPath(file), FmFilesHelper.buildFmFileUpdatedPath(file));
	}

	public static long buildFilesSetSizeSum(final Set<FmFile> filesSet) {
		return filesSet.stream().mapToLong(FmFile::getSize).reduce(0, Long::sum);
	}

	public static FmFile buildFmFile(final Path directoryPath, final Path filePath) {

		FmFilesHelper.LOGGER.debug(String.format("Building file [%s] ...", filePath));

		Path initialRelativePath = FilesHelper.buildPathRelativePath(directoryPath, filePath);

		Date lastModifiedDate = new Date(filePath.toFile().lastModified());
		long size = FilesHelper.buildFileSize(filePath);

		String mimeType = FilesHelper.buildFileMimeType(filePath);

		if (FmFilesHelper.LOGGER.isDebugEnabled()) {

			FmFilesHelper.LOGGER.debug(String.format("\tFile [%s] - Initial relative path ....... [%s]", filePath, initialRelativePath));
			FmFilesHelper.LOGGER.debug(String.format("\tFile [%s] - Last modified date .......... [%s]", filePath, lastModifiedDate));
			FmFilesHelper.LOGGER.debug(String.format("\tFile [%s] - Size ........................ [%s]", filePath, FilesHelper.fileSizeToString(size)));
			FmFilesHelper.LOGGER.debug(String.format("\tFile [%s] - Mime type ................... [%s]", filePath, mimeType));

		}

		return new FmFile(directoryPath, initialRelativePath, lastModifiedDate, size, mimeType);

	}

	public static Path buildFmFileInitialPath(final FmFile file) {
		return file.getParentPath().resolve(file.getInitialRelativePath());
	}

	public static Path buildFmFileUpdatedPath(final FmFile file) {
		return file.getParentPath().resolve(file.getUpdatedRelativePath());
	}

	public static Set<FmFile> concatFileWithFilesSet(final FmFile file, final Set<FmFile> filesSet) {
		return Stream.concat(Stream.of(file), filesSet.stream()).collect(Collectors.toSet());
	}

}