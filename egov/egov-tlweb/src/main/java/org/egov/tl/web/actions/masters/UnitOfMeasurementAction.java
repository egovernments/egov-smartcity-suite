package org.egov.tl.web.actions.masters;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.tl.domain.entity.UnitOfMeasurement;
import org.egov.tl.domain.service.masters.UnitOfMeasurementService;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({ @Result(name = UnitOfMeasurementAction.NEW, location = "unitOfMeasurement-new.jsp"),
    @Result(name = UnitOfMeasurementAction.SEARCH, location = "unitOfMeasurement-search.jsp"),
    @Result(name = UnitOfMeasurementAction.EDIT, location = "unitOfMeasurement-edit.jsp") })
public class UnitOfMeasurementAction extends BaseFormAction {

    /**
     *
     */
    private static final long serialVersionUID = -5616536912073613646L;
    private UnitOfMeasurement unitOfMeasurement = new UnitOfMeasurement();
    private Long id;
    public static final String SEARCH = "search";
    public static final String VIEW = "view";
    private Map<Long, String> licenseUomMap;

    @Autowired
    private UnitOfMeasurementService unitOfMeasurementService;

    private static final Logger LOGGER = Logger.getLogger(UnitOfMeasurementAction.class);

    // UI field
    private String userMode;
    // used to persist active field in modify case
    private boolean uomActive;

    @Override
    public Object getModel() {
        // TODO Auto-generated method stub
        return unitOfMeasurement;
    }

    @Override
    public void prepare() {
        // In Modify and View Mode Load UOM dropdown.
        if (userMode != null && !userMode.isEmpty() && (userMode.equalsIgnoreCase(EDIT) || userMode.equalsIgnoreCase(VIEW)))
            setLicenseUomMap(getFormattedUOMMap(unitOfMeasurementService.findAll()));
        if (getId() != null)
            unitOfMeasurement = unitOfMeasurementService.findById(getId(), false);
    }

    /**
     * @param unitOfMeasurementList
     * @return
     */
    public static Map<Long, String> getFormattedUOMMap(final List<UnitOfMeasurement> unitOfMeasurementList) {
        final Map<Long, String> uomMap = new TreeMap<Long, String>();
        for (final UnitOfMeasurement uom : unitOfMeasurementList)
            uomMap.put(uom.getId(),
                    uom.getName().concat(" ~ ").concat(uom.getCode()));
        return uomMap;
    }

    /**
     * This method is invoked to create a new form.
     *
     * @return a <code>String</code> representing the value 'NEW'
     */
    @Action(value = "/masters/unitOfMeasurement-newform")
    public String newform() {
        if (userMode != null && !userMode.isEmpty()) {
            if (userMode.equalsIgnoreCase(VIEW))
                userMode = VIEW;
            else if (userMode.equalsIgnoreCase(EDIT))
                userMode = EDIT;
        }
        else
            userMode = NEW;
        return NEW;
    }

    /**
     * This method is invoked to Edit a form.
     *
     * @return a <code>String</code> representing the value 'SEARCH'
     */
    @Action(value = "/masters/unitOfMeasurement-edit")
    public String edit() {
        if (userMode.equalsIgnoreCase(EDIT))
            userMode = EDIT;
        else if (userMode.equalsIgnoreCase(VIEW))
            userMode = VIEW;
        return SEARCH;
    }

    /**
     * @return
     * @throws NumberFormatException
     * @throws ApplicationException
     */
    @ValidationErrorPage(value = EDIT)
    @Action(value = "/masters/unitOfMeasurement-save")
    public String save() throws NumberFormatException, ApplicationException {
        try {
            if (userMode.equalsIgnoreCase(EDIT))
                unitOfMeasurement.setActive(uomActive);
            unitOfMeasurement = unitOfMeasurementService.persist(unitOfMeasurement);
        } catch (final ValidationException valEx) {
            LOGGER.error("Exception found while persisting Unit of Measurement: " + valEx.getErrors());
            throw new ValidationException(valEx.getErrors());
        }
        if (userMode.equalsIgnoreCase(NEW))
            addActionMessage("\'" + unitOfMeasurement.getCode() + "\' " + getText("license.uom.save.success"));
        else if (userMode.equalsIgnoreCase(EDIT))
            addActionMessage("\'" + unitOfMeasurement.getCode() + "\' " + getText("license.uom.edit.success"));
        userMode = SUCCESS;
        return NEW;
    }

    public String getUserMode() {
        return userMode;
    }

    public void setUserMode(final String userMode) {
        this.userMode = userMode;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public UnitOfMeasurement getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(final UnitOfMeasurement unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public Map<Long, String> getLicenseUomMap() {
        return licenseUomMap;
    }

    public void setLicenseUomMap(final Map<Long, String> licenseUomMap) {
        this.licenseUomMap = licenseUomMap;
    }

    public boolean isUomActive() {
        return uomActive;
    }

    public void setUomActive(final boolean uomActive) {
        this.uomActive = uomActive;
    }
}
