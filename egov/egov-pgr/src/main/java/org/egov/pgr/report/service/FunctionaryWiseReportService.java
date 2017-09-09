/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.report.service;

import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infstr.services.Page;
import org.egov.pgr.report.entity.contract.DrillDownReportRequest;
import org.egov.pgr.report.entity.view.DrillDownReportView;
import org.egov.pgr.report.repository.FunctionarywiseReportRepository;
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
    public Page<DrillDownReportView> pagedFunctionarwiseRecords(DrillDownReportRequest request) {
        return functionarywiseReportRepository.findByFunctionarywiseRequest(request);
    }

    @ReadOnly
    public Page<DrillDownReportView> pagedFunctionarwiseReportByCompalints(DrillDownReportRequest request) {
        return functionarywiseReportRepository.findComplaintsByEmployeeId(request);
    }

    @ReadOnly
    public Object[] functionarywiseReportGrandTotal(DrillDownReportRequest request) {
        return functionarywiseReportRepository.findGrandTotalByRequest(request);
    }

    @ReadOnly
    public List<DrillDownReportView> getAllFunctionarywiseRecords(DrillDownReportRequest request) {
        return functionarywiseReportRepository.findFunctionarywiseReportByRequest(request);
    }

    @ReadOnly
    public List<DrillDownReportView> getFunctionarywiseRecordsByEmployee(DrillDownReportRequest request) {
        return functionarywiseReportRepository.findFunctionarywiseReportByEmployeeId(request);
    }
}