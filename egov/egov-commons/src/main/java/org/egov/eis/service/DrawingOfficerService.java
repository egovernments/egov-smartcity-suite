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
package org.egov.eis.service;

import org.egov.commons.service.EntityTypeService;
import org.egov.commons.utils.EntityType;
import org.egov.eis.entity.DrawingOfficer;
import org.egov.eis.repository.DrawingOfficerRepository;
import org.egov.infra.validation.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DrawingOfficerService implements EntityTypeService {

    private final DrawingOfficerRepository drawingOfficerRepository;

    @Autowired
    public DrawingOfficerService(final DrawingOfficerRepository drawingOfficerRepository) {
        this.drawingOfficerRepository = drawingOfficerRepository;
    }
    
    
    public List<DrawingOfficer> getAllDrawingOfficers() {
        return drawingOfficerRepository.findAll();
    }
    
    
    /**
     * Get drawing officer object by id
     * 
     * @param id
     * @return Drawing officer object
     */
    public DrawingOfficer findById(Long id) {
        return drawingOfficerRepository.findOne(id);
    }
    
    /**
     * Get drawing officer object by drawing officer name
     * 
     * @param name
     * @return Drawing officer object
     */
    public DrawingOfficer getDrawingOfficerByName(final String name) {
        return drawingOfficerRepository.findByName(name);
    }

    /**
     * Get drawing officer by drawing officer code
     * 
     * @param code
     * @return Drawing officer object
     */
    public DrawingOfficer getDrawingOfficerByCode(final String code) {
        return drawingOfficerRepository.findByCode(code);
    }
    
    /**
     * Get drawing officer associated with a particular position
     * 
     * @param posId
     * @return Drawing officer object
     */
    public DrawingOfficer getDrawingOfficerByPosition(final Long posId) {
        return drawingOfficerRepository.findByPosition_Id(posId);
    }
    
    /**
     * Get List of drawing officer objects containing given name string
     * 
     * @param name
     * @return List of drawing officer objects
     */
    public List<DrawingOfficer> getListOfDrawingOfficersByNameLike(final String name) {
        return drawingOfficerRepository.findByNameContainingIgnoreCase(name);
    }
    
    /**
     * Get List of drawing officer objects containing given code string
     * 
     * @param code
     * @return List of drawing officer objects
     */
    public List<DrawingOfficer> getListOfDrawingOfficerByCodeLike(final String code) {
        return drawingOfficerRepository.findByCodeContainingIgnoreCase(code);
    }
    
    @Transactional
    public void create(final DrawingOfficer officer) {
        drawingOfficerRepository.save(officer);
    }

    @Transactional
    public void update(final DrawingOfficer officer) {
        drawingOfficerRepository.save(officer);
    }
    
    @Transactional
    public void delete(final DrawingOfficer officer) {
        drawingOfficerRepository.delete(officer);
    }

    @Override
    public List<EntityType> getAllActiveEntities(Integer accountDetailTypeId) {
        final List<EntityType> entities = new ArrayList<EntityType>();
        final List<DrawingOfficer> drawingOfficers = getAllDrawingOfficers();
        entities.addAll(drawingOfficers);
        return entities;
    }

    @Override
    public List<? extends EntityType> filterActiveEntities(String filterKey, int maxRecords, Integer accountDetailTypeId) {
        return drawingOfficerRepository.findByNameLikeOrCodeLike(filterKey + "%", filterKey + "%");
    }

    @Override
    public List getAssetCodesForProjectCode(Integer accountdetailkey) throws ValidationException {
        return null;
    }

    @Override
    public List<EntityType> validateEntityForRTGS(List<Long> idsList) throws ValidationException {
        return null;
    }

    @Override
    public List<EntityType> getEntitiesById(List<Long> idsList) throws ValidationException {
        return null;
    }
}
