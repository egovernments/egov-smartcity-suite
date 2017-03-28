/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.
    Copyright (C) <2015>  eGovernments Foundation
    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.
        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.
        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.
  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.bpa.application.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGBPA_MSTR_SERVICETYPE")
@SequenceGenerator(name = ServiceType.SEQ_SERVICETYPE, sequenceName = ServiceType.SEQ_SERVICETYPE, allocationSize = 1)
public class ServiceType extends AbstractAuditable {

    private static final long serialVersionUID = 3078684328383202788L;
    public static final String SEQ_SERVICETYPE = "SEQ_EGBPA_MSTR_SERVICETYPE";
    @Id
    @GeneratedValue(generator = SEQ_SERVICETYPE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Length(min = 1, max = 128)
    @Column(name = "code", unique = true)
    private String code;

    @Length(min = 1, max = 256)
    private String description;
    private Boolean isActive;
    private Boolean buildingPlanApproval;
    @NotNull
    private Boolean siteApproval;
    @NotNull
    private Boolean isApplicationFeeRequired;
    @NotNull
    private Boolean isPtisNumberRequired;
    @NotNull
    private Boolean isAutoDcrNumberRequired;
    @NotNull
    @Length(min = 1, max = 128)
    private String serviceNumberPrefix;
    @Length(min = 1, max = 256)
    private String descriptionLocal;
    private Boolean isDocUploadForCitizen;
    private Long sla;

    @OneToMany(mappedBy = "serviceType", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Document> document = new ArrayList<Document>(0);

    @OneToMany(mappedBy = "serviceType", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Noc> noc = new ArrayList<Noc>(0);

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(final Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsPtisNumberRequired() {
        return isPtisNumberRequired;
    }

    public void setIsPtisNumberRequired(final Boolean isPtisNumberRequired) {
        this.isPtisNumberRequired = isPtisNumberRequired;
    }

    public Boolean getIsAutoDcrNumberRequired() {
        return isAutoDcrNumberRequired;
    }

    public void setIsAutoDcrNumberRequired(final Boolean isAutoDcrNumberRequired) {
        this.isAutoDcrNumberRequired = isAutoDcrNumberRequired;
    }

    public String getServiceNumberPrefix() {
        return serviceNumberPrefix;
    }

    public void setServiceNumberPrefix(final String serviceNumberPrefix) {
        this.serviceNumberPrefix = serviceNumberPrefix;
    }

    public String getDescriptionLocal() {
        return descriptionLocal;
    }

    public void setDescriptionLocal(final String descriptionLocal) {
        this.descriptionLocal = descriptionLocal;
    }

    public Boolean getIsDocUploadForCitizen() {
        return isDocUploadForCitizen;
    }

    public void setIsDocUploadForCitizen(final Boolean isDocUploadForCitizen) {
        this.isDocUploadForCitizen = isDocUploadForCitizen;
    }

    public Boolean getSiteApproval() {
        return siteApproval;
    }

    public Boolean getIsApplicationFeeRequired() {
        return isApplicationFeeRequired;
    }

    public Long getSla() {
        return sla;
    }

    public void setSiteApproval(Boolean siteApproval) {
        this.siteApproval = siteApproval;
    }

    public void setIsApplicationFeeRequired(Boolean isApplicationFeeRequired) {
        this.isApplicationFeeRequired = isApplicationFeeRequired;
    }

    public void setSla(Long sla) {
        this.sla = sla;
    }

    public Boolean getBuildingPlanApproval() {
        return buildingPlanApproval;
    }

    public void setBuildingPlanApproval(Boolean buildingPlanApproval) {
        this.buildingPlanApproval = buildingPlanApproval;
    }

    public List<Document> getDocument() {
        return document;
    }

    public List<Noc> getNoc() {
        return noc;
    }

}