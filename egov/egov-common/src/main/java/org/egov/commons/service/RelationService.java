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
package org.egov.commons.service;

import org.egov.commons.Relation;
import org.egov.commons.utils.EntityType;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;

import java.util.ArrayList;
import java.util.List;

public class RelationService extends PersistenceService<Relation, Integer> implements EntityTypeService, BidderTypeService {

	/**
	 * since it is mapped to only one AccountDetailType -creditor it ignores the input parameter
	 */
	@Override
	public List<EntityType> getAllActiveEntities(final Integer accountDetailTypeId) {
		final List<EntityType> entities = new ArrayList<EntityType>();
		entities.addAll(findAllBy("from Relation r where r.isactive=?", true));
		return entities;
	}

	@Override
	public List<EntityType> filterActiveEntities(String filterKey, final int maxRecords, final Integer accountDetailTypeId) {
		final Integer pageSize = (maxRecords > 0 ? maxRecords : null);
		final List<EntityType> entities = new ArrayList<EntityType>();
		filterKey = "%" + filterKey + "%";
		final String qry = "from Relation r where upper(code) like upper(?) or upper(name) like upper(?) and r.isactive=? order by code,name";
		entities.addAll(findPageBy(qry, 0, pageSize, filterKey, filterKey, true).getList());
		return entities;
	}

	@Override
	public List<Relation> getAllActiveBidders() {
		final List<Relation> entities = new ArrayList<Relation>();
		entities.addAll(findAllBy("from Relation r where r.isactive=? and relationtype.name=?", true, "Supplier"));
		return entities;
	}

	@Override
	public Relation getBidderById(final Integer bidderId) {
		return findById(bidderId, Boolean.TRUE);
	}

	@Override
	public Relation getBidderByCode(final String code) {
		return find("from Relation where code=?", code);
	}

	@Override
	public List getAssetCodesForProjectCode(Integer accountdetailkey)
			throws ValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends EntityType> validateEntityForRTGS(List<Long> idsList)
			throws ValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends EntityType> getEntitiesById(List<Long> idsList)
			throws ValidationException {
		// TODO Auto-generated method stub
		return null;
	}
}
