package org.egov.works.models.serviceOrder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.egov.infstr.models.BaseModel;
import org.egov.works.models.estimate.AbstractEstimate;
import javax.validation.Valid;

/**
 * EgwServiceorderobjectdetail entity. @author msahoo
 */
public class ServiceOrderObjectDetail extends BaseModel {

	
	private static final long serialVersionUID = 1L;
	private AbstractEstimate abstractEstimate;
	private ServiceTemplate serviceTemplate;
	private ServiceOrder serviceOrder;
	private BigDecimal objectamount;
	
	private Set soMeasurementHeaders = new HashSet(0);
	
	@Valid
	private List<ServiceOrderDetails> serviceOrderDetails = new LinkedList<ServiceOrderDetails>();
	
	public AbstractEstimate getAbstractEstimate() {
		return abstractEstimate;
	}
	public ServiceTemplate getServiceTemplate() {
		return serviceTemplate;
	}
	public ServiceOrder getServiceOrder() {
		return serviceOrder;
	}
	public BigDecimal getObjectamount() {
		return objectamount;
	}
	
	public Set getSoMeasurementHeaders() {
		return soMeasurementHeaders;
	}
	public void setAbstractEstimate(AbstractEstimate abstractEstimate) {
		this.abstractEstimate = abstractEstimate;
	}
	public void setServiceTemplate(ServiceTemplate serviceTemplate) {
		this.serviceTemplate = serviceTemplate;
	}
	public void setServiceOrder(ServiceOrder serviceOrder) {
		this.serviceOrder = serviceOrder;
	}
	public void setObjectamount(BigDecimal objectamount) {
		this.objectamount = objectamount;
	}
	
	public void setSoMeasurementHeaders(Set soMeasurementHeaders) {
		this.soMeasurementHeaders = soMeasurementHeaders;
	}
	public List<ServiceOrderDetails> getServiceOrderDetails() {
		return serviceOrderDetails;
	}
	public void setServiceOrderDetails(List<ServiceOrderDetails> serviceOrderDetails) {
		this.serviceOrderDetails = serviceOrderDetails;
	}
	
	

}
