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

package org.egov.tl.service;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.service.CFinancialYearService;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.pims.commons.Position;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.NatureOfBusiness;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.entity.dto.DemandnoticeForm;
import org.egov.tl.entity.dto.OnlineSearchForm;
import org.egov.tl.entity.dto.SearchForm;
import org.egov.tl.repository.SearchTradeRepository;
import org.egov.tl.repository.specs.SearchTradeSpec;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.egov.infra.utils.DateUtils.currentDateToDefaultDateFormat;
import static org.egov.infra.utils.DateUtils.getDefaultFormattedDate;
import static org.egov.infra.utils.DateUtils.toYearFormat;
import static org.egov.tl.utils.Constants.BUTTONAPPROVE;
import static org.egov.tl.utils.Constants.BUTTONREJECT;
import static org.egov.tl.utils.Constants.LICENSE_FEE_TYPE;
import static org.egov.tl.utils.Constants.NEW_LIC_APPTYPE;
import static org.egov.tl.utils.Constants.RENEWAL_LIC_APPTYPE;
import static org.egov.tl.utils.Constants.TRADE_LICENSE;
import static org.egov.tl.utils.Constants.CLOSURE_LIC_APPTYPE;

@Transactional(readOnly = true)
public class TradeLicenseService extends AbstractLicenseService<TradeLicense> {
    @Autowired
    private TradeLicenseSmsAndEmailService tradeLicenseSmsAndEmailService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private LicenseUtils licenseUtils;

    @Autowired
    private ModuleService moduleService;
    
    @Autowired
    private SearchTradeRepository searchTradeRepository;

    @Autowired
    private CFinancialYearService cFinancialYearService;

    @Autowired
    private CityService cityService;

    @Autowired
    private EisCommonService eisCommonService;

    @Override
    protected NatureOfBusiness getNatureOfBusiness() {
        return natureOfBusinessService.getNatureOfBusinessByName("Permanent");
    }

    @Override
    protected Module getModuleName() {
        return moduleService.getModuleByName(TRADE_LICENSE);
    }

    @Override
    protected void sendEmailAndSMS(final TradeLicense license, final String currentAction) {
        tradeLicenseSmsAndEmailService.sendSmsAndEmail(license, currentAction);
    }

    @Override
    protected LicenseAppType getLicenseApplicationTypeForRenew() {
        return licenseAppTypeService.getLicenseAppTypeByName(RENEWAL_LIC_APPTYPE);
    }

    @Override
    protected LicenseAppType getLicenseApplicationType() {
        return licenseAppTypeService.getLicenseAppTypeByName(NEW_LIC_APPTYPE);
    }

    @Override
    protected LicenseAppType getClosureLicenseApplicationType() {
        return licenseAppTypeService.getLicenseAppTypeByName(CLOSURE_LIC_APPTYPE);
    }

    @Transactional
    public void updateTradeLicense(final TradeLicense license, final WorkflowBean workflowBean) {
        licenseRepository.save(license);
        tradeLicenseSmsAndEmailService.sendSmsAndEmail(license, workflowBean.getWorkFlowAction());
        licenseApplicationIndexService.createOrUpdateLicenseApplicationIndex(license);
    }

