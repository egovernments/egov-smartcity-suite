package org.egov.ptis.nmc.integration.impl;

import static org.egov.demand.utils.DemandConstants.COLLECTIONTYPE_FIELD;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.EgBill;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.nmc.bill.NMCPTBillServiceImpl;
import org.egov.ptis.nmc.bill.NMCPropertyTaxBillable;
import org.egov.ptis.nmc.integration.bean.Property;
import org.egov.ptis.nmc.integration.utils.SpringBeanUtil;
import org.egov.ptis.nmc.util.DCBUtils;
import org.egov.ptis.nmc.util.PropertyTaxNumberGenerator;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.ptis.service.collection.PropertyTaxCollection;

public class PropertyImpl extends Property {

	private NMCPropertyTaxBillable billable;

	@Override
	protected Billable getBillable() {
		if (billable == null) {
			billable = new NMCPropertyTaxBillable();
			billable.setBasicProperty(basicProperty);
			billable.setCollectionType(COLLECTIONTYPE_FIELD);
		}
		return billable;
	}

	@Override
	public void setBillable(NMCPropertyTaxBillable billable) {
		this.billable = billable;
	}

	@Override
	protected DCBDisplayInfo getDCBDisplayInfo() {
		DCBUtils dcbUtils = new DCBUtils();
		return dcbUtils.prepareDisplayInfo();
	}

	@Override
	public EgBill createBill() {
		PropertyTaxCollection propertyTaxCollection = SpringBeanUtil.getPropertyTaxCollection();
		PropertyTaxUtil propTaxUtil = SpringBeanUtil.getPropertyTaxUtil();
		PropertyTaxNumberGenerator propNumberGenerator = SpringBeanUtil.getPropertyTaxNumberGenerator();
		/*
		 * because unlike counter collections, do NOT want collections to call
		 * the apportioning logic - we are apportioning ourselves.
		 */
		
		NMCPTBillServiceImpl billServiceInterface = new NMCPTBillServiceImpl();
		billServiceInterface.setPropertyTaxCollection(propertyTaxCollection);
		billServiceInterface.setPropertyTaxUtil(propTaxUtil);
		EgBill bill = billServiceInterface.generateBill(billable);

		// because the bill must be persisted before calling the collections API
		flushToGetBillID();
		return bill;
	}

	@Override
	protected void checkAuthorization() {
		String userId = EGOVThreadLocals.getUserId();
		if (userId == null) {
			throw new EGOVRuntimeException(" User is null.Please check ");
		}
	}

	@Override
	protected void checkIsActive() {
		if (!basicProperty.isActive()) {
			throw new EGOVRuntimeException("Property is Deactivated. Provided propertid : " + getPropertyID());
		}
	}

	private void flushToGetBillID() {
		HibernateUtil.getCurrentSession().flush();
	}

}
