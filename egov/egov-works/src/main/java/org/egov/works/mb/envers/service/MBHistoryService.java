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
package org.egov.works.mb.envers.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.utils.DateUtils;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.mb.entity.MBDetails;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.mb.entity.MBHeader.MeasurementBookStatus;
import org.egov.works.mb.entity.MBHistory;
import org.egov.works.mb.service.MBHeaderService;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author venki
 */

@Service
@Transactional(readOnly = true)
public class MBHistoryService {

    @Autowired
    private AssignmentService assignmentService;

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    private MBHeaderService mbHeaderService;

    public List<MBHeader> getAuditedMbHeaderById(final Long id) {

        final Revisions<Integer, MBHeader> revisions = mbHeaderService.findRevisions(id);
        final List<MBHeader> auditedMbs = new ArrayList<MBHeader>();
        for (final Revision<Integer, MBHeader> revision : revisions.getContent())
            auditedMbs.add(revision.getEntity());

        return auditedMbs;
    }

    public List<MBHistory> getMBHistory(final Long mbHeaderId) {

        final List<MBHeader> mbHeaders = getAuditedMbHeaderById(mbHeaderId);

        final List<MBHistory> mbHistorys = prepareHistory(mbHeaders);

        return mbHistorys;
    }

    private List<MBHistory> prepareHistory(final List<MBHeader> mbHeaders) {

        Assignment userAssignment = null;
        final List<MBHistory> mbHistorys = new ArrayList<MBHistory>();
        final Set<Activity> sorActivities = new HashSet<Activity>(0);
        final Set<Activity> nonSorActivities = new HashSet<Activity>(0);
        final Set<Activity> nonTenActivities = new HashSet<Activity>(0);
        final Set<Activity> lumpSumActivities = new HashSet<Activity>(0);
        final Map<Long, Activity> sorActivitiesMap = new HashMap<Long, Activity>();
        final Map<Long, Activity> nonSorActivitiesMap = new HashMap<Long, Activity>();
        final Map<Long, Activity> ntActivitiesMap = new HashMap<Long, Activity>();
        final Map<Long, Activity> lsActivitiesMap = new HashMap<Long, Activity>();
        List<MBDetails> mbDetails = new ArrayList<MBDetails>();
        MBDetails detail = null;
        MBHistory history = null;
        for (final MBHeader header : mbHeaders)
            if (!MeasurementBookStatus.NEW.toString().equals(header.getEgwStatus().getCode())) {
                history = new MBHistory();
                userAssignment = assignmentService.findByEmployeeAndGivenDate(header.getLastModifiedBy().getId(), new Date())
                        .get(0);
                history.setStatus(header.getEgwStatus().getDescription());
                history.setOwnerName(userAssignment.getDesignation().getName() + " - " +
                        header.getLastModifiedBy().getName());
                history.setDateTime(DateUtils.getFormattedDateWithTimeStamp(new DateTime(header.getLastModifiedDate())));
                history.setMbAmount(header.getMbAmount());
                history.setSorMbDetails((List<MBDetails>) header.getSORMBDetails());
                history.setNonSorMbDetails((List<MBDetails>) header.getNonSORMBDetails());
                history.setNonTenderedMbDetails((List<MBDetails>) header.getNonTenderedMBDetails());
                history.setLumpSumMbDetails((List<MBDetails>) header.getLumpSumMBDetails());
                for (final MBDetails details : header.getSORMBDetails()) {
                    sorActivitiesMap.put(details.getWorkOrderActivity().getActivity().getId(),
                            details.getWorkOrderActivity().getActivity());
                    sorActivities.add(details.getWorkOrderActivity().getActivity());
                }

                for (final MBDetails details : header.getNonSORMBDetails()) {
                    nonSorActivitiesMap.put(details.getWorkOrderActivity().getActivity().getId(),
                            details.getWorkOrderActivity().getActivity());
                    nonSorActivities.add(details.getWorkOrderActivity().getActivity());
                }

                for (final MBDetails details : header.getNonTenderedMBDetails()) {
                    ntActivitiesMap.put(details.getWorkOrderActivity().getActivity().getId(),
                            details.getWorkOrderActivity().getActivity());
                    nonTenActivities.add(details.getWorkOrderActivity().getActivity());
                }

                for (final MBDetails details : header.getLumpSumMBDetails()) {
                    lsActivitiesMap.put(details.getWorkOrderActivity().getActivity().getId(),
                            details.getWorkOrderActivity().getActivity());
                    lumpSumActivities.add(details.getWorkOrderActivity().getActivity());
                }
                mbHistorys.add(history);
            }

        for (final MBHistory hstr : mbHistorys) {
            hstr.getSorActivities().addAll(sorActivities);
            hstr.getNonSorActivities().addAll(nonSorActivities);
            hstr.getNonTenActivities().addAll(nonTenActivities);
            hstr.getLumpSumActivities().addAll(lumpSumActivities);
            mbDetails = new ArrayList<MBDetails>();
            for (final Long activityId : sorActivitiesMap.keySet()) {
                detail = new MBDetails();
                for (final MBDetails details : hstr.getSorMbDetails())
                    if (activityId.equals(details.getWorkOrderActivity().getActivity().getId()))
                        detail = details;
                mbDetails.add(detail);

            }
            hstr.setSorMbDetails(mbDetails);

            mbDetails = new ArrayList<MBDetails>();
            for (final Long activityId : nonSorActivitiesMap.keySet()) {
                detail = new MBDetails();
                for (final MBDetails details : hstr.getNonSorMbDetails())
                    if (activityId.equals(details.getWorkOrderActivity().getActivity().getId()))
                        detail = details;
                mbDetails.add(detail);

            }
            hstr.setNonSorMbDetails(mbDetails);

            mbDetails = new ArrayList<MBDetails>();
            for (final Long activityId : ntActivitiesMap.keySet()) {
                detail = new MBDetails();
                for (final MBDetails details : hstr.getNonTenderedMbDetails())
                    if (activityId.equals(details.getWorkOrderActivity().getActivity().getId()))
                        detail = details;
                mbDetails.add(detail);

            }
            hstr.setNonTenderedMbDetails(mbDetails);

            mbDetails = new ArrayList<MBDetails>();
            for (final Long activityId : lsActivitiesMap.keySet()) {
                detail = new MBDetails();
                for (final MBDetails details : hstr.getLumpSumMbDetails())
                    if (activityId.equals(details.getWorkOrderActivity().getActivity().getId()))
                        detail = details;
                mbDetails.add(detail);

            }
            hstr.setLumpSumMbDetails(mbDetails);

        }

        return mbHistorys;
    }

}