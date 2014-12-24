package org.egov.tender.services.common;


import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Bidder;
import org.egov.infstr.services.PersistenceService;
import org.egov.tender.interfaces.TenderFile;
import org.egov.tender.interfaces.Tenderable;
import org.egov.tender.interfaces.TenderableGroup;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.model.TenderFileType;
import org.egov.tender.model.TenderNotice;
import org.egov.tender.model.TenderUnit;
import org.egov.tender.utils.TenderConstants;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

@SuppressWarnings("unchecked")
public class GenericTenderService {

	private static final Logger LOGGER = Logger.getLogger(GenericTenderService.class);
	private static final String NOTICE		    	= "notice";
	private static final String TENDERUNIT	    	= "tenderUnit";
	private static final String STATUS	   			= "status";
	private static final String TENDERUNITGROUP		= "tenderUnitGroup";
	private static final String RESPONSE	    	= "response";
	private static final String STATUSCODE	    	= "status.code";
	private static final String RESPONSETENDERUNIT  = "response.tenderUnit";
	private static final String TENDERUNITNOTICE    = "tenderUnit.tenderNotice";
	private PersistenceService persistenceService;
	private TenderCommonService tenderCommonService;

	private Criteria buildCriteriaQuery(TenderableGroup group, Tenderable unit) 
	{
		Criteria tenderCriteria=persistenceService.getSession().createCriteria(TenderNotice.class,NOTICE)
		.createAlias("notice.tenderUnits",TENDERUNIT)
		.createAlias("tenderUnit.tenderableGroups", TENDERUNITGROUP,CriteriaSpecification.LEFT_JOIN)
		.createAlias("tenderUnit.status", STATUS)
		.createAlias("notice.status","noticeStatus")
		.createAlias("tenderUnit.tenderEntities", "entities");

		if(group.getNumber()!=null)
			tenderCriteria.add(Restrictions.eq("tenderUnitGroup.number",group.getNumber()));
		if(unit.getNumber()!=null)
			tenderCriteria.add(Restrictions.eq("entities.number",unit.getNumber()));

		tenderCriteria.add(Restrictions.conjunction()
				.add(Restrictions.ne(STATUSCODE,TenderConstants.TENDERUNIT_CANCELLED))
				.add(Restrictions.ne("noticeStatus.code",TenderConstants.TENDERNOTICE_CANCELLED)));
		return tenderCriteria;
	}

	/**
	 * This method will return total quantity used for each unit in tender notice. 
	 * @param group
	 * @param unit
	 * @return
	 * @throws EGOVRuntimeException
	 */
	public BigDecimal getTenderedQuantity(TenderableGroup group, Tenderable unit)
	{
		LOGGER.info("Tenderable Group is --->"+group);
		if(group!=null && unit!=null)
		{  
			Criteria tenderCriteria = buildCriteriaQuery(group, unit);
			tenderCriteria.setProjection(Projections.sum("entities.requestedQty"));
			return  (BigDecimal)tenderCriteria.uniqueResult();

		}else
			return null;

	}
	
	/**
	 * Use this method to varify the group and unit usage in tender notice. 
	 * If the tender group and unit already used in the tender notice,
	 * then this method will return boolean(true) value.
	 * Here Group and Units are mandatory fields.
	 * @param group
	 * @param unit
	 * @return
	 * @throws EGOVRuntimeException
	 */
	
	public Boolean checkTenderGroupAndUnitAlreadyUsedInTenderNotice(TenderableGroup group, Tenderable unit)
	{
		Boolean flag=Boolean.FALSE;
		if(group!=null && unit!=null)
		{
			Criteria tenderCriteria = buildCriteriaQuery(group, unit);
			tenderCriteria.setProjection(Projections.distinct(Projections.property(STATUSCODE)));
			List resultList=tenderCriteria.list();
			if(!resultList.isEmpty())
				flag= Boolean.TRUE;
		}
		return flag;
	}

	/**
	 * This method will return list of approved tender responses. 
	 * @param tenderFileNumber
	 * @param group Like: Estimate or Indent
	 * @return List of TenderReponses
	 * @throws EGOVRuntimeException
	 */
	
