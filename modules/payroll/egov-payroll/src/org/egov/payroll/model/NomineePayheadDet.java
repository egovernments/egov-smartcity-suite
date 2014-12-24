package org.egov.payroll.model;

import java.math.BigDecimal;
import java.util.Comparator;

import org.egov.infstr.models.BaseModel;
import org.egov.pims.model.EmployeeNomineeMaster;

public class NomineePayheadDet extends BaseModel implements Comparable{
	private EmployeeNomineeMaster empNomineeMaster;
	private NominationHeader nominationHeader;
	private BigDecimal pct;
	private String isEligible;	
	
	public EmployeeNomineeMaster getEmpNomineeMaster() {
		return empNomineeMaster;
	}
	public void setEmpNomineeMaster(EmployeeNomineeMaster empNomineeMaster) {
		this.empNomineeMaster = empNomineeMaster;
	}
	public NominationHeader getNominationHeader() {
		return nominationHeader;
	}
	public void setNominationHeader(NominationHeader nominationHeader) {
		this.nominationHeader = nominationHeader;
	}
	public BigDecimal getPct() {
		return pct;
	}
	public void setPct(BigDecimal pct) {
		this.pct = pct;
	}
	
	public String getIsEligible() {
		return isEligible;
	}
	public void setIsEligible(String isEligible) {
		this.isEligible = isEligible;
	}
	
	public int compareTo(Object anotherNomPayhead) throws ClassCastException {
	    if (!(anotherNomPayhead instanceof NomineePayheadDet))
	    {
	    	throw new ClassCastException("A NomineePayheadDet object expected.");
	    }
	    Long nomPayheadId = ((NomineePayheadDet) anotherNomPayhead).getId();  
	    return this.id.compareTo(nomPayheadId);  
	  }
	
	public static Comparator NominationHeaderComparator = new Comparator() {
	    public int compare(Object nomineePayhead, Object anotherNomineePayhead) {
	      String nomineePayhead1 = ((NomineePayheadDet) nomineePayhead).getNominationHeader().getCode().toUpperCase();	      
	      String nomineePayhead2 = ((NomineePayheadDet) anotherNomineePayhead).getNominationHeader().getCode().toUpperCase();	      
          return nomineePayhead1.compareTo(nomineePayhead2);
	    }
	 };
	
}
