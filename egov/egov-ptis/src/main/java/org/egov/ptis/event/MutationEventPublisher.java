/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.ptis.event;

import static org.egov.infra.config.core.ApplicationThreadLocals.getCityCode;

import java.math.BigInteger;
import java.util.Random;

import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.event.model.MutationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * 
 * @author subhash
 *
 */
@Service
public class MutationEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(PropertyMutation propertyMutation) {
        MutationEvent event = new MutationEvent(this, propertyMutation);
        BasicProperty basicProperty = propertyMutation.getBasicProperty();
        event.setCityCode(getCityCode());
        event.setPropertyType(basicProperty.getProperty().getPropertyDetail().getPropertyTypeMaster().getCode());
        event.setOwnerName(basicProperty.getFullOwnerName());
        event.setAadharNumber(basicProperty.getAadharNumber());
        event.setMobileNumber(basicProperty.getMobileNumber());
        event.setSurveyNumber(basicProperty.getActiveProperty().getPropertyDetail().getSurveyNumber());
        // Hard coded values to be removed once get the clarification
        event.setDistrictCode("510");
        event.setCityCode("251744");
        event.setWard(String.valueOf(basicProperty.getPropertyID().getWard().getBoundaryNum()));
        event.setBlock(String.valueOf(basicProperty.getPropertyID().getArea().getBoundaryNum()));
        event.setDoorNumber(basicProperty.getAddress().getHouseNoBldgApt());
        event.setLayoutNumber(basicProperty.getActiveProperty().getPropertyDetail().getLayoutPermitNo());
        event.setRandom(new BigInteger(32, new Random()));
        applicationEventPublisher.publishEvent(event);
    }
}
