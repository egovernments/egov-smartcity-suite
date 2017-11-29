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

import org.egov.wtms.masters.entity.PipeSize;
import org.egov.wtms.masters.entity.PropertyPipeSize;
import org.egov.wtms.masters.entity.PropertyType;
import org.egov.wtms.masters.repository.PropertyPipeSizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PropertyPipeSizeService {

    private final PropertyPipeSizeRepository propertyPipeSizeRepository;

    @Autowired
    public PropertyPipeSizeService(final PropertyPipeSizeRepository propertyPipeSizeRepository) {
        this.propertyPipeSizeRepository = propertyPipeSizeRepository;

    }

    public PropertyPipeSize findOne(final Long propertyPipeSizeId) {
        return propertyPipeSizeRepository.findOne(propertyPipeSizeId);
    }

    @Transactional
    public PropertyPipeSize createPropertyPipeSize(final PropertyPipeSize propertyPipeSize) {
        propertyPipeSize.setActive(true);
        return propertyPipeSizeRepository.save(propertyPipeSize);
    }

    @Transactional
    public void updatePropertyPipeSize(final PropertyPipeSize propertyPipeSize) {
        propertyPipeSizeRepository.save(propertyPipeSize);
    }

    public PropertyPipeSize findByPropertyTypecodeAndPipeSizecode(final String propertyType, final String code) {

        return propertyPipeSizeRepository.findByPropertyType_codeAndPipeSize_code(propertyType, code);
    }

    public PropertyPipeSize findByPropertyTypeAndPipeSizeInmm(final PropertyType propertyType,
            final double sizeInMilimeter) {

        return propertyPipeSizeRepository.findByPropertyTypeAndPipeSize_sizeInMilimeter(propertyType, sizeInMilimeter);
    }

    public PropertyPipeSize findByPropertyTypeAndPipeSizecode(final PropertyType propertyType, final String code) {

        return propertyPipeSizeRepository.findByPropertyTypeAndPipeSize_code(propertyType, code);
    }

    public PropertyPipeSize findByPropertyTypeAndPipeSize(final PropertyType propertyType, final PipeSize pipeSize) {

        return propertyPipeSizeRepository.findByPropertyTypeAndPipeSize(propertyType, pipeSize);
    }

    public List<PropertyPipeSize> findAll() {
        return propertyPipeSizeRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
    }

}
