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
package org.egov.infra.citizen.service;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.citizen.entity.Citizen;
import org.egov.infra.citizen.repository.CitizenRepository;
import org.egov.infra.utils.EmailUtils;
import org.egov.infstr.notification.HTTPSMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CitizenService {

    @Autowired
    private CitizenRepository citizenRepository;
    
    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private HTTPSMS httpSMS;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Transactional
    public void create(Citizen citizen) throws DuplicateElementException {
        if (getCitizenByUserName(citizen.getMobileNumber()) != null)
            throw new DuplicateElementException("Mobile Number already exists");

        if (StringUtils.isNotBlank(citizen.getEmailId()) && getCitizenByEmailId(citizen.getEmailId()) != null) {
            throw new DuplicateElementException("Email already exists");
        }
        Calendar pwdExpiryDate = Calendar.getInstance();
        pwdExpiryDate.setTime(new Date());
        pwdExpiryDate.add(Calendar.YEAR, 100);
        citizen.setPwdExpiryDate(pwdExpiryDate.getTime());
        citizen.setUsername(citizen.getMobileNumber());
        citizen.setPassword(passwordEncoder.encode(citizen.getPassword()));
        citizen.setActivationCode(RandomStringUtils.random(5, Boolean.TRUE, Boolean.TRUE));
        citizenRepository.save(citizen);
    }

    @Transactional
    public void update(final Citizen citizen) {
        citizenRepository.save(citizen);
    }

    public Citizen getCitizenById(final Long citizenID) {
        return citizenRepository.findOne(citizenID);
    }

    public Citizen getCitizenByEmailId(final String emailId) {
        return citizenRepository.findByEmailId(emailId);
    }

    public Citizen getCitizenByUserName(final String userName) {
        return citizenRepository.findByUsername(userName);
    }


    public void sendActivationMessage(Citizen citizen) throws EGOVRuntimeException {

        boolean hasSent = false;

        if (citizen.getEmailId() != null && !citizen.getEmailId().isEmpty()) {
            hasSent = emailUtils.sendMail(citizen.getEmailId(), "Hello,\r\n Your Portal Activation Code is : "
                    + citizen.getActivationCode(), "Portal Activation");
        }

        hasSent = httpSMS.sendSMS("Your Portal Activation Code is : " + citizen.getActivationCode(),
                "91" + citizen.getMobileNumber())
                || hasSent;

        if (!hasSent) {
            throw new EGOVRuntimeException("Neither email nor mobile activation send.");
        }
    }

}