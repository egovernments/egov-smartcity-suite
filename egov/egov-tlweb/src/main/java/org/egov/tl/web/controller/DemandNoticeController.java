/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.tl.web.controller;

import static org.egov.infra.utils.DateUtils.currentDateToDefaultDateFormat;
import static org.egov.infra.utils.DateUtils.getDefaultFormattedDate;
import static org.egov.infra.utils.DateUtils.toYearFormat;
import static org.egov.tl.utils.Constants.CITY_GRADE_CORPORATION;
import static org.egov.tl.utils.Constants.TL_LICENSE_ACT_CORPORATION;
import static org.egov.tl.utils.Constants.TL_LICENSE_ACT_DEFAULT;
import static org.egov.tl.utils.Constants.TRADELICENSE_MODULENAME;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.tl.entity.PenaltyRates;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.dto.DemandnoticeForm;
import org.egov.tl.service.PenaltyRatesService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.utils.LicenseUtils;
import org.egov.tl.utils.TradeLicenseDemandBillHelper;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demandnotice")
public class DemandNoticeController {
	private final Map<String, Object> reportParams = new HashMap<>();
	@Autowired
	private TradeLicenseService tradeLicenseService;
	@Autowired
	private LicenseUtils licenseUtils;
	@Autowired
	private InstallmentHibDao installmentDao;

	@Autowired
	private PenaltyRatesService penaltyRatesService;

	@Autowired
	private CityService cityService;

	@Autowired
	private AppConfigValueService appConfigValueService;

	@Autowired
	private ReportService reportService;
	private Map<Integer, String> monthMap = new LinkedHashMap<Integer, String>();

