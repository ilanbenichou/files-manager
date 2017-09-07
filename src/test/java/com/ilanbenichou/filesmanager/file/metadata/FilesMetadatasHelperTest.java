package com.ilanbenichou.filesmanager.file.metadata;

import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.ilanbenichou.filesmanager.bean.FmFile;

public final class FilesMetadatasHelperTest {

	private FmFile file1;

	private FmFile file2;

	private FmFile file3;

	@Before
	public void tearDown() {

		Map<String, String> file1MetadatasMap = new HashMap<>();
		file1MetadatasMap.put("aa", "AA");
		file1MetadatasMap.put("bb", "BB");
		file1MetadatasMap.put("cc", "CC");
		file1MetadatasMap.put("dd", "DD");
		file1MetadatasMap.put("ee", "EE");
		file1MetadatasMap.put("ff", "FF");
		file1MetadatasMap.put("zz", "ZZ");
		this.file1 = FilesMetadatasHelperTest.buildFile("filePath1", file1MetadatasMap);

		Map<String, String> file2MetadatasMap = new HashMap<>();
		file2MetadatasMap.put("aa", "AA");
		file2MetadatasMap.put("bb", "BBdiff");
		file2MetadatasMap.put("ee", "EEdiff");
		file2MetadatasMap.put("gg", "GG");
		file2MetadatasMap.put("ii", "II");
		file2MetadatasMap.put("zz", "ZZ");
		this.file2 = FilesMetadatasHelperTest.buildFile("filePath2", file2MetadatasMap);

		Map<String, String> file3MetadatasMap = new HashMap<>();
		file3MetadatasMap.put("aa", "AA");
		file3MetadatasMap.put("bb", "BB");
		file3MetadatasMap.put("dd", "DD");
		file3MetadatasMap.put("ee", "EEdif");
		file3MetadatasMap.put("gg", "GG");
		file3MetadatasMap.put("hh", "HH");
		file3MetadatasMap.put("jj", "");
		file3MetadatasMap.put("zz", "ZZ");
		this.file3 = FilesMetadatasHelperTest.buildFile("filePath3", file3MetadatasMap);

	}

	@Test
	public void testBuildFilesMetadatasMap() {

		Map<String, Set<String>> filesMetadatasMap = FilesMetadatasHelper.buildFilesMetadatasMap(Sets.newHashSet(this.file1, this.file2, this.file3));

		Assertions.assertThat(filesMetadatasMap).containsOnly( //
				Assertions.entry("aa", Sets.newHashSet("AA")), //
				Assertions.entry("bb", Sets.newHashSet("BB", "BBdiff")), //
				Assertions.entry("cc", Sets.newHashSet("CC", null)), //
				Assertions.entry("dd", Sets.newHashSet("DD", null)), //
				Assertions.entry("ee", Sets.newHashSet("EE", "EEdif", "EEdiff")), //
				Assertions.entry("ff", Sets.newHashSet("FF", null)), //
				Assertions.entry("gg", Sets.newHashSet("GG", null)), //
				Assertions.entry("hh", Sets.newHashSet("HH", null)), //
				Assertions.entry("ii", Sets.newHashSet("II", null)), //
				Assertions.entry("jj", Sets.newHashSet("", null)), //
				Assertions.entry("zz", Sets.newHashSet("ZZ")) //
		);

	}

	@Test
	public void testBuildFilesMetadatasKeyDifferenceSet() {

		Set<String> metadatasKeySet = FilesMetadatasHelper.buildFilesMetadatasKeyDifferenceSet(Sets.newHashSet(this.file1, this.file2, this.file3));

		Assertions.assertThat(metadatasKeySet).containsExactly("bb", "cc", "dd", "ee", "ff", "gg", "hh", "ii", "jj");

	}

