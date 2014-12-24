/*
 * @(#)ObjComparator.java 3.0, 14 Jun, 2013 3:54:47 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.security.terminal.client;

import java.io.Serializable;
import java.util.Comparator;

import org.egov.lib.security.terminal.model.Location;

public class ObjComparator implements Comparator<Location>, Serializable {

	private static final long serialVersionUID = 1L;
	char[] numbers = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

	private boolean isIn(final char ch, final char[] chars) {
		for (final char c : chars) {
			if (ch == c) {
				return true;
			}
		}
		return false;
	}

	private boolean inChunk(final char ch, final String s) {
		if (s.length() == 0) {
			return true;
		}

		final char s0 = s.charAt(0);
		int chunkType = 0; // 0 = alphabetic, 1 = numeric

		if (isIn(s0, this.numbers)) {
			chunkType = 1;
		}

		if ((chunkType == 0) && (isIn(ch, this.numbers))) {
			return false;
		}
		if ((chunkType == 1) && (!isIn(ch, this.numbers))) {
			return false;
		}

		return true;
	}

	@Override
	public int compare(final Location bp1, final Location bp2) {
		String s1 = "";
		String s2 = "";
		if (bp1.getName() != null) {
			s1 = bp1.getName();
		} else {
			s1 = "ZZZZZ";
		}
		if (bp2.getName() != null) {
			s2 = bp2.getName();
		} else {
			s2 = "ZZZZZ";
		}

		int thisMarker = 0;
		int thatMarker = 0;
		int thisNumericChunk = 0;
		int thatNumericChunk = 0;
		StringBuffer thisChunk = null;
		StringBuffer thatChunk = null;
		while ((thisMarker < s1.length()) && (thatMarker < s2.length())) {
			char thisCh = s1.charAt(thisMarker);
			char thatCh = s2.charAt(thatMarker);

			thisChunk = new StringBuffer("");
			thatChunk = new StringBuffer("");

			while ((thisMarker < s1.length()) && inChunk(thisCh, thisChunk.toString())) {
				thisChunk.append(thisCh);
				thisMarker++;
				if (thisMarker < s1.length()) {
					thisCh = s1.charAt(thisMarker);
				}
			}

			while ((thatMarker < s2.length()) && inChunk(thatCh, thatChunk.toString())) {
				thatChunk.append(thatCh);
				thatMarker++;
				if (thatMarker < s2.length()) {
					thatCh = s2.charAt(thatMarker);
				}
			}

			final int thisChunkType = isIn(thisChunk.charAt(0), this.numbers) ? 1 : 0;
			final int thatChunkType = isIn(thatChunk.charAt(0), this.numbers) ? 1 : 0;

			// If both chunks contain numeric characters, sort them numerically
			int result = 0;
			if ((thisChunkType == 1) && (thatChunkType == 1)) {
				thisNumericChunk = Integer.parseInt(thisChunk.toString());
				thatNumericChunk = Integer.parseInt(thatChunk.toString());
				if (thisNumericChunk < thatNumericChunk) {
					result = -1;
				}
				if (thisNumericChunk > thatNumericChunk) {
					result = 1;
				}
			} else {
				result = thisChunk.toString().compareTo(thatChunk.toString());
			}

			if (result != 0) {
				return result;
			}
		}
		return 0;
	}
}
