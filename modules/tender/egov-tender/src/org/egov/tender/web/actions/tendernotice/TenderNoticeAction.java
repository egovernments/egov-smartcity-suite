package org.egov.tender.web.actions.tendernotice;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.ContractorGrade;
import org.egov.commons.EgwStatus;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.infstr.models.State;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.user.User;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.tender.interfaces.TenderFile;
import org.egov.tender.interfaces.Tenderable;
import org.egov.tender.interfaces.TenderableGroup;
import org.egov.tender.model.TenderFileType;
import org.egov.tender.model.TenderNotice;
import org.egov.tender.model.TenderUnit;
import org.egov.tender.model.TenderableEntity;
import org.egov.tender.model.TenderableEntityComparator;
import org.egov.tender.model.TenderableEntityGroup;
import org.egov.tender.services.common.GenericTenderService;
import org.egov.tender.services.common.TenderCommonService;
import org.egov.tender.services.tendernotice.TenderNoticeService;
import org.egov.tender.utils.TenderConstants;
import org.egov.tender.web.actions.common.TenderWorkFlowAction;


/**
 * 
 * @author pritiranjan
 *
 */

@SuppressWarnings("serial")
@ParentPackage("egov") 
public class TenderNoticeAction extends TenderWorkFlowAction{
	
	private TenderNotice tenderNotice= new TenderNotice();
	private static final Logger LOGGER = Logger.getLogger(TenderNoticeAction.class);
	private final static String CREATE="create";
	private final SimpleDateFormat FORMATTER = new SimpleDateFormat(TenderConstants.DATEPATTERN,Locale.getDefault());
	private WorkflowService<TenderNotice> tenderNoticeWorkflowService;
	private GenericTenderService genericTenderService;
	private List<TenderUnit> tenderUnitDetailsList=new ArrayList<TenderUnit>();
	private List<TenderableEntityGroup> tenderableGroupsList=new ArrayList<TenderableEntityGroup>();
	private List<TenderableEntity> tenderEntitiesList=new LinkedList<TenderableEntity>();
	private HashMap<String,Object> tenderNoticeMapObject = null;
	private Long tenderFileId; 
	private String fileType;
	private String tenderDateTemp;
	private TenderNoticeService  tenderNoticeService;
	private boolean autoGenerateNumberFlag;
	private TenderFile tenderFile=null;
	private String linkSource;
	private ReportService reportService;
	private Integer reportId = -1;
	private TenderCommonService tenderCommonService;
	private EmployeeService employeeService;
	private Long noticeId;
	private String cancelRemarks;
	private String noticeNumber;
	private String messageKey;
	//private Map<String,List<ContractorGrade>> contractorGradeActualMap=new HashMap<String,List<ContractorGrade>>();
	
	


	

	/*public Map<String, List<ContractorGrade>> getContractorGradeActualMap() {
		return contractorGradeActualMap;
	}


	public void setContractorGradeActualMap(
			Map<String, List<ContractorGrade>> contractorGradeActualMap) {
		this.contractorGradeActualMap = contractorGradeActualMap;
	}
*/

	public TenderNoticeAction()
	{
		addRelatedEntity("department", DepartmentImpl.class);
		addRelatedEntity("state", State.class);
		addRelatedEntity("createdBy", User.class);
		addRelatedEntity("modifiedBy", User.class);
		addRelatedEntity("TenderUnit",TenderUnit.class);
		addRelatedEntity("tenderUnits.tenderableGroups",TenderableEntityGroup.class);
		addRelatedEntity("tenderFileType",TenderFileType.class);
		addRelatedEntity("requestedUOM",EgUom.class);
		addRelatedEntity("tenderUnit",TenderUnit.class);
		addRelatedEntity("status",EgwStatus.class); 
		addRelatedEntity("classOfContractor",ContractorGrade.class);
	}

	
	public TenderNotice getModel()
	{
		return tenderNotice;
	}
	
