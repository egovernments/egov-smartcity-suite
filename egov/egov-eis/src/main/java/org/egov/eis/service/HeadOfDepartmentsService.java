package org.egov.eis.service;

import org.egov.eis.entity.HeadOfDepartments;
import org.egov.eis.repository.HeadOfDepartmentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class HeadOfDepartmentsService {

    private HeadOfDepartmentsRepository employeeDepartmentRepository;
    
    @Autowired
    public HeadOfDepartmentsService(final HeadOfDepartmentsRepository employeeDepartmentRepository) {
        this.employeeDepartmentRepository = employeeDepartmentRepository;
    }
    
    @Transactional
    public void create(final HeadOfDepartments headOfDepartments) {
        employeeDepartmentRepository.save(headOfDepartments);
    }
    
    @Transactional
    public void update(final HeadOfDepartments headOfDepartments) {
        employeeDepartmentRepository.save(headOfDepartments);
    }
    
    @Transactional
    public void delete(final HeadOfDepartments headOfDepartments) {
        employeeDepartmentRepository.delete(headOfDepartments);
    }
    
    /**
     * Returns true if the given employee is an HOD
     *
     * @param assignId
     * @return true if HOD else false
     */
    public Boolean isHod(final Long assignId) {
        final List<HeadOfDepartments> hodList = employeeDepartmentRepository.getAllHodDepartments(assignId);
        return !hodList.isEmpty();
    }
}
