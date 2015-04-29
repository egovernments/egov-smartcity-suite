package org.egov.works.web.actions.tender;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.tender.TenderResponse;
import org.egov.works.services.TenderResponseService;
import org.egov.works.services.WorksService;
import org.springframework.beans.factory.annotation.Autowired;
@Results({
@Result(name=BaseFormAction.SUCCESS,type="StreamResult.class",location="tenderResponsePDF", params={"inputName","tenderResponsePDF","contentType","application/pdf","contentDisposition","no-cache"}),
@Result(name = "reportView", type = "StreamResult.class", location = "tenderScrtAbsrtPDF", params = { "contentType", "application/pdf", "contentDisposition", "attachment; filename=${fileName}" })
})
@ParentPackage("egov")
public class TenderNegotiationPDFAction extends BaseFormAction{
	private Long tenderResponseId;
	private InputStream tenderResponsePDF;
	private InputStream tenderScrtAbsrtPDF;
	private TenderResponseService tenderResponseService;
	private ReportService reportService;
	@Autowired
        private EmployeeService employeeService;
	private WorksService worksService;
	private String fileName;
	public Object getModel() {
		return null;
	}
	
	public String execute(){		
		if(tenderResponseId!=null){			
			Map<String,String> pdfLabel=getPdfReportLabel();
			TenderResponse tenderResponse=getTenderResponse();
			Boundary boundary =null;
			if(tenderResponse!=null && tenderResponse.getTenderEstimate()!=null && tenderResponse.getTenderEstimate().getWorksPackage()==null){
				AbstractEstimate estimate = tenderResponse.getTenderEstimate().getAbstractEstimate();
				boundary = getTopLevelBoundary(estimate.getWard());		
			}		
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024*100);			
			TenderNegotiationPDFGenerator pdfGenerator =new TenderNegotiationPDFGenerator(tenderResponse,boundary==null?"":boundary.getName(),out,pdfLabel);
			pdfGenerator.setPersistenceService(getPersistenceService());
			pdfGenerator.setEmployeeService(employeeService);
			pdfGenerator.setWorksService(worksService);
			pdfGenerator.generatePDF();
			tenderResponsePDF=new ByteArrayInputStream(out.toByteArray());			
		}
		return SUCCESS;
	}
			
	private TenderResponse getTenderResponse() {	
		return tenderResponseService.findById(tenderResponseId, false);	
	}
	
	protected Boundary getTopLevelBoundary(Boundary boundary) {
		Boundary b = boundary;
		while(b!=null && b.getParent()!=null){
			b=b.getParent();
		}
		return b;
	}	
	
	public void setTenderResponseId(Long tenderResponseId) {		
		this.tenderResponseId = tenderResponseId;
	}
	
	public InputStream getTenderResponsePDF() {
		return tenderResponsePDF;
	}
	
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setTenderResponseService(TenderResponseService tenderResponseService) {
		this.tenderResponseService = tenderResponseService;
	}
	
	/*
	 * Generating label 
	 */
	public Map<String,String> getPdfReportLabel(){
		Map<String,String> pdfLabel=new HashMap<String,String>();		
		pdfLabel.put("tenderNegotiationpdf.header","AFTER NEGOTIATION COMPARATIVE STATEMENT");
		pdfLabel.put("tenderNegotiationpdf.zone","Zone: ");
		pdfLabel.put("tenderNegotiationpdf.ward","Ward ");
		pdfLabel.put("tenderNegotiationpdf.nameofwork","Name of Work: ");
		pdfLabel.put("tenderNumber","Tender Number: ");
		pdfLabel.put("tenderFileNo", "Tender File No: ");
		pdfLabel.put("tenderNegotiationpdf.tenderdate","Tender Due On: ");
		pdfLabel.put("tenderNegotiationpdf.slno","Sl \n No");
		pdfLabel.put("tenderNegotiationpdf.scheduleno","SCH\nNO");
		pdfLabel.put("tenderNegotiationpdf.descofwork","Description \n of \n Work");
		pdfLabel.put("tenderNegotiationpdf.quantity","Quantity");
		pdfLabel.put("tenderNegotiationpdf.asPerEstimate","As Per Estimate"); 
		pdfLabel.put("tenderNegotiationpdf.rate","Rate");
		pdfLabel.put("tenderNegotiationpdf.Per","Per");
		pdfLabel.put("tenderNegotiationpdf.amount","Amount \n Rs.P.");
		pdfLabel.put("tenderNegotiationpdf.asPerTender","As Per Tender"); 
		pdfLabel.put("tenderNegotiationpdf.rate","Rate \n Rs.P.");
		pdfLabel.put("tenderNegotiationpdf.aftneg","After Negotiation Vide \n Letter dated"); 
		pdfLabel.put("tenderNegotiationpdf.marketratedate","Market rate date : \n");
		pdfLabel.put("tenderNegotiationpdf.tendertotal","Total"); 
		pdfLabel.put("tenderNegotiationpdf.percentage","Tender Percentage");
		pdfLabel.put("tenderNegotiationpdf.quoted.total","Total Value [Quoted Amount]");
		pdfLabel.put("tenderNegotiationpdf.preparedby","Prepared By:");
		pdfLabel.put("tenderNegotiationpdf.checkedby","Checked By:");
		pdfLabel.put("tenderNegotiationpdf.approvaldetails","Approval Details");
		pdfLabel.put("tenderNegotiationpdf.aprvalstep","Approval Step");
		pdfLabel.put("tenderNegotiationpdf.name","Name");
		pdfLabel.put("tenderNegotiationpdf.designation","Designation");
		pdfLabel.put("tenderNegotiationpdf.aprvdon","Approved on");
		pdfLabel.put("tenderNegotiationpdf.remarks","Remarks");	
		pdfLabel.put("tenderNegotiationpdf.contractorcode","Contractor Code");
		pdfLabel.put("tenderNegotiationpdf.contractorname","Contractor Name");
		pdfLabel.put("tenderNegotiationpdf.contractoraddress","Contractor Address");	
		return pdfLabel;
	}

	public WorksService getWorksService() {
		return worksService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public InputStream getTenderScrtAbsrtPDF() {
		return tenderScrtAbsrtPDF;
	}

	public void setTenderScrtAbsrtPDF(InputStream tenderScrtAbsrtPDF) {
		this.tenderScrtAbsrtPDF = tenderScrtAbsrtPDF;
	}
	
}
