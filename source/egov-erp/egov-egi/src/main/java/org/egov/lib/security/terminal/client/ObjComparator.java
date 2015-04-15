/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
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
		int chunkType = 0; // 0 = alphabetic, 1 = NUMERIC

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

			// If both chunks contain NUMERIC characters, sort them numerically
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
