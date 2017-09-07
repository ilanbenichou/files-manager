package com.ilanbenichou.filesmanager.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
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
import com.ilanbenichou.filesmanager.service.SourceTargetService;

public final class FindNewFilesService extends SourceTargetService {

	public static class FileMetadatasDiffDatas {

		private final Set<FmFile> targetFilesSet;

		private final Set<String> filesMetadatasKeyDifferenceSet;

		protected FileMetadatasDiffDatas(final Set<FmFile> targetFilesSet, final Set<String> filesMetadatasKeyDifferenceSet) {
			this.targetFilesSet = targetFilesSet;
			this.filesMetadatasKeyDifferenceSet = filesMetadatasKeyDifferenceSet;
		}

		public Set<FmFile> getTargetFilesSet() {
			return this.targetFilesSet;
		}

		public Set<String> getFilesMetadatasKeyDifferenceSet() {
			return this.filesMetadatasKeyDifferenceSet;
		}

		@Override
		public String toString() {
			return //
			(this.targetFilesSet != null ? this.targetFilesSet.toString() : "") //
					+ //
					(this.filesMetadatasKeyDifferenceSet != null ? this.filesMetadatasKeyDifferenceSet.toString() : "");
		}

		@Override
		public boolean equals(final Object otherObject) {

			if (otherObject == null || !(otherObject instanceof FileMetadatasDiffDatas)) {
				return false;
			}

			FileMetadatasDiffDatas otherFile = (FileMetadatasDiffDatas) otherObject;

			if ( //
			(this.targetFilesSet == null && otherFile.targetFilesSet != null) //
					|| //
					(this.targetFilesSet != null && !this.targetFilesSet.equals(otherFile.targetFilesSet)) //
			) {
				return false;
			}

			if ( //
			(this.filesMetadatasKeyDifferenceSet == null && otherFile.filesMetadatasKeyDifferenceSet != null) || //
					(this.filesMetadatasKeyDifferenceSet != null && !this.filesMetadatasKeyDifferenceSet.equals(otherFile.filesMetadatasKeyDifferenceSet)) //
			) {
				return false;
			}

			return true;

		}

		@Override
		public int hashCode() {

			final int prime = 31;

			int result = 1;

			result = prime * result + (this.targetFilesSet != null ? this.targetFilesSet.hashCode() : 0);
			result = prime * result + (this.filesMetadatasKeyDifferenceSet != null ? this.filesMetadatasKeyDifferenceSet.hashCode() : 0);

			return result;

		}

	}

	private static final Logger LOGGER = Logger.getLogger(FindNewFilesService.class);

	private Set<FmFile> newFilesSet;

	private Map<FmFile, Set<FmFile>> existingFilesMap;

	private Map<FmFile, FileMetadatasDiffDatas> existingFilesMetaDiffMap;

	private long numberOfNewFiles;

	private long numberOfExistingFiles;

	private long diskSpaceLost;

	public FindNewFilesService(final Path sourceDirectoryPath, final Path targetDirectoryPath) {
		super(ServicesEnum.FIND_NEW_FILES_SERVICE, sourceDirectoryPath, true, true, targetDirectoryPath, false);
	}

	/**
	 * Constructor used for Unit tests
	 * 
	 * @param sourceDirectory
	 *            Source directory
	 * @param targetDirectory
	 *            Target directory
	 */
	FindNewFilesService(final FmDirectory sourceDirectory, final FmDirectory targetDirectory) {
		super(ServicesEnum.FIND_NEW_FILES_SERVICE, sourceDirectory, targetDirectory);
	}

