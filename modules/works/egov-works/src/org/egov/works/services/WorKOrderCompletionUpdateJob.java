package org.egov.works.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.egov.dms.services.FileManagementService;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.scheduler.GenericJob;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.workorder.WorkOrder;


public class WorKOrderCompletionUpdateJob  implements GenericJob{

	private static final Logger LOGGER = Logger.getLogger(WorKOrderCompletionUpdateJob.class);
	protected PersistenceService persistenceService;
	private GenericHibernateDaoFactory genericDao;
	private WorkOrderService workOrderService;
	private FileManagementService fileMgmtService;
	private EmployeeService employeeService;
	
	public WorKOrderCompletionUpdateJob() {
	}

	
	@Override
	public void executeJob() {
		createNotificationForWO();
		
	}
	
	public void createNotificationForWO(){
		Date currentDate=new Date();
		List<WorkOrder> woList=workOrderService.getWOForCompletionDateChange(currentDate);
		if(woList!=null)
		if(woList!=null && !woList.isEmpty()){
			HibernateUtil.beginTransaction();
			for(WorkOrder wo:woList){
				createNotification(wo);
			}
			HibernateUtil.commitTransaction();
		}

	}
	
	public void createNotification(WorkOrder workOrder){

		final HashMap<String, String> fileDetails = new HashMap<String, String>();
		fileDetails.put("fileCategory", "INTER DEPARTMENT");
		fileDetails.put("filePriority", "MEDIUM");
		fileDetails.put("fileHeading", "Work Completion Date ");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		String fileSummary="";
		Date dd=workOrder.getWorkCompletionDate();
		if(dd==null)
			dd=new Date();
		fileSummary = "Work Order Number:"+workOrder.getWorkOrderNumber()+" is going to be completed on "+dateFormatter.format(dd)+".<br> Estimate Number:";
		fileSummary+=workOrder.getWorkOrderEstimates().get(0).getEstimate().getEstimateNumber()+"<br>";
		fileSummary+=" Contractor Name:"+workOrder.getContractor().getName()+"<br> Contractor Code:"+workOrder.getContractor().getCode();
		fileDetails.put("fileSummary", fileSummary);
		fileDetails.put("fileSource", "INTER DEPARTMENT");
		fileDetails.put("senderAddress", "");
		fileDetails.put("senderName", workOrder.getWorkOrderPreparedBy().getName());
		fileDetails.put("senderPhone", "");
		fileDetails.put("senderEmail", "");
		
		String user="";
		if(workOrder.getWorkOrderPreparedBy().getUserMaster()!=null){
			user=workOrder.getWorkOrderPreparedBy().getUserMaster().getId().toString();
		}else{
			PersonalInformation emp = employeeService.getEmployeeforPosition(workOrder.getState().getOwner());//Owner of Approved WO
			if(emp!=null)
				user=emp.getUserMaster().getId().toString();
		}
		fileMgmtService.generateFileNotification(fileDetails,user);
	
	}


	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}


	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setFileMgmtService(FileManagementService fileMgmtService) {
		this.fileMgmtService = fileMgmtService; 
	}

}

