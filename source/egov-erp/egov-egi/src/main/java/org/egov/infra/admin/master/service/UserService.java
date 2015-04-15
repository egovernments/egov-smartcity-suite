package org.egov.infra.admin.master.service;

import java.util.Set;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Set<User> getUsersByUsernameLike(final String userName) {
        return userRepository.findByUsernameContainingIgnoreCase(userName);
    }

    public User getUserById(final Long id) {
        return userRepository.findOne(id);
    }

    public User getUserByUsername(final String userName) {
        return userRepository.findByUsername(userName);
    }
    
    public User updateUser(final User user) {
        return userRepository.saveAndFlush(user);
    }
}