/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.collection.integration.models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ChallanInfo;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.entity.ReceiptMisc;
import org.egov.collection.integration.services.BillingIntegrationService;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.admin.master.entity.Location;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;

/**
 * The bill receipt information class. Provides details of a bill receipt.
 */

public class BillReceiptInfoImpl implements BillReceiptInfo {
    /**
     * The private receipt header object. This is used by the getters to provide
     * bill receipt information
     */
    private final ReceiptHeader receiptHeader;

    /**
     * Indicates the last event that has occurred on this receipt
     */
    private String event;

    private final Set<ReceiptAccountInfo> accountDetails = new HashSet<>(0);
    private final Set<ReceiptInstrumentInfo> instrumentDetails = new HashSet<>(0);

    /**
     * Set of bounced instruments of this receipt - Will be created only if
     * event is InstrumentBounced
     */
    private final Set<ReceiptInstrumentInfo> bouncedInstruments = new HashSet<>(0);
    private final Set<ChallanInfo> challanDetails = new HashSet<>(0);
    private ChallanInfo challan = null;
    /**
     * Billing system invokes this URL to view the receipt
     */
    private final String receiptURL;

    private final String additionalInfo;

    /**
     * Creates bill receipt information object for given receipt header and
     * bounced instrument(if any)
     *
     * @param receiptHeader
     *            the receipt header object
     * @param chartOfAccountsHibernateDAO
     */
    public BillReceiptInfoImpl(final ReceiptHeader receiptHeader,
            final ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO, final PersistenceService persistenceService,
            final InstrumentHeader bouncedInstrumentInfo) {
        this.receiptHeader = receiptHeader;
        receiptURL = CollectionConstants.RECEIPT_VIEW_SOURCEPATH + receiptHeader.getId();
        additionalInfo = null;
        final String receiptStatus = receiptHeader.getStatus().getCode();

        // Populate set of account info objects using receipt details
        for (final ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails())
            accountDetails
                    .add(new ReceiptAccountInfoImpl(receiptDetail, chartOfAccountsHibernateDAO, persistenceService));

        // Populate set of instrument headers that belong to this receipt
        if (bouncedInstrumentInfo != null)
            instrumentDetails.add(new ReceiptInstrumentInfoImpl(bouncedInstrumentInfo));
        else
            for (final InstrumentHeader instrumentHeader : receiptHeader.getReceiptInstrument())
                instrumentDetails.add(new ReceiptInstrumentInfoImpl(instrumentHeader));

        if (CollectionConstants.RECEIPT_STATUS_CODE_INSTRUMENT_BOUNCED.equals(receiptStatus)) {
            event = BillingIntegrationService.EVENT_INSTRUMENT_BOUNCED;
            // find bounced instruments of this receipt
            if (bouncedInstrumentInfo != null)
                findBouncedInstrument();
        } else if (CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED.equals(receiptStatus)
                || CollectionConstants.RECEIPT_STATUS_CODE_APPROVED.equals(receiptStatus)
                || CollectionConstants.RECEIPT_STATUS_CODE_SUBMITTED.equals(receiptStatus))
            event = BillingIntegrationService.EVENT_RECEIPT_CREATED;
        else if (CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED.equals(receiptStatus))
            event = BillingIntegrationService.EVENT_RECEIPT_CANCELLED;

    }

