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
package org.egov.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ApiCreator {

    /**
     * @param args
     * @throws ClassNotFoundException remove existing setter which adds the quotes and put new normal setter.
     */
    public static void main(String[] args) throws ClassNotFoundException {
        args = new String[10];
        // name of the domain object
        args[1] = "com.exilant.eGov.src.domain.FinancialYear";
        // name of the table
        args[2] = "financialyear";
        // List of egov and other classes which are declared
        // no need to remove these if file does not contain these just keep adding
        final List<String> excludeList = new ArrayList<String>();
        excludeList.add("EGovernCommon");
        excludeList.add("Logger");
        excludeList.add("TaskFailedException");
        excludeList.add("SimpleDateFormat");
        excludeList.add("Statement");
        excludeList.add("GenericHibernateDaoFactory");
        // List of name which doesnt participate in insertion or updation
        // no need to remove these if file does not contain these just keep adding
        final List<String> excludeNameList = new ArrayList<String>();
        excludeNameList.add("isId");
        // if id has different name like user_id replace the below id with actual id name
        excludeNameList.add("id");
        excludeNameList.add("isId");
        excludeNameList.add("insertQuery");
        excludeNameList.add("updateQuery");
        excludeNameList.add("isField");

        final Class<?> apiClass = Class.forName(args[1]);
        final Field[] fields = apiClass.getDeclaredFields();
        final StringBuilder apiString = new StringBuilder(500);
        apiString.append("public void newUpdate(Connection con) throws TaskFailedException,SQLException{");
        apiString
        .append("EGovernCommon commommethods = new EGovernCommon();\n created = commommethods.getCurrentDate(con);\n PreparedStatement pstmt=null;");
        apiString
        .append("try {\n created = formatter.format(sdf.parse(created));\n } catch (ParseException parseExp) "
                +
                "{ \n if(LOGGER.isDebugEnabled())     LOGGER.debug(parseExp.getMessage(),parseExp);}	\n	setCreated(created); \n setLastModified(created);");
        apiString
        .append(" StringBuilder query=new StringBuilder(500); \n query.append(\"update "
                + args[2] + " set \");\n");
        for (final Field f : fields)
            if (excludeList.contains(f.getType().getSimpleName()) || excludeNameList.contains(f.getName()))
                continue;
            else {
                apiString.append(" if(" + f.getName() + "!=null)	");
                apiString.append("query.append(\"" + f.getName() + "=?,\");\n");

            }
        // These two statements remove the last comma in the query
        apiString.append("int lastIndexOfComma = query.lastIndexOf(\",\");\n");
        apiString.append("query.deleteCharAt(lastIndexOfComma);\n");
        // if id has different name like user_id replace the below id with actual id name
        apiString.append("query.append(\" where id=?\");\n try{");
        apiString
        .append(" int i=1;\n pstmt=con.prepareStatement(query.toString());\n");
        for (final Field f : fields)
            if (excludeList.contains(f.getType().getSimpleName()) || excludeNameList.contains(f.getName()))
                continue;
            else {
                apiString.append(" if(" + f.getName() + "!=null)	");
                apiString.append("pstmt.set" + f.getType().getSimpleName()
                        + "(i++," + f.getName() + ");\n");

            }
        // if id has different name like user_id replace the below id with actual id name
        apiString.append("pstmt.setString(i++,id);\n");
        apiString.append("\n pstmt.executeQuery();\n");
        apiString.append("}catch(Exception e){	LOGGER.error(\"Exp in update: \"+e.getMessage()); throw taskExc;} finally{");
        apiString.append("try{ pstmt.close(); }catch(Exception e){LOGGER.error(\"Inside finally block of update\");}} \n }");

    }

}
