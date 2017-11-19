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
package org.egov.egf.web.actions.payment;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.ChequeFormat;
import org.egov.infra.utils.NumberToWord;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.services.instrument.InstrumentService;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParentPackage("egov")
@Results(value = {
        @Result(name = "chequeFormat-HTML", type = "stream", location = Constants.INPUT_STREAM, params = { Constants.INPUT_NAME,
                Constants.INPUT_STREAM,
                Constants.CONTENT_TYPE, "text/html" })
})
public class ChequeAssignmentPrintAction extends BaseFormAction {

    String jasperpath = "/reports/templates/ChequeFormat.jasper";
    ChequeFormat chequeFormat = new ChequeFormat();
    @Autowired
    @Qualifier("instrumentService")
    private InstrumentService instrumentService;
    private InputStream inputStream;
    private ReportHelper reportHelper;
    private List chequeFormatList = new ArrayList<ChequeFormat>();
    private final SimpleDateFormat DDMMYYYFORMAT = new SimpleDateFormat("ddMMYYYY");
    private final SimpleDateFormat DD_MON_YYYYFORMAT = Constants.DD_MON_YYYYFORMAT;
    private String instrumentHeader ;

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
    
    public boolean chequeFormatExists(){
        
        return true;
    }

    protected List<Object> getDataForChequeFormat() {
        
        return chequeFormatList;
    }

    protected String numberFormate(String amountToFormat)
    {
        DecimalFormat df = new DecimalFormat();
        String formatedAmount = df.format( Double.parseDouble(amountToFormat));
        return formatedAmount;
    }

    protected Map<String, Object> getParamMap() {
        InstrumentHeader instrumentDetails = new InstrumentHeader();
        if(instrumentHeader!=null){
            instrumentDetails = instrumentService.getInstrumentHeaderById(Long.valueOf(instrumentHeader));
        }
        
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if(instrumentDetails!=null){
        paramMap.put("payee", instrumentDetails.getPayTo());
        String totalAmount = numberFormate(instrumentDetails.getInstrumentAmount().toString());
        String amountInWords = NumberToWord.convertToWord(instrumentDetails.getInstrumentAmount().toString());
        final String chqDate = DDMMYYYFORMAT.format(instrumentDetails.getInstrumentDate());
        paramMap.put("totalAmount", totalAmount);
        paramMap.put("amountInWords", amountInWords);
        paramMap.put("chqDate", chqDate);
        }
        return paramMap;

    }

    public ChequeFormat getChequeFormat() {
        return chequeFormat;
    }

   

    public void setChequeFormat(ChequeFormat chequeFormat) {
        this.chequeFormat = chequeFormat;
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

    public String getInstrumentHeader() {
        return instrumentHeader;
    }

    public void setInstrumentHeader(String instrumentHeader) {
        this.instrumentHeader = instrumentHeader;
    }

}