    /**
     * Creates bill receipt information object for given receipt header and
     * additional message
     *
     * @param receiptHeader
     *            the receipt header object
     * @param chartOfAccountsHibernateDAO
     */
    public BillReceiptInfoImpl(final ReceiptHeader receiptHeader, final String additionalInfo,
            final ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO,
            final PersistenceService persistenceService) {
        this.receiptHeader = receiptHeader;
        receiptURL = CollectionConstants.RECEIPT_VIEW_SOURCEPATH + receiptHeader.getId();
        this.additionalInfo = additionalInfo;

        // Populate set of account info objects using receipt details
        for (final ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails())
            accountDetails
                    .add(new ReceiptAccountInfoImpl(receiptDetail, chartOfAccountsHibernateDAO, persistenceService));

        // Populate set of instrument headers that belong to this receipt
        for (final InstrumentHeader instrumentHeader : receiptHeader.getReceiptInstrument())
            instrumentDetails.add(new ReceiptInstrumentInfoImpl(instrumentHeader));
        final String receiptStatus = receiptHeader.getStatus().getCode();

        if (CollectionConstants.RECEIPT_STATUS_CODE_INSTRUMENT_BOUNCED.equals(receiptStatus)) {
            event = BillingIntegrationService.EVENT_INSTRUMENT_BOUNCED;
            // find all bounced instruments of this receipt
            findBouncedInstrument();
        } else if (CollectionConstants.RECEIPT_STATUS_CODE_TO_BE_SUBMITTED.equals(receiptStatus)
                || CollectionConstants.RECEIPT_STATUS_CODE_APPROVED.equals(receiptStatus)
                || CollectionConstants.RECEIPT_STATUS_CODE_SUBMITTED.equals(receiptStatus))
            event = BillingIntegrationService.EVENT_RECEIPT_CREATED;
        else if (CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED.equals(receiptStatus))
            event = BillingIntegrationService.EVENT_RECEIPT_CANCELLED;

    }

