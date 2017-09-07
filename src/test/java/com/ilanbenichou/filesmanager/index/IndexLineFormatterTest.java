package com.ilanbenichou.filesmanager.index;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.ilanbenichou.filesmanager.bean.FmFile;
import com.ilanbenichou.filesmanager.exception.IndexLineParseException;

public final class IndexLineFormatterTest {

	private SimpleDateFormat lastModifiedDateSdf;

	@Before
	public void tearDown() {
		this.lastModifiedDateSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		this.lastModifiedDateSdf.setLenient(false);
	}

	@Test
	public final void testParseWithValidIndexLine1() throws ParseException {

		String indexLine = "path/file§§2016-09-30 12:30:12.123§§filehash123§§filehash456§§1234§§audio/mpeg";

		try {
			FmFile indexFile = IndexLineFormatter.getInstance().parse(indexLine, Paths.get("/parent"), Paths.get(""));
			Assertions.assertThat(indexFile).isNotNull();
			Assertions.assertThat(indexFile.getParentPath()).isEqualTo(Paths.get("/parent"));
			Assertions.assertThat(indexFile.getInitialRelativePath()).isEqualTo(Paths.get("path/file"));
			Assertions.assertThat(indexFile.getLastModifiedDate()).isEqualTo(this.lastModifiedDateSdf.parse("2016-09-30 12:30:12.123"));
			Assertions.assertThat(indexFile.getHash()).isEqualTo("filehash123");
			Assertions.assertThat(indexFile.getAudioHash()).isEqualTo("filehash456");
			Assertions.assertThat(indexFile.getSize()).isEqualTo(1234);
			Assertions.assertThat(indexFile.getMimeType()).isEqualTo("audio/mpeg");
		} catch (final IndexLineParseException indexLineParseException) {
			Assertions.fail("Exception IndexLineParseException NOT expected here !");
		}

	}

	@Test
	public final void testParseWithValidIndexLine2() throws ParseException {

		String indexLine = "path/file§§2016-09-30 12:30:12.123§§filehash123§§§§1234§§image/png";

		try {
			FmFile indexFile = IndexLineFormatter.getInstance().parse(indexLine, Paths.get("/parent"), Paths.get("subFolder"));
			Assertions.assertThat(indexFile).isNotNull();
			Assertions.assertThat(indexFile.getParentPath()).isEqualTo(Paths.get("/parent"));
			Assertions.assertThat(indexFile.getInitialRelativePath()).isEqualTo(Paths.get("subFolder/path/file"));
			Assertions.assertThat(indexFile.getLastModifiedDate()).isEqualTo(this.lastModifiedDateSdf.parse("2016-09-30 12:30:12.123"));
			Assertions.assertThat(indexFile.getHash()).isEqualTo("filehash123");
			Assertions.assertThat(indexFile.getAudioHash()).isNull();
			Assertions.assertThat(indexFile.getSize()).isEqualTo(1234);
			Assertions.assertThat(indexFile.getMimeType()).isEqualTo("image/png");
		} catch (final IndexLineParseException indexLineParseException) {
			Assertions.fail("Exception IndexLineParseException NOT expected here !");
		}

	}

	@Test
	public final void testParseWithInvalidFormat1() throws ParseException {

		String indexLine = "path/file§§2016-09-30 27:30:12.12§§filehash123§§filehash456§§1234§§audio/mpeg";

		try {
			IndexLineFormatter.getInstance().parse(indexLine, Paths.get("/parent"), Paths.get(""));
			Assertions.fail("Exception IndexLineParseException expected here !");
		} catch (final IndexLineParseException indexLineParseException) {
			Assertions.assertThat(indexLineParseException.getMessage()).contains("Bad format of line");
		}

	}

	@Test
	public final void testParseWithInvalidFormat2() throws ParseException {

		String indexLine = "path/file§§2016-09-55 18:30:12.123§§filehash123§§filehash456§§1234§§audio/mpeg";

		try {
			IndexLineFormatter.getInstance().parse(indexLine, Paths.get("/parent"), Paths.get(""));
			Assertions.fail("Exception IndexLineParseException expected here !");
		} catch (final IndexLineParseException indexLineParseException) {
			Assertions.assertThat(indexLineParseException.getMessage()).contains("is NOT a valid date");
		}

	}

	@Test
	public final void testParseWithInvalidFormat3() throws ParseException {

		String indexLine = "path/file§§2016-09-30 27:30:12.123§§filehash123§§filehash456§§abcd§§audio/mpeg";

		try {
			IndexLineFormatter.getInstance().parse(indexLine, Paths.get("/parent"), Paths.get(""));
			Assertions.fail("Exception IndexLineParseException expected here !");
		} catch (final IndexLineParseException indexLineParseException) {
			Assertions.assertThat(indexLineParseException.getMessage()).contains("Bad format of line");
		}

	}

	@Test
	public final void testFormat() throws ParseException {

		Path initialRelativePath = Paths.get("/path/file");
		Date lastModifiedDate = this.lastModifiedDateSdf.parse("2016-10-04 12:30:12.123");
		long size = 1234L;
		String mimeType = "audio/mpeg";
		String hash = "filehash123";
		String audioHash = "filehash456";

		FmFile file = new FmFile(Paths.get("/parent"), initialRelativePath, lastModifiedDate, size, mimeType, hash, audioHash);

		String indexLine = IndexLineFormatter.getInstance().format(file);

		Assertions.assertThat(indexLine).isEqualTo("/path/file§§2016-10-04 12:30:12.123§§filehash123§§filehash456§§1234§§audio/mpeg");

	}

}