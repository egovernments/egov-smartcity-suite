/*
 * @(#)EgUomHibernateDAO.java 3.0, 17 Jun, 2013 11:18:18 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.commonMasters.dao;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commonMasters.EgUom;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class EgUomHibernateDAO extends GenericHibernateDAO {

	public EgUomHibernateDAO() {
		super(EgUom.class, null);
	}

	public EgUomHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);
	}

	public List findAllUom() {
		return getCurrentSession().createQuery("from EgUom uom order by uomcategoryid").list();

	}

	public List<EgUom> getAllUomsWithinCategoryByUom(final Integer uomId) throws ValidationException {
		if (uomId != null && uomId >= 0) {
			final Query qry = getCurrentSession().createQuery("from org.egov.infstr.commonMasters.EgUom as uoms where uoms.egUomcategory.id =(select uom.egUomcategory.id  from org.egov.infstr.commonMasters.EgUom uom where uom.id=:uomID)");
			qry.setInteger("uomID", uomId);
			return qry.list();
		} else {
			throw new ValidationException(Arrays.asList(new ValidationError("uom null", "UomId is null")));
		}
	}

	public BigDecimal getConversionFactorByUom(final Integer uomId) throws ValidationException {
		if (uomId != null && uomId > 0) {

			final Query qry = getCurrentSession().createQuery("select uom.convFactor from org.egov.infstr.commonMasters.EgUom as uom where uom.id=:uomID");
			qry.setInteger("uomID", uomId);
			return (BigDecimal) qry.uniqueResult();

		} else {
			throw new ValidationException(Arrays.asList(new ValidationError("uom null", "UomId is null")));
		}
	}

	public BigDecimal getConversionFactorByFromUomToUom(final Integer fromuomId, final Integer touomId) throws ValidationException {
		BigDecimal convFactor = null;

		if (fromuomId != null && fromuomId > 0 && touomId != null && touomId > 0) {
			final BigDecimal fromconvFactor = this.getConversionFactorByUom(fromuomId);
			final BigDecimal toFactor = this.getConversionFactorByUom(touomId);

			convFactor = BigDecimal.valueOf(fromconvFactor.doubleValue() / toFactor.doubleValue());

		} else {
			throw new ValidationException(Arrays.asList(new ValidationError("uom null", "from or to uomId  is null")));
		}

		return convFactor;
	}

}
