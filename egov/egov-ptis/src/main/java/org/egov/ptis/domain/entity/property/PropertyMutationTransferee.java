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
package org.egov.ptis.domain.entity.property;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.infra.persistence.entity.enums.UserType;

public class PropertyMutationTransferee extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 1L;
    private Long id; 
    private PropertyMutation propertyMutation;
    private User transferee;
    //TODO LATER now setting citizen as owner type in future this should be captured on owner transfer form
    private UserType ownerType = UserType.CITIZEN;

    public PropertyMutationTransferee() {
        
    }

    public PropertyMutationTransferee(final PropertyMutation propertyMutation, final User transferee,
            final Integer orderNo) {

        this.propertyMutation = propertyMutation;
        this.transferee = transferee;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public UserType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(final UserType ownerType) {
        this.ownerType = ownerType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (id == null ? 0 : id.hashCode());
        result = prime * result + (transferee == null ? 0 : transferee.hashCode());
        result = prime * result + (propertyMutation == null ? 0 : propertyMutation.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final PropertyMutationTransferee other = (PropertyMutationTransferee) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (transferee == null) {
            if (other.transferee != null)
                return false;
        } else if (!transferee.equals(other.transferee))
            return false;
        if (propertyMutation == null) {
            if (other.propertyMutation != null)
                return false;
        } else if (!propertyMutation.equals(other.propertyMutation))
            return false;
        return true;
    }

    public PropertyMutation getPropertyMutation() {
        return propertyMutation;
    }

    public void setPropertyMutation(PropertyMutation propertyMutation) {
        this.propertyMutation = propertyMutation;
    }

    public User getTransferee() {
        return transferee;
    }

    public void setTransferee(User transferee) {
        this.transferee = transferee;
    }

}
