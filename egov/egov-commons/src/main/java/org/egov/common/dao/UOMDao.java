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
