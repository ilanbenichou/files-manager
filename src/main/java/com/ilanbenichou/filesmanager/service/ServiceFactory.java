package com.ilanbenichou.filesmanager.service;

import com.ilanbenichou.filesmanager.bean.FmContext;
import com.ilanbenichou.filesmanager.exception.WrappedRuntimeException;
import com.ilanbenichou.filesmanager.service.impl.DefineGoldenSourceService;
import com.ilanbenichou.filesmanager.service.impl.FindDuplicateFilesService;
import com.ilanbenichou.filesmanager.service.impl.FindNewFilesService;
import com.ilanbenichou.filesmanager.service.impl.GenerateReportService;
import com.ilanbenichou.filesmanager.service.impl.RenameFilesService;
import com.ilanbenichou.filesmanager.service.impl.SynchronizeDirectoriesService;
import com.ilanbenichou.filesmanager.service.impl.UndefineGoldenSourceService;

public final class ServiceFactory {

	private ServiceFactory() {
	}

	public static Service buildServiceFromContext(final FmContext context) {

		switch (context.getServiceOptionValueEnum()) {

		case GENERATE_REPORT_SERVICE:
			return new GenerateReportService(context.getSourceDirectoryPath());

		case FIND_NEW_FILES_SERVICE:
			return new FindNewFilesService(context.getSourceDirectoryPath(), context.getTargetDirectoryPath());

		case FIND_DUPLICATE_FILES_SERVICE:
			return new FindDuplicateFilesService(context.getSourceDirectoryPath());

		case SYNCHRONIZE_DIRECTORIES_SERVICE:
			return new SynchronizeDirectoriesService(context.getSourceDirectoryPath(), context.getTargetDirectoryPath());

		case DEFINE_GOLDEN_SOURCE_SERVICE:
			return new DefineGoldenSourceService(context.getSourceDirectoryPath());

		case UNDEFINE_GOLDEN_SOURCE_SERVICE:
			return new UndefineGoldenSourceService(context.getSourceDirectoryPath());

		case RENAME_FILES_SERVICE:
			return new RenameFilesService(context.getSourceDirectoryPath());

		default:
			throw WrappedRuntimeException.wrap(String.format("Unable to build service, unknown service option value [%s] !", context.getServiceOptionValueEnum().getOptionValue()));

		}

	}

}