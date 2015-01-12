package org.egov.infstr.event.services;

import org.egov.infstr.event.listener.HibernateEventListener;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.internal.DefaultSaveOrUpdateEventListener;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

public class HibernateEventListenerIntegrator implements Integrator {

	@Override
	public void integrate(Configuration configuration, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
		registerCustomFilters(serviceRegistry);

	}

	@Override
	public void integrate(MetadataImplementor metadata, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
		registerCustomFilters(serviceRegistry);

	}

	@Override
	public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
		// Disintegration code have to be added.

	}

	private void registerCustomFilters(SessionFactoryServiceRegistry serviceRegistry) {
		final EventListenerRegistry eventListenerRegistry = serviceRegistry.getService(EventListenerRegistry.class);
		final DefaultSaveOrUpdateEventListener dfltSaveorUpdateListner = new DefaultSaveOrUpdateEventListener();
		final HibernateEventListener hibernateEventListener = new HibernateEventListener();
		eventListenerRegistry.setListeners(EventType.SAVE, hibernateEventListener, dfltSaveorUpdateListner);
		eventListenerRegistry.setListeners(EventType.UPDATE, hibernateEventListener, dfltSaveorUpdateListner);
		eventListenerRegistry.setListeners(EventType.SAVE_UPDATE, hibernateEventListener, dfltSaveorUpdateListner);
		eventListenerRegistry.prependListeners(EventType.PRE_UPDATE, hibernateEventListener);
	}

}
