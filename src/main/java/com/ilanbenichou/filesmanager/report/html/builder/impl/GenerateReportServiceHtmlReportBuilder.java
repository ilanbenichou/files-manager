package com.ilanbenichou.filesmanager.report.html.builder.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ilanbenichou.filesmanager.bean.FmFile;
import com.ilanbenichou.filesmanager.report.html.builder.HtmlReportBuilderHelper;
import com.ilanbenichou.filesmanager.report.html.builder.SourceServiceHtmlReportBuilder;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTable;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTableCell;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTableRow;
import com.ilanbenichou.filesmanager.service.impl.GenerateReportService;

import j2html.tags.ContainerTag;

public final class GenerateReportServiceHtmlReportBuilder extends SourceServiceHtmlReportBuilder<GenerateReportService> {

	private static class FmDirectoryToReport {

		private long size;

		private long numberOfFiles;

	}

	public GenerateReportServiceHtmlReportBuilder(final GenerateReportService service) {
		super(service);
	}

	@Override
	protected void completeBodyTag(final ContainerTag bodyTag, final GenerateReportService service) {

		bodyTag.with(GenerateReportServiceHtmlReportBuilder.buildDirectoriesListSectionTag(service));
		bodyTag.with(GenerateReportServiceHtmlReportBuilder.buildMimeTypesListSectionTag(service));
		bodyTag.with(GenerateReportServiceHtmlReportBuilder.buildFilesListSectionTag(service));

	}

	private static ContainerTag buildDirectoriesListSectionTag(final GenerateReportService service) {

		ContainerTag directoriesListSectionTag = HtmlReportBuilderHelper.buildSectionTag("Directories list", HtmlReportBuilderHelper.SectionColorsNumberEnum.ONE);

		List<String> columnsNameList = Arrays.asList("Relative directory path", "Size", "Number of files");

		List<HtmlTableRow> rowsList = new ArrayList<>();

		Set<FmFile> filesSet = service.getSourceDirectory().getIndex().getFilesSet();

		Map<Path, FmDirectoryToReport> directoriesToReportMap = new HashMap<>();

		filesSet.forEach(file -> GenerateReportServiceHtmlReportBuilder.updateDirectoriesToReportMapWithFile(directoriesToReportMap, file));

		directoriesToReportMap.forEach(

				(directoryPath, directory) ->

				rowsList.add( //
						new HtmlTableRow( //
								Arrays.asList( //
										new HtmlTableCell(directoryPath.toString(), "openSourceDirectory(this)"), //
										new HtmlTableCell(directory.size, true), new HtmlTableCell(directory.numberOfFiles, false)//
								) //
						) //
				)

		);

		directoriesListSectionTag.with(HtmlReportBuilderHelper.buildTableTag(new HtmlTable(columnsNameList, rowsList)));

		return directoriesListSectionTag;

	}

	private static void updateDirectoriesToReportMapWithFile(final Map<Path, FmDirectoryToReport> directoriesToReportMap, final FmFile file) {

		Path path = Paths.get("/").resolve(file.getInitialRelativePath()).getParent();

		do {

			FmDirectoryToReport directory = directoriesToReportMap.get(path);

			if (directory == null) {
				directory = new FmDirectoryToReport();
				directoriesToReportMap.put(path, directory);
			}

			directory.size += file.getSize();
			directory.numberOfFiles++;

		} while ((path = path.getParent()) != null);

	}

	private static ContainerTag buildMimeTypesListSectionTag(final GenerateReportService service) {

		ContainerTag mimeTypesListSectionTag = HtmlReportBuilderHelper.buildSectionTag("Mime types", HtmlReportBuilderHelper.SectionColorsNumberEnum.TWO);

		List<String> columnsNameList = Arrays.asList("Mime type", "Number of files");

		List<HtmlTableRow> rowsList = new ArrayList<>();

		Set<FmFile> filesSet = service.getSourceDirectory().getIndex().getFilesSet();

		Map<String, Long> numberOfFilesByMimeTypeMap = filesSet.stream().map(FmFile::getMimeType).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		numberOfFilesByMimeTypeMap.forEach(

				(mimeType, numberOfFilesByMimeType) -> rowsList.add(new HtmlTableRow(Arrays.asList(new HtmlTableCell(mimeType), new HtmlTableCell(numberOfFilesByMimeType, false))))

		);

		mimeTypesListSectionTag.with(HtmlReportBuilderHelper.buildTableTag(new HtmlTable(columnsNameList, rowsList)));

		return mimeTypesListSectionTag;

	}

	private static ContainerTag buildFilesListSectionTag(final GenerateReportService service) {

		return SourceServiceHtmlReportBuilder.buildSourceFilesListSectionTag( //
				"Files list", //
				HtmlReportBuilderHelper.SectionColorsNumberEnum.ONE, //
				service.getSourceDirectory().getIndex().getFilesSet() //
		);

	}

}