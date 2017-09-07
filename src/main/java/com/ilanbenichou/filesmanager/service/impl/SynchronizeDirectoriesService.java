package com.ilanbenichou.filesmanager.service.impl;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.ilanbenichou.filesmanager.bean.FmDirectory;
import com.ilanbenichou.filesmanager.bean.FmFile;
import com.ilanbenichou.filesmanager.file.FilesHelper;
import com.ilanbenichou.filesmanager.file.FmFilesHelper;
import com.ilanbenichou.filesmanager.report.ReportVisitor;
import com.ilanbenichou.filesmanager.service.ServicesEnum;
import com.ilanbenichou.filesmanager.service.SourceTargetService;

public final class SynchronizeDirectoriesService extends SourceTargetService {

	private static final Logger LOGGER = Logger.getLogger(SynchronizeDirectoriesService.class);

	private Set<FmFile> newFilesSet;

	private Set<FmFile> differentFilesSet;

	private Set<FmFile> deletedFilesSet;

	private Set<FmFile> existingFilesSet;

	private long numberOfNewFiles;

	private long numberOfDifferentFiles;

	private long numberOfDeletedFiles;

	private long numberOfExistingFiles;

	public SynchronizeDirectoriesService(final Path sourceDirectoryPath, final Path targetDirectoryPath) {
		super(ServicesEnum.SYNCHRONIZE_DIRECTORIES_SERVICE, sourceDirectoryPath, true, false, targetDirectoryPath, true);
	}

	/**
	 * Constructor used for Unit tests
	 * 
	 * @param sourceDirectory
	 *            Source directory
	 * @param targetDirectory
	 *            Target directory
	 */
	SynchronizeDirectoriesService(final FmDirectory sourceDirectory, final FmDirectory targetDirectory) {
		super(ServicesEnum.SYNCHRONIZE_DIRECTORIES_SERVICE, sourceDirectory, targetDirectory);
	}

	@Override
	protected void innerExecute() {

		this.buildNewFilesSet().forEach(

				newFile -> {

					SynchronizeDirectoriesService.LOGGER.info(String.format("\tFile [%s] NOT exists in target directory.", newFile.getInitialRelativePath()));

					FilesHelper.copyFile(FmFilesHelper.buildFmFileInitialPath(newFile), super.resolvePathWithTargetDirectory(newFile.getInitialRelativePath()), false);

				}

		);

		this.buildDifferentFilesSet().forEach(

				differentFile -> {

					SynchronizeDirectoriesService.LOGGER.info( //
							String.format("\tFile [%s] already exists in target directory but is different.", differentFile.getInitialRelativePath()) //
					);

					FilesHelper.copyFile(FmFilesHelper.buildFmFileInitialPath(differentFile), super.resolvePathWithTargetDirectory(differentFile.getInitialRelativePath()), true);

				}

		);

		this.buildDeletedFilesSet().forEach(

				fileToDelete -> {

					SynchronizeDirectoriesService.LOGGER.info( //
							String.format("\tFile [%s] NOT exists in source directory, this one will be deleted.", fileToDelete.getInitialRelativePath()) //
					);

					FilesHelper.deleteFile(FmFilesHelper.buildFmFileInitialPath(fileToDelete), true);

				}

		);

		if (SynchronizeDirectoriesService.LOGGER.isDebugEnabled()) {

			this.buildExistingFilesSet().forEach( //

					existingFile -> //
					SynchronizeDirectoriesService.LOGGER.debug(String.format("\tFile [%s] already exists in target directory.", existingFile.getInitialRelativePath())) //

			);

		}

		SynchronizeDirectoriesService.LOGGER.info(String.format("Number of new files ....................................... [%d]", this.buildNumberOfNewFiles()));
		SynchronizeDirectoriesService.LOGGER.info(String.format("Number of different files ................................. [%d]", this.buildNumberOfDifferentFiles()));
		SynchronizeDirectoriesService.LOGGER.info(String.format("Number of deleted files ................................... [%d]", this.buildNumberOfDeletedFiles()));
		SynchronizeDirectoriesService.LOGGER.info(String.format("Number of existing files .................................. [%d]", this.buildNumberOfExistingFiles()));

		Path targetIndexPath = super.getTargetDirectory().getIndex().getFilePath();

		SynchronizeDirectoriesService.LOGGER.info(String.format("Copying target index file [%s] ...", targetIndexPath));

		FilesHelper.copyFile(super.getSourceDirectory().getIndex().getFilePath(), targetIndexPath, true);

		SynchronizeDirectoriesService.LOGGER.info(String.format("Target index file [%s] copied.", targetIndexPath));

	}

