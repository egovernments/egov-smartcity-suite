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
package org.egov.bnd.services.registration;

import org.apache.log4j.Logger;
import org.egov.bnd.model.AdoptionDetails;
import org.egov.bnd.model.BirthRegistration;
import org.egov.bnd.model.Registration;
import org.egov.bnd.services.common.BndCommonService;
import org.egov.bnd.utils.BndConstants;
import org.egov.bnd.utils.BndDateUtils;
import org.egov.commons.EgwSatuschange;
import org.egov.eis.service.EisCommonService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Transactional(readOnly = true)
public class BirthRegistrationService extends PersistenceService<BirthRegistration, Long> {

    private static final Logger LOGGER = Logger.getLogger(BirthRegistrationService.class);
    private PersistenceService<AdoptionDetails, Long> adoptionService;
    private PersistenceService<EgwSatuschange, Integer> statusChangeService;
    private BndCommonService bndCommonService;
    private EisCommonService eisCommonService;
    private String workflowAction;
    private BirthRegistration birthRegistration;

    /**
     * This method is to save birth registration record
     *
     * @param birthRegistration
     *            - BirthRegistration object
     * @param workflowAction
     *            - Work Flow action like approve,reject
     * @return
     */

    @Transactional
    public Registration save(final BirthRegistration birthRegistration, final String workflowAction) {
        LOGGER.debug("strat save method");
        this.birthRegistration = birthRegistration;
        this.workflowAction = workflowAction;
        persist(birthRegistration);
        startWorkFlow();
        LOGGER.debug("end save method");
        return birthRegistration;
    }

    private void startWorkFlow() {
        LOGGER.debug("START Birth Registration workflow ");
        if (birthRegistration.getState() == null) {
            final Position pos = eisCommonService.getPositionByUserId(birthRegistration.getCreatedBy().getId());
            if (pos == null)
                throw new EGOVRuntimeException("User Position is Null");
            birthRegistration.transition(true).start().withOwner(pos).withComments("Birth Record created.");
        }
        if (workflowAction != null && org.apache.commons.lang.StringUtils.isNotBlank(workflowAction)
                && !BndConstants.SCRIPT_SAVE.equalsIgnoreCase(workflowAction))
            birthRegistration.transition(true).start().withNextAction(workflowAction.toLowerCase())
            .withComments("Birth Registration Number- " + birthRegistration.getRegistrationNo());

        LOGGER.debug("END Birth Registration workflow ");
    }

    /**
     * This API is to build Adoption details
     *
     * @param birthRegistration
     */

    public void buildAdoptionDetial(final BirthRegistration birthRegistration) {
        LOGGER.debug("START Adoption detail method");
        if (birthRegistration.getIsChildAdopted() == null || !birthRegistration.getIsChildAdopted()) {
            birthRegistration.setAdoptionDetail(null);
            birthRegistration.setIsChildAdopted(Boolean.FALSE);
        } else {
            // TODO egifix
            /*
             * birthRegistration.getAdoptionDetail().getAdopteeFather().setSex(
             * SexType.MALE.name());
             * birthRegistration.getAdoptionDetail().getAdopteeMother
             * ().setSex(SexType.FEMALE.name());
             */
            if (birthRegistration.getAdoptionDetail().getAdoptionInstituteId() != null
                    && birthRegistration.getAdoptionDetail().getAdoptionInstituteId() != -1)
                birthRegistration.getAdoptionDetail().setAdoptionInstitute(
                        bndCommonService.getAdoptionInstituteById(birthRegistration.getAdoptionDetail()
                                .getAdoptionInstituteId()));
            birthRegistration.setAdoptionDetail(saveAdoption(birthRegistration.getAdoptionDetail()));
            birthRegistration.setIsChildAdopted(Boolean.TRUE);
        }
        LOGGER.debug("END Adoption detail method");
    }

    @Transactional
    public BirthRegistration getBirthRegistrationById(final Long id) {
        return findById(id);
    }

    @Transactional
	public Boolean issueFreeCertificate(final Long reportId,
			final String roleName) {

		/*
		 * If the certificate is already issued or event and registration date
		 * difference is greater than 21 days, then donot issue free certificate
		 */
		if (reportId != null) {

			final BirthRegistration birthRegistration = getBirthRegistrationById(reportId);

			if (birthRegistration != null
					&& birthRegistration.getDateOfEvent() != null
					&& birthRegistration.getRegistrationDate() != null) {

				long diff = birthRegistration.getRegistrationDate().getTime()
						- birthRegistration.getDateOfEvent().getTime();
				diff = diff / (1000 * 60 * 60 * 24);

				if (birthRegistration.getIsCertIssued() != null
						&& birthRegistration.getIsCertIssued()
								.equalsIgnoreCase("Y") || diff > 21
						&& roleName != null
						&& !roleName.equals(BndConstants.HOSPITALUSER))
					return Boolean.FALSE;
				else
					return Boolean.TRUE;
			}
		}
		return null;

	}

