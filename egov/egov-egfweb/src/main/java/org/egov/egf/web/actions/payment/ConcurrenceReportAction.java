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

import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Bankaccount;
import org.egov.commons.Fund;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.ReportHelper;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Results(value = {
        @Result(name = "PDF", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                "application/pdf", "contentDisposition", "no-cache;filename=ConcurrenceReport.pdf" }),
                @Result(name = "XLS", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                        "application/xls", "contentDisposition", "no-cache;filename=ConcurrenceReport.xls" })
})
@ParentPackage("egov")

public class ConcurrenceReportAction extends BaseFormAction {
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;

    /**
     *
     */
    private static final long serialVersionUID = 6675640128074557827L;
    private List<ConcurrenceReportData> paymentHeaderList = new ArrayList<ConcurrenceReportData>();
    private List<ConcurrenceReportData> paymentHeaderListFnd = new ArrayList<ConcurrenceReportData>();
    List<Object> paymentHeaderReportList = new ArrayList<Object>();
    private BigDecimal grandTol = new BigDecimal("0");
    private static final Logger LOGGER = Logger.getLogger(ConcurrenceReportAction.class);
    private final String jasperpath = "/reports/templates/ConcurrenceReport.jasper";
    private Date asOnDate = new Date();
    private Date fromDate = new Date();
    private Date toDate = new Date();
    private Bankaccount bankAccount;
    private String dateType;
    private ReportHelper reportHelper;
    private InputStream inputStream;
    boolean bankAccountExist = false;
    // whether to include the payments for which cheque are assigned
    private String chequeOrRTGS;

    @Override
    public String execute() throws Exception {
        return "form";
    }

    @Override
    public void prepare() {
        super.prepare();
        if (!parameters.containsKey("skipPrepare")) {
            addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=true and isnotleaf=false order by name"));
            addDropdownData("bankList", Collections.EMPTY_LIST);
            addDropdownData("accNumList", Collections.EMPTY_LIST);
        }
    }

    @Override
    public Object getModel() {
        return null;
    }

    @Action(value = "/payment/concurrenceReport-exportPdf")
    public String exportPdf() throws JRException, IOException {
        ajaxLoadPaymentHeader();
        paymentHeaderReportList.addAll(paymentHeaderListFnd);
        inputStream = reportHelper.exportPdf(inputStream, jasperpath, getParamMap(), paymentHeaderReportList);
        return "PDF";
    }

    @Action(value = "/payment/concurrenceReport-exportXls")
    public String exportXls() throws JRException, IOException {
        ajaxLoadPaymentHeader();
        paymentHeaderReportList.addAll(paymentHeaderListFnd);
        inputStream = reportHelper.exportXls(inputStream, jasperpath, getParamMap(), paymentHeaderReportList);
        return "XLS";
    }

    @Action(value = "/payment/concurrenceReport-ajaxLoadPaymentHeader")
    public String ajaxLoadPaymentHeader() {
        grandTol = BigDecimal.ZERO;
        if (parameters.containsKey("bankAccount.id")
                && parameters.get("bankAccount.id")[0] != null) {
            final Integer id = Integer.valueOf(parameters.get("bankAccount.id")[0]);
            bankAccount = (Bankaccount) persistenceService.find(
                    "from Bankaccount where id=?", id);
            bankAccountExist = true;
            bankAccount.getBankbranch().getBank().getName();
        }
        if (parameters.containsKey("asOnDate")
                && parameters.get("asOnDate")[0] != null) {
            setDateData(parameters.get("asOnDate")[0], " ");
            final Query query = generateQuery();
            query.setDate("date", asOnDate).setResultTransformer(
                    Transformers.aliasToBean(ConcurrenceReportData.class));
            paymentHeaderList.addAll(query.list());
        } else if (parameters.containsKey("fromDate")
                && parameters.get("fromDate")[0] != null
                && parameters.containsKey("toDate")
                && parameters.get("toDate")[0] != null) {
            setDateData(parameters.get("fromDate")[0],
                    parameters.get("toDate")[0]);
            final Query query = generateQuery();
            query.setDate("fromDate", fromDate).setDate("toDate", toDate)
            .setResultTransformer(
                    Transformers
                    .aliasToBean(ConcurrenceReportData.class));
            paymentHeaderList.addAll(query.list());
        }
        generatePaymentList();
        return "results";
    }

