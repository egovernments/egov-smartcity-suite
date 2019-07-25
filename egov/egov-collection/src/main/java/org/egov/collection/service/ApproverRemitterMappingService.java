/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
package org.egov.collection.service;

import org.egov.collection.entity.ApproverRemitterMapping;
import org.egov.collection.repository.ApproverRemitterMappingRepository;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Transactional
@Service
public class ApproverRemitterMappingService {

    @Autowired
    UserService userService;

    @Autowired
    ApproverRemitterMappingRepository mappingRepository;

    private boolean isValid(Long id) {
        return id != null && id > 0;
    }

    private boolean isValid(Boolean c) {
        return c != null;
    }

    public ApproverRemitterMapping findActiveMappingByApprover(Long approverID) {
        List<ApproverRemitterMapping> activeMaps = mappingRepository.findByApproverIdAndIsActive(approverID, true);
        for (ApproverRemitterMapping map : activeMaps) {
            if (map.getIsActive())
                return map;
        }
        return null;
    }

    public List<ApproverRemitterMapping> findAll() {
        return mappingRepository.findAll();
    }

    public ApproverRemitterMapping findByApproverIdAndIsActive(long approverID, Boolean isActive) {
        for (ApproverRemitterMapping map : mappingRepository.findByApproverIdAndIsActive(approverID, isActive)) {
            if (map.getIsActive())
                return map;
        }
        return null;
    }

    public List<ApproverRemitterMapping> searchMappingBySpec(
            ApproverRemitterMappingService.ApproverRemitterMappingSpec searchSpec) {
        if (isValid(searchSpec.approverId) && isValid(searchSpec.remitterId) && isValid(searchSpec.isActive)) {
            ApproverRemitterMapping mapping = mappingRepository.findByApproverIdAndRemitterIdAndIsActive(
                    searchSpec.approverId, searchSpec.remitterId, searchSpec.isActive);
            return mapping == null ? Collections.emptyList() : Collections.singletonList(mapping);
        } else if (isValid(searchSpec.remitterId) && isValid(searchSpec.isActive)) {
            return mappingRepository.findByRemitterIdAndIsActive(
                    searchSpec.remitterId, searchSpec.isActive);
        } else if (isValid(searchSpec.approverId) && isValid(searchSpec.isActive)) {
            return mappingRepository.findByApproverIdAndIsActive(
                    searchSpec.approverId, searchSpec.isActive);
        } else if (isValid(searchSpec.approverId) && isValid(searchSpec.remitterId)) {
            ApproverRemitterMapping mapping = mappingRepository.findByApproverIdAndRemitterId(
                    searchSpec.approverId, searchSpec.remitterId);
            return mapping == null ? Collections.emptyList() : Collections.singletonList(mapping);
        } else if (isValid(searchSpec.approverId)) {
            return mappingRepository.findByApproverId(
                    searchSpec.approverId);
        } else if (isValid(searchSpec.remitterId)) {
            return mappingRepository.findByRemitterId(
                    searchSpec.remitterId);
        } else if (isValid(searchSpec.isActive)) {
            return mappingRepository.findByIsActive(
                    searchSpec.isActive);
        } else {
            return mappingRepository.findAll();
        }
    }

    public boolean validateAndUpdateMapping(ApproverRemitterMappingSpec searchSpec, BindingResult bindingResult) {
        if (searchSpec.id == null) {
            bindingResult.reject("mapping.404", "Mapping not found");
            return false;
        }
        if (searchSpec.approverId == null) {
            bindingResult.rejectValue("approverId", "validate.mapping.approver");
        }
        if (searchSpec.remitterId == null) {
            bindingResult.rejectValue("remitterId", "validate.mapping.remitter");
        }

        ApproverRemitterMapping mapping = mappingRepository.findOne(searchSpec.id);
        if (mapping == null) {
            bindingResult.reject("mapping.404", "Mapping not found");
            return false;
        }

        List<ApproverRemitterMapping> mappingList = mappingRepository.findByApproverIdAndIsActive(searchSpec.approverId, true);
        ApproverRemitterMapping activeMap = null;
        for (ApproverRemitterMapping map : mappingList) {
            if (map.getIsActive()) {
                activeMap = map;
                break;
            }
        }
        if (searchSpec.isActive
                && activeMap != null && (activeMap.getId() != mapping.getId()
                        || activeMap.getRemitter().getId() == searchSpec.remitterId)) {
            bindingResult.reject("validate.mapping.exists", "An active Mapping already exists");
            return false;
        }

        User approver = userService.getUserById(searchSpec.approverId);
        User remitter = userService.getUserById(searchSpec.remitterId);
        if (approver == null) {
            bindingResult.reject("validate.approver.404", "User for approver not found");
            return false;
        }
        if (remitter == null) {
            bindingResult.reject("validate.remitter.404", "User for remitter not found");
            return false;
        }

        mapping.setApprover(approver);
        mapping.setRemitter(remitter);
        mapping.setIsActive(searchSpec.isActive);
        mappingRepository.save(mapping);
        return true;
    }

