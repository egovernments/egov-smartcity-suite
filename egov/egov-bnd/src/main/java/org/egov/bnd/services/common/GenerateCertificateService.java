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
package org.egov.bnd.services.common;

import org.egov.bnd.model.BirthRegistration;
import org.egov.bnd.model.DeathRegistration;
import org.egov.bnd.model.NonAvailability;
import org.egov.bnd.services.registration.BirthRegistrationService;
import org.egov.bnd.services.registration.DeathRegistrationService;
import org.egov.bnd.services.registration.NonAvailabilityRegistrationService;
import org.egov.bnd.utils.BndConstants;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a common service class defined for bnd module. This class will be
 * used to generate certificate for birth/death and Non availability
 * certificate.
 *
 * @author Pradeep Kumar
 */

@Transactional(readOnly = true)
public class GenerateCertificateService {

    private PersistenceService persistenceService;
    private BndCommonService bndCommonService;
    private BirthRegistrationService birthRegistrationService;
    private DeathRegistrationService deathRegistrationService;
    private NonAvailabilityRegistrationService nonAvailableRegService;
    protected ReportService reportService;

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public BndCommonService getBndCommonService() {
        return bndCommonService;
    }

    public BirthRegistrationService getBirthRegistrationService() {
        return birthRegistrationService;
    }

    public DeathRegistrationService getDeathRegistrationService() {
        return deathRegistrationService;
    }

