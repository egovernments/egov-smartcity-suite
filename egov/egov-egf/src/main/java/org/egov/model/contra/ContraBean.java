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
package org.egov.model.contra;

import java.math.BigDecimal;

public class ContraBean {
    private String chequeInHand;
    private String cashInHand;
    private String bankBranchId;
    private String accountNumberId;
    private String fromBankAccountId;
    private String functionId;
    private String chequeNumber;
    private String chequeDate;
    private String fromBankId;
    private String bankId;
    private String toBankId;
    private BigDecimal amount;
    private String boundaryLevel;
    private String accnumnar;
    private BigDecimal availableBalance;
    private String fromBankBranchId;
    private String toBankBranchId;
    private String result;
    private String mode;;
    private BigDecimal accountBalance;
    private String saveMode;
    private String modeOfCollection;
    private String fromBankBalance;
    private String toBankBalance;
    private Integer fromFundId;
    private Integer toFundId;
    private String sourceGlcode;
    private String destinationGlcode;
    private Integer toDepartment;

    /**
     * @return the fromBankBalance
     */
    public String getFromBankBalance() {
        return fromBankBalance;
    }

    /**
     * @param fromBankBalance the fromBankBalance to set
     */
    public void setFromBankBalance(final String fromBankBalance) {
        this.fromBankBalance = fromBankBalance;
    }

    /**
     * @return the toBankBalance
     */
    public String getToBankBalance() {
        return toBankBalance;
    }

    /**
     * @param toBankBalance the toBankBalance to set
     */
    public void setToBankBalance(final String toBankBalance) {
        this.toBankBalance = toBankBalance;
    }

    public String getSaveMode() {
        return saveMode;
    }

    public void setSaveMode(final String saveMode) {
        this.saveMode = saveMode;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(final BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(final String result) {
        this.result = result;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(final String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getFromBankId() {
        return fromBankId;
    }

    public void setFromBankId(final String fromBankId) {
        this.fromBankId = fromBankId;
    }

    public String getSourceGlcode() {
        return sourceGlcode;
    }

    public void setSourceGlcode(final String sourceGlcode) {
        this.sourceGlcode = sourceGlcode;
    }

    public String getDestinationGlcode() {
        return destinationGlcode;
    }

    public void setDestinationGlcode(final String destinationGlcode) {
        this.destinationGlcode = destinationGlcode;
    }

    public String getToBankId() {
        return toBankId;
    }

    public void setToBankId(final String toBankId) {
        this.toBankId = toBankId;
    }

    public String getFromBankBranchId() {
        return fromBankBranchId;
    }

    public void setFromBankBranchId(final String fromBankBranchId) {
        this.fromBankBranchId = fromBankBranchId;
    }

    public String getToBankBranchId() {
        return toBankBranchId;
    }

    public void setToBankBranchId(final String toBankBranchId) {
        this.toBankBranchId = toBankBranchId;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(final String chequeDate) {
        this.chequeDate = chequeDate;
    }

    private String toBankAccountId;

    public String getFromBankAccountId() {
        return fromBankAccountId;
    }

    public void setFromBankAccountId(final String fromBankAccountId) {
        this.fromBankAccountId = fromBankAccountId;
    }

    public String getToBankAccountId() {
        return toBankAccountId;
    }

    public void setToBankAccountId(final String toBankAccountId) {
        this.toBankAccountId = toBankAccountId;
    }

    public String getChequeInHand() {
        return chequeInHand;
    }

    public void setChequeInHand(final String chequeInHand) {
        this.chequeInHand = chequeInHand;
    }

    public String getCashInHand() {
        return cashInHand;
    }

    public void setCashInHand(final String cashInHand) {
        this.cashInHand = cashInHand;
    }

    public String getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(final String bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public String getAccountNumberId() {
        return accountNumberId;
    }

    public void setAccountNumberId(final String accountNumberId) {
        this.accountNumberId = accountNumberId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public String getBoundaryLevel() {
        return boundaryLevel;
    }

    public void setBoundaryLevel(final String boundaryLevel) {
        this.boundaryLevel = boundaryLevel;
    }

    public String getAccnumnar() {
        return accnumnar;
    }

    public void setAccnumnar(final String accnumnar) {
        this.accnumnar = accnumnar;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(final BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(final String bankId) {
        this.bankId = bankId;
    }

    public String getModeOfCollection() {
        return modeOfCollection;
    }

    public void setModeOfCollection(final String modeOfCollection) {
        this.modeOfCollection = modeOfCollection;
    }

    public Integer getFromFundId() {
        return fromFundId;
    }

    public void setFromFundId(final Integer fromFundId) {
        this.fromFundId = fromFundId;
    }

    public Integer getToFundId() {
        return toFundId;
    }

    public void setToFundId(final Integer toFundId) {
        this.toFundId = toFundId;
    }

    public Integer getToDepartment() {
        return toDepartment;
    }

    public void setToDepartment(final Integer toDepartment) {
        this.toDepartment = toDepartment;
    }

    public void reset() {
        chequeInHand = null;
        cashInHand = null;
        bankBranchId = null;
        accountNumberId = null;
        fromBankAccountId = null;
        chequeNumber = null;
        chequeDate = null;
        fromBankId = null;
        bankId = null;
        toBankId = null;
        amount = null;
        boundaryLevel = null;
        accnumnar = null;
        availableBalance = null;
        fromBankBranchId = null;
        toBankBranchId = null;
        toBankAccountId = null;
        modeOfCollection = "cheque";
        fromBankBalance = null;
        toBankBalance = null;
        fromFundId = null;
        toFundId = null;
        toDepartment = null;

    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(final String functionId) {
        this.functionId = functionId;
    }
}
