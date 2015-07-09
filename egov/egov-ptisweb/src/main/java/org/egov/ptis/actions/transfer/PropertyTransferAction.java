/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.ptis.actions.transfer;

import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.web.utils.WebUtils;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.service.transfer.TransferOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Results({ @Result(name = BaseFormAction.NEW, location = "transfer/transferProperty-new.jsp"),
        @Result(name = BaseFormAction.EDIT, location = "transfer/transferProperty-edit.jsp"),
        @Result(name = PropertyTransferAction.WORKFLOW_ERROR, location = "workflow/workflow-error.jsp"),
        @Result(name = PropertyTransferAction.ACK, location = "transfer/transferProperty-ack.jsp"),
        @Result(name = PropertyTransferAction.REJECT_ON_TAXDUE, location = "transfer/transferProperty-balance.jsp"),
        @Result(name = PropertyTransferAction.PRINTACK, location = "transfer/transferProperty-printAck.jsp"),
        @Result(name = PropertyTransferAction.PRINTNOTICE, location = "transfer/transferProperty-printNotice.jsp") })
@Namespace("/property/transfer")
public class PropertyTransferAction extends BaseFormAction {

    private static final long serialVersionUID = 1L;
    private static final String WTMS_TAXDUE_RESTURL = "%s/wtms/rest/watertax/due/byptno/%s";
    public static final String ACK = "ack";
    public static final String WORKFLOW_ERROR = "workFlowError";
    public static final String REJECT_ON_TAXDUE = "balance";
    public static final String PRINTACK = "printAck";
    public static final String PRINTNOTICE = "printNotice";

    // Form Binding Model
    private PropertyMutation propertyMutation = new PropertyMutation();

    // Dependent Services
    @Autowired
    @Qualifier("transferOwnerService")
    private TransferOwnerService transferOwnerService;

    // Model and View data
    private Long mutationId;
    private String assessmentNo;
    private String wfErrorMsg;
    private BigDecimal currentPropertyTax;
    private BigDecimal currentPropertyTaxDue;
    private BigDecimal currentWaterTaxDue;
    private BigDecimal arrearPropertyTaxDue;
    private List<DocumentType> documentTypes = new ArrayList<>();
    private BasicProperty basicproperty; //Do not change variable name, struts2 crazy.
    private Integer reportId = -1;

    public PropertyTransferAction() {
        addRelatedEntity("mutationReason", PropertyMutationMaster.class);
    }

    @SkipValidation
    @Action(value = "/new")
    public String showNewTransferForm() {
        if (true)
            return NEW;
        if (basicproperty.isUnderWorkflow()) {
            wfErrorMsg = "Could not do property transfer now, property is undergoing some workflow.";
            return WORKFLOW_ERROR;
        } else {
            final String wtmsRestURL = String.format(WTMS_TAXDUE_RESTURL,
                    WebUtils.extractRequestDomainURL(ServletActionContext.getRequest(), false), assessmentNo);
            currentWaterTaxDue = transferOwnerService.getWaterTaxDues(wtmsRestURL, assessmentNo);
            if (currentWaterTaxDue.add(currentPropertyTaxDue).add(arrearPropertyTaxDue).longValue() > 0)
                return REJECT_ON_TAXDUE;
            else
                return NEW;
        }
    }

    @ValidationErrorPage(value = NEW)
    @Action(value = "/save")
    public String save() {
        transferOwnerService.initiatePropertyTransfer(basicproperty, propertyMutation);
        return ACK;
    }

    @SkipValidation
    @Action(value = "/view")
    public String view() {
        return EDIT;
    }

    @ValidationErrorPage(value = EDIT)
    @Action(value = "/forward")
    public String forward() {
        return ACK;
    }

    @SkipValidation
    @Action(value = "/reject")
    public String reject() {
        return ACK;
    }

    @ValidationErrorPage(value = EDIT)
    @Action(value = "/approve")
    public String approve() {
        transferOwnerService.approvePropertyTransfer(basicproperty, propertyMutation);
        return ACK;
    }

    @Action(value = "/printAck")
    public String printAck() {
        final HttpServletRequest request = ServletActionContext.getRequest();
        final String cityLogo = WebUtils.extractRequestDomainURL(request, false).concat(PropertyTaxConstants.IMAGES_BASE_PATH)
                .concat(request.getSession().getAttribute("citylogo").toString());
        final String cityName = request.getSession().getAttribute("cityname").toString();
        reportId = ReportViewerUtil.addReportToSession(
                transferOwnerService.generateAcknowledgement(basicproperty, propertyMutation, cityName, cityLogo), getSession());
        return PRINTACK;
    }

