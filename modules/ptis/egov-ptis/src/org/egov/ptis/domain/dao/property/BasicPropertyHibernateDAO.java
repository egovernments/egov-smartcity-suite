/*
 * BasicPropertyHibernateDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;

/**
 * This Class implememets the BasicPropertyDAO for the Hibernate specific
 * Implementation
 * 
 * @author Neetu
 * @version 2.00
 */

public class BasicPropertyHibernateDAO extends GenericHibernateDAO implements BasicPropertyDAO {
	private final static Logger LOGGER = Logger.getLogger(BasicPropertyHibernateDAO.class);

	/**
	 * @param persistentClass
	 * @param session
	 */
	public BasicPropertyHibernateDAO(Class persistentClass, Session session) {
		super(persistentClass, session);
	}

	public BasicProperty getBasicPropertyByRegNum(String RegNum) {
		// logger.info(">>>>>>>>>>>>>>>>>> RegNum"+RegNum);
		Query qry = getSession().createQuery("from BasicPropertyImpl BP where BP.regNum =:RegNum and BP.active='Y' ");

		qry.setString("RegNum", RegNum);
		// qry.setMaxResults(1);
		return (BasicProperty) qry.uniqueResult();
	}

	public BasicProperty getBasicPropertyByRegNumNew(String RegNum) {
		Query qry = getSession().createQuery(
				"from BasicPropertyImpl BP "
						+ "left join fetch BP.property pi "
						+ "left join fetch BP.ptAgent pangt "
						+ "left join fetch pangt.basicProperty bppangt "
						+ "left join fetch BP.address add "
						+
						// "left join fetch BP.propertyID pid " +
						// "left join fetch pid.basicProperty bpid " +
						"left join fetch pi.ptdcbBroker ptdcb " + "left join fetch pi.propertyOwnerSet prowns  "
						+ "left join fetch ptdcb.dcb dcb " + "left join fetch dcb.currentDemand currDmd "
						+ "left join fetch dcb.aggArrearsDemand arrDmd " + "left join fetch arrDmd.dcb "
						+ "left join fetch currDmd.dcb " + "left join fetch currDmd.cesses "
						+ "left join fetch currDmd.penalties " + "left join fetch currDmd.exemptions "
						+ "left join fetch arrDmd.cesses " + "left join fetch arrDmd.penalties "
						+ "left join fetch arrDmd.exemptions " + "where BP.regNum =:RegNum and BP.active='Y' ");
		qry.setString("RegNum", RegNum);
		// qry.setMaxResults(1);
		return (BasicProperty) qry.uniqueResult();
	}

	public BasicProperty getBasicPropertyByPropertyID(String propertyId) {
		Query qry = null;
		BasicProperty basicProperty = null;
		if (propertyId != null && !propertyId.equals("")) {
			qry = getSession().createQuery("from BasicPropertyImpl BP where BP.upicNo =:propertyId and BP.active='Y' ");
			qry.setString("propertyId", propertyId);
			basicProperty = (BasicProperty) qry.uniqueResult();
		}
		// qry.setMaxResults(1);
		return basicProperty;
	}

	public BasicProperty getAllBasicPropertyByPropertyID(String propertyId) {
		Query qry = null;
		BasicProperty basicProperty = null;
		if (propertyId != null && !propertyId.equals("")) {
			qry = getSession().createQuery("from BasicPropertyImpl BP where BP.upicNo =:propertyId");
			qry.setString("propertyId", propertyId);
			basicProperty = (BasicProperty) qry.uniqueResult();
		}
		// qry.setMaxResults(1);
		return basicProperty;
	}

	public BasicProperty getBasicPropertyByPropertyID(PropertyID propertyID) {

		Query qry = getSession().createQuery(
				"from BasicPropertyImpl BP where BP.propertyID =:PropertyID and BP.active='Y' ");
		qry.setEntity("PropertyID", propertyID);
		return (BasicProperty) qry.uniqueResult();
	}

