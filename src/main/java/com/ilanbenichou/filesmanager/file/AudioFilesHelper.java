package com.ilanbenichou.filesmanager.file;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public final class AudioFilesHelper {

	private static final Logger LOGGER = Logger.getLogger(AudioFilesHelper.class);

	private static final String AUDIO_MIME_TYPE = "audio";

	private AudioFilesHelper() {
	}

	public static boolean isAudioMimeType(final String mimeType) {
		return mimeType.startsWith(AudioFilesHelper.AUDIO_MIME_TYPE);
	}

	public static byte[] buildAudioFileBytesArray(final Path filePath) {

		AudioFilesHelper.LOGGER.debug(String.format("Building media bytes array for file [%s] ...", filePath));

		try (

				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(filePath.toFile());

		) {

			AudioFormat audioFormat = audioInputStream.getFormat();

			AudioFormat decodedAudioFormat = new AudioFormat( //
					AudioFormat.Encoding.PCM_SIGNED, //
					audioFormat.getSampleRate(), //
					16, //
					audioFormat.getChannels(), //
					audioFormat.getChannels() * 2, //
					audioFormat.getSampleRate(), //
					false //
			);

			try (

					AudioInputStream decodedAudioInputStream = AudioSystem.getAudioInputStream(decodedAudioFormat, audioInputStream);

					ByteArrayOutputStream baout = new ByteArrayOutputStream();

			) {
				IOUtils.copy(decodedAudioInputStream, baout);
				baout.flush();

				return baout.toByteArray();
			}

		} catch (final UnsupportedAudioFileException | ArrayIndexOutOfBoundsException | IOException exception) {
			AudioFilesHelper.LOGGER.warn(String.format("Unable to retrieve media bytes array for file [%s] ...", filePath));
		}

		return null;

	}

}