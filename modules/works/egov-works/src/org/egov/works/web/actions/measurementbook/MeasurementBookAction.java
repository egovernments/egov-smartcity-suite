package org.egov.works.web.actions.measurementbook;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Money;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.tender.BidType;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.services.common.GenericTenderService;
import org.egov.web.actions.workflow.GenericWorkFlowAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.Activity;
import org.egov.works.models.estimate.MeasurementSheet;
import org.egov.works.models.estimate.NonSor;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.masters.ScheduleOfRate;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.measurementbook.MBMeasurementSheet;
import org.egov.works.models.revisionEstimate.RevisionAbstractEstimate;
import org.egov.works.models.revisionEstimate.RevisionType;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.models.workorder.WorkOrderMeasurementSheet;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.MeasurementBookWFService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.services.impl.MeasurementBookServiceImpl;
import org.egov.works.services.impl.WorkOrderServiceImpl;
import org.egov.works.utils.DateConversionUtil;
import org.egov.works.utils.WorksConstants;


//@Result(name=Action.SUCCESS, type=ServletRedirectResult.class, value = "measurementBook.action")  
@ParentPackage("egov")
@Result(name=MeasurementBookAction.PRINT,type="stream",location="PdfInputStream", params={"inputName","PdfInputStream","contentType","application/pdf","contentDisposition","no-cache"})
public class MeasurementBookAction extends GenericWorkFlowAction{
	
	private static final Logger logger = Logger.getLogger(MeasurementBookAction.class);
	
	private InputStream pdfInputStream;   
	private ReportService reportService;    
	public static final String PRINT = "print"; 
	private String additionalRuleValue;
	public String getAdditionalRuleValue() {
		return getAdditionalRule();
	}

	private static final String PUBLIC_WORKS_DEPARTMENT="Public Work";
	//private static final String CANCEL_ACTION = "cancel";
	// private static final String SAVE_ACTION = "save";
	private static final String VERIFY = "verify";
	private static final String SUBMITTED = "submitted";
	private static final String ACTIVITY_SEARCH = "activitySearch";
	private static final String MB_SEARCH = "mbSearch";
	private static final String DATEFORMAT="dd-MMM-yyyy";
	
	private Map<String,Object> criteriaMap=null;
	private MBHeader mbHeader = new MBHeader();
	private List<MBDetails> mbDetails = new LinkedList<MBDetails>();
	private String messageKey;
	private Long id;
	private EmployeeService employeeService;
	private EmployeeView mbPreparedByView;
	private MeasurementBookService measurementBookService;
   	private CommonsService commonsService;
	private WorksService worksService;
	private WorkOrderService workOrderService;
	private List<MBDetails> actionMbDetailValues = new LinkedList<MBDetails>();
	private double quantityFactor;
    private List<WorkOrderActivity> activityList; // for search page
    private List<MBHeader> mbList; // for search page
    private List<WorkOrderEstimate> workOrderEstimateList= new ArrayList<WorkOrderEstimate>();
    private String 	workorderNo;
    private Long workOrderId;
    private String mode;
    private String mborderNumberRequired;
    private String workName;
    private String projectCode;
    //-----------------------Activity Search ----------------------------------
	private String 	activityCode;
	private String  activityDesc;
	//-------------------------------------------------------------------
	
	//-----------------------MB Search ----------------------------------
	private Long contractorId;
	private Date  fromDate;
	private Date  toDate;
	private String mbRefNo;
	private String mbPages;
	private String mbStatus;
	//-------------------------------------------------------------------
	
	//-----------------------Manual Workflow ----------------------------
	private Integer departmentId; 
	private Integer designationId; 
	private String approverComments;
	private Integer approverUserId;
	//-------------------------------------------------------------------
	
	// -------------- on for workflow service
	private MeasurementBookWFService measurementBookWFService;
	private WorkflowService<MBHeader> workflowService;	
	//private static final String SOURCE_SEARCH = "search";
	//private static final String SOURCE_INBOX = "inbox";	
	private static final String SAVE_ACTION = "save";
	//private static final Object REJECT_ACTION = "reject";	
	private final static String APPROVED = "APPROVED";
	
	private String sourcepage="";
	private String dispEmployeeName;
	private String dispDesignation;
	private Long estimateId;
	private PersonalInformationService personalInformationService;
	
	private static final String ACTION_NAME="actionName";
	
	public List<MBMeasurementSheet> mbMeasurementSheetList = new LinkedList<MBMeasurementSheet>();
	private String woActivityId;
	private String mbDtlsRecId;
	private AbstractEstimateService abstractEstimateService;
	private EisUtilService eisService;
	private String departmentName;
	private boolean isSpillOverWorks;
	
	private List<Activity> sorActivities = new LinkedList<Activity>();
	private List<Activity> nonSorActivities = new LinkedList<Activity>();
	private PersistenceService<NonSor,Long> nonSorService;
	private PersistenceService<RevisionAbstractEstimate,Long> revisionAbstractEstimateService;
	private String activitySearchType;
	
	private List<WorkOrderActivity> extraItemsActivities = new LinkedList<WorkOrderActivity>();
	public List<MeasurementSheet> measurementSheetList = new LinkedList<MeasurementSheet>();
	private String activityUOM;
	private String recordId;
	private Double totalMBExtraMSheetQty;
	private List<WorkOrderMeasurementSheet> changeQuantityMSheetList = new LinkedList<WorkOrderMeasurementSheet>();
	private String mbPercentagelevel;
	private boolean isTenderPercentageType;
	private GenericTenderService genericTenderService;
	private BigDecimal tenderPercentage;
	private String mbAmtAboveApprovedAmt;
	private String sign;
	
	private boolean isZeroQty=false;
	
	private Long mbId;
	private String estimateNo;
	private String cancelRemarks;
	private String preparedByTF = "";
	
    public MeasurementBookAction() {
		addRelatedEntity("workOrder", WorkOrder.class);
	}
    
