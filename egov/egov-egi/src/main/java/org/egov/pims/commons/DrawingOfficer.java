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
package org.egov.pims.commons;

import org.egov.commons.Bank;
import org.egov.commons.Bankbranch;
import org.egov.commons.EgwStatus;
import org.egov.commons.utils.EntityType;

public class DrawingOfficer implements  java.io.Serializable,EntityType {
	private static final long serialVersionUID = 1L;
	public static final String QRY_DO_STARTSWITH="DRAWINGOFFICER_STARTSWITH"; 
	private Integer id ;
	private String code;
	private String name;
	private Bank bank;
	private Bankbranch bankbranch;
	String accountNumber;
	String tan;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getBankaccount() {
		return null;
	}
	@Override
	public String getBankname() {
		return null;
	}
	
	public String getEntityDescription() {
		return getName();
	}
	@Override	
	public Integer getEntityId() {
		return this.id;		
	}
	@Override
	public String getIfsccode() {
		return null;
	}
	@Override
	public String getModeofpay() {
		return null;
	}
	@Override
	public String getPanno() {
		return null;
	}
	@Override
	public String getTinno() {
		return null;
	}
	
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	public Bankbranch getBankbranch() {
		return bankbranch;
	}
	public void setBankbranch(Bankbranch bankbranch) {
		this.bankbranch = bankbranch;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getTan() {
		return tan;
	}
	public void setTan(String tan) {
		this.tan = tan;
	}
	@Override
	public EgwStatus getEgwStatus() {
		// TODO Auto-generated method stub
		return null;
	}

}
