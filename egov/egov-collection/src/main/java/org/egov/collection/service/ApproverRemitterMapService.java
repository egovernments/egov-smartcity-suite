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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.criteria.Predicate;
import javax.validation.constraints.NotNull;

import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ApproverRemitterMapping;
import org.egov.collection.repository.ApproverRemitterMappingRepository;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Transactional(readOnly = true)
@Service
public class ApproverRemitterMapService {

    public enum ApproverType {
        UNMAPPED, ACTIVELY_MAPPED
    }

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource collMessageSource;

    @Autowired
    private UserService userService;

    @Autowired
    private ApproverRemitterMappingRepository mappingRepository;

    private static final String[] EMPTY_ARGS = {};

    private static boolean isValid(Long id) {
        return id != null && id > 0;
    }

    private static boolean isValid(Boolean isActive) {
        return isActive != null;
    }

    public ApproverRemitterMapping findActiveMappingByApprover(Long approverID) {
        List<ApproverRemitterMapping> activeMaps = mappingRepository.findByApproverIdAndIsActive(approverID, true);
        for (ApproverRemitterMapping map : activeMaps)
            if (map.getIsActive())
                return map;
        return null;
    }

    public List<ApproverRemitterMapping> findAll() {
        return mappingRepository.findAll();
    }

    public ApproverRemitterMapping findByApproverIdAndIsActive(long approverID, Boolean isActive) {
        for (ApproverRemitterMapping map : mappingRepository.findByApproverIdAndIsActive(approverID, isActive))
            if (map.getIsActive())
                return map;
        return null;
    }

    public List<ApproverRemitterMapping> searchMappingBySpec(
            ApproverRemitterSpec searchSpec) {
        return mappingRepository.findAll(searchApproveRemitterMap(searchSpec));
    }

    public static Specification<ApproverRemitterMapping> searchApproveRemitterMap(final ApproverRemitterSpec searchSpec) {
        return (root, query, builder) -> {
            final Predicate predicate = builder.conjunction();
            if (isValid(searchSpec.isActive))
                predicate.getExpressions().add(builder.equal(root.get("isActive"), searchSpec.isActive));
            if (isValid(searchSpec.approverId))
                predicate.getExpressions().add(builder.equal(root.get("approver").get("id"), searchSpec.approverId));
            if (isValid(searchSpec.remitterId))
                predicate.getExpressions().add(builder.equal(root.get("remitter").get("id"), searchSpec.remitterId));
            return predicate;
        };
    }

    private void validateMapObject(ApproverRemitterMapping approverRemitterMap, BindingResult bindingResult) {
        if (approverRemitterMap.getApprover() == null) {
            bindingResult.reject("validate.approver.404",
                    collMessageSource.getMessage("validate.approver.404", EMPTY_ARGS, Locale.getDefault()));
            return;
        }
        if (approverRemitterMap.getRemitter() == null) {
            bindingResult.reject("validate.remitter.404",
                    collMessageSource.getMessage("validate.remitter.404", EMPTY_ARGS, Locale.getDefault()));
            return;
        }

        for (ApproverRemitterMapping map : mappingRepository.findByApprover(approverRemitterMap.getApprover())) {
            // If both Id exists and are equal then mode == MODIFY
            // Skip all map for which id's are same ( for equivalence relation check )
            if (!Objects.equals(map.getId(), approverRemitterMap.getId()) && map.equals(approverRemitterMap)) {
                bindingResult.reject("validate.mapping.exists",
                        collMessageSource.getMessage("validate.mapping.exists", EMPTY_ARGS, Locale.getDefault()));
                break;
            }
        }
    }

    private void validateHelper(ApproverRemitterSpec spec, ApproverRemitterMapping approverRemitterMap,
            BindingResult bindingResult) {
        ApproverRemitterSpec.copyToEntity(approverRemitterMap, spec, userService);
        validateMapObject(approverRemitterMap, bindingResult);
    }