    @Override
	public void prepare() {
		AjaxMeasurementBookAction ajaxMBAction = new AjaxMeasurementBookAction();
		ajaxMBAction.setPersistenceService(getPersistenceService());
		ajaxMBAction.setEmployeeService(employeeService);
		ajaxMBAction.setPersonalInformationService(personalInformationService);
		ajaxMBAction.setEisService(eisService);
		ajaxMBAction.setAbstractEstimateService(abstractEstimateService);
		mbPercentagelevel=worksService.getWorksConfigValue(WorksConstants.MBPERCENTAPPCONFIGKEY);
		mbAmtAboveApprovedAmt=worksService.getWorksConfigValue(WorksConstants.MBAMTABOVEAPPROVEDAMT);
		if (id != null ) {
			mbHeader= measurementBookService.findById(id, false); 
			if(mbHeader!=null && mbHeader.getMbPreparedBy()!=null){
				 mbPreparedByView = (EmployeeView) getPersistenceService().find("from EmployeeView where id = ? and deptId.id=?", mbHeader.getMbPreparedBy().getIdPersonalInformation(),mbHeader.getWorkOrderEstimate().getEstimate().getExecutingDepartment().getId());
				 workOrderEstimateList.add(mbHeader.getWorkOrderEstimate());
			}
		}else if(workOrderId!=null){
		workOrderEstimateList.addAll(getPersistenceService().
						findAllByNamedQuery("getWorkOrderEstimateByWorkOrderId", workOrderId));
		
			mbHeader.setWorkOrder(workOrderService.findById(workOrderId, false));
		departmentName=workOrderEstimateList.get(0).getEstimate().getExecutingDepartment().getDeptName();
		}
		GenericTenderResponse tenderResponse=null;
		if(mbHeader!=null && mbHeader.getWorkOrder()!=null){
		 tenderResponse=genericTenderService.getGenericResponseByNumber(mbHeader.getWorkOrder().getNegotiationNumber());
		}
		if(tenderResponse!=null && tenderResponse.getBidType().equals(BidType.PERCENTAGE)){
			isTenderPercentageType=true;
			tenderPercentage=tenderResponse.getPercentage();
		}
		else{
			isTenderPercentageType=false;
			tenderPercentage=BigDecimal.ZERO;
		}
		if(tenderPercentage.compareTo(BigDecimal.ZERO)>=0){
			sign="+";
		}
		else{
			sign="-";
		}
		if(workOrderEstimateList.isEmpty())
			addDropdownData("workOrderEstimateList", Collections.EMPTY_LIST);
		else
			addDropdownData("workOrderEstimateList",measurementBookService.getWorkOrderEstimatesForMB(workOrderEstimateList));
			
		super.prepare();
		setupDropdownDataExcluding("workOrder");
		//populatePreparedByList(ajaxMBAction, mbHeader.getWorkOrder() != null);
		
		if((id==null) || (mbHeader.getCurrentState()!=null && (mbHeader.getCurrentState().getValue().equals("NEW") || mbHeader.getCurrentState().getValue().equals("REJECTED"))))
            populatePreparedByList(ajaxMBAction, mbHeader.getWorkOrder() != null);
	    else if(id != null) {
	            HashMap<String,Object> criteriaParams =new HashMap<String,Object>();
	            criteriaParams.put("code",mbHeader.getMbPreparedBy().getEmployeeCode());
	            criteriaParams.put("departmentId",mbHeader.getWorkOrderEstimate().getEstimate().getExecutingDepartment().getId().toString());
	            List<EmployeeView> employeeViewList =  eisService.getEmployeeInfoList(criteriaParams);
	            addDropdownData("preparedByList",employeeViewList );
	            if(employeeViewList==null||employeeViewList.isEmpty())
	            {
	            	preparedByTF = mbHeader.getMbPreparedBy().getEmployeeName();
	            }
	            else
	            {
	            	preparedByTF = employeeViewList.get(0).getEmployeeName() + "-" + employeeViewList.get(0).getDesigId().getDesignationName();
	            }
	    }
		setMBPreparedBy(getIdPersonalInformationFromParams());
		addDropdownData("executingDepartmentList", getPersistenceService().findAllBy("from DepartmentImpl"));
		if(getLatestAssignmentForCurrentLoginUser()!=null) {
			departmentId = getLatestAssignmentForCurrentLoginUser().getDeptId().getId();
		}
		populateQuantityFactor();
		addDropdownData("scheduleCategoryList", getPersistenceService().findAllBy("from ScheduleCategory order by upper(code)"));
		addDropdownData("uomList", getPersistenceService().findAllBy("from EgUom  order by upper(uom)"));
	
		
		if(!getWorkFlowDepartment().equals("")){
			departmentName=getWorkFlowDepartment();
		}
		if(mbHeader.getWorkOrderEstimate()!=null)
		getAdditionalRule();
		if(workOrderEstimateList.size()!=0){
			if(workOrderEstimateList.get(0).getEstimate().getIsSpillOverWorks()){
				isSpillOverWorks=true;
			}else{
				isSpillOverWorks=false;
    }
		}
		if(("inbox".equals(getSourcepage()) || "search".equals(getSourcepage()) || id!=null) && measurementSheetList.isEmpty() && changeQuantityMSheetList.isEmpty() && mbHeader.getRevisionEstimate()!=null){ 
			List<Activity> activityList =null;
			Iterator iterator=null;
			AbstractEstimate ae=null;
			List<MeasurementSheet> mSheetList =null;
			List<MeasurementSheet> mSheetList2 =null;
			Iterator mSheetIterator=null;
			Iterator mSheetIterator2=null;
			ae=mbHeader.getRevisionEstimate();
			if(!ae.getActivities().isEmpty() && ae.getActivities().size()!=0){
				activityList =ae.getActivities();
				iterator=activityList.iterator();
				Activity activity;
				MeasurementSheet ms=null;
				int count=1;
	            while(iterator.hasNext()){
	                    activity=(Activity) iterator.next();
	                    if(activity.getNonSor()!=null)
	                            activity.setSrlNo(count++);
	                    mSheetList=activity.getMeasurementSheetList();
	                 

	                    if(activity.getRevisionType()!=null && (!activity.getRevisionType().equals(RevisionType.EXTRA_ITEM)) && !activity.getParent().getMeasurementSheetList().isEmpty()){
	                    	mSheetList2=activity.getParent().getMeasurementSheetList();
	                    }
	                    mSheetIterator=mSheetList.iterator(); 
	                    if(mSheetList2!=null)
	                    	mSheetIterator2=mSheetList2.iterator();
	                    int mSheetcount=1;
	                    while(mSheetIterator.hasNext() || (mSheetIterator2!=null && mSheetIterator2.hasNext())){
	                    	if(mSheetIterator.hasNext())
	                    	ms=(MeasurementSheet)mSheetIterator.next();
	                    	else if(mSheetIterator2!=null && mSheetIterator2.hasNext())
	                    		ms=(MeasurementSheet)mSheetIterator2.next();
	                    	if(ms!=null) {
	                    	ms.setMbExtraItemSlNo(mSheetcount++);
	                    	WorkOrderMeasurementSheet woMsht=null;
	                    	for(MBDetails mbd:mbHeader.getMbDetails()){
	                    		if(mbd.getWorkOrderActivity().getActivity().getRevisionType()!=null && mbd.getWorkOrderActivity().getActivity().getRevisionType().equals(RevisionType.EXTRA_ITEM)) {
	                    			for(MBMeasurementSheet mbMsht:mbd.getMbMeasurementSheetList()){
	                    				if(ms.getId()==mbMsht.getWoMeasurementSheet().getMeasurementSheet().getId()) {
	                    					ms.setConsumedNo(mbMsht.getNo());
	                    					ms.setConsumedLength(mbMsht.getUomLength());
	                    					ms.setConsumedWidth(mbMsht.getWidth());
	                    					ms.setConsumedDH(mbMsht.getDepthOrHeight());
	                    					ms.setConsumedQuantity(mbMsht.getQuantity());
    }
    
	                    			}
	                    		}
	                    		else if(mbd.getWorkOrderActivity().getActivity().getRevisionType()!=null && (!mbd.getWorkOrderActivity().getActivity().getRevisionType().equals(RevisionType.EXTRA_ITEM))){
	                    			mbd.getWorkOrderActivity().setParent((WorkOrderActivity)persistenceService.find("from WorkOrderActivity where activity.id=? and (workOrderEstimate.id=? or workOrderEstimate.estimate.parent.id=?)", mbd.getWorkOrderActivity().getActivity().getParent().getId(),mbHeader.getWorkOrderEstimate().getId(),mbHeader.getWorkOrderEstimate().getEstimate().getId()));
	                    			for(MBMeasurementSheet mbMsht:mbd.getMbMeasurementSheetList()){
	                    				woMsht=mbMsht.getWoMeasurementSheet();
	      				  				if(ms.getId()==mbMsht.getWoMeasurementSheet().getMeasurementSheet().getId()) { 
	                    					ms.setConsumedNo(mbMsht.getNo());
	                    					ms.setConsumedLength(mbMsht.getUomLength());
	                    					ms.setConsumedWidth(mbMsht.getWidth());
	                    					ms.setConsumedDH(mbMsht.getDepthOrHeight());
	                    					ms.setConsumedQuantity(mbMsht.getQuantity());
	                    	}
	                    			}
	                    	
	                    			if(mbd.getWorkOrderActivity().getParent()!=null) {
	                    				//HibernateUtil.getCurrentSession().evict(mbd.getWorkOrderActivity().getParent().getWoMeasurementSheetList());
	                    				//woMSheetList.addAll(mbd.getWorkOrderActivity().getParent().getWoMeasurementSheetList());
	                    }
	                    			for(WorkOrderMeasurementSheet woMSheet:mbd.getWorkOrderActivity().getWoMeasurementSheetList()){
	                    				if(woMSheet!=null) {
	                    				if(ms.getId()==woMSheet.getMeasurementSheet().getId()) { 
	                    					woMSheet.setMeasurementSheet(ms);
	                    					//HibernateUtil.getCurrentSession().evict(woMSheet);
	                    					woMsht=woMSheet;                 					
	                    				}	
	            }
	                    			}   
	                    			for(WorkOrderMeasurementSheet woMSheet2:mbd.getWorkOrderActivity().getParent().getWoMeasurementSheetList()){
               				
	                    				if(woMSheet2!=null) {
	                    				if(ms.getId()==woMSheet2.getMeasurementSheet().getId()) { 
	                    					woMSheet2.setMeasurementSheet(ms);
	                    					//HibernateUtil.getCurrentSession().evict(woMSheet);
	                    					woMsht=woMSheet2;                 					
	                    				}	
			}
		}	
		}
	                    	}
	                    	if(activity.getRevisionType()!=null && activity.getRevisionType().equals(RevisionType.EXTRA_ITEM)) {
	                    		measurementSheetList.add(ms);	                    		
	                    	}
	                    	else if(activity.getRevisionType()!=null && (!activity.getRevisionType().equals(RevisionType.EXTRA_ITEM))) {
	                    		changeQuantityMSheetList.add(woMsht);	                    		
	                    	}
	                    	
	                    }
	                    }
	            }
			}
			  
		}
		if("cancelMB".equals(sourcepage)) {
			setMbStatus(MBHeader.MeasurementBookStatus.APPROVED.toString());
		}
	}
    public Assignment getLatestAssignmentForCurrentLoginUser() {
		PersonalInformation personalInformation=null;
		if(getCurrentUserId()!=0) {
			personalInformation=employeeService.getEmpForUserId(getCurrentUserId());		    
		}
		Assignment assignment=null;
		if(personalInformation!=null){
			assignment=employeeService.getLatestAssignmentForEmployee(personalInformation.getIdPersonalInformation());			
		}
		return assignment; 
	}
    
    public int getCurrentUserId() {
		return Integer.parseInt(EGOVThreadLocals.getUserId());
	}
	
    protected void populateQuantityFactor(){
    	String configVal = worksService.getWorksConfigValue("MAXEXTRALINEITEMPERCENTAGE");
    	try{
    		quantityFactor = Double.valueOf(configVal);
    	}catch(Exception e){
    		quantityFactor = 0.0d;    		
    	}
    }
    
    protected MBHeader calculateMBdetails(MBHeader mbHeader,boolean isPersistedObject){
    	boolean isTenderPercApplicable=(isTenderPercentageType==true?mbPercentagelevel.equalsIgnoreCase(WorksConstants.MBPERCENTCONFIGVALUE)?true:false:false);
    	return measurementBookService.calculateMBDetails(mbHeader,isPersistedObject,isTenderPercApplicable);
    }
    
	protected void populatePreparedByList(AjaxMeasurementBookAction ajaxMBAction, boolean executingDeptPopulated){
		if (executingDeptPopulated) {
			ajaxMBAction.setExecutingDepartment(workOrderEstimateList.get(0).getEstimate().getExecutingDepartment().getId());
			ajaxMBAction.usersInExecutingDepartment();
			addDropdownData("preparedByList", ajaxMBAction.getUsersInExecutingDepartment());
		}else 
			addDropdownData("preparedByList", Collections.emptyList());
	}
	
	protected Integer getIdPersonalInformationFromParams() {
		String[] ids = parameters.get("mbPreparedBy");
		if (ids != null && ids.length > 0) {
			parameters.remove("mbPreparedBy");
			String id = ids[0];
			if (id != null && id.length() > 0) {
				return Integer.parseInt(id);
			}
		}
		return null;
	}
	
	protected void setMBPreparedBy(Integer idPersonalInformation) {
		if (validMBPreparedBy(idPersonalInformation)) {
			 mbHeader.setMbPreparedBy(employeeService.getEmloyeeById(idPersonalInformation));
			 if(mbHeader.getWorkOrderEstimate()!=null && mbHeader.getWorkOrderEstimate().getEstimate()!=null && mbHeader.getWorkOrderEstimate().getEstimate().getExecutingDepartment()!=null){
				 mbPreparedByView = (EmployeeView) getPersistenceService().find("from EmployeeView where id = ? and deptId.id=?",idPersonalInformation,mbHeader.getWorkOrderEstimate().getEstimate().getExecutingDepartment().getId());
			 }
			 else{
			 mbPreparedByView = (EmployeeView) getPersistenceService().find("from EmployeeView where id = ?", idPersonalInformation);
			 }
		 }		 
	 }
	
