/**
 * Created on July 25, 2006
 * @author prabhu
 *
 * Please go through the corresponding file which uses this file.
 * Whenever a user enters some value, the following query is executed based on the value.
 */

<%@ page language="java" import="java.sql.*,java.util.HashMap,org.egov.infstr.utils.*" %>

<%
System.out.println("process Inside emp jsp");
Connection con=null;
ResultSet rs=null;
Statement stmt=null;

try
{
con = EgovDatabaseManager.openConnection();
stmt=con.createStatement();
}catch(Exception e){
System.out.println(e.getMessage());
//System.out.println(e.printStackTrace());
}

//Based on the type we will execute the query

if(request.getParameter("type").equalsIgnoreCase("getAllEmployeeCodes")){
String query="select emp.CODE||'`-`'||emp.EMP_FIRSTNAME||' '||emp.EMP_MIDDLENAME||' '||emp.EMP_LASTNAME||'`-`'||emp.id||'`-`'||emp.PRESENT_DESIGNATION||'`-`'||department.DEPT_NAME||'`-`'||emp.DATE_OF_FIRST_APPOINTMENT as \"code\"  " +
"from eg_employee emp,eg_emp_assignment ass,eg_emp_assignment_prd assprd,eg_department department " +
"where emp.id = assprd.ID_EMPLOYEE and assprd.ID = ass.ID_EMP_ASSIGN_PRD and ass.ID_DEPT = department.ID_DEPT order by emp.code ";
System.out.println("query :"+query);
rs=stmt.executeQuery(query);
}
if(request.getParameter("type").equalsIgnoreCase("getAllGlcodesFromAccount")){
System.out.println("GLCODEEEEEEEEE");
String query="select glcode||'`-`'||name||'`-`'||ID as \"code\" from chartofaccounts where classification=4  and type in('E','A','L') order by glcode ";
System.out.println("query :"+query);
rs=stmt.executeQuery(query);
}




StringBuffer accCode=new StringBuffer();
int i = 0;
try{
while(rs.next()){

if(i > 0)
{
accCode.append("+");
accCode.append(rs.getString("code"));
}
else
{
accCode.append(rs.getString("code"));

}
i++;

}
accCode.append("^");
String codeValues=accCode.toString();

response.setContentType("text/xml");
response.setHeader("Cache-Control", "no-cache");
response.getWriter().write(codeValues);
}
catch(Exception e){
System.out.println(e.getMessage());
}
finally{
rs.close();
EgovDatabaseManager.releaseConnection(con,stmt);
}
%>
<html>
<body>
</body>
</html>

