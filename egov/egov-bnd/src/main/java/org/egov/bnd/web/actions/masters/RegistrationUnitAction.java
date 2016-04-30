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
import org.egov.bnd.model.EstablishmentType;
import org.egov.bnd.model.RegistrationUnit;
import org.egov.bnd.services.masters.RegistrationUnitService;
import org.egov.bnd.utils.BndConstants;
import org.egov.bnd.web.actions.common.BndCommonAction;
import org.egov.infra.persistence.entity.Address;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;


@Validations(requiredFields = {
        @RequiredFieldValidator(fieldName = "regUnitDesc", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "regUnitConst", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "regUnitAddress.streetRoadLine", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "regUnitAddress.state", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "districtName", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "talukName", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "cityName", message = "", key = BndConstants.REQUIRED),
        @RequiredFieldValidator(fieldName = "regUnitAddress.pinCode", message = "", key = BndConstants.REQUIRED) }

)
@Namespace("/masters")
@ParentPackage("egov")
@Transactional(readOnly = true)
public class RegistrationUnitAction extends BndCommonAction {

    private static final long serialVersionUID = -1258956866879658003L;
    private final RegistrationUnit registrationUnit = new RegistrationUnit();
    private RegistrationUnitService registrationUnitService;
    private String isMainRegunitValue;

    // Place holder to capture temporary variables
    private String districtName;
    private String talukName;
    private String cityName;

    public void setRegistrationUnitService(final RegistrationUnitService registrationUnitService) {
        this.registrationUnitService = registrationUnitService;
    }

    public String getIsMainRegunitValue() {
        return isMainRegunitValue;
    }

    public void setIsMainRegunitValue(final String isMainRegunitValue) {
        this.isMainRegunitValue = isMainRegunitValue;
    }

    public RegistrationUnitAction() {
        addRelatedEntity("type", EstablishmentType.class);
        addRelatedEntity("address", Address.class);
    }

    @Override
    public RegistrationUnit getModel() {
        return registrationUnit;
    }

    @Override
    public void prepareNewform() {
        buildPrepareNewFormCreate();
        addDropdownData("talukList", Collections.EMPTY_LIST);
        addDropdownData("cityList", Collections.EMPTY_LIST);
    }

    private void buildPrepareNewFormCreate() {
        bndCommonService.getAppconfigActualValue(BndConstants.BNDMODULE, BndConstants.STATENAME);
    }

    @Override
    public void prepareCreate() {
        buildPrepareNewFormCreate();
        addDropdownData("talukList", Collections.EMPTY_LIST);
        addDropdownData("cityList", Collections.EMPTY_LIST);
    }

    @Override
    @Transactional
    @Action(value = "/registrationUnit-create", results = { @Result(name = NEW, type = "dispatcher") })
    public String create() {
        buildRegistrationUnit();
        registrationUnitService.save(registrationUnit);
        // TODO egifix
        /*
         * addDropdownData("districtList",
         * bndCommonService.getDistrictByStateId(
         * Integer.valueOf(registrationUnit.getRegUnitAddress().getState())));
         * addDropdownData("talukList",
         * bndCommonService.getTalukByDistrictId(Integer
         * .valueOf(districtName)));
         */
        addDropdownData("cityList", bndCommonService.getCityByTalukId(Integer.valueOf(talukName)));
        mode = VIEW;
        return NEW;
    }

    private void buildRegistrationUnit() {
        registrationUnit.setIsmainregunit(registrationUnit.getIsmainregunit());
        if (registrationUnit.getIsmainregunit() != null)
            if (registrationUnit.getIsmainregunit().equals(Boolean.TRUE))
                setIsMainRegunitValue(String.valueOf(1));
            else
                setIsMainRegunitValue(String.valueOf(0));
    }

    @Override
    protected String getMessage(final String key) {
        return getText(key);
    }

    @Override
    @Transactional
    public void validate() {
        if (registrationUnit.getRegUnitDesc() != null && !"".equals(registrationUnit.getRegUnitDesc())) {
            final boolean isDescAlreadyExist = registrationUnitService.checkUniqueRegUnitDesc(registrationUnit
                    .getRegUnitDesc());
            if (isDescAlreadyExist)
                addFieldError("descAlreadyExist", getMessage("registration.desc.alreadyExist"));
        }
        if (registrationUnit.getRegUnitConst() != null && !"".equals(registrationUnit.getRegUnitConst())) {
            final boolean isConstAlreadyExist = registrationUnitService.checkUniqueRegUnitConst(registrationUnit
                    .getRegUnitConst());
            if (isConstAlreadyExist)
                addFieldError("constAlreadyExist", getMessage("registration.const.alreadyExist"));
        }
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(final String districtName) {
        this.districtName = districtName;
    }

    public String getTalukName() {
        return talukName;
    }

    public void setTalukName(final String talukName) {
        this.talukName = talukName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(final String cityName) {
        this.cityName = cityName;
    }

}
