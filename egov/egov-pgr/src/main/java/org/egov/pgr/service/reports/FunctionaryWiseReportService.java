package org.egov.pgr.service.reports;

import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infstr.services.Page;
import org.egov.pgr.entity.dto.DrillDownReportRequest;
import org.egov.pgr.entity.view.DrillDownReports;
import org.egov.pgr.repository.FunctionarywiseReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FunctionaryWiseReportService {

    @Autowired
    private FunctionarywiseReportRepository functionarywiseReportRepository;

    @ReadOnly
    public Page<DrillDownReports> pagedFunctionarwiseRecords(DrillDownReportRequest request) {
        return functionarywiseReportRepository.findByFunctionarywiseRequest(request);
    }

    @ReadOnly
    public Page<DrillDownReports> pagedFunctionarwiseReportByCompalints(DrillDownReportRequest request) {
        return functionarywiseReportRepository.findComplaintsByEmployeeId(request);
    }

    @ReadOnly
    public Object[] functionarywiseReportGrandTotal(DrillDownReportRequest request) {
        return functionarywiseReportRepository.findGrandTotalByRequest(request);
    }

    @ReadOnly
    public List<DrillDownReports> getAllFunctionarywiseRecords(DrillDownReportRequest request) {
        return functionarywiseReportRepository.findFunctionarywiseReportByRequest(request);
    }

    @ReadOnly
    public List<DrillDownReports> getFunctionarywiseRecordsByEmployee(DrillDownReportRequest request) {
        return functionarywiseReportRepository.findFunctionarywiseReportByEmployeeId(request);
    }
}