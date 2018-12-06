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

import org.egov.infra.cache.impl.LRUCache;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.egov.infra.admin.master.entity.Action.SEQ_ACTION;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_ALPHABETS;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_ALPHABETS_UNDERSCORE_HYPHEN_SPACE;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_ALPHABETS_WITH_SPACE;
import static org.egov.infra.validation.constants.ValidationRegex.ALPHABETS;
import static org.egov.infra.validation.constants.ValidationRegex.ALPHABETS_UNDERSCORE_HYPHEN_SPACE;
import static org.egov.infra.validation.constants.ValidationRegex.ALPHABETS_WITH_SPACE;

@Entity
@Table(name = "eg_action")
@Unique(fields = "name", enableDfltMsg = true)
@SequenceGenerator(name = SEQ_ACTION, sequenceName = SEQ_ACTION, allocationSize = 1)
@Cacheable
public class Action extends AbstractAuditable {

    protected static final String SEQ_ACTION = "SEQ_EG_ACTION";
    private static final long serialVersionUID = -5459067787684736822L;
    private static final LRUCache<String, java.util.regex.Pattern> QUERY_PARAM_PATTERN_CACHE = new LRUCache<>(0, 100);

    @Id
    @GeneratedValue(generator = SEQ_ACTION, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    @SafeHtml
    @NotBlank
    @Length(max = 100)
    @Pattern(regexp = ALPHABETS_UNDERSCORE_HYPHEN_SPACE, message = INVALID_ALPHABETS_UNDERSCORE_HYPHEN_SPACE)
    private String name;

    @SafeHtml
    @NotBlank
    @Length(max = 150)
    @URL
    private String url;

    @SafeHtml
    @Length(max = 150)
    private String queryParams;

    @SafeHtml
    @Length(max = 50)
    private String queryParamRegex;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "eg_roleaction", joinColumns = @JoinColumn(name = "actionid"),
            inverseJoinColumns = @JoinColumn(name = "roleid"))
    @Fetch(FetchMode.SUBSELECT)
    private Set<Role> roles = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parentModule")
    @NotNull
    private Module parentModule;

    @Min(0)
    private Integer orderNumber;

    @SafeHtml
    @Length(max = 80)
    @Pattern(regexp = ALPHABETS_WITH_SPACE, message = INVALID_ALPHABETS_WITH_SPACE)
    private String displayName;

    private boolean enabled;

    @SafeHtml
    @NotBlank
    @Length(max = 32)
    @Pattern(regexp = ALPHABETS, message = INVALID_ALPHABETS)
    private String contextRoot;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public String getQueryParamRegex() {
        return queryParamRegex;
    }

    public void setQueryParamRegex(String queryParamRegex) {
        this.queryParamRegex = queryParamRegex;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Module getParentModule() {
        return parentModule;
    }

    public void setParentModule(Module parentModule) {
        this.parentModule = parentModule;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getContextRoot() {
        return contextRoot;
    }

    public void setContextRoot(String contextRoot) {
        this.contextRoot = contextRoot;
    }

    public void addRole(Role role) {
        if (!hasRole(role))
            getRoles().add(role);
    }

    public void removeRole(Role role) {
        if (hasRole(role))
            getRoles().remove(role);
    }

    public boolean hasRole(Role role) {
        return this.getRoles().contains(role);
    }

    public boolean queryParamMatches(String queryParams) {
        return isBlank(queryParamRegex) || QUERY_PARAM_PATTERN_CACHE
                .computeIfAbsent(queryParamRegex, val -> java.util.regex.Pattern.compile(queryParamRegex))
                .matcher(queryParams).matches();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Action))
            return false;
        Action action = (Action) other;
        return Objects.equals(getName(), action.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}