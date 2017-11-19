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
package org.egov.ptis.web.controller.transactions.writeOff;

import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.Map;

import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_PENALTY_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_PENALTY_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_PENALTY_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_PENALTY_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.TARGET_WORKFLOW_ERROR;

/**
 * The Class WriteOffController.
 *
 * @author subhash
 */
@Controller
@RequestMapping(value = "/writeOff")
public class WriteOffController extends GenericWorkFlowController {

    private static final String WRITE_OFF_FORM = "writeOff-form";
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private PtDemandDao ptDemandDAO;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    @Qualifier("documentTypePersistenceService")
    private PersistenceService<DocumentType, Long> documentTypePersistenceService;

    @RequestMapping(value = "/form/{assessmentNo}", method = RequestMethod.GET)
    public String form(@PathVariable("assessmentNo") String assessmentNo, Model model) {
        PropertyImpl propertyImpl = null;
        BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        if (basicProperty != null) {
            propertyImpl = basicProperty.getActiveProperty();
            if (null != basicProperty && basicProperty.isUnderWorkflow()) {
                model.addAttribute("wfPendingMsg", "Could not do Write Off now, property is undergoing some work flow.");
                return TARGET_WORKFLOW_ERROR;
            }
            final Map<String, BigDecimal> propertyTaxDetails = ptDemandDAO.getDemandCollMap(basicProperty
                    .getActiveProperty());
            final BigDecimal currTaxDue = propertyTaxDetails.get(CURR_FIRSTHALF_DMD_STR).subtract(
                    propertyTaxDetails.get(CURR_FIRSTHALF_COLL_STR));
            final BigDecimal arrearTaxDue = propertyTaxDetails.get(ARR_DMD_STR).subtract(
                    propertyTaxDetails.get(ARR_COLL_STR));
            final Map<String, BigDecimal> penaltyDetails = ptDemandDAO.getPenaltyDemandCollMap(basicProperty
                    .getActiveProperty());
            final BigDecimal currPenaltyDue = penaltyDetails.get(CURR_PENALTY_DMD_STR).subtract(
                    penaltyDetails.get(CURR_PENALTY_COLL_STR));
            final BigDecimal arrearPenaltyDue = penaltyDetails.get(ARR_PENALTY_DMD_STR).subtract(
                    penaltyDetails.get(ARR_PENALTY_COLL_STR));

            model.addAttribute("property", propertyImpl);
            model.addAttribute("basicProperty", basicProperty);
            model.addAttribute("currTaxDue", currTaxDue);
            model.addAttribute("arrearTaxDue", arrearTaxDue);
            model.addAttribute("currPenaltyDue", currPenaltyDue);
            model.addAttribute("arrearPenaltyDue", arrearPenaltyDue);
            model.addAttribute("isCorporation", propertyTaxUtil.isCorporation());
            model.addAttribute("writeOffReasons", PropertyTaxConstants.WRITEOFF_REASONS);
            model.addAttribute("installments", propertyTaxUtil.getInstallments(propertyImpl));
            model.addAttribute("documentTypes",
                    documentTypePersistenceService.findAllByNamedQuery(DocumentType.DOCUMENTTYPE_BY_TRANSACTION_TYPE,
                            TransactionType.WRITEOFF));
        }
        return WRITE_OFF_FORM;
    }
}
