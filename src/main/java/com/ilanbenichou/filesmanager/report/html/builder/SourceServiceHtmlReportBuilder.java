package com.ilanbenichou.filesmanager.report.html.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ilanbenichou.filesmanager.bean.FmIndex;
import com.ilanbenichou.filesmanager.file.FmFilesHelper;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTable;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTableCell;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTableRow;
import com.ilanbenichou.filesmanager.service.SourceService;

import j2html.tags.ContainerTag;

public abstract class SourceServiceHtmlReportBuilder<T extends SourceService> extends HtmlReportBuilder<T> {

	protected SourceServiceHtmlReportBuilder(final T service) {
		super(service);
	}

	@Override
	protected void completeGlobalInformationsTag(final ContainerTag globalInformationsTag, final T service) {

		List<HtmlTableRow> rowsList = new ArrayList<>();

		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Processing time"), new HtmlTableCell(service.getProcessingTime()))));

		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Report date"), new HtmlTableCell(super.getReportDate()))));

		HtmlTableCell directoryValueCell = new HtmlTableCell(service.getSourceDirectory().getPath().toString(), "openDirectory(this)");
		directoryValueCell.setId("sourceDirectory");
		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Directory"), directoryValueCell)));

		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Golden source"), new HtmlTableCell(service.isSourceDirectoryGoldenSource()))));

		FmIndex sourceDirectoryIndex = service.getSourceDirectory().getIndex();

		long sourceDirectorySize = sourceDirectoryIndex != null ? FmFilesHelper.buildFilesSetSizeSum(sourceDirectoryIndex.getFilesSet()) : 0L;
		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Size"), new HtmlTableCell(sourceDirectorySize, true))));

		int sourceDirectoryNumberOfFiles = sourceDirectoryIndex != null ? sourceDirectoryIndex.size() : 0;
		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Number of files"), new HtmlTableCell(sourceDirectoryNumberOfFiles))));

		this.completeGlobalInformations(rowsList, service);

		globalInformationsTag.with(HtmlReportBuilderHelper.buildTableTag(new HtmlTable(rowsList)));

	}

	protected void completeGlobalInformations(final List<HtmlTableRow> rowsList, final T service) {
	}

}