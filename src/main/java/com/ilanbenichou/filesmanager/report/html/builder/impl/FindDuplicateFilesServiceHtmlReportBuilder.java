package com.ilanbenichou.filesmanager.report.html.builder.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ilanbenichou.filesmanager.bean.FmFile;
import com.ilanbenichou.filesmanager.file.metadata.FilesMetadatasHelper;
import com.ilanbenichou.filesmanager.report.html.builder.CssClassesEnum;
import com.ilanbenichou.filesmanager.report.html.builder.HtmlReportBuilderHelper;
import com.ilanbenichou.filesmanager.report.html.builder.SourceServiceHtmlReportBuilder;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTable;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTableCell;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTableCellEmpty;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTableRow;
import com.ilanbenichou.filesmanager.service.impl.FindDuplicateFilesService;

import j2html.tags.ContainerTag;

public final class FindDuplicateFilesServiceHtmlReportBuilder extends SourceServiceHtmlReportBuilder<FindDuplicateFilesService> {

	public FindDuplicateFilesServiceHtmlReportBuilder(final FindDuplicateFilesService service) {
		super(service);
	}

	@Override
	protected void completeGlobalInformations(final List<HtmlTableRow> rowsList, final FindDuplicateFilesService service) {

		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Number of duplicate file pairs"), new HtmlTableCell(service.getNumberOfDuplicateFilePairs(), false))));

		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Number of duplicate files"), new HtmlTableCell(service.getNumberOfDuplicateFiles(), false))));

		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Number of unique files"), new HtmlTableCell(service.getNumberOfUniqueFiles(), false))));

		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Disk space lost"), new HtmlTableCell(service.getDiskSpaceLost(), true))));

	}

	@Override
	protected void completeBodyTag(final ContainerTag bodyTag, final FindDuplicateFilesService service) {

		bodyTag.with(FindDuplicateFilesServiceHtmlReportBuilder.buildDuplicateFilesListSectionTag(service));
		bodyTag.with(FindDuplicateFilesServiceHtmlReportBuilder.buildDuplicateFilesMetaDiffListSectionTag(service));
		bodyTag.with(FindDuplicateFilesServiceHtmlReportBuilder.buildUniqueFilesListSectionTag(service));

	}

	private static ContainerTag buildDuplicateFilesListSectionTag(final FindDuplicateFilesService service) {

		ContainerTag duplicateFilesListSectionTag = HtmlReportBuilderHelper.buildSectionTag("Duplicate files list", HtmlReportBuilderHelper.SectionColorsNumberEnum.ONE);

		List<String> columnsNameList = Arrays.asList("Relative paths", "Last updates", "Size", "Mime type");

		List<HtmlTableRow> rowsList = new ArrayList<>();

		Set<Set<FmFile>> duplicateFilesSetSet = service.getDuplicateFilesSetSet();

		duplicateFilesSetSet.forEach(

				duplicateFilesSet -> {

					List<HtmlTableRow> relativePathsTableRowList = new ArrayList<>();
					List<HtmlTableRow> lastUpdatesTableRowList = new ArrayList<>();

					duplicateFilesSet.forEach(

							file -> {

								HtmlTableCell relativePathCell = new HtmlTableCell( //
										file.getInitialRelativePath().toString(), //
										"openMovedSourceFile('" + file.getUpdatedRelativePath() + "')" //
								);
								relativePathsTableRowList.add(new HtmlTableRow(relativePathCell));

								lastUpdatesTableRowList.add(new HtmlTableRow(new HtmlTableCell(file.getLastModifiedDate())));

							} //

					);

					FmFile firstFile = duplicateFilesSet.stream().findFirst().get();

					rowsList.add( //
							new HtmlTableRow( //
									Arrays.asList( //
											new HtmlTableCell(new HtmlTable(relativePathsTableRowList)), //
											new HtmlTableCell(new HtmlTable(lastUpdatesTableRowList)), //
											new HtmlTableCell(firstFile.getSize(), true), //
											new HtmlTableCell(firstFile.getMimeType())

									) //
					) //
					);

				}

		);

		duplicateFilesListSectionTag.with(HtmlReportBuilderHelper.buildTableTag(new HtmlTable(columnsNameList, rowsList)));

		return duplicateFilesListSectionTag;

	}

