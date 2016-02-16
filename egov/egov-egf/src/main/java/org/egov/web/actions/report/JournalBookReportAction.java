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
package org.egov.web.actions.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Fundsource;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.hibernate.FlushMode;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.GLEngine.GeneralLedgerBean;
import com.exilant.eGov.src.transactions.JbReport;
import com.exilant.exility.common.TaskFailedException;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@Transactional(readOnly = true)
@ParentPackage("egov")
@Results({
    @Result(name = "result", location = "journalBookReport-result.jsp"),
    @Result(name = FinancialConstants.STRUTS_RESULT_PAGE_SEARCH, location = "journalBookReport-"
            + FinancialConstants.STRUTS_RESULT_PAGE_SEARCH + ".jsp")
})
public class JournalBookReportAction extends BaseFormAction {

    /**
     *
     */
    private static final long serialVersionUID = -7540296344209825345L;
    private static final Logger LOGGER = Logger.getLogger(JournalBookReportAction.class);
    private GeneralLedgerBean journalBookReport = new GeneralLedgerBean();
    private JbReport journalBook = new JbReport();
    protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    protected LinkedList journalBookDisplayList = new LinkedList();
    String heading = "";

    public JournalBookReportAction() {
        super();
    }

    @Override
    public Object getModel() {
        return journalBookReport;
    }

    public void prepareNewForm() {
        super.prepare();
        addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=true and isnotleaf=false order by name"));
        addDropdownData("departmentList", persistenceService.findAllBy("from Department order by deptName"));
        addDropdownData("fundsourceList",
                persistenceService.findAllBy(" from Fundsource where isactive=true and isnotleaf=false order by name"));
        addDropdownData("voucherNameList", VoucherHelper.VOUCHER_TYPE_NAMES.get(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside  Prepare ........");

    }

    @SkipValidation
    @Action(value = "/report/journalBookReport-newForm")
    public String newForm() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("..Inside NewForm method..");
        return FinancialConstants.STRUTS_RESULT_PAGE_SEARCH;
    }

    @Validations(requiredFields = {
            @RequiredFieldValidator(fieldName = "fund_id", message = "", key = FinancialConstants.REQUIRED),
            @RequiredFieldValidator(fieldName = "startDate", message = "", key = FinancialConstants.REQUIRED),
            @RequiredFieldValidator(fieldName = "endDate", message = "", key = FinancialConstants.REQUIRED) })
    @ValidationErrorPage(value = FinancialConstants.STRUTS_RESULT_PAGE_SEARCH)
    @SkipValidation
    @Action(value = "/report/journalBookReport-ajaxSearch")
    public String ajaxSearch() throws TaskFailedException {

        HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
        HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("JournalBookAction | Search | start");
        try {
            journalBookDisplayList = journalBook.getJbReport(journalBookReport);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("JournalBookAction | list | End");
        heading = getGLHeading();
        prepareNewForm();
        return "result";
    }

    private String getGLHeading() {

        String heading = "";
        heading = "Journal Book Report under " + journalBookReport.getFundName() + " from " + journalBookReport.getStartDate()
                + " to " + journalBookReport.getEndDate();
        Department dept = new Department();
        Fundsource fundsource = new Fundsource();
        if (checkNullandEmpty(journalBookReport.getDept_name())) {
            dept = (Department) persistenceService.find("from Department where  id = ?",
                    Integer.parseInt(journalBookReport.getDept_name()));
            heading = heading + " and Department : " + dept.getName();
        }
        if (checkNullandEmpty(journalBookReport.getFundSource_id())) {
            fundsource = (Fundsource) persistenceService.find("from Fundsource where  id = ?",
                    Integer.parseInt(journalBookReport.getFundSource_id()));
            heading = heading + " and Financing Source :" + fundsource.getName();
        }
        if (checkNullandEmpty(journalBookReport.getVoucher_name()))
            heading = heading + " and Voucher Type Name :" + journalBookReport.getVoucher_name();
        return heading;
    }

    private boolean checkNullandEmpty(final String column)
    {
        if (column != null && !column.isEmpty())
            return true;
        else
            return false;

    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(final String heading) {
        this.heading = heading;
    }

    public GeneralLedgerBean getJournalBookReport() {
        return journalBookReport;
    }

    public void setJournalBookReport(final GeneralLedgerBean journalBookReport) {
        this.journalBookReport = journalBookReport;
    }

    public JbReport getJournalBook() {
        return journalBook;
    }

    public void setJournalBook(final JbReport journalBook) {
        this.journalBook = journalBook;
    }

    public LinkedList getJournalBookDisplayList() {
        return journalBookDisplayList;
    }

    public void setJournalBookDisplayList(final LinkedList journalBookDisplayList) {
        this.journalBookDisplayList = journalBookDisplayList;
    }

}
