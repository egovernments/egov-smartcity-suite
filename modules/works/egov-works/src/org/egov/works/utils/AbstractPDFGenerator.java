package org.egov.works.utils;

import java.io.OutputStream;
import java.text.DecimalFormat;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.models.Money;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class AbstractPDFGenerator {
	public static final int LARGE_FONT=14;
	protected Document document;
	protected final OutputStream out;
	public AbstractPDFGenerator(OutputStream out,String type){
		this.out = out;
		try {
			if(type!=null && "landscape".equalsIgnoreCase(type))
				document = new Document(PageSize.A4.rotate());
			else
				document = new Document();
			
			PdfWriter.getInstance(document, out);
			document.open();
		} catch (Exception e) {
			throw new EGOVRuntimeException("estimate.pdf.error", e);
		}		
	}
	
	protected void addRow(PdfPTable table, boolean needsBorder, Paragraph... elements) {
		for (Paragraph element : elements) {
			PdfPCell cell = new PdfPCell(element);
		//	cell.setHorizontalAlignment(element.getAlignment());
			cell.setVerticalAlignment(Element.ALIGN_TOP);
			if(!needsBorder){
				cell.setBorderWidth(0);
			}
			table.addCell(cell);
		}
	}
	
	protected void addRowWithSignature(PdfPTable table, boolean needsBorder, PdfPCell... elements) {
		for (PdfPCell cell : elements) {
			cell.setVerticalAlignment(Element.ALIGN_TOP);
			if(!needsBorder){
				cell.setBorderWidth(0);
			}
			table.addCell(cell);
		}
	}


	protected Paragraph spacer() {
		return new Paragraph(" ");
	}

	protected Paragraph makePara(Object value, int alignment) {
		Paragraph header = new Paragraph(value == null ? "" : value.toString());
		header.setAlignment(alignment);
		return header;
	}

	protected Paragraph makePara(Object value) {
		return new Paragraph(value == null ? "" : value.toString());
	}

	protected Paragraph centerPara(Object value) {
		return makePara(value, Element.ALIGN_CENTER);
	}

	protected Paragraph rightPara(Object value) {
		return makePara(value, Element.ALIGN_RIGHT);
	}
	protected Font getUnderlinedFont() {
		return new Font(Font.UNDEFINED, Font.UNDEFINED, Font.UNDERLINE, null);
	}
	
	public String toCurrency(Money money){
		return toCurrency(money.getValue());
	}
	public String toCurrency(double money){
		double rounded=Math.round(money*100)/100.0;
		DecimalFormat formatter = new DecimalFormat("0.00");
		formatter.setDecimalSeparatorAlwaysShown(true);
		return formatter.format(rounded);
	}
}