	@Override
	protected void innerExecute() {

		this.buildNewFilesSet().forEach(newFile -> FindNewFilesService.LOGGER.info(String.format("\tFile [%s] NOT exists.", newFile.getInitialRelativePath())));

		this.buildExistingFilesMap().forEach((sourceExistingFile, targetFilesSet) -> FindNewFilesService.moveExistingFile(sourceExistingFile, targetFilesSet, true));

		this.buildExistingFilesMetaDiffMap().forEach(

				(sourceExistingFile, fileMetadatasDiffDatas) -> {

					FindNewFilesService.moveExistingFile(sourceExistingFile, fileMetadatasDiffDatas.getTargetFilesSet(), false);

					FindNewFilesService.logMetadatasDifferences(sourceExistingFile, fileMetadatasDiffDatas);

				}

		);

		FindNewFilesService.LOGGER.info(String.format("Number of new files ....................................... [%d]", this.buildNumberOfNewFiles()));
		FindNewFilesService.LOGGER.info(String.format("Number of existing files .................................. [%d]", this.buildNumberOfExistingFiles()));
		FindNewFilesService.LOGGER.info(String.format("Disk space lost ........................................... [%s]", FilesHelper.fileSizeToString(this.buildDiskSpaceLost())));

	}

	Map<FmFile, Set<FmFile>> buildExistingFilesMap() {

		if (this.existingFilesMap == null) {

			this.existingFilesMap = super.buildSourceDirectoryFilesMapByHash().entrySet().stream() //
					.filter(sourceEntry -> super.buildTargetDirectoryFilesMapByHash().containsKey(sourceEntry.getKey())) //
					.flatMap(sourceFilesSet -> sourceFilesSet.getValue().stream()) //
					.collect( //
							Collectors.toMap( //
									Function.identity(), //
									sourceExistingFile -> {
										FindNewFilesService.buildExistingFilesMapUpdatedRelativePath(sourceExistingFile, FilesEnum.OLD_FILES_DIRECTORY);
										return super.buildTargetDirectoryFilesMapByHash().get(sourceExistingFile.getHash());
									} //
							) //
			); //

		}

		return this.existingFilesMap;

	}

	Map<FmFile, FileMetadatasDiffDatas> buildExistingFilesMetaDiffMap() {

		if (this.existingFilesMetaDiffMap == null) {

			this.existingFilesMetaDiffMap = super.buildSourceDirectoryFilesMapByAudioHash().entrySet().stream() //
					.filter(sourceEntry -> super.buildTargetDirectoryFilesMapByAudioHash().containsKey(sourceEntry.getKey())) //
					.flatMap(sourceFilesSet -> sourceFilesSet.getValue().stream()) //
					.filter(sourceFile -> !super.buildTargetDirectoryFilesMapByHash().containsKey(sourceFile.getHash())) //
					.collect( //
							Collectors.toMap( //

									Function.identity(), //

									sourceExistingFile -> {

										FindNewFilesService.buildExistingFilesMapUpdatedRelativePath(sourceExistingFile, FilesEnum.OLD_FILES_META_DIFF_DIRECTORY);

										Set<FmFile> targetFilesSet = super.buildTargetDirectoryFilesMapByAudioHash().get(sourceExistingFile.getAudioHash());

										Set<String> filesMetadatasKeyDifferenceSet = FilesMetadatasHelper.buildFilesMetadatasKeyDifferenceSet( //
												FmFilesHelper.concatFileWithFilesSet(sourceExistingFile, targetFilesSet) //
										);

										return new FileMetadatasDiffDatas(targetFilesSet, filesMetadatasKeyDifferenceSet);

									} //

							) //
			); //

		}

		return this.existingFilesMetaDiffMap;

	}

	Set<FmFile> buildNewFilesSet() {

		if (this.newFilesSet == null) {

			this.newFilesSet = super.buildSourceDirectoryFilesMapByHash().entrySet().stream() //
					.flatMap(filesSet -> filesSet.getValue().stream()) //
					.filter(sourceFile -> //
					!super.buildTargetDirectoryFilesMapByHash().containsKey(sourceFile.getHash()) //
							&& //
							!super.buildTargetDirectoryFilesMapByAudioHash().containsKey(sourceFile.getAudioHash()) //
					) //
					.collect(Collectors.toSet());

		}

		return this.newFilesSet;

	}

	long buildNumberOfNewFiles() {

		if (this.numberOfNewFiles == 0L) {
			this.numberOfNewFiles = this.buildNewFilesSet().size();
		}

		return this.numberOfNewFiles;

	}

