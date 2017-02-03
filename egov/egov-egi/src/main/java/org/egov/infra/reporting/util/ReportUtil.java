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

import static org.egov.infra.utils.ApplicationConstant.CITY_LOGO_PATH_KEY;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.ReportConstants;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.NumberUtil;
import org.egov.infra.web.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides utility methods related to reports
 */

public final class ReportUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportUtil.class);

    /**
     * Private constructor to silence PMD warning of "all static methods"
     */
    private ReportUtil() {
    }

    /**
     * Returns input stream for given file. First checks in the custom location
     * (/custom/[filePath]/). If not found, tries the given location [filePath]
     *
     * @param filePath
     *            Path of file to be loaded from classpath
     * @return Input stream for given file
     */
    private static InputStream getFileAsStream(final String filePath) {
        InputStream fileInputStream = null;
        String errMsg = null;
        // Try custom file first
        fileInputStream = ReportUtil.class.getResourceAsStream(ReportConstants.CUSTOM_DIR_NAME + filePath);
        if (fileInputStream == null)
            // Custom file not available. Try given path
            fileInputStream = ReportUtil.class.getResourceAsStream(filePath);
        if (fileInputStream == null) {
            // Still not found. Logger error and throw exception.
            errMsg = "File [" + filePath + "] could not be loaded from CLASSPATH!";
            LOGGER.error(errMsg);
            throw new ApplicationRuntimeException(errMsg);
        }
        return fileInputStream;
    }

    /**
     * Returns input stream for given image file. First checks in the custom
     * location (/custom/reports/images/). If not found, tries the product
     * location (/reports/images/)
     *
     * @param imageName
     *            Name of image to be read
     * @return Input stream for given image file
     */
    public static InputStream getImageAsStream(final String imageName) {
        return getFileAsStream(ReportConstants.IMAGES_BASE_PATH + imageName);
    }

    public static String getCityName() {
        return ApplicationThreadLocals.getMunicipalityName();
    }

    /**
     * Returns input stream for given report template. First checks in the
     * custom location (/custom/reports/templates/). If not found, tries the
     * product location (/reports/templates/)
     *
     * @param templateName
     *            Report template to be read
     * @return Input stream for given report template
     */
    public static InputStream getTemplateAsStream(final String templateName) {
        return getFileAsStream(ReportConstants.REPORTS_BASE_PATH + ReportConstants.TEMPLATE_DIR_NAME + templateName);
    }

    /**
     * Loads the report configuration file from classpath
     * (/config/reports.properties)
     *
     * @return the Properties object created from the configuration file
     */
    public static Properties loadReportConfig() {
        final Properties reportProps = new Properties();
        try {
            reportProps.load(getFileAsStream(ReportConstants.REPORT_CONFIG_FILE));
            return reportProps;
        } catch (final IOException e) {
            LOGGER.warn(
                    "Exception while loading report configuration file [" + ReportConstants.REPORT_CONFIG_FILE + "]",
                    e);
            return null;
        } catch (final ApplicationRuntimeException e) {
            LOGGER.warn(
                    "Exception while loading report configuration file [" + ReportConstants.REPORT_CONFIG_FILE + "]",
                    e);
            return null;
        }
    }

    /**
     * Executes given SQL query (which is expected to return a single object)
     * and returns the output. Returns null if query doesn't fetch any data.
     *
     * @param connection
     *            Connection to be used for executing the query. Can be passed
     *            as $P{REPORT_CONNECTION} in case of a jasper report
     * @param sqlQuery
     *            Query to be executed to get the data
     * @return Output of the query
     */
    public static Object fetchFromDBSql(final Connection connection, final String sqlQuery) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sqlQuery);
            resultSet = statement.executeQuery();
            if (resultSet != null && resultSet.next())
                return resultSet.getString(1);
            else
                return null;
        } catch (final SQLException e) {
            final String errMsg = "Exception while executing query [" + sqlQuery + "]";
            LOGGER.error(errMsg, e);
            throw new ApplicationRuntimeException(errMsg, e);
        } finally {
            if (statement != null)
                statement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }

    /**
     * @param year
     * @param month
     * @param date
     * @return date object representing given year, month and date
     */
    public static Date getDate(final int year, final int month, final int date) {
        return DateUtils.getDate(year, month, date);
    }

    /**
     * @return Date object representing today
     */
    public static Date today() {
        return DateUtils.today();
    }

    /**
     * @return Date object representing today
     */
    public static Date now() {
        return DateUtils.now();
    }

    /**
     * @return Date object representing tomorrow
     */
    public static Date tomorrow() {
        return DateUtils.tomorrow();
    }

    /**
     * Adds given number of days/months/years to given date and returns the
     * resulting date
     *
     * @param inputDate
     *            Input date
     * @param addType
     *            type to be added
     *            (Calendar.DAY_OF_MONTH/Calendar.MONTH/Calendar.YEAR)
     * @param addAmount
     *            Number of days/months/years to be added to the input date
     * @return Date after adding given number of days/months/years to the input
     *         date
     */
    public static Date add(final Date inputDate, final int addType, final int addAmount) {
        return DateUtils.add(inputDate, addType, addAmount);
    }

    /**
     * Converts given amount to words with default decimal precision of 2.
     *
     * @param amount
     *            Amount to be converted to words
     * @return The amount in words with default decimal precision of 2.
     */
    public static String amountInWords(final BigDecimal amount) {
        return NumberUtil.amountInWords(amount);
    }

    /**
     * Formats a given number with given number of fraction digits <br>
     * e.g. formatNumber(1000, 2, false) will return 1000.00 <br>
     * formatNumber(1000, 2, true) will return 1,000.00 <br>
     *
     * @param number
     *            The number to be formatted
     * @param fractionDigits
     *            Number of fraction digits to be used for formatting
     * @param useGrouping
     *            Flag indicating whether grouping is to be used while
     *            formatting the number
     * @return Formatted number with given number of fraction digits
     */
    public static String formatNumber(final BigDecimal number, final int fractionDigits, final boolean useGrouping) {
        return NumberUtil.formatNumber(number, fractionDigits, useGrouping);
    }

    /**
     * Gives the absolute path of the logo image
     *
     * @return absolute path of the logo image
     */
    public static String logoBasePath() {
        final HttpServletRequest request = ServletActionContext.getRequest();
        final String url = WebUtils.extractRequestDomainURL(request, false);
        final String imagePath = url.concat(ReportConstants.IMAGE_CONTEXT_PATH)
                .concat((String) request.getSession().getAttribute(CITY_LOGO_PATH_KEY));
        return imagePath;
    }

    /**
     * Gives the absolute path of the 'CANCELLED' Watermark image
     *
     * @return absolute path of the 'CANCELLED' Watermark image
     */
    public static String cancelledWatermarkAbsolutePath() {
        final HttpServletRequest request = ServletActionContext.getRequest();
        final String url = WebUtils.extractRequestDomainURL(request, false);
        final String imagePath = url.concat(ReportConstants.IMAGE_CONTEXT_PATH)
                .concat("/resources/global/images/cancelled_watermark.png");
        return imagePath;
    }

}