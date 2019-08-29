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

import static org.egov.ptis.constants.PropertyTaxConstants.ENDORSEMENT_NOTICE;
import static org.egov.ptis.constants.PropertyTaxConstants.FULL_WRITEOFF;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_VALIDATION_FOR_SPRING;
import static org.egov.ptis.constants.PropertyTaxConstants.TARGET_WORKFLOW_ERROR;
import static org.egov.ptis.constants.PropertyTaxConstants.WO_FORM;
import static org.egov.ptis.constants.PropertyTaxConstants.WO_SUCCESS_FORM;
import static org.egov.ptis.constants.PropertyTaxConstants.WRITEOFF_CODE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.ptis.bean.demand.DemandDetail;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyMutationMasterHibDAO;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.WriteOff;
import org.egov.ptis.domain.entity.property.WriteOffReasons;
import org.egov.ptis.domain.repository.writeOff.WriteOffRepository;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.writeOff.WriteOffService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * The Class WriteOffController.
 *
 * @author
 */
@Controller
@RequestMapping(value = "/writeoff")
public class WriteOffController extends GenericWorkFlowController {

	private static final Logger LOGGER = Logger.getLogger(WriteOffController.class);

	private static final String CURRENT_STATE = "currentState";
	private static final String STATE_TYPE = "stateType";
	private static final String PROPERTY = "property";
	private static final String CREATED = "Created";
	private static final String APPROVAL_COMMENT = "approvalComment";
	private static final String WF_ACTION = "workFlowAction";
	private static final String APPROVAL_POSITION = "approvalPosition";
	private static final String WRITEOFF_REASONS = "reasons";
	private static final String ERROR_MSG ="errorMsg";
	private String WRITOFF_DOC = "attachedDocuments" ;
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private BasicPropertyDAO basicPropertyDAO;
	@Autowired
	private WriteOffService writeOffService;
	@Autowired
	private FileStoreService fileStoreService;
	@Autowired
	private PropertyTaxUtil propertyTaxUtil;
	@Autowired
	private PtDemandDao ptDemandDAO;
	@Autowired
	private PropertyMutationMasterHibDAO propertyMutationMasterHibDAO;
	@Autowired
	private WriteOffRepository writeOffRepo;
	@Autowired
	private PropertyService propertyService;

	@ModelAttribute("writeOff")
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

    @GetMapping(value = "/viewform/{assessmentNo}")
    public String view(@ModelAttribute WriteOff writeOff, Model model, final HttpServletRequest request) {
        BasicProperty basicProperty = writeOff.getBasicProperty();
        PropertyImpl property = writeOff.getProperty();
        BigDecimal balance = BigDecimal.ZERO;
        if (basicProperty.isUnderWorkflow()) {
            model.addAttribute("wfPendingMsg", "Could not do Write-off now, property is undergoing some work flow.");
            return TARGET_WORKFLOW_ERROR;
        }
        if (basicProperty.getActiveProperty().getPropertyDetail().isStructure()) {
            model.addAttribute(ERROR_MSG, "error.superstruc.prop.notallowed");
            return PROPERTY_VALIDATION_FOR_SPRING;
        }
        List<Installment> installmentList = propertyTaxUtil.getInstallments(basicProperty.getActiveProperty());
        model.addAttribute("writeOff", writeOff);
        model.addAttribute(PROPERTY, property);
        model.addAttribute(CURRENT_STATE, CREATED);
        model.addAttribute(STATE_TYPE, writeOff.getClass().getSimpleName());
        model.addAttribute(ENDORSEMENT_NOTICE, new ArrayList<>());
        writeOffService.addModelAttributes(model, writeOff.getBasicProperty().getUpicNo(), request, installmentList);
        model.addAttribute("type", propertyMutationMasterHibDAO
                .getAllPropertyMutationMastersByType(WRITEOFF_CODE));
        Set<EgDemandDetails> demandDetails = (ptDemandDAO.getNonHistoryCurrDmdForProperty(basicProperty.getProperty()))
                .getEgDemandDetails();
        List<EgDemandDetails> dmndDetails = new ArrayList<>(demandDetails);
        if (!dmndDetails.isEmpty())
            dmndDetails = writeOffService.sortDemandDetails(dmndDetails);
        for (EgDemandDetails dmndeatil : dmndDetails) {
            balance = balance.add(dmndeatil.getAmount().subtract(dmndeatil.getAmtCollected()));
        }
        if (balance.compareTo(BigDecimal.ZERO) == 0) {
            model.addAttribute(ENDORSEMENT_NOTICE, new ArrayList<>());
            model.addAttribute(ERROR_MSG, "writeoff.no.due");
            return PROPERTY_VALIDATION_FOR_SPRING;
        }
        List<DemandDetail> demandDetailBeanList = writeOffService.setDemandBeanList(dmndDetails);
        writeOff.setDemandDetailBeanList(demandDetailBeanList);
        model.addAttribute("dmndDetails", demandDetailBeanList);
        prepareWorkflow(model, writeOff, new WorkflowContainer());
        model.addAttribute(WRITOFF_DOC, "");
        return WO_FORM;
    }

