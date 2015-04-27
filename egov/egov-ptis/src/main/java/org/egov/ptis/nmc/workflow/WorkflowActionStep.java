package org.egov.ptis.nmc.workflow;

import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.nmc.util.PropertyTaxUtil;

public abstract class WorkflowActionStep {


	protected PropertyImpl propertyModel;
	protected EisCommonsService eisCommonsService;
	protected Integer userId;
	protected PropertyTaxUtil propertyTaxUtil;
	protected String actionName;
	private String comments;

	public WorkflowActionStep() {}

	public WorkflowActionStep(PropertyImpl propertyModel, Integer userId, String comments) {
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
		return eisCommonsService.getPositionByUserId(userId);
	}

	/**
	 * Changes the state
	 */
	public void changeState() {
		propertyModel.transition(true);
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

	public void setEisCommonsService(EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}

}