    public NonAvailabilityRegistrationService getNonAvailableRegService() {
        return nonAvailableRegService;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void setBndCommonService(final BndCommonService bndCommonService) {
        this.bndCommonService = bndCommonService;
    }

    public void setBirthRegistrationService(final BirthRegistrationService birthRegistrationService) {
        this.birthRegistrationService = birthRegistrationService;
    }

    public void setDeathRegistrationService(final DeathRegistrationService deathRegistrationService) {
        this.deathRegistrationService = deathRegistrationService;
    }

    public void setNonAvailableRegService(final NonAvailabilityRegistrationService nonAvailableRegService) {
        this.nonAvailableRegService = nonAvailableRegService;
    }

    /*
     * public ReportOutput generateCertificateBirthDeathNA(Long idTemp, String
     * recordType,String roleName,String templateName,Map<String, Object>
     * session) { ReportRequest reportInput = null; if(templateName==null ||
     * "".equals(templateName)) { if(recordType!=null && recordType.equals(
     * BndConstants.SEARCHBIRTH))
     * templateName=BndConstants.BIRTHREGISTRATION_TEMPLATE; else
     * if(recordType!=null && recordType.equals( BndConstants.SEARCHDEATH))
     * templateName=BndConstants.DEATHREGISTRATION_TEMPLATE; }
     * if(recordType!=null && recordType.equals( BndConstants.SEARCHBIRTH)) {
     * reportInput = buildReportRequestForBirthRecord(idTemp, roleName,
     * templateName); } else if(recordType!=null && recordType.equals(
     * BndConstants.SEARCHDEATH)){ reportInput
     * =buildReportRequestForDeathRecord(idTemp, roleName, templateName); }else
     * if(recordType!=null && recordType.equals(
     * BndConstants.SEARCHNONAVAILABILITY)){ reportInput
     * =buildReportRequestForNARecord(idTemp, roleName, templateName); }
     * ReportOutput reportOutput = reportService.createReport(reportInput);
     * return reportOutput; }
     */

    public Integer generateCertificate(final Long idTemp, final String recordType, final String roleName,
            String templateName, final Map<String, Object> session) {
        ReportRequest reportInput = null;

        if (templateName == null || "".equals(templateName))
            if (recordType != null && recordType.equals(BndConstants.SEARCHBIRTH))
                templateName = BndConstants.BIRTHREGISTRATION_TEMPLATE;
            else if (recordType != null && recordType.equals(BndConstants.SEARCHDEATH))
                templateName = BndConstants.DEATHREGISTRATION_TEMPLATE;

        if (recordType != null && recordType.equals(BndConstants.SEARCHBIRTH))
            reportInput = buildReportRequestForBirthRecord(idTemp, roleName, templateName);
        else if (recordType != null && recordType.equals(BndConstants.SEARCHDEATH))
            reportInput = buildReportRequestForDeathRecord(idTemp, roleName, templateName);
        else if (recordType != null && recordType.equals(BndConstants.SEARCHNONAVAILABILITY))
            reportInput = buildReportRequestForNARecord(idTemp, roleName, templateName);

        final ReportOutput reportOutput = reportService.createReport(reportInput);

        return addingReportToSession(reportOutput, session);
    }

    @Transactional
    private ReportRequest buildReportRequestForNARecord(final Long idTemp, final String roleName,
            final String templateName) {
        ReportRequest reportInput;
        final List<NonAvailability> list = new ArrayList<NonAvailability>();
        final NonAvailability nonAvailability = nonAvailableRegService.getNonAvailableRegById(idTemp);
        list.add(nonAvailability);
        final HashMap<String, Object> registrationMapObject = mapForReportGenerationForNARecord(nonAvailability,
                roleName);
        reportInput = new ReportRequest(templateName, list, registrationMapObject);
        return reportInput;
    }

    @Transactional
    private HashMap<String, Object> mapForReportGenerationForNARecord(final NonAvailability nonAvailability,
            final String roleName) {
        final HashMap<String, Object> registrationMapObject = new HashMap<String, Object>();

        final HttpServletRequest request = null; //FIX ME : ServletActionContext outside Action class, which does not work outside struts2 context //ServletActionContext.getRequest();

        if (nonAvailability != null && nonAvailability.getCitizenName() != null
                && !"".equals(nonAvailability.getCitizenName()))
            registrationMapObject.put("NAME", nonAvailability.getCitizenName().toUpperCase());
        else
            registrationMapObject.put("NAME", "********");

        if (nonAvailability != null && nonAvailability.getCitizenRelationName() != null
                && !"".equals(nonAvailability.getCitizenRelationName()))
            registrationMapObject.put("RELATIONNAME", nonAvailability.getCitizenRelationName().toUpperCase());
        else
            registrationMapObject.put("RELATIONNAME", "********");

        if (nonAvailability != null && nonAvailability.getCitizenRelationType() != null
                && !"".equals(nonAvailability.getCitizenRelationType()))
            registrationMapObject.put("RELATIONTYPE", nonAvailability.getCitizenRelationType().getDesc().toUpperCase());
        else
            registrationMapObject.put("RELATIONTYPE", "********");

        if (nonAvailability != null && nonAvailability.getEventType() != null
                && !"".equals(nonAvailability.getEventType()))
            registrationMapObject.put("EVENTTYPE", nonAvailability.getEventType());
        else
            registrationMapObject.put("EVENTTYPE", "********");

        if (nonAvailability != null && nonAvailability.getYearOfEvent() != null
                && !"".equals(nonAvailability.getYearOfEvent()))
            registrationMapObject.put("REGISTRATIONYEAR", nonAvailability.getYearOfEvent().toString());
        else
            registrationMapObject.put("REGISTRATIONYEAR", "********");

        if (nonAvailability != null && nonAvailability.getApplicantName() != null
                && !"".equals(nonAvailability.getApplicantName()))
            registrationMapObject.put("APPLICANTNAME", nonAvailability.getApplicantName().toUpperCase());
        else
            registrationMapObject.put("APPLICANTNAME", "********");

        if (nonAvailability != null && nonAvailability.getApplicantRelationName() != null
                && !"".equals(nonAvailability.getApplicantRelationName()))
            registrationMapObject
            .put("APPLICANTRELATIONNAME", nonAvailability.getApplicantRelationName().toUpperCase());
        else
            registrationMapObject.put("APPLICANTRELATIONNAME", "********");

        if (nonAvailability != null && nonAvailability.getApplicantRelationType() != null
                && !"".equals(nonAvailability.getApplicantRelationType()))
            registrationMapObject.put("APPLICANTRELATIONTYPE", nonAvailability.getApplicantRelationType().getDesc()
                    .toUpperCase());
        else
            registrationMapObject.put("APPLICANTRELATIONTYPE", "********");

        // registrationMapObject.put("LOGOINDIAPATH",BndConstants.DEFAULTLOGONAME);
        registrationMapObject.put("LOGOINDIAPATH", bndCommonService.getIndiaImage(request));
        registrationMapObject.put("LOGOOFCITYPATH", bndCommonService.getCityLogoName(request));

        // TODO: CHANGE THE CERTIFICATE NUMBER AS PER RECEIPT NUMBER
        registrationMapObject.put("CERTIFICATENUMBER", nonAvailability.getRegistrationNo());

        return registrationMapObject;
    }

    @Transactional
    private ReportRequest buildReportRequestForBirthRecord(final Long idTemp, final String roleName,
            final String templateName) {
        ReportRequest reportInput;
        final List<BirthRegistration> list = new ArrayList<BirthRegistration>();
        final BirthRegistration birthRegistration = birthRegistrationService.getBirthRegistrationById(idTemp);
        list.add(birthRegistration);

        if (birthRegistration != null && birthRegistration.getIsCertIssued() != null
                && birthRegistration.getIsCertIssued().equals("N"))
            birthRegistration.setIsCertIssued("Y");

        final Long userId = birthRegistration.getCreatedBy() == null ? EgovThreadLocals.getUserId()
                : birthRegistration.getCreatedBy().getId();

        bndCommonService.getRoleNamesByPassingUserId(userId);

        final HashMap<String, Object> registrationMapObject = mapForReportGenerationForBirthRecord(birthRegistration,
                roleName);
        reportInput = new ReportRequest(templateName, list, registrationMapObject);
        return reportInput;
    }

    protected Integer addingReportToSession(final ReportOutput reportOutput, final Map<String, Object> session) {
        return ReportViewerUtil.addReportToSession(reportOutput, session);
    }

    public HashMap<String, Object> mapForReportGenerationForBirthRecord(final BirthRegistration birthRegistration,
            final String roleName) {
        final HashMap<String, Object> registrationMapObject = new HashMap<String, Object>();

        final HttpServletRequest request = null;//ServletActionContext.getRequest();//FIX ME : ServletActionContext outside Action class, which does not work outside struts2 context

        // TODO: view birth record should be given
        final StringBuffer urlPatter = buildUrlPatterForBirthCertificate(birthRegistration, request);
        registrationMapObject.put("URL", urlPatter.toString());

        // TODO egifix
        if (!birthRegistration.getCitizen().getName().equals("NA"))
            registrationMapObject.put("NAME", birthRegistration.getCitizenName().toString().toUpperCase());
        else
            registrationMapObject.put("NAME", "********");

        registrationMapObject.put("CERTIFICATENUMBER", birthRegistration.getRegistrationNo());

        // TODO egifix
        // registrationMapObject.put("SEX",
        // birthRegistration.getCitizen().getSex().toUpperCase());

        registrationMapObject.put("dateOfEventInWords", DateUtils.convertToWords(birthRegistration.getDateOfEvent()));
        registrationMapObject.put("REGISTRATIONNUMBER", birthRegistration.getRegistrationNo());
        registrationMapObject.put("REGISTRATIONUNITNAME", birthRegistration.getRegistrationUnit().getRegUnitDesc());

        if (birthRegistration.getPlaceType().getDesc().equals("Hospital"))
            registrationMapObject.put("PLACEOFBIRTH",
                    (birthRegistration.getPlaceType().getDesc() != null ? birthRegistration.getPlaceType().getDesc()
                            .toUpperCase() : "")
                            + " " + birthRegistration.getEstablishment().getName().toUpperCase());
        else if (birthRegistration.getPlaceType().getDesc().equals("House"))
            registrationMapObject.put("PLACEOFBIRTH",
                    (birthRegistration.getPlaceType().getDesc() != null ? birthRegistration.getPlaceType().getDesc()
                            .toUpperCase() : "")
                            + " " + getAddressAsStringFromAddressObject(birthRegistration.getEventAddress()));
        else if (birthRegistration.getPlaceType().getDesc().equals("Other"))
            registrationMapObject.put("PLACEOFBIRTH",
                    (birthRegistration.getPlaceType().getDesc() != null ? birthRegistration.getPlaceType().getDesc()
                            .toUpperCase() : "")
                            + " " + getAddressAsStringFromAddressObject(birthRegistration.getEventAddress()));
        else if (birthRegistration.getPlaceType().getDesc().equals("Not Stated"))
            registrationMapObject.put("PLACEOFBIRTH",
                    birthRegistration.getPlaceType().getDesc() != null ? birthRegistration.getPlaceType().getDesc()
                            .toUpperCase() : "");

        if (birthRegistration.getIsChildAdopted() != null && birthRegistration.getIsChildAdopted().equals(Boolean.TRUE)) {
            if (birthRegistration.getAdoptionDetail() != null) {
                registrationMapObject.put("FATHERNAME", birthRegistration.getFatherFullName().toString().toUpperCase());
                registrationMapObject.put("MOTHERNAME", birthRegistration.getMotherFullName().toString().toUpperCase());
                registrationMapObject.put("PARENTADDRESS",
                        getAddressAsStringFromAddressObject(birthRegistration.getAdoptionDetail().getAdopteeAddress())
                        .toUpperCase());
                registrationMapObject
                .put("ADOPTIONFOOTERMESSAGE",
                        "This Birth Certificate is being issued following the order of the  N.G.P. REG. OFFICE, DEED OF ADOPTION  Court bearing No. "
                                + birthRegistration.getAdoptionDetail().getAdoptionNumber()
                                + " Date :"
                                + birthRegistration.getAdoptionDetail().getAdoptionDate()
                                + " declaring the parents mentioned above as adoptive Parents/Guardians of the above mentioned child.");
            } else {
                registrationMapObject.put("FATHERNAME", "");
                registrationMapObject.put("MOTHERNAME", "");
                registrationMapObject.put("PARENTADDRESS", "");
                registrationMapObject.put("ADOPTIONFOOTERMESSAGE", "");
            }

        } else {
            registrationMapObject.put("FATHERNAME", birthRegistration.getFatherFullName().toString().toUpperCase());
            registrationMapObject.put("MOTHERNAME", birthRegistration.getMotherFullName().toString().toUpperCase());
            registrationMapObject.put(
                    "PARENTADDRESS",
                    getAddressAsStringFromAddressObject(
                            birthRegistration.getCitizen().getRelatedPerson(BndConstants.FATHER.toUpperCase())
                            .getRelatedAddress(BndConstants.CORRESPONDINGADDRESS)).toUpperCase());
            registrationMapObject.put("ADOPTIONFOOTERMESSAGE", "");
        }
        if (birthRegistration.getCitizen() != null)
            registrationMapObject.put("PERMNENTADDRESS",
                    getAddressAsStringFromAddressObject(birthRegistration.getPermanentCitizenAddress()).toUpperCase());
        registrationMapObject.put("REMARKS", birthRegistration.getRemarks() != null ? birthRegistration.getRemarks()
                .toUpperCase() : "");
        registrationMapObject.put("LOGOINDIAPATH", bndCommonService.getIndiaImage(request));
        registrationMapObject.put("LOGOOFCITYPATH", bndCommonService.getCityLogoName(request));

        /*
         * ReportUtil.getUserSignature(userId) expects an user id to generate
         * signature hence use egovernments user whose id=1
         */
        registrationMapObject.put("USERSIGNPATH", "");

        registrationMapObject.put("ROLENAME", roleName);
        // TODO: when hospitaluser enters the record n tries to generate
        // certificate digital signature of the respected registrar should be
        // shown
        if (roleName != null && roleName.equalsIgnoreCase(BndConstants.HOSPITALUSER)) {
            // setUserSignImagePath(registrationMapObject,birthRegistration);
            // registrationMapObject.put("USERSIGNPATH",birthRegistration.getState().getModifiedBy().getId().toString());

        }
        return registrationMapObject;
    }

    private StringBuffer buildUrlPatterForBirthCertificate(final BirthRegistration birthRegistration,
            final HttpServletRequest request) {
        final StringBuffer urlPatter = new StringBuffer();
        // urlPatter.append("http://");
        String urlStr = HttpUtils.getRequestURL(request).toString();

        urlStr = WebUtils.extractRequestedDomainName(request);
        urlPatter.append("http://");
        // domain name
        urlPatter.append(urlStr);

        urlPatter.append(":");

        urlPatter.append(request.getServerPort());
        final List<String> appConfig = bndCommonService.getAppconfigActualValue(BndConstants.BNDMODULE,
                BndConstants.BIRTHVIEWURL);

        // context root
        urlPatter.append(appConfig.get(0));

        urlPatter.append("idTemp=" + birthRegistration.getId());

        return urlPatter;

    }

    @Transactional
    private ReportRequest buildReportRequestForDeathRecord(final Long idTemp, final String roleName,
            final String templateName) {
        ReportRequest reportInput;
        final List<DeathRegistration> list = new ArrayList<DeathRegistration>();
        final DeathRegistration deathRegistration = deathRegistrationService.getDeathRegistrationById(idTemp);
        list.add(deathRegistration);

        if (deathRegistration != null && deathRegistration.getIsCertIssued() != null
                && deathRegistration.getIsCertIssued().equals("N"))
            deathRegistration.setIsCertIssued("Y");

        final HashMap<String, Object> registrationMapObject = mapForReportGenerationForDeathRecord(deathRegistration,
                roleName);
        reportInput = new ReportRequest(templateName, list, registrationMapObject);
        return reportInput;
    }

    public HashMap<String, Object> mapForReportGenerationForDeathRecord(final DeathRegistration deathRegistration,
            final String roleName) {
        final HashMap<String, Object> registrationMapObject = new HashMap<String, Object>();

        final HttpServletRequest request = null /*ServletActionContext.getRequest()*/;//FIX ME : ServletActionContext outside Action class, which does not work outside struts2 context

        final StringBuffer urlPatter = buildUrlPatterForDeathCertificate(deathRegistration, request);
        registrationMapObject.put("URL", urlPatter.toString());

        // TODO egifix
        /*
         * if (!deathRegistration.getCitizen().getFirstName().equals("NA"))
         * registrationMapObject.put("NAME",
         * deathRegistration.getCitizenName().toString()); else
         * registrationMapObject.put("NAME", "********");
         */

        registrationMapObject.put("CERTIFICATENUMBER", deathRegistration.getRegistrationNo());

        // TODO egifix
        // registrationMapObject.put("SEX",
        // deathRegistration.getCitizen().getSex());

        // registrationMapObject.put("DATEOFDEATH",
        // DateUtils.formatDateInWords(deathRegistration.getDateOfEvent()));
        registrationMapObject.put("dateOfEventInWords", DateUtils.convertToWords(deathRegistration.getDateOfEvent()));
        registrationMapObject.put("REGISTRATIONNUMBER", deathRegistration.getRegistrationNo());
        // registrationMapObject.put("DATEOFREGISTRATION",
        // DateUtils.formatDateInWords(deathRegistration.getRegistrationDate()));
        registrationMapObject.put("REGISTRATIONUNITNAME", deathRegistration.getRegistrationUnit().getRegUnitDesc());
        if (deathRegistration.getPlaceType().getDesc().equals("Hospital"))
            registrationMapObject.put("PLACEOFDEATH",
                    (deathRegistration.getPlaceType().getDesc() != null ? deathRegistration.getPlaceType().getDesc()
                            .toUpperCase() : "")
                            + " " + deathRegistration.getEstablishment().getName());
        else if (deathRegistration.getPlaceType().getDesc().equals("House"))
            registrationMapObject.put("PLACEOFDEATH",
                    (deathRegistration.getPlaceType().getDesc() != null ? deathRegistration.getPlaceType().getDesc()
                            .toUpperCase() : "")
                            + " " + getAddressAsStringFromAddressObject(deathRegistration.getEventAddress()));
        else if (deathRegistration.getPlaceType().getDesc().equals("Other"))
            registrationMapObject.put("PLACEOFDEATH",
                    (deathRegistration.getPlaceType().getDesc() != null ? deathRegistration.getPlaceType().getDesc()
                            .toUpperCase() : "")
                            + " " + getAddressAsStringFromAddressObject(deathRegistration.getEventAddress()));
        else if (deathRegistration.getPlaceType().getDesc().equals("Not Stated"))
            registrationMapObject.put("PLACEOFDEATH",
                    deathRegistration.getPlaceType().getDesc() != null ? deathRegistration.getPlaceType().getDesc()
                            .toUpperCase() : "");

        registrationMapObject.put("FATHERNAME", deathRegistration.getFatherFullName().toString());
        registrationMapObject.put("MOTHERNAME", deathRegistration.getMotherFullName().toString());
        registrationMapObject.put("ADDRESSATDEATHTIME", getAddressAsStringFromAddressObject(deathRegistration
                .getCitizen().getRelatedAddress(BndConstants.DECEASEDADDRESS)));
        if (deathRegistration.getCitizen() != null)
            registrationMapObject.put("PERMNENTADDRESS", getAddressAsStringFromAddressObject(deathRegistration
                    .getCitizen().getRelatedAddress(BndConstants.PERMANENTADDRESS)));

        registrationMapObject.put("REMARKS", deathRegistration.getRemarks() != null ? deathRegistration.getRemarks()
                .toUpperCase() : "");
        registrationMapObject.put("LOGOINDIAPATH", bndCommonService.getIndiaImage(request));
        registrationMapObject.put("LOGOOFCITYPATH", bndCommonService.getCityLogoName(request));

        return registrationMapObject;
    }

    private StringBuffer buildUrlPatterForDeathCertificate(final DeathRegistration deathRegistration,
            final HttpServletRequest request) {
        final StringBuffer urlPatter = new StringBuffer();

        String urlStr = request.getRequestURI();

        urlStr = WebUtils.extractRequestedDomainName(request);

        urlPatter.append("http://");
        // domain name
        urlPatter.append(urlStr);
        // System.out.println(urlStr);
        urlPatter.append(":");

        urlPatter.append(request.getServerPort());
        final List<String> appConfig = bndCommonService.getAppconfigActualValue(BndConstants.BNDMODULE,
                BndConstants.DEATHVIEWURL);

        // context root
        urlPatter.append(appConfig.get(0));

        urlPatter.append("idTemp=" + deathRegistration.getId());

        return urlPatter;

    }

    protected String getAddressAsStringFromAddressObject(final Address addr) {
        final StringBuffer address = new StringBuffer();
        if (addr != null) {
            // TODO egifix
            /*
             * if (addr.getStreetAddress1() != null &&
             * !addr.getStreetAddress1().equals("NA")) {
             * address.append(addr.getStreetAddress1()).append(" "); if
             * (addr.getStreetAddress2() != null &&
             * !addr.getStreetAddress2().equals("NA"))
             * address.append(addr.getStreetAddress2()).append(" "); if
             * (addr.getCityTownVillage() != null &&
             * !addr.getCityTownVillage().equals("NA"))
             * address.append(addr.getCityTownVillage()).append(" "); } else
             * address.append("*******");
             */

        } else
            address.append("*******");

        return address.toString() != null ? address.toString().toUpperCase() : "";
    }

}
