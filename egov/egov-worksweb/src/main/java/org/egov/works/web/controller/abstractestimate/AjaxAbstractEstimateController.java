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
package org.egov.works.web.controller.abstractestimate;

import java.util.Date;

import org.egov.works.master.service.OverheadService;
import org.egov.works.models.masters.Overhead;
import org.egov.works.models.masters.OverheadRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/abstractestimate")
public class AjaxAbstractEstimateController {

	@Autowired
	private OverheadService overheadService;

	@RequestMapping(value = "/getpercentageorlumpsumbyoverheadid", method = RequestMethod.GET)
	public @ResponseBody OverheadRate getPercentageOrLumpsumByOverhead(@RequestParam("overheadId") Long overheadId) {

		Overhead overhead = new Overhead();
		OverheadRate overheadRate = new OverheadRate();
		Date startDate, endDate, estDate = new Date();
		if (overheadId != null) {
			overhead = overheadService.getOverheadById(overheadId);
		}

		if (overhead != null && overhead.getOverheadRates() != null && overhead.getOverheadRates().size() > 0) {
			for (OverheadRate obj : overhead.getOverheadRates()) {
				startDate = obj.getValidity().getStartDate();
				endDate = obj.getValidity().getEndDate();
				if (estDate.compareTo(startDate) >= 0 && (endDate == null || endDate.compareTo(estDate) >= 0 )) {
					overheadRate = obj;
				}
			}
		}

		return overheadRate;
	}

}
