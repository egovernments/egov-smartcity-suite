/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.ptis.domain.entity.property;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.enums.Gender;
import org.egov.infra.persistence.entity.enums.GuardianRelation;
import org.egov.infra.workflow.entity.StateAware;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.egov.ptis.constants.PropertyTaxConstants.GUARDIAN_RELATION_WIFE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_TYPE_CATEGORIES;

/**
 * @author subhash
 *
 */
public class PropertyMutation extends StateAware {

    private static final long serialVersionUID = -3387659460257524470L;
    private Long id;
    private PropertyMutationMaster mutationReason;
    private Date mutationDate;
    private BigDecimal mutationFee;
    private BigDecimal marketValue;
    private BigDecimal otherFee;
    private String receiptNum;
    private Date receiptDate;
    private String applicationNo;
    private String applicantName;
    private BasicProperty basicProperty;
    private Property property;
    private List<User> transferorInfos = new ArrayList<>();
    private List<PropertyMutationTransferee> transfereeInfos = new ArrayList<PropertyMutationTransferee>();
    private List<PropertyMutationTransferee> transfereeInfosProxy = new ArrayList<PropertyMutationTransferee>();
    private boolean feePayable;
    private String deedNo;
    private Date deedDate;
    private String documentNumber;
    private String saleDetail;
    private List<Document> documents = new ArrayList<>();
    private String meesevaApplicationNumber;//Temporary number for meeseva integration.
    private String source;
    private BigDecimal partyValue;
    private BigDecimal departmentValue;
    private boolean partialMutation;
    private boolean registrationDone;
    private String type;
    private String decreeNumber;
    private Date decreeDate;
    private String courtName;
    private MutationRegistrationDetails mutationRegistrationDetails = new MutationRegistrationDetails();

    
    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getStateDetails() {
        final StringBuffer stateDetails = new StringBuffer("");
        stateDetails.append(getBasicProperty().getUpicNo()).append(", ")
                .append(getPrimaryTransferee().getName()).append(", ")
                .append(PROPERTY_TYPE_CATEGORIES.get(getBasicProperty().getProperty().getPropertyDetail().getCategoryType()))
                .append(", ")
                .append(getBasicProperty().getPropertyID().getLocality().getName());
        return stateDetails.toString();
    }

    public PropertyMutationMaster getMutationReason() {
        return mutationReason;
    }

    public void setMutationReason(final PropertyMutationMaster mutationReason) {
        this.mutationReason = mutationReason;
    }

    public Date getMutationDate() {
        return mutationDate;
    }

    public void setMutationDate(final Date mutationDate) {
        this.mutationDate = mutationDate;
    }

    public BigDecimal getMutationFee() {
        return mutationFee;
    }

    public void setMutationFee(final BigDecimal mutationFee) {
        this.mutationFee = mutationFee;
    }

    public BigDecimal getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(final BigDecimal marketValue) {
        this.marketValue = marketValue;
    }

    public BigDecimal getOtherFee() {
        return otherFee;
    }

    public void setOtherFee(final BigDecimal otherFee) {
        this.otherFee = otherFee;
    }

    public String getReceiptNum() {
        return receiptNum;
    }

