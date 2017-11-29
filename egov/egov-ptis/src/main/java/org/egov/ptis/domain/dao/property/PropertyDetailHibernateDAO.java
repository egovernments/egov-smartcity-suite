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
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository(value = "propertyDetailDAO")
@Transactional(readOnly = true)
public class PropertyDetailHibernateDAO implements PropertyDetailDAO {

	private static final Logger LOGGER = Logger.getLogger(PropertyDetailHibernateDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	private Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	@Autowired
	private BasicPropertyDAO basicPropertyDAO;

	/*
	 * public PropertyDetail getPropertyDetailByPropertyDetailsID(String
	 * PropertyDetailsID) { Query qry =getSession().createQuery(
	 * "from PropertyDetail PD where pd.PropertyDetailsID =: PropertyDetailsID "
	 * ); qry.setString("PropertyDetailsID", PropertyDetailsID); return
	 * (PropertyDetail)qry.uniqueResult(); }
	 */

	@Override
	public PropertyDetail getPropertyDetailByProperty(Property property) {
		Query qry = getCurrentSession().createQuery(
				"from PropertyDetail PD where PD.property = :property ");
		qry.setEntity("property", property);
		return (PropertyDetail) qry.uniqueResult();
	}

	@Override
	public PropertyDetail getPropertyDetailBySurveyNumber(String surveyNumber) {
		Query qry = getCurrentSession().createQuery(
				"from PropertyDetail PD where PD.property.surveyNumber =: SURVEY_NUM ");
		qry.setString("surveyNumber", surveyNumber);
		return (PropertyDetail) qry.uniqueResult();
	}

	@Override
	public PropertyDetail getPropertyDetailByRegNum(String regNum) {
		LOGGER.info("getPropertyDetailByRegNum Invoked");

		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByRegNum(regNum);

		LOGGER.info("basicProperty : " + basicProperty);

		Query qry = getCurrentSession().createQuery(
				"from PropertyImpl P where P.basicProperty = :BasicProperty ");
		qry.setEntity("BasicProperty", basicProperty);
		Property property = (Property) qry.uniqueResult();
		LOGGER.info("property : " + property);
		Query qry1 = getCurrentSession().createQuery(
				"from PropertyDetail PD where PD.property =:Property ");
		qry1.setEntity("Property", property);

		return (PropertyDetail) qry1.uniqueResult();
	}

	@Override
	public PropertyDetail findById(Integer id, boolean lock) {

		return null;
	}

	@Override
	public List<PropertyDetail> findAll() {

		return null;
	}

	@Override
	public PropertyDetail create(PropertyDetail propertyDetail) {

		return null;
	}

	@Override
	public void delete(PropertyDetail propertyDetail) {


	}

	@Override
	public PropertyDetail update(PropertyDetail propertyDetail) {

		return null;
	}

}
