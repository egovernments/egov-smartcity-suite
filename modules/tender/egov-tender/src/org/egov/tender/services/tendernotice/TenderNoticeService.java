package org.egov.tender.services.tendernotice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFinancialYear;
import org.egov.commons.ContractorGrade;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EisUtilService;
import org.egov.tender.TenderableGroupType;
import org.egov.tender.interfaces.TenderFile;
import org.egov.tender.interfaces.TenderableGroup;
import org.egov.tender.model.EmdMaster;
import org.egov.tender.model.TenderCostMaster;
import org.egov.tender.model.TenderFileType;
import org.egov.tender.model.TenderNotice;
import org.egov.tender.model.TenderUnit;
import org.egov.tender.model.TenderableEntity;
import org.egov.tender.model.TenderableEntityGroup;
import org.egov.tender.services.common.GenericTenderFileService;
import org.egov.tender.services.common.TenderCommonService;
import org.egov.tender.services.common.TenderFileService;
import org.egov.tender.utils.TenderConstants;
import org.egov.tender.utils.TenderUtils;
import org.egov.web.utils.EgovPaginatedList;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author pritiranjan
 *
 */

@SuppressWarnings("unchecked")
public class TenderNoticeService extends PersistenceService<TenderNotice, Long>{

	//string constants
	private static final String TF_TYPE_SERVICE_SFX 			  = "TenderFileService";
	private static final String SERVICE_NOT_FOUND    			  = "tenderfile.service.not.found";
	private static final String TENDERNOTICEDATE   				  = "noticeDate";
	private static final String TENDERNOTICEFROMTENDERFILEQUERY   = "from TenderNotice where tenderFileRefNumber=?";
	private static final String TENDERFILENOTFOUND 				  = "TenderFile.class.notfound :";
	private static final String TENDERFILETYPENOTFOUND            = "TenderFileType.not.found :";

	private Map<String,TenderFileService> tenderFileServiceMap;
	private TenderFileService tenderFileService;
	private PersistenceService persistenceService;
	private final static  Logger LOGGER=Logger.getLogger(TenderNoticeService.class);
	private WorkflowService<TenderNotice> tenderNoticeWorkflowService;
	private EisUtilService eisService;
	private TenderCommonService tenderCommonService;
	private ScriptService scriptService;
	/**
	 * This method retrurns list of tenderfiles for which tendernotice needs to be created
	 * @param paramMap-This is set of parameters to get Tenderfile
	 * @param fileType- TenderFile type ex:indent,estimate
	 * @return
	 */

	public EgovPaginatedList searchTenderFile(Map<String,Object> paramMap,String fileType,int pageNumber)
	{
		return getAssociatedService(fileType).getAllTenderFilesToCreateTenderNotice(paramMap,pageNumber,15);
	}

	public void setTenderCommonService(TenderCommonService tenderCommonService) {
		this.tenderCommonService = tenderCommonService;
	}

