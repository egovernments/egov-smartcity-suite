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

package org.egov.infra.reporting.util;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.NumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import static java.lang.String.format;
import static org.egov.infra.config.core.ApplicationThreadLocals.getDomainURL;
import static org.egov.infra.config.core.ApplicationThreadLocals.getMunicipalityName;
import static org.egov.infra.config.core.ApplicationThreadLocals.getTenantID;
import static org.egov.infra.reporting.engine.ReportConstants.CANCELLED_WATERMARK_IMAGE_PATH;
import static org.egov.infra.reporting.engine.ReportConstants.DEFAULT_REPORT_FILE_PATH;
import static org.egov.infra.reporting.engine.ReportConstants.IMAGES_BASE_PATH;
import static org.egov.infra.reporting.engine.ReportConstants.REPORT_CONFIG_FILE;
import static org.egov.infra.reporting.engine.ReportConstants.TENANT_COMMON_REPORT_FILE_LOCATION;
import static org.egov.infra.reporting.engine.ReportConstants.TENANT_REPORT_FILE_PATH;
import static org.egov.infra.reporting.engine.ReportFormat.PDF;
import static org.egov.infra.utils.ApplicationConstant.CITY_LOGO_URL;
import static org.egov.infra.utils.ApplicationConstant.CONTENT_DISPOSITION;

public final class ReportUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportUtil.class);

    private ReportUtil() {
        //static api's
    }

    public static InputStream getImageAsStream(String imageName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(IMAGES_BASE_PATH + imageName);
    }

    public static String getCityName() {
        return getMunicipalityName();
    }

    public static InputStream getTemplateAsStream(String templateName) {
        InputStream fileInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                format(TENANT_REPORT_FILE_PATH, getTenantID(), templateName));
        if (fileInputStream == null)
            fileInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                    format(TENANT_REPORT_FILE_PATH, TENANT_COMMON_REPORT_FILE_LOCATION, templateName));
        if (fileInputStream == null)
            fileInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                    format(DEFAULT_REPORT_FILE_PATH, templateName));
        if (fileInputStream == null) {
            String errMsg = "File [" + templateName + "] could not be loaded from CLASSPATH!";
            LOGGER.error(errMsg);
            throw new ApplicationRuntimeException(errMsg);
        }
        return fileInputStream;
    }

    public static Properties loadReportConfig() {
        Properties reportProps = null;
        try {
            InputStream configStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(REPORT_CONFIG_FILE);
            if (configStream != null) {
                reportProps = new Properties();
                reportProps.load(configStream);
            }
            return reportProps;
        } catch (IOException e) {
            LOGGER.warn("Exception while loading report configuration file [{}]", REPORT_CONFIG_FILE, e);
            return null;
        }
    }

    public static Object fetchFromDBSql(Connection connection, String sqlQuery) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getString(1) : null;
        } catch (SQLException e) {
            String errMsg = "Exception while executing query [" + sqlQuery + "]";
            LOGGER.error(errMsg, e);
            throw new ApplicationRuntimeException(errMsg, e);
        }
    }

    public static Date getDate(int year, int month, int date) {
        return DateUtils.getDate(year, month, date);
    }

    public static Date today() {
        return DateUtils.today();
    }

    public static Date now() {
        return DateUtils.now();
    }

    public static Date tomorrow() {
        return DateUtils.tomorrow();
    }

    public static Date add(Date inputDate, int addType, int addAmount) {
        return DateUtils.add(inputDate, addType, addAmount);
    }

    public static String amountInWords(BigDecimal amount) {
        return NumberUtil.amountInWords(amount);
    }

    public static String formatNumber(BigDecimal number, int fractionDigits, boolean useGrouping) {
        return NumberUtil.formatNumber(number, fractionDigits, useGrouping);
    }

    public static String logoBasePath() {
        return format(CITY_LOGO_URL, getDomainURL());
    }

    public static String cancelledWatermarkAbsolutePath() {
        return format(CANCELLED_WATERMARK_IMAGE_PATH, getDomainURL());
    }

    public static ResponseEntity<InputStreamResource> reportAsResponseEntity(ReportOutput reportOutput) {
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (PDF.equals(reportOutput.getReportFormat()))
            mediaType = MediaType.APPLICATION_PDF;
        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .cacheControl(CacheControl.noCache())
                .contentLength(reportOutput.getReportOutputData().length)
                .header(CONTENT_DISPOSITION, reportOutput.reportDisposition())
                .body(new InputStreamResource(new ByteArrayInputStream(reportOutput.getReportOutputData())));
    }
}
