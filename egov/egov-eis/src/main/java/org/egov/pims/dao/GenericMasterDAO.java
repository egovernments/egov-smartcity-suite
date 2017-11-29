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
package org.egov.pims.dao;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.pims.model.GenericMaster;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
/**
 * @author deepak
 *
 */
@Service
@Transactional
public class GenericMasterDAO implements Serializable
{
	@PersistenceContext
	private EntityManager entityManager;
    
	public Session  getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	public void create(GenericMaster genericMaster) 
	{
		try
		{
			getCurrentSession().save(genericMaster);

		}
		catch (HibernateException e)
		{
			
			throw  new ApplicationRuntimeException(STR_HIB_EXP+e.getMessage(),e);
		}

	}

	public void update(GenericMaster genericMaster)
	{
		try
		{
			getCurrentSession().saveOrUpdate(genericMaster);
		}
		catch (HibernateException e)
		{
			throw  new ApplicationRuntimeException(STR_HIB_EXP+e.getMessage(),e);

		}

	}

	public void remove(GenericMaster genericMaster)
	{
		try
		{
			getCurrentSession().delete(genericMaster);
		}
		catch (HibernateException e)
		{
			
			throw  new ApplicationRuntimeException(STR_HIB_EXP+e.getMessage(),e);

		}

	}

	public GenericMaster getGenericMaster(int Id,String className)
	{
		try
		{
			String srt = "org.egov.pims.model."+className;
			GenericMaster imp = (GenericMaster)getCurrentSession().get(Class.forName(srt), Integer.valueOf(Id));

			return imp ;
		}catch (HibernateException e)
		{
			throw  new ApplicationRuntimeException(STR_HIB_EXP+e.getMessage(),e);

		}
		catch (ClassNotFoundException e)
		{
				
			throw  new ApplicationRuntimeException(STR_HIB_EXP+e.getMessage(),e);

		}


	}
	
	private final static String STR_HIB_EXP = "Hibernate Exception : ";


}
