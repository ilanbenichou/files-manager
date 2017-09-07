package com.ilanbenichou.filesmanager.service.impl;

import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.ilanbenichou.filesmanager.bean.FmDirectory;
import com.ilanbenichou.filesmanager.bean.FmFile;
import com.ilanbenichou.filesmanager.bean.FmIndex;

public class SynchronizeDirectoriesServiceTest {

	private FmFile sourceFile1;
	private FmFile sourceFile2;
	private FmFile sourceFile3;
	private FmFile sourceFile4;
	private FmFile sourceFile5;
	private FmFile sourceFile6;
	private FmFile sourceFile7;
	private FmFile sourceFile8;
	private FmFile sourceFile9;
	private FmFile sourceFile10;
	private FmFile sourceFile11;
	private FmFile sourceFile12;
	private FmFile sourceFile13;
	private FmFile sourceFile14;
	private FmFile sourceFile15;
	private FmFile sourceFile16;
	private FmFile sourceFile17;
	private FmFile sourceFile18;
	private FmFile sourceFile19;
	private FmFile sourceFile20;
	private FmFile sourceFile21;
	private FmFile sourceFile22;
	private FmFile sourceFile23;
	private FmFile sourceFile24;
	private FmFile sourceFile25;
	private FmFile sourceFile26;
	private FmFile sourceFile27;
	private FmFile sourceFile28;

	private FmFile targetFile1;
	private FmFile targetFile2;
	private FmFile targetFile3;
	private FmFile targetFile4;
	private FmFile targetFile5;
	private FmFile targetFile6;
	private FmFile targetFile7;
	private FmFile targetFile8;
	private FmFile targetFile9;
	private FmFile targetFile10;
	private FmFile targetFile11;
	private FmFile targetFile12;
	private FmFile targetFile13;
	private FmFile targetFile14;
	private FmFile targetFile15;
	private FmFile targetFile16;
	private FmFile targetFile17;
	private FmFile targetFile18;
	private FmFile targetFile19;
	private FmFile targetFile20;
	private FmFile targetFile21;
	private FmFile targetFile22;
	private FmFile targetFile23;
	private FmFile targetFile25;
	private FmFile targetFile26;
	private FmFile targetFile27;
	private FmFile targetFile28;

	private SynchronizeDirectoriesService synchronizeDirectoriesService;

