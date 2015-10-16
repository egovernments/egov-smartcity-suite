package org.egov.ptis.domain.service.demolition;

import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;

import java.util.Date;

import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.VacantProperty;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class PropertyDemolitionService extends PersistenceService<PropertyImpl, Long> {

    @Autowired
    private PropertyTypeMasterDAO propertyTypeMasterDAO;

    @Autowired
    private PropertyService propService;

    @Autowired
    private PropertyPersistenceService propertyPerService;

    PropertyImpl propertyModel = new PropertyImpl();

    @Transactional
    public void saveProperty(Property oldProperty, Property newProperty, Character status) {
        Date propCompletionDate = null;
        BasicProperty basicProperty = oldProperty.getBasicProperty();
        PropertyTypeMaster propTypeMstr = propertyTypeMasterDAO.getPropertyTypeMasterByCode(OWNERSHIP_TYPE_VAC_LAND);
        propertyModel = (PropertyImpl) newProperty;
        newProperty.getPropertyDetail().setPropertyTypeMaster(propTypeMstr);
        newProperty.getBasicProperty().setPropOccupationDate(newProperty.getPropertyDetail().getDateOfCompletion());
        propCompletionDate = newProperty.getPropertyDetail().getDateOfCompletion();
        String areaOfPlot = String.valueOf(propertyModel.getPropertyDetail().getSitalArea().getArea());
        propertyModel = propService.createProperty(propertyModel, areaOfPlot, "", propertyModel.getPropertyDetail()
                .getPropertyTypeMaster().getId().toString(), null, null, status, null, null, propertyModel
                .getPropertyDetail().getFloorType().getId(), propertyModel.getPropertyDetail().getRoofType().getId(),
                null, null, null);
        propertyModel.setBasicProperty(basicProperty);
        propertyModel.setEffectiveDate(propCompletionDate);
        propService.changePropertyDetail(propertyModel, new VacantProperty(), 0);
        basicProperty.setUnderWorkflow(Boolean.TRUE);
        propertyModel.setBasicProperty(basicProperty);
        basicProperty.addProperty(propertyModel);
        /*Property modProperty = propService.modifyDemand(propertyModel, (PropertyImpl) oldProperty);
        if (modProperty == null)
            basicProperty.addProperty(propertyModel);
        else
            basicProperty.addProperty(modProperty);*/
        propertyPerService.update(basicProperty);

    }

}
