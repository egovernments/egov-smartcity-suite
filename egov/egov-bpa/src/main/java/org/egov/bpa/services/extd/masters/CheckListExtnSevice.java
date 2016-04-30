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
package org.egov.bpa.services.extd.masters;


import org.egov.bpa.models.extd.masters.CheckListDetailsExtn;
import org.egov.bpa.models.extd.masters.ChecklistExtn;
import org.egov.infstr.services.PersistenceService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Transactional(readOnly=true)
public class CheckListExtnSevice extends PersistenceService<ChecklistExtn, Long>
	{
		
		
		@SuppressWarnings("rawtypes")
		private PersistenceService persistenceService;
		ChecklistExtn checkList=new ChecklistExtn();
		private List<CheckListDetailsExtn> checkListDetailsList = new ArrayList<CheckListDetailsExtn>(0);
		
		
		
		@Transactional
		public ChecklistExtn save(ChecklistExtn checkList,List<CheckListDetailsExtn> checkListDetailList)
		{
			
			
			checkList.setModifiedDate(new Date());
			checkList=buildFeeDetails(checkList,checkListDetailList);
			checkList.getId();
			if(checkList.getId()!=null)
				checkList=merge(checkList);			
			else
				checkList=persist(checkList);
			
		
			return checkList;
		}

		private ChecklistExtn buildFeeDetails(ChecklistExtn checkList,List<CheckListDetailsExtn> unitDetail)
		{
			Set<CheckListDetailsExtn> unitSet=new HashSet<CheckListDetailsExtn>();
	    	for(CheckListDetailsExtn unitdetail:unitDetail)
	    	{
	    		unitdetail.setCheckList(checkList);	    		
	    		unitSet.add(unitdetail);
	    	}
	    	
	    	checkList.getCheckListDetailsSet().clear();
	    	if(unitDetail.isEmpty()){
	    		checkList.getCheckListDetailsSet().remove(unitDetail);
	       	 }
	    	else
	    	checkList.getCheckListDetailsSet().addAll(unitSet);
	    	
	    	return checkList;
		}


		public boolean checkCode(String CheckId,Long ServiceId) 
		{
	    	ChecklistExtn relation = null;
			boolean nameExistsOrNot = false;
			if(CheckId!="-1" && !"".equals(CheckId) && ServiceId!=-1 && !"".equals(ServiceId))
				relation =(ChecklistExtn)persistenceService.find("from ChecklistExtn where checklistType=? and serviceType.id=? " ,CheckId,ServiceId);
				if(relation!=null) {
				nameExistsOrNot=true;
				
			}
			return nameExistsOrNot;
		}

		
		public PersistenceService getPersistenceService() {
			return persistenceService;
		}

		public void setPersistenceService(PersistenceService persistenceService) {
			this.persistenceService = persistenceService;
		}

		public List<CheckListDetailsExtn> getCheckListDetailsList() {
			return checkListDetailsList;
		}

		public void setCheckListDetailsList(List<CheckListDetailsExtn> checkListDetailsList) {
			this.checkListDetailsList = checkListDetailsList;
		}

}
