/*
 * @(#)BoundaryTypeDelegate.java 3.0, 18 Jun, 2013 1:21:48 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.delegate;


public class BoundaryTypeDelegate {
	/*private static BoundaryTypeDelegate boundaryTypeDelegate = new BoundaryTypeDelegate();
	private static final Logger logger = LoggerFactory.getLogger(BoundaryTypeDelegate.class);
	private final BoundaryTypeService boundaryTypeService = new BoundaryTypeServiceImpl(null);

	private BoundaryTypeDelegate() {
	}

	public static BoundaryTypeDelegate getInstance() {
		return boundaryTypeDelegate;
	}

	*//**
	 * This method creates a BoundaryType.
	 * @param BoundaryType object
	 * @exception DuplicateElementException
	 * @exception EGOVException
	 *//*
	public void createBoundaryType(final BoundaryType bType) throws DuplicateElementException, EGOVException {
		BoundaryType parentBoundaryType = null;
		validateBoundryType(bType);

		final String parentBoundaryTypeName = bType.getParentName();

		Long hn = 0l;
		final BoundaryType chBt = new BoundaryType();

		if (parentBoundaryTypeName != null && !parentBoundaryTypeName.trim().equalsIgnoreCase("null")) {
			parentBoundaryType = this.boundaryTypeService.getBoundaryType(parentBoundaryTypeName, bType.getHierarchyType());
			hn = parentBoundaryType.getHierarchy();

		}
		//chBt.setHeirarchy(nhn);
		//chBt.setHeirarchyType(bType.getHeirarchyType());
		chBt.setName(bType.getName());
		//chBt.setBndryTypeLocal(bType.getBndryTypeLocal());

		if (parentBoundaryType != null) {
			parentBoundaryType.addChildBoundaryType(chBt);
		}

		this.boundaryTypeService.createBoundaryType(chBt);

	}

	*//**
	 * This method Updates already existing BoundaryType.
	 * @param BoundaryType object
	 * @exception EGOVRuntimeException
	 * @exception EGOVException
	 *//*
	public void updateBoundaryType(final BoundaryType bType) throws EGOVRuntimeException, EGOVException {
		validateBoundryType(bType);
		try {

			BoundaryType tempBType = null;
			if (bType.getName() != null && bType.getParentName() != null) {

				//tempBType = this.boundaryTypeService.getBoundaryType(bType.getParentName(), bType.getHeirarchyType());
				tempBType.setName(bType.getName());
				//tempBType.setBndryTypeLocal(bType.getBndryTypeLocal());
				this.boundaryTypeService.updateBoundaryType(tempBType);
			} else {
				throw new EGOVRuntimeException("Parameters are Invalid");
			}
		}

		catch (final EGOVRuntimeException erx) {
			logger.error("EGOVRuntimeException Encountered!!!" + erx.getMessage());
			throw new EGOVRuntimeException("Internal Server Error in Updating Boundry Type", erx);

		} catch (final Exception ex) {
			logger.error("EGOVRuntimeException Encountered!!!" + ex.getMessage());
			throw new EGOVRuntimeException("Internal Server Error in Updating Boundry Type", ex);

		}
	}

	*//**
	 * This method Deletes an already existing BoundaryType. In the deletion process all the child BoundaryTypes are also deleted.
	 * @param BoundaryType object
	 * @exception EGOVRuntimeException
	 * @exception EGOVException
	 *//*
	public void deleteBoundaryType(final BoundaryType bType) throws EGOVRuntimeException, EGOVException {
		validateBoundryType(bType);
		try {
			BoundaryType tempBType = null;
			BoundaryType parentBType = null;
			if (bType.getName() != null && bType.getParentName() != null) {

				short hn = 0;
				//parentBType = this.boundaryTypeService.getBoundaryType(bType.getParentName(), bType.getHeirarchyType());
				if (!parentBType.getChildBoundaryTypes().isEmpty()) {
					tempBType = (BoundaryType) parentBType.getChildBoundaryTypes().iterator().next();
				} else {
					tempBType = parentBType;
				}
				tempBType.setParent(null);
				this.boundaryTypeService.updateBoundaryType(tempBType);
				this.boundaryTypeService.removeBoundaryType(tempBType);
				parentBType.setChildBoundaryTypes(new HashSet());

			} else {
				throw new EGOVRuntimeException("Parameters are Invalid");
			}

			logger.info("BoundaryTypeDelegate::updateBoundaryType():: After Updating Boundry Type  ");
		} catch (final EGOVRuntimeException erx) {
			logger.error("EGOVRuntimeException Encountered!!!" + erx.getMessage());
			throw new EGOVRuntimeException("Internal Server Error in Updating Boundry Type", erx);

		} catch (final Exception ex) {
			logger.info("EGOVRuntimeException Encountered!!!" + ex.getMessage());
			throw new EGOVRuntimeException("Internal Server Error in Updating Boundry Type", ex);

		}
	}

	
	 * This method validates the Boundry
	 
	private void validateBoundryType(final BoundaryType boundaryType) throws EGOVRuntimeException {
		if (boundaryType != null) {
			if (boundaryType.getName() == null || boundaryType.getName().trim().equals("")) {
				throw new EGOVRuntimeException("BoundryType Name is Invalid Please Check !!");
			}
		} else {
			throw new EGOVRuntimeException("Internal Server Error!!");
		}

	}
*/
}
