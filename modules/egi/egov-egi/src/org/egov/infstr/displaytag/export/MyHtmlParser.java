/*
 * @(#)MyHtmlParser.java 3.0, 17 Jun, 2013 11:51:29 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.displaytag.export;

import java.io.IOException;
import java.io.Reader;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

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
		// System.out.println("About to parse ");
		parser = new ParserDelegator();
		setBRemoveSpaces(removeSpaces);
		// Reader r = getInReader();
		try {
			parser.parse(r, new HTMLParseLister(), true);
			r.close();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		// TODO Auto-generated constructor stub

	}

	public Boolean getBRemoveSpaces() {
		return this.bRemoveSpaces;
	}

	public void setBRemoveSpaces(final Boolean removeSpaces) {
		this.bRemoveSpaces = removeSpaces;
	}

}
