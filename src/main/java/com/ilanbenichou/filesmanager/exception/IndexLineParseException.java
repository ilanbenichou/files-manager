package com.ilanbenichou.filesmanager.exception;

public final class IndexLineParseException extends Exception {

	private static final long serialVersionUID = -5410152869676576466L;

	public IndexLineParseException(final String message) {
		super(message);
	}

	public IndexLineParseException(final String message, final Throwable cause) {
		super(message, cause);
	}

}