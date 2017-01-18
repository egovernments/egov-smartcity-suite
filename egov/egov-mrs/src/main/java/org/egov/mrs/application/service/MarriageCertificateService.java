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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.web.utils.WebUtils;
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

/**
 * Service class to generate the Marriage Registration/Reissue Certificate
 *
 * @author Pradeep
 *
 */
@Service
public class MarriageCertificateService {

    private static final String CITY_CODE = "cityCode";
    private static final String CERTIFICATE_DATE = "certificateDate";
    private static final String CERTIFICATE_TEMPLATE_REGISTRATION = "registrationcertificate";
    private static final String CERTIFICATE_TEMPLATE_REISSUE = "reissuecertificate";
    private static final String CERTIFICATE_TEMPLATE_REJECTION = "rejectioncertificate";
    private static final String[] MONTHNAME = { "January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December" };
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ReportService reportService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
    @Autowired
    private MarriageCertificateNumberGenerator marriageCertificateNumberGenerator;
    @Autowired
    private MarriageCertificateRepository marriageCertificateRepository;
    @Autowired
    private MarriageRegistrationReportsService marriageRegistrationReportsService;
    private InputStream generateCertificatePDF;

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
    public ReportOutput generate(final MarriageRegistration registration, final String cityName,
            final String logopath, final String certificateNo) throws IOException {
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
        reportParams.put("cityName", cityName);
        reportParams.put(CERTIFICATE_DATE, new Date());
        reportParams.put("logoPath", logopath);
        reportParams.put("registrationcenter", registration.getMarriageRegistrationUnit().getName());
        reportParams.put("registrarName", registration.getState().getSenderName().split("::")[1]);
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
            wifePhoto = FileUtils.readFileToByteArray(
                    fileStoreService.fetch(registration.getWife().getPhotoFileStore(), MarriageConstants.FILESTORE_MODULECODE));
        if (registration.getHusband() != null && registration.getHusband().getPhotoFileStore() != null)
            husbandPhoto = FileUtils.readFileToByteArray(fileStoreService.fetch(registration.getHusband().getPhotoFileStore(),
                    MarriageConstants.FILESTORE_MODULECODE));

        if (registration.getMarriagePhotoFileStore() != null)
            marriagePhoto = FileUtils.readFileToByteArray(
                    fileStoreService.fetch(registration.getMarriagePhotoFileStore(), MarriageConstants.FILESTORE_MODULECODE));
        reportInput = new ReportRequest(template,
                new RegistrationCertificate(registration, securityUtils.getCurrentUser(), husbandPhoto, wifePhoto, marriagePhoto),
                reportParams);
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
    public MarriageCertificate generateMarriageCertificate(final MarriageRegistration marriageRegistration,
            final HttpServletRequest request) throws IOException {
        MarriageCertificate marriageCertificate = null;
        ReportOutput reportOutput;
        final String cityName = request.getSession().getAttribute("citymunicipalityname").toString();
        final String url = WebUtils.extractRequestDomainURL(request, false);
        final String cityLogo = url.concat(MarriageConstants.IMAGE_CONTEXT_PATH)
                .concat((String) request.getSession().getAttribute("citylogo"));
        final String certificateNo = marriageCertificateNumberGenerator.generateCertificateNumber(marriageRegistration,
                request.getSession().getAttribute(CITY_CODE).toString());
        reportOutput = generate(marriageRegistration, cityName, cityLogo, certificateNo);
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
    public MarriageCertificate reIssueCertificate(final ReIssue reIssue,
            final HttpServletRequest request, final MarriageCertificateType certificateType) throws IOException {
        MarriageCertificate marriageCertificate = null;
        ReportOutput reportOutput;
        final String cityName = request.getSession().getAttribute("citymunicipalityname").toString();
        final String url = WebUtils.extractRequestDomainURL(request, false);
        final String cityLogo = url.concat(MarriageConstants.IMAGE_CONTEXT_PATH)
                .concat((String) request.getSession().getAttribute("citylogo"));
        final String certificateNo = marriageCertificateNumberGenerator.generateCertificateNumber(reIssue,
                request.getSession().getAttribute(CITY_CODE).toString());
        reportOutput = generateCertificate(reIssue, certificateType.name(), cityName, cityLogo, certificateNo);
        if (reportOutput != null && reportOutput.getReportOutputData() != null) {
            generateCertificatePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
            marriageCertificate = saveReIssuedCertificate(reIssue, generateCertificatePDF,
                    request.getSession().getAttribute(CITY_CODE).toString(), certificateType, certificateNo);
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
    public ReportOutput generateCertificate(final ReIssue reIssue, final String certificateType, final String cityName,
            final String logopath, final String certificateNo) throws IOException {
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
        calForApplnDate.setTime(reIssue.getApplicationDate());
        calForMrgDate.setTime(reIssue.getRegistration().getDateOfMarriage());
        reportParams.put("certificateno", certificateNo);

        reportParams.put("cityName", cityName);
        reportParams.put(CERTIFICATE_DATE, new Date());
        reportParams.put("logoPath", logopath);
        reportParams.put("registrationcenter", reIssue.getMarriageRegistrationUnit().getName());
        reportParams.put("registrarName", reIssue.getState().getSenderName().split("::")[1]);
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

        if (reIssue.getRegistration().getWife() != null && reIssue.getRegistration().getWife().getPhotoFileStore() != null)
            wifePhoto = FileUtils.readFileToByteArray(fileStoreService
                    .fetch(reIssue.getRegistration().getWife().getPhotoFileStore(), MarriageConstants.FILESTORE_MODULECODE));
        if (reIssue.getRegistration().getHusband() != null && reIssue.getRegistration().getHusband().getPhotoFileStore() != null)
            husbandPhoto = FileUtils.readFileToByteArray(fileStoreService
                    .fetch(reIssue.getRegistration().getHusband().getPhotoFileStore(), MarriageConstants.FILESTORE_MODULECODE));

        if (reIssue.getRegistration().getMarriagePhotoFileStore() != null)
            marriagePhoto = FileUtils.readFileToByteArray(fileStoreService
                    .fetch(reIssue.getRegistration().getMarriagePhotoFileStore(), MarriageConstants.FILESTORE_MODULECODE));
        if (template.equalsIgnoreCase(CERTIFICATE_TEMPLATE_REISSUE))
            reportInput = new ReportRequest(template,
                    new RegistrationCertificate(reIssue.getRegistration(), securityUtils.getCurrentUser(), husbandPhoto,
                            wifePhoto, marriagePhoto),
                    reportParams);
        else {
            reportParams.put("placeOfMarriage", reIssue.getRegistration().getPlaceOfMarriage());
            reportParams.put("zoneName",
                    reIssue.getRegistration().getZone() != null ? reIssue.getRegistration().getZone().getName() : "");
            reportParams.put("userName", reIssue.getState().getSenderName().split("::")[1]);
            reportParams.put("applicationNumber", reIssue.getRegistration().getApplicationNo());
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
    public MarriageCertificate saveReIssuedCertificate(final ReIssue reIssue,
            final InputStream fileStream, final String cityCode, final MarriageCertificateType type, final String certificateNo) {
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
        final Criteria criteria = getCurrentSession().createCriteria(MarriageCertificate.class, "certificate");

        if (certificate.getCertificateType() != null
                && MarriageCertificateType.REGISTRATION.toString().equals(certificate.getCertificateType().toString()))
            criteria.createAlias("certificate.registration", "registration");
        else if (certificate.getCertificateType() != null
                && MarriageCertificateType.REISSUE.toString().equals(certificate.getCertificateType().toString()))
            criteria.createAlias("certificate.reIssue", "reIssue");

        if (certificate.getRegistration() != null && certificate.getRegistration().getRegistrationNo() != null)
            criteria.add(Restrictions.ilike("registration.registrationNo",
                    certificate.getRegistration().getRegistrationNo().trim(), MatchMode.ANYWHERE));
        if (certificate.getCertificateNo() != null)
            criteria.add(Restrictions.ilike("certificateNo", certificate.getCertificateNo().trim(), MatchMode.ANYWHERE));
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
        return criteria.list();
    }

    public MarriageCertificate findById(final long id) {
        return marriageCertificateRepository.findOne(id);
    }

}