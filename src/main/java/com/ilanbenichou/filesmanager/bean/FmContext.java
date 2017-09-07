package com.ilanbenichou.filesmanager.bean;

import java.nio.file.Path;

import com.ilanbenichou.filesmanager.service.ServicesEnum;

public final class FmContext {

	private ServicesEnum serviceOptionValueEnum;

	private Path sourceDirectoryPath;

	private Path targetDirectoryPath;

	public ServicesEnum getServiceOptionValueEnum() {
		return this.serviceOptionValueEnum;
	}

	public void setServiceOptionValuesEnum(final ServicesEnum serviceOptionValueEnum) {
		this.serviceOptionValueEnum = serviceOptionValueEnum;
	}

	public Path getSourceDirectoryPath() {
		return this.sourceDirectoryPath;
	}

	public void setSourceDirectoryPath(final Path sourceDirectoryPath) {
		this.sourceDirectoryPath = sourceDirectoryPath;
	}

	public Path getTargetDirectoryPath() {
		return this.targetDirectoryPath;
	}

	public void setTargetDirectoryPath(final Path targetDirectoryPath) {
		this.targetDirectoryPath = targetDirectoryPath;
	}

}