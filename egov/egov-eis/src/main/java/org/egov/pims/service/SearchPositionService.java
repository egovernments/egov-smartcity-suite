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
package org.egov.pims.service;

import org.apache.log4j.Logger;
import org.egov.commons.exception.NoSuchObjectException;
import org.egov.eis.entity.EmployeeView;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.pims.dao.PersonalInformationDAO;
import org.egov.pims.dao.PersonalInformationHibernateDAO;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SearchPositionService {

	private static final Logger logger = Logger.getLogger(SearchPositionService.class);

	@PersistenceContext
	private EntityManager entityManager;
    
	public Session  getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}
	
	/**
	 * 
	 * @param beginsWith
	 * @param desId
	 * @param deptId
	 * @param jurdId
	 * @param roleId
	 * @param userDate
	 * @param maxResults - if -1 all results are returned.
	 * @return
	 * @throws NoSuchObjectException 
	 */
	public  List<EmployeeView> getPositionBySearchParameters(String beginsWith,Integer desId,Integer deptId,Long jurdId,Integer roleId,Date userDate,Integer maxResults) throws NoSuchObjectException{
		List<EmployeeView> posList = new ArrayList<EmployeeView>() ;
		logger.debug("inside getPositionBySearchParameters method ***********"+desId);
		String searchQuery="";
		List userListInJur=null;
		String myBeginsWith = beginsWith;
		if (myBeginsWith == null)
			myBeginsWith = "";
		if(jurdId!=null)
		{

			PersonalInformationDAO personalDAO = new PersonalInformationHibernateDAO();
			//userListInJur = personalDAO.getListOfUsersByBoundaryId(jurdId);

		}
		try
		{

			searchQuery="Select EV from EmployeeView EV,Position P where " +
			
			"EV.position.id=P.id  and "+
			"trim(upper(P.name))  like '"+myBeginsWith.trim().toUpperCase()+"%' and "+
			" ((EV.toDate IS NULL AND EV.fromDate <= :userDate)OR(EV.fromDate <= :userDate AND EV.toDate >= :userDate))and EV.userActive ='1'";
		
			

			//Jurisdiction J,JurisdictionValues JurVal,
			if(userListInJur!=null && !userListInJur.isEmpty())
			{
				/*searchQuery+="  and EV.userMaster.allJurisdictions.id in (Select J.id from Jurisdiction J,JurisdictionValues JurVal " +
				" where JurVal.boundary in (:bndryObjList) and JurVal.userJurLevel.id=J.id and " +
				"J.user.id = EV.userMaster.id and JurVal.isHistory='N' and (" +
		"(JurVal.toDate IS NULL and JurVal.fromDate <= :userDate) " +
		"OR " +
		"(JurVal.fromDate <= :userDate and JurVal.toDate >= :userDate)))  " ;*/
				searchQuery+= "and EV.userMaster in (:bndryObjList)    "; 
			}
			if(desId!= null&& desId.intValue() != 0)
				searchQuery += " and EV.designation.id = :desId  ";
			if(deptId!= null&& deptId.intValue() != 0)
				searchQuery +=" and EV.department.id= :deptId ";
			if(roleId!=null && roleId!=null)
			{
				//FIXME: add isHistory check
				searchQuery+=" and EV.employee.id IN ( Select U.user.id from UserRole U where U.role.id=:roleId and U.isHistory='N'and "+
				"((U.toDate IS NULL AND U.fromDate <= :userDate)OR(U.fromDate <= :userDate AND U.toDate > :userDate)))" ;


				//searchQuery +=" EV.userid = userrole.user and userrole.roleid = :roleId and userrole.from";
			}

			Query query =getCurrentSession().createQuery(searchQuery);
			logger.info("quey >>>"+query.toString());
			
			if(userListInJur!=null && !userListInJur.isEmpty())
			{
				query.setParameterList("bndryObjList",userListInJur);
			}
			if(desId!= null&& desId.intValue() != 0)
			{
				query.setInteger("desId",desId);
			}
			if(deptId!= null&& deptId.intValue() != 0)
			{
				query.setInteger("deptId",deptId);
			}

			if(userDate!=null)
			{
				query.setDate("userDate",userDate);
			}
			if(roleId!=null && roleId!=null)
			{
				query.setInteger("roleId",roleId);
			}
			posList=(List)query.list();
		}
		catch(HibernateException h)
		{
			
			throw new ApplicationRuntimeException("Exception:" + h.getMessage(),h);
		}
		return  posList;
	}
}
