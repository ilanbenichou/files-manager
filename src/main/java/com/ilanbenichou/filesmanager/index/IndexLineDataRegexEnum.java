package com.ilanbenichou.filesmanager.index;

public enum IndexLineDataRegexEnum {

	RELATIVE_PATH(".+"),

	LAST_MODIFIED_DATE("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}"),

	HASH("[0-9a-z]+"),

	AUDIO_HASH("[0-9a-z]*"),

	SIZE("\\d+"),

	MIME_TYPE("\\w+/.+");

	private final String regex;

	IndexLineDataRegexEnum(final String regex) {
		this.regex = regex;
	}

	protected String getRegex() {
		return this.regex;
	}

}