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

package org.egov.tl.utils;

import org.egov.commons.Installment;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.InstallmentDao;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.tl.config.properties.TlApplicationProperties;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.LicenseSubCategory;
import org.egov.tl.service.LicenseStatusService;
import org.egov.tl.service.LicenseSubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.egov.tl.utils.Constants.TRADELICENSEMODULE;
import static org.egov.tl.utils.Constants.TRADE_LICENSE;
import static org.egov.tl.utils.Constants.NEW_LIC_APPTYPE;
import static org.egov.tl.utils.Constants.RENEWAL_LIC_APPTYPE;
@Service
public class LicenseUtils {
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DesignationService designationService;
    @Autowired
    private InstallmentDao installmentDao;
    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private LicenseSubCategoryService licenseSubCategoryService;

    @Autowired
    private LicenseStatusService licenseStatusService;
    @Autowired
    private TlApplicationProperties tlApplicationProperties;

    public Module getModule(final String moduleName) {
        return moduleService.getModuleByName(moduleName);
    }

    public List<LicenseSubCategory> getAllTradeNames(final String simpleName) {
        return licenseSubCategoryService.getSubCategoriesByLicenseTypeName(simpleName);
    }

    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    public Installment getCurrInstallment(final Module module) {
        return installmentDao.getInsatllmentByModuleForGivenDate(module, new Date());
    }

    public Boolean isDigitalSignEnabled() {
        final AppConfigValues appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                TRADE_LICENSE, Constants.DIGITALSIGNINCLUDEINWORKFLOW).get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }

    public String getDepartmentCodeForBillGenerate() {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                TRADE_LICENSE, "DEPARTMENTFORGENERATEBILL");
        return appConfigValue.isEmpty() ? EMPTY : appConfigValue.get(0).getValue();
    }

    public Position getCityLevelCommissioner() {
        final Department deptObj = departmentService.getDepartmentByName(Constants.ROLE_COMMISSIONERDEPARTEMNT);
        final Designation desgnObj = designationService.getDesignationByName("Commissioner");
        List<Assignment> assignlist = new ArrayList<>();
        if (deptObj != null)
            assignlist = assignmentService.getAssignmentsByDeptDesigAndDates(deptObj.getId(), desgnObj.getId(), new Date(),
                    new Date());
        if (assignlist.isEmpty())
            assignlist = assignmentService.getAllPositionsByDepartmentAndDesignationForGivenRange(null, desgnObj.getId(),
                    new Date());
        if (assignlist.isEmpty())
            assignlist = assignmentService.getAllActiveAssignments(desgnObj.getId());
        return !assignlist.isEmpty() ? assignlist.get(0).getPosition() : null;
    }

    public License applicationStatusChange(final License licenseObj, final String code) {
        licenseObj.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE, code));
        return licenseObj;
    }

    public License licenseStatusUpdate(final License licenseObj, final String code) {
        licenseObj.setStatus(licenseStatusService.getLicenseStatusByCode(code));
        return licenseObj;
    }

    public Integer getSlaForAppType(LicenseAppType licenseAppType) {
        if (NEW_LIC_APPTYPE.equals(licenseAppType.getName()))
            return tlApplicationProperties.getValue("sla.new.apptype");
        else if (RENEWAL_LIC_APPTYPE.equals(licenseAppType.getName()))
            return tlApplicationProperties.getValue("sla.renew.apptype");
        else
            return tlApplicationProperties.getValue("sla.closure.apptype");
    }
}