	 protected boolean validMBPreparedBy(Integer idPersonalInformation) {
		 if (idPersonalInformation != null && idPersonalInformation > 0) {
			 return true;
		 }
		 return false;
	 }
	
	 public String loadSerachForActivity(){
		 return ACTIVITY_SEARCH;
	 }
	 
	 public String searchActivitiesForMB(){
		Map<String, Object> criteriaMap = new HashMap<String, Object>();
		if(workOrderId != null)
			criteriaMap.put(WorkOrderServiceImpl.WORKORDER_ID, workOrderId);
		if(estimateId != null)
			criteriaMap.put(WorkOrderServiceImpl.WORKORDER_ESTIMATE_ID, estimateId);
		if(activityCode != null && !"".equalsIgnoreCase(activityCode))
			criteriaMap.put(WorkOrderServiceImpl.ACTIVITY_CODE, activityCode);
		if(activityDesc != null && !"".equalsIgnoreCase(activityDesc))
			criteriaMap.put(WorkOrderServiceImpl.ACTIVITY_DESC, activityDesc);
		
		List<WorkOrderActivity> tempActivityList = workOrderService.searchWOActivities(criteriaMap);
		activityList=new ArrayList<WorkOrderActivity>();
		 for(WorkOrderActivity woa: tempActivityList) {
			 if(woa.getActivity().getParent()==null)
				 activityList.add(woa);
		 }
		
		return ACTIVITY_SEARCH;
	 }
	 
	 public String loadSerachForMB(){
		 return MB_SEARCH;
	 }
	 
	 public String loadSearchForMB(){
		 return MB_SEARCH;
	 }
	 
	 
	 public String searchMB(){
		criteriaMap = new HashMap<String, Object>();
		if(workorderNo != null && !"".equalsIgnoreCase(workorderNo))
			criteriaMap.put(MeasurementBookServiceImpl.WORKORDER_NO, workorderNo);
		
		if(contractorId != null && contractorId != -1)
			criteriaMap.put(MeasurementBookServiceImpl.CONTRACTOR_ID, contractorId);
		
		if(fromDate!=null && toDate!=null && !DateUtils.compareDates(getToDate(),getFromDate()))
			//throw new ValidationException(Arrays.asList(new ValidationError("greaterthan.endDate.fromDate",getText("greaterthan.endDate.fromDate"))));
			addFieldError("enddate",getText("greaterthan.endDate.fromDate"));
		
		if(toDate!=null && !DateUtils.compareDates(new Date(),getToDate()))
			//throw new ValidationException(Arrays.asList(new ValidationError("greaterthan.endDate.currentdate",getText("greaterthan.endDate.currentdate"))));
			addFieldError("enddate",getText("greaterthan.endDate.currentdate"));		
		
		if(fromDate!=null && toDate==null){
			criteriaMap.put("FROM_DATE",new Date(DateUtils.getFormattedDate(getFromDate(),DATEFORMAT)));
		}else if(toDate!=null && fromDate==null){
			 criteriaMap.put("TO_DATE",new Date(DateUtils.getFormattedDate(getToDate(),DATEFORMAT)));
		}else if(fromDate!=null && toDate!=null && getFieldErrors().isEmpty()){
			criteriaMap.put("FROM_DATE", new Date(DateUtils.getFormattedDate(getFromDate(),DATEFORMAT)));
		    criteriaMap.put("TO_DATE",new Date(DateUtils.getFormattedDate(getToDate(),DATEFORMAT)));
		}		
		
		if(mbRefNo != null && !"".equalsIgnoreCase(mbRefNo))
			criteriaMap.put(MeasurementBookServiceImpl.MB_REF_NO, mbRefNo);
		if(mbPages != null && !"".equalsIgnoreCase(mbPages))
			criteriaMap.put(MeasurementBookServiceImpl.MB_PAGE_NO, mbPages);
		if(mbStatus != null && !"".equalsIgnoreCase(mbStatus) && !"-1".equals(mbStatus))
			criteriaMap.put(MeasurementBookServiceImpl.STATUS, mbStatus);
				
		if(estimateNo != null && !"".equalsIgnoreCase(estimateNo))
			criteriaMap.put(MeasurementBookServiceImpl.EST_NO, estimateNo);		
		
		criteriaMap.put("SOURCEPAGE", sourcepage);
		if(getFieldErrors().isEmpty()) {
			setPageSize(WorksConstants.PAGE_SIZE);
			search();
			if(searchResult.getFullListSize() !=0){
				mbList = getPositionAndUser(searchResult.getList());
			    searchResult.getList().clear();
			    searchResult.getList().addAll(mbList);
			}
		}
		return MB_SEARCH;
	 }
	 
	 protected List<MBHeader> getPositionAndUser(List<MBHeader> results){
			List<MBHeader> mbHeaderList = new ArrayList<MBHeader>();
			for(MBHeader mbh :results){
				PersonalInformation emp = employeeService.getEmployeeforPosition(mbh.getCurrentState().getOwner());
				if(emp!=null)
					mbh.setOwner(emp.getEmployeeName());
				mbHeaderList.add(mbh);
				String actions = worksService.getWorksConfigValue("MB_SHOW_ACTIONS");
				if(StringUtils.isNotBlank(actions)){
					mbh.getMbActions().addAll(Arrays.asList(actions.split(",")));
				}
			}	
			return mbHeaderList;
		}
	 
	 public Map<String,Object> getContractorForApprovedWorkOrder() {
			Map<String,Object> contractorsWithWOList = new HashMap<String, Object>();		
			if(workOrderService.getContractorsWithWO()!=null) {
				for(Contractor contractor :workOrderService.getContractorsWithWO()){ 
					contractorsWithWOList.put(contractor.getId()+"", contractor.getCode()+" - "+contractor.getName());
				}			
			}
			return contractorsWithWOList; 
		} 
	 
	 
	 public List<EgwStatus> getMbStatusList() {
		 return persistenceService.findAllBy("from EgwStatus s where moduletype=? and code<>'NEW' order by orderId",MBHeader.class.getSimpleName());
	}
	 
	public List<Contractor> getContractorList() {
		 return workOrderService.getAllContractorForWorkOrder();		
	}
	 
	public String newform(){
		return NEW;
	}
	
	public String edit(){
		mbHeader = calculateMBdetails(mbHeader,true);
		
		/*if(mbHeader.getWorkOrderEstimate().getEstimate().getIsSpillOverWorks()){
			mbHeader.setAdditionalWfRule("spillOverWorks");
		}
		else{
			 if(PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(getWorkFlowDepartment()) && getAdditionalRule().equalsIgnoreCase("ZonalPublicWork"))
				 mbHeader.setAdditionalWfRule("ZonalPublicWork");
			 else if(PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(getWorkFlowDepartment())){
				 mbHeader.setAdditionalWfRule("HQPublicWork");
			 }
			 }*/
			 
		return NEW;
	}
	
	public String execute(){
		return SUCCESS;
	}
	/*
	public String cancel(){
		return SUCCESS;
	}
	*/
	
