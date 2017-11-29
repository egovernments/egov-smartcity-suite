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
package org.egov.ptis.actions.reports;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.Installment;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.ptis.bean.ReportInfo;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Calendar.YEAR;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_HEADWISEDMDCOLL;

@ParentPackage("egov")
public class HeadWiseDmdNdRecReportAction extends BaseFormAction {
    private final Logger LOGGER = Logger.getLogger(getClass());
    private final String RESULT_GENERATE = "generate";
    private ReportService reportService;
    private String reportId;
    private BigDecimal arrDmdTotal = BigDecimal.ZERO;
    private BigDecimal currDmdTotal = BigDecimal.ZERO;
    private BigDecimal arrCollTotal = BigDecimal.ZERO;
    private BigDecimal currCollTotal = BigDecimal.ZERO;
    private BigDecimal arrDmdGrandTotal = BigDecimal.ZERO;
    private BigDecimal currDmdGrandTotal = BigDecimal.ZERO;
    private BigDecimal arrCollGrandTotal = BigDecimal.ZERO;
    private BigDecimal currCollGrandTotal = BigDecimal.ZERO;
    private Map<String, BigDecimal> dmdCollMap = new HashMap<String, BigDecimal>();

    private List arrInstDmdCollList = new ArrayList();
    private List currInstDmdCollList = new ArrayList();
    private Installment currentInstallment;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Autowired
    private ReportViewerUtil reportViewerUtil;

    public String generateHeadWiseDmdColl() {
        ReportRequest reportRequest = null;
        ReportInfo reportInfo = new ReportInfo();

        currentInstallment = propertyTaxCommonUtils.getCurrentInstallment();

        prepareReasonWiseCurrDmdColl();
        prepareReasonWiseArrDmdColl();

        Calendar installmentFromDate = Calendar.getInstance();
        installmentFromDate.setTime(currentInstallment.getFromDate());

        Calendar installmentToDate = Calendar.getInstance();
        installmentToDate.setTime(currentInstallment.getToDate());

        reportInfo.setCurrInstallment("Year " + installmentFromDate.get(YEAR) + "-" + installmentToDate.get(YEAR));
        reportInfo.setDemandCollMap(dmdCollMap);

        reportRequest = new ReportRequest(REPORT_TEMPLATENAME_HEADWISEDMDCOLL, reportInfo,
                new HashMap<String, Object>());
        reportRequest.setPrintDialogOnOpenReport(true);
        reportId = reportViewerUtil.addReportToTempCache(reportService.createReport(reportRequest));

        return RESULT_GENERATE;
    }

