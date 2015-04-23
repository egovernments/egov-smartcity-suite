/*
 * Created on Aug 13, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.ptis.domain.entity.property;

/**
 * @author Admin
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class PropertyMutationOwner {
	private Integer mutationOwnerId = null;
	private Integer ownerId = null;
	private PropertyMutation propertyMutation = null;
	private Integer idPropMutation = null;

	/**
	 * @return Returns the idPropMutation.
	 */
	public Integer getIdPropMutation() {
		return idPropMutation;
	}

	/**
	 * @param idPropMutation
	 *            The idPropMutation to set.
	 */
	public void setIdPropMutation(Integer idPropMutation) {
		this.idPropMutation = idPropMutation;
	}

	/**
	 * @return Returns the mutationOwnerId.
	 */
	public Integer getMutationOwnerId() {
		return mutationOwnerId;
	}

	/**
	 * @param mutationOwnerId
	 *            The mutationOwnerId to set.
	 */
	public void setMutationOwnerId(Integer mutationOwnerId) {
		this.mutationOwnerId = mutationOwnerId;
	}

	/**
	 * @return Returns the ownerId.
	 */
	public Integer getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId
	 *            The ownerId to set.
	 */
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * @return Returns the propertyMutation.
	 */
	public PropertyMutation getPropertyMutation() {
		return propertyMutation;
	}

	/**
	 * @param propertyMutation
	 *            The propertyMutation to set.
	 */
	public void setPropertyMutation(PropertyMutation propertyMutation) {
		this.propertyMutation = propertyMutation;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getMutationOwnerId()).append("|OwnerId: ").append(getOwnerId()).append(
				"|PropertyMutation: ").append(getPropertyMutation()).append("|PropMutationId: ").append(
				getIdPropMutation());

		return objStr.toString();
	}
}
