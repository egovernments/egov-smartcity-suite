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

import org.egov.infra.persistence.entity.AbstractAuditable;

@Entity
@Table(name = "EGADTAX_RATES")
@SequenceGenerator(name = AdvertisementRate.SEQ_RATES, sequenceName = AdvertisementRate.SEQ_RATES, allocationSize = 1)
public class AdvertisementRate extends AbstractAuditable {

    private static final long serialVersionUID = -3661778599272146492L;
    public static final String SEQ_RATES = "SEQ_EGADTAX_RATES";
    @Id
    @GeneratedValue(generator = SEQ_RATES, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category", nullable = false)
    private HoardingCategory category;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "subcategory", nullable = false)
    private SubCategory subCategory;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "unitofmeasure", nullable = false)
    private UnitOfMeasure unitofmeasure;

    private boolean active;

    @Temporal(value = TemporalType.DATE)
    private Date validFromoDate;

    @Temporal(value = TemporalType.DATE)
    private Date validToDate;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public HoardingCategory getCategory() {
        return category;
    }

    public void setCategory(final HoardingCategory category) {
        this.category = category;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(final SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public UnitOfMeasure getUnitofmeasure() {
        return unitofmeasure;
    }

    public void setUnitofmeasure(final UnitOfMeasure unitofmeasure) {
        this.unitofmeasure = unitofmeasure;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public Date getValidFromoDate() {
        return validFromoDate;
    }

    public void setValidFromoDate(Date validFromoDate) {
        this.validFromoDate = validFromoDate;
    }

    public Date getValidToDate() {
        return validToDate;
    }

    public void setValidToDate(Date validToDate) {
        this.validToDate = validToDate;
    }

  

}