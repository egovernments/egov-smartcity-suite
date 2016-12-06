/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.aadhaar.webservice.client;

import org.egov.infra.aadhaar.webservice.contract.AadhaarInfo;
import org.egov.infra.config.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;

@Service
public class AadhaarInfoServiceClient {

    @Autowired
    private ApplicationProperties applicationProperties;

    public AadhaarInfo getAadhaarInfo(final String uid) throws Exception {
        final SOAPConnection soapConnection = SOAPConnectionFactory.newInstance().createConnection();
        final String wsdlURL = applicationProperties.getProperty("aadhaar.wsdl.url");
        final String aadhaarInfoMethod = applicationProperties.getProperty("aadhaar.info.ws.method");
        final AadhaarInfo aahaarInfo = retriveAadhaarInfo(soapConnection.call(soapRequest(aadhaarInfoMethod, uid), wsdlURL));
        soapConnection.close();
        return aahaarInfo;
    }

    private SOAPMessage soapRequest(final String methodName, final String uid) throws Exception {
        final SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
        final SOAPEnvelope soapEnvelope = soapMessage.getSOAPPart().getEnvelope();
        soapEnvelope.addNamespaceDeclaration("end", applicationProperties.getProperty("aadhaar.info.ws.namespace"));
        soapEnvelope.getBody().addChildElement(methodName, "end").addChildElement("arg0").addTextNode(uid);
        soapMessage.saveChanges();
        return soapMessage;
    }

    private AadhaarInfo retriveAadhaarInfo(final SOAPMessage soapResponseMessage) throws Exception {
        final Unmarshaller unmarshaller = JAXBContext
                .newInstance(Class.forName(applicationProperties.getProperty("aadhaar.info.ws.client.impl.class.fqn"))).createUnmarshaller();
        return (AadhaarInfo) unmarshaller.unmarshal(soapResponseMessage.getSOAPBody().extractContentAsDocument());
    }

}
