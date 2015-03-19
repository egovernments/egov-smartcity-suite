package org.egov.infra.events.processing;

import org.egov.infra.events.entity.schema.Response;
import org.springframework.stereotype.Component;

@Component
public interface ResponseHandler {
	public void respond(Response r);
}
