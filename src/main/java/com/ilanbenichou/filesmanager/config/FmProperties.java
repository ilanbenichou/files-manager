package com.ilanbenichou.filesmanager.config;

import java.nio.file.Path;

public final class FmProperties {

	private int waitingTimeToWriteIndexFileSec;

	private Path reportsResourcesDirectoryPath;

	private Path reportsDirectoryPath;

	protected FmProperties() {
	}

	public int getWaitingTimeToWriteIndexFileSec() {
		return this.waitingTimeToWriteIndexFileSec;
	}

	public void setWaitingTimeToWriteIndexFileSec(final int waitingTimeToWriteIndexFileSec) {
		this.waitingTimeToWriteIndexFileSec = waitingTimeToWriteIndexFileSec;
	}

	public Path getReportsResourcesDirectoryPath() {
		return this.reportsResourcesDirectoryPath;
	}

	public void setReportsResourcesDirectoryPath(final Path reportsResourcesDirectoryPath) {
		this.reportsResourcesDirectoryPath = reportsResourcesDirectoryPath;
	}

	public Path getReportsDirectoryPath() {
		return this.reportsDirectoryPath;
	}

	public void setReportsDirectoryPath(final Path reportsDirectoryPath) {
		this.reportsDirectoryPath = reportsDirectoryPath;
	}

}