	public void prepare()
	{
		
		if(idTemp!=null && mode != null && !VIEW.equals(mode)){
			tenderNotice=tenderNoticeService.getTenderNoticeById(idTemp);
			tenderNotice=tenderNoticeService.merge(tenderNotice); 
		}
		else if (idTemp!=null && !VIEW.equals(mode)){
			tenderNotice=tenderNoticeService.getTenderNoticeById(idTemp);
		}
		
		if(tenderNotice!=null && tenderNotice.getCurrentState()!=null  && mode != null && !VIEW.equals(mode) )
		{
			 List<String> wfNextValue=tenderCommonService.getFurtherWorkflowState(tenderNotice.getCurrentState().getValue(),TenderConstants.WFSTATUS_NEXT,tenderNotice);
				
			 if(!wfNextValue.isEmpty()){
				setWfStatus(wfNextValue.get(1));
			 }
		}
		super.prepare();
		autoGenerateNumberFlag=tenderCommonService.getAppconfigValue(TenderConstants.TENDER, TenderConstants.FLAGTOGENERATENOTICENUMBER, "0");
		//addDropdownData("contractorGradeList",persistenceService.findAllBy("from org.egov.commons.ContractorGrade order by grade"));
		addDropdownData("departmentIdList",persistenceService.findAllBy("from DepartmentImpl order by deptName"));
		
	}
	
	protected void checkDatevalues(Date date,String field,String count){
		try{
			Date toDays=new Date();
			
			if(date!= null && !"".equals(date) && date.before(FORMATTER.parse(FORMATTER.format(toDays)))){
					addFieldError(field,getMessage(field) +" "+ count);
				}
			
		}catch(Exception ex)
		{
			LOGGER.info("Exception is---->"+ex.getMessage());
		}
	}
	
	protected void checkDatevaluesAgainstNoticeDate(Date date,String field,String count){
		try{
			Date toDays= tenderNotice.getNoticeDate();
			
			if(date!= null && !"".equals(date) && date.before(FORMATTER.parse(FORMATTER.format(toDays)))){
					addFieldError(field,getMessage(field) +" "+ count);
				}
			
		}catch(Exception ex)
		{
			LOGGER.info("Exception is---->"+ex.getMessage());
		}
	}
	
	public void validate()
	{
		LOGGER.info("..start validate method.....");
		int count=1;
		
		if(tenderNotice.getNoticeDate()==null)
			addFieldError("noticeDate",getMessage("noticeDate.null"));
		
		else if(CREATE.equals(mode))
			checkDatevalues( tenderNotice.getNoticeDate(),"noticeDate.NotGreaterThanCurrentDate","");
		
		
		if(!autoGenerateNumberFlag && ( tenderNotice.getNumber()==null || "".equals(tenderNotice.getNumber().trim())))
			addFieldError("noticeNumber",getMessage("noticeNumber.null"));
		
		if(CREATE.equals(mode) && !autoGenerateNumberFlag && tenderNotice.getNumber()!=null && !"".equals(tenderNotice.getNumber().trim()))
			{
			Integer totalExisingCount=tenderNoticeService.getTenderNoticeByNumber(tenderNotice.getNumber());
			if(totalExisingCount>0)
				{
					addFieldError("noticeNumberAlreadyExist",getMessage("noticeNumber.alreadyExist"));
				}
			}
		if(tenderUnitDetailsList!=null){
			
			for(TenderUnit tenderUnit: tenderUnitDetailsList) {
	
				if(tenderUnit!=null){
					if(tenderUnit.getBidMeetingDate()==null)
						addFieldError("bidmeetingDate", getMessage("bidMeeting.null.action")+" "+count);
					else
						//checkDatevalues( tenderUnit.getBidMeetingDate(),"bidmeetingDate.NotGreaterThanCurrentDate",String.valueOf(count));
						checkDatevaluesAgainstNoticeDate( tenderUnit.getBidMeetingDate(),"bidmeetingDate.NotGreaterThanNoticeDate",String.valueOf(count));
					
					if(tenderUnit.getDateOfOpeningOfEtender()==null)
						addFieldError("tenderopeningDate", getMessage("tenderOpeningDate.null.action")+" "+count);
					else
						//checkDatevalues( tenderUnit.getDateOfOpeningOfEtender(),"tenderOpeningDate.NotGreaterThanCurrentDate",String.valueOf(count));
						checkDatevaluesAgainstNoticeDate( tenderUnit.getDateOfOpeningOfEtender(),"tenderOpeningDate.NotGreaterThanNoticeDate",String.valueOf(count));
					 
					if(tenderUnit.getDateofSale()==null)
						addFieldError("saleDate", getMessage("saleDate.null.action")+" "+count);
					else
						//checkDatevalues( tenderUnit.getDateofSale(),"saleDate.NotGreaterThanCurrentDate",String.valueOf(count));
						checkDatevaluesAgainstNoticeDate(tenderUnit.getDateofSale(),"saleDate.NotGreaterThanNoticeDate",String.valueOf(count));
					
					if(tenderUnit.getDateofSubmission()==null)
						addFieldError("submissionDate", getMessage("submissionDate.null.action")+" "+count);
					else
						//checkDatevalues( tenderUnit.getDateofSubmission(),"submissionDate.NotGreaterThanCurrentDate",String.valueOf(count));
						checkDatevaluesAgainstNoticeDate( tenderUnit.getDateofSubmission(),"submissionDate.NotGreaterThanNoticeDate",String.valueOf(count));
					
					if(tenderNotice.getTenderFileType()!=null && tenderNotice.getTenderFileType().getGroupType()!=null 
							&& tenderNotice.getTenderFileType().getGroupType().equals(TenderConstants.FILETYPEESTIMATE) && ( tenderUnit.getTimeLimit()==null || tenderUnit.getTimeLimit().equals("")) )
					{
						addFieldError("timeLimit", getMessage("timeLimit.null.action")+" "+count);
					}
				}
				tenderUnit.setContractorGradeList((tenderNoticeService.getContractorsbyCost(tenderUnit.getEstimatedCost())));
				displaytenderfile();
				count++;
			}
							
			
			
		}
	}
	
