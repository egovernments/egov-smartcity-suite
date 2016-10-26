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

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Controller to show report of Registration status
 * 
 * @author nayeem
 *
 */
@Controller
@RequestMapping(value = "/report")
public class MarriageRegistrationReportsController {
	

	private final static String[] RANGES = new String[] { "0-18", "19-25", "26-30", "31-35", "36-40", "40-45", "46-50",
			"50-100" };
	private final static String KEY_AGE = "age";
	private final static String KEY_HUSBANDCOUNT = "husbandcount";
	private final static String KEY_WIFECOUNT = "wifecount";

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
	
	
	@RequestMapping(value="/age-wise", method = RequestMethod.GET)
    public String newSearchForm(final Model model) {
		model.addAttribute("registration", new MarriageRegistration());
        return "report-registration-agewise";
    }
	
	@SuppressWarnings("serial")
	@RequestMapping(value = "/age-wise", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String searchAgeWise(@RequestParam("year") int year, Model model, @ModelAttribute final MarriageRegistration registration)
			throws ParseException {

		HashMap<String, Integer> husbandAgeRangesCount = getCountByRange(marriageRegistrationService.searchRegistrationOfHusbandAgeWise(year));
		HashMap<String, Integer> wifeAgeRangesCount = getCountByRange(marriageRegistrationService.searchRegistrationOfWifeAgeWise(year));

		ArrayList<HashMap<String, Object>> result = new ArrayList<>();

		for (String range : RANGES) {
			HashMap<String, Object> rangeMap = new HashMap<>();
			rangeMap.put(KEY_AGE, range);
			rangeMap.put(KEY_HUSBANDCOUNT, husbandAgeRangesCount.get(range)!=null?husbandAgeRangesCount.get(range):0);
			rangeMap.put(KEY_WIFECOUNT, wifeAgeRangesCount.get(range)!=null?wifeAgeRangesCount.get(range):0);
			result.add(rangeMap);
		}

		JsonArray jsonArray = (JsonArray) new Gson().toJsonTree(result, new TypeToken<List<HashMap<String, Object>>>() {
		}.getType());

		JsonObject response = new JsonObject();
		response.add("data", jsonArray);
		return response.toString();
	}

	private HashMap<String, Integer> getCountByRange(String[] inputs) {

		HashMap<String, Integer> response = new HashMap<>();

		for (String input : inputs) {
			String[] values=input.split(","); //age,count -> [0] - age, [1] - count
			Integer age = Integer.valueOf(values[0]);
			for (String range : RANGES) {
				if (isInRange(range, age)) {
					int existingCount = response.get(range) != null ? response.get(range) : 0;
					response.put(range, existingCount + Integer.valueOf(values[1]));
					break;
				}
			}
		}

		return response;
	}

	private boolean isInRange(String ranges, Integer input) {
		String[] range = ranges.split("-");
		return (input >= Integer.valueOf(range[0]) && input <= Integer.valueOf(range[1]));
	}
	
	@RequestMapping(value = "/age-wise/view/{year}/{applicantType}/{ageRange}", method = RequestMethod.GET)
	public String viewAgeWiseDetails(@PathVariable final int year, @PathVariable final String applicantType,
			@PathVariable final String ageRange, final Model model) throws IOException, ParseException {
		List<MarriageRegistration> marriageRegistrations = marriageRegistrationService.getAgewiseDetails(ageRange,
				applicantType, year);
		model.addAttribute("marriageRegistrations", marriageRegistrations);
		model.addAttribute("applicantType", applicantType);
		return "marriage-agewise-view";
	}
	
	}
