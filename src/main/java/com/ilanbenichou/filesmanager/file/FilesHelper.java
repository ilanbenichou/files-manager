package com.ilanbenichou.filesmanager.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;

import com.ilanbenichou.filesmanager.exception.WrappedRuntimeException;
import com.ilanbenichou.filesmanager.hash.HashHelper;

public final class FilesHelper {

	private static final Logger LOGGER = Logger.getLogger(FilesHelper.class);

	/**
	 * Max characters to read for a file (100 MB).
	 */
	public static final int MAX_CHARACTERES_TO_READ = 104857600;

	private static final String[] FILE_SIZE_UNITS_ARRAY = new String[] { "B", "kB", "MB", "GB", "TB" };

	private static final String DECIMAL_FORMAT = "#,##0.###";

	private static final Tika TIKA = new Tika();

	private FilesHelper() {
	}

	public static Stream<Path> buildDirectoryAllFilesStream(final Path directoryPath) {
		return FilesHelper.buildDirectoryStream(directoryPath, FilesFilterHelper.allFilesFilterPredicate(), Integer.MAX_VALUE);
	}

	public static Stream<Path> buildDirectoryAllDirectoriesStream(final Path directoryPath) {
		return FilesHelper.buildDirectoryAllDirectoriesStream(directoryPath, Integer.MAX_VALUE);
	}

	public static Stream<Path> buildDirectoryAllDirectoriesStream(final Path directoryPath, final int maxDepth) {

		Comparator<Path> directoriesComparator = (directoryPath1, directoryPath2) -> directoryPath2.toString().compareTo(directoryPath1.toString());

		return FilesHelper.buildDirectoryStream(directoryPath, FilesFilterHelper.allDirectoriesFilterPredicate(), maxDepth).sorted(directoriesComparator);

	}

	public static Stream<Path> buildDirectoryFilesStream(final Path directoryPath) {
		return FilesHelper.buildDirectoryStream(directoryPath, FilesFilterHelper.filesFilterPredicate(), Integer.MAX_VALUE);
	}

	public static Stream<Path> buildDirectoryStream(final Path directoryPath, final Predicate<? super Path> filterPredicate, final int maxDepth) {

		FilesHelper.LOGGER.debug(String.format("Building files stream for directory [%s] ...", directoryPath));

		try {

			return Files.walk(directoryPath, maxDepth).filter(filterPredicate);

		} catch (final IOException ioException) {
			throw WrappedRuntimeException.wrap(String.format("An error occurred while retrieving files in directory [%s] !", directoryPath), ioException);
		}

	}

	public static boolean pathExists(final Path path) {
		return path.toFile().exists();
	}

	public static boolean pathNotExists(final Path path) {
		return !FilesHelper.pathExists(path);
	}

	public static boolean directoryExists(final Path directoryPath) {

		FilesHelper.LOGGER.debug(String.format("Checking if directory [%s] exists ...", directoryPath));

		boolean directoryExists = directoryPath != null && FilesHelper.pathExists(directoryPath) && directoryPath.toFile().isDirectory();

		FilesHelper.LOGGER.debug(String.format("Directory [%s] %s", directoryPath, directoryExists ? "exists." : "does NOT exist !"));

		return directoryExists;

	}

	public static Path makeDirectory(final Path directoryPath) {

		if (FilesHelper.pathNotExists(directoryPath)) {

			FilesHelper.LOGGER.debug(String.format("Creating directory [%s] ...", directoryPath));

			try {

				Files.createDirectories(directoryPath);
				FilesHelper.LOGGER.debug(String.format("Directory [%s] has been created successfully.", directoryPath));

			} catch (final IOException ioException) {
				throw WrappedRuntimeException.wrap(String.format("An error occurred while creating directory [%s] !", directoryPath), ioException);
			}

		} else {

			FilesHelper.LOGGER.debug(String.format("Unable to create directory [%s] because this one already exists !", directoryPath));

		}

		return directoryPath;

	}

