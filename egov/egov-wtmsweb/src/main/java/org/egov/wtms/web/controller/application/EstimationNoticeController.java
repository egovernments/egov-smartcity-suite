/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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


import static org.egov.infra.reporting.util.ReportUtil.reportAsResponseEntity;
import static org.egov.infra.utils.DateUtils.toDefaultDateFormat;
import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.wtms.masters.entity.enums.ConnectionType.NON_METERED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CATEGORY_BPL;
import static org.egov.wtms.utils.constants.WaterTaxConstants.FILESTORE_MODULECODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NEWCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ADDNLCONNECTION;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.FileStoreUtils;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.EstimationNotice;
import org.egov.wtms.application.entity.FieldInspectionDetails;
import org.egov.wtms.application.entity.SearchNoticeDetails;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.EstimationNoticeService;
import org.egov.wtms.application.service.ReportGenerationService;
import org.egov.wtms.application.service.SearchNoticeService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.autonumber.EstimationNumberGenerator;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.egov.wtms.service.WaterEstimationChargesPaymentService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/application")
public class EstimationNoticeController {

    public static final String ESTIMATION_NOTICE = "estimationNotice";
    
    @Autowired
    private ReportService reportService;
    
    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private FileStoreService fileStoreService;
    
    @Autowired
    private EstimationNoticeService estimationNoticeService;
    
	@Autowired
    private ReportGenerationService reportGenerationService;

    @Autowired
    private SearchNoticeService searchNoticeService;
	
	@Autowired
    private AutonumberServiceBeanResolver beanResolver;
	
	@Autowired
    private FileStoreUtils fileStoreUtils;
	
	@Autowired
	private WaterEstimationChargesPaymentService waterEstimationChargesPaymentService;
	
	@Autowired
    private CityService cityService;
    
	@Autowired
	@Qualifier("parentMessageSource")
	private MessageSource messageSource;

	@GetMapping("/generateestimationnotice")
	public String generateDemandBill(Model model) {
		model.addAttribute("searchNoticeDetails", new SearchNoticeDetails());
		return "generateEstimationNotice";
	}

	@GetMapping(value = "/estimationNotice", produces = APPLICATION_PDF_VALUE)
	@ResponseBody
	public ResponseEntity<InputStreamResource> generateEstimationNotice(HttpServletRequest request,
			HttpSession session) {

		WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
				.findByApplicationNumber(request.getParameter("pathVar"));
		return generateEstimationReport(waterConnectionDetails, session);
	}
	
