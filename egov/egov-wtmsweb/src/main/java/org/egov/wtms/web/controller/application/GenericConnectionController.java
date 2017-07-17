/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */
package org.egov.wtms.web.controller.application;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.wtms.application.entity.ApplicationDocuments;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.PropertyType;
import org.egov.wtms.masters.entity.WaterSource;
import org.egov.wtms.masters.entity.WaterSupply;
import org.egov.wtms.masters.service.ConnectionCategoryService;
import org.egov.wtms.masters.service.DocumentNamesService;
import org.egov.wtms.masters.service.PipeSizeService;
import org.egov.wtms.masters.service.PropertyTypeService;
import org.egov.wtms.masters.service.UsageTypeService;
import org.egov.wtms.masters.service.WaterSourceService;
import org.egov.wtms.masters.service.WaterSupplyService;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

public abstract class GenericConnectionController extends GenericWorkFlowController {

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

    @Autowired
    private DocumentNamesService documentNamesService;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    protected AssignmentService assignmentService;
    
    @Autowired
    protected WaterSupplyService waterSupplyService;
    
    /*public @ModelAttribute("meterCostMasters") List<MeterCost> meterCostMasters() {
        return meterCostService.findAll();
    }*/

    public @ModelAttribute("waterSourceTypes") List<WaterSource> waterSourceTypes() {
        return waterSourceService.getAllActiveWaterSourceTypes();
    }

    public @ModelAttribute("connectionTypes") Map<String, String> connectionTypes() {
        return waterConnectionDetailsService.getConnectionTypesMap();
    }

    /*public @ModelAttribute("connectionCategories") List<ConnectionCategory> connectionCategories() {
        return connectionCategoryService.getAllActiveConnectionCategory();
    }

    public @ModelAttribute("usageTypes") List<UsageType> usageTypes() {
        return usageTypeService.getActiveUsageTypes();
    }

    public @ModelAttribute("pipeSizes") List<PipeSize> pipeSizes() {
        return pipeSizeService.getAllActivePipeSize();
    }*/

    public @ModelAttribute("propertyTypes") List<PropertyType> propertyTypes() {
        return propertyTypeService.getAllActivePropertyTypes();
    }

    @ModelAttribute("waterSupplyTypes") 
    public List<WaterSupply> supplyTypes(){
        return waterSupplyService.findAllWaterSupplyType();
    }
    
    protected Set<FileStoreMapper> addToFileStore(final MultipartFile[] files) {
        if (ArrayUtils.isNotEmpty(files))
            return Arrays
                    .asList(files)
                    .stream()
                    .filter(file -> !file.isEmpty())
                    .map(file -> {
                        try {
                            return fileStoreService.store(file.getInputStream(), file.getOriginalFilename(),
                                    file.getContentType(), WaterTaxConstants.FILESTORE_MODULECODE);
                        } catch (final Exception e) {
                            throw new ApplicationRuntimeException("Error occurred while getting inputstream", e);
                        }
                    }).collect(Collectors.toSet());
        else
            return null;
    }

    protected void processAndStoreApplicationDocuments(final WaterConnectionDetails waterConnectionDetails) {
        if (!waterConnectionDetails.getApplicationDocs().isEmpty())
            for (final ApplicationDocuments applicationDocument : waterConnectionDetails.getApplicationDocs()) {
                applicationDocument.setDocumentNames(documentNamesService.load(applicationDocument.getDocumentNames()
                        .getId()));
                applicationDocument.setWaterConnectionDetails(waterConnectionDetails);
                applicationDocument.setSupportDocs(addToFileStore(applicationDocument.getFiles()));
            }
    }

    protected boolean validApplicationDocument(final ApplicationDocuments applicationDocument) {
        if (!applicationDocument.getDocumentNames().isRequired() && applicationDocument.getDocumentNumber() == null
                && applicationDocument.getDocumentDate() == null)
            return false;
        return true;
    }

}