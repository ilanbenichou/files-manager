package com.ilanbenichou.filesmanager.index;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.ilanbenichou.filesmanager.bean.FmFile;
import com.ilanbenichou.filesmanager.bean.FmIndex;
import com.ilanbenichou.filesmanager.config.PropertiesLoader;
import com.ilanbenichou.filesmanager.exception.IndexLineParseException;
import com.ilanbenichou.filesmanager.exception.WrappedRuntimeException;
import com.ilanbenichou.filesmanager.file.AudioFilesHelper;
import com.ilanbenichou.filesmanager.file.FilesEnum;
import com.ilanbenichou.filesmanager.file.FilesHelper;
import com.ilanbenichou.filesmanager.file.FmFilesHelper;
import com.ilanbenichou.filesmanager.hash.HashHelper;

public final class IndexHelper {

	private static final Logger LOGGER = Logger.getLogger(IndexHelper.class);

	private IndexHelper() {
	}

	private static Path buildIndexFilePath(final Path directoryPath) {

		IndexHelper.LOGGER.debug(String.format("Building index file path for directory [%s] ...", directoryPath));

		Path indexFilePath = directoryPath.resolve(FilesEnum.INDEX_FILE.getName());

		IndexHelper.LOGGER.debug(String.format("Index file path for directory [%s] is [%s].", directoryPath, indexFilePath));

		return indexFilePath;

	}

	private static void addFileToIndex(final FmIndex index, final String indexLine, final Path directoryPath, final Path indexFileParentRelativePath) {

		IndexHelper.LOGGER.debug(String.format("Adding file [%s] to index ...", indexLine));

		try {
			index.addFile(IndexLineFormatter.getInstance().parse(indexLine, directoryPath, indexFileParentRelativePath));
		} catch (final IndexLineParseException indexLineParseException) {
			IndexHelper.LOGGER.warn(String.format("Index [%s] contains following error : [%s] !", index.getFilePath(), indexLineParseException.getLocalizedMessage()));
		}

		IndexHelper.LOGGER.debug(String.format("File [%s] has been added to index.", indexLine));

	}

	private static Predicate<Path> allIndexFilesFilterPredicate() {
		return path -> path != null && path.toFile().isFile() && FilesEnum.INDEX_FILE.getName().equals(path.getFileName().toString());
	}

	private static Stream<Path> buildIndexFilesStream(final Path directoryPath) {
		return FilesHelper.buildDirectoryStream(directoryPath, IndexHelper.allIndexFilesFilterPredicate(), Integer.MAX_VALUE);
	}

	private static FmIndex readDirectoryIndexFiles(final Path directoryPath) {

		IndexHelper.LOGGER.debug(String.format("Searching for index files in directory [%s] ...", directoryPath));

		FmIndex index = new FmIndex(IndexHelper.buildIndexFilePath(directoryPath));

		IndexHelper.buildIndexFilesStream(directoryPath).forEach(

				indexFilePath -> {

					if (FilesHelper.pathNotExists(indexFilePath)) {
						return;
					}

					Path indexFileParentRelativePath = FilesHelper.buildPathRelativePath(directoryPath, indexFilePath.getParent());

					try (

							FileReader fileReader = new FileReader(indexFilePath.toFile()); //
							BufferedReader bufferedReader = new BufferedReader(fileReader); //

					) {

						String indexLine;

						while ((indexLine = bufferedReader.readLine()) != null) {
							IndexHelper.addFileToIndex(index, indexLine, directoryPath, indexFileParentRelativePath);
						}

						IndexHelper.LOGGER.debug(String.format("Existing index contains [%d] entries", index.size()));

					} catch (final IOException ioException) {
						throw WrappedRuntimeException.wrap(String.format("An error occurred while reading index [%s] !", indexFilePath), ioException);
					}

				}

		);

		return index;

	}

