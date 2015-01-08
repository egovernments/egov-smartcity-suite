package org.egov.lib.admbndry.ejb.api;

import java.util.NoSuchElementException;
import java.util.Set;

import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.exceptions.TooManyValuesException;
import org.egov.lib.admbndry.HeirarchyType;

public interface HeirarchyTypeService {

	void create(HeirarchyType heirarchyType) throws DuplicateElementException;

	void update(HeirarchyType heirarchyType) throws NoSuchElementException;

	void remove(HeirarchyType heirarchyType) throws NoSuchElementException;

	HeirarchyType getHeirarchyTypeByID(int heirarchyTypeId);

	Set<HeirarchyType> getAllHeirarchyTypes();

	HeirarchyType getHierarchyTypeByName(String name) throws NoSuchObjectException, TooManyValuesException;
}