	/*
	 * public BasicProperty getBasicPropertyByID(String ID) { Query qry =
	 * getSession().createQuery("from BasicProperty BP where bp.ID =: ID and
	 * BP.active='Y' "); qry.setString("ID", ID); return
	 * (BasicProperty)qry.uniqueResult(); }
	 */

	public BasicProperty getInActiveBasicPropertyByPropertyID(String propertyId) {
		Query qry = null;
		BasicProperty basicProperty = null;
		if (propertyId != null && !propertyId.equals("")) {
			qry = getSession().createQuery("from BasicPropertyImpl BP where BP.upicNo =:propertyId and BP.active='N' ");
			qry.setString("propertyId", propertyId);
			basicProperty = (BasicProperty) qry.uniqueResult();
		}
		return basicProperty;
	}

	public BasicProperty getBasicPropertyByID_PropertyID(String ID_PropertyID) {
		Query qry = getSession().createQuery(
				"from BasicPropertyImpl BP where bp.ID_PropertyID =:ID_PropertyID  and BP.active='Y'");
		qry.setString("ID_PropertyID", ID_PropertyID);
		return (BasicProperty) qry.uniqueResult();
	}

	public Integer getRegNum() {
	    //TODO POSTGRESQL
		return (Integer) getSession().createSQLQuery("SELECT REG_NUM.NEXTVAL as regnum from dual")
		.addScalar("regnum", StandardBasicTypes.INTEGER).uniqueResult();
	}

	public Integer getVoucherNum() {
        //TODO POSTGRESQL
	    return (Integer) getSession().createSQLQuery("SELECT SEQ_VOUCHER_NUM.NEXTVAL as vouchnum from dual")
        .addScalar("vouchnum", StandardBasicTypes.INTEGER).uniqueResult();
	}

	/*
	 * added by suhasini by passing oldMuncipalNo as parameter this method will
	 * give list of BasicProeprty Objects.
	 */
	public List getBasicPropertyByOldMunipalNo(String oldMuncipalNo) {
		// logger.info(">>>>>>>>>>>>>>>>>> oldMuncipalNo"+oldMuncipalNo);
		Query qry = getSession().createQuery(
				"from BasicPropertyImpl BP where BP.oldMuncipalNum =:oldMuncipalNo and BP.active='Y' ");
		qry.setString("oldMuncipalNo", oldMuncipalNo);
		// qry.setMaxResults(1);
		return (List) qry.list();
	}

	public List<BasicPropertyImpl> getChildBasicPropsForParent(BasicProperty basicProperty) {
		List<BasicPropertyImpl> basicPropList = new ArrayList<BasicPropertyImpl>();
		if (basicProperty != null) {
			Query qry = getSession()
					.createQuery(
							"from BasicPropertyImpl BP left join fetch BP.propertyStatusValuesSet PSV left join fetch PSV.propertyStatus PS where PSV.referenceBasicProperty =:BasicPropertyId and PS.statusCode = 'CREATE' and PSV.isActive='Y' ");
			qry.setString("BasicPropertyId", basicProperty.getId().toString());
			basicPropList = qry.list();
		}
		return basicPropList;
	}

	public BasicProperty getBasicPropertyByIndexNumAndParcelID(String indexNum, String parcelID) {
		Query qry = null;
		BasicProperty basicProperty = null;
		Boolean indexFound = Boolean.FALSE;
		boolean parcelFound = Boolean.FALSE;
		StringBuffer strquery = new StringBuffer(200);
		strquery.append("from BasicPropertyImpl BP where BP.active='Y' ");
		if (indexNum != null && !indexNum.equals("")) {
			indexFound = Boolean.TRUE;
			strquery.append(" and BP.upicNo=:indexNum");
		}
		if (parcelID != null && !parcelID.equals("")) {
			parcelFound = Boolean.TRUE;
			strquery.append(" and BP.gisReferenceNo =:parcelID");
		}
		qry = getSession().createQuery(strquery.toString());
		if (indexFound) {
			qry.setString("indexNum", indexNum);
		}
		if (parcelFound) {
			qry.setString("parcelID", parcelID);
		}
			basicProperty = (BasicProperty) qry.uniqueResult();
		return basicProperty;
	}

}
