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
package org.egov.collection.integration.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.models.ReceiptAmountInfo;
import org.egov.collection.xml.converter.BillReceiptInfoConverter;
import org.egov.collection.xml.converter.ReceiptAccountInfoConverter;
import org.egov.collection.xml.converter.ReceiptInstrumentInfoConverter;
import org.egov.infra.exception.ApplicationRuntimeException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * This interface needs to be implemented by any billing application that
 * integrates with the eGov collection system. For internal applications, the
 * methods can use direct API calls. For external applications, the integration
 * can be through web-service/REST calls. The convention to be followed: a bean
 * named "<servicename>collectionsInterface" needs to be available in the spring
 * context. Service name is the name provided for the billing service in
 * <ServiceDetails> class.
 */
public class BillingIntegrationServiceStub implements BillingIntegrationService {

    private static final Logger LOGGER = Logger.getLogger(BillingIntegrationServiceStub.class);

    @Override
    public void updateReceiptDetails(final Set<BillReceiptInfo> billReceipts) throws ApplicationRuntimeException {
        try {
            final String xml = convertToXML(billReceipts);
            LOGGER.debug("Written bill details to file successfully " + xml);

        } catch (final Exception e) {
            LOGGER.error("Error occrured while updating dishonored cheque status to billing system : " + e);
            throw new ApplicationRuntimeException("Exception Occured" + e);
        }

    }

    @Override
    public void apportionPaidAmount(final String billReferenceNumber, final BigDecimal actualAmountPaid,
            final ArrayList<ReceiptDetail> receiptDetailsArray) {

    }

    /**
     * This method converts the given bill receipt object into an XML
     *
     * @param billReceipt
     *            an instance of <code>BillReceiptInfo</code>
     * @return a <code>String</code> representing the XML format of the
     *         <code>BillReceiptInfo</code> object
     */
    private String convertToXML(final Set<BillReceiptInfo> billReceipts) {
        final XStream xStream = new XStream(new DomDriver());
        xStream.registerConverter(new BillReceiptInfoConverter());
        xStream.registerConverter(new ReceiptAccountInfoConverter());
        xStream.registerConverter(new ReceiptInstrumentInfoConverter());
        xStream.alias("Bill-Receipt", BillReceiptInfoImpl.class);
        return xStream.toXML(billReceipts);
    }

    @Override
    public List<ReceiptDetail> reconstructReceiptDetail(final String billReferenceNumber,
            final BigDecimal actualAmountPaid, final List<ReceiptDetail> receiptDetailList) {
        return receiptDetailList;
    }

    @Override
    public String constructAdditionalInfoForReceipt(final BillReceiptInfo billReceiptInfo) {
        return null;
    }

    @Override
    public ReceiptAmountInfo receiptAmountBifurcation(final BillReceiptInfo billReceiptInfo) {
        return new ReceiptAmountInfo();
    }
}