    public void setReceiptNum(final String receiptNum) {
        this.receiptNum = receiptNum;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(final Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(final String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(final String applicantName) {
        this.applicantName = applicantName;
    }

    public BasicProperty getBasicProperty() {
        return basicProperty;
    }

    public void setBasicProperty(final BasicProperty basicProperty) {
        this.basicProperty = basicProperty;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(final Property property) {
        this.property = property;
    }

    public List<User> getTransferorInfos() {
        return transferorInfos;
    }

    public void setTransferorInfos(final List<User> transferorInfos) {
        this.transferorInfos = transferorInfos;
    }

    public boolean isFeePayable() {
        return feePayable;
    }

    public void setFeePayable(final boolean feePayable) {
        this.feePayable = feePayable;
    }

    public String getDeedNo() {
        return deedNo;
    }

    public void setDeedNo(final String deedNo) {
        this.deedNo = deedNo;
    }

    public Date getDeedDate() {
        return deedDate;
    }

    public void setDeedDate(final Date deedDate) {
        this.deedDate = deedDate;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(final String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getSaleDetail() {
        return saleDetail;
    }

    public void setSaleDetail(final String saleDetail) {
        this.saleDetail = saleDetail;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(final List<Document> documents) {
        this.documents = documents;
    }

    public String getFullTranfereeName() {
        return buildOwnerName(getTransfereeInfos());
    }

    public String getFullTranferorName() {
        return buildTransferorOwnerName(getTransferorInfos());
    }

    public String getFullTransferorGuardianName() {
        return buildTransferorGuarianName(getTransferorInfos());
    }

    public String getFullTransfereeGuardianName() {
        return buildGuarianName(getTransfereeInfos());
    }

    public String getTransfereeGuardianRelation(){
    	return buildOwnerGuardianRelation(getTransfereeInfos());
    }
    
    private String buildGuarianName(final List<PropertyMutationTransferee> userInfo) {
        final StringBuilder guardianName = new StringBuilder();
        for (final PropertyMutationTransferee owner : userInfo)
            if (StringUtils.isNotBlank(owner.getTransferee().getGuardian()))
                guardianName.append(owner.getTransferee().getGuardian()).append(", ");
        if (guardianName.length() > 0)
            guardianName.deleteCharAt(guardianName.length() - 2);
        return guardianName.toString();
    }
    
    private String buildTransferorGuarianName(final List<User> userInfo) {
        final StringBuilder guardianName = new StringBuilder();
        for (final User owner : userInfo)
            if (StringUtils.isNotBlank(owner.getGuardian()))
                guardianName.append(owner.getGuardian()).append(", ");
        if (guardianName.length() > 0)
            guardianName.deleteCharAt(guardianName.length() - 2);
        return guardianName.toString();
    }

    private String buildOwnerName(final List<PropertyMutationTransferee> userInfos) {
        final StringBuilder ownerName = new StringBuilder();
        for (final PropertyMutationTransferee owner : userInfos)
            ownerName.append(owner.getTransferee().getName()).append(", ");
        if (ownerName.length() > 0)
            ownerName.deleteCharAt(ownerName.length() - 2);
        return ownerName.toString();
    }
    
    
    private String buildTransferorOwnerName(final List<User> userInfos) {
        final StringBuilder ownerName = new StringBuilder();
        for (final User owner : userInfos)
            ownerName.append(owner.getName()).append(", ");
        if (ownerName.length() > 0)
            ownerName.deleteCharAt(ownerName.length() - 2);
        return ownerName.toString();
    }

    private String buildOwnerGuardianRelation(final List<PropertyMutationTransferee> userInfo) {
        final StringBuilder ownerGuardianRelation = new StringBuilder();
        String relation = "";
        for (final PropertyMutationTransferee owner : userInfo){
            if (StringUtils.isNotBlank(owner.getTransferee().getGuardian())){
            	ownerGuardianRelation.append(owner.getTransferee().getName());
            	if(owner.getTransferee().getGuardianRelation().equalsIgnoreCase(GuardianRelation.Father.toString()) || owner.getTransferee().getGuardianRelation().equalsIgnoreCase(GuardianRelation.Mother.toString())){
            		if(owner.getTransferee().getGender().equals(Gender.FEMALE))
            			relation = " D/O ";
            		else if(owner.getTransferee().getGender().equals(Gender.MALE))
                		relation = " S/O ";
            	}
            	else if(owner.getTransferee().getGuardianRelation().equalsIgnoreCase(GuardianRelation.Husband.toString()))
            		relation = " W/O ";
            	else if(owner.getTransferee().getGuardianRelation().equalsIgnoreCase(GUARDIAN_RELATION_WIFE))
            		relation = " H/O ";
            	else
            		relation = " C/O ";
            	ownerGuardianRelation.append(relation).append(owner.getTransferee().getGuardian()).append(", ");
            }
        }    	
        if (ownerGuardianRelation.length() > 0)
        	ownerGuardianRelation.deleteCharAt(ownerGuardianRelation.length() - 2);
        return ownerGuardianRelation.toString();
    }
    
    public User getPrimaryTransferee() {
        User user = new User();
        for (final PropertyMutationTransferee transferee : getTransfereeInfos()) {
            user = transferee.getTransferee();
            break;
        }
        return user;
    }

    public User getPrimaryTransferor() {
        User user = new User();
        for (final User transferee : getTransferorInfos()) {
            user = transferee;
            break;
        }
        return user;

    }

    public String getMeesevaApplicationNumber() {
        return meesevaApplicationNumber;
    }

    public void setMeesevaApplicationNumber(String meesevaApplicationNumber) {
        this.meesevaApplicationNumber = meesevaApplicationNumber;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<PropertyMutationTransferee> getTransfereeInfos() {
        return transfereeInfos;
    }

    public void setTransfereeInfos(List<PropertyMutationTransferee> transfereeInfos) {
        this.transfereeInfos = transfereeInfos;
    }
    
    public void addTransfereeInfos(final PropertyMutationTransferee transfereeInfo) {
        getTransfereeInfos().add(transfereeInfo);
    }

    public void removeTransfereeInfos(final PropertyMutationTransferee transfereeInfo) {
        getTransfereeInfos().remove(transfereeInfo);
    }

    public List<PropertyMutationTransferee> getTransfereeInfosProxy() {
        return transfereeInfosProxy;
    }

    public void setTransfereeInfosProxy(List<PropertyMutationTransferee> transfereeInfosProxy) {
        this.transfereeInfosProxy = transfereeInfosProxy;
    }

    public BigDecimal getPartyValue() {
        return partyValue;
    }

    public void setPartyValue(BigDecimal partyValue) {
        this.partyValue = partyValue;
    }

    public BigDecimal getDepartmentValue() {
        return departmentValue;
    }

    public void setDepartmentValue(BigDecimal departmentValue) {
        this.departmentValue = departmentValue;
    }

    public boolean isPartialMutation() {
        return partialMutation;
    }

    public void setPartialMutation(boolean partialMutation) {
        this.partialMutation = partialMutation;
    }

    public boolean isRegistrationDone() {
        return registrationDone;
    }

    public void setRegistrationDone(boolean registrationDone) {
        this.registrationDone = registrationDone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MutationRegistrationDetails getMutationRegistrationDetails() {
        return mutationRegistrationDetails;
    }

    public void setMutationRegistrationDetails(MutationRegistrationDetails mutationRegistrationDetails) {
        this.mutationRegistrationDetails = mutationRegistrationDetails;
    }
    
    public String getDecreeNumber() {
        return decreeNumber;
    }

    public void setDecreeNumber(String decreeNumber) {
        this.decreeNumber = decreeNumber;
    }

    public Date getDecreeDate() {
        return decreeDate;
    }

    public void setDecreeDate(Date decreeDate) {
        this.decreeDate = decreeDate;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }
   
}
