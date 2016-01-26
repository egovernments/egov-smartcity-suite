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
package org.egov.commons;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "FUNCTIONARY")
@SequenceGenerator(name = Functionary.SEQ_FUNCTIONARY, sequenceName = Functionary.SEQ_FUNCTIONARY, allocationSize = 1)
public class Functionary extends AbstractAuditable implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String SEQ_FUNCTIONARY = "SEQ_FUNCTIONARY";

	@Id
	@GeneratedValue(generator = SEQ_FUNCTIONARY, strategy = GenerationType.SEQUENCE)
	private Long id;

	@NotNull
	private BigDecimal code;
	
	@Length(max = 256)
	@NotNull
	private String name;

	private Boolean isactive;

	public Functionary() {
		// For hibernate to work
	}

	public Functionary(Long id, BigDecimal code, String name) {
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public Functionary(Long id, BigDecimal code, String name, Boolean isactive) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.isactive = isactive;
	}

	public Long getId() {
		return id;

	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getCode() {
		return this.code;
	}

	public void setCode(BigDecimal code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}

}
