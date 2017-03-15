/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.ptis.client.workflow;

import org.egov.eis.service.EisCommonService;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;

public abstract class WorkflowActionStep {


	protected PropertyImpl propertyModel;
	protected EisCommonService eisCommonService;
	protected Long userId;
	protected PropertyTaxUtil propertyTaxUtil;
	protected String actionName;
	private String comments;

	public WorkflowActionStep() {}

	public WorkflowActionStep(PropertyImpl propertyModel, Long userId, String comments) {
		this.propertyModel = propertyModel;
		this.userId = userId;
		this.comments = comments;
	}

	/**
	 * Gives the workflow step as Approve, Save, Forward or Reject
	 * @return
	 */
	public abstract String getStepName();

	/**
	 *
	 * @return
	 */
	public abstract String getStepValue();

	/**
	 * Gives the position to which the workflow item to be saved, forwarded etc
	 * @return
	 */
	public Position getPosition()  {
		return eisCommonService.getPositionByUserId(userId);
	}

	/**
	 * Changes the state
	 */
	public void changeState() {
		propertyModel.transition().progressWithStateCopy();
	}

	public Property getPropertyModel() {
		return propertyModel;
	}

	public void setPropertyModel(PropertyImpl propertyModel) {
		this.propertyModel = propertyModel;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public PropertyTaxUtil getPropertyTaxUtil() {
		return propertyTaxUtil;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

	public void setEisCommonService(EisCommonService eisCommonService) {
		this.eisCommonService = eisCommonService;
	}

}
