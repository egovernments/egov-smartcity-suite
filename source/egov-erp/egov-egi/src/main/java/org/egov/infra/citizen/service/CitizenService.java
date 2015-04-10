package org.egov.infra.citizen.service;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.citizen.entity.Citizen;
import org.egov.infra.citizen.repository.CitizenRepository;
import org.egov.infra.utils.EmailUtils;
import org.egov.infstr.notification.HTTPSMS;
import org.egov.infstr.security.utils.CryptoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CitizenService {

    @Autowired
    private CitizenRepository citizenRepository;
    @Autowired
    private EmailUtils emailUtils;
    
    @Transactional
    public void create( Citizen citizen) throws DuplicateElementException{
        Citizen checkcitizen = getCitizenByUserName(citizen.getUsername());
        if (checkcitizen != null)
            throw new DuplicateElementException("Mobile Number already exists");
        if(citizen.getEmailId()!=null && citizen.getEmailId().length()>0){
            checkcitizen = getCitizenByEmailId(citizen.getEmailId());
            if (checkcitizen != null)
                throw new DuplicateElementException("Email already exists");
        }
            
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
    public void registerCitizen(Citizen citizen) throws DuplicateElementException{
        
        Calendar pwdExpiryDate = Calendar.getInstance();
        pwdExpiryDate.setTime(new Date());
        pwdExpiryDate.add(Calendar.YEAR,100);
        citizen.setPwdExpiryDate(pwdExpiryDate.getTime());
        citizen.setUsername(citizen.getMobileNumber());
        citizen.setPassword(CryptoHelper.encrypt(citizen.getPassword()));
        citizen.setActivationCode(RandomStringUtils.random(5, Boolean.TRUE, Boolean.TRUE));
        try {
            create(citizen);
        } catch (DuplicateElementException e) {
            throw new DuplicateElementException(e.getMessage());
        }
        
    }
    public void sendActivationMessage(Citizen citizen) throws  EGOVRuntimeException {
        
        boolean hasSent = false;
        
        if(citizen.getEmailId()!=null && !citizen.getEmailId().isEmpty()){
            hasSent  = emailUtils.sendMail(citizen.getEmailId(),"Hello,\r\n Your Portal Activation Code is : " +  citizen.getActivationCode(),"Portal Activation");
        }
        
        hasSent = HTTPSMS.sendSMS("Your Portal Activation Code is : " + citizen.getActivationCode(), "91" +citizen.getMobileNumber()) || hasSent;
        
        if (!hasSent) {
                throw new EGOVRuntimeException("Neither email nor mobile activation send.");
        }
    }

}