	private static ContainerTag buildDuplicateFilesMetaDiffListSectionTag(final FindDuplicateFilesService service) {

		ContainerTag duplicateFilesMetaDiffListSectionTag = HtmlReportBuilderHelper.buildSectionTag( //
				"Duplicate files list with different metadatas", //
				HtmlReportBuilderHelper.SectionColorsNumberEnum.TWO //
		);

		List<String> columnsNameList = Arrays.asList("Relative paths", "Last updates", "Size", "Mime type", "Metadatas");

		List<HtmlTableRow> rowsList = new ArrayList<>();

		Map<Set<FmFile>, Set<String>> duplicateFilesMetaDiffSetMap = service.getDuplicateFilesMetaDiffSetMap();

		duplicateFilesMetaDiffSetMap.forEach(

				(duplicateFilesMetaDiffSet, metadatasKeyDiffSet) -> {

					List<HtmlTableRow> relativePathsTableRowList = new ArrayList<>();
					List<HtmlTableRow> lastUpdatesTableRowList = new ArrayList<>();
					List<HtmlTableRow> sizesTableRowList = new ArrayList<>();
					List<HtmlTableRow> mimeTypesTableRowList = new ArrayList<>();
					List<HtmlTableRow> metadatasTableRowList = new ArrayList<>();

					if (!metadatasKeyDiffSet.isEmpty()) {

						relativePathsTableRowList.add(new HtmlTableRow(new HtmlTableCellEmpty()));
						lastUpdatesTableRowList.add(new HtmlTableRow(new HtmlTableCellEmpty()));
						sizesTableRowList.add(new HtmlTableRow(new HtmlTableCellEmpty()));
						mimeTypesTableRowList.add(new HtmlTableRow(new HtmlTableCellEmpty()));

						metadatasTableRowList.add( //
								new HtmlTableRow( //
										metadatasKeyDiffSet.stream().map( //
												metadataKey -> new HtmlTableCell("[" + metadataKey + "]").addCssClass(CssClassesEnum.METADATA_KEY) //
						).collect(Collectors.toList()) //
						) //
						);

					}

					duplicateFilesMetaDiffSet.forEach(

							file -> {

								HtmlTableCell relativePathCell = new HtmlTableCell( //
										file.getInitialRelativePath().toString(), //
										"openMovedSourceFile('" + file.getUpdatedRelativePath() + "')" //
								);
								relativePathsTableRowList.add(new HtmlTableRow(relativePathCell));

								lastUpdatesTableRowList.add(new HtmlTableRow(new HtmlTableCell(file.getLastModifiedDate())));
								sizesTableRowList.add(new HtmlTableRow(new HtmlTableCell(file.getSize(), true)));
								mimeTypesTableRowList.add(new HtmlTableRow(new HtmlTableCell(file.getMimeType())));

								List<HtmlTableCell> metadatasValuesList = metadatasKeyDiffSet.stream().map(

										metadatasKey -> new HtmlTableCell(FilesMetadatasHelper.buildMetadataValue(file.getMetadatasMap().get(metadatasKey))) //
												.addCssClass(CssClassesEnum.METADATA_VALUE) //

								).collect(Collectors.toList());
								metadatasTableRowList.add(new HtmlTableRow(metadatasValuesList));

							} //

					);

					rowsList.add( //
							new HtmlTableRow( //
									Arrays.asList( //
											new HtmlTableCell(new HtmlTable(relativePathsTableRowList)), //
											new HtmlTableCell(new HtmlTable(lastUpdatesTableRowList)), //
											new HtmlTableCell(new HtmlTable(sizesTableRowList)), //
											new HtmlTableCell(new HtmlTable(mimeTypesTableRowList)), //
											new HtmlTableCell(new HtmlTable(metadatasTableRowList)) //
					) //
					) //
					);

				}

		);

		duplicateFilesMetaDiffListSectionTag.with(HtmlReportBuilderHelper.buildTableTag(new HtmlTable(columnsNameList, rowsList)));

		return duplicateFilesMetaDiffListSectionTag;

	}

	private static ContainerTag buildUniqueFilesListSectionTag(final FindDuplicateFilesService service) {
		return SourceServiceHtmlReportBuilder.buildSourceFilesListSectionTag("Unique files list", HtmlReportBuilderHelper.SectionColorsNumberEnum.ONE, service.getUniqueFilesSet());
	}

}