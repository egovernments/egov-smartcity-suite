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
package org.egov.infstr.client.taglib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.jsp.tagext.BodyTagSupport;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;

/**
 * @author deepak TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class BndryTagForGreView extends BodyTagSupport {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private Object name = null;
        private static HashMap bndryCombo = new HashMap();

        public Object getName() {
                return this.name;
        }

        public void setName(final Object name) {
                this.name = name;
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

        public int getParentForBlock(final Boundary bnd, final BoundaryType btypeforgivenLevel) {
                int bndruyInt = 0;
                if (bnd.getBoundaryType().equals(btypeforgivenLevel)) {
                        bndruyInt = bnd.getId().intValue();
                        return bndruyInt;
                } else {
                        final Boundary bnd1 = bnd.getParent();
                        return this.getParentForBlock(bnd1, btypeforgivenLevel);
                }

        }

        @Override
        public int doStartTag() throws javax.servlet.jsp.JspTagException {
                return SKIP_BODY;
        }

        @Override
        public int doEndTag() {
                /*
                 * try { // *********************************************************************************************** final javax.servlet.http.HttpSession gSession = this.pageContext.getSession(); final
                 * String toplevelID = (String) gSession.getAttribute("org.egov.topBndryID"); // String bndry = (String)session.getAttribute("com.egov.cityID"); final Integer bndryid = new
                 * Integer(toplevelID); if (!bndryCombo.containsKey(bndryid)) { this.createCombo(bndryid); } final ConstructString conString = new ConstructString(); final StringBuffer finalString = new
                 * StringBuffer(conString.getFnString(toplevelID)); final JspWriter out = this.pageContext.getOut(); out.print(bndryCombo.get(bndryid)); // out.print(bndrySelect1); // out.print(bndrySelect2);
                 * out.print(this.getScript(finalString)); } catch (final Exception ioe) { System.out.println("Error in HeadingTag: " + ioe); try { throw new JspTagException("Exception occured --------- ",
                 * ioe); } catch (final JspTagException e) { 
                 */

                return EVAL_PAGE;
        }
}
