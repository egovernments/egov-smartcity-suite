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

package org.egov.infra.config.persistence.event.listener;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.entity.Auditable;
import org.egov.infstr.models.BaseModel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.event.spi.EventSource;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.hibernate.event.spi.SaveOrUpdateEventListener;

import java.util.Date;

/**
 * This Event listener class sets the audit properties createdBy, createdDate modifiedBy and modifiedDate. It does this by hooking
 * to the pre-update and pre-insert events. The pre-update event was chosen instead of save-update events for setting modified
 * properties to fix a bug where even for object reads the Hibernate objects were getting updated. However, the save-update event
 * is still required for new objects (inserts) as Hibernate checks for not-null constraints before the pre-update event is fired.
 * Due to auto flushing strategy that we use, every transaction commit causes a session.flush to be called. Flush calls cascade of
 * collections or properties which fires the save-update event if cascade value is anything other than Cascade.NONE.
 *
 * @author sahinab
 */
public class HibernateEventListener implements SaveOrUpdateEventListener, PreUpdateEventListener {

    private static final long serialVersionUID = 1L;

    /**
     * Sets the modifiedBy and modifiedDate properties on objects that inherit from {@link org.egov.infstr.models.BaseModel}.
     * event.getState() is used to get the list of properties for the object as these are the properties that Hibernate generates
     * the UPDATE statement for. A separate session is used to get the User object to ensure that the object thus obtained is
     * flushed from within the event. @ return false to continue the processing
     */
    @Override
    public boolean onPreUpdate(final PreUpdateEvent event) {
        final Object entity = event.getEntity();
        if (entity instanceof BaseModel)
            updateAuditableProperties(event);
        else if (entity instanceof Auditable)
            updateAuditable(event);
        return false;
    }

    /**
     * For new objects that are created, this event is used to set the audit properties. This is done here instead of the
     * pre-insert event because Hibernate checks for not-null constraints before the pre-update and pre-insert are fired.
     */
    @Override
    public void onSaveOrUpdate(final SaveOrUpdateEvent event) {
        final EventSource session = event.getSession();
        final Object object = event.getObject();
        if (object instanceof BaseModel && !session.getPersistenceContext().reassociateIfUninitializedProxy(object)) {
            // only update the entity if it has been changed
            final Date currentDate = new Date();
            final User usr = session.load(User.class, ApplicationThreadLocals.getUserId());

            final BaseModel entity = (BaseModel) session.getPersistenceContext().unproxyAndReassociate(object);
            if (entity.getCreatedBy() == null) {
                entity.setCreatedDate(currentDate);
                entity.setCreatedBy(usr);
                entity.setModifiedBy(usr);
                entity.setModifiedDate(currentDate);
            }

        } else if (object instanceof Auditable && !session.getPersistenceContext().reassociateIfUninitializedProxy(object)) {
            final User usr = session.load(User.class, ApplicationThreadLocals.getUserId());
            final AbstractAuditable entity = (AbstractAuditable) session.getPersistenceContext().unproxyAndReassociate(object);
            if (entity.getCreatedBy() == null) {
                final Date currentDate = new Date();
                entity.setCreatedDate(currentDate);
                entity.setCreatedBy(usr);
                entity.setLastModifiedBy(usr);
                entity.setLastModifiedDate(currentDate);
            }
        }

    }

    /**
     * When reading an object from within the PreInsert or PreUpdate event handlers, a different session has to be used from the
     * one in which the event was fired. This is to make sure that the objects loaded here are flushed. Otherwise it results in
     * collection was not processed by flush() Assertion failures
     *
     * @param session
     * @return
     */
    private User getUserObjectFromWithinEventListener(final EventSource session) {
        // Since we are already in the flush logic of our current session,
        // get the user object from a different session
        final SessionFactory factory = session.getFactory();
        final Session session2 = factory.openSession();
        final User usr = session2.load(User.class, ApplicationThreadLocals.getUserId());
        session2.flush();
        session2.close();
        return usr;
    }

    private void updateAuditableProperties(final PreUpdateEvent event) {
        int i = 0;
        for (final String propName : event.getPersister().getPropertyNames()) {
            if ("modifiedDate".equals(propName))
                event.getState()[i] = new Date();
            if ("modifiedBy".equals(propName))
                event.getState()[i] = getUserObjectFromWithinEventListener(event.getSession());
            i++;
        }
    }

    private void updateAuditable(final PreUpdateEvent event) {
        int i = 0;
        for (final String propName : event.getPersister().getPropertyNames()) {
            if ("lastModifiedDate".equals(propName))
                event.getState()[i] = new Date();
            if ("lastModifiedBy".equals(propName))
                event.getState()[i] = getUserObjectFromWithinEventListener(event.getSession());
            i++;
        }
    }
}