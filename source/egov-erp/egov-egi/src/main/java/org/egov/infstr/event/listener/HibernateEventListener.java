package org.egov.infstr.event.listener;

import java.util.Date;

import org.egov.infra.admin.master.entity.UserImpl;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.models.BaseModel;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.event.spi.EventSource;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.hibernate.event.spi.SaveOrUpdateEventListener;

/**
 * This Event listener class sets the audit properties createdBy, createdDate modifiedBy and modifiedDate. It does this by hooking to the pre-update and pre-insert events. The pre-update event was
 * chosen instead of save-update events for setting modified properties to fix a bug where even for object reads the Hibernate objects were getting updated. However, the save-update event is still
 * required for new objects (inserts) as Hibernate checks for not-null constraints before the pre-update event is fired. Due to auto flushing strategy that we use, every transaction commit causes a
 * session.flush to be called. Flush calls cascade of collections or properties which fires the save-update event if cascade value is anything other than Cascade.NONE.
 * 
 * @author sahinab
 */
public class HibernateEventListener implements SaveOrUpdateEventListener, PreUpdateEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private void updateAuditProperties(final EventSource session, final String[] propNames, final Object[] entityProps) {

		int i = 0;
		for (final String propName : propNames) {
			if ("modifiedDate".equals(propName)) {
				entityProps[i] = new Date();
			}
			if ("modifiedBy".equals(propName)) {
				entityProps[i] = this.getUserObjectFromWithinEventListener(session);
			}
			i++;
		}

	}

	/**
	 * When reading an object from within the PreInsert or PreUpdate event handlers, a different session has to be used from the one in which the event was fired. This is to make sure that the objects
	 * loaded here are flushed. Otherwise it results in collection was not processed by flush() Assertion failures
	 * 
	 * @param session
	 * @return
	 */
	private UserImpl getUserObjectFromWithinEventListener(final EventSource session) {
		// Since we are already in the flush logic of our current session,
		// get the user object from a different session
		final SessionFactory factory = session.getFactory();
		final Session session2 = factory.openSession();
		final UserImpl usr = (UserImpl) session2.load(UserImpl.class, Integer.valueOf(EGOVThreadLocals.getUserId()));
		session2.flush();
		session2.close();
		return usr;
	}

	/**
	 * Sets the modifiedBy and modifiedDate properties on objects that inherit from {@link org.egov.infstr.models.BaseModel}. event.getState() is used to get the list of properties for the object as
	 * these are the properties that Hibernate generates the UPDATE statement for. A separate session is used to get the UserImpl object to ensure that the object thus obtained is flushed from within
	 * the event. @ return false to continue the processing
	 */
	@Override
	public boolean onPreUpdate(final PreUpdateEvent event) {
		final Object entity = event.getEntity();
		if (entity instanceof BaseModel) {
			this.updateAuditProperties(event.getSession(), event.getPersister().getPropertyNames(), event.getState());
		}
		return false;
	}

	/**
	 * For new objects that are created, this event is used to set the audit properties. This is done here instead of the pre-insert event because Hibernate checks for not-null constraints before the
	 * pre-update and pre-insert are fired.
	 */
	@Override
	public void onSaveOrUpdate(final SaveOrUpdateEvent event) throws HibernateException {
		final EventSource session = event.getSession();
		final Object object = event.getObject();
		if (object instanceof BaseModel && !session.getPersistenceContext().reassociateIfUninitializedProxy(object)) {
			// only update the entity if it has been changed
			final Date currentDate = new Date();
			final UserImpl usr = (UserImpl) session.load(UserImpl.class, Integer.valueOf(EGOVThreadLocals.getUserId()));

			final BaseModel entity = (BaseModel) session.getPersistenceContext().unproxyAndReassociate(object);
			if (entity.getCreatedBy() == null) {
				entity.setCreatedDate(currentDate);
				entity.setCreatedBy(usr);
				entity.setModifiedBy(usr);
				entity.setModifiedDate(currentDate);
			}

		}

	}

}