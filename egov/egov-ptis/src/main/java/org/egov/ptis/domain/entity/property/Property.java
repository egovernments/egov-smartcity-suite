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

import org.egov.commons.Installment;
import org.egov.exceptions.InvalidPropertyException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.persistence.entity.Auditable;
import org.egov.ptis.domain.entity.demand.Ptdemand;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * This is the interface for the Property which reperesents the Status and
 * Source of each Property. Every Property Object has some source associated
 * with it, which can be either Self-Assessment, Surveys or PropertyFiles.
 * Property from different Sources would be represented as seperate entities.
 *
 * @author Neetu
 * @version 2.00
 */
public interface Property extends Auditable {

    User getCreatedBy();

    void setCreatedBy(User createdBy);

    Date getCreatedDate();

    void setCreatedDate(Date createdDate);

    BasicProperty getBasicProperty();

    void setBasicProperty(BasicProperty basicProperty);

    Boolean isVacant();

    void setVacant(Boolean vacant);

    Address getPropertyAddress();

    void setPropertyAddress(Address address);

    PropertySource getPropertySource();

    void setPropertySource(PropertySource propertySource);

    boolean validateProperty() throws InvalidPropertyException;

    /*
     * void addPropertyTenants(Citizen citzen); void
     * removePropertyTenants(Citizen citzen);
     */
    Character getIsDefaultProperty();

    /**
     * @param isDefaultProperty The isDefaultProperty to set. If a property is set to default,
     *                          this application will consider this property's details for all
     *                          the demand calculation etc.
     */

    void setIsDefaultProperty(Character isDefaultProperty);

    Character getStatus();

    void setStatus(Character status);

    Date getEffectiveDate();

    void setEffectiveDate(Date date);

    Set<Ptdemand> getPtDemandSet();

    void setPtDemandSet(Set<Ptdemand> ptDemandSet);

    void addPtDemand(Ptdemand ptDmd);

    void removePtDemand(Ptdemand ptDmd);

    PropertyDetail getPropertyDetail();

    void setPropertyDetail(PropertyDetail propertyDetail);

    Character getIsChecked();

    void setIsChecked(Character isChecked);

    String getRemarks();

    void setRemarks(String remarks);

    String getPropertyModifyReason();

    void setPropertyModifyReason(String propertyModifyReason);

    /*
     * Set<PtDemandARV> getPtDemandARVSet(); void
     * setPtDemandARVSet(Set<PtDemandARV> ptDemandARVSet); void
     * addPtDemandARV(PtDemandARV ptDemandARV);
     */

    Installment getInstallment();

    void setInstallment(Installment installment);

    Property createPropertyclone();

    Boolean getIsExemptedFromTax();

    void setIsExemptedFromTax(Boolean isExemptedFromTax);

    TaxExemptionReason getTaxExemptedReason();

    void setTaxExemptedReason(TaxExemptionReason taxExemptedReason);

    String getDocNumber();

    void setDocNumber(String docNumber);

    BigDecimal getManualAlv();

    void setManualAlv(BigDecimal manualAlv);

    String getOccupierName();

    void setOccupierName(String occupierName);

    Boundary getAreaBndry();

    void setAreaBndry(Boundary areaBndry);

    BigDecimal getAlv();

    void setAlv(BigDecimal Alv);

    Set<UnitCalculationDetail> getUnitCalculationDetails();

    void setUnitCalculationDetails(Set<UnitCalculationDetail> unitCalculationDetails);

    void addUnitCalculationDetails(UnitCalculationDetail unitCalculationDetail);

    void addAllUnitCalculationDetails(Set<UnitCalculationDetail> unitCalculationDetails);

    List<Document> getDocuments();

    void setDocuments(List<Document> documents);

    String getApplicationNo();

    void setApplicationNo(String applicationNo);

    String getDemolitionReason();

    void setDemolitionReason(String demolitionReason);

    String getSource();

    void setSource(String source);

    List<AmalgamationOwner> getAmalgamationOwners();

    void setAmalgamationOwners(List<AmalgamationOwner> amalgamationOwners);

    List<AmalgamationOwner> getAmalgamationOwnersProxy();

    void setAmalgamationOwnersProxy(List<AmalgamationOwner> amalgamationOwnersProxy);

    void addAmalgamationOwners(AmalgamationOwner ownerInfo);

    List<Document> getAssessmentDocuments();

    void setAssessmentDocuments(List<Document> assessmentDocuments);

    List<Document> getTaxExemptionDocuments();

    void setTaxExemptionDocuments(List<Document> taxExemptionDocuments);

    List<Document> getTaxExemptionDocumentsProxy();

    void setTaxExemptionDocumentsProxy(List<Document> taxExemptionDocumentsProxy);

    void addTaxExemptionDocuments(Document exemptionDocument);

    String getReferenceId();
    
    void setReferenceId(String referenceId);


}
