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
package org.egov.infra.events.entity.schema;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.egov.infra.events.processing.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JAXBSample {
    private static final Logger LOG = LoggerFactory.getLogger(EventListener.class);

    /**
     * @param args
     */
    public static void main(final String[] args) {

        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance("org.egov.infra.events.entity.schema");
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            final String text = "<response> <email> <to>ro@coc.gov.in</to> <cc>${cc_address}</cc>  <body> Dear ${to_name}, The following receipt has been cancelled - lorem ipsum ${receipt_no} dolor lorem dolor </body> </email> </response>";

            /*
             * 12. Convert String to InputStream using ByteArrayInputStream 13.
             * class. This class constructor takes the string byte array 14.
             * which can be done by calling the getBytes() method. 15.
             */

            final InputStream is = new ByteArrayInputStream(text.getBytes("UTF-8"));
            final Response response = (Response) unmarshaller.unmarshal(new File(
                    "src/org/egov/infra/events/entity/schema/Response.xml"));
            final Response reponseInput = (Response) unmarshaller.unmarshal(is);
            if (LOG.isDebugEnabled()) {
                LOG.debug("response = " + response.getSms().get(0).getPhonenumber());
                LOG.debug("reponseInput = " + reponseInput.getEmail().get(0));
            }

            final EmailType email = reponseInput.getEmail().get(0);
            if (LOG.isDebugEnabled())
                LOG.debug("email::::::::::" + email.to.get(0));

        } catch (final JAXBException e) {
            e.printStackTrace();
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}