    private void setDateData(final String frmDate, final String toDate) {
        try {
            if (!toDate.equals(" ")) {
                setDateType("0");
                setFromDate(Constants.DDMMYYYYFORMAT2.parse(frmDate));
                setToDate(Constants.DDMMYYYYFORMAT2.parse(toDate));
            } else {
                setDateType("1");
                setAsOnDate(Constants.DDMMYYYYFORMAT2.parse(frmDate));
            }
        } catch (final ParseException e) {
            throw new ValidationException("Invalid date", "Invalid date");
        }
    }

    /**
     * Creates every row in the view for Each fund
     */
    private void generatePaymentList() {
        if (paymentHeaderList.size() != 0) {
            BigDecimal fundAmt = new BigDecimal("0");
            String fndIdPre = paymentHeaderList.get(0).getFundId().toString();
            int lastInd;
            final int size = paymentHeaderList.size();
            paymentHeaderListFnd = new ArrayList<ConcurrenceReportData>();
            for (final ConcurrenceReportData row : paymentHeaderList) {
                if (row.getFundId().toString().equalsIgnoreCase(fndIdPre)) {
                    paymentHeaderListFnd.add(row);
                    fundAmt = fundAmt.add(row.getAmount());
                } else {
                    final Fund fundNm = (Fund) persistenceService.find(
                            "from Fund where id=?", Integer.valueOf(fndIdPre));
                    paymentHeaderListFnd.add(new ConcurrenceReportData(
                            new String(fundNm.getName()), fundAmt, "Total"));
                    grandTol = grandTol.add(fundAmt);
                    fundAmt = BigDecimal.ZERO;
                    fndIdPre = row.getFundId().toString();
                    paymentHeaderListFnd.add(row);
                    fundAmt = fundAmt.add(row.getAmount());
                }
                lastInd = paymentHeaderList.indexOf(row);
                if (lastInd == size - 1) {
                    final Fund fundNm = (Fund) persistenceService.find(
                            "from Fund where id=?", Integer.valueOf(fndIdPre));
                    paymentHeaderListFnd.add(new ConcurrenceReportData(
                            new String(fundNm.getName()), fundAmt, "Total"));
                    grandTol = grandTol.add(fundAmt);
                    setGrandTol(grandTol);
                }
            }
        }
    }

    private Query generateQuery() {
        final Query query = persistenceService.getSession().createSQLQuery(
                getQueryString().toString()).addScalar("bankName").addScalar(
                        "bankAccountNumber").addScalar("fundId").addScalar(
                                "departmentName").addScalar("billNumber").addScalar("billDate")
                                .addScalar("uac").addScalar("bpvNumber").addScalar("bpvDate")
                                .addScalar("bpvAccountCode").addScalar("amount");
        return query;
    }

    public void setPaymentHeaderList(
            final List<ConcurrenceReportData> paymentHeaderList) {
        this.paymentHeaderList = paymentHeaderList;
    }

