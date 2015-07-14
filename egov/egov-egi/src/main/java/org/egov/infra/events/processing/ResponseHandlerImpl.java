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
package org.egov.infra.events.processing;

import java.util.ArrayList;
import java.util.List;

import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.events.entity.schema.EmailType;
import org.egov.infra.events.entity.schema.Response;
import org.egov.infstr.mail.Email;
import org.egov.infstr.mail.Email.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResponseHandlerImpl implements ResponseHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseHandlerImpl.class);

    @Autowired
    AppConfigValueService appConfigValuesService;
    	

    @Override
    public void respond(final Response r) {
        /*
         * See what is the type of the response and call its respond() method.
         */
       /*
        * FIXME or THROW
        *  if (r.getEmail() != null && r.getEmail().size() != 0) {
            if (LOG.isDebugEnabled())
                LOG.debug("::::Response Type Email::::");
            for (int i = 0; i < r.getEmail().size(); i++)
                respondViaEmail(r.getEmail().get(i));
        }

        if (r.getSms() != null && r.getSms().size() != 0) {
            if (LOG.isDebugEnabled())
                LOG.debug("::::Response Type Sms::::");
            for (int i = 0; i < r.getSms().size(); i++)
                respondViaSms(r.getSms().get(i));
        }*/
    }

    /**
     * @param mail
     */
    private void respondViaEmail(final EmailType mail) {
        if (mail.getTo() != null && !mail.getTo().isEmpty()) {
            final Builder builder = new Email.Builder(getAddressList(mail.getTo()), mail.getBody());

            if (mail.getCc() != null && mail.getCc().size() != 0)
                builder.addCc(getAddressList(mail.getCc()));

            if (mail.getBcc() != null && mail.getBcc().size() != 0)
                builder.addBcc(getAddressList(mail.getBcc()));

            if (mail.getSubject() != null && mail.getSubject().length() != 0)
                builder.subject(mail.getSubject());

            final Email email = builder.build();
            email.send();
            if (LOG.isDebugEnabled())
                LOG.debug("::::Email Sent Successfully::::");
        }
    }

    /**
     * @param recipientList
     * @return
     */
    private List<String> getAddressList(final List<String> recipientList) {
        String[] recipientAddress = null;
        final List<String> mailAddress = new ArrayList<String>();
        for (int i = 0; i < recipientList.size(); i++) {
            recipientAddress = recipientList.get(i).toString().split(",");
            for (final String recipientAddres : recipientAddress)
                mailAddress.add(recipientAddres);
        }
        return mailAddress;
    }
}
