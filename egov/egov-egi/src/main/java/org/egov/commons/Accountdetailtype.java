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
package org.egov.commons;

import java.math.BigDecimal;
import javax.persistence.Column;
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
@Table(name="ACCOUNTDETAILTYPE")
@SequenceGenerator(name = Accountdetailtype.SEQ_ACCOUNTDETAILTYPE, sequenceName = Accountdetailtype.SEQ_ACCOUNTDETAILTYPE, allocationSize = 1)
public class Accountdetailtype extends AbstractAuditable implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String SEQ_ACCOUNTDETAILTYPE = "SEQ_ACCOUNTDETAILTYPE";

	@Id
	@GeneratedValue(generator = SEQ_ACCOUNTDETAILTYPE, strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(nullable=false, unique=true)
	@Length(max=50)
	private String name;
	
	@NotNull
	@Length(max=50)
	private String description;

	@Length(max=25)
	private String tablename;

	@Length(max=25)
	private String columnname;

	@Column(nullable=false, unique=true)
	@Length(max=50)
	private String attributename;

	@NotNull
	private BigDecimal nbroflevels;

	private Boolean isactive;

	@Column(name="FULL_QUALIFIED_NAME")
	@Length(max=250)
	private String fullQualifiedName;

	//private Accountdetailtype accountdetailtype;

	public Accountdetailtype() {
		//For hibernate to work
	}

	public Accountdetailtype(String name, String description, String attributename, BigDecimal nbroflevels) {
		this.name = name;
		this.description = description;
		this.attributename = attributename;
		this.nbroflevels = nbroflevels;
	}

	public Accountdetailtype(String name, String description, String tablename, String columnname, String attributename, BigDecimal nbroflevels, Boolean isactive) {
		this.name = name;
		this.description = description;
		this.tablename = tablename;
		this.columnname = columnname;
		this.attributename = attributename;
		this.nbroflevels = nbroflevels;
		this.isactive = isactive;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTablename() {
		return this.tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getColumnname() {
		return this.columnname;
	}

	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}

	public String getAttributename() {
		return this.attributename;
	}

	public void setAttributename(String attributename) {
		this.attributename = attributename;
	}

	public BigDecimal getNbroflevels() {
		return this.nbroflevels;
	}

	public void setNbroflevels(BigDecimal nbroflevels) {
		this.nbroflevels = nbroflevels;
	}

	public Boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}

	/*public Accountdetailtype getAccountdetailtype() {
		return this.accountdetailtype;
	}

	public void setAccountdetailtype(Accountdetailtype accountdetailtype) {
		this.accountdetailtype = accountdetailtype;
	}*/

	public String getFullQualifiedName() {
		return fullQualifiedName;
	}

	public void setFullQualifiedName(String fullQualifiedName) {
		this.fullQualifiedName = fullQualifiedName;
	}

}
