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
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.utils.EntityType;
import org.egov.egf.masters.model.LoanGrantBean;
import org.egov.egf.web.actions.masters.loangrant.LoanGrantBaseAction;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.util.ReportUtil;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.services.report.LoanGrantService;
import org.egov.utils.ReportHelper;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



@Results({
    @Result(name = "result", location = "schemeUtilizationReport-result.jsp"),
    @Result(name = SchemeUtilizationReportAction.NEW, location = "schemeUtilizationReport-"
            + SchemeUtilizationReportAction.NEW + ".jsp"),
            @Result(name = "PDF", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                    "application/pdf", "contentDisposition", "no-cache;filename=SchemeUtilization.pdf" }),
                    @Result(name = "XLS", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                            "application/xls", "contentDisposition", "no-cache;filename=SchemeUtilization.xls" }),
                            @Result(name = "HTML", type = "stream", location = "inputStream", params = { "inputName", "inputStream", "contentType",
                                    "text/html", "contentDisposition", "no-cache;filename=SchemeUtilization.html" })
})
public class SchemeUtilizationReportAction extends LoanGrantBaseAction {
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;

    private static final long serialVersionUID = 5416901822456802437L;
    final static Logger LOGGER = Logger.getLogger(SchemeUtilizationReportAction.class);
    private LoanGrantService lgService;
    private List<Integer> projectCodeIdList;
    private List<Object> projectCodeResultList;
    private List<LoanGrantBean> fundingPatternBysubScheme;
    private final String jasperpath = "/reports/templates/SchemeUtilization.jasper";
    private InputStream inputStream;
    private ReportService reportService;
    ReportHelper reportHelper;
    private int maxRows;
    private Map<String, BigDecimal> faTotalMap;
    private List<Object> fundingAgencyResultList;

    @Override
    public void prepare()
    {
        super.prepare();
    }

    @Override
    public Object getModel() {
        return super.getModel();
    }

    @Action(value = "/report/loangrant/schemeUtilizationReport-newForm")
    public String newForm()
    {
        return NEW;
    }

    @Action(value = "/report/loangrant/schemeUtilizationReport-search")
    public String search()
    {
        if (projectCodeIdList != null)
            projectCodeIdList = removeNulls(projectCodeIdList);
        projectCodeResultList = lgService.schemeUtilizationBy(schemeId, subSchemeId, fromDate, toDate, projectCodeIdList, fundId);
        if (projectCodeResultList.size() == 0)
        {
            addActionMessage("No Records Found");
            throw new ValidationException(Arrays.asList(new ValidationError("No Records Found", "no.records.found")));
        }
        fundingPatternBysubScheme = lgService.fundingPatternBy(subSchemeId, schemeId);
        maxRows = projectCodeResultList.size() > fundingPatternBysubScheme.size() ? projectCodeResultList.size()
                : fundingPatternBysubScheme.size();
        projectCodeResultList.size();
        fundingPatternBysubScheme.size();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Passed Params are FundID:" + fundId + " schemeId:" + schemeId + " subSchemeId" + subSchemeId);
        final Accountdetailtype pcType = (Accountdetailtype) persistenceService
                .find("from Accountdetailtype  where name='PROJECTCODE'");
        final String table = pcType.getFullQualifiedName();
        try {
            Class.forName(table);
        } catch (final ClassNotFoundException e) {
            LOGGER.error("Cannot load class", e);
        }

        // used to identify subschemes
        String temp = "";
        final String pcQryStr = "from " + table + " where  id=?";
        BigDecimal grandTotal = BigDecimal.ZERO;
        // sub scheme wise total
        final Map<String, BigDecimal> ssTotalMap = new LinkedHashMap<String, BigDecimal>();
        for (final Object lp : projectCodeResultList)
        {
            final LoanGrantBean lgForPc = (LoanGrantBean) lp;
            grandTotal = grandTotal.add(lgForPc.getAmount());
            if (!temp.equalsIgnoreCase(lgForPc.getSubScheme()))
                ssTotalMap.put(lgForPc.getSubScheme(), lgForPc.getAmount());
            else
                ssTotalMap.put(lgForPc.getSubScheme(), ssTotalMap.get(temp).add(lgForPc.getAmount()));
            temp = lgForPc.getSubScheme();

            final EntityType entity = (EntityType) persistenceService.find(pcQryStr, lgForPc.getId());
            if (entity.getEgwStatus() != null)
                lgForPc.setStatus(entity.getEgwStatus().getDescription());
        }
        temp = "";
        int i = 0;
        final List<Object> projectCodeResultList2 = new ArrayList<Object>();
        projectCodeResultList2.addAll(projectCodeResultList);
        for (final Object lpg : projectCodeResultList2)
        {
            final LoanGrantBean lgForPc = (LoanGrantBean) lpg;
            if (!temp.equalsIgnoreCase(lgForPc.getSubScheme()))
                if (i == 0) {
                    temp = lgForPc.getSubScheme();
                    i++;
                    continue;
                }

                else
                {
                    final LoanGrantBean lg = new LoanGrantBean();
                    lg.setCode("Total");
                    lg.setAmount(ssTotalMap.get(temp));
                    projectCodeResultList.add(i, lg);
                    i++;
                }
            i++;
            temp = lgForPc.getSubScheme();
        }
        // get it for Last record
        LoanGrantBean lg = new LoanGrantBean();
        lg.setCode("Total");
        lg.setAmount(ssTotalMap.get(temp));
        projectCodeResultList.add(lg);
        lg = new LoanGrantBean();
        lg.setCode("GrandTotal");
        lg.setAmount(grandTotal);
        projectCodeResultList.add(lg);

        temp = "";
        faTotalMap = new LinkedHashMap<String, BigDecimal>();
        BigDecimal faGrandTotal = BigDecimal.ZERO;
        for (final LoanGrantBean lgForFa : fundingPatternBysubScheme)
        {
            if (null == faTotalMap.get(lgForFa.getName()))
            {
                if (ssTotalMap.get(lgForFa.getSubScheme()) != null)
                {
                    final BigDecimal faTotal = ssTotalMap.get(lgForFa.getSubScheme()).multiply(lgForFa.getAmount())
                            .divide(BigDecimal.valueOf(100));
                    faGrandTotal = faGrandTotal.add(faTotal);
                    faTotalMap.put(lgForFa.getName(), faTotal);
                }
            } else if (ssTotalMap.get(lgForFa.getSubScheme()) != null)
            {
                final BigDecimal faTotal = ssTotalMap.get(lgForFa.getSubScheme()).multiply(lgForFa.getAmount())
                        .divide(BigDecimal.valueOf(100));
                faGrandTotal = faGrandTotal.add(faTotal);
                faTotalMap.put(lgForFa.getName(), faTotalMap.get(lgForFa.getName()).add(faTotal));
            }
            lgForFa.getName();
            temp = lgForFa.getSubScheme();
        }
        faTotalMap.put("GrandTotal", faGrandTotal);
        fundingAgencyResultList = new ArrayList<Object>();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("SSTOTAL" + ssTotalMap);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("FATOTAL" + faTotalMap);
        for (final String key : faTotalMap.keySet())
        {
            final LoanGrantBean fa = new LoanGrantBean();
            fa.setAgencyName(key);
            fa.setPercentAmount(faTotalMap.get(key));
            fundingAgencyResultList.add(fa);
        }
        if (LOGGER.isInfoEnabled())
            LOGGER.info("fundingAgencyResultList" + fundingAgencyResultList.size());

        return "result";
    }

