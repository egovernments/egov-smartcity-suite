package org.egov.works.web.actions.estimate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.dispatcher.StreamResult;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CommonsService;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksService;
import org.springframework.beans.factory.annotation.Autowired;

@Result(name=BaseFormAction.SUCCESS,type="StreamResult.class",location="estimatePDF", params={"inputName","estimatePDF","contentType","application/pdf","contentDisposition","no-cache;filename=AbstractEstimatePDF.pdf"})
@ParentPackage("egov")
public class AbstractEstimatePDFAction extends BaseFormAction{
	private static final Logger logger = Logger.getLogger(AbstractEstimatePDFAction.class);
	private Long estimateID;
	private InputStream estimatePDF;
	private AbstractEstimateService abstractEstimateService;
	@Autowired
        private EmployeeService employeeService;
	@Autowired
        private CommonsService commonsService;
	private WorksService worksService;
	private BudgetDetailsDAO budgetDetailsDAO;
	
	public Object getModel() {
		return null;
	}	
	
	public String execute(){
		if(estimateID!=null){
			AbstractEstimate estimate = getAbstractEstimate();
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
				} catch (ValidationException e) {
					// TODO Auto-generated catch block
					logger.debug("exception "+e);
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

	public EmployeeService getemployeeService() {
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

	public void setBudgetDetailsDAO(BudgetDetailsDAO budgetDetailsDAO) {
		this.budgetDetailsDAO = budgetDetailsDAO;
	}

}
