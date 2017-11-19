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
import org.egov.wtms.masters.repository.PipeSizeRepository;
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
public class PipeSizeService {

    private final PipeSizeRepository pipeSizeRepository;

    @Autowired
    public PipeSizeService(final PipeSizeRepository pipeSizeRepository) {
        this.pipeSizeRepository = pipeSizeRepository;
    }

    public PipeSize findOne(final Long pipeSizeId) {
        return pipeSizeRepository.findOne(pipeSizeId);
    }

    @Transactional
    public PipeSize createPipeSize(final PipeSize pipeSize) {
        final double pipeSizeininch = pipeSize.getSizeInMilimeter() * 0.039370;
        pipeSize.setSizeInInch(Math.round(pipeSizeininch * 1000.0) / 1000.0);
        pipeSize.setActive(true);
        return pipeSizeRepository.save(pipeSize);
    }

    @Transactional
    public void updatePipeSize(final PipeSize pipeSize) {
        pipeSizeRepository.save(pipeSize);
    }

    public List<PipeSize> findAll() {
        return pipeSizeRepository.findAll(new Sort(Sort.Direction.DESC, "code"));
    }

    public PipeSize load(final Long id) {
        return pipeSizeRepository.getOne(id);
    }

    public Page<PipeSize> getListOfPipeSizes(final Integer pageNumber, final Integer pageSize) {
        final Pageable pageable = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "code");
        return pipeSizeRepository.findAll(pageable);
    }

    public PipeSize findByCode(final String code) {
        return pipeSizeRepository.findByCode(code);
    }

    public PipeSize findBySizeInMilimeter(final double sizeInMilimeter) {
        return pipeSizeRepository.findBySizeInMilimeter(sizeInMilimeter);
    }

    public PipeSize findBySizeInInch(final double sizeInInch) {
        return pipeSizeRepository.findBySizeInInch(sizeInInch);
    }

    public List<PipeSize> getAllActivePipeSize() {
        return pipeSizeRepository.findByActiveTrueOrderByCodeAsc();
    }

    public List<PipeSize> getAllPipeSizesByPropertyType(final Long propertyType) {
        return pipeSizeRepository.getAllPipeSizesByPropertyType(propertyType);
    }

    public PipeSize findByCodeAndPipeSizeInmm(final String code, final double sizeInMilimeter) {

        return pipeSizeRepository.findByCodeAndSizeInMilimeter(code, sizeInMilimeter);
    }

    public PipeSize findByCodeIgnoreCase(final String code) {
        return pipeSizeRepository.findByCodeIgnoreCase(code);
    }

    public List<PipeSize> getPipeSizeListForRest() {
        final List<PipeSize> pipeSizeList = pipeSizeRepository.findByActiveTrueOrderByCodeAsc();
        final List<PipeSize> prepareListForRest = new ArrayList<PipeSize>(0);

        for (final PipeSize pipeSize : pipeSizeList) {
            final PipeSize pipeSizeRest = new PipeSize();
            pipeSizeRest.setCode(pipeSize.getCode());
            pipeSizeRest.setSizeInInch(pipeSize.getSizeInInch());
            pipeSizeRest.setSizeInMilimeter(pipeSize.getSizeInMilimeter());
            pipeSizeRest.setActive(pipeSize.isActive());
            prepareListForRest.add(pipeSizeRest);
        }
        return prepareListForRest;
    }

}
