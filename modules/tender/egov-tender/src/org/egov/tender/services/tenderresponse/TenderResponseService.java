package org.egov.tender.services.tenderresponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Bidder;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.service.BidderTypeService;
import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EisUtilService;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.model.TenderFileType;
import org.egov.tender.model.TenderJustification;
import org.egov.tender.model.TenderUnit;
import org.egov.tender.services.common.TenderCommonService;
import org.egov.tender.services.common.TenderService;
import org.egov.tender.utils.TenderConstants;
import org.egov.web.utils.EgovPaginatedList;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * @author pritiranjan
 *
 */


@SuppressWarnings("unchecked")
public class TenderResponseService extends PersistenceService<GenericTenderResponse,Long>{
	
	private static final String RESPONSEDATE  		 = "responseDate";
	private static final String RESPONSEWORKFLOWEND  = "Generic TenderResponse Workflow is Ended";
	private static Map<String, Map<String,String>> FINALSTATUSACTIONMAP = new HashMap<String,Map<String,String>>();
	private WorkflowService<GenericTenderResponse> tenderResponseWorkflowService;
	private EisUtilService eisService;
	private ScriptService scriptService;
	private SequenceGenerator sequenceGenerator;
	private PersistenceService persistenceService;
	private TenderCommonService tenderCommonService;
	private Map<String,TenderService> tenderServiceMap ;
	private static final String TR_TYPE_SERVICE_SFX   = "TenderResponseService";
	private static final String DEFAULT 			  = "default";
	
	/**
	 * This method is called to save TenderResponse Object.
	 * @param response
	 * @param workflowType
	 * @return
	 */
	
	public GenericTenderResponse save(GenericTenderResponse response,String workflowType)
	{
		if(response.getNumber()==null || "".equals(response.getNumber().trim()))
			response.setNumber(getTenderResponseNumber(response,Boolean.TRUE));
		Position pos=response.getPosition();
		if(response.getId()==null)
			persist(response);
		else
			merge(response);
		response.setPosition(pos);
		//If Tender Response is Accepted then end the workflow of all the responses for different bidder
		// and change the status to rejected and change the unit status to Accepted
		//updateResponsesAndUnitAndNotice(response);
		
		startWorkflow(response,workflowType);
		return response;
	}
	
	/**  
	 * If Tender Response is Accepted then end the workflow of all the responses for different bidder
	 *   and change the status to rejected.
	 *
	 */
	
	/*private void updateResponsesAndUnitAndNotice(GenericTenderResponse response)
	{
		if(TenderConstants.TENDERRESPONSE_ACCEPTED.equals(response.getStatus().getCode())){
			List<GenericTenderResponse> responseList=findAllByNamedQuery(TenderConstants.GETALLRESPONSESBYUNITTOCANCELL, response.getTenderUnit().getId(),response.getId(),TenderConstants.TENDERRESPONSE_REJECTED);
			if(responseList!=null && !responseList.isEmpty())
			{
				EgwStatus rejStatus=(EgwStatus)persistenceService.find(TenderConstants.STATUSQUERY,
						TenderConstants.TENDERRESPONSEMODULE,TenderConstants.TENDERRESPONSE_REJECTED);
				//update all the response status as rejected and end the workflow
				for(GenericTenderResponse rejResponse:responseList){
					rejResponse.setStatus(rejStatus);
					tenderResponseWorkflowService.end(rejResponse, 
							eisService.getPrimaryPositionForUser(rejResponse.getCreatedBy().getId(), new Date()), RESPONSEWORKFLOWEND);
				}
			}
			
			//update unit status as Accepted
			TenderUnit unit=response.getTenderUnit();
			unit.setStatus((EgwStatus)persistenceService.find(TenderConstants.STATUSQUERY,
					TenderConstants.TENDERUNIT,TenderConstants.TENDERUNIT_ACCEPTED));
			
			//if all the unit status are accepted update notice status as accepted
			TenderNotice notice=unit.getTenderNotice();
			Boolean flag=Boolean.TRUE;
			for(TenderUnit unitTemp:notice.getTenderUnits()){
				if(!TenderConstants.TENDERUNIT_ACCEPTED.equals(unitTemp.getStatus().getCode())){
					flag=Boolean.FALSE;	
					break;
			    }
		    }
			if(flag){
				notice.setStatus((EgwStatus)persistenceService.find(TenderConstants.STATUSQUERY,
						TenderConstants.TENDERNOTICE,TenderConstants.TENDERUNIT_ACCEPTED));
			}
		}
	}
	*/
	/**
	 * This method is called to save negotiation
	 * @param response
	 * @return
	 */
	
