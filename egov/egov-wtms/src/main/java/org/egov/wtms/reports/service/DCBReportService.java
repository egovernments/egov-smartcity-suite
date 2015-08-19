/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.wtms.reports.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.enums.EmployeeStatus;
import org.egov.wtms.reports.entity.DCBReportView;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DCBReportService {

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

   public List<DCBReportView> searchDCBReportZoneWise(DCBReportView searchParams) {
        
        Criteria criteria  = getCurrentSession().createCriteria(Assignment.class,"assignment").
                createAlias("assignment.employee", "employee");
       /* if(null!=searchParams.getCode() && !searchParams.getCode().isEmpty())
            criteria.add(Restrictions.eq("employee.code",searchParams.getCode()));
        if(null!=searchParams.getName() && !searchParams.getName().isEmpty())
            criteria.add(Restrictions.eq("employee.name",searchParams.getName()));
        if(null!=searchParams.getAadhaar() && !searchParams.getAadhaar().isEmpty())
            criteria.add(Restrictions.eq("employee.aadhaar",searchParams.getAadhaar()));
        if(null!=searchParams.getMobileNumber() && !searchParams.getMobileNumber().isEmpty())
            criteria.add(Restrictions.eq("employee.mobileNumber",searchParams.getMobileNumber()));
        if(null!=searchParams.getPan() && !searchParams.getPan().isEmpty())
            criteria.add(Restrictions.eq("employee.pan",searchParams.getPan()));
        if(null!=searchParams.getEmail() && !searchParams.getEmail().isEmpty())
            criteria.add(Restrictions.eq("employee.emailId",searchParams.getEmail()));
        if(null!=searchParams.getStatus() && !searchParams.getStatus().isEmpty())
            criteria.add(Restrictions.eq("employee.employeeStatus",EmployeeStatus.valueOf(searchParams.getStatus())));
        if(null!=searchParams.getEmployeeType() && !searchParams.getEmployeeType().isEmpty()){
            criteria.createAlias("employee.employeeType", "employeeType");
            criteria.add(Restrictions.eq("employeeType.name",searchParams.getEmployeeType()));
        }
        if(null!=searchParams.getDepartment() && !searchParams.getDepartment().isEmpty()){
            criteria.createAlias("assignment.department", "department");
            criteria.add(Restrictions.eq("department.name",searchParams.getDepartment()));
        }
        if(null!=searchParams.getDesignation() && !searchParams.getDesignation().isEmpty()) {
            criteria.createAlias("assignment.designation", "designation");
            criteria.add(Restrictions.eq("designation.name",searchParams.getDesignation()));
        }
        if(null!=searchParams.getFunctionary() && !searchParams.getFunctionary().isEmpty()) {
            criteria.createAlias("assignment.functionary", "functionary");
            criteria.add(Restrictions.eq("functionary.name",searchParams.getFunctionary()));
        }
        if(null!=searchParams.getFunction() && !searchParams.getFunction().isEmpty()) {
            criteria.createAlias("assignment.function", "function");
            criteria.add(Restrictions.eq("function.name",searchParams.getFunction()));
        }
        */
        final ProjectionList projections = Projections.projectionList().add(Projections.property("assignment.employee"));
        criteria.setProjection(projections);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        return (List<DCBReportView>)criteria.list();
        
    }

   

}
