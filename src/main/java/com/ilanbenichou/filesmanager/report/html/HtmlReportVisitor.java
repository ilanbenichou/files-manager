package com.ilanbenichou.filesmanager.report.html;

import java.io.File;

import com.ilanbenichou.filesmanager.report.ReportVisitor;
import com.ilanbenichou.filesmanager.report.html.builder.HtmlReportBuilder;
import com.ilanbenichou.filesmanager.report.html.builder.HtmlReportBuilderHelper;
import com.ilanbenichou.filesmanager.report.html.builder.impl.FindDuplicateFilesServiceHtmlReportBuilder;
import com.ilanbenichou.filesmanager.report.html.builder.impl.FindNewFilesServiceHtmlReportBuilder;
import com.ilanbenichou.filesmanager.report.html.builder.impl.GenerateReportServiceHtmlReportBuilder;
import com.ilanbenichou.filesmanager.report.html.builder.impl.SynchronizeDirectoriesServiceHtmlReportBuilder;
import com.ilanbenichou.filesmanager.service.impl.FindDuplicateFilesService;
import com.ilanbenichou.filesmanager.service.impl.FindNewFilesService;
import com.ilanbenichou.filesmanager.service.impl.GenerateReportService;
import com.ilanbenichou.filesmanager.service.impl.SynchronizeDirectoriesService;

public final class HtmlReportVisitor implements ReportVisitor {

	private HtmlReportBuilder<?> reportBuilder;

	public void visit(final GenerateReportService service) {
		this.reportBuilder = new GenerateReportServiceHtmlReportBuilder(service);
	}

	public void visit(final FindNewFilesService service) {
		this.reportBuilder = new FindNewFilesServiceHtmlReportBuilder(service);
	}

	public void visit(final FindDuplicateFilesService service) {
		this.reportBuilder = new FindDuplicateFilesServiceHtmlReportBuilder(service);
	}

	public void visit(final SynchronizeDirectoriesService service) {
		this.reportBuilder = new SynchronizeDirectoriesServiceHtmlReportBuilder(service);
	}

	public void writeAndOpenReport() {

		if (this.reportBuilder != null) {

			File htmlReportFile = HtmlReportBuilderHelper.write(this.reportBuilder);

			HtmlReportBuilderHelper.open(htmlReportFile);

		}

	}

}