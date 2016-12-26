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
package org.egov.wtms.web.controller.reports;

import static org.egov.ptis.constants.PropertyTaxConstants.LOCALITY;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.wtms.application.entity.InstDmdCollResponse;
import org.egov.wtms.application.entity.WaterChargeMaterlizeView;
import org.egov.wtms.application.service.ArrearRegisterReportService;
import org.egov.wtms.web.reports.entity.ArrearRegisterReport;
import org.egov.wtms.web.reports.entity.ArrearReportInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/reports/arrear")
public class ArrearRegisterReportController {

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private ReportService reportService;
    @Autowired
    private ArrearRegisterReportService arrearRegisterReportService;

    private final Map<String, Object> reportParams = new HashMap<>();

    @ModelAttribute("zones")
    public List<Boundary> zones() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ZONE,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("wards")
    public List<Boundary> wards() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute
    public void getReportHelper(final Model model) {
        final ArrearRegisterReport reportHealperObj = new ArrearRegisterReport();
        model.addAttribute("reportHelper", reportHealperObj);

    }

    @ModelAttribute("localitys")
    public List<Boundary> localitys() {
        return boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(LOCALITY, LOCATION_HIERARCHY_TYPE);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/coonectionReport/wardWise")
    public String searchNoOfConnectionByBoundaryForm(final Model model) {
        model.addAttribute("currDate", new Date());
        return "arrearRegister-report";
    }

    @ExceptionHandler(Exception.class)
    @RequestMapping(value = "/arrearReportList", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> springPaginationDataTablesUpdate(@RequestParam final String wardId,
            @RequestParam final String localityId, @RequestParam final String areaId,
            @RequestParam final String zoneId, final HttpServletRequest request, final HttpSession session,
            final HttpServletResponse response)
            throws IOException {
        final List<ArrearRegisterReport> propertyWiseInfoList = null;
        final ArrearReportInfo arrearreportInfo = new ArrearReportInfo();
        String strZoneNum = "";
        String strWardNum = "";
        String strBlockNum = "";
        String strLocalityNum = "";
        if (localityId == null && zoneId != null)
            strZoneNum = boundaryService.getBoundaryById(Long.valueOf(zoneId)).getName();
        else if (localityId != null) {
            strLocalityNum = boundaryService.getBoundaryById(Long.valueOf(localityId)).getName();
            if (zoneId != null)
                strZoneNum = boundaryService.getBoundaryById(Long.valueOf(zoneId)).getName();
        }
        if (wardId != null)
            strWardNum = boundaryService.getBoundaryById(Long.valueOf(wardId)).getName();
        if (areaId != null)
            strBlockNum = boundaryService.getBoundaryById(Long.valueOf(areaId)).getName();
        reportParams.put("municipality", session.getAttribute("citymunicipalityname"));
        reportParams.put("district", session.getAttribute("districtName"));

        final List<WaterChargeMaterlizeView> propertyViewList = arrearRegisterReportService.prepareQueryforArrearRegisterReport(
                Long.valueOf(wardId),
                Long.valueOf(localityId));

        for (final WaterChargeMaterlizeView propMatView : propertyViewList)
            // If there is only one Arrear Installment
            if (propMatView.getInstDmdCollResponseList().size() == 1) {
                final InstDmdCollResponse currIDCMatView = propMatView.getInstDmdCollResponseList()
                        .iterator().next();
                final ArrearRegisterReport propertyWiseInfo = preparePropertyWiseInfo(currIDCMatView);
                if (propertyWiseInfo != null)
                    propertyWiseInfoList.add(propertyWiseInfo);
            } else {
                // if there are more than one arrear Installments
                final List<InstDmdCollResponse> idcList = new ArrayList<>(
                        propMatView.getInstDmdCollResponseList());
                final List unitList = new ArrayList();
                ArrearRegisterReport propertyWiseInfoTotal = null;

                for (final InstDmdCollResponse instlDmdColMatView : idcList) {
                    final ArrearRegisterReport propertyWiseInfo = preparePropertyWiseInfo(instlDmdColMatView);
                    if (propertyWiseInfo != null) {
                        // initially the block is executed
                        if (unitList.isEmpty()) {
                            unitList.add(propertyWiseInfo.getArrearInstallmentDesc());
                            propertyWiseInfoTotal = propertyWiseInfo;
                        } else if (unitList.contains(propertyWiseInfo.getArrearInstallmentDesc()))
                            propertyWiseInfoTotal = addPropertyWiseInfo(propertyWiseInfoTotal, propertyWiseInfo);
                        else if (!unitList.contains(propertyWiseInfo.getArrearInstallmentDesc())) {

                            propertyWiseInfoList.add(propertyWiseInfoTotal);
                            unitList.add(propertyWiseInfo.getArrearInstallmentDesc());
                            propertyWiseInfoTotal = propertyWiseInfo;
                            propertyWiseInfoTotal.setIndexNumber("");
                            propertyWiseInfoTotal.setOwnerName("");
                            propertyWiseInfoTotal.setHouseNo("");
                        }
                    } // end of if - null condition
                    else
                        propertyWiseInfoList.add(propertyWiseInfoTotal);
                }
            }

        arrearreportInfo.setZoneNo(strZoneNum);
        arrearreportInfo.setWardNo(strWardNum);
        arrearreportInfo.setBlockNo(strBlockNum);
        arrearreportInfo.setLocalityNo(strLocalityNum);
        arrearreportInfo.setMunicipal("");
        arrearreportInfo.setDistrict("");
        arrearreportInfo.setPropertyWiseArrearInfoList(propertyWiseInfoList);
        return generateReport(arrearreportInfo);

    }

    private ResponseEntity<byte[]> generateReport(final ArrearReportInfo arrearreportInfo) {
        ReportRequest reportInput = null;
        ReportOutput reportOutput;
        if (null != arrearreportInfo) {

            reportParams.put("propertyWiseArrearInfoList", arrearreportInfo.getArrAdvtColl());
            reportParams.put("zoneNo", arrearreportInfo.getZoneNo());
            reportInput = new ReportRequest("ArrearWTRegister", arrearreportInfo, reportParams);
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=arrearReport.pdf");
        reportOutput = reportService.createReport(reportInput);
        return new ResponseEntity<byte[]>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

    /**
     * @param propertyWiseInfoTotal
     * @param propertyInfo
     * @return
     */
    private ArrearRegisterReport addPropertyWiseInfo(final ArrearRegisterReport propertyWiseInfoTotal,
            final ArrearRegisterReport propertyInfo) {
        propertyWiseInfoTotal.setArrearLibraryCess(propertyWiseInfoTotal.getArrearLibraryCess().add(
                propertyInfo.getArrearLibraryCess()));
        propertyWiseInfoTotal.setArrearPropertyTax(propertyWiseInfoTotal.getArrearPropertyTax().add(
                propertyInfo.getArrearPropertyTax()));
        propertyWiseInfoTotal.setArrearVacantLandTax(propertyWiseInfoTotal.getArrearVacantLandTax().add(
                propertyInfo.getArrearVacantLandTax()));
        propertyWiseInfoTotal.setArrearPenalty(propertyWiseInfoTotal.getArrearPenalty().add(propertyInfo.getArrearPenalty()));
        propertyWiseInfoTotal.setArrearEducationCess(
                propertyWiseInfoTotal.getArrearEducationCess().add(propertyInfo.getArrearEducationCess()));
        propertyWiseInfoTotal.setTotalArrearTax(propertyWiseInfoTotal.getTotalArrearTax().add(propertyInfo.getTotalArrearTax()));
        return propertyWiseInfoTotal;
    }

    /**
     * @param currInstDmdColMatView
     * @param currInstallment
     * @return
     */
    private ArrearRegisterReport preparePropertyWiseInfo(final InstDmdCollResponse currInstDmdColMatView) {
        ArrearRegisterReport propertyWiseInfo;
        propertyWiseInfo = preparePropInfo(currInstDmdColMatView.getWaterChargeMaterlizeView());
        final BigDecimal totalTax = currInstDmdColMatView.getWaterCharge();

        propertyWiseInfo.setArrearInstallmentDesc(currInstDmdColMatView.getInstallment().getDescription());
        propertyWiseInfo.setWaterCharge(currInstDmdColMatView.getWaterCharge());
        propertyWiseInfo.setWaterChargeColl(currInstDmdColMatView.getWaterchargecoll());
        /*
         * Total of Arrear Librarycess tax,general tax and penalty tax
         */
        propertyWiseInfo.setTotalArrearTax(totalTax);
        return propertyWiseInfo;
    }

    /**
     * @param propMatView
     * @return
     */
    private ArrearRegisterReport preparePropInfo(final WaterChargeMaterlizeView propMatView) {
        final ArrearRegisterReport propertyWiseInfo = new ArrearRegisterReport();
        propertyWiseInfo.setBasicPropId(propMatView.getId().getWaterConnectionDetails().getId());
        propertyWiseInfo.setIndexNumber(propMatView.getHscno());
        propertyWiseInfo.setOwnerName(propMatView.getUsername());
        propertyWiseInfo.setHouseNo(propMatView.getHouseno());
        return propertyWiseInfo;
    }
}