	long buildNumberOfExistingFiles() {

		if (this.numberOfExistingFiles == 0L) {
			this.numberOfExistingFiles = Integer.toUnsignedLong(this.buildExistingFilesMap().size() + this.buildExistingFilesMetaDiffMap().size());
		}

		return this.numberOfExistingFiles;

	}

	long buildDiskSpaceLost() {

		if (this.diskSpaceLost == 0L) {

			this.diskSpaceLost = Sets.union(this.buildExistingFilesMap().keySet(), this.buildExistingFilesMetaDiffMap().keySet()).stream().mapToLong(FmFile::getSize).sum();

		}

		return this.diskSpaceLost;

	}

	private static void buildExistingFilesMapUpdatedRelativePath(final FmFile existingFile, final FilesEnum directoryEnum) {
		existingFile.setUpdatedRelativePath(Paths.get(directoryEnum.getName()).resolve(existingFile.getInitialRelativePath()));
	}

	private static void logMetadatasDifferences(final FmFile sourceExistingFile, final FileMetadatasDiffDatas fileMetadatasDiffDatas) {

		StringBuilder stringBuilder = new StringBuilder();

		fileMetadatasDiffDatas.getFilesMetadatasKeyDifferenceSet().forEach(

				metadataKey -> {

					stringBuilder.append("\r\n\t[").append(metadataKey).append("] :");

					Map<String, Set<FmFile>> metadataValuesMap = FilesMetadatasHelper.buildMetadataValuesMap( //
							metadataKey, //
							FmFilesHelper.concatFileWithFilesSet(sourceExistingFile, fileMetadatasDiffDatas.getTargetFilesSet()) //
					);

					metadataValuesMap.forEach(

							(metadataValue, filesSetWithValue) -> {

								stringBuilder.append("\r\n\t\t+ ").append("[").append(metadataValue).append("] :");

								filesSetWithValue.forEach(

										file ->

										stringBuilder //
												.append("\r\n\t\t\t[") //
												.append(sourceExistingFile.getParentPath().equals(file.getParentPath()) ? "source" : "target") //
												.append("]/") //
												.append(file.getInitialRelativePath()) //

								);

							}

				);

				}

		);

		String metadatasDifferences = stringBuilder.toString();

		if (StringUtils.isNotBlank(metadatasDifferences)) {
			FindNewFilesService.LOGGER.info(String.format("Different metadatas are :%s", metadatasDifferences));
		}

	}

	private static void moveExistingFile(final FmFile sourceExistingFile, final Set<FmFile> targetFilesSet, boolean filesExactlyIdentical) {

		long filesSize = sourceExistingFile.getSize();

		FindNewFilesService.LOGGER.info( //
				String.format( //
						"The file [path = %s, size = %s] is %sidentical with the %d following file(s) :", //
						sourceExistingFile.getInitialRelativePath(), //
						FilesHelper.fileSizeToString(filesSize), //
						(!filesExactlyIdentical ? "almost " : ""), //
						targetFilesSet.size() //
				) //
		);

		targetFilesSet.forEach(targetFile -> FindNewFilesService.LOGGER.info(String.format("\t* %s", targetFile.getInitialRelativePath())));

		FmFilesHelper.moveFile(sourceExistingFile);

	}

	@Override
	public void accept(final ReportVisitor visitor) {
		visitor.visit(this);
	}

	public Map<FmFile, Set<FmFile>> getExistingFilesMap() {
		return this.existingFilesMap;
	}

	public Map<FmFile, FileMetadatasDiffDatas> getExistingFilesMetaDiffMap() {
		return this.existingFilesMetaDiffMap;
	}

	public Set<FmFile> getNewFilesSet() {
		return this.newFilesSet;
	}

	public long getNumberOfNewFiles() {
		return this.numberOfNewFiles;
	}

	public long getNumberOfExistingFiles() {
		return this.numberOfExistingFiles;
	}

	public long getDiskSpaceLost() {
		return this.diskSpaceLost;
	}

}