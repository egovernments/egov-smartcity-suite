package org.egov.tender.services.common;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.tender.interfaces.TenderFile;
import org.egov.tender.interfaces.Tenderable;
import org.egov.tender.interfaces.TenderableGroup;
import org.egov.tender.model.TenderNotice;
import org.egov.tender.utils.TenderConstants;
import org.egov.web.utils.EgovPaginatedList;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * @author pritiranjan
 *
 */

@SuppressWarnings("unchecked")
public class GenericTenderFileService  implements TenderFileService{

	private static final Logger LOGGER      = Logger.getLogger(GenericTenderFileService.class);
	private static final String EMPTYSTRING = "";
	private static final String SINGLEQUOTE = "'";
	private static final String TENDERFILETYPENOTFOUND = "tenderfiletype.not.found";
	private PersistenceService persistenceService;
	private Class<?> tenderFileType;
	

	/**
	 * {@inheritDoc}
	 */
	
	public EgovPaginatedList getAllTenderFilesToCreateTenderNotice(Map<String,Object> paramMap,int pageNumber,int pageSize)
	{
		LOGGER.info("inside getAllTenderFilesToCreateTenderNotice method");
		
		if(tenderFileType==null)
			throw new EGOVRuntimeException(TENDERFILETYPENOTFOUND);
		
		StringBuffer query=new StringBuffer("from ");
		query.append(tenderFileType.getName())
			 .append(" tfile where  not exists (Select tn from TenderNotice tn WHERE tn.tenderFileRefNumber = tfile.fileNumber and tn.status.code not in('Cancelled','Retendered'))") 
			 .append(paramMap.containsKey(TenderConstants.STATUS)?" and tfile.status.code = '"+(String)paramMap.get(TenderConstants.STATUS)+SINGLEQUOTE:EMPTYSTRING)
			 .append(paramMap.containsKey(TenderConstants.DEPARTMENT)? " and tfile.department.id = "+(Integer)paramMap.get(TenderConstants.DEPARTMENT):EMPTYSTRING)
			 .append(paramMap.containsKey(TenderConstants.TENDERFILENUMBER)?" and upper(tfile.fileNumber) like '%"+((String)paramMap.get(TenderConstants.TENDERFILENUMBER)).toUpperCase()+"%'":EMPTYSTRING)
		     .append(paramMap.containsKey(TenderConstants.FROMDATE)? " and to_char(tfile.tenderDate,'dd/MM/yyyy') >='"+DateUtils.getFormattedDate((Date)paramMap.get(TenderConstants.FROMDATE),TenderConstants.DATEPATTERN)+SINGLEQUOTE:EMPTYSTRING)
		     .append(paramMap.containsKey(TenderConstants.TODATE)?" and to_char(tfile.tenderDate,'dd/mm/yyyy') <='"+DateUtils.getFormattedDate((Date)paramMap.get(TenderConstants.TODATE),TenderConstants.DATEPATTERN)+SINGLEQUOTE:EMPTYSTRING);
		
		LOGGER.info("Filtered Query for tender File is----> "+query.toString());
		Query queryObj=persistenceService.getSession().createQuery(query.toString());
		Page unitPage=new Page(queryObj,pageNumber,pageSize);
		return new EgovPaginatedList(unitPage,queryObj.list().size());
	}
	

	/**
	 * {@inheritDoc}
	 */
	
	public TenderFile getTenderFileById(Long id)
	{
		if(tenderFileType == null)
			throw new EGOVRuntimeException(TENDERFILETYPENOTFOUND);
		
		return (TenderFile) persistenceService.find("from "+tenderFileType.getName()+" where id=?",id);
		
	}
	
	/*public BigDecimal getTenderedQuantity(TenderableGroup group, Tenderable unit)
	{
		if(tenderFileType==null)
			throw new EGOVRuntimeException(TENDERFILETYPENOTFOUND);
		
		return BigDecimal.TEN;
	}*/
	  public BigDecimal getTenderedQuantity(TenderableGroup group, Tenderable unit)
	    {
		 /* TenderFileType tenderFileType=null;
			
			tenderFileType=(TenderFileType) persistenceService.find("from TenderFileType where fileType=?","RateContractTender");
				
	        if(tenderFileType==null)
	            throw new EGOVRuntimeException(TENDERFILETYPENOTFOUND);
	      */  
	       /* StringBuffer queryStr=new StringBuffer(" select sum(entity.quantity) from ")   
	        .append(tenderFileType.getClass().getName())
	        .append(" as tenderFile left join tenderFile.tenderGroups as tenderGroup " )
	        .append(" join tenderFile.tenderEntities  as entity ")      
	        .append(" where tenderGroup.requisitionNo = :groupNumber")           
	        .append(" and entity.egItem.id = :unitNumber") 
	        .append(" and tenderFile.fileNumber not in ")
	        .append("(select notice.tenderFileRefNumber from TenderNotice as notice join notice.tenderUnits as tenderUnit ")
	        .append(" join tenderUnit.tenderableGroups tenderUnitGroup where tenderUnitGroup.number = :groupNumber ")
	        .append(" and tenderUnit.status.code = 'CANCELLED')");*/
	        //.append("");
	      
		  /*StringBuffer queryStr=new StringBuffer("select sum(requestedQty) ")
	        .append(" from TenderNotice as notice join notice.tenderUnits as tenderUnit left join tenderUnit.tenderableGroups as tenderUnitGroup  join tenderUnit.tenderEntities as entities ")
	        .append("  where tenderUnitGroup.number = :groupNumber ")
	        .append(" and  entities.number=:unitNumber and tenderUnit.status.code = 'CANCELLED'");
	         
	        
	        Query query=persistenceService.getSession().createQuery(queryStr.toString());
	        query.setString("groupNumber", group.getNumber());
	        query.setString("unitNumber", unit.getNumber());
	          Long ret = (Long)query.uniqueResult();
	        return BigDecimal.valueOf(ret);
	      */
		  Criteria tenderCriteria=persistenceService.getSession().createCriteria(TenderNotice.class,"notice")
			.createAlias("notice.tenderUnits","tenderUnit")
			.createAlias("tenderUnit.tenderableGroups", "tenderUnitGroup",CriteriaSpecification.LEFT_JOIN)
			.createAlias("tenderUnit.status", "status")
			.createAlias("tenderUnit.tenderEntities", "entities");
		  tenderCriteria.setProjection(Projections.sum("entities.requestedQty"));
		  
		  tenderCriteria.add(Restrictions.eq("tenderUnitGroup.number",group.getNumber()));
		  tenderCriteria.add(Restrictions.eq("entities.number",unit.getNumber()));
		  tenderCriteria.add(Restrictions.eq("status.code","CANCELLED"));
		  
		  return (BigDecimal)tenderCriteria.uniqueResult();
	       // return ret;
	      //return null;
	    }

	public void setTenderFileType(Class<?> tenderFileType) {
		this.tenderFileType = tenderFileType;
	}

	public PersistenceService getPersistenceService() {
		return persistenceService;
	}


	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
}
