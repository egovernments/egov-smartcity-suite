package org.egov.eb.service.master;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.commons.utils.EntityType;
import org.egov.eb.domain.master.entity.EBConsumer;
import org.egov.eb.utils.EBConstants;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.commons.Position;
import org.hibernate.Query;

public class EBConsumerService {
//extends PersistenceService<EBConsumer, Long>  implements EntityTypeService {

	private static final String FALSE = "false";
	private static final boolean TRUE = false;

	/*@Override
	public List<EntityType> getAllActiveEntities(
			Integer accountDetailTypeId) {
		List<EntityType> entities=new ArrayList<EntityType>();
		entities.addAll(findAllBy("from EBConsumer r where r.isActive=?",true));
       return entities;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<? extends EntityType> filterActiveEntities(String filterKey,
			int maxRecords, Integer accountDetailTypeId) {
		Integer pageSize = (maxRecords > 0 ? maxRecords : null);
		  List<EntityType> entities=new ArrayList<EntityType>();
		  filterKey="%"+filterKey+"%";
			String qry="from EBConsumer r where upper(code) like upper(?) or upper(name) like upper(?) and r.isActive=? order by code,name";
			entities.addAll((List<EntityType>)findPageBy(qry, 0, pageSize,filterKey,filterKey,true).getList());
	       return entities;
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
	public List<EBConsumer> getEntitiesById(List<Long> idsList)
			throws ValidationException {
		 
		List<EBConsumer> entities=null;
		 Query entitysQuery =HibernateUtil.getCurrentSession().createQuery(" from EBConsumer where id in ( :IDS )");
		 entitysQuery.setParameterList("IDS", idsList);
		 entities = entitysQuery.list();
		return entities;
		 
	}*/
	//This fix is for Phoenix Migration.
	public boolean isNameUnique(String name, Long id)
	{
		boolean isunique = false;
		if (name!=null  && !name.isEmpty()  && null!= id)
		{
			 List<Object[]> list =HibernateUtil.getCurrentSession().createQuery(" select 'false' from EBConsumer where upper(name)=:name and id!=:id")
			 .setString("name", name.toUpperCase())
			 .setLong("id", id).list();
			 if(list.isEmpty())
			 {
				 isunique=true;
			 }
			 else
			 {
				 isunique = false;
			 }
			 
		}
		else if (name!=null  && !name.isEmpty())
		{
			 List<Object[]> list =HibernateUtil.getCurrentSession().createQuery(" select 'false' from EBConsumer where upper(name)=:name")
					 .setString("name", name.toUpperCase()).list();
			 if(list.isEmpty())
			 {
				 isunique = true;
			 }
			 else
			 {
				 isunique = false;
			 }
		}
		return isunique;
	}
	
	public boolean isCodeUnique(String code, Long id)
	{
		boolean isunique = false;
		if (code!=null  && !code.isEmpty()  && null!= id)
		{
			 List<Object[]> list =HibernateUtil.getCurrentSession().createQuery(" select 'false' from EBConsumer where upper(code)=:code and id!=:id")
			 .setString("code", code.toUpperCase())
			 .setLong("id", id).list();
			 if(list.isEmpty())
			 {
				 isunique=true;
			 }
			 else
			 {
				 isunique = false;
			 }
			 
		}
		else if (code!=null  && !code.isEmpty())
		{
			 List<Object[]> list =HibernateUtil.getCurrentSession().createQuery(" select 'false' from EBConsumer where upper(code)=:code")
					 .setString("code", code.toUpperCase()).list();
			 if(list.isEmpty())
			 {
				 isunique = true;
			 }
			 else
			 {
				 isunique = false;
			 }
		}
		return isunique;
	}
	
	/**
	 * Gives the list of <code> EBConsumer </code> for the given billing cycle
	 * 
	 * @param billingCycle
	 * @return list of <code> EBConsumer </code>
	 */

	@SuppressWarnings("unchecked")
	public List<EBConsumer> getAllConsumersByBillingCycle(String billingCycle) {
			return HibernateUtil.getCurrentSession().createQuery(" select e from EBConsumer e  where not exists (select ebConsumer.id  from EBDetails  d where  " +
					" extract(MONTH from d.createdDate)=extract(MONTH  from sysdate) and d.status.description  not in (:cancelledstatus) and e.id=d.ebConsumer.id )" +
					" and not exists (select eblogdet.ebConsumer.id  from EbSchedulerLogDetails eblogdet  where " +
					" trunc(eblogdet.ebSchedulerLog.createdDate)=trunc(sysdate) and e.id=eblogdet.ebConsumer.id) "+
					" and e.isActive = true and ward is not null and oddOrEvenBilling = :cycle")
					.setString("cancelledstatus", EBConstants.CODE_BILLINFO_CANCELLED)
					.setString("cycle", billingCycle)	                     				
					.list();
	}
	
	/**
	 * Gives the mapped <code> Position </code> to the <code> TargetArea </code> for the given 
	 * </code> EBConsumer </code>
	 * 
	 * @param consumer
	 * @return Mapped position <code> Position </code>
	 * 
	 * @see org.egov.pims.commons.Position
	 */
	@SuppressWarnings("unchecked")
	public Position getTargetAreaPositionForConsumer(EBConsumer consumer) {
		List<Position> positions =HibernateUtil.getCurrentSession().createQuery("select tam.area.position from EBConsumer c, TargetAreaMappings tam " +
				"where c.ward.id = tam.boundary.id " +
				"and c.ward.id = :wardId")
				//.setInteger("wardId", consumer.getWard().getId())
				.list();
		
		if (positions.isEmpty()) {
			return null;
		}
		
		return (Position) positions.get(0);
	}
	
	/**
	 * Gives the consumer details mapped target area and position details
	 * There is no need to join ebdetail table here 
	 * 
	 * @return map of consumer number with list containing target area and position
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<Object>> getConsumerDetails(String billingCycle) {
		List<Object> result =HibernateUtil.getCurrentSession().createQuery("select c.code, tam.area, tam.area.position from EBConsumer c, " +
				"TargetAreaMappings tam " +
				"where c.ward.id = tam.boundary.id " +
				"and c.isActive = true " +
				"and c.ward is not null " +
				"and c.oddOrEvenBilling = :cycle " +
				"")
				.setString("cycle", billingCycle)
				.list();
		
		Map<String, List<Object>> detailsByConsumer = new LinkedHashMap<String, List<Object>>();
		List<Object> details = null;
		
		if (result.isEmpty()) {
			return null;
		} else {
			
			for (Object object : result) {
				Object[] objects = (Object[]) object;
				details = new ArrayList<Object>();
				details.add(objects[1]); // TargetArea
				details.add(objects[2]); // Position
				//details.add(objects[3]); // EBDetails
				detailsByConsumer.put((String)objects[0], details);
			}
			
		}
		
		return detailsByConsumer;
	}
}