	public void setScriptService(ScriptService scriptService) {
		this.scriptService = scriptService;
	}

	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}

	public void setTenderNoticeWorkflowService(
			WorkflowService<TenderNotice> tenderNoticeWorkflowService) {
		this.tenderNoticeWorkflowService = tenderNoticeWorkflowService;
	}

	public TenderFileService getAssociatedService(final String fileType) {

		if (this.tenderFileServiceMap.containsKey(fileType.concat(TF_TYPE_SERVICE_SFX))) {
			tenderFileService = tenderFileServiceMap.get(fileType.concat(TF_TYPE_SERVICE_SFX));
		}
		else
		{
			final GenericTenderFileService defaultFileService = (GenericTenderFileService)tenderFileServiceMap.get("default".concat(TF_TYPE_SERVICE_SFX));
			if (defaultFileService == null) {
				throw new EGOVRuntimeException(SERVICE_NOT_FOUND);
			}
			TenderFileType tenderFileType=null;
			try{
				tenderFileType=(TenderFileType)persistenceService.find("from TenderFileType where fileType=?",fileType);
				if(tenderFileType==null)
					throw new EGOVRuntimeException(TENDERFILETYPENOTFOUND +fileType);
				defaultFileService.setTenderFileType(Thread.currentThread().getContextClassLoader().loadClass(tenderFileType.getFullClassName()));
			}catch(ClassNotFoundException ex)
			{
				throw new EGOVRuntimeException(TENDERFILENOTFOUND+ tenderFileType.getFullClassName());
			}
			tenderFileService = defaultFileService;
		}

		return tenderFileService;
	}

	/**
	 * This API is to get TenderNotice by passing TenderFile
	 * @param tenderFile
	 * @return-It returns TenderNotice if exists for a tenderfile else it returns null.
	 */

	public TenderNotice getTenderNoticeByTenderFile(TenderFile tenderFile)
	{
		return (TenderNotice)persistenceService.find(TENDERNOTICEFROMTENDERFILEQUERY,tenderFile.getFileNumber());
	}

	/**
	 * This API is to get Status of TenderNotice by passing tenderfilenumber
	 * @param tenderfilenumber- This is file number of a TenderFile
	 * @return-It returns status of TenderNotice if exists for a tenderfile else it returns null.
	 */

	public String getTenderNoticeStatusByTenderFileNumber(String filenumber)
	{
		TenderNotice tenderNotice=(TenderNotice)persistenceService.find(TENDERNOTICEFROMTENDERFILEQUERY,filenumber);

		if(tenderNotice!=null)
			return tenderNotice.getStatus().getCode();
		else
			return null;
	}

	/**
	 * This api is to get Status of TenderableGroup in TenderNotice
	 * @param tenderableGroup-
	 * @return-It returns  status of TenderableGroup in TenderNotice
	 */

	public String getStatusOfTenderableGroupInTenderNotice(TenderableGroup tenderableGroup)
	{
		//needs to be implemented
		return "";
	}

	/**
	 * This method is to get Pagination List of TenderNotice
	 * @param paramMap
	 * @param page
	 * @return
	 */

	public EgovPaginatedList searchTenderNotice(Map<String,Object> paramMap,Integer page)
	{
		Criteria criteria=buildSearchTenderNoticeCriteria(paramMap);
		Criteria countCriteria=buildSearchTenderNoticeCriteria(paramMap);
		Page pageObj=new Page(criteria,page,15);
		countCriteria.setProjection(Projections.rowCount());
		return new EgovPaginatedList(pageObj,((Long)countCriteria.uniqueResult()).intValue());
	}

	public List<TenderNotice>  searchTenderNoticeList(Map<String,Object> paramMap)
	{
		Criteria criteria=buildSearchTenderNoticeCriteria(paramMap);
		if(paramMap.containsKey(TenderConstants.SEARCHMODE) && paramMap.get(TenderConstants.SEARCHMODE).equals("new")){
			criteria.createAlias("status","statusObj");
			criteria.add(Restrictions.eq("statusObj.code", TenderConstants.TENDERNOTICE_APPROVED));
		}
		return criteria.list();
	}


	/**
	 * This api is to build the criteria from paramater map to get list of tendernotice
	 * @param paramMap
	 * @return
	 */

	private Criteria buildSearchTenderNoticeCriteria(Map<String,Object> paramMap)
	{
		Criteria criteria=getSession().createCriteria(TenderNotice.class);
		if(paramMap.containsKey(TenderConstants.FROMDATE))
			criteria.add(Restrictions.ge(TENDERNOTICEDATE, (Date)paramMap.get(TenderConstants.FROMDATE)));
		if(paramMap.containsKey(TenderConstants.TODATE))
			criteria.add(Restrictions.le(TENDERNOTICEDATE, (Date)paramMap.get(TenderConstants.TODATE)));
		if(paramMap.containsKey(TenderConstants.DEPARTMENT))
			criteria.add(Restrictions.eq("department.id", (Integer)paramMap.get(TenderConstants.DEPARTMENT)));
		if(paramMap.containsKey(TenderConstants.TENDERNOTICENUMBER))
			criteria.add(Restrictions.ilike("number", (String)paramMap.get(TenderConstants.TENDERNOTICENUMBER),MatchMode.ANYWHERE));
		if(paramMap.containsKey(TenderConstants.TENDERFILETYPE))
			criteria.add(Restrictions.eq("tenderFileType.id", (Long)paramMap.get(TenderConstants.TENDERFILETYPE)));
		if(paramMap.containsKey(TenderConstants.OBJECT_STATUS) && TenderConstants.TENDERNOTICE_APPROVED.equals((String)paramMap.get(TenderConstants.OBJECT_STATUS))){
			criteria.createAlias("status","statusObj");
			criteria.add(Restrictions.eq("statusObj.code", TenderConstants.TENDERNOTICE_APPROVED));
		}
		if(paramMap.containsKey(TenderConstants.TENDERFILETYPENUMBER)){
			
			DetachedCriteria entitydetcrit=DetachedCriteria.forClass(TenderableEntityGroup.class,"entity");		
			
			//Criteria entitydetcrit=getSession().createCriteria(TenderableEntityGroup.class,"entity");	
			entitydetcrit.createAlias("entity.tenderUnit", "units");
			TenderFileType fileTypeObj=(TenderFileType) persistenceService.find("from TenderFileType where id=?", (Long)paramMap.get(TenderConstants.TENDERFILETYPE));
			entitydetcrit.add(Restrictions.eq("tenderableGroupType",fileTypeObj.getGroupType()));
			entitydetcrit.add(Restrictions.ilike("number", paramMap.get(TenderConstants.TENDERFILETYPENUMBER).toString(),MatchMode.ANYWHERE));
			entitydetcrit.setProjection(Projections.distinct(Projections.property("units.id")));
			//entitydetcrit.list();
			DetachedCriteria tenderunitdetcrit=DetachedCriteria.forClass(TenderUnit.class,"tenderunit");		
		
			//Criteria tenderunitdetcrit=getSession().createCriteria(TenderUnit.class,"tenderunit");	
			tenderunitdetcrit.add(Property.forName("id").in(entitydetcrit));
			tenderunitdetcrit.setProjection(Projections.distinct(Projections.property("tenderNotice.id")));
			criteria.add(Property.forName("id").in(tenderunitdetcrit));
			//tenderunitdetcrit.list();
			//criteria.list();
		}
		return criteria;
	}

	public void setTenderFileServiceMap(
			Map<String, TenderFileService> tenderFileServiceMap) {
		this.tenderFileServiceMap = tenderFileServiceMap;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public TenderNotice save(TenderNotice tenderNotice,
			List<TenderUnit> tenderUnitDetailsList, List<TenderableEntityGroup> tenderableGroupsList, List<TenderableEntity> tenderEntitiesList,String workflowType, boolean autoGenerateNumberFlag, Position approver) {
		

		if(tenderNotice!=null && tenderNotice.getId()==null){
			//buildTenderNoticeObject(tenderNotice, tenderUnitDetailsList,tenderableGroupsList, tenderEntitiesList);
			buildTenderNoticeOnCreate(tenderNotice, tenderUnitDetailsList,tenderableGroupsList, tenderEntitiesList);

			if(tenderNotice.getNumber()==null)
				tenderNotice.setNumber(getTenderNoticeNumber(tenderNotice, Boolean.TRUE));
			else
				tenderNotice.setNumber(tenderNotice.getNumber());


			persist(tenderNotice);
		//	tenderNotice=createWorkflow(tenderNotice,workflowType);
		}else
		{
			tenderNotice.setModifiedDate(new Date());
			//buildTenderNoticeObjectOnUpdateMode(tenderNotice, tenderUnitDetailsList,tenderableGroupsList, tenderEntitiesList);
			buildTenderNoticeObjectOnUpdate(tenderNotice, tenderUnitDetailsList,tenderableGroupsList, tenderEntitiesList);
			merge(tenderNotice);

		}
		if(approver!=null)
			tenderNotice.setPosition(approver);

		createWorkflow(tenderNotice,workflowType);
		return tenderNotice;
	}


	/*private void buildTenderNoticeObjectOnUpdateMode(TenderNotice tenderNotice,List<TenderUnit> tenderUnitDetailsList,
			List<TenderableEntityGroup> tenderableGroupsList,List<TenderableEntity> tenderEntitiesList)
	{

		Set<TenderUnit> tenderUnits = new HashSet<TenderUnit>();

		//Boolean tenderEntityMappedWithTenderGroup=checkTenderEntityMappedWithGroup(tenderUnitDetailsList);

		for(TenderUnit unit:tenderUnitDetailsList)
		{
			unit.setTenderNotice(tenderNotice);
			unit.setModifiedDate(new Date());

			if(tenderableGroupsList!=null && !tenderableGroupsList.isEmpty()){

				if(!tenderNotice.getCombineTenderableGroups()){

					for (TenderableEntityGroup  tenderGroup:tenderableGroupsList)
					{
						//tenderGroup.setModifiedDate(new Date());
						unit.addTenderableGroups(tenderGroup);
						if(tenderGroup.getNumber()!=null && unit.getTenderGroupRefNumber()!=null && tenderGroup.getNumber().equals(unit.getTenderGroupRefNumber()))
						{
							tenderGroup.setTenderUnit(unit);
							tenderGroup.setModifiedDate(new Date());
							tenderGroup.setTenderUnitRefNumber(unit.getTenderGroupRefNumber());
							tenderGroup.setNumber(unit.getTenderGroupRefNumber());
							for(TenderableEntity  tenderEntity:tenderEntitiesList)
							{
								if(tenderGroup.getNumber()!=null && tenderEntity.getTempEntityGroupName()!=null && tenderGroup.getNumber().equals(tenderEntity.getTempEntityGroupName()))
								{
									tenderEntity.setTenderableEntityGroup(tenderGroup);
									tenderEntity.setTenderUnit(unit);
									tenderEntity.setModifiedDate(new Date());
									//	tenderEntities.add(tenderEntity);
									unit.addTenderEntities(tenderEntity);
									tenderGroup.addTenderEntities(tenderEntity);
								}
							}
						}

					}
				}else
				{
					*//**
					 * If the tender entity is not mapped with tender group, then meaning is, a unit is mapped with entity directly
					 * and there is no relation with group and entities. Always one unit is used with group and entities.
					 *//*
					unit.getTenderableGroups().clear();
					for (TenderableEntityGroup  tenderGroup:tenderableGroupsList)
					{
						unit.addTenderableGroups(tenderGroup);
						tenderGroup.setTenderUnit(unit);
						tenderGroup.setModifiedDate(new Date());
						//tenderGroup.setTenderUnitRefNumber(unit.getTenderGroupRefNumber());
						//tenderGroup.setNumber(unit.getTenderGroupRefNumber());
					}
					unit.getTenderEntities().clear();
					for(TenderableEntity  tenderEntity:tenderEntitiesList)
					{
						unit.addTenderEntities(tenderEntity);
						//tenderEntity.setTenderableEntityGroup(null);
						tenderEntity.setTenderUnit(unit);
						tenderEntity.setModifiedDate(new Date());

					}


				}

			}else
			{
				for(TenderableEntity  tenderEntity:tenderEntitiesList)
				{
					if(tenderEntity.getNumber()!=null && unit.getTenderGroupRefNumber()!=null && tenderEntity.getNumber().equals(unit.getTenderGroupRefNumber()))
					{
						tenderEntity.setTenderUnit(unit);
						tenderEntity.setModifiedDate(new Date());
						unit.addTenderEntities(tenderEntity);
					}
				}
			}
			if(unit.getClassOfContractor()!=null && unit.getClassOfContractor().getId()<0)
			{
				unit.setClassOfContractor(null);
			}
			//unit.setTenderableGroups(tenderableGroups);

			//TODO: add the status required at unit level.
			unit.setStatus( getStatusByPassingModuleAndCode(TenderConstants.TENDERUNIT,TenderConstants.TENDERUNITINCREATEDSTATE));

			tenderUnits.add(unit);

		}
		if(tenderNotice.getTenderUnits()!=null)
			tenderNotice.getTenderUnits().clear();

		tenderNotice.setTenderUnits(tenderUnits);
		tenderNotice.setStatus(getStatusByPassingModuleAndCode(TenderConstants.TENDERNOTICE,TenderConstants.TENDERNOTICEINCREATEDSTATE));


	}*/

	private List<TenderableEntity> entityList=new ArrayList<TenderableEntity>();
	private List<TenderUnit> unitDetailsList=new ArrayList<TenderUnit>();
	private List<TenderableEntityGroup> groupList=new ArrayList<TenderableEntityGroup>();


	private void buildTenderNoticeObjectOnUpdate(TenderNotice tenderNotice,List<TenderUnit> tenderUnitDetailsList,
			List<TenderableEntityGroup> tenderableGroupsList,List<TenderableEntity> tenderEntitiesList)
	{
		entityList=new ArrayList<TenderableEntity>(tenderEntitiesList);
		groupList= new ArrayList<TenderableEntityGroup>(tenderableGroupsList);
		unitDetailsList = new ArrayList<TenderUnit>(tenderUnitDetailsList);
		Set<TenderUnit> newUnitSet=new HashSet<TenderUnit>();
		ListIterator<TenderUnit> newUnitListIterator= unitDetailsList.listIterator();

		for(TenderUnit unitObj:tenderNotice.getTenderUnits()){

			newUnitListIterator= unitDetailsList.listIterator();
			while(newUnitListIterator.hasNext())
			{
				TenderUnit unit = newUnitListIterator.next();

				if(unitObj.getId().equals(unit.getId())){
					//unit.setTenderNotice(tenderNotice);
					unitObj.setModifiedDate(new Date());
					unitObj.setStatus(tenderCommonService.getStatusByModuleAndCode(TenderConstants.TENDERUNIT,TenderConstants.TENDERUNITINCREATEDSTATE));
					unitObj.setBidMeetingDate(unit.getBidMeetingDate());
					unitObj.setClassOfContractor(unit.getClassOfContractor());

					if(unit.getClassOfContractor() == null || unit.getClassOfContractor().getId()==-1)
						unitObj.setClassOfContractor(null);
					else
						unitObj.setClassOfContractor(unit.getClassOfContractor());

					unitObj.setDateOfOpeningOfEtender(unit.getDateOfOpeningOfEtender());
					unitObj.setDateofSale(unit.getDateofSale());
					unitObj.setDateofSubmission(unit.getDateofSubmission());
					unitObj.setEmd(unit.getEmd());
					unitObj.setEstimatedCost(unit.getEstimatedCost());
					unitObj.setFormCost(unit.getFormCost());
					unitObj.setSupplierGrade(unit.getSupplierGrade());
					unitObj.setTenderGroupNarration(unit.getTenderGroupNarration());
					unitObj.setTenderGroupRefNumber(unit.getTenderGroupRefNumber());
					unitObj.setTimeLimit(unit.getTimeLimit());
					//to build tenderable group
					buildTenderableGroupAndEntity(tenderNotice,unitObj);
					unitObj.setTenderNotice(tenderNotice);
					//remove this new unit object from the list
					newUnitListIterator.remove();
					newUnitSet.add(unitObj);
				}
			}
		}
		//if some new unit is added on modify mode
		if(!unitDetailsList.isEmpty())
		{
			for(TenderUnit unitObj:unitDetailsList){
				unitObj.setTenderNotice(tenderNotice);
				unitObj.setModifiedDate(new Date());
				unitObj.setStatus( tenderCommonService.getStatusByModuleAndCode(TenderConstants.TENDERUNIT,TenderConstants.TENDERUNITINCREATEDSTATE));
				buildTenderableGroupAndEntity(tenderNotice,unitObj);
				newUnitSet.add(unitObj);
			}
		}
		tenderNotice.getTenderUnits().clear();
		tenderNotice.getTenderUnits().addAll(newUnitSet);
		tenderNotice.setStatus(tenderCommonService.getStatusByModuleAndCode(TenderConstants.TENDERNOTICE,TenderConstants.TENDERNOTICEINCREATEDSTATE));

	}


	/**
	 * This method is called on modify mode to build the old object
	 * @param notice
	 * @param unit
	 * @return
	 */

	private TenderUnit buildTenderableGroupAndEntity(TenderNotice notice,TenderUnit unit)
	{
		ListIterator <TenderableEntityGroup> groupIterator= groupList.listIterator();
		ListIterator<TenderableEntity> entityIterator = entityList.listIterator();

		while(groupIterator.hasNext())
		{
			TenderableEntityGroup newGroup =groupIterator.next();
			for(TenderableEntityGroup oldGroup:unit.getTenderableGroups())
			{
				if(newGroup.getId() !=null && oldGroup.getId().equals(newGroup.getId()))
				{
					oldGroup.setDescription(newGroup.getDescription());
					oldGroup.setEstimatedCost(newGroup.getEstimatedCost());
					oldGroup.setModifiedDate(new Date());
					oldGroup.setNumber(newGroup.getNumber());
					oldGroup.setTenderableGroupType(newGroup.getTenderableGroupType());
					oldGroup.setTenderUnitRefNumber(newGroup.getTenderUnitRefNumber());
					groupIterator.remove();
				}
			}
		}

		while(entityIterator.hasNext())
		{
			TenderableEntity newEntity =entityIterator.next();
			for(TenderableEntity oldEntity:unit.getTenderEntities())
			{
				if(newEntity.getId() !=null && oldEntity.getId().equals(newEntity.getId()))
				{
					oldEntity.setDescription(newEntity.getDescription());
					oldEntity.setModifiedDate(new Date());
					oldEntity.setName(newEntity.getName());
					oldEntity.setNumber(newEntity.getNumber());
					oldEntity.setRequestedByDate(newEntity.getRequestedByDate());
					oldEntity.setRequestedUOM(newEntity.getRequestedUOM());
					oldEntity.setRequestedQty(newEntity.getRequestedQty());
					oldEntity.setRequestedValue(newEntity.getRequestedValue());
					oldEntity.setTenderableType(newEntity.getTenderableType());
					oldEntity.setIndexNo(newEntity.getIndexNo());
					entityIterator.remove();
				}
			}
		}
		buildNewGroupAndEntity(notice,unit);
		return unit;
	}


	private void buildNewGroupAndEntity(TenderNotice notice, TenderUnit unit)
	{
        
		//Integer indexNo = (null != unit.getTenderEntities() && !unit.getTenderEntities().isEmpty()) ? unit.getTenderEntities().size():0;
		if(groupList.isEmpty())
		{
			for(TenderableEntity  tenderEntity:entityList)
			{
				if(tenderEntity.getNumber()!=null && unit.getTenderGroupRefNumber()!=null && tenderEntity.getNumber().equals(unit.getTenderGroupRefNumber()))
				{
					tenderEntity.setTenderUnit(unit);
					tenderEntity.setModifiedDate(new Date());
					unit.addTenderEntities(tenderEntity);
				}
			}
		}
		else
		{
			if(!notice.getCombineTenderableGroups())
			{
				for (TenderableEntityGroup  tenderGroup: groupList)
				{
					if(tenderGroup.getNumber()!=null && unit.getTenderGroupRefNumber()!=null && tenderGroup.getNumber().equals(unit.getTenderGroupRefNumber()))
					{
						tenderGroup.setTenderUnit(unit);
						tenderGroup.setModifiedDate(new Date());
						tenderGroup.setTenderUnitRefNumber(unit.getTenderGroupRefNumber());
						tenderGroup.setNumber(unit.getTenderGroupRefNumber());
						Set<TenderableEntity> entitySet=new HashSet<TenderableEntity>();
						for(TenderableEntity  tenderEntity:entityList)
						{
							if(tenderGroup.getNumber()!=null && tenderEntity.getTempEntityGroupName()!=null && tenderGroup.getNumber().equals(tenderEntity.getTempEntityGroupName()))
							{
								tenderEntity.setTenderableEntityGroup(tenderGroup);
								tenderEntity.setTenderUnit(unit);
								tenderEntity.setModifiedDate(new Date());
								entitySet.add(tenderEntity);
							}
						}
						tenderGroup.getEntities().clear();
						tenderGroup.getEntities().addAll(entitySet);
						unit.addTenderableGroups(tenderGroup);
					}
				}
			}
			else
			{
				for (TenderableEntityGroup  tenderGroup:groupList)
				{
					tenderGroup.setTenderUnit(unit);
					tenderGroup.setModifiedDate(new Date());
					unit.addTenderableGroups(tenderGroup);
				}
				
				for(TenderableEntity  tenderEntity:entityList)
				{
					tenderEntity.setTenderUnit(unit);
					tenderEntity.setModifiedDate(new Date());
					unit.addTenderEntities(tenderEntity);
				}
				
			}
		}
	}

	private String getTenderNoticeNumber(TenderNotice tenderNotice,
			Boolean flag) {
		String sflag;
		FinancialYearDAO finYearDAO=CommonsDaoFactory.getDAOFactory().getFinancialYearDAO();
		CFinancialYear financialYearObject= finYearDAO.getFinancialYearByDate(tenderNotice.getNoticeDate());

		if(financialYearObject==null || financialYearObject.equals(""))
			throw new EGOVRuntimeException("Financial year Id doesnot Exist.");


		SequenceGenerator sequenceGenerator=new SequenceGenerator();
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT",TenderConstants.TENDERNUMBERGENERATORSCRIPT);
		if(scripts==null || scripts.isEmpty())
			throw new EGOVRuntimeException("There is no script to generate tender notice");
		else
		{
			//String objectType=TenderConstants.TENDERNOTICENUMBERPREFIX+"-"+financialYearObject.getFinYearRange();
			sflag=flag?"true":"false";
			return (String)scriptService.executeScript(TenderConstants.TENDERNUMBERGENERATORSCRIPT, ScriptService.createContext("tenderNotice",tenderNotice,"finYear",financialYearObject,"sequenceGenerator",sequenceGenerator,"sflag",sflag,"month",TenderUtils.getMonth(tenderNotice.getNoticeDate()))) ;
		}
	}

	private TenderNotice createWorkflow(TenderNotice tenderNotice,
			String workflowType) {

		LOGGER.info(" Start Tender notice work flow....");
		//try{
			if(tenderNotice.getState()==null && workflowType!=null){
				Position pos=null;
				//if(tenderNotice.getPosition()==null)
				pos= eisService.getPrimaryPositionForUser(tenderNotice.getCreatedBy().getId(), new Date());
				/*else
					pos = tenderNotice.getPosition();*/
				tenderNoticeWorkflowService.start(tenderNotice,pos,"Tender Created");

				if (workflowType!=null && TenderConstants.ACTION_SAVE_AND_SUBMIT.equalsIgnoreCase(workflowType))
				{
					tenderNoticeWorkflowService.transition(TenderConstants.SCRIPT_SAVE_AND_SUBMIT,tenderNotice,"Tender notice number-"+ tenderNotice.getNumber());
				}


				/*			List<String> wfNextValue=getTenderCommonService().getFurtherWorkflowState(tenderNotice.getCurrentState().getValue(),TenderConstants.WFSTATUS_NEXT,tenderNotice);
			if(TenderConstants.WF_END_STATE.equalsIgnoreCase(wfNextValue.get(1)))
				tenderNoticeWorkflowService.end(tenderNotice, getEisService().getPrimaryPositionForUser(tenderNotice.getModifiedBy().getId(), new Date()), "Approved and workflow is ended");
				 */
			}
			else
			{
				if(workflowType!=null && !TenderConstants.SCRIPT_SAVE.equals(workflowType))
				{
					if(TenderConstants.ACTION_SAVE_AND_SUBMIT.equals(workflowType))
						workflowType=TenderConstants.SCRIPT_SAVE_AND_SUBMIT;


					tenderNoticeWorkflowService.transition(workflowType, tenderNotice, tenderNotice.getNumber());

					if(tenderNotice.getCurrentState().getValue().equalsIgnoreCase(TenderConstants.WF_CANCELED_STATE))
						tenderNoticeWorkflowService.end(tenderNotice, eisService.getPrimaryPositionForUser(tenderNotice.getCreatedBy().getId(), new Date()), "Cancelled and workflow is ended");
					else
					{
						List<String> wfNextValue=tenderCommonService.getFurtherWorkflowState(tenderNotice.getCurrentState().getValue(),TenderConstants.WFSTATUS_NEXT,tenderNotice);
						if(TenderConstants.WF_END_STATE.equalsIgnoreCase(wfNextValue.get(1)))
							tenderNoticeWorkflowService.end(tenderNotice, eisService.getPrimaryPositionForUser(tenderNotice.getModifiedBy().getId(), new Date()), "Approved and workflow is ended");
					}

				}
			}
   /*	}

		catch(Exception ex)
		{

			throw new EGOVRuntimeException(ex.getMessage()+"Error in workflow");
		}*/

		return tenderNotice;


	}


	public void buildTenderNoticeOnCreate(TenderNotice tenderNotice,List<TenderUnit> tenderUnitDetailsList,
			List<TenderableEntityGroup> tenderableGroupsList,List<TenderableEntity> tenderEntitiesList)
	{
		Set<TenderUnit> tenderUnits = new HashSet<TenderUnit>();
		Set<TenderableEntityGroup> tenderableGroups =null;
		entityList=new ArrayList<TenderableEntity>(tenderEntitiesList);
		groupList= new ArrayList<TenderableEntityGroup>(tenderableGroupsList);
		for(TenderUnit unit:tenderUnitDetailsList)
		{
			unit.setTenderNotice(tenderNotice);
			unit.setModifiedDate(new Date());
			if(unit.getClassOfContractor() == null || unit.getClassOfContractor().getId()==-1)
				unit.setClassOfContractor(null);
			else
				unit.setClassOfContractor(unit.getClassOfContractor());

			unit.setTenderableGroups(tenderableGroups);
			unit.setStatus( tenderCommonService.getStatusByModuleAndCode(TenderConstants.TENDERUNIT,TenderConstants.TENDERUNITINCREATEDSTATE));
			buildNewGroupAndEntity(tenderNotice, unit);

			tenderUnits.add(unit);
		}
		tenderNotice.getTenderUnits().clear();
		tenderNotice.getTenderUnits().addAll(tenderUnits);
		tenderNotice.setTenderUnits(tenderUnits);
		tenderNotice.setStatus(tenderCommonService.getStatusByModuleAndCode(TenderConstants.TENDERNOTICE,TenderConstants.TENDERNOTICEINCREATEDSTATE));
	}


/**
 * Build the tendernotice object. Tender group is null in case of land estate module.
 * @param tenderNotice
 * @param tenderUnitDetailsList
 * @param tenderableGroupsList
 * @param tenderEntitiesList
 */
	/*private void buildTenderNoticeObject(TenderNotice tenderNotice,
			List<TenderUnit> tenderUnitDetailsList,
			List<TenderableEntityGroup> tenderableGroupsList,
			List<TenderableEntity> tenderEntitiesList) {
		Set<TenderUnit> tenderUnits = new HashSet<TenderUnit>();

		 Set<TenderableEntityGroup> tenderableGroups =null;
		 Set<TenderableEntity> tenderEntities =new HashSet<TenderableEntity>();
		 Set<TenderableEntity> listOfTenderEntities =new HashSet<TenderableEntity>();
		 	for(TenderUnit unit:tenderUnitDetailsList)
				{
					tenderableGroups = new HashSet<TenderableEntityGroup>();

					unit.setTenderNotice(tenderNotice);
					unit.setModifiedDate(new Date());

					 if(tenderableGroupsList!=null && !tenderableGroupsList.isEmpty()){
					 	*//**
					 	 * If combineTenderableGroup flag is false, then tender entities are mapped with tenderable group
					 	 *//*
						 if(!tenderNotice.getCombineTenderableGroups()){

								for (TenderableEntityGroup  tenderGroup:tenderableGroupsList)
								{
									tenderEntities = new HashSet<TenderableEntity>();
									if(tenderGroup.getNumber()!=null && unit.getTenderGroupRefNumber()!=null && tenderGroup.getNumber().equals(unit.getTenderGroupRefNumber()))
									{
										tenderGroup.setTenderUnit(unit);
										tenderGroup.setModifiedDate(new Date());
										tenderGroup.setTenderUnitRefNumber(unit.getTenderGroupRefNumber());
										tenderGroup.setNumber(unit.getTenderGroupRefNumber());
											for(TenderableEntity  tenderEntity:tenderEntitiesList)
											{
											if(tenderGroup.getNumber()!=null && tenderEntity.getTempEntityGroupName()!=null && tenderGroup.getNumber().equals(tenderEntity.getTempEntityGroupName()))
												{
													tenderEntity.setTenderableEntityGroup(tenderGroup);
													tenderEntity.setTenderUnit(unit);
													tenderEntity.setModifiedDate(new Date());
												//	tenderEntity.setRequestedUOM(null);

													tenderEntity.setRequestedQty(tenderEntity.getRequestedQty());
													tenderEntity.setDescription(tenderEntity.getDescription());
													tenderEntity.setName(tenderEntity.getName());
													tenderEntity.setNumber(tenderEntity.getNumber());
													tenderEntity.setRequestedByDate(tenderEntity.getRequestedByDate());
													tenderEntity.setRequestedValue(tenderEntity.getRequestedValue());
													tenderEntity.setRequestedUOM(tenderEntity.getRequestedUOM());

													tenderEntities.add(tenderEntity);

													//listOfTenderEntities.add(tenderEntity);
												}
										     }
											tenderGroup.setEntities(tenderEntities);

										tenderableGroups.add(tenderGroup);
									}
									unit.setTenderEntities(tenderEntities);
								}
							 }else
							 {
								*//**
								 * If CombineTenderableGroups is true, then there is no relation between tenderable group and tender entity.
								 *//*
								 for (TenderableEntityGroup  tenderGroup:tenderableGroupsList)
									{
										tenderEntities = new HashSet<TenderableEntity>();

											tenderGroup.setTenderUnit(unit);
											tenderGroup.setModifiedDate(new Date());
											tenderGroup.setEntities(null);

											tenderableGroups.add(tenderGroup);

										for(TenderableEntity  tenderEntity:tenderEntitiesList)
										{
												tenderEntity.setTenderableEntityGroup(null);
												tenderEntity.setTenderUnit(unit);
												tenderEntity.setModifiedDate(new Date());
												tenderEntities.add(tenderEntity);

									     }
										unit.setTenderEntities(tenderEntities);
									}
							 }

							 }else
							 {
								 for(TenderableEntity  tenderEntity:tenderEntitiesList)
									{
									 if(tenderEntity.getNumber()!=null && unit.getTenderGroupRefNumber()!=null && tenderEntity.getNumber().equals(unit.getTenderGroupRefNumber()))
										{
										tenderEntity.setTenderUnit(unit);
										unit.addTenderEntities(tenderEntity);
										tenderEntity.setTenderableEntityGroup(null);
										tenderEntity.setModifiedDate(new Date());
										listOfTenderEntities.add(tenderEntity);
										}
									}
								// unit.setTenderEntities(listOfTenderEntities);
							 }
					if(unit.getClassOfContractor()!=null && unit.getClassOfContractor().getId()<0)
					{
						unit.setClassOfContractor(null);
					}
					unit.setTenderableGroups(tenderableGroups);

					unit.setStatus( getStatusByPassingModuleAndCode(TenderConstants.TENDERUNIT,TenderConstants.TENDERUNITINCREATEDSTATE));

					tenderUnits.add(unit);

				}

		tenderNotice.setTenderUnits(tenderUnits);
		tenderNotice.setStatus(getStatusByPassingModuleAndCode(TenderConstants.TENDERNOTICE,TenderConstants.TENDERNOTICEINCREATEDSTATE));
	}
*/
	/*private EgwStatus getStatusByPassingModuleAndCode(String moduleType, String moduleCode) {
		if(moduleCode!=null && moduleType!=null)
		return (EgwStatus) persistenceService.find("from EgwStatus where moduletype=? and code=?",moduleType,moduleCode);
		else
			return null;
	}*/
	public Integer getTenderNoticeByNumber(String noticeNumber) {
		if(noticeNumber!=null )
		{
			Criteria criteriaForNotice= getSession().createCriteria(TenderNotice.class);
			criteriaForNotice.add(Restrictions.ilike("number", noticeNumber,MatchMode.ANYWHERE));
			return criteriaForNotice.list().size();
		}
		else
			return null;
	}

	public boolean getTenderNoticeNumberUniqueCheckByTenderNumber(
			String tenderNoticeNumber, Long id) {
		TenderNotice tenderNotice = null;
		boolean codeExistsOrNot = false;
		if(tenderNoticeNumber!=null && !"".equals(tenderNoticeNumber)&& id==null)
			tenderNotice=find("from TenderNotice where upper(number)=?",tenderNoticeNumber.toUpperCase());
		else if (tenderNoticeNumber!=null && !"".equals(tenderNoticeNumber))
			tenderNotice=find("from TenderNotice where upper(number)=? and id!=?",tenderNoticeNumber.toUpperCase(),id);
		if(tenderNotice!=null){
			codeExistsOrNot=true;
		}
		return codeExistsOrNot;
	}

	public TenderNotice getTenderNoticeById(Long idTemp) {

		TenderNotice tenderNotice=null;
		if(idTemp==null)
			throw new EGOVRuntimeException("Tender Notice Id is null");
		else
			tenderNotice=findById(idTemp);

		return tenderNotice;
	}

	/*public boolean isModifyableByLoginUser(TenderNotice tenderNotice) {
		Integer userId=Integer.valueOf(EGOVThreadLocals.getUserId());
		if(tenderNotice.getCreatedBy()!=null && userId.equals(tenderNotice.getCreatedBy().getId()))
			return Boolean.TRUE;
		else
			return Boolean.FALSE;

	}*/



	public List<ContractorGrade> getContractorsbyCost(BigDecimal cost) {

		BigDecimal maxAmount = cost==null ? BigDecimal.ZERO : cost;
		Criteria criteria =getSession().createCriteria(ContractorGrade.class,"contractorgrade");		
		criteria.add(Restrictions.ge("maxAmount",maxAmount));
		criteria.addOrder(Order.asc("id"));
		return criteria.list();
	}
	

	public BigDecimal getEMDAmountbyCost(BigDecimal cost){

		Criteria criteria=getSession().createCriteria(EmdMaster.class);
		criteria.add(Restrictions.le("minamount", cost));
		criteria.add(Restrictions.ge("maxamount", cost));
		EmdMaster emd=new EmdMaster();
		List <EmdMaster> emdMasterList=new ArrayList();
		emdMasterList=criteria.list();
		if(!emdMasterList.isEmpty()){
			emd=emdMasterList.get(0);
			if(emd.getPercentage()!=null){
				cost=(cost.multiply((emd.getPercentage()))).divide(BigDecimal.valueOf(100), BigDecimal.ROUND_HALF_UP);;
				return cost;
			}
			else if(emd.getFlatrate()!=null)
				return cost=emd.getFlatrate();
			else return cost=BigDecimal.ZERO;
		}else
			return BigDecimal.ZERO;

	}


	public BigDecimal getTenderCostAmount(BigDecimal cost){

		Criteria criteria=getSession().createCriteria(TenderCostMaster.class);
		criteria.add(Restrictions.le("minamount", cost));
		criteria.add(Restrictions.ge("maxamount", cost));
		TenderCostMaster tenderCost=new TenderCostMaster();
		List <TenderCostMaster> tenderCostList=new ArrayList();
		tenderCostList=criteria.list();
		if(!tenderCostList.isEmpty()){
			tenderCost=tenderCostList.get(0);
			if(tenderCost.getFlatrate()!=null){
				return cost=tenderCost.getFlatrate();
			}
			else if(tenderCost.getPercentage()!=null){
				cost=(cost.multiply(tenderCost.getPercentage())).divide(BigDecimal.valueOf(100), BigDecimal.ROUND_HALF_UP);
				return cost;
			}
			else return cost=BigDecimal.ZERO;
		}else
			return BigDecimal.ZERO;

	}

	public TenderFileType getTenderFileTypebyId(Long id)
	{
		TenderFileType tenderFileType=(TenderFileType)persistenceService.find("from TenderFileType where id=?",id);
		return tenderFileType;
	}

}
