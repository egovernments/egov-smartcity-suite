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
import org.egov.bnd.model.Addiction;
import org.egov.bnd.model.CitizenAddiction;
import org.egov.bnd.model.CitizenRelation;
import org.egov.bnd.model.DeathRegistration;
import org.egov.bnd.services.common.BndCommonService;
import org.egov.bnd.utils.BndConstants;
import org.egov.bnd.utils.BndDateUtils;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
public class DeathRegistrationService extends PersistenceService<DeathRegistration, Long> {

    private static final Logger LOGGER = Logger.getLogger(DeathRegistrationService.class);
    private BndCommonService bndCommonService;
    private WorkflowService<DeathRegistration> deathRegistrationWorkflowService;
    private EisCommonService eisCommonService;

    public BndCommonService getBndCommonService() {
        return bndCommonService;
    }

    public void setBndCommonService(final BndCommonService bndCommonService) {
        this.bndCommonService = bndCommonService;
    }

    @Transactional
    public DeathRegistration save(final DeathRegistration deathRegistration, final String workflowAction,
            final List<Addiction> addictionList) {
        LOGGER.info("Started save method");
        buildAddictionDetail(deathRegistration, addictionList);
        persist(deathRegistration);
        startWorkFlow(deathRegistration, workflowAction);
        LOGGER.info("Completed save method");
        return deathRegistration;
    }

    public void startWorkFlow(final DeathRegistration deathRegistration, final String workflowAction) {
        LOGGER.debug("START Death Registration workflow method");
        if (deathRegistration.getState() == null) {
            final Position position = eisCommonService.getPositionByUserId(deathRegistration.getCreatedBy().getId());
            deathRegistration.transition(true).start().withOwner(position).withComments("Death Record created.");
        }
        if (workflowAction != null && !"".equals(workflowAction)
                && !BndConstants.SCRIPT_SAVE.equalsIgnoreCase(workflowAction))

            deathRegistration.transition(true).start().withNextAction(workflowAction.toLowerCase())
            .withComments("Death Registration Number- " + deathRegistration.getRegistrationNo());

        LOGGER.debug("END Death Registration workflow method");

    }

    private DeathRegistration buildAddictionDetail(final DeathRegistration deathReg, final List<Addiction> addictionList) {
        LOGGER.info("Started buildAddictionDetail method");
        List<CitizenAddiction> citizenAddictionList = new ArrayList<CitizenAddiction>();
        final List<Addiction> tempAddictionList = new ArrayList<Addiction>();
        for (final Addiction addictions : addictionList)
            if (addictions.getNoOfYears() != null && !"".equals(addictions.getNoOfYears()))
                tempAddictionList.add(addictions);
        citizenAddictionList = getAddictionDetails(deathReg, tempAddictionList);
        if (citizenAddictionList != null && !citizenAddictionList.isEmpty()) {
            deathReg.getAddictions().clear();
            deathReg.getAddictions().addAll(citizenAddictionList);
        } else {
            if (deathReg.getAddictions() != null)
                deathReg.getAddictions().clear();
            deathReg.setAddictions(null);
        }
        LOGGER.info("Completed buildAddictionDetail method");
        return deathReg;

    }

    public List<CitizenAddiction> getAddictionDetails(final DeathRegistration deathReg,
            final List<Addiction> tempAddictionList) {

        LOGGER.info("Started getAddictionDetails method");
        List<CitizenAddiction> citizenAddictionList = new ArrayList<CitizenAddiction>();
        final List<CitizenAddiction> newcitizenAddictionList = new ArrayList<CitizenAddiction>();
        if (deathReg.getAddictions() != null) {
            if (deathReg.getAddictions().isEmpty()) {
                citizenAddictionList = new ArrayList<CitizenAddiction>();
                if (!tempAddictionList.isEmpty())
                    for (final Addiction addiction : tempAddictionList) {
                        final CitizenAddiction citaddiction = new CitizenAddiction();
                        citaddiction.setAddictedBy(addiction);
                        citaddiction.setNoOfYears(addiction.getNoOfYears());
                        citizenAddictionList.add(citaddiction);
                    }
                return citizenAddictionList;
            } else {

                citizenAddictionList = deathReg.getAddictions();
                for (final CitizenAddiction citaddiction : citizenAddictionList)
                    for (final Addiction addiction : tempAddictionList)
                        if (citaddiction.getAddictedBy().getDesc().equals(addiction.getDesc())) {
                            citaddiction.setNoOfYears(addiction.getNoOfYears());
                            newcitizenAddictionList.add(citaddiction);
                        } else {
                            Boolean isPresentflag = Boolean.TRUE;
                            for (final CitizenAddiction tempcitaddiction : deathReg.getAddictions())
                                if (addiction.getDesc().equals(tempcitaddiction.getAddictedBy().getDesc()))
                                    isPresentflag = Boolean.FALSE;
                            if (isPresentflag && addiction.getNoOfYears() != null) {
                                final CitizenAddiction cittempaddiction = new CitizenAddiction();
                                cittempaddiction.setAddictedBy(addiction);
                                cittempaddiction.setNoOfYears(addiction.getNoOfYears());
                                newcitizenAddictionList.add(cittempaddiction);
                            }
                        }

                LOGGER.info("Completed getAddictionDetails method");
                return newcitizenAddictionList;
            }
        } else
            return null;

    }

