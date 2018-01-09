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

package org.egov.pgr.integration.ivrs.event.listener;

import org.egov.infra.rest.client.SimpleRestClient;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.event.model.ComplaintEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.infra.utils.DateUtils.currentDateToGivenFormat;
import static org.egov.infra.utils.DateUtils.getFormattedDate;
import static org.egov.pgr.entity.enums.ComplaintStatus.COMPLETED;
import static org.egov.pgr.utils.constants.PGRConstants.MODULE_NAME;

@Component
public class IVRComplaintEventListener implements ApplicationListener<ComplaintEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(IVRComplaintEventListener.class);
    private static final String IVR_DATE_FORMAT = "dd-MM-yy";

    @Autowired
    private SimpleRestClient simpleRestClient;

    @Value("${ivr.url}")
    private String ivrURL;

    @Value("${ivr.enabled}")
    private boolean ivrEnabled;

    @Override
    public void onApplicationEvent(ComplaintEvent complaintEvent) {
        try {
            Complaint complaint = complaintEvent.getComplaint();
            if (ivrEnabled && isNotBlank(complaint.getComplainant().getMobile())
                    && COMPLETED.toString().equalsIgnoreCase(complaint.getStatus().getName())) {
                String ivrRequestURL = String
                        .format(ivrURL, complaint.getCrn(), complaintEvent.getCityCode(), MODULE_NAME,
                                complaint.getComplaintType().getCategory().getId(), complaint.getComplainant().getMobile(),
                                URLEncoder.encode(complaint.getState().getComments(), UTF_8.toString()),
                                getFormattedDate(complaint.getCreatedDate(), IVR_DATE_FORMAT),
                                currentDateToGivenFormat(IVR_DATE_FORMAT), complaint.getComplaintType().getId(),
                                complaint.getDepartment().getId(), URLEncoder.encode(complaint.getDetails(), UTF_8.toString()));
                if (LOG.isInfoEnabled())
                    LOG.info("IVR Request : {}", ivrRequestURL);
                Map<String, Object> ivrResponse = simpleRestClient.getRESTResponseAsMap(ivrRequestURL);
                if (LOG.isInfoEnabled())
                    LOG.info("IVR Request, Response : {}, Date : {}, Error : {}",
                            ivrResponse.get("result"), ivrResponse.get("incoming"),
                            defaultString((String) ivrResponse.get("error")));
            }
        } catch (UnsupportedEncodingException e) {
            LOG.error("Error occurred while sending IVR request", e);
        }
    }
}
