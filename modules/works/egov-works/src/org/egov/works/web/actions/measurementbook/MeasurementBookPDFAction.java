package org.egov.works.web.actions.measurementbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.pims.service.EmployeeService;
import org.egov.tender.services.common.GenericTenderService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;


@Result(name=BaseFormAction.SUCCESS,type="stream",location="measurementBookPDF", params={"inputName","measurementBookPDF","contentType","application/pdf","contentDisposition","no-cache"})
@ParentPackage("egov")
public class MeasurementBookPDFAction extends BaseFormAction{
	private static final Logger logger = Logger.getLogger(MeasurementBookPDFAction.class);
	
	private Long measurementBookId;
	private InputStream measurementBookPDF;
	private MeasurementBookService measurementBookService;
	private WorkOrderService workOrderService;
	private EmployeeService employeeService;
	private WorksService worksService;
	private GenericTenderService genericTenderService;
		
	public String execute(){
		logger.info("1----inside action excute begin");
		if(measurementBookId!=null){
			logger.info("1----inside action excute when measurementBookId is not null "+measurementBookId);
			Map<String,String> pdfLabel=getPdfReportLabel();
			MBHeader mbHeader=getMBHeader();			
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024*100);			
			MeasurementBookPDFGenerator pdfGenerator =new MeasurementBookPDFGenerator(mbHeader,out,pdfLabel);			
			pdfGenerator.setPersistenceService(getPersistenceService());
			pdfGenerator.setEmployeeService(employeeService);
			pdfGenerator.setWorkOrderService(workOrderService);
			pdfGenerator.setMeasurementBookService(measurementBookService);			
			pdfGenerator.setWorksService(worksService);
			pdfGenerator.setGenericTenderService(genericTenderService);
			try{
				pdfGenerator.generatePDF();
			} catch (Exception e) {
				addActionError(e.getMessage()==null?MeasurementBookPDFGenerator.MEASUREMENTBOOK_PDF_ERROR:e.getMessage());
				throw new EGOVRuntimeException(MeasurementBookPDFGenerator.MEASUREMENTBOOK_PDF_ERROR);
			}
			measurementBookPDF=new ByteArrayInputStream(out.toByteArray());
		}	
		return SUCCESS;
	}
		
	public MBHeader getMBHeader(){		
		return measurementBookService.findById(measurementBookId, false);
	}
	
	public InputStream getMeasurementBookPDF() {
		return measurementBookPDF;
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setMeasurementBookId(Long measurementBookId) {
		this.measurementBookId = measurementBookId;
	}
	
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	public void setMeasurementBookService(
			MeasurementBookService measurementBookService) {
		this.measurementBookService = measurementBookService;
	}
	
	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}
	
	/*
	 * Generating label 
	 */
	public Map<String,String> getPdfReportLabel(){		
		Map<String,String> pdfLabel=new HashMap<String,String>();
		pdfLabel.put("mbpdf.header","Measurement Book Statement");
		pdfLabel.put("mbpdf.refno","MB no : ");			
		
		pdfLabel.put("mbpdf.slno","Sl. \n No.");
		pdfLabel.put("mbpdf.schno","Sch. No.");	
		pdfLabel.put("mbpdf.descofwork","Description");
		pdfLabel.put("mbpdf.revisionType","Revision Type");
		pdfLabel.put("mbpdf.completedmeasurement","Completed Measurement");		
		pdfLabel.put("mbpdf.unitrate","Unit Rate");	
		pdfLabel.put("mbpdf.unit","UOM");
		pdfLabel.put("mbpdf.reducedorpartrate","Reduced Rate(RR)/Part Rate(PR)");
		pdfLabel.put("mbpdf.newrate","New Rate");
		pdfLabel.put("mbpdf.totalvalueofcomplwork","Completed Cost");		
		pdfLabel.put("mbpdf.previousmeasurement","Previous Measurement");		
		pdfLabel.put("mbpdf.pageno","Page No.");
		pdfLabel.put("mbpdf.measurements","Measurement");		
		pdfLabel.put("mbpdf.currentmeasurement","Current Measurement");		
		pdfLabel.put("mbpdf.currentcost","Current Cost");
		pdfLabel.put("mbpdf.percentagerate","Rate:");		
		pdfLabel.put("mbpdf.netmbamount","Net MB Amount:");
		pdfLabel.put("mbpdf.preparedby","Prepared By:");
		pdfLabel.put("mbpdf.date","Date : ");
		pdfLabel.put("mbpdf.pageno","Page Number ");
		
		pdfLabel.put("mbpdf.approvaldetails","Approval Details");
		pdfLabel.put("mbpdf.aprvalstep","Approval Step");
		pdfLabel.put("mbpdf.name","Name");
		pdfLabel.put("mbpdf.designation","Designation");
		pdfLabel.put("mbpdf.aprvdon","Approved on");
		pdfLabel.put("mbpdf.remarks","Remarks");
		pdfLabel.put("mbpdf.signature","Signature");
		return pdfLabel;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public void setGenericTenderService(GenericTenderService genericTenderService) {
		this.genericTenderService = genericTenderService;
	}
}