    private StringBuffer getQueryString() {

        final StringBuffer queryString = new StringBuffer();
        String bankQry = "";
        String dateQry = "";
        String insturmentQry = "";

        if (dateType.equals("1"))
            dateQry = "ph.concurrenceDate <=:date and ";
        else if (dateType.equals("0"))
            dateQry = "ph.concurrenceDate >=:fromDate and ph.concurrenceDate <= :toDate and ";

        if (bankAccountExist) {
            bankQry = "ph.bankaccountnumberid=" + bankAccount.getId() + " and ";
            insturmentQry = " where bankaccountid=" + bankAccount.getId();
        } else
            bankQry = " ";

        if (StringUtils.isNotBlank(chequeOrRTGS)) {
            // query to fetch vouchers for which no cheque has been assigned
            String chqOrRtgsQry = "";
            if (Constants.CHEQUE.equals(chequeOrRTGS))
                // this part is same as below query except " and iv.VOUCHERHEADERID is null" is removed
                chqOrRtgsQry = "ih.INSTRUMENTNUMBER is not null and ih.INSTRUMENTTYPE = (select id from egf_instrumenttype where type = '"
                        + FinancialConstants.INSTRUMENT_TYPE_CHEQUE + "') and iv.instrumentheaderId = ih.id and ";
            else if (Constants.RTGS.equals(chequeOrRTGS))
                chqOrRtgsQry = "ih.TRANSACTIONNUMBER is not null and ih.INSTRUMENTTYPE = (select id from egf_instrumenttype where type = '"
                        + FinancialConstants.INSTRUMENT_TYPE_ADVICE + "') and iv.instrumentheaderId = ih.id and ";
            queryString
            .append("select bk.name   As bankName,ba.accountnumber As bankAccountNumber, vh.fundid As fundId,d.dept_name as departmentName,ms.billnumber as billNumber, ")
            .append("ms.billdate as billDate ,egusr.first_name as uac, vh.vouchernumber as bpvNumber, vh.voucherdate as bpvDate, gl.glcode as bpvAccountCode,")
            .append("ms.paidamount as amount  from miscbilldetail ms,bank bk,bankbranch bb,bankaccount ba, voucherheader vh,vouchermis vmis, eg_department d,")
            .append("generalledger gl,paymentheader ph,eg_wf_states es,EGF_INSTRUMENTHEADER ih, egf_instrumentvoucher iv right outer join voucherheader vh1 on ")
            .append("vh1.id =iv.VOUCHERHEADERID,egw_status egws, eg_user egusr where ph.voucherheaderid=vh.id and gl.debitamount!=0 and gl.debitamount is not null and vh.id= vmis.voucherheaderid and ")
            .append("vmis.departmentid= d.id_dept and ph.state_id=es.id and egusr.id_user=ph.createdby and es.value='END' and gl.voucherheaderid=vh.id and ")
            .append(" ms.payvhid=vh.id and ph.voucherheaderid=vh.id and ")
            .append(chqOrRtgsQry)
            .append(dateQry)
            .append(bankQry)
            .append(" ph.bankaccountnumberid=ba.id and ba.branchid=bb.id and bb.bankid=bk.id")
            .append(" and  vh1.id=vh.id and vh.status=0  group by vh.fundid, ms.billnumber, d.dept_name,")
            .append(" egusr.first_name, ms.billdate,gl.glcode,vh.vouchernumber,bk.name,ba.accountnumber, vh.voucherdate, ms.paidamount ");

        } else
            queryString
            .append("select bk.name   As bankName,ba.accountnumber As bankAccountNumber, vh.fundid As fundId,d.dept_name as departmentName,ms.billnumber as billNumber, ")
            .append("ms.billdate as billDate ,egusr.first_name as uac, vh.vouchernumber as bpvNumber, vh.voucherdate as bpvDate, gl.glcode as bpvAccountCode,")
            .append("ms.paidamount as amount  from miscbilldetail ms,bank bk,bankbranch bb,bankaccount ba, voucherheader vh,vouchermis vmis, eg_department d,")
            .append("generalledger gl,paymentheader ph,eg_wf_states es,egf_instrumentvoucher iv right outer join voucherheader vh1 on ")
            .append("vh1.id =iv.VOUCHERHEADERID,egw_status egws, eg_user egusr where ph.voucherheaderid=vh.id and gl.debitamount!=0 and gl.debitamount is not null and vh.id= vmis.voucherheaderid and ")
            .append("vmis.departmentid= d.id_dept and ph.state_id=es.id and egusr.id_user=ph.createdby and es.value='END' and gl.voucherheaderid=vh.id and ")
            .append(" ms.payvhid=vh.id and ph.voucherheaderid=vh.id and ")
            .append(dateQry)
            .append(bankQry)
            .append(" ph.bankaccountnumberid=ba.id and ba.branchid=bb.id and bb.bankid=bk.id")
            .append(" and  vh1.id=vh.id and vh.status=0 and iv.VOUCHERHEADERID is null group by vh.fundid, ms.billnumber, d.dept_name,")
            .append(" egusr.first_name, ms.billdate,gl.glcode,vh.vouchernumber,bk.name,ba.accountnumber, vh.voucherdate, ms.paidamount ")
            .append(" union ")
            // query to fetch vouchers for which cheque has been assigned and surrendered
            .append(" select bk.name   As bankName,ba.accountnumber As bankAccountNumber, vh.fundid As fundId,d.dept_name as departmentName,ms.billnumber as billNumber, ")
            .append("ms.billdate as billDate ,egusr.first_name as uac, vh.vouchernumber as bpvNumber, vh.voucherdate as bpvDate, gl.glcode as bpvAccountCode,")
            .append("ms.paidamount as amount  from miscbilldetail ms, bank bk,bankbranch bb,bankaccount ba, egf_instrumentvoucher iv,voucherheader vh,")
            .append("vouchermis vmis, eg_department d,generalledger gl,")
            .append("paymentheader ph,eg_wf_states es, eg_user egusr,egw_status egws,(select ih1.id,ih1.id_status from egf_instrumentheader ih1, ")
            .append("(select bankid,bankaccountid,instrumentnumber,max(lastmodifieddate) as lastmodifieddate from egf_instrumentheader ")
            .append(insturmentQry)
            .append(" group by bankid,bankaccountid,")
            .append("instrumentnumber order by lastmodifieddate desc) max_rec where max_rec.bankid=ih1.bankid and max_rec.bankaccountid=ih1.bankaccountid and max_rec.instrumentnumber=ih1.instrumentnumber ")
            .append("and max_rec.lastmodifieddate=ih1.lastmodifieddate and rownum=1) ih where ph.voucherheaderid=vh.id and ms.payvhid=vh.id and vh.id= vmis.voucherheaderid and ")
            .append("vmis.departmentid= d.id_dept and ph.state_id=es.id and es.value='END' and egusr.id_user=ph.createdby and gl.voucherheaderid=vh.id and ph.voucherheaderid=vh.id ")
            .append(" and  iv.voucherheaderid=vh.id and iv.instrumentheaderid=ih.id and vh.status=0 and ")
            .append("ih.id_status=egws.id and egws.description in ('Surrendered','Surrender_For_Reassign') and gl.debitamount!=0 and gl.debitamount is not null and ")
            .append(dateQry).append(bankQry)
            .append(" ph.bankaccountnumberid=ba.id and ba.branchid=bb.id and bb.bankid=bk.id and vh.type='")
            .append(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT).append("'");
        return queryString.append("order by fundid ,bankaccountnumber,billdate");
    }

