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
package org.egov.egf.web.actions.revenue;

import net.sf.jasperreports.engine.JRException;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CVoucherHeader;
import org.egov.egf.revenue.FixedDeposit;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.utils.ReportHelper;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Results(value = {
        @Result(name = "PDF", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                "application/pdf", "contentDisposition", "no-cache;filename=FixedDepositReport.pdf" }),
                @Result(name = "XLS", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                        "application/xls", "contentDisposition", "no-cache;filename=FixedDepositReport.xls" })
})
@ParentPackage("egov")

public class FixedDepositAction extends BaseFormAction {
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;

    private static final long serialVersionUID = -145348568312338226L;
    protected List<FixedDeposit> fixedDepositList;
    private List<Bankbranch> bankBranchList;
    private BigDecimal voucherAmount; // either Receipt or GJV amount
    private FixedDeposit fixedDep = new FixedDeposit();
    private String serialNo = "";
    private ReportHelper reportHelper;
    private InputStream inputStream;
    private Date toDate;
    private Date fromDate;
    private String mode;
    public static final String VIEW = "view";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private List<Bankaccount> bankAccountListTemp = new ArrayList<Bankaccount>();
    protected List<FixedDeposit> childFDList = new ArrayList<FixedDeposit>();
    private final String jasperpath = "/reports/templates/FixedDepositReport.jasper";
    private static final Logger LOGGER = Logger.getLogger(FixedDepositAction.class);

    public FixedDepositAction() {
        super();
        fixedDep = new FixedDeposit();
        // fixedDep.setParentId(parentId)

    }

    @Override
    @SuppressWarnings("unchecked")
    public void prepare() {
        bankBranchList = persistenceService.findAllBy("from Bankbranch br where br.isactive=true order by br.bank.name asc ");
    }

    @Override
    public Object getModel() {
        return fixedDep;
    }

    @SkipValidation
    @Action(value = "/revenue/fixedDeposit-newForm")
    public String newForm() {
        fixedDepositList = new ArrayList<FixedDeposit>();
        fixedDepositList.add(new FixedDeposit());
        childFDList.add(new FixedDeposit());
        mode = NEW;
        return NEW;
    }

    @SkipValidation
    @Action(value = "/revenue/fixedDeposit-beforeEdit")
    public String beforeEdit() {
        fixedDepositList = new ArrayList<FixedDeposit>();
        // fixedDepositList.add(new FixedDeposit());
        // childFDList.add(new FixedDeposit());
        mode = EDIT;
        return EDIT;
    }

    @SkipValidation
    @Action(value = "/revenue/fixedDeposit-beforeSearch")
    public String beforeSearch() {
        fixedDepositList = new ArrayList<FixedDeposit>();
        mode = VIEW;
        return VIEW;
    }

    @SuppressWarnings("unchecked")
    @SkipValidation
    @Action(value = "/revenue/fixedDeposit-search")
    public String search() {
        final StringBuffer query = new StringBuffer();
        query.append("From FixedDeposit ");
        if (fromDate != null && toDate != null)
            query.append("where date >='" + sdf.format(fromDate) + "' and date <='" + sdf.format(toDate) + "'");
        else if (fromDate == null && toDate == null)
            query.append("where date<= CURRENT_DATE");
        else if (fromDate != null)
            query.append("where date>='" + sdf.format(fromDate) + "'");
        else
            query.append("where date<='" + sdf.format(toDate) + "'");
        query.append("  order by id");

        fixedDepositList = persistenceService.findAllBy(query.toString());
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Fixed deposit size= " + fixedDepositList.size());

        for (final FixedDeposit fd : fixedDepositList) {
            bankAccountListTemp = getPersistenceService().findAllBy(
                    "from Bankaccount ba where ba.bankbranch.id=? and isactive=true order by ba.chartofaccounts.glcode",
                    fd.getBankBranch().getId());
            fd.setBankAccountList(bankAccountListTemp);
            if (fd.getReceiptAmount() == null)
                fd.setReceiptAmount(BigDecimal.ZERO);
            if (fd.getParentId() != null)
                fd.setParentTemp(fd.getParentId().getId());
            else
                fd.setParentId(null);
        }
        if (VIEW.equalsIgnoreCase(mode))
            return VIEW;
        else
            return EDIT;

    }

    /*
     * public void validate(){ for(FixedDeposit fd : fixedDepositList){ if(fd.getChallanReceiptVoucher().getId()!=null &&
     * fd.getInFlowVoucher().getId()!=null){ addFieldError("", getText("double.select")); } } }
     */

