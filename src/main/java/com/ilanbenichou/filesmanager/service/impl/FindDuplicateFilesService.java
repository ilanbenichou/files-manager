package com.ilanbenichou.filesmanager.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.Sets;
import com.ilanbenichou.filesmanager.bean.FmDirectory;
import com.ilanbenichou.filesmanager.bean.FmFile;
import com.ilanbenichou.filesmanager.file.FilesEnum;
import com.ilanbenichou.filesmanager.file.FilesHelper;
import com.ilanbenichou.filesmanager.file.FmFilesHelper;
import com.ilanbenichou.filesmanager.file.metadata.FilesMetadatasHelper;
import com.ilanbenichou.filesmanager.report.ReportVisitor;
import com.ilanbenichou.filesmanager.service.ServicesEnum;
import com.ilanbenichou.filesmanager.service.SourceService;

public final class FindDuplicateFilesService extends SourceService {

	private static final Logger LOGGER = Logger.getLogger(FindDuplicateFilesService.class);

	private Set<Set<FmFile>> duplicateFilesSetSet;

	private Map<Set<FmFile>, Set<String>> duplicateFilesMetaDiffSetMap;

	private Set<FmFile> uniqueFilesSet;

	private long numberOfDuplicateFilePairs;

	private long numberOfDuplicateFiles;

	private long numberOfUniqueFiles;

	private long diskSpaceLost;

	public FindDuplicateFilesService(final Path sourceDirectoryPath) {
		super(ServicesEnum.FIND_DUPLICATE_FILES_SERVICE, sourceDirectoryPath, true, true);
	}

	/**
	 * Constructor used for Unit tests
	 * 
	 * @param sourceDirectory
	 *            Source directory
	 */
	FindDuplicateFilesService(final FmDirectory sourceDirectory) {
		super(ServicesEnum.FIND_DUPLICATE_FILES_SERVICE, sourceDirectory);
	}

	@Override
	protected void innerExecute() {

		FindDuplicateFilesService.moveDuplicateFiles(this.buildDuplicateFilesSetSet().stream().flatMap(Set::stream).collect(Collectors.toSet()), true);

		this.buildDuplicateFilesMetaDiffSetMap().forEach(

				(filesSet, filesMetadatasKeyDifferenceSet) -> {

					FindDuplicateFilesService.moveDuplicateFiles(filesSet, false);

					FindDuplicateFilesService.logMetadatasDifferences(filesSet, filesMetadatasKeyDifferenceSet);

				}

		);

		if (FindDuplicateFilesService.LOGGER.isDebugEnabled()) {

			this.buildUniqueFilesSet().forEach(file -> FindDuplicateFilesService.LOGGER.debug(String.format("File [%s] is unique.", file.getInitialRelativePath())));

		}

		FindDuplicateFilesService.LOGGER.info(String.format("Number of duplicate file pairs .................... [%d]", this.buildNumberOfDuplicateFilePairs()));
		FindDuplicateFilesService.LOGGER.info(String.format("Number of duplicate files ......................... [%d]", this.buildNumberOfDuplicateFiles()));
		FindDuplicateFilesService.LOGGER.info(String.format("Number of unique files ............................. [%d]", this.buildNumberOfUniqueFiles()));
		FindDuplicateFilesService.LOGGER.info(String.format("Disk space lost .................................... [%s]", FilesHelper.fileSizeToString(this.buildDiskSpaceLost())));

	}

