package com.ilanbenichou.filesmanager.service;

public enum ServicesEnum {

	GENERATE_REPORT_SERVICE("generate_report", "Generate complete report for a directory and build its index", false), //
	FIND_NEW_FILES_SERVICE("find_new", "Find new files between two directories", true), //
	FIND_DUPLICATE_FILES_SERVICE("find_dup", "Find duplicate files in a directory", false), //
	SYNCHRONIZE_DIRECTORIES_SERVICE("sync", "Synchronize two directories", true), //
	DEFINE_GOLDEN_SOURCE_SERVICE("def_gold_src", "Define a directory as the golden source one", false), //
	UNDEFINE_GOLDEN_SOURCE_SERVICE("undef_gold_src", "Undefine a directory which is the golden source one", false), //
	RENAME_FILES_SERVICE("rename", "Rename files in source directory", false);

	private final String optionValue;

	private final String description;

	private final boolean targetDirectoryRequired;

	ServicesEnum(final String optionValue, final String description, final boolean targetDirectoryRequired) {
		this.optionValue = optionValue;
		this.description = description;
		this.targetDirectoryRequired = targetDirectoryRequired;
	}

	public static ServicesEnum getByOptionValue(final String optionValue) {

		for (ServicesEnum serviceEnum : ServicesEnum.values()) {

			if (serviceEnum.getOptionValue().equals(optionValue)) {
				return serviceEnum;
			}

		}

		return null;

	}

	public String getOptionValue() {
		return this.optionValue;
	}

	public String getDescription() {
		return this.description;
	}

	public boolean isTargetDirectoryRequired() {
		return this.targetDirectoryRequired;
	}

}