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
package org.egov.model.instrument;

import java.math.BigDecimal;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.infstr.models.BaseModel;

public class DishonorChequeDetails extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = -6790212647262088197L;
    private DishonorCheque header;
    private CChartOfAccounts glcodeId;
    private BigDecimal debitAmt;
    private BigDecimal creditAmount;
    private Accountdetailkey detailKey;
    private Accountdetailtype detailType;
    private Integer functionId;
    private CFunction function;

    public Integer getFunctionId() {
        return functionId;
    }

    public void setFunctionId(final Integer functionId) {
        this.functionId = functionId;
    }

    public DishonorCheque getHeader() {
        return header;
    }

    public void setHeader(final DishonorCheque header) {
        this.header = header;
    }

    public CChartOfAccounts getGlcodeId() {
        return glcodeId;
    }

    public void setGlcodeId(final CChartOfAccounts glcodeId) {
        this.glcodeId = glcodeId;
    }

    public BigDecimal getDebitAmt() {
        return debitAmt;
    }

    public void setDebitAmt(final BigDecimal debitAmt) {
        this.debitAmt = debitAmt;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(final BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Accountdetailkey getDetailKey() {
        return detailKey;
    }

    public void setDetailKey(final Accountdetailkey detailKey) {
        this.detailKey = detailKey;
    }

    public Accountdetailtype getDetailType() {
        return detailType;
    }

    public void setDetailType(final Accountdetailtype detailType) {
        this.detailType = detailType;
    }

    public CFunction getFunction() {
        return function;
    }

    public void setFunction(final CFunction function) {
        this.function = function;
    }

}
