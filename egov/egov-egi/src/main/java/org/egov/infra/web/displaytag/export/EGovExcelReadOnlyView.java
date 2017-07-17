/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.web.displaytag.export;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.displaytag.Messages;
import org.displaytag.exception.BaseNestableJspTagException;
import org.displaytag.exception.SeverityEnum;
import org.displaytag.export.BinaryExportView;
import org.displaytag.export.excel.ExcelHssfView;
import org.displaytag.model.Column;
import org.displaytag.model.ColumnIterator;
import org.displaytag.model.HeaderCell;
import org.displaytag.model.Row;
import org.displaytag.model.RowIterator;
import org.displaytag.model.TableModel;

import javax.servlet.jsp.JspException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * Excel exporter(Read-only) using POI HSSFWorkbook
 * @author subhash
 */

public class EGovExcelReadOnlyView implements BinaryExportView {

	/**
	 * TableModel to render.
	 */
	private TableModel model;

	/**
	 * export full list?
	 */
	private boolean exportFull;

	/**
	 * include header in export?
	 */
	private boolean header;

	/**
	 * decorate export?
	 */
	private boolean decorated;

	/**
	 * Generated sheet.
	 */
	private HSSFSheet sheet;

	/**
	 * @see org.displaytag.export.ExportView#setParameters(TableModel, boolean, boolean, boolean)
	 */
	@Override
	public void setParameters(final TableModel tableModel, final boolean exportFullList, final boolean includeHeader, final boolean decorateValues) {
		this.model = tableModel;
		this.exportFull = exportFullList;
		this.header = includeHeader;
		this.decorated = decorateValues;
	}

	/**
	 * @return "application/vnd.ms-excel"
	 * @see org.displaytag.export.BaseExportView#getMimeType()
	 */
	@Override
	public String getMimeType() {
		return "application/vnd.ms-excel"; //$NON-NLS-1$
	}

	/**
	 * @see org.displaytag.export.BinaryExportView#doExport(OutputStream)
	 */
	@Override
	public void doExport(final OutputStream out) throws JspException {
		try {
			final HSSFWorkbook wb = new HSSFWorkbook();
			wb.writeProtectWorkbook("egov", "egov");// To make the workbook read-only
			this.sheet = wb.createSheet("-");

			int rowNum = 0;
			int colNum = 0;

			if (this.header) {
				// Create an header row
				final HSSFRow xlsRow = this.sheet.createRow(rowNum++);

				final HSSFCellStyle headerStyle = wb.createCellStyle();
				headerStyle.setFillPattern(HSSFCellStyle.FINE_DOTS);
				headerStyle.setFillBackgroundColor(HSSFColor.BLUE_GREY.index);
				final HSSFFont bold = wb.createFont();
				bold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				bold.setColor(HSSFColor.WHITE.index);
				headerStyle.setFont(bold);

				final Iterator iterator = this.model.getHeaderCellList().iterator();

				while (iterator.hasNext()) {
					final HeaderCell headerCell = (HeaderCell) iterator.next();

					String columnHeader = headerCell.getTitle();

					if (columnHeader == null) {
						columnHeader = StringUtils.capitalize(headerCell.getBeanPropertyName());
					}

					final HSSFCell cell = xlsRow.createCell(colNum++);
					cell.setCellValue(escapeColumnValue(columnHeader));
					cell.setCellStyle(headerStyle);
				}
			}

			// get the correct iterator (full or partial list according to the exportFull field)
			final RowIterator rowIterator = this.model.getRowIterator(this.exportFull);

			// iterator on rows
			while (rowIterator.hasNext()) {
				final Row row = rowIterator.next();
				final HSSFRow xlsRow = this.sheet.createRow(rowNum++);
				colNum = 0;

				// iterator on columns
				final ColumnIterator columnIterator = row.getColumnIterator(this.model.getHeaderCellList());

				while (columnIterator.hasNext()) {
					final Column column = columnIterator.nextColumn();

					// Get the value to be displayed for the column
					final Object value = column.getValue(this.decorated);

					final HSSFCell cell = xlsRow.createCell(colNum++);

					if (value instanceof Number) {
						final Number num = (Number) value;
						cell.setCellValue(num.doubleValue());
					} else if (value instanceof Date) {
						cell.setCellValue((Date) value);
					} else if (value instanceof Calendar) {
						cell.setCellValue((Calendar) value);
					} else {
						cell.setCellValue(escapeColumnValue(value));
					}
				}
			}

			for (short i = 0; i < colNum; i++) {
				this.sheet.autoSizeColumn(i, true);
			}
			wb.write(out);
		} catch (final Exception e) {
			throw new ExcelGenerationException(e);
		}
	}

	/**
	 * Escape certain values that are not permitted in excel cells.
	 * @param rawValue the object value
	 * @return the escaped value
	 */
	protected HSSFRichTextString escapeColumnValue(final Object rawValue) {
		if (rawValue == null) {
			return null;
		}
		String returnString = ObjectUtils.toString(rawValue);
		// escape the String to get the tabs, returns, newline explicit as \t \r \n
		returnString = StringEscapeUtils.escapeJava(StringUtils.trimToEmpty(returnString));
		// remove tabs, insert four whitespaces instead
		returnString = StringUtils.replace(StringUtils.trim(returnString), "\\t", "    ");
		// remove the return, only newline valid in excel
		returnString = StringUtils.replace(StringUtils.trim(returnString), "\\r", " ");
		// unescape so that \n gets back to newline
		returnString = StringEscapeUtils.unescapeJava(returnString);
		returnString = removeHtmlTagsAndSpaces(returnString);
		return new HSSFRichTextString(returnString);
	}

	/**
	 * Wraps IText-generated exceptions.
	 * @author Fabrizio Giustina
	 * @version $Revision: 1.2 $ ($Author: fgiust $)
	 */
	static class ExcelGenerationException extends BaseNestableJspTagException {

		/**
		 * D1597A17A6.
		 */
		private static final long serialVersionUID = 899149338534L;

		/**
		 * Instantiate a new PdfGenerationException with a fixed message and the given cause.
		 * @param cause Previous exception
		 */
		public ExcelGenerationException(final Throwable cause) {
			super(ExcelHssfView.class, Messages.getString("ExcelView.errorexporting"), cause); //$NON-NLS-1$
		}

		/**
		 * @see org.displaytag.exception.BaseNestableJspTagException#getSeverity()
		 */
		@Override
		public SeverityEnum getSeverity() {
			return SeverityEnum.ERROR;
		}
	}

	/**
	 * Checks for nbsp spaces and replaces with empty string
	 */
	private String removeHtmlTagsAndSpaces(final String strValue) {
		String returnValue = null;
		if (strValue != null) {
			final Reader strReader = new StringReader(strValue);
			final MyHtmlParser parser = new MyHtmlParser();
			returnValue = parser.parseMyHtml(strReader, true);
		}
		return returnValue;
	}
}
