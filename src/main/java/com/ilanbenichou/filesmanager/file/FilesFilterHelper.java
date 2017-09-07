package com.ilanbenichou.filesmanager.file;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public final class FilesFilterHelper {

	private FilesFilterHelper() {
	}

	static Predicate<Path> allFilesFilterPredicate() {
		return path -> //
		path != null //
				&& //
				path.toFile().isFile() //
				&& //
				FilesHelper.pathExists(path) //
				&& //
				!Arrays.asList(FilesEnum.WINDOWS_THUMBS_DB_FILE.getName(), FilesEnum.MAC_OS_DS_STORE_FILE.getName()).contains(path.getFileName().toString());
	}

	static Predicate<Path> allDirectoriesFilterPredicate() {
		return FilesHelper::directoryExists;
	}

	static Predicate<Path> filesFilterPredicate() {

		return path -> {

			if (path == null || !path.toFile().isFile()) {
				return false;
			}

			String fileName = path.toFile().getName();

			List<String> pathDividedDirectoriesNameList = new ArrayList<>();
			path.getParent().forEach(directory -> pathDividedDirectoriesNameList.add(directory.toFile().getName()));

			for (FilesEnum fileEnum : FilesEnum.values()) {

				if ((fileEnum.isFile() && fileEnum.getName().equals(fileName)) || (fileEnum.isDirectory() && pathDividedDirectoriesNameList.contains(fileEnum.getName()))) {
					return false;
				}

			}

			return true;

		};

	}

}