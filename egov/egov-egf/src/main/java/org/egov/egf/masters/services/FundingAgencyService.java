/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
package org.egov.egf.masters.services;

import org.egov.commons.service.EntityTypeService;
import org.egov.commons.utils.EntityType;
import org.egov.egf.masters.model.FundingAgency;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mani Service class for FundingAgency Object
 */
@Transactional(readOnly = true)
public class FundingAgencyService extends PersistenceService<FundingAgency, Integer> implements EntityTypeService {

    public FundingAgencyService() {
        super(FundingAgency.class);
    }

    public FundingAgencyService(Class<FundingAgency> type) {
        super(type);
    }
    /**
     * since it is mapped to only one AccountDetailType -creditor it ignores the input parameter
     */
    @Override
    public List<EntityType> getAllActiveEntities(final Integer accountDetailTypeId) {
        final List<EntityType> entities = new ArrayList<EntityType>();
        entities.addAll(findAllBy("from FundingAgency r where r.isActive=?", true));
        return entities;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<EntityType> filterActiveEntities(String filterKey, final int maxRecords, final Integer accountDetailTypeId) {
        final Integer pageSize = maxRecords > 0 ? maxRecords : null;
        final List<EntityType> entities = new ArrayList<EntityType>();
        filterKey = "%" + filterKey + "%";
        final String qry = "from FundingAgency r where upper(code) like upper(?) or upper(name) like upper(?) and r.isActive=? order by code,name";
        entities.addAll(findPageBy(qry, 0, pageSize, filterKey, filterKey, true).getList());
        return entities;
    }

    /*
     * (non-Javadoc)
     * @see org.egov.commons.service.EntityTypeService#getAssetCodesForProjectCode(java.lang.Integer)
     */
    /*
     * @Override public List getAssetCodesForProjectCode(Integer accountdetailkey) throws ValidationException { return null; }
     */
    @Override
    public List<FundingAgency> validateEntityForRTGS(final List<Long> idsList) throws ValidationException {

        return null;

    }

    @Override
    public List<FundingAgency> getEntitiesById(final List<Long> idsList) throws ValidationException {

        return null;
    }

    @Override
    public List getAssetCodesForProjectCode(final Integer accountdetailkey)
            throws ValidationException {

        return null;
    }

}
