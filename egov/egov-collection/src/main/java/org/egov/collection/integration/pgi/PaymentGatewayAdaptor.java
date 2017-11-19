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
package org.egov.collection.integration.pgi;

import org.egov.collection.entity.ReceiptHeader;
import org.egov.infstr.models.ServiceDetails;

/**
 * Adaptor interface for integrating with third party payment gateway systems.<br>
 * Any new payment gateway can be integrated with collections system by writing
 * a class that implements this interface, and then defining a bean in
 * application context for this class with id as
 * <code>[ServiceCode]PaymentGatewayAdaptor</code> <br>
 * where <code>[ServiceCode]</code> is the service code for that payment gateway
 * service. <br>
 * <br>
 * e.g. If <code>BDPGI</code> is the service code of Bill Desk Payment Gateway
 * service, then the corresponding adaptor bean should have the id as
 * <code>BDPGIPaymentGatewayAdaptor</code>
 */
public interface PaymentGatewayAdaptor {

    /**
     * Creates payment request object that will be used by the system to invoke
     * the payment gateway URL with appropriate request parameters.
     *
     * @param service
     *            The payment gateway service object
     * @param receipt
     *            The receipt for which payment is to be done
     * @return The payment request object
     * @see PaymentRequest
     */
    public PaymentRequest createPaymentRequest(ServiceDetails service, ReceiptHeader receipt);

    /**
     * Parses the payment response string received from the payment gateway and
     * creates the payment response object
     *
     * @param response
     *            The response string received from payment gateway
     * @return The payment response object
     * @see PaymentResponse
     */
    public PaymentResponse parsePaymentResponse(String response);

}
