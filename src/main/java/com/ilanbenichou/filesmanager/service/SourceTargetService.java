package com.ilanbenichou.filesmanager.service;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

import com.ilanbenichou.filesmanager.bean.FmDirectory;
import com.ilanbenichou.filesmanager.bean.FmFile;
import com.ilanbenichou.filesmanager.exception.WrappedRuntimeException;

public abstract class SourceTargetService extends SourceService {

	private final FmDirectory targetDirectory;

	private final boolean initTargetDirectory;

	private final boolean checkTargetDirectoryIsNotGoldenSource;

	private Map<Path, FmFile> targetDirectoryFilesMapByRelativePath;

	private Map<String, Set<FmFile>> targetFilesMapByHash;

	private Map<String, Set<FmFile>> targetFilesMapByAudioHash;

	protected SourceTargetService( //
			final ServicesEnum serviceEnum, //
			final Path sourceDirectoryPath, //
			final boolean initSourceTargetDirectories, //
			final boolean checkSourceDirectoryIsNotGoldenSource, //
			final Path targetDirectoryPath, //
			final boolean checkTargetDirectoryIsNotGoldenSource //
	) {
		super(serviceEnum, sourceDirectoryPath, initSourceTargetDirectories, checkSourceDirectoryIsNotGoldenSource);
		this.targetDirectory = new FmDirectory(targetDirectoryPath, "target");
		this.initTargetDirectory = initSourceTargetDirectories;
		this.checkTargetDirectoryIsNotGoldenSource = checkTargetDirectoryIsNotGoldenSource;
	}

	/**
	 * Constructor used for Unit tests
	 * 
	 * @param serviceEnum
	 *            Service
	 * @param sourceDirectory
	 *            Source directory
	 * @param targetDirectory
	 *            Target directory
	 */
	protected SourceTargetService(final ServicesEnum serviceEnum, final FmDirectory sourceDirectory, final FmDirectory targetDirectory) {
		super(serviceEnum, sourceDirectory);
		this.targetDirectory = targetDirectory;
		this.initTargetDirectory = false;
		this.checkTargetDirectoryIsNotGoldenSource = false;
	}

	@Override
	protected final void preHandle() {

		super.preHandle();

		Path sourceDirectoryPath = super.getSourceDirectory().getPath();
		Path targetDirectoryPath = this.targetDirectory.getPath();

		if (sourceDirectoryPath.startsWith(targetDirectoryPath)) {
			throw WrappedRuntimeException.wrap( //
					String.format( //
							"Unable to execute this service because [%s] source directory is a subdirectory of [%s] target directory !", //
							sourceDirectoryPath, //
							targetDirectoryPath //
					) //
			);
		}

		if (targetDirectoryPath.startsWith(sourceDirectoryPath)) {
			throw WrappedRuntimeException.wrap( //
					String.format( //
							"Unable to execute this service because [%s] target directory is a subdirectory of [%s] source directory !", //
							targetDirectoryPath, //
							sourceDirectoryPath //
					) //
			);
		}

		if (this.checkTargetDirectoryIsNotGoldenSource) {
			super.checkDirectoryIsNotGoldenSource(this.targetDirectory);
		}

		if (this.initTargetDirectory) {
			this.initDirectory(this.targetDirectory, this.isTargetDirectoryGoldenSource());
		}

	}

	@Override
	protected void postHandle() {
		super.postHandle();
		super.deleteDirectoryEmptyDirectories(this.targetDirectory);
	}

	public final FmDirectory getTargetDirectory() {
		return this.targetDirectory;
	}

	protected final Map<Path, FmFile> buildTargetDirectoryFilesMapByRelativePath() {

		if (this.targetDirectoryFilesMapByRelativePath == null) {
			this.targetDirectoryFilesMapByRelativePath = super.buildDirectoryFilesMapByRelativePath(this.targetDirectory);
		}

		return this.targetDirectoryFilesMapByRelativePath;

	}

	protected final Map<String, Set<FmFile>> buildTargetDirectoryFilesMapByHash() {

		if (this.targetFilesMapByHash == null) {
			this.targetFilesMapByHash = super.buildDirectoryFilesMapByHash(this.targetDirectory);
		}

		return this.targetFilesMapByHash;

	}

	protected final Map<String, Set<FmFile>> buildTargetDirectoryFilesMapByAudioHash() {

		if (this.targetFilesMapByAudioHash == null) {
			this.targetFilesMapByAudioHash = super.buildDirectoryFilesMapByAudioHash(this.targetDirectory);
		}

		return this.targetFilesMapByAudioHash;

	}

	protected final Path resolvePathWithTargetDirectory(final Path relativePath) {
		return super.resolvePathWithDirectory(this.targetDirectory, relativePath);
	}

	public boolean isTargetDirectoryGoldenSource() {
		return this.isDirectoryGoldenSource(this.targetDirectory);
	}

}