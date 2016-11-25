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

package org.egov.infra.config.jms.messaging;

import org.egov.infra.config.jms.messaging.errorhandler.MessagingErrorHandler;
import org.egov.infra.config.jms.messaging.listener.EmailQueueListener;
import org.egov.infra.config.jms.messaging.listener.SMSQueueListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.support.destination.JndiDestinationResolver;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.jms.Destination;

@Configuration
@DependsOn("jmsConfiguration")
public class MessagingConfiguration {

    @Bean(name = "smsQueue")
    public JndiObjectFactoryBean smsQueue() {
        JndiObjectFactoryBean smsQueue = new JndiObjectFactoryBean();
        smsQueue.setExpectedType(Destination.class);
        smsQueue.setResourceRef(true);
        smsQueue.setJndiName("java:/jms/queue/sms");
        return smsQueue;
    }

    @Bean(name = "emailQueue")
    public JndiObjectFactoryBean emailQueue() {
        JndiObjectFactoryBean emailQueue = new JndiObjectFactoryBean();
        emailQueue.setExpectedType(Destination.class);
        emailQueue.setResourceRef(true);
        emailQueue.setJndiName("java:/jms/queue/email");
        return emailQueue;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(CachingConnectionFactory cachingConnectionFactory,
                                                                          PlatformTransactionManager transactionManager,
                                                                          JndiDestinationResolver jmsDestinationResolver) {
        DefaultJmsListenerContainerFactory listenerContainerFactory = new DefaultJmsListenerContainerFactory();
        listenerContainerFactory.setConnectionFactory(cachingConnectionFactory);
        listenerContainerFactory.setTransactionManager(transactionManager);
        listenerContainerFactory.setErrorHandler(messagingErrorHandler());
        listenerContainerFactory.setDestinationResolver(jmsDestinationResolver);
        listenerContainerFactory.setConcurrency("10");
        return listenerContainerFactory;
    }

    @Bean
    public MessagingErrorHandler messagingErrorHandler() {
        return new MessagingErrorHandler();
    }

    @Bean
    public EmailQueueListener emailQueueListener() {
        return new EmailQueueListener();
    }

    @Bean
    public SMSQueueListener smsQueueListener() {
        return new SMSQueueListener();
    }

}
