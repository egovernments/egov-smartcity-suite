package org.egov.ptis.domain.service.property;

import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.model.PropertyBoundary;
import org.egov.ptis.domain.model.PropertyOwnerBean;
import org.egov.ptis.domain.model.ViewProperty;
import org.springframework.beans.factory.annotation.Autowired;

public class ViewPropertyService {
	@Autowired
	private BasicPropertyDAO basicPropertyDAO;
	@Autowired
	private PtDemandDao ptDemandDAO;

	public ViewProperty getViewProperty(String propertyId) {

		ViewProperty viewProperty = new ViewProperty();
		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(propertyId);
		prepareBoundaryInfo(basicProperty);
		return viewProperty;
	}

	private PropertyBoundary prepareBoundaryInfo(BasicProperty basicProperty) {
		PropertyBoundary propertyBoundary = new PropertyBoundary();
		propertyBoundary.setZoneName(basicProperty.getPropertyID().getZone().getName());
		propertyBoundary.setWardName(basicProperty.getPropertyID().getWard().getName());
		propertyBoundary.setBlockName(basicProperty.getPropertyID().getArea().getName());
		propertyBoundary.setStreetName(basicProperty.getPropertyID().getStreet().getName());
		
		propertyBoundary.setLocalityName(basicProperty.getPropertyID().getLocality().getName());
		return propertyBoundary;
	}
	
	private PropertyOwnerBean prepareOwerInfo(BasicProperty basicProperty){
		PropertyOwnerBean propertyOwnerBean = new PropertyOwnerBean();
		return propertyOwnerBean;
	}
}
