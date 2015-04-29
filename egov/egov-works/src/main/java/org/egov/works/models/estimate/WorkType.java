package org.egov.works.models.estimate;

import javax.validation.constraints.NotNull;

import org.egov.infstr.models.BaseModel;
import org.egov.works.models.masters.ExpenditureType;
import org.hibernate.validator.constraints.NotEmpty;

public class WorkType extends BaseModel {

	@NotEmpty
	private String name;
	@NotNull
	private ExpenditureType expenditureType;
	private String code;
	public WorkType(){}
	public WorkType(String name, ExpenditureType expenditureType) {
		super();
		this.name = name;
		this.expenditureType = expenditureType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ExpenditureType getExpenditureType() {
		return expenditureType;
	}

	public void setExpenditureType(ExpenditureType expenditureType) {
		this.expenditureType = expenditureType;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
      