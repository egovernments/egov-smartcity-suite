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

package org.egov.restapi.web.controller.pgr.integration.ivrs;

import org.egov.pgr.integration.ivrs.entiry.contract.IVRSFeedbackUpdateRequest;
import org.egov.pgr.integration.ivrs.repository.IVRSRatingRepository;
import org.egov.pgr.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class IVRSFeedbackUpdateAPIValidator implements Validator {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private IVRSRatingRepository feedbackRatingRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return IVRSFeedbackUpdateRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        IVRSFeedbackUpdateRequest request = (IVRSFeedbackUpdateRequest) target;

        if (request.getCrn().isEmpty() || complaintService.getComplaintByCRN(request.getCrn()) == null)
            errors.rejectValue("crn", "Invalid CRN", "Invalid CRN");

        if (request.getRating().isEmpty() || feedbackRatingRepository.findByName(request.getRating()) == null)
            errors.rejectValue("rating", "Invalid Rating", "Invalid Rating");

    }
}