    public boolean validate(ApproverRemitterSpec spec, BindingResult bindingResult) {
        ApproverRemitterMapping approverRemitterMap = spec.id == null ? new ApproverRemitterMapping()
                : mappingRepository.findOne(spec.id);
        if (approverRemitterMap == null) {
            bindingResult.reject("mapping.404");
            return false;
        }

        if (spec.approverIdList == null)
            validateHelper(spec, approverRemitterMap, bindingResult);
        else {
            for (Long approverId : spec.approverIdList) {
                spec.approverId = approverId;
                validateHelper(spec, approverRemitterMap, bindingResult);
            }
        }
        return !bindingResult.hasErrors();
    }

    @Transactional
    public void update(ApproverRemitterSpec spec) {
        ApproverRemitterMapping approverRemitterMap = mappingRepository.findOne(spec.id);
        ApproverRemitterSpec.copyToEntity(approverRemitterMap, spec, userService);
        mappingRepository.save(approverRemitterMap);
    }

    @Transactional
    public void create(ApproverRemitterSpec spec) {
        List<ApproverRemitterMapping> mappingList = new ArrayList<>(spec.approverIdList.size());
        for (Long approverId : spec.approverIdList) {
            spec.approverId = approverId;
            ApproverRemitterMapping approverRemitterMap = ApproverRemitterSpec.copyToEntity(new ApproverRemitterMapping(), spec,
                    userService);
            mappingList.add(approverRemitterMap);
        }
        mappingRepository.save(mappingList);
    }

    public ApproverRemitterMapping findById(Long mappingId) {
        return mappingRepository.findOne(mappingId);
    }

    public Set<User> getActivelyMappedApprover() {
        return mappingRepository.findActiveApprovers();
    }

    public List getApprovers(User remiiter) {
        return mappingRepository.findByRemitterIdAndIsActive(remiiter.getId(), true);
    }

    /**
     * Return both mapped and unmapped set of approvers as a pair. The union of both set is complete set of Approvers.
     * @return Pair of two User set, which are mapped and unmapped approvers
     */
    public Map<ApproverType, Set<User>> getCollectionAprovers() {
        Set<User> unmappedApproverSet = userService.getUsersByRoleName(CollectionConstants.ROLE_COLLECTION_APPROVER);
        Set<User> activelyMappedApproverSet = getActivelyMappedApprover();
        unmappedApproverSet.removeAll(activelyMappedApproverSet);
        Map<ApproverType, Set<User>> result = new EnumMap<>(ApproverType.class);
        result.put(ApproverType.ACTIVELY_MAPPED, activelyMappedApproverSet);
        result.put(ApproverType.UNMAPPED, unmappedApproverSet);
        return result;
    }

    /**
     * Lightweight POJO to pass the data between UI and backend
     */
    public static class ApproverRemitterSpec {
        private Long id; // mapping id

        private Long approverId;

        private List<Long> approverIdList;

        @NotNull(message = "validate.mapping.remitter")
        private Long remitterId;

        @NotNull(message = "validate.mapping.status")
        private Boolean isActive;

        // Support data
        private String approverName;
        private String remitterName;

        public static ApproverRemitterSpec of(ApproverRemitterMapping mapping) {
            ApproverRemitterSpec spec = new ApproverRemitterSpec();
            spec.id = mapping.getId();
            spec.approverId = mapping.getApprover().getId();
            spec.remitterId = mapping.getRemitter().getId();
            spec.isActive = mapping.getIsActive();
            spec.approverName = mapping.getApprover().getName();
            spec.remitterName = mapping.getRemitter().getName();
            return spec;
        }

        public static ApproverRemitterMapping copyToEntity(ApproverRemitterMapping approverRemitterMap, ApproverRemitterSpec spec,
                UserService userService) {
            approverRemitterMap.setIsActive(spec.isActive);
            approverRemitterMap.setApprover(userService.getUserById(spec.approverId));
            approverRemitterMap.setRemitter(userService.getUserById(spec.remitterId));
            return approverRemitterMap;
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