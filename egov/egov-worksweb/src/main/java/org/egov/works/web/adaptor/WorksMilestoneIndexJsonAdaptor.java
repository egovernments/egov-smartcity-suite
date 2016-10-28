/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.works.web.adaptor;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import org.egov.works.elasticsearch.model.WorksMilestoneIndexResponse;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class WorksMilestoneIndexJsonAdaptor implements JsonSerializer<WorksMilestoneIndexResponse> {
    @Override
    public JsonElement serialize(final WorksMilestoneIndexResponse response, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (response != null) {
            jsonObject.addProperty(response.getReporttype(), response.getName());
            jsonObject.addProperty("Total no of works", response.getTotalnoofworks());
            jsonObject.addProperty("Total estimated cost in lakhs",
                    BigDecimal.valueOf(response.getTotalestimatedcostinlakhs() / 100000).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
            jsonObject.addProperty("Total work order value in lakhs",
                    BigDecimal.valueOf(response.getTotalworkordervalueinlakhs() / 100000).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
            jsonObject.addProperty("Total bill amount in lakhs",
                    BigDecimal.valueOf(response.getTotalbillamountinlakhs() / 100000).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
            jsonObject.addProperty("Total paid amount in lakhs",
                    BigDecimal.valueOf(response.getTotalpaidamountinlakhs() / 100000).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
            jsonObject.addProperty("Milestone not created", response.getMilestonenotcreatedcount());
            final DateTime currentDate = new DateTime();
            switch (currentDate.getMonthOfYear()) {
            case 1:
                prepareOctoberData(jsonObject, response);
                prepareNovemberData(jsonObject, response);
                prepareDecemberData(jsonObject, response);

                if (response.getJan01to15target() != null && !response.getJan01to15target().isNaN())
                    jsonObject.addProperty("Jan 01 to 15 target",
                            BigDecimal.valueOf(response.getJan01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getJan01to15actual() != null && !response.getJan01to15actual().isNaN())
                    jsonObject.addProperty("Jan 01 to 15 actual",
                            BigDecimal.valueOf(response.getJan01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getJan01to15variance() != null)
                    jsonObject.addProperty("Jan 01 to 15 variance",
                            BigDecimal.valueOf(response.getJan01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());

                if (currentDate.getDayOfMonth() > 15) {

                    if (response.getJan16to31target() != null && !response.getJan16to31target().isNaN())
                        jsonObject.addProperty("Jan 16 to 31 target",
                                BigDecimal.valueOf(response.getJan16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getJan16to31actual() != null && !response.getJan16to31actual().isNaN())
                        jsonObject.addProperty("Jan 16 to 31 actual",
                                BigDecimal.valueOf(response.getJan16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getJan16to31variance() != null)
                        jsonObject.addProperty("Jan 16 to 31 variance",
                                BigDecimal.valueOf(response.getJan16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());

                    if (response.getJan16to31actual() != null && !response.getJan16to31actual().isNaN())
                        jsonObject.addProperty("Total % of Balance work",
                                BigDecimal.valueOf(100 - response.getJan16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    else
                        jsonObject.addProperty("Total % of Balance work", "0");

                } else if (response.getJan01to15actual() != null && !response.getJan01to15actual().isNaN())
                    jsonObject.addProperty("Total % of Balance work",
                            BigDecimal.valueOf(100 - response.getJan01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
                else
                    jsonObject.addProperty("Total % of Balance work", "0");

                break;
            case 2:
                prepareNovemberData(jsonObject, response);
                prepareDecemberData(jsonObject, response);
                prepareJanuaryData(jsonObject, response);

                if (response.getFeb01to15target() != null && !response.getFeb01to15target().isNaN())
                    jsonObject.addProperty("Feb 01 to 15 target",
                            BigDecimal.valueOf(response.getFeb01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getFeb01to15actual() != null && !response.getFeb01to15actual().isNaN())
                    jsonObject.addProperty("Feb 01 to 15 actual",
                            BigDecimal.valueOf(response.getFeb01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getFeb01to15variance() != null)
                    jsonObject.addProperty("Feb 01 to 15 variance",
                            BigDecimal.valueOf(response.getFeb01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());

                if (currentDate.getDayOfMonth() > 15) {
                    if (response.getFeb16to28or29target() != null && !response.getFeb16to28or29target().isNaN())
                        jsonObject.addProperty("Feb 16 to 28 or 29 target",
                                BigDecimal.valueOf(response.getFeb16to28or29target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getFeb16to28or29actual() != null && !response.getFeb16to28or29actual().isNaN())
                        jsonObject.addProperty("Feb 16 to 28 or 29 actual",
                                BigDecimal.valueOf(response.getFeb16to28or29actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getFeb16to28or29variance() != null)
                        jsonObject.addProperty("Feb 16 to 28 or 29 variance",
                                BigDecimal.valueOf(response.getFeb16to28or29variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getFeb16to28or29actual() != null && !response.getFeb16to28or29actual().isNaN())
                        jsonObject.addProperty("Total % of Balance work",
                                BigDecimal.valueOf(100 - response.getFeb16to28or29actual())
                                        .setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    else
                        jsonObject.addProperty("Total % of Balance work", "0");

                } else if (response.getFeb01to15actual() != null && !response.getFeb01to15actual().isNaN())
                    jsonObject.addProperty("Total % of Balance work",
                            BigDecimal.valueOf(100 - response.getFeb01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
                else
                    jsonObject.addProperty("Total % of Balance work", "0");

                break;
            case 3:
                prepareDecemberData(jsonObject, response);
                prepareJanuaryData(jsonObject, response);
                prepareFebruaryData(jsonObject, response);

                if (response.getMar01to15target() != null && !response.getMar01to15target().isNaN())
                    jsonObject.addProperty("Mar 01 to 15 target",
                            BigDecimal.valueOf(response.getMar01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getMar01to15actual() != null && !response.getMar01to15actual().isNaN())
                    jsonObject.addProperty("Mar 01 to 15 actual",
                            BigDecimal.valueOf(response.getMar01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getMar01to15variance() != null)
                    jsonObject.addProperty("Mar 01 to 15 variance",
                            BigDecimal.valueOf(response.getMar01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());

                if (currentDate.getDayOfMonth() > 15) {
                    if (response.getMar16to31target() != null && !response.getMar16to31target().isNaN())
                        jsonObject.addProperty("Mar 16 to 31 target",
                                BigDecimal.valueOf(response.getMar16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getMar16to31actual() != null && !response.getMar16to31actual().isNaN())
                        jsonObject.addProperty("Mar 16 to 31 actual",
                                BigDecimal.valueOf(response.getMar16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getMar16to31variance() != null)
                        jsonObject.addProperty("Mar 16 to 31 variance",
                                BigDecimal.valueOf(response.getMar16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());

                    if (response.getMar16to31actual() != null && !response.getMar16to31actual().isNaN())
                        jsonObject.addProperty("Total % of Balance work",
                                BigDecimal.valueOf(100 - response.getMar16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    else
                        jsonObject.addProperty("Total % of Balance work", "0");

                } else if (response.getMar01to15actual() != null && !response.getMar01to15actual().isNaN())
                    jsonObject.addProperty("Total % of Balance work",
                            BigDecimal.valueOf(100 - response.getMar01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
                else
                    jsonObject.addProperty("Total % of Balance work", "0");

                break;
            case 4:
                prepareJanuaryData(jsonObject, response);
                prepareFebruaryData(jsonObject, response);
                prepareMarchData(jsonObject, response);

                if (response.getApr01to15target() != null && !response.getApr01to15target().isNaN())
                    jsonObject.addProperty("Apr 01 to 15 target",
                            BigDecimal.valueOf(response.getApr01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getApr01to15actual() != null && !response.getApr01to15actual().isNaN())
                    jsonObject.addProperty("Apr 01 to 15 actual",
                            BigDecimal.valueOf(response.getApr01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getApr01to15variance() != null)
                    jsonObject.addProperty("Apr 01 to 15 variance",
                            BigDecimal.valueOf(response.getApr01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());

                if (currentDate.getDayOfMonth() > 15) {
                    if (response.getApr16to30target() != null && !response.getApr16to30target().isNaN())
                        jsonObject.addProperty("Apr 16 to 30 target",
                                BigDecimal.valueOf(response.getApr16to30target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getApr16to30actual() != null && !response.getApr16to30actual().isNaN())
                        jsonObject.addProperty("Apr 16 to 30 actual",
                                BigDecimal.valueOf(response.getApr16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getApr16to30variance() != null)
                        jsonObject.addProperty("Apr 16 to 30 variance",
                                BigDecimal.valueOf(response.getApr16to30variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getApr16to30actual() != null && !response.getApr16to30actual().isNaN())
                        jsonObject.addProperty("Total % of Balance work",
                                BigDecimal.valueOf(100 - response.getApr16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    else
                        jsonObject.addProperty("Total % of Balance work", "0");
                } else if (response.getApr01to15actual() != null && !response.getApr01to15actual().isNaN())
                    jsonObject.addProperty("Total % of Balance work",
                            BigDecimal.valueOf(100 - response.getApr01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
                else
                    jsonObject.addProperty("Total % of Balance work", "0");
                break;
            case 5:
                prepareFebruaryData(jsonObject, response);
                prepareMarchData(jsonObject, response);
                prepareAprilData(jsonObject, response);

                if (response.getMay01to15target() != null && !response.getMay01to15target().isNaN())
                    jsonObject.addProperty("May 01 to 15 target",
                            BigDecimal.valueOf(response.getMay01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getMay01to15actual() != null && !response.getMay01to15actual().isNaN())
                    jsonObject.addProperty("May 01 to 15 actual",
                            BigDecimal.valueOf(response.getMay01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getMay01to15variance() != null)
                    jsonObject.addProperty("May 01 to 15 variance",
                            BigDecimal.valueOf(response.getMay01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());

                if (currentDate.getDayOfMonth() > 15) {
                    if (response.getMay16to31target() != null && !response.getMay16to31target().isNaN())
                        jsonObject.addProperty("May 16 to 31 target",
                                BigDecimal.valueOf(response.getMay16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getMay16to31actual() != null && !response.getMay16to31actual().isNaN())
                        jsonObject.addProperty("May 16 to 31 actual",
                                BigDecimal.valueOf(response.getMay16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getMay16to31variance() != null)
                        jsonObject.addProperty("May 16 to 31 variance",
                                BigDecimal.valueOf(response.getMay16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getMay16to31actual() != null && !response.getMay16to31actual().isNaN())
                        jsonObject.addProperty("Total % of Balance work",
                                BigDecimal.valueOf(100 - response.getMay16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    else
                        jsonObject.addProperty("Total % of Balance work", "0");

                } else if (response.getMay01to15actual() != null && !response.getMay01to15actual().isNaN())
                    jsonObject.addProperty("Total % of Balance work",
                            BigDecimal.valueOf(100 - response.getMay01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
                else
                    jsonObject.addProperty("Total % of Balance work", "0");

                break;
            case 6:
                prepareMarchData(jsonObject, response);
                prepareAprilData(jsonObject, response);
                prepareMayData(jsonObject, response);

                if (response.getJun01to15target() != null && !response.getJun01to15target().isNaN())
                    jsonObject.addProperty("Jun 01 to 15 target",
                            BigDecimal.valueOf(response.getJun01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getJun01to15actual() != null && !response.getJun01to15actual().isNaN())
                    jsonObject.addProperty("Jun 01 to 15 actual",
                            BigDecimal.valueOf(response.getJun01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getJun01to15variance() != null)
                    jsonObject.addProperty("Jun 01 to 15 variance",
                            BigDecimal.valueOf(response.getJun01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());

                if (currentDate.getDayOfMonth() > 15) {

                    if (response.getJun16to30target() != null && !response.getJun16to30target().isNaN())
                        jsonObject.addProperty("Jun 16 to 30 target",
                                BigDecimal.valueOf(response.getJun16to30target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getJun16to30actual() != null && !response.getJun16to30actual().isNaN())
                        jsonObject.addProperty("Jun 16 to 30 actual",
                                BigDecimal.valueOf(response.getJun16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getJun16to30variance() != null)
                        jsonObject.addProperty("Jun 16 to 30 variance",
                                BigDecimal.valueOf(response.getJun16to30variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getJun16to30actual() != null && !response.getJun16to30actual().isNaN())
                        jsonObject.addProperty("Total % of Balance work",
                                BigDecimal.valueOf(100 - response.getJun16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    else
                        jsonObject.addProperty("Total % of Balance work", "0");

                } else if (response.getJun01to15actual() != null && !response.getJun01to15actual().isNaN())
                    jsonObject.addProperty("Total % of Balance work",
                            BigDecimal.valueOf(100 - response.getJun01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
                else
                    jsonObject.addProperty("Total % of Balance work", "0");
                break;
            case 7:
                prepareAprilData(jsonObject, response);
                prepareMayData(jsonObject, response);
                prepareJuneData(jsonObject, response);

                if (response.getJul01to15target() != null && !response.getJul01to15target().isNaN())
                    jsonObject.addProperty("Jul 01 to 15 target",
                            BigDecimal.valueOf(response.getJul01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getJul01to15actual() != null && !response.getJul01to15actual().isNaN())
                    jsonObject.addProperty("Jul 01 to 15 actual",
                            BigDecimal.valueOf(response.getJul01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getJul01to15variance() != null)
                    jsonObject.addProperty("Jul 01 to 15 variance",
                            BigDecimal.valueOf(response.getJul01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());

                if (currentDate.getDayOfMonth() > 15) {
                    if (response.getJul16to31target() != null && !response.getJul16to31target().isNaN())
                        jsonObject.addProperty("Jul 16 to 31 target",
                                BigDecimal.valueOf(response.getJul16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getJul16to31actual() != null && !response.getJul16to31actual().isNaN())
                        jsonObject.addProperty("Jul 16 to 31 actual",
                                BigDecimal.valueOf(response.getJul16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getJul16to31variance() != null)
                        jsonObject.addProperty("Jul 16 to 31 variance",
                                BigDecimal.valueOf(response.getJul16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getJul16to31actual() != null && !response.getJul16to31actual().isNaN())
                        jsonObject.addProperty("Total % of Balance work",
                                BigDecimal.valueOf(100 - response.getJul16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    else
                        jsonObject.addProperty("Total % of Balance work", "0");

                } else if (response.getJun01to15actual() != null && !response.getJul01to15actual().isNaN())
                    jsonObject.addProperty("Total % of Balance work",
                            BigDecimal.valueOf(100 - response.getJul01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
                else
                    jsonObject.addProperty("Total % of Balance work", "0");

                break;
            case 8:
                prepareMayData(jsonObject, response);
                prepareJuneData(jsonObject, response);
                prepareJulyData(jsonObject, response);

                if (response.getAug01to15target() != null && !response.getAug01to15target().isNaN())
                    jsonObject.addProperty("Aug 01 to 15 target",
                            BigDecimal.valueOf(response.getAug01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getAug01to15actual() != null && !response.getAug01to15actual().isNaN())
                    jsonObject.addProperty("Aug 01 to 15 actual",
                            BigDecimal.valueOf(response.getAug01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getAug01to15variance() != null)
                    jsonObject.addProperty("Aug 01 to 15 variance",
                            BigDecimal.valueOf(response.getAug01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());

                if (currentDate.getDayOfMonth() > 15) {
                    if (response.getAug16to31target() != null && !response.getAug16to31target().isNaN())
                        jsonObject.addProperty("Aug 16 to 31 target",
                                BigDecimal.valueOf(response.getAug16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getAug16to31actual() != null && !response.getAug16to31actual().isNaN())
                        jsonObject.addProperty("Aug 16 to 31 actual",
                                BigDecimal.valueOf(response.getAug16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getAug16to31variance() != null)
                        jsonObject.addProperty("Aug 16 to 31 variance",
                                BigDecimal.valueOf(response.getAug16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getAug16to31actual() != null && !response.getAug16to31actual().isNaN())
                        jsonObject.addProperty("Total % of Balance work",
                                BigDecimal.valueOf(100 - response.getAug16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    else
                        jsonObject.addProperty("Total % of Balance work", "0");
                } else if (response.getAug01to15actual() != null && !response.getAug01to15actual().isNaN())
                    jsonObject.addProperty("Total % of Balance work",
                            BigDecimal.valueOf(100 - response.getAug01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
                else
                    jsonObject.addProperty("Total % of Balance work", "0");

                break;
            case 9:
                prepareJuneData(jsonObject, response);
                prepareJulyData(jsonObject, response);
                prepareAugustData(jsonObject, response);

                if (response.getSep01to15target() != null && !response.getSep01to15target().isNaN())
                    jsonObject.addProperty("Sep 01 to 15 target",
                            BigDecimal.valueOf(response.getSep01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getSep01to15actual() != null && !response.getSep01to15actual().isNaN())
                    jsonObject.addProperty("Sep 01 to 15 actual",
                            BigDecimal.valueOf(response.getSep01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getSep01to15variance() != null)
                    jsonObject.addProperty("Sep 01 to 15 variance",
                            BigDecimal.valueOf(response.getSep01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());

                if (currentDate.getDayOfMonth() > 15) {
                    if (response.getSep16to30target() != null && !response.getSep16to30target().isNaN())
                        jsonObject.addProperty("Sep 16 to 30 target",
                                BigDecimal.valueOf(response.getSep16to30target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getSep16to30actual() != null && !response.getSep16to30actual().isNaN())
                        jsonObject.addProperty("Sep 16 to 30 actual",
                                BigDecimal.valueOf(response.getSep16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getSep16to30variance() != null)
                        jsonObject.addProperty("Sep 16 to 30 variance",
                                BigDecimal.valueOf(response.getSep16to30variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getSep16to30actual() != null && !response.getSep16to30actual().isNaN())
                        jsonObject.addProperty("Total % of Balance work",
                                BigDecimal.valueOf(100 - response.getSep16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    else
                        jsonObject.addProperty("Total % of Balance work", "0");

                } else if (response.getSep01to15actual() != null && !response.getSep01to15actual().isNaN())
                    jsonObject.addProperty("Total % of Balance work",
                            BigDecimal.valueOf(100 - response.getSep01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
                else
                    jsonObject.addProperty("Total % of Balance work", "0");

                break;
            case 10:
                prepareJulyData(jsonObject, response);
                prepareAugustData(jsonObject, response);
                prepareSeptemberData(jsonObject, response);

                if (response.getOct01to15target() != null && !response.getOct01to15target().isNaN())
                    jsonObject.addProperty("Oct 01 to 15 target",
                            BigDecimal.valueOf(response.getOct01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getOct01to15actual() != null && !response.getOct01to15actual().isNaN())
                    jsonObject.addProperty("Oct 01 to 15 actual",
                            BigDecimal.valueOf(response.getOct01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getOct01to15variance() != null)
                    jsonObject.addProperty("Oct 01 to 15 variance",
                            BigDecimal.valueOf(response.getOct01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());

                if (currentDate.getDayOfMonth() > 15) {
                    if (response.getOct16to31target() != null && !response.getOct16to31target().isNaN())
                        jsonObject.addProperty("Oct 16 to 31 target",
                                BigDecimal.valueOf(response.getOct16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    else
                        jsonObject.addProperty("Oct 16 to 31 target", "0");
                    if (response.getOct16to31actual() != null && !response.getOct16to31actual().isNaN())
                        jsonObject.addProperty("Oct 16 to 31 actual",
                                BigDecimal.valueOf(response.getOct16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    else
                        jsonObject.addProperty("Oct 16 to 31 actual", "0");
                    if (response.getOct16to31variance() != null)
                        jsonObject.addProperty("Oct 16 to 31 variance",
                                BigDecimal.valueOf(response.getOct16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    else
                        jsonObject.addProperty("Oct 16 to 31 variance", "0");
                    if (response.getOct16to31actual() != null && !response.getOct16to31actual().isNaN())
                        jsonObject.addProperty("Total % of Balance work",
                                BigDecimal.valueOf(100 - response.getOct16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    else
                        jsonObject.addProperty("Total % of Balance work", "0");

                } else if (response.getOct01to15actual() != null && !response.getOct01to15actual().isNaN())
                    jsonObject.addProperty("Total % of Balance work",
                            BigDecimal.valueOf(100 - response.getOct01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
                else
                    jsonObject.addProperty("Total % of Balance work", "0");
                break;
            case 11:
                prepareAugustData(jsonObject, response);
                prepareSeptemberData(jsonObject, response);
                prepareOctoberData(jsonObject, response);

                if (response.getNov01to15target() != null && !response.getNov01to15target().isNaN())
                    jsonObject.addProperty("Nov 01 to 15 target",
                            BigDecimal.valueOf(response.getNov01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getNov01to15actual() != null && !response.getNov01to15actual().isNaN())
                    jsonObject.addProperty("Nov 01 to 15 actual",
                            BigDecimal.valueOf(response.getNov01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getNov01to15variance() != null)
                    jsonObject.addProperty("Nov 01 to 15 variance",
                            BigDecimal.valueOf(response.getNov01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());

                if (currentDate.getDayOfMonth() > 15) {
                    if (response.getNov16to30target() != null && !response.getNov16to30target().isNaN())
                        jsonObject.addProperty("Nov 16 to 30 target",
                                BigDecimal.valueOf(response.getNov16to30target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getNov16to30actual() != null && !response.getNov16to30actual().isNaN())
                        jsonObject.addProperty("Nov 16 to 30 actual",
                                BigDecimal.valueOf(response.getNov16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getNov16to30variance() != null)
                        jsonObject.addProperty("Nov 16 to 30 variance",
                                BigDecimal.valueOf(response.getNov16to30variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getNov16to30actual() != null && !response.getNov16to30actual().isNaN())
                        jsonObject.addProperty("Total % of Balance work",
                                BigDecimal.valueOf(100 - response.getNov16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    else
                        jsonObject.addProperty("Total % of Balance work", "0");
                } else if (response.getNov01to15actual() != null && !response.getNov01to15actual().isNaN())
                    jsonObject.addProperty("Total % of Balance work",
                            BigDecimal.valueOf(100 - response.getNov01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
                else
                    jsonObject.addProperty("Total % of Balance work", "0");

                break;
            case 12:
                prepareSeptemberData(jsonObject, response);
                prepareOctoberData(jsonObject, response);
                prepareNovemberData(jsonObject, response);

                if (response.getDec01to15target() != null && !response.getDec01to15target().isNaN())
                    jsonObject.addProperty("Dec 01 to 15 target",
                            BigDecimal.valueOf(response.getDec01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getDec01to15actual() != null && !response.getDec01to15actual().isNaN())
                    jsonObject.addProperty("Dec 01 to 15 actual",
                            BigDecimal.valueOf(response.getDec01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                if (response.getDec01to15variance() != null)
                    jsonObject.addProperty("Dec 01 to 15 variance",
                            BigDecimal.valueOf(response.getDec01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());

                if (currentDate.getDayOfMonth() > 15) {
                    if (response.getDec16to31target() != null && !response.getDec16to31target().isNaN())
                        jsonObject.addProperty("Dec 16 to 31 target",
                                BigDecimal.valueOf(response.getDec16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getDec16to31actual() != null && !response.getDec16to31actual().isNaN())
                        jsonObject.addProperty("Dec 16 to 31 actual",
                                BigDecimal.valueOf(response.getDec16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    if (response.getDec16to31variance() != null)
                        jsonObject.addProperty("Dec 16 to 31 variance",
                                BigDecimal.valueOf(response.getDec16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());

                    if (response.getDec16to31actual() != null && !response.getDec16to31actual().isNaN())
                        jsonObject.addProperty("Total % of Balance work",
                                BigDecimal.valueOf(100 - response.getDec16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                        .toString());
                    else
                        jsonObject.addProperty("Total % of Balance work", "0");
                } else if (response.getDec01to15actual() != null && !response.getDec01to15actual().isNaN())
                    jsonObject.addProperty("Total % of Balance work",
                            BigDecimal.valueOf(100 - response.getDec01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                                    .toString());
                else
                    jsonObject.addProperty("Total % of Balance work", "0");

                break;
            default:
                break;
            }

            jsonObject.addProperty("Financial progress %",
                    BigDecimal.valueOf(response.getFinancialprogress()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        }
        return jsonObject;
    }

    private void prepareJanuaryData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getJan01to15target() != null && !response.getJan01to15target().isNaN())
            jsonObject.addProperty("Jan 01 to 15 target",
                    BigDecimal.valueOf(response.getJan01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getJan01to15actual() != null && !response.getJan01to15actual().isNaN())
            jsonObject.addProperty("Jan 01 to 15 actual",
                    BigDecimal.valueOf(response.getJan01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getJan01to15variance() != null)
            jsonObject.addProperty("Jan 01 to 15 variance",
                    BigDecimal.valueOf(response.getJan01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getJan16to31target() != null && !response.getJan16to31target().isNaN())
            jsonObject.addProperty("Jan 16 to 31 target",
                    BigDecimal.valueOf(response.getJan16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getJan16to31actual() != null && !response.getJan16to31actual().isNaN())
            jsonObject.addProperty("Jan 16 to 31 actual",
                    BigDecimal.valueOf(response.getJan16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getJan16to31variance() != null)
            jsonObject.addProperty("Jan 16 to 31 variance",
                    BigDecimal.valueOf(response.getJan16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
    }

    private void prepareFebruaryData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getFeb01to15target() != null && !response.getFeb01to15target().isNaN())
            jsonObject.addProperty("Feb 01 to 15 target",
                    BigDecimal.valueOf(response.getFeb01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getFeb01to15actual() != null && !response.getFeb01to15actual().isNaN())
            jsonObject.addProperty("Feb 01 to 15 actual",
                    BigDecimal.valueOf(response.getFeb01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getFeb01to15variance() != null)
            jsonObject.addProperty("Feb 01 to 15 variance",
                    BigDecimal.valueOf(response.getFeb01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getFeb16to28or29target() != null && !response.getFeb16to28or29target().isNaN())
            jsonObject.addProperty("Feb 16 to 28 or 29 target",
                    BigDecimal.valueOf(response.getFeb16to28or29target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getFeb16to28or29actual() != null && !response.getFeb16to28or29actual().isNaN())
            jsonObject.addProperty("Feb 16 to 28 or 29 actual",
                    BigDecimal.valueOf(response.getFeb16to28or29actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getFeb16to28or29variance() != null)
            jsonObject.addProperty("Feb 16 to 28 or 29 variance",
                    BigDecimal.valueOf(response.getFeb16to28or29variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
    }

    private void prepareMarchData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getMar01to15target() != null && !response.getMar01to15target().isNaN())
            jsonObject.addProperty("Mar 01 to 15 target",
                    BigDecimal.valueOf(response.getMar01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getMar01to15actual() != null && !response.getMar01to15actual().isNaN())
            jsonObject.addProperty("Mar 01 to 15 actual",
                    BigDecimal.valueOf(response.getMar01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getMar01to15variance() != null)
            jsonObject.addProperty("Mar 01 to 15 variance",
                    BigDecimal.valueOf(response.getMar01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getMar16to31target() != null && !response.getMar16to31target().isNaN())
            jsonObject.addProperty("Mar 16 to 31 target",
                    BigDecimal.valueOf(response.getMar16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getMar16to31actual() != null && !response.getMar16to31actual().isNaN())
            jsonObject.addProperty("Mar 16 to 31 actual",
                    BigDecimal.valueOf(response.getMar16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getMar16to31variance() != null)
            jsonObject.addProperty("Mar 16 to 31 variance",
                    BigDecimal.valueOf(response.getMar16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
    }

    private void prepareAprilData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getApr01to15target() != null && !response.getApr01to15target().isNaN())
            jsonObject.addProperty("Apr 01 to 15 target",
                    BigDecimal.valueOf(response.getApr01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getApr01to15actual() != null && !response.getApr01to15actual().isNaN())
            jsonObject.addProperty("Apr 01 to 15 actual",
                    BigDecimal.valueOf(response.getApr01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getApr01to15variance() != null)
            jsonObject.addProperty("Apr 01 to 15 variance",
                    BigDecimal.valueOf(response.getApr01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getApr16to30target() != null && !response.getApr16to30target().isNaN())
            jsonObject.addProperty("Apr 16 to 30 target",
                    BigDecimal.valueOf(response.getApr16to30target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getApr16to30actual() != null && !response.getApr16to30actual().isNaN())
            jsonObject.addProperty("Apr 16 to 30 actual",
                    BigDecimal.valueOf(response.getApr16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getApr16to30variance() != null)
            jsonObject.addProperty("Apr 16 to 30 variance",
                    BigDecimal.valueOf(response.getApr16to30variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
    }

    private void prepareMayData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getMay01to15target() != null && !response.getMay01to15target().isNaN())
            jsonObject.addProperty("May 01 to 15 target",
                    BigDecimal.valueOf(response.getMay01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getMay01to15actual() != null && !response.getMay01to15actual().isNaN())
            jsonObject.addProperty("May 01 to 15 actual",
                    BigDecimal.valueOf(response.getMay01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getMay01to15variance() != null)
            jsonObject.addProperty("May 01 to 15 variance",
                    BigDecimal.valueOf(response.getMay01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getMay16to31target() != null && !response.getMay16to31target().isNaN())
            jsonObject.addProperty("May 16 to 31 target",
                    BigDecimal.valueOf(response.getMay16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getMay16to31actual() != null && !response.getMay16to31actual().isNaN())
            jsonObject.addProperty("May 16 to 31 actual",
                    BigDecimal.valueOf(response.getMay16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getMay16to31variance() != null)
            jsonObject.addProperty("May 16 to 31 variance",
                    BigDecimal.valueOf(response.getMay16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
    }

    private void prepareJuneData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {

        if (response.getJun01to15target() != null && !response.getJun01to15target().isNaN())
            jsonObject.addProperty("Jun 01 to 15 target",
                    BigDecimal.valueOf(response.getJun01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getJun01to15actual() != null && !response.getJun01to15actual().isNaN())
            jsonObject.addProperty("Jun 01 to 15 actual",
                    BigDecimal.valueOf(response.getJun01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getJun01to15variance() != null)
            jsonObject.addProperty("Jun 01 to 15 variance",
                    BigDecimal.valueOf(response.getJun01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getJun16to30target() != null && !response.getJun16to30target().isNaN())
            jsonObject.addProperty("Jun 16 to 30 target",
                    BigDecimal.valueOf(response.getJun16to30target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getJun16to30actual() != null && !response.getJun16to30actual().isNaN())
            jsonObject.addProperty("Jun 16 to 30 actual",
                    BigDecimal.valueOf(response.getJun16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getJun16to30variance() != null)
            jsonObject.addProperty("Jun 16 to 30 variance",
                    BigDecimal.valueOf(response.getJun16to30variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
    }

    private void prepareJulyData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getJul01to15target() != null && !response.getJul01to15target().isNaN())
            jsonObject.addProperty("Jul 01 to 15 target",
                    BigDecimal.valueOf(response.getJul01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getJul01to15actual() != null && !response.getJul01to15actual().isNaN())
            jsonObject.addProperty("Jul 01 to 15 actual",
                    BigDecimal.valueOf(response.getJul01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getJul01to15variance() != null)
            jsonObject.addProperty("Jul 01 to 15 variance",
                    BigDecimal.valueOf(response.getJul01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getJul16to31target() != null && !response.getJul16to31target().isNaN())
            jsonObject.addProperty("Jul 16 to 31 target",
                    BigDecimal.valueOf(response.getJul16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getJul16to31actual() != null && !response.getJul16to31actual().isNaN())
            jsonObject.addProperty("Jul 16 to 31 actual",
                    BigDecimal.valueOf(response.getJul16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getJul16to31variance() != null)
            jsonObject.addProperty("Jul 16 to 31 variance",
                    BigDecimal.valueOf(response.getJul16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
    }

    private void prepareAugustData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getAug01to15target() != null && !response.getAug01to15target().isNaN())
            jsonObject.addProperty("Aug 01 to 15 target",
                    BigDecimal.valueOf(response.getAug01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getAug01to15actual() != null && !response.getAug01to15actual().isNaN())
            jsonObject.addProperty("Aug 01 to 15 actual",
                    BigDecimal.valueOf(response.getAug01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getAug01to15variance() != null)
            jsonObject.addProperty("Aug 01 to 15 variance",
                    BigDecimal.valueOf(response.getAug01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getAug16to31target() != null && !response.getAug16to31target().isNaN())
            jsonObject.addProperty("Aug 16 to 31 target",
                    BigDecimal.valueOf(response.getAug16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getAug16to31actual() != null && !response.getAug16to31actual().isNaN())
            jsonObject.addProperty("Aug 16 to 31 actual",
                    BigDecimal.valueOf(response.getAug16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getAug16to31variance() != null)
            jsonObject.addProperty("Aug 16 to 31 variance",
                    BigDecimal.valueOf(response.getAug16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
    }

    private void prepareSeptemberData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getSep01to15target() != null && !response.getSep01to15target().isNaN())
            jsonObject.addProperty("Sep 01 to 15 target",
                    BigDecimal.valueOf(response.getSep01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getSep01to15actual() != null && !response.getSep01to15actual().isNaN())
            jsonObject.addProperty("Sep 01 to 15 actual",
                    BigDecimal.valueOf(response.getSep01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getSep01to15variance() != null)
            jsonObject.addProperty("Sep 01 to 15 variance",
                    BigDecimal.valueOf(response.getSep01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getSep16to30target() != null && !response.getSep16to30target().isNaN())
            jsonObject.addProperty("Sep 16 to 30 target",
                    BigDecimal.valueOf(response.getSep16to30target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getSep16to30actual() != null && !response.getSep16to30actual().isNaN())
            jsonObject.addProperty("Sep 16 to 30 actual",
                    BigDecimal.valueOf(response.getSep16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getSep16to30variance() != null)
            jsonObject.addProperty("Sep 16 to 30 variance",
                    BigDecimal.valueOf(response.getSep16to30variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
    }

    private void prepareOctoberData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getOct01to15target() != null && !response.getOct01to15target().isNaN())
            jsonObject.addProperty("Oct 01 to 15 target",
                    BigDecimal.valueOf(response.getOct01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getOct01to15actual() != null && !response.getOct01to15actual().isNaN())
            jsonObject.addProperty("Oct 01 to 15 actual",
                    BigDecimal.valueOf(response.getOct01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getOct01to15variance() != null)
            jsonObject.addProperty("Oct 01 to 15 variance",
                    BigDecimal.valueOf(response.getOct01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getOct16to31target() != null && !response.getOct16to31target().isNaN())
            jsonObject.addProperty("Oct 16 to 31 target",
                    BigDecimal.valueOf(response.getOct16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getOct16to31actual() != null && !response.getOct16to31actual().isNaN())
            jsonObject.addProperty("Oct 16 to 31 actual",
                    BigDecimal.valueOf(response.getOct16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getOct16to31variance() != null)
            jsonObject.addProperty("Oct 16 to 31 variance",
                    BigDecimal.valueOf(response.getOct16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
    }

    private void prepareNovemberData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getNov01to15target() != null && !response.getNov01to15target().isNaN())
            jsonObject.addProperty("Nov 01 to 15 target",
                    BigDecimal.valueOf(response.getNov01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getNov01to15actual() != null && !response.getNov01to15actual().isNaN())
            jsonObject.addProperty("Nov 01 to 15 actual",
                    BigDecimal.valueOf(response.getNov01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getNov01to15variance() != null)
            jsonObject.addProperty("Nov 01 to 15 variance",
                    BigDecimal.valueOf(response.getNov01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getNov16to30target() != null && !response.getNov16to30target().isNaN())
            jsonObject.addProperty("Nov 16 to 30 target",
                    BigDecimal.valueOf(response.getNov16to30target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getNov16to30actual() != null && !response.getNov16to30actual().isNaN())
            jsonObject.addProperty("Nov 16 to 30 actual",
                    BigDecimal.valueOf(response.getNov16to30actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getNov16to30variance() != null)
            jsonObject.addProperty("Nov 16 to 30 variance",
                    BigDecimal.valueOf(response.getNov16to30variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
    }

    private void prepareDecemberData(final JsonObject jsonObject, final WorksMilestoneIndexResponse response) {
        if (response.getDec01to15target() != null && !response.getDec01to15target().isNaN())
            jsonObject.addProperty("Dec 01 to 15 target",
                    BigDecimal.valueOf(response.getDec01to15target()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getDec01to15actual() != null && !response.getDec01to15actual().isNaN())
            jsonObject.addProperty("Dec 01 to 15 actual",
                    BigDecimal.valueOf(response.getDec01to15actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        if (response.getDec01to15variance() != null)
            jsonObject.addProperty("Dec 01 to 15 variance",
                    BigDecimal.valueOf(response.getDec01to15variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getDec16to31target() != null && !response.getDec16to31target().isNaN())
            jsonObject.addProperty("Dec 16 to 31 target",
                    BigDecimal.valueOf(response.getDec16to31target()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getDec16to31actual() != null && !response.getDec16to31actual().isNaN())
            jsonObject.addProperty("Dec 16 to 31 actual",
                    BigDecimal.valueOf(response.getDec16to31actual()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
        if (response.getDec16to31variance() != null)
            jsonObject.addProperty("Dec 16 to 31 variance",
                    BigDecimal.valueOf(response.getDec16to31variance()).setScale(2, BigDecimal.ROUND_HALF_EVEN)
                            .toString());
    }
}