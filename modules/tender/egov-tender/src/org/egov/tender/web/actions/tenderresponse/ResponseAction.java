package org.egov.tender.web.actions.tenderresponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.egov.commons.service.BidderTypeService;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.tender.BidType;
import org.egov.tender.interfaces.Tenderable;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.model.TenderResponseLine;
import org.egov.tender.model.TenderResponseLineComparator;
import org.egov.tender.model.TenderableEntity;
import org.egov.tender.services.tenderresponse.TenderResponseService;
import org.egov.tender.utils.TenderConstants;
import org.egov.tender.utils.TenderUtils;
import org.egov.tender.web.actions.common.TenderWorkFlowAction;

/**
 * 
 * @author pritiranjan
 *
 */

@SuppressWarnings("serial")
public abstract class ResponseAction extends TenderWorkFlowAction{
	
	protected TenderResponseService tenderResponseService;
	protected BidderTypeService bidderTypeService;
	protected GenericTenderResponse tenderResponse  = new GenericTenderResponse();
	protected List<TenderResponseLine> responseLineList=new LinkedList<TenderResponseLine>();
	protected List<GenericTenderResponse> negotiationDetails=new LinkedList<GenericTenderResponse>();
	protected Map<String,List<String>> tenderBidderType=new HashMap<String,List<String>>();
	protected List<Double> estimatedRate=new ArrayList<Double>();
	protected Long tenderUnitId;
	//flag for auto generation of tender response number.
	protected boolean autoGenResponse;
	protected String tenderType;
	protected String sign;
	protected List<String> uomName=new ArrayList<String>();
	protected List<String> item=new ArrayList<String>();
	protected List<Double> totalBidRate= new ArrayList<Double>();
	protected List<Double> totalEstimatedRate = new ArrayList<Double>();
	
	
	public GenericTenderResponse getModel()
	{
		return tenderResponse;
	}
	
