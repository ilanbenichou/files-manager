package com.ilanbenichou.filesmanager.index;

import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.ilanbenichou.filesmanager.bean.FmFile;
import com.ilanbenichou.filesmanager.exception.IndexLineParseException;
import com.ilanbenichou.filesmanager.exception.WrappedRuntimeException;

public final class IndexLineFormatter {

	private static IndexLineFormatter instance;

	private static final String INDEX_FILE_DATAS_SEPARATOR = "§§";

	private final Pattern indexLinePattern;

	private IndexLineFormatter() {
		this.indexLinePattern = IndexLineFormatter.buildIndexLinePattern();
	}

	protected static IndexLineFormatter getInstance() {

		if (IndexLineFormatter.instance == null) {
			IndexLineFormatter.instance = new IndexLineFormatter();
		}

		return IndexLineFormatter.instance;
	}

	private static Pattern buildIndexLinePattern() {

		StringBuilder indexLineRegexSB = new StringBuilder();

		IndexLineDataRegexEnum[] indexLineDataRegexEnumArray = IndexLineDataRegexEnum.values();

		for (IndexLineDataRegexEnum indexLineDataRegexEnum : indexLineDataRegexEnumArray) {

			boolean addDatasSeparator = indexLineDataRegexEnum.ordinal() < indexLineDataRegexEnumArray.length - 1;

			indexLineRegexSB.append("(");
			indexLineRegexSB.append(indexLineDataRegexEnum.getRegex());
			indexLineRegexSB.append(")");
			indexLineRegexSB.append(addDatasSeparator ? IndexLineFormatter.INDEX_FILE_DATAS_SEPARATOR : "");

		}

		return Pattern.compile(indexLineRegexSB.toString());

	}

	private static SimpleDateFormat buildLastModifiedDateSdf() {

		SimpleDateFormat lastModifiedDateSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		lastModifiedDateSdf.setLenient(false);
		return lastModifiedDateSdf;

	}

	private String buildLineData(final Matcher indexLineMatcher, final IndexLineDataRegexEnum indexLineDataRegexEnum) {
		String lineData = indexLineMatcher.group(indexLineDataRegexEnum.ordinal() + 1);
		return StringUtils.isNotBlank(lineData) ? lineData : null;
	}

	FmFile parse(final String indexLine, final Path directoryPath, final Path indexFileParentRelativePath) throws IndexLineParseException {

		Matcher indexLineMatcher = this.indexLinePattern.matcher(indexLine);

		if (indexLineMatcher.matches()) {

			Path initialRelativePath = indexFileParentRelativePath.resolve(this.buildLineData(indexLineMatcher, IndexLineDataRegexEnum.RELATIVE_PATH));

			String lastModifiedDateString = this.buildLineData(indexLineMatcher, IndexLineDataRegexEnum.LAST_MODIFIED_DATE);
			Date lastModifiedDate = null;
			try {
				lastModifiedDate = IndexLineFormatter.buildLastModifiedDateSdf().parse(lastModifiedDateString);
			} catch (final ParseException parseException) {
				throw new IndexLineParseException(String.format("Last modified date [%s] in line [%s] is NOT a valid date !", lastModifiedDateString, indexLine), parseException);
			}

			long size = Long.parseLong(this.buildLineData(indexLineMatcher, IndexLineDataRegexEnum.SIZE));
			String mimeType = this.buildLineData(indexLineMatcher, IndexLineDataRegexEnum.MIME_TYPE);
			String hash = this.buildLineData(indexLineMatcher, IndexLineDataRegexEnum.HASH);
			String audioHash = this.buildLineData(indexLineMatcher, IndexLineDataRegexEnum.AUDIO_HASH);

			return new FmFile(directoryPath, initialRelativePath, lastModifiedDate, size, mimeType, hash, audioHash);

		} else {
			throw new IndexLineParseException(String.format("Bad format of line [%s]", indexLine));
		}

	}

	private String fileDataToString(final FmFile file, final IndexLineDataRegexEnum indexLineDataRegexEnum) {

		String fileData = null;

		switch (indexLineDataRegexEnum) {
		case RELATIVE_PATH:
			fileData = file.getInitialRelativePath().toString();
			break;
		case LAST_MODIFIED_DATE:
			fileData = IndexLineFormatter.buildLastModifiedDateSdf().format(file.getLastModifiedDate());
			break;
		case SIZE:
			fileData = String.valueOf(file.getSize());
			break;
		case MIME_TYPE:
			fileData = String.valueOf(file.getMimeType());
			break;
		case HASH:
			fileData = file.getHash();
			break;
		case AUDIO_HASH:
			fileData = file.getAudioHash() != null ? file.getAudioHash() : "";
			break;
		default:
			WrappedRuntimeException.wrap(String.format("[%s] index line data enum has not been added to format file data !", indexLineDataRegexEnum.name()));
		}

		return fileData;
	}

	protected String format(final FmFile file) {

		StringBuilder indexLineSB = new StringBuilder();

		IndexLineDataRegexEnum[] indexLineDataRegexEnumArray = IndexLineDataRegexEnum.values();

		for (IndexLineDataRegexEnum indexLineDataRegexEnum : indexLineDataRegexEnumArray) {

			boolean addDatasSeparator = indexLineDataRegexEnum.ordinal() < indexLineDataRegexEnumArray.length - 1;

			indexLineSB.append(this.fileDataToString(file, indexLineDataRegexEnum));
			indexLineSB.append(addDatasSeparator ? IndexLineFormatter.INDEX_FILE_DATAS_SEPARATOR : "");

		}

		return indexLineSB.toString();

	}

}