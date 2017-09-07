package com.ilanbenichou.filesmanager.index;

import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.ilanbenichou.filesmanager.bean.FmFile;
import com.ilanbenichou.filesmanager.bean.FmIndex;

public final class IndexHelperTest {

	private FmFile file1;
	private FmFile file2;
	private FmFile file3;
	private FmFile file4;
	private FmFile file5;
	private FmFile file6;
	private FmFile file7;

	private FmIndex index;

	@Before
	public void tearDown() {

		this.file1 = IndexHelperTest.buildFile("filePath1", "h1", "ah1");
		this.file2 = IndexHelperTest.buildFile("filePath2", "h2", "ah1");
		this.file3 = IndexHelperTest.buildFile("filePath3", "h1", "ah1");

		this.file4 = IndexHelperTest.buildFile("filePath4", "h4", "ah4");

		this.file5 = IndexHelperTest.buildFile("filePath5", "h5", null);
		this.file6 = IndexHelperTest.buildFile("filePath6", "h5", null);
		this.file7 = IndexHelperTest.buildFile("filePath7", "h7", null);

		this.index = new FmIndex(Paths.get("path/.fmindex"));

		this.index.addFile(this.file1);
		this.index.addFile(this.file2);
		this.index.addFile(this.file3);
		this.index.addFile(this.file4);
		this.index.addFile(this.file5);
		this.index.addFile(this.file6);
		this.index.addFile(this.file7);

	}

	private static FmFile buildFile(final String filePath, final String hash, final String audioHash) {

		FmFile file = new FmFile(Paths.get("/parent"), Paths.get(filePath), new Date(), 1L, "mimeType");
		file.setHash(hash);
		file.setAudioHash(audioHash);
		return file;

	}

	@Test
	public void testBuildMapByHash() {

		Map<String, Set<FmFile>> mapByHash = IndexHelper.buildMapByHash(this.index);

		Assertions.assertThat(mapByHash).containsOnlyKeys("h1", "h2", "h4", "h5", "h7");

		Assertions.assertThat(mapByHash.get("h1")).containsOnly(this.file1, this.file3);
		Assertions.assertThat(mapByHash.get("h2")).containsOnly(this.file2);
		Assertions.assertThat(mapByHash.get("h4")).containsOnly(this.file4);
		Assertions.assertThat(mapByHash.get("h5")).containsOnly(this.file5, this.file6);
		Assertions.assertThat(mapByHash.get("h7")).containsOnly(this.file7);

	}

	@Test
	public void testBuildMapByAudioHash() {

		Map<String, Set<FmFile>> mapByAudioHash = IndexHelper.buildMapByAudioHash(this.index);

		Assertions.assertThat(mapByAudioHash).containsOnlyKeys("ah1", "ah4");

		Assertions.assertThat(mapByAudioHash.get("ah1")).containsOnly(this.file1, this.file2, this.file3);
		Assertions.assertThat(mapByAudioHash.get("ah4")).containsOnly(this.file4);

	}

}