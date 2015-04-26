package org.egov.ptis.bean;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPERTYTYPE_CODE_TO_STR;

import java.util.ArrayList;
import java.util.List;

import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.nmc.model.ConsolidatedUnitTaxCalReport;
import org.egov.ptis.utils.PTISCacheManager;

/**
 * 
 * @author nayeem
 *
 */
public class PropertyCalSheetInfo {
	
	private Property activeProperty;
	private BasicProperty basicProperty;
	
	private List<ConsolidatedUnitTaxCalReport> consolidatedUnitTaxCalReportList = new ArrayList<ConsolidatedUnitTaxCalReport>();
	private PTISCacheManager ptisCasheManager = new PTISCacheManager();
	
	public PropertyCalSheetInfo() {}
	
	public PropertyCalSheetInfo(Property activeProperty) {
		this.activeProperty = activeProperty;
		this.basicProperty = activeProperty.getBasicProperty();
	}
	
	public static PropertyCalSheetInfo createCalSheetInfo(Property activeProperty) {
		return new PropertyCalSheetInfo(activeProperty);
	}
	
	public String getIndexNo() {
		return basicProperty.getUpicNo();
	}

	public String getParcelId() {
		return basicProperty.getGisReferenceNo();
	}

	public String getWard() {
		return getPropertyID().getArea().getId().toString();
	}

	public String getHouseNumber() {
		return basicProperty.getAddress().getHouseNoBldgApt();
	}

	public String getPropertyType() {
		return PROPERTYTYPE_CODE_TO_STR.get(activeProperty.getPropertyDetail().getPropertyTypeMaster()
				.getCode());
	}

	public String getArea() {
		return getPropertyID().getArea().getId().toString();
	}

	public String getPropertyOwnerName() {
		return ptisCasheManager.buildOwnerFullName(basicProperty);
	}
	
	private PropertyID getPropertyID() {
		return basicProperty.getPropertyID();
	}

	public String getPropertyAddress() {
		return ptisCasheManager.buildAddressByImplemetation(basicProperty.getAddress());
	}

	public List<ConsolidatedUnitTaxCalReport> getConsolidatedUnitTaxCalReportList() {
		return consolidatedUnitTaxCalReportList;
	}

	public void setConsolidatedUnitTaxCalReportList(List<ConsolidatedUnitTaxCalReport> consolidatedUnitTaxCalReportList) {
		this.consolidatedUnitTaxCalReportList = consolidatedUnitTaxCalReportList;
	}
	
	public String getAmenities() {
		return activeProperty.getPropertyDetail().getExtra_field4();
	}
}