	public GenericTenderResponse saveNegotiation(GenericTenderResponse response)
	{
		if(response.getId()==null)
			persist(response);
		else
			merge(response);
		if(TenderConstants.TENDERRESPONSE_ACCEPTED.equals(response.getStatus().getCode())){
			GenericTenderResponse parent=response.getRootResponse();
			parent.setStatus(tenderCommonService.getStatusByModuleAndCode(TenderConstants.TENDERRESPONSEMODULE, TenderConstants.TENDERRESPONSE_APPROVED));
			//updateResponsesAndUnitAndNotice(parent);
			tenderResponseWorkflowService.end(parent,
					eisService.getPrimaryPositionForUser(parent.getCreatedBy().getId(), new Date()),RESPONSEWORKFLOWEND );
		}
		return response;
	}
	
	/**
	 * This method is called to update the negotiation
	 * @param response
	 * @return
	 */
	
	public GenericTenderResponse updateNegotiation(GenericTenderResponse response)
	{
		persist(response);
		if(TenderConstants.TENDERRESPONSE_ACCEPTED.equals(response.getStatus().getCode())){
			GenericTenderResponse parent = response.getRootResponse();
			parent.setStatus(tenderCommonService.getStatusByModuleAndCode(TenderConstants.TENDERRESPONSEMODULE, TenderConstants.TENDERRESPONSE_APPROVED));
			tenderResponseWorkflowService.end(parent,
					eisService.getPrimaryPositionForUser(parent.getCreatedBy().getId(), new Date()),RESPONSEWORKFLOWEND );
		}
		return response;
	}
	
	/**
	 * This method is called to start workflow..
	 * @param response
	 * @param workflowType
	 */
	
	private void startWorkflow(GenericTenderResponse response,String workflowType)
	{

		if(TenderConstants.TENDERRESPONSE_ACCEPTED.equals(response.getStatus().getCode()) ||
				TenderConstants.TENDERRESPONSE_REJECTED.equals(response.getStatus().getCode()))
			tenderResponseWorkflowService.end(response,
					eisService.getPrimaryPositionForUser(response.getModifiedBy().getId(), new Date()),RESPONSEWORKFLOWEND);
		else{		
			if(workflowType!=null && !"".equals(workflowType.trim())){

				if(response.getPosition()==null)
					response.setPosition(eisService.getPrimaryPositionForUser(response.getCreatedBy().getId(), new Date()));

				if(response.getState()==null){
					 tenderResponseWorkflowService.start(response,response.getPosition(),"Tender Response Created");
					if(TenderConstants.ACTION_SAVE_AND_SUBMIT.equals(workflowType))
						tenderResponseWorkflowService.transition(TenderConstants.ACTION_SAVE_AND_SUBMIT,response,"Tender Response number-"+ response.getNumber());
				}

				else if (!TenderConstants.ACTION_SAVE.equals(workflowType)){

					tenderResponseWorkflowService.transition(workflowType,response,"Tender Response number-"+ response.getNumber());
					if(response.getCurrentState().getValue().equalsIgnoreCase(TenderConstants.WF_CANCELED_STATE))
						tenderResponseWorkflowService.end(response,
								eisService.getPrimaryPositionForUser(response.getCreatedBy().getId(), new Date()), RESPONSEWORKFLOWEND);
					else
					{
						List<String> wfNextValue=tenderCommonService.getFurtherWorkflowState(response.getCurrentState().getValue(),TenderConstants.WFSTATUS_NEXT,response);
						if(TenderConstants.WF_END_STATE.equalsIgnoreCase(wfNextValue.get(1)))
							tenderResponseWorkflowService.end(response, 
									eisService.getPrimaryPositionForUser(response.getModifiedBy().getId(), new Date()),RESPONSEWORKFLOWEND);
					}
				}
			}
			}
	}
	
	/**
	 * This method is to auto Generate Tender Response number
	 * @param response
	 * @param flag
	 * @return
	 */
	
