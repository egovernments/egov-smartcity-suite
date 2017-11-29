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
package org.egov.ptis.client.integration.bean;

import org.apache.log4j.Logger;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.commons.Installment;
import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.dcb.bean.DCBReport;
import org.egov.dcb.bean.Payment;
import org.egov.dcb.bean.Receipt;
import org.egov.dcb.service.DCBService;
import org.egov.dcb.service.DCBServiceImpl;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.EgBill;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.ptis.client.integration.impl.PropertyImpl;
import org.egov.ptis.client.integration.utils.CollectionHelper;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public abstract class Property {

	private static final Logger LOGGER = Logger.getLogger(Property.class);
	public static final int FLAG_NONE = 0;
	public static final int FLAG_BASIC = 1;
	public static final int FLAG_DCB = 2;
	public static final int FLAG_RECEIPTS = 3;
	public static final int FLAG_BASIC_AND_RECEIPTINFO = 4;

	protected BasicProperty basicProperty;
	protected DCBService dcbService;

	private String propertyID;
	private String citizenName;
	private String doorNumber;
	private String wardName;
	private DCBReport dcbReport = new DCBReport();
	private BillReceiptInfo billreceiptInfo;
	private String receiptNo;
	private int infoFlag;
	@Autowired
	private BasicPropertyDAO basicPropertyDAO;

	protected abstract Billable getBillable();

	public abstract void setBillable(PropertyTaxBillable billable);

	protected abstract EgBill createBill();

	protected abstract DCBDisplayInfo getDCBDisplayInfo();

	protected abstract void checkAuthorization();

	protected abstract void checkIsActive();

	/**
	 * Factory method that returns a property object with the given ID.
	 *
	 * @param propertyID
	 * @param flag
	 *            Should be one of the FLAG_* values defined in this class. It
	 *            determines whether any of Basic/DCB/payment info should be
	 *            populated in the returned property object.
	 * @return
	 */
	public static Property fromPropertyID(String propertyID, String receiptNo, int flag) {
		LOGGER.info("fromPropertyID : propertyID " + propertyID);
		Property p = new PropertyImpl();
		p.propertyID = propertyID;
		p.infoFlag = flag;
		p.receiptNo = receiptNo;
		p.validate();

		p.initBasicProperty();
		p.initDCBService();
		p.populate();
		return p;
	}

	private void initBasicProperty() {
		if (getPropertyID() != null) {
			basicProperty = basicPropertyDAO.getAllBasicPropertyByPropertyID(getPropertyID());
		}
	}

	private void initDCBService() {
		dcbService = new DCBServiceImpl(getBillable());
	}

	private void populate() {
		LOGGER.info("Instantiating property with propertyID: " + propertyID);
		if (basicProperty != null) {
			getBasicInfo();
			getDCB();
			getReceipts();
			getReceiptForRcptNo();
		}
		LOGGER.info("Property instantiated: " + this);
	}

	/**
	 * Executes a collection.
	 */
	public BillReceiptInfo collect(Payment payment) {
		LOGGER.info("Property.collect() called: " + payment);
		BillReceiptInfo billReceiptInfo = null;
		checkIsActive();
		checkAuthorization();
		EgBill egBill = createBill();
		CollectionHelper collHelper = new CollectionHelper(egBill);
		billReceiptInfo = collHelper.executeCollection(payment, null);
		LOGGER.info("Property.collect() returned: " + billReceiptInfo);
		return billReceiptInfo;
	}

	/**
	 * Fetches basic property information like owner name and address (if
	 * requested).
	 */
	private void getBasicInfo() {
		if (!isBasicInfoRequested()) {
			return;
		}
		this.propertyID = basicProperty.getUpicNo();
		this.citizenName = basicProperty.getFullOwnerName();
		if (basicProperty.getPropertyID() != null && basicProperty.getPropertyID().getWard() != null) {
			this.wardName = basicProperty.getPropertyID().getWard().getName();
		}
		if (basicProperty.getAddress() != null && basicProperty.getAddress().getHouseNoBldgApt() != null) {
			this.doorNumber = basicProperty.getAddress().getHouseNoBldgApt();
		}
		LOGGER.info("Got basic info...");
	}

	/**
	 * Fetches demand-collection-balance info (if requested).
	 */
	private void getDCB() {
		if (!isDCBRequested()) {
			return;
		}
		DCBReport report = dcbService.getCurrentDCBOnly(getDCBDisplayInfo());
		dcbReport.setFieldNames(report.getFieldNames());
		dcbReport.setRecords(report.getRecords());
		LOGGER.info("Got DCB...");
	}

	/**
	 * Fetches all receipts paid against this property (if requested).
	 */
	private void getReceipts() {
		if (!isPaymentsRequested()) {
			return;
		}
		DCBReport report = dcbService.getReceiptsOnly();

		Map<Installment, List<Receipt>> receipts = report.getReceipts();
		dcbReport.setReceipts(receipts);

		LOGGER.info("Got payments...");
	}

	/**
	 * Fetches the receipt for given receipt no (if requested).
	 */
	private void getReceiptForRcptNo() {
		if (!isReceiptInfoRequested()) {
			return;
		}
		CollectionHelper collectionHelper = new CollectionHelper();
		billreceiptInfo = collectionHelper.getReceiptInfo(receiptNo);

		LOGGER.info("Got payments...");
	}

	private boolean isBasicInfoRequested() {
		return (infoFlag == FLAG_BASIC || infoFlag == FLAG_BASIC_AND_RECEIPTINFO);
	}

	private boolean isDCBRequested() {
		return (infoFlag & FLAG_DCB) == FLAG_DCB;
	}

	private boolean isPaymentsRequested() {
		return (infoFlag & FLAG_RECEIPTS) == FLAG_RECEIPTS;
	}

	private boolean isReceiptInfoRequested() {
		return (infoFlag & FLAG_BASIC_AND_RECEIPTINFO) == FLAG_BASIC_AND_RECEIPTINFO;
	}

	private void validate() {
		if ((propertyID == null || propertyID.trim().equals(""))) {
			throw new ApplicationRuntimeException("PropertyID was null or empty!");
		}
		if (isReceiptInfoRequested() && (receiptNo == null || receiptNo.equals(""))) {
			throw new ApplicationRuntimeException("receiptNo was null or empty!");
		}
	}

	public String getFullAddress() {
		return basicProperty.getAddress().toString();
	}

	public String getPropertyID() {
		return propertyID;
	}

	public String getCitizenName() {
		return citizenName;
	}

	public String getDoorNumber() {
		return doorNumber;
	}

	public String getWardName() {
		return wardName;
	}

	public DCBReport getDcbReport() {
		return dcbReport;
	}

	@Override
	public String toString() {
		return getPropertyID() + "-" + getDcbReport();
	}

	public BasicProperty getBasicProperty() {
		return basicProperty;
	}

	public BillReceiptInfo getBillreceiptInfo() {
		return billreceiptInfo;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

}