	@Test
	public void testBuildMetadataValuesMap() {

		Set<FmFile> filesSet = Sets.newHashSet(this.file1, this.file2, this.file3);

		Assertions.assertThat(FilesMetadatasHelper.buildMetadataValuesMap("aa", filesSet)).containsOnly( //
				Assertions.entry("AA", Sets.newHashSet(this.file1, this.file2, this.file3)) //
		);

		Assertions.assertThat(FilesMetadatasHelper.buildMetadataValuesMap("bb", filesSet)).containsOnly( //
				Assertions.entry("BB", Sets.newHashSet(this.file1, this.file3)), //
				Assertions.entry("BBdiff", Sets.newHashSet(this.file2)) //
		);

		Assertions.assertThat(FilesMetadatasHelper.buildMetadataValuesMap("cc", filesSet)).containsOnly( //
				Assertions.entry("CC", Sets.newHashSet(this.file1)), //
				Assertions.entry(FilesMetadatasHelper.NOT_AVAILABLE_VALUE, Sets.newHashSet(this.file2, this.file3)) //
		);

		Assertions.assertThat(FilesMetadatasHelper.buildMetadataValuesMap("dd", filesSet)).containsOnly( //
				Assertions.entry("DD", Sets.newHashSet(this.file1, this.file3)), //
				Assertions.entry(FilesMetadatasHelper.NOT_AVAILABLE_VALUE, Sets.newHashSet(this.file2)) //
		);

		Assertions.assertThat(FilesMetadatasHelper.buildMetadataValuesMap("ee", filesSet)).containsOnly( //
				Assertions.entry("EE", Sets.newHashSet(this.file1)), //
				Assertions.entry("EEdif", Sets.newHashSet(this.file3)), //
				Assertions.entry("EEdiff", Sets.newHashSet(this.file2)) //
		);

		Assertions.assertThat(FilesMetadatasHelper.buildMetadataValuesMap("ff", filesSet)).containsOnly( //
				Assertions.entry("FF", Sets.newHashSet(this.file1)), //
				Assertions.entry(FilesMetadatasHelper.NOT_AVAILABLE_VALUE, Sets.newHashSet(this.file2, this.file3)) //
		);

		Assertions.assertThat(FilesMetadatasHelper.buildMetadataValuesMap("gg", filesSet)).containsOnly( //
				Assertions.entry("GG", Sets.newHashSet(this.file2, this.file3)), //
				Assertions.entry(FilesMetadatasHelper.NOT_AVAILABLE_VALUE, Sets.newHashSet(this.file1)) //
		);

		Assertions.assertThat(FilesMetadatasHelper.buildMetadataValuesMap("hh", filesSet)).containsOnly( //
				Assertions.entry("HH", Sets.newHashSet(this.file3)), //
				Assertions.entry(FilesMetadatasHelper.NOT_AVAILABLE_VALUE, Sets.newHashSet(this.file1, this.file2)) //
		);

		Assertions.assertThat(FilesMetadatasHelper.buildMetadataValuesMap("ii", filesSet)).containsOnly( //
				Assertions.entry("II", Sets.newHashSet(this.file2)), //
				Assertions.entry(FilesMetadatasHelper.NOT_AVAILABLE_VALUE, Sets.newHashSet(this.file1, this.file3)) //
		);

		Assertions.assertThat(FilesMetadatasHelper.buildMetadataValuesMap("jj", filesSet)).containsOnly( //
				Assertions.entry(FilesMetadatasHelper.EMPTY_VALUE, Sets.newHashSet(this.file3)), //
				Assertions.entry(FilesMetadatasHelper.NOT_AVAILABLE_VALUE, Sets.newHashSet(this.file1, this.file2)) //
		);

		Assertions.assertThat(FilesMetadatasHelper.buildMetadataValuesMap("zz", filesSet)).containsOnly( //
				Assertions.entry("ZZ", Sets.newHashSet(this.file1, this.file2, this.file3)) //
		);

	}

	private static FmFile buildFile(final String filePath, Map<String, String> fileMetadatasMap) {

		FmFile file = new FmFile(Paths.get("/parent"), Paths.get(filePath), new Date(), 1L, "mimeType");
		file.setMetadatasMap(fileMetadatasMap);

		return file;

	}

}