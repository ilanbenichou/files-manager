package com.ilanbenichou.filesmanager.service.impl;

import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.ilanbenichou.filesmanager.bean.FmDirectory;
import com.ilanbenichou.filesmanager.bean.FmFile;
import com.ilanbenichou.filesmanager.bean.FmIndex;
import com.ilanbenichou.filesmanager.service.impl.FindNewFilesService.FileMetadatasDiffDatas;

public class FindNewFilesServiceTest {

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
	private FmFile sourceFile29;
	private FmFile sourceFile30;

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

	private FindNewFilesService findNewFilesService;

	@Before
	public void tearDown() {

		this.sourceFile1 = FindNewFilesServiceTest.buildFile("sourceFilePath1", "h1", "ah1", 142);
		this.sourceFile2 = FindNewFilesServiceTest.buildFile("sourceFilePath2", "h2", "ah1", 142);
		this.sourceFile3 = FindNewFilesServiceTest.buildFile("sourceFilePath3", "h1", "ah1", 142);

		this.targetFile1 = FindNewFilesServiceTest.buildFile("targetFilePath1", "h1", "ah1", 142);

		this.sourceFile4 = FindNewFilesServiceTest.buildFile("sourceFilePath4", "h4", "ah4", 145);
		this.sourceFile5 = FindNewFilesServiceTest.buildFile("sourceFilePath5", "h5", "ah4", 145);
		this.sourceFile6 = FindNewFilesServiceTest.buildFile("sourceFilePath6", "h4", "ah4", 145);

		this.targetFile2 = FindNewFilesServiceTest.buildFile("targetFilePath2", "ht2", "ah4", 144);

		this.sourceFile7 = FindNewFilesServiceTest.buildFile("sourceFilePath7", "h7", "ah7", 242);
		this.sourceFile8 = FindNewFilesServiceTest.buildFile("sourceFilePath8", "h7", "ah7", 242);
		this.sourceFile9 = FindNewFilesServiceTest.buildFile("sourceFilePath9", "h7", "ah7", 242);

		this.targetFile3 = FindNewFilesServiceTest.buildFile("targetFilePath3", "h7", "ah7", 242);
		this.targetFile4 = FindNewFilesServiceTest.buildFile("targetFilePath4", "ht4", "ah7", 241);

		this.sourceFile10 = FindNewFilesServiceTest.buildFile("sourceFilePath10", "h10", "ah10", 245);
		this.sourceFile11 = FindNewFilesServiceTest.buildFile("sourceFilePath11", "h10", "ah10", 245);
		this.sourceFile12 = FindNewFilesServiceTest.buildFile("sourceFilePath12", "h10", "ah10", 245);

		this.targetFile5 = FindNewFilesServiceTest.buildFile("targetFilePath5", "ht5", "ah10", 246);
		this.targetFile6 = FindNewFilesServiceTest.buildFile("targetFilePath6", "ht6", "ah10", 246);

		this.sourceFile13 = FindNewFilesServiceTest.buildFile("sourceFilePath13", "h13", "ah13", 342);
		this.sourceFile14 = FindNewFilesServiceTest.buildFile("sourceFilePath14", "h13", "ah13", 342);

		this.targetFile7 = FindNewFilesServiceTest.buildFile("targetFilePath7", "ht7", "ah13", 341);

		this.sourceFile15 = FindNewFilesServiceTest.buildFile("sourceFilePath15", "h15", "ah15", 442);
		this.sourceFile16 = FindNewFilesServiceTest.buildFile("sourceFilePath16", "h16", "ah15", 442);

		this.targetFile8 = FindNewFilesServiceTest.buildFile("targetFilePath8", "h15", "ah15", 442);

		this.sourceFile17 = FindNewFilesServiceTest.buildFile("sourceFilePath17", "h17", "ah17", 542);
		this.sourceFile18 = FindNewFilesServiceTest.buildFile("sourceFilePath18", "h17", "ah17", 542);

		this.targetFile9 = FindNewFilesServiceTest.buildFile("targetFilePath9", "h17", "ah17", 542);
		this.targetFile10 = FindNewFilesServiceTest.buildFile("targetFilePath10", "h17", "ah17", 542);

		this.sourceFile19 = FindNewFilesServiceTest.buildFile("sourceFilePath19", "h19", "ah19", 642);

		this.targetFile11 = FindNewFilesServiceTest.buildFile("targetFilePath11", "h19", "ah19", 642);
		this.targetFile12 = FindNewFilesServiceTest.buildFile("targetFilePath12", "h19", "ah19", 642);

		this.sourceFile20 = FindNewFilesServiceTest.buildFile("sourceFilePath20", "h20", "ah20", 742);

		this.targetFile13 = FindNewFilesServiceTest.buildFile("targetFilePath13", "ht13", "ah20", 743);

		this.sourceFile21 = FindNewFilesServiceTest.buildFile("sourceFilePath21", "h21", "ah21", 842);

		this.targetFile14 = FindNewFilesServiceTest.buildFile("targetFilePath14", "ht14", "ah21", 846);
		this.targetFile15 = FindNewFilesServiceTest.buildFile("targetFilePath15", "ht15", "ah21", 846);

		this.sourceFile22 = FindNewFilesServiceTest.buildFile("sourceFilePath22", "h22", "ah22", 942);

		this.sourceFile23 = FindNewFilesServiceTest.buildFile("sourceFilePath23", "h23", "ah23", 1042);
		this.sourceFile24 = FindNewFilesServiceTest.buildFile("sourceFilePath24", "h23", "ah23", 1042);

		this.sourceFile25 = FindNewFilesServiceTest.buildFile("sourceFilePath25", "h25", "ah23", 1142);
		this.sourceFile26 = FindNewFilesServiceTest.buildFile("sourceFilePath26", "h26", "ah23", 1145);

		this.sourceFile27 = FindNewFilesServiceTest.buildFile("sourceFilePath27", "h27", null, 1242);
		this.sourceFile28 = FindNewFilesServiceTest.buildFile("sourceFilePath28", "h27", null, 1242);

		this.targetFile16 = FindNewFilesServiceTest.buildFile("targetFilePath16", "h27", null, 1242);

		this.sourceFile29 = FindNewFilesServiceTest.buildFile("sourceFilePath29", "h29", null, 1342);

		this.targetFile17 = FindNewFilesServiceTest.buildFile("targetFilePath17", "h29", null, 1342);
		this.targetFile18 = FindNewFilesServiceTest.buildFile("targetFilePath18", "h29", null, 1342);

		this.sourceFile30 = FindNewFilesServiceTest.buildFile("sourceFilePath30", "h30", null, 1445);

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
		sourceIndex.addFile(this.sourceFile29);
		sourceIndex.addFile(this.sourceFile30);

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

		FmDirectory targetDirectory = new FmDirectory(Paths.get("targetPath"), "target");
		targetDirectory.setIndex(targetIndex);

		this.findNewFilesService = new FindNewFilesService(sourceDirectory, targetDirectory);

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
	public void testBuildExistingFilesMap() {

		Map<FmFile, Set<FmFile>> existingFilesMap = this.findNewFilesService.buildExistingFilesMap();

		Assertions.assertThat(existingFilesMap).containsOnly( //
				Assertions.entry(this.sourceFile1, Sets.newTreeSet(Sets.newHashSet(this.targetFile1))), //
				Assertions.entry(this.sourceFile3, Sets.newTreeSet(Sets.newHashSet(this.targetFile1))), //
				Assertions.entry(this.sourceFile7, Sets.newTreeSet(Sets.newHashSet(this.targetFile3))), //
				Assertions.entry(this.sourceFile8, Sets.newTreeSet(Sets.newHashSet(this.targetFile3))), //
				Assertions.entry(this.sourceFile9, Sets.newTreeSet(Sets.newHashSet(this.targetFile3))), //
				Assertions.entry(this.sourceFile15, Sets.newTreeSet(Sets.newHashSet(this.targetFile8))), //
				Assertions.entry(this.sourceFile17, Sets.newTreeSet(Sets.newHashSet(this.targetFile9, this.targetFile10))), //
				Assertions.entry(this.sourceFile18, Sets.newTreeSet(Sets.newHashSet(this.targetFile9, this.targetFile10))), //
				Assertions.entry(this.sourceFile19, Sets.newTreeSet(Sets.newHashSet(this.targetFile11, this.targetFile12))), //
				Assertions.entry(this.sourceFile27, Sets.newTreeSet(Sets.newHashSet(this.targetFile16))), //
				Assertions.entry(this.sourceFile28, Sets.newTreeSet(Sets.newHashSet(this.targetFile16))), //
				Assertions.entry(this.sourceFile29, Sets.newTreeSet(Sets.newHashSet(this.targetFile17, this.targetFile18))) //
		);

	}

	@Test
	public void testBuildExistingFilesMetaDiffMap() {

		Map<FmFile, FileMetadatasDiffDatas> existingFilesMetaDiffMap = this.findNewFilesService.buildExistingFilesMetaDiffMap();

		Assertions.assertThat(existingFilesMetaDiffMap).containsOnly( //
				Assertions.entry(this.sourceFile2, FindNewFilesServiceTest.buildFileMetadatasDiffDatas(Sets.newHashSet(this.targetFile1), "h1", "h2")), //
				Assertions.entry(this.sourceFile4, FindNewFilesServiceTest.buildFileMetadatasDiffDatas(Sets.newHashSet(this.targetFile2), "h4", "ht2")), //
				Assertions.entry(this.sourceFile5, FindNewFilesServiceTest.buildFileMetadatasDiffDatas(Sets.newHashSet(this.targetFile2), "h5", "ht2")), //
				Assertions.entry(this.sourceFile6, FindNewFilesServiceTest.buildFileMetadatasDiffDatas(Sets.newHashSet(this.targetFile2), "h4", "ht2")), //
				Assertions.entry(this.sourceFile10, FindNewFilesServiceTest.buildFileMetadatasDiffDatas(Sets.newHashSet(this.targetFile5, this.targetFile6), "h10", "ht5", "ht6")), //
				Assertions.entry(this.sourceFile11, FindNewFilesServiceTest.buildFileMetadatasDiffDatas(Sets.newHashSet(this.targetFile5, this.targetFile6), "h10", "ht5", "ht6")), //
				Assertions.entry(this.sourceFile12, FindNewFilesServiceTest.buildFileMetadatasDiffDatas(Sets.newHashSet(this.targetFile5, this.targetFile6), "h10", "ht5", "ht6")), //
				Assertions.entry(this.sourceFile13, FindNewFilesServiceTest.buildFileMetadatasDiffDatas(Sets.newHashSet(this.targetFile7), "h13", "ht7")), //
				Assertions.entry(this.sourceFile14, FindNewFilesServiceTest.buildFileMetadatasDiffDatas(Sets.newHashSet(this.targetFile7), "h13", "ht7")), //
				Assertions.entry(this.sourceFile16, FindNewFilesServiceTest.buildFileMetadatasDiffDatas(Sets.newHashSet(this.targetFile8), "h15", "h16")), //
				Assertions.entry(this.sourceFile20, FindNewFilesServiceTest.buildFileMetadatasDiffDatas(Sets.newHashSet(this.targetFile13), "h20", "ht13")), //
				Assertions.entry( //
						this.sourceFile21, //
						FindNewFilesServiceTest.buildFileMetadatasDiffDatas(Sets.newHashSet(this.targetFile14, this.targetFile15), "h21", "ht14", "ht15") //
				) //
		);

	}

	private static FileMetadatasDiffDatas buildFileMetadatasDiffDatas(final Set<FmFile> targetFilesSet, final String... metadatasArray) {
		return new FileMetadatasDiffDatas(Sets.newTreeSet(targetFilesSet), Sets.newTreeSet(Sets.newHashSet(metadatasArray)));
	}

	@Test
	public void testBuildNewFilesSet() {

		Set<FmFile> newFilesSetSet = this.findNewFilesService.buildNewFilesSet();

		Assertions.assertThat(newFilesSetSet).containsOnly( //
				this.sourceFile22, //
				this.sourceFile23, //
				this.sourceFile24, //
				this.sourceFile25, //
				this.sourceFile26, //
				this.sourceFile30 //
		);

	}

	@Test
	public void testBuildNumberOfNewFiles() {

		long numberOfNewFiles = this.findNewFilesService.buildNumberOfNewFiles();

		Assertions.assertThat(numberOfNewFiles).isEqualTo(6L);

	}

	@Test
	public void testBuildNumberOfExistingFiles() {

		long numberOfExistingFiles = this.findNewFilesService.buildNumberOfExistingFiles();

		Assertions.assertThat(numberOfExistingFiles).isEqualTo(24L);

	}

	@Test
	public void testBuildDiskSpaceLost() {

		long diskSpaceLost = this.findNewFilesService.buildDiskSpaceLost();

		Assertions.assertThat(diskSpaceLost).isEqualTo( //
				Integer.toUnsignedLong( //
						142 + 142 + 142 + 145 + 145 + 145 + 242 + 242 + 242 + 245 + 245 + 245 + 342 + 342 + 442 + 442 + 542 + 542 + 642 + 742 + 842 + 1242 + 1242 + 1342 //
				) //
		);

	}

}
