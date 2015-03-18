package org.egov.infra.admin.master.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.exceptions.DuplicateElementException;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.repository.RoleRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public RoleService(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void createRole(final Role role) throws DuplicateElementException {
        final Role checkRole = getRoleByName(role.getName());
        if (checkRole != null)
            throw new DuplicateElementException("The Role by ' " + role.getName() + " ' already exists.");
        roleRepository.save(role);
    }

    @Transactional
    public void update(final Role role) {
        roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(final Long roleID) {
        return roleRepository.findOne(roleID);
    }

    public Role getRoleByName(final String name) {
        return roleRepository.findByName(name);
    }

    public List<Role> getRolesByNameLike(final String name) {
        return roleRepository.findByNameContainingIgnoreCase(name);
    }

    public Role load(final Long id) {
        // FIXME alternative ?
        return (Role) entityManager.unwrap(Session.class).load(Role.class, id);
    }

}