	public List <GenericTenderResponse> getAcceptedTenderResponseByTenderFileNumber(String tenderFileNumber,Integer deptId)
	{
		List<GenericTenderResponse> responseList= Collections.emptyList();
		if(tenderFileNumber!=null)
		{
			Criteria tenderCriteria=persistenceService.getSession().createCriteria(GenericTenderResponse.class,RESPONSE)
			.createAlias(RESPONSETENDERUNIT,TENDERUNIT)
			.createAlias(TENDERUNITNOTICE,NOTICE)
			.createAlias("response.status", STATUS)
			.createAlias("tenderUnit.tenderableGroups", TENDERUNITGROUP,CriteriaSpecification.LEFT_JOIN);

			tenderCriteria.add(Restrictions.eq("notice.tenderFileRefNumber",tenderFileNumber));
			if(deptId!=null)
				tenderCriteria.add(Restrictions.eq("notice.department.id",deptId));

			tenderCriteria.add(Restrictions.eq(STATUSCODE,TenderConstants.TENDERRESPONSE_ACCEPTED));
			tenderCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			responseList= (List<GenericTenderResponse>)tenderCriteria.list();
		}
		return responseList;
	}
	
	/**
	 * This method will return list of approved tender responses. 
	 * @param tenderFileNumber
	 * @param group Like: Estimate or Indent
	 * @return List of TenderReponses
	 * @throws EGOVRuntimeException
	 */
	
	public List <GenericTenderResponse> getAcceptedTenderResponse(String tenderFileNumber, TenderableGroup group)
	{
		List<GenericTenderResponse> resultList=Collections.emptyList();

		if(tenderFileNumber!=null)
		{
			Criteria tenderCriteria=persistenceService.getSession().createCriteria(GenericTenderResponse.class,RESPONSE)
			.createAlias(RESPONSETENDERUNIT,TENDERUNIT)
			.createAlias(TENDERUNITNOTICE  , NOTICE)
			.createAlias("response.status" , STATUS)
			.createAlias("tenderUnit.tenderableGroups", TENDERUNITGROUP,CriteriaSpecification.LEFT_JOIN);

			if(tenderFileNumber!=null)
				tenderCriteria.add(Restrictions.eq("notice.tenderFileRefNumber",tenderFileNumber));
			if( group!=null && group.getNumber()!=null)
				tenderCriteria.add(Restrictions.eq("tenderUnitGroup.number",group.getNumber()));
			
			tenderCriteria.add(Restrictions.eq(STATUSCODE,TenderConstants.TENDERRESPONSE_ACCEPTED));
			tenderCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			resultList= (List<GenericTenderResponse>)tenderCriteria.list();
		}
		return resultList;
	}
	
	
	public List <GenericTenderResponse> getAcceptedTenderResponse(TenderableGroup group)
	{
		List<GenericTenderResponse> resultList=Collections.emptyList();
		
			if( group!=null && group.getNumber()!=null)
			{

				Criteria tenderCriteria=persistenceService.getSession().createCriteria(GenericTenderResponse.class,RESPONSE)
				.createAlias(RESPONSETENDERUNIT,TENDERUNIT)
				.createAlias(TENDERUNITNOTICE,NOTICE)
				.createAlias("response.status", STATUS)
				.createAlias("tenderUnit.tenderableGroups", TENDERUNITGROUP,CriteriaSpecification.LEFT_JOIN);

				tenderCriteria.add(Restrictions.eq("tenderUnitGroup.number",group.getNumber()));
				tenderCriteria.add(Restrictions.eq(STATUSCODE,TenderConstants.TENDERRESPONSE_ACCEPTED));
				tenderCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
				resultList=(List<GenericTenderResponse>)tenderCriteria.list();

			}

		

		return resultList;
	}
	/**
	 * Method used to get List of Generic Tender Responses by passing tender file number, tender group and Bidder Code.
	 * Using tender file number bidder type will be decided.  
	 * @param tenderFileNumber
	 * @param group
	 * @param bidderCode
	 * @return
	 * @throws EGOVRuntimeException
	 */
	public List <GenericTenderResponse> getAcceptedTenderResponse(String tenderFileNumber, TenderableGroup group,String bidderCode)
	{
		List<GenericTenderResponse> resultList=Collections.emptyList();
					
			if( group!=null && group.getNumber()!=null)
			{

				Criteria tenderCriteria=persistenceService.getSession().createCriteria(GenericTenderResponse.class,RESPONSE)
				.createAlias(RESPONSETENDERUNIT,TENDERUNIT)
				.createAlias(TENDERUNITNOTICE,NOTICE)
				.createAlias("response.status", STATUS) 
				.createAlias("tenderUnit.tenderableGroups",TENDERUNITGROUP,CriteriaSpecification.LEFT_JOIN);


				TenderFileType fileType=tenderCommonService.getTenderFileTypeBypassingTenderFileNumber(tenderFileNumber);
				if(fileType!=null){
					Bidder bidder= tenderCommonService.getBidder( tenderFileNumber, bidderCode);

					if(tenderFileNumber!=null)
						tenderCriteria.add(Restrictions.eq("notice.tenderFileRefNumber",tenderFileNumber));

					if(group.getNumber()!=null)
						tenderCriteria.add(Restrictions.eq("tenderUnitGroup.number",group.getNumber()));

					if(bidderCode!=null && bidder!=null) 
					{
						tenderCriteria.add(Restrictions.eq("bidderId",Long.valueOf(bidder.getBidderId()))); 
						tenderCriteria.add(Restrictions.eq("bidderType",fileType.getBidderType()));   
					}
					tenderCriteria.add(Restrictions.eq(STATUSCODE,TenderConstants.TENDERRESPONSE_ACCEPTED));
					tenderCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
					resultList=(List <GenericTenderResponse>)tenderCriteria.list();
				}
			}

			return resultList;
	}


