package org.egov.works.web.actions.contractoradvance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.contractorBill.ContractorBillRegister;
import org.egov.works.models.contractoradvance.ContractorAdvanceRequisition;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.services.contractoradvance.ContractorAdvanceService;
import org.egov.works.utils.WorksConstants;

public class AjaxContractorAdvanceAction extends BaseFormAction {
	private static final Logger LOGGER = Logger.getLogger(AjaxContractorAdvanceAction.class);

	private static final String ESTIMATE_NUMBER_SEARCH_RESULTS = "estimateNoSearchResults";	
	private static final String WORKORDER_NUMBER_SEARCH_RESULTS = "workOrderNoSearchResults";
	private static final String WP_NUMBER_SEARCH_RESULTS = "wpNoSearchResults";
	private static final String TN_NUMBER_SEARCH_RESULTS = "tenderNegotiationNoSearchResults";
	private static final String DRAWINGOFFICER_SEARCH_RESULTS = "drawingOfficers";
	
	private String query;
	private List<String> estimateNumberSearchList = new LinkedList<String>();
	private List<String> workOrderNumberSearchList = new LinkedList<String>();
	private List<String> wpNumberSearchList = new LinkedList<String>();
	private List<String> tenderNegotiationNumberSearchList = new LinkedList<String>();
	private List<HashMap> drawingOfficerList = new LinkedList<HashMap>();
	
	private ContractorAdvanceService contractorAdvanceService;
	private Date advanceRequisitionDate;
	
	public String execute(){
		return SUCCESS; 
	}

	public Object getModel() {
		return null;
	}
		
	/*
	 * Autocomplete for estimates where WO is approved and part bills are not created
	 */
	public String searchEstimateNumber(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		if(!StringUtils.isEmpty(query)) {
			strquery="select distinct(woe.estimate.estimateNumber) from WorkOrderEstimate woe where woe.estimate.parent is null " +
					"and NOT EXISTS (select 1 from MBHeader mbh where mbh.workOrderEstimate.id = woe.id and mbh.egwStatus.code = ? and (mbh.egBillregister is not null and mbh.egBillregister.billstatus <> ?)) " +
					"and woe.workOrder.egwStatus.code = ? and UPPER(woe.estimate.estimateNumber) like '%'||?||'%' and woe.estimate.egwStatus.code = ? order by woe.estimate.estimateNumber";
			params.add(MBHeader.MeasurementBookStatus.APPROVED.toString());	
			params.add(ContractorBillRegister.BillStatus.CANCELLED.toString());
			params.add(WorksConstants.APPROVED.toString());
			params.add(query.toUpperCase());
			params.add(AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString());
			
			estimateNumberSearchList = persistenceService.findAllBy(strquery,params.toArray());
		}
		return ESTIMATE_NUMBER_SEARCH_RESULTS;
	}
	
	/*
	 * Autocomplete for WPs where WO is approved
	 */
	public String searchWPNumber(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		if(!StringUtils.isEmpty(query)) {
			strquery="select distinct(woe.workOrder.packageNumber) from WorkOrderEstimate woe where woe.workOrder.parent is null " +
					" and woe.workOrder.egwStatus.code = ?  and UPPER(woe.workOrder.packageNumber) like '%'||?||'%' order by woe.workOrder.packageNumber";			
			params.add(WorksConstants.APPROVED.toString());
			params.add(query.toUpperCase());
			
			wpNumberSearchList = persistenceService.findAllBy(strquery,params.toArray());
		}
		return WP_NUMBER_SEARCH_RESULTS;
	}
	
	/*
	 * Autocomplete for TNs where WO is approved
	 */
	public String searchTNNumber(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		if(!StringUtils.isEmpty(query)) {
			strquery="select distinct(woe.workOrder.negotiationNumber) from WorkOrderEstimate woe where woe.workOrder.parent is null " +
					" and woe.workOrder.egwStatus.code = ?  and UPPER(woe.workOrder.negotiationNumber) like '%'||?||'%' order by woe.workOrder.negotiationNumber";			
			params.add(WorksConstants.APPROVED.toString());
			params.add(query.toUpperCase());
			
			tenderNegotiationNumberSearchList = persistenceService.findAllBy(strquery,params.toArray());
		}
		return TN_NUMBER_SEARCH_RESULTS;
	}
	
