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
package org.egov.ptis.web.controller.transactions.deactivation;

import org.apache.commons.lang3.ArrayUtils;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.PropertyDeactivation;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.service.deactivation.PropertyDeactivationService;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author rafeek
 */
@Controller
@RequestMapping(value = "/deactivation")
public class PropertyDeactivationController extends GenericWorkFlowController {

    private static final String BASIC_PROPERTY = "basicproperty";
    private static final String ORIGINAL_PROPERTY = "originalAssessment";
    private static final String DEACTIVATE_REASONS = "deactivationReasons";
    private static final String DEACT_FORM = "deactivation-form";
    private static final String BOGUS_PROPERTY = "Bogus Property";
    private static final String NO_PROP_ERROR = "error.noproperty";
    private static final String DOCS_LIST = "documentsList";

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private PropertyPersistenceService basicPropertyService;

    @Autowired
    private PropertyDeactivationService propertyDeactivationService;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @ModelAttribute
    public PropertyDeactivation propertyDeactivation() {
        return new PropertyDeactivation();
    }

    @ModelAttribute(DOCS_LIST)
    public List<DocumentType> documentsList(@ModelAttribute final PropertyDeactivation propertyDeactivation) {
        return propertyDeactivationService.getDocuments(TransactionType.DEACTIVATE);
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String deactivationForm(final Model model) {
        List<String> deactivationReasons = propertyDeactivationService.getDeactivationReasons();
        model.addAttribute("propertyDeactivation", new PropertyDeactivation());
        model.addAttribute("deactivationReasons", deactivationReasons);
        return DEACT_FORM;
    }

    @RequestMapping(value = "/search/{assessmentNo}", method =RequestMethod.POST )
    public String deactivationSearchForm(@ModelAttribute final PropertyDeactivation propertyDeactivation,
            final Model model, final BindingResult resultBinder,
            @PathVariable("assessmentNo") final String assessmentNo, final HttpServletRequest request,
            final Errors errors) {
        List<String> deactivationReasons = propertyDeactivationService.getDeactivationReasons();
        List<DocumentType> documentTypes;
        boolean isPropChildUnderWF;
        boolean hasActiveWC;
        model.addAttribute(BASIC_PROPERTY, propertyDeactivation.getBasicproperty());
        model.addAttribute("reasonMaster", propertyDeactivation.getReasonMaster());
        model.addAttribute(ORIGINAL_PROPERTY, propertyDeactivation.getOriginalAssessment());
        model.addAttribute(DEACTIVATE_REASONS, deactivationReasons);

        BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        BasicProperty orgBasicProp = basicPropertyDAO
                .getBasicPropertyByPropertyID(propertyDeactivation.getOriginalAssessment());
        if (basicProperty == null) {
            resultBinder.rejectValue(BASIC_PROPERTY, NO_PROP_ERROR);
            return DEACT_FORM;
        } else {
            isPropChildUnderWF = propertyTaxUtil.checkForParentUsedInBifurcation(basicProperty.getUpicNo());
            if (basicProperty.isUnderWorkflow() || isPropChildUnderWF) {
                resultBinder.rejectValue(BASIC_PROPERTY, "error.prop.under.wf");
                return DEACT_FORM;
            } else if (!BOGUS_PROPERTY.equalsIgnoreCase(propertyDeactivation.getReasonMaster())) {
                if (orgBasicProp == null || basicProperty.getUpicNo().equals(orgBasicProp.getUpicNo())) {
                    resultBinder.rejectValue(ORIGINAL_PROPERTY, NO_PROP_ERROR);
                    return DEACT_FORM;
                } else {
                    Map<String, String> orgPropDetails = propertyDeactivationService.getPropertyDetails(orgBasicProp);
                    List<Map<String, Object>> orgPropWCDetails = propertyService.getWCDetails(orgBasicProp.getUpicNo(),
                            request);
                    model.addAttribute("orgPropDetails", orgPropDetails);
                    model.addAttribute("orgPropWCDetails", orgPropWCDetails);

                }
            } else {
                Map<String, String> propDetails = propertyDeactivationService.getPropertyDetails(basicProperty);
                List<Map<String, Object>> wcDetails = propertyService.getWCDetails(basicProperty.getUpicNo(), request);
                hasActiveWC = propertyDeactivationService.checkActiveWC(wcDetails);
                model.addAttribute("propDetails", propDetails);
                model.addAttribute("wcDetails", wcDetails);
                model.addAttribute("hasActiveWC", hasActiveWC);
            }
        }
        documentTypes = propertyService.getDocumentTypesForTransactionType(TransactionType.DEACTIVATE);
        model.addAttribute("documentTypes", documentTypes);
        return DEACT_FORM;
    }

    @RequestMapping(value = "/update/{assessmentNo}", method = RequestMethod.POST)
    public String deactivationFormSubmit(@ModelAttribute final PropertyDeactivation propertyDeactivation,
            final BindingResult resultBinder, final RedirectAttributes redirectAttrs, final Model model,
            @PathVariable("assessmentNo") final String assessmentNo) {
        BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        final List<Document> documents = new ArrayList<>();
        if (basicProperty == null) {
            resultBinder.rejectValue(BASIC_PROPERTY, NO_PROP_ERROR);
            return DEACT_FORM;
        } else if (basicProperty.isUnderWorkflow()) {
            resultBinder.rejectValue(BASIC_PROPERTY, "error.prop.under.wf");
            return DEACT_FORM;

        } else if (propertyDeactivation.getOriginalAssessment() == null
                && !BOGUS_PROPERTY.equalsIgnoreCase(propertyDeactivation.getReasonMaster())) {
            resultBinder.rejectValue(ORIGINAL_PROPERTY, NO_PROP_ERROR);
            return DEACT_FORM;
        } else {
            documents.addAll(propertyDeactivation.getDocuments());
            propertyDeactivation.getDocuments().clear();
            propertyDeactivation.setDocuments(documents);
            processAndStoreApplicationDocuments(propertyDeactivation);
            PropertyStatusValues propStatusValues = propertyService.createPropStatVal(basicProperty,
                    PropertyTaxConstants.PROP_DEACT_RSN, null, null, null, null, null);
            propertyDeactivation.setBasicproperty(basicProperty.getId());
            propertyDeactivationService.save(propertyDeactivation);
            basicProperty.setActive(false);
            basicProperty.setModifiedDate(new Date());
            basicProperty.addPropertyStatusValues(propStatusValues);
            basicPropertyService.persist(basicProperty);
            model.addAttribute("message", "deactivate.ack.msg");
            model.addAttribute("assessmentNo", assessmentNo);
            return "deactivation-success";
        }
    }

    protected void processAndStoreApplicationDocuments(final PropertyDeactivation dsd) {
        if (!dsd.getDocuments().isEmpty())
            for (final Document applicationDocument : dsd.getDocuments()) {
                applicationDocument
                        .setType(propertyDeactivationService.getDocType(applicationDocument.getType().getName()));
                applicationDocument.setFiles(addToFileStore(applicationDocument.getFile()));
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
}