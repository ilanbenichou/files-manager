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
import com.ilanbenichou.filesmanager.report.html.builder.SourceTargetServiceHtmlReportBuilder;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTable;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTableCell;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTableCellEmpty;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTableRow;
import com.ilanbenichou.filesmanager.service.impl.FindNewFilesService;
import com.ilanbenichou.filesmanager.service.impl.FindNewFilesService.FileMetadatasDiffDatas;

import j2html.tags.ContainerTag;

public final class FindNewFilesServiceHtmlReportBuilder extends SourceTargetServiceHtmlReportBuilder<FindNewFilesService> {

	public FindNewFilesServiceHtmlReportBuilder(final FindNewFilesService service) {
		super(service);
	}

	@Override
	protected void completeGlobalInformations(final List<HtmlTableRow> rowsList, final FindNewFilesService service) {

		HtmlTableCell numberOfNewFilesTableCell = new HtmlTableCell(service.getNumberOfNewFiles(), false);
		numberOfNewFilesTableCell.setColspan(2);
		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Number of new files"), numberOfNewFilesTableCell)));

		HtmlTableCell numberOfExistingFilesTableCell = new HtmlTableCell(service.getNumberOfExistingFiles(), false);
		numberOfExistingFilesTableCell.setColspan(2);
		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Number of existing files"), numberOfExistingFilesTableCell)));

		HtmlTableCell diskSpaceLostTableCell = new HtmlTableCell(service.getDiskSpaceLost(), true);
		diskSpaceLostTableCell.setColspan(2);
		rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell("Disk space lost"), diskSpaceLostTableCell)));

	}

	@Override
	protected void completeBodyTag(final ContainerTag bodyTag, final FindNewFilesService service) {

		bodyTag.with(FindNewFilesServiceHtmlReportBuilder.buildNewFilesListSectionTag(service));
		bodyTag.with(FindNewFilesServiceHtmlReportBuilder.buildExistingFilesListSectionTag(service));
		bodyTag.with(FindNewFilesServiceHtmlReportBuilder.buildExistingFilesMetaDiffListSectionTag(service));

	}

	private static ContainerTag buildNewFilesListSectionTag(final FindNewFilesService service) {
		return SourceServiceHtmlReportBuilder.buildSourceFilesListSectionTag("New files list", HtmlReportBuilderHelper.SectionColorsNumberEnum.ONE, service.getNewFilesSet());
	}

	private static ContainerTag buildExistingFilesListSectionTag(final FindNewFilesService service) {

		ContainerTag existingFilesListSectionTag = HtmlReportBuilderHelper.buildSectionTag("Existing files list", HtmlReportBuilderHelper.SectionColorsNumberEnum.TWO);

		List<String> columnsNameList = Arrays.asList("Directory", "Relative paths", "Last updates", "Size", "Mime type");

		List<HtmlTableRow> rowsList = new ArrayList<>();

		Map<FmFile, Set<FmFile>> existingFilesMap = service.getExistingFilesMap();

		existingFilesMap.forEach(

				(sourceFile, targetFilesSet) -> {

					List<HtmlTableRow> directoriesPathsTableRowList = new ArrayList<>();
					List<HtmlTableRow> relativePathsTableRowList = new ArrayList<>();
					List<HtmlTableRow> lastUpdatesTableRowList = new ArrayList<>();

					directoriesPathsTableRowList.add(new HtmlTableRow(new HtmlTableCell("[Source]")));

					relativePathsTableRowList.add( //
							new HtmlTableRow( //
									new HtmlTableCell(sourceFile.getInitialRelativePath().toString(), "openMovedSourceFile('" + sourceFile.getUpdatedRelativePath() + "')") //
					) //
					);

					lastUpdatesTableRowList.add(new HtmlTableRow(new HtmlTableCell(sourceFile.getLastModifiedDate())));

					targetFilesSet.forEach(

							file -> {

								directoriesPathsTableRowList.add(new HtmlTableRow(new HtmlTableCell("[Target]")));

								HtmlTableCell relativePathCell = new HtmlTableCell(file.getInitialRelativePath().toString(), "openTargetFile(this)");
								relativePathsTableRowList.add(new HtmlTableRow(relativePathCell));

								lastUpdatesTableRowList.add(new HtmlTableRow(new HtmlTableCell(file.getLastModifiedDate())));

							} //

					);

					rowsList.add( //
							new HtmlTableRow( //
									Arrays.asList( //
											new HtmlTableCell(new HtmlTable(directoriesPathsTableRowList)), //
											new HtmlTableCell(new HtmlTable(relativePathsTableRowList)), //
											new HtmlTableCell(new HtmlTable(lastUpdatesTableRowList)), //
											new HtmlTableCell(sourceFile.getSize(), true), //
											new HtmlTableCell(sourceFile.getMimeType())

									) //
					) //
					);

				}

		);

		existingFilesListSectionTag.with(HtmlReportBuilderHelper.buildTableTag(new HtmlTable(columnsNameList, rowsList)));

		return existingFilesListSectionTag;

	}

	private static ContainerTag buildExistingFilesMetaDiffListSectionTag(final FindNewFilesService service) {

		ContainerTag existingFilesListMetaDiffSectionTag = HtmlReportBuilderHelper.buildSectionTag( //
				"Existing files list with different metadatas", //
				HtmlReportBuilderHelper.SectionColorsNumberEnum.TWO //
		);

		List<String> columnsNameList = Arrays.asList("Directory", "Relative paths", "Last updates", "Size", "Mime type", "Metadatas");

		List<HtmlTableRow> rowsList = new ArrayList<>();

		Map<FmFile, FileMetadatasDiffDatas> existingFilesMetaDiffMap = service.getExistingFilesMetaDiffMap();

		existingFilesMetaDiffMap.forEach(

				(sourceFile, fileMetadatasDiffDatas) -> {

					List<HtmlTableRow> directoriesPathsTableRowList = new ArrayList<>();
					List<HtmlTableRow> relativePathsTableRowList = new ArrayList<>();
					List<HtmlTableRow> lastUpdatesTableRowList = new ArrayList<>();
					List<HtmlTableRow> sizesTableRowList = new ArrayList<>();
					List<HtmlTableRow> mimeTypesTableRowList = new ArrayList<>();
					List<HtmlTableRow> metadatasTableRowList = new ArrayList<>();

					if (!fileMetadatasDiffDatas.getFilesMetadatasKeyDifferenceSet().isEmpty()) {

						directoriesPathsTableRowList.add(new HtmlTableRow(new HtmlTableCellEmpty()));
						relativePathsTableRowList.add(new HtmlTableRow(new HtmlTableCellEmpty()));
						lastUpdatesTableRowList.add(new HtmlTableRow(new HtmlTableCellEmpty()));
						sizesTableRowList.add(new HtmlTableRow(new HtmlTableCellEmpty()));
						mimeTypesTableRowList.add(new HtmlTableRow(new HtmlTableCellEmpty()));

						metadatasTableRowList.add( //
								new HtmlTableRow( //
										fileMetadatasDiffDatas.getFilesMetadatasKeyDifferenceSet().stream().map( //
												metadataKey -> new HtmlTableCell("[" + metadataKey + "]").addCssClass(CssClassesEnum.METADATA_KEY) //
						).collect(Collectors.toList()) //
						) //
						);

					}

					directoriesPathsTableRowList.add(new HtmlTableRow(new HtmlTableCell("[Source]")));

					relativePathsTableRowList.add( //
							new HtmlTableRow( //
									new HtmlTableCell(sourceFile.getInitialRelativePath().toString(), "openMovedSourceFile('" + sourceFile.getUpdatedRelativePath() + "')") //
					) //
					);

					lastUpdatesTableRowList.add(new HtmlTableRow(new HtmlTableCell(sourceFile.getLastModifiedDate())));
					sizesTableRowList.add(new HtmlTableRow(new HtmlTableCell(sourceFile.getSize(), true)));
					mimeTypesTableRowList.add(new HtmlTableRow(new HtmlTableCell(sourceFile.getMimeType())));

					List<HtmlTableCell> sourceFileMetadatasValuesList = fileMetadatasDiffDatas.getFilesMetadatasKeyDifferenceSet().stream().map(

							metadatasKey -> new HtmlTableCell(FilesMetadatasHelper.buildMetadataValue(sourceFile.getMetadatasMap().get(metadatasKey))) //
									.addCssClass(CssClassesEnum.METADATA_VALUE) //

					).collect(Collectors.toList());
					metadatasTableRowList.add(new HtmlTableRow(sourceFileMetadatasValuesList));

					fileMetadatasDiffDatas.getTargetFilesSet().forEach(

							file -> {

								directoriesPathsTableRowList.add(new HtmlTableRow(new HtmlTableCell("[Target]")));

								HtmlTableCell relativePathCell = new HtmlTableCell(file.getInitialRelativePath().toString(), "openTargetFile(this)");
								relativePathsTableRowList.add(new HtmlTableRow(relativePathCell));

								lastUpdatesTableRowList.add(new HtmlTableRow(new HtmlTableCell(file.getLastModifiedDate())));
								sizesTableRowList.add(new HtmlTableRow(new HtmlTableCell(file.getSize(), true)));
								mimeTypesTableRowList.add(new HtmlTableRow(new HtmlTableCell(file.getMimeType())));

								List<HtmlTableCell> targetFilemetadatasValuesList = fileMetadatasDiffDatas.getFilesMetadatasKeyDifferenceSet().stream().map(

										metadatasKey -> new HtmlTableCell(FilesMetadatasHelper.buildMetadataValue(file.getMetadatasMap().get(metadatasKey))) //
												.addCssClass(CssClassesEnum.METADATA_VALUE) //

								).collect(Collectors.toList());
								metadatasTableRowList.add(new HtmlTableRow(targetFilemetadatasValuesList));

							} //

					);

					rowsList.add( //
							new HtmlTableRow( //
									Arrays.asList( //
											new HtmlTableCell(new HtmlTable(directoriesPathsTableRowList)), //
											new HtmlTableCell(new HtmlTable(relativePathsTableRowList)), //
											new HtmlTableCell(new HtmlTable(lastUpdatesTableRowList)), //
											new HtmlTableCell(new HtmlTable(sizesTableRowList)), //
											new HtmlTableCell(new HtmlTable(mimeTypesTableRowList)), //
											new HtmlTableCell(new HtmlTable(metadatasTableRowList))) //
					) //
					);

				}

		);

		existingFilesListMetaDiffSectionTag.with(HtmlReportBuilderHelper.buildTableTag(new HtmlTable(columnsNameList, rowsList)));

		return existingFilesListMetaDiffSectionTag;

	}

}