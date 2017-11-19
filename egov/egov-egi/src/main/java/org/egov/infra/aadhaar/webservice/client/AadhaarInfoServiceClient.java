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

package org.egov.infra.aadhaar.webservice.client;

import org.egov.infra.aadhaar.webservice.contract.AadhaarInfo;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

@Service
public class AadhaarInfoServiceClient {

    @Value("${aadhaar.info.ws.namespace}")
    private String aadharWSDLNamespace;

    @Value("${aadhaar.wsdl.url}")
    private String aadharWSDLUrl;

    @Value("${aadhaar.info.ws.method}")
    private String aadharWSDLMethod;

    @Value("${aadhaar.info.ws.client.impl.class.fqn}")
    private String clientImplClassFQN;

    public AadhaarInfo getAadhaarInfo(String uid) {
        try {
            SOAPConnection soapConnection = SOAPConnectionFactory.newInstance().createConnection();
            AadhaarInfo aahaarInfo = retriveAadhaarInfo(soapConnection.call(soapRequest(uid), aadharWSDLUrl));
            soapConnection.close();
            return aahaarInfo;
        } catch (SOAPException e) {
            throw new ApplicationRuntimeException("Error occurred while getting Aadhaar Info", e);
        }
    }

    private SOAPMessage soapRequest(String uid) {
        try {
            SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
            SOAPEnvelope soapEnvelope = soapMessage.getSOAPPart().getEnvelope();
            soapEnvelope.addNamespaceDeclaration("end", aadharWSDLNamespace);
            soapEnvelope.getBody().addChildElement(aadharWSDLMethod, "end").addChildElement("arg0").addTextNode(uid);
            soapMessage.saveChanges();
            return soapMessage;
        } catch (SOAPException e) {
            throw new ApplicationRuntimeException("Error occurred while preparing Aadhaar Info request", e);
        }
    }

    private AadhaarInfo retriveAadhaarInfo(SOAPMessage soapResponseMessage) {
        try {
            Unmarshaller unmarshaller = JAXBContext
                    .newInstance(Class.forName(clientImplClassFQN)).createUnmarshaller();
            return (AadhaarInfo) unmarshaller.unmarshal(soapResponseMessage.getSOAPBody().extractContentAsDocument());
        } catch (JAXBException | SOAPException | ClassNotFoundException e) {
            throw new ApplicationRuntimeException("Error occurred while converting Aadhaar Info", e);
        }
    }

}
