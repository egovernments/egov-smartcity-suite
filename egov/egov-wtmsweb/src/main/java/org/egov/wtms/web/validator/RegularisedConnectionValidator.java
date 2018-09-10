package org.egov.wtms.web.validator;

import org.egov.wtms.application.entity.RegularisedConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.RegularisedConnectionService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegularisedConnectionValidator implements Validator {

    @Autowired
    private RegularisedConnectionService regularisedConnectionService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return RegularisedConnection.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        // override default implementation
    }

    public boolean validateRegularizationApplication(final Long id) {
        if (id == null)
            return false;
        else {
            final RegularisedConnection connection = regularisedConnectionService.findById(id);
            if (connection == null)
                return false;
            final WaterConnectionDetails connectionDetails = waterConnectionDetailsService
                    .findByApplicationNumber(connection.getApplicationNumber());
            if (connectionDetails == null)
                return false;
        }
        return true;
    }
}
