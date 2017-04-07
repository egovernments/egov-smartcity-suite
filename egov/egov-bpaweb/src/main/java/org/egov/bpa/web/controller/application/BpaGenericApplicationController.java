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
package org.egov.bpa.web.controller.application;

import static org.egov.ptis.constants.PropertyTaxConstants.ADMIN_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.egov.bpa.application.entity.ApplicationDocument;
import org.egov.bpa.application.entity.BpaApplication;
import org.egov.bpa.application.entity.BuildingCategory;
import org.egov.bpa.application.entity.CheckListDetail;
import org.egov.bpa.application.entity.ServiceType;
import org.egov.bpa.application.entity.StakeHolder;
import org.egov.bpa.application.entity.enums.ApplicantMode;
import org.egov.bpa.application.service.CheckListDetailService;
import org.egov.bpa.masters.service.BuildingCategoryService;
import org.egov.bpa.masters.service.ServiceTypeService;
import org.egov.bpa.masters.service.StakeHolderService;
import org.egov.bpa.utils.BpaConstants;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

public abstract class BpaGenericApplicationController extends GenericWorkFlowController {

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private ServiceTypeService serviceTypeService;

    @Autowired
    private StakeHolderService stakeHolderService;

    @Autowired
    private CheckListDetailService checkListDetailService;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
    
    @Autowired
    private BuildingCategoryService buildingCategoryService;

    @ModelAttribute("zones")
    public List<Boundary> zones() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(BpaConstants.ZONE,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("serviceTypeList")
    public List<ServiceType> getServiceTypeList() {
        return serviceTypeService.findAll();
    }
    @ModelAttribute("checkListDetailList")
    public List<CheckListDetail> checkListDetailList() {
        return checkListDetailService.findActiveCheckListByChecklistType(BpaConstants.CHECKLIST_TYPE);
    }
    @ModelAttribute("buildingCategorYlist")
    public List<BuildingCategory> getAllBuildingCategoryList() {
        return buildingCategoryService.findAll();
    }

    @ModelAttribute("stakeHolderList")
    public List<StakeHolder> getStakeHolder() {
        return stakeHolderService.findAll();
    }

    @ModelAttribute("electionwards")
    public List<Boundary> wards() {

        return boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD, ADMIN_HIERARCHY_TYPE);
    }

    @ModelAttribute("wards")
    public List<Boundary> adminWards() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD,
                BpaConstants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("street")
    public List<Boundary> blocks() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(BpaConstants.STREET,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @ModelAttribute("localitys")
    public List<Boundary> localitys() {
        return boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(BpaConstants.LOCALITY,
                        BpaConstants.LOCATION_HIERARCHY_TYPE);
    }

    public @ModelAttribute("applicationModes") Map<String, String> applicationModes() {
        return getApplicationModeMap();
    }

    public Map<String, String> getApplicationModeMap() {
        final Map<String, String> applicationModeMap = new LinkedHashMap<>(0);
        applicationModeMap.put(ApplicantMode.NEW.toString(), ApplicantMode.NEW.name());
        applicationModeMap.put(ApplicantMode.REVISED.name(), ApplicantMode.REVISED.name());
        applicationModeMap.put(ApplicantMode.OTHERS.name(), ApplicantMode.OTHERS.name());
        return applicationModeMap;
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
                                    file.getContentType(), BpaConstants.FILESTORE_MODULECODE);
                        } catch (final Exception e) {
                            throw new ApplicationRuntimeException("Error occurred while getting inputstream", e);
                        }
                    }).collect(Collectors.toSet());
        else
            return null;
    }

    protected void processAndStoreApplicationDocuments(final BpaApplication bpaApplication) {
        if (!bpaApplication.getApplicationDocument().isEmpty())
            for (final ApplicationDocument applicationDocument : bpaApplication.getApplicationDocument()) {
                applicationDocument.setChecklistDetail(checkListDetailService.load(applicationDocument.getChecklistDetail()
                        .getId()));
                applicationDocument.setApplication(bpaApplication);
                applicationDocument.setSupportDocs(addToFileStore(applicationDocument.getFiles()));
            }
    }

}