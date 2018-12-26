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
package org.egov.ptis.domain.entity.property;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.entity.Auditable;

public class GisDetails extends AbstractAuditable implements Auditable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Property gisProperty;
    private Property applicationProperty;
    private Boundary gisZone;
    private Boundary propertyZone;
    private BigDecimal gisTax = BigDecimal.ZERO;
    private BigDecimal applicationTax = BigDecimal.ZERO;
    private BigDecimal approvedTax = BigDecimal.ZERO;
    private BigDecimal systemTax = BigDecimal.ZERO;
    private List<GisAuditDetails> gisAuditDetails = new ArrayList<>();
    private Long version;
    private String uploadDocumentStatus;

    public Property getGisProperty() {
        return gisProperty;
    }

    public void setGisProperty(Property gisProperty) {
        this.gisProperty = gisProperty;
    }

    public Property getApplicationProperty() {
        return applicationProperty;
    }

    public void setApplicationProperty(Property applicationProperty) {
        this.applicationProperty = applicationProperty;
    }

    public Boundary getGisZone() {
        return gisZone;
    }

    public void setGisZone(Boundary gisZone) {
        this.gisZone = gisZone;
    }

    public Boundary getPropertyZone() {
        return propertyZone;
    }

    public void setPropertyZone(Boundary propertyZone) {
        this.propertyZone = propertyZone;
    }

    public BigDecimal getGisTax() {
        return gisTax;
    }

    public void setGisTax(BigDecimal gisTax) {
        this.gisTax = gisTax;
    }

    public BigDecimal getApplicationTax() {
        return applicationTax;
    }

    public void setApplicationTax(BigDecimal applicationTax) {
        this.applicationTax = applicationTax;
    }

    public BigDecimal getApprovedTax() {
        return approvedTax;
    }

    public void setApprovedTax(BigDecimal approvedTax) {
        this.approvedTax = approvedTax;
    }

    public BigDecimal getSystemTax() {
        return systemTax;
    }

    public void setSystemTax(BigDecimal systemTax) {
        this.systemTax = systemTax;
    }

    public List<GisAuditDetails> getGisAuditDetails() {
        return gisAuditDetails;
    }

    public void setGisAuditDetails(List<GisAuditDetails> gisAuditDetails) {
        this.gisAuditDetails = gisAuditDetails;
    }

    public void addAuditDetails(GisAuditDetails gisAuditDetails) {
        getGisAuditDetails().add(gisAuditDetails);
    }

    @Override
    public Long getId() {
        return id;
    }
    
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

	public String getUploadDocumentStatus() {
		return uploadDocumentStatus;
	}

	public void setUploadDocumentStatus(String uploadDocumentStatus) {
		this.uploadDocumentStatus = uploadDocumentStatus;
	}

}
