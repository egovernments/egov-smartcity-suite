package org.egov.lib.admbndry.ejb.api;

import java.util.NoSuchElementException;
import java.util.Set;

import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.exceptions.TooManyValuesException;
import org.egov.infra.admin.master.entity.HierarchyType;

public interface HeirarchyTypeService {

	void create(HierarchyType heirarchyType) throws DuplicateElementException;

	void update(HierarchyType heirarchyType) throws NoSuchElementException;

	void remove(HierarchyType heirarchyType) throws NoSuchElementException;

	HierarchyType getHeirarchyTypeByID(int heirarchyTypeId);

	Set<HierarchyType> getAllHeirarchyTypes();

	HierarchyType getHierarchyTypeByName(String name) throws NoSuchObjectException, TooManyValuesException;
}
