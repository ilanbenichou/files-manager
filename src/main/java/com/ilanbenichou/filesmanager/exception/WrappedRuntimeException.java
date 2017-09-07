package com.ilanbenichou.filesmanager.exception;

import org.apache.log4j.Logger;

public final class WrappedRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 9126641521335299617L;

	private static final Logger LOGGER = Logger.getLogger(WrappedRuntimeException.class);

	private WrappedRuntimeException(final String message) {
		super(message);
	}

	private WrappedRuntimeException(final Throwable cause) {
		super(cause);
	}

	private WrappedRuntimeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public static WrappedRuntimeException wrap(final String message) {
		WrappedRuntimeException.LOGGER.error(message);
		return new WrappedRuntimeException(message);
	}

	public static WrappedRuntimeException wrap(final Throwable cause) {
		WrappedRuntimeException.LOGGER.error(cause);
		return new WrappedRuntimeException(cause);
	}

	public static WrappedRuntimeException wrap(final String message, final Throwable cause) {
		WrappedRuntimeException.LOGGER.error(message, cause);
		return new WrappedRuntimeException(message, cause);
	}

}