package org.egov.works.web.actions.report;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.WorkOrderService;
 
public class AjaxMBReportAction extends BaseFormAction{
	
	private static final String SEARCH_ESTIMATENUMBER="searchEstimateNumber";
	private static final String SEARCH_WONUMBER="searchWONumber";
	private String query;
	private WorkOrderService workOrderService;
	private static final Logger LOGGER = Logger.getLogger(AjaxMBReportAction.class);

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String searchEstimateNumberAjax(){
		return SEARCH_ESTIMATENUMBER;
	}
	
	public List<String> getEstimateList() {  
		return persistenceService.findAllBy("select mb.workOrderEstimate.estimate.estimateNumber from MBHeader mb where " +
				"mb.workOrderEstimate.estimate.parent.id is null and mb.egwStatus.code not in(?,?,?) " + 
				"and upper(mb.workOrderEstimate.estimate.estimateNumber) like '%' || ? || '%' order by mb.workOrderEstimate.estimate.estimateNumber", MBHeader.MeasurementBookStatus.NEW.toString(), 
				MBHeader.MeasurementBookStatus.CANCELLED.toString(), MBHeader.MeasurementBookStatus.REJECTED.toString(), query.toUpperCase());
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	public String searchWONumberAjax(){
		return SEARCH_WONUMBER;
	}
	
	public Collection<WorkOrder> getWorkOrderList() {  
		return workOrderService.findAllBy("select distinct mb.workOrder from MBHeader mb where mb.workOrder.parent.id is null " +
				"and mb.egwStatus.code not in(?,?,?) and upper(mb.workOrder.workOrderNumber) like '%' || ? || '%' order by mb.workOrder.workOrderNumber", 
				MBHeader.MeasurementBookStatus.NEW.toString(), MBHeader.MeasurementBookStatus.CANCELLED.toString(),
				MBHeader.MeasurementBookStatus.REJECTED.toString(), query.toUpperCase());
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}
}