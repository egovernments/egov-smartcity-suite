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
package org.egov.ptis.bean.aadharseeding;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.workflow.entity.StateAware;
import org.egov.pims.commons.Position;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;

@Entity
@Table(name= "egpt_aadharseeding")
@SequenceGenerator(name = AadharSeeding.SEQ_AADAHARSEEDING, sequenceName = AadharSeeding.SEQ_AADAHARSEEDING, allocationSize = 1)
public class AadharSeeding extends StateAware<Position>{
    private static final long serialVersionUID = 6939591953507240792L;

    public static final String SEQ_AADAHARSEEDING = "SEQ_EGPT_AADHARSEEDING";

    @Id
    @GeneratedValue(generator = SEQ_AADAHARSEEDING, strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @OneToOne
    @NotNull
    @JoinColumn(name = "basicproperty")
    private BasicPropertyImpl basicProperty;
    
    @OrderBy("id")
    @OneToMany(mappedBy = "aadharSeeding", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AadharSeedingDetails> aadharSeedingDetails = new LinkedList<>();
    
    private String status;
    
    private Boolean flag;
    
    private Boolean isAadharValid;
    
    private String responseCode;

    private String bhudharId;
    
    public BasicPropertyImpl getBasicProperty() {
        return basicProperty;
    }

    public void setBasicProperty(BasicPropertyImpl basicProperty) {
        this.basicProperty = basicProperty;
    }

    public List<AadharSeedingDetails> getAadharSeedingDetails() {
        return aadharSeedingDetails;
    }

    public void setAadharSeedingDetails(List<AadharSeedingDetails> aadharSeedingDetails) {
        this.aadharSeedingDetails = aadharSeedingDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Boolean getIsAadharValid() {
        return isAadharValid;
    }

    public void setIsAadharValid(Boolean isAadharValid) {
        this.isAadharValid = isAadharValid;
    }

    @Override
    public String getStateDetails() {
        return "Aadhar Seeding" + " - " + this.basicProperty.getUpicNo();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(Long id) {
        this.id = id;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
    
    public String getBhudharId() {
        return bhudharId;
    }

    public void setBhudharId(String bhudharId) {
        this.bhudharId = bhudharId;
    }

}
