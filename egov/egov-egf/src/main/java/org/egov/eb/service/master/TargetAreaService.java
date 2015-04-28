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