	public String create()
	{
		Position approver = null;
		approver=getPosition();
		tenderNotice.setPosition(approver);
		if(tenderNotice.getCurrentState()!=null)
			  tenderNotice.getCurrentState().setText2(comments);
		
		tenderNotice=tenderNoticeService.save(tenderNotice,tenderUnitDetailsList,tenderableGroupsList,tenderEntitiesList, workFlowType,autoGenerateNumberFlag,approver);
		
		setMode(VIEW);
		displaytenderfile();
		for(TenderUnit tenderUnit:tenderNotice.getTenderUnits())
		{						
			tenderUnit.setContractorGradeList((tenderNoticeService.getContractorsbyCost(tenderUnit.getEstimatedCost())));
		}
		return NEW;
	}
	
	
	public void displaytenderfile(){
		
		if(tenderNotice.getTenderFileType()!=null){
			tenderFile=genericTenderService.getTenderFileByFiletypeAndNumber(tenderNotice.getTenderFileType(),tenderNotice.getTenderFileRefNumber());
			linkSource= tenderNotice.getTenderFileType().getLinkSource();
		}
		
	}
	
	@SkipValidation
	public String loadTenderFileToCreateNotice()
	{
		/**
		 * Get TenderFile by passing tenderfile type and TenderFileId  
		 */
		tenderFile= tenderNoticeService.getAssociatedService(fileType).getTenderFileById(tenderFileId);
		TenderFileType fileTypetemp=(TenderFileType)persistenceService.find("from TenderFileType where fileType=?",fileType);
		if(fileTypetemp!=null)
		linkSource=fileTypetemp.getLinkSource();
		buildTenderNoticeObjectByPassingTenderFile();
		setMode(CREATE);
		return NEW;
	}
	
	private Boolean checkTenderEntityMappedWithGroup(Set<TenderUnit> tenderUnitDetailsList)
	{

		for(TenderUnit unit:tenderUnitDetailsList)
		{
			for (TenderableEntity  tenderEntity:unit.getTenderEntities())
			{
				if(tenderEntity.getTenderableEntityGroup()==null)
				{	
					return true;
				}
			}
		}
		return false;

	}
	