	/**
	 * This method will return tender notice object. Tender file number is the input parameter.
	 * @param tenderFile
	 * @return
	 */
	public TenderNotice getTenderNoticeByTenderFile(TenderFile tenderFile)
	{
		if(tenderFile!=null)
			return (TenderNotice)persistenceService.find("from TenderNotice where tenderFileRefNumber=?",tenderFile.getFileNumber());
		else
			return null;
	}
	
	/**
	 * This method will return list of responses for an unit irrespective of their status.
	 * @param unit
	 * @return
	 */
	
	public List<GenericTenderResponse> getAllTenderResponsesByTenderUnit(TenderUnit unit) 
	{
		Criteria tenderCriteria=persistenceService.getSession().createCriteria(GenericTenderResponse.class,RESPONSE)
								.createAlias(RESPONSETENDERUNIT,TENDERUNIT);

		if(unit!=null)
			tenderCriteria.add(Restrictions.eq("tenderUnit.id",unit.getId()));
		tenderCriteria.addOrder(Order.desc("bidderId"));

		return (List<GenericTenderResponse>)tenderCriteria.list();

	}
	/**
	 * This method used to get distinct tender response by passing tender unit object.
	 * If the render response has multiple negotiation detail, then latest negotiation detail will be selected using this query.
	 * @param unit
	 * @return
	 */ 
	public List<GenericTenderResponse> getDistinctTenderResponsesByPassingTenderUnit(TenderUnit unit) 
	{
		String query= "select distinct resp from GenericTenderResponse resp where resp.tenderUnit.id=:unitId and resp.id not in (select distinct (subResp.parent.id) from GenericTenderResponse subResp where subResp.parent is not null)";
		Query queryObj=persistenceService.getSession().createQuery(query);	
		if(unit!=null)
			queryObj.setLong("unitId",unit.getId());
		else
		{
			throw new EGOVRuntimeException("Unit id is mandatory");
		}
		return (List<GenericTenderResponse>)queryObj.list();

	}

	/**
	 * This Api is to get Accepted GenericTenderResponse By passing responseNumber
	 * @param number
	 * @return
	 */
	
	public GenericTenderResponse getGenericResponseByNumber(String responseNumber)
	{
		return (GenericTenderResponse)persistenceService.find("from GenericTenderResponse where number = ? and status.moduletype=? and status.code=?",
				responseNumber,TenderConstants.TENDERRESPONSEMODULE,TenderConstants.TENDERRESPONSE_ACCEPTED);
	}
	
	/**
	 * This api is to get Accepted GenericTenderResponse by passing id.
	 * @param responseId
	 * @return
	 */
	
	public GenericTenderResponse getGenericResponseById(Long responseId)
	{
		if(responseId == null)
			throw new EGOVRuntimeException("GenericTenderResponse id is null");
		return (GenericTenderResponse)persistenceService.find("from GenericTenderResponse where id = ? and status.moduletype=? and status.code=?",
				responseId,TenderConstants.TENDERRESPONSEMODULE,TenderConstants.TENDERRESPONSE_ACCEPTED);
	}
	
	
	public void setTenderCommonService(TenderCommonService tenderCommonService) {
		this.tenderCommonService = tenderCommonService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public TenderFile getTenderFileByFiletypeAndNumber(TenderFileType fileType,String fileNumber)
	{
		TenderFile tenderFile=null;
		if(fileType!=null&&fileNumber!=null)
		 tenderFile=(TenderFile)persistenceService.find("from " +fileType.getFullClassName()+" where fileNumber=?",fileNumber);
		return tenderFile;
}

	
	
	/**
	 * This Api is to return bid accepted date of tender response
	 * @param responseNumber - responseNumber of GenericTenderResponse
	 * @return
	 */
	
	public Date getBidAcceptedDate(String responseNumber){
		
		GenericTenderResponse tenderResponse = getGenericResponseByNumber(responseNumber);
		if(tenderResponse!=null){
			return tenderResponse.getBidAcceptedDate();
}
		return null;
	}


}
