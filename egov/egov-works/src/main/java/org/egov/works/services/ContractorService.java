package org.egov.works.services;

import java.util.List;


import org.egov.commons.service.EntityTypeService;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.masters.Contractor;
import org.hibernate.Query;



public class ContractorService extends PersistenceService<Contractor, Long> implements EntityTypeService{

	
	public List<Contractor> getAllActiveEntities(Integer accountDetailTypeId) {
       return findAllBy("select distinct contractorDet.contractor from ContractorDetail contractorDet " +
       		"where contractorDet.status.description=? and contractorDet.status.moduletype=?", "Active","Contractor");
	}
	
	public List<Contractor> filterActiveEntities(String filterKey,
			int maxRecords, Integer accountDetailTypeId) {
		Integer pageSize = (maxRecords > 0 ? maxRecords : null);
		String param = "%" + filterKey.toUpperCase() + "%";
		String qry = "select distinct cont from Contractor cont, ContractorDetail contractorDet " +
				"where cont.id=contractorDet.contractor.id and contractorDet.status.description=? and contractorDet.status.moduletype=? and (upper(cont.code) like ? " +
				"or upper(cont.name) like ?) order by cont.code,cont.name";
		return (List<Contractor>) findPageBy(qry, 0, pageSize,
				"Active", "Contractor", param, param).getList();
	}

	@Override
	public List getAssetCodesForProjectCode(Integer accountdetailkey)
			throws ValidationException {
		// TODO Auto-generated method stub
		return null;
	}	
	
	public List<Contractor> validateEntityForRTGS(List<Long> idsList) throws ValidationException {
		 
		List<Contractor> entities=null;
		 Query entitysQuery = getSession().createQuery(" from Contractor where panNumber is null or bank is null and id in ( :IDS )");
		 entitysQuery.setParameterList("IDS", idsList);
		 entities = entitysQuery.list();
		return entities;
		 
	 }
	
	 public List<Contractor> getEntitiesById(List<Long> idsList) throws ValidationException {
		 
		 List<Contractor> entities=null;
		 Query entitysQuery = getSession().createQuery(" from Contractor where id in ( :IDS )");
		 entitysQuery.setParameterList("IDS", idsList);
		 entities = entitysQuery.list();
		return entities;
	 }

}
