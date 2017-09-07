package com.ilanbenichou.filesmanager.report.html.builder.impl;

import java.util.Arrays;
import java.util.List;

import com.ilanbenichou.filesmanager.report.html.builder.HtmlReportBuilderHelper;
import com.ilanbenichou.filesmanager.report.html.builder.SourceServiceHtmlReportBuilder;
import com.ilanbenichou.filesmanager.report.html.builder.SourceTargetServiceHtmlReportBuilder;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTableCell;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTableRow;
import com.ilanbenichou.filesmanager.service.impl.SynchronizeDirectoriesService;

import j2html.tags.ContainerTag;
import j2html.tags.DomContent;

public final class SynchronizeDirectoriesServiceHtmlReportBuilder extends SourceTargetServiceHtmlReportBuilder<SynchronizeDirectoriesService> {

	public SynchronizeDirectoriesServiceHtmlReportBuilder(final SynchronizeDirectoriesService service) {
		super(service);
	}

	@Override
	protected void completeGlobalInformations(final List<HtmlTableRow> rowsList, final SynchronizeDirectoriesService service) {

		HtmlTableCell numberOfNewFilesTableCell = new HtmlTableCell(service.getNumberOfNewFiles(), false);
		numberOfNewFilesTableCell.setColspan(2);
		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Number of new files"), numberOfNewFilesTableCell)));

		HtmlTableCell numberOfDifferentFilesTableCell = new HtmlTableCell(service.getNumberOfDifferentFiles(), false);
		numberOfDifferentFilesTableCell.setColspan(2);
		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Number of different files"), numberOfDifferentFilesTableCell)));

		HtmlTableCell numberOfDeletedFilesTableCell = new HtmlTableCell(service.getNumberOfDeletedFiles(), false);
		numberOfDeletedFilesTableCell.setColspan(2);
		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Number of deleted files"), numberOfDeletedFilesTableCell)));

		HtmlTableCell numberOfExistingFilesTableCell = new HtmlTableCell(service.getNumberOfExistingFiles(), false);
		numberOfExistingFilesTableCell.setColspan(2);
		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Number of existing files"), numberOfExistingFilesTableCell)));

	}

	@Override
	protected void completeBodyTag(ContainerTag bodyTag, SynchronizeDirectoriesService service) {

		bodyTag.with(SynchronizeDirectoriesServiceHtmlReportBuilder.buildNewFilesListSectionTag(service));
		bodyTag.with(SynchronizeDirectoriesServiceHtmlReportBuilder.buildDifferentFilesListSectionTag(service));
		bodyTag.with(SynchronizeDirectoriesServiceHtmlReportBuilder.buildFilesToDeleteListSectionTag(service));
		bodyTag.with(SynchronizeDirectoriesServiceHtmlReportBuilder.buildExistingFilesListSectionTag(service));

	}

	private static DomContent buildNewFilesListSectionTag(final SynchronizeDirectoriesService service) {
		return SourceServiceHtmlReportBuilder.buildSourceFilesListSectionTag("New files list", HtmlReportBuilderHelper.SectionColorsNumberEnum.ONE, service.getNewFilesSet());
	}

	private static DomContent buildDifferentFilesListSectionTag(final SynchronizeDirectoriesService service) {
		return SourceServiceHtmlReportBuilder.buildSourceFilesListSectionTag( //
				"Different files list", //
				HtmlReportBuilderHelper.SectionColorsNumberEnum.TWO, //
				service.getDifferentFilesSet() //
		);
	}

	private static DomContent buildFilesToDeleteListSectionTag(final SynchronizeDirectoriesService service) {
		return SourceServiceHtmlReportBuilder.buildFilesListSectionTag( //
				"Deleted files list", //
				HtmlReportBuilderHelper.SectionColorsNumberEnum.ONE, //
				service.getDeletedFilesSet(), //
				null //
		);
	}

	private static DomContent buildExistingFilesListSectionTag(final SynchronizeDirectoriesService service) {
		return SourceServiceHtmlReportBuilder.buildSourceFilesListSectionTag( //
				"Existing files list", //
				HtmlReportBuilderHelper.SectionColorsNumberEnum.TWO, //
				service.getExistingFilesSet() //
		);
	}

}