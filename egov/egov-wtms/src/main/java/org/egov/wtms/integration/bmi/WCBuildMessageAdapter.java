/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
package org.egov.wtms.integration.bmi;

import static org.egov.eventnotification.utils.Constants.DDMMYYYY;
import static org.egov.eventnotification.utils.Constants.MESSAGE_USERNAME;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.egov.eventnotification.entity.contracts.UserTaxInformation;
import org.egov.eventnotification.integration.bmi.BuildMessageAdapter;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class WCBuildMessageAdapter implements BuildMessageAdapter {

    @Autowired
    private UserService userService;

    @Override
    public String buildMessage(UserTaxInformation userTaxInformation, String message) {
        DateFormat formatter = new SimpleDateFormat(DDMMYYYY);
        User user = userService.getUserById(userTaxInformation.getUserId());
        String finalMessage = message;
        if (finalMessage.contains("{{consumerNumber}}"))
            finalMessage = finalMessage.replace("{{consumerNumber}}", userTaxInformation.getConsumerNumber());

        if (finalMessage.contains("{{billNumber}}"))
            finalMessage = finalMessage.replace("{{billNumber}}", userTaxInformation.getBillNo());

        if (finalMessage.contains("{{billAmount}}"))
            finalMessage = finalMessage.replace("{{billAmount}}", String.valueOf(userTaxInformation.getDueAmount()));

        if (finalMessage.contains(MESSAGE_USERNAME))
            finalMessage = finalMessage.replace(MESSAGE_USERNAME, user.getName());

        if (finalMessage.contains("{{disruptionDate}}") && userTaxInformation.getDueDate() != null)
            finalMessage = finalMessage.replace("{{disruptionDate}}", formatter.format(userTaxInformation.getDueDate()));

        return finalMessage;
    }

}
