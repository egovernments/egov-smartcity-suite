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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGBPA_DOCKET_DETAIL")
@SequenceGenerator(name = DocketDetail.SEQ_DOCKETDETAIL, sequenceName = DocketDetail.SEQ_DOCKETDETAIL, allocationSize = 1)
public class DocketDetail extends AbstractAuditable {

    private static final long serialVersionUID = 3078684328383202788L;
    public static final String SEQ_DOCKETDETAIL = "SEQ_EGBPA_DOCKET_DETAIL";

    @Id
    @GeneratedValue(generator = SEQ_DOCKETDETAIL, strategy = GenerationType.SEQUENCE)
    private Long id;
    @Length(min = 1, max = 32)
    private String value;
    @Length(min = 1, max = 256)
    private String remarks;
    @Length(min = 1, max = 32)
    private String required;
    @Length(min = 1, max = 32)
    private String provided;
    @Length(min = 1, max = 32)
    private String extentOfViolation;
    @Length(min = 1, max = 32)
    private String percentageOfViolation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docket")
    private Docket docket;
    @ManyToOne(fetch = FetchType.LAZY)
    private CheckListDetail checkListDetail;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getProvided() {
        return provided;
    }

    public void setProvided(String provided) {
        this.provided = provided;
    }

    public String getExtentOfViolation() {
        return extentOfViolation;
    }

    public void setExtentOfViolation(String extentOfViolation) {
        this.extentOfViolation = extentOfViolation;
    }

    public String getPercentageOfViolation() {
        return percentageOfViolation;
    }

    public void setPercentageOfViolation(String percentageOfViolation) {
        this.percentageOfViolation = percentageOfViolation;
    }

    public Docket getDocket() {
        return docket;
    }

    public void setDocket(Docket docket) {
        this.docket = docket;
    }

    public CheckListDetail getCheckListDetail() {
        return checkListDetail;
    }

    public void setCheckListDetail(CheckListDetail checkListDetail) {
        this.checkListDetail = checkListDetail;
    }

}