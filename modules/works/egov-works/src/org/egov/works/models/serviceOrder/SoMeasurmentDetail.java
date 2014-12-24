package org.egov.works.models.serviceOrder;

import org.egov.infstr.models.BaseModel;

/**
 * EgwSoMeasurmentdetail entity. @author MyEclipse Persistence Tools
 */
public class SoMeasurmentDetail extends BaseModel {

	
	private static final long serialVersionUID = 1L;
	private ServiceOrderDetails serviceOrderDetails;
	private SoMeasurementHeader soMeasurementHeader;
	public ServiceOrderDetails getServiceOrderDetails() {
		return serviceOrderDetails;
	}
	public SoMeasurementHeader getSoMeasurementHeader() {
		return soMeasurementHeader;
	}
	public void setServiceOrderDetails(ServiceOrderDetails serviceOrderDetails) {
		this.serviceOrderDetails = serviceOrderDetails;
	}
	public void setSoMeasurementHeader(SoMeasurementHeader soMeasurementHeader) {
		this.soMeasurementHeader = soMeasurementHeader;
	}
	


}
