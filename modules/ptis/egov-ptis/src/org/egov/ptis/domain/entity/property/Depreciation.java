package org.egov.ptis.domain.entity.property;

import java.io.Serializable;

/**
 * <p>
 * </p>
 * 
 * @poseidon-object-id [Im25721da6m10537ba33ffmm73ea]
 */
public class Depreciation implements Serializable {

	/**
	 * <p>
	 * Represents ...
	 * </p>
	 * 
	 * @poseidon-object-id [Im25721da6m10537ba33ffmm73d7]
	 */
	private Integer depriciationID;

	/**
	 * <p>
	 * Represents ...
	 * </p>
	 * 
	 * @poseidon-object-id [Im25721da6m10537ba33ffmm73c6]
	 */
	private Integer noOfYears;

	/**
	 * <p>
	 * Represents ...
	 * </p>
	 * 
	 * @poseidon-object-id [Im25721da6m10537ba33ffmm73b5]
	 */
	private int depreciation;

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(depriciationID).append("|NoOfYears:").append(noOfYears).append("|Depreciation: ")
				.append(depreciation);

		return objStr.toString();
	}
}
