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
package org.egov.works.services;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.models.tender.TenderResponse;
import org.egov.works.models.tender.TenderResponseActivity;
import org.egov.works.models.tender.TenderResponseContractors;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TenderResponseService extends PersistenceService<TenderResponse, Long> {
    private static final Logger logger = Logger.getLogger(TenderResponseService.class);
    private PersonalInformationService personalInformationService;
    private PersistenceService persistenceService;

    public TenderResponseService() {
        super(TenderResponse.class);
    }

    public TenderResponseService(Class<TenderResponse> type) {
        super(type);
    }

    public List getApprovedByList(final Integer deptId) {
        List approvedByList = null;
        try {
            // approvedByList = eisManager.searchEmployee(deptId, 0, null, null, 0);
            final HashMap<String, Object> criteriaParams = new HashMap<String, Object>();
            criteriaParams.put("departmentId", deptId);
            criteriaParams.put("isPrimary", "Y");
            approvedByList = personalInformationService.getListOfEmployeeViewBasedOnCriteria(criteriaParams, -1, -1);
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("user.find.error", e);
        }
        return approvedByList;
    }

    public List populateNegotiationPreparedByList(final AbstractEstimate abstractEstimate) {
        List negotiationPreparedByList = null;
        if (abstractEstimate != null && abstractEstimate.getExecutingDepartment() != null)
            try {
                final HashMap<String, Object> criteriaParams = new HashMap<String, Object>();
                criteriaParams.put("departmentId", abstractEstimate.getExecutingDepartment().getId());
                criteriaParams.put("isPrimary", "Y");
                // negotiationPreparedByList = eisManager.searchEmployee(abstractEstimate.getExecutingDepartment().getId(),
                // 0,null, null, 0);
                negotiationPreparedByList = personalInformationService.getListOfEmployeeViewBasedOnCriteria(criteriaParams, -1,
                        -1);
            } catch (final Exception e) {
                logger.info("-----inside tenderResponseservice---------Exception");
                negotiationPreparedByList = Collections.emptyList();
            }
        if (negotiationPreparedByList == null)
            negotiationPreparedByList = Collections.emptyList();

        return negotiationPreparedByList;
    }

    /*
     * returns employee name and designation
     * @ return String
     * @ abstractEstimate, eisManager
     */

    /*
     * public PersonalInformation getPersonalInformation(Position position) { PersonalInformation personalInformation = null; try
     * { personalInformation = employeeService.getEmpForPosition(position.getEfferctiveDate(), position.getId()); } catch
     * (Exception e) { logger.debug("exception " + e); } return personalInformation; }
     */

    public Collection<TenderResponseActivity> getTenderResponseActivityList(
            final List<TenderResponseActivity> actionTenderResponseActivities) {
        return CollectionUtils.select(
                actionTenderResponseActivities,
                tenderReponseAct -> {
                    final TenderResponseActivity tra = (TenderResponseActivity) tenderReponseAct;
                    if (tra == null)
                        return false;
                    else {
                        tra.setActivity((Activity) persistenceService.find("from Activity where id=?",
                                tra.getActivity().getId()));
                        return true;
                    }

                });
    }

    public Collection<TenderResponseContractors> getActionTenderResponseContractorsList(
            final List<TenderResponseContractors> actionTenderResponseContractors) {
        return CollectionUtils.select(actionTenderResponseContractors,
                tenderResponseContractors -> (TenderResponseContractors) tenderResponseContractors != null);
    }

    public void setPersonalInformationService(
            final PersonalInformationService personalInformationService) {
        this.personalInformationService = personalInformationService;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

}
