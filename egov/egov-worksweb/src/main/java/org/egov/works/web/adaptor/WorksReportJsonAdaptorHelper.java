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

package org.egov.works.web.adaptor;

import com.google.gson.JsonObject;
import org.egov.works.elasticsearch.model.WorksMilestoneIndexResponse;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class WorksReportJsonAdaptorHelper {
    private static final String JAN_01_TO_15_TARGET = "Jan 01 to 15 target";
    private static final String JAN_01_TO_15_ACTUAL = "Jan 01 to 15 actual";
    private static final String JAN_01_TO_15_VARIANCE = "Jan 01 to 15 variance";
    private static final String JAN_16_TO_31_TARGET = "Jan 16 to 31 target";
    private static final String JAN_16_TO_31_ACTUAL = "Jan 16 to 31 actual";
    private static final String JAN_16_TO_31_VARIANCE = "Jan 16 to 31 variance";
    private static final String FEB_01_TO_15_TARGET = "Feb 01 to 15 target";
    private static final String FEB_01_TO_15_ACTUAL = "Feb 01 to 15 actual";
    private static final String FEB_01_TO_15_VARIANCE = "Feb 01 to 15 variance";
    private static final String FEB_16_TO_28_OR_29_TARGET = "Feb 16 to 28 or 29 target";
    private static final String FEB_16_TO_28_OR_29_ACTUAL = "Feb 16 to 28 or 29 actual";
    private static final String FEB_16_TO_28_OR_29_VARIANCE = "Feb 16 to 28 or 29 variance";
    private static final String MAR_01_TO_15_TARGET = "Mar 01 to 15 target";
    private static final String MAR_01_TO_15_ACTUAL = "Mar 01 to 15 actual";
    private static final String MAR_01_TO_15_VARIANCE = "Mar 01 to 15 variance";
    private static final String MAR_16_TO_31_TARGET = "Mar 16 to 31 target";
    private static final String MAR_16_TO_31_ACTUAL = "Mar 16 to 31 actual";
    private static final String MAR_16_TO_31_VARIANCE = "Mar 16 to 31 variance";
    private static final String APR_01_TO_15_TARGET = "Apr 01 to 15 target";
    private static final String APR_01_TO_15_ACTUAL = "Apr 01 to 15 actual";
    private static final String APR_01_TO_15_VARIANCE = "Apr 01 to 15 variance";
    private static final String APR_16_TO_30_TARGET = "Apr 16 to 30 target";
    private static final String APR_16_TO_30_ACTUAL = "Apr 16 to 30 actual";
    private static final String APR_16_TO_30_VARIANCE = "Apr 16 to 30 variance";
    private static final String MAY_01_TO_15_TARGET = "May 01 to 15 target";
    private static final String MAY_01_TO_15_ACTUAL = "May 01 to 15 actual";
    private static final String MAY_01_TO_15_VARIANCE = "May 01 to 15 variance";
    private static final String MAY_16_TO_31_TARGET = "May 16 to 31 target";
    private static final String MAY_16_TO_31_ACTUAL = "May 16 to 31 actual";
    private static final String MAY_16_TO_31_VARIANCE = "May 16 to 31 variance";
    private static final String JUN_01_TO_15_TARGET = "Jun 01 to 15 target";
    private static final String JUN_01_TO_15_ACTUAL = "Jun 01 to 15 actual";
    private static final String JUN_01_TO_15_VARIANCE = "Jun 01 to 15 variance";
    private static final String JUN_16_TO_30_TARGET = "Jun 16 to 30 target";
    private static final String JUN_16_TO_30_ACTUAL = "Jun 16 to 30 actual";
    private static final String JUN_16_TO_30_VARIANCE = "Jun 16 to 30 variance";
    private static final String JUL_01_TO_15_TARGET = "Jul 01 to 15 target";
    private static final String JUL_01_TO_15_ACTUAL = "Jul 01 to 15 actual";
    private static final String JUL_01_TO_15_VARIANCE = "Jul 01 to 15 variance";
    private static final String JUL_16_TO_31_TARGET = "Jul 16 to 31 target";
    private static final String JUL_16_TO_31_ACTUAL = "Jul 16 to 31 actual";
    private static final String JUL_16_TO_31_VARIANCE = "Jul 16 to 31 variance";
    private static final String AUG_01_TO_15_TARGET = "Aug 01 to 15 target";
    private static final String AUG_01_TO_15_ACTUAL = "Aug 01 to 15 actual";
    private static final String AUG_01_TO_15_VARIANCE = "Aug 01 to 15 variance";
    private static final String AUG_16_TO_31_TARGET = "Aug 16 to 31 target";
    private static final String AUG_16_TO_31_ACTUAL = "Aug 16 to 31 actual";
    private static final String AUG_16_TO_31_VARIANCE = "Aug 16 to 31 variance";
    private static final String SEP_01_TO_15_TARGET = "Sep 01 to 15 target";
    private static final String SEP_01_TO_15_ACTUAL = "Sep 01 to 15 actual";
    private static final String SEP_01_TO_15_VARIANCE = "Sep 01 to 15 variance";
    private static final String SEP_16_TO_30_TARGET = "Sep 16 to 30 target";
    private static final String SEP_16_TO_30_ACTUAL = "Sep 16 to 30 actual";
    private static final String SEP_16_TO_30_VARIANCE = "Sep 16 to 30 variance";
    private static final String OCT_01_TO_15_TARGET = "Oct 01 to 15 target";
    private static final String OCT_01_TO_15_ACTUAL = "Oct 01 to 15 actual";
    private static final String OCT_01_TO_15_VARIANCE = "Oct 01 to 15 variance";
    private static final String OCT_16_TO_31_TARGET = "Oct 16 to 31 target";
    private static final String OCT_16_TO_31_ACTUAL = "Oct 16 to 31 actual";
    private static final String OCT_16_TO_31_VARIANCE = "Oct 16 to 31 variance";
    private static final String NOV_01_TO_15_TARGET = "Nov 01 to 15 target";
    private static final String NOV_01_TO_15_ACTUAL = "Nov 01 to 15 actual";
    private static final String NOV_01_TO_15_VARIANCE = "Nov 01 to 15 variance";
    private static final String NOV_16_TO_30_TARGET = "Nov 16 to 30 target";
    private static final String NOV_16_TO_30_ACTUAL = "Nov 16 to 30 actual";
    private static final String NOV_16_TO_30_VARIANCE = "Nov 16 to 30 variance";
    private static final String DEC_01_TO_15_TARGET = "Dec 01 to 15 target";
    private static final String DEC_01_TO_15_ACTUAL = "Dec 01 to 15 actual";
    private static final String DEC_01_TO_15_VARIANCE = "Dec 01 to 15 variance";
    private static final String DEC_16_TO_31_TARGET = "Dec 16 to 31 target";
    private static final String DEC_16_TO_31_ACTUAL = "Dec 16 to 31 actual";
    private static final String DEC_16_TO_31_VARIANCE = "Dec 16 to 31 variance";
    private static final String TOTAL_OF_BALANCE_WORK = "Total % of Balance work";
    private static final String PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE = "0.00";

    public void showDecemberData(final WorksMilestoneIndexResponse response, final JsonObject jsonObject,
            final DateTime currentDate) {
        prepareSeptemberData(jsonObject, response);
        prepareOctoberData(jsonObject, response);
        prepareNovemberData(jsonObject, response);

        if (response.getDec01to15target() != null && !response.getDec01to15target().isNaN())
            jsonObject.addProperty(DEC_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getDec01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(DEC_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getDec01to15actual() != null && !response.getDec01to15actual().isNaN())
            jsonObject.addProperty(DEC_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getDec01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(DEC_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getDec01to15variance() != null)
            jsonObject.addProperty(DEC_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getDec01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(DEC_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        if (currentDate.getDayOfMonth() > 15) {
            if (response.getDec16to31target() != null && !response.getDec16to31target().isNaN())
                jsonObject.addProperty(DEC_16_TO_31_TARGET,
                        BigDecimal.valueOf(response.getDec16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(DEC_16_TO_31_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getDec16to31actual() != null && !response.getDec16to31actual().isNaN())
                jsonObject.addProperty(DEC_16_TO_31_ACTUAL,
                        BigDecimal.valueOf(response.getDec16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(DEC_16_TO_31_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getDec16to31variance() != null)
                jsonObject.addProperty(DEC_16_TO_31_VARIANCE,
                        BigDecimal.valueOf(response.getDec16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(DEC_16_TO_31_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

            if (response.getDec16to31actual() != null && !response.getDec16to31actual().isNaN()) {
                if (response.getDec16to31target() != null && !response.getDec16to31target().isNaN()
                        && response.getDec16to31target().compareTo((double) 0) == 0)
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
                else
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                            BigDecimal.valueOf(100 - response.getDec16to31actual())
                                    .setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
            } else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        } else if (response.getDec01to15actual() != null && !response.getDec01to15actual().isNaN()) {
            if (response.getDec01to15target() != null && !response.getDec01to15target().isNaN()
                    && response.getDec01to15target().compareTo((double) 0) == 0)
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                        BigDecimal.valueOf(100 - response.getDec01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
        } else
            jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    public void showNovemberData(final WorksMilestoneIndexResponse response, final JsonObject jsonObject,
            final DateTime currentDate) {
        prepareAugustData(jsonObject, response);
        prepareSeptemberData(jsonObject, response);
        prepareOctoberData(jsonObject, response);

        if (response.getNov01to15target() != null && !response.getNov01to15target().isNaN())
            jsonObject.addProperty(NOV_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getNov01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(NOV_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getNov01to15actual() != null && !response.getNov01to15actual().isNaN())
            jsonObject.addProperty(NOV_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getNov01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(NOV_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getNov01to15variance() != null)
            jsonObject.addProperty(NOV_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getNov01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(NOV_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        if (currentDate.getDayOfMonth() > 15) {
            if (response.getNov16to30target() != null && !response.getNov16to30target().isNaN())
                jsonObject.addProperty(NOV_16_TO_30_TARGET,
                        BigDecimal.valueOf(response.getNov16to30target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(NOV_16_TO_30_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getNov16to30actual() != null && !response.getNov16to30actual().isNaN())
                jsonObject.addProperty(NOV_16_TO_30_ACTUAL,
                        BigDecimal.valueOf(response.getNov16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(NOV_16_TO_30_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getNov16to30variance() != null)
                jsonObject.addProperty(NOV_16_TO_30_VARIANCE,
                        BigDecimal.valueOf(response.getNov16to30variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(NOV_16_TO_30_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getNov16to30actual() != null && !response.getNov16to30actual().isNaN()) {
                if (response.getNov16to30target() != null && !response.getNov16to30target().isNaN()
                        && response.getNov16to30target().compareTo((double) 0) == 0)
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
                else
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                            BigDecimal.valueOf(100 - response.getNov16to30actual())
                                    .setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
            } else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        } else if (response.getNov01to15actual() != null && !response.getNov01to15actual().isNaN()) {
            if (response.getNov01to15target() != null && !response.getNov01to15target().isNaN()
                    && response.getNov01to15target().compareTo((double) 0) == 0)
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                        BigDecimal.valueOf(100 - response.getNov01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
        } else
            jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    public void showOctoberData(final WorksMilestoneIndexResponse response, final JsonObject jsonObject,
            final DateTime currentDate) {
        prepareJulyData(jsonObject, response);
        prepareAugustData(jsonObject, response);
        prepareSeptemberData(jsonObject, response);

        if (response.getOct01to15target() != null && !response.getOct01to15target().isNaN())
            jsonObject.addProperty(OCT_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getOct01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(OCT_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getOct01to15actual() != null && !response.getOct01to15actual().isNaN())
            jsonObject.addProperty(OCT_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getOct01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(OCT_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getOct01to15variance() != null)
            jsonObject.addProperty(OCT_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getOct01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(OCT_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        if (currentDate.getDayOfMonth() > 15) {
            if (response.getOct16to31target() != null && !response.getOct16to31target().isNaN())
                jsonObject.addProperty(OCT_16_TO_31_TARGET,
                        BigDecimal.valueOf(response.getOct16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(OCT_16_TO_31_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getOct16to31actual() != null && !response.getOct16to31actual().isNaN())
                jsonObject.addProperty(OCT_16_TO_31_ACTUAL,
                        BigDecimal.valueOf(response.getOct16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(OCT_16_TO_31_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getOct16to31variance() != null)
                jsonObject.addProperty(OCT_16_TO_31_VARIANCE,
                        BigDecimal.valueOf(response.getOct16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(OCT_16_TO_31_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getOct16to31actual() != null && !response.getOct16to31actual().isNaN()) {
                if (response.getOct16to31target() != null && !response.getOct16to31target().isNaN()
                        && response.getOct16to31target().compareTo((double) 0) == 0)
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
                else
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                            BigDecimal.valueOf(100 - response.getOct16to31actual())
                                    .setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
            } else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        } else if (response.getOct01to15actual() != null && !response.getOct01to15actual().isNaN()) {
            if (response.getOct01to15target() != null && !response.getOct01to15target().isNaN()
                    && response.getOct01to15target().compareTo((double) 0) == 0)
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                        BigDecimal.valueOf(100 - response.getOct01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
        } else
            jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    public void showSeptemberData(final WorksMilestoneIndexResponse response, final JsonObject jsonObject,
            final DateTime currentDate) {
        prepareJuneData(jsonObject, response);
        prepareJulyData(jsonObject, response);
        prepareAugustData(jsonObject, response);

        if (response.getSep01to15target() != null && !response.getSep01to15target().isNaN())
            jsonObject.addProperty(SEP_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getSep01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(SEP_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getSep01to15actual() != null && !response.getSep01to15actual().isNaN())
            jsonObject.addProperty(SEP_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getSep01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(SEP_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getSep01to15variance() != null)
            jsonObject.addProperty(SEP_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getSep01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(SEP_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        if (currentDate.getDayOfMonth() > 15) {
            if (response.getSep16to30target() != null && !response.getSep16to30target().isNaN())
                jsonObject.addProperty(SEP_16_TO_30_TARGET,
                        BigDecimal.valueOf(response.getSep16to30target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(SEP_16_TO_30_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getSep16to30actual() != null && !response.getSep16to30actual().isNaN())
                jsonObject.addProperty(SEP_16_TO_30_ACTUAL,
                        BigDecimal.valueOf(response.getSep16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(SEP_16_TO_30_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getSep16to30variance() != null)
                jsonObject.addProperty(SEP_16_TO_30_VARIANCE,
                        BigDecimal.valueOf(response.getSep16to30variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(SEP_16_TO_30_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getSep16to30actual() != null && !response.getSep16to30actual().isNaN()) {
                if (response.getSep16to30target() != null && !response.getSep16to30target().isNaN()
                        && response.getSep16to30target().compareTo((double) 0) == 0)
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
                else
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                            BigDecimal.valueOf(100 - response.getSep16to30actual())
                                    .setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
            } else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        } else if (response.getSep01to15actual() != null && !response.getSep01to15actual().isNaN()) {
            if (response.getSep01to15target() != null && !response.getSep01to15target().isNaN()
                    && response.getSep01to15target().compareTo((double) 0) == 0)
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                        BigDecimal.valueOf(100 - response.getSep01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
        } else
            jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    public void showAugustData(final WorksMilestoneIndexResponse response, final JsonObject jsonObject,
            final DateTime currentDate) {
        prepareMayData(jsonObject, response);
        prepareJuneData(jsonObject, response);
        prepareJulyData(jsonObject, response);

        if (response.getAug01to15target() != null && !response.getAug01to15target().isNaN())
            jsonObject.addProperty(AUG_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getAug01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(AUG_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getAug01to15actual() != null && !response.getAug01to15actual().isNaN())
            jsonObject.addProperty(AUG_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getAug01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(AUG_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getAug01to15variance() != null)
            jsonObject.addProperty(AUG_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getAug01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(AUG_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        if (currentDate.getDayOfMonth() > 15) {
            if (response.getAug16to31target() != null && !response.getAug16to31target().isNaN())
                jsonObject.addProperty(AUG_16_TO_31_TARGET,
                        BigDecimal.valueOf(response.getAug16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(AUG_16_TO_31_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getAug16to31actual() != null && !response.getAug16to31actual().isNaN())
                jsonObject.addProperty(AUG_16_TO_31_ACTUAL,
                        BigDecimal.valueOf(response.getAug16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(AUG_16_TO_31_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getAug16to31variance() != null)
                jsonObject.addProperty(AUG_16_TO_31_VARIANCE,
                        BigDecimal.valueOf(response.getAug16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(AUG_16_TO_31_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getAug16to31actual() != null && !response.getAug16to31actual().isNaN()) {
                if (response.getAug16to31target() != null && !response.getAug16to31target().isNaN()
                        && response.getAug16to31target().compareTo((double) 0) == 0)
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
                else
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                            BigDecimal.valueOf(100 - response.getAug16to31actual())
                                    .setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
            } else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        } else if (response.getAug01to15actual() != null && !response.getAug01to15actual().isNaN()) {
            if (response.getAug01to15target() != null && !response.getAug01to15target().isNaN()
                    && response.getAug01to15target().compareTo((double) 0) == 0)
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                        BigDecimal.valueOf(100 - response.getAug01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
        } else
            jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    public void showJulyData(final WorksMilestoneIndexResponse response, final JsonObject jsonObject,
            final DateTime currentDate) {
        prepareAprilData(jsonObject, response);
        prepareMayData(jsonObject, response);
        prepareJuneData(jsonObject, response);

        if (response.getJul01to15target() != null && !response.getJul01to15target().isNaN())
            jsonObject.addProperty(JUL_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getJul01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(JUL_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJul01to15actual() != null && !response.getJul01to15actual().isNaN())
            jsonObject.addProperty(JUL_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getJul01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(JUL_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJul01to15variance() != null)
            jsonObject.addProperty(JUL_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getJul01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(JUL_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        if (currentDate.getDayOfMonth() > 15) {
            if (response.getJul16to31target() != null && !response.getJul16to31target().isNaN())
                jsonObject.addProperty(JUL_16_TO_31_TARGET,
                        BigDecimal.valueOf(response.getJul16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(JUL_16_TO_31_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getJul16to31actual() != null && !response.getJul16to31actual().isNaN())
                jsonObject.addProperty(JUL_16_TO_31_ACTUAL,
                        BigDecimal.valueOf(response.getJul16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(JUL_16_TO_31_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getJul16to31variance() != null)
                jsonObject.addProperty(JUL_16_TO_31_VARIANCE,
                        BigDecimal.valueOf(response.getJul16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(JUL_16_TO_31_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getJul16to31actual() != null && !response.getJul16to31actual().isNaN()) {
                if (response.getJul16to31target() != null && !response.getJul16to31target().isNaN()
                        && response.getJul16to31target().compareTo((double) 0) == 0)
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
                else
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                            BigDecimal.valueOf(100 - response.getJul16to31actual())
                                    .setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
            } else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        } else if (response.getJun01to15actual() != null && !response.getJul01to15actual().isNaN()) {
            if (response.getJul01to15target() != null && !response.getJul01to15target().isNaN()
                    && response.getJul01to15target().compareTo((double) 0) == 0)
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                        BigDecimal.valueOf(100 - response.getJul01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
        } else
            jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    public void showJuneData(final WorksMilestoneIndexResponse response, final JsonObject jsonObject,
            final DateTime currentDate) {
        prepareMarchData(jsonObject, response);
        prepareAprilData(jsonObject, response);
        prepareMayData(jsonObject, response);

        if (response.getJun01to15target() != null && !response.getJun01to15target().isNaN())
            jsonObject.addProperty(JUN_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getJun01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(JUN_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJun01to15actual() != null && !response.getJun01to15actual().isNaN())
            jsonObject.addProperty(JUN_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getJun01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(JUN_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJun01to15variance() != null)
            jsonObject.addProperty(JUN_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getJun01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(JUN_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        if (currentDate.getDayOfMonth() > 15) {
            if (response.getJun16to30target() != null && !response.getJun16to30target().isNaN())
                jsonObject.addProperty(JUN_16_TO_30_TARGET,
                        BigDecimal.valueOf(response.getJun16to30target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(JUN_16_TO_30_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getJun16to30actual() != null && !response.getJun16to30actual().isNaN())
                jsonObject.addProperty(JUN_16_TO_30_ACTUAL,
                        BigDecimal.valueOf(response.getJun16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(JUN_16_TO_30_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getJun16to30variance() != null)
                jsonObject.addProperty(JUN_16_TO_30_VARIANCE,
                        BigDecimal.valueOf(response.getJun16to30variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(JUN_16_TO_30_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getJun16to30actual() != null && !response.getJun16to30actual().isNaN()) {
                if (response.getJun16to30target() != null && !response.getJun16to30target().isNaN()
                        && response.getJun16to30target().compareTo((double) 0) == 0)
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
                else
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                            BigDecimal.valueOf(100 - response.getJun16to30actual())
                                    .setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
            } else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        } else if (response.getJun01to15actual() != null && !response.getJun01to15actual().isNaN()) {
            if (response.getJun01to15target() != null && !response.getJun01to15target().isNaN()
                    && response.getJun01to15target().compareTo((double) 0) == 0)
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                        BigDecimal.valueOf(100 - response.getJun01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
        } else
            jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    public void showMayData(final WorksMilestoneIndexResponse response, final JsonObject jsonObject,
            final DateTime currentDate) {
        prepareFebruaryData(jsonObject, response);
        prepareMarchData(jsonObject, response);
        prepareAprilData(jsonObject, response);

        if (response.getMay01to15target() != null && !response.getMay01to15target().isNaN())
            jsonObject.addProperty(MAY_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getMay01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(MAY_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getMay01to15actual() != null && !response.getMay01to15actual().isNaN())
            jsonObject.addProperty(MAY_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getMay01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(MAY_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getMay01to15variance() != null)
            jsonObject.addProperty(MAY_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getMay01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(MAY_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        if (currentDate.getDayOfMonth() > 15) {
            if (response.getMay16to31target() != null && !response.getMay16to31target().isNaN())
                jsonObject.addProperty(MAY_16_TO_31_TARGET,
                        BigDecimal.valueOf(response.getMay16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(MAY_16_TO_31_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getMay16to31actual() != null && !response.getMay16to31actual().isNaN())
                jsonObject.addProperty(MAY_16_TO_31_ACTUAL,
                        BigDecimal.valueOf(response.getMay16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(MAY_16_TO_31_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getMay16to31variance() != null)
                jsonObject.addProperty(MAY_16_TO_31_VARIANCE,
                        BigDecimal.valueOf(response.getMay16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(MAY_16_TO_31_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getMay16to31actual() != null && !response.getMay16to31actual().isNaN()) {
                if (response.getMay16to31target() != null && !response.getMay16to31target().isNaN()
                        && response.getMay16to31target().compareTo((double) 0) == 0)
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
                else
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                            BigDecimal.valueOf(100 - response.getMay16to31actual())
                                    .setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
            } else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        } else if (response.getMay01to15actual() != null && !response.getMay01to15actual().isNaN()) {
            if (response.getMay01to15target() != null && !response.getMay01to15target().isNaN()
                    && response.getMay01to15target().compareTo((double) 0) == 0)
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                        BigDecimal.valueOf(100 - response.getMay01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
        } else
            jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    public void showAprilData(final WorksMilestoneIndexResponse response, final JsonObject jsonObject,
            final DateTime currentDate) {
        prepareJanuaryData(jsonObject, response);
        prepareFebruaryData(jsonObject, response);
        prepareMarchData(jsonObject, response);

        if (response.getApr01to15target() != null && !response.getApr01to15target().isNaN())
            jsonObject.addProperty(APR_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getApr01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(APR_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getApr01to15actual() != null && !response.getApr01to15actual().isNaN())
            jsonObject.addProperty(APR_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getApr01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(APR_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getApr01to15variance() != null)
            jsonObject.addProperty(APR_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getApr01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(APR_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        if (currentDate.getDayOfMonth() > 15) {
            if (response.getApr16to30target() != null && !response.getApr16to30target().isNaN())
                jsonObject.addProperty(APR_16_TO_30_TARGET,
                        BigDecimal.valueOf(response.getApr16to30target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(APR_16_TO_30_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getApr16to30actual() != null && !response.getApr16to30actual().isNaN())
                jsonObject.addProperty(APR_16_TO_30_ACTUAL,
                        BigDecimal.valueOf(response.getApr16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(APR_16_TO_30_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getApr16to30variance() != null)
                jsonObject.addProperty(APR_16_TO_30_VARIANCE,
                        BigDecimal.valueOf(response.getApr16to30variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(APR_16_TO_30_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getApr16to30actual() != null && !response.getApr16to30actual().isNaN()) {
                if (response.getApr16to30target() != null && !response.getApr16to30target().isNaN()
                        && response.getApr16to30target().compareTo((double) 0) == 0)
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
                else
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                            BigDecimal.valueOf(100 - response.getApr16to30actual())
                                    .setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
            } else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        } else if (response.getApr01to15actual() != null && !response.getApr01to15actual().isNaN()) {
            if (response.getApr01to15target() != null && !response.getApr01to15target().isNaN()
                    && response.getApr01to15target().compareTo((double) 0) == 0)
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                        BigDecimal.valueOf(100 - response.getApr01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
        } else
            jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    public void showMarchData(final WorksMilestoneIndexResponse response, final JsonObject jsonObject,
            final DateTime currentDate) {
        prepareDecemberData(jsonObject, response);
        prepareJanuaryData(jsonObject, response);
        prepareFebruaryData(jsonObject, response);

        if (response.getMar01to15target() != null && !response.getMar01to15target().isNaN())
            jsonObject.addProperty(MAR_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getMar01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(MAR_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getMar01to15actual() != null && !response.getMar01to15actual().isNaN())
            jsonObject.addProperty(MAR_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getMar01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(MAR_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getMar01to15variance() != null)
            jsonObject.addProperty(MAR_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getMar01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(MAR_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        if (currentDate.getDayOfMonth() > 15) {
            if (response.getMar16to31target() != null && !response.getMar16to31target().isNaN())
                jsonObject.addProperty(MAR_16_TO_31_TARGET,
                        BigDecimal.valueOf(response.getMar16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(MAR_16_TO_31_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getMar16to31actual() != null && !response.getMar16to31actual().isNaN())
                jsonObject.addProperty(MAR_16_TO_31_ACTUAL,
                        BigDecimal.valueOf(response.getMar16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(MAR_16_TO_31_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getMar16to31variance() != null)
                jsonObject.addProperty(MAR_16_TO_31_VARIANCE,
                        BigDecimal.valueOf(response.getMar16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(MAR_16_TO_31_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

            if (response.getMar16to31actual() != null && !response.getMar16to31actual().isNaN()) {
                if (response.getMar16to31target() != null && !response.getMar16to31target().isNaN()
                        && response.getMar16to31target().compareTo((double) 0) == 0)
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
                else
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                            BigDecimal.valueOf(100 - response.getMar16to31actual())
                                    .setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
            } else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        } else if (response.getMar01to15actual() != null && !response.getMar01to15actual().isNaN()) {
            if (response.getMar01to15target() != null && !response.getMar01to15target().isNaN()
                    && response.getMar01to15target().compareTo((double) 0) == 0)
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                        BigDecimal.valueOf(100 - response.getMar01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
        } else
            jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    public void showFebruaryData(final WorksMilestoneIndexResponse response, final JsonObject jsonObject,
            final DateTime currentDate) {
        prepareNovemberData(jsonObject, response);
        prepareDecemberData(jsonObject, response);
        prepareJanuaryData(jsonObject, response);

        if (response.getFeb01to15target() != null && !response.getFeb01to15target().isNaN())
            jsonObject.addProperty(FEB_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getFeb01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(FEB_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getFeb01to15actual() != null && !response.getFeb01to15actual().isNaN())
            jsonObject.addProperty(FEB_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getFeb01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(FEB_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getFeb01to15variance() != null)
            jsonObject.addProperty(FEB_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getFeb01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(FEB_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        if (currentDate.getDayOfMonth() > 15) {
            if (response.getFeb16to28or29target() != null && !response.getFeb16to28or29target().isNaN())
                jsonObject.addProperty(FEB_16_TO_28_OR_29_TARGET,
                        BigDecimal.valueOf(response.getFeb16to28or29target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(FEB_16_TO_28_OR_29_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getFeb16to28or29actual() != null && !response.getFeb16to28or29actual().isNaN())
                jsonObject.addProperty(FEB_16_TO_28_OR_29_ACTUAL,
                        BigDecimal.valueOf(response.getFeb16to28or29actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(FEB_16_TO_28_OR_29_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getFeb16to28or29variance() != null)
                jsonObject.addProperty(FEB_16_TO_28_OR_29_VARIANCE,
                        BigDecimal.valueOf(response.getFeb16to28or29variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(FEB_16_TO_28_OR_29_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getFeb16to28or29actual() != null && !response.getFeb16to28or29actual().isNaN()) {
                if (response.getFeb16to28or29target() != null && !response.getFeb16to28or29target().isNaN()
                        && response.getFeb16to28or29target().compareTo((double) 0) == 0)
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
                else
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                            BigDecimal.valueOf(100 - response.getFeb16to28or29actual())
                                    .setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
            } else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        } else if (response.getFeb01to15actual() != null && !response.getFeb01to15actual().isNaN()) {
            if (response.getFeb01to15target() != null && !response.getFeb01to15target().isNaN()
                    && response.getFeb01to15target().compareTo((double) 0) == 0)
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                        BigDecimal.valueOf(100 - response.getFeb01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
        } else
            jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    public void showJanuaryData(final WorksMilestoneIndexResponse response, final JsonObject jsonObject,
            final DateTime currentDate) {
        prepareOctoberData(jsonObject, response);
        prepareNovemberData(jsonObject, response);
        prepareDecemberData(jsonObject, response);

        if (response.getJan01to15target() != null && !response.getJan01to15target().isNaN())
            jsonObject.addProperty(JAN_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getJan01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(JAN_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJan01to15actual() != null && !response.getJan01to15actual().isNaN())
            jsonObject.addProperty(JAN_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getJan01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(JAN_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJan01to15variance() != null)
            jsonObject.addProperty(JAN_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getJan01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(JAN_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        if (currentDate.getDayOfMonth() > 15) {
            if (response.getJan16to31target() != null && !response.getJan16to31target().isNaN())
                jsonObject.addProperty(JAN_16_TO_31_TARGET,
                        BigDecimal.valueOf(response.getJan16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(JAN_16_TO_31_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getJan16to31actual() != null && !response.getJan16to31actual().isNaN())
                jsonObject.addProperty(JAN_16_TO_31_ACTUAL,
                        BigDecimal.valueOf(response.getJan16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(JAN_16_TO_31_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            if (response.getJan16to31variance() != null)
                jsonObject.addProperty(JAN_16_TO_31_VARIANCE,
                        BigDecimal.valueOf(response.getJan16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
            else
                jsonObject.addProperty(JAN_16_TO_31_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

            if (response.getJan16to31actual() != null && !response.getJan16to31actual().isNaN())
                if (response.getJan16to31target() != null && !response.getJan16to31target().isNaN()
                        && response.getJan16to31target().compareTo((double) 0) == 0)
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
                else
                    jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                            BigDecimal.valueOf(100 - response.getJan16to31actual())
                                    .setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
            else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);

        } else if (response.getJan01to15actual() != null && !response.getJan01to15actual().isNaN()) {
            if (response.getJan01to15target() != null && !response.getJan01to15target().isNaN()
                    && response.getJan01to15target().compareTo((double) 0) == 0)
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
            else
                jsonObject.addProperty(TOTAL_OF_BALANCE_WORK,
                        BigDecimal.valueOf(100 - response.getJan01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                .toString());
        } else
            jsonObject.addProperty(TOTAL_OF_BALANCE_WORK, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    private void prepareJanuaryData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getJan01to15target() != null && !response.getJan01to15target().isNaN())
            jsonObject.addProperty(JAN_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getJan01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(JAN_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJan01to15actual() != null && !response.getJan01to15actual().isNaN())
            jsonObject.addProperty(JAN_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getJan01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(JAN_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJan01to15variance() != null)
            jsonObject.addProperty(JAN_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getJan01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(JAN_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJan16to31target() != null && !response.getJan16to31target().isNaN())
            jsonObject.addProperty(JAN_16_TO_31_TARGET,
                    BigDecimal.valueOf(response.getJan16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(JAN_16_TO_31_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJan16to31actual() != null && !response.getJan16to31actual().isNaN())
            jsonObject.addProperty(JAN_16_TO_31_ACTUAL,
                    BigDecimal.valueOf(response.getJan16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(JAN_16_TO_31_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJan16to31variance() != null)
            jsonObject.addProperty(JAN_16_TO_31_VARIANCE,
                    BigDecimal.valueOf(response.getJan16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(JAN_16_TO_31_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    private void prepareFebruaryData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getFeb01to15target() != null && !response.getFeb01to15target().isNaN())
            jsonObject.addProperty(FEB_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getFeb01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(FEB_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getFeb01to15actual() != null && !response.getFeb01to15actual().isNaN())
            jsonObject.addProperty(FEB_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getFeb01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(FEB_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getFeb01to15variance() != null)
            jsonObject.addProperty(FEB_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getFeb01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(FEB_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getFeb16to28or29target() != null && !response.getFeb16to28or29target().isNaN())
            jsonObject.addProperty(FEB_16_TO_28_OR_29_TARGET,
                    BigDecimal.valueOf(response.getFeb16to28or29target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(FEB_16_TO_28_OR_29_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getFeb16to28or29actual() != null && !response.getFeb16to28or29actual().isNaN())
            jsonObject.addProperty(FEB_16_TO_28_OR_29_ACTUAL,
                    BigDecimal.valueOf(response.getFeb16to28or29actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(FEB_16_TO_28_OR_29_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getFeb16to28or29variance() != null)
            jsonObject.addProperty(FEB_16_TO_28_OR_29_VARIANCE,
                    BigDecimal.valueOf(response.getFeb16to28or29variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(FEB_16_TO_28_OR_29_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    private void prepareMarchData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getMar01to15target() != null && !response.getMar01to15target().isNaN())
            jsonObject.addProperty(MAR_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getMar01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(MAR_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getMar01to15actual() != null && !response.getMar01to15actual().isNaN())
            jsonObject.addProperty(MAR_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getMar01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(MAR_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getMar01to15variance() != null)
            jsonObject.addProperty(MAR_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getMar01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(MAR_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getMar16to31target() != null && !response.getMar16to31target().isNaN())
            jsonObject.addProperty(MAR_16_TO_31_TARGET,
                    BigDecimal.valueOf(response.getMar16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(MAR_16_TO_31_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getMar16to31actual() != null && !response.getMar16to31actual().isNaN())
            jsonObject.addProperty(MAR_16_TO_31_ACTUAL,
                    BigDecimal.valueOf(response.getMar16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(MAR_16_TO_31_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getMar16to31variance() != null)
            jsonObject.addProperty(MAR_16_TO_31_VARIANCE,
                    BigDecimal.valueOf(response.getMar16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(MAR_16_TO_31_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    private void prepareAprilData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getApr01to15target() != null && !response.getApr01to15target().isNaN())
            jsonObject.addProperty(APR_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getApr01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(APR_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getApr01to15actual() != null && !response.getApr01to15actual().isNaN())
            jsonObject.addProperty(APR_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getApr01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(APR_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getApr01to15variance() != null)
            jsonObject.addProperty(APR_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getApr01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(APR_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getApr16to30target() != null && !response.getApr16to30target().isNaN())
            jsonObject.addProperty(APR_16_TO_30_TARGET,
                    BigDecimal.valueOf(response.getApr16to30target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(APR_16_TO_30_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getApr16to30actual() != null && !response.getApr16to30actual().isNaN())
            jsonObject.addProperty(APR_16_TO_30_ACTUAL,
                    BigDecimal.valueOf(response.getApr16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(APR_16_TO_30_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getApr16to30variance() != null)
            jsonObject.addProperty(APR_16_TO_30_VARIANCE,
                    BigDecimal.valueOf(response.getApr16to30variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(APR_16_TO_30_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    private void prepareMayData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getMay01to15target() != null && !response.getMay01to15target().isNaN())
            jsonObject.addProperty(MAY_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getMay01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(MAY_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getMay01to15actual() != null && !response.getMay01to15actual().isNaN())
            jsonObject.addProperty(MAY_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getMay01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(MAY_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getMay01to15variance() != null)
            jsonObject.addProperty(MAY_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getMay01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(MAY_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getMay16to31target() != null && !response.getMay16to31target().isNaN())
            jsonObject.addProperty(MAY_16_TO_31_TARGET,
                    BigDecimal.valueOf(response.getMay16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(MAY_16_TO_31_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getMay16to31actual() != null && !response.getMay16to31actual().isNaN())
            jsonObject.addProperty(MAY_16_TO_31_ACTUAL,
                    BigDecimal.valueOf(response.getMay16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(MAY_16_TO_31_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getMay16to31variance() != null)
            jsonObject.addProperty(MAY_16_TO_31_VARIANCE,
                    BigDecimal.valueOf(response.getMay16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(MAY_16_TO_31_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    private void prepareJuneData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {

        if (response.getJun01to15target() != null && !response.getJun01to15target().isNaN())
            jsonObject.addProperty(JUN_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getJun01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(JUN_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJun01to15actual() != null && !response.getJun01to15actual().isNaN())
            jsonObject.addProperty(JUN_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getJun01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(JUN_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJun01to15variance() != null)
            jsonObject.addProperty(JUN_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getJun01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(JUN_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJun16to30target() != null && !response.getJun16to30target().isNaN())
            jsonObject.addProperty(JUN_16_TO_30_TARGET,
                    BigDecimal.valueOf(response.getJun16to30target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(JUN_16_TO_30_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJun16to30actual() != null && !response.getJun16to30actual().isNaN())
            jsonObject.addProperty(JUN_16_TO_30_ACTUAL,
                    BigDecimal.valueOf(response.getJun16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(JUN_16_TO_30_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJun16to30variance() != null)
            jsonObject.addProperty(JUN_16_TO_30_VARIANCE,
                    BigDecimal.valueOf(response.getJun16to30variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(JUN_16_TO_30_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    private void prepareJulyData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getJul01to15target() != null && !response.getJul01to15target().isNaN())
            jsonObject.addProperty(JUL_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getJul01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(JUL_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJul01to15actual() != null && !response.getJul01to15actual().isNaN())
            jsonObject.addProperty(JUL_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getJul01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(JUL_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJul01to15variance() != null)
            jsonObject.addProperty(JUL_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getJul01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(JUL_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJul16to31target() != null && !response.getJul16to31target().isNaN())
            jsonObject.addProperty(JUL_16_TO_31_TARGET,
                    BigDecimal.valueOf(response.getJul16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(JUL_16_TO_31_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJul16to31actual() != null && !response.getJul16to31actual().isNaN())
            jsonObject.addProperty(JUL_16_TO_31_ACTUAL,
                    BigDecimal.valueOf(response.getJul16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(JUL_16_TO_31_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getJul16to31variance() != null)
            jsonObject.addProperty(JUL_16_TO_31_VARIANCE,
                    BigDecimal.valueOf(response.getJul16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(JUL_16_TO_31_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    private void prepareAugustData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getAug01to15target() != null && !response.getAug01to15target().isNaN())
            jsonObject.addProperty(AUG_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getAug01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(AUG_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getAug01to15actual() != null && !response.getAug01to15actual().isNaN())
            jsonObject.addProperty(AUG_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getAug01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(AUG_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getAug01to15variance() != null)
            jsonObject.addProperty(AUG_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getAug01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(AUG_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getAug16to31target() != null && !response.getAug16to31target().isNaN())
            jsonObject.addProperty(AUG_16_TO_31_TARGET,
                    BigDecimal.valueOf(response.getAug16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(AUG_16_TO_31_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getAug16to31actual() != null && !response.getAug16to31actual().isNaN())
            jsonObject.addProperty(AUG_16_TO_31_ACTUAL,
                    BigDecimal.valueOf(response.getAug16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(AUG_16_TO_31_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getAug16to31variance() != null)
            jsonObject.addProperty(AUG_16_TO_31_VARIANCE,
                    BigDecimal.valueOf(response.getAug16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(AUG_16_TO_31_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    private void prepareSeptemberData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getSep01to15target() != null && !response.getSep01to15target().isNaN())
            jsonObject.addProperty(SEP_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getSep01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(SEP_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getSep01to15actual() != null && !response.getSep01to15actual().isNaN())
            jsonObject.addProperty(SEP_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getSep01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(SEP_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getSep01to15variance() != null)
            jsonObject.addProperty(SEP_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getSep01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(SEP_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getSep16to30target() != null && !response.getSep16to30target().isNaN())
            jsonObject.addProperty(SEP_16_TO_30_TARGET,
                    BigDecimal.valueOf(response.getSep16to30target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(SEP_16_TO_30_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getSep16to30actual() != null && !response.getSep16to30actual().isNaN())
            jsonObject.addProperty(SEP_16_TO_30_ACTUAL,
                    BigDecimal.valueOf(response.getSep16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(SEP_16_TO_30_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getSep16to30variance() != null)
            jsonObject.addProperty(SEP_16_TO_30_VARIANCE,
                    BigDecimal.valueOf(response.getSep16to30variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(SEP_16_TO_30_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    private void prepareOctoberData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getOct01to15target() != null && !response.getOct01to15target().isNaN())
            jsonObject.addProperty(OCT_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getOct01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getOct01to15actual() != null && !response.getOct01to15actual().isNaN())
            jsonObject.addProperty(OCT_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getOct01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getOct01to15variance() != null)
            jsonObject.addProperty(OCT_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getOct01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getOct16to31target() != null && !response.getOct16to31target().isNaN())
            jsonObject.addProperty(OCT_16_TO_31_TARGET,
                    BigDecimal.valueOf(response.getOct16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(OCT_16_TO_31_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getOct16to31actual() != null && !response.getOct16to31actual().isNaN())
            jsonObject.addProperty(OCT_16_TO_31_ACTUAL,
                    BigDecimal.valueOf(response.getOct16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(OCT_16_TO_31_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getOct16to31variance() != null)
            jsonObject.addProperty(OCT_16_TO_31_VARIANCE,
                    BigDecimal.valueOf(response.getOct16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(OCT_16_TO_31_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    private void prepareNovemberData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getNov01to15target() != null && !response.getNov01to15target().isNaN())
            jsonObject.addProperty(NOV_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getNov01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(NOV_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getNov01to15actual() != null && !response.getNov01to15actual().isNaN())
            jsonObject.addProperty(NOV_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getNov01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(NOV_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getNov01to15variance() != null)
            jsonObject.addProperty(NOV_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getNov01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(NOV_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getNov16to30target() != null && !response.getNov16to30target().isNaN())
            jsonObject.addProperty(NOV_16_TO_30_TARGET,
                    BigDecimal.valueOf(response.getNov16to30target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(NOV_16_TO_30_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getNov16to30actual() != null && !response.getNov16to30actual().isNaN())
            jsonObject.addProperty(NOV_16_TO_30_ACTUAL,
                    BigDecimal.valueOf(response.getNov16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(NOV_16_TO_30_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getNov16to30variance() != null)
            jsonObject.addProperty(NOV_16_TO_30_VARIANCE,
                    BigDecimal.valueOf(response.getNov16to30variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(NOV_16_TO_30_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }

    private void prepareDecemberData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getDec01to15target() != null && !response.getDec01to15target().isNaN())
            jsonObject.addProperty(DEC_01_TO_15_TARGET,
                    BigDecimal.valueOf(response.getDec01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(DEC_01_TO_15_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getDec01to15actual() != null && !response.getDec01to15actual().isNaN())
            jsonObject.addProperty(DEC_01_TO_15_ACTUAL,
                    BigDecimal.valueOf(response.getDec01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        else
            jsonObject.addProperty(DEC_01_TO_15_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getDec01to15variance() != null)
            jsonObject.addProperty(DEC_01_TO_15_VARIANCE,
                    BigDecimal.valueOf(response.getDec01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(DEC_01_TO_15_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getDec16to31target() != null && !response.getDec16to31target().isNaN())
            jsonObject.addProperty(DEC_16_TO_31_TARGET,
                    BigDecimal.valueOf(response.getDec16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(DEC_16_TO_31_TARGET, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getDec16to31actual() != null && !response.getDec16to31actual().isNaN())
            jsonObject.addProperty(DEC_16_TO_31_ACTUAL,
                    BigDecimal.valueOf(response.getDec16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(DEC_16_TO_31_ACTUAL, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
        if (response.getDec16to31variance() != null)
            jsonObject.addProperty(DEC_16_TO_31_VARIANCE,
                    BigDecimal.valueOf(response.getDec16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        else
            jsonObject.addProperty(DEC_16_TO_31_VARIANCE, PHYSICAL_PROGRESS_DEFAULT_EMPTY_VALUE);
    }
}