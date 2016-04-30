/*******************************************************************************
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
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.bnd.web.actions.common;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bnd.client.utils.BndRuleBook;
import org.egov.bnd.model.AgeType;
import org.egov.bnd.model.AttentionDeathType;
import org.egov.bnd.model.AttentionType;
import org.egov.bnd.model.CRelation;
import org.egov.bnd.model.CrematoriumMaster;
import org.egov.bnd.model.DeliveryMethod;
import org.egov.bnd.model.Disease;
import org.egov.bnd.model.Education;
import org.egov.bnd.model.Establishment;
import org.egov.bnd.model.EstablishmentType;
import org.egov.bnd.model.Occupation;
import org.egov.bnd.model.Religion;
import org.egov.bnd.services.common.BndCommonService;
import org.egov.bnd.services.common.GenerateCertificateService;
import org.egov.bnd.services.masters.RegKeyService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.actions.workflow.GenericWorkFlowAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.pims.commons.Position;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

//import org.egov.mdm.masters.administration.State;

/**
 * This is a common action class defined for bnd module. It supports for
 * implementation of work flow Every action class in bnd module is expected to
 * extend this class.
 *
 * @author pritiranjan
 */

/*@Result(name = BndCommonAction.WORKFLOWERROR, location = "bndCommon", type = "redirectAction", params = { "namespace",
        "/common", "method", BndCommonAction.WORKFLOWERROR })*/
@Namespace("/common")
@ParentPackage("egov")
@Transactional(readOnly = true)
public class BndCommonAction extends GenericWorkFlowAction {

    private static final long serialVersionUID = -5225237259269667373L;
    protected static final String VIEW = "view";
    protected static final String EMPTY = "";
    protected static final String MODIFY = "modify";
    protected static final String NOTMODIFY = "notmodify";
    protected static final String WORKFLOWERROR = "workFlowError";
    protected static final String INBOXERROR = "inboxError";
    protected static final String LOCK = "lock";
    protected static final String UNLOCK = "unlock";
    protected static final String NAMEINCLUSION = "nameinclusion";
    protected String mode;
    public Long idTemp;
    private static final Logger LOGGER = Logger.getLogger(BndCommonAction.class);
    protected BndCommonService bndCommonService;
    protected RegKeyService regKeyService;
    protected GenerateCertificateService generateCertificateService;

    @Override
    public void prepare() {
        super.prepare();
        populate();
    }

    @Override
    public StateAware getModel() {
        return null;
    }
    