	private String getTenderResponseNumber(GenericTenderResponse response,Boolean flag)
	{
		String sflag;
		FinancialYearDAO finYearDAO=CommonsDaoFactory.getDAOFactory().getFinancialYearDAO();
		CFinancialYear finyear =  finYearDAO.getFinYearByDate(response.getResponseDate());
		if(finyear==null)
			throw new EGOVRuntimeException("Financial year does not Exist.");
		//String objectType=TenderConstants.TENDERRESPONSEENUMBERPREFIX+"-"+finyear.getFinYearRange();
		sflag=flag?"true":"false";
		Calendar cal = Calendar.getInstance();
		cal.setTime(response.getResponseDate());
		return (String)scriptService.executeScript(TenderConstants.TENDERRESPONSEGENERATORSCRIPT, ScriptService.createContext("response",response,"finYear",finyear,"sequenceGenerator",sequenceGenerator,"sflag",sflag,"month",String.valueOf(cal.get(Calendar.MONTH)+1)));
	}
	
	/**
	 * This method is to validate uniqueness of response number
	 * @param response
	 * @param flag
	 * @return
	 */
	
	public Boolean checkUniqueResponseNumber(String number,Long id)
	{
		Criteria responseCriteria=getSession().createCriteria(GenericTenderResponse.class);
		responseCriteria.add(Restrictions.eq("number",number));
		responseCriteria.add(Restrictions.isNull("parent"));
		if(id != null)
			responseCriteria.add(Restrictions.ne("id", id));
		return 	!responseCriteria.list().isEmpty();	
	}
	
	/**
	 * This method is to get TenderResponse by passing id 
	 * @param id
	 * @return
	 */
	
	public GenericTenderResponse getTenderResponseById(Long id)
	{
		return findById(id, Boolean.TRUE);
	}
	
	/**
	 * This Api is to search all the tender responses
	 * @param paramMap
	 * @param page
	 * @return
	 */
	
	public EgovPaginatedList searchTenderResponse(Map<String,Object> paramMap,Integer page)
	{
		Criteria criteria=buildSearchTenderResponseCriteria(paramMap);
		Criteria countCriteria=buildSearchTenderResponseCriteria(paramMap);
		Page pageObj=new Page(criteria,page,10);
		//countCriteria.setProjection(Projections.distinct(Projections.countDistinct("id")));
		countCriteria.setProjection(Projections.rowCount());
		int itemCount = ((Long)countCriteria.uniqueResult()).intValue();
		return new EgovPaginatedList(pageObj,itemCount);
	}
	
	private Criteria buildSearchTenderResponseCriteria(Map<String,Object> paramMap)
	{
		Criteria criteria=getSession().createCriteria(GenericTenderResponse.class)
									  .createAlias("tenderUnit", "unit")
									  .createAlias("unit.tenderNotice", "notice");
		if(paramMap.containsKey(TenderConstants.FROMDATE))
			criteria.add(Restrictions.ge(RESPONSEDATE, (Date)paramMap.get(TenderConstants.FROMDATE)));
		if(paramMap.containsKey(TenderConstants.TODATE))
			criteria.add(Restrictions.le(RESPONSEDATE, (Date)paramMap.get(TenderConstants.TODATE)));
		if(paramMap.containsKey(TenderConstants.DEPARTMENT))
			criteria.add(Restrictions.eq("notice.department.id", (Integer)paramMap.get(TenderConstants.DEPARTMENT)));
		if(paramMap.containsKey(TenderConstants.TENDERNOTICENUMBER)){
			criteria.add(Restrictions.ilike("notice.number", (String)paramMap.get(TenderConstants.TENDERNOTICENUMBER),MatchMode.ANYWHERE));
		}
		if(paramMap.containsKey(TenderConstants.RESPONSENUMBER)){
			criteria.add(Restrictions.ilike("number", (String)paramMap.get(TenderConstants.RESPONSENUMBER),MatchMode.ANYWHERE));
		}
		if(paramMap.containsKey(TenderConstants.CREATEDBY)){
			criteria.add(Restrictions.eq("createdBy.id", (Integer)paramMap.get(TenderConstants.CREATEDBY)));
		}
		if(paramMap.containsKey(TenderConstants.TENDERFILETYPE))
			criteria.add(Restrictions.eq("notice.tenderFileType.id", (Long)paramMap.get(TenderConstants.TENDERFILETYPE)));
		if(paramMap.containsKey(TenderConstants.GROUPNUMBER)){
			criteria.createAlias("unit.tenderableGroups", "groups",CriteriaSpecification.LEFT_JOIN);
			criteria.add(Restrictions.ilike("groups.number", (String)paramMap.get(TenderConstants.GROUPNUMBER)));
		}
		if(paramMap.containsKey(TenderConstants.TENDERFILENUMBER))
			criteria.add(Restrictions.ilike("notice.tenderFileRefNumber", (String)paramMap.get(TenderConstants.TENDERFILENUMBER),MatchMode.ANYWHERE));
		criteria.add(Restrictions.isNull("parent"));
		
		if(paramMap.containsKey(TenderConstants.SEARCHMODE))
		{
			criteria.createAlias("status", "statusObj");
			if(TenderConstants.TENDERRESPONSE_NEGOTIATED.equals((String)paramMap.get(TenderConstants.SEARCHMODE)))
				criteria.add(Restrictions.eq("statusObj.code",TenderConstants.TENDERRESPONSE_NEGOTIATED));
			else if(TenderConstants.TENDERRESPONSE_APPROVED.equals((String)paramMap.get(TenderConstants.SEARCHMODE))){
				criteria.add(Restrictions.or(Restrictions.eq("statusObj.code",TenderConstants.TENDERRESPONSE_APPROVED), Restrictions.eq("statusObj.code",TenderConstants.TENDERRESPONSE_ACCEPTED)));
				
			}
			
		}
	
		
		
			
		//if(TenderConstants.TENDERRESPONSE_NEGOTIATED.)
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria;
	}
	
