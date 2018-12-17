/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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

package org.egov.adtax.service.es;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.adtax.workflow.AdvertisementWorkFlowService;
import org.egov.commons.entity.Source;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.search.elasticsearch.entity.ApplicationIndex;
import org.egov.search.elasticsearch.entity.enums.ApprovalStatus;
import org.egov.search.elasticsearch.entity.enums.ClosureStatus;
import org.egov.search.elasticsearch.service.ApplicationIndexService;
import org.egov.infra.security.utils.SecurityUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvertisementPermitDetailUpdateIndexService {

    private static final Logger LOGGER = Logger.getLogger(AdvertisementPermitDetailUpdateIndexService.class);

    private static final String ADTAX_APPLICATION_VIEW = "/adtax/hoarding/view/%s";

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ApplicationIndexService applicationIndexService;
    @Autowired
    private AdvertisementWorkFlowService advertisementWorkFlowService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private AdvertisementIndexService advertisementIndexService;
    @Autowired
    protected AppConfigValueService appConfigValuesService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    /**
     * @param advertisementPermitDetail
     */
    public void updateAdvertisementPermitDetailIndexes(final AdvertisementPermitDetail advertisementPermitDetail) {

        User user = advertisementWorkFlowService.getApproverByStatePosition(advertisementPermitDetail);

        user = logUserInfo(user);

        // For legacy application - create only advertisementIndex
        if (isLegacyOrPermitGenerated(advertisementPermitDetail)) {
            advertisementIndexService.createAdvertisementIndex(advertisementPermitDetail);
            return;
        }

        ApplicationIndex applicationIndex = applicationIndexService.findByApplicationNumber(advertisementPermitDetail
                .getApplicationNumber());
        // update existing application index

        if (applicationIndex != null && null != advertisementPermitDetail.getId()
                && advertisementPermitDetail.getStatus() != null) {
            if (advertisementPermitDetail.getStatus() != null &&
                    (advertisementPermitDetail.getStatus().getCode()
                            .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_APPROVED)
                            || advertisementPermitDetail.getStatus().getCode()
                                    .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_ADTAXAMOUNTPAID)
                            || advertisementPermitDetail.getStatus().getCode()
                                    .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_ADTAXPERMITGENERATED)
                            || advertisementPermitDetail.getStatus().getCode()
                                    .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_CREATED)
                            || advertisementPermitDetail.getStatus().getCode()
                                    .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_CANCELLED))) {
                applicationIndex.setStatus(advertisementPermitDetail.getStatus().getDescription());
                applicationIndex.setOwnerName(getOwnerName(user));

                // Set application index status to approved on advertisement approval and close
                approveAndCloseOnApproval(advertisementPermitDetail, applicationIndex);
                // mark application index as closed on generate permit order
                approveAndCloseOnPermitGeneration(advertisementPermitDetail, applicationIndex);
                // mark application index as rejected and closed on advertisement cancellation / Deactivation
                rejectAndCloseOnDeactivationOrCancellation(advertisementPermitDetail, applicationIndex);

                setConsumerCode(advertisementPermitDetail, applicationIndex);

                applicationIndexService.updateApplicationIndex(applicationIndex);
            }
            // create advertisement index
            advertisementIndexService.createAdvertisementIndex(advertisementPermitDetail);

        } else {
            setApplicationDateAndNumber(advertisementPermitDetail);
            createApplicationIndex(advertisementPermitDetail, user, applicationIndex);
        }

    }

    private User logUserInfo(User user) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get user from asignList ....");
            LOGGER.info(user != null ? user : "user is null");
        }
        if (user == null) {
            user = securityUtils.getCurrentUser();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Get curent  user....");
                LOGGER.info(user != null ? user : "user is null");
            }
        }
        return user;
    }

    private void createApplicationIndex(final AdvertisementPermitDetail advertisementPermitDetail, User user,
            ApplicationIndex applicationIndex) {
        if (applicationIndex == null) {
            applicationIndex = ApplicationIndex.builder().withModuleName(AdvertisementTaxConstants.MODULE_NAME)
                    .withApplicationNumber(advertisementPermitDetail.getApplicationNumber())
                    .withApplicationDate(advertisementPermitDetail.getApplicationDate())
                    .withApplicationType(advertisementPermitDetail.getState().getNatureOfTask())
                    .withApplicantName(getApplicantName(advertisementPermitDetail))
                    .withStatus(advertisementPermitDetail.getStatus().getDescription()).withUrl(
                            String.format(ADTAX_APPLICATION_VIEW, advertisementPermitDetail.getId()))
                    .withApplicantAddress(getAddress(advertisementPermitDetail))
                    .withOwnername(getOwnerName(user))
                    .withChannel(getChannel(advertisementPermitDetail))
                    .withMobileNumber(getMobileNumber(advertisementPermitDetail))
                    .withClosed(ClosureStatus.NO)
                    .withSla(getSla(getSlaForAdvertisement(advertisementPermitDetail)))
                    .withApproved(ApprovalStatus.INPROGRESS).build();
            applicationIndexService.createApplicationIndex(applicationIndex);
        }
    }

    private void setApplicationDateAndNumber(final AdvertisementPermitDetail advertisementPermitDetail) {
        if (advertisementPermitDetail.getApplicationDate() == null)
            advertisementPermitDetail.setApplicationDate(new Date());
        if (advertisementPermitDetail.getApplicationNumber() == null)
            advertisementPermitDetail.setApplicationNumber(advertisementPermitDetail.getApplicationNumber());
    }

    private AppConfigValues getSlaForAdvertisement(final AdvertisementPermitDetail advertisementPermitDetail) {
        AppConfigValues slaForAdvertisement = null;

        if (isApplicationTypeRenew(advertisementPermitDetail))
            slaForAdvertisement = getSlaAppConfigValuesForAdvertisement(
                    AdvertisementTaxConstants.MODULE_NAME, AdvertisementTaxConstants.SLAFORRENEWADVERTISEMENT);
        else if (isApplicationTypeNew(advertisementPermitDetail))
            slaForAdvertisement = getSlaAppConfigValuesForAdvertisement(
                    AdvertisementTaxConstants.MODULE_NAME, AdvertisementTaxConstants.SLAFORNEWADVERTISEMENT);
        return slaForAdvertisement;
    }

    private boolean isLegacyOrPermitGenerated(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getAdvertisement().getLegacy()
                && (null == advertisementPermitDetail.getId() || null != advertisementPermitDetail.getId()
                        && advertisementPermitDetail.getStatus().getCode()
                                .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_ADTAXPERMITGENERATED));
    }

    private boolean isApplicationTypeNew(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail != null && advertisementPermitDetail.getApplicationtype() != null
                && AdvertisementTaxConstants.NEW.equals(advertisementPermitDetail.getApplicationtype().toString());
    }

    private boolean isApplicationTypeRenew(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail != null && advertisementPermitDetail.getApplicationtype() != null
                && AdvertisementTaxConstants.RENEW.equals(advertisementPermitDetail.getApplicationtype().toString());
    }

    private String getChannel(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getSource() == null ? Source.SYSTEM.toString()
                : advertisementPermitDetail.getSource();
    }

    private int getSla(AppConfigValues slaForAdvertisement) {
        return slaForAdvertisement != null && slaForAdvertisement.getValue() != null
                ? Integer.valueOf(slaForAdvertisement.getValue())
                : 0;
    }

    private String getMobileNumber(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getAgency() != null
                ? advertisementPermitDetail.getAgency().getMobileNumber()
                : EMPTY;
    }

    private String getAddress(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getAgency() != null
                ? advertisementPermitDetail.getAgency().getAddress()
                : advertisementPermitDetail.getOwnerDetail();
    }

    private String getApplicantName(final AdvertisementPermitDetail advertisementPermitDetail) {
        return advertisementPermitDetail.getAgency() != null
                ? advertisementPermitDetail.getAgency().getName()
                : advertisementPermitDetail.getOwnerDetail();
    }

    private void setConsumerCode(final AdvertisementPermitDetail advertisementPermitDetail, ApplicationIndex applicationIndex) {
        if (advertisementPermitDetail.getPermissionNumber() != null)
            applicationIndex.setConsumerCode(advertisementPermitDetail.getPermissionNumber());
    }

    private String getOwnerName(User user) {
        return user != null ? user.getUsername() + "::" + user.getName() : "";
    }

    private void rejectAndCloseOnDeactivationOrCancellation(final AdvertisementPermitDetail advertisementPermitDetail,
            ApplicationIndex applicationIndex) {
        if (advertisementPermitDetail.getStatus().getCode()
                .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_CANCELLED) ||
                advertisementPermitDetail.getAdvertisement().getStatus().name().equalsIgnoreCase("INACTIVE")) {
            applicationIndex.setApproved(ApprovalStatus.REJECTED);
            applicationIndex.setClosed(ClosureStatus.YES);
        }
    }

    private void approveAndCloseOnPermitGeneration(final AdvertisementPermitDetail advertisementPermitDetail,
            ApplicationIndex applicationIndex) {
        if (advertisementPermitDetail.getStatus().getCode()
                .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_ADTAXPERMITGENERATED)) {
            applicationIndex.setApproved(ApprovalStatus.APPROVED);
            applicationIndex.setClosed(ClosureStatus.YES);
        }
    }

    private void approveAndCloseOnApproval(final AdvertisementPermitDetail advertisementPermitDetail,
            ApplicationIndex applicationIndex) {
        if (advertisementPermitDetail.getStatus().getCode()
                .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_APPROVED)) {
            String applicantName = getApplicantName(advertisementPermitDetail);
            String address = getAddress(advertisementPermitDetail);
            applicationIndex.setApplicantName(applicantName);
            applicationIndex.setApplicantAddress(address);
            applicationIndex.setMobileNumber(advertisementPermitDetail.getAgency() != null
                    ? advertisementPermitDetail.getAgency().getMobileNumber()
                    : "");
            applicationIndex.setApproved(ApprovalStatus.APPROVED);
            applicationIndex.setClosed(ClosureStatus.YES);

        }
    }

    public AppConfigValues getSlaAppConfigValuesForAdvertisement(final String moduleName, final String keyName) {
        final List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(moduleName, keyName);
        return !appConfigValues.isEmpty() ? appConfigValues.get(0) : null;
    }
}