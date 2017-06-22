package org.egov.pgr.service.reports;

import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infstr.services.Page;
import org.egov.pgr.entity.dto.FunctionarywiseReportRequest;
import org.egov.pgr.entity.view.FunctionarywiseReport;
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
    public Page<FunctionarywiseReport> pagedFunctionarwiseRecords(FunctionarywiseReportRequest request) {
        return functionarywiseReportRepository.findByFunctionarywiseRequest(request);
    }

    @ReadOnly
    public Page<FunctionarywiseReport> pagedFunctionarwiseReportByCompalints(FunctionarywiseReportRequest request) {
        return functionarywiseReportRepository.findComplaintsByEmployeeId(request);
    }

    @ReadOnly
    public Object[] functionarywiseReportGrandTotal(FunctionarywiseReportRequest request) {
        return functionarywiseReportRepository.findGrandTotalByRequest(request);
    }

    @ReadOnly
    public List<FunctionarywiseReport> getAllFunctionarywiseRecords(FunctionarywiseReportRequest request) {
        return functionarywiseReportRepository.findFunctionarywiseReportByRequest(request);
    }

    @ReadOnly
    public List<FunctionarywiseReport> getFunctionarywiseRecordsByEmployee(FunctionarywiseReportRequest request) {
        return functionarywiseReportRepository.findFunctionarywiseReportByEmployeeId(request);
    }
}