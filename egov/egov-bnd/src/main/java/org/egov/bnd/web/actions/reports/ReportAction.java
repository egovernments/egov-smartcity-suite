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
package org.egov.bnd.web.actions.reports;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.displaytag.pagination.PaginatedList;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.egov.bnd.model.FeeCollection;
import org.egov.bnd.model.RegistrationUnit;
import org.egov.bnd.model.ReportDetails;
import org.egov.bnd.model.ViewBirthDeathRegistration;
import org.egov.bnd.services.reports.PaymentReportService;
import org.egov.bnd.services.reports.ReportService;
import org.egov.bnd.utils.BndConstants;
import org.egov.bnd.utils.BndDateUtils;
import org.egov.bnd.web.actions.common.BndCommonAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Namespace("/reports")
@ParentPackage("egov")
@Transactional(readOnly = true)
public class ReportAction extends BndCommonAction {

    private static final long serialVersionUID = 6161737564229417153L;
    private static final String SUMMARYMONTHLYREPORT = "summaryMonthlyReport";
    private static final String AGGREGATEREPORT = "aggregateReport";
    private static final String DELAYEDREGREPORT = "delayedRegistrationReport";
    private static final String YEARWISEREPORT = "yearWiseReport";
    private static final String REGUNITWISEREPORT = "regUnitWiseReport";
    private static final String EFFICIENCYREPORT = "efficiencyReport";
    private static final String HOSPITALREGREPORT = "hospitalRegReport";
    private static final String CANCELLEDREPORT = "cancelledReport";
    private static final String CERTIFICATEREPORT = "certificateReport";
    private static final String NAMEINCLUSIONSTATUSREPORT = "nameInclusionReport";
    private static final String UPDATEDREGISTRATIONREPORT = "updatedRegistrationReport";

    private static final String MONTH = "MONTH";
    private static final String YEAR = "YEAR";
    private static final String REGTYPE = "REGTYPE";
    private static final String RESULT = "result";
    private static final String BIRTH = "Birth";
    private static final Logger LOGGER = Logger.getLogger(ReportAction.class);

    private ViewBirthDeathRegistration birthdeathview = new ViewBirthDeathRegistration();
    private Integer regYear;
    private Integer month;
    private Integer establishment;
    private Integer status;
    private String regType;
    private String reportTypeMap;
    private Long regUnitId;
    private RegistrationUnit regUnit;
    private String reportType;
    private String subTitle;
    private String searchMode;
    private Date fromDate;
    private Date toDate;
    private final Date todaysDate = new Date();
    private String regNo;
    private ReportService viewReportService;
    private PaymentReportService paymentReportService;
    private Map<String, Object> resultMap = new HashMap<String, Object>();
    private PaginatedList birthDeathList;
    private final SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat yearformat = new SimpleDateFormat("yyyy");
    private List<ReportDetails> reportdetailsList = new ArrayList<ReportDetails>();
    private final List<FeeCollection> feeCollectionList = new ArrayList<FeeCollection>();
    private ReportDetails hospitalreportdetail;
    private ReportDetails nonhospitalreportdetail;
    private ReportDetails totalReportdetail;
    private FeeCollection feeCollection;
    private Integer noofcopy;
    // used incase of regunitwise report
    private Integer totalBirthRegforMonth = 0;
    private Integer totalDeathRegforMonth = 0;
    private Integer totalBirthRegforMonthprogressive = 0;
    private Integer totalDeathRegforMonthprogressive = 0;
    private Integer totalInfantforMonth = 0;
    private Integer totalInfantforMonthprogressive = 0;
    private Integer totalCertifiedforMonth = 0;
    private Integer totalCertifiedforMonthprogressive = 0;
    private Integer totalDeliverydeathforMonth = 0;
    private Integer totalDeliverydeathforMonthprogressive = 0;

    @Override
    public ViewBirthDeathRegistration getModel() {
        return birthdeathview;
    }

    @Action(value = "/report-delayedRegistration", results = { @Result(name = DELAYEDREGREPORT, type = "dispatcher") })
    public String delayedRegistration() {
        LOGGER.debug("Started delayedRegistration method");
        setReportType(DELAYEDREGREPORT);
        LOGGER.debug("Completed delayedRegistration method");
        return DELAYEDREGREPORT;

    }