    public String getChequeOrRTGS() {
        return chequeOrRTGS;
    }

    public void setChequeOrRTGS(final String chequeOrRTGS) {
        this.chequeOrRTGS = chequeOrRTGS;
    }

    public String getUlbName() {
        final Query query = persistenceService.getSession().createSQLQuery(
                "select name from companydetail");
        final List<String> result = query.list();
        if (result != null)
            return result.get(0);
        return "";
    }

    Map<String, Object> getParamMap() {
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        String header = "";
        paramMap.put("ulbName", getUlbName());
        paramMap.put("grandTol", grandTol);
        String bankName = " ";
        if (bankAccountExist)
            bankName = "for ".concat(
                    bankAccount.getBankbranch().getBank().getName())
                    .concat("-").concat(
                            bankAccount.getBankbranch().getBranchname())
                            .concat("-").concat(bankAccount.getAccountnumber());
        if (dateType.equals("1"))
            header = "Concurrence Report " + bankName + " as on "
                    + Constants.DDMMYYYYFORMAT2.format(asOnDate);
        else
            header = "Concurrence Report " + bankName + " "
                    + Constants.DDMMYYYYFORMAT2.format(fromDate) + " - "
                    + Constants.DDMMYYYYFORMAT2.format(toDate);
        paramMap.put("heading", header);
        paramMap.put("paymentHeaderListFnd", paymentHeaderListFnd);
        paramMap.put("paymentHeaderReportList", paymentHeaderReportList);
        return paramMap;
    }

    public List<ConcurrenceReportData> getPaymentHeaderList() {
        return paymentHeaderList;
    }

    public List<ConcurrenceReportData> getPaymentHeaderListFnd() {
        return paymentHeaderListFnd;
    }

    public void setPaymentHeaderListFnd(
            final List<ConcurrenceReportData> paymentHeaderListFnd) {
        this.paymentHeaderListFnd = paymentHeaderListFnd;
    }

    public String getFormattedDate(final Date date) {
        return Constants.DDMMYYYYFORMAT2.format(date);
    }

    public void setBankAccount(final Bankaccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Bankaccount getBankAccount() {
        return bankAccount;
    }

    public void setAsOnDate(final Date asOnDate) {
        this.asOnDate = asOnDate;
    }

    public Date getAsOnDate() {
        return asOnDate;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(final String dateType) {
        this.dateType = dateType;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public BigDecimal getGrandTol() {
        return grandTol;
    }

    public void setGrandTol(final BigDecimal grandTol) {
        this.grandTol = grandTol;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public String getFormattedAsOnDate() {
        return Constants.DDMMYYYYFORMAT2.format(asOnDate);
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public List<Object> getPaymentHeaderReportList() {
        return paymentHeaderReportList;
    }

    public void setPaymentHeaderReportList(final List<Object> paymentHeaderReportList) {
        this.paymentHeaderReportList = paymentHeaderReportList;
    }

}