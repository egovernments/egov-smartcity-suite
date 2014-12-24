package org.egov.tender.services.common;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Bidder;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.infstr.utils.StringUtils;
import org.egov.tender.BidType;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.model.TenderNotice;
import org.egov.tender.model.TenderResponseLine;
import org.egov.tender.model.TenderUnit;
import org.egov.tender.model.TenderableEntity;
import org.egov.tender.utils.TenderConstants;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;


public class DefaultTenderService implements TenderService{
	
	private PersistenceService persistenceService;
	private ScriptService scriptService;
	private SequenceGenerator sequenceGenerator;
	public static final String DEPARTMENT = "department";
	public static final String FILENUMBER = "fileNumber";

	public Map<String,String> getApprovedEntityForBidResponse(String bidResponseNumber){
		Map<String,String> entityMap = new HashMap<String,String>();
		entityMap.put(TenderConstants.TYPE, "XYZ");
		entityMap.put(TenderConstants.NUMBER, "ABC");
		return entityMap;
	}
	
	/**
	 * This Api returns list of tender notices for a particular department and tender file number.
	 * @param paramMap
	 * @return
	 */

	public List<TenderNotice> searchTenderNotice(Map<String,Object> paramMap)
	{
		Criteria noticeCriteria = persistenceService.getSession().createCriteria(TenderNotice.class);

		if(null != paramMap.get(DEPARTMENT))
			noticeCriteria.add(Restrictions.eq("department.id",(Integer)paramMap.get(DEPARTMENT)));

		if(null != paramMap.get(FILENUMBER) && StringUtils.isNotEmpty((String)paramMap.get(FILENUMBER)))
			noticeCriteria.add(Restrictions.eq("tenderFileRefNumber",(String)paramMap.get(FILENUMBER)));

		return noticeCriteria.list();
	}


	public TenderNotice getTenderNoticeByNumber(String number)
	{
		Criteria noticeCriteria = persistenceService.getSession().createCriteria(TenderNotice.class);
		noticeCriteria.createAlias("status", "statusObj");
		noticeCriteria.add(Restrictions.eq("number", number));
		noticeCriteria.add(Restrictions.eq("statusObj.code", TenderConstants.TENDERNOTICE_APPROVED));
		List<TenderNotice> noticeList = noticeCriteria.list();

		if(noticeList.isEmpty())
			return null;
		else
			return noticeList.get(0);
	}
	
	
	/**
	 * This method is to validate any response exists for a supplier and unit
	 * @param id
	 * @param bidderId
	 * @param bidderType
	 * @param unit
	 * @return
	 */
	
	public Boolean checkUniqueResponseForBidder(Long bidderId,String bidderType,TenderUnit unit)
	{
		Criteria criteria= persistenceService.getSession().createCriteria(GenericTenderResponse.class)
														  .createAlias("status", "statusObj");
		
		criteria.add(Restrictions.eq("bidderId", bidderId));
		criteria.add(Restrictions.eq("bidderType", bidderType));
		criteria.add(Restrictions.eq("tenderUnit", unit));
		criteria.add(Restrictions.ne("statusObj.code", TenderConstants.TENDERRESPONSE_REJECTED));
		criteria.add(Restrictions.ne("statusObj.code", TenderConstants.TENDERRESPONSE_CANCELLED));
		criteria.add(Restrictions.isNull("parent"));
		return !criteria.list().isEmpty();
	}
	
	
	public GenericTenderResponse saveResponse(String noticeNumber,Bidder contractor,BigDecimal percentage)
	{
		TenderNotice tenderNotice      = getTenderNoticeByNumber(noticeNumber);
		Set<TenderUnit> tenderUnits    = tenderNotice.getTenderUnits();
		GenericTenderResponse response = new GenericTenderResponse();
		response.setResponseDate(DateUtils.today());
		response.setBidType(BidType.PERCENTAGE);
		response.setBidderType(tenderNotice.getTenderFileType().getBidderType());
		response.setBidder(contractor);
		response.setBidderId(contractor.getBidderId().longValue());
		response.setPercentage(percentage);
		BigDecimal bidRate = BigDecimal.ZERO;

		response.setStatus((EgwStatus)persistenceService.find(TenderConstants.STATUSQUERY,TenderConstants.TENDERRESPONSEMODULE,
				TenderConstants.TENDERRESPONSE_ACCEPTED));
		Set<TenderResponseLine> responseLineSet = new HashSet<TenderResponseLine>();

		for(TenderUnit tenderUnit:tenderUnits){
			response.setTenderUnit(tenderUnit);
			Set<TenderableEntity> tenderEntitySet = tenderUnit.getTenderEntities();
			TenderResponseLine responseLine;

			for(TenderableEntity tenderableEntity:tenderEntitySet){
				responseLine = new TenderResponseLine();
				responseLine.setQuantity(tenderableEntity.getRequestedQty());
				responseLine.setTenderableEntity(tenderableEntity);
				responseLine.setBidRate(tenderableEntity.getRequestedQty().add(
						tenderableEntity.getRequestedQty().multiply(
								percentage.divide(new BigDecimal("100"))
								.setScale(4, BigDecimal.ROUND_HALF_UP)).setScale(4, BigDecimal.ROUND_HALF_UP)));
				responseLine.setTenderResponse(response);
				responseLine.setModifiedDate(DateUtils.today());
				bidRate = bidRate.add(responseLine.getBidRate());
				responseLineSet.add(responseLine);
			}

			response.setBidValue(bidRate);
			break;
		}

		response.setNumber(getTenderResponseNumber(response,Boolean.TRUE));
		response.getResponseLines().clear();
		response.getResponseLines().addAll(responseLineSet);
		persistenceService.setType(GenericTenderResponse.class);
		response = (GenericTenderResponse) persistenceService.persist(response);
		return response;
	}
	
	
	private String getTenderResponseNumber(GenericTenderResponse response,Boolean flag)
	{
		String sflag;
		FinancialYearDAO finYearDAO=CommonsDaoFactory.getDAOFactory().getFinancialYearDAO();
		CFinancialYear finyear =  finYearDAO.getFinYearByDate(response.getResponseDate());
		if(finyear==null)
			throw new EGOVRuntimeException("Financial year does not Exist.");
		sflag=flag?"true":"false";
		Calendar cal = Calendar.getInstance();
		cal.setTime(response.getResponseDate());
		return (String)scriptService.executeScript(TenderConstants.TENDERRESPONSEGENERATORSCRIPT, 
				ScriptService.createContext("response",response,"finYear",finyear,"sequenceGenerator",sequenceGenerator,"sflag",sflag,"month",String.valueOf(cal.get(Calendar.MONTH)+1)));
	}
	

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	public void setScriptService(ScriptService scriptService) {
		this.scriptService = scriptService;
	}
	

}