	@RequestMapping(value = "/report", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> generateDemandNotice(@RequestParam Long licenseId) {
		final TradeLicense license = tradeLicenseService.getLicenseById(licenseId);
		return generateReport(license);
	}

	private ResponseEntity<byte[]> generateReport(TradeLicense license) {

		if (license != null && license.getCurrentDemand() != null) {

			DateTimeFormatter FORMAT_DATE_TO_YEAR_YY = DateTimeFormat.forPattern("yy");
			// Get current installment by using demand.
			Installment currentInstallment = license.getCurrentDemand().getEgInstallmentMaster();
			reportParams.put("cityName", ApplicationThreadLocals.getMunicipalityName());
			reportParams.put("licenseNumber", license.getLicenseNumber());
			reportParams.put("ownerName", license.getLicensee().getApplicantName());
			reportParams.put("tradeNature", license.getTradeName().getName());
			reportParams.put("tradeName", license.getNameOfEstablishment());
			reportParams.put("tradeAddress", license.getAddress());
			reportParams.put("cityUrl", ApplicationThreadLocals.getDomainURL());
			getActDeclarationDetailBasedOnCityGrade();

			reportParams.put("installmentYear", toYearFormat(currentInstallment.getFromDate()) + "-"
					+ toYearFormat(currentInstallment.getToDate()));
			reportParams.put("currentDate", currentDateToDefaultDateFormat());

			// GET PREVIOUS INSTALLMENTS BASED ON CURRENT INSTALLMENT.
			List<Installment> previousInstallment = installmentDao
					.fetchPreviousInstallmentsInDescendingOrderByModuleAndDate(licenseUtils.getModule("Trade License"),
							currentInstallment.getToDate(), 1);

			if (previousInstallment != null && previousInstallment.size() > 0) {
				reportParams.put("lastyear", toYearFormat(previousInstallment.get(0).getFromDate()) + "-"
						+ FORMAT_DATE_TO_YEAR_YY.print(new LocalDate(previousInstallment.get(0).getToDate())));
				// 31-december-financialyear will be considered as last date for
				// renewal.
				Date endDateOfPreviousFinancialYear = new DateTime(previousInstallment.get(0).getFromDate())
						.withMonthOfYear(12).withDayOfMonth(31).toDate();

				reportParams.put("endDateOfPreviousFinancialYear",
						getDefaultFormattedDate(endDateOfPreviousFinancialYear));

				BigDecimal currLicenseFee;
				BigDecimal arrLicenseFee;
				BigDecimal arrLicensePenalty;

				Map<String, Map<String, BigDecimal>> outstandingFees = tradeLicenseService
						.getOutstandingFeeForDemandNotice(license, currentInstallment, previousInstallment.get(0));
				Map<String, BigDecimal> licenseFees = outstandingFees.get("License Fee");
				if (licenseFees != null) {
					currLicenseFee = licenseFees.get("current") == null ? BigDecimal.ZERO
							: licenseFees.get("current").setScale(0, BigDecimal.ROUND_HALF_UP);
					arrLicenseFee = licenseFees.get("arrear") == null ? BigDecimal.ZERO
							: licenseFees.get("arrear").setScale(0, BigDecimal.ROUND_HALF_UP);
					arrLicensePenalty = licenseFees.get("penalty") == null ? BigDecimal.ZERO
							: licenseFees.get("penalty").setScale(0, BigDecimal.ROUND_HALF_UP);
				} else {
					currLicenseFee = BigDecimal.ZERO;
					arrLicenseFee = BigDecimal.ZERO;
					arrLicensePenalty = BigDecimal.ZERO;
				}

				BigDecimal totalAmount = currLicenseFee.add(arrLicenseFee).add(arrLicensePenalty);
				monthMap = DateUtils.getAllMonths();
				List<TradeLicenseDemandBillHelper> monthWiseDemandDetails = new LinkedList<TradeLicenseDemandBillHelper>();
				getMonthWiseLatePenaltyFeeDetails(license, currentInstallment, currLicenseFee, arrLicenseFee,
						arrLicensePenalty, monthWiseDemandDetails);

				reportParams.put("monthWiseDemandDetails", monthWiseDemandDetails);
				reportParams.put("licenseFee", currLicenseFee);
				reportParams.put("penaltyFee", arrLicensePenalty);
				reportParams.put("arrearLicenseFee", arrLicenseFee);
				reportParams.put("totalLicenseFee", totalAmount.setScale(0, BigDecimal.ROUND_HALF_UP));
				List<PenaltyRates> penaltyRates = penaltyRatesService.search(license.getLicenseAppType().getId());
				reportParams.put("penaltyCalculationMessage", getPenaltyRateDetails(penaltyRates, currentInstallment));
				reportParams.put("currentYear", toYearFormat(currentInstallment.getFromDate()));

			}
		}
		final ReportRequest reportInput = new ReportRequest("tldemandnotice", license, reportParams);

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		headers.add("content-disposition", "inline;filename=License Demand Notice.pdf");
		final ReportOutput reportOutput = reportService.createReport(reportInput);
		return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
	}

	private void getActDeclarationDetailBasedOnCityGrade() {

		City city = cityService.getCityByCode(ApplicationThreadLocals.getCityCode());

		if (city != null && city.getGrade().equalsIgnoreCase(CITY_GRADE_CORPORATION)) {
			final List<AppConfigValues> corporationAct = appConfigValueService
					.getConfigValuesByModuleAndKey(TRADELICENSE_MODULENAME, TL_LICENSE_ACT_CORPORATION);
			reportParams.put("actDeclaration",
					corporationAct != null && corporationAct.get(0) != null ? corporationAct.get(0).getValue() : " ");
		} else {
			final List<AppConfigValues> municipalityAct = appConfigValueService
					.getConfigValuesByModuleAndKey(TRADELICENSE_MODULENAME, TL_LICENSE_ACT_DEFAULT);

			reportParams.put("actDeclaration", municipalityAct != null && municipalityAct.get(0) != null
					? municipalityAct.get(0).getValue() : " ");
		}
	}

	private void getMonthWiseLatePenaltyFeeDetails(TradeLicense license, Installment currentInstallment,
			BigDecimal currLicenseFee, BigDecimal arrLicenseFee, BigDecimal arrLicensePenalty,
			List<TradeLicenseDemandBillHelper> monthWiseDemandDetails) {

		Date currentInstallment_March_thirtyFirst = new DateTime(currentInstallment.getFromDate()).withMonthOfYear(3)
				.withDayOfMonth(31).toDate();
		String installmentYear = toYearFormat(currentInstallment.getFromDate());

		// GET LICENSE FEE TYPES AND DECIDE PENALTY. Monthwise, show penalty
		// details
		for (int i = 1; i <= 12; i++) {
			TradeLicenseDemandBillHelper demandBillDtl = new TradeLicenseDemandBillHelper();

			DateTime financialYearDate = new DateTime(currentInstallment.getFromDate()).withMonthOfYear(i);
			Date monthEndDate = new DateTime(financialYearDate)
					.withDayOfMonth(financialYearDate.dayOfMonth().getMaximumValue()).toDate();

			// Eg: 31/03/2016 vs 31/01/2016 days penalty 0%
			// 31/03/2016 vs 29/02/2016 days penalty 0%
			// 31/03/2016 vs 31/03/2016 days penalty 25%
			BigDecimal penaltyAmt = penaltyRatesService.calculatePenalty(currentInstallment_March_thirtyFirst,
					monthEndDate, currLicenseFee, license);

			demandBillDtl.setMonth(monthMap.get(i).concat(", ").concat(installmentYear));
			demandBillDtl.setArrersWithPenalty(arrLicenseFee.add(arrLicensePenalty));
			demandBillDtl.setLicenseFee(currLicenseFee);
			demandBillDtl.setPenalty(penaltyAmt.setScale(0, BigDecimal.ROUND_HALF_UP));
			demandBillDtl.setTotalDues((arrLicenseFee.add(arrLicensePenalty).add(currLicenseFee).add(penaltyAmt))
					.setScale(0, BigDecimal.ROUND_HALF_UP));
			monthWiseDemandDetails.add(demandBillDtl);
		}
	}

	public String getPenaltyRateDetails(List<PenaltyRates> penaltyRates, Installment currentInstallment) {
		StringBuilder penaltylist = new StringBuilder();
		for (PenaltyRates penaltyRate : penaltyRates) {
			LocalDate currentinstStartdate = LocalDate.fromDateFields(currentInstallment.getFromDate());
			if (penaltyRate.getRate() <= 0 || penaltyRate.getToRange() < 0) {
				penaltylist.append("Before "
						+ getDefaultFormattedDate(
								currentinstStartdate.plusDays(penaltyRate.getToRange().intValue()).toDate())
						+ " without penalty" + "\n");
			}
			if (penaltyRate.getToRange() >= 0) {
				if (penaltyRate.getToRange() >= 999) {
					penaltylist.append("    After "
							+ getDefaultFormattedDate(
									currentinstStartdate.plusDays(penaltyRate.getFromRange().intValue()).toDate())
							+ " with " + penaltyRate.getRate().intValue() + "% penalty");
				} else {
					penaltylist
							.append("    From "
									+ getDefaultFormattedDate(currentinstStartdate
											.plusDays(penaltyRate.getFromRange().intValue()).toDate())
									+ " to "
									+ getDefaultFormattedDate(currentinstStartdate
											.plusDays(penaltyRate.getToRange().intValue() - 1).toDate())
									+ " with " + penaltyRate.getRate().intValue() + "% penalty" + "\n");

				}

			}
		}
		return penaltylist.toString();

	}

	@RequestMapping(value = "/generate", method = RequestMethod.GET)
	@ResponseBody
	public String mergeAndDownload(@ModelAttribute final DemandnoticeForm searchRequest,
			final HttpServletResponse response) throws Exception {
		final List<DemandnoticeForm> noticeList = tradeLicenseService.searchLicensefordemandnotice(searchRequest);
		final List<InputStream> demandNotice_pdf = new ArrayList<InputStream>();

		if (noticeList != null && noticeList.size() > 0) {
			for (final DemandnoticeForm tlNotice : noticeList) {
				if (tlNotice != null) {
					ResponseEntity<byte[]> demandNotice = generateReport(
							tradeLicenseService.getLicenseById(tlNotice.getLicenseId()));

					final byte[] bFile = demandNotice.getBody();
					demandNotice_pdf.add(new ByteArrayInputStream(bFile));
				}
			}

			try {
				if (demandNotice_pdf.size() > 0) {
					final ByteArrayOutputStream output = new ByteArrayOutputStream();
					final byte[] data = tradeLicenseService.mergePdfFiles(demandNotice_pdf, output);
					response.setHeader("content-disposition", "inline;filename=License Demand Notice.pdf");
					response.setContentType("application/pdf");
					response.setContentLength(data.length);
					response.getOutputStream().write(data);
				}
			} catch (final IOException e) {
				throw new ValidationException(Arrays.asList(new ValidationError("error", e.getMessage())));
			}
		}
		return null;
	}
}