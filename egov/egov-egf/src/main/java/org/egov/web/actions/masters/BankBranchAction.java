/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.web.actions.masters;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.egov.commons.Bank;
import org.egov.commons.Bankbranch;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
@Transactional(readOnly=true)
public class BankBranchAction extends JQueryGridActionSupport {
	private static final long serialVersionUID = 1L;
	private String mode;
	private Integer bankId;
	private PersistenceService<Bankbranch, Integer> bankBranchPersistenceService;

	@Override
	public String execute() {
		if ("CRUD".equals(mode)) {
			try {
				if (oper.equals(ADD)) {
					addBankBranch();
				} else if (oper.equals(EDIT)) {
					editBankBranch();
				} else if (oper.equals(DELETE)) {
					deleteBankBranch();
				}
				sendAJAXResponse(SUCCESS);
			} catch (RuntimeException e) {
				sendAJAXResponse("failed");
				throw new EGOVRuntimeException("Error occurred in Bank Branch.", e);
			}
		} else if ("LIST_BRANCH".equals(mode)) {
			this.listAllBankBranches();
		} else if ("CHECK_UNQ_MICR".equals(mode)) {
			sendAJAXResponse(String.valueOf(checkIsUniqueMicr()));
		}
		return null;
	}

	private void addBankBranch() {
		final Bank bank = (Bank) persistenceService.getSession().load(Bank.class, bankId);
		final Date currentDate = new Date();
		final Bankbranch bankBranch = new Bankbranch();
		bankBranch.setBank(bank);
		bankBranch.setCreated(currentDate);
		populateBankBranchDetail(bankBranch);
		bankBranchPersistenceService.persist(bankBranch);
	}

	private void editBankBranch() {
		final Bankbranch bankBranch = (Bankbranch) bankBranchPersistenceService.getSession().get(Bankbranch.class, id);
		populateBankBranchDetail(bankBranch);
		bankBranchPersistenceService.update(bankBranch);
	}

	private void deleteBankBranch() {
		final Bankbranch bankBranch =(Bankbranch) bankBranchPersistenceService.getSession().load(Bankbranch.class, id);
		bankBranchPersistenceService.delete(bankBranch);
	}

	private void populateBankBranchDetail(final Bankbranch bankBranch) {
		final HttpServletRequest request = ServletActionContext.getRequest();
		bankBranch.setModifiedby(BigDecimal.valueOf(Long.valueOf(EgovThreadLocals.getUserId())));
		bankBranch.setLastmodified(new Date());
		bankBranch.setBranchcode(request.getParameter("branchcode"));
		bankBranch.setBranchname(request.getParameter("branchname"));
		bankBranch.setBranchaddress1(request.getParameter("branchaddress1"));
		bankBranch.setIsactive(request.getParameter("isactive").equals("Y"));
		bankBranch.setBranchaddress2(request.getParameter("branchaddress2"));
		bankBranch.setBranchcity(request.getParameter("branchcity"));
		bankBranch.setBranchstate(request.getParameter("branchstate"));
		bankBranch.setBranchpin(request.getParameter("branchpin"));
		bankBranch.setBranchphone(request.getParameter("branchphone"));
		bankBranch.setBranchfax(request.getParameter("branchfax"));
		bankBranch.setContactperson(request.getParameter("contactperson"));
		bankBranch.setNarration(request.getParameter("narration"));
		if (StringUtils.isNotBlank(request.getParameter("branchMICR")))
			bankBranch.setBranchMICR(BigDecimal.valueOf(Long.valueOf(request.getParameter("branchMICR"))).toString());
	}

	private void listAllBankBranches() {
		final List<Bankbranch> bankBranches = getPagedResult(Bankbranch.class,"bank.id", bankId).getList();
		final String jsonString = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
			@Override
			public boolean shouldSkipField(final FieldAttributes arg0) {
				return arg0.getName().equals("bankaccounts") || arg0.getName().equals("bank");
			}

			@Override
			public boolean shouldSkipClass(final Class<?> arg0) {
				return false;
			}
		}).create().toJson(bankBranches).replaceAll("true", "\"Y\"").replaceAll("false", "\"N\"");
		sendAJAXResponse(constructJqGridResponse(jsonString));
	}

	private boolean checkIsUniqueMicr() {
		boolean isUnique = true;
		final BigDecimal branchMICR = BigDecimal.valueOf(Long.valueOf(ServletActionContext.getRequest().getParameter("branchMICR")));
		if (branchMICR != null && id != null) {
			isUnique = null == persistenceService.find("from Bankbranch where branchMICR=? and id!=?", branchMICR, id);
		} else if (branchMICR != null) {
			isUnique = null == persistenceService.find("from Bankbranch where branchMICR=?", branchMICR);
		}
		return isUnique;
	}

	public void setBankBranchPersistenceService(final PersistenceService<Bankbranch, Integer> bankBranchPersistenceService) {
		this.bankBranchPersistenceService = bankBranchPersistenceService;
	}

	public void setMode(final String mode) {
		this.mode = mode;
	}

	public void setBankId(final Integer bankId) {
		this.bankId = bankId;
	}
}
