/*
 * @(#)EGovExcelView.java 3.0, 17 Jun, 2013 11:51:08 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.displaytag.export;

import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.lang.StringUtils;
import org.displaytag.export.BaseExportView;
import org.displaytag.model.TableModel;

public class EGovExcelView extends BaseExportView {

	/**
	 * @see org.displaytag.export.BaseExportView#setParameters(TableModel, boolean, boolean, boolean)
	 */
	@Override
	public void setParameters(final TableModel tableModel, final boolean exportFullList, final boolean includeHeader, final boolean decorateValues) {
		super.setParameters(tableModel, exportFullList, includeHeader, decorateValues);
	}

	/**
	 * @see org.displaytag.export.ExportView#getMimeType()
	 * @return "application/vnd.ms-excel"
	 */
	@Override
	public String getMimeType() {
		return "application/vnd.ms-excel"; //$NON-NLS-1$
	}

	/**
	 * @see org.displaytag.export.BaseExportView#getRowEnd()
	 */
	@Override
	protected String getRowEnd() {
		return "\n"; //$NON-NLS-1$
	}

	/**
	 * @see org.displaytag.export.BaseExportView#getCellEnd()
	 */
	@Override
	protected String getCellEnd() {
		return "\t"; //$NON-NLS-1$
	}

	@Override
	protected String getDocumentStart() {
		return escapeColumnValue(super.getDocumentStart());
	}

	/**
	 * @see org.displaytag.export.BaseExportView#getAlwaysAppendCellEnd()
	 * @return false
	 */
	@Override
	protected boolean getAlwaysAppendCellEnd() {
		return false;
	}

	/**
	 * @see org.displaytag.export.BaseExportView#getAlwaysAppendRowEnd()
	 * @return false
	 */
	@Override
	protected boolean getAlwaysAppendRowEnd() {
		return false;
	}

	/**
	 * Escaping for excel format.
	 * <ul>
	 * <li>Quotes inside quoted strings are escaped with a double quote</li>
	 * <li>Fields are surrounded by " (should be optional, but sometimes you get a "Sylk error" without those)</li>
	 * </ul>
	 * @see org.displaytag.export.BaseExportView#escapeColumnValue(java.lang.Object)
	 */
	@Override
	protected String escapeColumnValue(Object value) {
		if (value != null && !value.toString().trim().equalsIgnoreCase("")) {
			// strip out the html tags
			value = removeHtmlTagsAndSpaces(value.toString());
			// quotes around fields are needed to avoid occasional "Sylk format invalid" messages from excel

			return "\"" //$NON-NLS-1$
					+ StringUtils.replace(StringUtils.trim(value.toString()), "\"", "\"\"") //$NON-NLS-1$ //$NON-NLS-2$ 
					+ "\""; //$NON-NLS-1$ 
		}

		return null;
	}

	/**
	 * Custom-method. Checks for nbsp spaces and replaces with empty string
	 */
	private String removeHtmlTagsAndSpaces(final String strValue) {
		String returnValue = null;
		if (strValue != null) {
			// if((strValue.trim().indexOf("&nbsp;") > -1) ||(strValue.trim().indexOf("<hr>") > -1) || (strValue.trim().indexOf("<B>") > -1) ||
			// (strValue.trim().indexOf("</B>") > -1) || (strValue.trim().indexOf("Br") > -1)) {
			final Reader strReader = new StringReader(strValue);
			final MyHtmlParser parser = new MyHtmlParser();
			returnValue = parser.parseMyHtml(strReader, true);
			// returnValue = returnValue.trim().replaceAll("\\s","");

		}
		return returnValue;
	}

}
