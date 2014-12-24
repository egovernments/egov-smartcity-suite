package org.egov.lib.rrbac.model;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.egov.exceptions.RBACException;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.role.RoleImpl;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class ActionTest {

	@Test
	public void testActionClosure() throws Exception {
		final Action action = new Action();
		action.setName("name");
		action.setRoles(new HashSet());
		final Role role = new RoleImpl();
		role.setId(1);
		role.setRoleName("name");
		action.addRole(role);
		action.setRuleGroup(new HashSet());
		final RuleGroup rule = new RuleGroup();
		rule.setRoleId(role);
		rule.setName("name");
		action.addRuleGroup(rule);
		action.compareTo(action);
		try {
			action.compareTo(new Action());
		} catch (final ClassCastException e) {
			assertTrue(true);
		}
		action.equals(null);
		action.equals(action);
		action.equals(new Object());
		final Action newAct = new Action();
		newAct.setName("nam2323e1");
		action.equals(newAct);
		newAct.setName("name");
		action.equals(newAct);
		action.hashCode();
		action.getContextRoot();
		action.getDisplayName();
		action.getEntityId();
		action.getHelpURL();
		action.getId();
		action.getIsEnabled();
		action.getModule();
		action.getName();
		action.getOrderNumber();
		action.getQueryParams();
		action.getRoles();
		action.getRuleGroup();
		action.getTaskId();
		action.getUpdatedTime();
		action.getUrl();
		action.getUrlOrderId();
		action.setContextRoot(null);
		action.setDisplayName(null);
		action.setEntityId(null);
		action.setHelpURL(null);
		action.setId(1);
		action.setIsEnabled(1);
		action.setModule(null);
		action.setOrderNumber(1);
		action.setQueryParams(null);
		action.setTaskId(null);
		action.setUpdatedTime(null);
		action.setUrl(null);
		action.setUrlOrderId(1);
		final AccountCodeRule ruleData = new AccountCodeRule();
		ruleData.setName("name");
		Rules rules = ruleData;
		rules.setActive(0);
		HashSet ruleSet = new HashSet();
		ruleSet.add(rules);
		rule.setRules(ruleSet);
		action.addRuleGroup(rule);
		action.isValid(ruleData, role);
		action.isActionValid(role);
		try {
			action.isActionValid(new RoleImpl());
		} catch (final RBACException e) {
			assertTrue(true);
		}
		rules = Mockito.mock(Rules.class);
		rules.setActive(1);
		Mockito.when(rules.isValid(Matchers.any(RuleData.class))).thenThrow(
				new RBACException());
		ruleSet = new HashSet();
		ruleSet.add(rules);
		rule.setRules(ruleSet);
		action.addRuleGroup(rule);
		ruleData.setIncluded(1);
		ruleData.setExcluded(0);
		try {
			action.isValid(ruleData, role);
		} catch (final RBACException e) {
			assertTrue(true);
		}
		action.removeRole(role);
		action.removeRuleGroup(rule);
		assertTrue(true);

	}

}
