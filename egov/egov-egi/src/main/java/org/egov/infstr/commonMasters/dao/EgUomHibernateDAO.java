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
