package com.ilanbenichou.filesmanager.file;

public enum FilesEnum {

	OLD_FILES_DIRECTORY("__FM_OLD__", true), //
	OLD_FILES_META_DIFF_DIRECTORY("__FM_OLD_META_DIFF__", true), //
	DUPLICATE_FILES_DIRECTORY("__FM_DUP__", true), //
	DUPLICATE_FILES_META_DIFF_DIRECTORY("__FM_DUP_META_DIFF__", true), //
	GIT_DIRECTORY(".git", true), //
	GIT_IGNORE_FILE(".gitignore", false), //
	INDEX_FILE(".fmindex", false), //
	GOLDEN_SOURCE_FILE(".fmgoldsrc", false), //
	MAC_OS_DS_STORE_FILE(".DS_Store", false), //
	WINDOWS_THUMBS_DB_FILE("Thumbs.db", false);

	private final String name;

	private final boolean isDirectory;

	FilesEnum(final String name, final boolean isDirectory) {
		this.name = name;
		this.isDirectory = isDirectory;
	}

	public String getName() {
		return this.name;
	}

	public boolean isDirectory() {
		return this.isDirectory;
	}

	public boolean isFile() {
		return !this.isDirectory;
	}

}