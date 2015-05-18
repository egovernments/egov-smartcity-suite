/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.web.actions.contractoradvance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.eis.entity.DrawingOfficer;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.SearchFormAction;
import org.egov.works.models.contractoradvance.ContractorAdvanceRequisition;
import org.egov.works.models.masters.Contractor;
import org.egov.works.services.WorksService;
import org.egov.works.services.contractoradvance.ContractorAdvanceService;
import org.egov.works.utils.WorksConstants;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Sathish P
 *
 */
@ParentPackage("egov")
public class SearchContractorAdvanceRequisitionAction extends SearchFormAction { 

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(SearchContractorAdvanceRequisitionAction.class);
	private Integer arfStatus;
	private String estimateNumber;
	private Date advanceRequisitionFromDate;
	private Date advanceRequisitionToDate;
	private Long contractorId;
	private String workOrderNumber;
	private Integer executingDepartmentId = -1;
	private Integer drawingOfficerId;
	public static final Locale LOCALE = new Locale("en","IN");
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",LOCALE);
	private WorksService worksService;	
	@Autowired
	private DepartmentService departmentService;
	@Autowired
        private EmployeeService employeeService;
	private PersistenceService<Contractor, Long> contractorService;
	private ContractorAdvanceService contractorAdvanceService;
	
	public SearchContractorAdvanceRequisitionAction() {		
		
	}

	@Override
	public Object getModel() {	
		return null;
	}
	
	@Override
	public void prepare() {
		super.prepare(); 
		addDropdownData("statusList", contractorAdvanceService.getAllContractorAdvanceRequisitionStatus());
		addDropdownData("executingDepartmentList", departmentService.getAllDepartments()); 
		List<DrawingOfficer> drawingOfficerList = contractorAdvanceService.getAllDrawingOfficerFromARF();
		if(drawingOfficerList != null)
			addDropdownData("drawingOfficerList", drawingOfficerList);
		else
			addDropdownData("drawingOfficerList", Collections.emptyList());
	}
	
	public String beforeSearch() {		
		return "search";
	}
		
	private Map getQuery(){
		StringBuffer query = new StringBuffer(700);
		List<Object> paramList = new ArrayList<Object>();
		HashMap<String,Object> queryAndParams=new HashMap<String,Object>();
		query.append("from ContractorAdvanceRequisition arf where arf.status.code <> ? ");
		paramList.add(ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.NEW.toString());
		if(arfStatus != null && arfStatus != -1){
			query.append(" and arf.status.id = ?");
			paramList.add(arfStatus);
		}
		
		if(StringUtils.isNotBlank(estimateNumber)){
			query.append(" and UPPER(arf.workOrderEstimate.estimate.estimateNumber) like '%'||?||'%'");
			paramList.add(StringUtils.trim(estimateNumber).toUpperCase());
		}
		
		if(advanceRequisitionFromDate!=null && advanceRequisitionToDate!=null && getFieldErrors().isEmpty()){
			query.append(" and arf.advanceRequisitionDate between ? and ? ");
			paramList.add(advanceRequisitionFromDate);
			paramList.add(advanceRequisitionToDate);
		}
		
		if(contractorId != 0 && contractorId != -1){
			query.append(" and arf.workOrderEstimate.workOrder.contractor.id = ? ");
			paramList.add(Long.valueOf(contractorId));
		}
		
		if(executingDepartmentId !=0 && executingDepartmentId!= -1){		
			query.append(" and arf.workOrderEstimate.estimate.executingDepartment.id = ? ");
			paramList.add(executingDepartmentId);
		}	
		
		if(StringUtils.isNotBlank(workOrderNumber)){
			query.append(" and UPPER(arf.workOrderEstimate.workOrder.workOrderNumber) like '%'||?||'%'");
			paramList.add(StringUtils.trim(workOrderNumber).toUpperCase());
		}
		
		if(drawingOfficerId != null && drawingOfficerId != 0 && drawingOfficerId != -1){
			query.append(" and arf.drawingOfficer.id = ?");
			paramList.add(drawingOfficerId);
		}
		
		query.append(" order by arf.advanceRequisitionDate");
		
		queryAndParams.put("query", query.toString());
		queryAndParams.put("params", paramList);
		return queryAndParams;
	}

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {		
		String query =null;
		String countQuery = null;
		Map queryAndParms=null;
		List<Object> paramList = new ArrayList<Object>();
			queryAndParms=getQuery();
			paramList=(List<Object>)queryAndParms.get("params");
			query=(String) queryAndParms.get("query");
			countQuery="select count(distinct arf.id) " + query;
			query="select distinct arf "+query;
			return new SearchQueryHQL(query, countQuery, paramList);
	}
	
