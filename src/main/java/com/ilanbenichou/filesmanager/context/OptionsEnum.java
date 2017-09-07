package com.ilanbenichou.filesmanager.context;

public enum OptionsEnum {

	SERVICE("r", "service", true, "Service to execute"), //
	SOURCE("s", "source", true, "Source directory to take into account"), //
	TARGET("t", "target", true, "Target directory to take into account");

	private final String opt;

	private final String longOpt;

	private final boolean hasArg;

	private final String description;

	OptionsEnum(final String opt, final String longOpt, final boolean hasArg, final String description) {
		this.opt = opt;
		this.longOpt = longOpt;
		this.hasArg = hasArg;
		this.description = description;
	}

	public String getOpt() {
		return this.opt;
	}

	public String getLongOpt() {
		return this.longOpt;
	}

	public boolean hasArg() {
		return this.hasArg;
	}

	public String getDescription() {
		return this.description;
	}

}