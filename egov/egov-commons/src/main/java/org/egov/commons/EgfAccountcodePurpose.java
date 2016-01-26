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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGF_ACCOUNTCODE_PURPOSE")
@SequenceGenerator(name = EgfAccountcodePurpose.SEQ_EGF_ACCOUNTCODE_PURPOSE, sequenceName = EgfAccountcodePurpose.SEQ_EGF_ACCOUNTCODE_PURPOSE)
public class EgfAccountcodePurpose implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String SEQ_EGF_ACCOUNTCODE_PURPOSE = "SEQ_EGF_ACCOUNTCODE_PURPOSE";

	@Id
	@GeneratedValue(generator = SEQ_EGF_ACCOUNTCODE_PURPOSE, strategy = GenerationType.SEQUENCE)
	private Integer id;

	@Length(max = 250)
	@Column(unique = true)
	private String name;

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy="purposeId", targetEntity=CChartOfAccounts.class)
    private Set<CChartOfAccounts> chartofaccountses = new HashSet<CChartOfAccounts>(0);

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy="egfAccountcodePurpose", targetEntity=Fund.class)
	private Set<Fund> funds = new HashSet<Fund>(0);

	public EgfAccountcodePurpose() {
		//For hibernate to work
	}

	public EgfAccountcodePurpose(Integer id) {
		this.id = id;
	}

	public EgfAccountcodePurpose(Integer id, String name, Set chartofaccountses, Set funds) {
		this.id = id;
		this.name = name;
		this.chartofaccountses = chartofaccountses;
		this.funds = funds;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set getChartofaccountses() {
		return this.chartofaccountses;
	}

	public void setChartofaccountses(Set chartofaccountses) {
		this.chartofaccountses = chartofaccountses;
	}

	public Set getFunds() {
		return this.funds;
	}

	public void setFunds(Set funds) {
		this.funds = funds;
	}

}