    private void validateMapSpecForCreate(Long approverId, Long remitterId, Boolean isActive, BindingResult bindingResult) {
        ApproverRemitterMapping mapping;
        if ((mapping = mappingRepository.findByApproverIdAndRemitterId(approverId, remitterId)) != null) {
            if (isActive != null && isActive) {
                bindingResult.reject("validate.mapping.exists",
                        "An Active Mapping already exists with approver " + mapping.getApprover().getName());
            } else if (mapping.getRemitter().getId() == remitterId) {
                bindingResult.reject("validate.mapping.exists",
                        String.format("An mapping already exists with same approver (%s) and remitter (%s)",
                                mapping.getApprover().getName(),
                                mapping.getRemitter().getName()));
            }
        }
    }

    public boolean validateAndCreateMapping(ApproverRemitterMappingSpec spec, BindingResult bindingResult) {
        if (spec.getApproverIdList() == null || spec.getApproverIdList().isEmpty()) {
            bindingResult.rejectValue("approverIdList", "validation.required", "Please select at least one approver");
            return false;
        }
        for (Long approverId : spec.approverIdList)
            validateMapSpecForCreate(approverId, spec.remitterId, spec.isActive, bindingResult);
        if (bindingResult.hasErrors())
            return false;
        List<ApproverRemitterMapping> newMappingList = new ArrayList<>(spec.approverIdList.size());
        for (Long approverId : spec.approverIdList) {
            ApproverRemitterMapping mapping = new ApproverRemitterMapping();
            mapping.setApprover(userService.getUserById(approverId));
            mapping.setRemitter(userService.getUserById(spec.remitterId));
            mapping.setIsActive(spec.isActive);
            newMappingList.add(mapping);
        }
        mappingRepository.save(newMappingList);
        return true;
    }

    public ApproverRemitterMapping findById(Long mappingId) {
        return mappingRepository.findOne(mappingId);
    }

    public Set<User> getActivelyMappedApprover() {
        return mappingRepository.findActiveApprovers();
    }

    /**
     * Lightweight POJO to pass the data between UI and backend
     */
    public static class ApproverRemitterMappingSpec {
        private Long id; // mapping id

        // @NotNull(message = "validate.mapping.approver")
        private Long approverId;

        private List<Long> approverIdList;

        @NotNull(message = "validate.mapping.remitter")
        private Long remitterId;

        @NotNull(message = "validate.mapping.status")
        private Boolean isActive;

        // Support data
        private String approverName;
        private String remitterName;

        public static ApproverRemitterMappingSpec of(ApproverRemitterMapping mapping) {
            ApproverRemitterMappingSpec spec = new ApproverRemitterMappingSpec();
            spec.id = mapping.getId();
            spec.approverId = mapping.getApprover().getId();
            spec.remitterId = mapping.getRemitter().getId();
            spec.isActive = mapping.getIsActive();
            spec.approverName = mapping.getApprover().getName();
            spec.remitterName = mapping.getRemitter().getName();
            return spec;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getApproverId() {
            return approverId;
        }

        public void setApproverId(Long approverId) {
            this.approverId = approverId;
        }

        public List<Long> getApproverIdList() {
            return approverIdList;
        }

        public void setApproverIdList(List<Long> approverIdList) {
            this.approverIdList = approverIdList;
        }

        public Long getRemitterId() {
            return remitterId;
        }

        public void setRemitterId(Long remitterId) {
            this.remitterId = remitterId;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean active) {
            isActive = active;
        }

        public String getApproverName() {
            return approverName;
        }

        public void setApproverName(String approverName) {
            this.approverName = approverName;
        }

        public String getRemitterName() {
            return remitterName;
        }

        public void setRemitterName(String remitterName) {
            this.remitterName = remitterName;
        }

        @Override
        public String toString() {
            return "ApproverRemitterMappingSpec{" +
                    "id=" + id +
                    ", approverId=" + approverId +
                    ", remitterId=" + remitterId +
                    ", isActive=" + isActive +
                    ", *approverName='" + approverName + '\'' +
                    ", *remitterName='" + remitterName + '\'' +
                    '}';
        }
    }
}