    @Transactional
    public void updateStatusInWorkFlowProgress(TradeLicense license, final String workFlowAction) {

        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(securityUtils.getCurrentUser().getId());
        final Position wfInitiator = getWorkflowInitiator(license);
        if (BUTTONAPPROVE.equals(workFlowAction)) {
            if (license.getTempLicenseNumber() == null && license.isNewApplication())
                license.setLicenseNumber(licenseNumberUtils.generateLicenseNumber());

            if (license.getCurrentDemand().getBaseDemand().compareTo(license.getCurrentDemand().getAmtCollected()) <= 0)
                licenseUtils.applicationStatusChange(license,
                        Constants.APPLICATION_STATUS_APPROVED_CODE);
            else
                licenseUtils.applicationStatusChange(license,
                        Constants.APPLICATION_STATUS_SECONDCOLLECTION_CODE);

        }
        if (BUTTONAPPROVE.equals(workFlowAction) || Constants.BUTTONFORWARD.equals(workFlowAction)) {
            license.setStatus(licenseStatusService.getLicenseStatusByCode(Constants.STATUS_UNDERWORKFLOW));
            if (license.getState().getValue().equals(Constants.WF_REVENUECLERK_APPROVED))
                licenseUtils.applicationStatusChange(license,
                        Constants.APPLICATION_STATUS_INSPE_CODE);
            if (license.getState().getValue().equals(Constants.WORKFLOW_STATE_REJECTED))
                licenseUtils.applicationStatusChange(license,
                        Constants.APPLICATION_STATUS_CREATED_CODE);
        }

        if (Constants.GENERATECERTIFICATE.equals(workFlowAction)) {
            license.setActive(true);
            license.setStatus(licenseStatusService.getLicenseStatusByCode(Constants.STATUS_ACTIVE));
            // setting license to non-legacy, old license number will be the only tracking
            // to check a license created as legacy or new hereafter.
            license.setLegacy(false);
            validityService.applyLicenseValidity(license);
            licenseUtils.applicationStatusChange(license,
                    Constants.APPLICATION_STATUS_GENECERT_CODE);
        }
        if (BUTTONREJECT.equals(workFlowAction))
            if (license.getLicenseAppType() != null && wfInitiator.equals(userAssignment.getPosition())
                    && ("Rejected".equals(license.getState().getValue()))
                    || "License Created".equals(license.getState().getValue())) {
                license.setStatus(licenseStatusService.getLicenseStatusByCode(Constants.STATUS_CANCELLED));
                licenseUtils.applicationStatusChange(license,
                        Constants.APPLICATION_STATUS_CANCELLED);
                if (license.isNewApplication())
                    license.setActive(false);
            } else {
                license.setStatus(licenseStatusService.getLicenseStatusByCode(Constants.STATUS_REJECTED));
                licenseUtils.applicationStatusChange(license,
                        Constants.APPLICATION_STATUS_REJECTED);
            }
        if (license.hasState() && license.getState().getValue().contains(Constants.WF_REVENUECLERK_APPROVED)) {
            final BigDecimal currentDemandAmount = recalculateLicenseFee(license.getCurrentDemand());
            final BigDecimal recalDemandAmount = calculateFeeAmount(license);
            if (recalDemandAmount.compareTo(currentDemandAmount) >= 0)
                updateDemandForChangeTradeArea(license);
        }
    }

    public ReportRequest prepareReportInputData(final License license) {
        final String cityGrade = getCityGrade();
        if (cityGrade != null && cityGrade.equalsIgnoreCase(Constants.CITY_GRADE_CORPORATION)) {
            final Map<String, Object> reportParams = getReportParamsForCertificate(license, null, null);
            return new ReportRequest("licenseCertificateForCorp", license, reportParams);
        } else {
            final Map<String, Object> reportParams = getReportParamsForCertificate(license, null, null);
            return new ReportRequest("licenseCertificate", license, reportParams);
        }
    }

    private String getCityGrade() {
        return cityService.getCityByURL(ApplicationThreadLocals.getDomainName()).getGrade();
    }

    public ReportOutput prepareReportInputDataForDig(final License license, final String districtName,
                                                     final String cityMunicipalityName) {
        final String cityGrade = getCityGrade();
        if (cityGrade != null && cityGrade.equalsIgnoreCase(Constants.CITY_GRADE_CORPORATION)) {
            return reportService.createReport(
                    new ReportRequest("licenseCertificateForCorp", license, getReportParamsForCertificate(license, districtName,
                            cityMunicipalityName)));
        } else {
            return reportService.createReport(
                    new ReportRequest("licenseCertificate", license, getReportParamsForCertificate(license, districtName,
                            cityMunicipalityName)));
        }
    }

