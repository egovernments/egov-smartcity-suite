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
package org.egov.works.contractorportal.service;

import static java.lang.Boolean.TRUE;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.egov.infra.messaging.MessagePriority.HIGH;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.CFinancialYear;
import org.egov.infra.messaging.MessagingService;
import org.egov.infra.security.token.service.TokenService;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.works.autonumber.ContractorMBNumberGenerator;
import org.egov.works.contractorportal.entity.ContractorMBDetails;
import org.egov.works.contractorportal.entity.ContractorMBHeader;
import org.egov.works.contractorportal.entity.ContractorMBMeasurementSheet;
import org.egov.works.contractorportal.repository.ContractorMBHeaderRepository;
import org.egov.works.letterofacceptance.service.WorkOrderActivityService;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrderActivity;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class ContractorMBHeaderService {

    private static final String CONTRACTOR_MB_SERVICE = "Contractor Measurement Book";

    @PersistenceContext
    private EntityManager entityManager;

    private final ContractorMBHeaderRepository contractorMBHeaderRepository;

    @Autowired
    private WorkOrderActivityService workOrderActivityService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource messageSource;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MessagingService messagingService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public ContractorMBHeaderService(final ContractorMBHeaderRepository contractorMBHeaderRepository) {
        this.contractorMBHeaderRepository = contractorMBHeaderRepository;
    }

    public ContractorMBHeader getContractorMBHeaderById(final Long id) {
        return contractorMBHeaderRepository.findOne(id);
    }

    public List<ContractorMBHeader> getContractorMBHeaderByWorkOrderEstimateId(final Long id) {
        return contractorMBHeaderRepository.findByWorkOrderEstimate_Id(id);
    }

    @Transactional
    public ContractorMBHeader create(final ContractorMBHeader contractorMBHeader, final MultipartFile[] files)
            throws IOException {
        final List<ContractorMBDetails> filteredDetails = new ArrayList<>();
        for (final ContractorMBDetails details : contractorMBHeader.getContractorMBDetails())
            if (details.getAmount() > 0) {
                details.setWorkOrderActivity(
                        workOrderActivityService.getWorkOrderActivityById(details.getWorkOrderActivity().getId()));
                for (final ContractorMBMeasurementSheet cmbms : details.getMeasurementSheets())
                    cmbms.setContractorMBDetails(details);
                details.setContractorMBHeader(contractorMBHeader);
                filteredDetails.add(details);
            }
        contractorMBHeader.setContractorMBDetails(filteredDetails);
        contractorMBHeader.setMbDate(new Date());
        mergeAdditionalItemDetails(contractorMBHeader);
        final CFinancialYear financialYear = worksUtils.getFinancialYearByDate(contractorMBHeader.getMbDate());
        final ContractorMBNumberGenerator numberGenerator = beanResolver
                .getAutoNumberServiceFor(ContractorMBNumberGenerator.class);
        contractorMBHeader.setMbRefNo(numberGenerator.getNextNumber(contractorMBHeader, financialYear));
        final ContractorMBHeader savedContractorMBHeader = contractorMBHeaderRepository.save(contractorMBHeader);

        final List<DocumentDetails> documentDetails = worksUtils.getDocumentDetails(files, savedContractorMBHeader,
                WorksConstants.CONTRACTORMBHEADER);
        if (!documentDetails.isEmpty()) {
            savedContractorMBHeader.setDocumentDetails(documentDetails);
            worksUtils.persistDocuments(documentDetails);
        }
        return savedContractorMBHeader;
    }

    public void populateContractorMBDetails(final ContractorMBHeader contractorMBHeader) {
        final List<WorkOrderActivity> workOrderActivities = workOrderActivityService
                .getWorkOrderActivitiesForContractorPortal(contractorMBHeader.getWorkOrderEstimate().getWorkOrder().getId());
        final List<WorkOrderActivity> mergedActivities = new ArrayList<>();
        for (final WorkOrderActivity workOrderActivity : workOrderActivities)
            if (workOrderActivity.getActivity().getParent() == null)
                mergedActivities.add(workOrderActivity);
        for (final WorkOrderActivity workOrderActivity : workOrderActivities)
            for (final WorkOrderActivity activity : mergedActivities)
                if (workOrderActivity.getActivity().getParent() != null
                        && activity.getActivity().getId() == workOrderActivity.getActivity().getParent().getId())
                    activity.setApprovedQuantity(activity.getApprovedQuantity() + workOrderActivity.getApprovedQuantity());
        if (contractorMBHeader.getContractorMBDetails().isEmpty())
            for (final WorkOrderActivity activity : mergedActivities) {
                final ContractorMBDetails details = new ContractorMBDetails();
                details.setWorkOrderActivity(activity);
                details.setRate(activity.getApprovedRate());
                contractorMBHeader.getContractorMBDetails().add(details);
            }
        else
            for (final ContractorMBDetails contractorMBDetails : contractorMBHeader.getContractorMBDetails()) {
                final WorkOrderActivity woa = workOrderActivityService
                        .getWorkOrderActivityById(contractorMBDetails.getWorkOrderActivity().getId());
                contractorMBDetails.setWorkOrderActivity(woa);
                contractorMBDetails.setRate(woa.getApprovedRate());
            }
    }

    public String validateContractorMBHeader(final ContractorMBHeader contractorMBHeader) {
        String message = "";
        boolean quantityExists = false;
        boolean additionalQuantityExists = false;
        for (final ContractorMBDetails contractorMBDetails : contractorMBHeader.getContractorMBDetails())
            if (contractorMBDetails.getQuantity() > 0)
                quantityExists = true;

        for (final ContractorMBDetails contractorMBDetails : contractorMBHeader.getAdditionalMBDetails())
            if (contractorMBDetails.getQuantity() > 0)
                additionalQuantityExists = true;

        if (!quantityExists && !additionalQuantityExists)
            message = messageSource.getMessage("error.mbdetails.quantity.zero", new String[] {}, null);

        return message;
    }

    @Transactional
    public Boolean sendOTPMessage(final String mobileNumber) {
        final String otp = randomNumeric(5);
        tokenService.generate(otp, mobileNumber, CONTRACTOR_MB_SERVICE);
        messagingService.sendSMS(mobileNumber, messageSource.getMessage("contractormb.otp.sms", new String[] { otp }, null),
                HIGH);
        return TRUE;
    }

    @Transactional
    public Boolean isValidOTP(final String otp, final String mobileNumber) {
        return tokenService.redeemToken(otp, mobileNumber, CONTRACTOR_MB_SERVICE);
    }

    private void mergeAdditionalItemDetails(final ContractorMBHeader contractorMBHeader) {
        for (final ContractorMBDetails details : contractorMBHeader.getAdditionalMBDetails())
            if (details.getId() == null) {
                removeEmptyMS(details);
                details.setContractorMBHeader(contractorMBHeader);
                for (final ContractorMBMeasurementSheet ms : details.getMeasurementSheets())
                    ms.setContractorMBDetails(details);
                contractorMBHeader.getContractorMBDetails().add(details);
            }
    }

    private void removeEmptyMS(final ContractorMBDetails details) {
        final List<ContractorMBMeasurementSheet> toRemove = new LinkedList<ContractorMBMeasurementSheet>();
        for (final ContractorMBMeasurementSheet ms : details.getMeasurementSheets())
            if (ms.getQuantity() == null || ms.getQuantity() != null && ms.getQuantity().equals(""))
                toRemove.add(ms);

        for (final ContractorMBMeasurementSheet msremove : toRemove)
            details.getMeasurementSheets().remove(msremove);
    }
}