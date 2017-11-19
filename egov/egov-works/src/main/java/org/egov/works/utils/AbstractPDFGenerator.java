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
package org.egov.works.utils;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.component.Money;

import java.io.OutputStream;
import java.text.DecimalFormat;

public class AbstractPDFGenerator {
    public static final int LARGE_FONT = 14;
    protected Document document;
    protected final OutputStream out;

    public AbstractPDFGenerator(final OutputStream out, final String type) {
        this.out = out;
        try {
            if (type != null && "landscape".equalsIgnoreCase(type))
                document = new Document(PageSize.A4.rotate());
            else
                document = new Document();

            PdfWriter.getInstance(document, out);
            document.open();
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("estimate.pdf.error", e);
        }
    }

    protected void addRow(final PdfPTable table, final boolean needsBorder, final Paragraph... elements) {
        for (final Paragraph element : elements) {
            final PdfPCell cell = new PdfPCell(element);
            // cell.setHorizontalAlignment(element.getAlignment());
            cell.setVerticalAlignment(Element.ALIGN_TOP);
            if (!needsBorder)
                cell.setBorderWidth(0);
            table.addCell(cell);
        }
    }

    protected Paragraph spacer() {
        return new Paragraph(" ");
    }

    protected Paragraph makePara(final Object value, final int alignment) {
        final Paragraph header = new Paragraph(value == null ? "" : value.toString());
        header.setAlignment(alignment);
        return header;
    }

    protected Paragraph makeParaWithFont(final float fontSize, final Object value, final int alignment) {
        final Font font = new Font();
        font.setSize(fontSize);
        final Paragraph header = new Paragraph(value == null ? "" : value.toString(), font);
        header.setAlignment(alignment);
        return header;
    }

    protected Paragraph makePara(final Object value) {
        return new Paragraph(value == null ? "" : value.toString());
    }

    protected Paragraph makeParaWithFont(final float fontSize, final Object value) {
        final Font font = new Font();
        font.setSize(fontSize);
        return new Paragraph(value == null ? "" : value.toString(), font);
    }

    protected Paragraph makePara(final float size, final Object value) {
        final Font font = new Font();
        font.setSize(size);
        return new Paragraph(value == null ? "" : value.toString(), font);
    }

    protected Paragraph centerPara(final Object value) {
        return makePara(value, Element.ALIGN_CENTER);
    }

    protected Paragraph centerPara(final float fontSize, final Object value) {
        return makeParaWithFont(fontSize, value, Element.ALIGN_CENTER);
    }

    protected Paragraph rightPara(final Object value) {
        return makePara(value, Element.ALIGN_RIGHT);
    }

    protected Paragraph rightPara(final float size, final Object value) {
        final Font font = new Font();
        font.setSize(size);
        final Paragraph header = new Paragraph(value == null ? "" : value.toString(), font);
        header.setAlignment(Element.ALIGN_RIGHT);
        return header;
    }

    protected Font getUnderlinedFont() {
        return new Font(Font.UNDEFINED, Font.UNDEFINED, Font.UNDERLINE, null);
    }

    public String toCurrency(final Money money) {
        return toCurrency(money.getValue());
    }

    public String toCurrency(final double money) {
        final double rounded = Math.round(money * 100) / 100.0;
        final DecimalFormat formatter = new DecimalFormat("0.00");
        formatter.setDecimalSeparatorAlwaysShown(true);
        return formatter.format(rounded);
    }
}