	public static FmIndex buildDirectoryIndex(final Path directoryPath) {

		IndexHelper.LOGGER.debug(String.format("Building index for directory [%s] ...", directoryPath));

		FmIndex existingIndex = IndexHelper.readDirectoryIndexFiles(directoryPath);

		Stream<Path> directoryFilesStream = FilesHelper.buildDirectoryFilesStream(directoryPath);

		FmIndex currentIndex = new FmIndex(IndexHelper.buildIndexFilePath(directoryPath));

		directoryFilesStream.parallel().forEach(

				filePath -> {

					if (FilesHelper.pathNotExists(filePath)) {
						return;
					}

					FmFile file = FmFilesHelper.buildFmFile(directoryPath, filePath);

					IndexHelper.LOGGER.info(String.format("\tAdding file [%s] with size [%s] to index ...", filePath, FilesHelper.fileSizeToString(file.getSize())));

					FmFile fileInExistingIndex = existingIndex.getFile(file);

					if (fileInExistingIndex != null) {

						currentIndex.addFile(fileInExistingIndex);

					} else {

						file.setHash(new StringBuilder(String.valueOf(file.getSize())).append(FilesHelper.buildFileHash(filePath)).toString());

						if (AudioFilesHelper.isAudioMimeType(file.getMimeType())) {

							byte[] audioBytesArray = AudioFilesHelper.buildAudioFileBytesArray(filePath);

							if (audioBytesArray != null) {

								byte[] firstAudioBytesArray = Arrays.copyOfRange(audioBytesArray, 0, FilesHelper.MAX_CHARACTERES_TO_READ);

								String firstAudioBytes = new String(firstAudioBytesArray);

								if (StringUtils.isNotBlank(firstAudioBytes.trim())) {

									String audioHash = HashHelper.hash(String.valueOf(firstAudioBytesArray.length) + firstAudioBytes);

									IndexHelper.LOGGER.debug(String.format("Audio hash of file [%s] is equals to [%s].", filePath, audioHash));

									file.setAudioHash(audioHash);

								}

							}

						}

						currentIndex.addFile(file);
					}

					IndexHelper.writeTemporaryIndexFile(existingIndex, currentIndex);

				}

		);

		IndexHelper.writeIndexFile(currentIndex);

		return currentIndex;

	}

	private static synchronized void writeTemporaryIndexFile(final FmIndex existingIndex, final FmIndex currentIndex) {

		long nowTime = new Date().getTime();

		if ( //
		currentIndex.getLastTemporaryIndexFileBackupTime() == 0L //
				|| //
				nowTime - currentIndex.getLastTemporaryIndexFileBackupTime() //
				> Integer.toUnsignedLong(PropertiesLoader.getInstance().getProperties().getWaitingTimeToWriteIndexFileSec() * 1000) //
		) {

			FmIndex temporaryIndex = new FmIndex(currentIndex.getFilePath());

			if (existingIndex != null) {
				existingIndex.getFilesSet().forEach(temporaryIndex::addFile);
			}

			currentIndex.getFilesSet().forEach(temporaryIndex::addFile);

			IndexHelper.writeIndexFile(temporaryIndex);

			currentIndex.setLastTemporaryIndexFileBackupTime(nowTime);

		}

	}

	public static void writeIndexFile(final FmIndex index) {

		Path indexFilePath = index.getFilePath();

		IndexHelper.LOGGER.info(String.format("Writing index file [%s] ...", indexFilePath));

		StringBuilder indexSB = new StringBuilder();

		Set<FmFile> filesSet = index.getFilesSet();

		filesSet.forEach(

				file -> {

					indexSB.append(IndexLineFormatter.getInstance().format(file));
					indexSB.append("\r\n");

				}

		);

		FilesHelper.writeFile(indexFilePath, indexSB.toString(), true);

	}

	public static Map<Path, FmFile> buildMapByRelativePath(final FmIndex index) {
		return index.getFilesSet().stream().collect(Collectors.toMap(FmFile::getInitialRelativePath, Function.identity()));
	}

	public static Map<String, Set<FmFile>> buildMapByHash(final FmIndex index) {
		return index.getFilesSet().stream().collect(Collectors.groupingBy(FmFile::getHash, HashMap::new, Collectors.mapping(Function.identity(), Collectors.toSet())));
	}

	public static Map<String, Set<FmFile>> buildMapByAudioHash(final FmIndex index) {

		return index.getFilesSet().stream().filter(file -> file.getAudioHash() != null).collect( //
				Collectors.groupingBy( //
						FmFile::getAudioHash, //
						HashMap::new, //
						Collectors.mapping(Function.identity(), Collectors.toSet()) //
				) //
		);

	}

}