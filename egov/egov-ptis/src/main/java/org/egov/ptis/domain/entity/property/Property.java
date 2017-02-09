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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.egov.commons.Installment;
import org.egov.exceptions.InvalidPropertyException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.persistence.entity.Auditable;
import org.egov.ptis.domain.entity.demand.Ptdemand;

/**
 * This is the interface for the Property which reperesents the Status and Source of each Property. Every Property Object has some
 * source associated with it, which can be either Self-Assessment, Surveys or PropertyFiles. Property from different Sources would
 * be represented as seperate entities.
 *
 * @author Neetu
 * @version 2.00
 */
public interface Property extends Auditable {

    public User getCreatedBy();

    public void setCreatedBy(User createdBy);

    public Date getCreatedDate();

    public void setCreatedDate(Date createdDate);

    public BasicProperty getBasicProperty();

    public void setBasicProperty(BasicProperty basicProperty);

    public Boolean isVacant();

    public void setVacant(Boolean vacant);

    public Address getPropertyAddress();

    public void setPropertyAddress(Address address);

    public PropertySource getPropertySource();

    public void setPropertySource(PropertySource propertySource);

    public boolean validateProperty() throws InvalidPropertyException;

    /*
     * public void addPropertyTenants(Citizen citzen); public void removePropertyTenants(Citizen citzen);
     */
    public Character getIsDefaultProperty();

    /**
     * @param isDefaultProperty The isDefaultProperty to set. If a property is set to default, this application will consider this
     * property's details for all the demand calculation etc.
     */

    public void setIsDefaultProperty(Character isDefaultProperty);

    public Character getStatus();

    public void setStatus(Character status);

    public void setEffectiveDate(Date date);

    public Date getEffectiveDate();

    public Set<Ptdemand> getPtDemandSet();

    public void setPtDemandSet(Set<Ptdemand> ptDemandSet);

    public void addPtDemand(Ptdemand ptDmd);

    public void removePtDemand(Ptdemand ptDmd);

    public PropertyDetail getPropertyDetail();

    public void setPropertyDetail(PropertyDetail propertyDetail);

    public void setIsChecked(Character isChecked);

    public Character getIsChecked();

    public String getRemarks();

    public void setRemarks(String remarks);

    public String getPropertyModifyReason();

    public void setPropertyModifyReason(String propertyModifyReason);

    /*
     * public Set<PtDemandARV> getPtDemandARVSet(); public void setPtDemandARVSet(Set<PtDemandARV> ptDemandARVSet); public void
     * addPtDemandARV(PtDemandARV ptDemandARV);
     */

    public void setInstallment(Installment installment);

    public Installment getInstallment();

    public Property createPropertyclone();

    public Boolean getIsExemptedFromTax();

    public void setIsExemptedFromTax(Boolean isExemptedFromTax);

    public TaxExemptionReason getTaxExemptedReason();

    public void setTaxExemptedReason(TaxExemptionReason taxExemptedReason);

    public String getDocNumber();

    public void setDocNumber(String docNumber);

    public BigDecimal getManualAlv();

    public void setManualAlv(BigDecimal manualAlv);

    public String getOccupierName();

    public void setOccupierName(String occupierName);

    public Boundary getAreaBndry();

    public void setAreaBndry(Boundary areaBndry);

    public BigDecimal getAlv();

    public void setAlv(BigDecimal Alv);

    public Set<UnitCalculationDetail> getUnitCalculationDetails();

    public void setUnitCalculationDetails(Set<UnitCalculationDetail> unitCalculationDetails);

    public void addUnitCalculationDetails(UnitCalculationDetail unitCalculationDetail);

    public void addAllUnitCalculationDetails(Set<UnitCalculationDetail> unitCalculationDetails);

    public List<Document> getDocuments();

    public void setDocuments(List<Document> documents);

    public String getApplicationNo();

    public void setApplicationNo(String applicationNo);

    public String getDemolitionReason();

    public void setDemolitionReason(String demolitionReason);

    public Character getSource();

    public void setSource(Character source);

    public List<AmalgamationOwner> getAmalgamationOwners();

    public void setAmalgamationOwners(List<AmalgamationOwner> amalgamationOwners);

    public List<AmalgamationOwner> getAmalgamationOwnersProxy();

    public void setAmalgamationOwnersProxy(List<AmalgamationOwner> amalgamationOwnersProxy);

    public void addAmalgamationOwners(AmalgamationOwner ownerInfo);
    
    public List<Document> getAssessmentDocuments();
    
    public void setAssessmentDocuments(List<Document> assessmentDocumentsProxy);
    
}