	@SkipValidation
	public String view() 
	{

		tenderUnitDetailsList.clear();
		tenderableGroupsList.clear();
		tenderEntitiesList.clear();
		
		tenderNotice=tenderNoticeService.getTenderNoticeById(idTemp);
		displaytenderfile();
		if(tenderNotice!=null){
			tenderNotice.setCombineTenderableGroups(checkTenderEntityMappedWithGroup(tenderNotice.getTenderUnits()));
			
			for(TenderUnit tenderUnit:tenderNotice.getTenderUnits())
			{
				tenderUnit.setContractorGradeList((tenderNoticeService.getContractorsbyCost(tenderUnit.getEstimatedCost())));	
				//tenderUnit.getId();
				//tenderUnit.getClassOfContractor().getId();

				//For LnE Tenderable groups will be empty......
				if(tenderUnit.getTenderableGroups().isEmpty()){
					for(TenderableEntity tenderableEntity:tenderUnit.getTenderEntities())
					{
						tenderUnit.setTenderGroupRefNumber(tenderUnit.getTenderUnitNumber());

						tenderEntitiesList.add(tenderableEntity);
					}
				}
				
				else
				{
					for(TenderableEntityGroup tenderGroup:tenderUnit.getTenderableGroups()){

						tenderableGroupsList.add(tenderGroup);
						tenderUnit.setTenderGroupRefNumber(tenderUnit.getTenderUnitNumber());

						/**
						 * If entities are not mapped with groups.
						 */
						if(tenderGroup.getEntities().isEmpty() && tenderEntitiesList.isEmpty())
						{
							for(TenderableEntity tenderableEntity:tenderUnit.getTenderEntities())
							{
							//	tenderUnit.setTenderGroupRefNumber(tenderUnit.getTenderUnitNumber());

								tenderEntitiesList.add(tenderableEntity);
							}

						}
						else if(!tenderGroup.getEntities().isEmpty())
						{
							for(TenderableEntity tendrable:tenderGroup.getEntities())
							{
								//tendrable.setTempEntityGroupName(tenderGroup.getNumber());
								tenderEntitiesList.add(tendrable);
							}
						}
					}
				}
				tenderUnitDetailsList.add(tenderUnit);

			}
			
		}
		if(tenderNotice.getCurrentState()!=null  && !VIEW.equals(mode) )
		{
			
			 List<String> wfNextValue=tenderCommonService.getFurtherWorkflowState(tenderNotice.getCurrentState().getValue(),TenderConstants.WFSTATUS_NEXT,tenderNotice);
				
			 if(!wfNextValue.isEmpty()){
				setWfStatus(wfNextValue.get(1));
			
			 }
		
		}
		if(tenderNotice.getCurrentState()!=null && tenderNotice.getCurrentState().getPrevious()!=null)
		{
			 setComments( tenderNotice.getCurrentState().getPrevious().getText2());
		}
		Collections.sort(tenderEntitiesList,new TenderableEntityComparator());
		if(VIEW.equals(mode) )
			setMode(VIEW);
		else
			setMode(tenderCommonService.isModifyableByLoginUser(tenderNotice)?MODIFY:NOTMODIFY);
		return NEW;
	}
	
	@SkipValidation
	public String print(){
		tenderNoticeMapObject = new HashMap<String,Object>();
		String fileName=TenderConstants.TENDERNOTICEFILENAMEFORSTORESMODULE;
		if(idTemp!=null)
			tenderNotice=tenderNoticeService.getTenderNoticeById(idTemp);
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		
		tenderNoticeMapObject=buildTenderNoticeMap(request, session);
		
		if(tenderNotice.getTenderFileType()!=null && (tenderNotice.getTenderFileType().getGroupType().equals(TenderConstants.FILETYPEITEM_INDENT)
				|| tenderNotice.getTenderFileType().getGroupType().equals(TenderConstants.FILETYPERATECONTRACT_INDENT)))
			fileName=TenderConstants.TENDERNOTICEFILENAMEFORSTORESMODULE;
		else if (tenderNotice.getTenderFileType()!=null && (tenderNotice.getTenderFileType().getGroupType().equals(TenderConstants.FILETYPEWORKS_RC_INDENT)
				|| tenderNotice.getTenderFileType().getGroupType().equals(TenderConstants.FILETYPEESTIMATE)))
			fileName=TenderConstants.TENDERNOTICEFILENAMEFORWORKSSMODULE;
		else
			fileName=TenderConstants.TENDERNOTICEFILENAMEFORLESMODULE;
		
		
		ReportRequest reportInput = new ReportRequest(fileName,tenderNotice.getTenderUnits(),tenderNoticeMapObject);
		ReportOutput reportOutput = reportService.createReport(reportInput);
		
		reportId = ReportViewerUtil.addReportToSession(reportOutput,getSession());
		return "report";
	}