    @Override
    @Transactional
    public void prepare() {
        super.prepare();
        final List<RegistrationUnit> registrationUnitList = bndCommonService.getRegistrationUnit();
        addDropdownData("registrationUnitList", registrationUnitList);
        addDropdownData("establishmentList", bndCommonService.getHospitalName());
    }

    @Action(value = "/report-yearlyWiseReport", results = { @Result(name = YEARWISEREPORT, type = "dispatcher") })
    public String yearlyWiseReport() {
        LOGGER.debug("Started yearlyWiseReport method");
        setReportType(YEARWISEREPORT);
        LOGGER.debug("Completed yearlyWiseReport method");
        return YEARWISEREPORT;
    }

    @Action(value = "/report-aggregateReport", results = { @Result(name = AGGREGATEREPORT, type = "dispatcher") })
    public String aggregateReport() {

        LOGGER.debug("Started aggregateReport method");
        setRegType(BIRTH);
        setReportTypeMap("BOTH");
        setReportType(AGGREGATEREPORT);
        LOGGER.debug("Completed aggregateReport method");
        return AGGREGATEREPORT;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    @Action(value = "/report-aggregateReportResult", results = { @Result(name = AGGREGATEREPORT, type = "dispatcher") })
    public String aggregateReportResult() {

        LOGGER.debug("Started aggregateReportResult method");
        final HashMap<String, Object> InputMap = createMap();
        prepareMap(InputMap);
        if (fromDate != null)
            InputMap.put("FROMDATE", fromDate);
        if (toDate != null)
            InputMap.put("TODATE", toDate);

        if (getReportTypeMap().equals("DELAYED"))
            InputMap.put("DELAYED", "YES");
        else if (getReportTypeMap().equals("CURRENT"))
            InputMap.put("DELAYED", "NO");
        birthDeathList = viewReportService.getStillBirthDeath(InputMap, getPage(), getPageSize());
        setStateNameinRegistration(birthDeathList);
        if (getReportTypeMap().equals("BOTH"))
            setSubTitle("Total Registration");
        else
            setSubTitle(BndConstants.REPORTFORMATMAP.get(getReportTypeMap()));
        setSearchMode(RESULT);
        setReportType(AGGREGATEREPORT);
        LOGGER.debug("Completed aggregateReportResult method");
        return AGGREGATEREPORT;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    @Action(value = "/report-yearlyWiseReportResult", results = { @Result(name = YEARWISEREPORT, type = "dispatcher") })
    public String yearlyWiseReportResult() {
        LOGGER.debug("Started yearlyWiseReportResult method");
        final HashMap<String, Object> InputMap = createMap();
        if (getMonth() != null)
            InputMap.put(MONTH, getMonth());
        if (getRegYear() != null)
            InputMap.put(YEAR, getRegYear());
        reportdetailsList = viewReportService.getCompleteRegistrationDetails(InputMap);
        totalReportdetail = viewReportService.getTotalObjforyear(reportdetailsList);
        setSearchMode(RESULT);
        setReportType(YEARWISEREPORT);
        LOGGER.debug("Completed yearlyWiseReportResult method");
        return YEARWISEREPORT;
    }

    public String yearlyWiseReportPrint() {
        final String jspprefix = yearlyWiseReportResult();
        return jspprefix + "Print";
    }

    @Action(value = "/report-regUnitWiseReport", results = { @Result(name = REGUNITWISEREPORT, type = "dispatcher") })
    public String regUnitWiseReport() {
        LOGGER.debug("Started regUnitWiseReport method");
        setReportType(REGUNITWISEREPORT);
        LOGGER.debug("Completed regUnitWiseReport method");
        return REGUNITWISEREPORT;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    @Action(value = "/report-regUnitWiseReportResult", results = { @Result(name = REGUNITWISEREPORT, type = "dispatcher") })
    public String regUnitWiseReportResult() {

        LOGGER.debug("Started regUnitWiseReportResult method");
        final HashMap<String, Object> InputMap = createMap();
        prepareMap(InputMap);
        if (getMonth() != null)
            InputMap.put(MONTH, getMonth());
        final Date currdate = new Date();
        InputMap.put(YEAR, Integer.parseInt(yearformat.format(currdate)));
        InputMap.put("REGISTRATIONUNIT", getRegUnitId());
        final List<ReportDetails> reportdetailListRegunit = viewReportService.getUnitwiseRegistrationDetails(InputMap);
        hospitalreportdetail = reportdetailListRegunit.get(0);
        nonhospitalreportdetail = reportdetailListRegunit.get(1);
        totalBirthRegforMonth = hospitalreportdetail.getBirthTotal() + nonhospitalreportdetail.getBirthTotal();
        totalBirthRegforMonthprogressive = hospitalreportdetail.getBirthTotalprogressive()
                + nonhospitalreportdetail.getBirthTotalprogressive();
        totalDeathRegforMonth = hospitalreportdetail.getDeathTotal() + nonhospitalreportdetail.getDeathTotal();
        totalDeathRegforMonthprogressive = hospitalreportdetail.getDeathTotalprogressive()
                + nonhospitalreportdetail.getDeathTotalprogressive();
        totalInfantforMonth = hospitalreportdetail.getInfantdeath() + nonhospitalreportdetail.getInfantdeath();
        totalInfantforMonthprogressive = hospitalreportdetail.getInfantdeathprogressive()
                + nonhospitalreportdetail.getInfantdeathprogressive();
        totalCertifiedforMonth = hospitalreportdetail.getCertifieddeath() + nonhospitalreportdetail.getCertifieddeath();
        totalCertifiedforMonthprogressive = hospitalreportdetail.getCertifieddeathprogressive()
                + nonhospitalreportdetail.getCertifieddeathprogressive();
        totalDeliverydeathforMonth = hospitalreportdetail.getDeliverydeath()
                + nonhospitalreportdetail.getDeliverydeath();
        totalDeliverydeathforMonthprogressive = hospitalreportdetail.getDeliverydeathprogressive()
                + nonhospitalreportdetail.getDeliverydeathprogressive();
        setSearchMode(RESULT);
        setReportType(REGUNITWISEREPORT);
        LOGGER.debug("Completed regUnitWiseReportResult method");
        return REGUNITWISEREPORT;

    }

    public String regUnitWiseReportPrint() {

        final String jspprefix = regUnitWiseReportResult();
        regUnit = bndCommonService.getRegistrationUnitById(getRegUnitId());
        return jspprefix + "Print";
    }

    @Action(value = "/report-summaryReport", results = { @Result(name = SUMMARYMONTHLYREPORT, type = "dispatcher") })
    public String summaryReport() {

        LOGGER.debug("Started summaryReport method");
        setReportType(SUMMARYMONTHLYREPORT);
        setRegType(BIRTH);
        LOGGER.debug("Completed summaryReport method");
        return SUMMARYMONTHLYREPORT;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    @Action(value = "/report-efficiencyReportResult", results = { @Result(name = EFFICIENCYREPORT, type = "dispatcher") })
    public String efficiencyReportResult() {

        LOGGER.debug("Started efficiencyReportResult method");
        final HashMap<String, Object> InputMap = createMap();
        prepareMap(InputMap);
        BigDecimal birthrate = BigDecimal.ZERO;
        BigDecimal deathrate = BigDecimal.ZERO;
        BigDecimal stillbirthrate = BigDecimal.ZERO;
        BigDecimal infantmortalityrate = BigDecimal.ZERO;
        BigDecimal meternalmortalityrate = BigDecimal.ZERO;
        BigDecimal growthrate = BigDecimal.ZERO;
        final BigDecimal livebirthno = BigDecimal.valueOf(viewReportService.getNumberOfLiveBirths(InputMap));
        final BigDecimal deathno = BigDecimal.valueOf(viewReportService.getNumberOfDeaths(InputMap));
        final BigDecimal stillBirthno = BigDecimal.valueOf(viewReportService.getNumberOfStillBirths(InputMap));
        final BigDecimal infantsno = BigDecimal.valueOf(viewReportService.getInfantDeaths(InputMap));
        final BigDecimal maternno = BigDecimal.valueOf(viewReportService.getMaternalDeath(InputMap));
        final BigDecimal populationno = BigDecimal.valueOf(viewReportService.getPopulation(InputMap));
        final BigDecimal multiplicand = BigDecimal.valueOf(1000);
        final BigDecimal zero = BigDecimal.valueOf(0);
        birthrate = populationno != zero ? livebirthno.multiply(multiplicand).divide(populationno, 2,
                RoundingMode.HALF_UP) : zero;
        deathrate = populationno != zero ? deathno.multiply(multiplicand).divide(populationno, 2, RoundingMode.HALF_UP)
                : zero;
        stillbirthrate = livebirthno != zero ? stillBirthno.multiply(multiplicand).divide(livebirthno, 2,
                RoundingMode.HALF_UP) : zero;
        infantmortalityrate = livebirthno != zero ? infantsno.multiply(multiplicand).divide(livebirthno, 2,
                RoundingMode.HALF_UP) : zero;
        meternalmortalityrate = livebirthno != zero ? maternno.multiply(multiplicand).divide(livebirthno, 2,
                RoundingMode.HALF_UP) : zero;
        growthrate = populationno != zero ? livebirthno.subtract(deathno).multiply(multiplicand)
                .divide(populationno, 2, RoundingMode.HALF_UP) : zero;
                resultMap.put("birthrate", birthrate);
                resultMap.put("deathrate", deathrate);
                resultMap.put("stillbirthrate", stillbirthrate);
                resultMap.put("infantmortalityrate", infantmortalityrate);
                resultMap.put("meternalmortalityrate", meternalmortalityrate);
                resultMap.put("growthrate", growthrate);
                setSearchMode(RESULT);
                setReportType(EFFICIENCYREPORT);
                LOGGER.debug("Completed efficiencyReportResult method");
                return EFFICIENCYREPORT;
    }

    public String efficiencyReportPrint() {

        final String jspprefix = efficiencyReportResult();
        return jspprefix + "Print";

    }

    @Transactional
    @SuppressWarnings("unchecked")
    @Action(value = "/report-summaryReportResult", results = { @Result(name = SUMMARYMONTHLYREPORT, type = "dispatcher") })
    public String summaryReportResult() {

        LOGGER.debug("Started summaryReportResult method");
        final HashMap<String, Object> InputMap = createMap();
        prepareMap(InputMap);
        if (getRegUnitId() != -1)
            InputMap.put("REGISTRATIONUNIT", getRegUnitId());
        resultMap = viewReportService.getSummaryReportResult(InputMap);
        setSearchMode(RESULT);
        setReportType(SUMMARYMONTHLYREPORT);
        setReportTypeMap("BOTH");
        regUnit = bndCommonService.getRegistrationUnitById(getRegUnitId());
        LOGGER.debug("Completed summaryReportResult method");
        return SUMMARYMONTHLYREPORT;
    }

    public String summaryReportPrint() {
        final String jspprefix = summaryReportResult();
        return jspprefix + "Print";

    }

    @Transactional
    @SuppressWarnings("unchecked")
    @Action(value = "/report-delayedRegistrationResult", results = { @Result(name = DELAYEDREGREPORT, type = "dispatcher") })
    public String delayedRegistrationResult() {

        LOGGER.debug("Started delayedRegistrationResult method");
        final Map<String, Object> InputMap = createMap();
        InputMap.put("YEAR", getRegYear());
        reportdetailsList = viewReportService.getDelayedRegistrationDetails(InputMap);
        totalReportdetail = viewReportService.getTotalObj(reportdetailsList);
        setSearchMode(RESULT);
        setReportType(DELAYEDREGREPORT);
        LOGGER.debug("Completed delayedRegistrationResult method");
        return DELAYEDREGREPORT;
    }

    public String delayedRegistrationReportPrint() {
        final String jspprefix = delayedRegistrationResult();
        return jspprefix + "Print";

    }

    @Transactional
    @SuppressWarnings("unchecked")
    @Action(value = "/report-summaryDetailedReport", results = { @Result(name = NEW, type = "dispatcher") })
    public String summaryDetailedReport() {

        LOGGER.debug("Started summaryDetailedReport method");
        final HashMap<String, Object> InputMap = createMap();
        prepareMap(InputMap);
        if (getReportTypeMap().equals("DELAYED"))
            InputMap.put("DELAYED", "YES");
        else if (getReportTypeMap().equals("CURRENT"))
            InputMap.put("DELAYED", "NO");
        if (getRegUnitId() != -1)
            InputMap.put("REGISTRATIONUNIT", getRegUnitId());
        birthDeathList = viewReportService.getStillBirthDeath(InputMap, getPage(), getPageSize());
        setStateNameinRegistration(birthDeathList);
        fromDate = BndDateUtils.calenderutils(regYear, month).get(0);
        toDate = BndDateUtils.calenderutils(regYear, month).get(1);
        if (getReportTypeMap().equals("BOTH"))
            setSubTitle("Total Registration");
        else
            setSubTitle(BndConstants.REPORTFORMATMAP.get(getReportTypeMap()));
        LOGGER.debug("Completed summaryDetailedReport method");
        return "new";
    }

    @SuppressWarnings("unchecked")
    public HashMap createMap() {

        final HashMap<String, Object> hashMap = new HashMap<String, Object>();
        return hashMap;
    }

    @SuppressWarnings("unchecked")
    public HashMap prepareMap(final HashMap<String, Object> InputMap) {

        LOGGER.debug("Started prepareMap method");
        if (getMonth() != null)
            InputMap.put(MONTH, getMonth().toString());
        if (getRegYear() != null)
            InputMap.put(YEAR, getRegYear().toString());
        if (getRegType() != null)
            if (getRegType().equals(BIRTH))
                InputMap.put(REGTYPE, BndConstants.BIRTH);
            else if (getRegType().equals("Death"))
                InputMap.put(REGTYPE, BndConstants.DEATH);
            else
                InputMap.put(REGTYPE, BndConstants.STILLBIRTH);
        LOGGER.debug("Completed prepareMap method");
        return InputMap;

    }

    @Action(value = "/report-hospitalRegReport", results = { @Result(name = HOSPITALREGREPORT, type = "dispatcher") })
    public String hospitalRegReport() {

        LOGGER.debug("Started hospitalRegReport method");
        setRegType(BIRTH);
        setReportType(HOSPITALREGREPORT);
        LOGGER.debug("Completed hospitalRegReport method");
        return HOSPITALREGREPORT;
    }
    
    @Transactional
    @SuppressWarnings("unchecked")
    @Action(value = "/report-hospitalRegReportResult", results = { @Result(name = HOSPITALREGREPORT, type = "dispatcher") })
    public String hospitalRegReportResult() {

        LOGGER.debug("Started hospitalRegReportResult method");
        final HashMap<String, Object> InputMap = createMap();
        prepareMap(InputMap);
        if (fromDate != null)
            InputMap.put("FROMDATE", fromDate);

        if (toDate != null)
            InputMap.put("TODATE", toDate);
        InputMap.put("REPORTTYPE", HOSPITALREGREPORT);
        InputMap.put("PLACEOFDEATH", "hospital");
        if (getStatus() != null && getStatus() != 0)
            InputMap.put("STATUS", viewReportService.getStatusMap().get(getStatus()));
        if (getEstablishment() != null && getEstablishment() != 0)
            InputMap.put("ESTABLISHMENTID", getEstablishment());
        birthDeathList = viewReportService.getStillBirthDeath(InputMap, getPage(), getPageSize());
        setStateNameinRegistration(birthDeathList);
        setSearchMode(RESULT);
        setReportType(HOSPITALREGREPORT);
        LOGGER.debug("Completed hospitalRegReportResult method");
        return HOSPITALREGREPORT;
    }

    private EgovPaginatedList pagedResults;

    public EgovPaginatedList getPagedResults() {
        return pagedResults;
    }

    @Action(value = "/report-nameIncusionStatusReport", results = { @Result(name = NAMEINCLUSIONSTATUSREPORT, type = "dispatcher") })
    public String nameIncusionStatusReport() {
        fromDate = null;
        toDate = null;
        regType = BndConstants.NAMEINCLUSIONPENDING;
        regNo = "BIRTH";
        return NAMEINCLUSIONSTATUSREPORT;
    }

    public void validateNameInclusionReportResult() {
        if (fromDate == null)
            addFieldError("fromDate", getMessage("fromDate.required"));
        if (toDate == null)
            addFieldError("toDate", getMessage("toDate.required"));

        if (fromDate != null && toDate != null && fromDate.after(toDate))
            addFieldError("fromDate", getMessage("fromDate.toDate.validate"));
    }

    @Transactional
    @ValidationErrorPage(NAMEINCLUSIONSTATUSREPORT)
    @Action(value = "/report-nameInclusionReportResult", results = { @Result(name = NAMEINCLUSIONSTATUSREPORT, type = "dispatcher") })
    public String nameInclusionReportResult() throws ParseException {
        searchMode = RESULT;
        export();
        pagedResults = paymentReportService.getNameInclusionReport(fromDate, toDate, regType, getPage(), getPageSize());
        return NAMEINCLUSIONSTATUSREPORT;
    }

    public void export() {
        final ParamEncoder paramEncoder = new ParamEncoder("currentRowObject");
        if (parameters.get(paramEncoder.encodeParameterName(TableTagParameters.PARAMETER_EXPORTTYPE)) != null) {
            setPageSize(0);
            setPage(1);
        }

    }

    @Action(value = "/report-updatedRegistrationReport", results = { @Result(name = UPDATEDREGISTRATIONREPORT, type = "dispatcher") })
    public String updatedRegistrationReport() {
        setRegType(BIRTH);
        return UPDATEDREGISTRATIONREPORT;
    }

    @Transactional
    @Action(value = "/report-updatedRegistrationReportResult", results = { @Result(name = UPDATEDREGISTRATIONREPORT, type = "dispatcher") })
    public String updatedRegistrationReportResult() {
        pagedResults = viewReportService.getRegistrationUpdatedList(fromDate, toDate, regType, regNo, getPage(),
                getPageSize());
        searchMode = RESULT;
        return UPDATEDREGISTRATIONREPORT;
    }

    public Integer getEstablishment() {
        return establishment;
    }

    public void setEstablishment(final Integer establishment) {
        this.establishment = establishment;
    }

    @Action(value = "/report-efficiencyReport", results = { @Result(name = EFFICIENCYREPORT, type = "dispatcher") })
    public String efficiencyReport() {

        setReportType(EFFICIENCYREPORT);
        return EFFICIENCYREPORT;
    }

    @Action(value = "/report-delayedregistration", results = { @Result(name = DELAYEDREGREPORT, type = "dispatcher") })
    public String delayedregistration() {

        setReportType(DELAYEDREGREPORT);
        return DELAYEDREGREPORT;
    }

    @Action(value = "/report-certificateReport", results = { @Result(name = CERTIFICATEREPORT, type = "dispatcher") })
    public String certificateReport() {

        LOGGER.debug("Started certificateReport method");
        setRegType(BIRTH);
        setReportType(CERTIFICATEREPORT);
        LOGGER.debug("Completed certificateReport method");
        return CERTIFICATEREPORT;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    @Action(value = "/report-certificateReportResult", results = { @Result(name = CERTIFICATEREPORT, type = "dispatcher") })
    public String certificateReportResult() {

        LOGGER.debug("Started certificateReportResult method");
        final HashMap<String, Object> InputMap = createMap();
        prepareMap(InputMap);
        InputMap.put("REGISTRATIONUNIT", getRegUnitId());
        InputMap.put("REGISTRATIONNO", getRegNo());
        // feeCollectionList=viewReportService.getFeeCollectionDetails(InputMap);
        setSearchMode(RESULT);
        setReportType(CERTIFICATEREPORT);
        if (feeCollectionList != null && !feeCollectionList.isEmpty()) {
            feeCollection = feeCollectionList.get(0);
            setNoofcopy(feeCollection.getNo_Of_copies());
        } else
            setNoofcopy(0);
        regUnit = bndCommonService.getRegistrationUnitById(getRegUnitId());
        LOGGER.debug("Completed certificateReportResult method");
        return CERTIFICATEREPORT;
    }

    public String certificateReportPrint() {

        final String jspprefix = certificateReportResult();
        return jspprefix + "Print";

    }

    @Action(value = "/report-cancelledReport", results = { @Result(name = CANCELLEDREPORT, type = "dispatcher") })
    public String cancelledReport() {

        LOGGER.debug("Started cancelledReport method");
        setRegType(BIRTH);
        setReportType(CANCELLEDREPORT);
        LOGGER.debug("Completed cancelledReport method");
        return CANCELLEDREPORT;
    }

    @Transactional
    @Action(value = "/report-cancelledReportResult", results = { @Result(name = CANCELLEDREPORT, type = "dispatcher") })
    public String cancelledReportResult() {

        LOGGER.debug("Started cancelledReportResult method");
        final HashMap<String, Object> InputMap = createMap();
        prepareMap(InputMap);
        if (fromDate != null)
            InputMap.put("FROMDATE", fromDate);

        if (toDate != null)
            InputMap.put("TODATE", toDate);
        if (getRegUnitId() != null && getRegUnitId() != -1)
            InputMap.put("REGISTRATIONUNIT", getRegUnitId());
        InputMap.put("STATUS", bndCommonService.getStatusByModuleAndCode(BndConstants.BIRTHREGISTRATION, "Cancelled"));
        birthDeathList = viewReportService.getStillBirthDeath(InputMap, getPage(), getPageSize());
        setStateNameinRegistration(birthDeathList);
        setSearchMode(RESULT);
        setReportType(CANCELLEDREPORT);
        LOGGER.debug("Completed cancelledReportResult method");

        return CANCELLEDREPORT;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public void setStateNameinRegistration(final PaginatedList birthDeathList) {

        final List<ViewBirthDeathRegistration> finalResult = birthDeathList != null ? birthDeathList.getList() : null;
        if (finalResult != null) {
            bndCommonService.getAllStatesOfCountry();
            for (final ViewBirthDeathRegistration reg : finalResult)
                if (reg.getRegistrationObject().getEventAddress() != null
                && reg.getRegistrationObject().getEventAddress().getState() != null)
                    if (bndCommonService.ADDRESSMAP.containsKey(Integer.valueOf(reg.getRegistrationObject()
                            .getEventAddress().getState())))
                        reg.getRegistrationObject().setStateName(
                                bndCommonService.ADDRESSMAP.get(Integer.valueOf(reg.getRegistrationObject()
                                        .getEventAddress().getState())));
        }
    }

    public List<String> getRegistrationTypeList() {
        return BndConstants.REGISTRATIONTYPELIST;
    }

    public List<String> getRegistrationReportList() {
        return BndConstants.REGISTRATIONREPORTLIST;
    }

    public Map<Integer, String> getMonthMap() {
        return BndConstants.MONTHMAP;
    }

    public Map<Integer, String> getStatusMap() {
        return viewReportService.getStatusMap();
    }

    public ViewBirthDeathRegistration getBirthdeathview() {
        return birthdeathview;
    }

    public void setBirthdeathview(final ViewBirthDeathRegistration birthdeathview) {
        this.birthdeathview = birthdeathview;
    }

    public Integer getRegYear() {
        return regYear;
    }

    public void setRegYear(final Integer regYear) {
        this.regYear = regYear;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(final Integer month) {
        this.month = month;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(final String reportType) {
        this.reportType = reportType;
    }

    public String getSearchMode() {
        return searchMode;
    }

    public void setSearchMode(final String searchMode) {
        this.searchMode = searchMode;
    }

    public String getRegType() {
        return regType;
    }

    public void setRegType(final String regType) {
        this.regType = regType;
    }

    public Map<String, String> getReportFormatMap() {
        return BndConstants.REPORTFORMATMAP;
    }

    public String getReportTypeMap() {
        return reportTypeMap;
    }

    public void setReportTypeMap(final String reportTypeMap) {
        this.reportTypeMap = reportTypeMap;
    }

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(final Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

    public PaginatedList getBirthDeathList() {
        return birthDeathList;
    }

    public void setBirthDeathList(final PaginatedList birthDeathList) {
        this.birthDeathList = birthDeathList;
    }

    public void setViewReportService(final ReportService viewReportService) {
        this.viewReportService = viewReportService;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(final String subTitle) {
        this.subTitle = subTitle;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public List<ReportDetails> getReportdetailsList() {
        return reportdetailsList;
    }

    public void setReportdetailsList(final List<ReportDetails> reportdetailsList) {
        this.reportdetailsList = reportdetailsList;
    }

    public Long getRegUnitId() {
        return regUnitId;
    }

    public void setRegUnitId(final Long regUnitId) {
        this.regUnitId = regUnitId;
    }

    public ReportDetails getHospitalreportdetail() {
        return hospitalreportdetail;
    }

    public void setHospitalreportdetail(final ReportDetails hospitalreportdetail) {
        this.hospitalreportdetail = hospitalreportdetail;
    }

    public ReportDetails getNonhospitalreportdetail() {
        return nonhospitalreportdetail;
    }

    public void setNonhospitalreportdetail(final ReportDetails nonhospitalreportdetail) {
        this.nonhospitalreportdetail = nonhospitalreportdetail;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

    public ReportDetails getTotalReportdetail() {
        return totalReportdetail;
    }

    public void setTotalReportdetail(final ReportDetails totalReportdetail) {
        this.totalReportdetail = totalReportdetail;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(final String regNo) {
        this.regNo = regNo;
    }

    public RegistrationUnit getRegUnit() {
        return regUnit;
    }

    public void setRegUnit(final RegistrationUnit regUnit) {
        this.regUnit = regUnit;
    }

    public FeeCollection getFeeCollection() {
        return feeCollection;
    }

    public void setFeeCollection(final FeeCollection feeCollection) {
        this.feeCollection = feeCollection;
    }

    public Integer getNoofcopy() {
        return noofcopy;
    }

    public void setNoofcopy(final Integer noofcopy) {
        this.noofcopy = noofcopy;
    }

    public String getTodaysDate() {
        return sf.format(todaysDate);
    }

    public Integer getTotalBirthRegforMonth() {
        return totalBirthRegforMonth;
    }

    public void setTotalBirthRegforMonth(final Integer totalBirthRegforMonth) {
        this.totalBirthRegforMonth = totalBirthRegforMonth;
    }

    public Integer getTotalDeathRegforMonth() {
        return totalDeathRegforMonth;
    }

    public void setTotalDeathRegforMonth(final Integer totalDeathRegforMonth) {
        this.totalDeathRegforMonth = totalDeathRegforMonth;
    }

    public Integer getTotalBirthRegforMonthprogressive() {
        return totalBirthRegforMonthprogressive;
    }

    public void setTotalBirthRegforMonthprogressive(final Integer totalBirthRegforMonthprogressive) {
        this.totalBirthRegforMonthprogressive = totalBirthRegforMonthprogressive;
    }

    public Integer getTotalDeathRegforMonthprogressive() {
        return totalDeathRegforMonthprogressive;
    }

    public void setTotalDeathRegforMonthprogressive(final Integer totalDeathRegforMonthprogressive) {
        this.totalDeathRegforMonthprogressive = totalDeathRegforMonthprogressive;
    }

    public void setPaymentReportService(final PaymentReportService paymentReportService) {
        this.paymentReportService = paymentReportService;
    }

    public Integer getTotalInfantforMonth() {
        return totalInfantforMonth;
    }

    public void setTotalInfantforMonth(final Integer totalInfantforMonth) {
        this.totalInfantforMonth = totalInfantforMonth;
    }

    public Integer getTotalInfantforMonthprogressive() {
        return totalInfantforMonthprogressive;
    }

    public void setTotalInfantforMonthprogressive(final Integer totalInfantforMonthprogressive) {
        this.totalInfantforMonthprogressive = totalInfantforMonthprogressive;
    }

    public Integer getTotalCertifiedforMonth() {
        return totalCertifiedforMonth;
    }

    public void setTotalCertifiedforMonth(final Integer totalCertifiedforMonth) {
        this.totalCertifiedforMonth = totalCertifiedforMonth;
    }

    public Integer getTotalCertifiedforMonthprogressive() {
        return totalCertifiedforMonthprogressive;
    }

    public void setTotalCertifiedforMonthprogressive(final Integer totalCertifiedforMonthprogressive) {
        this.totalCertifiedforMonthprogressive = totalCertifiedforMonthprogressive;
    }

    public Integer getTotalDeliverydeathforMonth() {
        return totalDeliverydeathforMonth;
    }

    public void setTotalDeliverydeathforMonth(final Integer totalDeliverydeathforMonth) {
        this.totalDeliverydeathforMonth = totalDeliverydeathforMonth;
    }

    public Integer getTotalDeliverydeathforMonthprogressive() {
        return totalDeliverydeathforMonthprogressive;
    }

    public void setTotalDeliverydeathforMonthprogressive(final Integer totalDeliverydeathforMonthprogressive) {
        this.totalDeliverydeathforMonthprogressive = totalDeliverydeathforMonthprogressive;
    }

}
