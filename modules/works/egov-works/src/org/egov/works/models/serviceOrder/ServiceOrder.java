package org.egov.works.models.serviceOrder;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.EgwStatus;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.CheckDateFormat;
import org.egov.infstr.models.validator.Required;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.user.User;
import org.hibernate.validator.constraints.Length;
import javax.validation.Valid;

/**
 * EgwServiceorder entity. @author MyEclipse Persistence Tools
 */
public class ServiceOrder  extends BaseModel {

	
	
	private static final long serialVersionUID = 1L;
	
	private Accountdetailtype accountdetailtype;
	private String serviceordernumber;
	private Integer detailkeyid;
	private Date serviceorderdate;
	private String comments;
	private EgwStatus status;
	private User  preparedby;
	
	private DepartmentImpl departmentId;
	
	@Valid
	private List<ServiceOrderObjectDetail> serviceOrderObjectDetails = new LinkedList<ServiceOrderObjectDetail>();
	
	public Accountdetailtype getAccountdetailtype() {
		return accountdetailtype;
	}
	public String getServiceordernumber() {
		return serviceordernumber;
	}
	@Required(message="so.architect.null")
	public Integer getDetailkeyid() {
		return detailkeyid;
	}
	@Required(message="so.date.null")
	@CheckDateFormat(message="so.invalid.date") 
	public Date getServiceorderdate() {
		return serviceorderdate;
	}
	@Length(max = 1024, message = "so.comments.length")
	public String getComments() {
		return comments;
	}
	public EgwStatus getStatus() {
		return status;
	}
	@Required(message="so.preparedby.null")
	public User getPreparedby() {
		return preparedby;
	}
	
	public void setAccountdetailtype(Accountdetailtype accountdetailtype) {
		this.accountdetailtype = accountdetailtype;
	}
	public void setServiceordernumber(String serviceordernumber) {
		this.serviceordernumber = serviceordernumber;
	}
	public void setDetailkeyid(Integer detailkeyid) {
		this.detailkeyid = detailkeyid;
	}
	public void setServiceorderdate(Date serviceorderdate) {
		this.serviceorderdate = serviceorderdate;
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
	public List<ServiceOrderObjectDetail> getServiceOrderObjectDetails() {
		return serviceOrderObjectDetails;
	}
	public void setServiceOrderObjectDetails(
			List<ServiceOrderObjectDetail> serviceOrderObjectDetails) {
		this.serviceOrderObjectDetails = serviceOrderObjectDetails;
	}
	public DepartmentImpl getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(DepartmentImpl departmentId) {
		this.departmentId = departmentId;
	}
	

}
