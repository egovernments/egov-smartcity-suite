/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.tl.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.tl.entity.License;
import org.egov.tl.entity.Validity;
import org.egov.tl.repository.ValidityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ValidityService {

    private final ValidityRepository validityRepository;
   
    @Autowired
    private  FinancialYearHibernateDAO financialYearHibernateDAO;

    @Autowired
    public ValidityService(final ValidityRepository validityRepository) {
        this.validityRepository = validityRepository;
    }
 
    @Transactional
    public Validity create(final Validity validity) {
        return validityRepository.save(validity);
    }

    @Transactional
    public Validity update(final Validity validity) {
        return validityRepository.save(validity);
    }

    public List<Validity> findAll() {
        return validityRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public Validity findOne(final Long id) {
        return validityRepository.findOne(id);
    }

    public List<Validity> search(final Long natureOfBusiness, final Long licenseCategory) {
        if (natureOfBusiness != null && licenseCategory != null)
            return validityRepository.findByNatureOfBusinessIdAndLicenseCategoryId(natureOfBusiness, licenseCategory);
        else if (natureOfBusiness != null)
            return validityRepository.findByNatureOfBusinessId(natureOfBusiness);
        else if (licenseCategory != null)
            return validityRepository.findByLicenseCategoryId(licenseCategory);
        else
            return validityRepository.findAll();
    }
    /**
     * 
     * @param license
     * if the renewal is happening in the previous financial year ie,before expire this code needs to be updated.
     */
    public void applyLicenseValidity(License license)
    {
    	Validity validity=null;
    	List<Validity> validityList = validityRepository.findByNatureOfBusinessIdAndLicenseCategoryId(license.getBuildingType().getId(), license.getTradeName().getCategory().getId());
    	
    	if(validityList!=null && !validityList.isEmpty())
    	{
    		validity=validityList.get(0);
    	}else
    	{
    		validityList=	validityRepository.findByNatureOfBusinessId(license.getBuildingType().getId());
    	}
    	if(validityList!=null && !validityList.isEmpty())
    	{
    		validity=validityList.get(0);
    	}else 
    	{
    		throw new ApplicationRuntimeException("Validity is not defined for the application type");
    	}
    	
    		if(validity.isBasedOnFinancialYear())
    		{
    			CFinancialYear financialYearByDate = financialYearHibernateDAO.getFinancialYearByDate(new Date());
    			license.setDateOfExpiry(financialYearByDate.getEndingDate());
    		}else
    		{
    			Calendar cal=Calendar.getInstance();
    			cal.setTime(new Date());
    			if(validity.getYear()!=null && validity.getYear().compareTo(0)>1)
    			cal.add(Calendar.YEAR,   validity.getYear());
    			if(validity.getMonth()!=null && validity.getMonth().compareTo(0)>1)
        			cal.add(Calendar.MONTH,   validity.getMonth());
    			if(validity.getWeek()!=null && validity.getWeek().compareTo(0)>1)
        			cal.add(Calendar.DATE,   validity.getWeek()*7);
    			if(validity.getDay()!=null && validity.getDay().compareTo(0)>1)
        			cal.add(Calendar.DATE,   validity.getDay());
    			license.setDateOfExpiry(cal.getTime());
    		}
    		
    	}
    
}
