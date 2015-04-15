package org.egov.infra.admin.master.service;

import java.util.Set;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.repository.UserRepository;
import org.egov.infra.utils.EmailUtils;
import org.egov.infstr.notification.HTTPSMS;
import org.egov.infstr.security.utils.CryptoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmailUtils emailUtils;

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
    public User getUserByEmailId(final String emailId) {
        return userRepository.findByEmailId(emailId);
    }

    public boolean sentPasswordRecovery(String emailOrMobNum) {
        User user;
        boolean hasSent = false;
        if (emailOrMobNum.indexOf('@') != -1) {
            user = getUserByEmailId(emailOrMobNum);
        } else {
            user = getUserByUsername(emailOrMobNum);
        }
        if (user != null) {
            final String pwd = CryptoHelper.decrypt(user.getPassword());
            if (user.getEmailId() != null && !user.getEmailId().isEmpty()) {
                hasSent = emailUtils.sendMail(user.getEmailId(), new StringBuilder(
                        "Hello,\r\n Your login credential is given below \r\n User Name : ")
                        .append(user.getUsername()).append("\r\n Password : ").append(pwd).toString()
                       , "Password Recovery");
            }

            hasSent = HTTPSMS.sendSMS("Your login credential, User Name : " + user.getUsername() + " and Password : "
                    + pwd, "91" + user.getMobileNumber())
                    || hasSent;

        }else{
            hasSent = false;
        }
        return hasSent;
    }
}