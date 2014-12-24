package org.egov.demand.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.EgfAccountcodePurpose;
import org.egov.commons.Installment;



/**
 * EgDemandReason entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class EgDemandReason implements java.io.Serializable {

	// Fields

	private Long id;
	private EgDemandReason egDemandReason;
	private EgDemandReasonMaster egDemandReasonMaster;
	private Installment egInstallmentMaster;
	private BigDecimal percentageBasis;
	private Date createTimestamp;
	private Date lastUpdatedTimestamp;
	private Set<EgDemandReasonDetails> egDemandReasonDetails = new HashSet<EgDemandReasonDetails>(0);
	//private AccountHead accountHeadMaster=null;
	private Set<EgDemandDetails> egDemandDetails = new HashSet<EgDemandDetails>(0);
	private EgfAccountcodePurpose purposeCode = null;
	private CChartOfAccounts glcodeId;

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(egDemandReasonMaster).append("-").append(egInstallmentMaster);
        return sb.toString();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EgDemandReason getEgDemandReason() {
		return this.egDemandReason;
	}

	public void setEgDemandReason(EgDemandReason egDemandReason) {
		this.egDemandReason = egDemandReason;
	}

	public EgDemandReasonMaster getEgDemandReasonMaster() {
		return this.egDemandReasonMaster;
	}

	public void setEgDemandReasonMaster(
			EgDemandReasonMaster egDemandReasonMaster) {
		this.egDemandReasonMaster = egDemandReasonMaster;
	}

	public Installment getEgInstallmentMaster() {
		return this.egInstallmentMaster;
	}

	public void setEgInstallmentMaster(Installment egInstallmentMaster) {
		this.egInstallmentMaster = egInstallmentMaster;
	}

	public BigDecimal getPercentageBasis() {
		return this.percentageBasis;
	}

	public void setPercentageBasis(BigDecimal percentageBasis) {
		this.percentageBasis = percentageBasis;
	}

	/*public AccountHead getAccountHeadMaster() {
		return accountHeadMaster;
	}

	public void setAccountHeadMaster(AccountHead accountHeadMaster) {
		this.accountHeadMaster = accountHeadMaster;
	}*/

	public Date getCreateTimestamp() {
		return this.createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public Date getLastUpdatedTimestamp() {
		return this.lastUpdatedTimestamp;
	}

	public void setLastUpdatedTimestamp(Date lastUpdatedTimestamp) {
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
	}


	public Set<EgDemandReasonDetails> getEgDemandReasonDetails() {
		return egDemandReasonDetails;
	}

	public void setEgDemandReasonDetails(Set<EgDemandReasonDetails> egDemandReasonDetails) {
		this.egDemandReasonDetails = egDemandReasonDetails;
	}

	public Set<EgDemandDetails> getEgDemandDetails() {
		return egDemandDetails;
	}

	public void setEgDemandDetails(Set<EgDemandDetails> egDemandDetails) {
		this.egDemandDetails = egDemandDetails;
	}

	public void addEgDemandReasonDetails(EgDemandReasonDetails egDemandReasonDetails)
	{
		getEgDemandReasonDetails().add(egDemandReasonDetails);
	}
	public void removeEgDemandReasonDetails(EgDemandReasonDetails egDemandReasonDetails)
	{
		getEgDemandReasonDetails().remove(egDemandReasonDetails);
	}

	public void addEgDemandDetails(EgDemandDetails egDemandDetails)
	{
		getEgDemandDetails().add(egDemandDetails);
	}
	public void removeEgDemandDetails(EgDemandDetails egDemandDetails)
	{
		getEgDemandDetails().remove(egDemandDetails);
	}


	 public EgfAccountcodePurpose getPurposeCode() {
		return purposeCode;
	}

	public void setPurposeCode(EgfAccountcodePurpose purposeCode) {
		this.purposeCode = purposeCode;
	}

	/**
	 * @return Returns if the given Object is equal to PropertyImpl
	 */
    public boolean equals(Object obj)
	{
		if(obj == null) return false;

		if(this == obj) return true;

		if(! (obj instanceof EgDemandReason)) return false;

		final EgDemandReason other = (EgDemandReason) obj;

		if(getId() != null || other.getId() != null)
		{
			if(getId().equals(other.getId()))
			{
				return true;
			}
		}
        if((getEgDemandReasonMaster() != null || other.getEgDemandReasonMaster() != null)&& (getEgInstallmentMaster() != null || other.getEgInstallmentMaster() != null))
        {
            if(getEgDemandReasonMaster().equals(other.getEgDemandReasonMaster()) && getEgInstallmentMaster().equals(other.getEgInstallmentMaster()))
            {
                return true;
            }
            else
                return false;
        }
		else
			return false;
	}

    /**
	 * @return Returns the hashCode
	 */
    public int hashCode()
	{
		int hashCode = 0;

		if(getId() != null)
		{
			hashCode = hashCode + this.getId().hashCode();
		}
		if(getEgDemandReasonMaster() != null)
		{
			hashCode = hashCode + this.getEgDemandReasonMaster().hashCode();
		}
		if(getEgInstallmentMaster() != null)
		{
			hashCode = hashCode + this.getEgInstallmentMaster().hashCode();
		}
		return hashCode;
	}

	public CChartOfAccounts getGlcodeId() {
		return glcodeId;
	}

	public void setGlcodeId(CChartOfAccounts glcodeId) {
		this.glcodeId = glcodeId;
	}

	


}
