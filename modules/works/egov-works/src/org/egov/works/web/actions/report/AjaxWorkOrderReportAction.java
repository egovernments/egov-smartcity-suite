package org.egov.works.web.actions.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infstr.services.PersistenceService;
import org.egov.tender.model.TenderNotice;
import org.egov.tender.services.tendernotice.TenderNoticeService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.WorkOrderService;


@ParentPackage("egov")
@SuppressWarnings("serial")
public class AjaxWorkOrderReportAction extends BaseFormAction {

	private WorkOrderService workOrderService;
	private TenderNoticeService tenderNoticeService;
	private static final Logger LOGGER = Logger.getLogger(AjaxWorkOrderReportAction.class);
	private String query;
	
	private static final String SEARCH_WO="searchWorkOrder";
	private static final String SEARCH_TENDER_NOTICE="searchTenderNotice";
	private static final String SEARCH_TENDER_FILE="searchTenderFile";
	
	@Override
	public Object getModel() {
		return null;
	}
	
	public String searchWOAjax(){
		return SEARCH_WO;
	}
	
	public String searchTenderNoticeAjax() {
		return SEARCH_TENDER_NOTICE; 
	}
	
	public String searchTenderFileAjax() {
		return SEARCH_TENDER_FILE;
	}
	
	public Collection<WorkOrder> getWorkOrderList()  
	{	
		return workOrderService.findAllBy("from WorkOrder where egwStatus.code!='NEW' and upper(workOrderNumber) like '%' || ? || '%'", query.toUpperCase());
	}  
	
	public Collection<TenderNotice> getTenderFileList()
	{	
		List x = persistenceService.findAllBy("from TenderNotice tn, WorkOrder wo where tn.number=wo.tenderNumber and upper(tn.tenderFileRefNumber) like '%' || ? || '%'", query.toUpperCase());
		List<TenderNotice> noticeList = new ArrayList<TenderNotice>() ;
		Object[] objArr;
		for(Object ob : x )
		{
			objArr = (Object[]) ob;
			noticeList.add( (TenderNotice) objArr[0]);
		}
		return noticeList;
	}
	
	public Collection<WorkOrder> getTenderNoticeList()  
	{	
		return workOrderService.findAllBy("from WorkOrder where upper(tenderNumber) like '%' || ? || '%'", query.toUpperCase());
	} 
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	public void setTenderNoticeService(TenderNoticeService tenderNoticeService) {
		this.tenderNoticeService = tenderNoticeService;
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}
} 
