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
package org.egov.infra.persistence.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.DocumentId;

import com.google.gson.annotations.Expose;

/**
 * Abstract base class for entities. Allows parameterization of id type, chooses
 * auto-generation and implements {@link #equals(Object)} and
 * {@link #hashCode()} based on that id.
 */
@MappedSuperclass
public abstract class AbstractPersistable<PK extends Serializable> implements Serializable {

    private static final long serialVersionUID = -477198900757851804L;

    @Expose
    @Id
    @GenericGenerator(name = "seq_id", strategy = "org.egov.infra.persistence.utils.SequenceIdGenerator")
    @GeneratedValue(generator = "seq_id")
    @DocumentId
    private PK id;

    @Version
    private Long version;

    public PK getId() {
        return id;
    }

    protected void setId(final PK id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public boolean isNew() {
        return null == getId();
    }

    @Override
    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
    }

    @Override
    public boolean equals(final Object obj) {
        if (null == obj)
            return false;

        if (this == obj)
            return true;

        if (!getClass().equals(obj.getClass()))
            return false;

        final AbstractPersistable<?> that = (AbstractPersistable<?>) obj;

        return null == this.getId() ? false : this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += null == getId() ? 0 : getId().hashCode() * 31;
        return hashCode;
    }
}