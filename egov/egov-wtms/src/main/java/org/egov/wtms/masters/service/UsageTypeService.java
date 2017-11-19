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
package org.egov.wtms.masters.service;

import org.egov.wtms.masters.entity.UsageType;
import org.egov.wtms.masters.repository.UsageTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UsageTypeService {

    private final UsageTypeRepository usageTypeRepository;

    @Autowired
    public UsageTypeService(final UsageTypeRepository usageTypeRepository) {
        this.usageTypeRepository = usageTypeRepository;

    }

    public UsageType findBy(final Long usageTypeId) {
        return usageTypeRepository.findOne(usageTypeId);
    }

    @Transactional
    public UsageType createUsageType(final UsageType usageType) {
        usageType.setActive(true);
        return usageTypeRepository.save(usageType);
    }

    @Transactional
    public void updateUsageType(final UsageType usageType) {
        usageTypeRepository.save(usageType);
    }

    public List<UsageType> findAll() {
        return usageTypeRepository.findAll(new Sort(Sort.Direction.DESC, "name"));
    }

    public List<UsageType> findAllByNameLike(final String name) {
        return usageTypeRepository.findByNameContainingIgnoreCase(name);
    }

    public UsageType findByNameIgnoreCase(final String name) {
        return usageTypeRepository.findByNameIgnoreCase(name);
    }

    public UsageType findByCodeIgnoreCase(final String code) {
        return usageTypeRepository.findByCodeIgnoreCase(code);
    }

    public UsageType findByName(final String name) {
        return usageTypeRepository.findByName(name);
    }

    public UsageType load(final Long id) {
        return usageTypeRepository.getOne(id);
    }

    public Page<UsageType> getListOfUsageType(final Integer pageNumber, final Integer pageSize) {
        final Pageable pageable = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "name");
        return usageTypeRepository.findAll(pageable);
    }

    public UsageType findByCode(final String code) {
        return usageTypeRepository.findByCode(code);
    }

    public List<UsageType> getActiveUsageTypes() {
        return usageTypeRepository.findByActiveTrueOrderByNameAsc();
    }

    public List<UsageType> getAllActiveUsageTypesByPropertyType(final Long propertyType) {
        return usageTypeRepository.getAllActiveUsageTypesByPropertyType(propertyType);
    }

    public UsageType findByNameAndCode(final String name, final String code) {
        return usageTypeRepository.findByNameAndCode(name, code);
    }

    public UsageType findOne(final Long usageTypeId) {
        return usageTypeRepository.findOne(usageTypeId);
    }

    public List<UsageType> getUsageTypeListForRest() {
        final List<UsageType> usageTypeList = usageTypeRepository.findByActiveTrueOrderByNameAsc();
        final List<UsageType> prepareListForRest = new ArrayList<UsageType>(0);

        for (final UsageType usageType : usageTypeList) {
            final UsageType usageTypeRest = new UsageType();
            usageTypeRest.setCode(usageType.getCode());
            usageTypeRest.setName(usageType.getName());
            usageTypeRest.setActive(usageType.isActive());
            prepareListForRest.add(usageTypeRest);
        }
        return prepareListForRest;
    }

}
