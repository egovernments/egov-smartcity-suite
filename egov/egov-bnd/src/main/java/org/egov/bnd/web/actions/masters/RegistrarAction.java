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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.bnd.web.actions.masters;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bnd.model.Establishment;
import org.egov.bnd.model.Registrar;
import org.egov.bnd.model.RegistrationUnit;
import org.egov.bnd.services.common.BndCommonService;
import org.egov.bnd.services.masters.RegistrarService;
import org.egov.bnd.utils.BndConstants;
import org.egov.bnd.web.actions.common.BndCommonAction;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.workflow.entity.StateAware;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@ParentPackage("egov")
@Validations(requiredFields = {
        @RequiredFieldValidator(fieldName = "userId", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "role", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "regUnitId", message = "", key = BndConstants.REQUIRED) }

        )
@Namespace("/masters")
@Transactional(readOnly = true)
public class RegistrarAction extends BndCommonAction {

    private static final String STRUTS_RESULT_EDIT = "edit";
    private static final long serialVersionUID = -3371819952197320353L;
    Registrar registrar = new Registrar();
    private BndCommonService bndCommonService;
    private RegistrarService registrarService;
    private List<Role> roleList = new ArrayList<Role>();

    public RegistrarAction() {
        addRelatedEntity("userId", User.class);
        addRelatedEntity("role", Role.class);
        addRelatedEntity("establishment", Establishment.class);
        addRelatedEntity("regUnitId", RegistrationUnit.class);
    }

    public BndCommonService getBndCommonService() {
        return bndCommonService;
    }

    @Override
    public void setBndCommonService(final BndCommonService bndCommonService) {
        this.bndCommonService = bndCommonService;
    }

    public RegistrarService getRegistrarService() {
        return registrarService;
    }

    public void setRegistrarService(final RegistrarService registrarService) {
        this.registrarService = registrarService;
    }

    @Override
    public StateAware getModel() {
        return registrar;
    }

    @Override
    public void prepareNewform() {
        buildPrepareNewFormCreate();
        addDropdownData("roleList", Collections.EMPTY_LIST);

    }

    @Transactional
    private void buildRegistrar() {
        registrar.setValid(Boolean.TRUE);
        registrar.setStartDate(new Date());
        registrar.setUserId(registrar.getUserId());
        final List<String> appConfigBnDRoleList = bndCommonService.getAppconfigActualValue(BndConstants.BNDMODULE,
                BndConstants.BND_ROLE);
        setRoleList(bndCommonService.getRoleNamesByUserId(registrar.getUserId().getId(), appConfigBnDRoleList));
        addDropdownData("roleList", getRoleList());
        if (registrar.getEstablishment() != null && !"".equals(registrar.getEstablishment()))
            registrar.setEstablishment(registrar.getEstablishment());
    }

    @Override
    public void prepareCreate() {
        buildPrepareNewFormCreate();
    }

    @Transactional
    private void buildPrepareNewFormCreate() {
        final List<RegistrationUnit> registrationUnitList = bndCommonService.getRegistrationUnit();
        addDropdownData("registrationUnitList", registrationUnitList);
        final List<String> appConfigBnDRoleList = bndCommonService.getAppconfigActualValue(BndConstants.BNDMODULE,
                BndConstants.BND_ROLE);
        final List<User> userNameList = bndCommonService.getUserName(appConfigBnDRoleList);
        addDropdownData("userNameList", userNameList);
        addDropdownData("establishmentList", bndCommonService.getHospitalName());
    }

    @Override
    @Transactional
    @Action(value = "/registrar-create", results = { @Result(name = NEW, type = "dispatcher") })
    public String create() {
        buildRegistrar();
        registrarService.save(registrar);
        mode = VIEW;
        return NEW;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(final List<Role> roleList) {
        this.roleList = roleList;
    }

    @Transactional
    private void buildEditRegistrar() {
        final List<RegistrationUnit> registrationUnitList = bndCommonService.getRegistrationUnit();
        addDropdownData("registrationUnitList", registrationUnitList);

        final List<User> userNameList = bndCommonService.getUserName();
        addDropdownData("userNameList", userNameList);

        addDropdownData("establishmentList", bndCommonService.getHospitalName());

    }

    @Override
    public void prepareBeforeEdit() {
        buildEditRegistrar();
        addDropdownData("roleList", Collections.EMPTY_LIST);
    }

    @Override
    @SkipValidation
    @Action(value = "/registrar-beforeEdit", results = { @Result(name = STRUTS_RESULT_EDIT, type = "dispatcher") })
    public String beforeEdit() {
        return STRUTS_RESULT_EDIT;
    }

    @Override
    @Transactional
    @Action(value = "/registrar-edit", results = { @Result(name = STRUTS_RESULT_EDIT, type = "dispatcher") })
    public String edit() {

        buildEditRegistrar();
        final List<String> appConfigBnDRoleList = bndCommonService.getAppconfigActualValue(BndConstants.BNDMODULE,
                BndConstants.BND_ROLE);
        setRoleList(bndCommonService.getRoleNamesByUserId(registrar.getUserId().getId(), appConfigBnDRoleList));
        addDropdownData("roleList", getRoleList());

        final Registrar existingRegistrar = bndCommonService.getRegistrarByUserId(registrar.getUserId().getId());
        if (registrar.getRegUnitId() != null && !"".equals(registrar.getRegUnitId()))
            existingRegistrar.setRegUnitId(registrar.getRegUnitId());
        if (registrar.getEstablishment() != null && !"".equals(registrar.getEstablishment()))
            existingRegistrar.setEstablishment(registrar.getEstablishment());

        registrarService.save(existingRegistrar);

        mode = VIEW;
        return STRUTS_RESULT_EDIT;
    }
}