	Set<Set<FmFile>> buildDuplicateFilesSetSet() {

		if (this.duplicateFilesSetSet == null) {

			this.duplicateFilesSetSet = super.buildSourceDirectoryFilesMapByHash().values().stream().filter(

					filesSetByHash -> {

						Set<FmFile> sourceFilesSetByAudioHash = super.buildSourceDirectoryFilesMapByAudioHash().get(filesSetByHash.stream().findFirst().get().getAudioHash());

						return //
						filesSetByHash.size() > 1 //
								&& //
						( //
						sourceFilesSetByAudioHash == null //
								|| //
						(sourceFilesSetByAudioHash != null && sourceFilesSetByAudioHash.size() == filesSetByHash.size()) //
						);

					}

			).collect(Collectors.toSet());

			this.buildDuplicateFilesSetSetUpdatedRelativePath(this.duplicateFilesSetSet, FilesEnum.DUPLICATE_FILES_DIRECTORY);

		}

		return this.duplicateFilesSetSet;

	}

	private Set<Set<FmFile>> duplicateFilesMetaDiffSetSet;

	Set<Set<FmFile>> buildDuplicateFilesMetaDiffSetSet() {

		if (this.duplicateFilesMetaDiffSetSet == null) {

			this.duplicateFilesMetaDiffSetSet = super.buildSourceDirectoryFilesMapByAudioHash().values().stream().filter(

					filesSetByAudioHash ->

					filesSetByAudioHash.size() > 1 //
							&& //
							filesSetByAudioHash.size() > super.buildSourceDirectoryFilesMapByHash().get(filesSetByAudioHash.stream().findFirst().get().getHash()).size()

			).collect(Collectors.toSet());

			this.buildDuplicateFilesSetSetUpdatedRelativePath(this.duplicateFilesMetaDiffSetSet, FilesEnum.DUPLICATE_FILES_META_DIFF_DIRECTORY);

		}

		return this.duplicateFilesMetaDiffSetSet;

	}

	private void buildDuplicateFilesSetSetUpdatedRelativePath(final Set<Set<FmFile>> duplicateFilesSetSet, final FilesEnum directoryEnum) {

		duplicateFilesSetSet.stream().forEach(

				filesSet -> {

					int duplicateFileNumber = 1;

					for (FmFile file : filesSet) {

						file.setUpdatedRelativePath( //
								Paths.get(directoryEnum.getName()).resolve(Paths.get("__FM_" + duplicateFileNumber++ + "__").resolve(file.getInitialRelativePath())) //
						);

					}

				}

		);

	}

	/**
	 * Returns Map<Set<File>,Set<MetadataKeyDifference>> containing all
	 * metadatas key of a files set with differences.
	 * 
	 * @return Map<Set<File>,Set<MetadataKeyDifference>> containing all
	 *         metadatas key of a files set with differences
	 */
	private Map<Set<FmFile>, Set<String>> buildDuplicateFilesMetaDiffSetMap() {

		if (this.duplicateFilesMetaDiffSetMap == null) {

			this.duplicateFilesMetaDiffSetMap = this.buildDuplicateFilesMetaDiffSetSet().stream().collect( //
					Collectors.toMap(Function.identity(), FilesMetadatasHelper::buildFilesMetadatasKeyDifferenceSet) //
			);

		}

		return this.duplicateFilesMetaDiffSetMap;

	}

	private Set<Set<FmFile>> allDuplicateFilesSetSet;

	private Set<Set<FmFile>> buildAllDuplicateFilesSetSet() {

		if (this.allDuplicateFilesSetSet == null) {
			this.allDuplicateFilesSetSet = Sets.union(this.buildDuplicateFilesSetSet(), this.buildDuplicateFilesMetaDiffSetSet());
		}

		return this.allDuplicateFilesSetSet;

	}

	Set<FmFile> buildUniqueFilesSet() {

		if (this.uniqueFilesSet == null) {

			this.uniqueFilesSet = Sets.difference( //
					super.getSourceDirectory().getIndex().getFilesSet(), //
					this.buildAllDuplicateFilesSetSet().stream().flatMap(Set::stream).collect(Collectors.toSet()) //
			);

		}

		return this.uniqueFilesSet;

	}

	long buildNumberOfDuplicateFilePairs() {

		if (this.numberOfDuplicateFilePairs == 0L) {
			this.numberOfDuplicateFilePairs = this.buildAllDuplicateFilesSetSet().size();
		}

		return this.numberOfDuplicateFilePairs;

	}