    private List<Integer> removeNulls(final List<Integer> projectCodeIdList2) {
        if (projectCodeIdList != null)
            while (projectCodeIdList.contains(null))
                projectCodeIdList.remove(null);
        return projectCodeIdList;
    }

    public String exportPdf()
    {
        try {
            search();
            inputStream = reportHelper.exportPdf(inputStream, jasperpath, getParamMap(), projectCodeResultList);
        } catch (final JRException e) {
            LOGGER.error(e);
        } catch (final IOException e) {
            LOGGER.error(e);
        } catch (final ValidationException e)
        {
            return NEW;
        }
        return "PDF";
    }

    public String exportXls()
    {

        try {
            search();
            inputStream = reportHelper.exportXls(inputStream, jasperpath, getParamMap(), projectCodeResultList);
        } catch (final JRException e) {
            LOGGER.error(e);
        } catch (final IOException e) {
            LOGGER.error(e);
        } catch (final ValidationException e)
        {
            return NEW;
        }

        return "XLS";
    }

    public String exportHTML()
    {

        try {
            search();
            inputStream = reportHelper.exportHtml(inputStream, jasperpath, getParamMap(), projectCodeResultList, "pt");
        } catch (final ValidationException e)
        {
            return NEW;
        } catch (final Exception e) {
            LOGGER.error(e);
        }

        return "HTML";
    }

    private Map<String, Object> getParamMap() {

        final Map<String, Object> paramMap = new HashMap<String, Object>();

        if (schemeId != null)
        {
            final String schemeName = (String) persistenceService.find("select name from Scheme where id=?", getSchemeId());
            paramMap.put("reportBy", "Scheme Utilization Report for " + schemeName);
            paramMap.put("schemeName", schemeName);
        }
        if (getSubSchemeId() != null)
        {
            final String subSchemeName = (String) persistenceService.find("select name from SubScheme where id=?",
                    getSubSchemeId());
            paramMap.put("reportBy", "Scheme Utilization Report for " + subSchemeName);
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
        paramMap.put("fundingAgencyResultList", fundingAgencyResultList);
        paramMap.put("SchemeUtilization_fundingAgency", ReportUtil.getTemplateAsStream("SchemeUtilization_fundingAgency.jasper"));

        return paramMap;
    }

    private String getUlbName() {
        final SQLQuery query = persistenceService.getSession().createSQLQuery("select name from companydetail");
        final List<String> result = query.list();
        if (result != null)
            return result.get(0);
        return "";
    }

    public void setLgService(final LoanGrantService lgService) {
        this.lgService = lgService;
    }

    public List<Integer> getProjectCodeIdList() {
        return projectCodeIdList;
    }

    public void setProjectCodeIdList(final List<Integer> projectCodeIdList) {
        this.projectCodeIdList = projectCodeIdList;
    }

    public List<Object> getProjectCodeResultList() {
        return projectCodeResultList;
    }

    public void setProjectCodeResultList(final List<Object> projectCodeResultList) {
        this.projectCodeResultList = projectCodeResultList;
    }

    public List<LoanGrantBean> getFundingPatternBysubScheme() {
        return fundingPatternBysubScheme;
    }

    public void setFundingPatternBysubScheme(final List<LoanGrantBean> fundingPatternBysubScheme) {
        this.fundingPatternBysubScheme = fundingPatternBysubScheme;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(final int maxRows) {
        this.maxRows = maxRows;
    }

    public Map<String, BigDecimal> getFaTotalMap() {
        return faTotalMap;
    }

    public void setFaTotalMap(final Map<String, BigDecimal> faTotalMap) {
        this.faTotalMap = faTotalMap;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    public List<Object> getFundingAgencyResultList() {
        return fundingAgencyResultList;
    }

    public void setFundingAgencyResultList(final List<Object> fundingAgencyResultList) {
        this.fundingAgencyResultList = fundingAgencyResultList;
    }

}