	public String save(){
		String status=null;
		RevisionAbstractEstimate oldRE=null;
		
		if(mbHeader.getRevisionEstimate()!=null) {
			oldRE=mbHeader.getRevisionEstimate();
		}
		mbHeader.getMbDetails().clear();
		
		String actionName = "";
		if (parameters.get(ACTION_NAME) != null && parameters.get(ACTION_NAME)[0] != null) 
			actionName = parameters.get(ACTION_NAME)[0];
			
		if(!sorActivities.isEmpty() || !nonSorActivities.isEmpty() || !extraItemsActivities.isEmpty()) {			
			createRevisionEstimate();
		}
		mbHeader.setWorkOrderEstimate((WorkOrderEstimate)persistenceService.findByNamedQuery("getWorkOrderEstimateByEstAndWO", estimateId,workOrderId));
		populateMBActivities();
		
				
		if((actionName.equalsIgnoreCase(MBHeader.Actions.SAVE.toString()) || actionName.equalsIgnoreCase("Forward")) &&  mbHeader.getMbDetails().isEmpty()){
			throw new ValidationException(Arrays.asList(new ValidationError("measurementbook.item.mandatory","measurementbook.item.mandatory")));
		}
		
		if(getModel().getCurrentState()!=null){
			status=getModel().getCurrentState().getValue();
		}
		else{
			status="NEW";
		}
		if((actionName.equalsIgnoreCase("Forward")||actionName.equalsIgnoreCase("Approve")) && customizedWorkFlowService.getWfMatrix(getModel().getStateType(), getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(),status, getPendingActions())==null && !mbHeader.getWorkOrderEstimate().getEstimate().getIsSpillOverWorks()){
			String msg="Workflow is not available for "+getWorkFlowDepartment();
			throw new ValidationException(Arrays.asList(new ValidationError(null,msg)));
		}
		
		/*if(!actionName.equalsIgnoreCase(MBHeader.Actions.APPROVAL.toString())
				&& workOrderService.isMBInApprovalPendingForWO(workorderNo))
			throw new ValidationException(Arrays.asList(new ValidationError("measurementbook.approvalpending",
			"measurementbook.approvalpending")));*/
		
		//if(measurementBookService.approvalLimitCrossed(mbHeader)){
		
		if(getMborderNumberRequired().equals("yes")){
			for(MBDetails details:mbHeader.getMbDetails()){
				if(measurementBookService.approvalLimitCrossed(details)){
				  if(!StringUtils.isNotBlank(details.getOrderNumber())){
					throw new ValidationException(Arrays.asList(new ValidationError("Enter Order number ","Enter Order number ")));
				  }
				  if(details.getMbdetailsDate()==null){		
					throw new ValidationException(Arrays.asList(new ValidationError("Enter Order Date","Enter Order date")));
			      }
				  else if(details.getMbdetailsDate()!=null && DateConversionUtil.isBeforeByDate(new Date(),details.getMbdetailsDate())){
			    	throw new ValidationException(Arrays.asList(new ValidationError("Order Date should be current or previous date","Order Date should be current or previous date")));
			      }
			   }
		   }
		}else{
			/*for(MBDetails details:mbHeader.getMbDetails()){
				if(measurementBookService.approvalLimitCrossed(details)){
					Double approvedQuantityPercentage;
					Double extraPercentage = worksService.getConfigval();
					String errorMessage;
					if(extraPercentage.doubleValue()>0){
						approvedQuantityPercentage=100+extraPercentage;
						errorMessage=getText("measurementbook.currMBEntry.quantityFactor.error","measurementbook.currMBEntry.quantityFactor.error") +" "+approvedQuantityPercentage+" "+getText("measurementbook.currMBEntry.approvedQuantity.error","measurementbook.currMBEntry.approvedQuantity.error");
					 }else{
						 errorMessage=getText("measurementbook.currMBEntry.error","measurementbook.currMBEntry.error");
					 }
					
					throw new ValidationException(Arrays.asList(new ValidationError(errorMessage,errorMessage)));
			   }
		   }*/
		   }
		//}		
		mbHeader = calculateMBdetails(mbHeader,true);
		mbHeader = measurementBookService.persist(mbHeader);
		
		if(oldRE!=null) {			
			if(sorActivities.isEmpty() && nonSorActivities.isEmpty() && extraItemsActivities.isEmpty())
				mbHeader.setRevisionEstimate(null);	
			deleteRevisionEstimate(oldRE);
		}
/*		if (!actionName.isEmpty()) {			
			mbHeader = (MBHeader) workflowService.transition(actionName, mbHeader,approverComments);
		}		
		
		mbHeader = calculateMBdetails(mbHeader,true);
		if(mbHeader.getCurrentState()!=null && mbHeader.getCurrentState().getValue()!=null 
				&&  APPROVED.equalsIgnoreCase(mbHeader.getCurrentState().getValue())){
			messageKey="measurementbook.approved";
		}
		else{
			messageKey="measurementbook.save.success";
		}
		addActionMessage(getText(messageKey,messageKey));
		getDesignation(mbHeader);
		return SAVE_ACTION.equals(actionName)?EDIT:SUCCESS;	
*/		
		//addActionMessage(getText("measurementbook.save.success"));
		//return EDIT;
		
		if(mbHeader.getWorkOrderEstimate().getEstimate().getIsSpillOverWorks()){
			mbHeader.setAdditionalWfRule("spillOverWorks");
		}
		
		else{
			 if(PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(getWorkFlowDepartment()) && getAdditionalRule().equalsIgnoreCase("ZonalPublicWork"))
				 mbHeader.setAdditionalWfRule("ZonalPublicWork");
			 else if(PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(getWorkFlowDepartment())){
				 mbHeader.setAdditionalWfRule("HQPublicWork");
			 }
			 }
			 
		if(actionName.equalsIgnoreCase("save")){
			mbHeader.setEgwStatus(commonsService.getStatusByModuleAndCode("MBHeader","NEW"));
			if(id ==null){
			Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
			mbHeader = (MBHeader) workflowService.start(mbHeader, pos, "MeasurementBook created.");
			}
			messageKey="measurementbook."+actionName;
			addActionMessage(getText(messageKey,"The MeasurementBook was saved successfully"));
			mbHeader = measurementBookService.persist(mbHeader);
			getDesignation(mbHeader);
		}
		else{
			if(id ==null){
				mbHeader.setEgwStatus(commonsService.getStatusByModuleAndCode("MBHeader","NEW"));
				Position pos = employeeService.getPositionforEmp(employeeService.getEmpForUserId(abstractEstimateService.getCurrentUserId()).getIdPersonalInformation());
				mbHeader = (MBHeader) workflowService.start(mbHeader, pos, "Measurementbook created.");
			}
			workflowService.transition(actionName, mbHeader, approverComments);
			//mbHeader.setEgwStatus(getModel().getCurrentState().getValue(););
			mbHeader = measurementBookService.persist(mbHeader);
			messageKey="measurementbook."+actionName;
			getDesignation(mbHeader);
		}
		
		return SAVE_ACTION.equalsIgnoreCase(actionName)?EDIT:SUCCESS;
	}
	
	protected void deleteRevisionEstimate(RevisionAbstractEstimate oldRE) {		
		WorkOrderEstimate revisionWOE=(WorkOrderEstimate)persistenceService.find("from WorkOrderEstimate where estimate.id=?", oldRE.getId());
		workOrderService.delete(revisionWOE.getWorkOrder());
		revisionAbstractEstimateService.delete(oldRE);			
	}
	
	protected void createRevisionEstimate() {
		AbstractEstimate abstractEstimate=abstractEstimateService.findById(estimateId, false);
		WorkOrderEstimate workOrderEstimate=(WorkOrderEstimate)persistenceService.findByNamedQuery("getWorkOrderEstimateByEstAndWO", estimateId,workOrderId);
		
		List<AbstractEstimate> revisionEstimates=abstractEstimateService.findAllBy("from AbstractEstimate where parent.id=?", estimateId);
		int reCount=1;
		reCount=reCount+revisionEstimates.size();
		if(mbHeader.getRevisionEstimate()!=null) {
			reCount=reCount-1;
		}
		RevisionAbstractEstimate revisionEstimate=new RevisionAbstractEstimate();
		revisionEstimate.setParent(abstractEstimate);
		revisionEstimate.setEstimateDate(mbHeader.getMbDate());
		revisionEstimate.setEstimateNumber(abstractEstimate.getEstimateNumber()+"/RE".concat(Integer.toString(reCount)));
		revisionEstimate.setName("Revision Estimate for: "+abstractEstimate.getName());
		revisionEstimate.setDescription("Revision Estimate for: "+abstractEstimate.getDescription());
		revisionEstimate.setNecessity(abstractEstimate.getNecessity()); 
		revisionEstimate.setScopeOfWork(abstractEstimate.getScopeOfWork()); 
		revisionEstimate.setType(abstractEstimate.getType());
		revisionEstimate.setExecutingDepartment(abstractEstimate.getExecutingDepartment());
		revisionEstimate.setEstimatePreparedBy(abstractEstimate.getEstimatePreparedBy());
		revisionEstimate.setWard(abstractEstimate.getWard());
		revisionEstimate.setFundSource(abstractEstimate.getFundSource());
		revisionEstimate.setEgwStatus(commonsService.getStatusByModuleAndCode("AbstractEstimate","NEW"));
		if(parameters.get(ACTION_NAME)[0].equalsIgnoreCase("Approve"))	
			revisionEstimate.setEgwStatus(commonsService.getStatusByModuleAndCode("AbstractEstimate","CREATED"));
		if(parameters.get(ACTION_NAME)[0].equalsIgnoreCase("Cancel"))	
			revisionEstimate.setEgwStatus(commonsService.getStatusByModuleAndCode("AbstractEstimate","CANCELLED"));
		populateSorActivities(revisionEstimate);
		populateNonSorActivities(revisionEstimate);
		populateActivities(revisionEstimate);
		populateExtraItems(revisionEstimate);
		revisionAbstractEstimateService.persist(revisionEstimate);
		
		WorkOrder revisionWO=new WorkOrder();
		revisionWO.setParent(workOrderEstimate.getWorkOrder());
		revisionWO.setWorkOrderDate(mbHeader.getMbDate());
		revisionWO.setWorkOrderNumber(workOrderEstimate.getWorkOrder().getWorkOrderNumber()+"/RW".concat(Integer.toString(reCount)));
		revisionWO.setContractor(workOrderEstimate.getWorkOrder().getContractor());
		revisionWO.setWorkOrderPreparedBy(workOrderEstimate.getWorkOrder().getWorkOrderPreparedBy());
		revisionWO.setEngineerIncharge(workOrderEstimate.getWorkOrder().getEngineerIncharge());
		revisionWO.setEgwStatus(commonsService.getStatusByModuleAndCode("WorkOrder","NEW"));
		if(parameters.get(ACTION_NAME)[0].equalsIgnoreCase("Approve"))	
			revisionWO.setEgwStatus(commonsService.getStatusByModuleAndCode("WorkOrder","CREATED"));
		if(parameters.get(ACTION_NAME)[0].equalsIgnoreCase("Cancel"))	
			revisionWO.setEgwStatus(commonsService.getStatusByModuleAndCode("WorkOrder","CANCELLED"));
		populateWorkOrderActivities(revisionWO,revisionEstimate);
		mbHeader.setRevisionWorkOrderEstimate(revisionWO.getWorkOrderEstimates().get(0));
		workOrderService.persist(revisionWO);	
		
		for(Activity act:abstractEstimate.getActivities()) {
			if(act.getQuantity()==0.0)
				isZeroQty=true;
		}
		
		if(isZeroQty && parameters.get(ACTION_NAME)[0].equalsIgnoreCase("Approve")) {
			BigDecimal woAmount=workOrderService.getRevisionEstimateWOAmount(mbHeader.getWorkOrder());
			BigDecimal mbAmount = measurementBookService.getRevisionEstimateMBAmount(abstractEstimate);
			BigDecimal revWoAmount=BigDecimal.valueOf(revisionWO.getWorkOrderAmount());
			if((mbAmount.add(revWoAmount).compareTo(woAmount))==-1){   // Total cost exceeding WO amount
				revisionEstimate.setEgwStatus(commonsService.getStatusByModuleAndCode("AbstractEstimate","APPROVED"));
				revisionWO.setEgwStatus(commonsService.getStatusByModuleAndCode("WorkOrder","APPROVED"));				
			} 
		}
				
		mbHeader.setRevisionEstimate(revisionEstimate);
	}
	
	
	 protected void populateSorActivities(AbstractEstimate abstractEstimate) {
		 for(Activity activity: sorActivities) {
			 if (validSorActivity(activity)) {
				 activity.setSchedule((ScheduleOfRate) getPersistenceService().find("from ScheduleOfRate where id = ?", activity.getSchedule().getId()));
				 activity.setUom(activity.getSchedule().getUom());
				 abstractEstimate.addActivity(activity);
			 }
		 }
	 }
	 