    @Transactional
    public DeathRegistration getDeathRegistrationById(final Long id) {
        return findById(id);
    }

    @Transactional
    public Boolean checkUniqueDeathRegistrationNumber(final Long regUnit, final Long id, final String regNo,
            final Date deathDate) {
        LOGGER.info("Started checkUniqueDeathRegistrationNumber method");
        final Criteria deathCriteria = getSession().createCriteria(DeathRegistration.class);

        if (regUnit != null && regUnit != -1)
            deathCriteria.add(Restrictions.eq("registrationUnit.id", regUnit));

        if (id != null && id != -1)
            deathCriteria.add(Restrictions.ne("id", id));

        if (regNo != null && !"".equals(regNo))
            deathCriteria.add(Restrictions.eq("registrationNo", regNo));

        if (deathDate != null) {
            final DateFormat format = new SimpleDateFormat("yyyy");
            final String birthYear = format.format(deathDate);
            final Date startDate = BndDateUtils.getStartDateFromYear(Integer.valueOf(birthYear));
            final Date endDate = BndDateUtils.getEndDateFromYear(Integer.valueOf(birthYear));
            final String appConfigValue = bndCommonService.getAppconfigValueResult(BndConstants.BNDMODULE,
                    BndConstants.NUMBERGENKEY, BndConstants.REGISTRATIONDATE);
            if (appConfigValue != null && BndConstants.REGISTRATIONDATE.equalsIgnoreCase(appConfigValue))
                deathCriteria.add(Restrictions.between("registrationDate", startDate, endDate));
            else
                deathCriteria.add(Restrictions.between("dateOfEvent", startDate, endDate));

        }
        LOGGER.info("Completed checkUniqueDeathRegistrationNumber method");
        return !deathCriteria.list().isEmpty();
    }

    @Transactional
    public List<CitizenRelation> getRelativeDetails(final DeathRegistration deathRegistration) {

        LOGGER.info("Started getRelativeDetails method");
        final Criteria relativeCriteria = getSession().createCriteria(CitizenRelation.class, "citizenRelationship");
        relativeCriteria.createAlias("citizenRelationship.relatedAs", "Relation");
        relativeCriteria.add(Restrictions.eq("cit", deathRegistration.getCitizen()));
        final Criterion cr1 = Restrictions.eq("Relation.relatedAsConst", BndConstants.FATHER.toUpperCase());
        final Criterion cr2 = Restrictions.eq("Relation.relatedAsConst", BndConstants.HUSBAND.toUpperCase());
        relativeCriteria.add(Restrictions.or(cr1, cr2));
        LOGGER.info("Completed getRelativeDetails method");
        return relativeCriteria.list();

    }

    public WorkflowService<DeathRegistration> getDeathRegistrationWorkflowService() {
        return deathRegistrationWorkflowService;
    }

   

    public EisCommonService getEisCommonService() {
		return eisCommonService;
	}

	public void setEisCommonService(EisCommonService eisCommonService) {
		this.eisCommonService = eisCommonService;
	}

	public void setDeathRegistrationWorkflowService(
            final WorkflowService<DeathRegistration> deathRegistrationWorkflowService) {
        this.deathRegistrationWorkflowService = deathRegistrationWorkflowService;
    }

  

    @Transactional
    public Boolean isEventAndRegistrationDateDiffGreaterThan21Days(final Long reportId) {
        if (reportId != null) {
            final DeathRegistration dthRegn = getDeathRegistrationById(reportId);
            if (dthRegn != null && dthRegn.getDateOfEvent() != null && dthRegn.getRegistrationDate() != null) {
                long diff = dthRegn.getDateOfEvent().getTime() - dthRegn.getRegistrationDate().getTime();
                diff = diff / (1000 * 60 * 60 * 24);
                if (diff > 21)
                    return Boolean.TRUE;
                return Boolean.FALSE;
            }
        }
        return null;
    }

    public Boolean issueFreeCertificate(final Long reportId, final String roleName) {

        /*
         * If the certificate is already issued or event and registration date
         * difference is greater than 21 days, then do not issue free
         * certificate.
         */
        if (reportId != null) {

            final DeathRegistration dthRegn = getDeathRegistrationById(reportId);
            if (dthRegn != null && dthRegn.getDateOfEvent() != null && dthRegn.getRegistrationDate() != null) {

                long diff = dthRegn.getRegistrationDate().getTime() - dthRegn.getDateOfEvent().getTime();
                diff = diff / (1000 * 60 * 60 * 24);

                if (dthRegn.getIsCertIssued() != null && dthRegn.getIsCertIssued().equalsIgnoreCase("Y") || diff > 21
                        && roleName != null && !roleName.equals(BndConstants.HOSPITALUSER))
                    return Boolean.FALSE;
                else
                    return Boolean.TRUE;
            }
        }
        return null;

    }
}
