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
package org.egov.lcms.transactions.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.eis.service.EmployeeService;
import org.egov.infra.utils.DateUtils;
import org.egov.lcms.transactions.entity.LcInterimOrderDocuments;
import org.egov.lcms.transactions.entity.LegalCase;
import org.egov.lcms.transactions.entity.LegalCaseInterimOrder;
import org.egov.lcms.transactions.repository.LegalCaseInterimOrderRepository;
import org.egov.lcms.utils.LegalCaseUtil;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LegalCaseInterimOrderService {

    private final LegalCaseInterimOrderRepository legalCaseInterimOrderRepository;

    @Autowired
    private LegalCaseUtil legalCaseUtil;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    public LegalCaseInterimOrderService(final LegalCaseInterimOrderRepository legalCaseInterimOrderRepository) {
        this.legalCaseInterimOrderRepository = legalCaseInterimOrderRepository;
    }

    @Transactional
    public LegalCaseInterimOrder persist(final LegalCaseInterimOrder legalCaseInterimOrder) {
        final EgwStatus statusObj = legalCaseUtil.getStatusForModuleAndCode(LcmsConstants.MODULE_TYPE_LEGALCASE,
                LcmsConstants.LEGALCASE_STATUS_IN_PROGRESS);
        updateNextDate(legalCaseInterimOrder, legalCaseInterimOrder.getLegalCase());
        legalCaseInterimOrder.getLegalCase().setStatus(statusObj);
        final List<LcInterimOrderDocuments> interiomOrderDoc = legalCaseUtil
                .getLcInterimOrderDocumentList(legalCaseInterimOrder);
        processAndStoreApplicationDocuments(legalCaseInterimOrder, interiomOrderDoc);
        legalCaseInterimOrder.setEmployee(employeeService.getEmployeeById(legalCaseInterimOrder.getEmployee().getId()));
        /* legalCaseRepository.save(legalCaseInterimOrder.getLegalCase()); */
        return legalCaseInterimOrderRepository.save(legalCaseInterimOrder);
    }

    public List<LegalCaseInterimOrder> findAll() {
        return legalCaseInterimOrderRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
    }

    public LegalCaseInterimOrder findById(final Long id) {
        return legalCaseInterimOrderRepository.findById(id);
    }

    public List<LcInterimOrderDocuments> getLcInterimOrderDocList(final LegalCaseInterimOrder legalCaseInterimOrder) {
        return legalCaseInterimOrder.getLcInterimOrderDocuments();
    }

    protected void processAndStoreApplicationDocuments(final LegalCaseInterimOrder legalCaseInterimOrder,
            final List<LcInterimOrderDocuments> interimOrderDoc) {
        if (legalCaseInterimOrder.getId() == null) {
            if (!legalCaseInterimOrder.getLcInterimOrderDocuments().isEmpty())
                for (final LcInterimOrderDocuments applicationDocument : legalCaseInterimOrder
                        .getLcInterimOrderDocuments()) {
                    applicationDocument.setLegalCaseInterimOrder(legalCaseInterimOrder);
                    applicationDocument.setDocumentName("LcInterimOrder");
                    applicationDocument.setSupportDocs(legalCaseUtil.addToFileStore(applicationDocument.getFiles()));
                }
        } else {
            final List<LcInterimOrderDocuments> tempLcInterimDoc = new ArrayList<LcInterimOrderDocuments>(
                    legalCaseInterimOrder.getLcInterimOrderDocuments());
            for (final LcInterimOrderDocuments applicationDocument : tempLcInterimDoc) {
                applicationDocument.setLegalCaseInterimOrder(legalCaseInterimOrder);
                applicationDocument.setDocumentName("LcInterimOrder");
                applicationDocument.getSupportDocs()
                        .addAll(legalCaseUtil.addToFileStore(applicationDocument.getFiles()));
                legalCaseInterimOrder.getLcInterimOrderDocuments().add(applicationDocument);
            }
            legalCaseInterimOrder.getLcInterimOrderDocuments().addAll(interimOrderDoc);
        }

    }

    public List<LegalCaseInterimOrder> findByLCNumber(final String lcNumber) {
        return legalCaseInterimOrderRepository.findByLegalCase_lcNumber(lcNumber);
    }

    public void updateNextDate(final LegalCaseInterimOrder legalCaseInterimOrder, final LegalCase legalCase) {

        if (!DateUtils.compareDates(legalCase.getNextDate(), legalCaseInterimOrder.getIoDate()))
            legalCase.setNextDate(legalCaseInterimOrder.getIoDate());
        else {
            final List<Date> ioDateList = new ArrayList<Date>(0);
            ioDateList.add(legalCaseInterimOrder.getIoDate());
            final Iterator<LegalCaseInterimOrder> iteratorInterimOrder = legalCase.getLegalCaseInterimOrder()
                    .iterator();
            while (iteratorInterimOrder.hasNext()) {
                final LegalCaseInterimOrder lcinterimorderObj = iteratorInterimOrder.next();
                if (!lcinterimorderObj.getId().equals(legalCaseInterimOrder.getId()))
                    ioDateList.add(lcinterimorderObj.getIoDate());
            }

            legalCase.setNextDate(Collections.max(ioDateList));
        }
    }

}