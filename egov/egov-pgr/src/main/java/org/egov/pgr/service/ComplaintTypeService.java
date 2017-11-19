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

package org.egov.pgr.service;

import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.web.support.search.DataTableSearchRequest;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.repository.ComplaintTypeRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ComplaintTypeService {

    private static final String COMPLAINT_COMPLAINT_TYPE = "complaint.complaintType";
    private final ComplaintTypeRepository complaintTypeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ComplaintTypeService(ComplaintTypeRepository complaintTypeRepository) {
        this.complaintTypeRepository = complaintTypeRepository;
    }

    public ComplaintType findBy(Long complaintTypeId) {
        return complaintTypeRepository.findOne(complaintTypeId);
    }

    @Transactional
    public ComplaintType createComplaintType(ComplaintType complaintType) {
        return complaintTypeRepository.save(complaintType);
    }

    @Transactional
    public ComplaintType updateComplaintType(ComplaintType complaintType) {
        return complaintTypeRepository.save(complaintType);
    }

    public List<ComplaintType> findAll() {
        return complaintTypeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public List<ComplaintType> findAllActiveByNameLike(String name) {
        return complaintTypeRepository.findByIsActiveTrueAndNameContainingIgnoreCase(name);
    }

    public List<ComplaintType> findActiveComplaintTypesByCategory(Long categoryId) {
        return complaintTypeRepository.findByIsActiveTrueAndCategoryIdOrderByNameAsc(categoryId);
    }

    public ComplaintType findByName(String name) {
        return complaintTypeRepository.findByName(name);
    }

    public ComplaintType load(Long id) {
        return complaintTypeRepository.getOne(id);
    }

    @ReadOnly
    public Page<ComplaintType> getComplaintType(DataTableSearchRequest request) {
        return complaintTypeRepository.findAll(new PageRequest(request.pageNumber(), request.pageSize()));
    }

    /**
     * List top 5 complaint types filed in last one month
     *
     * @return complaint Type list
     */
    @ReadOnly
    public List<ComplaintType> getFrequentlyFiledComplaints() {

        DateTime previousDate = new DateTime();
        DateTime currentDate = new DateTime();
        previousDate = previousDate.minusMonths(1);

        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Complaint.class, "complaint");
        criteria.createAlias(COMPLAINT_COMPLAINT_TYPE, "compType");
        criteria.setProjection(Projections.projectionList().add(Projections.property(COMPLAINT_COMPLAINT_TYPE))
                .add(Projections.count(COMPLAINT_COMPLAINT_TYPE).as("count"))
                .add(Projections.groupProperty(COMPLAINT_COMPLAINT_TYPE)));
        criteria.add(Restrictions.between("complaint.createdDate", previousDate.toDate(), currentDate.toDate()));
        criteria.add(Restrictions.eq("compType.isActive", Boolean.TRUE));
        criteria.setMaxResults(5).addOrder(Order.desc("count"));
        List<Object> resultList = criteria.list();
        List<ComplaintType> complaintTypeList = new ArrayList<>();

        for (Object row : resultList) {
            Object[] columns = (Object[]) row;
            complaintTypeList.add((ComplaintType) columns[0]);
        }
        return complaintTypeList;

    }

    public ComplaintType findByCode(final String code) {

        return complaintTypeRepository.findByCode(code);
    }

    public List<ComplaintType> findActiveComplaintTypes() {
        return complaintTypeRepository.findByIsActiveTrueOrderByNameAsc();
    }

    public List<String> getActiveComplaintTypeCode() {
        return findActiveComplaintTypes()
                .stream()
                .map(ComplaintType::getCode)
                .collect(Collectors.toList());
    }

    public List<ComplaintType> getComplaintTypeByDepartmentId(Long departmentId) {
        return departmentId == null ? findAll() : complaintTypeRepository.findByDepartmentId(departmentId);
    }
}