	/**
	 *  This Api is to get Response list by passing group number 
	 * @param groupNumber
	 * @return
	 */
	
	public List<GenericTenderResponse> getResponseListByReferenceNumber(String groupNumber)
	{
		List<GenericTenderResponse> responseList= Collections.emptyList();
		if(groupNumber!=null && !"".equals(groupNumber.trim())){
			String query=" select distinct tr from GenericTenderResponse tr,TenderableEntityGroup tgroup " +
			"where tr.tenderUnit = tgroup.tenderUnit and tgroup.number = :groupNumber and tr.parent is null) " ;
			Query queryObj=persistenceService.getSession().createQuery(query);
			queryObj.setString("groupNumber", groupNumber);
			responseList=(List<GenericTenderResponse>)queryObj.list();
		}
		return responseList;
	}
	
	/**
	 * This method is to validate any response exists for a supplier and unit
	 * @param id
	 * @param bidderId
	 * @param bidderType
	 * @param unit
	 * @return
	 */
	
	public Boolean checkUniqueResponseForBidder(Long id,Long bidderId,String bidderType,TenderUnit unit)
	{
		Criteria criteria=getSession().createCriteria(GenericTenderResponse.class).createAlias("status", "statusObj");
		criteria.add(Restrictions.eq("bidderId", bidderId));
		criteria.add(Restrictions.eq("bidderType", bidderType));
		criteria.add(Restrictions.eq("tenderUnit", unit));
		criteria.add(Restrictions.ne("statusObj.code", TenderConstants.TENDERRESPONSE_REJECTED));
		criteria.add(Restrictions.ne("statusObj.code", TenderConstants.TENDERRESPONSE_CANCELLED));
		criteria.add(Restrictions.isNull("parent"));
		if(id!=null)
			criteria.add(Restrictions.ne("id", id));
		return !criteria.list().isEmpty();
	}
	
	/**
	 * 	This api is to get valid actions that can be performed on response
	 * @param response
	 * @return
	 */
	
	public Map<String,String> getAvailableActions(GenericTenderResponse response)
	{

		Map<String,String> statusList=Collections.EMPTY_MAP;
		String status=getActionStatus(response);
		if(!FINALSTATUSACTIONMAP.containsKey(status)){
			statusList=(Map<String,String>)scriptService.executeScript("tender.responsesearch", ScriptService.createContext("status",status));
			FINALSTATUSACTIONMAP.put(status, statusList);
		}
		return FINALSTATUSACTIONMAP.get(status);
	}
	
