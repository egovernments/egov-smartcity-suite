package org.egov.eb.service.master;

import java.util.List;

import org.egov.eb.domain.master.entity.EBConsumer;
import org.egov.eb.domain.master.entity.TargetArea;
import org.egov.infstr.utils.HibernateUtil;

public class TargetAreaService{
	//extends PersistenceService<TargetArea, Long> {
	//This fix is for Phoenix Migration.

	public List<TargetArea> getAllTargetAreas()
	{
		return null;//findAllBy("from TargetArea order by name");
		
	}
	
	@SuppressWarnings("unchecked")
	public boolean isCodeUnique(String code, Long id)
	{
		boolean isUnique = true;
		if (code!=null  && !code.isEmpty()  && null!= id)
		{
			 List<Object[]> list =HibernateUtil.getCurrentSession().createQuery(" select 'false' from TargetArea where upper(code)=:code and id!=:id")					 		     
			 .setString("code", code.toUpperCase())
			 .setLong("id", id).list();
			 if(list.isEmpty())
			 {
				 isUnique=true;
			 }
			 else  
			 {
				 isUnique = false;
			 }
			 
		}
		else if (code!=null && !code.isEmpty()  )
		{
			 List<Object[]> list =HibernateUtil.getCurrentSession().createQuery(" select 'false' from TargetArea where upper(code)=:code")
					 .setString("code", code.toUpperCase()).list();
			 if(list.isEmpty())
			 {
				 isUnique = true;
			 }
			 else
			 {
				 isUnique = false;
			 }
		}
		return isUnique;
	}
	
	/**
	 * Gives the TargetArea by area name
	 * 
	 * @param areaName
	 * @return TargetArea
	 */
	public TargetArea getTargetAreaByAreaName(String areaName) { 
		return (TargetArea)HibernateUtil.getCurrentSession().createQuery("from TargetArea where name = :areaName")
				.setString("areaName", areaName)
				.list();
	}
	
	/**
	 * Gives the TargetArea by Consumer
	 * 
	 * @param ebConsumer
	 * @return TargetArea
	 */
	public TargetArea getTargetAreaByConsumer(EBConsumer ebConsumer) {
		@SuppressWarnings("unchecked")
		List<TargetArea> areas =HibernateUtil.getCurrentSession().createQuery("select tam.area from EBConsumer c, TargetAreaMappings tam " +
				"where c.ward.id = tam.boundary.id " +
				"and c.ward.id = :wardId")
				//.setInteger("wardId", ebConsumer.getWard().getId())
				.list();
		
		if (areas.isEmpty()) {
			return null;
		}
		
		return (TargetArea) areas.get(0);
	}
}
