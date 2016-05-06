/* eGov suite of products aim to improve the internal efficiency,transparency,
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

package org.egov.mrs.masters.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

/**
 * Entity representing the Fee to be paid for Marriage Registration/Re-issue
 *
 * @author nayeem
 *
 */

@Entity
@Table(name = "egmrs_fee")
@SequenceGenerator(name = Fee.SEQ_FEE, sequenceName = Fee.SEQ_FEE, allocationSize = 1)
public class Fee extends AbstractAuditable {

    private static final long serialVersionUID = 4605301246092443240L;
    public static final String SEQ_FEE = "SEQ_EGMRS_FEE";

    @Id
    @GeneratedValue(generator = SEQ_FEE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @SafeHtml
    @Length(max = 50)
    private String criteria;

    @NotNull
    private Double fees;
    
    private Integer fromDays;
    private Integer toDays;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(final String criteria) {
        this.criteria = criteria;
    }

    public Double getFees() {
        return fees;
    }

    public void setFees(final Double fees) {
        this.fees = fees;
    }
    
    public Integer getFromDays() {
        return fromDays;
    }

    public void setFromDays(Integer fromDays) {
        this.fromDays = fromDays;
    }

    public Integer getToDays() {
        return toDays;
    }
    
    public void setToDays(Integer toDays) {
        this.toDays = toDays;
    }
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (criteria == null ? 0 : criteria.hashCode());
        result = prime * result + (fees == null ? 0 : fees.hashCode());
        result = prime * result + (id == null ? 0 : id.hashCode());
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
        
        final Fee other = (Fee) obj;

        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        
        if (criteria == null) {
            if (other.criteria != null)
                return false;
        } else if (!criteria.equals(other.criteria))
            return false;
        
        if (fees == null) {
            if (other.fees != null)
                return false;
        } else if (!fees.equals(other.fees))
            return false;
        
        return true;
    }

}
