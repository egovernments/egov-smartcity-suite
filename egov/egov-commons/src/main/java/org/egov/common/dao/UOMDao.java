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

package org.egov.common.dao;

import org.egov.common.entity.UOM;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Transactional(readOnly=true)
public class UOMDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public List <UOM>findAllUom() {
		final Query qry = entityManager.unwrap(Session.class).createQuery("from EgUom uom order by uomcategoryid");
		return qry.list();

	}

	public List<UOM> getAllUomsWithinCategoryByUom(final Integer uomId) throws ValidationException {
		List<UOM> uoms = new ArrayList<UOM>();
		Query qry = null;
		if (uomId != null && uomId >= 0) {

			qry = entityManager.unwrap(Session.class).createQuery(
							"from org.egov.common.entity.UOM as uoms where uoms.uomCategory.id =(select uom.uomCategory.id  from org.egov.common.entity.UOM uom where uom.id=:uomID)");
			qry.setInteger("uomID", uomId);
			uoms = qry.list();
		} else
			throw new ValidationException(Arrays.asList(new ValidationError("uom null", "UomId is null")));
		return uoms;
	}

	public BigDecimal getConversionFactorByUom(final Integer uomId) throws ValidationException {
		BigDecimal convFactor = BigDecimal.ZERO;
		Query qry = null;
		if (uomId != null && uomId > 0) {

			qry = entityManager.unwrap(Session.class).createQuery(
					"select uom.convFactor from org.egov.infstr.commonMasters.EgUom as uom where uom.id=:uomID");
			qry.setInteger("uomID", uomId);
			convFactor = (BigDecimal) qry.uniqueResult();

		} else
			throw new ValidationException(Arrays.asList(new ValidationError("uom null", "UomId is null")));

		return convFactor;
	}

	public BigDecimal getConversionFactorByFromUomToUom(final Integer fromuomId, final Integer touomId)
			throws ValidationException {
		BigDecimal convFactor = null;

		if (fromuomId != null && fromuomId > 0 && touomId != null && touomId > 0) {
			final BigDecimal fromconvFactor = getConversionFactorByUom(fromuomId);
			final BigDecimal toFactor = getConversionFactorByUom(touomId);

			convFactor = BigDecimal.valueOf(fromconvFactor.doubleValue() / toFactor.doubleValue());

		} else
			throw new ValidationException(Arrays.asList(new ValidationError("uom null", "from or to uomId  is null")));

		return convFactor;
	}

}