    @Transactional
    public String getRoleNameByLoginUserId() {
        if (EgovThreadLocals.getUserId() != null) {
            final List<String> roleList = bndCommonService.getRoleNamesByPassingUserId(Long.valueOf(EgovThreadLocals
                    .getUserId()));

            if (!roleList.isEmpty())
                return BndRuleBook.getInstance().getHighestPrivilegedRole(roleList);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public void populate() {
        // TODO egifix
        // final List<State> statesList =
        // EgovMasterDataCaching.getInstance().get("bnd-state");
        // addDropdownData("stateList", statesList);
        final List<AttentionType> attentionList = EgovMasterDataCaching.getInstance().get("bnd-attentionAtDelivery");
        addDropdownData("attentionList", attentionList);
        final List<Religion> religionList = EgovMasterDataCaching.getInstance().get("bnd-religionType");
        addDropdownData("religionList", religionList);
        final List<DeliveryMethod> deliveryMethodList = EgovMasterDataCaching.getInstance().get("bnd-deliveryType");
        addDropdownData("deliveryMethodList", deliveryMethodList);
        final List<Education> educationMasterList = EgovMasterDataCaching.getInstance().get("bnd-educationType");
        addDropdownData("educationMasterList", educationMasterList);
        final List<Occupation> occupMasterList = EgovMasterDataCaching.getInstance().get("bnd-occupationType");
        addDropdownData("occupMasterList", occupMasterList);
        final List<EstablishmentType> hospitalTypeList = EgovMasterDataCaching.getInstance().get(
                "bnd-establishmentType");
        addDropdownData("hospitalTypeList", hospitalTypeList);
       // final List<Establishment> hospitalList = EgovMasterDataCaching.getInstance().get("bnd-establishmentNames");
        final List<Establishment> hospitalList = Collections.EMPTY_LIST;
        addDropdownData("hospitalList", hospitalList);
        final List<CRelation> relationTypeList = EgovMasterDataCaching.getInstance().get("bnd-relationType");
        addDropdownData("relationTypeList", relationTypeList);
        final List<Establishment> placeTypeList = EgovMasterDataCaching.getInstance().get("bnd-placetype");
        addDropdownData("placeTypeList", placeTypeList);
        final List<Establishment> adoptionInstitueList = EgovMasterDataCaching.getInstance()
                .get("bnd-adoptionInstitue");
        addDropdownData("adoptionInstitueList", adoptionInstitueList);
        //final List<CrematoriumMaster> crematoriumList = EgovMasterDataCaching.getInstance().get("bnd-crematoriumNames");
        final List<CrematoriumMaster> crematoriumList = Collections.EMPTY_LIST;
        addDropdownData("crematoriumList", crematoriumList);
        final List<AgeType> ageTypeList = EgovMasterDataCaching.getInstance().get("bnd-ageType");
        addDropdownData("ageTypeList", ageTypeList);
        final List<AttentionDeathType> attentionDeathList = EgovMasterDataCaching.getInstance().get(
                "bnd-attentionAtDeath");
        addDropdownData("attentionDeathList", attentionDeathList);
        final List<Disease> deathCauseList = EgovMasterDataCaching.getInstance().get("bnd-diseasesType");
        addDropdownData("deathCauseList", deathCauseList);
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public Long getIdTemp() {
        return idTemp;
    }

    public void setIdTemp(final Long idTemp) {
        this.idTemp = idTemp;
    }

    public void prepareCreate() {
        LOGGER.debug("To Prepare a create method");
    }

    @Transactional
    @Action(value = "bndCommon-create", results = {@Result(name = NEW, type = "dispatcher")} )
    public String create() {
        saveOrUpdate();
        mode = VIEW;
        return NEW;
    }

    public void prepareView() {
        LOGGER.debug("To Prepare a view method");
    }

    @SkipValidation
    @Action(value = "bndCommon-view", results = {@Result(name = NEW, type = "dispatcher")} )
    public String view() {
        mode = VIEW;
        return NEW;
    }

    @Transactional
    @ValidationErrorPage(NEW)
    @Action(value = "bndCommon-edit", results = {@Result(name = NEW, type = "dispatcher")} )
    public String edit() {
        saveOrUpdate();
        mode = VIEW;
        return NEW;
    }

    public void prepareEdit() {
        LOGGER.debug("To Prepare a edit method");
    }

    public void prepareNewform() {
        LOGGER.debug("To Prepare a new form method");
    }

    @SkipValidation
    @Action(value = "bndCommon-newform", results = {@Result(name = NEW, type = "dispatcher")} )
    public String newform() {
        return NEW;
    }

    public void prepareBeforeEdit() {
        LOGGER.debug("To Prepare a before edit method");
    }

    @SkipValidation
    @Action(value = "bndCommon-beforeEdit", results = {@Result(name = NEW, type = "dispatcher")} )
    public String beforeEdit() {
        mode = EDIT;
        return NEW;
    }

    @SkipValidation
    @Transactional
    @Action(value = "bndCommon-inbox", results = {@Result(name = NEW, type = "dispatcher")} )
    public String inbox() {
        if (!validateInboxItemForUser(getModel(), EgovThreadLocals.getUserId()))
            return WORKFLOWERROR;
        mode = bndCommonService.isCreatedByLoggedInUser(getModel()) ? MODIFY : NOTMODIFY;
        return NEW;

    }

    public void prepareInbox() {
        LOGGER.debug("Needs to be overridden in Individul action  ");
    }

    /**
     * This method needs to be Overridden by the sub classes
     */

    protected void saveOrUpdate() {
        LOGGER.debug("To implement business logic for Individul action  ");
    }

    /**
     * This method validate whether the work flow object passed is belongs to
     * given user or not If workFlow object is belongs to given user returns
     * boolean value true or boolean value false.
     *
     * @param wfObj
     *            - StateAware object
     * @param userid
     *            - id of User
     * @return boolean value.
     **/

    @Transactional
    public Boolean validateInboxItemForUser(final StateAware wfObj, final Long userId) {
        Boolean validateObjectStatus = Boolean.FALSE;
        if (userId != null && wfObj.getCurrentState() != null && !wfObj.getCurrentState().isEnded()) {
            final List<Position> positionList = bndCommonService.getPositionsForUser(userId, DateUtils.today());
            if (positionList.contains(wfObj.getCurrentState().getOwnerPosition()))
                validateObjectStatus = Boolean.TRUE;
        }
        return validateObjectStatus;
    }

    @Action(value = "bndCommon-workFlowError", results = {@Result(name = INBOXERROR, type = "dispatcher")} )
    public String workFlowError() {
        return INBOXERROR;
    }

    protected String getMessage(final String text) {
        return getText(text);
    }

    public void setBndCommonService(final BndCommonService bndCommonService) {
        this.bndCommonService = bndCommonService;
    }

    public void setGenerateCertificateService(final GenerateCertificateService generateCertificateService) {
        this.generateCertificateService = generateCertificateService;
    }

    public void setRegKeyService(final RegKeyService regKeyService) {
        this.regKeyService = regKeyService;
    }
}
