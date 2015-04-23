/**
 * 
 */
package org.egov.masters.services;

import java.util.ArrayList;
import java.util.List;

import org.egov.commons.service.EntityTypeService;
import org.egov.commons.utils.EntityType;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.masters.model.AccountEntity;
import org.hibernate.Query;

/**
 * @author mani
 *
 */
public class AccountEntityService extends PersistenceService<AccountEntity, Integer> implements EntityTypeService{
		public List<EntityType> getAllActiveEntities(Integer accountDetailTypeId) {
	      List<EntityType> entities=new ArrayList<EntityType>();
			entities.addAll(findAllBy("from AccountEntity a where a.isactive=? and accountdetailtype.id=?",true,accountDetailTypeId));
	       return entities;
		}

		@Override
		public List<EntityType> filterActiveEntities(String filterKey, int maxRecords, Integer accountDetailTypeId) {
			Integer pageSize = (maxRecords > 0 ? maxRecords : null);
			  List<EntityType> entities=new ArrayList<EntityType>();
			  filterKey="%"+filterKey+"%";
				String qry="from AccountEntity  where accountdetailtype.id=? and ((upper(code) like upper(?) or upper(name) like upper(?))  and isactive=?)   order by code,name";
				entities.addAll((List<EntityType>)findPageBy(qry,0, pageSize,accountDetailTypeId,filterKey,filterKey,true).getList());
		       return entities;
			
		}
//
//		@Override
//		public List getAssetCodesForProjectCode(Integer accountdetailkey)
//			throws ValidationException {
//		// TODO Auto-generated method stub
//		return null;
//		}
		 public List<EntityType> validateEntityForRTGS(List<Long> idsList) throws ValidationException {
			 
			 return null;
			 
		 }
		 public List<EntityType> getEntitiesById(List<Long> idsList) throws ValidationException {
			
			 List<Integer> ids=new ArrayList<Integer>();
				if(idsList!=null)
				{
					for(Long id:idsList)
					{
						ids.add(id.intValue());
					}
				}
			
			 List<EntityType> entities=new ArrayList<EntityType>();
			 Query entitysQuery = null;//This fix is for Phoenix Migration.HibernateUtil.getCurrentSession().createQuery(" from AccountEntity where id in ( :IDS )");
			 entitysQuery.setParameterList("IDS", ids);
			 entities = entitysQuery.list();
			return entities;
			 
		 }

}
