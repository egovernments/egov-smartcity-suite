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