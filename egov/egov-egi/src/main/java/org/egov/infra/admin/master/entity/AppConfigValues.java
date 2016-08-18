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

package org.egov.infra.admin.master.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.search.domain.Searchable;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "eg_appconfig_values")
@Searchable
@SequenceGenerator(name = AppConfigValues.SEQ_APPCONFIG_VALUE, sequenceName = AppConfigValues.SEQ_APPCONFIG_VALUE, allocationSize = 1)
public class AppConfigValues extends AbstractAuditable {

    private static final long serialVersionUID = 1L;
    public static final String SEQ_APPCONFIG_VALUE = "SEQ_EG_APPCONFIG_VALUES";

    @Expose
    @DocumentId
    @Id
    @GeneratedValue(generator = SEQ_APPCONFIG_VALUE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @SafeHtml
    @Length(max = 4000)
    @Searchable
    @Column(name = "value")
    private String value;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "effective_from", updatable = false)
    private Date effectiveFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_id", nullable = false)
    @JsonIgnore
    private AppConfig config;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Date getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(final Date effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public AppConfig getConfig() {
        return config;
    }

    public void setConfig(final AppConfig config) {
        this.config = config;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AppConfigValues))
            return false;
        final AppConfigValues that = (AppConfigValues) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value);
    }
}