/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
package org.egov.tl.web.controller.report;

import static org.egov.tl.utils.Constants.REVENUE_HIERARCHY_TYPE;
import static org.egov.tl.utils.Constants.WARD;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Date;
import java.util.List;

import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.es.CollectionDocument;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.tl.entity.dto.DCRSearchRequest;
import org.egov.tl.service.DCRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/reports/dailycollectionreport")
public class DCRController {

    @Autowired
    public EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private DCRService dCRService;

    @ModelAttribute("dcrSearchRequest")
    public DCRSearchRequest dcrSearchRequest() {
        return new DCRSearchRequest();
    }

    @RequestMapping(method = GET)
    public String search(final Model model) {
        model.addAttribute("currentDate", new Date());
        model.addAttribute("operators", dCRService.getCollectionOperators());
        model.addAttribute("status", egwStatusHibernateDAO.getStatusByModule(CollectionConstants.MODULE_NAME_RECEIPTHEADER));
        model.addAttribute("wards",
                boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD, REVENUE_HIERARCHY_TYPE));
        return "dcr-search";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public List<CollectionDocument> searchCollection(@ModelAttribute final DCRSearchRequest searchRequest) {
        return dCRService.searchDailyCollection(searchRequest);

    }

}
