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

package org.egov.infra.web.taglib;

import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.utils.StringUtils;

import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Administrator 
 */
public class BndryTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(BndryTag.class);

	public void appendFunctionCalls(final StringBuffer function, final Set childBndryTypes) {
		if (childBndryTypes != null) {
			for (final Iterator itr3 = childBndryTypes.iterator(); itr3.hasNext();) {
				final BoundaryType chBndryType = (BoundaryType) itr3.next();
				BoundaryType ptBndryType = null;
				if (chBndryType != null) {
					ptBndryType = chBndryType.getParent();
				}

				if (ptBndryType != null) {
					function.append(ptBndryType.getName() + "Match();\n");
				}

				if (chBndryType != null) {
					this.appendFunctionCalls(function, chBndryType.getChildBoundaryTypes());
				}
			}
		}

	}

	public String generateAllJSForBoundaries(final BoundaryType parentBndryType, final BoundaryType childBndryType, final Set parentBndrySet, final int i) {
		final String parentBndryTypeName = parentBndryType.getName();

		childBndryType.getName();

		// Integer childBndryID = childBndryType.getId();
		// Integer parentBndryID = parentBndryType.getId();

		final int childBndryID = i + 1;
		final int parentBndryID = i;

		final StringBuffer function = new StringBuffer(1000);

		function.append("\nfunction " + parentBndryTypeName + "Match(){\n");

		function.append("var len = document.forms[0].adminBndry" + childBndryID + ".options.length;\n");

		// function.append("var len1 = document.forms[0].adminBndry3.options.length;\n");

		function.append("for(var i=0;i<=len;i++)\n");
		function.append("{\n");
		function.append("\tdocument.forms[0].adminBndry" + childBndryID + ".options[0] = null;\n");
		function.append("}\n");
		// function.append("for(var i=0;i<=len1;i++)\n");
		// function.append("{\n");
		// function.append("	document.forms[0].adminBndry3.options[0] = null;\n");
		// function.append("}\n");
		function.append("if(document.forms[0].adminBndry" + parentBndryID + ".options[document.forms[0].adminBndry" + parentBndryID + ".selectedIndex].text == \"Choose\")\n");
		function.append("{\n");
		function.append("\tdocument.forms[0].adminBndry" + childBndryID + ".options[0] = new Option(\"Choose\",0,true,true);\n");
		// function.append("\tdocument.forms[0].adminBndry3.options[0] = new Option("Choose",0,true,true);\n");
		function.append("}\n");

		final Set childBndryTypes = childBndryType.getChildBoundaryTypes();

		for (final Iterator itr = parentBndrySet.iterator(); itr.hasNext();) {
			final Boundary bndry = (Boundary) itr.next();
			final Set children = bndry.getChildren();
			function.append("if(document.forms[0].adminBndry" + parentBndryID + ".options[document.forms[0].adminBndry" + parentBndryID + ".selectedIndex].text == \""
					+ StringUtils.encodeString(bndry.getName().trim()) + "\")\n");
			function.append("{\n");
			function.append("\tdocument.forms[0].adminBndry" + childBndryID + ".options[0] = new Option(\"" + "Choose" + "\",0,true,true);\n");
			int j = 0;
			for (final Iterator itr2 = children.iterator(); itr2.hasNext();) {
				j++;
				final Boundary bndryChild = (Boundary) itr2.next();
				function.append("\tdocument.forms[0].adminBndry" + childBndryID + ".options[" + j + "] = new Option(\"" + bndryChild.getName() + "\"," + bndryChild.getId() + ",false,false);\n");
				// function.append("\tdocument.forms[0].adminBndry3.options[0] = new Option("Choose",0,true,true);\n");

			}
			function.append("}\n");

		}

		this.appendFunctionCalls(function, childBndryTypes);

		function.append("}");

		return function.toString();

	}

	public Map getExtractMap(final List al, final Map typeMap) {
		final Set set = typeMap.keySet();
		final HashMap hm = new HashMap(typeMap);
		System.out.println("set............." + set);
		for (final Iterator itr = set.iterator(); itr.hasNext();) {
			System.out.println("1111111111");
			final Integer i = (Integer) itr.next();
			System.out.println("22222222222");
			final String s = (String) typeMap.get(i);
			System.out.println("3333333333");
			for (final Iterator itr1 = al.iterator(); itr1.hasNext();) {
				System.out.println("44444444");
				final String s1 = (String) itr1.next();
				System.out.println("s1............." + s1 + s);
				if (s.equals(s1)) {
					hm.remove(i);

				}
				System.out.println("555555555555");
			}
			System.out.println("666666666666");

		}
		return hm;
	}

	@Override
	public int doStartTag() throws javax.servlet.jsp.JspTagException {
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() {
		/*
		 * try { // *********************************************************************************************** // String bndry = (String)session.getAttribute("com.egov.cityID"); final Integer
		 * bndryid = new Integer("99"); final HeirarchyType htype = hmang.getHeirarchyTypeByID(1); final String bndryName = bmang.getBoundaryNameForID(bndryid); final Boundary topLevelBoundary =
		 * (Boundary) bmang.getTopBoundary(bndryName, htype); System.out.println("topLevelBoundary>>>>>>>>>>>>>>>>>>>>>>>>" + topLevelBoundary.getName()); final StringBuffer setBndryTypeStr = new
		 * StringBuffer(1000); final int j = 1; String bndrySelect1 = ""; String bndrySelect2 = ""; final EgovInfrastrUtilInteface inteface = new EgovInfrastrUtil(); final ArrayList al = (ArrayList)
		 * ExcludeBndryType.getExcludeType(); final Map typeMap = inteface.getMapOfBoundryTypes(); System.out.println("typeMap>>>>>>>>>>>>>>>>>>>>>>>>" + typeMap.keySet()); final Map typeMapm1 =
		 * this.getExtractMap(al, typeMap); System.out.println("typeMap>>>>>>>>>>>>>>>>>>>>>>>>" + typeMapm1); final Set typeset = new TreeSet(typeMapm1.keySet()); Iterator itr = typeset.iterator();
		 * if (itr.hasNext()) { final Integer parentType = (Integer) itr.next(); final String typeName = (String) typeMapm1.get(parentType); System.out.println("typeName>>>>>>>>>>>>>>>>>>>>>>>>" +
		 * typeName); final BoundaryType btype = (BoundaryType) btmang.getBoundaryType(typeName, htype); final List bondryList = (List) bmang.getAllBoundaries(btype,
		 * topLevelBoundary.getId().intValue()); final Set bondrySet = new HashSet(); bondrySet.addAll(bondryList); System.out.println("bondrySet>>>>>>>>>>>>>>>>>>>>>>>>" + bondrySet);
		 * setBndryTypeStr.append("function setBndryTypenValue" + j + "(name,value)\n{\n document.forms[0].bndryType.value=\"" + typeName + "\"\n}"); if (bondrySet != null && !bondrySet.isEmpty()) {
		 * final List bndryidlist = inteface.getBoundaryList(bondrySet); // request.setAttribute("bndryidlist",bndryidlist); final Map bndrymap = inteface.getBoundaryMap(bondrySet);
		 * System.out.println("bndryidlist>>>>>>>>>>>>>>>>>>>>>>>>" + bndryidlist); final String admin = "adminBndry" + j; final String onchang = "javascript:" + typeName + "Match()";
		 * logger.info("admin  ::" + admin); bndrySelect1 = "<TR><TD width=\"40%\" align=\"left\" class=\"eGovTblContent\" >" + typeName + "<font class=\"ErrorText\">*</font></TD>" +
		 * "<TD width=\"40%\" class=\"eGovTblContent\"><select name=\"" + admin + "\" size=\"2\" class=\"controlText\" onchange = \"" + onchang + ";setBndryTypenValue" + j + "(" + admin +
		 * ",document.forms[0]." + admin + ".options[document.forms[0]." + admin + ".selectedIndex].value" + ");\" multiple>" + "<option value=\"\">Choose"; String name = ""; for (final Iterator iter
		 * = bndryidlist.iterator(); iter.hasNext();) { final Integer bndryId = (Integer) iter.next(); logger.info("bndryId  ::" + bndryId); name = (String) bndrymap.get(bndryId); bndrySelect1 +=
		 * "<option value=\"" + bndryId.toString() + "\">" + name; } bndrySelect1 += "</select></TD></TR>"; logger.info("bndrySelect1  ::" + bndrySelect1); <TD width="200"
		 * align="left"><b>&nbsp;</b><FONT id="LocalLang_size10pt"> <bean:message key="zone"/> / </font> <font id="normal_font">Complaint <%=typeName%><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </b><br></font>
		 * <html:select property= "<%=admin%>" onchange = "<%=onchang%>"> <html:option value = "0"> Choose </html:option> <logic:iterate id = "id" name = "bndryidlist"> String name =
		 * (String)bndrymap.get((Integer)id); <option value = "<%= id.toString() %>"><%= name %></option> </logic:iterate> </html:select>&nbsp; </TD> } } int x = 2; while (itr.hasNext()) { final
		 * Integer parentType = (Integer) itr.next(); final String typeName = (String) typeMapm1.get(parentType); System.out.println("typeName>>>>>>>>>>>>>>>>>>>>>>>>" + typeName); final BoundaryType
		 * btype = (BoundaryType) btmang.getBoundaryType(typeName, htype); final List bondryList = (List) bmang.getAllBoundaries(btype, topLevelBoundary.getId().intValue()); final Set bondrySet = new
		 * HashSet(); bondrySet.addAll(bondryList); System.out.println("bondrySet>>>>>>>>>>>>>>>>>>>>>>>>" + bondrySet); // if(bondrySet!=null && !bondrySet.isEmpty()) // { final List bndryidlist =
		 * inteface.getBoundaryList(bondrySet); // request.setAttribute("bndryidlist",bndryidlist); final Map bndrymap = inteface.getBoundaryMap(bondrySet);
		 * System.out.println("bndryidlist>>>>>>>>>>>>>>>>>>>>>>>>" + bndryidlist); String onchang = ""; final String admin = "adminBndry" + x; if (btype.getChildBoundaryTypes() != null &&
		 * !btype.getChildBoundaryTypes().isEmpty()) { onchang = "javascript:" + typeName + "Match()"; } for(Iterator itr1 = bondrySet.iterator();itr1.hasNext();) { getChildBoundaryTypes Boundary
		 * bndry = (Boundary)itr1.next(); if(!bndry.isLeaf()) { onchang= "javascript:"+typeName+"Match()"; } } setBndryTypeStr.append("function setBndryTypenValue" + x +
		 * "(name,value)\n{\n document.forms[0].bndryType.value=\"" + typeName + "\"\n}"); bndrySelect2 += "<TR><TD width=\"40%\" class=\"eGovTblContent\"  align=\"left\">" + typeName +
		 * "<font class=\"ErrorText\">*</font></TD>" + "<TD width=\"40%\" class=\"eGovTblContent\"><select name=\"" + admin + "\" size=\"2\" class=\"controlText\"  onchange = \"" + onchang +
		 * ";setBndryTypenValue" + x + "(" + admin + ",document.forms[0]." + admin + ".options[document.forms[0]." + admin + ".selectedIndex].value" + ");\" multiple>" + "<option value=\"\">Choose" +
		 * "</select></TD></TR>"; logger.info("bndrySelect2  ::" + bndrySelect2); <TD width="200" align="left"><b>&nbsp;</b><FONT id="LocalLang_size10pt"> <bean:message key="zone"/> / </font> <font
		 * id="normal_font">Complaint <%=typeName%><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </b><br></font> <html:select property= "<%=admin%>" onchange = "<%=onchang%>"> <html:option value = "0"> Choose
		 * </html:option> </html:select>&nbsp; </TD> // } x++; } // go over again to load all the javascript functions dynamically. // typeset is a list of boundary type primary keys. It omits all the
		 * boundary // types which are excluded. itr = typeset.iterator(); final StringBuffer functionString = new StringBuffer(5000); int k = 1; while (itr.hasNext()) { final Integer parentType =
		 * (Integer) itr.next(); final String typeName = (String) typeMapm1.get(parentType); System.out.println("2nd loop typeName>>>>>>>>>>>>>>>>>>>>>>>>" + typeName); final BoundaryType btype =
		 * (BoundaryType) btmang.getBoundaryType(typeName, htype); // set of boundary values for the above declared boundary type. final List bndryList = (List) bmang.getAllBoundaries(btype,
		 * topLevelBoundary.getId().intValue()); final Set bndrySet = new HashSet(); bndrySet.addAll(bndryList); System.out.println("bndrySet>>>>>>>>>>>>>>>>>>>>>>>>" + bndrySet); final Set
		 * chBndryTypes = btype.getChildBoundaryTypes(); for (final Iterator itr1 = chBndryTypes.iterator(); itr1.hasNext();) { final BoundaryType chBndryType = (BoundaryType) itr1.next(); if
		 * (bndrySet != null && !bndrySet.isEmpty()) { functionString.append(this.generateAllJSForBoundaries(btype, chBndryType, bndrySet, k)); } } k++; } final JspWriter out =
		 * this.pageContext.getOut(); out.print(bndrySelect1); out.print(bndrySelect2); out.print(this.getScript(setBndryTypeStr)); out.print(this.getScript(functionString)); } catch (final Exception
		 * ioe) { System.out.println("Error in HeadingTag: " + ioe); try { throw new JspTagException("Exception occured --------- ", ioe); } catch (final JspTagException e) { 
		 * catch block e.printStackTrace(); } }
		 */
		return EVAL_PAGE;
	}
}