    private Map<String, Object> getReportParamsForCertificate(final License license, final String districtName,
                                                              final String cityMunicipalityName) {

        final Map<String, Object> reportParams = new HashMap<>();
        reportParams.put("applicationnumber", license.getApplicationNumber());
        reportParams.put("applicantName", license.getLicensee().getApplicantName());
        reportParams.put("licencenumber", license.getLicenseNumber());
        reportParams.put("wardName", license.getBoundary().getName());
        reportParams.put("cscNumber", "");
        reportParams.put("nameOfEstablishment", license.getNameOfEstablishment());
        reportParams.put("licenceAddress", StringEscapeUtils.escapeXml(license.getAddress()));
        reportParams.put("municipality", cityMunicipalityName);
        reportParams.put("district", districtName);
        reportParams.put("category", license.getCategory().getName());
        reportParams.put("subCategory", license.getTradeName().getName());

        if (license.getState() != null && license.getState().getValue().equals(Constants.WF_FIRST_LVL_FEECOLLECTED)) {
            reportParams.put("certificateType", "provisional");
        }

        reportParams.put("appType", license.isNewApplication() ? "New Trade" : "Renewal");
        reportParams.put("currentDate", currentDateToDefaultDateFormat());
        if (ApplicationThreadLocals.getMunicipalityName().contains("Corporation"))
            reportParams.put("carporationulbType", Boolean.TRUE);
        reportParams.put("municipality", ApplicationThreadLocals.getMunicipalityName());
        String startYear;
        String endYear;
        BigDecimal amtPaid;
        Optional<EgDemandDetails> demandDetails = license.getCurrentDemand().getEgDemandDetails().stream()
                .sorted(Comparator.comparing(EgDemandDetails::getInstallmentEndDate).reversed())
                .filter(demandDetail -> demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster().equals(LICENSE_FEE_TYPE))
                .filter(demandDetail -> demandDetail.getAmount().subtract(demandDetail.getAmtCollected())
                        .doubleValue() <= 0)
                .findFirst();
        if (demandDetails.isPresent()) {
            amtPaid = demandDetails.get().getAmtCollected();
            startYear = toYearFormat(demandDetails.get().getInstallmentStartDate());
            endYear = toYearFormat(demandDetails.get().getInstallmentEndDate());
        } else
            throw new ValidationException("License Fee is not paid", "License Fee is not paid");

        reportParams.put("installMentYear", startYear + "-" + endYear);
        reportParams.put("applicationdate", getDefaultFormattedDate(license.getApplicationDate()));
        reportParams.put("demandUpdateDate", getDefaultFormattedDate(license.getCurrentDemand().getModifiedDate()));
        reportParams.put("demandTotalamt", amtPaid);
        return reportParams;
    }

    @ReadOnly
    public List<String> getTradeLicenseForGivenParam(final String paramValue, final String paramType) {
        List<String> licenseList = new ArrayList<>();
        if (paramType.equals(Constants.SEARCH_BY_APPNO))
            licenseList = licenseRepository.findAllApplicationNumberLike(paramValue);

        else if (paramType.equals(Constants.SEARCH_BY_LICENSENO))
            licenseList = licenseRepository.findAllLicenseNumberLike(paramValue);

        else if (paramType.equals(Constants.SEARCH_BY_OLDLICENSENO))
            licenseList = licenseRepository.findAllOldLicenseNumberLike(paramValue);

        else if (paramType.equals(Constants.SEARCH_BY_TRADETITLE))
            licenseList = licenseRepository.findAllNameOfEstablishmentLike(paramValue);

        else if (paramType.equals(Constants.SEARCH_BY_TRADEOWNERNAME))
            licenseList = licenseRepository.findAllApplicantNameLike(paramValue);

        else if (paramType.equals(Constants.SEARCH_BY_PROPERTYASSESSMENTNO))
            licenseList = licenseRepository.findAllAssessmentNoLike(paramValue);

        else if (paramType.equals(Constants.SEARCH_BY_MOBILENO))
            licenseList = licenseRepository.findAllMobilePhoneNumberLike(paramValue);

        return licenseList;
    }

    @ReadOnly
    public Page<SearchForm> searchTradeLicense(final SearchForm searchForm) {
        final Pageable pageable = new PageRequest(searchForm.pageNumber(),
                searchForm.pageSize(), searchForm.orderDir(), searchForm.orderBy());
        final String currentUserRoles = securityUtils.getCurrentUser().getRoles().toString();
        Page<License> licenses = searchTradeRepository.findAll(SearchTradeSpec.searchTrade(searchForm), pageable);
        List<SearchForm> searchResults = new ArrayList<>();
        licenses.forEach(license -> {
            final CFinancialYear financialYear = cFinancialYearService.getFinancialYearByDate(license.getDateOfExpiry());
            final String expiryYear = financialYear != null ? financialYear.getFinYearRange() : "";
            searchResults.add(new SearchForm(license, currentUserRoles, getOwnerName(license), expiryYear));
        });
        return new PageImpl<>(searchResults, pageable, licenses.getTotalElements());
    }

