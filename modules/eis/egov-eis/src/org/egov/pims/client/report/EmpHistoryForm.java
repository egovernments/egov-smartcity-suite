package org.egov.pims.client.report;


public class EmpHistoryForm extends BaseSearchForm {

private static final long serialVersionUID = 1L;

private	String designationId;

private String submitType;

public String getDesignationId() {
	return designationId;
}
public void setDesignationId(String designationId) {
	this.designationId = designationId;
}

public String getSubmitType() {
	return submitType;
}
public void setSubmitType(String submitType) {
	this.submitType = submitType;
}

}
