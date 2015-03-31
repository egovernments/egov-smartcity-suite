package org.egov.infra.admin.master.repository;

import java.util.Set;

import org.egov.infra.admin.master.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Set<User> findByUsernameContainingIgnoreCase(String userName);

    public User findByUsername(String userName);
}