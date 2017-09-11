package org.egov.restapi.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DocumentTypeDetails implements Serializable {

	private String documentName;
	private String documentNumber;
	private String documentDate;
	private String mroProceedingNumber;
	private String mroProceedingDate;
	private String courtName;
	private Boolean signed;

	@Override
	public String toString() {
		return "DocumentTypeDetails [documentName=" + documentName + ", documentNumber=" + documentNumber
				+ ", documentDate=" + documentDate + ", mroProceedingNumber=" + mroProceedingNumber
				+ ", mroProceedingDate=" + mroProceedingDate + ", courtName=" + courtName + ", signed=" + signed + "]";
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(String documentDate) {
		this.documentDate = documentDate;
	}

	public String getMroProceedingNumber() {
		return mroProceedingNumber;
	}

	public void setMroProceedingNumber(String mroProceedingNumber) {
		this.mroProceedingNumber = mroProceedingNumber;
	}

	public String getMroProceedingDate() {
		return mroProceedingDate;
	}

	public void setMroProceedingDate(String mroProceedingDate) {
		this.mroProceedingDate = mroProceedingDate;
	}

	public String getCourtName() {
		return courtName;
	}

	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

	public Boolean getSigned() {
		return signed;
	}

	public void setSigned(Boolean signed) {
		this.signed = signed;
	}

}
