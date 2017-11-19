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

import org.egov.wtms.masters.entity.ConnectionCategory;
import org.egov.wtms.masters.repository.ConnectionCategoryRepository;
import org.egov.wtms.utils.constants.WaterTaxConstants;
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
public class ConnectionCategoryService {

    private final ConnectionCategoryRepository connectionCategoryRepository;

    @Autowired
    public ConnectionCategoryService(final ConnectionCategoryRepository connectionCategoryRepository) {
        this.connectionCategoryRepository = connectionCategoryRepository;
    }

    public ConnectionCategory findOne(final Long connectionCategoryId) {
        return connectionCategoryRepository.findOne(connectionCategoryId);
    }

    @Transactional
    public ConnectionCategory createConnectionCategory(final ConnectionCategory connectionCategory) {
        connectionCategory.setActive(true);
        return connectionCategoryRepository.save(connectionCategory);
    }

    @Transactional
    public void updateConnectionCategory(final ConnectionCategory connectionCategory) {
        connectionCategoryRepository.save(connectionCategory);
    }

    public List<ConnectionCategory> findAll() {
        return connectionCategoryRepository.findAll(new Sort(Sort.Direction.DESC, "name"));
    }

    public ConnectionCategory findByNameIgnoreCase(final String name) {
        return connectionCategoryRepository.findByNameIgnoreCase(name);
    }

    public ConnectionCategory findByCodeIgnoreCase(final String code) {
        return connectionCategoryRepository.findByCodeIgnoreCase(code);
    }

    public List<ConnectionCategory> findAllByNameLike(final String name) {
        return connectionCategoryRepository.findByNameContainingIgnoreCase(name);
    }

    public ConnectionCategory findByName(final String name) {
        return connectionCategoryRepository.findByName(name);
    }

    public ConnectionCategory findByNameAndCode(final String name, final String code) {
        return connectionCategoryRepository.findByNameAndCode(name, code);
    }

    public Page<ConnectionCategory> getListOfConnectionCategory(final Integer pageNumber, final Integer pageSize) {
        final Pageable pageable = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "name");
        return connectionCategoryRepository.findAll(pageable);
    }

    public ConnectionCategory findByCode(final String code) {
        return connectionCategoryRepository.findByCode(code);
    }

    public List<ConnectionCategory> getAllActiveConnectionCategory() {
        return connectionCategoryRepository.findByActiveTrueOrderByNameAsc();
    }

    public List<ConnectionCategory> getAllActiveCategoryTypesByPropertyType(final Long propertyType,
            final String connectionType) {
        if (connectionType.equals(WaterTaxConstants.ADDNLCONNECTION))
            return connectionCategoryRepository.getAllCategoryTypesByPropertyTypeNotInBPL(propertyType);
        else
            return connectionCategoryRepository.getAllActiveCategoryTypesByPropertyType(propertyType);
    }

    public List<ConnectionCategory> getConnectionCategoryListForRest() {
        final List<ConnectionCategory> connectionCategoryList = connectionCategoryRepository
                .findByActiveTrueOrderByNameAsc();
        final List<ConnectionCategory> prepareListForRest = new ArrayList<ConnectionCategory>(0);

        for (final ConnectionCategory connectionCategory : connectionCategoryList) {
            final ConnectionCategory connectionCategoryRest = new ConnectionCategory();
            connectionCategoryRest.setCode(connectionCategory.getCode());
            connectionCategoryRest.setName(connectionCategory.getName());
            connectionCategoryRest.setActive(connectionCategory.isActive());
            prepareListForRest.add(connectionCategoryRest);
        }
        return prepareListForRest;
    }

}