    @Action(value = "/printNotice")
    public String printNotice() {
        reportId = ReportViewerUtil
                .addReportToSession(transferOwnerService.generateTransferNotice(basicproperty, propertyMutation), getSession());
        return PRINTNOTICE;
    }

    @Override
    public void prepare() {
        if (StringUtils.isNotBlank(assessmentNo))
            basicproperty = transferOwnerService.getBasicPropertyByUpicNo(assessmentNo);

        if (mutationId != null) {
            propertyMutation = transferOwnerService.load(mutationId, PropertyMutation.class);
            basicproperty = propertyMutation.getBasicProperty();
        }
        final Map<String, BigDecimal> propertyTaxDetails = transferOwnerService
                .getCurrentPropertyTaxDetails(basicproperty.getActiveProperty());
        currentPropertyTax = propertyTaxDetails.get(CURR_DMD_STR);
        currentPropertyTaxDue = propertyTaxDetails.get(CURR_DMD_STR).subtract(propertyTaxDetails.get(CURR_COLL_STR));
        arrearPropertyTaxDue = propertyTaxDetails.get(ARR_DMD_STR).subtract(propertyTaxDetails.get(ARR_COLL_STR));
        documentTypes = transferOwnerService.getPropertyTransferDocumentTypes();
        addDropdownData("MutationReason", transferOwnerService.getPropertyTransferReasons());
        super.prepare();
    }

    @Override
    public void validate() {
        if (propertyMutation.getMutationReason() == null || propertyMutation.getMutationReason().getId() == -1)
            addActionError(getText("mandatory.trRsnId"));
        else if (propertyMutation.getMutationReason().getMutationName().equals(PropertyTaxConstants.MUTATIONRS_SALES_DEED)
                && StringUtils.isBlank(propertyMutation.getSaleDetail()))
            addActionError(getText("mandatory.saleDtl"));
        if (propertyMutation.getDeedDate() == null)
            addActionError("Registration Document Date should not be empty");
        if (StringUtils.isBlank(propertyMutation.getDeedNo()))
            addActionError("Registration Document Number should not be empty");
        boolean anyDocIsMandatory = false;
        for (final DocumentType docTypes : documentTypes)
            if (docTypes.isMandatory()) {
                anyDocIsMandatory = true;
                break;
            }

        if (anyDocIsMandatory)
            if (propertyMutation.getDocuments().isEmpty())
                addActionError("Please attach the mandatory documents.");
            else
                for (final Document document : propertyMutation.getDocuments())
                    if (document.getType().isMandatory() && document.getFiles().isEmpty())
                        addActionError("Please upload documents for " + document.getType());

        if (getMutationId() != null) {
            if (propertyMutation.getMutationFee() == null)
                addActionError(getText("mandatory.mutationFee"));
            else if (propertyMutation.getMutationFee().compareTo(BigDecimal.ZERO) < 1)
                addActionError(getText("madatory.mutFeePos"));

            for (final User propOwnerInfo : propertyMutation.getTransfereeInfos())
                if (StringUtils.isBlank(propOwnerInfo.getName()))
                    addActionError(getText("mandatory.ownerName"));
        }

        super.validate();
    }

    public BigDecimal getCurrentPropertyTax() {
        return currentPropertyTax;
    }

    public BigDecimal getCurrentPropertyTaxDue() {
        return currentPropertyTaxDue;
    }

    public BigDecimal getCurrentWaterTaxDue() {
        return currentWaterTaxDue;
    }

    @Override
    public Object getModel() {
        return propertyMutation;
    }

    public String getWfErrorMsg() {
        return wfErrorMsg;
    }

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(final String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

    public BasicProperty getBasicproperty() {
        return basicproperty;
    }

    public List<DocumentType> getDocumentTypes() {
        return documentTypes;
    }

    public Long getMutationId() {
        return mutationId;
    }

    public void setMutationId(final Long mutationId) {
        this.mutationId = mutationId;
    }

    public BigDecimal getArrearPropertyTaxDue() {
        return arrearPropertyTaxDue;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(final Integer reportId) {
        this.reportId = reportId;
    }
}