    private void prepareReasonWiseCurrDmdColl() {
        LOGGER.debug("Enter prepareReasonWiseCurrDmdColl");

        StringBuilder currDmdCollBuilder = new StringBuilder(1500);

        currDmdCollBuilder.append("select")
                .append(" dcb.installment.id, sum(dcb.generalTax), sum(dcb.generalTaxColl),")
                .append(" sum(dcb.egsTax), sum(dcb.egsTaxColl),")
                .append(" sum(dcb.eduCessResdTax) + sum(dcb.eduCessNonResdTax),")
                .append(" sum(dcb.eduCessResdTaxColl) + sum(dcb.eduCessNonResdTaxColl),")
                .append(" sum(dcb.waterTax), sum(dcb.waterTaxColl),")
                .append(" sum(dcb.fireTax), sum(dcb.fireTaxColl),")
                .append(" sum(dcb.sewerageTax), sum(dcb.sewerageTaxColl),")
                .append(" sum(dcb.lightTax), sum(dcb.lightTaxColl),")
                .append(" sum(dcb.bigBldgTax), sum(dcb.bigBldgTaxColl),")
                .append(" sum(dcb.chqBouncePenalty), sum(dcb.chqBuncPnltyColl),")
                .append(" sum(dcb.penaltyFine), sum(dcb.penaltyFineColl)")
                .append(" from InstDmdCollMaterializeView dcb, Installment inst, Module module").append(" where")
                .append(" inst.module.id = module.id").append(" and module.moduleName = 'Property Tax'")
                .append(" and inst.fromDate <= sysdate").append(" and inst.toDate >= sysdate")
                .append(" and dcb.installment.id = inst.id").append(" group by dcb.installment.id");

        Query qry = getPersistenceService().getSession().createQuery(currDmdCollBuilder.toString());
        currInstDmdCollList = qry.list();
        Object[] currInst = (Object[]) currInstDmdCollList.get(0);

        dmdCollMap.put("currSewerageTax", ((BigDecimal) currInst[11]));
        dmdCollMap.put("currWaterTax", ((BigDecimal) currInst[7]));
        dmdCollMap.put("currGenTax", ((BigDecimal) currInst[1]));
        dmdCollMap.put("currFireTax", ((BigDecimal) currInst[9]));
        dmdCollMap.put("currLightTax", ((BigDecimal) currInst[13]));
        dmdCollMap.put("currEduTax", ((BigDecimal) currInst[5]));
        dmdCollMap.put("currEgsTax", ((BigDecimal) currInst[3]));
        dmdCollMap.put("currBigBldgTax", ((BigDecimal) currInst[15]));
        dmdCollMap.put("currSewerageTaxColl", ((BigDecimal) currInst[12]));
        dmdCollMap.put("currWaterTaxColl", ((BigDecimal) currInst[8]));
        dmdCollMap.put("currGenTaxColl", ((BigDecimal) currInst[2]));
        dmdCollMap.put("currFireTaxColl", ((BigDecimal) currInst[10]));
        dmdCollMap.put("currLightTaxColl", ((BigDecimal) currInst[14]));
        dmdCollMap.put("currEduTaxColl", ((BigDecimal) currInst[6]));
        dmdCollMap.put("currEgsTaxColl", ((BigDecimal) currInst[4]));
        dmdCollMap.put("currBigBldgTaxColl", ((BigDecimal) currInst[16]));

        dmdCollMap.put("currChqBuncPenalty", ((BigDecimal) currInst[17]));
        dmdCollMap.put("currPenaltyFine", ((BigDecimal) currInst[19]));
        dmdCollMap.put("currChqBncPntyColl", ((BigDecimal) currInst[18]));
        dmdCollMap.put("currPnltyFineColl", ((BigDecimal) currInst[20]));

        currDmdTotal = currDmdTotal.add((BigDecimal) currInst[11]).add((BigDecimal) currInst[7])
                .add((BigDecimal) currInst[1]).add((BigDecimal) currInst[9]).add((BigDecimal) currInst[13])
                .add((BigDecimal) currInst[5]).add((BigDecimal) currInst[3]).add((BigDecimal) currInst[15])
                .add((BigDecimal) currInst[17]).add((BigDecimal) currInst[19]);
        currCollTotal = currCollTotal.add((BigDecimal) currInst[12]).add((BigDecimal) currInst[8])
                .add((BigDecimal) currInst[2]).add((BigDecimal) currInst[10]).add((BigDecimal) currInst[14])
                .add((BigDecimal) currInst[6]).add((BigDecimal) currInst[4]).add((BigDecimal) currInst[16])
                .add((BigDecimal) currInst[18]).add((BigDecimal) currInst[20]);
        currDmdGrandTotal = currDmdGrandTotal.add(currDmdTotal);
        currCollGrandTotal = currCollGrandTotal.add(currCollTotal);

        dmdCollMap.put("currDmdTotal", currDmdTotal);
        dmdCollMap.put("currCollTotal", currCollTotal);
        dmdCollMap.put("currDmdGrandTotal", currDmdGrandTotal);
        dmdCollMap.put("currCollGrandTotal", currCollGrandTotal);

        LOGGER.debug("Exit prepareReasonWiseCurrDmdColl");
    }