	private HashMap<String, Object> buildTenderNoticeMap(HttpServletRequest request,
			HttpSession session) {
		//TODO: ADD WORKFLOW DATA ALSO.
		tenderNoticeMapObject.put("NAMEOFTHECITY",session.getAttribute("cityname"));
		tenderNoticeMapObject.put("logoPath",getTenderCommonService().getCityLogoName(request));
		tenderNoticeMapObject.put("TENDERNOTICENUMBER",tenderNotice.getNumber());
		tenderNoticeMapObject.put("TENDERNOTICEDATE",FORMATTER.format(tenderNotice.getNoticeDate()));
		
		tenderNoticeMapObject.put("TENDERFILENUMBER",tenderNotice.getTenderFileRefNumber());
		if(tenderNotice.getDepartment()!=null){
			tenderNoticeMapObject.put("HEADOFDEPARTMENTNAME",getTenderCommonService().getNameOfHeadOfDepartmentByPassingDeptId(tenderNotice.getDepartment().getId()));
			tenderNoticeMapObject.put("DEPARTMENTNAME",tenderNotice.getDepartment().getDeptName());
		}
		return  tenderNoticeMapObject;
	}

	private void buildTenderNoticeObjectByPassingTenderFile() 
	{
		Integer index = 0;
		if(tenderFile!=null)
		{
			//String Lists="lists";
			//tenderNotice.setNoticeDate(tenderFile.getTenderDate());
			setTenderDateTemp(FORMATTER.format(tenderFile.getTenderDate()));
			tenderNotice.setTenderFileRefNumber(tenderFile.getFileNumber());
			LOGGER.info("tenderFile number" + tenderFile.getFileNumber());
			tenderNotice.setDepartment(tenderFile.getDepartment());
			Boolean entityAlreadyExist=false;
			tenderNotice.setTenderFileType((TenderFileType)persistenceService.find("from TenderFileType where fileType=?",fileType));
			tenderNotice.setDepartment(tenderFile.getDepartment());

			//Used default or null value as False to combine tenderable groups.
			tenderNotice.setCombineTenderableGroups((tenderFile.combineTenderableGroups()!=null?tenderFile.combineTenderableGroups():Boolean.FALSE));

			/**
			 *  For each tender group, we need to create a tenderunit. It depends on client setup. For coc, there is only one unit and 
			 *  all tender groups are combined together. For nmc, for each tender group we need to create seperate tender unit.
			 *  In case of land estate module, there is no tender groups.
			 */

			//TODO: IF TENDERGROUP IS NULL , THEN USE ENTITIES DIRECTLY.

			if(tenderFile.combineTenderableGroups()!=null && !tenderFile.combineTenderableGroups() && tenderFile.getTenderGroups()!=null  && !tenderFile.getTenderGroups().isEmpty()){
				//int i=0;
				for(TenderableGroup tenderGrp: tenderFile.getTenderGroups())
				{
					TenderUnit tenderUnit= new TenderUnit();
					TenderableEntityGroup entityGrp= new TenderableEntityGroup();
					/*Set<TenderableEntityGroup> tenderableGroups= new HashSet<TenderableEntityGroup>();
				  Set<TenderableEntity> tenderEntities = new HashSet<TenderableEntity>();
					 */ 
					tenderUnit.setTenderGroupNarration(tenderGrp.getDescription());
					tenderUnit.setTenderGroupRefNumber(tenderGrp.getNumber());
					//tenderUnit.setEstimatedCost(tenderGrp.getEstimatedCost());

					entityGrp.setTenderUnitRefNumber(tenderGrp.getNumber());

					entityGrp.setNumber(tenderGrp.getNumber());
					entityGrp.setTenderableGroupType(tenderGrp.getTenderableGroupType());
					entityGrp.setTenderUnit(tenderUnit);
					//entityGrp.setEstimatedCost(tenderGrp.getEstimatedCost());
					//TODO:check
					entityGrp.setModifiedDate(new Date());

					for(Tenderable tendrable:tenderGrp.getEntities())
					{
					       
						TenderableEntity tenderEntity=new TenderableEntity();
						tenderEntity.setDescription(tendrable.getDescription());
						System.out.println("Tenderable Description>>>>>"+tenderEntity.getDescription());
						tenderEntity.setName(tendrable.getName());
						tenderEntity.setNumber(tendrable.getNumber());
						tenderEntity.setRequestedByDate(tendrable.getRequestedByDate());
						tenderEntity.setRequestedQty(tendrable.getRequestedQty());
						tenderEntity.setRequestedUOM(tendrable.getRequestedUOM());
						tenderEntity.setRequestedValue(tendrable.getRequestedValue());
						tenderEntity.setTenderableType(tendrable.getTenderableType());

						//check this
						tenderEntity.setId(null);
						tenderEntity.setTenderableEntityGroup(entityGrp);

						//EntityGroup number added to identify group in  entity.
						tenderEntity.setTempEntityGroupName(entityGrp.getNumber());
						tenderEntity.setIndexNo(Long.valueOf(index));

						//	tenderEntity.setTenderUnit(tenderUnit);
						tenderEntitiesList.add(tenderEntity);
						index = index+1;
					}

					entityGrp.setEntities(new HashSet<TenderableEntity>(tenderEntitiesList));
					entityGrp.setDescription(tenderGrp.getDescription());



					tenderableGroupsList.add(entityGrp);
					tenderUnit.setTenderableGroups(new HashSet<TenderableEntityGroup>(tenderableGroupsList));
					tenderUnit.setEstimatedCost(tenderGrp.getEstimatedCost()!=null?tenderGrp.getEstimatedCost():BigDecimal.ZERO);
					tenderUnit.setEmd(tenderNoticeService.getEMDAmountbyCost(tenderUnit.getEstimatedCost()));
					tenderUnit.setFormCost(tenderNoticeService.getTenderCostAmount(tenderUnit.getEstimatedCost()));
					/*Map<String, String> contractorGradeTemp=new HashMap<String, String>();
					
					for(ContractorGrade contractors:(tenderNoticeService.getContractorsbyCost(tenderUnit.getEstimatedCost()))){
						contractorGradeTemp.put(contractors.getId().toString(),contractors.getDescription());
						}*/
					tenderUnit.setContractorGradeList((tenderNoticeService.getContractorsbyCost(tenderUnit.getEstimatedCost())));
					//contractorGradeActualMap.put(Lists+i, tenderUnit.getContractorGradeMap());
					tenderUnitDetailsList.add(tenderUnit);
					//i++;
				} 
				tenderNotice.setTenderUnits(new HashSet<TenderUnit>(tenderUnitDetailsList));
			}else if(tenderFile.getTenderGroups()!=null && !tenderFile.getTenderGroups().isEmpty())
			{
				/**
				 * If combineTenderableGroups flag is true, then combine multiple tenderable group into a single unit.
				 * The tender file description and file number used as  description and tender reference number in tender notice line level.
				 * When tender groups are combined, if items are common in both tender group, then show total quantity in UI.
				 */
				TenderUnit tenderUnit= new TenderUnit();
				BigDecimal estimatedCost=BigDecimal.ZERO;
				tenderUnit.setTenderGroupNarration(tenderFile.getDescription());
				tenderUnit.setTenderGroupRefNumber(tenderFile.getFileNumber());

				for(TenderableGroup tenderGrp: tenderFile.getTenderGroups())
				{
					TenderableEntityGroup entityGrp= new TenderableEntityGroup();

					entityGrp.setTenderUnitRefNumber(tenderGrp.getNumber());
					entityGrp.setNumber(tenderGrp.getNumber());
					entityGrp.setTenderableGroupType(tenderGrp.getTenderableGroupType());
					entityGrp.setTenderUnit(tenderUnit);
					entityGrp.setEstimatedCost(tenderGrp.getEstimatedCost());
					entityGrp.setModifiedDate(new Date());
					entityGrp.setDescription(tenderGrp.getDescription());

					estimatedCost=estimatedCost.add(tenderGrp.getEstimatedCost()!=null?tenderGrp.getEstimatedCost():BigDecimal.ZERO);

					for(Tenderable tendrable:tenderGrp.getEntities())
					{
						if(!tenderEntitiesList.isEmpty())
						{
							for(TenderableEntity entity: tenderEntitiesList)
							{
								if(entity.getNumber().equalsIgnoreCase(tendrable.getNumber()))
								{
									entityAlreadyExist=true;
									//TODO: CHECK DATA IS COMING IN BASE CONVERSION ?

									if(tendrable.getRequestedQty()!=null)
										entity.setRequestedQty(entity.getRequestedQty().add(tendrable.getRequestedQty()));

									/**
									 * Assumption : here requested value is same for items which are in different indents.
									 */

									if(tendrable.getRequestedValue()!=null)
										entity.setRequestedValue(entity.getRequestedValue());

									//entity.setRequestedValue(entity.getRequestedValue().add(tendrable.getRequestedValue()));
								}
							}
						}

						if(!entityAlreadyExist){

							TenderableEntity tenderEntity = buildTenderableEntityObjectWithOutTenderGroup(tenderUnit, tendrable);
							tenderEntity.setIndexNo(Long.valueOf(index));
							tenderEntitiesList.add(tenderEntity);
							index = index+1;
							
						}else
							entityAlreadyExist=false;
					}

					tenderableGroupsList.add(entityGrp);
				}


				tenderUnit.setEstimatedCost(estimatedCost);
				tenderUnit.setEmd(tenderNoticeService.getEMDAmountbyCost(tenderUnit.getEstimatedCost()));
				tenderUnit.setFormCost(tenderNoticeService.getTenderCostAmount(tenderUnit.getEstimatedCost()));
				tenderUnit.setTenderableGroups(new HashSet<TenderableEntityGroup>(tenderableGroupsList));
				/*Map<String, String> contractorGradeTemp=new HashMap<String, String>();
				
				for(ContractorGrade contractors:(tenderNoticeService.getContractorsbyCost(tenderUnit.getEstimatedCost()))){
				contractorGradeTemp.put(contractors.getId().toString(),contractors.getDescription());
				}*/
				
				tenderUnit.setContractorGradeList(tenderNoticeService.getContractorsbyCost(tenderUnit.getEstimatedCost()));
				
				//contractorGradeActualMap.put("lists0", tenderUnit.getContractorGradeMap());
				tenderUnitDetailsList.add(tenderUnit);

				tenderNotice.setTenderUnits(new HashSet<TenderUnit>(tenderUnitDetailsList));
			}else
			{

				/**
				 * For land and estate module, tenderable group value is null. Here only entities are send by land estate module. 
				 * Here separate unit is created for each entity.
				 */


				for(Tenderable tendrable:tenderFile.getTenderEntities())
				{
					TenderUnit tenderUnit= new TenderUnit();
					tenderUnit.setTenderGroupNarration(tendrable.getDescription());
					tenderUnit.setTenderGroupRefNumber(tendrable.getNumber());
					tenderUnit.setEstimatedCost(tendrable.getRequestedValue());
					tenderUnit.setEmd(tenderNoticeService.getEMDAmountbyCost(tenderUnit.getEstimatedCost()));
					tenderUnit.setFormCost(tenderNoticeService.getTenderCostAmount(tenderUnit.getEstimatedCost()));
					TenderableEntity tenderEntity = buildTenderableEntityObjectWithOutTenderGroup(
							tenderUnit, tendrable);
					/*//Used this temporariry variable value to link unit and tender entity in UI.
						 	 tenderEntity.setTempEntityGroupName(tenderUnit.getTenderGroupRefNumber());
					 */	
					tenderEntity.setIndexNo(Long.valueOf(index));
					tenderEntitiesList.add(tenderEntity);
					index = index+1;
					tenderUnit.setTenderableGroups(new HashSet<TenderableEntityGroup>(tenderableGroupsList));
					tenderUnitDetailsList.add(tenderUnit);
				}


				tenderNotice.setTenderUnits(new HashSet<TenderUnit>(tenderUnitDetailsList));

			}
		}
	}

/**
 * Method used to build tenderableEntity Object. Here tender entity group is not mapped with entity.
 * @param tenderUnit
 * @param tendrable
 * @return
 */
	private TenderableEntity buildTenderableEntityObjectWithOutTenderGroup(TenderUnit tenderUnit,
			Tenderable tendrable) {
		TenderableEntity tenderEntity=new TenderableEntity();
		tenderEntity.setDescription(tendrable.getDescription());
		tenderEntity.setName(tendrable.getName());
		tenderEntity.setNumber(tendrable.getNumber());
		tenderEntity.setRequestedByDate(tendrable.getRequestedByDate());
		tenderEntity.setRequestedQty(tendrable.getRequestedQty());
		tenderEntity.setRequestedUOM(tendrable.getRequestedUOM());
		tenderEntity.setRequestedValue(tendrable.getRequestedValue());
		tenderEntity.setTenderableType(tendrable.getTenderableType());
		tenderEntity.setTenderUnit(tenderUnit);
		tenderEntity.setId(null);
		//Group is not required, as multiple indents or estimates are combined.
		tenderEntity.setTenderableEntityGroup(null);
		
		//EntityGroup number added to identify group in  entity.
		tenderEntity.setTempEntityGroupName(null);
		return tenderEntity;
	}
	
