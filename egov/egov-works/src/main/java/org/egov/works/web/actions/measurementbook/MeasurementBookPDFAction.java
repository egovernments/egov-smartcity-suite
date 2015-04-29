package org.egov.works.web.actions.measurementbook;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.commons.EgwStatus;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.Activity;
import org.egov.works.models.measurementbook.ApprovalDetails;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.measurementbook.MeasurementBookPDF;
import org.egov.works.models.revisionEstimate.RevisionType;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;


@Result(name=BaseFormAction.SUCCESS,type="StreamResult.class",location="measurementBookPDF", params={"inputName","measurementBookPDF","contentType","application/pdf","contentDisposition","no-cache;filename=MeasurementBook.pdf"})
@ParentPackage("egov")
public class MeasurementBookPDFAction extends BaseFormAction{
	private static final Logger logger = Logger.getLogger(MeasurementBookPDFAction.class);
	
	private Long measurementBookId;
	private InputStream measurementBookPDF;
	private MeasurementBookService measurementBookService;
	private WorkOrderService workOrderService;
	@Autowired
        private EmployeeService employeeService;
	private ReportService reportService;
	
	public String execute(){
		logger.info("----inside action excute begin");
		if(measurementBookId!=null){
			logger.info("----inside action excute when measurementBookId is not null "+measurementBookId);
			MBHeader mbHeader=getMBHeader();			
			ReportRequest reportRequest = null;
			if(areNTOrLSItemsPresent(mbHeader))
				reportRequest = new ReportRequest("mbWithRevisionType",createMbData(mbHeader), getParamMap(mbHeader));
			else
				reportRequest = new ReportRequest("measurementBook",createMbData(mbHeader), getParamMap(mbHeader));
			ReportOutput reportOutput = reportService.createReport(reportRequest);
			if (reportOutput != null && reportOutput.getReportOutputData() != null)
				measurementBookPDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
		}	
		return SUCCESS;
	}
	
	private Map<String, Object> getParamMap(MBHeader mbHeader) {
		Map<String, Object> reportParams = new HashMap<String, Object>();
		reportParams.put("mbNumber", mbHeader.getMbRefNo());
		reportParams.put("pageNumber", mbHeader.getFromPageNo()+(mbHeader.getToPageNo()==null?"":" to "+mbHeader.getToPageNo()));
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		reportParams.put("mbDate",dateFormatter.format(mbHeader.getMbDate()) );
		reportParams.put("reportTitle", getText("page.title.measurement.book"));
		reportParams.put("approvalDetails", createApprovalDetailsTable(mbHeader));
		reportParams.put("preparedBy", mbHeader.getMbPreparedBy().getEmployeeName());
		return reportParams;
	}