	public String search() 	{
		return "search";
	}
	
	public String searchList() {
		getPersistenceService().getSession().setDefaultReadOnly(true);
		getPersistenceService().getSession().setFlushMode(FlushMode.MANUAL);
		boolean isError=false;
		if(advanceRequisitionFromDate!=null && advanceRequisitionToDate==null){
			addFieldError("advanceRequisitionToDate",getText("search.advanceRequisitionToDate.null"));
			isError=true;
		}
		if(advanceRequisitionToDate!=null && advanceRequisitionFromDate==null){
			addFieldError("startdate",getText("search.advanceRequisitionFromDate.null"));		
			isError=true;
		}
		
		if(!DateUtils.compareDates(advanceRequisitionToDate,advanceRequisitionFromDate)){
			addFieldError("advanceRequisitionToDate",getText("advanceRequisitionFromDate.greaterthan.advanceRequisitionToDate"));
			isError=true;
		}
		
		if(isError){
			return "search";
		}

		setPageSize(WorksConstants.PAGE_SIZE);
		super.search();
		
		if(searchResult!=null && searchResult.getList()!=null && !searchResult.getList().isEmpty()) {
			setOwnerName();
		}
		return "search";
	}
			
	private void setOwnerName() {
		List<ContractorAdvanceRequisition> arfList = new LinkedList<ContractorAdvanceRequisition>();
		
		Iterator iter = searchResult.getList().iterator();
		while(iter.hasNext()) {
			Object row = (Object)iter.next();
			ContractorAdvanceRequisition arf = (ContractorAdvanceRequisition) row;
			PersonalInformation emp = employeeService.getEmployeeforPosition(arf.getCurrentState().getOwnerPosition());
			if(emp!=null){
				arf.setOwnerName(emp.getUserMaster().getName());
			}
			arfList.add(arf);
		}
		searchResult.getList().clear();
		searchResult.getList().addAll(arfList);
	}
		
	public List<String> getActionsList() { 
		String actions = worksService.getWorksConfigValue("ARF_SHOW_ACTIONS");
		if(actions!=null)
		  return Arrays.asList(actions.split(","));
		return new ArrayList<String>();
	}
	
	public Map<String,Object> getContractorsInARF() {
		Map<String,Object> contractorsInARFList = new LinkedHashMap<String, Object>();	
		List<Contractor> contractorList = contractorService.findAllByNamedQuery("getAllContractorsInARF");
		if(contractorList != null) {
			for(Contractor contractor :contractorList){
				contractorsInARFList.put(contractor.getId()+"", contractor.getCode()+" - "+contractor.getName());
			}			
		}
		return contractorsInARFList; 
	}

	public String getEstimateNumber() {
		return estimateNumber;
	}

	public void setEstimateNumber(String estimateNumber) {
		this.estimateNumber = estimateNumber;
	}

	public Date getAdvanceRequisitionFromDate() {
		return advanceRequisitionFromDate;
	}

	public void setAdvanceRequisitionFromDate(Date advanceRequisitionFromDate) {
		this.advanceRequisitionFromDate = advanceRequisitionFromDate;
	}

	public Date getAdvanceRequisitionToDate() {
		return advanceRequisitionToDate;
	}

	public void setAdvanceRequisitionToDate(Date advanceRequisitionToDate) {
		this.advanceRequisitionToDate = advanceRequisitionToDate;
	}

	public Long getContractorId() {
		return contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

	public String getWorkOrderNumber() {
		return workOrderNumber;
	}

	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}

	public Integer getExecutingDepartmentId() {
		return executingDepartmentId;
	}

	public void setExecutingDepartmentId(Integer executingDepartmentId) {
		this.executingDepartmentId = executingDepartmentId;
	}

	public Integer getDrawingOfficerId() {
		return drawingOfficerId;
	}

	public void setDrawingOfficerId(Integer drawingOfficerId) {
		this.drawingOfficerId = drawingOfficerId;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public Integer getArfStatus() {
		return arfStatus;
	}

	public void setArfStatus(Integer arfStatus) {
		this.arfStatus = arfStatus;
	}

	public void setContractorService(
			PersistenceService<Contractor, Long> contractorService) {
		this.contractorService = contractorService;
	}

	public void setContractorAdvanceService(
			ContractorAdvanceService contractorAdvanceService) {
		this.contractorAdvanceService = contractorAdvanceService;
	}

}