	@SkipValidation
	public String cancelApprovedTenderNotice() {  
		tenderNotice=tenderNoticeService.findById(noticeId, false);
		tenderNotice.getCurrentState().getPrevious().setValue("CANCELLED");
		PersonalInformation personinfo=employeeService.getEmpForUserId(Integer.valueOf(getLoggedInUserId()));
		String empName="";
		if(personinfo.getEmployeeFirstName()!=null)
			empName=personinfo.getEmployeeFirstName();
		if(personinfo.getEmployeeLastName()!=null)
			empName=empName.concat(" ").concat(personinfo.getEmployeeLastName());
		tenderNotice.getCurrentState().getPrevious().setText1(cancelRemarks+". Tender Notice Cancelled by: "+empName);
		tenderNotice.setStatus(tenderCommonService.getStatusByModuleAndCode(TenderConstants.TENDERNOTICE,TenderConstants.TENDERNOTICE_CANCELLED));
		noticeNumber=tenderNotice.getNumber();
		tenderNoticeService.persist(tenderNotice);
		messageKey=noticeNumber+": Tender Notice Cancelled successfully"; 
		return SUCCESS;
	}
	
	public int getLoggedInUserId() {
		return Integer.parseInt(EGOVThreadLocals.getUserId());
	}

	
	public boolean isAutoGenerateNumberFlag() {
		return autoGenerateNumberFlag;
	}


