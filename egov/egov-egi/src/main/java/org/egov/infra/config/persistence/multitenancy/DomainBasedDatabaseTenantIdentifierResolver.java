package org.egov.infra.config.persistence.multitenancy;

import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class DomainBasedDatabaseTenantIdentifierResolver implements CurrentTenantIdentifierResolver {

	@Override
	public String resolveCurrentTenantIdentifier() {
		return EGOVThreadLocals.getTenantID() == null ? "ezgovDatabasePool" : EGOVThreadLocals.getTenantID();
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

}