	public static void deleteDirectoryIfEmpty(final Path directoryPath) {

		if (FilesHelper.pathNotExists(directoryPath)) {
			return;
		}

		FilesHelper.LOGGER.debug(String.format("Deleting directory [%s] ...", directoryPath));

		File directoryFile = directoryPath.toFile();

		Arrays.asList(FilesEnum.WINDOWS_THUMBS_DB_FILE, FilesEnum.MAC_OS_DS_STORE_FILE)
				.forEach(fileEnum -> FilesHelper.deleteFile(directoryPath.resolve(fileEnum.getName()), true));

		if (directoryFile.listFiles().length == 0) {

			boolean directoryDeleted = directoryFile.delete();

			if (directoryDeleted) {
				FilesHelper.LOGGER.debug(String.format("Empty directory [%s] has been deleted.", directoryPath));
			}

		}

	}

	public static void moveFile(final Path oldFilePath, final Path newFilePath) {
		FilesHelper.moveFile(oldFilePath, newFilePath, false);
	}

	public static void moveFile(final Path oldFilePath, final Path newFilePath, final boolean force) {

		FilesHelper.LOGGER.debug(String.format("Moving file [%s] to [%s] ...", oldFilePath, newFilePath));

		if (FilesHelper.pathNotExists(oldFilePath)) {
			return;
		}

		try {

			FilesHelper.deleteFile(newFilePath, force);

			FileUtils.moveFile(oldFilePath.toFile(), newFilePath.toFile());
			FilesHelper.LOGGER.debug(String.format("File [%s] has been moved to [%s].", oldFilePath, newFilePath));

			FilesHelper.deleteDirectoryIfEmpty(oldFilePath.getParent());

		} catch (final IOException ioException) {
			throw WrappedRuntimeException.wrap(String.format("An error occurred while moving file [%s] to [%s] !", oldFilePath, newFilePath), ioException);
		}

	}

	public static void copyFile(final Path sourceFilePath, final Path targetFilePath, final boolean force) {

		FilesHelper.LOGGER.info(String.format("Copying file [%s] to [%s] ...", sourceFilePath, targetFilePath));

		try {

			FilesHelper.deleteFile(targetFilePath, force);

			File targetFilePathFile = targetFilePath.toFile();

			if (!force && targetFilePathFile.exists()) {
				throw WrappedRuntimeException.wrap(String.format("Unable to copy file [%s] to [%s] because this one already exists !", sourceFilePath, targetFilePath));
			}

			FileUtils.copyFile(sourceFilePath.toFile(), targetFilePathFile, true);

			FilesHelper.LOGGER.debug(String.format("File [%s] has been copied to [%s].", sourceFilePath, targetFilePath));

		} catch (final IOException ioException) {
			throw WrappedRuntimeException.wrap(String.format("An error occurred while copying file [%s] to [%s] !", sourceFilePath, targetFilePath), ioException);
		}

	}

	public static void createNewFile(final Path newFilePath) {

		File newFilePathFile = newFilePath.toFile();

		if (newFilePathFile.exists()) {
			throw WrappedRuntimeException.wrap(String.format("Unable to create the file [%s] that already exists !", newFilePath));
		}

		try {

			if (!newFilePathFile.createNewFile()) {
				throw WrappedRuntimeException.wrap(String.format("The file [%s] has NOT been created for an unknown reason !", newFilePathFile));
			}

		} catch (final IOException ioException) {
			throw WrappedRuntimeException.wrap(String.format("An error occurred while creating file [%s] !", newFilePathFile), ioException);
		}

	}

	public static void deleteFile(final Path filePath, final boolean force) {

		File filePathFile = filePath.toFile();

		if (filePathFile.exists() && force) {

			FilesHelper.LOGGER.info(String.format("Deleting file [%s] ...", filePath));

			if (!filePathFile.delete()) {
				throw WrappedRuntimeException.wrap(String.format("An error occurred while deleting file [%s] !", filePath));
			}

		}

	}

	public static long buildFileSize(final Path filePath) {
		return filePath.toFile().length();
	}

