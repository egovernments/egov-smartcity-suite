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
package org.egov.ptis.web.controller.transactions.digitalsignature;

import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.inbox.InboxRenderServiceDeligate;
import org.egov.infra.workflow.service.WorkflowTypeService;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;

@Controller
@RequestMapping(value = "/digitalSignature")
public class DigitalSignatureReportController {

    private static final String DIGITAL_SIGNATURE_REPORT_FORM = "digitalSignatureReport-form";

    @Autowired
    private InboxRenderServiceDeligate<StateAware> inboxRenderServiceDeligate;

    @Autowired
    private WorkflowTypeService workflowTypeService;

    @RequestMapping(value = "/digitalSignatureReport-form", method = RequestMethod.GET)
    public String searchForm(final Model model) {
        final List<HashMap<String, Object>> resultList = getRecordsForDigitalSignature();
        model.addAttribute("digitalSignatureReportList", resultList);
        model.addAttribute("noticeType", PropertyTaxConstants.NOTICE_TYPE_SPECIAL_NOTICE);
        return DIGITAL_SIGNATURE_REPORT_FORM;
    }

    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> getRecordsForDigitalSignature() {
        final List<HashMap<String, Object>> resultList = new ArrayList<>();
        final List<StateAware> stateAwareList = fetchItems();

        if (null != stateAwareList && !stateAwareList.isEmpty()) {
            HashMap<String, Object> tempMap;
            for (final StateAware record : stateAwareList)
                if (record != null && record.getState() != null && record.getState().getNextAction() != null &&
                        record.getState().getNextAction().equalsIgnoreCase(PropertyTaxConstants.DIGITAL_SIGNATURE_PENDING)) {
                    tempMap = new HashMap<>();
                    WorkflowTypes workflowTypes = workflowTypeService.getEnabledWorkflowTypeByType(record.getStateType());
                    if (PTMODULENAME.equalsIgnoreCase(workflowTypes.getModule().getName())) {
                        if (record.getState().getValue().startsWith("Create")
                                || record.getState().getValue().startsWith("Alter")
                                || record.getState().getValue().startsWith("Bifurcate")
                                || record.getState().getValue().startsWith("Demolition")
                                || record.getState().getValue().startsWith("Exemption"))
                            tempMap.put("objectId", ((PropertyImpl) record).getBasicProperty().getId());
                        else
                            tempMap.put("objectId", record.getId());
                        tempMap.put("type", record.getState().getNatureOfTask());
                        tempMap.put("module", workflowTypes.getModule().getDisplayName());
                        tempMap.put("details", record.getStateDetails());
                        tempMap.put("status", record.getCurrentState().getValue());
                        resultList.add(tempMap);
                    }
                }
        }
        return resultList;
    }

    public List<StateAware> fetchItems() {
        return new ArrayList<>(inboxRenderServiceDeligate.getInboxItems());
    }

}