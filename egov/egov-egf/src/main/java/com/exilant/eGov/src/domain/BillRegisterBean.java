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
package com.exilant.eGov.src.domain;

import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Iliyaraja
 *
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class BillRegisterBean
{
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;


    private static final Logger LOGGER = Logger.getLogger(BillRegisterBean.class);
    private String id = null;
    private String billNumber = null;
    private String billDate = null;
    private String billStatus = null;
    private String fieldId = null;
    private String worksDetailId = null;
    private double billAmount = 0.0;
    private String billNarration = "";
    private String expenditureType = null;
    private String billType = null;
    private double passedAmount = 0.0;
    private double advanceAdjusted = 0.0;
    private int createdby;
    private int lastModifiedBy;
    private String createdDate = "";
    private String lastModifiedDate = "";
    private String billStatusId = null;
    private TaskFailedException taskExc;
    private boolean isId = false, isField = false;
    private boolean isSelected = false;
    private String billDeptName = null;

    public BillRegisterBean() {
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getWorksDetailId() {
		return worksDetailId;
	}

	public void setWorksDetailId(String worksDetailId) {
		this.worksDetailId = worksDetailId;
	}

	public double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(double billAmount) {
		this.billAmount = billAmount;
	}

	public String getBillNarration() {
		return billNarration;
	}

	public void setBillNarration(String billNarration) {
		this.billNarration = billNarration;
	}

	public String getExpenditureType() {
		return expenditureType;
	}

	public void setExpenditureType(String expenditureType) {
		this.expenditureType = expenditureType;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public double getPassedAmount() {
		return passedAmount;
	}

	public void setPassedAmount(double passedAmount) {
		this.passedAmount = passedAmount;
	}

	public double getAdvanceAdjusted() {
		return advanceAdjusted;
	}

	public void setAdvanceAdjusted(double advanceAdjusted) {
		this.advanceAdjusted = advanceAdjusted;
	}

	public int getCreatedby() {
		return createdby;
	}

	public void setCreatedby(int createdby) {
		this.createdby = createdby;
	}

	public int getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(int lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getBillStatusId() {
		return billStatusId;
	}

	public void setBillStatusId(String billStatusId) {
		this.billStatusId = billStatusId;
	}

	public TaskFailedException getTaskExc() {
		return taskExc;
	}

	public void setTaskExc(TaskFailedException taskExc) {
		this.taskExc = taskExc;
	}

	public boolean getIsId() {
		return isId;
	}

	public void setIsId(boolean isId) {
		this.isId = isId;
	}

	public boolean isField() {
		return isField;
	}

	public void setField(boolean isField) {
		this.isField = isField;
	}

	public boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getBillDeptName() {
		return billDeptName;
	}

	public void setBillDeptName(String billDeptName) {
		this.billDeptName = billDeptName;
	}

   

   
}