	public static String fileSizeToPrettyString(final long fileSize) {

		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setGroupingSeparator(' ');

		DecimalFormat df = new DecimalFormat();
		df.setDecimalFormatSymbols(symbols);
		df.setGroupingSize(3);

		return df.format(fileSize);

	}

	public static String fileSizeToString(final long fileSize) {

		if (fileSize <= 0) {
			return String.valueOf(0);
		}

		int digitGroups = (int) (Math.log10(fileSize) / Math.log10(1024));

		StringBuilder fileSizeSB = new StringBuilder();
		fileSizeSB.append(new DecimalFormat(FilesHelper.DECIMAL_FORMAT).format(fileSize / Math.pow(1024, digitGroups)));
		fileSizeSB.append(" ");
		fileSizeSB.append(FilesHelper.FILE_SIZE_UNITS_ARRAY[digitGroups]);

		return fileSizeSB.toString();

	}

	public static String buildFileHash(final Path filePath) {

		FilesHelper.LOGGER.debug(String.format("Building hash of file [%s] ...", filePath));

		File file = filePath.toFile();

		String firstCharacters = "";

		try (

				InputStream inputStream = new FileInputStream(file);

		) {

			if (inputStream.available() != 0) {
				byte[] byteArray = new byte[Math.min(inputStream.available(), FilesHelper.MAX_CHARACTERES_TO_READ)];
				firstCharacters = new String(byteArray, 0, inputStream.read(byteArray));
			}

		} catch (final IOException ioException) {
			throw WrappedRuntimeException.wrap(String.format("An error occurred while reading file [%s] !", file.getAbsolutePath()), ioException);
		}

		String hash = HashHelper.hash(FilesHelper.buildFileSize(filePath) + firstCharacters);

		FilesHelper.LOGGER.debug(String.format("Hash of file [%s] is equals to [%s].", filePath, hash));

		return hash;

	}

	public static Path buildPathRelativePath(final Path directoryPath, final Path absolutePath) {

		FilesHelper.LOGGER.debug(String.format("Building relative path of path [%s] in directory [%s] ...", absolutePath, directoryPath));

		Path fileRelativePath = directoryPath.relativize(absolutePath);

		FilesHelper.LOGGER.debug(String.format("Relative path of path [%s] in directory [%s] is [%s].", absolutePath, directoryPath, fileRelativePath));

		return fileRelativePath;

	}

	public static String buildFileMimeType(final Path filePath) {

		FilesHelper.LOGGER.debug(String.format("Building MIME type for file [%s] ...", filePath));

		try {

			return FilesHelper.TIKA.detect(filePath);

		} catch (final IOException ioException) {
			throw WrappedRuntimeException.wrap(String.format("An error occurred while retrieving MIME type for file [%s] !", filePath), ioException);
		}

	}

	public static void writeFile(final Path filePath, final String fileContent, final boolean force) {

		File file = filePath.toFile();

		FilesHelper.LOGGER.info(String.format("Writing file [%s] ...", filePath));

		StandardOpenOption standardOpenOption = StandardOpenOption.CREATE;

		boolean fileExists = file.exists();

		if (fileExists && force) {

			boolean writableActivated = file.setWritable(true);

			FilesHelper.LOGGER.debug(String.format("Has file permission 'Writable' been activated for file [%s] : [%s]", filePath, String.valueOf(writableActivated)));

			standardOpenOption = StandardOpenOption.TRUNCATE_EXISTING;
		}

		try {

			Files.write(filePath, fileContent.getBytes(), standardOpenOption);

			FilesHelper.LOGGER.info(String.format("File [%s] has been written successfully.", filePath));

		} catch (final IOException ioException) {
			throw WrappedRuntimeException.wrap(String.format("An error occurred while writing file [%s] !", filePath), ioException);
		} finally {

			if (fileExists) {

				boolean readOnlyActivated = file.setReadOnly();

				FilesHelper.LOGGER.debug(String.format("Has file permission 'Read Only' been activated for file [%s] : [%s]", filePath, String.valueOf(readOnlyActivated)));

			}

		}

	}

}