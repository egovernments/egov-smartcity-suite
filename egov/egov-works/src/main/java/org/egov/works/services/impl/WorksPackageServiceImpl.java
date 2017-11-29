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
package org.egov.works.services.impl;

import org.egov.commons.CFinancialYear;
import org.egov.infra.utils.DateUtils;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.models.tender.EstimateLineItemsForWP;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.models.tender.WorksPackageDetails;
import org.egov.works.models.tender.WorksPackageNumberGenerator;
import org.egov.works.services.WorksPackageService;
import org.egov.works.utils.WorksConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorksPackageServiceImpl extends BaseServiceImpl<WorksPackage, Long> implements WorksPackageService {
    private WorksPackageNumberGenerator workspackageGenerator;

    public WorksPackageServiceImpl(final PersistenceService<WorksPackage, Long> persistenceService) {
        super(persistenceService);
    }

    @Override
    public void setWorksPackageNumber(final WorksPackage entity, final CFinancialYear finYear) {
        if (entity.getWpNumber() == null)
            entity.setWpNumber(workspackageGenerator.getWorksPackageNumber(entity, finYear));
    }

    @Override
    public List<AbstractEstimate> getAbStractEstimateListByWorksPackage(final WorksPackage entity) {
        final List<AbstractEstimate> abList = new ArrayList<AbstractEstimate>();
        if (entity != null && !entity.getWorksPackageDetails().isEmpty())
            for (final WorksPackageDetails wpd : entity.getWorksPackageDetails())
                abList.add(wpd.getEstimate());
        return abList;
    }

    public void setWorkspackageGenerator(final WorksPackageNumberGenerator workspackageGenerator) {
        this.workspackageGenerator = workspackageGenerator;
    }

    @Override
    public Collection<EstimateLineItemsForWP> getActivitiesForEstimate(final WorksPackage wpObj) {
        final Map<Long, EstimateLineItemsForWP> resultMap = new HashMap<Long, EstimateLineItemsForWP>();
        final List<AbstractEstimate> abList = getAbStractEstimateListByWorksPackage(wpObj);
        for (final Activity act : getAllActivities(abList)) {
            final EstimateLineItemsForWP estlineItem = new EstimateLineItemsForWP();
            if (act.getSchedule() != null)
                if (resultMap.containsKey(act.getSchedule().getId())) {
                    final EstimateLineItemsForWP preEstlineItem = resultMap.get(act.getSchedule().getId());
                    preEstlineItem.setQuantity(act.getQuantity() + preEstlineItem.getQuantity());
                    if (DateUtils.compareDates(act.getAbstractEstimate().getEstimateDate(),
                            preEstlineItem.getEstimateDate())) {
                        preEstlineItem.setRate(act.getSORCurrentRate().getValue());
                        preEstlineItem.setAmt(preEstlineItem.getQuantity() * act.getRate());
                    }
                    resultMap.put(act.getSchedule().getId(), preEstlineItem);
                } else {
                    addEstLineItem(act, estlineItem);
                    resultMap.put(act.getSchedule().getId(), estlineItem);
                }
            if (act.getNonSor() != null) {
                addEstLineItem(act, estlineItem);
                resultMap.put(act.getNonSor().getId(), estlineItem);
            }
        }
        return getEstLineItemsWithSrlNo(resultMap.values());
    }

    private void addEstLineItem(final Activity act, final EstimateLineItemsForWP estlineItem) {
        if (act.getSchedule() == null) {
            estlineItem.setCode("");
            estlineItem.setDescription(act.getNonSor().getDescription());
            estlineItem.setRate(act.getRate());
        } else {
            estlineItem.setCode(act.getSchedule().getCode());
            estlineItem.setDescription(act.getSchedule().getDescription());
            estlineItem.setRate(act.getSORCurrentRate().getValue());
        }
        estlineItem.setAmt(act.getQuantity() * act.getRate());
        estlineItem.setEstimateDate(act.getAbstractEstimate().getEstimateDate());
        estlineItem.setQuantity(act.getQuantity());
        estlineItem.setUom(act.getUom().getUom());
    }

    private List<Activity> getAllActivities(final List<AbstractEstimate> abList) {
        final List<Activity> actList = new ArrayList<Activity>();
        for (final AbstractEstimate ab : abList)
            actList.addAll(ab.getActivities());
        return actList;
    }

    @Override
    public double getTotalAmount(final Collection<EstimateLineItemsForWP> actList) {
        double totalAmt = 0;
        for (final EstimateLineItemsForWP act : actList)
            totalAmt += act.getAmt();
        return totalAmt;
    }

    private Collection<EstimateLineItemsForWP> getEstLineItemsWithSrlNo(final Collection<EstimateLineItemsForWP> actList) {
        int i = 1;
        final Collection<EstimateLineItemsForWP> latestEstLineItemList = new ArrayList<EstimateLineItemsForWP>();
        for (final EstimateLineItemsForWP act : actList) {
            act.setSrlNo(i);
            latestEstLineItemList.add(act);
            i++;
        }
        return latestEstLineItemList;
    }

    @Override
    public WorksPackage getWorksPackageForAbstractEstimate(final AbstractEstimate estimate) {
        final WorksPackage wp = persistenceService
                .find("select wpd.worksPackage from WorksPackageDetails wpd where wpd.estimate.estimateNumber = ? and wpd.worksPackage.egwStatus.code<>'CANCELLED'",
                        estimate.getEstimateNumber());
        return wp;
    }

    @Override
    public List<Object> getWorksPackageDetails(final Long estimateId) {
        final List<Object> wpDetails = genericService.findAllBy(
                "select wpd.worksPackage.id, wpd.worksPackage.wpNumber from WorksPackageDetails wpd "
                        + " where wpd.estimate.id= ? and  wpd.worksPackage.egwStatus.code not in (?,?) ",
                estimateId,
                WorksConstants.NEW, WorksConstants.CANCELLED_STATUS);
        return wpDetails;
    }
}