	@Before
	public void tearDown() {

		this.sourceFile1 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path1", "h1", "ah1", 142);
		this.targetFile1 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path1", "h1", "ah1", 142);

		this.sourceFile2 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path2", "h2", "ah2", 245);
		this.targetFile2 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path2", "h2", "ah2", 245);

		this.sourceFile3 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path3", "h3", "ah3", 346);
		this.targetFile3 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path3", "h3", "ah3", 346);

		this.sourceFile4 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path4", "h4", "ah4", 445);
		this.targetFile4 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path4", "ht4", "ah4", 446);

		this.sourceFile5 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path5", "h5", "ah5", 546);
		this.targetFile5 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/subPath/path5", "ht5", "ah5", 546);

		this.sourceFile6 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path6", "h6", "ah6", 1142);
		this.targetFile6 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/subPath/path6", "h6", "ah6", 1142);

		this.sourceFile7 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path7", "h7", "ah7", 1245);
		this.targetFile7 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/subPath/path7", "h7", "ah7", 1245);

		this.sourceFile8 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path8", "h8", "ah8", 1346);
		this.targetFile8 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/subPath/path8", "h8", "ah8", 1346);

		this.sourceFile9 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path9", "h9", "ah9", 1445);
		this.targetFile9 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/subPath/path9", "ht9", "ah9", 1445);

		this.sourceFile10 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path10", "h10", "ah10", 1546);
		this.targetFile10 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/subPath/path10", "ht10", "ah10", 1546);

		this.sourceFile11 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path11", "h11", "ah11", 2546);
		this.sourceFile12 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path12", "h11", "ah11", 2546);
		this.sourceFile13 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path13", "h11", "ah11", 2546);
		this.sourceFile14 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path14", "h11", "ah11", 2546);

		this.targetFile11 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path11", "ht11", "aht11", 3546);
		this.targetFile12 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path12", "ht12", "aht12", 3546);
		this.targetFile13 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path13", "ht13", "aht13", 3546);
		this.targetFile14 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path14", "ht14", "aht14", 3546);

		this.sourceFile15 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path15", "h15", null, 4142);
		this.targetFile15 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path15", "h15", null, 4142);

		this.sourceFile16 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path16", "h16", null, 4245);
		this.targetFile16 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path16", "h16", null, 4245);

		this.sourceFile17 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path17", "h17", null, 4346);
		this.targetFile17 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path17", "h17", null, 4346);

		this.sourceFile18 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path18", "h18", null, 4445);
		this.targetFile18 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path18", "ht18", null, 4446);

		this.sourceFile19 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path19", "h19", null, 4546);
		this.targetFile19 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/subPath/path19", "ht19", null, 4546);

		this.sourceFile20 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path20", "h20", null, 5142);
		this.targetFile20 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/subPath/path20", "h20", null, 5142);

		this.sourceFile21 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path21", "h21", null, 5245);
		this.targetFile21 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/subPath/path21", "h21", null, 5245);

		this.sourceFile22 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path22", "h22", null, 5346);
		this.targetFile22 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/subPath/path22", "h22", null, 5346);

		this.sourceFile23 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path23", "h23", null, 5445);
		this.targetFile23 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/subPath/path23", "ht23", null, 5445);

		this.sourceFile24 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path24", "h24", null, 5546);

		this.sourceFile25 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path25", "h25", null, 6546);
		this.sourceFile26 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path26", "h26", null, 6546);
		this.sourceFile27 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path27", "h27", null, 6546);
		this.sourceFile28 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path28", "h28", null, 6546);

		this.targetFile25 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path25", "ht25", null, 7546);
		this.targetFile26 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path26", "ht26", null, 7546);
		this.targetFile27 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path27", "ht27", null, 7546);
		this.targetFile28 = SynchronizeDirectoriesServiceTest.buildFile("parentPath/path28", "ht28", null, 7546);

		FmIndex sourceIndex = new FmIndex(Paths.get("sourcePath/.fmindex"));

		sourceIndex.addFile(this.sourceFile1);
		sourceIndex.addFile(this.sourceFile2);
		sourceIndex.addFile(this.sourceFile3);
		sourceIndex.addFile(this.sourceFile4);
		sourceIndex.addFile(this.sourceFile5);
		sourceIndex.addFile(this.sourceFile6);
		sourceIndex.addFile(this.sourceFile7);
		sourceIndex.addFile(this.sourceFile8);
		sourceIndex.addFile(this.sourceFile9);
		sourceIndex.addFile(this.sourceFile10);
		sourceIndex.addFile(this.sourceFile11);
		sourceIndex.addFile(this.sourceFile12);
		sourceIndex.addFile(this.sourceFile13);
		sourceIndex.addFile(this.sourceFile14);
		sourceIndex.addFile(this.sourceFile15);
		sourceIndex.addFile(this.sourceFile16);
		sourceIndex.addFile(this.sourceFile17);
		sourceIndex.addFile(this.sourceFile18);
		sourceIndex.addFile(this.sourceFile19);
		sourceIndex.addFile(this.sourceFile20);
		sourceIndex.addFile(this.sourceFile21);
		sourceIndex.addFile(this.sourceFile22);
		sourceIndex.addFile(this.sourceFile23);
		sourceIndex.addFile(this.sourceFile24);
		sourceIndex.addFile(this.sourceFile25);
		sourceIndex.addFile(this.sourceFile26);
		sourceIndex.addFile(this.sourceFile27);
		sourceIndex.addFile(this.sourceFile28);

		FmDirectory sourceDirectory = new FmDirectory(Paths.get("sourcePath"), "source");
		sourceDirectory.setIndex(sourceIndex);

		FmIndex targetIndex = new FmIndex(Paths.get("targetPath/.fmindex"));

		targetIndex.addFile(this.targetFile1);
		targetIndex.addFile(this.targetFile2);
		targetIndex.addFile(this.targetFile3);
		targetIndex.addFile(this.targetFile4);
		targetIndex.addFile(this.targetFile5);
		targetIndex.addFile(this.targetFile6);
		targetIndex.addFile(this.targetFile7);
		targetIndex.addFile(this.targetFile8);
		targetIndex.addFile(this.targetFile9);
		targetIndex.addFile(this.targetFile10);
		targetIndex.addFile(this.targetFile11);
		targetIndex.addFile(this.targetFile12);
		targetIndex.addFile(this.targetFile13);
		targetIndex.addFile(this.targetFile14);
		targetIndex.addFile(this.targetFile15);
		targetIndex.addFile(this.targetFile16);
		targetIndex.addFile(this.targetFile17);
		targetIndex.addFile(this.targetFile18);
		targetIndex.addFile(this.targetFile19);
		targetIndex.addFile(this.targetFile20);
		targetIndex.addFile(this.targetFile21);
		targetIndex.addFile(this.targetFile22);
		targetIndex.addFile(this.targetFile23);
		targetIndex.addFile(this.targetFile25);
		targetIndex.addFile(this.targetFile26);
		targetIndex.addFile(this.targetFile27);
		targetIndex.addFile(this.targetFile28);

		FmDirectory targetDirectory = new FmDirectory(Paths.get("targetPath"), "target");
		targetDirectory.setIndex(targetIndex);

		this.synchronizeDirectoriesService = new SynchronizeDirectoriesService(sourceDirectory, targetDirectory);

	}

