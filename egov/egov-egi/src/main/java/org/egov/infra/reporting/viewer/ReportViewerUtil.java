/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
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

package org.egov.infra.reporting.viewer;

import org.egov.infra.cache.impl.ApplicationCacheManager;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.UUID;

@Service
public class ReportViewerUtil {
    private static final EnumMap<FileFormat, String> contentTypes = getContentTypes();

    @Autowired
    private ApplicationCacheManager applicationCacheManager;

    private static EnumMap<FileFormat, String> getContentTypes() {
        EnumMap<FileFormat, String> contentTypes = new EnumMap<>(FileFormat.class);
        contentTypes.put(FileFormat.PDF, "application/pdf");
        contentTypes.put(FileFormat.XLS, "application/vnd.ms-excel");
        contentTypes.put(FileFormat.RTF, "application/rtf");
        contentTypes.put(FileFormat.HTM, "text/html");
        contentTypes.put(FileFormat.TXT, "text/plain");
        contentTypes.put(FileFormat.CSV, "text/plain");
        return contentTypes;
    }

    public static String getContentType(FileFormat fileFormat) {
        return contentTypes.get(fileFormat);
    }

    public static String getContentDisposition(FileFormat fileFormat) {
        return "inline; filename=report." + fileFormat.toString();
    }

    public String addReportToTempCache(ReportOutput reportOutput) {
        String reportId = UUID.randomUUID().toString();
        applicationCacheManager.put(reportId, reportOutput);
        return reportId;
    }

    public ReportOutput getReportOutputFormCache(String reportOutputCacheKey) {
        return applicationCacheManager.get(reportOutputCacheKey, ReportOutput.class);
    }

    public void removeReportOutputFromCache(String reportOutputCacheKey) {
        applicationCacheManager.remove(reportOutputCacheKey);
    }
}
