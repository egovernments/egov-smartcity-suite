package org.egov.ptis.actions.view;

import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.SESSIONLOGINID;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPERTY_STATUS_MARK_DEACTIVE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.lib.address.model.Address;
import org.egov.lib.rjbac.role.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.FloorIF;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDocs;
import org.egov.ptis.domain.entity.property.PropertyOwner;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;
import org.egov.web.actions.BaseFormAction;

public class ViewPropertyAction extends BaseFormAction {
	private final Logger LOGGER = Logger.getLogger(getClass());
	private String propertyId;
	private BasicProperty basicProperty;
	private String ownerName;
	private String propAddress;
	private String ownerAddress;
	private String currTax;
	private String currTaxDue;
	private String totalArrDue;
	private String genWaterRate;
	private Map<String, Object> viewMap;
	private String[] floorNoStr = new String[200];
	private PropertyTaxUtil propertyTaxUtil;
	private String markedForDeactive = "N";
	private String roleName;
	private String parcelID;
	private Set<PropertyDocs> propDocsSet;
	private String docNumber;
	private boolean isDemandActive;
	private String demandEffectiveYear;
	private Integer noOfDaysForInactiveDemand;

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public String viewForm() {
		LOGGER.debug("Entered into viewForm method");
		String target = "";
		try {
			LOGGER.debug("viewForm : Index Num in View Property : " + propertyId + ", " + "Parcel Id : " + parcelID);
			viewMap = new HashMap<String, Object>();
			BasicPropertyDAO basicPropertyDAO = PropertyDAOFactory.getDAOFactory().getBasicPropertyDAO();
			if (getParcelID() != null || StringUtils.isNotEmpty(getParcelID()) || StringUtils.isBlank(getParcelID())) {
				BasicProperty bp = basicPropertyDAO.getBasicPropertyByIndexNumAndParcelID(propertyId, parcelID);
				LOGGER.debug("viewForm : BasicProperty : " + bp);
				if (bp != null) {
					setPropertyId(bp.getUpicNo());
				} else {
					LOGGER.debug("Exit from method viewForm");
					return "error";
				}
			}
			setBasicProperty(basicPropertyDAO.getBasicPropertyByPropertyID(propertyId));
			PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
			Set<PropertyStatusValues> propStatusValSet = new HashSet<PropertyStatusValues>();
			Property property = getBasicProperty().getProperty();
			
			if (!basicProperty.getStatus().getStatusCode().equalsIgnoreCase(PropertyTaxConstants.STATUS_OBJECTED_STR)
					&& property.getStatus().equals(PropertyTaxConstants.STATUS_DEMAND_INACTIVE)) {
				basicProperty.setIsDemandActive(false);
				demandEffectiveYear = PropertyTaxUtil.getRevisedDemandYear(property);
				noOfDaysForInactiveDemand = PropertyTaxUtil.getNoticeDaysForInactiveDemand(property);
			} else {
				basicProperty.setIsDemandActive(true);
			}
			
			LOGGER.debug("viewForm : Property : " + property);
			viewMap.put("ownerName", ptisCacheMgr.buildOwnerFullName(property.getPropertyOwnerSet()));
			viewMap.put("propAddress", ptisCacheMgr.buildAddressByImplemetation(getBasicProperty().getAddress()));
			if(property.getPropertyDetail().getExtra_field6()!=null){
				viewMap.put("propertyCategory",
						(Category) persistenceService.find("from Category c where c.id = ?",
								Long.valueOf(property.getPropertyDetail().getExtra_field6())));
			} else {
				viewMap.put("propertyCategory", NMCPTISConstants.NOTAVAIL);			
			}

			Set<PropertyOwner> ownerSet = property.getPropertyOwnerSet();
			if (ownerSet != null && !ownerSet.isEmpty()) {
				for (PropertyOwner owner : ownerSet) {
					Set<Address> addrSet = (Set<Address>) owner.getAddressSet();
					for (Address address : addrSet) {
						ownerAddress = ptisCacheMgr.buildAddressByImplemetation(address);
						break;
					}
				}
				viewMap.put("ownerAddress", ownerAddress == null ? NMCPTISConstants.NOTAVAIL : ownerAddress);
			}
			PtDemandDao ptDemandDao = PropertyDAOFactory.getDAOFactory().getPtDemandDao();
			Map<String, BigDecimal> demandCollMap = ptDemandDao.getDemandCollMap(property);
			viewMap.put("currTax", demandCollMap.get(CURR_DMD_STR).toString());
			viewMap.put("currTaxDue", (demandCollMap.get(CURR_DMD_STR).subtract(demandCollMap.get(CURR_COLL_STR)))
					.toString());
			viewMap.put("totalArrDue", (demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap.get(ARR_COLL_STR)))
					.toString());
			setGenWaterRate(CommonServices.getWaterMeterDtls(getBasicProperty().getProperty().getPropertyDetail()
					.getExtra_field1()));
			viewMap.put("genWaterRate", CommonServices.getWaterMeterDtls(getBasicProperty().getProperty()
					.getPropertyDetail().getExtra_field1()));
			setFloorDetails(property);
			getBasicProperty().getObjections();
			propStatusValSet = getBasicProperty().getPropertyStatusValuesSet();
			for (PropertyStatusValues propStatusVal : propStatusValSet) {
				LOGGER.debug("viewForm : Property Status Values : " + propStatusVal);
				if (propStatusVal.getPropertyStatus().getStatusCode().equals(PROPERTY_STATUS_MARK_DEACTIVE)) {
					markedForDeactive = "Y";
				}
				LOGGER.debug("Marked for Deactivation ? : " + markedForDeactive);
			}
			Integer userId = (Integer) session().get(SESSIONLOGINID);
			if (userId != null) {
				setRoleName(getRolesForUserId(userId));
			}
			if(!getBasicProperty().getPropertyDocsSet().isEmpty() && getBasicProperty().getPropertyDocsSet()!=null){
				for(PropertyDocs propDocs:getBasicProperty().getPropertyDocsSet()){
					setDocNumber(propDocs.getDocNumber());
				}
			}
		//	setPropDocsSet(getBasicProperty().getPropertyDocsSet());
			LOGGER.debug("viewForm : Owner Name : " + viewMap.get(ownerName) + ", " + "Property Address : "
					+ viewMap.get(propAddress) + ", " + "Owner Address : " + viewMap.get(ownerAddress) + ", "
					+ "Current Tax : " + viewMap.get(currTax) + ", " + "Current Tax Due : " + viewMap.get(currTaxDue)
					+ ", " + "Total Arrears Tax Due : " + viewMap.get(totalArrDue) + ", " + "General Water Rate : "
					+ viewMap.get(genWaterRate));
			LOGGER.debug("Exit from method viewForm");
			return "view";
		} catch (Exception e) {
			LOGGER.error("Exception in View Property: ", e);
			throw new EGOVRuntimeException("Exception : " + e);
		}
	}
	
	private String getRolesForUserId(Integer userId) {
		LOGGER.debug("Entered into method getRolesForUserId");
		LOGGER.debug("User id : " + userId);
		UserDAO userDao = new UserDAO();
		String roleName;
		List<String> roleNameList = new ArrayList<String>();
		User user = userDao.getUserByID(userId);
		for (Role role : user.getRoles()) {
			roleName = role.getRoleName() != null ? role.getRoleName() : "";
			roleNameList.add(roleName);
		}
		LOGGER.debug("Exit from method getRolesForUserId with return value : " + roleNameList.toString().toUpperCase());
		return roleNameList.toString().toUpperCase();
	}

	private void setFloorDetails(Property property) {
		LOGGER.debug("Entered into method setFloorDetails");
		LOGGER.debug("Property ===> " + property);
		Set<FloorIF> flrDtSet = property.getPropertyDetail().getFloorDetails();
		int i = 0;
		for (FloorIF flr : flrDtSet) {
			floorNoStr[i] = (propertyTaxUtil.getFloorStr(flr.getFloorNo()));
			i++;
		}
		property.getPropertyDetail().setFloorDetailsProxy(new ArrayList(flrDtSet));
		LOGGER.debug("Exit from method setFloorDetails");
	}

	public String getAmenitiesDtls(String mstrCode) {
		return CommonServices.getAmenitiesDtls(mstrCode);
	}
	
	public String getUnitTypeCategory(String unitTypeCode, String categoryCode) {
		return CommonServices.getUnitTypeCategory(unitTypeCode, categoryCode);
	}
	
	public String getWaterMeterDtls(String mstrCode) {
		return CommonServices.getWaterMeterDtls(mstrCode);
	}
	
	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public BasicProperty getBasicProperty() {
		return basicProperty;
	}

	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getPropAddress() {
		return propAddress;
	}

	public void setPropAddress(String propAddress) {
		this.propAddress = propAddress;
	}

	public String getOwnerAddress() {
		return ownerAddress;
	}

	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}

	public String getCurrTax() {
		return currTax;
	}

	public void setCurrTax(String currTax) {
		this.currTax = currTax;
	}

	public String getCurrTaxDue() {
		return currTaxDue;
	}

	public void setCurrTaxDue(String currTaxDue) {
		this.currTaxDue = currTaxDue;
	}

	public String getTotalArrDue() {
		return totalArrDue;
	}

	public void setTotalArrDue(String totalArrDue) {
		this.totalArrDue = totalArrDue;
	}

	public String getGenWaterRate() {
		return genWaterRate;
	}

	public void setGenWaterRate(String genWaterRate) {
		this.genWaterRate = genWaterRate;
	}

	public String[] getFloorNoStr() {
		return floorNoStr;
	}

	public void setFloorNoStr(String[] floorNoStr) {
		this.floorNoStr = floorNoStr;
	}

	public PropertyTaxUtil getPropertyTaxUtil() {
		return propertyTaxUtil;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

	public Map<String, Object> getViewMap() {
		return viewMap;
	}

	public String getMarkedForDeactive() {
		return markedForDeactive;
	}

	public void setMarkedForDeactive(String markedForDeactive) {
		this.markedForDeactive = markedForDeactive;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getParcelID() {
		return parcelID;
	}

	public void setParcelID(String parcelID) {
		this.parcelID = parcelID;
	}
	
	public Set<PropertyDocs> getPropDocsSet() {
		return propDocsSet;
	}

	public void setPropDocsSet(Set<PropertyDocs> propDocsSet) {
		this.propDocsSet = propDocsSet;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public boolean getIsDemandActive() {
		return isDemandActive;
	}

	public void setIsDemandActive(boolean isDemandActive) {
		this.isDemandActive = isDemandActive;
	}

	public String getDemandEffectiveYear() {
		return demandEffectiveYear;
	}

	public void setDemandEffectiveYear(String demandEffectiveYear) {
		this.demandEffectiveYear = demandEffectiveYear;
	}

	public Integer getNoOfDaysForInactiveDemand() {
		return noOfDaysForInactiveDemand;
	}

	public void setNoOfDaysForInactiveDemand(Integer noOfDaysForInactiveDemand) {
		this.noOfDaysForInactiveDemand = noOfDaysForInactiveDemand;
	}
}
