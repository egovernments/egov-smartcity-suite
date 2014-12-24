/*
 * @(#)UtilityMethods.java 3.0, 18 Jun, 2013 12:28:29 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class UtilityMethods {

	private static char[] alphbts = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	private static int mod = 11;

	/**
	 * Creates and returns a file Name in the temp directory.
	 * @param fileExtension -The extension of the file
	 * @return String - Returns the full file name of the randomely created file
	 */
	public static String getRandomFileName(final String fileExtension) throws IOException {
		boolean exist = true;
		// try to find out the temporary directory location in the following order
		// i) trademanager.properties file
		// ii) From Enviornment variable 'temp'
		// iii) From Enviornment variable 'tmp'
		// else throw exception

		String saveDirectory = EGovConfig.getProperty("tempdirectory", "/", "");
		if (saveDirectory == null) {
			saveDirectory = System.getProperty("temp");
		}
		if (saveDirectory == null) {
			saveDirectory = System.getProperty("tmp");
		}
		if (saveDirectory == null) {
			throw new IOException("Temp directory not specified");
		}

		final File f = new File(saveDirectory);

		// Check saveDirectory is truly a directory
		if (!f.isDirectory()) {
			throw new IOException("Not a directory: " + saveDirectory);
		}

		// Check saveDirectory is writable
		if (!f.canWrite()) {
			throw new IOException("Not writable: " + saveDirectory);
		}

		String fileName = null;
		final Random tempRan = new Random();
		while (exist) {
			final int itempFile = Math.abs(tempRan.nextInt(Integer.MAX_VALUE));
			String tempFilename = Integer.toString(itempFile);
			tempFilename += (fileExtension == null) ? "tmp" : fileExtension;
			final File file = new File(saveDirectory + File.separator + tempFilename);
			if (!file.exists()) {
				exist = false;
				fileName = file.toString();
			}
		}
		return fileName;
	}

	public static char getRandomChar() {
		final Random tempRan = new Random();
		int nextInt = 0;
		int tempInt = 0;

		if (mod < 25) {
			mod++;
		} else if (mod != 25) {
			mod = 11;
		}

		while (true) {
			tempInt = tempRan.nextInt();
			if (tempInt != Integer.MIN_VALUE) {
				nextInt = Math.abs(tempInt);
				break;
			}
		}

		nextInt = nextInt % mod;
		final char nextWord = alphbts[nextInt];

		if (mod == 25) {
			mod = 11;
		}

		return nextWord;
	}

	public static String getRandomString() {
		final Random tempRan = new Random();
		int itemp = Math.abs(tempRan.nextInt(Integer.MAX_VALUE));
		itemp = itemp % 1000;
		String tempName = Integer.toString(itemp);

		tempName += getRandomChar();
		tempName += getRandomChar();
		tempName += getRandomChar();
		return tempName;
	}

}