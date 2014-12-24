package org.egov.works.web.actions.estimate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CommonsService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.lib.admbndry.Boundary;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.TechnicalSanction;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksService;
//import com.opensymphony.xwork2.validator.ValidationException;

@Result(name=BaseFormAction.SUCCESS,type="stream",location="estimatePDF", params={"inputName","estimatePDF","contentType","application/pdf","contentDisposition","no-cache"})
@ParentPackage("egov")
public class AbstractEstimatePDFAction extends BaseFormAction{
	private static final Logger logger = Logger.getLogger(AbstractEstimatePDFAction.class);
	private Long estimateID;
	private InputStream estimatePDF;
	private AbstractEstimateService abstractEstimateService;
	private EmployeeService employeeService;
	private CommonsService commonsService;
	private WorksService worksService;
	
	public Object getModel() {
		return null;
	}	
	
	public String execute(){
		if(estimateID!=null){
			AbstractEstimate estimate = getAbstractEstimate();
			TechnicalSanction ts=(TechnicalSanction)getPersistenceService().findByNamedQuery("getLatestTechSanctionNumber",estimateID);
			if(ts!=null){
				String techSanctionNo=ts.getTechSanctionNumber(); 
				if(techSanctionNo!=null) 
					estimate.setTechSanctionNumber(techSanctionNo); 
			}
			Boundary b = getTopLevelBoundary(estimate.getWard());
			CFinancialYear financialYear = getCurrentFinancialYear();
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024*100);
			 EstimatePDFGenerator pdfGenerator =new EstimatePDFGenerator(estimate,b==null?"":b.getName(),financialYear,out);
			pdfGenerator.setPersistenceService(getPersistenceService());
			pdfGenerator.setEmployeeService(employeeService);
			pdfGenerator.setBudgetDetailsDAO(abstractEstimateService.getBudgetDetailsDAO());
			pdfGenerator.setAbstractEstimateService(abstractEstimateService);
			pdfGenerator.setWorksService(worksService);
			try {
				pdfGenerator.generatePDF();
			} catch (Exception e) {
				//TODO - the message needs to be read from package.properties. Remove unused imports. 
				//TODO - We can not use specific error message for all kind of errors here. There will be run time errors which are not due to employee assignments. Use generic error message
				addActionError(e.getMessage()==null?"Error getting employee information ":e.getMessage());
				throw new EGOVRuntimeException("Error getting employee information ");
			}
			estimatePDF=new ByteArrayInputStream(out.toByteArray());
		}
		return SUCCESS;
	}

	private AbstractEstimate getAbstractEstimate() {
		return (AbstractEstimate) getPersistenceService().find("from AbstractEstimate e where e.id=?", estimateID);
	}

	protected Boundary getTopLevelBoundary(Boundary boundary) {
		Boundary b = boundary;
		while(b!=null && b.getParent()!=null){
			b=b.getParent();
		}
		return b;
	}

	protected CFinancialYear getCurrentFinancialYear() {
		/**
		 * for the year end process getCurrentFinancialYear API should return the next CFinancialYear object
		 */
		return commonsService.getFinancialYearByFinYearRange(worksService.getWorksConfigValue("FINANCIAL_YEAR_RANGE"));
		//return (CFinancialYear) getPersistenceService().find("from CFinancialYear cfinancialyear where ? between cfinancialyear.startingDate and cfinancialyear.endingDate",new java.util.Date());
	}
	
	public void setEstimateID(Long estimateID) {
		this.estimateID = estimateID;
	}
	
	public InputStream getEstimatePDF() {
		return estimatePDF;
	}
	

	public AbstractEstimateService getAbstractEstimateService() {
		return abstractEstimateService;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}
}