    @Transactional
    public Boolean isEventAndRegistrationDateDiffGreaterThan21Days(final Long reportId) {

        if (reportId != null) {

            final BirthRegistration birthRegistration = getBirthRegistrationById(reportId);

            if (birthRegistration != null && birthRegistration.getDateOfEvent() != null
                    && birthRegistration.getRegistrationDate() != null) {

                long diff = birthRegistration.getDateOfEvent().getTime()
                        - birthRegistration.getRegistrationDate().getTime();
                diff = diff / (1000 * 60 * 60 * 24);
                if (diff > 21)
                    return Boolean.TRUE;
                return Boolean.FALSE;
            }
        }
        return null;

    }

    /**
     * @param regUnit
     *            RegistrationUnit id
     * @param id
     *            - BirthRegistration id
     * @param regNo
     *            - Birth Registration number
     * @param birthDate
     *            - Birth date of BirthRegistration
     * @returns Boolean value
     */

    @Transactional
    public Boolean checkUniqueRegistrationNumber(final Long regUnit, final Long id, final String regNo,
            final Date birthDate, final String type) {
        final Criteria birthCriteria = getSession().createCriteria(BirthRegistration.class).createAlias(
                "citizenBDDetails", "bdDetails");

        if (regUnit != null && regUnit != -1)
            birthCriteria.add(Restrictions.eq("registrationUnit.id", regUnit));

        if (id != null && id != -1)
            birthCriteria.add(Restrictions.ne("id", id));

        if (regNo != null && !"".equals(regNo))
            birthCriteria.add(Restrictions.eq("registrationNo", regNo));

        if (birthDate != null) {
            final DateFormat format = new SimpleDateFormat("yyyy");
            final String birthYear = format.format(birthDate);
            final Date startDate = BndDateUtils.getStartDateFromYear(Integer.valueOf(birthYear));
            final Date endDate = BndDateUtils.getEndDateFromYear(Integer.valueOf(birthYear));
            final String appConfigValue = bndCommonService.getAppconfigValueResult(BndConstants.BNDMODULE,
                    BndConstants.NUMBERGENKEY, BndConstants.REGISTRATIONDATE);
            if (appConfigValue != null && BndConstants.REGISTRATIONDATE.equalsIgnoreCase(appConfigValue))
                birthCriteria.add(Restrictions.between("registrationDate", startDate, endDate));
            else
                birthCriteria.add(Restrictions.between("dateOfEvent", startDate, endDate));
        }

        if (BndConstants.BIRTH.equals(type))
            birthCriteria.add(Restrictions.eq("bdDetails.isStillBirth", 'N'));

        if (BndConstants.STILLBIRTH.equals(type))
            birthCriteria.add(Restrictions.eq("bdDetails.isStillBirth", 'Y'));

        return !birthCriteria.list().isEmpty();
    }

    @Transactional
    public void deleteAdoption(final AdoptionDetails adoptionDetail) {
        if (adoptionDetail != null && adoptionDetail.getId() != null)
            adoptionService.delete(adoptionDetail);
    }

    @Transactional
    public AdoptionDetails saveAdoption(final AdoptionDetails adoptionDetail) {

        if (adoptionDetail != null)
            adoptionService.persist(adoptionDetail);
        LOGGER.debug("Adoption Details---:" + adoptionDetail);
        return adoptionDetail;
    }

    /**
     * This api is to save status change instance
     *
     * @param statusChange
     * @return
     */

    @Transactional
    public EgwSatuschange saveStatusChange(final EgwSatuschange statusChange) {
        return statusChangeService.persist(statusChange);
    }

    public void setBndCommonService(final BndCommonService bndCommonService) {
        this.bndCommonService = bndCommonService;
    }

    public void setAdoptionService(final PersistenceService<AdoptionDetails, Long> adoptionService) {
        this.adoptionService = adoptionService;
    }



    public EisCommonService getEisCommonService() {
		return eisCommonService;
	}

	public void setEisCommonService(EisCommonService eisCommonService) {
		this.eisCommonService = eisCommonService;
	}

	public void setBirthRegistrationWorkflowService(
            final WorkflowService<BirthRegistration> birthRegistrationWorkflowService) {
    }

    public void setStatusChangeService(final PersistenceService<EgwSatuschange, Integer> statusChangeService) {
        this.statusChangeService = statusChangeService;
    }

}
