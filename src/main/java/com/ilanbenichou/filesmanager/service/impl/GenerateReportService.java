package com.ilanbenichou.filesmanager.service.impl;

import java.nio.file.Path;

import com.ilanbenichou.filesmanager.report.ReportVisitor;
import com.ilanbenichou.filesmanager.service.ServicesEnum;
import com.ilanbenichou.filesmanager.service.SourceService;

public final class GenerateReportService extends SourceService {

	public GenerateReportService(final Path sourceDirectoryPath) {
		super(ServicesEnum.GENERATE_REPORT_SERVICE, sourceDirectoryPath, true, false);
	}

	@Override
	protected void innerExecute() {
		super.buildSourceDirectoryFilesMapByHash();
	}

	@Override
	public void accept(final ReportVisitor visitor) {
		visitor.visit(this);
	}

}