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

package org.egov.infra.admin.common.entity;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import static org.egov.infra.admin.common.entity.Favourites.SEQ_FAVOURITES;

@Entity
@Table(name = "eg_favourites", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "actionId"}))
@SequenceGenerator(name = SEQ_FAVOURITES, sequenceName = SEQ_FAVOURITES, allocationSize = 1)
public class Favourites extends AbstractPersistable<Long> {

    public static final String SEQ_FAVOURITES = "SEQ_EG_FAVOURITES";
    private static final long serialVersionUID = 8966137226966715994L;
    @Id
    @GeneratedValue(generator = SEQ_FAVOURITES, strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long userId;

    private Integer actionId;

    @SafeHtml
    @NotBlank
    private String name;

    @SafeHtml
    @NotBlank
    private String contextRoot;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(final Integer actionId) {
        this.actionId = actionId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getContextRoot() {
        return contextRoot;
    }

    public void setContextRoot(final String contextRoot) {
        this.contextRoot = contextRoot;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (actionId == null ? 0 : actionId.hashCode());
        result = prime * result + (contextRoot == null ? 0 : contextRoot.hashCode());
        result = prime * result + (id == null ? 0 : id.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (userId == null ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Favourites other = (Favourites) obj;
        if (actionId == null) {
            if (other.actionId != null)
                return false;
        } else if (!actionId.equals(other.actionId))
            return false;
        if (contextRoot == null) {
            if (other.contextRoot != null)
                return false;
        } else if (!contextRoot.equals(other.contextRoot))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

}
