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
/**
 *
 */
package org.egov.eb.web.action.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.CFinancialYear;
import org.egov.eb.domain.master.entity.EBConsumer;
import org.egov.eb.domain.master.entity.TargetArea;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.BaseFormAction;
import org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly=true)
@Results({ @Result(name = "AJAX_RESULT", type = "stream", location = "returnStream", params = { "contentType",
		"text/plain" }) })

public class EBCommonAction extends BaseFormAction {

	private static final Logger LOGGER = Logger.getLogger(EBCommonAction.class);
	
	private static final String AJAX_RESULT_RTGS = "rtgs";
	
	private static final long serialVersionUID = 1L;
	private String consumerNumber;
	private String accountNumber;
	private String targetAreaCode;
	private String targetAreaName;
	private Integer wardId;
	private List<EBConsumer> consumerList;
	private List<TargetArea> targetAreaList = new ArrayList<TargetArea>();
	private List<EBConsumer> accountNumberList;
	private List<TargetArea> targetAreaCodeList;
	private List<String> targetAreaNameList;
	
	private Integer month;
	private Long financialYearId;
	private List<String> rtgsNumbers;

	public EBCommonAction() {
	}

	public Object getModel() {

		return null;
	}

	@SuppressWarnings("unchecked")
@Action(value="/common/eBCommon-ajaxLoadEBConsumerNumbers")
	public String ajaxLoadEBConsumerNumbers() {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Starting ajaxLoadEBConsumerNumbers...");
		if (consumerNumber == null) {
			consumerList = new ArrayList<EBConsumer>();
		} else {

			consumerList = (List<EBConsumer>) persistenceService.findAllBy(
					"SELECT ebc FROM EBConsumer ebc where upper(ebc.code) like upper(?)", "%" + consumerNumber + "%");
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Completed ajaxLoadEBConsumerNumbers.");
		return "consumerNumbers";
	}

	@SuppressWarnings("unchecked")
@Action(value="/common/eBCommon-ajaxLoadEBAccountNumbers")
	public String ajaxLoadEBAccountNumbers() {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Starting ajaxLoadEBAccountNumbers...");
		if (accountNumber == null) {
			accountNumberList = new ArrayList<EBConsumer>();
		} else {

			accountNumberList = (List<EBConsumer>) persistenceService.findAllBy(
					"SELECT ebc FROM EBConsumer ebc where upper(ebc.name) like upper(?)", "%" + accountNumber + "%");
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Completed ajaxLoadEBAccountNumbers.");
		return "accountNumbers";
	}

	@SuppressWarnings("unchecked")
@Action(value="/common/eBCommon-ajaxLoadTargetAreaCodes")
	public String ajaxLoadTargetAreaCodes() {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Starting ajaxLoadTargetAreaCodes...");
		if (targetAreaCode == null) {
			targetAreaCodeList = new ArrayList<TargetArea>();
		} else {
			targetAreaCodeList = persistenceService.findAllBy("FROM TargetArea ta where upper(ta.code) like upper(?) OR upper(ta.name) like" +
					" upper(?) order by code",
					"%" + targetAreaCode+ "%","%" + targetAreaCode+ "%");  
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Completed ajaxLoadTargetAreaCodes.");
		return "targetAreaCodes";
	}

	@SuppressWarnings("unchecked")
@Action(value="/common/eBCommon-ajaxLoadTargetAreaNames")
	public String ajaxLoadTargetAreaNames() {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Starting ajaxLoadTargetAreaNames...");
		if (targetAreaName == null) {
			targetAreaNameList = new ArrayList<String>();
		} else {
			targetAreaNameList = (List<String>) persistenceService.findAllBy(
					"SELECT ta.name FROM TargetArea ta where upper(ta.name) like upper(?)", "%" + targetAreaName + "%");
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Completed ajaxLoadTargetAreaNames.");
		return "targetAreaNames";
	}

	@SuppressWarnings("unchecked")
@Action(value="/common/eBCommon-ajaxLoadTargetAreaByWard")
	public String ajaxLoadTargetAreaByWard() {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Starting ajaxLoadTargetAreaByWard...");
		TargetArea targetArea;
		if (wardId == null) {
			targetArea = new TargetArea();
		} else {
			targetArea = (TargetArea) persistenceService.find("select ta from TargetArea ta , TargetAreaMappings tam where " +
					"ta.id = tam.area.id and tam.boundary.id = ?",wardId);
		}
		targetAreaList.add(targetArea);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Completed ajaxLoadTargetAreaByWard.");
		return "targetArea";
	}
	
	@SuppressWarnings("unchecked")
	//TODO- consider the instrument status. take only NEW and Reconciled
	// Join the miscbilldetail and voucherheader using the billvhid
@Action(value="/common/eBCommon-ajaxLoadRTGSNumbers")
	public String ajaxLoadRTGSNumbers() {
		String query = "select distinct ih.transactionNumber " +
				"from EBDetails d, EgBillregister br, Miscbilldetail mbd, " +
				"InstrumentVoucher iv inner join iv.instrumentHeaderId ih " +
				"where d.receiptNo is null " +
				"and d.egBillregister = br " +
				"and br.billnumber = mbd.billnumber " +
				"and mbd.payVoucherHeader = iv.voucherHeaderId " +
				"and ih.statusId.code in ('"
				+ FinancialConstants.INSTRUMENT_CREATED_STATUS + "', '"
				+ FinancialConstants.INSTRUMENT_RECONCILED_STATUS + "') " +
				"and month(ih.transactionDate) = ? " +
				"and year(ih.transactionDate) between  ? and ?";
		
		CFinancialYear financialYear = (CFinancialYear) persistenceService.find("from CFinancialYear where id = ?",
				financialYearId);
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(financialYear.getStartingDate());
		Integer startingYear = calendar.get(Calendar.YEAR);
		
		calendar.setTime(financialYear.getEndingDate());
		Integer endingYear = calendar.get(Calendar.YEAR);
		
		rtgsNumbers = (List<String>) persistenceService.findAllBy(query, month, startingYear, endingYear);
		
		Collections.sort(rtgsNumbers);
		
		return AJAX_RESULT_RTGS; 
	}

	public String getConsumerNumber() {
		return consumerNumber;
	}

	public void setConsumerNumber(String consumerNumber) {
		this.consumerNumber = consumerNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public List<EBConsumer> getConsumerList() {
		return consumerList;
	}

	public void setConsumerList(List<EBConsumer> consumerList) {
		this.consumerList = consumerList;
	}

	public List<EBConsumer> getAccountNumberList() {
		return accountNumberList;
	}

	public void setAccountNumberList(List<EBConsumer> accountNumberList) {
		this.accountNumberList = accountNumberList;
	}

	public String getTargetAreaCode() {
		return targetAreaCode;
	}

	public void setTargetAreaCode(String targetAreaCode) {
		this.targetAreaCode = targetAreaCode;
	}

	public String getTargetAreaName() {
		return targetAreaName;
	}

	public void setTargetAreaName(String targetAreaName) {
		this.targetAreaName = targetAreaName;
	}

	public List<TargetArea> getTargetAreaCodeList() {
		return targetAreaCodeList;
	}

	public void setTargetAreaCodeList(List<TargetArea> targetAreaCodeList) {
		this.targetAreaCodeList = targetAreaCodeList;
	}

	public List<String> getTargetAreaNameList() {
		return targetAreaNameList;
	}

	public void setTargetAreaNameList(List<String> targetAreaNameList) {
		this.targetAreaNameList = targetAreaNameList;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public List<TargetArea> getTargetAreaList() {
		return targetAreaList;
	}

	public void setTargetAreaList(List<TargetArea> targetAreaList) {
		this.targetAreaList = targetAreaList;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Long getFinancialYearId() {
		return financialYearId;
	}

	public void setFinancialYearId(Long financialYearId) {
		this.financialYearId = financialYearId;
	}

	public List<String> getRtgsNumbers() {
		return rtgsNumbers;
	}

	public void setRtgsNumbers(List<String> rtgsNumbers) {
		this.rtgsNumbers = rtgsNumbers;
	}
}
