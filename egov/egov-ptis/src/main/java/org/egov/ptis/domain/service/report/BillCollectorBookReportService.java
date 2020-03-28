package org.egov.ptis.domain.service.report;

import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;

import java.util.Date;
import java.util.List;

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

}
