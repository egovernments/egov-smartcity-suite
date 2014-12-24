package org.egov.works.models.serviceOrder;

import java.math.BigDecimal;

import org.apache.commons.collections.Predicate;
import org.egov.infstr.models.BaseModel;

/**
 * EgwServiceorderdetails entity. @author msahoo
 */
public class ServiceOrderDetails  extends BaseModel implements Predicate {

	
	
	private static final long serialVersionUID = 1L;
	private SoTemplateActivities soTemplateActivities;
	private ServiceOrderObjectDetail serviceOrderObjectDetail;
	private BigDecimal ratePercentage;
	private Boolean iscompleted;
	private SoMeasurmentDetail soMeasurmentDetail;
	
	
	public SoMeasurmentDetail getSoMeasurmentDetail() {
		return soMeasurmentDetail;
	}
	public void setSoMeasurmentDetail(SoMeasurmentDetail soMeasurmentDetail) {
		this.soMeasurmentDetail = soMeasurmentDetail;
	}
	public BigDecimal getRatePercentage() {
		return ratePercentage;
	}
	public Boolean getIscompleted() {
		return iscompleted;
	}
	

	public void setRatePercentage(BigDecimal ratePercentage) {
		this.ratePercentage = ratePercentage;
	}
	public void setIscompleted(Boolean iscompleted) {
		this.iscompleted = iscompleted;
	}
	
	public SoTemplateActivities getSoTemplateActivities() {
		return soTemplateActivities;
	}
	public ServiceOrderObjectDetail getServiceOrderObjectDetail() {
		return serviceOrderObjectDetail;
	}
	public void setSoTemplateActivities(SoTemplateActivities soTemplateActivities) {
		this.soTemplateActivities = soTemplateActivities;
	}
	public void setServiceOrderObjectDetail(
			ServiceOrderObjectDetail serviceOrderObjectDetail) {
		this.serviceOrderObjectDetail = serviceOrderObjectDetail;
	}
	@Override
	public boolean evaluate(Object arg0) {
		ServiceOrderDetails  soDetails = (ServiceOrderDetails)arg0;
		if(null != soDetails.getIscompleted()){
			return soDetails.getIscompleted();
		}else{
			return false;
		}
	}

}
