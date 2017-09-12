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
package org.egov.ptis.master.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.ptis.domain.dao.property.PropertyUsageDAO;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.repository.master.usage.PropertyUsageRepository;
import org.egov.ptis.report.bean.PropertyUsageSearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for PropertyUsage
 * 
 * @author nayeem
 */
@Service
public class PropertyUsageService {

    private final PropertyUsageDAO propertyUsageHibernateDAO;

    @Autowired
    private UserService userService;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private PropertyUsageRepository propertyUsageRepository;

    @Autowired
    public PropertyUsageService(final PropertyUsageDAO propertyUsageHibernateDAO) {
        this.propertyUsageHibernateDAO = propertyUsageHibernateDAO;
    }

    public PropertyUsage create(PropertyUsage propertyUsage, Long Id) {

        propertyUsage.setLastModifiedDate(new Date());
        propertyUsage.setCreatedDate(new Date());
        propertyUsage.setIsActive(true);
        propertyUsage.setIsEnabled(1);
        final User createdBy = userService.getUserById(ApplicationThreadLocals.getUserId());
        propertyUsage.setCreatedBy(createdBy);
        propertyUsage.setLastModifiedBy(createdBy);
        propertyUsageHibernateDAO.create(propertyUsage);

        return propertyUsage;
    }
    
    @ReadOnly
    public List<PropertyUsage> getAllActivePropertyUsages() {
        return propertyUsageHibernateDAO.getAllActivePropertyUsage();
    }
    
    @ReadOnly
    public PropertyUsage findById(Long id) {
        return propertyUsageHibernateDAO.findById(id, false);
    }

    public String getRolesForUserId(final Long userId) {
        String roleName;
        final List<String> roleNameList = new ArrayList<String>();
        final User user = userService.getUserById(userId);
        for (final Role role : user.getRoles()) {
            roleName = role.getName() != null ? role.getName() : "";
            roleNameList.add(roleName);
        }
        return roleNameList.toString().toUpperCase();
    }
    
    @ReadOnly
    public List<PropertyUsageSearchResult> getPropertyUsageByTypeUsgAndfromDate(final PropertyUsage propertyUsage) {
        StringBuffer queryString =  new StringBuffer(200);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        List<PropertyUsageSearchResult> propertyUsageSearchList= new ArrayList<PropertyUsageSearchResult>();
        queryString.append(" from PropertyUsage PU where PU.isEnabled = 1 and PU.isResidential = :isResidential ");
        if(StringUtils.isNotBlank(propertyUsage.getUsageName())) {
            queryString.append(" and upper(PU.usageName) like :usageName ");
        }
        if(propertyUsage.getFromDate() != null ) {
            queryString.append(" and PU.fromDate >= :fromDate ");
        }
        queryString.append(" order by usageName ");
        final Query qry = entityManager.createQuery(queryString.toString());
        qry.setParameter("isResidential", propertyUsage.getIsResidential());
        if(StringUtils.isNotBlank(propertyUsage.getUsageName())) {
            qry.setParameter("usageName", "%"+propertyUsage.getUsageName().toUpperCase()+"%");
        }
        if(propertyUsage.getFromDate() != null ) {
            qry.setParameter("fromDate",propertyUsage.getFromDate());
        }
        List<PropertyUsage> propertyUsageList = qry.getResultList();
        
        for(PropertyUsage propertyUsageObj : propertyUsageList) {
            PropertyUsageSearchResult propertyUsageSearchObj = new PropertyUsageSearchResult();
            propertyUsageSearchObj.setUsageName(propertyUsageObj.getUsageName());
            propertyUsageSearchObj.setUsageType(propertyUsageObj.getIsResidential() ? "Residential" : "Non-Residential");
            propertyUsageSearchObj.setFromDate(dateFormat.format(propertyUsageObj.getFromDate()));
            propertyUsageSearchObj.setToDate(dateFormat.format(propertyUsageObj.getToDate()));
            propertyUsageSearchList.add(propertyUsageSearchObj);
        }
        
        return propertyUsageSearchList;
    }
    
    @ReadOnly
    public List<PropertyUsage> getResidentialPropertyUsages(){
        return propertyUsageRepository.findByIsResidentialTrueAndIsActiveTrueOrderByUsageName();
    }
    
    @ReadOnly
    public List<PropertyUsage> getNonResidentialPropertyUsages(){
        return propertyUsageRepository.findByIsResidentialFalseAndIsActiveTrueOrderByUsageName();
    }
    
    @Transactional
    public void updateUsage(final PropertyUsage propertyUsage) {
        propertyUsageRepository.save(propertyUsage);
    }

    public List<String> validateModifyPropertyUsage(final PropertyUsage propertyUsage) {
        final List<String> errors = new ArrayList<>();
        if (!propertyUsageRepository.findByCodeAndNotInId(propertyUsage.getUsageCode().toUpperCase(), propertyUsage.getId())
                .isEmpty())
            errors.add("error.duplicate.code");
        else if (!propertyUsageRepository.findByNameAndNotInId(propertyUsage.getUsageName().toUpperCase(), propertyUsage.getId())
                .isEmpty())
            errors.add("error.duplicate.usage");
        else if (!propertyUsageRepository.findByUsageUnitRateActive(propertyUsage.getId()).isEmpty()
                && !propertyUsage.getIsActive())
            errors.add("error.active.unitrates.exist");
        return errors;
    }
    
    public Boolean isActiveUsage(String code){
    	return propertyUsageRepository.findIsActiveByCode(code);
    }
    public PropertyUsage  getUsageByCode(String code){
    	return propertyUsageRepository.findUsageByCode(code);
    }
    
    @ReadOnly
    public List<PropertyUsage> getAllActiveMixedPropertyUsages(){
        return propertyUsageRepository.findByIsActiveTrueOrderByUsageName();
    }
}