	 protected boolean validSorActivity(Activity activity) {
		 if (activity != null && activity.getSchedule() != null && activity.getSchedule().getId() != null) {
			 return true;
		 }
		 
		 return false;
	 }
	 
	 protected void populateNonSorActivities(AbstractEstimate abstractEstimate) {
		 for (Activity activity: nonSorActivities) {
			 if (activity!=null) {
				 activity.setUom(activity.getNonSor().getUom());
				 if(activity.getNonSor().getId()!=null && activity.getNonSor().getId()!=0 && activity.getNonSor().getId()!=1) {
					 activity.setNonSor((NonSor)getPersistenceService().find("from NonSor where id = ?", activity.getNonSor().getId()));
					
				 }
				 else{
					 if(activity.getNonSor()!=null) {
						 nonSorService.persist(activity.getNonSor()); 
					 }
				 }			 
				 
				 abstractEstimate.addActivity(activity);
			 }
		 }
	 }
		
	 protected void populateActivities(AbstractEstimate abstractEstimate) { 
		 int count=1;
		 for(Activity activity: abstractEstimate.getActivities()) {
			 activity.setAbstractEstimate(abstractEstimate);
			 activity.setRevisionType(RevisionType.EXTRA_ITEM);
			 if(!measurementSheetList.isEmpty()){
				 for (MeasurementSheet ms: measurementSheetList) { 
					   	 if (ms!=null) {
							if(((ms.getActivity().getSchedule()!=null && ms.getActivity().getSchedule().getId() !=null 
									 && activity.getSchedule()!=null && activity.getSchedule().getId()!=null && 
									 activity.getSchedule().getId().equals(ms.getActivity().getSchedule().getId())))) {
										 ms.setActivity(activity);
										 activity.addMeasurementSheet(ms);
							 }
							 else if( ms.getActivity()!=null  && ms.getSlNo()!=null && activity!=null && activity.getSchedule()==null){
										 if(activity.getSrlNo().equals(ms.getSlNo())) {
											 ms.setActivity(activity);
											 activity.addMeasurementSheet(ms);
										 }								 
								 } 
							}
					 	
				 } 
			 }

			 if(activity.getSrlNo()!=null) {				 
                 activity.setSrlNo(count++);
			 }
		 }
	 }
	 
	 protected void populateExtraItems(AbstractEstimate abstractEstimate) {
		 for(WorkOrderActivity woa: extraItemsActivities) {
			 if(woa!=null) {
				 WorkOrderActivity parentWOA=(WorkOrderActivity)getPersistenceService().find("from WorkOrderActivity where id=?", woa.getId());
				 woa.getActivity().setAbstractEstimate(abstractEstimate);
				 if("-".equals(woa.getActivity().getSignValue()))
					 woa.getActivity().setRevisionType(RevisionType.REDUCED_QUANTITY);
				 else
					 woa.getActivity().setRevisionType(RevisionType.ADDITITONAL_QUANTITY);
				 woa.getActivity().setParent(parentWOA.getActivity());
				 woa.getActivity().setUom(parentWOA.getActivity().getUom());
				 woa.getActivity().setRate(woa.getActivity().getRate());
				 //woa.getActivity().setQuantity(woa.getActivity().getMbQuantity());
				 if(parentWOA.getActivity().getNonSor()==null)
					 woa.getActivity().setSchedule(parentWOA.getActivity().getSchedule());
				 else
					 woa.getActivity().setNonSor(parentWOA.getActivity().getNonSor());
				 
				 
				 if(!changeQuantityMSheetList.isEmpty()){
					for (WorkOrderMeasurementSheet woms: changeQuantityMSheetList) { 
					   	 if(woms!=null) {
					   	
					   		 if(woms.getWoActivity()!=null && woms.getWoActivity().getId()!=null &&  woms.getWoActivity().getId().equals(woa.getId()) && woms.getMeasurementSheet().getId()==null){
					   			woms.getWoActivity().setParent(parentWOA);
					   			 woms.getMeasurementSheet().setActivity(woa.getActivity());
					   			 woa.getActivity().addMeasurementSheet(woms.getMeasurementSheet());
					   		 }
						}
				 }
				 }
				 abstractEstimate.addActivity(woa.getActivity());
			 }
		 }
		 
	 }
	 
	 protected void populateWorkOrderActivities(WorkOrder workOrder, AbstractEstimate abstractEstimate) {			
			WorkOrderEstimate workOrderEstimate = new WorkOrderEstimate();
			workOrderEstimate.setEstimate(abstractEstimate);
			workOrderEstimate.setWorkOrder(workOrder);
			addWorkOrderEstimateActivities(workOrderEstimate, abstractEstimate);
			workOrder.addWorkOrderEstimate(workOrderEstimate);
	}
	 
	 private void addWorkOrderEstimateActivities(WorkOrderEstimate workOrderEstimate, AbstractEstimate abstractEstimate) {
		double woTotalAmount=0;
		for (Activity activity : abstractEstimate.getActivities()) {
			WorkOrderActivity workOrderActivity=new WorkOrderActivity();
			workOrderActivity.setActivity(activity);
			double approvedAmount=0;
			if(workOrderActivity.getActivity().getRevisionType().equals(RevisionType.EXTRA_ITEM)) {
			workOrderActivity.setApprovedRate(activity.getRate().getValue()*activity.getConversionFactor());
			workOrderActivity.setApprovedQuantity(activity.getQuantity());			
				approvedAmount=new Money(workOrderActivity.getApprovedRate() * workOrderActivity.getApprovedQuantity() * activity.getConversionFactor()).getValue();
			}
			else{
				workOrderActivity.setApprovedRate(activity.getRate().getValue());
				workOrderActivity.setApprovedQuantity(activity.getQuantity());			
				approvedAmount=new Money(workOrderActivity.getApprovedRate() * workOrderActivity.getApprovedQuantity()).getValue();	
			}
			if(workOrderActivity.getActivity().getRevisionType().equals(RevisionType.REDUCED_QUANTITY))
				woTotalAmount=woTotalAmount-approvedAmount;
			else
			woTotalAmount=woTotalAmount+approvedAmount;
			workOrderActivity.setApprovedAmount(approvedAmount);
			workOrderActivity.setWorkOrderEstimate(workOrderEstimate);
			for(MeasurementSheet measurementSheet: activity.getMeasurementSheetList()){
				WorkOrderMeasurementSheet woMsheet=new WorkOrderMeasurementSheet();
				woMsheet.setNo(measurementSheet.getNo());
				woMsheet.setLength(measurementSheet.getUomLength());
				woMsheet.setWidth(measurementSheet.getWidth());
				woMsheet.setDepthOrHeight(measurementSheet.getDepthOrHeight());
				woMsheet.setQuantity(measurementSheet.getQuantity()); 
				woMsheet.setMeasurementSheet(measurementSheet);
				woMsheet.setWoActivity(workOrderActivity);
				workOrderActivity.addWoMeasurementSheet(woMsheet);
			}			
			workOrderEstimate.addWorkOrderActivity(workOrderActivity);
		}
		workOrderEstimate.getWorkOrder().setWorkOrderAmount(woTotalAmount);
	}
	
	
	protected void populateMBActivities() {	
		 for(MBDetails mbDetails: actionMbDetailValues) { 
			 if(mbDetails != null) {
				 mbDetails.setMbHeader(mbHeader);				
				 if(mbDetails.getWorkOrderActivity().getActivity().getId()==null) 
					 mbDetails.setWorkOrderActivity((WorkOrderActivity)getPersistenceService().find("from WorkOrderActivity where id=?", mbDetails.getWorkOrderActivity().getId()));
				 if(!mbMeasurementSheetList.isEmpty()){
					for (MBMeasurementSheet mbMS: mbMeasurementSheetList) {
						   	 if(mbMS!=null && mbMS.getWoMeasurementSheet()!=null && mbMS.getWoMeasurementSheet().getId()!=null) {  
								if(((mbMS.getMbDetails().getWorkOrderActivity().getId()!=null && mbDetails.getWorkOrderActivity().getId()!=null && 
										mbMS.getMbDetails().getWorkOrderActivity().getId().equals(mbDetails.getWorkOrderActivity().getId())))) {
										WorkOrderMeasurementSheet woMSheet=(WorkOrderMeasurementSheet)getPersistenceService().find("from WorkOrderMeasurementSheet where id=?", mbMS.getWoMeasurementSheet().getId());
										mbMS.setWoMeasurementSheet(woMSheet);
										if(mbMS.getIdentifier()!='A' && mbMS.getIdentifier()!='D')
											mbMS.setIdentifier(' ');
										mbMS.setMbDetails(mbDetails);
										mbDetails.addMbMeasurementSheet(mbMS); 
								 }
					 }
						   	 else if(mbMS!=null && mbMS.getWoMeasurementSheet()!=null && mbMS.getWoMeasurementSheet().getId()==null){  
						   		if(((mbMS.getMbDetails().getWorkOrderActivity().getId()!=null && mbDetails.getWorkOrderActivity().getId()!=null && 
										mbMS.getMbDetails().getWorkOrderActivity().getId().equals(mbDetails.getWorkOrderActivity().getId()) && mbMS.getQuantity()>0))) {
										mbMS.setWoMeasurementSheet(null);
										mbMS.setMbDetails(mbDetails);
										mbDetails.addMbMeasurementSheet(mbMS);   
				 }
						   	 }
					 }
				 }
				 mbHeader.addMbDetails(mbDetails); 
			 }
		 } 
		 if(mbHeader.getRevisionEstimate()!=null && (!sorActivities.isEmpty() || !nonSorActivities.isEmpty() || !extraItemsActivities.isEmpty() || !changeQuantityMSheetList.isEmpty())) {
			 for(Activity activity: mbHeader.getRevisionEstimate().getActivities()){
				 MBDetails reMbDetails = new MBDetails();
				 reMbDetails.setMbHeader(mbHeader);
				 reMbDetails.setWorkOrderActivity((WorkOrderActivity)getPersistenceService().find("from WorkOrderActivity where activity.id=? and workOrderEstimate.estimate.id=?", activity.getId(),mbHeader.getRevisionEstimate().getId()));				 
				 reMbDetails.setQuantity(activity.getMbQuantity());
				 if(activity.getPartRate()>0){
					 reMbDetails.setPartRate(activity.getPartRate());
				 }
				 if(activity.getReducedRate()>0){
					 reMbDetails.setReducedRate(activity.getReducedRate());
				 }
				 if(!activity.getMeasurementSheetList().isEmpty()){
					 for (MeasurementSheet ms: activity.getMeasurementSheetList()) { 
						   	 if (ms!=null && ms.getConsumedQuantity()!=0) {
						   		MBMeasurementSheet mbMS = new MBMeasurementSheet();
								WorkOrderMeasurementSheet woMSheet=(WorkOrderMeasurementSheet)getPersistenceService().find("from WorkOrderMeasurementSheet where woActivity=? and measurementSheet.id=?", reMbDetails.getWorkOrderActivity(), ms.getId());
								mbMS.setWoMeasurementSheet(woMSheet);
								mbMS.setNo(ms.getConsumedNo());
								mbMS.setUomLength(ms.getConsumedLength());
								mbMS.setWidth(ms.getConsumedWidth());
								mbMS.setDepthOrHeight(ms.getConsumedDH());
								mbMS.setQuantity(ms.getConsumedQuantity()); 
								mbMS.setMbDetails(reMbDetails);
								if(mbMS.getIdentifier()!='A' && mbMS.getIdentifier()!='D')
									mbMS.setIdentifier(' ');
								reMbDetails.addMbMeasurementSheet(mbMS);
						   	 }
					 }
				 }
				 if(!changeQuantityMSheetList.isEmpty()){ 
					 Long activityId;
					 if(activity.getParent()!=null)
						 activityId=activity.getParent().getId();
					 else
						 activityId=activity.getId(); 
					 reMbDetails.getWorkOrderActivity().setParent((WorkOrderActivity)getPersistenceService().find("from WorkOrderActivity where activity.id=? and (workOrderEstimate.id=? or workOrderEstimate.estimate.parent.id=?)", activityId,mbHeader.getWorkOrderEstimate().getId(),mbHeader.getWorkOrderEstimate().getEstimate().getId()));
					 for (WorkOrderMeasurementSheet woms: changeQuantityMSheetList) { 
						 if(woms!=null) {	
							 if(woms.getWoActivity()!=null && woms.getWoActivity().getId()!=null &&  woms.getWoActivity().getId().equals(reMbDetails.getWorkOrderActivity().getParent().getId()) && woms.getMeasurementSheet().getId()!=null){
								 if (woms!=null && woms.getMeasurementSheet().getConsumedQuantity()!=0) {  
									 WorkOrderMeasurementSheet woMSheet=(WorkOrderMeasurementSheet)getPersistenceService().find("from WorkOrderMeasurementSheet where woActivity=? and measurementSheet.id=?",reMbDetails.getWorkOrderActivity().getParent(), woms.getMeasurementSheet().getId());	
									 if(woMSheet!=null) {
									    MBMeasurementSheet mbMS = new MBMeasurementSheet();
										mbMS.setWoMeasurementSheet(woMSheet);
										mbMS.setNo(woms.getMeasurementSheet().getConsumedNo());
										mbMS.setUomLength(woms.getMeasurementSheet().getConsumedLength());
										mbMS.setWidth(woms.getMeasurementSheet().getConsumedWidth());
										mbMS.setDepthOrHeight(woms.getMeasurementSheet().getConsumedDH());
										mbMS.setQuantity(woms.getMeasurementSheet().getConsumedQuantity()); 
										mbMS.setMbDetails(reMbDetails);
										if(mbMS.getIdentifier()!='A' && mbMS.getIdentifier()!='D')
											mbMS.setIdentifier(' ');
										reMbDetails.addMbMeasurementSheet(mbMS);	
									 }
								 }
							 }
				   		 
						 }
					 }
				 }
				 mbHeader.addMbDetails(reMbDetails);
			 }			 
		 }			 
	 }
	
