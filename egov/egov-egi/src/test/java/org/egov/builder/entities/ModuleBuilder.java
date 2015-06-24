package org.egov.builder.entities;

import org.egov.infra.admin.master.entity.Module;
import org.junit.Ignore;

/**
 * @author Ramki
 */
@Ignore
public class ModuleBuilder {
	private final Module module;

	public ModuleBuilder() {
		module = new Module();
	}

	public Module build() {
		return module;
	}

	public ModuleBuilder withName(final String name) {
		module.setName(name);
		return this;
	}
}
