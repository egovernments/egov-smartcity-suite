package org.egov.works.web.actions.revisionEstimate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.revisionEstimate.RevisionWorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.MeasurementBookService;

public class AjaxRevisionEstimateAction extends BaseFormAction{
	private static final Logger logger = Logger.getLogger(AjaxRevisionEstimateAction.class);
	private static final String ACTIVITY_DETAILS = "activityDetails";
	
	private MeasurementBookService measurementBookService;
	private Long woActivityId;
	private Long revEstId;
	private WorkOrderActivity workOrderActivity;
	private Double prevCulmEntry;
	private Double totalEstQuantity;
	private PersistenceService<RevisionWorkOrder,Long> revisionWorkOrderService;
	private List<WorkOrderActivity> woActivityList =  new LinkedList<WorkOrderActivity>();
	private static final String CANCEL_REVISIONESTIMATE = "cancelRE";
	List <MBHeader> approvedMBList = new ArrayList<MBHeader>(); 
	List <RevisionWorkOrder> approvedWOList = new ArrayList<RevisionWorkOrder>(); 
	private Long workOrderId;
	private String revisionWO;
	//-------------------------------------------------------------------
	
	public Object getModel() {
		return null;
	}
	
	public String activityDetails() {
		prevCulmEntry = null;
		try{
			workOrderActivity = (WorkOrderActivity) persistenceService.find("from WorkOrderActivity where id=?",woActivityId);	
			if(workOrderActivity.getActivity().getParent()==null) {
				prevCulmEntry = measurementBookService.prevCumulativeQuantity(woActivityId,null,workOrderActivity.getActivity().getId());
				totalEstQuantity=measurementBookService.totalEstimatedQuantityForRE(woActivityId,null,workOrderActivity.getActivity().getId());
			}
			else {				
				prevCulmEntry = measurementBookService.prevCumulativeQuantity(woActivityId,null,workOrderActivity.getActivity().getParent().getId());
				totalEstQuantity=measurementBookService.totalEstimatedQuantityForRE(woActivityId,null,workOrderActivity.getActivity().getParent().getId());				
			}
			if(totalEstQuantity==0)
				totalEstQuantity=workOrderActivity.getApprovedQuantity();
		} 
		catch (Exception e) {
			throw new EGOVRuntimeException("activity.find.error", e);
		}
		
		return ACTIVITY_DETAILS;
	}
	
	public String getWOandMBDetails() throws Exception{
		RevisionWorkOrder reWO=revisionWorkOrderService.find("from RevisionWorkOrder where id=?",workOrderId);
		if(null != reWO.getCreationType() && !reWO.getEgwStatus().getCode().equals("CANCELLED")){
			revisionWO=reWO.getWorkOrderNumber();
		}
		else if(reWO.getCreationType()==null  && reWO.getEgwStatus().getCode().equals("APPROVED")) {
			List<MBHeader> mbheaderlist = new ArrayList<MBHeader>();
			int flag=0;
			mbheaderlist = measurementBookService.findAllBy(" from MBHeader where workOrder.id=? and state.previous.value<>'CANCELLED'", reWO.getParent().getId());
			for(MBHeader mbh: mbheaderlist){
				flag=0;
				for(MBDetails mbd :mbh.getMbDetails()){
					for(WorkOrderActivity woa : reWO.getWorkOrderEstimates().get(0).getWorkOrderActivities()){
						if(mbd.getWorkOrderActivity().getActivity().getId().equals(woa.getActivity().getId())){
							flag=1;
						}
					}
				}
				if(flag==1)
					approvedMBList.add(mbh);
			}
		}
		return CANCEL_REVISIONESTIMATE; 
	}
	
	public void setWoActivityId(Long woActivityId) {
		this.woActivityId = woActivityId;
	}

	public WorkOrderActivity getWorkOrderActivity() {
		return workOrderActivity;
	}

	public Double getPrevCulmEntry() {
		return prevCulmEntry;
	}
	
	public void setRevEstId(Long revEstId) {
		this.revEstId = revEstId;
	}

	public Double getTotalEstQuantity() {
		return totalEstQuantity;
	}

	public void setTotalEstQuantity(Double totalEstQuantity) {
		this.totalEstQuantity = totalEstQuantity;
	}

	public void setMeasurementBookService(
			MeasurementBookService measurementBookService) {
		this.measurementBookService = measurementBookService;
	}

	public void setRevisionWorkOrderService(
			PersistenceService<RevisionWorkOrder, Long> revisionWorkOrderService) {
		this.revisionWorkOrderService = revisionWorkOrderService;
	}

	public List<WorkOrderActivity> getWoActivityList() {
		return woActivityList; 
	}

	public void setWoActivityList(List<WorkOrderActivity> woActivityList) {
		this.woActivityList = woActivityList;
	}

	public Long getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(Long workOrderId) {
		this.workOrderId = workOrderId;
	}

	public List<MBHeader> getApprovedMBList() {
		return approvedMBList;
	}

	public void setApprovedMBList(List<MBHeader> approvedMBList) {
		this.approvedMBList = approvedMBList;
	}

	public List<RevisionWorkOrder> getApprovedWOList() {
		return approvedWOList;
	}

	public void setApprovedWOList(List<RevisionWorkOrder> approvedWOList) {
		this.approvedWOList = approvedWOList;
	}

	public String getRevisionWO() {
		return revisionWO;
	}

	public void setRevisionWO(String revisionWO) {
		this.revisionWO = revisionWO;
	}
}  