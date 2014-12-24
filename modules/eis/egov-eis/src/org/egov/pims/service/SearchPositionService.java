package org.egov.pims.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.pims.commons.Position;
import org.egov.pims.dao.EisDAOFactory;
import org.egov.pims.dao.PersonalInformationDAO;
import org.egov.pims.dao.PersonalInformationHibernateDAO;
import org.egov.pims.model.EmployeeView;
import org.hibernate.HibernateException;
import org.hibernate.Query;

public class SearchPositionService {

	private static final Logger logger = Logger.getLogger(SearchPositionService.class);

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
	public  List<EmployeeView> getPositionBySearchParameters(String beginsWith,Integer desId,Integer deptId,Integer jurdId,Integer roleId,Date userDate,Integer maxResults) throws NoSuchObjectException{
		List<EmployeeView> posList = new ArrayList<EmployeeView>() ;
		logger.debug("inside getPositionBySearchParameters method ***********"+desId);
		String searchQuery="";
		List userListInJur=null;
		String myBeginsWith = beginsWith;
		if (myBeginsWith == null)
			myBeginsWith = "";
		if(jurdId!=null)
		{

			PersonalInformationDAO personalDAO = EisDAOFactory.getDAOFactory().getPersonalInformationDAO();
			userListInJur = personalDAO.getListOfUsersByBoundaryId(jurdId);

		}
		try
		{

			searchQuery="Select EV from EmployeeView EV,Position P where " +
			
			"EV.position.id=P.id  and "+
			"trim(upper(P.name))  like '"+myBeginsWith.trim().toUpperCase()+"%' and "+
			" ((EV.toDate IS NULL AND EV.fromDate <= :userDate)OR(EV.fromDate <= :userDate AND EV.toDate >= :userDate))and EV.isActive ='1'";
		
			

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
				searchQuery += " and EV.desigId.designationId = :desId  ";
			if(deptId!= null&& deptId.intValue() != 0)
				searchQuery +=" and EV.deptId.id= :deptId ";
			if(roleId!=null && roleId!=null)
			{
				//FIXME: add isHistory check
				searchQuery+=" and EV.userMaster.id IN ( Select U.user.id from UserRole U where U.role.id=:roleId and U.isHistory='N'and "+
				"((U.toDate IS NULL AND U.fromDate <= :userDate)OR(U.fromDate <= :userDate AND U.toDate > :userDate)))" ;


				//searchQuery +=" EV.userid = userrole.user and userrole.roleid = :roleId and userrole.from";
			}

			Query query =HibernateUtil.getCurrentSession().createQuery(searchQuery);
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
			
			throw new EGOVRuntimeException("Exception:" + h.getMessage(),h);
		}
		return  posList;
	}
}
