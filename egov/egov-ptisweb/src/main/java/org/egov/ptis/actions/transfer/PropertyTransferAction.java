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

import static org.egov.ptis.constants.PropertyTaxConstants.WFOWNER;
import static org.egov.ptis.constants.PropertyTaxConstants.WFSTATUS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.service.transfer.TransferOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Results({ @Result(name = BaseFormAction.NEW, location = "transfer/transferProperty-new.jsp"),
    @Result(name = BaseFormAction.EDIT, location = "transfer/transferProperty-edit.jsp"),
    @Result(name = "workFlowError", location = "workflow/workflow-error.jsp"),
    @Result(name = PropertyTransferAction.ACK, location = "transfer/transferProperty-ack.jsp"),
    @Result(name = "balance", location = "transfer/transferProperty-balance.jsp") })
@Namespace("/property/transfer")
public class PropertyTransferAction extends BaseFormAction {

    private static final long serialVersionUID = 1L;

    public static final String ACK = "ack";
    
    // Form Binding Model
    private PropertyMutation propertyMutation = new PropertyMutation();

    // Dependent Services
    @Autowired
    @Qualifier("transferOwnerService")
    private TransferOwnerService transferOwnerService;
    
    
    // Model and View data
    private String indexNumber;
    private String wfErrorMsg;
    private String currentPropertyTax;
    private List<PropertyOwnerInfo> newOwnerInfos = new ArrayList<>();
    private List<DocumentType> documentTypes = new ArrayList<>();
    private PropertyImpl propertyImpl;
    
    public PropertyTransferAction() {
        addRelatedEntity("mutationReason", PropertyMutationMaster.class);
    }
    
    @SkipValidation
    @Action(value = "/new")
    public String showTransferForm() {
        final Map<String, String> currentWFStatus = propertyImpl.getBasicProperty().getPropertyWfStatus();
        if ("TRUE".equalsIgnoreCase(currentWFStatus.get(WFSTATUS))) {
            wfErrorMsg = (String.format("Could not do property transfer now, property is undergoing some workflow in %s's inbox.",
                    getSession().get(WFOWNER)));
            return "workFlowError";
        } else {
            final boolean anyTaxDues = false; // TODO add check Ptax and Wtax for dues
            if (anyTaxDues)
                return "balance";
            else
                return NEW;
        }
    }

    @ValidationErrorPage(value = NEW)
    @Action(value = "/save")
    public String save() {
        transferOwnerService.doPropertyTransfer(propertyMutation, indexNumber, newOwnerInfos);
        return ACK;
    }
    
    @Override
    public void prepare() {
        super.prepare();
        if (StringUtils.isNotBlank(indexNumber))
            propertyImpl = transferOwnerService.getActiveProperty(indexNumber);
        this.currentPropertyTax = transferOwnerService.getCurrentPropertyTax(propertyImpl);
        this.documentTypes = transferOwnerService.getPropertyTransferDocumentTypes();
        addDropdownData("MutationReason", transferOwnerService.getPropertyTransferReasons());
    }

    public String getCurrentPropertyTax() {
        return this.currentPropertyTax;
    }
    
    @Override
    public Object getModel() {
        return propertyMutation;
    }

    public String getWfErrorMsg() {
        return wfErrorMsg;
    }
    
    public String getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(final String indexNumber) {
        this.indexNumber = indexNumber;
    }

    public PropertyImpl getPropertyImpl() {
        return propertyImpl;
    }

    public List<PropertyOwnerInfo> getNewOwnerInfos() {
        return newOwnerInfos;
    }

    public void setNewOwnerInfos(List<PropertyOwnerInfo> newOwnerInfos) {
        this.newOwnerInfos = newOwnerInfos;
    }

    public List<DocumentType> getDocumentTypes() {
        return documentTypes;
    }
}
