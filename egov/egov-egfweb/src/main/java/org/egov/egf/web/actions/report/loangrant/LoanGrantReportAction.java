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
package org.egov.egf.web.actions.report.loangrant;

import net.sf.jasperreports.engine.JRException;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.service.EntityTypeService;
import org.egov.egf.masters.model.FundingAgency;
import org.egov.egf.masters.model.LoanGrantBean;
import org.egov.egf.web.actions.masters.loangrant.LoanGrantBaseAction;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.services.report.LoanGrantService;
import org.egov.utils.ReportHelper;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Results({
    @Result(name = "searchGC", location = "loanGrantReport-searchGC.jsp"),
    @Result(name = "searchLoan", location = "loanGrantReport-searchLoan.jsp"),
    @Result(name = "PDF", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
            "application/pdf", "contentDisposition", "no-cache;filename=LoanGrant.pdf" }),
            @Result(name = "XLS", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                    "application/xls", "contentDisposition", "no-cache;filename=LoanGrant.xls" }),
                    @Result(name = "HTML", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                            "text/html", "contentDisposition", "no-cache;filename=LoanGrant.html" })
})
public class LoanGrantReportAction extends LoanGrantBaseAction {
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;

    private static final String GRANT_CONTRIBUTION = "GrantContribution";
    private static final String LOAN_OUT_STANDINNG = "LoanOutStandinng";
    private static final String SEARCH_LOAN = "searchLoan";
    private static final String PDF = "PDF";
    private static final String HTML = "HTML";
    private static final String XLS = "XLS";
    private static final long serialVersionUID = 6488591368651970475L;
    private static final String NEWGC = "searchGC";
    private static final String RESULTSGC = "resultsGC";
    private LoanGrantService lgService;
    private Long agencyId;
    List<Object> searchGCList;
    List<Object> repaymentList;
    private final String jasperpathForGrant = "/reports/templates/GrantContribution.jasper";
    private final String jasperpathForLoan = "/reports/templates/LoanOutStanding.jasper";
    // private String subreportJasperpath="/reports/templates/SchemeUtilization_fundingAgency.jasper";
    private InputStream inputStream;
    ReportHelper reportHelper;
    final static Logger LOGGER = Logger.getLogger(LoanGrantReportAction.class);

    // Search for Grant Contribution
    @Override
    @SuppressWarnings("unchecked")
    public void prepare()
    {
        super.prepare();
        final List<FundingAgency> agencyList = persistenceService.findAllBy("from FundingAgency where isActive=true order by name ");
        addDropdownData("agencyList", agencyList);
    }

    @Action(value = "/report/loangrant/loanGrantReport-newFormGC")
    public String newFormGC()
    {
        return NEWGC;
    }

    public String searchGC()
    {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting Grant Contribution Search");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Passed Params are FundId:" + fundId + " schemeId:" + schemeId + " subSchemeId" + subSchemeId
                    + " agencyId" + agencyId);
        final Accountdetailtype pcType = (Accountdetailtype) persistenceService
                .find("from Accountdetailtype  where name='PROJECTCODE'");
        final Accountdetailtype agencyType = (Accountdetailtype) persistenceService
                .find("from Accountdetailtype  where name='FundingAgency'");
        searchGCList = lgService.searchGC(schemeId, subSchemeId, fromDate, toDate, agencyId, pcType.getId(), agencyType.getId(),
                fundId);
        //
        if (searchGCList == null || searchGCList.size() == 0)
        {
            addActionMessage("No Records Found");
            throw new ValidationException(Arrays.asList(new ValidationError("No Records Found", "no.records.found")));
        }

