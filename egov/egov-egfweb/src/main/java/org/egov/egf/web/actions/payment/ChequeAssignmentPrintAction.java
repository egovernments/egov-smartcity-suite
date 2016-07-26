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
package org.egov.egf.web.actions.payment;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.ChequeFormat;
import org.egov.infra.utils.NumberToWord;
import org.egov.infra.utils.NumberUtil;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;

@ParentPackage("egov")
@Results(value = {
        @Result(name = "chequeFormat-HTML", type = "stream", location = Constants.INPUT_STREAM, params = { Constants.INPUT_NAME,
                Constants.INPUT_STREAM,
                Constants.CONTENT_TYPE, "text/html" })
})
public class ChequeAssignmentPrintAction extends BaseFormAction {

    String jasperpath = "/reports/templates/ChequeFormat.jasper";
    ChequeFormat chequeFormat = new ChequeFormat();
    private String payee;
    private String chequeNo;
    String amount;
    private Date chequeDate;
    private InputStream inputStream;
    private ReportHelper reportHelper;
    private List chequeFormatList = new ArrayList<ChequeFormat>();
    private final SimpleDateFormat DDMMYYYFORMAT = new SimpleDateFormat("ddMMYYYY");
    private final SimpleDateFormat DD_MON_YYYYFORMAT = Constants.DD_MON_YYYYFORMAT;

    @Override
    public Object getModel() {
        return chequeFormat;
    }

    @SkipValidation
    @Action(value = "/payment/chequeAssignmentPrint-generateChequeFormat")
    public String generateChequeFormat() throws IOException {

        final Map<String, Object> paramMap = getParamMap();
        inputStream = reportHelper.exportHtml(inputStream, jasperpath, paramMap, getDataForChequeFormat(), "pt");
        return "chequeFormat-HTML";
    }

    protected List<Object> getDataForChequeFormat() {

        return chequeFormatList;
    }

    protected String numberFormate(String amountToFormat)
    {
        String formatedAmount = NumberUtil.formatNumber(BigDecimal.valueOf(Double.valueOf(amountToFormat)));
        return formatedAmount;
    }

    protected Map<String, Object> getParamMap() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("payee", payee);
        String totalAmount = numberFormate(amount);
        String amountInWords = NumberToWord.convertToWord(amount).replaceAll("Rupees", "").replaceAll("Only", "")
                .replaceAll(" and", " Rupees and");
        final String chqDate = DDMMYYYFORMAT.format(chequeDate);
        paramMap.put("totalAmount", totalAmount);
        paramMap.put("amountInWords", amountInWords);
        paramMap.put("chqDate", chqDate);
        return paramMap;

    }

    public ChequeFormat getChequeFormat() {
        return chequeFormat;
    }

    public String getPayee() {
        return payee;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public String getAmount() {
        return amount;
    }

    public Date getChequeDate() {
        return chequeDate;
    }

    public void setChequeFormat(ChequeFormat chequeFormat) {
        this.chequeFormat = chequeFormat;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setChequeDate(Date chequeDate) {
        this.chequeDate = chequeDate;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public ReportHelper getReportHelper() {
        return reportHelper;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setReportHelper(ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

}
