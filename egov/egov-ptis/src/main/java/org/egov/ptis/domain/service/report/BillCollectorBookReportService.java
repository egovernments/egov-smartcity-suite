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
package org.egov.ptis.domain.service.report;

import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_ADVANCE;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.view.BillCollectorCollectionRequest;
import org.egov.ptis.domain.entity.property.view.CollectionIndexInfo;
import org.egov.ptis.repository.reports.CollectionIndexInfoRepository;
import org.egov.ptis.repository.spec.BillCollectorCollectionBookSpec;
import org.hibernate.Session;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BillCollectorBookReportService {

    @Autowired
    private CollectionIndexInfoRepository billCollectorBookRepository;
    @Autowired
    private FinancialYearDAO financialYearDAO;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @ReadOnly
    public Page<CollectionIndexInfo> pagedBCCOllectionRecords(
            final BillCollectorCollectionRequest billCollectorCollectionRequest) {
        return billCollectorBookRepository.findAll(
                BillCollectorCollectionBookSpec.bcCollectionSpecification(billCollectorCollectionRequest),
                new PageRequest(billCollectorCollectionRequest.pageNumber(), billCollectorCollectionRequest.pageSize(),
                        billCollectorCollectionRequest.orderDir(), billCollectorCollectionRequest.orderBy()));

    }

    @ReadOnly
    public List<CollectionIndexInfo> getAllBCCOllectionRecords(
            final BillCollectorCollectionRequest billCollectorCollectionRequest) {
        return billCollectorBookRepository
                .findAll(BillCollectorCollectionBookSpec.bcCollectionSpecification(billCollectorCollectionRequest));
    }

    public List<CFinancialYear> getFinancialYears() {
        return financialYearDAO
                .getFinancialYearsAfterFromDate(
                        DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(getStartDateFromAppConfig()).toDate(), new Date());
    }

    public String getStartDateFromAppConfig() {
        final List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(PTMODULENAME,
                PropertyTaxConstants.PTYEARWISEDCBSTARTYEAR);
        return appConfigValues.get(0).getValue();
    }

    public Boolean getbalanceAmount(String receiptNum) {
        BigDecimal totalAmountToBePaid = BigDecimal.ZERO;
        final Query qry = entityManager
                .createQuery("from ReceiptHeader where receiptnumber = :receiptNum");
        qry.setParameter("receiptNum", receiptNum);
        final ReceiptHeader receiptHeader = (ReceiptHeader) qry.getSingleResult();
        for (ReceiptDetail receiptdetatail : receiptHeader.getReceiptDetails()) {
            if (receiptdetatail.getCramountToBePaid() != null
                    && receiptdetatail.getCramountToBePaid().compareTo(BigDecimal.ZERO) > 0
                    && !receiptdetatail.getDescription().contains(DEMANDRSN_STR_ADVANCE))
                totalAmountToBePaid = totalAmountToBePaid.add(receiptdetatail.getCramountToBePaid());
        }
        if (totalAmountToBePaid.compareTo(receiptHeader.getTotalAmount()) > 0)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}
