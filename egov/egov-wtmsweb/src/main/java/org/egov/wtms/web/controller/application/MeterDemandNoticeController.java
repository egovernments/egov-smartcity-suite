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
package org.egov.wtms.web.controller.application;

import org.apache.commons.lang.WordUtils;
import org.egov.commons.Installment;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.MeterReadingConnectionDetails;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/application")
public class MeterDemandNoticeController {

    public static final String METERDEMAND_NOTICE = "meterDemandNotice";

    @Autowired
    private ReportService reportService;

    @Autowired
    private DemandGenericDao demandGenericDao;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private WaterConnectionDetailsRepository waterConnectionDetailsRepository;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @RequestMapping(value = "/meterdemandnotice", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> generateEstimationNotice(final HttpServletRequest request,
            final HttpSession session) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(request.getParameter("pathVar"), ConnectionStatus.ACTIVE);
        return generateReport(waterConnectionDetails, session);
    }

    private ResponseEntity<byte[]> generateReport(final WaterConnectionDetails waterConnectionDetails,
            final HttpSession session) {
        ReportRequest reportInput = null;
        ReportOutput reportOutput;
        if (waterConnectionDetails != null) {
            Map<String, Object> reportParams;
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                    waterConnectionDetails.getConnection().getPropertyIdentifier(),
                    PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ACTIVE);

            final StringBuilder ownerName = new StringBuilder();

            int counter = 0;
            for (final OwnerName names : assessmentDetails.getOwnerNames()) {
                if (counter > 0)
                    ownerName.append(", ");
                ownerName.append(names.getOwnerName());
                counter++;
            }

            EgBill billObj = null;
            final List<EgBill> billlist = demandGenericDao.getAllBillsForDemand(
                    waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand(),
                    "N", "N");
            if (!billlist.isEmpty())
                billObj = billlist.get(0);
            final MeterReadingConnectionDetails meterReadingpriviousObj = new MeterReadingConnectionDetails();
            final List<MeterReadingConnectionDetails> meterReadingpriviousObjlist = waterConnectionDetailsRepository
                    .findPreviousMeterReadingReading(waterConnectionDetails.getId());
            if (meterReadingpriviousObjlist.size() > 1) {
                meterReadingpriviousObj.setCurrentReading(meterReadingpriviousObjlist.get(1).getCurrentReading());
                if (meterReadingpriviousObjlist.get(1).getCurrentReadingDate() != null)
                    meterReadingpriviousObj.setCurrentReadingDate(meterReadingpriviousObjlist.get(1).getCurrentReadingDate());
                else
                    meterReadingpriviousObj.setCurrentReadingDate(waterConnectionDetails.getExecutionDate());
            } else {
                if (waterConnectionDetails.getConnection().getInitialReading() != null)
                    meterReadingpriviousObj.setCurrentReading(waterConnectionDetails.getConnection()
                            .getInitialReading());
                else
                    meterReadingpriviousObj.setCurrentReading(0l);
                meterReadingpriviousObj.setCurrentReadingDate(waterConnectionDetails.getExecutionDate());
            }
            final Format formattermonth = new SimpleDateFormat("MMMM");
            final Format formatterYear = new SimpleDateFormat("YYYY");
            final String monthName = formattermonth.format(waterConnectionDetails.getMeterConnection().get(0)
                    .getCurrentReadingDate());
            final String yearName = formatterYear.format(waterConnectionDetails.getMeterConnection().get(0)
                    .getCurrentReadingDate());
            reportParams = prepareReportParams(waterConnectionDetails, session, formatter, assessmentDetails,
                    ownerName.toString(), billObj,
                    meterReadingpriviousObj, monthName, yearName);
            reportInput = new ReportRequest(METERDEMAND_NOTICE, waterConnectionDetails,
                    reportParams);
            reportInput.setPrintDialogOnOpenReport(true);
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=DemandNotice.pdf");
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

    /**
     * @param waterConnectionDetails
     * @param session
     * @param formatter
     * @param assessmentDetails
     * @param ownerName
     * @param billObj
     * @param meterReadingpriviousObj
     * @param monthName
     * @param yearName
     */
    private Map<String, Object> prepareReportParams(final WaterConnectionDetails waterConnectionDetails,
            final HttpSession session,
            final SimpleDateFormat formatter, final AssessmentDetails assessmentDetails, final String ownerName,
            final EgBill billObj, final MeterReadingConnectionDetails meterReadingpriviousObj, final String monthName,
            final String yearName) {
        final Map<String, Object> reportParams = new HashMap<>();
        if (WaterTaxConstants.NEWCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode())
                || WaterTaxConstants.ADDNLCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType()
                        .getCode()))
            reportParams.put("applicationType",
                    WordUtils.capitalize(waterConnectionDetails.getApplicationType().getName()));

