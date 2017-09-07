package com.ilanbenichou.filesmanager.service;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.ilanbenichou.filesmanager.bean.FmDirectory;
import com.ilanbenichou.filesmanager.bean.FmFile;

public abstract class SourceService extends Service {

	private final FmDirectory sourceDirectory;

	private final boolean initSourceDirectory;

	private final boolean checkSourceDirectoryIsNotGoldenSource;

	private Map<Path, FmFile> sourceDirectoryFilesMapByRelativePath;

	private Map<String, Set<FmFile>> sourceFilesMapByHash;

	private Map<String, Set<FmFile>> sourceFilesMapByAudioHash;

	protected SourceService( //
			final ServicesEnum serviceEnum, //
			final FmDirectory sourceDirectory, //
			final boolean initSourceDirectory, //
			final boolean checkSourceDirectoryIsNotGoldenSource //
	) {
		super(serviceEnum);
		this.sourceDirectory = sourceDirectory;
		this.initSourceDirectory = initSourceDirectory;
		this.checkSourceDirectoryIsNotGoldenSource = checkSourceDirectoryIsNotGoldenSource;
	}

	protected SourceService( //
			final ServicesEnum serviceEnum, //
			final Path sourceDirectoryPath, //
			final boolean initSourceDirectory, //
			final boolean checkSourceDirectoryIsNotGoldenSource //
	) {
		this(serviceEnum, new FmDirectory(sourceDirectoryPath, "source"), initSourceDirectory, checkSourceDirectoryIsNotGoldenSource);
	}

	/**
	 * Constructor used for Unit tests
	 * 
	 * @param serviceEnum
	 *            Service
	 * @param sourceDirectory
	 *            Source directory
	 */
	protected SourceService(final ServicesEnum serviceEnum, final FmDirectory sourceDirectory) {
		this(serviceEnum, sourceDirectory, false, false);
	}

	@Override
	protected void preHandle() {

		if (this.checkSourceDirectoryIsNotGoldenSource) {
			super.checkDirectoryIsNotGoldenSource(this.sourceDirectory);
		}

		if (this.initSourceDirectory) {
			this.initDirectory(this.sourceDirectory, this.isSourceDirectoryGoldenSource());
		}

	}

	@Override
	protected void postHandle() {
		super.deleteDirectoryEmptyDirectories(this.sourceDirectory);
	}

	public final FmDirectory getSourceDirectory() {
		return this.sourceDirectory;
	}

	protected final Stream<Path> buildSourceDirectoryFilesStream() {
		return super.buildDirectoryFilesStream(this.sourceDirectory);
	}

	protected final Map<Path, FmFile> buildSourceDirectoryFilesMapByRelativePath() {

		if (this.sourceDirectoryFilesMapByRelativePath == null) {
			this.sourceDirectoryFilesMapByRelativePath = super.buildDirectoryFilesMapByRelativePath(this.sourceDirectory);
		}

		return this.sourceDirectoryFilesMapByRelativePath;

	}

	protected final Map<String, Set<FmFile>> buildSourceDirectoryFilesMapByHash() {

		if (this.sourceFilesMapByHash == null) {
			this.sourceFilesMapByHash = super.buildDirectoryFilesMapByHash(this.sourceDirectory);
		}

		return this.sourceFilesMapByHash;

	}

	protected final Map<String, Set<FmFile>> buildSourceDirectoryFilesMapByAudioHash() {

		if (this.sourceFilesMapByAudioHash == null) {
			this.sourceFilesMapByAudioHash = super.buildDirectoryFilesMapByAudioHash(this.sourceDirectory);
		}

		return this.sourceFilesMapByAudioHash;

	}

	protected final Path buildSourceDirectoryGoldenSourcePath() {
		return super.buildDirectoryGoldenSourcePath(this.sourceDirectory);
	}

	public boolean isSourceDirectoryGoldenSource() {
		return this.isDirectoryGoldenSource(this.sourceDirectory);
	}

}