package org.egov.infra.events.processing;

import java.util.Map;

public interface EventCallBack {
	public void execute(Map<String, String> params);

}
