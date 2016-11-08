/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.mrs.web.controller.reports;

import static org.egov.infra.web.utils.WebUtils.toJSON;
import static org.egov.mrs.application.MarriageConstants.BOUNDARY_TYPE;
import static org.egov.mrs.application.MarriageConstants.REVENUE_HIERARCHY_TYPE;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.mrs.application.reports.service.MarriageRegistrationReportsService;
import org.egov.mrs.domain.entity.MaritalStatusReport;
import org.egov.mrs.domain.entity.MarriageCertificate;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.MarriageRegistration.RegistrationStatus;
import org.egov.mrs.domain.entity.RegistrationCertificatesResultForReport;
import org.egov.mrs.domain.enums.MaritalStatus;
import org.egov.mrs.domain.service.MarriageRegistrationService;
import org.egov.mrs.web.adaptor.MaritalStatusReportJsonAdaptor;
import org.egov.mrs.web.adaptor.MarriageRegistrationCertificateReportJsonAdaptor;
import org.egov.mrs.web.adaptor.MarriageRegistrationJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Controller to show report of Registration status
 * 
 * @author nayeem
 *
 */
@Controller
@RequestMapping(value = "/report")
public class MarriageRegistrationReportsController {
	

	private final static String[] RANGES = new String[] { "0-18", "19-25", "26-30", "31-35", "36-40", "40-45", "46-50",
			"50-100" };
	private final static String KEY_AGE = "age";
	private final static String KEY_HUSBANDCOUNT = "husbandcount";
	private final static String KEY_WIFECOUNT = "wifecount";
	
	@Autowired
    protected BoundaryService boundaryService;
	
	@Autowired
	private MarriageRegistrationService marriageRegistrationService;
	
	@Autowired
	private MarriageRegistrationReportsService marriageRegistrationReportsService;
	
	
	
	 public @ModelAttribute("zones") List<Boundary> getZonesList() {
			return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(BOUNDARY_TYPE, REVENUE_HIERARCHY_TYPE);
	 }	
	@RequestMapping(value = "/registrationstatus", method = RequestMethod.GET)
	public String showReportForm(final Model model) {
		model.addAttribute("registration", new MarriageRegistration());
		model.addAttribute("status", RegistrationStatus.values());
		return "report-registrationstatus";
	}