    @PostMapping(value = "/viewform/{assessmentNo}")
    public String save(@ModelAttribute("writeOff") @Valid WriteOff writeOff, BindingResult resultBinder,
            final RedirectAttributes redirectAttributes, final Model model, final HttpServletRequest request,
            @RequestParam String workFlowAction, @PathVariable String assessmentNo) {
        String target = null;
        Long approvalPosition = 0l;
        String approvalComent = "";
        String errorMessages = null;
        final List<Document> documents = new ArrayList<>();
        if (request.getParameterValues("checkbox") != null && request.getParameterValues("checkbox").length > 0) {
            writeOff.setPropertyDeactivateFlag(true);
        } else {
            writeOff.setPropertyDeactivateFlag(false);
        }
        if (writeOff.getPropertyDeactivateFlag() && writeOff.getWriteOffType().getCode().equals("FULL WRITEOFF")) {
            errorMessages = validateWCSewconnection(writeOff, request);
            if (!errorMessages.isEmpty()) {
                model.addAttribute(ERROR_MSG, errorMessages);
                model.addAttribute(ENDORSEMENT_NOTICE, new ArrayList<>());
                return WO_FORM;
            }
        }
        resultBinder = validate(writeOff, resultBinder);
        if (resultBinder != null && resultBinder.hasErrors())
            return WO_FORM;
        final PropertyMutationMaster propMutMstr = propertyMutationMasterHibDAO
                .getPropertyMutationMasterByCode(writeOff.getWriteOffType().getCode());
        writeOff.setWriteOffType(propMutMstr);
        List<WriteOffReasons> woreasons = writeOffService.getAllwriteoffMastersByName(writeOff.getWriteOffReasons().getName());
        writeOff.setWriteOffReasons(woreasons.get(0));
        if (request.getParameter(APPROVAL_COMMENT) != null)
            approvalComent = request.getParameter(APPROVAL_COMMENT);
        if (request.getParameter(WF_ACTION) != null)
            workFlowAction = request.getParameter(WF_ACTION);
        if (request.getParameter(APPROVAL_POSITION) != null && !request.getParameter(APPROVAL_POSITION).isEmpty())
            approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));

        List<WriteOff> councilresult = writeOffRepo.findCouncilTypeAndNo(writeOff.getResolutionType(), writeOff.getResolutionNo(),
                writeOff.getBasicProperty().getUpicNo());
        if (councilresult != null && !councilresult.isEmpty()) {
            model.addAttribute(ERROR_MSG,
                    "Could not do Write-off now, Resolution type and resolution no already exist for the given Assessments.");
            model.addAttribute(ENDORSEMENT_NOTICE, new ArrayList<>());
            return WO_FORM;
        }
        if (!writeOff.getDocuments().isEmpty()) {
            documents.addAll(writeOff.getDocuments());
            writeOff.getDocuments().clear();
            writeOff.getDocuments().addAll(documents);
            processAndStoreApplicationDocuments(writeOff);
        }
        writeOffService.saveProperty(writeOff);
        writeOff.getBasicProperty().addProperty(writeOff.getProperty());
        writeOffService.setPtDemandSet(writeOff);
        writeOffService.updateDemandDetails(writeOff);
        writeOffService.saveWriteOff(writeOff, approvalPosition, approvalComent, null, workFlowAction);
        final String successMsg = "write Off Saved Successfully in the System and forwarded to : "
                + propertyTaxUtil.getApproverUserName(writeOff.getState().getOwnerPosition().getId())
                + " with application number : " + writeOff.getApplicationNumber();
        LOGGER.error("Write off saved successfully");
        model.addAttribute("successMessage", successMsg);
        model.addAttribute("showAckBtn", Boolean.TRUE);
        model.addAttribute("propertyId", assessmentNo);
        model.addAttribute(ENDORSEMENT_NOTICE, new ArrayList<>());
        target = WO_SUCCESS_FORM;

        return target;
    }

    protected void processAndStoreApplicationDocuments(final WriteOff writeoff) {
		if (!writeoff.getDocuments().isEmpty())
			for (final Document applicationDocument : writeoff.getDocuments()) {
			    if(applicationDocument.getFile() != null) {
				applicationDocument.setType(writeOffService.getDocType(applicationDocument.getType().getName()));
				applicationDocument.setFiles(addToFileStore(applicationDocument.getFile()));
			    }
			}
	}

	protected Set<FileStoreMapper> addToFileStore(final MultipartFile[] files) {
		if (ArrayUtils.isNotEmpty(files))
			return Arrays.asList(files).stream().filter(file -> !file.isEmpty()).map(file -> {
				try {
					return fileStoreService.store(file.getInputStream(), file.getOriginalFilename(),
							file.getContentType(), PropertyTaxConstants.FILESTORE_MODULE_NAME);
				} catch (final Exception e) {
					throw new ApplicationRuntimeException("err.input.stream", e);
				}
			}).collect(Collectors.toSet());
		else
			return Collections.emptySet();
	}
	
    private BindingResult validate(final WriteOff writeOff, final BindingResult errors) {
        if (StringUtils.isBlank(writeOff.getWriteOffType().getCode()))
            errors.rejectValue("writeOffType.mutationDesc", "writeoff.type");
        if (StringUtils.isBlank(writeOff.getWriteOffReasons().getName()))
            errors.rejectValue("writeOffReasons.name", "writeoff.reason");
        if (StringUtils.isBlank(writeOff.getResolutionNo()))
            errors.rejectValue("resolutionNo", "writeoff.resolution.type");
        if (StringUtils.isBlank(writeOff.getResolutionType()))
            errors.rejectValue("resolutionType", "writeoff.resolution.no");
        return errors;
    }

    public String validateWCSewconnection(WriteOff writeOff, final HttpServletRequest request) {
        boolean hasActiveWC;
        String error = null;
        List<Map<String, Object>> activeWCDetails = propertyService.getWCDetails(writeOff.getBasicProperty().getUpicNo(),
                request);
        hasActiveWC = writeOffService.checkActiveWC(activeWCDetails);
        if (hasActiveWC) {
            error = "Water Connections are active cannot deactivate the property.";
        }
        return error;
    }

}