	public void setAutoGenerateNumberFlag(boolean autoGenerateNumberFlag) {
		this.autoGenerateNumberFlag = autoGenerateNumberFlag;
	}


	protected String getMessage(String key){
		return getText(key);
	}
	
	public List<TenderUnit> getTenderUnitDetailsList() {
		return tenderUnitDetailsList;
	}

	public void setTenderUnitDetailsList(List<TenderUnit> tenderUnitDetailsList) {
		this.tenderUnitDetailsList = tenderUnitDetailsList;
	}
	
	public Long getTenderFileId() {
		return tenderFileId;
	}


	public WorkflowService<TenderNotice> getTenderNoticeWorkflowService() {
		return tenderNoticeWorkflowService;
	}


	public void setTenderNoticeWorkflowService(
			WorkflowService<TenderNotice> tenderNoticeWorkflowService) {
		this.tenderNoticeWorkflowService = tenderNoticeWorkflowService;
	}


	public void setTenderFileId(Long tenderFileId) {
		this.tenderFileId = tenderFileId;
	}



	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getTenderDateTemp() {
		return tenderDateTemp;
	}

	public void setTenderDateTemp(String tenderDateTemp) {
		this.tenderDateTemp = tenderDateTemp;
	}

	public List<TenderableEntityGroup> getTenderableGroupsList() {
		return tenderableGroupsList;
	}

