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
import org.egov.bnd.model.NonAvailability;
import org.egov.bnd.services.common.NumberGenerationService;
import org.egov.bnd.utils.BndConstants;
import org.egov.infstr.services.PersistenceService;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Transactional(readOnly = true)
public class NonAvailabilityRegistrationService extends PersistenceService<NonAvailability, Long> {

    private static final Logger LOGGER = Logger.getLogger(NonAvailabilityRegistrationService.class);
    private NumberGenerationService numberGenerationService;
    private PersistenceService persistenceService;

    public NumberGenerationService getNumberGenerationService() {
        return numberGenerationService;
    }

    public void setNumberGenerationService(final NumberGenerationService numberGenerationService) {
        this.numberGenerationService = numberGenerationService;
    }

    @Transactional
    public NonAvailability save(final NonAvailability nonAvailableReg, final String workflowAction) {
        LOGGER.info("start save method");
        persist(nonAvailableReg);
        // startWorkFlow(deathnonAvail,workflowAction);
        LOGGER.info("end save method");
        return nonAvailableReg;
    }

    @Transactional
    public NonAvailability getNonAvailableRegById(final Long id) {
        return findById(id);
    }

    @Transactional
    public List searchRecordsByReceipt(final HashMap<String, Object> hashMap) {
        List<NonAvailability> nonAvailList = null;
        if (hashMap.get("REGTYPE").equals(BndConstants.SEARCHNONAVAILABILITY))
            nonAvailList = persistenceService
            .findAllBy(
                    "from NonAvailability nonAvail where nonAvail.id in ( select feeCollection.reportId from FeeCollection feeCollection, BillReceipt br where  feeCollection.egBills.id=br.billId.id and br.receiptNumber=? and feeCollection.type=? and br.isCancelled=?)",
                    hashMap.get("RECEIPTNO"), BndConstants.SEARCHNONAVAILABILITY, Boolean.FALSE);
        return nonAvailList;

    }

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }
}
