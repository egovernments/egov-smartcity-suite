package org.egov.works.models.serviceOrder;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;
import org.egov.lib.rjbac.user.User;
import org.egov.model.bills.EgBillregister;
import javax.validation.Valid;

/**
 * EgwSoMeasurementheader entity. @author MyEclipse Persistence Tools
 */
public class SoMeasurementHeader extends BaseModel {

	
	private static final long serialVersionUID = 1L;
	private ServiceOrderObjectDetail serviceOrderObjectDetail;
	private EgBillregister egBillregister;
	private Date measurementDate;
	private String comments;
	private EgwStatus status;
	private User preparedby;
	private String mbNumber;
	@Valid
	private List<SoMeasurmentDetail> soMeasurmentDetails = new LinkedList<SoMeasurmentDetail>();
	public ServiceOrderObjectDetail getServiceOrderObjectDetail() {
		return serviceOrderObjectDetail;
	}
	public EgBillregister getEgBillregister() {
		return egBillregister;
	}
	public Date getMeasurementDate() {
		return measurementDate;
	}
	public String getComments() {
		return comments;
	}
	public EgwStatus getStatus() {
		return status;
	}
	@Required(message="somb.preparedby.null")
	public User getPreparedby() {
		return preparedby;
	}
	
	public void setServiceOrderObjectDetail(
			ServiceOrderObjectDetail serviceOrderObjectDetail) {
		this.serviceOrderObjectDetail = serviceOrderObjectDetail;
	}
	public void setEgBillregister(EgBillregister egBillregister) {
		this.egBillregister = egBillregister;
	}
	public void setMeasurementDate(Date measurementDate) {
		this.measurementDate = measurementDate;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public void setStatus(EgwStatus status) {
		this.status = status;
	}
	public void setPreparedby(User preparedby) {
		this.preparedby = preparedby;
	}
	public List<SoMeasurmentDetail> getSoMeasurmentDetails() {
		return soMeasurmentDetails;
	}
	public void setSoMeasurmentDetails(List<SoMeasurmentDetail> soMeasurmentDetails) {
		this.soMeasurmentDetails = soMeasurmentDetails;
	}
	
	   
	public String getMbNumber() {
		return mbNumber;
	}
	public void setMbNumber(String mbNumber) {
		this.mbNumber = mbNumber;
	}
	

}
