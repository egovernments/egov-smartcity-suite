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
	
	
	public PropertyImpl updateProperty(PropertyImpl property, String areaOfPlot, String mutationCode,
			String propTypeId, Long floorTypeId, Long roofTypeId, Long wallTypeId, Long woodTypeId) {
		LOGGER.debug("Entered into createProperty");
		LOGGER.debug("createProperty: Property: " + property + ", areaOfPlot: " + areaOfPlot + ", mutationCode: "
				+ mutationCode + ",propTypeId: " + propTypeId);
	
		if (floorTypeId != -1 && floorTypeId != null) {
			FloorType floorType = (FloorType) find("From FloorType where id = ?", floorTypeId);
			property.getPropertyDetail().setFloorType(floorType);
		}
		if (roofTypeId != -1 && roofTypeId != null) {
			RoofType roofType = (RoofType) find("From RoofType where id = ?", roofTypeId);
			property.getPropertyDetail().setRoofType(roofType);
		}
		if (wallTypeId != -1 && wallTypeId != null) {
			WallType wallType = (WallType) find("From WallType where id = ?", wallTypeId);
			property.getPropertyDetail().setWallType(wallType);
		}
		if (woodTypeId != -1 && woodTypeId != null) {
			WoodType woodType = (WoodType) find("From WoodType where id = ?", woodTypeId);
			property.getPropertyDetail().setWoodType(woodType);
		}

		if (areaOfPlot != null && !areaOfPlot.isEmpty()) {
			Area area = new Area();
			area.setArea(new Float(areaOfPlot));
			property.getPropertyDetail().setSitalArea(area);
		}

		if (property.getPropertyDetail().getApartment() != null
				&& property.getPropertyDetail().getApartment().getId() != null) {
			Apartment apartment = (Apartment) find("From Apartment where id = ?",
					property.getPropertyDetail().getApartment().getId());
			property.getPropertyDetail().setApartment(apartment);
		} else {
			property.getPropertyDetail().setApartment(null);
		}
		property.getPropertyDetail().setProperty(property);
		PropertyMutationMaster propMutMstr = (PropertyMutationMaster)find(
				"from PropertyMutationMaster PM where upper(PM.code) = ?", mutationCode);
		PropertyTypeMaster propTypeMstr = (PropertyTypeMaster)find(
				"from PropertyTypeMaster PTM where PTM.id = ?", Long.valueOf(propTypeId));
		
		if (propTypeMstr.getCode().equals(OWNERSHIP_TYPE_VAC_LAND)) {
			property.getPropertyDetail().setPropertyType(VACANT_PROPERTY);
		} else {
			property.getPropertyDetail().setPropertyType(BUILT_UP_PROPERTY);
		}
		property.getPropertyDetail().setPropertyTypeMaster(propTypeMstr);
		property.getPropertyDetail().setPropertyMutationMaster(propMutMstr);
		property.getPropertyDetail().setUpdatedTime(new Date());
		updateFloors(property);
		LOGGER.debug("Exiting from update property");
		return property;
	}
	
	private void updateFloors(Property property) {
		Area totBltUpArea = new Area();
		Float totBltUpAreaVal = new Float(0);
		List<Floor> floorList = new ArrayList<Floor>();
		if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
			
			for (Floor floor : property.getPropertyDetail().getFloorDetails()) {
				if (floor != null) {
					totBltUpAreaVal = totBltUpAreaVal + floor.getBuiltUpArea().getArea();
					PropertyUsage usage = null;
					PropertyOccupation occupancy = null;
					TaxExeptionReason taxExemption = null;
					if (floor.getTaxExemptedReason() != null && floor.getTaxExemptedReason().getId() != null
							&& floor.getTaxExemptedReason().getId() != -1) {
						taxExemption = (TaxExeptionReason) find("from TaxExeptionReason where id = ?",
								floor.getTaxExemptedReason().getId());
					}
					if (floor.getPropertyUsage() != null) {
						usage = (PropertyUsage) find("from PropertyUsage pu where pu.id = ?",
								floor.getPropertyUsage().getId());
					}
					if (floor.getPropertyOccupation() != null) {
						occupancy = (PropertyOccupation) find(
								"from PropertyOccupation po where po.id = ?", floor.getPropertyOccupation().getId());
					}

					StructureClassification structureClass = null;

					if (floor.getStructureClassification() != null) {
						structureClass = (StructureClassification) find(
								"from StructureClassification sc where sc.id = ?",
								floor.getStructureClassification().getId());
					}

					if (floor.getOccupancyDate() != null) {
						floor.setDepreciationMaster(propertyTaxUtil.getDepreciationByDate(floor.getOccupancyDate()));
					}

					LOGGER.debug("createFloors: PropertyUsage: " + usage + ", PropertyOccupation: " + occupancy
							+ ", StructureClass: " + structureClass);

					floor.setPropertyUsage(usage);
					floor.setPropertyOccupation(occupancy);
					floor.setStructureClassification(structureClass);
                                        floor.setTaxExemptedReason(taxExemption);
					floor.setPropertyDetail(property.getPropertyDetail());
					floor.setCreatedDate(new Date());
					floor.setModifiedDate(new Date());
					User user = userService.getUserById(EgovThreadLocals.getUserId());
					floor.setCreatedBy(user);
					floor.setModifiedBy(user);
					// setting total builtup area.
					totBltUpArea.setArea(totBltUpAreaVal);
					property.getPropertyDetail().setTotalBuiltupArea(totBltUpArea);

				}
				floorList.add(floor);
			}
			property.getPropertyDetail().getFloorDetails().clear();
			//getSession().delete(property.getPropertyDetail().getFloorDetails());
			property.getPropertyDetail().getFloorDetails().addAll(floorList);
		} else {
			property.getPropertyDetail().setNo_of_floors(0);
			property.getPropertyDetail().getFloorDetails().clear();
			totBltUpArea.setArea(totBltUpAreaVal);
			property.getPropertyDetail().setTotalBuiltupArea(totBltUpArea);
		}
		
	}
	
	public PropertyDetail updatePropertyDetail(Property property,PropertyDetail propertyDetail) {
		PropertyDetail propDetail = property.getPropertyDetail();

		propDetail.setSitalArea(propertyDetail.getSitalArea());
		propDetail.setTotalBuiltupArea(propertyDetail.getTotalBuiltupArea());
		propDetail.setCommBuiltUpArea(propertyDetail.getCommBuiltUpArea());
		propDetail.setPlinthArea(propertyDetail.getPlinthArea());
		propDetail.setCommVacantLand(propertyDetail.getCommVacantLand());
		propDetail.setSurveyNumber(propertyDetail.getSurveyNumber());
		propDetail.setFieldVerified(propertyDetail.getFieldVerified());
		propDetail.setFieldVerificationDate(propertyDetail.getFieldVerificationDate());
		propDetail.setFloorDetails(propertyDetail.getFloorDetails());
		propDetail.setPropertyDetailsID(propertyDetail.getPropertyDetailsID());
		propDetail.setWater_Meter_Num(propertyDetail.getWater_Meter_Num());
		propDetail.setElec_Meter_Num(propertyDetail.getElec_Meter_Num());
		propDetail.setNo_of_floors(0);
		propDetail.setFieldIrregular(propertyDetail.getFieldIrregular());
		propDetail.setDateOfCompletion(propertyDetail.getDateOfCompletion());
		propDetail.setProperty(propertyDetail.getProperty());
		propDetail.setUpdatedTime(propertyDetail.getUpdatedTime());
		propDetail.setPropertyTypeMaster(propertyDetail.getPropertyTypeMaster());
		propDetail.setPropertyType(propertyDetail.getPropertyType());
		propDetail.setInstallment(propertyDetail.getInstallment());
		propDetail.setPropertyOccupation(propertyDetail.getPropertyOccupation());
		propDetail.setPropertyMutationMaster(propertyDetail.getPropertyMutationMaster());
		propDetail.setComZone(propertyDetail.getComZone());
		propDetail.setCornerPlot(propertyDetail.getCornerPlot());
		propDetail.setCable(propertyDetail.isCable());
		propDetail.setAttachedBathRoom(propertyDetail.isAttachedBathRoom());
		propDetail.setElectricity(propertyDetail.isElectricity());
		propDetail.setWaterTap(propertyDetail.isWaterTap());
		propDetail.setWaterHarvesting(propertyDetail.isWaterHarvesting());
		propDetail.setLift(propertyDetail.isLift());
		propDetail.setToilets(propertyDetail.isToilets());
		propDetail.setFloorType(propertyDetail.getFloorType());
		propDetail.setRoofType(propertyDetail.getRoofType());
		propDetail.setWallType(propertyDetail.getWallType());
		propDetail.setWoodType(propertyDetail.getWoodType());
		propDetail.setExtentSite(propertyDetail.getExtentSite());
		propDetail.setStructure(propertyDetail.isStructure());
		propDetail.setExtentAppartenauntLand(propertyDetail.getExtentAppartenauntLand());
		propDetail.setManualAlv(propertyDetail.getManualAlv());
		propDetail.setOccupierName(propertyDetail.getOccupierName());
		property.setPropertyDetail(propDetail);
		return propDetail;

	}
	
	
	public PropertyTaxUtil getPropertyTaxUtil() {
		return propertyTaxUtil;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}



}
