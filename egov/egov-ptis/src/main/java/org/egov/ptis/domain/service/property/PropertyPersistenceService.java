package org.egov.ptis.domain.service.property;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.persistence.entity.CorrespondenceAddress;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infstr.services.PersistenceService;
import org.egov.portal.entity.Citizen;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


public class PropertyPersistenceService extends PersistenceService<BasicProperty,Long > {
	
	private static final Logger LOGGER = Logger.getLogger(PropertyPersistenceService.class);
	@Autowired
        private UserService userService;
	@Autowired
	private PropertyTaxUtil propertyTaxUtil;
	
	public void createUserIfNotExist(BasicProperty basicProperty) {
       // final List<PropertyOwnerInfo> newOwners = new ArrayList<PropertyOwnerInfo>();
        final List<User> citizens = new ArrayList<User>();
        for(PropertyOwnerInfo Owner : basicProperty.getPropertyOwnerInfo()) {
            if (Owner.isNew()) {
                final User user = userService.getUserByAadhaarNumberAndType(Owner.getOwner().getAadhaarNumber(), Owner.getOwner().getType());
                if(user == null) {
                	//PropertyOwnerInfo ownerInfo = new PropertyOwnerInfo();
                	final User newOwner = new User();
                	//ownerInfo.setBasicProperty(basicProperty);
                	newOwner.setAadhaarNumber(Owner.getOwner().getAadhaarNumber());
                	newOwner.setMobileNumber(Owner.getOwner().getMobileNumber());
                	newOwner.setEmailId(Owner.getOwner().getEmailId());
                	newOwner.setGender(Owner.getOwner().getGender());
                	newOwner.setGuardian(Owner.getOwner().getGuardian());
                	newOwner.setGuardianRelation(Owner.getOwner().getGuardianRelation());
                	newOwner.setName(Owner.getOwner().getName());
                	newOwner.setSalutation(Owner.getOwner().getSalutation());
                	newOwner.setPassword("NOT SET");
                	newOwner.setUsername(Owner.getOwner().getMobileNumber());
                	userService.updateUser(newOwner);
                	citizens.add(newOwner);
                }
            } else
            	citizens.add(Owner.getOwner());
        }
		basicProperty.getPropertyOwnerInfo().clear();
		for (User citizen : citizens) {
			PropertyOwnerInfo ownerInfo = new PropertyOwnerInfo();
			ownerInfo.setOwner(citizen);
			ownerInfo.setBasicProperty(basicProperty);
			basicProperty.addPropertyOwners(ownerInfo);
		}
		//basicProperty.getPropertyOwnerInfo().addAll(newOwners);
    }
	
	public void createOwners(Property property,BasicProperty basicProperty,String corrAddr1,String corrAddr2,String corrPinCode) {

            LOGGER.debug("createOwners:  CorrAddress1: " + corrAddr1 + ", CorrAddress2: "
                            + corrAddr2 + ", CorrPinCode: " + corrPinCode);
            String addrStr1;
            String addrStr2;
            int orderNo = 0;
            basicProperty.getPropertyOwnerInfo().clear();
            for (PropertyOwnerInfo ownerInfo : property.getBasicProperty().getPropertyOwnerInfoProxy()) {
                    orderNo++;
                    if (ownerInfo != null) {
                        final User user = userService.getUserByAadhaarNumberAndType(ownerInfo.getOwner().getAadhaarNumber(), ownerInfo.getOwner().getType());
                            if(user == null) {
                                final Citizen newOwner = new Citizen();
                                newOwner.setAadhaarNumber(ownerInfo.getOwner().getAadhaarNumber());
                                newOwner.setMobileNumber(ownerInfo.getOwner().getMobileNumber());
                                newOwner.setEmailId(ownerInfo.getOwner().getEmailId());
                                newOwner.setGender(ownerInfo.getOwner().getGender());
                                newOwner.setGuardian(ownerInfo.getOwner().getGuardian());
                                newOwner.setGuardianRelation(ownerInfo.getOwner().getGuardianRelation());
                                newOwner.setName(ownerInfo.getOwner().getName());
                                newOwner.setSalutation(ownerInfo.getOwner().getSalutation());
                                newOwner.setPassword("NOT SET");
                                newOwner.setUsername(ownerInfo.getOwner().getMobileNumber());
                                userService.createUser(newOwner);
                                ownerInfo.setBasicProperty(basicProperty);
                                ownerInfo.setOwner(newOwner);
                                ownerInfo.setOrderNo(orderNo);
                                Address ownerAddr = new CorrespondenceAddress();
                                addrStr1 = corrAddr1;
                                addrStr2 = corrAddr2;
                                addrStr1 = propertyTaxUtil.antisamyHackReplace(addrStr1);
                                addrStr2 = propertyTaxUtil.antisamyHackReplace(addrStr2);
                                ownerAddr.setLandmark(addrStr1);
                                ownerAddr.setAreaLocalitySector(addrStr2);
                                if (isNotBlank(corrPinCode)) {
                                        ownerAddr.setPinCode(corrPinCode);
                                }
                                LOGGER.debug("createOwners: OwnerAddress: " + ownerAddr);
                                ownerInfo.getOwner().addAddress(ownerAddr);
                              
                            } else {
                                ownerInfo.setOwner(user);
                                ownerInfo.getOwner().setAddress(user.getAddress());;
                                ownerInfo.setBasicProperty(basicProperty);
                            }
                    }
                    basicProperty.addPropertyOwners(ownerInfo);
            }
    }

	
	
}
