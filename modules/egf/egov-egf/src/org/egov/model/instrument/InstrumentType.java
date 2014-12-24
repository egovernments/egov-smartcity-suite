package org.egov.model.instrument;

import java.util.HashSet;
import java.util.Set;

import org.egov.infstr.models.BaseModel;
import org.egov.model.instrument.InstrumentAccountCodes;

public class InstrumentType extends BaseModel {
	private Long id;
	private String type;
	private String isActive;
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	private Set instrumentAccountCodes=new HashSet<InstrumentAccountCodes>();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Set<InstrumentAccountCodes> getInstrumentAccountCodes() {
		return instrumentAccountCodes;
	}
	public void setInstrumentAccountCodes(Set<InstrumentAccountCodes> instrumentAccountCodes) {
		this.instrumentAccountCodes = instrumentAccountCodes;
	}
	

}
