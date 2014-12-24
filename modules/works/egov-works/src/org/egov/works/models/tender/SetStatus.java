package org.egov.works.models.tender;

import java.util.Date;

import org.egov.commons.EgwStatus;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.ValidateDate;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class SetStatus extends BaseModel{
	@NotEmpty(message="ws.name.is.null")
	private String objectType;
	@NotNull(message="ws.status.is.null")
	private EgwStatus egwStatus;
	@NotNull(message="ws.statusDate.is.null")
	@ValidateDate(allowPast=true, dateFormat="dd/MM/yyyy",message="invalid.statusDate")
	private Date statusDate;
	@NotNull(message="ws.objectId.is.null")
	private Long objectId;
		
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public Date getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}
	public Long getObjectId() {
		return objectId;
	}
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}
	public EgwStatus getEgwStatus() {
		return egwStatus;
	}
	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}
}