	private String getActionStatus(GenericTenderResponse response)
	{
		final String DEFAULT="Default";
		final char LINE='-';
		StringBuffer status=new StringBuffer();
		status.append(DEFAULT).append(LINE);
		
		//for negotiated statuso
		if(TenderConstants.TENDERRESPONSE_NEGOTIATED.equals(response.getStatus().getCode())){
			List<GenericTenderResponse> negotiationList = getNegotiationDetail(response.getId());
			if(negotiationList == null || negotiationList.isEmpty()){
				status.append(TenderConstants.TENDERRESPONSE_NEGOTIATED);
				status.append(LINE);
			}
			else{
				Boolean checkFlag=Boolean.TRUE;
				for(GenericTenderResponse negotiation: negotiationList){
					if(TenderConstants.TENDERRESPONSE_NEGOTIATED.equals(negotiation.getStatus().getCode())){
						checkFlag=Boolean.FALSE;
						break;
					}
				}
				if(checkFlag){
					status.append(TenderConstants.TENDERRESPONSE_NEGOTIATED);
					status.append(LINE);
				}
			}
			status.append(TenderConstants.NEGOTIATIONNOTICE);
			status.append(LINE);
		}

		//for Justified status
		else if(TenderConstants.TENDERRESPONSE_JUSTIFIED.equals(response.getStatus().getCode()))
		{
			status.append(TenderConstants.JUSTIFICATIONNOTICE);
			status.append(LINE);
			if(getJustificationByResponse(response) == null){
				status.append(TenderConstants.TENDERRESPONSE_JUSTIFIED);
				status.append(LINE);
			}
		}
		return status.toString();
	}
	
	
	public TenderJustification getJustificationByResponse(GenericTenderResponse response)
	{
		Long responseId= getNegotiationDetail(response.getId()).isEmpty()?response.getId():getLatestNegotiation(response.getId()).getId();
		return (TenderJustification)persistenceService.findByNamedQuery(TenderConstants.GETJUSTIFICATIONBYRESPONSE,responseId); 
	}
	
	/**
	 * This method will return the negotiation details for a response
	 * @param parent
	 */
	
	public List<GenericTenderResponse> getNegotiationDetail(Long parent)
	{
		List<GenericTenderResponse> responseLineList=new ArrayList<GenericTenderResponse>();
		GenericTenderResponse latestNegotiation=getLatestNegotiation(parent);
		while(latestNegotiation.getParent()!=null){
			responseLineList.add(latestNegotiation);
			latestNegotiation=latestNegotiation.getParent();
		}
		return responseLineList;
	}
	
	/**
	 * This method is to return latest Negotiation 
	 */
	
	public GenericTenderResponse getLatestNegotiation(Long parent)
	{
		GenericTenderResponse response=getTenderResponseById(parent);
		String query="select max(id) from GenericTenderResponse where tenderUnit.id =:unitId and bidderId=:bidderId and bidderType=:bidderType";
		Query queryObj=persistenceService.getSession().createQuery(query);
		queryObj.setLong("unitId", response.getTenderUnit().getId());
		queryObj.setLong("bidderId",response.getBidderId());
		queryObj.setString("bidderType",response.getBidderType());
		return getTenderResponseById((Long)queryObj.uniqueResult());
	}
	
	private Map<String,List<String>> tenderBidderType=new HashMap<String,List<String>>();
	
	
	public Map<String,List<String>> setTenderTypeAndBidderName(GenericTenderResponse response)
	{
			tenderBidderType = (Map<String, List<String>>) scriptService
					.executeScript(TenderConstants.TENDER_RESPONSE_TENDERTYPE,ScriptService.createContext("notice", response.getNotice()));
		return tenderBidderType;
	}
	
	
	public List<Bidder> populateBidderList(GenericTenderResponse response)
	{
		List<Bidder> bidderList=Collections.emptyList();
		if(tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0)!=null && 
				!TenderConstants.TENDER_OWNER.equals(tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0))){
			BidderTypeService bidderTypeService=tenderCommonService.getBidderService(response.getNotice().getTenderFileType());
			bidderList= (List<Bidder>)bidderTypeService.getAllActiveBidders();
		}
		return bidderList;
	}
	
	
	public TenderService getAsociatedTenderService(String fileType)
	{
		TenderFileType file = (TenderFileType)persistenceService.find("from TenderFileType where id = ?",Long.valueOf(fileType));
		TenderService genericTenderService = tenderServiceMap.get(file.getFileType().concat(TR_TYPE_SERVICE_SFX));
		if(genericTenderService == null)
			genericTenderService = tenderServiceMap.get(DEFAULT.concat(TR_TYPE_SERVICE_SFX));
		return genericTenderService;
	}
	
	
	public void setTenderResponseWorkflowService(
			WorkflowService<GenericTenderResponse> tenderResponseWorkflowService) {
		this.tenderResponseWorkflowService = tenderResponseWorkflowService;
	}
	
	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}
	
	public void setScriptService(ScriptService scriptService) {
		this.scriptService = scriptService;
	}
	
	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}
	
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	public void setTenderCommonService(TenderCommonService tenderCommonService) {
		this.tenderCommonService = tenderCommonService;
	}

	public void setTenderServiceMap(Map<String, TenderService> tenderServiceMap) {
		this.tenderServiceMap = tenderServiceMap;
	}

}