        reportParams.put("municipality", session.getAttribute("citymunicipalityname"));
        reportParams.put("district", session.getAttribute("districtName"));
        reportParams.put("waterCharges", waterConnectionDetails.getConnectionType().name());
        reportParams.put("propertyassesmentNumber", waterConnectionDetails.getConnection().getPropertyIdentifier());
        reportParams.put("consumerNumber", waterConnectionDetails.getConnection().getConsumerCode());
        reportParams.put("pipeSize", waterConnectionDetails.getPipeSize().getSizeInInch());
        reportParams.put("mterSerialNumber", waterConnectionDetails.getConnection().getMeterSerialNumber());
        reportParams.put("applicantName", ownerName);
        reportParams.put("demandNoticeNumber", billObj != null && billObj.getBillNo() != null ? billObj.getBillNo()
                : "");
        reportParams.put("billMonth", monthName + "-" + yearName);
        reportParams.put("demandNoticeDate",
                billObj != null && billObj.getCreateDate() != null ? formatter.format(billObj.getCreateDate())
                        : null);
        reportParams.put("previousReading", meterReadingpriviousObj.getCurrentReading());
        if (meterReadingpriviousObj.getCurrentReadingDate() != null)
            reportParams.put("previousReadingDate", formatter.format(meterReadingpriviousObj.getCurrentReadingDate()));
        else
            reportParams.put("previousReadingDate", "");
        reportParams.put("currentReading", waterConnectionDetails.getMeterConnection().get(0).getCurrentReading());
        reportParams.put("currrentReadingDate",
                formatter.format(waterConnectionDetails.getMeterConnection().get(0).getCurrentReadingDate()));
        if (meterReadingpriviousObj.getCurrentReading() != null
                && !waterConnectionDetails.getMeterConnection().isEmpty()
                && waterConnectionDetails.getMeterConnection().get(0).getCurrentReading() != null)
            reportParams.put("noofunitsconsume", waterConnectionDetails.getMeterConnection().get(0).getCurrentReading()
                    - meterReadingpriviousObj.getCurrentReading());
        reportParams.put("totalBilltoCollect", waterConnectionDetailsService.getTotalAmount(waterConnectionDetails));
        reportParams.put("currentMonthCharges", getCurrentMonthDemandAmount(waterConnectionDetails,
                waterConnectionDetails.getMeterConnection().get(0).getCurrentReadingDate()));
        reportParams.put("totalDueAmount",
                getTotalDue(waterConnectionDetails, waterConnectionDetails.getMeterConnection().get(0).getCurrentReadingDate()));

        reportParams.put("address", assessmentDetails.getPropertyAddress());
        return reportParams;
    }

    public BigDecimal getTotalDue(final WaterConnectionDetails waterConnectionDetails, final Date givenDate) {
        BigDecimal balance;
        balance = waterConnectionDetailsService.getTotalAmount(waterConnectionDetails);
        final BigDecimal demnadDetCurrentamount = getCurrentMonthDemandAmount(waterConnectionDetails, givenDate);
        balance = balance.subtract(demnadDetCurrentamount);
        return balance;
    }

    private BigDecimal getCurrentMonthDemandAmount(final WaterConnectionDetails waterConnectionDetails, final Date givenDate) {
        BigDecimal currentAmount = BigDecimal.ZERO;
        final Installment installment = connectionDemandService.getCurrentInstallment(WaterTaxConstants.EGMODULE_NAME,
                WaterTaxConstants.MONTHLY, givenDate);
        final EgDemandReason demandReasonObj = connectionDemandService.getDemandReasonByCodeAndInstallment(
                WaterTaxConstants.WATERTAXREASONCODE, installment);
        final List<EgDemandDetails> demnadDetList = demandGenericDao.getDemandDetailsForDemandAndReasons(
                waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand(), Arrays.asList(demandReasonObj));
        if (!demnadDetList.isEmpty()) {
            final int detLength = demnadDetList.size() - 1;
            if (demnadDetList.get(0).getAmount() != null)
                currentAmount = demnadDetList.get(detLength).getAmount();
        }
        return currentAmount;
    }

    @RequestMapping(value = "/meterdemandnotice/view/{applicationNumber}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> viewEstimationNotice(@PathVariable final String applicationNumber,
            final HttpSession session) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByApplicationNumber(applicationNumber);
        return generateReport(waterConnectionDetails, session);
    }

}