    public BillReceiptInfoImpl(final ReceiptHeader receiptHeader, final EgovCommon egovCommon,
            final ReceiptHeader receiptHeaderRefObj, final ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO,
            final PersistenceService persistenceService) {
        this.receiptHeader = receiptHeader;
        receiptURL = CollectionConstants.RECEIPT_VIEW_SOURCEPATH + receiptHeader.getId();
        additionalInfo = null;
        // Populate set of account info objects using receipt details
        for (final ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails())
            accountDetails
                    .add(new ReceiptAccountInfoImpl(receiptDetail, chartOfAccountsHibernateDAO, persistenceService));

        // Populate set of instrument headers that belong to this receipt
        for (final InstrumentHeader instrumentHeader : receiptHeader.getReceiptInstrument())
            instrumentDetails.add(new ReceiptInstrumentInfoImpl(instrumentHeader));
        if (receiptHeader.getReceipttype() == CollectionConstants.RECEIPT_TYPE_CHALLAN) {
            challan = new ChallanInfo(receiptHeader, egovCommon, receiptHeaderRefObj, chartOfAccountsHibernateDAO,
                    persistenceService);
            challanDetails.add(challan);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getReceiptNum()).append(" ").append(getAccountDetails());
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getBillReferenceNum()
     */
    @Override
    public String getBillReferenceNum() {
        return receiptHeader.getReferencenumber();
    }

    /*
     * (non-Javadoc)
     * @see
     * org.egov.infstr.collections.integration.models.IBillReceiptInfo#getEvent
     * ()
     */
    @Override
    public String getEvent() {
        return event;
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getReceiptNum ()
     */
    @Override
    public String getReceiptNum() {
        return receiptHeader.getReceiptnumber();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getReceiptDate()
     */
    @Override
    public Date getReceiptDate() {
        return receiptHeader.getReceiptdate();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getReceiptLocation()
     */
    @Override
    public Location getReceiptLocation() {
        return receiptHeader.getLocation();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getReceiptStatus()
     */
    @Override
    public EgwStatus getReceiptStatus() {
        return receiptHeader.getStatus();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getPayeeName ()
     */
    @Override
    public String getPayeeName() {
        return StringEscapeUtils.unescapeJavaScript(receiptHeader.getPayeeName());
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getPayeeAddress()
     */
    @Override
    public String getPayeeAddress() {
        return receiptHeader.getPayeeAddress();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getAccountDetails()
     */
    @Override
    public Set<ReceiptAccountInfo> getAccountDetails() {
        return accountDetails;
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getInstrumentDetails()
     */
    @Override
    public Set<ReceiptInstrumentInfo> getInstrumentDetails() {
        return instrumentDetails;
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getBouncedInstruments()
     */
    @Override
    public Set<ReceiptInstrumentInfo> getBouncedInstruments() {
        return bouncedInstruments;
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getServiceName()
     */
    @Override
    public String getServiceName() {
        return receiptHeader.getService().getName();
    }

    /*
     * (non-Javadoc)
     * @see
     * org.egov.infstr.collections.integration.models.IBillReceiptInfo#getPaidBy
     * ()
     */
    @Override
    public String getPaidBy() {
        return receiptHeader.getPaidBy();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getDescription()
     */
    @Override
    public String getDescription() {
        return receiptHeader.getReferenceDesc();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getTotalAmount()
     */
    @Override
    public BigDecimal getTotalAmount() {
        return receiptHeader.getTotalAmount();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getCreatedBy ()
     */
    @Override
    public User getCreatedBy() {
        return receiptHeader.getCreatedBy();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getModifiedBy ()
     */
    @Override
    public User getModifiedBy() {
        return receiptHeader.getLastModifiedBy();
    }

    /**
     * Finds all instruments of this receipt that are in bounced (dishonored)
     * status and adds them to the set of bounced instruments.
     */
    private void findBouncedInstrument() {
        for (final ReceiptInstrumentInfo instrumentInfo : instrumentDetails)
            if (instrumentInfo.isBounced())
                bouncedInstruments.add(instrumentInfo);
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getReceiptMisc()
     */
    public ReceiptMisc getReceiptMisc() {
        return receiptHeader.getReceiptMisc();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getChallanDetails()
     */
    public Set<ChallanInfo> getChallanDetails() {
        return challanDetails;
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getChallan ()
     */
    public ChallanInfo getChallan() {
        return challan;
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getReceiptURL ()
     */
    @Override
    public String getReceiptURL() {
        return receiptURL;
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getCollectionType()
     */
    @Override
    public String getCollectionType() {
        return receiptHeader.getCollectiontype().toString();
    }

    public String getConsumerCode() {
        return receiptHeader.getConsumerCode();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getManualReceiptNumber()
     */
    @Override
    public String getManualReceiptNumber() {
        return receiptHeader.getManualreceiptnumber() == null ? "" : receiptHeader.getManualreceiptnumber();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getManualReceiptDate()
     */
    @Override
    public Date getManualReceiptDate() {
        return receiptHeader.getManualreceiptdate() == null ? null : receiptHeader.getManualreceiptdate();
    }

    /*
     * (non-Javadoc)
     * @see
     * org.egov.infstr.collections.integration.models.IBillReceiptInfo#getLegacy
     * ()
     */
    @Override
    public Boolean getLegacy() {
        Boolean legacy = Boolean.FALSE;
        for (final ReceiptAccountInfo receiptAccountInfo : getAccountDetails())
            if (receiptAccountInfo.getDescription() != null && !"".equals(receiptAccountInfo.getDescription())
                    && (!receiptAccountInfo.getDescription().contains("#") || receiptAccountInfo.getDescription()
                            .contains(CollectionConstants.ESTIMATION_CHARGES_WATERTAX_MODULE))) {
                legacy = Boolean.TRUE;
                break;
            }
        return legacy;
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getAdditionalInfo()
     */
    @Override
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.egov.infstr.collections.integration.models.IBillReceiptInfo#getSource
     * ()
     */
    @Override
    public String getSource() {
        return receiptHeader.getSource() == null ? "" : receiptHeader.getSource();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infstr.collections.integration.models.IBillReceiptInfo#
     * getReceiptInstrumentType()
     */
    @Override
    public String getReceiptInstrumentType() {
        String instrumentType = "";
        for (final ReceiptInstrumentInfo instrumentInfo : instrumentDetails)
            if (instrumentInfo.getInstrumentType().equals(CollectionConstants.INSTRUMENTTYPE_CHEQUE)
                    || instrumentInfo.getInstrumentType().equals(CollectionConstants.INSTRUMENTTYPE_DD)) {
                instrumentType = CollectionConstants.INSTRUMENTTYPE_CHEQUEORDD;
                break;
            } else
                instrumentType = instrumentInfo.getInstrumentType();
        return instrumentType;
    }
}