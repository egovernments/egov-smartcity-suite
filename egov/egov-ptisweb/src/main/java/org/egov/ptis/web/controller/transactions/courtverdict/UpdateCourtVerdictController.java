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
package org.egov.ptis.web.controller.transactions.courtverdict;

import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_COURT_VERDICT;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.ptis.domain.entity.property.CourtVerdict;
import org.egov.ptis.domain.service.courtverdict.CourtVerdictService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courtVerdict/update/{id}")
public class UpdateCourtVerdictController extends GenericWorkFlowController {

    @Autowired
    private CourtVerdictService courtVerdictService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    @Autowired
    private PropertyService propService;

    @ModelAttribute
    public CourtVerdict courtVerdictModel(@PathVariable Long id) {
        return courtVerdictService.getCourtVerdictById(id);

    }

    @GetMapping
    public String view(@ModelAttribute CourtVerdict courtVerdict, Model model, HttpServletRequest request) {
        boolean citizenPortalUser = false;

        User loggedInUser = securityUtils.getCurrentUser();
        citizenPortalUser = propertyService.isCitizenPortalUser(loggedInUser);
        List<HashMap<String, Object>> historyMap;

        model.addAttribute("courtVerdict", courtVerdict);
        model.addAttribute("property", courtVerdict.getProperty());
        model.addAttribute("citizenPortalUser", citizenPortalUser);
        model.addAttribute("currentState", courtVerdict.getCurrentState().getValue());
        model.addAttribute("transactionType", APPLICATION_TYPE_COURT_VERDICT);
        model.addAttribute("stateAwareId", courtVerdict.getId());
        model.addAttribute("stateType", courtVerdict.getClass().getSimpleName());
        model.addAttribute("endorsementNotices",
                propertyTaxCommonUtils.getEndorsementNotices(courtVerdict.getApplicationNumber()));
        model.addAttribute("loggedUserIsEmployee", propertyService.isEmployee(loggedInUser));
        prepareWorkflow(model, courtVerdict, new WorkflowContainer());

        if (courtVerdict.getId() != null && courtVerdict.getState() != null) {
            historyMap = propService.populateHistory(courtVerdict);
            model.addAttribute("historyMap", historyMap);
            model.addAttribute("state", courtVerdict.getState());
        }

        return "courtVerdict-view";

    }
}