	/*
	 * Autocomplete for Approved WOs
	 */
	public String searchWorkOrderNumber(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		if(!StringUtils.isEmpty(query)) {
			strquery="select distinct(woe.workOrder.workOrderNumber) from WorkOrderEstimate woe where woe.workOrder.parent is null " +
					" and woe.workOrder.egwStatus.code = ?  and UPPER(woe.workOrder.workOrderNumber) like '%'||?||'%' order by woe.workOrder.workOrderNumber";			
			params.add(WorksConstants.APPROVED.toString());
			params.add(query.toUpperCase());
			
			workOrderNumberSearchList = persistenceService.findAllBy(strquery,params.toArray());
		}
		return WORKORDER_NUMBER_SEARCH_RESULTS;
	}
	
	public String searchDrawingOfficer() {
		try {
			drawingOfficerList = contractorAdvanceService.getDrawingOfficerListForARF(query, advanceRequisitionDate);
		} catch (Exception e) {
			LOGGER.error("Error in method searchDrawingOfficer:::"+e.getMessage());
		}
		return DRAWINGOFFICER_SEARCH_RESULTS;
	 }
	
	/*
	 * Autocomplete for distinct estimates from ARF
	 */
	public String searchEstimateNumberFromARF(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		if(!StringUtils.isEmpty(query)) {
			strquery="select distinct(arf.workOrderEstimate.estimate.estimateNumber) from ContractorAdvanceRequisition arf where arf.status.code <> ? and " +
					"UPPER(arf.workOrderEstimate.estimate.estimateNumber) like '%'||?||'%'  order by arf.workOrderEstimate.estimate.estimateNumber";
			params.add(ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.NEW.toString());
			params.add(query.toUpperCase());
			
			estimateNumberSearchList = persistenceService.findAllBy(strquery,params.toArray());
		}
		return ESTIMATE_NUMBER_SEARCH_RESULTS;
	}
	
	/*
	 * Autocomplete for Approved WOs
	 */
	public String searchWorkOrderNumberFromARF(){
		String strquery="";
		ArrayList<Object> params=new ArrayList<Object>();
		if(!StringUtils.isEmpty(query)) {
			strquery="select distinct(arf.workOrderEstimate.workOrder.workOrderNumber) from ContractorAdvanceRequisition arf where arf.status.code <> ? and " +
					"UPPER(arf.workOrderEstimate.workOrder.workOrderNumber) like '%'||?||'%'  order by arf.workOrderEstimate.workOrder.workOrderNumber";
			params.add(ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.NEW.toString());
			params.add(query.toUpperCase());
			
			workOrderNumberSearchList = persistenceService.findAllBy(strquery,params.toArray());
		}
		return WORKORDER_NUMBER_SEARCH_RESULTS;
	}
		
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<String> getEstimateNumberSearchList() {
		return estimateNumberSearchList;
	}

	public List<String> getWorkOrderNumberSearchList() {
		return workOrderNumberSearchList;
	}

	public List<String> getWpNumberSearchList() {
		return wpNumberSearchList;
	}

	public List<String> getTenderNegotiationNumberSearchList() {
		return tenderNegotiationNumberSearchList;
	}

	public List<HashMap> getDrawingOfficerList() {
		return drawingOfficerList;
	}

	public void setDrawingOfficerList(List<HashMap> drawingOfficerList) {
		this.drawingOfficerList = drawingOfficerList;
	}

	public ContractorAdvanceService getContractorAdvanceService() {
		return contractorAdvanceService;
	}

	public void setContractorAdvanceService(
			ContractorAdvanceService contractorAdvanceService) {
		this.contractorAdvanceService = contractorAdvanceService;
	}

	public Date getAdvanceRequisitionDate() {
		return advanceRequisitionDate;
	}

	public void setAdvanceRequisitionDate(Date advanceRequisitionDate) {
		this.advanceRequisitionDate = advanceRequisitionDate;
	}

}