    private void prepareReasonWiseArrDmdColl() {
        LOGGER.debug("Enter prepareReasonWiseArrDmdColl");

        StringBuilder arrDmdCollBuilder = new StringBuilder(1500);

        arrDmdCollBuilder.append("select").append(" sum(dcb.generalTax), sum(dcb.generalTaxColl),")
                .append(" sum(dcb.egsTax), sum(dcb.egsTaxColl), ")
                .append(" sum(dcb.eduCessResdTax) + sum(dcb.eduCessNonResdTax),")
                .append(" sum(dcb.eduCessResdTaxColl) + sum(dcb.eduCessNonResdTaxColl),")
                .append(" sum(dcb.waterTax), sum(dcb.waterTaxColl), sum(dcb.fireTax),")
                .append(" sum(dcb.fireTaxColl), sum(dcb.sewerageTax), sum(dcb.sewerageTaxColl),")
                .append(" sum(dcb.lightTax), sum(dcb.lightTaxColl), sum(dcb.bigBldgTax),")
                .append(" sum(dcb.bigBldgTaxColl), sum(dcb.chqBouncePenalty),")
                .append(" sum(dcb.chqBuncPnltyColl), sum(dcb.penaltyFine),").append(" sum(dcb.penaltyFineColl)")
                .append(" from InstDmdCollMaterializeView dcb, Installment inst, Module module").append(" where")
                .append(" inst.module.id = module.id").append(" and module.moduleName = 'Property Tax'")
                .append(" and inst.toDate < :currInstFromDate").append(" and dcb.installment.id = inst.id");

        Query qry = getPersistenceService().getSession().createQuery(arrDmdCollBuilder.toString());
        qry.setDate("currInstFromDate", currentInstallment.getFromDate());
        arrInstDmdCollList = qry.list();

        Object[] arrearTaxes = (Object[]) arrInstDmdCollList.get(0);

        dmdCollMap.put("arrSewerageTax", (BigDecimal) arrearTaxes[10]);
        dmdCollMap.put("arrWaterTax", (BigDecimal) arrearTaxes[6]);
        dmdCollMap.put("arrGenTax", (BigDecimal) arrearTaxes[0]);
        dmdCollMap.put("arrFireTax", (BigDecimal) arrearTaxes[8]);
        dmdCollMap.put("arrLightTax", (BigDecimal) arrearTaxes[12]);
        dmdCollMap.put("arrEduTax", (BigDecimal) arrearTaxes[4]);
        dmdCollMap.put("arrEgsTax", (BigDecimal) arrearTaxes[2]);
        dmdCollMap.put("arrBigBldgTax", (BigDecimal) arrearTaxes[14]);
        dmdCollMap.put("arrSewerageTaxColl", (BigDecimal) arrearTaxes[11]);
        dmdCollMap.put("arrWaterTaxColl", (BigDecimal) arrearTaxes[7]);
        dmdCollMap.put("arrGenTaxColl", (BigDecimal) arrearTaxes[1]);
        dmdCollMap.put("arrFireTaxColl", (BigDecimal) arrearTaxes[9]);
        dmdCollMap.put("arrLightTaxColl", (BigDecimal) arrearTaxes[13]);
        dmdCollMap.put("arrEduTaxColl", (BigDecimal) arrearTaxes[5]);
        dmdCollMap.put("arrEgsTaxColl", (BigDecimal) arrearTaxes[3]);
        dmdCollMap.put("arrBigBldgTaxColl", (BigDecimal) arrearTaxes[15]);

        dmdCollMap.put("arrChqBuncPenalty", (BigDecimal) arrearTaxes[16]);
        dmdCollMap.put("arrPenaltyFine", (BigDecimal) arrearTaxes[18]);
        dmdCollMap.put("arrChqBuncPnltyColl", (BigDecimal) arrearTaxes[17]);
        dmdCollMap.put("arrPenaltyFineColl", (BigDecimal) arrearTaxes[19]);

        for (Map.Entry<String, BigDecimal> mapEntry : dmdCollMap.entrySet()) {
            if (mapEntry.getKey().startsWith("arr")) {
                if (mapEntry.getKey().endsWith("Tax")) {
                    arrDmdTotal = arrDmdTotal.add(mapEntry.getValue());
                } else if (mapEntry.getKey().endsWith("Coll")) {
                    arrCollTotal = arrCollTotal.add(mapEntry.getValue());
                }
            }
        }
        arrDmdTotal = arrDmdTotal.add(dmdCollMap.get("arrChqBuncPenalty")).add(dmdCollMap.get("arrPenaltyFine"));

        arrDmdGrandTotal = arrDmdGrandTotal.add(arrDmdTotal);
        arrCollGrandTotal = arrCollGrandTotal.add(arrCollTotal);

        dmdCollMap.put("arrDmdTotal", arrDmdTotal);
        dmdCollMap.put("arrCollTotal", arrCollTotal);
        dmdCollMap.put("arrDmdGrandTotal", arrDmdGrandTotal);
        dmdCollMap.put("arrCollGrandTotal", arrCollGrandTotal);

        LOGGER.debug("Exit from prepareReasonWiseArrDmdColl");
    }

    public ReportService getReportService() {
        return reportService;
    }

    public void setReportService(ReportService reportService) {
        this.reportService = reportService;
    }

    public String getReportId() {
        return reportId;
    }

    @Override
    public Object getModel() {
        return null;
    }

}
