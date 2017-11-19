/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.infra.web.taglib;

import org.apache.log4j.Logger;

import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class BoundryTag extends BodyTagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object name = null;
	private String formName = "";
	private String[] functionName = null;
	// private HashMap bndryCombo = new HashMap();
	private static Logger logger = Logger.getLogger(BoundryTag.class);

	public Object getName() {
		return this.name;
	}

	public void setName(final Object name) {
		this.name = name;
	}

	public String getFormName() {
		return this.formName;
	}

	public void setFormName(final String formName) {
		this.formName = formName;
	}

	public static String removeSpaces(final String s) {
		if (s == null) {
			return s;
		}

		final StringBuffer methodName = new StringBuffer(15);
		final StringTokenizer st = new StringTokenizer(s);
		while (st.hasMoreTokens()) {
			methodName.append(st.nextToken());
		}

		return methodName.toString();
	}

	public Map getExtractMap(final List al, final Map typeMap) {
		final Set set = typeMap.keySet();
		final HashMap hm = new HashMap(typeMap);
		logger.debug("set............." + set);
		for (final Iterator itr = set.iterator(); itr.hasNext();) {
			logger.debug("1111111111");
			final Integer i = (Integer) itr.next();
			logger.debug("22222222222");
			final String s = (String) typeMap.get(i);
			logger.debug("3333333333");
			for (final Iterator itr1 = al.iterator(); itr1.hasNext();) {
				logger.debug("44444444");
				final String s1 = (String) itr1.next();
				logger.debug("s1............." + s1 + s);
				if (s.equals(s1)) {
					hm.remove(i);

				}
				logger.debug("555555555555");
			}
			logger.debug("666666666666");

		}
		return hm;
	}

	@Override
	public int doStartTag() throws javax.servlet.jsp.JspTagException {
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() {

		return EVAL_PAGE;
	}

	private String createCombo(final Integer bndryid, final String formName, final String[] functionName) {

		/*
		 * final HeirarchyType htype = (HeirarchyType) this.getName(); // HeirarchyType htype = hmang.getHeirarchyTypeByID(1); final String bndryName = bmang.getBoundaryNameForID(bndryid); final
		 * Boundary topLevelBoundary = (Boundary) bmang.getTopBoundary(bndryName, htype); logger.info("topLevelBoundary>>>>>>>>>>>>>>>>>>>>>>>>" + topLevelBoundary.getName()); final int j = 1;
		 * StringBuilder bndrySelect1 = new StringBuilder(""); ; String bndrySelect2 = ""; final EgovInfrastrUtilInteface inteface = new EgovInfrastrUtil(); final ArrayList al = (ArrayList)
		 * ExcludeBndryType.getExcludeType(); final Map typeMap = inteface.getMapOfBoundryTypes(); logger.debug("typeMap>>>>>>>>>>>>>>>>>>>>>>>>" + typeMap.keySet()); final Map typeMapm1 =
		 * this.getExtractMap(al, typeMap); logger.debug("typeMap>>>>>>>>>>>>>>>>>>>>>>>>" + typeMapm1); final Set typeset = new TreeSet(typeMapm1.keySet()); final Iterator itr = typeset.iterator();
		 * if (itr.hasNext()) { final Integer parentType = (Integer) itr.next(); final String typeName = (String) typeMapm1.get(parentType); logger.debug("typeName>>>>>>>>>>>>>>>>>>>>>>>>" +
		 * typeName); final BoundaryType btype = (BoundaryType) btmang.getBoundaryType(typeName, htype); // List bondrySet = (List)bmang.getAllBoundaries(btype,topLevelBoundary.getId().intValue());
		 * final List bondrySet = inteface.getBndryTypeBndryList(topLevelBoundary, btype); logger.debug("bondrySet>>>>>>>>>>>>>>>>>>>>>>>>" + bondrySet); if (bondrySet != null && !bondrySet.isEmpty())
		 * { final Iterator bsetitr = bondrySet.iterator(); final List bndryidlist = new ArrayList(); while (bsetitr.hasNext()) { final Boundary bundary = (Boundary) bsetitr.next();
		 * bndryidlist.add(bundary.getId()); } final Map bndrymap = inteface.getBoundaryMap(); logger.debug("bndryidlist>>>>>>>>>>>>>>>>>>>>>>>>" + bndryidlist); final String admin = "adminBndry" + j;
		 * String onchang = "javascript:" + removeSpaces(typeName) + "Match" + formName + "('null');zoneDeptcheck();"; if (functionName != null) { if (functionName.length != 0) { onchang =
		 * "javascript:" + removeSpaces(typeName) + "Match" + formName + "('null');zoneDeptcheck();" + removeSpaces(functionName[0]); } } if (typeset.size() == 1) { bndrySelect1 = new
		 * StringBuilder("<TR  ><TD valign=\"top\" width=\"50%\" colspan = \"4\" align=\"left\" class=\"eGovTblContent\" ><font id=\"normal_font\" >" + typeName + "</font>" + "<select name=\"" + admin
		 * + "\" class=\"controlText\" onchange = \"" + onchang + "\" tabindex = \"1\">" + "<option value=\"\">----Choose----"); } else if (typeset.size() == 2) { bndrySelect1 = new
		 * StringBuilder("<TR  ><TD valign=\"top\" width=\"50%\" colspan = \"2\" align=\"left\" class=\"eGovTblContent\" ><font id=\"normal_font\" >" + typeName + "</font>" + "<select name=\"" + admin
		 * + "\" class=\"controlText\" onchange = \"" + onchang + "\" tabindex = \"1\">" + "<option value=\"\">----Choose----"); } else if (typeset.size() >= 3) { bndrySelect1 = new
		 * StringBuilder("<TR width=\"100%\" ><TD valign=\"top\" width=" + 100 / typeset.size() + "%  align=\"left\" class=\"eGovTblContent\" ><font id=\"normal_font\" >" + typeName + "</font>" +
		 * "<select name=\"" + admin + "\" class=\"controlText\" onchange = \"" + onchang + "\" tabindex = \"1\">" + "<option value=\"\">----Choose----</option>"); } String name = ""; for (final
		 * Iterator iter = bndryidlist.iterator(); iter.hasNext();) { final Integer bndryId = (Integer) iter.next(); name = (String) bndrymap.get(bndryId); bndrySelect1.append("<option value=\"" +
		 * bndryId.toString() + "\">" + name); } bndrySelect1.append("</select> "); // Commenting because the links are not working since Maps are not available bndrySelect1 += "<a name=\"find" +
		 * typeName.toLowerCase() + "id\" href=\"javascript:get" + typeName + "Value();\" > Find" + typeName + "</a>"; } } int count = 1; int x = 2; while (itr.hasNext()) { final Integer parentType =
		 * (Integer) itr.next(); final String typeName = (String) typeMapm1.get(parentType); logger.debug("typeName>>>>>>>>>>>>>>>>>>>>>>>>" + typeName); final BoundaryType btype = (BoundaryType)
		 * btmang.getBoundaryType(typeName, htype); final Map bndrymap = inteface.getBoundaryMap(); final String admin = "adminBndry" + x; String onchang = ""; if (functionName != null) { if ((count <
		 * functionName.length) && (functionName.length != 0)) { onchang = "javascript:" + removeSpaces(functionName[count]); } } if (btype.getChildBoundaryTypes() != null &&
		 * !btype.getChildBoundaryTypes().isEmpty()) { onchang = "javascript:" + removeSpaces(typeName) + "Match" + formName + "('null')"; if (functionName != null) { if ((count < functionName.length)
		 * && (functionName.length != 0)) { onchang = "javascript:" + removeSpaces(typeName) + "Match" + formName + "('null');" + removeSpaces(functionName[count]); } } } final String level =
		 * EGovConfig.getProperty("LEVEL", "", "ADMBNDRY"); if (typeset.size() >= 3) { bndrySelect2 += "<TD valign=\"top\" width=" + 100 / typeset.size() +
		 * "%   class=\"eGovTblContent\"  align=\"left\"><font id=\"normal_font\">" + typeName + "</font>" + "<select name=\"" + admin + "\"  class=\"controlText\" onchange = \"" + onchang +
		 * "\" tabindex = \"1\">" + "<option value=\"\">----Choose----</option>" + "</select>"; } else { bndrySelect2 +=
		 * "<TD valign=\"top\" width=\"50%\"  colspan = \"2\" class=\"eGovTblContent\"  align=\"left\"><font id=\"normal_font\">" + typeName + "</font>" + "<select name=\"" + admin +
		 * "\"  class=\"controlText\" onchange = \"" + onchang + "\" tabindex = \"1\">" + "<option value=\"\">----Choose----" + "</select>"; } // Commenting because the links are not working since
		 * Maps are not available if(!typeName.equals("Nyay Panchayat")) { if(getFormName()==null||getFormName().equals("")||getFormName().equals("0")) bndrySelect2 +=
		 * "<a name=\"find"+level+"id\" href=\"javascript:get"+removeSpaces(typeName)+"Value(document.forms[0].adminBndry1.options[document.forms[0].adminBndry1.selectedIndex].value);\" >Find" +
		 * typeName + "</a></TD>"; else bndrySelect2 +=
		 * "<a name=\"find"+level+"id\" href=\"javascript:get"+removeSpaces(typeName)+"Value(document."+getFormName()+".adminBndry1.options[document."+getFormName
		 * ()+".adminBndry1.selectedIndex].value);\" >Find" + typeName + "</a></TD>"; } x++; count++; } bndrySelect2 += "</tr>"; return bndrySelect1.toString() + bndrySelect2;
		 */
		return null;

	}

	/**
	 * methord added by deepak
	 * 
	 * @param:String toplevelBndryID,String formName
	 * @return:String javaScriptString
	 */
	public String getFnString(final String toplevelBndryID, final String formName, final String[] functionName) {
		final Map mainMap = new HashMap();
		String javaScriptString = "";
		if (mainMap.containsKey(toplevelBndryID)) {
			boolean formPresent = false;
			final Map form = new HashMap();
			final List listOfFormMaps = (List) mainMap.get(toplevelBndryID);
			final Iterator listIter = listOfFormMaps.iterator();
			while (listIter.hasNext()) {
				final Map formMap = (Map) listIter.next();
				if (formMap.containsKey(formName)) {
					javaScriptString = (String) formMap.get(formName);
					formPresent = true;
				}
			}
			if (!formPresent) {
				form.put(formName, this.createCombo(new Integer(toplevelBndryID), formName, functionName));
				listOfFormMaps.add(form);
				javaScriptString = (String) form.get(formName);
			}

		} else {
			final List listOfFormMaps = new ArrayList();
			final Map form = new HashMap();
			form.put(formName, this.createCombo(new Integer(toplevelBndryID), formName, functionName));
			listOfFormMaps.add(form);
			mainMap.put(toplevelBndryID, listOfFormMaps);
			javaScriptString = (String) form.get(formName);

		}
		return javaScriptString;
	}

	/**
	 * @return Returns the functionName.
	 */
	public String[] getFunctionName() {
		return this.functionName;
	}

	/**
	 * @param functionName The functionName to set.
	 */
	public void setFunctionName(final String[] functionName) {
		this.functionName = functionName;
	}
}
