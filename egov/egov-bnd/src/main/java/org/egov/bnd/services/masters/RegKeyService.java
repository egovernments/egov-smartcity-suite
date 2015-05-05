/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.bnd.services.masters;

import org.egov.bnd.model.RegKeys;
import org.egov.bnd.model.RegistrationUnit;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public class RegKeyService extends PersistenceService<RegKeys, Long> {

    @Transactional
    public RegKeys getRegKeyByType(final String type) {
        return find("from RegKeys where regType = ?", type);
    }

    /**
     * @param type
     * @param year
     * @param minValue
     * @param maxValue
     * @return
     */

    @Transactional
    public RegKeys save(final String type, final int year, final Long minValue, final Long maxValue,
            final RegistrationUnit unit) {
        final RegKeys regKey = new RegKeys();
        regKey.setRegType(type);
        regKey.setYear(year);
        regKey.setMinValue(minValue);
        regKey.setMaxValue(maxValue);
        regKey.setRegistrationUnit(unit);
        persist(regKey);
        return regKey;
    }

    @Transactional
    public RegKeys getRegKeyByRegUnitAndDate(final RegistrationUnit regUnit, final int year, final String objectType) {
        final Criteria regKeyCrit = getSession().createCriteria(RegKeys.class);
        regKeyCrit.add(Restrictions.eq("registrationUnit", regUnit));

        if (year != 0 && year != -1)
            regKeyCrit.add(Restrictions.eq("year", year));

        if (objectType != null && !"".equals(objectType))
            regKeyCrit.add(Restrictions.eq("regType", objectType));

        return (RegKeys) regKeyCrit.uniqueResult();
    }

}
