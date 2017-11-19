/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.infra.web.displaytag.export;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.IOException;
import java.io.Reader;

public class MyHtmlParser {
	Reader inReader;
	String outText;
	Boolean bRemoveSpaces = true;

	public Reader getInReader() {
		return this.inReader;
	}

	public void setInReader(final Reader inReader) {
		this.inReader = inReader;
	}

	public String getOutText() {
		return this.outText;
	}

	public void setOutText(final String outText) {
		this.outText = outText;
	}

	public String parseMyHtml(final Reader r, final boolean removeSpaces) {
		HTMLEditorKit.Parser parser;
		parser = new ParserDelegator();
		setBRemoveSpaces(removeSpaces);
		// Reader r = getInReader();
		try {
			parser.parse(r, new HTMLParseLister(), true);
			r.close();
		} catch (final IOException e) {

		}
		return getOutText();

	}

	/**
	 * HTML parsing proceeds by calling a callback for each and every piece of the HTML.
	 */
	class HTMLParseLister extends HTMLEditorKit.ParserCallback {

		/** Takes care of the text after striping out the HTML tags */
		@Override
		public void handleText(final char[] data, final int pos) {
			String TrimText = new String(data);
			TrimText = TrimText.replace((char) 160, ' '); // &nbsp character
			if (MyHtmlParser.this.bRemoveSpaces) {
				TrimText = TrimText.replace('�', ' ');
				setOutText(TrimText);
			}

		}

	}

	public MyHtmlParser() {
		super();


	}

	public Boolean getBRemoveSpaces() {
		return this.bRemoveSpaces;
	}

	public void setBRemoveSpaces(final Boolean removeSpaces) {
		this.bRemoveSpaces = removeSpaces;
	}

}