    @ReadOnly
    public List<OnlineSearchForm> onlineSearchTradeLicense(final OnlineSearchForm searchForm) {
        final Criteria searchCriteria = entityQueryService.getSession().createCriteria(TradeLicense.class);
        searchCriteria.createAlias("licensee", "licc").createAlias("category", "cat")
                .createAlias("tradeName", "subcat").createAlias("status", "licstatus");
        if (StringUtils.isNotBlank(searchForm.getApplicationNumber()))
            searchCriteria.add(Restrictions.eq("applicationNumber", searchForm.getApplicationNumber()).ignoreCase());
        if (StringUtils.isNotBlank(searchForm.getLicenseNumber()))
            searchCriteria.add(Restrictions.eq("licenseNumber", searchForm.getLicenseNumber()).ignoreCase());

        searchCriteria.add(Restrictions.isNotNull("applicationNumber"));
        searchCriteria.addOrder(Order.asc("id"));
        List<OnlineSearchForm> searchResult = new ArrayList<>();
        License license = (License) searchCriteria.uniqueResult();
        if (license != null)
            searchResult.add(new OnlineSearchForm(license, getDemandColl(license)));
        return searchResult;
    }

    public BigDecimal[] getDemandColl(License license) {
        Module module = moduleService.getModuleByName(TRADE_LICENSE);
        final Installment currInstallment = getCurrentInstallment(module);
        BigDecimal[] dmdColl = new BigDecimal[3];
        BigDecimal currDmd = BigDecimal.ZERO;
        BigDecimal totColl = BigDecimal.ZERO;
        BigDecimal arrDmd = BigDecimal.ZERO;
        for (EgDemandDetails dmddtls : license.getCurrentDemand().getEgDemandDetails()) {
            if (dmddtls.getInstallmentStartDate().before(currInstallment.getFromDate()) || dmddtls.getInstallmentStartDate().equals(currInstallment.getFromDate())) {
                arrDmd = arrDmd.add(dmddtls.getAmount());
                totColl = totColl.add(dmddtls.getAmtCollected());
            } else {
                currDmd = currDmd.add(dmddtls.getAmount());
                totColl = totColl.add(dmddtls.getAmtCollected());
            }
        }
        dmdColl[0] = arrDmd;
        dmdColl[1] = currDmd;
        dmdColl[2] = totColl;
        return dmdColl;
    }

    public Installment getInstallmentForDate(final Date date, final Module module) {
        return installmentDao.getInsatllmentByModuleForGivenDate(module, date);
    }

    public Installment getCurrentInstallment(final Module module) {
        return getInstallmentForDate(new Date(), module);
    }

    @ReadOnly
    public List<DemandnoticeForm> searchLicensefordemandnotice(final DemandnoticeForm demandnoticeForm) {
        final Criteria searchCriteria = entityQueryService.getSession().createCriteria(TradeLicense.class);
        searchCriteria.createAlias("licensee", "licc").createAlias("category", "cat").createAlias("tradeName", "subcat")
                .createAlias("status", "licstatus");
        if (StringUtils.isNotBlank(demandnoticeForm.getLicenseNumber()))
            searchCriteria.add(Restrictions.eq("licenseNumber", demandnoticeForm.getLicenseNumber()).ignoreCase());
        if (StringUtils.isNotBlank(demandnoticeForm.getOldLicenseNumber()))
            searchCriteria
                    .add(Restrictions.eq("oldLicenseNumber", demandnoticeForm.getOldLicenseNumber()).ignoreCase());
        if (demandnoticeForm.getCategoryId() != null)
            searchCriteria.add(Restrictions.eq("cat.id", demandnoticeForm.getCategoryId()));
        if (demandnoticeForm.getSubCategoryId() != null)
            searchCriteria.add(Restrictions.eq("subcat.id", demandnoticeForm.getSubCategoryId()));
        if (demandnoticeForm.getWardId() != null)
            searchCriteria.createAlias("parentBoundary", "wards")
                    .add(Restrictions.eq("wards.id", demandnoticeForm.getWardId()));
        if (demandnoticeForm.getLocalityId() != null)
            searchCriteria.createAlias("boundary", "locality")
                    .add(Restrictions.eq("locality.id", demandnoticeForm.getLocalityId()));
        if (demandnoticeForm.getStatusId() != null)
            searchCriteria.add(Restrictions.eq("status.id", demandnoticeForm.getStatusId()));
        else
            searchCriteria.add(Restrictions.ne("licstatus.statusCode", StringUtils.upperCase("CAN")));

        searchCriteria.addOrder(Order.asc("id"));
        final List<DemandnoticeForm> finalList = new LinkedList<>();

        for (final TradeLicense license : (List<TradeLicense>) searchCriteria.list()) {
            Installment currentInstallment = license.getCurrentDemand().getEgInstallmentMaster();
            List<Installment> previousInstallment = installmentDao
                    .fetchPreviousInstallmentsInDescendingOrderByModuleAndDate(
                            licenseUtils.getModule(TRADE_LICENSE), currentInstallment.getToDate(), 1);
            Map<String, Map<String, BigDecimal>> outstandingFees = getOutstandingFeeForDemandNotice(license,
                    currentInstallment, previousInstallment.get(0));
            Map<String, BigDecimal> licenseFees = outstandingFees.get(LICENSE_FEE_TYPE);
            finalList.add(new DemandnoticeForm(license, licenseFees, getOwnerName(license)));
        }
        return finalList;
    }

