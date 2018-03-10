/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.infra.event.model;

import static org.egov.infra.config.core.ApplicationThreadLocals.clearValues;
import static org.egov.infra.config.core.ApplicationThreadLocals.getCityCode;
import static org.egov.infra.config.core.ApplicationThreadLocals.getCityName;
import static org.egov.infra.config.core.ApplicationThreadLocals.getDomainName;
import static org.egov.infra.config.core.ApplicationThreadLocals.getDomainURL;
import static org.egov.infra.config.core.ApplicationThreadLocals.getMunicipalityName;
import static org.egov.infra.config.core.ApplicationThreadLocals.getTenantID;
import static org.egov.infra.config.core.ApplicationThreadLocals.getUserId;
import static org.egov.infra.config.core.ApplicationThreadLocals.setCityCode;
import static org.egov.infra.config.core.ApplicationThreadLocals.setCityName;
import static org.egov.infra.config.core.ApplicationThreadLocals.setDomainName;
import static org.egov.infra.config.core.ApplicationThreadLocals.setDomainURL;
import static org.egov.infra.config.core.ApplicationThreadLocals.setMunicipalityName;
import static org.egov.infra.config.core.ApplicationThreadLocals.setTenantID;
import static org.egov.infra.config.core.ApplicationThreadLocals.setUserId;

public abstract class AbstractApplicationEvent<T> {

    private T sourceObject;
    private Long userId;
    private String tenantId;
    private String cityCode;
    private String cityName;
    private String domainURL;
    private String domainName;
    private String municiplalityName;

    public AbstractApplicationEvent(T sourceObject) {
        this.sourceObject = sourceObject;
        this.userId = getUserId();
        this.tenantId = getTenantID();
        this.cityCode = getCityCode();
        this.cityName = getCityName();
        this.domainName = getDomainName();
        this.domainURL = getDomainURL();
        this.municiplalityName = getMunicipalityName();
    }

    public T getSource() {
        return sourceObject;
    }

    public T initializeAndGetSource() {
        initialize();
        return sourceObject;
    }

    public void initialize() {
        setUserId(userId);
        setTenantID(tenantId);
        setCityCode(cityCode);
        setCityName(cityName);
        setDomainName(domainName);
        setDomainURL(domainURL);
        setMunicipalityName(municiplalityName);
    }

    public void finish() {
        clearValues();
    }
}
