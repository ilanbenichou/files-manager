package com.ilanbenichou.filesmanager.bean;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class FmIndex {

	private final Path filePath;

	private final Set<FmFile> filesSet;

	private long lastTemporaryIndexFileBackupTime;

	public FmIndex(final Path filePath) {
		this.filePath = filePath;
		this.filesSet = Collections.synchronizedSet(new TreeSet<>());
	}

	public Path getFilePath() {
		return this.filePath;
	}

	public void addFile(final FmFile file) {
		this.filesSet.add(file);
	}

	public Set<FmFile> getFilesSet() {
		return this.filesSet;
	}

	public FmFile getFile(final FmFile file) {

		for (FmFile fileInIndex : this.filesSet) {
			if (fileInIndex.equals(file)) {
				return fileInIndex;
			}
		}

		return null;

	}

	public int size() {
		return this.filesSet.size();
	}

	public long getLastTemporaryIndexFileBackupTime() {
		return this.lastTemporaryIndexFileBackupTime;
	}

	public void setLastTemporaryIndexFileBackupTime(final long lastTemporaryIndexFileBackupTime) {
		this.lastTemporaryIndexFileBackupTime = lastTemporaryIndexFileBackupTime;
	}

}