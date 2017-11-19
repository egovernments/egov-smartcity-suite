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

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.displaytag.Messages;
import org.displaytag.exception.BaseNestableJspTagException;
import org.displaytag.exception.SeverityEnum;
import org.displaytag.export.BinaryExportView;
import org.displaytag.export.PdfView;
import org.displaytag.model.Column;
import org.displaytag.model.ColumnIterator;
import org.displaytag.model.HeaderCell;
import org.displaytag.model.Row;
import org.displaytag.model.RowIterator;
import org.displaytag.model.TableModel;
import org.displaytag.util.TagConstants;
import org.egov.infra.exception.ApplicationRuntimeException;

import javax.servlet.jsp.JspException;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Iterator;

public class EGovPdfView implements BinaryExportView {
	// private PdfView pdfView ;

	/**
	 * include header in export?
	 */
	private boolean header;
	/**
	 * TableModel to render.
	 */
	private TableModel model;

	/**
	 * export full list?
	 */
	private boolean exportFull;

	/**
	 * The default font used in the document.
	 */
	private Font smallFont;

	/**
	 * decorate export?
	 */
	private boolean decorated;

	/**
	 * This is the table, added as an Element to the PDF document. It contains all the data, needed to represent the visible table into the PDF
	 */
	private Table tablePDF;

	private Paragraph tableCaption;

	@Override
	public void setParameters(final TableModel tableModel, final boolean exportFullList, final boolean includeHeader, final boolean decorateValues) {
		this.model = tableModel;
		this.exportFull = exportFullList;
		this.header = includeHeader;
		this.decorated = decorateValues;

	}

	@Override
	public String getMimeType() {
		return "application/pdf"; //$NON-NLS-1$
	}

