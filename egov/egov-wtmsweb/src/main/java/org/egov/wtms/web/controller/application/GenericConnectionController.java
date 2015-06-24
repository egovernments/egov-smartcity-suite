/**
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
package org.egov.wtms.web.controller.application;

import java.util.List;
import java.util.Map;

import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.ConnectionCategory;
import org.egov.wtms.masters.entity.PipeSize;
import org.egov.wtms.masters.entity.PropertyType;
import org.egov.wtms.masters.entity.UsageType;
import org.egov.wtms.masters.entity.WaterSource;
import org.egov.wtms.masters.service.ConnectionCategoryService;
import org.egov.wtms.masters.service.PipeSizeService;
import org.egov.wtms.masters.service.PropertyTypeService;
import org.egov.wtms.masters.service.UsageTypeService;
import org.egov.wtms.masters.service.WaterSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

public abstract class GenericConnectionController {

    @Autowired(required = true)
    protected WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    protected UsageTypeService usageTypeService;

    @Autowired
    protected ConnectionCategoryService connectionCategoryService;

    @Autowired
    protected WaterSourceService waterSourceService;

    @Autowired
    protected PipeSizeService pipeSizeService;

    @Autowired
    protected PropertyTypeService propertyTypeService;

    public @ModelAttribute("connectionTypes") Map<String, String> connectionTypes() {
        return waterConnectionDetailsService.getConnectionTypesMap();
    }

    public @ModelAttribute("usageTypes") List<UsageType> usageTypes() {
        return usageTypeService.getActiveUsageTypes();
    }

    public @ModelAttribute("connectionCategories") List<ConnectionCategory> connectionCategories() {
        return connectionCategoryService.getAllActiveConnectionCategory();
    }

    public @ModelAttribute("waterSourceTypes") List<WaterSource> waterSourceTypes() {
        return waterSourceService.getAllActiveWaterSourceTypes();
    }

    public @ModelAttribute("pipeSizes") List<PipeSize> pipeSizes() {
        return pipeSizeService.getAllActivePipeSize();
    }

    public @ModelAttribute("propertyTypes") List<PropertyType> propertyTypes() {
        return propertyTypeService.getAllActivePropertyTypes();
    }

}