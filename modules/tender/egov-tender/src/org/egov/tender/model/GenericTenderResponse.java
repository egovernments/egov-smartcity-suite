package org.egov.tender.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.struts2.ServletActionContext;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Bidder;
import org.egov.commons.EgwStatus;
import org.egov.infstr.models.State;
import org.egov.infstr.models.StateAware;
import org.egov.lib.address.model.Address;
import org.egov.lib.citizen.model.Owner;
import org.egov.pims.commons.Position;
import org.egov.tender.BidType;
import org.egov.tender.services.common.TenderCommonService;
import org.egov.tender.utils.TenderConstants;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class GenericTenderResponse extends StateAware{

	private static final long serialVersionUID = 1L;
	private Bidder bidder;
	private Long bidderId;
	private String bidderType;
	private Date responseDate;
	private String number;
	private BigDecimal bidValue;
	private BigDecimal percentage;
	private BidType bidType;
	private TenderUnit tenderUnit;
	private Set<TenderResponseLine> responseLines=new HashSet<TenderResponseLine>(0);
	private EgwStatus status;
	private Address bidderAddress;
	private BigDecimal ratePerUnit;
	private GenericTenderResponse parent;
	
	//temparay Variable
	private Owner owner;
	private Position position;
	

	public Long getBidderId() {
		return bidderId;
	}

	public void setBidderId(Long bidderId) {
		this.bidderId = bidderId;
	}

	public BigDecimal getRatePerUnit() {
		return ratePerUnit;
	}

	public void setRatePerUnit(BigDecimal ratePerUnit) {
		this.ratePerUnit = ratePerUnit;
	}
	
	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public Position getPosition() {
		return position;
	}

	public Address getBidderAddress() {
		return bidderAddress;
	}

	public void setBidderAddress(Address bidderAddress) {
		this.bidderAddress = bidderAddress;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public EgwStatus getStatus() {
		return status;
	}


	public void setStatus(EgwStatus status) {
		this.status = status;
	}


	public TenderUnit getTenderUnit() {
		return tenderUnit;
	}

	
	public String getBidderType() {
		return bidderType;
	}


	public void setBidderType(String bidderType) {
		this.bidderType = bidderType;
	}


	public void setTenderUnit(TenderUnit tenderUnit) {
		this.tenderUnit = tenderUnit;
	}

	public Bidder getBidder() {
		
		
		if (this.bidderType != null) {
			try {
				String bidderTypeService = this.bidderType.toLowerCase()
						+ "Service";
				WebApplicationContext wac = WebApplicationContextUtils
						.getWebApplicationContext(ServletActionContext
								.getServletContext());
				TenderCommonService bidderPersistenceService = (TenderCommonService) wac
						.getBean(TenderConstants.TENDERCOMMONSERVICE);

				if (bidderPersistenceService != null
						&& bidderPersistenceService.getBidderTypeServiceMap()
								.get(bidderTypeService) != null
						&& this.bidderId != null)
					return bidderPersistenceService.getBidderTypeServiceMap()
							.get(bidderTypeService).getBidderById(
									this.bidderId.intValue());

			} catch (Exception e) {
				throw new EGOVRuntimeException(
						"Error in GenericTenderResponse -getBidder() method");
			}
		}
		return this.bidder;
	}

	public void setBidder(Bidder bidder) {
		this.bidder = bidder;
	}

	public Date getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}

	public BigDecimal getBidValue() {
		return bidValue;
	}

	public void setBidValue(BigDecimal bidValue) {
		this.bidValue = bidValue;
	}

	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	public BidType getBidType() {
		return bidType;
	}

	public void setBidType(BidType bidType) {
		this.bidType = bidType;
	}

	public Set<TenderResponseLine> getResponseLines() {
		return responseLines;
	}

	public void setResponseLines(Set<TenderResponseLine> responseLines) {
		this.responseLines = responseLines;
	}

	public String getNumber() {
		return number;
	}


	public void setNumber(String number) {
		this.number = number;
	}
	
	@Override
	public String getStateDetails() {
		return TenderConstants.TENDERRESPONSENUMBER+number ;
	}
	
	public TenderNotice getNotice()
	{
		return this.tenderUnit.getTenderNotice();
	}
	
	public GenericTenderResponse getParent() {
		return parent;
	}

	public void setParent(GenericTenderResponse parent) {
		this.parent = parent;
	}
	
	
	
	//tempary methods
	public GenericTenderResponse getRootResponse(){
		if(this.parent==null){
			return this;
		}
		else{
			GenericTenderResponse response = this.parent;
			while(response.getParent()!=null)
				response=response.getParent();
			return response;
		}
	}
	
	public TenderResponseLine getTenderResponseLineByEntityCode(String itemCode)
	{
		
			for(TenderResponseLine  responseLines : this.responseLines)
			{
				if(itemCode!=null && !itemCode.equals("") && responseLines.getTenderableEntity()!=null && responseLines.getTenderableEntity().getNumber().equals(itemCode))
					return responseLines;
			}
					
		return null;
	}
	
	
	/**
	 * Assuming that work flow is ended at the time of bid acceptance.
	 * @return accepted date of the bid
	 */
	public Date getBidAcceptedDate(){
		List<State> stateList= this.getRootResponse().getHistory();
			for(State state:stateList){
				if(State.END.equals(state.getValue()))
					return state.getCreatedDate();	
}
		return null;
	}
	
}
	