        final String table = pcType.getFullQualifiedName();
        Class<?> service = null;
        try {
            service = Class.forName(table);
        } catch (final ClassNotFoundException e) {
            LOGGER.error("Cannot load class", e);
        }
        String simpleName = service.getSimpleName();
        simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1) + "Service";

        final WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext
                .getServletContext());
        final EntityTypeService entityService = (EntityTypeService) wac.getBean(simpleName);
        String allAssetCodes = "";
        int i = 0;
        BigDecimal grantTotal = BigDecimal.ZERO;
        BigDecimal agencyTotal = BigDecimal.ZERO;
        BigDecimal pcwiseTotal = BigDecimal.ZERO;
        for (final Object obj : searchGCList)
        {
            final LoanGrantBean lg = (LoanGrantBean) obj;
            if (lg.getDetailType() == pcType.getId())
            {
                allAssetCodes = "";
                List<String> assetCodeList = null;
                try {
                    assetCodeList = entityService.getAssetCodesForProjectCode(lg.getDetailKey());
                } catch (final ValidationException e) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("No Details Found Setting to Empty for asset codes");
                }
                if (assetCodeList != null && assetCodeList.size() > 0)
                {
                    i = 0;
                    for (final String assetCode : assetCodeList)
                    {
                        if (i == 0)
                            allAssetCodes = allAssetCodes + assetCode;
                        else
                            allAssetCodes = allAssetCodes + "," + assetCode;
                        i++;
                    }
                }
                lg.setStatus(allAssetCodes);
            }

            if (lg.getGrantAmount() != null)
                grantTotal = grantTotal.add(lg.getGrantAmount());
            if (lg.getAgencyAmount() != null)
                agencyTotal = agencyTotal.add(lg.getAgencyAmount());
            if (lg.getAmount() != null)
                pcwiseTotal = pcwiseTotal.add(lg.getAmount());
        }
        // the Below Lines is only to add total row
        final String totalStatus = "A-B=" + grantTotal.subtract(agencyTotal).setScale(2);
        final LoanGrantBean lgTotal = new LoanGrantBean(pcwiseTotal, agencyTotal, grantTotal, "Total", totalStatus);
        searchGCList.add(lgTotal);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("exiting from Grant Contribution Search");
        return RESULTSGC;
    }

    @Action(value = "/report/loangrant/loanGrantReport-newFormLoan")
    public String newFormLoan()
    {
        return SEARCH_LOAN;
    }

    public String searchLoan()
    {
        clearMessages();
        final Accountdetailtype agencyType = (Accountdetailtype) persistenceService
                .find("from Accountdetailtype  where name='FundingAgency'");
        repaymentList = lgService.getLoanBy(schemeId, agencyId, agencyType.getId(), fundId);
        if (repaymentList == null || repaymentList.size() == 0)
            throw new ValidationException(Arrays.asList(new ValidationError("No Records Found", "no.records.found")));
        BigDecimal soFarPaid = BigDecimal.ZERO;// get The amount
        BigDecimal balance = BigDecimal.ZERO;
        for (final Object ob : repaymentList)
        {
            final LoanGrantBean lg = (LoanGrantBean) ob;
            if (lg.getLoanAmount() != null)
            {
                balance = lg.getBalance();
                soFarPaid = lg.getAgencyAmount();
            } else
            {
                soFarPaid = soFarPaid.add(lg.getAmount());
                lg.setAgencyAmount(soFarPaid);
                balance = balance.subtract(lg.getAmount());
                lg.setBalance(balance);
            }
        }

        return SEARCH_LOAN;
    }

    public void setLgService(final LoanGrantService lgService) {
        this.lgService = lgService;
    }

    public Long getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(final Long agencyId) {
        this.agencyId = agencyId;
    }

    public void setSearchGCList(final List<Object> searchGCList) {
        this.searchGCList = searchGCList;
    }

    @ValidationErrorPage(SEARCH_LOAN)
    public String exportLoanPdf()
    {
        return generateLoanOutStaindingReport(PDF);

    }

    @ValidationErrorPage(SEARCH_LOAN)
    public String exportLoanXls()
    {
        return generateLoanOutStaindingReport(XLS);

    }

    @ValidationErrorPage(SEARCH_LOAN)
    public String exportLoanHTML()
    {
        return generateLoanOutStaindingReport(HTML);

    }

    public String generateLoanOutStaindingReport(String type)
    {
        try {
            searchLoan();
            if (type.equalsIgnoreCase(PDF))
                inputStream = reportHelper.exportPdf(inputStream, jasperpathForLoan, getParamMap(LOAN_OUT_STANDINNG),
                        repaymentList);
            else if (type.equalsIgnoreCase(XLS))
                inputStream = reportHelper.exportXls(inputStream, jasperpathForLoan, getParamMap(LOAN_OUT_STANDINNG),
                        repaymentList);
            else
                inputStream = reportHelper.exportHtml(inputStream, jasperpathForLoan, getParamMap(LOAN_OUT_STANDINNG),
                        repaymentList, "pt");

        } catch (final JRException e) {
            LOGGER.error(e);

        } catch (final IOException e) {
            LOGGER.error(e);
        } catch (final ValidationException e) {
            LOGGER.error(e);
            addActionMessage("No Records Found");

            type = SEARCH_LOAN;

        }
        return type;
    }

    public String exportPdf()
    {
        return generateGrantContributionReport(PDF);

    }

    public String exportXls()
    {
        return generateGrantContributionReport(XLS);

    }

    public String exportHTML()
    {
        return generateGrantContributionReport(HTML);

    }

    public String generateGrantContributionReport(final String type)
    {
        try {
            searchGC();
            if (type.equalsIgnoreCase("pdf"))
                inputStream = reportHelper.exportPdf(inputStream, jasperpathForGrant, getParamMap(GRANT_CONTRIBUTION),
                        searchGCList);
            else if (type.equalsIgnoreCase(XLS))
                inputStream = reportHelper.exportXls(inputStream, jasperpathForGrant, getParamMap(GRANT_CONTRIBUTION),
                        searchGCList);
            else
                inputStream = reportHelper.exportHtml(inputStream, jasperpathForGrant, getParamMap(GRANT_CONTRIBUTION),
                        searchGCList, "pt");

        } catch (final JRException e) {
            LOGGER.error(e);

        } catch (final IOException e) {
            LOGGER.error(e);
        } catch (final ValidationException e)
        {
            return NEWGC;
        }
        return type;
    }

    private Map<String, Object> getParamMap(final String name) {

        final Map<String, Object> paramMap = new HashMap<String, Object>();
        String reportByStr = "";
        if (name.equalsIgnoreCase(GRANT_CONTRIBUTION))
            reportByStr = "Grant Contribution Report for ";
        else
            reportByStr = "Loan OutStainding Report for ";
        if (schemeId != null)
        {
            final String schemeName = (String) persistenceService.find("select name from Scheme where id=?", getSchemeId());
            paramMap.put("reportBy", reportByStr + schemeName);
            paramMap.put("schemeName", schemeName);
        }
        if (getSubSchemeId() != null)
        {
            final String subSchemeName = (String) persistenceService.find("select name from SubScheme where id=?",
                    getSubSchemeId());
            paramMap.put("reportBy", reportByStr + subSchemeName);
            paramMap.put("subSchemeName", subSchemeName);
        }
        if (fundId != null)
        {
            final String fundName = (String) persistenceService.find("select name from Fund where id=?", fundId);
            paramMap.put("fundName", fundName);
        }
        paramMap.put("fromDate", fromDate);
        paramMap.put("toDate", toDate);
        paramMap.put("ulbName", getUlbName());

        return paramMap;
    }

    private String getUlbName() {
        final SQLQuery query = persistenceService.getSession().createSQLQuery("select name from companydetail");
        final List<String> result = query.list();
        if (result != null)
            return result.get(0);
        return "";
    }

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

}