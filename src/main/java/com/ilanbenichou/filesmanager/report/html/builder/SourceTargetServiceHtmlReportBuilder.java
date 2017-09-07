package com.ilanbenichou.filesmanager.report.html.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ilanbenichou.filesmanager.bean.FmIndex;
import com.ilanbenichou.filesmanager.file.FmFilesHelper;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTable;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTableCell;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTableCellEmpty;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTableRow;
import com.ilanbenichou.filesmanager.service.SourceTargetService;

import j2html.tags.ContainerTag;

public abstract class SourceTargetServiceHtmlReportBuilder<T extends SourceTargetService> extends HtmlReportBuilder<T> {

	protected SourceTargetServiceHtmlReportBuilder(final T service) {
		super(service);
	}

	@Override
	protected void completeGlobalInformationsTag(ContainerTag globalInformationsTag, final T service) {

		List<HtmlTableRow> rowsList = new ArrayList<>();

		HtmlTableCell processingTimeValueCell = new HtmlTableCell(service.getProcessingTime());
		processingTimeValueCell.setColspan(2);
		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Processing time"), processingTimeValueCell)));

		HtmlTableCell reportDateValueCell = new HtmlTableCell(super.getReportDate());
		reportDateValueCell.setColspan(2);
		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Report date"), reportDateValueCell)));

		HtmlTableCell cellEmpty = new HtmlTableCellEmpty();
		cellEmpty.setColspan(3);

		rowsList.add(new HtmlTableRow(Arrays.asList(cellEmpty)));
		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCellEmpty(), new HtmlTableCell("Source directory"), new HtmlTableCell("Target directory"))));

		HtmlTableCell sourceDirectoryValueCell = new HtmlTableCell(service.getSourceDirectory().getPath().toString(), "openDirectory(this)");
		sourceDirectoryValueCell.setId("sourceDirectory");
		HtmlTableCell targetDirectoryValueCell = new HtmlTableCell(service.getTargetDirectory().getPath().toString(), "openDirectory(this)");
		targetDirectoryValueCell.setId("targetDirectory");
		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Directory"), sourceDirectoryValueCell, targetDirectoryValueCell)));

		rowsList.add( //
				new HtmlTableRow( //
						Arrays.asList( //
								new HtmlTableCell("Golden source"), //
								new HtmlTableCell(service.isSourceDirectoryGoldenSource()), //
								new HtmlTableCell(service.isTargetDirectoryGoldenSource()) //
						) //
				) //
		);

		FmIndex sourceDirectoryIndex = service.getSourceDirectory().getIndex();
		FmIndex targetDirectoryIndex = service.getTargetDirectory().getIndex();

		long sourceDirectorySize = sourceDirectoryIndex != null ? FmFilesHelper.buildFilesSetSizeSum(sourceDirectoryIndex.getFilesSet()) : 0L;
		long targetDirectorySize = targetDirectoryIndex != null ? FmFilesHelper.buildFilesSetSizeSum(targetDirectoryIndex.getFilesSet()) : 0L;
		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Size"), new HtmlTableCell(sourceDirectorySize, true), new HtmlTableCell(targetDirectorySize, true))));

		int sourceDirectoryNumberOfFiles = sourceDirectoryIndex != null ? sourceDirectoryIndex.size() : 0;
		int targetDirectoryNumberOfFiles = targetDirectoryIndex != null ? targetDirectoryIndex.size() : 0;
		rowsList.add(new HtmlTableRow( //
				Arrays.asList( //
						new HtmlTableCell("Number of files"), //
						new HtmlTableCell(sourceDirectoryNumberOfFiles), //
						new HtmlTableCell(targetDirectoryNumberOfFiles) //
				) //
		) //
		);

		this.completeGlobalInformations(rowsList, service);

		globalInformationsTag.with(HtmlReportBuilderHelper.buildTableTag(new HtmlTable(rowsList)));

	}

	protected void completeGlobalInformations(final List<HtmlTableRow> rowsList, final T service) {
	}

}