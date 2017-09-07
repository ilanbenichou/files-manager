package com.ilanbenichou.filesmanager.hash;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.ilanbenichou.filesmanager.exception.WrappedRuntimeException;

public final class HashHelper {

	private HashHelper() {
	}

	public static String hash(final String stringToHash) {

		try {

			MessageDigest digest = MessageDigest.getInstance("SHA-1");

			byte[] hashedBytesArray = digest.digest(stringToHash.getBytes("UTF-8"));

			return HashHelper.convertByteArrayToHexString(hashedBytesArray);

		} catch (NoSuchAlgorithmException | UnsupportedEncodingException exception) {
			throw WrappedRuntimeException.wrap("An error occurred while calculating a hash !", exception);
		}

	}

	private static String convertByteArrayToHexString(final byte[] bytesArray) {

		StringBuilder hexStringSB = new StringBuilder();

		for (int index = 0; index < bytesArray.length; index++) {
			hexStringSB.append(Integer.toString((bytesArray[index] & 0xff) + 0x100, 16).substring(1));
		}

		return hexStringSB.toString();

	}

}