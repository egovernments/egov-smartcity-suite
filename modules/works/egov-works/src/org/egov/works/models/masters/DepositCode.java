package org.egov.works.models.masters;

import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.utils.EntityType;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.Unique;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.works.models.estimate.WorkType;
import org.hibernate.validator.constraints.Length;

@Unique(fields={"code"},id="id", tableName="EGW_DEPOSITCODE",columnName={"CODE"},message="depositCode.isUnique")
public class DepositCode extends BaseModel implements EntityType {
	
	private String code;
	@Length(max=1024,message="depositCode.description.length")
	private String description;
	private WorkType worksType;
	
	@Required(message="depositCode.workName.null")
	@Length(max=256,message="depositCode.workName.length")
	private String codeName;
	private Fund fund;
	private Functionary functionary;
	private CFunction function;
	private Scheme scheme;
	private SubScheme subScheme;
	private DepartmentImpl department;
	private BoundaryImpl ward;
	private BoundaryImpl zone;
	
	@Required(message="depositCode.finYear.null")
	private CFinancialYear financialYear;
	private Fundsource fundSource;
	private EgwTypeOfWork typeOfWork;
	private EgwTypeOfWork subTypeOfWork;
	private Boolean isActive;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public WorkType getWorksType() {
		return worksType;
	}
	public void setWorksType(WorkType worksType) {
		this.worksType = worksType;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public Fund getFund() {
		return fund;
	}
	public void setFund(Fund fund) {
		this.fund = fund;
	}
	public Functionary getFunctionary() {
		return functionary;
	}
	public void setFunctionary(Functionary functionary) {
		this.functionary = functionary;
	}
	public CFunction getFunction() {
		return function;
	}
	public void setFunction(CFunction function) {
		this.function = function;
	}
	public Scheme getScheme() {
		return scheme;
	}
	public void setScheme(Scheme scheme) {
		this.scheme = scheme;
	}
	public SubScheme getSubScheme() {
		return subScheme;
	}
	public void setSubScheme(SubScheme subScheme) {
		this.subScheme = subScheme;
	}
	public DepartmentImpl getDepartment() {
		return department;
	}
	public void setDepartment(DepartmentImpl department) {
		this.department = department;
	}
	public BoundaryImpl getWard() {
		return ward;
	}
	public void setWard(BoundaryImpl ward) {
		this.ward = ward;
	}
	public BoundaryImpl getZone() {
		return zone;
	}
	public void setZone(BoundaryImpl zone) {
		this.zone = zone;
	}
	public CFinancialYear getFinancialYear() {
		return financialYear;
	}
	public void setFinancialYear(CFinancialYear financialYear) {
		this.financialYear = financialYear;
	}
	public Fundsource getFundSource() {
		return fundSource;
	}
	public void setFundSource(Fundsource fundSource) {
		this.fundSource = fundSource;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public EgwTypeOfWork getTypeOfWork() {
		return typeOfWork;
	}
	public void setTypeOfWork(EgwTypeOfWork typeOfWork) {
		this.typeOfWork = typeOfWork;
	}
	public EgwTypeOfWork getSubTypeOfWork() {
		return subTypeOfWork;
	}
	public void setSubTypeOfWork(EgwTypeOfWork subTypeOfWork) {
		this.subTypeOfWork = subTypeOfWork;
	}
	
	@Override
	public String getName() {
		return codeName;
	}
	
	@Override
	public String getEntityDescription() {
		return description;
	}
	
	@Override
	public Integer getEntityId() {
		return Integer.valueOf(id.intValue());
	}
	
	@Override
	public String getIfsccode() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getModeofpay() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getBankaccount() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getBankname() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getPanno() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getTinno() {
		// TODO Auto-generated method stub
		return null;
	}
}