	public void setTenderableGroupsList(
			List<TenderableEntityGroup> tenderableGroupsList) {
		this.tenderableGroupsList = tenderableGroupsList;
	}

	public List<TenderableEntity> getTenderEntitiesList() {
		return tenderEntitiesList;
	}


	public void setTenderEntitiesList(List<TenderableEntity> tenderEntitiesList) {
		this.tenderEntitiesList = tenderEntitiesList;
	}

	public TenderNoticeService getTenderNoticeService() {
		return tenderNoticeService;
	}

	public void setTenderNoticeService(TenderNoticeService tenderNoticeService) {
		this.tenderNoticeService = tenderNoticeService;
	}

	public void setModel(TenderNotice notice)
	{
		this.tenderNotice=notice;
	}


	public ReportService getReportService() {
		return reportService;
	}


	public Integer getReportId() {
		return reportId;
	}


	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}


	public TenderCommonService getTenderCommonService() {
		return tenderCommonService;
	}


	public void setTenderCommonService(TenderCommonService tenderCommonService) {
		this.tenderCommonService = tenderCommonService;
	}


	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
	
	public TenderFile getTenderFile() {
		return tenderFile;
}

	public String getLinkSource() {
		return linkSource;
	}
	
	public GenericTenderService getGenericTenderService() {
		return genericTenderService;
	}

	public void setGenericTenderService(GenericTenderService genericTenderService) {
		this.genericTenderService = genericTenderService;
	}

	public Long getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

    public String getCancelRemarks() {
		return cancelRemarks;
	}

    public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}

    public String getNoticeNumber() {
		return noticeNumber;
	}

    public void setNoticeNumber(String noticeNumber) {
		this.noticeNumber = noticeNumber;
	}

    public String getMessageKey() {
		return messageKey;
	}

    public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
	
	
}
