/*
 * @(#)GenericCommonsManager.java 3.0, 17 Jun, 2013 11:22:28 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.commons.service;

import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.commons.Module;
import org.egov.lib.address.model.AddressTypeMaster;

public interface GenericCommonsService {
	/**
	 * Returns a module object identified by its identifier.
	 * @return Module object if found or null refernce if not found in the system.
	 * @see Module.java
	 */
	public Module getModuleByID(Integer id);

	/**
	 * Persists the module passed in. Also rolls back the current transaction, if it fails to create the module.
	 * @param module
	 * @throws EGOVRuntimeException, if it fails to create.
	 * @see Module.java
	 */

	public void createModule(Module module);

	/**
	 * Deletes the module passed in. Also rolls back the current transaction, if it fails to delete the module.
	 * @param module
	 * @throws EGOVRuntimeException, if it fails to delete.
	 * @see Module.java
	 */

	public void deleteModule(Module module);

	/**
	 * Updates the module passed in. Also rolls back the current transaction, if it fails to update the module.
	 * @param module
	 * @throws EGOVRuntimeException, if it fails to update.
	 * @see Module.java
	 */

	public void updateModule(Module module);

	/**
	 * This method looks up the system for the passed module name and returns if found.
	 * @param moduleName
	 * @return Module object representing the given name, or null refrence if not found.
	 * @throws EGOVRuntimeException, if it finds any system error.
	 */
	public Module getModuleByName(String moduleName);

	/**
	 * This method looks up the system for all modules in the system and returns if found.
	 * @param moduleName
	 * @return List of module objects, or empty list refrence if none are found.
	 * @throws EGOVRuntimeException, if it finds any system error.
	 */

	public List<Module> getAllModules();

	/**
	 * Returns the Owner Id if found, otherwise throws an exception
	 * @param string
	 * @return AddressTypeMaster
	 */
	public AddressTypeMaster getAddressType(String addrTypeName);
}
