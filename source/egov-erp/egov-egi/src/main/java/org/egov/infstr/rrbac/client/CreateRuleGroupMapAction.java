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
package org.egov.infstr.rrbac.client;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rrbac.model.AccountCodeRule;
import org.egov.lib.rrbac.model.AmountRule;
import org.egov.lib.rrbac.model.FundRule;
import org.egov.lib.rrbac.model.IEList;
import org.egov.lib.rrbac.model.RuleGroup;
import org.egov.lib.rrbac.model.Rules;
import org.egov.lib.rrbac.services.RbacService;
import org.egov.lib.rrbac.services.RbacServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateRuleGroupMapAction extends org.apache.struts.action.Action {

	private static final Logger logger = LoggerFactory.getLogger(CreateRuleGroupMapAction.class);
	private final RbacService rbacService = new RbacServiceImpl();

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "";
		String alertMessage = null;

		try {
			final org.apache.struts.action.DynaActionForm mfb = (org.apache.struts.action.DynaActionForm) form;

			final Integer rgID = (Integer) mfb.get("ruleGroupId");
			final String ruleName = (String) mfb.get("ruleName");
			final String defaultValue = (String) mfb.get("default");
			final String type = (String) mfb.get("type");
			final String minRange = (String) mfb.get("minRange");
			Integer min = null;
			Integer max = null;
			if (!minRange.equalsIgnoreCase("") || !(minRange.length() == 0)) {
				min = Integer.parseInt(minRange);
			}
			final String maxRange = (String) mfb.get("maxRange");
			if (!maxRange.equalsIgnoreCase("") || !(maxRange.length() == 0)) {
				max = Integer.parseInt(maxRange);
			}
			final String active = (String) mfb.get("active");
			Integer acti;
			if (active.equals("on")) {
				acti = new Integer(1);
			} else {
				acti = new Integer(0);
			}
			final String include = (String) mfb.get("include");
			Integer incl;
			if (include.equals("on")) {
				incl = new Integer(1);
			} else {
				incl = new Integer(0);
			}
			final String exclude = (String) mfb.get("exclude");
			Integer excl;
			if (exclude.equals("on")) {
				excl = new Integer(1);
			} else {
				excl = new Integer(0);
			}
			final String includeList = (String) mfb.get("includeList");
			final String excludeList = (String) mfb.get("excludeList");
			final RuleGroup ruleGroup = this.rbacService.getRuleGroupById(rgID);
			final Rules rules = this.rbacService.getRuleByName(ruleName);
			if (rules == null) {
				if (type.equals("AmountRule")) {
					final AmountRule rs = new AmountRule();
					rs.setName(ruleName);
					rs.setDefaultValue(defaultValue);
					rs.setMinRange(min);
					rs.setMaxRange(max);
					rs.setType(type);
					rs.setActive(acti);
					ruleGroup.addRules(rs);
					HibernateUtil.getCurrentSession().flush();
					alertMessage = "Executed successfully";
					target = "success";

				} else if (type.equals("FundRule")) {
					final FundRule fr = new FundRule();
					fr.setName(ruleName);
					fr.setDefaultValue(defaultValue);
					fr.setIncluded(incl);
					fr.setExcluded(excl);
					fr.setType(type);
					fr.setActive(acti);
					ruleGroup.addRules(fr);
					if (includeList.length() > 0) {
						final StringTokenizer st = new StringTokenizer(includeList, ",");
						IEList ieList = null;
						while (st.hasMoreTokens()) {
							ieList = new IEList();
							ieList.setType("I");
							ieList.setValue(st.nextToken());
							ieList.setRuleId(fr);
							fr.addIeList(ieList);

						}

					}
					if (excludeList.length() > 0) {
						final StringTokenizer st = new StringTokenizer(excludeList, ",");
						IEList ieList = null;
						while (st.hasMoreTokens()) {
							ieList = new IEList();
							ieList.setType("E");
							ieList.setValue(st.nextToken());
							ieList.setRuleId(fr);
							fr.addIeList(ieList);

						}

					}

					HibernateUtil.getCurrentSession().flush();
					alertMessage = "Executed successfully";
					target = "success";
				} else if (type.equals("AccountCodeRule")) {
					final AccountCodeRule ac = new AccountCodeRule();
					ac.setName(ruleName);
					ac.setDefaultValue(defaultValue);
					ac.setIncluded(incl);
					ac.setExcluded(excl);
					ac.setType(type);
					ac.setActive(acti);
					ruleGroup.addRules(ac);
					if (includeList.length() > 0) {
						final StringTokenizer st = new StringTokenizer(includeList, ",");
						IEList ieList = null;
						while (st.hasMoreTokens()) {
							ieList = new IEList();
							ieList.setType("I");
							ieList.setValue(st.nextToken());
							ieList.setRuleId(ac);
							ac.addIeList(ieList);
						}

					}
					if (excludeList.length() > 0) {
						final StringTokenizer st = new StringTokenizer(excludeList, ",");
						IEList ieList = null;
						while (st.hasMoreTokens()) {
							ieList = new IEList();
							ieList.setType("E");
							ieList.setValue(st.nextToken());
							ieList.setRuleId(ac);
							ac.addIeList(ieList);
						}

					}

					HibernateUtil.getCurrentSession().flush();
					alertMessage = "Executed successfully";
					target = "success";

				}

				else {
					target = "error";
					logger.error("UnKnown Rule Type");
					alertMessage = "Configuration error.plrase report this to administrator.";

				}
			} else {
				alertMessage = "Duplicate Rule name.";
				target = "success";
			}
			req.setAttribute("alertMessage", alertMessage);

		}

		catch (final Exception ex) {
			target = "error";
			logger.error("Exception Encountered!!!" + ex.getMessage());
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());

		}
		return mapping.findForward(target);
	}

}