	@Override
	public void accept(final ReportVisitor visitor) {
		visitor.visit(this);
	}

	Set<FmFile> buildNewFilesSet() {

		if (this.newFilesSet == null) {

			this.newFilesSet = super.buildSourceDirectoryFilesMapByRelativePath().entrySet().stream() //
					.filter(sourceFileEntry -> !super.buildTargetDirectoryFilesMapByRelativePath().containsKey(sourceFileEntry.getKey())) //
					.map(Map.Entry::getValue).collect(Collectors.toCollection(TreeSet::new));

		}

		return this.newFilesSet;

	}

	Set<FmFile> buildDifferentFilesSet() {

		if (this.differentFilesSet == null) {

			this.differentFilesSet = super.buildSourceDirectoryFilesMapByRelativePath().entrySet().stream().filter(

					sourceFileEntry -> {

						FmFile targetFile = super.buildTargetDirectoryFilesMapByRelativePath().get(sourceFileEntry.getKey());

						return targetFile != null && !targetFile.getHash().equals(sourceFileEntry.getValue().getHash());

					}

			).map(Map.Entry::getValue).collect(Collectors.toCollection(TreeSet::new));

		}

		return this.differentFilesSet;

	}

	Set<FmFile> buildDeletedFilesSet() {

		if (this.deletedFilesSet == null) {

			this.deletedFilesSet = super.buildTargetDirectoryFilesMapByRelativePath().entrySet().stream() //
					.filter(sourceFileEntry -> !super.buildSourceDirectoryFilesMapByRelativePath().containsKey(sourceFileEntry.getKey())) //
					.map(Map.Entry::getValue).collect(Collectors.toCollection(TreeSet::new));

		}

		return this.deletedFilesSet;

	}

	Set<FmFile> buildExistingFilesSet() {

		if (this.existingFilesSet == null) {

			this.existingFilesSet = super.buildSourceDirectoryFilesMapByRelativePath().entrySet().stream().filter(

					sourceFileEntry -> {

						FmFile targetFile = super.buildTargetDirectoryFilesMapByRelativePath().get(sourceFileEntry.getKey());

						return targetFile != null && targetFile.getHash().equals(sourceFileEntry.getValue().getHash());

					}

			).map(Map.Entry::getValue).collect(Collectors.toCollection(TreeSet::new));

		}

		return this.existingFilesSet;

	}

	long buildNumberOfNewFiles() {

		if (this.numberOfNewFiles == 0L) {
			this.numberOfNewFiles = this.buildNewFilesSet().size();
		}

		return this.numberOfNewFiles;

	}

	long buildNumberOfDifferentFiles() {

		if (this.numberOfDifferentFiles == 0L) {
			this.numberOfDifferentFiles = this.buildDifferentFilesSet().size();
		}

		return this.numberOfDifferentFiles;

	}

	long buildNumberOfDeletedFiles() {

		if (this.numberOfDeletedFiles == 0L) {
			this.numberOfDeletedFiles = this.buildDeletedFilesSet().size();
		}

		return this.numberOfDeletedFiles;

	}

	long buildNumberOfExistingFiles() {

		if (this.numberOfExistingFiles == 0L) {
			this.numberOfExistingFiles = this.buildExistingFilesSet().size();
		}

		return this.numberOfExistingFiles;

	}

	public Set<FmFile> getNewFilesSet() {
		return this.newFilesSet;
	}

	public Set<FmFile> getExistingFilesSet() {
		return this.existingFilesSet;
	}

	public Set<FmFile> getDifferentFilesSet() {
		return this.differentFilesSet;
	}

	public Set<FmFile> getDeletedFilesSet() {
		return this.deletedFilesSet;
	}

	public long getNumberOfNewFiles() {
		return this.numberOfNewFiles;
	}

	public long getNumberOfDifferentFiles() {
		return this.numberOfDifferentFiles;
	}

	public long getNumberOfDeletedFiles() {
		return this.numberOfDeletedFiles;
	}

	public long getNumberOfExistingFiles() {
		return this.numberOfExistingFiles;
	}

}