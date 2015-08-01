package org.egov.ptis.domain.service.property;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.persistence.entity.CorrespondenceAddress;
import org.egov.infstr.services.PersistenceService;
import org.egov.portal.entity.Citizen;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.springframework.beans.factory.annotation.Autowired;


public class PropertyPersistenceService extends PersistenceService<BasicProperty,Long > {
	
	private static final Logger LOGGER = Logger.getLogger(PropertyPersistenceService.class);
	@Autowired
        private UserService userService;
	@Autowired
	private PropertyTaxUtil propertyTaxUtil;
	
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
                                ownerInfo.setOrderNo(orderNo);
                                ownerInfo.getOwner().setAddress(user.getAddress());;
                                ownerInfo.setBasicProperty(basicProperty);
                            }
                    }
                    basicProperty.addPropertyOwners(ownerInfo);
            }
    }

	
	
}
