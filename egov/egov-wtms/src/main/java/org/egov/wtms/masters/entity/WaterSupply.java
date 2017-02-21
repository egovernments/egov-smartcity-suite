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
package org.egov.wtms.masters.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "egwtr_water_supply_type")
@SequenceGenerator(name = WaterSupply.SEQ_WATERSUPPLYTYPE, sequenceName = WaterSupply.SEQ_WATERSUPPLYTYPE, allocationSize = 1)
@Unique(id = "id", tableName = "egwtr_water_supply_type", columnName = { "code", "watersupplytype" }, fields = { "code",
        "waterSupplyType" }, enableDfltMsg = true)
@AuditOverrides({
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy")
})
public class WaterSupply extends AbstractAuditable {

    /**
     *
     */
    private static final long serialVersionUID = 5349817008827222018L;

    public static final String SEQ_WATERSUPPLYTYPE = "SEQ_EGWTR_WATER_SUPPLY_TYPE";

    @Id
    @GeneratedValue(generator = WaterSupply.SEQ_WATERSUPPLYTYPE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @SafeHtml
    @NotNull
    @Length(min = 1, max = 100)
    @Audited
    private String code;

    @NotNull
    @SafeHtml
    @Length(min = 3, max = 255)
    @Audited
    private String waterSupplyType;

    @SafeHtml
    @Audited
    private String description;

    @Audited
    private boolean active;

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

    public String getWaterSupplyType() {
        return waterSupplyType;
    }

    public void setWaterSupplyType(final String waterSupplyType) {
        this.waterSupplyType = waterSupplyType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

}