    public String getOwnerName(License license) {
        String ownerName;
        if (license.getState() != null) {
            final List<Assignment> assignmentList = assignmentService
                    .getAssignmentsForPosition(license.getState().getOwnerPosition().getId(), new Date());
            ownerName = !assignmentList.isEmpty() ? assignmentList.get(0).getEmployee().getName()
                    : license.getLastModifiedBy().getName();
        } else
            ownerName = license.getLastModifiedBy().getName();
        return ownerName;

    }

    public List<HashMap<String, Object>> populateHistory(final StateAware stateAware) {
        final List<HashMap<String, Object>> processHistoryDetails = new ArrayList<>();
        if (stateAware.hasState()) {
            State state = stateAware.getCurrentState();
            final HashMap<String, Object> currentStateDetail = new HashMap<>();
            currentStateDetail.put("date", state.getLastModifiedDate());
            currentStateDetail.put("updatedBy", state.getLastModifiedBy().getName());
            currentStateDetail.put("status", "END".equals(state.getValue()) ? "Completed" : state.getValue());
            currentStateDetail.put("comments", defaultString(state.getComments()));
            User ownerUser = state.getOwnerUser();
            Position ownerPosition = state.getOwnerPosition();
            if (ownerPosition != null) {
                User usr = eisCommonService.getUserForPosition(ownerPosition.getId(), state.getLastModifiedDate());
                currentStateDetail.put("user", usr == null ? EMPTY : usr.getName());
            } else
                currentStateDetail.put("user", ownerUser == null ? EMPTY : ownerUser.getName());

            processHistoryDetails.add(currentStateDetail);
            state.getHistory().stream().sorted(Comparator.comparing(StateHistory::getLastModifiedDate).reversed()).
                    forEach(sh -> processHistoryDetails.add(constructHistory(sh)));
        }
        return processHistoryDetails;
    }

    private HashMap<String, Object> constructHistory(StateHistory stateHistory) {
        final HashMap<String, Object> processHistory = new HashMap<>();
        processHistory.put("date", stateHistory.getLastModifiedDate());
        processHistory.put("updatedBy", stateHistory.getLastModifiedBy().getName());
        processHistory.put("status", "END".equals(stateHistory.getValue()) ? "Completed" : stateHistory.getValue());
        processHistory.put("comments", defaultString(stateHistory.getComments()));
        Position ownerPosition = stateHistory.getOwnerPosition();
        User ownerUser = stateHistory.getOwnerUser();
        if (ownerPosition != null) {
            User userPos = eisCommonService.getUserForPosition(ownerPosition.getId(), stateHistory.getLastModifiedDate());
            processHistory.put("user", userPos == null ? EMPTY : userPos.getName());
        } else
            processHistory.put("user", ownerUser == null ? EMPTY : ownerUser.getName());
        return processHistory;
    }

    @ReadOnly
    public List<License> getLicenses(Example license) {
        return licenseRepository.findAll(license);
    }
}