package com.ilanbenichou.filesmanager.file.metadata;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.ilanbenichou.filesmanager.bean.FmFile;
import com.ilanbenichou.filesmanager.file.FmFilesHelper;

public final class FilesMetadatasHelper {

	private static final Logger LOGGER = Logger.getLogger(FilesMetadatasHelper.class);

	private static final ContentHandler CONTENT_HANDLER = new DefaultHandler();

	private static final Parser PARSER = new AutoDetectParser();

	private static final ParseContext PARSE_CONTEXT = new ParseContext();

	public static final String EMPTY_VALUE = "{EMPTY}";

	public static final String NOT_AVAILABLE_VALUE = "{N/A}";

	private FilesMetadatasHelper() {
	}

	private static Map<String, String> buildFileMetadatasSet(final FmFile file) {

		Path fileInitialPath = FmFilesHelper.buildFmFileInitialPath(file);

		FilesMetadatasHelper.LOGGER.debug(String.format("Building metadatas set for file [%s] ...", fileInitialPath));

		Metadata metadata = new Metadata();

		try (

				FileInputStream fileInputStream = new FileInputStream(fileInitialPath.toFile());

		) {

			FilesMetadatasHelper.PARSER.parse(fileInputStream, FilesMetadatasHelper.CONTENT_HANDLER, metadata, FilesMetadatasHelper.PARSE_CONTEXT);

		} catch (final IOException | SAXException | TikaException exception) {
			FilesMetadatasHelper.LOGGER.warn(String.format("Unable to retrieve metadatas for file [%s] !", fileInitialPath));
		}

		Map<String, String> fileMetadatasMap = new TreeMap<>();

		for (String metadataKey : metadata.names()) {
			fileMetadatasMap.put(metadataKey, metadata.get(metadataKey));

		}

		return fileMetadatasMap;

	}

	/**
	 * Returns Map<MetadataKey,Set<MetadataValue>> containing all metadatas of a
	 * files set.
	 * 
	 * @param filesSet
	 *            Files set
	 * @return Map<MetadataKey,Set<MetadataValue>> containing all metadatas of a
	 *         files set
	 */
	public static Map<String, Set<String>> buildFilesMetadatasMap(final Set<FmFile> filesSet) {

		filesSet.forEach(

				file -> {

					if (file.getMetadatasMap() == null) {
						file.setMetadatasMap(FilesMetadatasHelper.buildFileMetadatasSet(file));
					}

				}

		);

		return filesSet.stream().flatMap(file -> file.getMetadatasMap().keySet().stream()) //
				.distinct().collect( //
						Collectors.toMap( //
								Function.identity(), //
								metadataKey -> filesSet.stream().map(file -> file.getMetadatasMap().get(metadataKey)).collect(Collectors.toSet()) //
						) //
		);

	}

	/**
	 * Returns Set<MetadataKey> containing all metadatas key of a files set.
	 * 
	 * @param filesSet
	 *            Files set
	 * @return Set<MetadataKey> containing all metadatas key of a files set
	 */
	public static Set<String> buildFilesMetadatasKeyDifferenceSet(final Set<FmFile> filesSet) {

		return FilesMetadatasHelper.buildFilesMetadatasMap(filesSet) //
				.entrySet().stream().filter(metadata -> metadata.getValue().size() != 1) //
				.map(Entry::getKey).collect(Collectors.toCollection(TreeSet::new));

	}

	/**
	 * Returns Map<MetadataValue,Set<File>> containing all metadata values of a
	 * files set from metadata key.
	 * 
	 * @param metadataKey
	 *            Metadata key
	 * @param filesSet
	 *            Files set
	 * @return Map<MetadataValue,Set<File>> containing all metadata values of a
	 *         files set from metadata key
	 */
	public static Map<String, Set<FmFile>> buildMetadataValuesMap(final String metadataKey, final Set<FmFile> filesSet) {

		return filesSet.stream().collect( //
				Collectors.groupingBy( //
						file -> //
						FilesMetadatasHelper.buildMetadataValue(file.getMetadatasMap().get(metadataKey)), //
						TreeMap::new, //
						Collectors.mapping(Function.identity(), Collectors.toCollection(TreeSet::new)) //
				) //
		);

	}

	public static String buildMetadataValue(final String metadataValue) {

		if (metadataValue == null) {
			return FilesMetadatasHelper.NOT_AVAILABLE_VALUE;
		}

		if (StringUtils.isEmpty(metadataValue)) {
			return FilesMetadatasHelper.EMPTY_VALUE;
		}

		return metadataValue;

	}

}