	@RequestMapping(value = "/registrationstatus", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String search(Model model, @ModelAttribute final MarriageRegistration registration)
			throws ParseException {
		List<MarriageRegistration> searchResultList = marriageRegistrationService
				.searchRegistrationByStatus(registration, registration.getStatus().getCode());
		String result = new StringBuilder("{ \"data\":")
				.append(toJSON(searchResultList, MarriageRegistration.class, MarriageRegistrationJsonAdaptor.class))
				.append("}").toString();
		return result;
	}
	
	
	@RequestMapping(value="/age-wise", method = RequestMethod.GET)
    public String newSearchForm(final Model model) {
		model.addAttribute("registration", new MarriageRegistration());
        return "report-registration-agewise";
    }
	
	@SuppressWarnings("serial")
	@RequestMapping(value = "/age-wise", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String searchAgeWise(@RequestParam("year") int year, Model model, @ModelAttribute final MarriageRegistration registration)
			throws ParseException {

		HashMap<String, Integer> husbandAgeRangesCount = getCountByRange(marriageRegistrationReportsService.searchRegistrationOfHusbandAgeWise(year));
		HashMap<String, Integer> wifeAgeRangesCount = getCountByRange(marriageRegistrationReportsService.searchRegistrationOfWifeAgeWise(year));

		ArrayList<HashMap<String, Object>> result = new ArrayList<>();

		for (String range : RANGES) {
			HashMap<String, Object> rangeMap = new HashMap<>();
			rangeMap.put(KEY_AGE, range);
			rangeMap.put(KEY_HUSBANDCOUNT, husbandAgeRangesCount.get(range)!=null?husbandAgeRangesCount.get(range):0);
			rangeMap.put(KEY_WIFECOUNT, wifeAgeRangesCount.get(range)!=null?wifeAgeRangesCount.get(range):0);
			result.add(rangeMap);
		}

		JsonArray jsonArray = (JsonArray) new Gson().toJsonTree(result, new TypeToken<List<HashMap<String, Object>>>() {
		}.getType());

		JsonObject response = new JsonObject();
		response.add("data", jsonArray);
		return response.toString();
	}

	private HashMap<String, Integer> getCountByRange(String[] inputs) {

		HashMap<String, Integer> response = new HashMap<>();

		for (String input : inputs) {
			String[] values=input.split(","); //age,count -> [0] - age, [1] - count
			Integer age = Integer.valueOf(values[0]);
			for (String range : RANGES) {
				if (isInRange(range, age)) {
					int existingCount = response.get(range) != null ? response.get(range) : 0;
					response.put(range, existingCount + Integer.valueOf(values[1]));
					break;
				}
			}
		}

		return response;
	}

	private boolean isInRange(String ranges, Integer input) {
		String[] range = ranges.split("-");
		return (input >= Integer.valueOf(range[0]) && input <= Integer.valueOf(range[1]));
	}
	
	@RequestMapping(value = "/age-wise/view/{year}/{applicantType}/{ageRange}", method = RequestMethod.GET)
	public String viewAgeWiseDetails(@PathVariable final int year, @PathVariable final String applicantType,
			@PathVariable final String ageRange, final Model model) throws IOException, ParseException {
		List<MarriageRegistration> marriageRegistrations = marriageRegistrationReportsService.getAgewiseDetails(ageRange,
				applicantType, year);
		model.addAttribute("marriageRegistrations", marriageRegistrations);
		model.addAttribute("applicantType", applicantType);
		return "marriage-agewise-view";
	}
	
	@RequestMapping(value = "/certificatedetails", method = RequestMethod.GET)
    public String searchCertificatesForReport(final Model model) {
    	model.addAttribute("certificate", new MarriageCertificate());
        return "registration-certificates-report";
    }

    @RequestMapping(value = "/certificatedetails", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String searchApprovedMarriageRecords(Model model,@ModelAttribute final MarriageCertificate certificate) throws ParseException {
    	List<RegistrationCertificatesResultForReport> regCertificateResult = new ArrayList<RegistrationCertificatesResultForReport>();
    	List<Object[]> searchResultList = marriageRegistrationReportsService.searchMarriageRegistrationsForCertificateReport(certificate);
    	for (Object[] objects : searchResultList) {
    		RegistrationCertificatesResultForReport certificatesResultForReport = new RegistrationCertificatesResultForReport();
    		certificatesResultForReport.setRegistrationNo(objects[0].toString());
    		certificatesResultForReport.setDateOfMarriage(objects[1].toString());
    		certificatesResultForReport.setRegistrationDate(objects[2].toString());
    		//certificatesResultForReport.setRejectReason(objects[3].toString()!= null?objects[3].toString():"");
    		certificatesResultForReport.setCertificateNo(objects[4].toString()!= null?objects[4].toString():"");
    		certificatesResultForReport.setCertificateType(objects[5].toString());
    		certificatesResultForReport.setCertificateDate(objects[6].toString());
    		certificatesResultForReport.setZone(objects[7].toString());
    		certificatesResultForReport.setHusbandName(objects[8].toString());
    		certificatesResultForReport.setWifeName(objects[9].toString());
    		certificatesResultForReport.setId(Long.valueOf(objects[10].toString()));
    		regCertificateResult.add(certificatesResultForReport);
		}
      	 String result = new StringBuilder("{ \"data\":").append(toJSON(regCertificateResult,RegistrationCertificatesResultForReport.class,  MarriageRegistrationCertificateReportJsonAdaptor.class)).append("}")
                   .toString();
          return result;
    } 
    
	@RequestMapping(value = "/status-at-time-marriage", method = RequestMethod.GET)
	public String getStatusAtTimeOfMarriage(final Model model) {
		model.addAttribute("registration", new MarriageRegistration());
		model.addAttribute("maritalStatusList", Arrays.asList(MaritalStatus.values()));
		return "statustime-ofmarriage-report";
	}

	@RequestMapping(value = "/status-at-time-marriage", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String searchStatusAtTimeOfMarriage(@RequestParam("year") int year, Model model,
			@ModelAttribute final MarriageRegistration registration) throws ParseException {
		List<MaritalStatusReport> maritalStatusReports = new ArrayList<>();
		maritalStatusReports.addAll(putRecordsIntoHashMapByMonth(
				marriageRegistrationReportsService.getHusbandCountByMaritalStatus(year), "husband"));
		maritalStatusReports.addAll(putRecordsIntoHashMapByMonth(
				marriageRegistrationReportsService.getWifeCountByMaritalStatus(year), "wife"));
		String result = new StringBuilder("{ \"data\":")
				.append(toJSON(maritalStatusReports, MaritalStatusReport.class, MaritalStatusReportJsonAdaptor.class))
				.append("}").toString();
		return result;
	}

	private List<MaritalStatusReport> putRecordsIntoHashMapByMonth(List<String[]> recordList, String applicantType) {
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		for (Object[] category : recordList) {
			if (map.containsKey(category[1])) {
				if (map.get(category[1]).containsKey(category[0])) {
					map.get(category[1]).put(String.valueOf(category[0]), String.valueOf(category[2]));
				} else {
					Map<String, String> subMap = new HashMap<>();
					subMap.put(String.valueOf(category[0]), String.valueOf(category[2]));
					map.get(category[1]).put(String.valueOf(category[0]), String.valueOf(category[2]));

				}
			} else {
				Map<String, String> subMap = new HashMap<>();
				subMap.put(String.valueOf(category[0]), String.valueOf(category[2]));
				map.put(String.valueOf(category[1]), subMap);

				map.get(category[1]).put("applicantType", applicantType);
			}
		}
		List<MaritalStatusReport> maritalStatusReports = new ArrayList<>();
		for (Entry<String, Map<String, String>> resMap : map.entrySet()) {
			MaritalStatusReport report = new MaritalStatusReport();
			report.setMonth(resMap.getKey());
			for (Entry<String, String> valuesMap : resMap.getValue().entrySet()) {
				if (valuesMap.getValue().equalsIgnoreCase("husband") || valuesMap.getValue().equalsIgnoreCase("wife"))
					report.setApplicantType(applicantType);

				if (valuesMap.getKey().equalsIgnoreCase(MaritalStatus.Married.toString())) {
					report.setMarried(valuesMap.getValue() != null ? valuesMap.getValue() : "0");
				} else if (valuesMap.getKey().equalsIgnoreCase(MaritalStatus.Unmarried.toString())) {
					report.setUnmarried(valuesMap.getValue() != null ? valuesMap.getValue() : "0");
				} else if (valuesMap.getKey().equalsIgnoreCase(MaritalStatus.Widower.toString())) {
					report.setWidower(valuesMap.getValue() != null ? valuesMap.getValue() : "0");
				} else if (valuesMap.getKey().equalsIgnoreCase(MaritalStatus.Divorced.toString())) {
					report.setDivorced(valuesMap.getValue() != null ? valuesMap.getValue() : "0");
				}

			}
			maritalStatusReports.add(report);
		}

		return maritalStatusReports;
	}

	@RequestMapping(value = "/status-at-time-marriage/view/{year}/{month}/{applicantType}/{maritalStatus}", method = RequestMethod.GET)
	public String viewByMaritalStatus(@PathVariable final int year, @PathVariable final String month,
			@PathVariable final String applicantType, @PathVariable final String maritalStatus, final Model model)
			throws IOException, ParseException {
		List<MarriageRegistration> marriageRegistrations = marriageRegistrationReportsService
				.getByMaritalStatusDetails(year, month, applicantType, maritalStatus);
		model.addAttribute("marriageRegistrations", marriageRegistrations);
		model.addAttribute("applicantType", applicantType);
		return "status-timeofmrg-view";
	}
    
	}
