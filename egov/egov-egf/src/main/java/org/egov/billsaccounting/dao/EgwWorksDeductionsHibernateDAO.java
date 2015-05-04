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
package org.egov.billsaccounting.dao;
/*
 * Created on Mar 5, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.util.List;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.billsaccounting.model.EgwWorksDeductions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;
import org.egov.billsaccounting.model.Worksdetail; 
         
/**
 * @author Admin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly=true)
public class EgwWorksDeductionsHibernateDAO extends GenericHibernateDAO
{

	public EgwWorksDeductionsHibernateDAO(final Class persistentClass, final Session session)
	{
			super(persistentClass, session);
	}    
    
     public List<EgwWorksDeductions> getStatutoryDeductionsByWorksdetail(final Worksdetail worksdetail)
     {
         final Query qry =HibernateUtil.getCurrentSession().createQuery("from EgwWorksDeductions ewd where ewd.dedtype = 'S' and ewd.worksdetail=:worksdetail");
         qry.setEntity("worksdetail", worksdetail);
         return qry.list();
     }
      
      public List<EgwWorksDeductions> getNonStatutoryDeductionsByWorksdetail(final Worksdetail worksdetail)
      {
          final Query qry =HibernateUtil.getCurrentSession().createQuery("from EgwWorksDeductions ewd where ewd.dedtype = 'N' and ewd.worksdetail=:worksdetail");
          qry.setEntity("worksdetail", worksdetail);
          return qry.list();
      }

	public List getAllDeductionsByWorkorder(final String worksdetailid) {
		// TODO Auto-generated method stub
		return null;
	}
}
