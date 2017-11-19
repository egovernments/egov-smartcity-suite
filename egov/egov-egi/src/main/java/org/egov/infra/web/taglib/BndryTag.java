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
		for (final Iterator itr = set.iterator(); itr.hasNext();) {
			final Integer i = (Integer) itr.next();
			final String s = (String) typeMap.get(i);
			for (final Iterator itr1 = al.iterator(); itr1.hasNext();) {
				final String s1 = (String) itr1.next();
				if (s.equals(s1)) {
					hm.remove(i);

				}
			}

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
}
