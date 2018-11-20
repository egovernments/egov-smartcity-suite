package org.egov.restapi.web.contracts.councilpreamble;

/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.council.entity.CouncilPreambleBidderDetails;
import org.egov.council.enums.PreambleTypeEnum;
import org.egov.infra.admin.master.entity.Boundary;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

public class CouncilPreambleRequest {
    
    @NotBlank(message = "ULB code must be present")
    private String ulbCode;
    
    @NotBlank(message = "Provide referenceNumber")
    private String referenceNumber;
    
    @NotBlank(message = "Provide Department Code")
    @Length(min = 1, max = 128)
    private String departmentcode;
    
    @NotNull(message = "Enter Gist of Preamble")
    @Length(max = 10000)
    private String gistOfPreamble; 
    
    @NotNull(message = "Provide Preamble Type")
    private PreambleTypeEnum preambleType;
    
    private BigDecimal estimateAmount;
    
    private List<Boundary> wards = new ArrayList<>();
    
    private List<CouncilPreambleBidderDetails> bidders = new ArrayList<>();
    
    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getGistOfPreamble() {
        return gistOfPreamble;
    }

    public void setGistOfPreamble(String gistOfPreamble) {
        this.gistOfPreamble = gistOfPreamble;
    }

    public String getPreambleType() {
        return preambleType.name();
    }

    public void setPreambleType(PreambleTypeEnum preambleType) {
        this.preambleType = preambleType;
    }

    public List<Boundary> getWards() {
        return wards;
    }

    public void setWards(List<Boundary> wards) {
        this.wards = wards;
    }

    public String getDepartmentcode() {
        return departmentcode;
    }

    public void setDepartmentcode(String departmentcode) {
        this.departmentcode = departmentcode;
    }


    public List<CouncilPreambleBidderDetails> getBidders() {
        return bidders;
    }

    public void setBidders(List<CouncilPreambleBidderDetails> bidders) {
        this.bidders = bidders;
    }

    public String getUlbCode() {
        return ulbCode;
    }

    public void setUlbCode(String ulbCode) {
        this.ulbCode = ulbCode;
    }

    public BigDecimal getEstimateAmount() {
        return estimateAmount;
    }

    public void setEstimateAmount(BigDecimal estimateAmount) {
        this.estimateAmount = estimateAmount;
    }

}
