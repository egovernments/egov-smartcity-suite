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

package org.egov.mrs.application.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.commons.Installment;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.infra.admin.master.entity.Module;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.domain.enums.MarriageFeeType;
import org.springframework.stereotype.Service;

/**
 * Creates Re-Issue fee details
 * 
 * @author nayeem
 *
 */

@Service
public class ReIssueDemandService extends MarriageDemandService {

    @Override
    public Set<EgDemandDetails> createDemandDetails(final BigDecimal amount) {
        final Set<EgDemandDetails> demandDetails = new HashSet<>();
        final Module module = moduleService.getModuleByName(MarriageConstants.MODULE_NAME);
        final Installment installment = installmentDAO.getInsatllmentByModuleForGivenDate(module, new Date());
        final EgDemandReasonMaster demandReasonMaster = demandGenericDAO
                .getDemandReasonMasterByCode(MarriageFeeType.CERTIFICATEISSUE.name(), module);
        final EgDemandReason demandReason = demandGenericDAO
                .getDmdReasonByDmdReasonMsterInstallAndMod(demandReasonMaster, installment, module);
        demandDetails.add(EgDemandDetails.fromReasonAndAmounts(amount, demandReason, BigDecimal.ZERO));
        return demandDetails;
    }

    @Override
    public void updateDemand(EgDemand demand, BigDecimal amount) {
        if (demand != null) {
            for (final EgDemandDetails dmdDtl : demand.getEgDemandDetails()) {
                if (dmdDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()
                        .equalsIgnoreCase(MarriageFeeType.CERTIFICATEISSUE.name())) {
                    dmdDtl.setAmount(amount);
                }
            }
            demand.setBaseDemand(amount);
        }
    }

    /**
     * 
     * @param marriageRegistration
     * @return
     */
    public Boolean checkAnyTaxIsPendingToCollect(final ReIssue reIssue) {
        Boolean pendingTaxCollection = false;
        if (reIssue != null && reIssue.getDemand() != null)
            for (final EgDemandDetails demandDtl : reIssue.getDemand().getEgDemandDetails())
                if (demandDtl.getAmount().subtract(demandDtl.getAmtCollected()).compareTo(BigDecimal.ZERO) > 0) {
                    pendingTaxCollection = true;
                    break;
                }
        return pendingTaxCollection;
    }
}
