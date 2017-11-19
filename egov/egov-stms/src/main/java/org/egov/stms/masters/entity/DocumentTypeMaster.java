/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.stms.masters.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "egswtax_document_type_master")
@SequenceGenerator(name = DocumentTypeMaster.SEQ_DOCUMENTTYPEMASTER, sequenceName = DocumentTypeMaster.SEQ_DOCUMENTTYPEMASTER, allocationSize = 1)
public class DocumentTypeMaster extends AbstractPersistable<Long> {

    /**
     *
     */
    private static final long serialVersionUID = 8765394185413119502L;

    public static final String SEQ_DOCUMENTTYPEMASTER = "SEQ_EGSWTAX_DOCUMENT_TYPE_MASTER";

    @Id
    @GeneratedValue(generator = SEQ_DOCUMENTTYPEMASTER, strategy = GenerationType.SEQUENCE)
    private Long id;
    
   

    @NotNull
    @SafeHtml
    @Length(max = 64)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "applicationtype", nullable = false)
    private SewerageApplicationType applicationType;

    private boolean isMandatory;

    private boolean isActive;
    
    @Transient
    private String applicationTypeName;
    
    

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }


    public SewerageApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(SewerageApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public boolean isMandatory() {
        return isMandatory;
    }
    
    public boolean getIsMandatory() {
        return isMandatory;
    }

    public void setMandatory(final boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(final boolean isActive) {
        this.isActive = isActive;
    }
    

    public String getApplicationTypeName() {
        return applicationTypeName;
    }

    public void setApplicationTypeName(String applicationTypeName) {
        this.applicationTypeName = applicationTypeName;
    }

    
    
}
