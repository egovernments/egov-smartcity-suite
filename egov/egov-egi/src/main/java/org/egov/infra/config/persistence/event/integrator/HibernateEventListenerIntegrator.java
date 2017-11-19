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

package org.egov.infra.config.persistence.event.integrator;

import org.egov.infra.config.persistence.event.listener.HibernateEventListener;
import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.internal.DefaultSaveOrUpdateEventListener;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

public class HibernateEventListenerIntegrator implements Integrator {

    @Override
    public void disintegrate(final SessionFactoryImplementor sessionFactory,
                             final SessionFactoryServiceRegistry serviceRegistry) {
        // Disintegration code have to be added.
    }

    private void registerCustomFilters(final SessionFactoryServiceRegistry serviceRegistry) {
        final EventListenerRegistry eventListenerRegistry = serviceRegistry.getService(EventListenerRegistry.class);
        final DefaultSaveOrUpdateEventListener dfltSaveorUpdateListner = new DefaultSaveOrUpdateEventListener();
        final HibernateEventListener hibernateEventListener = new HibernateEventListener();
        eventListenerRegistry.setListeners(EventType.SAVE, hibernateEventListener, dfltSaveorUpdateListner);
        eventListenerRegistry.setListeners(EventType.UPDATE, hibernateEventListener, dfltSaveorUpdateListner);
        eventListenerRegistry.setListeners(EventType.SAVE_UPDATE, hibernateEventListener, dfltSaveorUpdateListner);
        eventListenerRegistry.prependListeners(EventType.PRE_UPDATE, hibernateEventListener);
    }

    @Override
    public void integrate(final Metadata metadata, final SessionFactoryImplementor sessionFactory,
                          final SessionFactoryServiceRegistry serviceRegistry) {
        registerCustomFilters(serviceRegistry);

    }

}