	/*protected void validateMbdetails(List<MBDetails> mbDetails) {
		for(MBDetails mbDetail:mbDetails){
			 if (mbDetail != null && mbDetail.getWorkOrderActivity() != null 
					 && mbDetail.getWorkOrderActivity().getWorkOrder() != null && !workorderNo.equalsIgnoreCase
						 (mbDetail.getWorkOrderActivity().getWorkOrder().getWorkOrderNumber())){
						throw new ValidationException(Arrays.asList(new ValidationError("measurementbook.activity.wo.invalid",
								"measurementbook.activity.wo.invalid")));
			 }
		}
	}*/
	
	//workflow for reject mb
	/** reject  */	 
/*	public String reject(){
		String actionName = "";
		if (parameters.get(ACTION_NAME) != null && parameters.get(ACTION_NAME)[0] != null) 
			actionName = parameters.get(ACTION_NAME)[0];
		
		if(mbHeader!=null && mbHeader.getId()!=null && !actionName.isEmpty()){	
			//calling workflow api
			workflowService.transition(actionName,mbHeader,approverComments);		
			mbHeader=measurementBookService.persist(mbHeader);				
			getDesignation(mbHeader);
		}
		messageKey="measurementbook.reject";		
		return SUCCESS;
	}
	
	//workflow for cancel mb		 
	public String cancel(){		
		if(mbHeader!=null && mbHeader.getEgBillregister()!=null 
				&& !mbHeader.getEgBillregister().getCurrentState().getValue().equalsIgnoreCase("CANCELLED")) {
		  	messageKey="measurementbook.cancel.failure";	
		  	addActionError(getText(messageKey));
			return EDIT;
		}
				
		String actionName = "";
		if (parameters.get(ACTION_NAME) != null && parameters.get(ACTION_NAME)[0] != null) 
			actionName = parameters.get(ACTION_NAME)[0];
		
		if(mbHeader!=null && mbHeader.getId()!=null && !actionName.isEmpty()){			
			workflowService.transition(actionName,mbHeader,approverComments);		
			mbHeader=measurementBookService.persist(mbHeader);		
			getDesignation(mbHeader);
		}
		messageKey="measurementbook.cancel";		
		return SUCCESS;
	}
*/	
	
	
	 public String cancelApprovedMB() {  
		MBHeader mbHeader= measurementBookService.findById(mbId, false);
		mbHeader.getCurrentState().getPrevious().setValue(MBHeader.MeasurementBookStatus.CANCELLED.toString());
		mbHeader.setEgwStatus(commonsService.getStatusByModuleAndCode("MbHeader",MBHeader.MeasurementBookStatus.CANCELLED.toString()));
		
		PersonalInformation prsnlInfo=employeeService.getEmpForUserId(Integer.valueOf(getLoggedInUserId()));			
		String empName="";
		if(prsnlInfo.getEmployeeFirstName()!=null)
			empName=prsnlInfo.getEmployeeFirstName();
		if(prsnlInfo.getEmployeeLastName()!=null)
			empName=empName.concat(" ").concat(prsnlInfo.getEmployeeLastName()); 			
		mbHeader.getCurrentState().getPrevious().setText1(cancelRemarks+". MB Cancelled by: "+empName);
		
		mbRefNo=mbHeader.getMbRefNo();
		messageKey=mbRefNo+": The Measurement Book was Cancelled successfully"; 
		return SUCCESS;
	}
	 
	 public int getLoggedInUserId() {
		 return Integer.parseInt(EGOVThreadLocals.getUserId());
	}
	
	protected void getDesignation(MBHeader mbHeader){	
		/* start for customizing workflow message display */
		if(mbHeader.getCurrentState()!= null 
				&& !"NEW".equalsIgnoreCase(mbHeader.getCurrentState().getValue())) {
			String result = worksService.getEmpNameDesignation(mbHeader.getState().getOwner(),mbHeader.getState().getCreatedDate());
			if(result != null && !"@".equalsIgnoreCase(result)) {
				String empName = result.substring(0,result.lastIndexOf('@'));
				String designation =result.substring(result.lastIndexOf('@')+1,result.length());
				setDispEmployeeName(empName);
				setDispDesignation(designation);			
			}
		}
		/* end */	
	}	
	
	public String verify(){
		// TODO save and show details for submit
		return VERIFY;
	}
	
	public String submit(){
		// TODO make status approval pending for all MBS  
		return SUBMITTED;
	}
	
	@Override
	public StateAware getModel() {
		// TODO Auto-generated method stub
		return mbHeader;
	}
	
	public void setModel(MBHeader mbHeader){
		this.mbHeader = mbHeader;		
	}

	public List<MBDetails> getMbDetails() {
		return mbDetails;
	}

