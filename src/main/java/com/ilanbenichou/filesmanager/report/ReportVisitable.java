package com.ilanbenichou.filesmanager.report;

@FunctionalInterface
public interface ReportVisitable {

	public void accept(final ReportVisitor visitor);

}