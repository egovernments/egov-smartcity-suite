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

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.mrs.application.MarriageConstants.BOUNDARY_TYPE;
import static org.egov.mrs.application.MarriageConstants.REVENUE_HIERARCHY_TYPE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.utils.DateUtils;
import org.egov.mrs.application.MarriageUtils;
import org.egov.mrs.application.reports.service.MarriageRegistrationReportsService;
import org.egov.mrs.domain.entity.ApplicationStatusResultForReport;
import org.egov.mrs.domain.entity.MaritalStatusReport;
import org.egov.mrs.domain.entity.MarriageCertificate;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.MarriageRegistration.RegistrationStatus;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.domain.entity.RegistrationCertificatesResultForReport;
import org.egov.mrs.domain.entity.RegistrationReportsSearchResult;
import org.egov.mrs.domain.entity.SearchModel;
import org.egov.mrs.domain.entity.SearchResult;
import org.egov.mrs.domain.enums.MaritalStatus;
import org.egov.mrs.masters.entity.MarriageAct;
import org.egov.mrs.masters.entity.MarriageRegistrationUnit;
import org.egov.mrs.masters.service.MarriageActService;
import org.egov.mrs.masters.service.MarriageRegistrationUnitService;
import org.egov.mrs.masters.service.ReligionService;
import org.egov.mrs.web.adaptor.ApplicationStatusResultReportJsonAdaptor;
import org.egov.mrs.web.adaptor.MaritalStatusReportJsonAdaptor;
import org.egov.mrs.web.adaptor.MarriageReIssueJsonAdaptor;
import org.egov.mrs.web.adaptor.MarriageRegistrationCertificateReportJsonAdaptor;
import org.egov.mrs.web.adaptor.MarriageRegistrationJsonAdaptor;
import org.egov.mrs.web.adaptor.MarriageRegistrationReportsJsonAdaptor;
import org.egov.mrs.web.adaptor.ReligionWiseReportJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    private static final String STATUS = "status";
    private static final String HUSBAND = "husband";
    private static final String DATA = "{ \"data\":";
    private static final String REISSUE = "reissue";
    private static final String MARRIAGE_REGISTRATIONS = "marriageRegistrations";
    private static final String APPLICANT_TYPE = "applicantType";
    private static final String YEARLIST = "yearlist";
    private static final String REGISTRATION = "registration";
    private static final String[] RANGES = new String[] { "0-18", "19-25",
            "26-30", "31-35", "36-40", "40-45", "46-50", "50-100" };
    private static final String KEY_AGE = "age";
    private static final String KEY_HUSBANDCOUNT = "husbandcount";
    private static final String KEY_WIFECOUNT = "wifecount";
    private static final String KEY_MONTH = "month";
    private static final String KEY_REGCOUNT = "Registrationcount";
    private static final String KEY_ACT = "MarriageAct";
    private static final String[] DAYRANGE = new String[] { "0-3", "4-6","7-9","10-12","13-15","16-20","20-25","26-30","31-365" };
    private static final String KEY_DAY = "days";
    private static final String KEY_REGISTRATIONCOUNT = "registrationcount";
    private static final String KEY_COLLECTIONAMOUNT = "totalcollection";
    @Autowired
    protected BoundaryService boundaryService;

    @Autowired
    private MarriageRegistrationReportsService marriageRegistrationReportsService;

    @Autowired
    private MarriageRegistrationUnitService marriageRegistrationUnitService;

    @Autowired
    private MarriageActService marriageActService;

    @Autowired
    private ReligionService religionService;
    @Autowired
    private MarriageUtils marriageUtils;
    
    @Autowired
    private CityService cityService;
    
    private final Map<Integer, String> monthMap = DateUtils
            .getAllMonthsWithFullNames();
    
    @ModelAttribute("zones")
    public List<Boundary> getZonesList() {
        return boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                        BOUNDARY_TYPE, REVENUE_HIERARCHY_TYPE);
    }
    @ModelAttribute("marriageRegistrationUnit")
    public List<MarriageRegistrationUnit> getMarriageRegistrationUnitList() {
        return marriageRegistrationUnitService.getActiveRegistrationunit();
    }
    

    @RequestMapping(value = "/registrationstatus", method = RequestMethod.GET)
    public String showReportForm(final Model model) {
        model.addAttribute(REGISTRATION, new MarriageRegistration());
        model.addAttribute(STATUS, RegistrationStatus.values());
        return "report-registrationstatus";
    }

    @RequestMapping(value = "/applicantionsstatus-count", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getByApplicationsStatusCount(final Model model,
            @ModelAttribute final MarriageRegistration registration)
            throws ParseException {
        final ArrayList<ApplicationStatusResultForReport> result = new ArrayList<>();
        
        final List<String[]> applnsStatusCount = marriageRegistrationReportsService
                .getCountOfApplnsStatusWise(registration.getStatus().getCode(), registration.getFromDate(), registration.getToDate(),registration.getMarriageRegistrationUnit(),registration);

        final Map<String, Map<String, String>> map = new HashMap<>();
        for (final Object[] category : applnsStatusCount)
            if (map.containsKey(category[0])) { //category[0] - Registration Unit,category[1]- Created,category[2]- Approved,category[2]-Registered,category[2]-Cancelled,category[2]-Rejected
                if (map.get(category[0]).containsKey(category[1]))
                    map.get(category[0]).put(String.valueOf(category[1]),
                            String.valueOf(category[2]));
                else {
                    final Map<String, String> subMap = new HashMap<>();
                    subMap.put(String.valueOf(category[1]),
                            String.valueOf(category[2]));
                    map.get(category[0]).put(String.valueOf(category[1]),
                            String.valueOf(category[2]));

                }
            } else {
                final Map<String, String> subMap = new HashMap<>();
                subMap.put(String.valueOf(category[1]),
                        String.valueOf(category[2]));
                map.put(String.valueOf(category[0]), subMap);

            }

        buildApplicationStatusWiseResult(result, map);
        
        return new StringBuilder(DATA)
                .append(toJSON(result, ApplicationStatusResultForReport.class,
                        ApplicationStatusResultReportJsonAdaptor.class)).append("}")
                .toString();
    }
    private void buildApplicationStatusWiseResult(final ArrayList<ApplicationStatusResultForReport> result,
            final Map<String, Map<String, String>> map) {
        for (final Entry<String, Map<String, String>> resMap : map.entrySet()) {
            ApplicationStatusResultForReport statusResultForReport = new ApplicationStatusResultForReport();
            statusResultForReport.setRegistrationUnit(resMap.getKey());
            buildApplicationStatusWiseInnerMapResult(resMap, statusResultForReport);
            result.add(statusResultForReport);
        }
    }
    private void buildApplicationStatusWiseInnerMapResult(final Entry<String, Map<String, String>> resMap,
            ApplicationStatusResultForReport statusResultForReport) {
        Integer count=0;
        for (final Entry<String, String> valuesMap : resMap.getValue().entrySet()) {
            if (MarriageRegistration.RegistrationStatus.CREATED.toString().equalsIgnoreCase(valuesMap.getKey())) {
                count = count+Integer.parseInt(valuesMap.getValue());
                statusResultForReport.setCreatedCount(valuesMap.getValue());
            }
            if (MarriageRegistration.RegistrationStatus.APPROVED.toString().equalsIgnoreCase(valuesMap.getKey())) {
                count=count+Integer.parseInt(valuesMap.getValue());
                statusResultForReport.setApprovedCount(valuesMap.getValue());
            }
            if (MarriageRegistration.RegistrationStatus.REGISTERED.toString().equalsIgnoreCase(valuesMap.getKey())) {
                count=count+Integer.parseInt(valuesMap.getValue());
                statusResultForReport.setRegisteredCount(valuesMap.getValue());
            }
            if (MarriageRegistration.RegistrationStatus.REJECTED.toString().equalsIgnoreCase(valuesMap.getKey())) {
                count=count+Integer.parseInt(valuesMap.getValue());
                statusResultForReport.setRejectedCount(valuesMap.getValue());
            }
            if (MarriageRegistration.RegistrationStatus.CANCELLED.toString().equalsIgnoreCase(valuesMap.getKey())) {
                count=count+Integer.parseInt(valuesMap.getValue());
                statusResultForReport.setCancelledCount(valuesMap.getValue());
            }
            statusResultForReport.setTotal(count);
        }
    }
    
    @RequestMapping(value = "/applicantionsstatus-count", method = RequestMethod.GET)
    public String showApplicationsStatusDetails(final Model model,@RequestParam(STATUS) String status,@RequestParam("registrationunit") String registrationUnit,
            @RequestParam("fromdate") Date fromDate,@RequestParam("todate") Date toDate) {
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute(STATUS, status);
        model.addAttribute("registrationUnit", registrationUnit);
        return "report-viewregistration-statusdetails";
    }
    
    @RequestMapping(value = "/registrationstatus", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String search(final Model model,
            @RequestParam(STATUS) String status,@RequestParam("registrationUnit") String registrationUnit,
            @RequestParam("fromDate") Date fromDate,@RequestParam("toDate") Date toDate)
            throws ParseException {
        final List<MarriageRegistration> searchResultList = marriageRegistrationReportsService
                .searchRegistrationByStatusForReport(registrationUnit,status,fromDate,toDate);
        return new StringBuilder(DATA)
                .append(toJSON(searchResultList, MarriageRegistration.class,
                        MarriageRegistrationJsonAdaptor.class)).append("}")
                .toString();
    }
    
    @RequestMapping(value = "/age-wise", method = RequestMethod.GET)
    public String newSearchForm(final Model model) {
        model.addAttribute(REGISTRATION, new MarriageRegistration());
        model.addAttribute(YEARLIST, getPreviousyears());
        return "report-registration-agewise";
    }

    @RequestMapping(value = "/age-wise", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchAgeWise(@RequestParam("year") final int year,
            final Model model, @ModelAttribute final MarriageRegistration registration)
            throws ParseException {

        final  Map<String, Integer> husbandAgeRangesCount = getCountByRange(marriageRegistrationReportsService
                .searchRegistrationOfHusbandAgeWise(year,registration));
        final Map<String, Integer> wifeAgeRangesCount = getCountByRange(marriageRegistrationReportsService
                .searchRegistrationOfWifeAgeWise(year,registration));

        final ArrayList<HashMap<String, Object>> result = new ArrayList<>();

        for (final String range : RANGES) {
            final HashMap<String, Object> rangeMap = new HashMap<>();
            rangeMap.put(KEY_AGE, range);
            rangeMap.put(
                    KEY_HUSBANDCOUNT,
                    husbandAgeRangesCount.get(range) != null ? husbandAgeRangesCount
                            .get(range) : 0);
            rangeMap.put(
                    KEY_WIFECOUNT,
                    wifeAgeRangesCount.get(range) != null ? wifeAgeRangesCount
                            .get(range) : 0);
            result.add(rangeMap);
        }

        final JsonArray jsonArray = (JsonArray) new Gson().toJsonTree(result,
                new TypeToken<List<HashMap<String, Object>>>() {

                    private static final long serialVersionUID = 5562709025385195886L;
                }.getType());

        final JsonObject response = new JsonObject();
        response.add("data", jsonArray);
        return response.toString();
    }
    
    public Map<String, Integer> getCountByRange(final List<String[]> inputs) {

        final HashMap<String, Integer> response = new HashMap<>();

        for (final Object[] input : inputs) {
             final String[] values =   Arrays.toString(input).replaceFirst("^\\[", "").replaceFirst("\\]$", "").split(","); // days,count -> [0] - age, [1] 
               
                final Integer age = Integer.parseInt(values[0]);
           
            for (final String range : RANGES)
                if (isInRange(range, age)) {
                    final int existingCount = response.get(range) != null ? response
                            .get(range) : 0;
                    response.put(range,
                            existingCount + Integer.valueOf(values[1].trim()));
                    break;
                }
        }

        return response;
    }

    private boolean isInRange(final String ranges, final Integer input) {
        final String[] range = ranges.split("-");
        return input >= Integer.valueOf(range[0]) && input <= Integer
                .valueOf(range[1]);
    }

    @RequestMapping(value = "/age-wise/view", method = RequestMethod.GET)
    public String viewAgeWiseDetails(@ModelAttribute final MarriageRegistration registration,
    		@RequestParam(value="regunit",required=false) final String regunit,@RequestParam("year") final int year,
    		@RequestParam("applicantType") final String applicantType,
    		@RequestParam("agerange") final String ageRange, final Model model)
            throws ParseException {
        final List<MarriageRegistration> marriageRegistrations = marriageRegistrationReportsService
                .getAgewiseDetails(registration,regunit,ageRange,year, applicantType);
        model.addAttribute(MARRIAGE_REGISTRATIONS, marriageRegistrations);
        model.addAttribute(APPLICANT_TYPE, applicantType);
        
        return "marriage-agewise-view";
    }
    @RequestMapping(value = "/certificatedetails", method = RequestMethod.GET)
    public String searchCertificatesForReport(final Model model) {
        model.addAttribute("certificate", new MarriageCertificate());
        return "registration-certificates-report";
    }

    @RequestMapping(value = "/certificatedetails", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchApprovedMarriageRecords(final Model model,
            @ModelAttribute final MarriageCertificate certificate)
            throws ParseException {
        final List<RegistrationCertificatesResultForReport> regCertificateResult = new ArrayList<>();
        final List<Object[]> searchResultList = marriageRegistrationReportsService
                .searchMarriageRegistrationsForCertificateReport(certificate);
        for (final Object[] objects : searchResultList) {
            final RegistrationCertificatesResultForReport certificatesResultForReport = new RegistrationCertificatesResultForReport();
            certificatesResultForReport
                    .setRegistrationNo(objects[0] != null ? objects[0].toString() : "");
            certificatesResultForReport
                    .setDateOfMarriage(objects[1].toString());
            certificatesResultForReport.setRegistrationDate(objects[2]
                    .toString());
            certificatesResultForReport.setRejectReason(objects[3] != null ? objects[3].toString() : "");
            certificatesResultForReport
                    .setCertificateNo(objects[4] != null ? objects[4]
                            .toString() : "");
            certificatesResultForReport.setCertificateType(objects[5]
                    .toString());
            certificatesResultForReport.setCertificateDate(objects[6]
                    .toString());
            certificatesResultForReport.setZone(objects[7].toString());
            certificatesResultForReport.setHusbandName(objects[8].toString());
            certificatesResultForReport.setWifeName(objects[9].toString());
            certificatesResultForReport.setId(Long.valueOf(objects[10]
                    .toString()));
            regCertificateResult.add(certificatesResultForReport);
        }
        return new StringBuilder(DATA)
                .append(toJSON(regCertificateResult,
                        RegistrationCertificatesResultForReport.class,
                        MarriageRegistrationCertificateReportJsonAdaptor.class))
                .append("}").toString();
    }

    @RequestMapping(value = "/status-at-time-marriage", method = RequestMethod.GET)
    public String getStatusAtTimeOfMarriage(final Model model) {
        model.addAttribute(REGISTRATION, new MarriageRegistration());
        model.addAttribute("maritalStatusList",
                Arrays.asList(MaritalStatus.values()));
        model.addAttribute(YEARLIST, getPreviousyears());
        return "statustime-ofmarriage-report";
    }

    @RequestMapping(value = "/status-at-time-marriage", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchStatusAtTimeOfMarriage(
            @RequestParam("fromDate") final Date fromDate, @RequestParam("toDate") final Date toDate,@RequestParam("maritalStatus") final String maritalStatus,@RequestParam(APPLICANT_TYPE) final String applicantType, final Model model,
            @ModelAttribute final MarriageRegistration registration)
            throws ParseException {
        final List<MaritalStatusReport> maritalStatusReports = new ArrayList<>();
        if("Husband".equals(applicantType)){
        maritalStatusReports.addAll(putRecordsIntoHashMapByMonth(
                marriageRegistrationReportsService
                        .getHusbandCountByMaritalStatus(fromDate,toDate,maritalStatus,registration), HUSBAND));
        }else if("Wife".equals(applicantType)){
        maritalStatusReports.addAll(putRecordsIntoHashMapByMonth(
                marriageRegistrationReportsService
                        .getWifeCountByMaritalStatus(fromDate,toDate,maritalStatus,registration), "wife"));
        }else {
            maritalStatusReports.addAll(putRecordsIntoHashMapByMonth(
                    marriageRegistrationReportsService
                            .getHusbandCountByMaritalStatus(fromDate,toDate,maritalStatus,registration), HUSBAND));
            maritalStatusReports.addAll(putRecordsIntoHashMapByMonth(
                    marriageRegistrationReportsService
                            .getWifeCountByMaritalStatus(fromDate,toDate,maritalStatus,registration), "wife"));
        }
        return new StringBuilder(DATA)
                .append(toJSON(maritalStatusReports, MaritalStatusReport.class,
                        MaritalStatusReportJsonAdaptor.class)).append("}")
                .toString();
    }

    public List<MaritalStatusReport> putRecordsIntoHashMapByMonth(
            final List<String[]> recordList, final String applicantType) {
        final Map<String, Map<String, String>> map = new HashMap<>();
        for (final Object[] category : recordList)
            if (map.containsKey(category[1])) { //category[0] - Marital status,category[1]- Month,category[2]- Count
                if (map.get(category[1]).containsKey(category[0]))
                    map.get(category[1]).put(String.valueOf(category[0]),
                            String.valueOf(category[2]));
                else {
                    final Map<String, String> subMap = new HashMap<>();
                    subMap.put(String.valueOf(category[0]),
                            String.valueOf(category[2]));
                    map.get(category[1]).put(String.valueOf(category[0]),
                            String.valueOf(category[2]));

                }
            } else {
                final Map<String, String> subMap = new HashMap<>();
                subMap.put(String.valueOf(category[0]),
                        String.valueOf(category[2]));
                map.put(String.valueOf(category[1]), subMap);

                map.get(category[1]).put(APPLICANT_TYPE, applicantType);
            }
        final List<MaritalStatusReport> maritalStatusReports = new ArrayList<>();
        buildMaritalStatusResult(applicantType, map, maritalStatusReports);

        return maritalStatusReports;
    }
    private void buildMaritalStatusResult(final String applicantType, final Map<String, Map<String, String>> map,
            final List<MaritalStatusReport> maritalStatusReports) {
        for (final Entry<String, Map<String, String>> resMap : map.entrySet()) {
            final MaritalStatusReport report = new MaritalStatusReport();
            report.setMonth(resMap.getKey());
            buildMaritalStatusWiseInnerMapResult(applicantType, resMap, report);
            maritalStatusReports.add(report);
        }
    }
    private void buildMaritalStatusWiseInnerMapResult(final String applicantType, final Entry<String, Map<String, String>> resMap,
             final MaritalStatusReport report) {
        int count = 0;
        for (final Entry<String, String> valuesMap : resMap.getValue().entrySet()) {
            if (HUSBAND.equalsIgnoreCase(valuesMap.getValue())
                    || "wife".equalsIgnoreCase(valuesMap.getValue()))
                report.setApplicantType(applicantType);

            if (MaritalStatus.Married.toString().equalsIgnoreCase(valuesMap.getKey())) {
                count = count+Integer.parseInt(valuesMap.getValue());
                report.setMarried(valuesMap.getValue() != null ? valuesMap
                        .getValue() : "0");
            }else if (MaritalStatus.Unmarried.toString().equalsIgnoreCase(valuesMap.getKey())) {
                count = count+Integer.parseInt(valuesMap.getValue());
                report.setUnmarried(valuesMap.getValue() != null ? valuesMap
                        .getValue() : "0");
            } else if (MaritalStatus.Widower.toString().equalsIgnoreCase(valuesMap.getKey())) {
                count = count+Integer.parseInt(valuesMap.getValue());
                report.setWidower(valuesMap.getValue() != null ? valuesMap
                        .getValue() : "0");
            } else if (MaritalStatus.Divorced.toString().equalsIgnoreCase(valuesMap.getKey())) {
                count = count+Integer.parseInt(valuesMap.getValue());
                report.setDivorced(valuesMap.getValue() != null ? valuesMap
                        .getValue() : "0");
            }
            report.setTotal(count);
        }
    }

    @RequestMapping(value = "/status-at-time-marriage/view", method = RequestMethod.GET)
    public String viewByMaritalStatus(@ModelAttribute final MarriageRegistration registration,
    		@RequestParam(value="regunit",required=false) final String regunit,@RequestParam(APPLICANT_TYPE) final String applicantType,
            @RequestParam("maritalStatus") final String maritalStatus,@RequestParam("fromDate") final Date fromDate,
            @RequestParam("toDate") final Date toDate, final Model model)
            throws ParseException {
        final List<MarriageRegistration> marriageRegistrations = marriageRegistrationReportsService
                .getByMaritalStatusDetails(registration,regunit,applicantType,
                        maritalStatus,fromDate,toDate);
        model.addAttribute(MARRIAGE_REGISTRATIONS, marriageRegistrations);
        model.addAttribute(APPLICANT_TYPE, applicantType);
        return "status-timeofmrg-view";
    }

    @RequestMapping(value = "/datewiseregistration", method = RequestMethod.GET)
    public String showDatewiseReportForm(final Model model) {
        model.addAttribute(REGISTRATION, new MarriageRegistration());
        model.addAttribute(STATUS, RegistrationStatus.values());
        return "report-datewiseregsitration";
    }

    @RequestMapping(value = "/datewiseregistration", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String showDatewiseReportresult(final Model model,
            @ModelAttribute final MarriageRegistration registration)
            throws ParseException {
        final List<MarriageRegistration> searchResultList = marriageRegistrationReportsService
                .searchRegistrationBydate(registration);
        return new StringBuilder(DATA)
                .append(toJSON(searchResultList, MarriageRegistration.class,
                        MarriageRegistrationJsonAdaptor.class)).append("}")
                .toString();
    }

    @RequestMapping(value = "/monthwiseregistration", method = RequestMethod.GET)
    public String showMonthwiseReportForm(final Model model) {
        model.addAttribute(REGISTRATION, new MarriageRegistration());
        model.addAttribute(STATUS, RegistrationStatus.values());
        return "report-monthwiseregistration";
    }

    @RequestMapping(value = "/monthly-applications-count", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getMonthlyApplicationsCount(final Model model,
            @ModelAttribute final MarriageRegistration registration)
            throws ParseException {
        final ArrayList<HashMap<String, Object>> result = new ArrayList<>();
        final List<String[]> applnsCount = marriageRegistrationReportsService
                .getCountOfApplications(registration);
       
        final Map<String, Map<String, String>> map = new HashMap<>();
        for (final Object[] input : applnsCount) {
            final String[] values =   Arrays.toString(input).replaceFirst("^\\[", "").replaceFirst("\\]$", "").split(","); // [0] -> applicationtype  - [1] -> count 
            final Integer count = Integer.valueOf(values[1].trim());// count
            if(map.containsKey(values[0])){
                map.get(values[0]).put(String.valueOf(values[3].trim()),
                        String.valueOf(count));
                        }
            else{
                final Map<String, String> subMap = new HashMap<>();
                subMap.put("registrationunit", values[0].trim());
                subMap.put(values[3].trim(),  String.valueOf(count));
                subMap.put(KEY_MONTH, values[2].trim());
                map.put(String.valueOf(values[0].trim()), subMap);
            }
        }
        
        buildMonthlyApplicationsCountResult(result, map);
        final JsonArray jsonArray = (JsonArray) new Gson().toJsonTree(result,
                new TypeToken<List<HashMap<String, Integer>>>() {

                    /**
             *
             */
                    private static final long serialVersionUID = -3045535969083515053L;
                }.getType());

        final JsonObject response = new JsonObject();
        response.add("data", jsonArray);
        return response.toString();
    }
    private void buildMonthlyApplicationsCountResult(final ArrayList<HashMap<String, Object>> result,
            final Map<String, Map<String, String>> map) {
        for (final Entry<String, Map<String, String>> resMap : map.entrySet()) {
            final HashMap<String, Object> resultMap = new HashMap<>();
            boolean regExist = true;
            boolean reissueExist = true;
            if(!resMap.getValue().containsKey(REGISTRATION)){
                regExist = false;
            }
            
            if(!resMap.getValue().containsKey(REISSUE)){
                reissueExist = false;
            }
            
            resultMap.put("registrationunit", resMap.getKey());
            buildMonthlyApplicationsInnerMapResult(resMap, resultMap, regExist, reissueExist);
            result.add(resultMap);
        }
    }
    private void buildMonthlyApplicationsInnerMapResult(final Entry<String, Map<String, String>> resMap,
            final HashMap<String, Object> resultMap, boolean regExist, boolean reissueExist) {
        Integer count = 0;
        for (final Entry<String, String> valuesMap : resMap.getValue().entrySet()) {
            if(regExist) {
                if(REGISTRATION.equalsIgnoreCase(valuesMap.getKey().trim())){
                    count = count+Integer.parseInt(valuesMap.getValue());
                    resultMap.put(REGISTRATION, valuesMap.getValue());
                }
            } else {
                resultMap.put(REGISTRATION, 0);
            }
            if(reissueExist) {
            if(REISSUE.equalsIgnoreCase(valuesMap.getKey().trim())){
                count = count+Integer.parseInt(valuesMap.getValue());
                resultMap.put(REISSUE, valuesMap.getValue());
            }
            }else {
                resultMap.put(REISSUE, 0);
            }
            if(KEY_MONTH.equalsIgnoreCase(valuesMap.getKey().trim())){
                resultMap.put(KEY_MONTH, valuesMap.getValue());
            }
            resultMap.put("total", count);
        }
    }
    
    
    @RequestMapping(value = "/monthwisefundcollection", method = RequestMethod.GET)
    public String showFundColllectionReportForm(final Model model) {
        model.addAttribute(REGISTRATION, new MarriageRegistration());
        model.addAttribute(YEARLIST, getPreviousyears());
        model.addAttribute(STATUS, RegistrationStatus.values());
        return "report-monthwisefundcollection";
    }

    @RequestMapping(value = "/monthly-fund-collection", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getMonthlyFundCollection(@RequestParam("year") final String year, final Model model,
            @ModelAttribute final MarriageRegistration registration)
            throws ParseException {
        final ArrayList<HashMap<String, String>> result = new ArrayList<>();
        final List<String[]> totalCollection = marriageRegistrationReportsService
                .getMonthwiseFundCollected(registration, year);
        if (year != null && registration.getMonth_year() != null && registration.getMonth_year().substring(3).equals(year)) {
            final HashMap<String, String> map = new HashMap<>();
            for (final Object[] input : totalCollection) {
                final String[] values = Arrays.toString(input).replaceFirst("^\\[", "").replaceFirst("\\]$", "").split(",");
                map.put(KEY_MONTH, values[2].trim());
                map.put("totalcollection", values[1].trim());
                result.add(map);
            }
        }
        else if (year != null && registration.getMonth_year() == null) {
            final HashMap<String, Integer> registrationCount = getFundBymonth(totalCollection);
            for (final Map.Entry<Integer, String> monthname : monthMap.entrySet()) {
                final HashMap<String, String> monthRegMap = new HashMap<>();
                monthRegMap.put(KEY_MONTH, monthname.getValue());
                monthRegMap
                        .put(KEY_COLLECTIONAMOUNT,
                                registrationCount.get(monthname.getValue()) != null ? String.valueOf(registrationCount
                                        .get(monthname.getValue())) : "0");
                result.add(monthRegMap);
            }
        }
        final JsonArray jsonArray = (JsonArray) new Gson().toJsonTree(result,
                new TypeToken<List<HashMap<String, Integer>>>() {
                    private static final long serialVersionUID = -3045535969083515053L;
                }.getType());
        final JsonObject response = new JsonObject();
        response.add("data", jsonArray);
        return response.toString();
    }

    private HashMap<String, Integer> getFundBymonth(final List<String[]> inputs) {
        final HashMap<String, Integer> response = new HashMap<>();
        for (final Object[] input : inputs) {
            final String[] values = Arrays.toString(input).replaceFirst("^\\[", "").replaceFirst("\\]$", "").split(",");
            final Integer month = Double.valueOf(values[0]).intValue();
            for (final Map.Entry<Integer, String> monthname : monthMap.entrySet())
                if (month.equals(monthname.getKey())) {
                    final int existingCount = response.get(monthname) != null ? response
                            .get(monthname) : 0;
                    response.put(monthname.getValue(),
                            existingCount + Double.valueOf(values[1]).intValue());
                }
        }
        return response;
    }
    
    @RequestMapping(value = "/show-applications-details", method = RequestMethod.GET)
    public String showMonthlyApplicationDetails(final Model model,
            @RequestParam(KEY_MONTH) String month,@RequestParam("regunit") String  registrationUnit,
            @RequestParam("applicationType") String applicationType)
            throws ParseException {
        model.addAttribute(REGISTRATION, new MarriageRegistration());
        model.addAttribute(KEY_MONTH, month);
        model.addAttribute("registrationUnit", registrationUnit.replaceAll("[^a-zA-Z0-9]", " "));
        model.addAttribute("applicationType", applicationType);
        
        return "show-monthlyapplns-details";
    }
    
    @RequestMapping(value = "/monthwiseregistration", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getMonthlyApplicationDetailsResult(final Model model,  @ModelAttribute final MarriageRegistration registration,
            @RequestParam(KEY_MONTH) String month,@RequestParam("regunit") String  registrationUnit,
           @RequestParam("applicationType") String applicationType)
            throws ParseException {
        if("registration".equalsIgnoreCase(applicationType)){
            final List<MarriageRegistration> searchResultList = marriageRegistrationReportsService
                    .searchRegistrationBymonth(registration,month,registrationUnit);
            return new StringBuilder(DATA)
                    .append(toJSON(searchResultList, MarriageRegistration.class,
                            MarriageRegistrationJsonAdaptor.class)).append("}")
                    .toString();
        }else {
            final List<ReIssue> searchResultList = marriageRegistrationReportsService
                    .searchReissueBymonth(registration,month, registrationUnit);
            return new StringBuilder(DATA)
                    .append(toJSON(searchResultList, ReIssue.class,
                            MarriageReIssueJsonAdaptor.class)).append("}")
                    .toString();
        }
        
    }
    @RequestMapping(value = "/actwiseregistration", method = RequestMethod.GET)
    public String showActwiseReportForm(final Model model) {
        model.addAttribute(REGISTRATION, new MarriageRegistration());
        model.addAttribute("acts", marriageActService.getActs());
        model.addAttribute(YEARLIST, getPreviousyears());
        return "report-actwiseregistration";
    }

    @RequestMapping(value = "/actwiseregistration", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String showActwiseReportresult(
            @RequestParam("year") final int year,
            @ModelAttribute final MarriageRegistration registration)
            throws ParseException {
        final JsonObject response = new JsonObject();
        List<String[]> regcount;
        HashMap<String, Integer> registrationCount;
        JsonArray jsonArray;
        if (registration.getMarriageAct() != null
                && registration.getMarriageAct().getId() != null) {
            regcount = marriageRegistrationReportsService
                    .searchRegistrationActWise(registration, year);
            registrationCount = getCountBymonth(regcount);
            final ArrayList<HashMap<String, Object>> result = new ArrayList<>();
            for (final Map.Entry<Integer, String> monthname : monthMap.entrySet()) {

                final HashMap<String, Object> monthRegMap = new HashMap<>();
                monthRegMap.put(KEY_MONTH, monthname.getValue());
                monthRegMap
                        .put(KEY_REGCOUNT,
                                registrationCount.get(monthname.getValue()) != null ? registrationCount
                                        .get(monthname.getValue()) : 0);
                result.add(monthRegMap);

            }

            jsonArray = (JsonArray) new Gson().toJsonTree(result,
                    new TypeToken<List<HashMap<String, Object>>>() {

                        /**
                 *
                 */
                        private static final long serialVersionUID = -3045535969083515053L;
                    }.getType());

            response.add("data", jsonArray);
        }

        else {
            regcount = marriageRegistrationReportsService
                    .searchRegistrationMrActWise(year,registration);
            registrationCount = getCountByAct(regcount);
            final ArrayList<HashMap<String, Object>> result = new ArrayList<>();
            final List<MarriageAct> actList = marriageActService.getActs();
            for (final MarriageAct actName : actList) {

                final HashMap<String, Object> actRegMap = new HashMap<>();
                actRegMap.put(KEY_ACT, actName.getName());
                actRegMap
                        .put(KEY_REGCOUNT,
                                registrationCount.get(actName.getName()) != null ? registrationCount
                                        .get(actName.getName()) : 0);
                result.add(actRegMap);

            }

            jsonArray = (JsonArray) new Gson().toJsonTree(result,
                    new TypeToken<List<HashMap<String, Object>>>() {

                        /**
                 *
                 */
                        private static final long serialVersionUID = -3419248131719029680L;
                    }.getType());
            response.add("data", jsonArray);
        }
        return response.toString();
    }

    private HashMap<String, Integer> getCountBymonth(final List<String[]> inputs) {

        final HashMap<String, Integer> response = new HashMap<>();

        for (final Object[] input : inputs) {
            final String[] values =   Arrays.toString(input).replaceFirst("^\\[", "").replaceFirst("\\]$", "").split(","); // month -> [0] - count, [1] -

            final Integer month = Double.valueOf(values[0]).intValue();

            for (final Map.Entry<Integer, String> monthname : monthMap.entrySet())
                if (month.equals(monthname.getKey())) {
                    final int existingCount = response.get(monthname) != null ? response
                            .get(monthname) : 0;
                    response.put(monthname.getValue(),
                            existingCount + Integer.valueOf(values[1].trim()));

                }
        }

        return response;
    }

    private HashMap<String, Integer> getCountByAct(final List<String[]>  inputs) {

        final HashMap<String, Integer> response = new HashMap<>();

        for (final Object[] input : inputs) {
            final String[] values =   Arrays.toString(input).replaceFirst("^\\[", "").replaceFirst("\\]$", "").split(","); // days,count -> [0] - age, [1] -
            final String actName = values[0];
            final List<MarriageAct> actList = marriageActService.getActs();
            for (final MarriageAct act : actList)
                if (actName.equals(act.getName())) {
                    final int existingCount = response.get(actName) != null ? response
                            .get(actName) : 0;
                    response.put(actName,
                            existingCount + Integer.valueOf(values[1].trim()));

                    break;
                }
        }

        return response;
    }

    @RequestMapping(value = "/religionwiseregistration", method = RequestMethod.GET)
    public String showRegionwiseReportForm(final Model model) {
        model.addAttribute(REGISTRATION, new MarriageRegistration());
        model.addAttribute("religions", religionService.getReligions());
        model.addAttribute(YEARLIST, getPreviousyears());
        return "religionwise-report";
    }

    public List<Integer> getPreviousyears() {
        final List<Integer> previousyears = new ArrayList<>();
        final int currentyear = Calendar.getInstance().get(Calendar.YEAR);
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2009);
        int startyear = cal.get(Calendar.YEAR);

        for (int i = startyear; i < currentyear; i++) {
            previousyears.add(startyear + 1);
            startyear++;
        }
        Collections.sort(previousyears, Collections.reverseOrder());
        return previousyears;
    }

    @RequestMapping(value = "/religionwiseregistration", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String showRegionwiseReportresult(
            @RequestParam("year") final int year, final Model model,
            @ModelAttribute final MarriageRegistration registration)
            throws ParseException {
        final List<MarriageRegistration> searchResultList = marriageRegistrationReportsService
                .searchRegistrationByreligion(registration, year);
        return new StringBuilder(DATA)
                .append(toJSON(searchResultList, MarriageRegistration.class,
                        MarriageRegistrationJsonAdaptor.class)).append("}")
                .toString();
    }

    @RequestMapping(value = "/act-wise/view/{year}/{MarriageAct}", method = RequestMethod.GET)
    public String viewActWiseDetails(@PathVariable final int year, 
            @PathVariable final String MarriageAct, final Model model)
            throws  ParseException {
        final List<MarriageRegistration> marriageRegistrations = marriageRegistrationReportsService
                .getActwiseDetails(year, MarriageAct);
        model.addAttribute(MARRIAGE_REGISTRATIONS, marriageRegistrations);
        return "marriage-actwise-view";
    }

    @RequestMapping(value = "/act-wise/view/{year}/{month}/{actid}", method = RequestMethod.GET)
    public String viewActWiseDetails(@PathVariable final int year,
            @PathVariable final String month, @PathVariable final Long actid,@ModelAttribute final MarriageRegistration registration,
            final Model model) throws ParseException {
        final Date date = new SimpleDateFormat("MMM").parse(month);
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int months = cal.get(Calendar.MONTH) + 1;
        final List<MarriageRegistration> marriageRegistrations = marriageRegistrationReportsService
                .getmonthWiseActDetails(year, months, actid);
        model.addAttribute(MARRIAGE_REGISTRATIONS, marriageRegistrations);
        return "marriage-actwise-view";
    }
    
    @RequestMapping(value = "/ageing-report", method = RequestMethod.GET)
    public String ageingReportForm(final Model model) {
        model.addAttribute(REGISTRATION, new MarriageRegistration());
        model.addAttribute(YEARLIST, getPreviousyears());
        return "ageing-report";
    }
    
    @RequestMapping(value = "/ageing-report", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String agiengReport(@RequestParam("year") final int year,
            final Model model, @ModelAttribute final MarriageRegistration registration)
            throws ParseException {

        final Map<String, Integer> registrationcount = getCountByDays(marriageRegistrationReportsService
                .searchRegistrationbyDays(year,registration));
        final ArrayList<HashMap<String, Object>> result = new ArrayList<>();

        for (final String range : DAYRANGE) {
            final HashMap<String, Object> rangeMap = new HashMap<>();
            rangeMap.put(KEY_DAY, range);
            rangeMap.put(
                    KEY_REGISTRATIONCOUNT,
                    registrationcount.get(range) != null ? registrationcount
                            .get(range) : 0);
            
            result.add(rangeMap);
        }
        final JsonArray jsonArray = (JsonArray) new Gson().toJsonTree(result,
                new TypeToken<List<HashMap<String, Object>>>() {

                    /**
             *
             */
                    private static final long serialVersionUID = 5562709025385195886L;
                }.getType());

        final JsonObject response = new JsonObject();
        response.add("data", jsonArray);
        return response.toString();
    }
        
    public Map<String, Integer> getCountByDays(final List<String[]> inputs) {

            final HashMap<String, Integer> response = new HashMap<>();

            for (final Object[] input : inputs) {
               
                final String[] values =   Arrays.toString(input).replaceFirst("^\\[", "").replaceFirst("\\]$", "").split(","); // days,count -> [0] - age, [1] -
                // count
                final Integer days = Double.valueOf(values[0]).intValue();
                
                for (final String range : DAYRANGE)
                    if (isInRange(range, days)) {
                        final int existingCount = response.get(range) != null ? response
                                .get(range) : 0;
                        response.put(range,
                                existingCount + Integer.valueOf(values[1].trim()));
                        break;
                }
            }

            return response;
        }
        
        @RequestMapping(value = "/ageing-report/view/{year}/{dayRange}", method = RequestMethod.GET)
        public String viewAgeingRegDetails(@PathVariable final int year,
                @PathVariable final String dayRange, final Model model)
                throws ParseException {
            model.addAttribute("year", year);
            model.addAttribute("dayRange", dayRange);
            return "ageingreport-view";
        }
        
        @RequestMapping(value = "/ageing-report/view/", method = RequestMethod.POST,produces = MediaType.TEXT_PLAIN_VALUE)
        @ResponseBody
        public String getAgeingRegDetails(@RequestParam("year") final int year,
                @RequestParam("dayRange") final String dayRange, final Model model)
                throws ParseException {
            List<RegistrationReportsSearchResult> reportsSearchResults = new ArrayList<>();
            final List<Object[]> marriageRegistrations = marriageRegistrationReportsService
                    .getAgeingRegDetails(dayRange,year);
            RegistrationReportsSearchResult reportsSearchResult = new RegistrationReportsSearchResult();
            for (Object[] mrgReg : marriageRegistrations) {
                reportsSearchResult.setApplicationNo(mrgReg[0].toString());
                reportsSearchResult.setRegistrationNo(mrgReg[1].toString());
                reportsSearchResult.setApplicationType(mrgReg[9].toString());
                reportsSearchResult.setHusbandName(mrgReg[2].toString());
                reportsSearchResult.setWifeName(mrgReg[3].toString());
                reportsSearchResult.setDateOfMarriage(mrgReg[4].toString());
                reportsSearchResult.setRegistrationDate(mrgReg[5].toString());
                reportsSearchResult.setPlaceOfMarriage(mrgReg[6].toString());
                reportsSearchResult.setZone(mrgReg[7].toString());
                reportsSearchResult.setStatus(mrgReg[8].toString());
                reportsSearchResult.setUserName(marriageUtils.getApproverName(Long.valueOf(mrgReg[10].toString())));
                reportsSearchResult.setPendingAction(mrgReg[11].toString());
                reportsSearchResults.add(reportsSearchResult);
            }
            return new StringBuilder(DATA)
                    .append(toJSON(reportsSearchResults, RegistrationReportsSearchResult.class,
                            MarriageRegistrationReportsJsonAdaptor.class)).append("}")
                    .toString();
        }
    
        @RequestMapping(value = "/religion-wise-registrations-report", method = RequestMethod.GET)
        public String showfagStatutoryReport(final Model model) {
            model.addAttribute("searchRequest", new SearchModel());
            model.addAttribute(YEARLIST, getPreviousyears());
            return "religion-wise-statutory-report";
        }

        @RequestMapping(value = "/religion-wise-registrations-report", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
        @ResponseBody
        public String getAgeWiseStatutoryReportDetails(@ModelAttribute final SearchModel searchRequest,
                final Model model)
                throws ParseException {
            final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
            if (cityWebsite != null)
                searchRequest.setUlbName(cityWebsite.getName());
            List<SearchResult> religionsSearchResults = marriageRegistrationReportsService.getUlbWiseReligionDetails(searchRequest);
            return new StringBuilder(DATA)
                    .append(toJSON(religionsSearchResults, SearchResult.class,
                            ReligionWiseReportJsonAdaptor.class)).append("}")
                    .toString();
        }
        
        
        @RequestMapping(value = "/print-religion-wise-details", method = RequestMethod.GET)
        @ResponseBody
        public ResponseEntity<byte[]> printReligionWiseReport(HttpServletRequest request,
                        @RequestParam("year") final int year, final Model model, final HttpSession session) {
            SearchModel searchRequest = new SearchModel();
            searchRequest.setYear(year);
            List<SearchResult> religionsSearchResults = marriageRegistrationReportsService.getUlbWiseReligionDetails(searchRequest);
            return marriageRegistrationReportsService.generateReligionWiseReport(year,religionsSearchResults, session, request);
            
        }
}
