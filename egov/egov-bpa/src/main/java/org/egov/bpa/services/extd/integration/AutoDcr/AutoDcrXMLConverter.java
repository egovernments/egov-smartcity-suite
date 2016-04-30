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
package org.egov.bpa.services.extd.integration.AutoDcr;

import org.egov.exceptions.EGOVRuntimeException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class AutoDcrXMLConverter {
	public AutoDcrXMLConverter() {

	}

	public static AutoDcrBpaIntegration unmarshall(final Class<?> type,
			final String response) {
		try {
		/*	
			InputSource is = new InputSource(inputStream);
			final SAXParserFactory sax = SAXParserFactory.newInstance();
			sax.setNamespaceAware(false);
			final XMLReader reader;
			try {
			    reader = sax.newSAXParser().getXMLReader();
			} catch (SAXException | ParserConfigurationException e) {
			    throw new RuntimeException(e);
			}
			SAXSource source = new SAXSource(reader, is);*/
		/*	final XMLReader reader;
			final SAXParserFactory sax = SAXParserFactory.newInstance();
			sax.setNamespaceAware(false);
			sax.setValidating(false);
			try {
			    reader = sax.newSAXParser().getXMLReader();
			} catch (Exception e) {
			    throw new RuntimeException(e);
			}
			
			SAXSource source = new SAXSource(reader, new InputSource(response));
			final JAXBContext jaxbContext = JAXBContext.newInstance(AutoDcrBpaIntegration.class);
			Unmarshaller unmarshalObject = jaxbContext.createUnmarshaller();
			  ValidationEventCollector vec = new ValidationEventCollector();
			  unmarshalObject.setEventHandler( vec );
			
			//unmarshalObject.setValidating(false);
			@SuppressWarnings("unchecked")
			JAXBElement<AutoDcrBpaIntegration> doc = (JAXBElement<AutoDcrBpaIntegration>)unmarshalObject.unmarshal(source);
			return doc.getValue();*/
			final JAXBContext jaxbContext = JAXBContext.newInstance(AutoDcrBpaIntegration.class);
			final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			//unmarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			AutoDcrBpaIntegration a = (AutoDcrBpaIntegration) unmarshaller
					.unmarshal(new StringReader(response));
			return a;
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("Error occurred while trying to unmarshall XML{} ");
			throw new EGOVRuntimeException(
					"Error occurred while trying to unmarshall XML", e);
		}
	}

}
