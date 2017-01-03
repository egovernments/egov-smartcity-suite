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

package org.egov.pgr.web.controller.masters.escalation;

import org.apache.commons.io.IOUtils;
import org.egov.commons.ObjectType;
import org.egov.commons.service.ObjectTypeService;
import org.egov.eis.entity.PositionHierarchy;
import org.egov.eis.service.PositionHierarchyService;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.utils.constants.PGRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/escalation")
public class SearchEscalationController {
    public static final String CONTENTTYPE_JSON = "application/json";

    protected final ComplaintTypeService complaintTypeService;

    private final ObjectTypeService objectTypeService;

    @Autowired
    private PositionHierarchyService positionHierarchyService;

    @ModelAttribute
    public PositionHierarchy positionHierarchy() {
        return new PositionHierarchy();
    }

    @Autowired
    public SearchEscalationController(final ComplaintTypeService complaintTypeService,
            final ObjectTypeService objectTypeService) {
        this.complaintTypeService = complaintTypeService;
        this.objectTypeService = objectTypeService;
    }

    @RequestMapping(value = "/view", method = RequestMethod.POST)
    public String searchEscalationTimeForm(@ModelAttribute final PositionHierarchy positionHierarchy, final Model model) {
        return "escalation-view";
    }

    @RequestMapping(value = "/view", method = GET)
    public String searchForm(@ModelAttribute final PositionHierarchy positionHierarchy, final Model model) {

        return "escalation-view";
    }

    @ExceptionHandler(Exception.class)
    @RequestMapping(value = "resultList-update", method = RequestMethod.GET)
    public @ResponseBody void springPaginationDataTablesUpdate(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        Long positionId = Long.valueOf(0);
        Long complaintTypeId = Long.valueOf(0);
        ComplaintType complaintType = null;
        String complaintTypeCode = null;

        if (request.getParameter("positionId") != null && !"".equals(request.getParameter("positionId")))
            positionId = Long.valueOf(request.getParameter("positionId"));
        if (request.getParameter("complaintTypeId") != null && !"".equals(request.getParameter("complaintTypeId"))) {
            complaintTypeId = Long.valueOf(request.getParameter("complaintTypeId"));
            complaintType = complaintTypeService.findBy(complaintTypeId);
            if (complaintType != null)
                complaintTypeCode = complaintType.getCode();
        }

        final ObjectType objectType = objectTypeService.getObjectTypeByName(PGRConstants.EG_OBJECT_TYPE_COMPLAINT);

        if (objectType != null) {
            final String escalationTimeRouterJSONData = commonSearchResult(positionId, complaintTypeCode,
                    objectType.getId());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            IOUtils.write(escalationTimeRouterJSONData, response.getWriter());
        }
    }

    // final Integer pageNumber, final Integer pageSize,
    public String commonSearchResult(final Long positionId, final String complaintTypeCode, final Integer objectId) {
        final List<EscalationHelper> escalationHelperList = new ArrayList<EscalationHelper>();
        final List<PositionHierarchy> pageOfEscalation = positionHierarchyService
                .getListOfPositionHeirarchyByFromPositionAndObjectTypeAndSubType(positionId, objectId,
                        complaintTypeCode);

        for (final PositionHierarchy posHir : pageOfEscalation) {
            final EscalationHelper escalationHelper = new EscalationHelper();
            if (posHir.getObjectSubType() != null)
                escalationHelper.setComplaintType(complaintTypeService.findByCode(posHir.getObjectSubType()));

            escalationHelper.setFromPosition(posHir.getFromPosition());
            escalationHelper.setToPosition(posHir.getToPosition());
            escalationHelperList.add(escalationHelper);
        }
        return new StringBuilder("{ \"data\":").append(toJSON(escalationHelperList, EscalationHelper.class, EscalationHelperAdaptor.class)).append("}").toString();
    }
}