    private ResponseEntity<InputStreamResource> generateEstimationReport(WaterConnectionDetails waterConnectionDetails,
                                                                         HttpSession session) {
        ReportOutput reportOutput = new ReportOutput();
        if (waterConnectionDetails != null){
        	EstimationNotice estimationNotice = estimationNoticeService.getNonHistoryEstimationNoticeForConnection(waterConnectionDetails);
            if (estimationNotice != null) {
                File file = fileStoreService.fetch(estimationNotice.getEstimationNoticeFileStore(), FILESTORE_MODULECODE);
                reportOutput = new ReportOutput();
                try {
                    reportOutput.setReportName(estimationNotice.getEstimationNumber());
                    reportOutput.setReportOutputData(FileUtils.readFileToByteArray(file));
                    reportOutput.setReportFormat(ReportFormat.PDF);
                } catch (IOException e) {
                    throw new ApplicationRuntimeException("Exception in generating work order notice" + e);
                }
            } else {

                Map<String, Object> reportParams = new HashMap<>();
                AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                        waterConnectionDetails.getConnection().getPropertyIdentifier(),
                        PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ACTIVE);
                String[] doorNo = assessmentDetails.getPropertyAddress().split(",");
                StringBuilder ownerName = new StringBuilder();

                for (OwnerName names : assessmentDetails.getOwnerNames()) {
                    if (assessmentDetails.getOwnerNames().size() > 1)
                        ownerName.append(", ");
                    ownerName.append(names.getOwnerName());
                }

                reportParams.put("applicationType", WordUtils.capitalize(waterConnectionDetails.getApplicationType().getName()));
                reportParams.put("cityName", session.getAttribute("citymunicipalityname"));
                reportParams.put("district", session.getAttribute("districtName"));
                EstimationNumberGenerator estimationNoGen = beanResolver
        				.getAutoNumberServiceFor(EstimationNumberGenerator.class);
        		String estimationNumber = estimationNoGen.generateEstimationNumber();
                reportParams.put("estimationNumber", estimationNumber);
                reportParams.put("donationCharges", waterConnectionDetails.getDonationCharges());
                FieldInspectionDetails inspectionDetails = waterConnectionDetails.getFieldInspectionDetails();
                reportParams.put("estimationDate", toDefaultDateFormat(inspectionDetails.getCreatedDate()));
                double totalCharges = waterConnectionDetails.getDonationCharges()
                        + inspectionDetails.getSupervisionCharges()
                        + inspectionDetails.getRoadCuttingCharges()
                        + inspectionDetails.getSecurityDeposit();
                reportParams.put("totalCharges", totalCharges);
                reportParams.put("applicationDate", toDefaultDateFormat(waterConnectionDetails.getApplicationDate()));
                reportParams.put("applicantName", ownerName.toString());
                reportParams.put("address", assessmentDetails.getPropertyAddress());
                reportParams.put("houseNo", doorNo[0]);
                reportParams.put("propertyID", waterConnectionDetails.getConnection().getPropertyIdentifier());
                reportParams.put("amountInWords", reportGenerationService.getTotalAmntInWords(totalCharges));
                reportParams.put("securityDeposit", inspectionDetails.getSecurityDeposit());
                reportParams.put("roadCuttingCharges", inspectionDetails.getRoadCuttingCharges());
                reportParams.put("superVisionCharges", inspectionDetails.getSupervisionCharges());
                if (waterConnectionDetails.getConnectionType().equals(NON_METERED)) {
                    reportParams.put("estimationDetails", waterConnectionDetails.getEstimationDetails());
                    reportParams.put("designation", waterConnectionDetails.getState().getOwnerPosition().getDeptDesig().getDesignation().getName());
                    reportOutput = reportService.createReport(new ReportRequest("wtr_estimation_notice_for_non_metered",
                            waterConnectionDetails.getEstimationDetails(), reportParams));
                } else {
                    reportOutput = reportService.createReport(new ReportRequest(ESTIMATION_NOTICE,
                            waterConnectionDetails.getEstimationDetails(), reportParams));
                }

                reportOutput.setReportFormat(ReportFormat.PDF);
                reportOutput.setReportName(estimationNumber);
            }
        }
        return reportAsResponseEntity(reportOutput);
    }

    @GetMapping(value = "/estimationNotice/view/{applicationNumber}", produces = APPLICATION_PDF_VALUE)
    @ResponseBody
    public ResponseEntity<InputStreamResource> viewEstimationNotice(@PathVariable String applicationNumber,
                                                                    HttpSession session) {
        WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumber(applicationNumber);
        return generateEstimationReport(waterConnectionDetails, session);
    }
    
	@RequestMapping(value = "/result", method = POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String searchResult(@ModelAttribute SearchNoticeDetails searchNoticeDetails, BindingResult resultBinder,
			Model model, final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		String failureMessage = StringUtils.EMPTY;

		WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
				.findActiveConnectionDetailsByConsumerCodeAndApplicationNumber(searchNoticeDetails.getHscNo(),
						searchNoticeDetails.getApplicationNumber());
		if (waterConnectionDetails != null) {
			boolean isNonMeteredAndNonBPL = false;
			if ((NEWCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode())
					|| ADDNLCONNECTION.equalsIgnoreCase(waterConnectionDetails.getApplicationType().getCode()))
					&& !CATEGORY_BPL.equals(waterConnectionDetails.getCategory().getName())
					&& ConnectionType.NON_METERED.equals(waterConnectionDetails.getConnectionType())
					&& waterConnectionDetails.getCreatedDate()
							.compareTo(DateUtils
									.toDateUsingDefaultPattern(waterConnectionDetailsService.getGOEffectiveDate())) >= 0
					&& waterConnectionDetails.getUlbMaterial() != null
					&& waterConnectionDetails.getExecutionDate() != null)
				isNonMeteredAndNonBPL = true;

			if (!isNonMeteredAndNonBPL) {
				failureMessage = messageSource.getMessage("err.validate.estimationnotice.generate", null,
						Locale.getDefault());
			} else {
				BigDecimal estimationDues = waterEstimationChargesPaymentService
						.getEstimationDueAmount(waterConnectionDetails);
				if (estimationDues.compareTo(BigDecimal.ZERO) == 0)
					failureMessage = messageSource.getMessage("err.connection.without.due", null, Locale.getDefault());
			}
		} else
			failureMessage = messageSource.getMessage("err.applicationno.and.consumerno.not.correct", null,
					Locale.getDefault());

		if (StringUtils.isNotBlank(failureMessage))
			return String.format("{ \"error\":\" %s \" }", failureMessage);

		return new StringBuilder("{\"data\":").append(toJSON(preparNoticeDetails(waterConnectionDetails))).append("}")
				.toString();
	}

	@RequestMapping(value = "/waterTax/downloadEstimationNotice")
	public ResponseEntity<InputStreamResource> downloadSignedWorkOrderConnection(
			@RequestParam final String fileStoreId) {
		return fileStoreUtils.fileAsResponseEntity(fileStoreId, WaterTaxConstants.FILESTORE_MODULECODE, true);
	}

	private List<SearchNoticeDetails> preparNoticeDetails(WaterConnectionDetails waterConnectionDetails) {
		EstimationNumberGenerator estimationNoGen = beanResolver
				.getAutoNumberServiceFor(EstimationNumberGenerator.class);
		String estimationNumber = estimationNoGen.generateEstimationNumber();
		EstimationNotice estimationNotice = waterConnectionDetailsService
				.addEstimationNoticeToConnectionDetails(waterConnectionDetails, estimationNumber);
		ReportOutput reportOutput = reportGenerationService.generateNewEstimationNotice(waterConnectionDetails,
				estimationNumber, cityService.getMunicipalityName(), cityService.getDistrictName());
		if (reportOutput != null)
			waterConnectionDetailsService.updateConnectionDetailsWithEstimationNotice(waterConnectionDetails,
					estimationNotice, reportOutput);
		SearchNoticeDetails noticeDetails = searchNoticeService.buildNoticeDetails(waterConnectionDetails);
		noticeDetails.setEstimationNumber(estimationNotice.getEstimationNumber());
		noticeDetails.setEstimationDate(toDefaultDateFormat(estimationNotice.getEstimationNoticeDate()));
		noticeDetails.setFileStoreID(estimationNotice.getEstimationNoticeFileStore().getFileStoreId());
		List<SearchNoticeDetails> noticeDetailList = new ArrayList<SearchNoticeDetails>();
		noticeDetailList.add(noticeDetails);
		return noticeDetailList;
	}

}