    // @ValidationErrorPage(value="edit")
    @SuppressWarnings("unchecked")
    @Action(value = "/revenue/fixedDeposit-update")
    public String saveOrupdate() {
        for (final FixedDeposit fd : fixedDepositList) {
            fd.setBankBranch((Bankbranch) persistenceService.find("from Bankbranch where id=?", fd.getBankBranch().getId()));
            fd.setBankAccount((Bankaccount) persistenceService.find("from Bankaccount where id=?", fd.getBankAccount().getId()));
            fd.setOutFlowVoucher((CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?", fd
                    .getOutFlowVoucher().getId()));
            if (fd.getInFlowVoucher().getId() != null)
                fd.setInFlowVoucher((CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?", fd
                        .getInFlowVoucher().getId()));
            else
                fd.setInFlowVoucher(null);

            if (fd.getChallanReceiptVoucher().getId() != null)
                fd.setChallanReceiptVoucher((CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?", fd
                        .getChallanReceiptVoucher().getId()));
            else
                fd.setChallanReceiptVoucher(null);
            if (fd.getInstrumentHeader().getId() != null)
                fd.setInstrumentHeader((InstrumentHeader) persistenceService.find("from InstrumentHeader where id=?", fd
                        .getInstrumentHeader().getId()));
            else
                fd.setInstrumentHeader(null);
            if (fd.getReceiptAmount() != null)
                fd.setReceiptAmount(BigDecimal.ZERO);

        }
        //persistenceService.setType(FixedDeposit.class);

        if (childFDList.size() > 0)
            for (final FixedDeposit fdd : fixedDepositList) {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("is EXTEND contains" + fdd.getExtend());
                for (final FixedDeposit chld : childFDList)
                    if (fdd.getReferenceNumber().equals(chld.getReferenceNumber())) {
                        chld.setParentId(fdd);
                        chld.setBankBranch((Bankbranch) persistenceService.find("from Bankbranch where id=?", chld
                                .getBankBranch().getId()));
                        chld.setBankAccount((Bankaccount) persistenceService.find("from Bankaccount where id=?", chld
                                .getBankAccount().getId()));
                        chld.setOutFlowVoucher((CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?", chld
                                .getOutFlowVoucher().getId()));
                        if (chld.getInstrumentHeader().getId() != null)
                            chld.setInstrumentHeader((InstrumentHeader) persistenceService.find(
                                    "from InstrumentHeader where id=?", chld.getInstrumentHeader().getId()));
                        else
                            chld.setInstrumentHeader(null);
                        if (chld.getInFlowVoucher().getId() != null)
                            chld.setInFlowVoucher((CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?", chld
                                    .getInFlowVoucher().getId()));
                        else
                            chld.setInFlowVoucher(null);

                        if (chld.getChallanReceiptVoucher().getId() != null)
                            chld.setChallanReceiptVoucher((CVoucherHeader) persistenceService.find(
                                    "from CVoucherHeader where id=?", chld.getChallanReceiptVoucher().getId()));
                        else
                            chld.setChallanReceiptVoucher(null);

                    }
                persistenceService.persist(fdd);
                for (final FixedDeposit cld : childFDList)
                    persistenceService.persist(cld);
            }
        else {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Child doesnot exist");
            for (final FixedDeposit fdd : fixedDepositList)
                persistenceService.persist(fdd);
        }
        if (EDIT.equalsIgnoreCase(mode))
            return EDIT;
        else
            return NEW;
    }

    @SuppressWarnings("unchecked")
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
        final String header = "";
        paramMap.put("ulbName", getUlbName());
        paramMap.put("heading", header);
        return paramMap;
    }

    public List<Bankaccount> getBankAccountListTemp() {
        return bankAccountListTemp;
    }

    public void setBankAccountListTemp(final List<Bankaccount> bankAccountListTemp) {
        this.bankAccountListTemp = bankAccountListTemp;
    }

    public String exportPdf() throws JRException, IOException {
        fixedDepositList = new ArrayList<FixedDeposit>();
        search();
        final List<Object> dataSource = new ArrayList<Object>();
        for (final FixedDeposit row : fixedDepositList) {

            row.setBankBranch((Bankbranch) persistenceService.find("from Bankbranch where id=?", row.getBankBranch().getId()));
            row.setBankAccount((Bankaccount) persistenceService.find("from Bankaccount where id=?", row.getBankAccount().getId()));

            dataSource.add(row);
        }
        setInputStream(reportHelper.exportPdf(getInputStream(), jasperpath,
                getParamMap(), dataSource));
        return "PDF";
    }

    public String exportXls() throws JRException, IOException {
        fixedDepositList = new ArrayList<FixedDeposit>();
        search();
        final List<Object> dataSource = new ArrayList<Object>();
        for (final FixedDeposit row : fixedDepositList) {
            // row.setBankAccountList(bankAccountListTemp);
            row.setBankBranch((Bankbranch) persistenceService.find("from Bankbranch where id=?", row.getBankBranch().getId()));
            row.setBankAccount((Bankaccount) persistenceService.find("from Bankaccount where id=?", row.getBankAccount().getId()));
            dataSource.add(row);
        }
        setInputStream(reportHelper.exportXls(getInputStream(), jasperpath,
                getParamMap(), dataSource));
        return "XLS";
    }

    public List<FixedDeposit> getFixedDepositList() {
        return fixedDepositList;
    }

    public void setFixedDepositList(final List<FixedDeposit> fixedDepositList) {
        this.fixedDepositList = fixedDepositList;
    }

    public List<Bankbranch> getBankBranchList() {
        return bankBranchList;
    }

    public void setBankBranchList(final List<Bankbranch> bankBranchList) {
        this.bankBranchList = bankBranchList;
    }

    public BigDecimal getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(final BigDecimal voucherAmount) {
        this.voucherAmount = voucherAmount;
    }

    public List<FixedDeposit> getChildFDList() {
        return childFDList;
    }

    public FixedDeposit getFixedDep() {
        return fixedDep;
    }

    public void setFixedDep(final FixedDeposit fixedDep) {
        this.fixedDep = fixedDep;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(final String serialNo) {
        this.serialNo = serialNo;
    }

    public void setChildFDList(final List<FixedDeposit> childFDList) {
        this.childFDList = childFDList;
    }

    public ReportHelper getReportHelper() {
        return reportHelper;
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public List<FixedDeposit> getFixedDepositListx() {
        return fixedDepositList;
    }

}