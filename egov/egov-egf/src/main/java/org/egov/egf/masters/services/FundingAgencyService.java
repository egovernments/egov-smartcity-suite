package org.egov.egf.masters.services;

import java.util.ArrayList;
import java.util.List;

import org.egov.commons.service.EntityTypeService;
import org.egov.commons.utils.EntityType;
import org.egov.egf.masters.model.FundingAgency;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
/**
 * 
 * @author mani
 * Service class for FundingAgency Object
 */
public class FundingAgencyService extends PersistenceService<FundingAgency, Integer> implements EntityTypeService {
	/**
	 * since it is mapped to only one AccountDetailType -creditor it ignores the input parameter
	 */
	public List<EntityType> getAllActiveEntities(Integer accountDetailTypeId) {
      List<EntityType> entities=new ArrayList<EntityType>();
		entities.addAll(findAllBy("from FundingAgency r where r.isActive=?",true));
       return entities;
	}

	@SuppressWarnings("unchecked")
	public List<EntityType> filterActiveEntities(String filterKey, int maxRecords, Integer accountDetailTypeId) {
		Integer pageSize = (maxRecords > 0 ? maxRecords : null);
		  List<EntityType> entities=new ArrayList<EntityType>();
		  filterKey="%"+filterKey+"%";
			String qry="from FundingAgency r where upper(code) like upper(?) or upper(name) like upper(?) and r.isActive=? order by code,name";
			entities.addAll((List<EntityType>)findPageBy(qry, 0, pageSize,filterKey,filterKey,true).getList());
	       return entities;
	}

	/* (non-Javadoc)
	 * @see org.egov.commons.service.EntityTypeService#getAssetCodesForProjectCode(java.lang.Integer)
	 */
	/*@Override
	public List getAssetCodesForProjectCode(Integer accountdetailkey) throws ValidationException {
		
		return null;
	}*/
	 public List<FundingAgency> validateEntityForRTGS(List<Long> idsList) throws ValidationException {
		 
		return null;
		 
	 }
	 public List<FundingAgency> getEntitiesById(List<Long> idsList) throws ValidationException {
		 
		 return null;
	 }
	  
}
