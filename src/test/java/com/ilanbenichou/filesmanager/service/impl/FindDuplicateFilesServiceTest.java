package com.ilanbenichou.filesmanager.service.impl;

import java.nio.file.Paths;
import java.util.Date;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.ilanbenichou.filesmanager.bean.FmDirectory;
import com.ilanbenichou.filesmanager.bean.FmFile;
import com.ilanbenichou.filesmanager.bean.FmIndex;

public class FindDuplicateFilesServiceTest {

	private FmFile file1;
	private FmFile file2;
	private FmFile file3;
	private FmFile file4;
	private FmFile file5;
	private FmFile file6;
	private FmFile file7;
	private FmFile file8;
	private FmFile file9;
	private FmFile file10;
	private FmFile file11;
	private FmFile file12;

	private FindDuplicateFilesService findDuplicateFilesService;

	@Before
	public void tearDown() {

		this.file1 = FindDuplicateFilesServiceTest.buildFile("filePath1", "h1", "ah1", 42);
		this.file2 = FindDuplicateFilesServiceTest.buildFile("filePath2", "h2", "ah1", 42);
		this.file3 = FindDuplicateFilesServiceTest.buildFile("filePath3", "h1", "ah1", 42);

		this.file4 = FindDuplicateFilesServiceTest.buildFile("filePath4", "h4", "ah4", 92);

		this.file5 = FindDuplicateFilesServiceTest.buildFile("filePath5", "h5", "ah5", 57);
		this.file6 = FindDuplicateFilesServiceTest.buildFile("filePath6", "h5", "ah5", 57);

		this.file7 = FindDuplicateFilesServiceTest.buildFile("filePath7", "h7", "ah7", 31);
		this.file8 = FindDuplicateFilesServiceTest.buildFile("filePath8", "h8", "ah7", 31);

		this.file9 = FindDuplicateFilesServiceTest.buildFile("filePath9", "h9", "ah9", 109);

		this.file10 = FindDuplicateFilesServiceTest.buildFile("filePath10", "h10", null, 237);
		this.file11 = FindDuplicateFilesServiceTest.buildFile("filePath11", "h10", null, 237);

		this.file12 = FindDuplicateFilesServiceTest.buildFile("filePath12", "h12", null, 576);

		FmIndex index = new FmIndex(Paths.get("path/.fmindex"));

		index.addFile(this.file1);
		index.addFile(this.file2);
		index.addFile(this.file3);
		index.addFile(this.file4);
		index.addFile(this.file5);
		index.addFile(this.file6);
		index.addFile(this.file7);
		index.addFile(this.file8);
		index.addFile(this.file9);
		index.addFile(this.file10);
		index.addFile(this.file11);
		index.addFile(this.file12);

		FmDirectory sourceDirectory = new FmDirectory(Paths.get("path"), "source");
		sourceDirectory.setIndex(index);

		this.findDuplicateFilesService = new FindDuplicateFilesService(sourceDirectory);

	}

	private static FmFile buildFile(final String filePath, final String hash, final String audioHash, final long size) {

		FmFile file = new FmFile(Paths.get("/parent"), Paths.get(filePath), new Date(), size, "mimeType");
		file.setHash(hash);
		file.setAudioHash(audioHash);
		return file;

	}

	@Test
	public void testBuildDuplicateFilesSetSet() {

		Set<Set<FmFile>> duplicateFilesSetSet = this.findDuplicateFilesService.buildDuplicateFilesSetSet();

		Assertions.assertThat(duplicateFilesSetSet).containsOnly( //
				Sets.newTreeSet(Sets.newHashSet(this.file5, this.file6)), //
				Sets.newTreeSet(Sets.newHashSet(this.file10, this.file11)) //
		);

	}

	@Test
	public void testBuildDuplicateFilesMetaDiffSetSet() {

		Set<Set<FmFile>> duplicateMetaDiffFilesSetSet = this.findDuplicateFilesService.buildDuplicateFilesMetaDiffSetSet();

		Assertions.assertThat(duplicateMetaDiffFilesSetSet).containsOnly( //
				Sets.newTreeSet(Sets.newHashSet(this.file1, this.file2, this.file3)), //
				Sets.newTreeSet(Sets.newHashSet(this.file7, this.file8)) //
		);

	}

	@Test
	public void testBuildNumberOfDuplicateFilePairs() {

		long numberOfDuplicateFilePairs = this.findDuplicateFilesService.buildNumberOfDuplicateFilePairs();

		Assertions.assertThat(numberOfDuplicateFilePairs).isEqualTo(4L);

	}

	@Test
	public void testBuildNumberOfDuplicateFiles() {

		long numberOfDuplicateFiles = this.findDuplicateFilesService.buildNumberOfDuplicateFiles();

		Assertions.assertThat(numberOfDuplicateFiles).isEqualTo(9L);

	}

	@Test
	public void testBuildNumberOfUniqueFiles() {

		long numberOfUniqueFiles = this.findDuplicateFilesService.buildNumberOfUniqueFiles();

		Assertions.assertThat(numberOfUniqueFiles).isEqualTo(3L);

	}

	@Test
	public void testBuildUniqueFilesSet() {

		Set<FmFile> uniqueFilesSetSet = this.findDuplicateFilesService.buildUniqueFilesSet();

		Assertions.assertThat(uniqueFilesSetSet).containsOnly(this.file4, this.file9, this.file12);

	}

	@Test
	public void testBuildDiskSpaceLost() {

		long diskSpaceLost = this.findDuplicateFilesService.buildDiskSpaceLost();

		Assertions.assertThat(diskSpaceLost).isEqualTo(42 * 2 + 57 + 31 + 237);

	}

}
