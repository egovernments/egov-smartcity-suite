/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
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
