/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.contractorbill.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.model.bills.EgBillregistermis;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.repository.ContractorBillRegisterRepository;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class ContractorBillRegisterService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ContractorBillRegisterRepository contractorBillRegisterRepository;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private FinancialYearHibernateDAO financialYearHibernateDAO;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public ContractorBillRegisterService(final ContractorBillRegisterRepository contractorBillRegisterRepository) {
        this.contractorBillRegisterRepository = contractorBillRegisterRepository;
    }

    public ContractorBillRegister getContractorBillById(final Long id) {
        return contractorBillRegisterRepository.findOne(id);
    }

    public Integer getMaxSequenceNumberByWorkOrder(final WorkOrder workOrder) {
        return contractorBillRegisterRepository.findMaxBillSequenceNumberByWorkOrder(workOrder);
    }

    public ContractorBillRegister getContractorBillByBillNumber(final String billNumber) {
        return contractorBillRegisterRepository.findByBillnumber(billNumber);
    }

    @Transactional
    public ContractorBillRegister create(final ContractorBillRegister contractorBillRegister,
            final LineEstimateDetails lineEstimateDetails, final MultipartFile[] files)
                    throws IOException {

        contractorBillRegister.setStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.CONTRACTORBILL,
                ContractorBillRegister.BillStatus.APPROVED.toString()));
        contractorBillRegister.setBillstatus(contractorBillRegister.getStatus().getCode());
        contractorBillRegister.setExpendituretype(WorksConstants.BILL_EXPENDITURE_TYPE);
        final EgBillregistermis egBillRegisterMis = setEgBillRegisterMis(contractorBillRegister, lineEstimateDetails);
        contractorBillRegister.setEgBillregistermis(egBillRegisterMis);
        ;
        final ContractorBillRegister savedContractorBillRegister = contractorBillRegisterRepository.save(contractorBillRegister);
        final List<DocumentDetails> documentDetails = worksUtils.getDocumentDetails(files, savedContractorBillRegister,
                WorksConstants.CONTRACTORBILL);
        if (!documentDetails.isEmpty()) {
            savedContractorBillRegister.setDocumentDetails(documentDetails);
            worksUtils.persistDocuments(documentDetails);
        }
        return savedContractorBillRegister;
    }

    private EgBillregistermis setEgBillRegisterMis(final ContractorBillRegister contractorBillRegister,
            final LineEstimateDetails lineEstimateDetails) {
        final EgBillregistermis egBillRegisterMis = contractorBillRegister.getEgBillregistermis();
        egBillRegisterMis.setEgBillregister(contractorBillRegister);
        egBillRegisterMis.setPayto(contractorBillRegister.getWorkOrder().getContractor().getName());
        egBillRegisterMis.setFieldid(lineEstimateDetails.getLineEstimate().getWard());

        if (lineEstimateDetails.getLineEstimate().getFund() != null)
            egBillRegisterMis.setFund(lineEstimateDetails.getLineEstimate().getFund());
        if (lineEstimateDetails.getLineEstimate().getFunction() != null)
            egBillRegisterMis.setFunction(lineEstimateDetails.getLineEstimate().getFunction());
        if (lineEstimateDetails.getLineEstimate().getScheme() != null)
            egBillRegisterMis.setScheme(lineEstimateDetails.getLineEstimate().getScheme());
        if (lineEstimateDetails.getLineEstimate().getSubScheme() != null)
            egBillRegisterMis.setSubScheme(lineEstimateDetails.getLineEstimate().getSubScheme());

        egBillRegisterMis.setEgDepartment(lineEstimateDetails.getLineEstimate().getExecutingDepartment());
        final CFinancialYear financialYear = financialYearHibernateDAO
                .getFinancialYearByDate(contractorBillRegister.getBilldate());
        egBillRegisterMis.setFinancialyear(financialYear);
        egBillRegisterMis.setLastupdatedtime(new Date());
        return egBillRegisterMis;
    }

}