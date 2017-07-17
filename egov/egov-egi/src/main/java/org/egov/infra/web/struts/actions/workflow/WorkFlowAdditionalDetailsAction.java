/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.web.struts.actions.workflow;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.entity.WorkflowTypes;
import org.egov.infra.workflow.matrix.entity.WorkFlowAdditionalRule;
import org.egov.infra.workflow.matrix.service.WorkFlowAdditionalDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@ParentPackage("egov")
public class WorkFlowAdditionalDetailsAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	public WorkFlowAdditionalRule wfAdditionalRule = new WorkFlowAdditionalRule();
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowAdditionalDetailsAction.class);
	private List<WorkflowTypes> objectList = new ArrayList<WorkflowTypes>();
	private WorkFlowAdditionalDetailsService workFlowAdditionalDetailsService;
	private List<String> buttonList = new ArrayList<String>();
	private List<String> statusList = new ArrayList<String>();
	private List<String> stateList = new ArrayList<String>();
	private List<String> actionList = new ArrayList<String>();
	private Long objectType;
	private String additionalRules;
	private String mode;
	private List<WorkFlowAdditionalRule> additionalRuleList = new ArrayList<WorkFlowAdditionalRule>();

	public List<WorkFlowAdditionalRule> getAdditionalRuleList() {
		return this.additionalRuleList;
	}

	public void setAdditionalRuleList(final List<WorkFlowAdditionalRule> additionalRuleList) {
		this.additionalRuleList = additionalRuleList;
	}

	public String getAdditionalRules() {
		return this.additionalRules;
	}

	public void setAdditionalRules(final String additionalRules) {
		this.additionalRules = additionalRules;
	}

	public Long getObjectType() {
		return this.objectType;
	}

	public void setObjectType(final Long objectType) {
		this.objectType = objectType;
	}

	public List<String> getButtonList() {
		return this.buttonList;
	}

	public void setButtonList(final List<String> buttonList) {
		this.buttonList = buttonList;
	}

	public List<String> getStateList() {
		return this.stateList;
	}

	public void setStateList(final List<String> stateList) {
		this.stateList = stateList;
	}

	public List<String> getActionList() {
		return this.actionList;
	}

	public void setActionList(final List<String> actionList) {
		this.actionList = actionList;
	}

	public String getMode() {
		return this.mode;
	}

	public void setMode(final String mode) {
		this.mode = mode;
	}

	public WorkFlowAdditionalDetailsService getWorkFlowAdditionalDetailsService() {
		return this.workFlowAdditionalDetailsService;
	}

	public void setWorkFlowAdditionalDetailsService(final WorkFlowAdditionalDetailsService workFlowAdditionalDetailsService) {
		this.workFlowAdditionalDetailsService = workFlowAdditionalDetailsService;
	}

	public WorkFlowAdditionalRule getWfAdditionalRule() {
		return this.wfAdditionalRule;
	}

	public void setWfAdditionalRule(final WorkFlowAdditionalRule wfAdditionalRule) {
		this.wfAdditionalRule = wfAdditionalRule;
	}

	public WorkFlowAdditionalDetailsAction() {
		addRelatedEntity("objecttypeid", WorkflowTypes.class);

	}

	@Override
	public void prepare() {

		LOGGER.info("Prepare Method is called");
		super.prepare();
		this.objectList = this.workFlowAdditionalDetailsService.getobjectTypeList();
		this.statusList = this.workFlowAdditionalDetailsService.getAllModuleTypeforStatus();
		if (getObjectType() != null && !getObjectType().equals("-1") && !getObjectType().equals("")) {
			this.additionalRuleList = this.workFlowAdditionalDetailsService.getAdditionalRulesbyObject(getObjectType());
		}
		addDropdownData("objectTypeList", this.objectList);
		addDropdownData("statusList", this.statusList);
		addDropdownData("additionalRuleList", this.additionalRuleList);
		LOGGER.info("Prepare Method is ended");

	}

	@Override
	public Object getModel() {

		return this.wfAdditionalRule;
	}

	public String newForm() {
		return "search";
	}

	public String buildWorkFlowDetails() {
		LOGGER.info("BuildWorkFlowDetails Method is called");
		this.buttonList.add("Approve");
		this.buttonList.add("Reject");
		this.actionList.add("END");
		LOGGER.info("BuildWorkFlowDetails Method is ended");
		return NEW;
	}

	public String create() {
		LOGGER.info("create Method is called");
		if (this.wfAdditionalRule.getObjecttypeid() == null) {
			addActionError("Please Select ObjectType");
		}
		if (this.wfAdditionalRule.getAdditionalRule() == null && this.wfAdditionalRule.getAdditionalRule().equals("-1")) {
			setAdditionalRules(null);
		}
		WorkFlowAdditionalRule additionalRuleobj = null;
		if (this.wfAdditionalRule.getId() == null) {
			additionalRuleobj = this.workFlowAdditionalDetailsService.getObjectbyTypeandRule(this.wfAdditionalRule.getObjecttypeid().getId(), this.wfAdditionalRule.getAdditionalRule());
		} else {
			additionalRuleobj = this.workFlowAdditionalDetailsService.getObjectbyTypeandRule(this.wfAdditionalRule.getId(), this.wfAdditionalRule.getObjecttypeid().getId(), this.wfAdditionalRule.getAdditionalRule());
		}
		if (additionalRuleobj == null) {
			setStateActionandButtonforObject();
			this.workFlowAdditionalDetailsService.save(this.wfAdditionalRule);
			setStateActionandButtonListforDisplay();
			setMode("view");
		} else {
			addActionError("Details already present for the objecttype and additional rule");
			return NEW;
		}
		LOGGER.info("create Method is ended");
		return NEW;
	}

	public String view() {

		LOGGER.info("view Method is called");

		if (getObjectType() == null) {
			addActionError("Please Select ObjectType");
		}
		if (getAdditionalRules() == null && getAdditionalRules().equals("-1")) {
			setAdditionalRules(null);
		}
		this.wfAdditionalRule = this.workFlowAdditionalDetailsService.getObjectbyTypeandRule(getObjectType(), getAdditionalRules());
		if (this.wfAdditionalRule == null) {
			addActionError("No details found for the objecttype");
			return "search";
		}
		setStateActionandButtonListforDisplay();
		setMode("view");
		LOGGER.info("view Method is ended");
		return NEW;
	}

	public String modify() {

		LOGGER.info("modify Method is Started");
		if (getObjectType() == null) {
			addActionError("Please Select ObjectType");
		}
		if (getAdditionalRules() == null || getAdditionalRules().equals("-1")) {
			setAdditionalRules(null);
		}
		this.wfAdditionalRule = this.workFlowAdditionalDetailsService.getObjectbyTypeandRule(getObjectType(), getAdditionalRules());
		if (this.wfAdditionalRule == null) {
			addActionError("No details found for the objecttype");
			return "search";
		}
		setStateActionandButtonListforDisplay();
		setMode("modify");
		LOGGER.info("modify Method is ended");
		return NEW;
	}

	public void setStateActionandButtonforObject() {
		LOGGER.info("setStateActionandButtonforObject Method is Started");
		final StringBuffer statestr = new StringBuffer();
		if (getStateList() != null) {
			for (String statename : getStateList()) {
				statename = statename.trim();
				if (!new String(statestr).toUpperCase().contains(statename.toUpperCase())) {
					statestr.append(statename);
					statestr.append(",");
				}
			}
		}

		final String statestring = new String(statestr).substring(0, statestr.length() - 1);
		this.wfAdditionalRule.setStates(statestring);

		final StringBuffer actionstr = new StringBuffer();
		if (getActionList() != null) {
			for (String actionname : getActionList()) {
				actionname = actionname.trim();
				if (!new String(actionstr).toUpperCase().contains(actionname.toUpperCase())) {
					actionstr.append(actionname);
					actionstr.append(",");
				}
			}
		}

		final String actionstring = new String(actionstr).substring(0, actionstr.length() - 1);
		this.wfAdditionalRule.setWorkFlowActions(actionstring);

		final StringBuffer buttonstr = new StringBuffer();
		if (getButtonList() != null) {
			for (String butname : getButtonList()) {
				butname = butname.trim();
				if (!new String(buttonstr).toUpperCase().contains(butname.toUpperCase())) {
					buttonstr.append(butname);
					buttonstr.append(",");
				}
			}
		}
		final String buttonstring = new String(buttonstr).substring(0, buttonstr.length() - 1);
		this.wfAdditionalRule.setButtons(buttonstring);
		LOGGER.info("setStateActionandButtonforObject Method is Ended");
	}

	public void setStateActionandButtonListforDisplay() {
		LOGGER.info("setStateActionandButtonListforDisplay Method is Started");
		if (this.wfAdditionalRule != null) {
			final List<String> statesList = new ArrayList<String>();
			if (this.wfAdditionalRule.getStates() != null) {
				final StringTokenizer strngtkn = new StringTokenizer(this.wfAdditionalRule.getStates(), ",");
				while (strngtkn.hasMoreTokens()) {
					final String statetkn = strngtkn.nextToken();
					if (!statesList.contains(statetkn)) {
						statesList.add(statetkn);
					}
				}
				if (statesList.size() != 0) {
					setStateList(statesList);
				}

			}

			final List<String> actionsList = new ArrayList<String>();
			if (this.wfAdditionalRule.getWorkFlowActions() != null) {
				final StringTokenizer strngtkn = new StringTokenizer(this.wfAdditionalRule.getWorkFlowActions(), ",");
				while (strngtkn.hasMoreTokens()) {
					final String statetkn = strngtkn.nextToken();
					if (!actionsList.contains(statetkn)) {
						actionsList.add(statetkn);
					}
				}
				if (actionsList.size() != 0) {
					setActionList(actionsList);
				}

			}

			final List<String> buttonList = new ArrayList<String>();
			if (this.wfAdditionalRule.getButtons() != null) {
				final StringTokenizer strngtkn = new StringTokenizer(this.wfAdditionalRule.getButtons(), ",");
				while (strngtkn.hasMoreTokens()) {
					final String statetkn = strngtkn.nextToken();
					if (!buttonList.contains(statetkn)) {
						buttonList.add(statetkn);
					}
				}
				if (buttonList.size() != 0) {
					setButtonList(buttonList);
				}

			}
		}

		LOGGER.info("setStateActionandButtonListforDisplay Method is Ended");
	}

	public String getAdditionalRulesforObject() {
		this.additionalRuleList = this.workFlowAdditionalDetailsService.getAdditionalRulesbyObject(getObjectType());
		return "additionalRule";
	}

}
