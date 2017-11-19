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

import org.egov.ptis.domain.entity.property.StructureClassification;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Repository(value = "structureClassificationDAO")
@Transactional(readOnly = true)
public class StructureClassificationHibernateDAO implements StructureClassificationDAO {

	@PersistenceContext
	private EntityManager entityManager;

	private Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	/**
	 * To get all StructuralClassification data
	 * 
	 * @return {@link StructureClassification} List
	 * */
	@Override
	public List getAllStructureClassification() {
		Query qry = getCurrentSession().createQuery(
				"FROM StructureClassification SC WHERE ((SC.toDate IS NULL AND SC.fromDate <= :currDate) "
						+ "OR (SC.fromDate <= :currDate AND SC.toDate >= :currDate))");
		qry.setDate("currDate", new Date());
		return qry.list();
	}

	/**
	 * To get the construction cost by Construction Type Code and Floor Number
	 * 
	 * @param constrTypeCode
	 * @param floorNum
	 * @return {@link StructureClassification} StructureClassification
	 * */
	@Override
	public StructureClassification getStructureClassification(String constrTypeCode,
			Integer floorNum) {
		Query qry = getCurrentSession()
				.createQuery(
						"FROM StructureClassification SC WHERE SC.constrTypeCode =:constrTypeCode AND SC.floorNum =:floorNum");
		qry.setString("constrTypeCode", constrTypeCode);
		qry.setInteger("floorNum", floorNum);
		return (StructureClassification) qry.uniqueResult();
	}

	/**
	 * To get the construction cost by Construction Type Code, From Date and
	 * Floor Number
	 * 
	 * @param constrTypeCode
	 * @param fromDate
	 * @param floorNum
	 * @return {@link StructureClassification} StructureClassification
	 **/
	@Override
	public StructureClassification getStructureClassification(String constrTypeCode, Date fromDate,
			Integer floorNum) {
		Query qry = getCurrentSession()
				.createQuery(
						"FROM StructureClassification SC WHERE SC.constrTypeCode =:constrTypeCode "
								+ "AND SC.floorNum =:floorNum AND ((SC.toDate IS NULL AND SC.fromDate <= :fromDate) "
								+ "OR (SC.fromDate <= :fromDate AND SC.toDate >= :fromDate))");
		qry.setString("constrTypeCode", constrTypeCode);
		qry.setInteger("floorNum", floorNum);
		qry.setDate("fromDate", fromDate);
		return (StructureClassification) qry.uniqueResult();
	}

	/**
	 * To get the construction cost by Construction Type
	 * 
	 * @param constrTypeCode
	 * @return {@link StructureClassification}StructureClassification
	 * */
	@Override
	public StructureClassification getStructureClassification(String constrTypeCode) {
		Query qry = getCurrentSession()
				.createQuery(
						"FROM StructureClassification SC WHERE SC.constrTypeCode =:constrTypeCode "
								+ "AND ((SC.toDate IS NULL AND SC.fromDate <= :currDate) OR (SC.fromDate <= :currDate AND SC.toDate >= :currDate))");
		qry.setString("constrTypeCode", constrTypeCode);
		qry.setDate("currDate", new Date());
		return (StructureClassification) qry.uniqueResult();
	}

	/**
	 * To get the construction cost by Construction Type Code and From Date
	 * 
	 * @param constrTypeCode
	 * @param fromDate
	 * @return {@link StructureClassification} StructureClassification
	 **/
	@Override
	public StructureClassification getStructureClassification(String constrTypeCode, Date fromDate) {
		Query qry = getCurrentSession()
				.createQuery(
						"FROM StructureClassification SC WHERE SC.constrTypeCode =:constrTypeCode "
								+ "AND ((SC.toDate IS NULL AND SC.fromDate <= :fromDate) OR (SC.fromDate <= :fromDate AND SC.toDate >= :fromDate))");
		qry.setString("constrTypeCode", constrTypeCode);
		qry.setDate("fromDate", fromDate);
		return (StructureClassification) qry.uniqueResult();
	}

	@Override
	public StructureClassification findById(Long id, boolean lock) {

		return null;
	}

	@Override
	public List<StructureClassification> findAll() {

		return null;
	}

	@Override
	public StructureClassification create(StructureClassification structureClassification) {

		return null;
	}

	@Override
	public void delete(StructureClassification structureClassification) {


	}

	@Override
	public StructureClassification update(StructureClassification structureClassification) {

		return null;
	}
}
