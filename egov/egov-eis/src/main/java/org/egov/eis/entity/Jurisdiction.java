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
package org.egov.eis.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.search.annotations.DocumentId;

@Entity
@Table(name = "egeis_jurisdiction")
@SequenceGenerator(name = Jurisdiction.SEQ_JURISDICTION, sequenceName = Jurisdiction.SEQ_JURISDICTION, allocationSize = 1)
public class Jurisdiction extends AbstractAuditable {

    private static final long serialVersionUID = 8931560836436430730L;

    public static final String SEQ_JURISDICTION = "SEQ_EGEIS_JURISDICTION";

    @DocumentId
    @Id
    @GeneratedValue(generator = SEQ_JURISDICTION, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boundarytype")
    private BoundaryType boundaryType;

    @OneToMany(mappedBy = "jurisdiction", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<JurisdictionDetails> jurisdictionDetails = new HashSet<JurisdictionDetails>();

    public Set<JurisdictionDetails> getJurisdictionDetails() {
        return jurisdictionDetails;
    }

    public void setJurisdictionDetails(final Set<JurisdictionDetails> jurisdictionDetails) {
        this.jurisdictionDetails = jurisdictionDetails;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(final Employee employee) {
        this.employee = employee;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public BoundaryType getBoundaryType() {
        return boundaryType;
    }

    public void setBoundaryType(final BoundaryType boundaryType) {
        this.boundaryType = boundaryType;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null)
            return false;

        if (this == obj)
            return true;

        if (!(obj instanceof Jurisdiction))
            return false;

        final Jurisdiction other = (Jurisdiction) obj;
        if (other.getEmployee().equals(getEmployee())) {
            if (other.getJurisdictionDetails().equals(getJurisdictionDetails()))
                return true;
            else
                return false;

        } else
            return false;

    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        if (getEmployee() != null)
            hashCode = hashCode + getEmployee().hashCode();

        if (getJurisdictionDetails() != null)
            hashCode = hashCode + getJurisdictionDetails().hashCode();

        return hashCode;

    }

    public boolean validate() {
        final BoundaryType bt = boundaryType;
        final Set bndries = jurisdictionDetails;
        for (final Iterator iter = bndries.iterator(); iter.hasNext();) {
            final JurisdictionDetails element = (JurisdictionDetails) iter.next();
            if (!element.getBoundary().getBoundaryType().equals(bt))
                throw new EGOVRuntimeException("Invalid Boundary " + element.getBoundary().getName()
                        + " for Boundary Type " + bt.getName() + ".");
        }

        return true;
    }

}