	/**
	 * Initialize the main info holder table.
	 * @throws BadElementException for errors during table initialization
	 */
	protected void initTable() throws BadElementException {
		this.tablePDF = new Table(this.model.getNumberOfColumns());
		this.tablePDF.setCellsFitPage(true);
		this.tablePDF.setWidth(100);

		this.tablePDF.setPadding(2);
		this.tablePDF.setSpacing(0);

		this.smallFont = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, new Color(0, 0, 0));
		/*
		 * try { BaseFont btKannada = BaseFont.createFont("Tunga", "Identity-H", BaseFont.NOT_EMBEDDED); } catch (DocumentException e) {   } catch (IOException e) {   }
		 */

	}

	protected void generatePDFTable() throws JspException, BadElementException {
		if (this.header) {
			generateCaption();
			generateHeaders();
		}
		this.tablePDF.endHeaders();
		generateRows();

	}

	/**
	 * Generates all the row cells.
	 * @throws JspException for errors during value retrieving from the table model
	 * @throws BadElementException errors while generating content
	 */
	protected void generateRows() throws JspException, BadElementException {
		// get the correct iterator (full or partial list according to the exportFull field)
		final RowIterator rowIterator = this.model.getRowIterator(this.exportFull);
		// iterator on rows
		while (rowIterator.hasNext()) {
			final Row row = rowIterator.next();

			// iterator on columns
			final ColumnIterator columnIterator = row.getColumnIterator(this.model.getHeaderCellList());

			while (columnIterator.hasNext()) {
				final Column column = columnIterator.nextColumn();

				// Get the value to be displayed for the column
				final Object value = column.getValue(this.decorated);
				final Cell cell = getCell(ObjectUtils.toString(value));
				if (value instanceof BigDecimal) {
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				}
				this.tablePDF.addCell(cell);
			}
		}
	}

	/**
	 * Generates the header cells, which persist on every page of the PDF document.
	 * @throws BadElementException IText exception
	 */
	protected void generateHeaders() throws BadElementException {
		final Iterator iterator = this.model.getHeaderCellList().iterator();

		while (iterator.hasNext()) {
			final HeaderCell headerCell = (HeaderCell) iterator.next();

			String columnHeader = headerCell.getTitle();

			if (columnHeader == null) {
				columnHeader = StringUtils.capitalize(headerCell.getBeanPropertyName());
			}

			final Cell hdrCell = getCell(columnHeader);
			hdrCell.setGrayFill(0.9f);
			hdrCell.setHeader(true);
			this.tablePDF.addCell(hdrCell);

		}
	}

	protected void generateCaption() throws BadElementException {
		final Paragraph caption = new Paragraph(new Chunk(removeHtmlTagsAndSpaces(this.model.getCaption()), this.getCaptionFont()));
		caption.setAlignment(this.getCaptionHorizontalAlignment());

		this.tableCaption = caption;

	}

	@Override
	public void doExport(final OutputStream out) throws JspException {

		try {
			// Initialize the table with the appropriate number of columns
			initTable();

			// Initialize the Document and register it with PdfWriter listener and the OutputStream
			final Document document = new Document(PageSize.A4.rotate(), 60, 60, 40, 40);
			document.addCreationDate();
			final HeaderFooter footer = new HeaderFooter(new Phrase(TagConstants.TAGNAME_CAPTION, this.smallFont), true);
			footer.setBorder(Rectangle.NO_BORDER);
			footer.setAlignment(Element.ALIGN_CENTER);

			// PdfWriter.getInstance(document, out);
			PdfWriter.getInstance(document, out).setPageEvent(new PageNumber());
			// Fill the virtual PDF table with the necessary data
			generatePDFTable();
			document.open();

			// Table table = new Table(this.model.getNumberOfColumns());
			// ItextTableWriter writer = new ItextTableWriter(tablePDF, document);
			// writer.writeTable(this.model, "-1");
			// document.setFooter(footer);
			// document.setHeader(footer);
			document.add(this.tableCaption);
			document.add(this.tablePDF);
			document.close();

		} catch (final Exception e) {

			throw new PdfGenerationException(e);
		}

	}

	protected int getCaptionHorizontalAlignment() {
		return Element.ALIGN_LEFT;
	}

	/**
	 * Returns a formatted cell for the given value.
	 * @param value cell value
	 * @return Cell
	 * @throws BadElementException errors while generating content
	 */
	private Cell getCell(String value) throws BadElementException {

		value = removeHtmlTagsAndSpaces(value);
		final Cell cell = new Cell(new Chunk(StringUtils.trimToEmpty(value), this.smallFont));
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setLeading(8);
		return cell;
	}

	/**
	 * Custom-method. Checks for html tags and spaces and replaces with empty string
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

	protected Font getCaptionFont() {
		return FontFactory.getFont(FontFactory.HELVETICA, 17, Font.BOLD, new Color(0x00, 0x00, 0x00));
	}

	/**
	 * Wraps IText-generated exceptions.
	 * @author Fabrizio Giustina
	 * @version $Revision: 1.7 $ ($Author: fgiust $)
	 */
	static class PdfGenerationException extends BaseNestableJspTagException {

		/**
		 * D1597A17A6.
		 */
		private static final long serialVersionUID = 899149338534L;

		/**
		 * Instantiate a new PdfGenerationException with a fixed message and the given cause.
		 * @param cause Previous exception
		 */
		public PdfGenerationException(final Throwable cause) {
			super(PdfView.class, Messages.getString("PdfView.errorexporting"), cause); //$NON-NLS-1$
		}

		/**
		 * @see org.displaytag.exception.BaseNestableJspTagException#getSeverity()
		 */
		@Override
		public SeverityEnum getSeverity() {
			return SeverityEnum.ERROR;
		}
	}

	private static class PageNumber extends PdfPageEventHelper {

		/**
		 * @see com.lowagie.text.pdf.PdfPageEventHelper#onEndPage(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
		 */
		@Override
		public void onEndPage(final PdfWriter writer, final Document document) {
			/** The headertable. */
			final PdfPTable table = new PdfPTable(2);
			/** A template that will hold the total number of pages. */
			final PdfTemplate tpl = writer.getDirectContent().createTemplate(100, 100);
			/** The font that will be used. */
			BaseFont helv = null;
			try {
				helv = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
			} catch (final DocumentException e) {

				throw new ApplicationRuntimeException("Exception occured -----> " + e.getMessage());
			} catch (final IOException e) {

				throw new ApplicationRuntimeException("Exception occured -----> " + e.getMessage());
			}
			final PdfContentByte cb = writer.getDirectContent();
			cb.saveState();
			cb.restoreState();
			// write the headertable
			table.setTotalWidth(document.right() - document.left());
			//table.writeSelectedRows(0, -1, document.left(), document.getPageSize().getHeight() - 50, cb);
			// compose the footer
			final String text = "Page " + writer.getPageNumber();
			final float textSize = helv.getWidthPoint(text, 12);
			final float textBase = document.bottom() - 20;
			cb.beginText();
			cb.setFontAndSize(helv, 12);
			final float adjust = helv.getWidthPoint("0", 12);
			cb.setTextMatrix(document.right() - textSize - adjust, textBase);
			cb.showText(text);
			cb.endText();
			cb.addTemplate(tpl, document.right() - adjust, textBase);
			cb.saveState();
			cb.restoreState();
		}
	}

}
