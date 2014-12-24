package org.egov.works.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.tender.interfaces.Tenderable;
import org.egov.tender.interfaces.TenderableGroup;
import org.egov.tender.utils.TenderConstants;
import org.egov.web.utils.EgovPaginatedList;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.rateContract.Indent;
import org.egov.works.models.rateContract.RateContract;
import org.egov.works.models.tender.TenderFile;
import org.egov.works.models.tender.TenderFileDetail;
import org.egov.works.models.tender.TenderFileNumberGenerator;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.TenderFileService;
import org.hibernate.Query;

public class TenderFileServiceImpl extends BaseServiceImpl<TenderFile,Long> implements TenderFileService{
	private TenderFileNumberGenerator tenderFileNumberGenerator;
	
	private static final String EMPTYSTRING = "";
	private static final String SINGLEQUOTE = "'";
	private static final String TENDERFILETYPENOTFOUND = "tenderfiletype.not.found";
	private static final String TENDERABLE_GROUP_ESTIMATE = "ESTIMATE";
	private PersistenceService persistenceService;
	private Class<?> tenderFileType;
	private Long bidResponseId;
	List <WorkOrder> approvedWOList = new ArrayList<WorkOrder>();
	public TenderFileServiceImpl(PersistenceService<TenderFile, Long> persistenceService) {
		super(persistenceService);
	}
	
	public void setTenderFileNumber(TenderFile entity,CFinancialYear finYear) {
		if(entity.getFileNumber()==null) {
			entity.setFileNumber(tenderFileNumberGenerator.getTenderFileNumber(entity,finYear));
		}
	}
	
	public List<AbstractEstimate> getAbstractEstimateListByTenderFile(TenderFile entity) {
		List<AbstractEstimate> abList = new ArrayList<AbstractEstimate>();
		if(entity!=null && !entity.getTenderFileDetails().isEmpty())
		{
			for(TenderFileDetail tfd:entity.getTenderFileDetails())
				if(tfd.getAbstractEstimate()!=null)
					abList.add(tfd.getAbstractEstimate());
		}
		return abList;
	}

	public void setTenderFileNumberGenerator(
			TenderFileNumberGenerator tenderFileNumberGenerator) {
		this.tenderFileNumberGenerator = tenderFileNumberGenerator;
	}
	
	public EgovPaginatedList getAllTenderFilesToCreateTenderNotice(Map<String,Object> paramMap,int pageNumber,int pageSize) {
		if(tenderFileType==null)
			throw new EGOVRuntimeException(TENDERFILETYPENOTFOUND);
		
		StringBuffer query=new StringBuffer("from ");
		query.append(tenderFileType.getName())
			 .append(" tfile where  not exists (Select tn from TenderNotice tn WHERE tn.tenderFileRefNumber = tfile.fileNumber and tn.status.code not in('Cancelled','Retendered'))") 
			 .append(paramMap.containsKey(TenderConstants.TENDERFILEGROUPTYPE)?((((String)paramMap.get(TenderConstants.TENDERFILEGROUPTYPE))).equalsIgnoreCase(TENDERABLE_GROUP_ESTIMATE)? 
					 " and exists (Select 'true' from TenderFileDetail tfd WHERE tfd.tenderFile.id = tfile.id and tfd.indent is null)":" and exists (Select 'true' from TenderFileDetail tfd WHERE tfd.tenderFile.id = tfile.id and tfd.indent is not null)"):EMPTYSTRING)
			 .append(paramMap.containsKey(TenderConstants.STATUS)?" and tfile.egwStatus.code = '"+((String)paramMap.get(TenderConstants.STATUS)).toUpperCase()+SINGLEQUOTE:EMPTYSTRING)
			 .append(paramMap.containsKey(TenderConstants.DEPARTMENT)? " and tfile.department.id = "+(Integer)paramMap.get(TenderConstants.DEPARTMENT):EMPTYSTRING)
			 .append(paramMap.containsKey(TenderConstants.TENDERFILENUMBER)?" and upper(tfile.fileNumber) like '%"+((String)paramMap.get(TenderConstants.TENDERFILENUMBER)).toUpperCase()+"%'":EMPTYSTRING)
		     .append(paramMap.containsKey(TenderConstants.FROMDATE)? " and to_char(tfile.fileDate,'dd/MM/yyyy') >='"+DateUtils.getFormattedDate((Date)paramMap.get(TenderConstants.FROMDATE),TenderConstants.DATEPATTERN)+SINGLEQUOTE:EMPTYSTRING)
		     .append(paramMap.containsKey(TenderConstants.TODATE)?" and to_char(tfile.fileDate,'dd/mm/yyyy') <='"+DateUtils.getFormattedDate((Date)paramMap.get(TenderConstants.TODATE),TenderConstants.DATEPATTERN)+SINGLEQUOTE:EMPTYSTRING);
		
		Query queryObj=persistenceService.getSession().createQuery(query.toString());
		Page unitPage=new Page(queryObj,pageNumber,pageSize);
		return new EgovPaginatedList(unitPage,queryObj.list().size());
	}

	
	public TenderFile getTenderFileById(Long id) {
		if(tenderFileType == null)
			throw new EGOVRuntimeException(TENDERFILETYPENOTFOUND);
		
		return (TenderFile) persistenceService.find("from "+tenderFileType.getName()+" where id=?",id);		
	}
	
	public BigDecimal getTenderedQuantity(TenderableGroup group, Tenderable unit) {
		if(tenderFileType==null)
			throw new EGOVRuntimeException(TENDERFILETYPENOTFOUND);
		
		return BigDecimal.TEN;
	}

	public List<Indent> getIndentListByTenderFile(TenderFile entity) {
		List<Indent> abList = new ArrayList<Indent>();
		if(entity!=null && !entity.getTenderFileDetails().isEmpty())
		{
			for(TenderFileDetail tfd:entity.getTenderFileDetails())
				if(tfd.getIndent()!=null)
					abList.add(tfd.getIndent());
		}
		return abList;
	}
	
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void setTenderFileType(Class<?> tenderFileType) {
		this.tenderFileType = tenderFileType;
	}
	
	
	public Map<String,String> getApprovedEntityForBidResponse(String bidResponseNumber){
		HashMap<String,String> details=new HashMap<String,String>();
		WorkOrder workOrder = (WorkOrder) persistenceService.find(" from WorkOrder wo where wo.negotiationNumber=? and wo.egwStatus.code<>'CANCELLED'", bidResponseNumber);
		if(workOrder!=null) {
			details.put("type", "WorkOrder");
			details.put("number",workOrder.getWorkOrderNumber());
		}
		else {
			RateContract rateContract=(RateContract)persistenceService.find("from RateContract rc where rc.responseNumber=? and rc.egwStatus.code<>'CANCELLED'", bidResponseNumber);
			if(rateContract!=null) {
				details.put("type","RateContract");
				details.put("number",rateContract.getRcNumber());
			}
		}
		return details;
	}

	
	public Long getBidResponseId() {
		return bidResponseId;
	}

	public void setBidResponseId(Long bidResponseId) {
		this.bidResponseId = bidResponseId;
	}

	public List<WorkOrder> getApprovedWOList() {
		return approvedWOList;
	}

	public void setApprovedWOList(List<WorkOrder> approvedWOList) {
		this.approvedWOList = approvedWOList;
	}
	
}