	private static FmFile buildFile(final String filePath, final String hash, final String audioHash, final long size) {

		FmFile file = new FmFile(Paths.get("/parent"), Paths.get(filePath), new Date(), size, "mimeType");
		file.setHash(hash);
		file.setAudioHash(audioHash);

		if (audioHash != null) {
			Map<String, String> metadatasMap = new TreeMap<>();
			metadatasMap.put(hash, hash);
			file.setMetadatasMap(metadatasMap);
		}

		return file;

	}

	@Test
	public void testBuildNewFilesSet() {

		Set<FmFile> newFilesSet = this.synchronizeDirectoriesService.buildNewFilesSet();

		Assertions.assertThat(newFilesSet).containsExactly( //
				this.sourceFile10, this.sourceFile19, this.sourceFile20, this.sourceFile21, this.sourceFile22, this.sourceFile23, //
				this.sourceFile24, this.sourceFile5, this.sourceFile6, this.sourceFile7, this.sourceFile8, this.sourceFile9 //
		);

	}

	@Test
	public void testBuildDifferentFilesSet() {

		Set<FmFile> differentFilesSet = this.synchronizeDirectoriesService.buildDifferentFilesSet();

		Assertions.assertThat(differentFilesSet).containsExactly( //
				this.sourceFile11, this.sourceFile12, this.sourceFile13, this.sourceFile14, this.sourceFile18, //
				this.sourceFile25, this.sourceFile26, this.sourceFile27, this.sourceFile28, this.sourceFile4 //
		);

	}

	@Test
	public void testBuildDeletedFilesSet() {

		Set<FmFile> deletedFilesSet = this.synchronizeDirectoriesService.buildDeletedFilesSet();

		Assertions.assertThat(deletedFilesSet).containsExactly( //
				this.targetFile10, this.targetFile19, this.targetFile20, this.targetFile21, this.targetFile22, this.targetFile23, //
				this.targetFile5, this.targetFile6, this.targetFile7, this.targetFile8, this.targetFile9 //
		);

	}

	@Test
	public void testBuildExistingFilesSet() {

		Set<FmFile> existingFilesSet = this.synchronizeDirectoriesService.buildExistingFilesSet();

		Assertions.assertThat(existingFilesSet).containsExactly(this.sourceFile1, this.sourceFile15, this.sourceFile16, this.sourceFile17, this.sourceFile2, this.sourceFile3);

	}

	@Test
	public void testBuildNumberOfNewFiles() {

		long numberOfNewFiles = this.synchronizeDirectoriesService.buildNumberOfNewFiles();

		Assertions.assertThat(numberOfNewFiles).isEqualTo(12L);

	}

	@Test
	public void testBuildNumberOfDifferentFiles() {

		long numberOfDifferentFiles = this.synchronizeDirectoriesService.buildNumberOfDifferentFiles();

		Assertions.assertThat(numberOfDifferentFiles).isEqualTo(10L);

	}

	@Test
	public void testBuildNumberOfDeletedFiles() {

		long numberOfDeletedFiles = this.synchronizeDirectoriesService.buildNumberOfDeletedFiles();

		Assertions.assertThat(numberOfDeletedFiles).isEqualTo(11L);

	}

	@Test
	public void testBuildNumberOfExistingFiles() {

		long numberOfExistingFiles = this.synchronizeDirectoriesService.buildNumberOfExistingFiles();

		Assertions.assertThat(numberOfExistingFiles).isEqualTo(6L);

	}

}