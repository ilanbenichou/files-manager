package com.ilanbenichou.filesmanager.report.html.builder;

public enum CssClassesEnum {

	LINK("c_lk"), //
	EMPTY("c_empty"), //
	METADATA_KEY("c_mk"), //
	METADATA_VALUE("c_mv");

	private final String value;

	CssClassesEnum(final String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

}