	private List<Object>  createMbData(MBHeader mbHeader){
		double uomFactor=0.0;
		List<Object> mbPDFList = new ArrayList<Object>();
		for(MBDetails mbDetails : mbHeader.getMbDetails()){
			MeasurementBookPDF mbPDF = new MeasurementBookPDF();
			String description="";
			String per="";
			String schNo="";
			double currentMeasurement=0.0;
			currentMeasurement=mbDetails.getQuantity();
			WorkOrderActivity workOrderActivity=mbDetails.getWorkOrderActivity();
			Activity activity=workOrderActivity.getActivity();
			if(activity!=null){
				if(activity.getSchedule()!=null	&& activity.getSchedule().getCode()!=null)
					schNo=activity.getSchedule().getCode();
				mbPDF.setScheduleNo(schNo);

				//start  sor/non sor description
				if(activity.getSchedule()!=null && activity.getSchedule().getDescription()!=null)
					description=activity.getSchedule().getDescription();

				if(activity.getNonSor()!=null && activity.getNonSor().getDescription()!=null)
					description=activity.getNonSor().getDescription();
				mbPDF.setWorkDescription(description);
				if(activity.getRevisionType()!=null && activity.getRevisionType().toString().equalsIgnoreCase(RevisionType.NON_TENDERED_ITEM.toString()))
					mbPDF.setRevisionType("Non Tendered");
				if(activity.getRevisionType()!=null && activity.getRevisionType().toString().equalsIgnoreCase(RevisionType.LUMP_SUM_ITEM.toString()))
					mbPDF.setRevisionType("Lump Sum");
			}
			
			//for completedMeasurement area --------------->Cumulative quantity including current entry= Cumulative upto previous entry + Current MB entry
			//( cumulative MB  measurement  for line item) for selected MB including  MB entry
			
			double completedMeasurement=0.0;
			double cumlPrevMb=0.0;
			try{
				long woaId=0l;
				if(workOrderActivity.getId()!=null)
					woaId=workOrderActivity.getId();
				
				cumlPrevMb=measurementBookService.prevCumulativeQuantityIncludingCQ(woaId,mbHeader.getId(),workOrderActivity.getActivity().getId(),mbHeader.getWorkOrder());
			}catch(Exception e){					
				cumlPrevMb=0.0;
			}			
			completedMeasurement=cumlPrevMb + currentMeasurement;
			mbPDF.setCompletedMeasurement(completedMeasurement);
			
			double approveRateWo=0.0;
			approveRateWo=workOrderActivity.getApprovedRate();
			mbPDF.setUnitRate(approveRateWo);
			
			//start unit
			if(activity!=null){
			//  umofactor for conversion of rate and amount
				uomFactor =workOrderActivity.getConversionFactor();
				logger.debug("----------uomFactor------------"+uomFactor);
					
				if(activity.getSchedule()!=null && activity.getSchedule().getUom()!=null && activity.getSchedule().getUom().getUom()!=null)
					per=activity.getSchedule().getUom().getUom();
				if(activity.getNonSor()!=null && activity.getNonSor().getUom()!=null && activity.getNonSor().getUom().getUom()!=null)
					per=activity.getNonSor().getUom().getUom();
				mbPDF.setUom(per);
			//end start unit
			} //end of if activity


			/*
			 measurementBookService.prevCumulativeAmount(workOrderActivity.getId());
			total work completed------->(completed mesurement(col 5) * rate) here rate is wo.getAprovedrate
			added uom factor on april4th 2010 
			*/
			double workCompleted=completedMeasurement*approveRateWo * uomFactor;
			mbPDF.setCompletedCost(workCompleted);
			
			//previous measurements a)pageno and b)measurements
			//a)Page no: for last  MB entry for  forline item---->page-no call api
			Integer frompageNo = null;
			Integer topageNo = null;

			MBHeader resultHeader = workOrderService.findLastMBPageNoForLineItem(workOrderActivity,mbHeader.getId());
			if(resultHeader != null) {
				frompageNo 	= resultHeader.getFromPageNo();
				topageNo 	= resultHeader.getToPageNo();
			}

			String pageNoInfo = "";
			if(frompageNo != null)
				pageNoInfo = resultHeader.getMbRefNo() + "/" +frompageNo.toString();
			if(topageNo != null)
				pageNoInfo = pageNoInfo +"-" + topageNo;
			mbPDF.setPageNo(pageNoInfo);
			
			//b)Cumulative measurement recorded for the previous MB entry for line item( Cumulative measurements-current MB entry)
			mbPDF.setPrevMeasurement(cumlPrevMb);

			//Current Finalised Measurements  a)Current MB entry  and b) Column6 Estimate Percentage
			//a)Current MB entry---->Measurements (Col5-8) i.e (area-previous measurement)
			//double finalCurMeasurement=area-prevMeasurement;
			mbPDF.setCurrentMeasurement( currentMeasurement);

			//current cost
			double currentCost=0.0;
			currentCost=currentMeasurement*approveRateWo * uomFactor;
			mbPDF.setCurrentCost(currentCost);
			mbPDFList.add(mbPDF);
		}
		return mbPDFList;
	}
	// Are nontendered or lumpsum items present
	private boolean areNTOrLSItemsPresent(MBHeader mbHeader)
	{
		if(mbHeader!=null && mbHeader.getMbDetails()!=null && mbHeader.getMbDetails().size()>0)
		{
			for(MBDetails mbdetails : mbHeader.getMbDetails())
			{
				if(mbdetails.getWorkOrderActivity()!=null 
						&& mbdetails.getWorkOrderActivity().getActivity()!=null 
						&& mbdetails.getWorkOrderActivity().getActivity().getRevisionType()!=null)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private List<ApprovalDetails> createApprovalDetailsTable(MBHeader mbHeader) {
		try {
			List<StateHistory> history =null;		
			String code="";
			List<ApprovalDetails> approvalDetList = new ArrayList<ApprovalDetails>();
			if(mbHeader.getCurrentState()!=null && mbHeader.getCurrentState().getHistory()!=null)
				history=mbHeader.getCurrentState().getHistory();
			if(history!=null){
				Collections.reverse(history);
				for (StateHistory state : history) {
					if(!state.getValue().equals("NEW") && !state.getValue().equals("END")){
						ApprovalDetails approvalDet = new ApprovalDetails();
						String nextAction="";
						if(state.getNextAction()!=null)
							nextAction=state.getNextAction();
						Long positionId =null;
						String desgName=null;
						DeptDesig deptdesig= null;
						//if(state.getPrevious()==null){
							positionId = state.getOwnerPosition().getId();
							deptdesig= state.getOwnerPosition().getDeptDesigId();
							desgName = deptdesig.getDesigId().getDesignationName();
						//}
						/*else{
							positionId =state.getPrevious().getOwner().getId();
							deptdesig= state.getPrevious().getOwner().getDeptDesigId();
							desgName = deptdesig.getDesigId().getDesignationName();
						}*/
						PersonalInformation emp=employeeService.getEmpForPositionAndDate(state.getCreatedDate(), Integer.parseInt(positionId.toString()));
						//if(state.getValue().equals("END"))
							//code = state.getPrevious().getValue();
						//else
							code = state.getValue();
						EgwStatus status =(EgwStatus) getPersistenceService().find("from EgwStatus where moduletype=? and code=?","MBHeader",code);
						String statusDesc=status.getDescription();
						if(!nextAction.equalsIgnoreCase(""))
							statusDesc=status.getDescription()+" - "+nextAction;
						approvalDet.setStatusDesc(statusDesc);
						approvalDet.setEmplName(emp.getEmployeeName());
						approvalDet.setDesgName(desgName);
						approvalDet.setDate(state.getCreatedDate());
						approvalDet.setText(state.getComments());
						approvalDetList.add(approvalDet);
					}
				}
			}
			return approvalDetList;
		}catch (Exception e) {
			return null;
		}
	}
	
	public MBHeader getMBHeader(){		
		return measurementBookService.findById(measurementBookId, false);
	}
	
	public InputStream getMeasurementBookPDF() {
		return measurementBookPDF;
	}

	@Override
	public Object getModel() {
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
	
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
}