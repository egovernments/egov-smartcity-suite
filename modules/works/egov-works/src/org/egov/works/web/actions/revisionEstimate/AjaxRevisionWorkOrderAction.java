package org.egov.works.web.actions.revisionEstimate;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.revisionEstimate.RevisionWorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.services.MeasurementBookService;

public class AjaxRevisionWorkOrderAction extends BaseFormAction{
	private static final Logger logger = Logger.getLogger(AjaxRevisionWorkOrderAction.class);
	
	private Long workOrderId;
	private PersistenceService<RevisionWorkOrder,Long> revisionWorkOrderService;
	List <MBHeader> approvedMBList = new ArrayList<MBHeader>(); 
	private MeasurementBookService measurementBookService;
	private static final String CANCEL_REVISIONWORKORDER = "cancelRWO";
	
	public Object getModel() {
		return null;
	}
	
	public String getMBDetails() throws Exception{
		RevisionWorkOrder reWO=revisionWorkOrderService.find("from RevisionWorkOrder where id=?",workOrderId);
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
		return CANCEL_REVISIONWORKORDER; 
	}

	public Long getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(Long workOrderId) {
		this.workOrderId = workOrderId;
	}

	public void setRevisionWorkOrderService(
			PersistenceService<RevisionWorkOrder, Long> revisionWorkOrderService) {
		this.revisionWorkOrderService = revisionWorkOrderService;
	}

	public List<MBHeader> getApprovedMBList() {
		return approvedMBList;
	}

	public void setApprovedMBList(List<MBHeader> approvedMBList) {
		this.approvedMBList = approvedMBList;
	}

	public void setMeasurementBookService(
			MeasurementBookService measurementBookService) {
		this.measurementBookService = measurementBookService;
	}
	
}
