package com.ilanbenichou.filesmanager.report.html.builder;

import static j2html.TagCreator.div;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.hr;
import static j2html.TagCreator.table;
import static j2html.TagCreator.td;
import static j2html.TagCreator.th;
import static j2html.TagCreator.tr;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.ilanbenichou.filesmanager.config.PropertiesLoader;
import com.ilanbenichou.filesmanager.date.DateHelper;
import com.ilanbenichou.filesmanager.file.FilesHelper;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTable;

import j2html.tags.ContainerTag;
import j2html.tags.EmptyTag;

public final class HtmlReportBuilderHelper {

	private static final Logger LOGGER = Logger.getLogger(HtmlReportBuilderHelper.class);

	protected static final String FLAVICON_PATH = PropertiesLoader.getInstance().getProperties().getReportsResourcesDirectoryPath() + "/images/flavicon.png";

	public enum SectionColorsNumberEnum {

		ONE(1), TWO(2);

		private int value;

		private SectionColorsNumberEnum(final int value) {
			this.value = value;
		}

		protected int getValue() {
			return this.value;
		}

	}

	private HtmlReportBuilderHelper() {
	}

	public static ContainerTag buildSectionTag(final String title, final SectionColorsNumberEnum sectionColorNumber) {

		ContainerTag sectionTag = div().withClass("c_section c_section_color_" + sectionColorNumber.value);
		sectionTag.with(div().withClass("section-title center").with(h2(title)).with(hr()));

		return sectionTag;

	}

	public static ContainerTag buildTableTag(final HtmlTable table) {

		boolean isDatatable = table.getColumnsNameList() != null && !table.getColumnsNameList().isEmpty();

		ContainerTag tableTag = table().withClass("table table-striped" + (isDatatable ? " datatable" : ""));

		if (isDatatable) {

			ContainerTag tableHeadTrTag = tr();
			tableTag.with(new ContainerTag("thead").with(tableHeadTrTag));

			ContainerTag tableFootTrTag = tr();
			tableTag.with(new ContainerTag("tfoot").with(tableFootTrTag));

			table.getColumnsNameList().forEach(

					columnName -> {

						tableHeadTrTag.with(th().withText(columnName));
						tableFootTrTag.with(th().withText(columnName));

					}

			);

		}

		ContainerTag tableBodyTag = new ContainerTag("tbody");
		tableTag.with(tableBodyTag);

		table.getRowsList().forEach(

				row -> {

					ContainerTag tableBodyTrTag = tr();
					tableBodyTag.with(tableBodyTrTag);

					row.getCellsList().forEach(

							cell -> {

								ContainerTag tableBodyTrTdTag = td();
								tableBodyTrTag.with(tableBodyTrTdTag);

								if (cell.getId() != null) {
									tableBodyTrTdTag.withId(cell.getId());
								}

								List<CssClassesEnum> cssClassesList = cell.getCssClassesList();
								if (cssClassesList != null) {

									tableBodyTrTdTag.withClasses( //
											cssClassesList.stream().map(CssClassesEnum::getValue).collect(Collectors.toList()).toArray(new String[cssClassesList.size()]) //
									);

								}

								if (cell.getOnClickJsAction() != null) {
									tableBodyTrTdTag.attr("onclick", cell.getOnClickJsAction());
								}

								if (cell.getColspan() != null) {
									tableBodyTrTdTag.attr("colspan", cell.getColspan().toString());
								}

								if (cell.hasText()) {
									tableBodyTrTdTag.withText(cell.getText());
								} else {
									tableBodyTrTdTag.with(cell.getTag());
								}

							}

				);

				}

		);

		return tableTag;

	}

	public static File write(final HtmlReportBuilder<?> reportBuilder) {

		reportBuilder.build();

		HtmlReportBuilderHelper.LOGGER.info("Starting to write HTML report ...");

		StringBuilder htmlReportFilePathSB = new StringBuilder();

		htmlReportFilePathSB.append("fmreport.");
		htmlReportFilePathSB.append(DateHelper.formatYyyyMmDdHhMmSsSss(reportBuilder.getReportDate()));
		htmlReportFilePathSB.append(".");
		htmlReportFilePathSB.append(reportBuilder.getService().getServiceEnum().getOptionValue());
		htmlReportFilePathSB.append(".htm");

		Path htmlReportFilePath = PropertiesLoader.getInstance().getProperties().getReportsDirectoryPath().resolve(htmlReportFilePathSB.toString());

		FilesHelper.writeFile(htmlReportFilePath, new EmptyTag("!DOCTYPE html").render() + reportBuilder.getHtmlTag().render(), false);

		HtmlReportBuilderHelper.LOGGER.info(String.format("Finished successfully to write HTML report [%s].", htmlReportFilePath.toString()));

		return htmlReportFilePath.toFile();

	}

	public static void open(final File htmlReportFile) {

		if (htmlReportFile == null || !Desktop.isDesktopSupported()) {
			return;
		}

		Desktop desktop = Desktop.getDesktop();

		if (!desktop.isSupported(Desktop.Action.OPEN)) {
			return;
		}

		try {
			desktop.open(htmlReportFile);
		} catch (final IOException ioException) {

			HtmlReportBuilderHelper.LOGGER.warn(String.format("An error occurred while opening HTML report file [%s] !", htmlReportFile.toPath()));

		}

	}

}