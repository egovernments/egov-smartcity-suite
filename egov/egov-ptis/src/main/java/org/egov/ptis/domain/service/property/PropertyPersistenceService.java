package org.egov.ptis.domain.service.property;

import static org.egov.ptis.constants.PropertyTaxConstants.BUILT_UP_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.OPEN_PLOT_UNIT_FLOORNUMBER;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_IS_DEFAULT;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_SOURCE;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANT_PROPERTY;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.Area;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.portal.entity.Citizen;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.Apartment;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.FloorType;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertySource;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.RoofType;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.ptis.domain.entity.property.TaxExeptionReason;
import org.egov.ptis.domain.entity.property.VacantProperty;
import org.egov.ptis.domain.entity.property.WallType;
import org.egov.ptis.domain.entity.property.WoodType;
import org.hibernate.FlushMode;
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
	
	
}
