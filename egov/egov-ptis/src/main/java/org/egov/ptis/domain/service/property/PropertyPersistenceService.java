package org.egov.ptis.domain.service.property;

import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infstr.services.PersistenceService;
import org.egov.portal.entity.Citizen;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.springframework.beans.factory.annotation.Autowired;

public class PropertyPersistenceService extends PersistenceService<BasicProperty, Long> {

    private static final Logger LOGGER = Logger.getLogger(PropertyPersistenceService.class);
    @Autowired
    private UserService userService;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @SuppressWarnings("unchecked")
    public void createOwners(final Property property, final BasicProperty basicProperty, final Address ownerAddress) {

        LOGGER.debug("createOwners for property: " + property + ", basicProperty: " + basicProperty
                + ", ownerAddress: " + ownerAddress);
        int orderNo = 0;
        basicProperty.getPropertyOwnerInfo().clear();
        for (final PropertyOwnerInfo ownerInfo : property.getBasicProperty().getPropertyOwnerInfoProxy()) {
            orderNo++;
            if (ownerInfo != null) {
                final User user = userService.getUserByAadhaarNumberAndType(ownerInfo.getOwner().getAadhaarNumber(),
                        ownerInfo.getOwner().getType());
                if (user == null) {
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
                    LOGGER.debug("createOwners: OwnerAddress: " + ownerAddress);
                    ownerInfo.getOwner().addAddress(ownerAddress);
                } else {
                    ownerInfo.setOwner(user);
                    user.addAddress(ownerAddress);
                    ownerInfo.setOrderNo(orderNo);
                    ownerInfo.setBasicProperty(basicProperty);
                }
            }
            basicProperty.addPropertyOwners(ownerInfo);
        }
    }

}
