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

package org.egov.infra.reporting.util;

import org.apache.struts2.ServletActionContext;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.NumberUtil;
import org.egov.infra.web.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import static org.egov.infra.reporting.engine.ReportConstants.DEFAULT_REPORT_FILE_PATH;
import static org.egov.infra.reporting.engine.ReportConstants.IMAGES_BASE_PATH;
import static org.egov.infra.reporting.engine.ReportConstants.IMAGE_CONTEXT_PATH;
import static org.egov.infra.reporting.engine.ReportConstants.REPORT_CONFIG_FILE;

public final class ReportUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportUtil.class);
    private static final String TENANT_REPORT_FILE_PATH = DEFAULT_REPORT_FILE_PATH + "/%s";
    private static final String TENANT_COMMON_REPORT_FILE_LOCATION = "common";

    private ReportUtil() {
        //static api's
    }

    public static InputStream getImageAsStream(String imageName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(IMAGES_BASE_PATH + imageName);
    }

    public static String getCityName() {
        return ApplicationThreadLocals.getMunicipalityName();
    }

    public static InputStream reportTemplateAsStream(String templateName) {
        InputStream fileInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                String.format(TENANT_REPORT_FILE_PATH, ApplicationThreadLocals.getTenantID(), templateName));
        if (fileInputStream == null)
            fileInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                    String.format(TENANT_REPORT_FILE_PATH, TENANT_COMMON_REPORT_FILE_LOCATION, templateName));
        if (fileInputStream == null)
            fileInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                    String.format(DEFAULT_REPORT_FILE_PATH, templateName));
        if (fileInputStream == null) {
            String errMsg = "File [" + templateName + "] could not be loaded from CLASSPATH!";
            LOGGER.error(errMsg);
            throw new ApplicationRuntimeException(errMsg);
        }
        return fileInputStream;
    }

    public static Properties loadReportConfig() {
        Properties reportProps = new Properties();
        try {
            reportProps.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(REPORT_CONFIG_FILE));
            return reportProps;
        } catch (Exception e) {
            LOGGER.warn("Exception while loading report configuration file [{}]", REPORT_CONFIG_FILE, e);
            return null;
        }
    }

    public static Object fetchFromDBSql(Connection connection, String sqlQuery) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet != null && resultSet.next() ? resultSet.getString(1) : null;
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
        HttpServletRequest request = ServletActionContext.getRequest();
        String url = WebUtils.extractRequestDomainURL(request, false);
        return url.concat(IMAGE_CONTEXT_PATH).concat(
                (String) request.getSession().getAttribute("citylogo"));
    }

    public static String cancelledWatermarkAbsolutePath() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String url = WebUtils.extractRequestDomainURL(request, false);
        return url.concat(IMAGE_CONTEXT_PATH).concat(
                "/resources/global/images/cancelled_watermark.png");
    }
}
