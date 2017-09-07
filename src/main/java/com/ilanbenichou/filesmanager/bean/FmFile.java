package com.ilanbenichou.filesmanager.bean;

import java.nio.file.Path;
import java.util.Date;
import java.util.Map;

public class FmFile implements Comparable<FmFile> {

	private final Path parentPath;

	private final Path initialRelativePath;

	private final Date lastModifiedDate;

	private final long size;

	private final String mimeType;

	private String hash;

	private String audioHash;

	private Path updatedRelativePath;

	private Map<String, String> metadatasMap;

	public FmFile(final Path parentPath, final Path initialRelativePath, final Date lastModifiedDate, final long size, final String mimeType) {
		this(parentPath, initialRelativePath, lastModifiedDate, size, mimeType, null, null);
	}

	public FmFile( //
			final Path parentPath, //
			final Path initialRelativePath, //
			final Date lastModifiedDate, //
			final long size, //
			final String mimeType, //
			final String hash, //
			final String audioHash //
	) {
		this.parentPath = parentPath;
		this.initialRelativePath = initialRelativePath;
		this.lastModifiedDate = lastModifiedDate;
		this.size = size;
		this.mimeType = mimeType;
		this.hash = hash;
		this.audioHash = audioHash;
	}

	public Path getParentPath() {
		return this.parentPath;
	}

	public Path getInitialRelativePath() {
		return this.initialRelativePath;
	}

	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public long getSize() {
		return this.size;
	}

	public String getMimeType() {
		return this.mimeType;
	}

	public String getHash() {
		return this.hash;
	}

	public void setHash(final String hash) {
		this.hash = hash;
	}

	public String getAudioHash() {
		return this.audioHash;
	}

	public void setAudioHash(final String audioHash) {
		this.audioHash = audioHash;
	}

	public Path getUpdatedRelativePath() {
		return this.updatedRelativePath;
	}

	public void setUpdatedRelativePath(final Path updatedRelativePath) {
		this.updatedRelativePath = updatedRelativePath;
	}

	public Map<String, String> getMetadatasMap() {
		return this.metadatasMap;
	}

	public void setMetadatasMap(final Map<String, String> metadatasMap) {
		this.metadatasMap = metadatasMap;
	}

	@Override
	public boolean equals(final Object otherObject) {

		if (otherObject == null || !(otherObject instanceof FmFile)) {
			return false;
		}

		FmFile otherFile = (FmFile) otherObject;

		if ( //
		(this.initialRelativePath == null && otherFile.initialRelativePath != null) //
				|| //
				(this.initialRelativePath != null && !this.initialRelativePath.equals(otherFile.initialRelativePath)) //
		) {
			return false;
		}

		if ((this.lastModifiedDate == null && otherFile.lastModifiedDate != null) || (this.lastModifiedDate != null && !this.lastModifiedDate.equals(otherFile.lastModifiedDate))) {
			return false;
		}

		if (this.size != otherFile.size) {
			return false;
		}

		if ((this.mimeType == null && otherFile.mimeType != null) || (this.mimeType != null && !this.mimeType.equals(otherFile.mimeType))) {
			return false;
		}

		return true;

	}

	@Override
	public int hashCode() {

		final int prime = 31;

		int result = 1;

		result = prime * result + (this.initialRelativePath != null ? this.initialRelativePath.hashCode() : 0);
		result = prime * result + (this.lastModifiedDate != null ? this.lastModifiedDate.hashCode() : 0);
		result = prime * result + Long.hashCode(this.size);
		result = prime * result + (this.mimeType != null ? this.mimeType.hashCode() : 0);

		return result;

	}

	@Override
	public int compareTo(final FmFile otherFile) {
		return this.initialRelativePath.compareTo(otherFile.initialRelativePath);
	}

	@Override
	public String toString() {
		return this.initialRelativePath.toString();
	}

}