	@SuppressWarnings("unchecked")
	public void buildTenderResponse()
	{
		if(tenderType ==null || "".equals(tenderType))
			tenderResponse.setBidType(BidType.RATE);
		else
			tenderResponse.setBidType(BidType.valueOf(tenderType.toUpperCase()));

		if(tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0)!=null && 
				TenderConstants.TENDER_OWNER.equals(tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0))){
			tenderResponse.getOwner().getAddressSet().clear();
			tenderResponse.getOwner().getAddressSet().add(tenderResponse.getBidderAddress());
			tenderCommonService.saveOwner(tenderResponse.getOwner());
			tenderResponse.setBidder(tenderResponse.getOwner());
			tenderResponse.setBidderId(tenderResponse.getOwner().getId().longValue());
		}
		tenderResponse.setBidderType(tenderResponse.getNotice().getTenderFileType().getBidderType());

		if("-".equals(sign))
			tenderResponse.setPercentage(tenderResponse.getPercentage().negate());
		buildTenderResponseLines();

	}
	
	public void buildTenderResponseLines()
	{
		Set<TenderResponseLine> responseLineSet = new HashSet<TenderResponseLine>();
		Collections.sort(responseLineList,new TenderResponseLineComparator());
		BigDecimal bidValue=BigDecimal.ZERO;
        //modify mode
		if(!tenderResponse.getResponseLines().isEmpty() && !responseLineList.isEmpty()){
			ListIterator<TenderResponseLine> responseLineIterator=responseLineList.listIterator();
			
			while(responseLineIterator.hasNext()){
				TenderResponseLine newResponseLine=responseLineIterator.next();
                List<TenderResponseLine> oldResponseLineList = new ArrayList<TenderResponseLine>(tenderResponse.getResponseLines());
                Collections.sort(oldResponseLineList,new TenderResponseLineComparator());
				for(TenderResponseLine oldResponseLine:oldResponseLineList){
					if(newResponseLine.getId()!=null && newResponseLine.getId().equals(oldResponseLine.getId())){
						oldResponseLine.setTenderResponse(tenderResponse);
						oldResponseLine.setTenderableEntity((TenderableEntity)persistenceService.find(" from TenderableEntity where id=?",newResponseLine.getTenderableEntity().getId()));

						if(newResponseLine.getUom().getId()==null){
							oldResponseLine.setUom(null);
						}
						else{
							oldResponseLine.setUom((EgUom)persistenceService.find("from  EgUom where id=?",newResponseLine.getUom().getId()));
							oldResponseLine.setBidRate(newResponseLine.getBidRate()==null?
									BigDecimal.ZERO:newResponseLine.getBidRate().divide(oldResponseLine.getUom().getConvFactor(),10,BigDecimal.ROUND_HALF_UP));
							if(newResponseLine.getQuantity()!=null)
								oldResponseLine.setQuantity(newResponseLine.getQuantity().multiply(oldResponseLine.getUom().getConvFactor()));

						}
						oldResponseLine.setModifiedDate(new Date());
						//bidValue=bidValue.add( (oldResponseLine.getBidRate()==null) ? BigDecimal.ZERO:oldResponseLine.getBidRate());
						bidValue=bidValue.add( (oldResponseLine.getBidRate()==null) ?
								BigDecimal.ZERO: oldResponseLine.getQuantity()== null ?
										oldResponseLine.getBidRate():oldResponseLine.getBidRate().multiply(oldResponseLine.getQuantity()));
						responseLineSet.add(oldResponseLine);
						responseLineIterator.remove();
					}
				}
			}
		}
		//create mode
		if(!responseLineList.isEmpty()){
			for(TenderResponseLine responseLine:responseLineList){
				responseLine.setTenderResponse(tenderResponse);
				responseLine.setTenderableEntity((TenderableEntity)persistenceService.find(" from TenderableEntity where id=?",responseLine.getTenderableEntity().getId()));

				if(responseLine.getUom().getId()==null){
					responseLine.setUom(null);
				}
				else{
					responseLine.setUom((EgUom)persistenceService.find("from  EgUom where id=?",responseLine.getUom().getId()));
					responseLine.setBidRate(responseLine.getBidRate()==null?
							BigDecimal.ZERO:responseLine.getBidRate().divide(responseLine.getUom().getConvFactor(),10,BigDecimal.ROUND_HALF_UP));
					if(responseLine.getQuantity()!=null)
						responseLine.setQuantity(responseLine.getQuantity().multiply(responseLine.getUom().getConvFactor()));

				}
				responseLine.setModifiedDate(new Date());
				bidValue=bidValue.add( (responseLine.getBidRate()==null) ?
						BigDecimal.ZERO: responseLine.getQuantity()== null ?
								responseLine.getBidRate():responseLine.getBidRate().multiply(responseLine.getQuantity()));
				responseLineSet.add(responseLine);
			}
		}
		
		tenderResponse.setBidValue(bidValue);
		tenderResponse.getResponseLines().clear();
		tenderResponse.getResponseLines().addAll(responseLineSet);
	}
	
	
	protected void buildTenderBeforeViewAndModify()
	{
		
		responseLineList=new ArrayList<TenderResponseLine>(tenderResponse.getResponseLines());
		Collections.sort(responseLineList,new TenderResponseLineComparator());
		if(tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0)!=null && 
				TenderConstants.TENDER_OWNER.equals(tenderBidderType.get(TenderConstants.TENDER_BIDDERTYPE).get(0)))
			tenderResponse.setOwner(tenderCommonService.getOwnerById(tenderResponse.getBidderId().intValue()));
		setTenderType(TenderUtils.initCapString(tenderResponse.getBidType().name()));
		if(tenderResponse.getCurrentState()!=null && tenderResponse.getCurrentState().getPrevious()!=null)
			 setComments( tenderResponse.getCurrentState().getPrevious().getText2());
		
		if(tenderResponse.getPercentage()!=null && BigDecimal.ZERO.compareTo(tenderResponse.getPercentage())>0)
			setSign("-");
		else 
			setSign("+");
	}
	
	public Long getTenderUnitId() {
		return tenderUnitId;
	}

	public void setTenderUnitId(Long tenderUnitId) {
		this.tenderUnitId = tenderUnitId;
	}
	
	public List<TenderResponseLine> getResponseLineList() {
		return responseLineList;
	}

	public void setResponseLineList(List<TenderResponseLine> responseLineList) {
		this.responseLineList = responseLineList;
	}

	public boolean isAutoGenResponse() {
		return autoGenResponse;
	}

	public void setAutoGenResponse(boolean autoGenResponse) {
		this.autoGenResponse = autoGenResponse;
	}
	
	public List<String> getItem() {
		return item;
	}

	public void setItem(List<String> item) {
		this.item = item;
	}
	
	public Map<String, List<String>> getTenderBidderType() {
		return tenderBidderType;
	}

	public List<Double> getEstimatedRate() {
		return estimatedRate;
	}

	public void setEstimatedRate(List<Double> estimatedRate) {
		this.estimatedRate = estimatedRate;
	}
	
	public void setTenderResponseService(TenderResponseService tenderResponseService) {
		this.tenderResponseService = tenderResponseService;
	}

	public String getTenderType() {
		return tenderType;
	}

	public void setTenderType(String tenderType) {
		this.tenderType = tenderType;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public void setTenderBidderType(Map<String, List<String>> tenderBidderType) {
		this.tenderBidderType = tenderBidderType;
	}
	
	public void setModel(GenericTenderResponse response)
	{
		this.tenderResponse=response;
	}
	
	public List<GenericTenderResponse> getNegotiationDetails() {
		return negotiationDetails;
	}
	public List<String> getUomName() {
		return uomName;
	}

	public void setUomName(List<String> uomName) {
		this.uomName = uomName;
	}

	public List<Double> getTotalBidRate() {
		return totalBidRate;
	}

	public void setTotalBidRate(List<Double> totalBidRate) {
		this.totalBidRate = totalBidRate;
	}

	public List<Double> getTotalEstimatedRate() {
		return totalEstimatedRate;
	}

	public void setTotalEstimatedRate(List<Double> totalEstimatedRate) {
		this.totalEstimatedRate = totalEstimatedRate;
	}
	
	public TenderResponseLine createResponseLineFromTenderable(Tenderable tenderable)
	{
		TenderResponseLine responseLine=new TenderResponseLine();
		responseLine.setQuantity(tenderable.getRequestedQty());
		responseLine.setTenderableEntity((TenderableEntity)tenderable);
		responseLine.setUom(tenderable.getRequestedUOM());
		return responseLine;
	}

}
