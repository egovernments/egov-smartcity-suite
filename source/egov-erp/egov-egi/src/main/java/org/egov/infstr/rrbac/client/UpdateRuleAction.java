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

import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class UpdateRuleAction extends org.apache.struts.action.Action {

	private static final Logger LOG = LoggerFactory.getLogger(UpdateRuleAction.class);
	private final RbacService rbacService = new RbacServiceImpl();

	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		String target = "";
		String alertMessage = null;
		try {

			final org.apache.struts.action.DynaActionForm mfb = (org.apache.struts.action.DynaActionForm) form;

			final String ruleGroupId = (String) mfb.get("ruleGroupId");
			final Integer rgID = Integer.parseInt(ruleGroupId);
			AmountRule ar = null;
			FundRule fr = null;
			AccountCodeRule acr = null;
			final String[] ruleId = (String[]) mfb.get("ruleIdArr");
			final String[] ruleName = (String[]) mfb.get("ruleNameArr");
			final String[] defaultVal = (String[]) mfb.get("defaultArr");
			final String[] minRan = (String[]) mfb.get("minRangeArr");
			final String[] maxRan = (String[]) mfb.get("maxRangeArr");
			final String[] active = (String[]) mfb.get("activeArr");
			final String[] include = (String[]) mfb.get("includeListArr");
			final String[] exclude = (String[]) mfb.get("excludeListArr");
			final String[] includeId = (String[]) mfb.get("includeIdArr");
			final String[] excludeId = (String[]) mfb.get("excludeIdArr");

			for (int i = 0; i < ruleId.length; i++) {
				final RuleGroup ruleGroup = this.rbacService.getRuleGroupById(rgID);
				final Set ruleSet = ruleGroup.getRules();
				Rules rules = null;
				for (final Iterator itr = ruleSet.iterator(); itr.hasNext();) {
					rules = (Rules) itr.next();
					if (((rules.getId()).toString()).equals(ruleId[i])) {
						if (rules.getType().equalsIgnoreCase("AmountRule")) {
							ar = (AmountRule) rules;
							ar.setName(ruleName[i]);
							ar.setDefaultValue(defaultVal[i]);
							if (maxRan[i] != "") {
								ar.setMaxRange(Integer.valueOf(maxRan[i]));
							} else {
								ar.setMaxRange(new Integer(0));
							}
							if (minRan[i] != "") {
								ar.setMinRange(Integer.valueOf(minRan[i]));
							} else {
								ar.setMinRange(new Integer(0));
							}
							ar.setActive(Integer.valueOf(active[i]));

						}
						if (rules.getType().equalsIgnoreCase("FundRule")) {
							fr = (FundRule) rules;
							fr.setName(ruleName[i]);
							fr.setDefaultValue(defaultVal[i]);
							fr.setActive(Integer.valueOf(active[i]));
							if (includeId[i].length() > 0) {
								final StringTokenizer st = new StringTokenizer(includeId[i], ",");
								while (st.hasMoreTokens()) {
									final String incId = st.nextToken();
									if (incId.length() > 0) {
										fr.setIncluded(new Integer(0));
										final Set ieSet1 = fr.getIeList();
										final CopyOnWriteArraySet ieSet = new CopyOnWriteArraySet();
										for (final Iterator dup1 = ieSet1.iterator(); dup1.hasNext();) {
											final IEList ieLi = (IEList) dup1.next();
											ieSet.add(ieLi);
										}
										for (final Iterator itr1 = ieSet.iterator(); itr1.hasNext();) {
											final IEList ieLi = (IEList) itr1.next();
											fr.removeIeList(ieLi);
										}
									}
								}
							}
							if (excludeId[i].length() > 0) {
								final StringTokenizer st = new StringTokenizer(excludeId[i], ",");
								while (st.hasMoreTokens()) {
									final String excId = st.nextToken();
									if (excId.length() > 0) {
										fr.setExcluded(new Integer(0));
										final Set ieSet1 = fr.getIeList();
										final CopyOnWriteArraySet ieSet = new CopyOnWriteArraySet();
										for (final Iterator dup2 = ieSet1.iterator(); dup2.hasNext();) {
											final IEList ieLi = (IEList) dup2.next();
											ieSet.add(ieLi);
										}
										for (final Iterator itr2 = ieSet.iterator(); itr2.hasNext();) {
											final IEList ieLi = (IEList) itr2.next();
											fr.removeIeList(ieLi);
										}
									}
								}
							}
							if (include[i].length() > 0) {
								final StringTokenizer st = new StringTokenizer(include[i], ",");
								while (st.hasMoreTokens()) {
									final String incIdVal = st.nextToken();
									if (incIdVal.length() > 0) {
										fr.setIncluded(new Integer(1));
										final IEList ieList = new IEList();
										ieList.setRuleId(fr);
										ieList.setType("I");
										ieList.setValue(incIdVal);
										fr.addIeList(ieList);
									}
								}
							}
							if (exclude[i].length() > 0) {
								final StringTokenizer st = new StringTokenizer(exclude[i], ",");
								while (st.hasMoreTokens()) {
									final String excIdVal = st.nextToken();
									if (excIdVal.length() > 0) {
										fr.setExcluded(new Integer(1));
										final IEList ieList = new IEList();
										ieList.setRuleId(fr);
										ieList.setType("E");
										ieList.setValue(excIdVal);
										fr.addIeList(ieList);
									}
								}
							}
						}
						if (rules.getType().equalsIgnoreCase("AccountCodeRule")) {
							acr = (AccountCodeRule) rules;
							acr.setName(ruleName[i]);
							acr.setDefaultValue(defaultVal[i]);
							acr.setActive(Integer.valueOf(active[i]));
							if (includeId[i].length() > 0) {
								final StringTokenizer st = new StringTokenizer(includeId[i], ",");
								while (st.hasMoreTokens()) {
									final String incId = st.nextToken();
									if (incId.length() > 0) {
										acr.setIncluded(new Integer(0));
										final Set ieSet1 = acr.getIeList();
										final CopyOnWriteArraySet ieSet = new CopyOnWriteArraySet();
										for (final Iterator dup3 = ieSet1.iterator(); dup3.hasNext();) {
											final IEList ieLi = (IEList) dup3.next();
											ieSet.add(ieLi);
										}
										for (final Iterator itr3 = ieSet.iterator(); itr3.hasNext();) {
											final IEList ieLi = (IEList) itr3.next();
											acr.removeIeList(ieLi);
										}

									}
								}
							}
							if (excludeId[i].length() > 0) {
								final StringTokenizer st = new StringTokenizer(excludeId[i], ",");
								while (st.hasMoreTokens()) {
									final String excId = st.nextToken();
									if (excId.length() > 0) {
										acr.setExcluded(new Integer(0));
										final Set ieSet1 = acr.getIeList();
										final CopyOnWriteArraySet ieSet = new CopyOnWriteArraySet();
										for (final Iterator dup4 = ieSet1.iterator(); dup4.hasNext();) {
											final IEList ieLi = (IEList) dup4.next();
											ieSet.add(ieLi);
										}
										for (final Iterator itr4 = ieSet.iterator(); itr4.hasNext();) {
											final IEList ieLi = (IEList) itr4.next();
											acr.removeIeList(ieLi);
										}
									}
								}
							}
							if (include[i].length() > 0) {
								final StringTokenizer st = new StringTokenizer(include[i], ",");
								while (st.hasMoreTokens()) {
									final String incIdVal = st.nextToken();
									if (incIdVal.length() > 0) {
										acr.setIncluded(new Integer(1));
										final IEList ieList = new IEList();
										ieList.setRuleId(acr);
										ieList.setType("I");
										ieList.setValue(incIdVal);
										acr.addIeList(ieList);
									}
								}
							}
							if (exclude[i].length() > 0) {
								final StringTokenizer st = new StringTokenizer(exclude[i], ",");
								while (st.hasMoreTokens()) {
									final String excIdVal = st.nextToken();
									if (excIdVal.length() > 0) {
										acr.setExcluded(new Integer(1));
										final IEList ieList = new IEList();
										ieList.setRuleId(acr);
										ieList.setType("E");
										ieList.setValue(excIdVal);
										acr.addIeList(ieList);
									}
								}
							}
						}

					}
				}
			}

			HibernateUtil.getCurrentSession().flush();
			alertMessage = "Executed successfully";
			req.setAttribute("alertMessage", alertMessage);

			target = "success";
		} catch (final Exception ex) {
			target = "error";
			LOG.error("Exception Encountered!!!" + ex.getMessage());
			throw new EGOVRuntimeException("Exception occured -----> " + ex.getMessage());

		}
		return mapping.findForward(target);

	}

}
