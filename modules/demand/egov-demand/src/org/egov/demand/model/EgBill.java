package org.egov.demand.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.infstr.commons.Module;
import org.egov.infstr.flexfields.model.EgAttributevalues;

/**
 * EgBill entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EgBill implements java.io.Serializable {

    // Fields

    private Long id;
    private EgDemand egDemand;
    private EgBillType egBillType;
    private String citizenName;
    private String citizenAddress;
    private String billNo;
    private Date issueDate;
    private Date lastDate;
    private Module module;
    private Long userId;
    private Date createTimeStamp;
    private Date lastUpdatedTimeStamp;
    private Set<EgBillDetails> egBillDetails = new HashSet<EgBillDetails>(0);
    private Set<EgAttributevalues> attributeValues = new HashSet<EgAttributevalues>(0);
    private String is_History;
    private String is_Cancelled;
    private String fundCode;
    private BigDecimal functionaryCode;
    private String fundSourceCode;
    private String departmentCode;
    private String collModesNotAllowed;
    private Integer boundaryNum;
    private String boundaryType;
    private BigDecimal totalAmount;
    private BigDecimal totalCollectedAmount;
    private String serviceCode;
    private Boolean partPaymentAllowed;
    private Boolean overrideAccountHeadsAllowed;
    private String description;
    private BigDecimal minAmtPayable;
    private String consumerId;
    private String displayMessage;
    private Boolean callBackForApportion;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
        .append(id).append("-")
        .append(totalAmount).append("-")
        .append(description).append("-")
        .append(egBillDetails);
        return sb.toString();
    }
    
    /**
     * Checks if the given GL code exists in the bill details for this bill.
     *  
     * @param glCode
     * @return
     */
    public boolean containsGLCode(String glCode) {
        boolean contains = false;
        for (EgBillDetails bd : getEgBillDetails()) {
            if (bd.getGlcode().equals(glCode)) {
                contains = true;
                break;
            }
        }
        return contains;
    }
    
    /**
     * Returns the difference between the CR and DR amount for the given GL code if it exists; 
     * null otherwise.
     */
    public BigDecimal balanceForGLCode(String glCode) {
        BigDecimal balance = null;
        for (EgBillDetails bd : getEgBillDetails()) {
            if (bd.getGlcode().equals(glCode)) {
                balance = bd.balance();
                break;
            }
        }
        return balance;
    }
    
    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public BigDecimal getMinAmtPayable() {
        return minAmtPayable;
    }

    public void setMinAmtPayable(BigDecimal minAmtPayable) {
        this.minAmtPayable = minAmtPayable;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundSourceCode() {
        return fundSourceCode;
    }

    public void setFundSourceCode(String fundSourceCode) {
        this.fundSourceCode = fundSourceCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getCollModesNotAllowed() {
        return collModesNotAllowed;
    }

    public void setCollModesNotAllowed(String collModesNotAllowed) {
        this.collModesNotAllowed = collModesNotAllowed;
    }

    public Integer getBoundaryNum() {
        return boundaryNum;
    }

    public void setBoundaryNum(Integer boundaryNum) {
        this.boundaryNum = boundaryNum;
    }

    public String getBoundaryType() {
        return boundaryType;
    }

    public void setBoundaryType(String boundaryType) {
        this.boundaryType = boundaryType;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EgDemand getEgDemand() {
        return this.egDemand;
    }

    public void setEgDemand(EgDemand egDemand) {
        this.egDemand = egDemand;
    }

    public EgBillType getEgBillType() {
        return this.egBillType;
    }

    public void setEgBillType(EgBillType egBillType) {
        this.egBillType = egBillType;
    }

    public String getCitizenName() {
        return this.citizenName;
    }

    public void setCitizenName(String citizenName) {
        this.citizenName = citizenName;
    }

    public String getCitizenAddress() {
        return this.citizenAddress;
    }

    public void setCitizenAddress(String citizenAddress) {
        this.citizenAddress = citizenAddress;
    }

    public Date getIssueDate() {
        return this.issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getLastDate() {
        return this.lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTimeStamp() {
        return this.createTimeStamp;
    }

    public void setCreateTimeStamp(Date createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public Date getLastUpdatedTimeStamp() {
        return lastUpdatedTimeStamp;
    }

    public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
        this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
    }

    public Set<EgAttributevalues> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(Set<EgAttributevalues> attributeValues) {
        this.attributeValues = attributeValues;
    }

    public Set<EgBillDetails> getEgBillDetails() {
        return egBillDetails;
    }

    public void setEgBillDetails(Set<EgBillDetails> egBillDetails) {
        this.egBillDetails = egBillDetails;
    }

    public String getIs_History() {
        return is_History;
    }

    public void setIs_History(String is_History) {
        this.is_History = is_History;
    }

    public String getIs_Cancelled() {
        return is_Cancelled;
    }

    public void setIs_Cancelled(String is_Cancelled) {
        this.is_Cancelled = is_Cancelled;
    }

    public void addEgBillDetails(EgBillDetails egBillDetails) {
        getEgBillDetails().add(egBillDetails);
    }

    public void removeEgBillDetails(EgBillDetails egBillDetails) {
        getEgBillDetails().remove(egBillDetails);
    }

    public void addEgAttributeValues(EgAttributevalues egAttributevalues) {
        getAttributeValues().add(egAttributevalues);
    }

    public void removeEgAttributeValues(EgAttributevalues egAttributevalues) {
        getAttributeValues().remove(egAttributevalues);
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public Boolean getPartPaymentAllowed() {
        return partPaymentAllowed;
    }

    public void setPartPaymentAllowed(Boolean partPaymentAllowed) {
        this.partPaymentAllowed = partPaymentAllowed;
    }

    public Boolean getOverrideAccountHeadsAllowed() {
        return overrideAccountHeadsAllowed;
    }

    public void setOverrideAccountHeadsAllowed(Boolean overrideAccountHeadsAllowed) {
        this.overrideAccountHeadsAllowed = overrideAccountHeadsAllowed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalCollectedAmount() {
        return totalCollectedAmount;
    }

    public void setTotalCollectedAmount(BigDecimal totalCollectedAmount) {
        this.totalCollectedAmount = totalCollectedAmount;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public BigDecimal getFunctionaryCode() {
        return functionaryCode;
    }

    public void setFunctionaryCode(BigDecimal functionaryCode) {
        this.functionaryCode = functionaryCode;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public Boolean getCallBackForApportion() {
		return callBackForApportion;
	}

	public void setCallBackForApportion(Boolean callBackForApportion) {
		this.callBackForApportion = callBackForApportion;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        EgBill other = (EgBill) obj;

        if (id != null && other != null && id.equals(other.id)) {
            return true;
        }
        return false;
    }

}