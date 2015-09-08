/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.services;

import java.util.List;

import org.egov.commons.service.EntityTypeService;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.masters.Contractor;
import org.hibernate.Query;

public class ContractorService extends PersistenceService<Contractor, Long>implements EntityTypeService {

    @Override
    public List<Contractor> getAllActiveEntities(final Integer accountDetailTypeId) {
        return findAllBy("select distinct contractorDet.contractor from ContractorDetail contractorDet " +
                "where contractorDet.status.description=? and contractorDet.status.moduletype=?", "Active", "Contractor");
    }

    @Override
    public List<Contractor> filterActiveEntities(final String filterKey,
            final int maxRecords, final Integer accountDetailTypeId) {
        final Integer pageSize = maxRecords > 0 ? maxRecords : null;
        final String param = "%" + filterKey.toUpperCase() + "%";
        final String qry = "select distinct cont from Contractor cont, ContractorDetail contractorDet " +
                "where cont.id=contractorDet.contractor.id and contractorDet.status.description=? and contractorDet.status.moduletype=? and (upper(cont.code) like ? "
                +
                "or upper(cont.name) like ?) order by cont.code,cont.name";
        return findPageBy(qry, 0, pageSize,
                "Active", "Contractor", param, param).getList();
    }

    @Override
    public List getAssetCodesForProjectCode(final Integer accountdetailkey)
            throws ValidationException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Contractor> validateEntityForRTGS(final List<Long> idsList) throws ValidationException {

        List<Contractor> entities = null;
        final Query entitysQuery = getSession()
                .createQuery(" from Contractor where panNumber is null or bank is null and id in ( :IDS )");
        entitysQuery.setParameterList("IDS", idsList);
        entities = entitysQuery.list();
        return entities;

    }

    @Override
    public List<Contractor> getEntitiesById(final List<Long> idsList) throws ValidationException {

        List<Contractor> entities = null;
        final Query entitysQuery = getSession().createQuery(" from Contractor where id in ( :IDS )");
        entitysQuery.setParameterList("IDS", idsList);
        entities = entitysQuery.list();
        return entities;
    }

}
