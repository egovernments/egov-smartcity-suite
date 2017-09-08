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

package org.egov.ptis.domain.service.reassign;

import static org.egov.ptis.constants.PropertyTaxConstants.APPCONFIG_REASSIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_GRP;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_VACANCY_REMISSION;
import static org.egov.ptis.constants.PropertyTaxConstants.GENERAL_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.REVISION_PETITION;

import java.util.List;

import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.egov.ptis.bean.ReassignInfo;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.property.VacancyRemissionService;
import org.egov.ptis.domain.service.revisionPetition.RevisionPetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReassignService {

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<StateAware> propertyWorkflowService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private VacancyRemissionService vacancyRemissionService;

    @Autowired
    private PersistenceService<StateAware, Long> persistenceService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private RevisionPetitionService revisionPetitionService;
    
    @Autowired
    private PropertyService propertyService;

    public User getLoggedInUser() {
        return securityUtils.getCurrentUser();
    }

    public Boolean getStateObject(final ReassignInfo reassignInfo, final Position position) {
        Long stateAwareId = reassignInfo.getStateAwareId();
        String transactionType = reassignInfo.getTransactionType();
        StateAware stateAware;
        if (APPLICATION_TYPE_VACANCY_REMISSION.equalsIgnoreCase(transactionType)) {
            stateAware = vacancyRemissionService.getVacancyRemissionById(stateAwareId);
        } else if (APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP.equalsIgnoreCase(transactionType)) {
            stateAware = persistenceService.find("From PropertyMutation where id = ? ", stateAwareId);
        } else if (GENERAL_REVISION_PETITION.equalsIgnoreCase(transactionType) || REVISION_PETITION.equalsIgnoreCase(transactionType)) {
            stateAware = revisionPetitionService.findById(Long.valueOf(stateAwareId), false);
            transactionType = transactionType.equalsIgnoreCase(REVISION_PETITION) ? APPLICATION_TYPE_REVISION_PETITION
                    : APPLICATION_TYPE_GRP;
        } else {
            stateAware = persistenceService.findByNamedQuery(QUERY_PROPERTYIMPL_BYID, Long.valueOf(stateAwareId));
        }
        stateAware.transition().progressWithStateCopy().withOwner(position).withInitiator(position);
        propertyService.updateIndexes(stateAware, transactionType);
        persistenceService.persist(stateAware);
        return true;
    }

    public boolean isReassignEnabled() {
        final List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(PTMODULENAME,
                APPCONFIG_REASSIGN);
        return !appConfigValues.isEmpty() && "Y".equals(appConfigValues.get(0).getValue()) ? true : false;
    }
}