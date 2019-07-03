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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.ptis.actions.edit.EditDemandAction;
import org.egov.ptis.bean.demand.DemandDetail;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.*;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.writeOff.WriteOffService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;
import javax.persistence.*;
import javax.servlet.http.*;
import static org.egov.ptis.constants.PropertyTaxConstants.*;

/**
 * The Class WriteOffController.
 *
 * @author
 */
@Controller
@RequestMapping(value = "/writeOff")
public class WriteOffController extends GenericWorkFlowController {

	private static final String WRITE_OFF_FORM = "writeOff-form";
	private static final String IS_NEW = "isNew";
	private static final String COLLECTION = "collection";
	private static final String AMOUNT = "amount";
	private static final Logger LOGGER = Logger.getLogger(EditDemandAction.class);

	private static final String APPROVAL_POSITION = "approvalPosition";
	private static final String APPLICATION_SOURCE = "applicationSource";

	private Boolean loggedUserIsMeesevaUser = Boolean.FALSE;
	private boolean citizenPortalUser = false;
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private BasicPropertyDAO basicPropertyDAO;

	@Autowired
	private PropertyService propertyService;

	@Autowired
	private WriteOffService writeOffService;
	@Autowired
	private SecurityUtils securityUtils;
	@Autowired
	private PropertyTaxCommonUtils propertyTaxCommonUtils;
	@Autowired
	private NoticeService noticeService;

	@Autowired
	private FileStoreService fileStoreService;

	@Autowired
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;

	@Autowired
	private PropertyTaxUtil propertyTaxUtil;
	private List<DemandDetail> demandDetailBeanList = new ArrayList<>();
	private Map<String, String> demandReasonMap = new HashMap<>();

	private static final String ERROR_MSG = "errorMsg";
	private static final String WRITE_OFF = "writeOff";
	private static final String LOGGED_IN_USER = "loggedUserIsEmployee";
	private static final String CITIZEN_PORTAL_USER = "citizenPortalUser";
	private static final String CURRENT_STATE = "currentState";
	private static final String STATE_TYPE = "stateType";
	private static final String ENDRSMNT_NOTICE = "endorsementNotices";
	private static final String WC_FORM = "writeOff-form";
	private static final String PROPERTY = "property";
	private static final String CREATED = "Created";
	private static final String APPROVAL_COMMENT = "approvalComent";
	private static final String WF_ACTION = "workFlowAction";

	@Autowired
	private PtDemandDao ptDemandDAO;

	@ModelAttribute
	public WriteOff writeOff(@PathVariable("assessmentNo") String assessmentNo) {
		WriteOff writeOff = new WriteOff();
		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
		if (basicProperty != null) {
			writeOff.setBasicProperty((BasicPropertyImpl) basicProperty);
			PropertyImpl propertyImpl = (PropertyImpl) basicProperty.getActiveProperty().createPropertyclone();
			writeOff.setProperty(propertyImpl);
		}
		return writeOff;
	}

	@ModelAttribute("documentsList")
	public List<DocumentType> documentsList(@ModelAttribute final WriteOff writeOff) {
		return writeOffService.getDocuments(TransactionType.WRITEOFF);
	}

	@GetMapping(value = "/viewForm/{assessmentNo}")
	public String view(@ModelAttribute WriteOff writeOff, Model model, final HttpServletRequest request) {

		BasicProperty basicProperty = writeOff.getBasicProperty();
		PropertyImpl property = writeOff.getProperty();
		PropertyImpl propertyImpl = basicProperty.getActiveProperty();
		User loggedInUser = securityUtils.getCurrentUser();
		citizenPortalUser = propertyService.isCitizenPortalUser(loggedInUser);
		List<DocumentType> documentTypes;
		documentTypes = propertyService.getDocumentTypesForTransactionType(TransactionType.WRITEOFF);
		if (basicProperty.isUnderWorkflow()) {
			model.addAttribute("wfPendingMsg", "Could not do Write-off now, property is undergoing some work flow.");
			return TARGET_WORKFLOW_ERROR;
		}

		model.addAttribute(WRITE_OFF, writeOff);
		model.addAttribute(PROPERTY, property);
		model.addAttribute(CITIZEN_PORTAL_USER, citizenPortalUser);
		model.addAttribute(CURRENT_STATE, CREATED);
		model.addAttribute(STATE_TYPE, writeOff.getClass().getSimpleName());
		model.addAttribute(ENDRSMNT_NOTICE, new ArrayList<>());
		/*
		 * model.addAttribute("types", PropertyTaxConstants.WRITEOFFTYPES);
		 * model.addAttribute("reasons",
		 * PropertyTaxConstants.FULLWRITEOFFREASONS);
		 * model.addAttribute("councilTypes",
		 * PropertyTaxConstants.COUNCILTYPES);
		 */
		model.addAttribute(LOGGED_IN_USER, propertyService.isEmployee(loggedInUser));
		writeOffService.addModelAttributes(model, basicProperty, request);
		model.addAttribute("documentTypes", documentTypes);

		Set<EgDemandDetails> demandDetails = (ptDemandDAO.getNonHistoryCurrDmdForProperty(basicProperty.getProperty()))
				.getEgDemandDetails();

		List<EgDemandDetails> dmndDetails = new ArrayList<>(demandDetails);
		if (!dmndDetails.isEmpty())
			Collections.sort(dmndDetails, (o1, o2) -> o1.getEgDemandReason().getEgInstallmentMaster()
					.compareTo(o2.getEgDemandReason().getEgInstallmentMaster()));
		model.addAttribute("installments", propertyTaxUtil.getInstallments(propertyImpl));
		model.addAttribute("dmndDetails", demandDetailBeanList);

		prepareWorkflow(model, writeOff, new WorkflowContainer());
		return WC_FORM;
	}

	@PostMapping(value = "/viewForm/{assessmentNo}")
	public String save(@ModelAttribute("courtVerdict") WriteOff writeOff, final BindingResult resultBinder,
			final RedirectAttributes redirectAttributes, final Model model, final HttpServletRequest request,
			@RequestParam String workFlowAction, @PathVariable String assessmentNo) {
		String target = null;
		PropertyImpl property = writeOff.getProperty();
		PropertyImpl oldProperty = writeOff.getBasicProperty().getActiveProperty();

		final Boolean loggedUserIsEmployee = Boolean.valueOf(request.getParameter(LOGGED_IN_USER));
		User loggedInUser = securityUtils.getCurrentUser();
		Map<String, String> errorMessages = new HashMap<>();
		writeOffSource(writeOff, request);

		return target;
	}

	private void writeOffSource(final WriteOff writeOff, final HttpServletRequest request) {
		User loggedInUser = securityUtils.getCurrentUser();
		if (StringUtils.isNotBlank(request.getParameter(APPLICATION_SOURCE))
				&& SOURCE_ONLINE.equalsIgnoreCase(request.getParameter(APPLICATION_SOURCE))
				&& ANONYMOUS_USER.equalsIgnoreCase(loggedInUser.getName())) {
			writeOff.setSource(propertyTaxCommonUtils.setSourceOfProperty(loggedInUser, Boolean.TRUE));
		} else {
			writeOff.setSource(propertyTaxCommonUtils.setSourceOfProperty(loggedInUser, Boolean.FALSE));
		}
	}

}
