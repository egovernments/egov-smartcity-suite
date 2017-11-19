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

package org.egov.infra.admin.master.entity;

import com.google.gson.annotations.Expose;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.CompositeUnique;
import org.hibernate.annotations.Fetch;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;
import static org.egov.infra.admin.master.entity.AppConfig.FETCH_WITH_VALUES;
import static org.egov.infra.admin.master.entity.AppConfig.SEQ_APPCONFIG;
import static org.hibernate.annotations.FetchMode.JOIN;

@Entity
@Table(name = "eg_appconfig")
@SequenceGenerator(name = SEQ_APPCONFIG, sequenceName = SEQ_APPCONFIG, allocationSize = 1)
@CompositeUnique(fields = {"keyName", "module"}, enableDfltMsg = true)
@NamedEntityGraph(name = FETCH_WITH_VALUES, attributeNodes = @NamedAttributeNode("confValues"))
public class AppConfig extends AbstractAuditable {

    public static final String SEQ_APPCONFIG = "SEQ_EG_APPCONFIG";
    public static final String FETCH_WITH_VALUES = "AppConfig.values";
    private static final long serialVersionUID = 8904645810221559541L;
    @Expose
    @Id
    @GeneratedValue(generator = SEQ_APPCONFIG, strategy = SEQUENCE)
    private Long id;

    @NotBlank
    @SafeHtml
    @Length(max = 250)
    @Column(name = "key_name", updatable = false)
    private String keyName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "module", nullable = false, updatable = false)
    @NotNull
    private Module module;

    @NotBlank
    @SafeHtml
    @Length(max = 250)
    @Column(name = "description")
    private String description;

    @Valid
    @OneToMany(cascade = ALL, fetch = EAGER, mappedBy = "config", orphanRemoval = true)
    @Fetch(JOIN)
    private List<AppConfigValues> confValues = new ArrayList<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(final String keyName) {
        this.keyName = keyName;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(final Module module) {
        this.module = module;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<AppConfigValues> getConfValues() {
        confValues.forEach(configValue -> configValue.setConfig(this));
        return confValues;
    }

    public void setConfValues(final List<AppConfigValues> confValues) {
        this.confValues = confValues;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AppConfig))
            return false;
        final AppConfig appConfig = (AppConfig) o;
        return Objects.equals(keyName, appConfig.keyName) &&
                Objects.equals(module, appConfig.module);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyName, module);
    }
}