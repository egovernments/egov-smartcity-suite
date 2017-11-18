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

package org.egov.mrs.application.service;

import org.apache.commons.io.FileUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.application.reports.service.MarriageRegistrationReportsService;
import org.egov.mrs.autonumber.MarriageCertificateNumberGenerator;
import org.egov.mrs.domain.entity.MarriageCertificate;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.domain.entity.RegistrationCertificate;
import org.egov.mrs.domain.enums.MarriageCertificateType;
import org.egov.mrs.domain.repository.MarriageCertificateRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class to generate the Marriage Registration/Reissue Certificate
 *
 * @author Pradeep
 */
@Service
public class MarriageCertificateService {

    private static final String REGISTRAR_NAME = "registrarName";
    private static final String RE_ISSUE_DOT_REGISTRATION = "reIssue.registration";
    private static final String REGISTRATION = "registration";
    private static final String CERTIFICATE_DOT_REGISTRATION = "certificate.registration";
    private static final String REGISTRATION_DOT_REGISTRATION_NO = "registration.registrationNo";
    private static final String CERTIFICATE = "certificate";
    private static final String CERTIFICATE_DATE = "certificateDate";
    private static final String CERTIFICATE_TEMPLATE_REGISTRATION = "registrationcertificate";
    private static final String CERTIFICATE_TEMPLATE_REISSUE = "reissuecertificate";
    private static final String CERTIFICATE_TEMPLATE_REJECTION = "rejectioncertificate";
    private static final String[] MONTHNAME = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};
    private static final String COMMISSIONER = "Commissioner";
    private static final String IS_COMMISSIONER = "isCommissioner";
    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ReportService reportService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private MarriageCertificateNumberGenerator marriageCertificateNumberGenerator;
    @Autowired
    private MarriageCertificateRepository marriageCertificateRepository;
    @Autowired
    private MarriageRegistrationReportsService marriageRegistrationReportsService;
    private InputStream generateCertificatePDF;
    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private CityService cityService;

    private Session getCurrentSession() {

        return entityManager.unwrap(Session.class);
    }

    /**
     * Generates Marriage Registration Certificate and returns the reportOutput
     *
     * @param registration
     * @param certificateType
     * @param cityName
     * @param logopath
     * @return
     * @throws IOException
     */
    public ReportOutput generate(final MarriageRegistration registration, final String certificateNo) throws IOException {
        ReportRequest reportInput;
        ReportOutput reportOutput;
        final Calendar calForApplnDate = Calendar.getInstance();
        final Calendar calForMrgDate = Calendar.getInstance();
        calForApplnDate.setTime(registration.getApplicationDate());
        calForMrgDate.setTime(registration.getDateOfMarriage());
        final Map<String, Object> reportParams = new HashMap<>();
        final String template = CERTIFICATE_TEMPLATE_REGISTRATION;
        byte[] husbandPhoto = null;
        byte[] wifePhoto = null;
        byte[] marriagePhoto = null;
        final User user = securityUtils.getCurrentUser();
        List<Assignment> loggedInUserAssignmentForRegistration;
        String loggedInUserDesignationForReg;
        loggedInUserAssignmentForRegistration = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                registration.getCurrentState().getOwnerPosition().getId(), user.getId(), new Date());
        loggedInUserDesignationForReg = !loggedInUserAssignmentForRegistration.isEmpty()
                ? loggedInUserAssignmentForRegistration.get(0).getDesignation().getName() : null;
        if (loggedInUserDesignationForReg != null && COMMISSIONER.equalsIgnoreCase(loggedInUserDesignationForReg))
            reportParams.put(IS_COMMISSIONER, true);
        else
            reportParams.put(IS_COMMISSIONER, false);
        reportParams.put("userSignature",
                user.getSignature() != null ? new ByteArrayInputStream(user.getSignature()) : "");

        reportParams.put("cityName", cityService.getMunicipalityName());
        reportParams.put(CERTIFICATE_DATE, new Date());
        reportParams.put("logoPath", cityService.getCityLogoURL());
        reportParams.put("registrationcenter", registration.getMarriageRegistrationUnit().getName());
        if (registration.getRegistrarName() != null && !"".equals(registration.getRegistrarName()))
            reportParams.put(REGISTRAR_NAME, registration.getRegistrarName());
        else
            reportParams.put(REGISTRAR_NAME, registration.getState().getSenderName().split("::")[1]);

        reportParams.put("husbandParentName", registration.getHusband().getParentsName());
        reportParams.put("wifeParentName", registration.getWife().getParentsName());
        reportParams.put("applicationdateday", calForApplnDate.get(Calendar.DAY_OF_MONTH)
                + getDayNumberSuffix(calForApplnDate.get(Calendar.DAY_OF_MONTH)));
        reportParams.put("applicationdatemonth", MONTHNAME[calForApplnDate.get(Calendar.MONTH)]);
        reportParams.put("applicationdateyear", calForApplnDate.get(Calendar.YEAR));
        reportParams.put("marriagedateday", calForMrgDate.get(Calendar.DAY_OF_MONTH)
                + getDayNumberSuffix(calForMrgDate.get(Calendar.DAY_OF_MONTH)));
        reportParams.put("marriagedatemonth", MONTHNAME[calForMrgDate.get(Calendar.MONTH)]);
        reportParams.put("marriagedateyear", calForMrgDate.get(Calendar.YEAR));

        reportParams.put("serialno", registration.getSerialNo());
        reportParams.put("pageno", registration.getPageNo());
        reportParams.put("certificateno", certificateNo);
        if (registration.getWife() != null && registration.getWife().getPhotoFileStore() != null)
            wifePhoto = FileUtils.readFileToByteArray(fileStoreService.fetch(registration.getWife().getPhotoFileStore(),
                    MarriageConstants.FILESTORE_MODULECODE));
        if (registration.getHusband() != null && registration.getHusband().getPhotoFileStore() != null)
            husbandPhoto = FileUtils.readFileToByteArray(fileStoreService
                    .fetch(registration.getHusband().getPhotoFileStore(), MarriageConstants.FILESTORE_MODULECODE));

        if (registration.getMarriagePhotoFileStore() != null)
            marriagePhoto = FileUtils.readFileToByteArray(fileStoreService
                    .fetch(registration.getMarriagePhotoFileStore(), MarriageConstants.FILESTORE_MODULECODE));
        reportInput = new ReportRequest(template, new RegistrationCertificate(registration,
                securityUtils.getCurrentUser(), husbandPhoto, wifePhoto, marriagePhoto), reportParams);
        reportOutput = reportService.createReport(reportInput);
        return reportOutput;
    }

    // append the suffix st,nd,rd,th with day
    private String getDayNumberSuffix(final int day) {
        if (day >= 11 && day <= 13)
            return "th";
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    /**
     * @param marriageRegistration
     * @param request
     * @return
     * @throws IOException
     */
    public MarriageCertificate generateMarriageCertificate(final MarriageRegistration marriageRegistration) throws IOException {
        MarriageCertificate marriageCertificate = null;
        ReportOutput reportOutput;
        final String certificateNo = marriageCertificateNumberGenerator.generateCertificateNumber(marriageRegistration);
        reportOutput = generate(marriageRegistration, certificateNo);
        if (reportOutput != null && reportOutput.getReportOutputData() != null) {
            generateCertificatePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
            marriageCertificate = saveRegisteredCertificate(marriageRegistration, generateCertificatePDF,
                    certificateNo);
        }
        return marriageCertificate;
    }

    /**
     * @param marriageRegistration
     * @param fileStream
     * @param cityCode
     * @return
     */
    public MarriageCertificate saveRegisteredCertificate(final MarriageRegistration marriageRegistration,
                                                         final InputStream fileStream, final String certificateNo) {
        final MarriageCertificate marriageCertificate = new MarriageCertificate();
        if (marriageRegistration != null) {
            final String fileName = certificateNo + ".pdf";
            buildCertificate(marriageRegistration, null, marriageCertificate, certificateNo,
                    MarriageCertificateType.REGISTRATION);
            final FileStoreMapper fileStore = fileStoreService.store(fileStream, fileName, "application/pdf",
                    MarriageConstants.FILESTORE_MODULECODE);
            marriageCertificate.setFileStore(fileStore);
        }
        return marriageCertificate;
    }

    /**
     * @param marriageRegistration
     * @param reIssue
     * @param marriageCertificate
     * @param certificateNumber
     * @param certificateType
     */
    private void buildCertificate(final MarriageRegistration marriageRegistration, final ReIssue reIssue,
                                  final MarriageCertificate marriageCertificate, final String certificateNumber,
                                  final MarriageCertificateType certificateType) {
        marriageCertificate.setCertificateDate(new Date());
        marriageCertificate.setCertificateIssued(true);
        marriageCertificate.setCertificateNo(certificateNumber);
        marriageCertificate.setCertificateType(certificateType);
        marriageCertificate.setRegistration(marriageRegistration);
        marriageCertificate.setReIssue(reIssue);
    }

    /**
     * @param reIssue
     * @param request
     * @return
     * @throws IOException
     */
    public MarriageCertificate reIssueCertificate(ReIssue reIssue, MarriageCertificateType certificateType) throws IOException {
        MarriageCertificate marriageCertificate = null;
        ReportOutput reportOutput;
        final String certificateNo = marriageCertificateNumberGenerator.generateCertificateNumber(reIssue);
        reportOutput = generateCertificate(reIssue, certificateType.name(), certificateNo);
        if (reportOutput != null && reportOutput.getReportOutputData() != null) {
            generateCertificatePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
            marriageCertificate = saveReIssuedCertificate(reIssue, generateCertificatePDF, certificateType, certificateNo);
        }
        return marriageCertificate;
    }

    /**
     * @param reIssue
     * @param certificateType
     * @param cityName
     * @param logopath
     * @return
     * @throws IOException
     */
    public ReportOutput generateCertificate(final ReIssue reIssue, final String certificateType, final String certificateNo) throws IOException {
        ReportRequest reportInput;
        ReportOutput reportOutput;
        final Map<String, Object> reportParams = new HashMap<>();
        final String template = certificateType.equalsIgnoreCase(MarriageCertificateType.REISSUE.toString())
                ? CERTIFICATE_TEMPLATE_REISSUE : CERTIFICATE_TEMPLATE_REJECTION;
        byte[] husbandPhoto = null;
        byte[] wifePhoto = null;
        byte[] marriagePhoto = null;
        final Calendar calForApplnDate = Calendar.getInstance();
        final Calendar calForMrgDate = Calendar.getInstance();
        calForApplnDate.setTime(reIssue.getRegistration().getApplicationDate());
        calForMrgDate.setTime(reIssue.getRegistration().getDateOfMarriage());
        reportParams.put("certificateno", certificateNo);
        final User reissueUser = securityUtils.getCurrentUser();
        List<Assignment> loggedInUserAssignmentForReissue;
        String loggedInUserDesignationForReissue;
        loggedInUserAssignmentForReissue = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                reIssue.getCurrentState().getOwnerPosition().getId(), reissueUser.getId(), new Date());
        loggedInUserDesignationForReissue = !loggedInUserAssignmentForReissue.isEmpty()
                ? loggedInUserAssignmentForReissue.get(0).getDesignation().getName() : null;
        if (loggedInUserDesignationForReissue != null && COMMISSIONER.equalsIgnoreCase(loggedInUserDesignationForReissue))
            reportParams.put(IS_COMMISSIONER, true);
        else
            reportParams.put(IS_COMMISSIONER, false);
        reportParams.put("userSignature",
                reissueUser.getSignature() != null ? new ByteArrayInputStream(reissueUser.getSignature()) : "");

        reportParams.put("cityName", cityService.getMunicipalityName());
        reportParams.put(CERTIFICATE_DATE, new Date());
        reportParams.put("logoPath", cityService.getCityLogoURL());
        reportParams.put("registrationcenter", reIssue.getMarriageRegistrationUnit().getName());

        if (reIssue.getRegistration().getRegistrarName() != null
                && !"".equals(reIssue.getRegistration().getRegistrarName()))
            reportParams.put(REGISTRAR_NAME, reIssue.getRegistration().getRegistrarName());
        else
            reportParams.put(REGISTRAR_NAME, reIssue.getState().getSenderName().split("::")[1]);

        reportParams.put("husbandParentName", reIssue.getRegistration().getHusband().getParentsName());
        reportParams.put("wifeParentName", reIssue.getRegistration().getWife().getParentsName());
        reportParams.put("applicationdateday", calForApplnDate.get(Calendar.DAY_OF_MONTH)
                + getDayNumberSuffix(calForApplnDate.get(Calendar.DAY_OF_MONTH)));
        reportParams.put("applicationdatemonth", MONTHNAME[calForApplnDate.get(Calendar.MONTH)]);
        reportParams.put("applicationdateyear", calForApplnDate.get(Calendar.YEAR));
        reportParams.put("marriagedateday", calForMrgDate.get(Calendar.DAY_OF_MONTH)
                + getDayNumberSuffix(calForMrgDate.get(Calendar.DAY_OF_MONTH)));
        reportParams.put("marriagedatemonth", MONTHNAME[calForMrgDate.get(Calendar.MONTH)]);
        reportParams.put("marriagedateyear", calForMrgDate.get(Calendar.YEAR));

        reportParams.put("serialno", reIssue.getRegistration().getSerialNo());
        reportParams.put("pageno", reIssue.getRegistration().getPageNo());

        if (reIssue.getRegistration().getWife() != null
                && reIssue.getRegistration().getWife().getPhotoFileStore() != null)
            wifePhoto = FileUtils.readFileToByteArray(fileStoreService.fetch(
                    reIssue.getRegistration().getWife().getPhotoFileStore(), MarriageConstants.FILESTORE_MODULECODE));
        if (reIssue.getRegistration().getHusband() != null
                && reIssue.getRegistration().getHusband().getPhotoFileStore() != null)
            husbandPhoto = FileUtils.readFileToByteArray(
                    fileStoreService.fetch(reIssue.getRegistration().getHusband().getPhotoFileStore(),
                            MarriageConstants.FILESTORE_MODULECODE));

        if (reIssue.getRegistration().getMarriagePhotoFileStore() != null)
            marriagePhoto = FileUtils.readFileToByteArray(fileStoreService.fetch(
                    reIssue.getRegistration().getMarriagePhotoFileStore(), MarriageConstants.FILESTORE_MODULECODE));
        if (template.equalsIgnoreCase(CERTIFICATE_TEMPLATE_REISSUE)) {
            reportParams.put("applicationNumber", reIssue.getApplicationNo());
            reportParams.put("zoneName", reIssue.getZone().getName());
            reportInput = new ReportRequest(template, new RegistrationCertificate(reIssue.getRegistration(),
                    securityUtils.getCurrentUser(), husbandPhoto, wifePhoto, marriagePhoto), reportParams);
        } else {
            reportParams.put("placeOfMarriage", reIssue.getRegistration().getPlaceOfMarriage());
            reportParams.put("zoneName",
                    reIssue.getRegistration().getZone() != null ? reIssue.getRegistration().getZone().getName() : "");
            reportParams.put("userName", reIssue.getState().getSenderName().split("::")[1]);
            reportParams.put("applicationNumber", reIssue.getApplicationNo());
            reportParams.put("registrationNumber", reIssue.getRegistration().getRegistrationNo());

            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            if (reIssue.getState().getCreatedDate() != null)
                reportParams.put("rejectionDate", sdf.format(reIssue.getState().getCreatedDate()));
            reportInput = new ReportRequest(template, reIssue, reportParams);
        }
        reportOutput = reportService.createReport(reportInput);
        return reportOutput;
    }

    /**
     * @param reIssue
     * @param fileStream
     * @param cityCode
     * @return
     */
    public MarriageCertificate saveReIssuedCertificate(final ReIssue reIssue, final InputStream fileStream,
                                                       final MarriageCertificateType type, final String certificateNo) {
        final MarriageCertificate marriageCertificate = new MarriageCertificate();
        if (reIssue != null) {
            final String fileName = certificateNo + ".pdf";
            buildCertificate(null, reIssue, marriageCertificate, certificateNo, type);
            final FileStoreMapper fileStore = fileStoreService.store(fileStream, fileName, "application/pdf",
                    MarriageConstants.FILESTORE_MODULECODE);
            marriageCertificate.setFileStore(fileStore);
        }
        return marriageCertificate;
    }

    @SuppressWarnings("unchecked")
    public List<MarriageCertificate> searchMarriageCertificates(final MarriageCertificate certificate) {
        final Criteria criteria = getCurrentSession().createCriteria(MarriageCertificate.class, CERTIFICATE);
        final List<MarriageCertificate> certificateResultList = new ArrayList<>();
        if (certificate.getRegistration().getRegistrationNo() != null) {
            criteria.createAlias(CERTIFICATE_DOT_REGISTRATION, REGISTRATION);
            criteria.add(Restrictions.eq(REGISTRATION_DOT_REGISTRATION_NO,
                    certificate.getRegistration().getRegistrationNo().trim()));
        }
        buildSearchCriteriaForMrgCertificate(certificate, criteria);
        certificateResultList.addAll(criteria.list());
        // To fetch reissue certificate details along with registration
        // certificate details when searching
        // using registration number
        if (certificate.getRegistration().getRegistrationNo() != null) {
            final Criteria criteriaForReissue = getCurrentSession().createCriteria(MarriageCertificate.class, "cert");
            criteriaForReissue.createAlias("cert.reIssue", "reIssue").createAlias(RE_ISSUE_DOT_REGISTRATION, "reg");
            criteriaForReissue.add(
                    Restrictions.eq("reg.registrationNo", certificate.getRegistration().getRegistrationNo().trim()));
            buildSearchCriteriaForMrgCertificate(certificate, criteriaForReissue);
            certificateResultList.addAll(criteriaForReissue.list());
        }

        return certificateResultList;
    }

    private void buildSearchCriteriaForMrgCertificate(final MarriageCertificate certificate, final Criteria criteria) {
        if (certificate.getCertificateNo() != null)
            criteria.add(
                    Restrictions.ilike("certificateNo", certificate.getCertificateNo().trim(), MatchMode.ANYWHERE));
        if (certificate.getCertificateType() != null)
            criteria.add(Restrictions.eq("certificateType", certificate.getCertificateType()));
        if (certificate.getFromDate() != null)
            criteria.add(Restrictions.ge(CERTIFICATE_DATE,
                    marriageRegistrationReportsService.resetFromDateTimeStamp(certificate.getFromDate())));
        if (certificate.getToDate() != null)
            criteria.add(Restrictions.le(CERTIFICATE_DATE,
                    marriageRegistrationReportsService.resetToDateTimeStamp(certificate.getToDate())));
        if (certificate.getFrequency() != null && "LATEST".equalsIgnoreCase(certificate.getFrequency()))
            criteria.addOrder(Order.desc("createdDate"));
    }

    public MarriageCertificate findById(final long id) {
        return marriageCertificateRepository.findOne(id);
    }

    @SuppressWarnings("unchecked")
    public List<MarriageCertificate> getGeneratedRegCertificate(final MarriageRegistration registration) {
        final Criteria criteriaReg = getCurrentSession().createCriteria(MarriageCertificate.class, CERTIFICATE);
        if (registration != null && registration.getRegistrationNo() != null) {
            criteriaReg.createAlias(CERTIFICATE_DOT_REGISTRATION, REGISTRATION);
            criteriaReg.add(Restrictions.eq(REGISTRATION_DOT_REGISTRATION_NO, registration.getRegistrationNo().trim()));
        }
        return criteriaReg.list();
    }

    @SuppressWarnings("unchecked")
    public List<MarriageCertificate> getGeneratedReIssueCertificate(final ReIssue reIssue) {
        final Criteria criteriaReissue = getCurrentSession().createCriteria(MarriageCertificate.class, CERTIFICATE);
        if (reIssue != null && reIssue.getRegistration() != null
                && reIssue.getRegistration().getRegistrationNo() != null) {
            criteriaReissue.createAlias("certificate.reIssue", "reIssue").createAlias(RE_ISSUE_DOT_REGISTRATION, "reg");
            criteriaReissue
                    .add(Restrictions.eq("reg.registrationNo", reIssue.getRegistration().getRegistrationNo().trim()));
        }
        return criteriaReissue.list();
    }

    public MarriageCertificate getGeneratedCertificate(final MarriageRegistration registration) {
        return marriageCertificateRepository.findByRegistration(registration);
    }

    public MarriageCertificate getGeneratedReIssueCertificateForPrint(final ReIssue reIssue) {
        return marriageCertificateRepository.findByReIssue(reIssue);
    }

}
