/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.mrs.web.controller.reports;

import static org.egov.infra.web.utils.WebUtils.toJSON;

import java.text.ParseException;
import java.util.List;

import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.MarriageRegistration.RegistrationStatus;
import org.egov.mrs.domain.service.MarriageRegistrationService;
import org.egov.mrs.web.adaptor.MarriageRegistrationJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller to show report of Registration status
 * 
 * @author nayeem
 *
 */
@Controller
@RequestMapping(value = "/report")
public class RegistrationStatusReportController {

	@Autowired
	private MarriageRegistrationService marriageRegistrationService;

	@RequestMapping(value = "/registrationstatus", method = RequestMethod.GET)
	public String showReportForm(final Model model) {
		model.addAttribute("registration", new MarriageRegistration());
		model.addAttribute("status", RegistrationStatus.values());
		return "report-registrationstatus";
	}

	@RequestMapping(value = "/registrationstatus", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String search(Model model, @ModelAttribute final MarriageRegistration registration)
			throws ParseException {
		List<MarriageRegistration> searchResultList = marriageRegistrationService
				.searchRegistrationByStatus(registration, registration.getStatus().getCode());
		String result = new StringBuilder("{ \"data\":")
				.append(toJSON(searchResultList, MarriageRegistration.class, MarriageRegistrationJsonAdaptor.class))
				.append("}").toString();
		return result;
	}

}