	public void setMbDetails(List<MBDetails> mbDetails) {
		this.mbDetails = mbDetails;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public EmployeeView getMbPreparedByView() {
		return mbPreparedByView;
	}

	public void setMbPreparedByView(EmployeeView mbPreparedByView) {
		this.mbPreparedByView = mbPreparedByView;
	}

	public void setMeasurementBookService(
			MeasurementBookService measurementBookService) {
		this.measurementBookService = measurementBookService;
	}

	public void setActionMbDetailValues(List<MBDetails> actionMbDetailValues) {
		this.actionMbDetailValues = actionMbDetailValues;
	}

	public List<WorkOrderActivity> getActivityList() {
		return activityList;
	}

	public List<MBDetails> getActionMbDetailValues() {
		return actionMbDetailValues;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getActivityDesc() {
		return activityDesc;
	}

	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}

	public String getWorkorderNo() {
		return workorderNo;
	}

	public void setWorkorderNo(String workorderNo) {
		this.workorderNo = workorderNo;
	}

	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	public double getQuantityFactor() {
		return quantityFactor;
	}

	public void setQuantityFactor(double quantityFactor) {
		this.quantityFactor = quantityFactor;
	}

	public List<MBHeader> getMbList() {
		return mbList;
	}

	public void setActivityList(List<WorkOrderActivity> activityList) {
		this.activityList = activityList;
	}

	public Long getContractorId() {
		return contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

	public String getMbRefNo() {
		return mbRefNo;
	}

	public void setMbRefNo(String mbRefNo) {
		this.mbRefNo = mbRefNo;
	}

	public String getMbPages() {
		return mbPages;
	}

	public void setMbPages(String mbPages) {
		this.mbPages = mbPages;
	}

	public String getMbStatus() {
		return mbStatus;
	}

	public void setMbStatus(String mbStatus) {
		this.mbStatus = mbStatus;
	}
	// on jan 13 th workflow related
/*	public List<org.egov.infstr.workflow.Action> getValidActions(){
		return workflowService.getValidActions(mbHeader);
	}
*/
	public void setMeasurementBookWorkflowService(WorkflowService<MBHeader> workflow) {
		this.workflowService = workflow;
	}
	
	
	public MeasurementBookWFService getMeasurementBookWFService() {
		return measurementBookWFService;
	}

	public void setMeasurementBookWFService(
			MeasurementBookWFService measurementBookWFService) {
		this.measurementBookWFService = measurementBookWFService;
	}
	public String getSourcepage() {
		return sourcepage;
	}

	public void setSourcepage(String sourcepage) {
		this.sourcepage = sourcepage;
	}
	
	public String getDispEmployeeName() {
		return dispEmployeeName;
	}

	public void setDispEmployeeName(String dispEmployeeName) {
		this.dispEmployeeName = dispEmployeeName;
	}

	public String getDispDesignation() {
		return dispDesignation;
	}

	public void setDispDesignation(String dispDesignation) {
		this.dispDesignation = dispDesignation;
	}	
	
	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public String getApproverComments() {
		return approverComments;
	}

	public void setApproverComments(String approverComments) {
		this.approverComments = approverComments;
	}

	public Integer getApproverUserId() {
		return approverUserId;
	}

	public void setApproverUserId(Integer approverUserId) {
		this.approverUserId = approverUserId;
	}

	public String getMborderNumberRequired() {
		mborderNumberRequired= worksService.getWorksConfigValue("ORDER_NUMBER_REQUIRED");
		return mborderNumberRequired;
	}

	public String getWorkOrderEstimateRequired() {
		return  worksService.getWorksConfigValue("WORKORDER_ESTIMATE_REQUIRED");
	}
	
	public String getMBWorkflowModifyDesignation() {
		return  worksService.getWorksConfigValue("MB_WORKFLOW_MODIFY_DESIG");
	}
	
	public void setMborderNumberRequired(String mborderNumberRequired) {
		this.mborderNumberRequired = mborderNumberRequired;
	}

	public Long getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(Long workOrderId) {
		this.workOrderId = workOrderId;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public Long getEstimateId() {
		return estimateId;
	}

	public void setEstimateId(Long estimateId) {
		this.estimateId = estimateId;
	}

	public String getWorkName() {
		return workName;
	}

	public void setWorkName(String workName) {
		this.workName = workName;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public List<WorkOrderEstimate> getWorkOrderEstimateList() {
		return workOrderEstimateList;
	}

	public void setWorkOrderEstimateList(
			List<WorkOrderEstimate> workOrderEstimateList) {
		this.workOrderEstimateList = workOrderEstimateList;
	}

	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}
	// end workflow related

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder){
		
		String dynQuery = " from MBHeader mbh where mbh.id != null and mbh.state.value<>'NEW'" ;
		List<Object> paramList = new ArrayList<Object>();

		if(criteriaMap.get("WORKORDER_NO") != null){
			dynQuery = dynQuery + " and mbh.workOrder.workOrderNumber like '%"
								+ criteriaMap.get("WORKORDER_NO")
								+ "%'";
		}
		
		if(criteriaMap.get("EST_NO") != null){
			dynQuery = dynQuery + " and mbh.workOrderEstimate.estimate.estimateNumber like '%"
								+ criteriaMap.get("EST_NO")
								+ "%'";
		}
		if(criteriaMap.get("CONTRACTOR_ID") != null && !"-1".equals(criteriaMap.get("CONTRACTOR_ID"))) {
			dynQuery = dynQuery + " and mbh.workOrder.contractor.id = ?";
			paramList.add(criteriaMap.get("CONTRACTOR_ID"));
		}
		if(criteriaMap.get("CREATE_DATE") != null) {
			dynQuery = dynQuery + " and mbh.mbDate = ?";
			paramList.add(criteriaMap.get("CREATE_DATE"));
		}
		if(criteriaMap.get("FROM_DATE") != null && criteriaMap.get("TO_DATE")==null) {
			dynQuery = dynQuery + " and mbh.mbDate >= ? ";
			paramList.add(criteriaMap.get("FROM_DATE"));

		}else if(criteriaMap.get("TO_DATE") != null && criteriaMap.get("FROM_DATE")==null) {
			dynQuery = dynQuery + " and mbh.mbDate <= ? ";
			paramList.add(criteriaMap.get("TO_DATE"));
		}else if(criteriaMap.get("FROM_DATE") != null && criteriaMap.get("TO_DATE")!=null) {
			dynQuery = dynQuery + " and mbh.mbDate between ? and ? ";
			paramList.add(criteriaMap.get("FROM_DATE"));
			paramList.add(criteriaMap.get("TO_DATE"));
		}
		
		
		if(criteriaMap.get("MB_REF_NO") != null) {
			dynQuery = dynQuery + " and mbh.mbRefNo like '%"
			+ criteriaMap.get("MB_REF_NO")
			+ "%'";
		}
		if("1".equals(criteriaMap.get("ALL_STATUS"))){
			dynQuery = dynQuery + " and mbh.mbBills is not null"; 
		}
		else{
			if(!"-1".equals(criteriaMap.get("STATUS")) && criteriaMap.get("STATUS") != null && (criteriaMap.get("STATUS").equals(MBHeader.MeasurementBookStatus.APPROVED.toString()) || criteriaMap.get("STATUS").equals(MBHeader.MeasurementBookStatus.CANCELLED.toString()))) {
				dynQuery = dynQuery + " and mbh.mbDate.state.previous.value = ?";
				paramList.add(criteriaMap.get("STATUS"));
			}
			else if(!"-1".equals(criteriaMap.get("STATUS")) && criteriaMap.get("STATUS") != null){
				dynQuery = dynQuery + " and mbh.mbDate.state.value = ?"; 
				paramList.add(criteriaMap.get("STATUS"));
			}
		}
						
		String mbSearchQuery="select distinct mbh "+	dynQuery;
		String countQuery = "select distinct count(mbh) " + dynQuery;
		return new SearchQueryHQL(mbSearchQuery, countQuery, paramList);
	}

	public List<MBMeasurementSheet> getMbMeasurementSheetList() {
		return mbMeasurementSheetList;
	}

	public void setMbMeasurementSheetList(
			List<MBMeasurementSheet> mbMeasurementSheetList) {
		this.mbMeasurementSheetList = mbMeasurementSheetList;
	}

	public String getWoActivityId() {
		return woActivityId;
	}

	public void setWoActivityId(String woActivityId) {
		this.woActivityId = woActivityId;
	}

	public String getMbDtlsRecId() {
		return mbDtlsRecId;
	}

	public void setMbDtlsRecId(String mbDtlsRecId) {
		this.mbDtlsRecId = mbDtlsRecId;
	}
	
	
	/**
	 * print PDF
	 * @throws JRException,Exception 
	 */
	@SkipValidation
	public String viewMBMeasurementSheetPdf() throws JRException,Exception{
		Map<String,Object> reportParams = new HashMap<String,Object>();
		MBHeader mbHeader = measurementBookService.findById(id, false);
		Integer isMbDetails=0;
		List<MBMeasurementSheet> mbDetails=getMeasurementSheetForMB(mbHeader.getMbDetails());
		List<MBMeasurementSheet> reMbDetails=getRevisedMeasurementSheetForMB(mbHeader.getMbDetails());
		reportParams.put("reMbDetails", reMbDetails);

		if((mbDetails != null) && mbDetails.size()!=0){
			isMbDetails=1;
		}else{
			isMbDetails=0;
		}

		reportParams.put("isMbDetails",isMbDetails);

		if(mbHeader.getRevisionEstimate()!=null){
			reportParams.put("revisedEstNo", mbHeader.getRevisionEstimate().getEstimateNumber());
		}
		boolean isTenderPercApplicable=(isTenderPercentageType==true?mbPercentagelevel.equalsIgnoreCase(WorksConstants.MBPERCENTCONFIGVALUE)?true:false:false);
		
		reportParams.put("isTenderPercApplicable", isTenderPercApplicable);
		reportParams.put("tenderPercentage", tenderPercentage.divide(new BigDecimal(100)));
		
		ReportRequest reportRequest = new ReportRequest("MBMeasurementSheet", mbDetails, reportParams); 
		ReportOutput reportOutput = reportService.createReport(reportRequest);  
	    if(reportOutput!=null && reportOutput.getReportOutputData()!=null)
   		   pdfInputStream = new ByteArrayInputStream(reportOutput.getReportOutputData()); 
		
		return PRINT; 
	}
 
	public List<MBMeasurementSheet> getMeasurementSheetForMB(List<MBDetails> mbDetailsLst){
		ArrayList<MBMeasurementSheet> finalList = new ArrayList<MBMeasurementSheet>();
		for(MBDetails mbDetails : mbDetailsLst)	{
			RevisionType revisionType=null;
			if(mbDetails.getWorkOrderActivity()!=null){
				revisionType=mbDetails.getWorkOrderActivity().getActivity().getRevisionType();
			}
			if(revisionType==null){
			if(mbDetails.getMbMeasurementSheetList().size()!= 0){
			   for(MBMeasurementSheet  mbmSheet : mbDetails.getMbMeasurementSheetList()){
				  mbmSheet.setMbMSheetTotalQuantity(mbmSheet.calculateQuantity());
			      finalList.add(mbmSheet);
		       }
			}
			else{
				  finalList.add(addMBDetails(mbDetails)); 	
			}
		}
		}
		return finalList;
	}
	
	public List<MBMeasurementSheet> getRevisedMeasurementSheetForMB(List<MBDetails> mbDetailsLst){
		ArrayList<MBMeasurementSheet> finalList = new ArrayList<MBMeasurementSheet>();
		for(MBDetails mbDetails : mbDetailsLst)	{
			RevisionType revisionType=null;
			if(mbDetails.getWorkOrderActivity()!=null){
				revisionType=mbDetails.getWorkOrderActivity().getActivity().getRevisionType();
			}
			if(revisionType!=null){
			if(mbDetails.getMbMeasurementSheetList().size()!= 0){
			   for(MBMeasurementSheet  mbmSheet : mbDetails.getMbMeasurementSheetList()){
				  mbmSheet.setMbMSheetTotalQuantity(mbmSheet.calculateQuantity());
			      finalList.add(mbmSheet);
		       }
			}
			else{
				  finalList.add(addMBDetails(mbDetails)); 	
			}
		}
		}
		return finalList;
	}
	
	public MBMeasurementSheet addMBDetails(MBDetails mbDetails){
		MBMeasurementSheet mbSheet=new MBMeasurementSheet();
		mbSheet.setNo(null);
		mbSheet.setUomLength(0);
		mbSheet.setWidth(0);
		mbSheet.setDepthOrHeight(0); 
		mbSheet.setMbDetails(mbDetails);
		return mbSheet;  
	}
	
	public Map createHeaderParams(MBHeader mbHeader, String type){
		Map<String,Object> reportParams = new HashMap<String,Object>();
		return reportParams; 
	}

	public InputStream getPdfInputStream() {
		return pdfInputStream;
	}

	public void setPdfInputStream(InputStream pdfInputStream) {
		this.pdfInputStream = pdfInputStream;
	}
	
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public String getMbAppConfigValue() {
		return  worksService.getWorksConfigValue("MB_CANT_CREATE_AFTER_COMPLETION_OF_WORK_DATE");
	}

	protected String getAdditionalRule() {
		if(mbHeader!=null && mbHeader.getId()!=null && mbHeader.getWorkOrderEstimate().getEstimate().getIsSpillOverWorks()){
			return "spillOverWorks";
		}else if(workOrderEstimateList!=null && workOrderEstimateList.size()!=0 && workOrderEstimateList.get(0).getEstimate().getIsSpillOverWorks()){
						return "spillOverWorks";
			
		}else if(workOrderEstimateList!=null && workOrderEstimateList.size()!=0 && !workOrderEstimateList.get(0).getEstimate().getIsSpillOverWorks() && workOrderEstimateList.get(0).getEstimate().getExecutingDepartment()!=null && PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(workOrderEstimateList.get(0).getEstimate().getExecutingDepartment().getDeptName())) {
						List functionCodes = getFunctionCodes();
						if(functionCodes!=null && !functionCodes.isEmpty() && functionCodes.contains(mbHeader.getWorkOrderEstimate().getEstimate().getFinancialDetails().get(0).getFunction().getCode())){				
							additionalRuleValue="ZonalPublicWork";
							mbHeader.setAdditionalWfRule(additionalRuleValue);
							
				}
						else{ 
							additionalRuleValue="HQPublicWork";
							mbHeader.setAdditionalWfRule(additionalRuleValue);
						}
					 
						return additionalRuleValue;
		}else if(mbHeader.getWorkOrderEstimate().getEstimate().getExecutingDepartment()!=null && PUBLIC_WORKS_DEPARTMENT.equalsIgnoreCase(mbHeader.getWorkOrderEstimate().getEstimate().getExecutingDepartment().getDeptName())){
						List functionCodes = getFunctionCodes();
						if(functionCodes!=null && !functionCodes.isEmpty() && functionCodes.contains(mbHeader.getWorkOrderEstimate().getEstimate().getFinancialDetails().get(0).getFunction().getCode())){				
							additionalRuleValue="ZonalPublicWork";
							mbHeader.setAdditionalWfRule(additionalRuleValue);
				}else{ 
						additionalRuleValue="HQPublicWork";
						mbHeader.setAdditionalWfRule(additionalRuleValue);
						}
					 return additionalRuleValue;
			}
	
		 return null;
}

	public List getFunctionCodes() {
		List<AppConfigValues> appConfigList = worksService.getAppConfigValue(
				"Works", "WORKS_PWD_FUNCTIONWISE_WF");
		List functionCodes = new LinkedList();
		if (appConfigList != null && !appConfigList.isEmpty()) {
			if (appConfigList.get(0).getValue() != ""
					&& appConfigList.get(0).getValue() != null) {
				String[] configVals = appConfigList.get(0).getValue()
						.split(",");
				for (int i = 0; i < configVals.length; i++)
					functionCodes.add(configVals[i]);
			}
		}
		return functionCodes;
	}
	public String getWorkCompletionDate(){
		String workCompletionDate = null;
		if(workOrderId!=null) {
			try{			
				WorkOrder workOrder = workOrderService.findById(workOrderId, false);			
			Date completionDate = workOrder.getWorkCompletionDate();
			logger.debug("-------- work completion Date in Date object: --------:"+ completionDate);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			if(completionDate!=null)
			   workCompletionDate = sdf.format(completionDate);
		}catch(Exception e){
			logger.error("Date parsing exception", e);
		}
		logger.debug("-------- work completion Date--------Sting object:"+ workCompletionDate);
		}
		return workCompletionDate;
	}

	public AbstractEstimateService getAbstractEstimateService() {
		return abstractEstimateService;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public EisUtilService getEisService() {
		return eisService;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}
	
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public String getPendingActions()
	{
		return mbHeader==null?"":
			(mbHeader.getCurrentState()==null?"":mbHeader.getCurrentState().getNextAction());
	}

	public String getWorkFlowDepartment(){
		return mbHeader==null?"":(mbHeader.getWorkOrderEstimate()==null?"":(mbHeader.getWorkOrderEstimate().getEstimate()==null?"":(mbHeader.getWorkOrderEstimate().getEstimate().getIsSpillOverWorks()?"":(mbHeader.getWorkOrderEstimate().getEstimate().getExecutingDepartment()==null?"":mbHeader.getWorkOrderEstimate().getEstimate().getExecutingDepartment().getDeptName()))));
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public List<Activity> getSorActivities() {
		return sorActivities;
	}

	public void setSorActivities(List<Activity> sorActivities) {
		this.sorActivities = sorActivities;
	}

	public List<Activity> getNonSorActivities() {
		return nonSorActivities;
	}

	public void setNonSorActivities(List<Activity> nonSorActivities) {
		this.nonSorActivities = nonSorActivities;
	}

	public void setNonSorService(PersistenceService<NonSor, Long> nonSorService) {
		this.nonSorService = nonSorService;
	}

	public void setRevisionAbstractEstimateService(
			PersistenceService<RevisionAbstractEstimate, Long> revisionAbstractEstimateService) {
		this.revisionAbstractEstimateService = revisionAbstractEstimateService;
	}

	public void setIsSpillOverWorks(boolean isSpillOverWorks) {
		this.isSpillOverWorks = isSpillOverWorks;
}

	public boolean getIsSpillOverWorks() {
		return isSpillOverWorks;
	}

	public String getActivitySearchType() {
		return activitySearchType;
	}

	public void setActivitySearchType(String activitySearchType) {
		this.activitySearchType = activitySearchType;
	}

	public List<WorkOrderActivity> getExtraItemsActivities() {
		return extraItemsActivities;
	}

	public void setExtraItemsActivities(List<WorkOrderActivity> extraItemsActivities) {
		this.extraItemsActivities = extraItemsActivities;
	}

	public List<MeasurementSheet> getMeasurementSheetList() {
		return measurementSheetList;
	}

	public void setMeasurementSheetList(List<MeasurementSheet> measurementSheetList) {
		this.measurementSheetList = measurementSheetList;
	}

	public String getActivityUOM() {
		return activityUOM;
	}

	public void setActivityUOM(String activityUOM) {
		this.activityUOM = activityUOM;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public Double getTotalMBExtraMSheetQty() {
		return totalMBExtraMSheetQty; 
	}

	public void setTotalMBExtraMSheetQty(Double totalMBExtraMSheetQty) {
		this.totalMBExtraMSheetQty = totalMBExtraMSheetQty;
	}

	public List<WorkOrderMeasurementSheet> getChangeQuantityMSheetList() {
		return changeQuantityMSheetList;
	}

	public void setChangeQuantityMSheetList(
			List<WorkOrderMeasurementSheet> changeQuantityMSheetList) {
		this.changeQuantityMSheetList = changeQuantityMSheetList;
	}

	public String getMbPercentagelevel() {
		return mbPercentagelevel;
	}

	public void setMbPercentagelevel(String mbPercentagelevel) {
		this.mbPercentagelevel = mbPercentagelevel;
	}

	public boolean getIsTenderPercentageType() {
		return isTenderPercentageType;
	}

	public void setIsTenderPercentageType(boolean isTenderPercentageType) {
		this.isTenderPercentageType = isTenderPercentageType;
	}

	public void setGenericTenderService(GenericTenderService genericTenderService) {
		this.genericTenderService = genericTenderService;
	}

	public BigDecimal getTenderPercentage() {
		return tenderPercentage;
	}

	public void setTenderPercentage(BigDecimal tenderPercentage) {
		this.tenderPercentage = tenderPercentage;
	}

	public String getMbAmtAboveApprovedAmt() {
		return mbAmtAboveApprovedAmt;
	}

	public void setMbAmtAboveApprovedAmt(String mbAmtAboveApprovedAmt) {
		this.mbAmtAboveApprovedAmt = mbAmtAboveApprovedAmt;
	}

	public boolean getIsZeroQty() {
		return isZeroQty;
	}

	public void setZeroQty(boolean isZeroQty) {
		this.isZeroQty = isZeroQty;
	}
	
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public Long getMbId() {
		return mbId;
	}

	public void setMbId(Long mbId) {
		this.mbId = mbId;
	}

	public String getEstimateNo() {
		return estimateNo;
	}

	public void setEstimateNo(String estimateNo) {
		this.estimateNo = estimateNo;
	}

	public String getCancelRemarks() {
		return cancelRemarks;
	}

	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}

	public String getPreparedByTF() {
		return preparedByTF;
	}

	public void setPreparedByTF(String preparedByTF) {
		this.preparedByTF = preparedByTF;
	}

}