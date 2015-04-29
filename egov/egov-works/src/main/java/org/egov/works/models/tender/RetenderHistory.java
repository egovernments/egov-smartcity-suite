package org.egov.works.models.tender;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infstr.models.BaseModel;

public class RetenderHistory extends BaseModel{
	
	private static final long serialVersionUID = 1L;
	@NotNull(message="ws.status.is.null")
	private EgwStatus egwStatus;
	@NotNull(message="ws.statusDate.is.null")
	@ValidateDate(allowPast=true, dateFormat="dd/MM/yyyy",message="invalid.statusDate")
	private Date statusDate;
	@NotNull(message="ws.objectId.is.null")
	private WorksPackage worksPackage;
	private Retender retender; 
		
	public Date getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}
	public EgwStatus getEgwStatus() {
		return egwStatus;
	}
	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}
	public WorksPackage getWorksPackage() {
		return worksPackage;
	}
	public void setWorksPackage(WorksPackage worksPackage) {
		this.worksPackage = worksPackage;
	}
	public Retender getRetender() {
		return retender;
	}
	public void setRetender(Retender retender) {
		this.retender = retender;
	}
}
