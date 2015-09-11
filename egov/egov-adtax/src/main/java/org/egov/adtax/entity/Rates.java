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
package org.egov.adtax.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.validation.regex.Constants;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "EGADTAX_RATES")
@SequenceGenerator(name = Rates.SEQ_RATES, sequenceName = Rates.SEQ_RATES, allocationSize = 1)
public class Rates extends AbstractAuditable {
   
    private static final long serialVersionUID = -3661778599272146492L;
    public static final String SEQ_RATES = "SEQ_EGADTAX_RATES";
    @Id
    @GeneratedValue(generator = SEQ_RATES, strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "category",nullable = false)
    private HoardingCategory category;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "subcategory",nullable = false)
    private SubCategory subCategory;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "unitofmeasure",nullable = false)
    private UnitOfMeasure unitofmeasure;
    
    private boolean active;


    @NotNull
    @Temporal(value = TemporalType.DATE)
    private Date readingDate;
    
    @NotNull
    @Temporal(value = TemporalType.DATE)
    private Date validtodate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HoardingCategory getCategory() {
        return category;
    }

    public void setCategory(HoardingCategory category) {
        this.category = category;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public UnitOfMeasure getUnitofmeasure() {
        return unitofmeasure;
    }

    public void setUnitofmeasure(UnitOfMeasure unitofmeasure) {
        this.unitofmeasure = unitofmeasure;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(Date readingDate) {
        this.readingDate = readingDate;
    }

    public Date getValidtodate() {
        return validtodate;
    }

    public void setValidtodate(Date validtodate) {
        this.validtodate = validtodate;
    }
    
    
    
}