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
package org.egov.commons.dao;

import java.util.ArrayList;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class EgwStatusHibernateDAO extends GenericHibernateDAO {
	
	public EgwStatusHibernateDAO() {
		super(EgwStatus.class,null);
	}
	
	public EgwStatusHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	public List<EgwStatus> getEgwStatusFilterByStatus(final ArrayList<Integer> statusId) {
		final Query qry = getCurrentSession().createQuery("from EgwStatus egs where egs.id in (:statusId)  order by orderId");
		qry.setParameterList("statusId", statusId);
		return qry.list();
	}

	/**
	 * @param moduleType Module type
	 * @param statusCode Status code
	 * @return EgwStatus object for given module type and status code
	 */
	public EgwStatus getStatusByModuleAndCode(final String moduleType, final String code) {
		final Query qry = getCurrentSession().createQuery("from EgwStatus S where S.moduletype =:moduleType and S.code =:code");
		qry.setString("moduleType", moduleType);
		qry.setString("code", code);
		return (EgwStatus) qry.uniqueResult();
	}

	public List<EgwStatus> getStatusByModule(final String moduleType) {
		final Query qry = getCurrentSession().createQuery("from EgwStatus S where S.moduletype =:moduleType  order by orderId");
		qry.setString("moduleType", moduleType);
		return qry.list();
	}

	/**
	 * @param moduleType Module type
	 * @param codeList List of status codes
	 * @return List of all EgwStatus objects filtered by given module type and list of status codes
	 */
	public List<EgwStatus> getStatusListByModuleAndCodeList(final String moduleType, final List codeList) {
		final Query qry = getCurrentSession().createQuery("from EgwStatus S where S.moduletype =:moduleType and S.code in(:codeList)  order by orderId");
		qry.setString("moduleType", moduleType);
		qry.setParameterList("codeList", codeList);
		return qry.list();
	}

	public EgwStatus getEgwStatusByCode(final String code) {
		final Query qry = getCurrentSession().createQuery("from EgwStatus S where S.code =:code ");
		qry.setString("code", code);
		return (EgwStatus) qry.uniqueResult();
	}
}
