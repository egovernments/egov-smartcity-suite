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
package org.egov.ptis.domain.dao.property;

import org.apache.log4j.Logger;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository(value = "propertyIDDAO")
@Transactional(readOnly = true)
public class PropertyIDHibernateDAO implements PropertyIDDAO {
	private final static Logger LOGGER = Logger.getLogger(PropertyIDHibernateDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	private Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	@Override
	public PropertyID getPropertyIDByUPICNo(String upicNo) {
		Query qry = getCurrentSession().createQuery(
				"from PropertyID PD where PD.id_PropertyId =:upicNo");
		qry.setString("upicNo", upicNo);
		return (PropertyID) qry.uniqueResult();
	}

	@Override
	public List getPropertyIDByBoundry(Integer zoneID, Integer wardID, Integer colonyID) {
		LOGGER.info(">>>>>>>>>>>>>>>>>> colonyId" + zoneID + ":::::::" + wardID + ":::::::::::"
				+ colonyID);
		Query qry = getCurrentSession()
				.createQuery(
						"from PropertyID PD where PD.zoneId=:zoneID And PD.wardId=:wardID And PD.colonyId =:colonyID");
		qry.setInteger("zoneID", zoneID);
		qry.setInteger("wardID", wardID);
		qry.setInteger("colonyID", colonyID);
		LOGGER.info(">>>>>>>>>>>>>>>>>> After Qry");
		return qry.list();
		// return (PropertyID)qry.uniqueResult();
	}

	@Override
	public List getPropertyIDByBoundryForWardBlockStreet(Integer wardID, Integer blockID,
			Integer streetID) {
		LOGGER.info(">>>>>>>>>>>>>>>>>> streetID" + wardID + ":::::::" + blockID + ":::::::::::"
				+ streetID);
		Query qry = getCurrentSession()
				.createQuery(
						"from PropertyID PD where PD.wardId=:wardID And PD.blockId=:blockID And PD.streetId =:streetID");
		qry.setInteger("wardID", wardID);
		qry.setInteger("blockID", blockID);
		qry.setInteger("streetID", streetID);
		LOGGER.info(">>>>>>>>>>>>>>>>>> After Qry");
		return qry.list();
		// return (PropertyID)qry.uniqueResult();
	}

	@Override
	public PropertyID getPropertyByBoundryAndMunNo(Integer zoneID, Integer wardID,
			Integer colonyID, Integer munNo) {
		LOGGER.info(">>>>>>>>>>>>>>>>>> colonyId" + colonyID + ":::::::::::::::munNo:" + munNo);
		Query qry = getCurrentSession()
				.createQuery(
						"from PropertyID PD where PD.zoneId=:zoneID And PD.wardId=:wardID And PD.colonyId =:colonyID and PD.doorNum =:munNo");
		qry.setInteger("zoneID", zoneID);
		qry.setInteger("wardID", wardID);
		qry.setInteger("colonyID", colonyID);
		qry.setInteger("munNo", munNo);
		return (PropertyID) qry.uniqueResult();

	}

	@Override
	public PropertyID findById(Integer id, boolean lock) {

		return null;
	}

	@Override
	public List<PropertyID> findAll() {

		return null;
	}

	@Override
	public PropertyID create(PropertyID propertyID) {

		return null;
	}

	@Override
	public void delete(PropertyID propertyID) {


	}

	@Override
	public PropertyID update(PropertyID propertyID) {

		return null;
	}
}
