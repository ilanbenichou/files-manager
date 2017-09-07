package com.ilanbenichou.filesmanager.bean;

import java.nio.file.Path;

public class FmDirectory {

	private final Path path;

	private final String alias;

	private FmIndex index;

	public FmDirectory(final Path path, final String alias) {
		this.path = path;
		this.alias = alias;
	}

	public Path getPath() {
		return this.path;
	}

	public String getAlias() {
		return this.alias;
	}

	public FmIndex getIndex() {
		return this.index;
	}

	public void setIndex(final FmIndex index) {
		this.index = index;
	}

}