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
package org.egov.works.revisionestimate.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.entity.MeasurementSheet;
import org.egov.works.abstractestimate.entity.AbstractEstimate.EstimateStatus;
import org.egov.works.revisionestimate.entity.RevisionAbstractEstimate;
import org.egov.works.revisionestimate.repository.RevisionEstimateRepository;
import org.egov.works.utils.WorksConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RevisionEstimateService {

    private static final Logger LOG = LoggerFactory.getLogger(RevisionEstimateService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final RevisionEstimateRepository revisionEstimateRepository;
    
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    public RevisionEstimateService(final RevisionEstimateRepository revisionEstimateRepository) {
        this.revisionEstimateRepository = revisionEstimateRepository;
    }

    public List<RevisionAbstractEstimate> findApprovedRevisionEstimatesByParent(Long id) {
        return revisionEstimateRepository.findByParent_IdAndStatus(id, AbstractEstimate.EstimateStatus.APPROVED.toString());
    }

    public RevisionAbstractEstimate getRevisionEstimateById(Long id) {
        return revisionEstimateRepository.findOne(id);
    }

    @Transactional
    public RevisionAbstractEstimate createRevisionEstimate(final RevisionAbstractEstimate revisionEstimate) {
        mergeSorAndNonSorActivities(revisionEstimate);
        final AbstractEstimate abstractEstimate = revisionEstimate.getParent();
        final List<RevisionAbstractEstimate> revisionEstimates = revisionEstimateRepository.findByParent_Id(abstractEstimate.getId());

        Integer reCount = revisionEstimates.size();
        if (revisionEstimate.getId() != null)
            reCount = reCount - 1;
        if (revisionEstimate.getId() == null) {
            revisionEstimate.setParent(abstractEstimate);
            revisionEstimate.setEstimateDate(new Date());
            revisionEstimate.setEstimateNumber(abstractEstimate.getEstimateNumber()
                    + "/RE".concat(Integer.toString(reCount)));
            revisionEstimate.setName("Revision Estimate for: " + abstractEstimate.getName());
            revisionEstimate.setDescription("Revision Estimate for: " + abstractEstimate.getDescription());
            revisionEstimate.setNatureOfWork(abstractEstimate.getNatureOfWork());
            revisionEstimate.setExecutingDepartment(abstractEstimate.getExecutingDepartment());
            revisionEstimate.setUserDepartment(abstractEstimate.getUserDepartment());
            revisionEstimate.setWard(abstractEstimate.getWard());
            revisionEstimate.setDepositCode(abstractEstimate.getDepositCode());
            revisionEstimate.setFundSource(abstractEstimate.getFundSource());
            revisionEstimate.setParentCategory(abstractEstimate.getParentCategory());
        }
    
        revisionEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.ABSTRACTESTIMATE,
                EstimateStatus.ADMIN_SANCTIONED.toString()));
        revisionEstimateRepository.save(revisionEstimate);
        return revisionEstimate;
    }
    
    private void mergeSorAndNonSorActivities(final RevisionAbstractEstimate revisionEstimate) {
        for (final Activity activity : revisionEstimate.getNonTenderedActivities())
            if (activity.getId() == null) {
                activity.setAbstractEstimate(revisionEstimate);
                revisionEstimate.addActivity(activity);
            } else
                for (final Activity oldActivity : revisionEstimate.getSORActivities())
                    if (oldActivity.getId().equals(activity.getId()))
                        updateActivity(oldActivity, activity);
        for (final Activity activity : revisionEstimate.getLumpSumActivities())
            if (activity.getId() == null) {
                activity.setAbstractEstimate(revisionEstimate);
                revisionEstimate.addActivity(activity);
            } else
                for (final Activity oldActivity : revisionEstimate.getNonSORActivities())
                    if (oldActivity.getId().equals(activity.getId()))
                        updateActivity(oldActivity, activity);
        if (LOG.isDebugEnabled())
            for (final Activity ac : revisionEstimate.getActivities())
                LOG.debug(ac.getMeasurementSheetList().size() + "    " + ac.getQuantity());

        for (final Activity ac : revisionEstimate.getNonTenderedActivities())
            for (final MeasurementSheet ms : ac.getMeasurementSheetList())
                if (ms.getActivity() == null)
                    ms.setActivity(ac);

        for (final Activity ac : revisionEstimate.getLumpSumActivities())
            for (final MeasurementSheet ms : ac.getMeasurementSheetList())
                if (ms.getActivity() == null)
                    ms.setActivity(ac);
    }

    private List<MeasurementSheet> mergeMeasurementSheet(final Activity oldActivity, final Activity activity) {
        final List<MeasurementSheet> newMsList = new LinkedList<MeasurementSheet>(oldActivity.getMeasurementSheetList());
        for (final MeasurementSheet msnew : activity.getMeasurementSheetList()) {
            if (msnew.getId() == null) {
                msnew.setActivity(oldActivity);
                oldActivity.getMeasurementSheetList().add(msnew);
                continue;
            }

            for (final MeasurementSheet msold : oldActivity.getMeasurementSheetList())
                if (msnew.getId().longValue() == msold.getId().longValue()) {
                    msold.setLength(msnew.getLength());
                    msold.setWidth(msnew.getWidth());
                    msold.setDepthOrHeight(msnew.getDepthOrHeight());
                    msold.setNo(msnew.getNo());
                    msold.setActivity(msnew.getActivity());
                    msold.setIdentifier(msnew.getIdentifier());
                    msold.setRemarks(msnew.getRemarks());
                    msold.setSlNo(msnew.getSlNo());
                    msold.setQuantity(msnew.getQuantity());
                    newMsList.add(msold);

                }

        }
        final List<MeasurementSheet> toRemove = new LinkedList<MeasurementSheet>();
        for (final MeasurementSheet msold : oldActivity.getMeasurementSheetList()) {
            Boolean found = false;
            if (LOG.isDebugEnabled()) {
                LOG.debug(oldActivity.getMeasurementSheetList().size() + "activity.getMeasurementSheetList()");
                LOG.debug(msold.getId() + "------msold.getId()");
            }
            if (msold.getId() == null)
                continue;

            for (final MeasurementSheet msnew : activity.getMeasurementSheetList())
                if (msnew.getId() == null) {
                    // found=true;
                } else if (msnew.getId().longValue() == msold.getId().longValue()) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(msnew.getId() + "------msnew.getId()");
                        LOG.debug(msnew.getRemarks() + "------remarks");
                    }

                    found = true;
                }

            if (!found)
                toRemove.add(msold);

        }

        for (final MeasurementSheet msremove : toRemove) {
            if (LOG.isInfoEnabled())
                LOG.info("...........Removing rows....................Of MeasurementSheet" + msremove.getId());
            oldActivity.getMeasurementSheetList().remove(msremove);
        }

        return oldActivity.getMeasurementSheetList();

    }

    private void updateActivity(final Activity oldActivity, final Activity activity) {
        oldActivity.setSchedule(activity.getSchedule());
        oldActivity.setAmt(activity.getAmt());
        oldActivity.setNonSor(activity.getNonSor());
        oldActivity.setQuantity(activity.getQuantity());
        oldActivity.setRate(activity.getRate());
        oldActivity.setServiceTaxPerc(activity.getServiceTaxPerc());
        oldActivity.setEstimateRate(activity.getEstimateRate());
        oldActivity.setUom(activity.getUom());
        oldActivity.setMeasurementSheetList(mergeMeasurementSheet(oldActivity, activity));
    }
}