	long buildNumberOfDuplicateFiles() {

		if (this.numberOfDuplicateFiles == 0L) {
			this.numberOfDuplicateFiles = this.buildAllDuplicateFilesSetSet().stream().flatMap(Set::stream).count();
		}

		return this.numberOfDuplicateFiles;

	}

	long buildNumberOfUniqueFiles() {

		if (this.numberOfUniqueFiles == 0L) {
			this.numberOfUniqueFiles = this.buildUniqueFilesSet().size();
		}

		return this.numberOfUniqueFiles;

	}

	long buildDiskSpaceLost() {

		if (this.diskSpaceLost == 0L) {
			this.diskSpaceLost = this.buildAllDuplicateFilesSetSet().stream().mapToLong(set -> set.stream().findFirst().get().getSize() * (set.size() - 1)).sum();
		}

		return this.diskSpaceLost;

	}

	private static void moveDuplicateFiles(final Set<FmFile> duplicateFilesSetSet, final boolean filesExactlyIdentical) {

		Optional<FmFile> firstFile = duplicateFilesSetSet.stream().findFirst();

		long filesSize = firstFile.isPresent() ? firstFile.get().getSize() : 0L;

		FindDuplicateFilesService.LOGGER.info( //
				String.format( //
						"The following files are %sidentical [size = %s, count = %d] :", //
						(!filesExactlyIdentical ? "almost " : ""), //
						FilesHelper.fileSizeToString(filesSize), //
						duplicateFilesSetSet.size() //
				) //
		);

		for (FmFile file : duplicateFilesSetSet) {

			FindDuplicateFilesService.LOGGER.info(String.format("\t* %s", file.getInitialRelativePath()));

			FmFilesHelper.moveFile(file);

		}

	}

	private static void logMetadatasDifferences(final Set<FmFile> filesSet, final Set<String> filesMetadatasKeyDifferenceSet) {

		StringBuilder stringBuilder = new StringBuilder();

		filesMetadatasKeyDifferenceSet.forEach(

				metadataKey -> {

					stringBuilder.append("\r\n\t[").append(metadataKey).append("] :");

					Map<String, Set<FmFile>> metadataValuesMap = FilesMetadatasHelper.buildMetadataValuesMap(metadataKey, filesSet);

					metadataValuesMap.forEach(

							(metadataValue, filesSetWithValue) -> {

								stringBuilder.append("\r\n\t\t+ ").append("[").append(metadataValue).append("] :");

								filesSetWithValue.forEach(file -> stringBuilder.append("\r\n\t\t\t").append(file.getInitialRelativePath()));

							}

				);

				}

		);

		String metadatasDifferences = stringBuilder.toString();

		if (StringUtils.isNotBlank(metadatasDifferences)) {
			FindDuplicateFilesService.LOGGER.info(String.format("Different metadatas are :%s", metadatasDifferences));
		}

	}

	@Override
	public void accept(final ReportVisitor visitor) {
		visitor.visit(this);
	}

	public Set<Set<FmFile>> getDuplicateFilesSetSet() {
		return this.duplicateFilesSetSet;
	}

	public Map<Set<FmFile>, Set<String>> getDuplicateFilesMetaDiffSetMap() {
		return this.duplicateFilesMetaDiffSetMap;
	}

	public Set<FmFile> getUniqueFilesSet() {
		return this.uniqueFilesSet;
	}

	public long getNumberOfDuplicateFilePairs() {
		return this.numberOfDuplicateFilePairs;
	}

	public long getNumberOfDuplicateFiles() {
		return this.numberOfDuplicateFiles;
	}

	public long getNumberOfUniqueFiles() {
		return this.numberOfUniqueFiles;
	}

	public long getDiskSpaceLost() {
		return this.diskSpaceLost;
	}

}